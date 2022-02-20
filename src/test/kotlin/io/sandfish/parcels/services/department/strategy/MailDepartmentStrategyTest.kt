package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.services.department.DepartmentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MailDepartmentStrategyTest {

    @Test
    fun isApplicableWhenWeightWeightUnderOrEqualTo1Kg() {
        val receipient = ContainerParcelTestHelper.defaultReceipient()
        val mailDepartmentStrategy = MailDepartmentStrategy()

        assertThat(mailDepartmentStrategy.isApplicable(ContainerParcel(0.0, 0.0, receipient))).isEqualTo(true)
        assertThat(mailDepartmentStrategy.isApplicable(ContainerParcel(0.0, 1.0, receipient))).isEqualTo(true)
        assertThat(mailDepartmentStrategy.isApplicable(ContainerParcel(0.0, 1.001, receipient))).isEqualTo(false)
        assertThat(mailDepartmentStrategy.isApplicable(ContainerParcel(0.0, 10.0, receipient))).isEqualTo(false)

    }

    @Test
    fun heavyDepartmentStrategyHasDepartmentTypeHeavy() {
        assertThat(MailDepartmentStrategy().getType()).isEqualTo(DepartmentType.Mail)
    }

}
