package io.sandfish.parcels.services

import io.sandfish.parcels.domain.Container
import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.dtos.ContainerStatisticsDto
import io.sandfish.parcels.dtos.ContainerXMLPayload
import io.sandfish.parcels.repositories.ContainerRepository
import io.sandfish.parcels.services.department.DepartmentService
import io.sandfish.parcels.services.department.strategy.DepartmentStrategyInput
import io.sandfish.parcels.services.department.strategy.execute
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ContainerService(
    private val containerRepository: ContainerRepository,
    private val departmentService: DepartmentService
) {

    fun processContainer(payload: ContainerXMLPayload) {
        val container = Container(
            containerId = payload.id,
            shippingDate = payload.shippingDate,
            arrivalDate = ZonedDateTime.now(),
            parcels = payload.parcels.map {
                val matchingStrategies = findDepartmentForParcel(it)

                Parcel(
                    value = it.value,
                    weight = it.weight,
                    metadata = it.receipient.toDomain(),
                    department = matchingStrategies.name,
                    state = ParcelState.InProcessing
                )
            }
        )

        containerRepository.save(container)
    }

    private fun findDepartmentForParcel(parcel: ContainerParcel): Department {
        val input = DepartmentStrategyInput(
            weight = parcel.weight,
            value = parcel.value
        )

        val department = this.departmentService.getDepartments()
            .sortedBy { it.priority }
            .find { department -> department.rules.all { it.execute(input) } }

        requireNotNull(department)

        return department
    }

    fun getContainerById(id: Long): Container {
        return containerRepository.findById(id).orElseThrow { RuntimeException() }
    }

    fun getContainers(): List<Container> {
        return containerRepository.findAll().toList()
    }

    /**
     * TODO this returns a DTO but should be a model
     */
    fun getStatistics(): ContainerStatisticsDto {
        return ContainerStatisticsDto(
            containerCount = containerRepository.count(),
            containerCountLast24Hours = containerRepository.countByArrivalDateAfter(
                ZonedDateTime.now().minusDays(1)
            )
        )
    }
}
