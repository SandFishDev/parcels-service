package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.services.department.DepartmentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RegularDepartmentStrategyTest {

    @Test
    fun isApplicableWhenAboveWeightAbove1KgUnder10Kg() {
        val receipient = ContainerParcelTestHelper.defaultReceipient()
        val regularDepartmentStrategy = RegularDepartmentStrategy()

        assertThat(regularDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 0.0))).isEqualTo(false)
        assertThat(regularDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 1.0))).isEqualTo(false)
        assertThat(regularDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 1.00001))).isEqualTo(true)
        assertThat(regularDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 10.0))).isEqualTo(true)
        assertThat(regularDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 10.001))).isEqualTo(false)
        assertThat(regularDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 1000.0))).isEqualTo(false)

    }

    @Test
    fun heavyDepartmentStrategyHasDepartmentTypeHeavy() {
        assertThat(RegularDepartmentStrategy().getType()).isEqualTo(DepartmentType.Regular)
    }

}
