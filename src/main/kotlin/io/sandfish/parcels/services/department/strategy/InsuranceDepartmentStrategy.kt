package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.services.department.DepartmentType
import org.springframework.stereotype.Component

@Component
class InsuranceDepartmentStrategy : DepartmentStrategy {
    override val getPriority: Long get() = 1
    override fun isApplicable(input: DepartmentStrategyInput): Boolean = input.value > 1000
    override fun getType(): DepartmentType = DepartmentType.Insurance
}
