package io.sandfish.parcels.services

import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.dtos.ParcelStatisticsDto
import io.sandfish.parcels.repositories.ParcelRepository
import io.sandfish.parcels.services.department.DepartmentService
import io.sandfish.parcels.services.department.DepartmentType
import org.springframework.stereotype.Service

@Service
class ParcelService(
    private val parcelRepository: ParcelRepository,
    private val departmentService: DepartmentService
) {

    fun findParcelByDepartment(departmentType: DepartmentType): List<Parcel> {
        return parcelRepository.findParcelByDepartmentAndState(departmentType, ParcelState.InProcessing)
    }

    fun findParcelById(id: Long): Parcel {
        return parcelRepository.findById(id).orElseThrow { RuntimeException() }
    }

    fun processParcel(id: Long): Parcel {
        val parcel: Parcel = parcelRepository.findById(id).orElseThrow { RuntimeException() }

        parcel.state = ParcelState.Processed

        return parcelRepository.save(parcel)
    }

    fun getStatistics(): ParcelStatisticsDto {
        val departmentParcelCounts = departmentService.getDepartments()
            .associate { it.name to parcelRepository.countByDepartmentAndState(DepartmentType.valueOf(it.name), ParcelState.InProcessing) }

        return ParcelStatisticsDto(
            parcelCountbyDepartment = departmentParcelCounts
        )
    }
}
