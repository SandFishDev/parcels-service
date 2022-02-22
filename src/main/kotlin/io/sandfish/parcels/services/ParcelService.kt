package io.sandfish.parcels.services

import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.dtos.ParcelStatisticsDto
import io.sandfish.parcels.repositories.ParcelRepository
import io.sandfish.parcels.services.department.DepartmentService
import io.sandfish.parcels.services.department.DepartmentType
import io.sandfish.parcels.services.department.strategy.DepartmentStrategy
import io.sandfish.parcels.services.department.strategy.DepartmentStrategyInput
import org.springframework.stereotype.Service

@Service
class ParcelService(
    private val parcelRepository: ParcelRepository,
    private val departmentService: DepartmentService,
    private val departmentStrategies: List<DepartmentStrategy>
) {

    fun findParcelByDepartment(departmentType: DepartmentType): List<Parcel> {
        return parcelRepository.findParcelByDepartmentAndState(departmentType, ParcelState.InProcessing)
    }

    fun findParcelById(id: Long): Parcel {
        return parcelRepository.findById(id).orElseThrow { RuntimeException() }
    }

    fun processParcel(id: Long): Parcel {
        val parcel: Parcel = parcelRepository.findById(id).orElseThrow { RuntimeException() }

        val currentDepartment = departmentService.getDepartmentByName(parcel.department.name)

        if (currentDepartment.successors.size == 0) {
            parcel.state = ParcelState.Processed
        } else {
            val successorDepartments = departmentStrategies.filter {
                currentDepartment.successors.map { it.name }.contains(it.getType().name)
            }
                .sortedBy { it.getPriority }

            val nextDepartment = successorDepartments
                .find { departmentStrategy ->
                    departmentStrategy.isApplicable(
                        DepartmentStrategyInput(
                            parcel.weight,
                            parcel.value
                        )
                    )
                }!!
                .getType()

            parcel.department = nextDepartment
        }

        return parcelRepository.save(parcel)
    }

    fun getStatistics(): ParcelStatisticsDto {
        val departmentParcelCounts = departmentService.getDepartments()
            .associate {
                it.name to parcelRepository.countByDepartmentAndState(
                    DepartmentType.valueOf(it.name),
                    ParcelState.InProcessing
                )
            }

        return ParcelStatisticsDto(
            parcelCountbyDepartment = departmentParcelCounts
        )
    }
}
