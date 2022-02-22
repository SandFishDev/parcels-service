package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.services.department.DepartmentType
import org.springframework.stereotype.Component

@Component
class MailDepartmentStrategy : DepartmentStrategy {
    override val getPriority: Long get() = 2
    override fun isApplicable(input: DepartmentStrategyInput): Boolean = input.weight <= 1.00
    override fun getType(): DepartmentType = DepartmentType.Mail
}
