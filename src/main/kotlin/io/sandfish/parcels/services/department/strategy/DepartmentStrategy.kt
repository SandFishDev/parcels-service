package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.services.department.DepartmentType

sealed interface DepartmentStrategy {
    fun isApplicable(parcel: ContainerParcel): Boolean

    fun getType(): DepartmentType

}
