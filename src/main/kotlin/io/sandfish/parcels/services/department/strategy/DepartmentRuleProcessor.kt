package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.domain.Rule
import kotlin.reflect.KProperty1


infix fun Rule.execute(input: DepartmentStrategyInput): Boolean {
    return fun(input: DepartmentStrategyInput): Boolean {
        val actual = memberValueFromKey(input)

        println("$actual ${this.sign} ${this.value}")
        return sign(this.sign)(actual, this.value)
    }(input)
}

private fun Rule.memberValueFromKey(input: DepartmentStrategyInput): Double {
    val keyValue = input::class.members.toList().first { it.name == this.key } as KProperty1<Any, *>
    return keyValue.get(input) as Double
}

private fun sign(value: String): (Double, Double) -> Boolean {
    return when (value) {
        ">" -> { a, b -> a > b }
        ">=" -> { a, b -> a >= b }
        "<" -> { a, b -> a < b }
        "<=" -> { a, b -> a <= b }
        "=" -> { a, b -> a == b }
        else -> throw RuntimeException()
    }
}

