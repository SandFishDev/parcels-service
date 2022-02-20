package io.sandfish.parcels.dtos

data class DepartmentDto(
    var id: Long?,
    var name: String
){
    constructor(name: String) : this(null, name)
}
