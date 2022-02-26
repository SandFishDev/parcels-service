package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.Parcel
import io.sandfish.parcels.domain.ParcelState
import org.springframework.data.repository.CrudRepository

interface ParcelRepository : CrudRepository<Parcel, Long>{
    fun findParcelByDepartmentAndState(department: String, state: ParcelState) :  List<Parcel>

    fun countByDepartmentAndState(department: String, state: ParcelState): Long
}
