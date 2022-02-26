package io.sandfish.parcels.services.container

import io.sandfish.parcels.controllers.exceptions.EntityNotFoundException
import io.sandfish.parcels.domain.Container
import io.sandfish.parcels.dtos.ContainerStatisticsDto
import io.sandfish.parcels.repositories.ContainerRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ContainerService(private val containerRepository: ContainerRepository) {

    fun getContainerById(id: Long): Container {
        return containerRepository.findById(id).orElseThrow { EntityNotFoundException("Container with id '$id' could not be found") }
    }

    fun getContainers(): List<Container> {
        return containerRepository.findAll().toList()
    }

    fun getStatistics(): ContainerStatisticsDto {
        return ContainerStatisticsDto(
            containerCount = containerRepository.count(),
            containerCountLast24Hours = containerRepository.countByArrivalDateAfter(
                ZonedDateTime.now().minusDays(1)
            )
        )
    }
}
