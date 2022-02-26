package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.domain.Rule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DepartmentRuleProcessorTest {

    @ParameterizedTest
    @MethodSource("combinations")
    fun testRules(weight: Double, value: Double, key: String, sign: String, ruleValue: Double, expectedResult: Boolean){
        val departmentStrategyInput = DepartmentStrategyInput(weight = weight, value = value)
        val rule = Rule(key, sign, ruleValue)

        val result = rule.execute(departmentStrategyInput)
        assertThat(result).isEqualTo(expectedResult)
    }

    private fun combinations(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of(10.0, 0.0, "weight", ">", 9.999, true),
            Arguments.of(10.0, 0.0, "weight", ">=", 9.999, true),
            Arguments.of(10.0, 0.0, "weight", "<", 9.999, false),
            Arguments.of(10.0, 0.0, "weight", "<=", 9.999, false),
            Arguments.of(10.0, 0.0, "weight", "=", 9.999, false),
            Arguments.of(0.0, 10.0, "value", ">", 9.999, true),
            Arguments.of(0.0, 10.0, "value", ">=", 9.999, true),
            Arguments.of(0.0, 10.0, "value", "<", 9.999, false),
            Arguments.of(0.0, 10.0, "value", "<=", 9.999, false),
            Arguments.of(0.0, 10.0, "value", "=", 9.999, false),

            Arguments.of(10.0, 0.0, "weight", ">", 10.001, false),
            Arguments.of(10.0, 0.0, "weight", ">=", 10.001, false),
            Arguments.of(10.0, 0.0, "weight", "<", 10.001, true),
            Arguments.of(10.0, 0.0, "weight", "<=", 10.001, true),
            Arguments.of(0.0, 10.0, "value", ">", 10.001, false),
            Arguments.of(0.0, 10.0, "value", ">=", 10.001, false),
            Arguments.of(0.0, 10.0, "value", "<", 10.001, true),
            Arguments.of(0.0, 10.0, "value", "<=", 10.001, true),

            Arguments.of(0.0, 10.0, "value", "<=", 10.000, true),
            Arguments.of(10.0, 0.0, "weight", "<=", 10.000, true),

            Arguments.of(0.0, 10.0, "value", ">=", 10.000, true),
            Arguments.of(0.0, 10.0, "value", ">=", 10.000, true),

            Arguments.of(0.0, 10.0, "value", "=", 10, true),
            Arguments.of(10.0, 0.0, "weight", "=", 10, true),
        )
    }

}
