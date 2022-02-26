package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.domain.Rule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DepartmentRuleProcessorTest {

    @Test
    fun testRuleIsTrue(){
        val departmentStrategyInput = DepartmentStrategyInput(weight = 10.0, value = 100.0)
        val rule = Rule("weight", ">", 9.0)

        val result = rule.execute(departmentStrategyInput)
        assertThat(result).isTrue
    }

    @Test
    fun testRuleIsFalse(){
        val departmentStrategyInput = DepartmentStrategyInput(weight = 10.0, value = 100.0)
        val rule = Rule("weight", ">", 120.0)

        val result = rule.execute(departmentStrategyInput)

        assertThat(result).isFalse
    }
}
