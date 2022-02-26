package io.sandfish.parcels.services.container

import io.sandfish.parcels.controllers.exceptions.NoMatchingDepartmentFoundException
import io.sandfish.parcels.domain.Container
import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.dtos.ContainerXMLPayload
import io.sandfish.parcels.repositories.ContainerRepository
import io.sandfish.parcels.services.department.DepartmentService
import io.sandfish.parcels.services.department.strategy.DepartmentStrategyInput
import io.sandfish.parcels.services.department.strategy.execute
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

/**
 * Class for processing the XML payload when inserting new containers
 */
@Service
class ContainerPayloadProcessor(
    private val containerRepository: ContainerRepository,
    private val departmentService: DepartmentService,
) {

    /**
     * Convert XML payload to container domain object and save it
     * Each parcel is processed according to current departments
     */
    fun processContainer(payload: ContainerXMLPayload): Container {
        val sortedDepartments = this.departmentService.getDepartments()
            .sortedBy { it.priority }

        val container = Container(
            containerId = payload.id,
            shippingDate = payload.shippingDate,
            arrivalDate = ZonedDateTime.now(),
            parcels = payload.parcels.map {
                val matchingStrategies = findDepartmentForParcel(it, sortedDepartments)

                Parcel(
                    value = it.value,
                    weight = it.weight,
                    metadata = it.receipient.toDomain(),
                    department = matchingStrategies.name,
                    state = ParcelState.InProcessing
                )
            }
        )

        return containerRepository.save(container)
    }

    /**
     * Find an entry department for a new parcel.
     * The first complete rule match (sorted by the department priority - lower is earlier) is the department assigned to the parcel
     *
     * @throws NoMatchingDepartmentFoundException if the parcel cannot be matched to any department
     */
    private fun findDepartmentForParcel(parcel: ContainerParcel, sortedDepartments: List<Department>): Department {
        val input = DepartmentStrategyInput(
            weight = parcel.weight,
            value = parcel.value
        )

        return sortedDepartments.find { department -> department.rules.all { it.execute(input) } }
            ?: throw NoMatchingDepartmentFoundException("a parcel does not match ANY department ruleset.")
    }
}
