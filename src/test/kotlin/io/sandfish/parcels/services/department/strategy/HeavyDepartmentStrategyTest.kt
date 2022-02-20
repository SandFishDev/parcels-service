package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.services.department.DepartmentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HeavyDepartmentStrategyTest {

    @Test
    fun isApplicableWhenWeightAbove10KG() {
        val receipient = ContainerParcelTestHelper.defaultReceipient()
        val heavyDepartmentStrategy = HeavyDepartmentStrategy()

        assertThat(heavyDepartmentStrategy.isApplicable(ContainerParcel(0.0, 0.0, receipient))).isEqualTo(false)
        assertThat(heavyDepartmentStrategy.isApplicable(ContainerParcel(0.0, 10.0, receipient))).isEqualTo(false)
        assertThat(heavyDepartmentStrategy.isApplicable(ContainerParcel(0.0, 10.001, receipient))).isEqualTo(true)
        assertThat(heavyDepartmentStrategy.isApplicable(ContainerParcel(0.0, 1000.0, receipient))).isEqualTo(true)

    }

    @Test
    fun heavyDepartmentStrategyHasDepartmentTypeHeavy() {
        assertThat(HeavyDepartmentStrategy().getType()).isEqualTo(DepartmentType.Heavy)
    }

}
