package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.Container
import org.springframework.data.repository.CrudRepository
import java.time.ZonedDateTime

interface ContainerRepository : CrudRepository<Container, Long> {
    fun countByArrivalDateAfter(datetime: ZonedDateTime): Long
}
