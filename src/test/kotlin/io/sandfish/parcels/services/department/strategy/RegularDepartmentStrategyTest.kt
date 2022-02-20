package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.services.department.DepartmentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RegularDepartmentStrategyTest {

    @Test
    fun isApplicableWhenAboveWeightAbove1KgUnder10Kg() {
        val receipient = ContainerParcelTestHelper.defaultReceipient()
        val regularDepartmentStrategy = RegularDepartmentStrategy()

        assertThat(regularDepartmentStrategy.isApplicable(ContainerParcel(0.0, 0.0, receipient))).isEqualTo(false)
        assertThat(regularDepartmentStrategy.isApplicable(ContainerParcel(0.0, 1.0, receipient))).isEqualTo(false)
        assertThat(regularDepartmentStrategy.isApplicable(ContainerParcel(0.0, 1.00001, receipient))).isEqualTo(true)
        assertThat(regularDepartmentStrategy.isApplicable(ContainerParcel(0.0, 10.0, receipient))).isEqualTo(true)
        assertThat(regularDepartmentStrategy.isApplicable(ContainerParcel(0.0, 10.001, receipient))).isEqualTo(false)
        assertThat(regularDepartmentStrategy.isApplicable(ContainerParcel(0.0, 1000.0, receipient))).isEqualTo(false)

    }

    @Test
    fun heavyDepartmentStrategyHasDepartmentTypeHeavy() {
        assertThat(RegularDepartmentStrategy().getType()).isEqualTo(DepartmentType.Regular)
    }

}
