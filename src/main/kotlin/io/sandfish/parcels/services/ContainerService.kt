package io.sandfish.parcels.services

import io.sandfish.parcels.domain.Container
import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.dtos.ContainerStatisticsDto
import io.sandfish.parcels.dtos.ContainerXMLPayload
import io.sandfish.parcels.repositories.ContainerRepository
import io.sandfish.parcels.services.department.strategy.DepartmentStrategy
import io.sandfish.parcels.services.department.strategy.DepartmentStrategyInput
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ContainerService(
    private val containerRepository: ContainerRepository,
    private val departmentStrategies: List<DepartmentStrategy>
) {

    fun processContainer(payload: ContainerXMLPayload) {
        val container = Container(
            containerId = payload.id,
            shippingDate = payload.shippingDate,
            arrivalDate = ZonedDateTime.now(),
            parcels = payload.parcels.map {
                val matchingStrategies = findStrategyForParcel(it)

                Parcel(
                    value = it.value,
                    weight = it.weight,
                    metadata = it.receipient.toDomain(),
                    department = matchingStrategies.getType(),
                    state = ParcelState.InProcessing
                )
            }
        )

        containerRepository.save(container)
    }

    private fun findStrategyForParcel(parcel: ContainerParcel): DepartmentStrategy {
        val department = departmentStrategies.sortedBy { it.getPriority }
            .find { departmentStrategy ->
                departmentStrategy.isApplicable(
                    DepartmentStrategyInput(
                        weight = parcel.weight,
                        value = parcel.value
                    )
                )
            }

        requireNotNull(department)

        return department
    }

    fun getContainerById(id: Long): Container {
        return containerRepository.findById(id).orElseThrow { RuntimeException() }
    }

    fun getContainers(): List<Container> {
        return containerRepository.findAll().toList()
    }

    fun getStatistics(): ContainerStatisticsDto {
        return ContainerStatisticsDto(
            containerCount = containerRepository.count(),
            containerCountLast24Hours = containerRepository.countByArrivalDateAfter(
                ZonedDateTime.now().minusDays(1)
            )
        )
    }
}
