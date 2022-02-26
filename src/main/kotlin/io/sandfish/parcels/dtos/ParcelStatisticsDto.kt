package io.sandfish.parcels.dtos

data class ParcelStatisticsDto(
    var parcelCountbyDepartment: Map<String, Long>
)
