package io.sandfish.parcels.dtos

import io.sandfish.parcels.domain.Rule

data class RuleDto (
    val key: String,
    val sign: String,
    val value: Double
){
    fun toDomain() : Rule {
        return Rule(
            key = key,
            sign = sign,
            value = value
        )
    }
}
