package io.sandfish.parcels.services

import io.sandfish.parcels.controllers.exceptions.EntityNotFoundException
import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.dtos.ParcelStatisticsDto
import io.sandfish.parcels.repositories.ParcelRepository
import io.sandfish.parcels.services.department.DepartmentService
import io.sandfish.parcels.services.department.strategy.DepartmentStrategyInput
import io.sandfish.parcels.services.department.strategy.execute
import org.springframework.stereotype.Service

@Service
class ParcelService(
    private val parcelRepository: ParcelRepository,
    private val departmentService: DepartmentService,
) {

    fun findParcelByDepartmentName(departmentName: String): List<Parcel> {
        return parcelRepository.findParcelByDepartmentAndState(departmentName, ParcelState.InProcessing)
    }

    fun findParcelById(id: Long): Parcel {
        return parcelRepository.findById(id).orElseThrow { EntityNotFoundException("Parcel with id '$id' could not be found") }
    }

    /**
     * process a parcel by id
     * If there are no successors: The parcel is processed
     *
     * If there are successors: Update the parcels department with the first (sorted by priority) that matches its entire ruleset.
     * If none can be matched the parcel is considered processed.
     *
     */
    fun processParcel(id: Long): Parcel {
        val parcel: Parcel = parcelRepository.findById(id).orElseThrow { EntityNotFoundException("Parcel not found") }
        val input = DepartmentStrategyInput(
            parcel.weight,
            parcel.value
        )

        val currentDepartment = departmentService.getDepartmentByName(parcel.department)

        when (currentDepartment.successors.size) {
            0 -> {
                parcel.state = ParcelState.Processed
            }
            else -> {
                val nextDepartment = departmentService.getDepartments()
                    .filter { department -> currentDepartment.successors.map { it.name }.contains(department.name) }
                    .sortedBy { it.priority }
                    .find { department ->
                        department.rules.all {
                            it.execute(input)
                        }
                    }

                //If there is no department to process we'll consider the parcel processed
                if (nextDepartment == null) {
                    parcel.state = ParcelState.Processed
                }

                parcel.department = (nextDepartment?.name ?: "")
            }
        }

        return parcelRepository.save(parcel)
    }

    fun getStatistics(): ParcelStatisticsDto {
        val departmentParcelCounts = departmentService.getDepartments()
            .associate {
                it.name to parcelRepository.countByDepartmentAndState(
                    it.name,
                    ParcelState.InProcessing
                )
            }

        return ParcelStatisticsDto(
            parcelCountbyDepartment = departmentParcelCounts
        )
    }
}
