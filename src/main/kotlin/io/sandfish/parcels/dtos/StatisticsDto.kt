package io.sandfish.parcels.dtos

data class ContainerStatisticsDto(
    var containerCount: Long,
    var containerCountLast24Hours: Long
)

data class ParcelStatisticsDto(
    var parcelCountbyDepartment: Map<String, Long>
)
