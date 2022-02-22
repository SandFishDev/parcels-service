package io.sandfish.parcels.dtos

data class DepartmentDto(
    var id: Long?,
    var name: String,
    var successors: Set<Long>,
) {
    constructor(name: String) : this(null, name, emptySet())

}
