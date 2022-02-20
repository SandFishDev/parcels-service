package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.services.department.DepartmentType
import org.springframework.data.repository.CrudRepository

interface ParcelRepository : CrudRepository<Parcel, Long>{
    fun findParcelByDepartment(department: DepartmentType): List<Parcel>

    fun findParcelByDepartmentAndState(department: DepartmentType, state: ParcelState) :  List<Parcel>

    fun countByDepartmentAndState(department: DepartmentType, state: ParcelState): Long
}
