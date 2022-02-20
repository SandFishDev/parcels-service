package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.ContainerDto
import io.sandfish.parcels.dtos.ContainerStatisticsDto
import io.sandfish.parcels.dtos.ContainerXMLPayload
import io.sandfish.parcels.services.ContainerService
import org.springframework.web.bind.annotation.*

/**
 * RestController for endpoint related to containers
 */
@RestController
@RequestMapping("/api/containers")
class ContainerController(
    private val containerService: ContainerService
) {

    /**
     * Endpoint for process XML container input into the system
     */
    @PostMapping(consumes = ["application/xml"])
    fun insertParcels(@RequestBody body: ContainerXMLPayload) {
        containerService.processContainer(body)
    }

    /**
     * Get a container by id
     */
    @GetMapping("/{id}")
    fun getContainerById(@PathVariable id: Long): ContainerDto {
        return containerService.getContainerById(id).toTransferObject()
    }

    /**
     * get all containers
     * TODO this would be paged and based on a timewindow if I had anough time
     */
    @GetMapping
    fun getContainers(): List<ContainerDto>{
        return containerService.getContainers().map { it.toTransferObject() }
    }

    /**
     * Get statistics to show on the dashboard screen
     */
    @GetMapping("/statistics")
    fun getDashboardStatistics(): ContainerStatisticsDto {
        return containerService.getStatistics()
    }
}
