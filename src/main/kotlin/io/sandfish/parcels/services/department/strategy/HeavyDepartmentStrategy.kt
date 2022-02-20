package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.services.department.DepartmentType
import org.springframework.stereotype.Service

@Service
class HeavyDepartmentStrategy : DepartmentStrategy {
    override fun isApplicable(parcel: ContainerParcel): Boolean = parcel.weight > 10.00
    override fun getType(): DepartmentType = DepartmentType.Heavy
}