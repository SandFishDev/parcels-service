package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.services.department.DepartmentType

sealed interface DepartmentStrategy {
    val getPriority: Long

    fun isApplicable(input: DepartmentStrategyInput): Boolean

    fun getType(): DepartmentType

}

data class DepartmentStrategyInput(
    val weight: Double, val value: Double
)
