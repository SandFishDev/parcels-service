package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.services.department.DepartmentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MailDepartmentStrategyTest {

    @Test
    fun isApplicableWhenWeightWeightUnderOrEqualTo1Kg() {
        val receipient = ContainerParcelTestHelper.defaultReceipient()
        val mailDepartmentStrategy = MailDepartmentStrategy()

        assertThat(mailDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 0.0))).isEqualTo(true)
        assertThat(mailDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 1.0))).isEqualTo(true)
        assertThat(mailDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 1.001))).isEqualTo(false)
        assertThat(mailDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 10.0))).isEqualTo(false)

    }

    @Test
    fun heavyDepartmentStrategyHasDepartmentTypeHeavy() {
        assertThat(MailDepartmentStrategy().getType()).isEqualTo(DepartmentType.Mail)
    }

}
