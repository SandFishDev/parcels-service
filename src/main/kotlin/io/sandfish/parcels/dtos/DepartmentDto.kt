package io.sandfish.parcels.dtos

data class DepartmentDto(
    var id: Long?,
    var name: String,
    var priority: Long,
    var successors: Set<Long>,
    var rules: Set<RuleDto>
)
