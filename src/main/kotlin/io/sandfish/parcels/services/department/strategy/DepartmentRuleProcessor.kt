package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.controllers.exceptions.NoSignMatchException
import io.sandfish.parcels.domain.Rule
import kotlin.reflect.KProperty1

/**
 * executes a rule on the provided input, returns true if the rule accepts it.
 */
infix fun Rule.execute(input: DepartmentStrategyInput): Boolean {
    return sign(this.sign)(memberValueFromKey(input), this.value)
}

/**
 * Get the requested class variable from the input
 */
private fun Rule.memberValueFromKey(input: DepartmentStrategyInput): Double {
    val keyValue = input::class.members.toList().first { it.name == this.key } as KProperty1<Any, *>
    return keyValue.get(input) as Double
}

/**
 * Returns a function that does a comparison between two doubles based on the provided comparison operator
 * @throws NoSignMatchException if no operator can be matched
 */
private fun sign(value: String): (Double, Double) -> Boolean {
    return when (value) {
        ">" -> { a, b -> a > b }
        ">=" -> { a, b -> a >= b }
        "<" -> { a, b -> a < b }
        "<=" -> { a, b -> a <= b }
        "=" -> { a, b -> a == b }
        else -> throw NoSignMatchException("Sign $value cannot be matched with any supported operator.")
    }
}

