package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.services.department.DepartmentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HeavyDepartmentStrategyTest {

    @Test
    fun isApplicableWhenWeightAbove10KG() {
        val receipient = ContainerParcelTestHelper.defaultReceipient()
        val heavyDepartmentStrategy = HeavyDepartmentStrategy()

        assertThat(heavyDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 0.0))).isEqualTo(false)
        assertThat(heavyDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 10.0))).isEqualTo(false)
        assertThat(heavyDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 10.001))).isEqualTo(true)
        assertThat(heavyDepartmentStrategy.isApplicable(DepartmentStrategyInput(0.0, 1000.0))).isEqualTo(true)

    }

    @Test
    fun heavyDepartmentStrategyHasDepartmentTypeHeavy() {
        assertThat(HeavyDepartmentStrategy().getType()).isEqualTo(DepartmentType.Heavy)
    }

}
