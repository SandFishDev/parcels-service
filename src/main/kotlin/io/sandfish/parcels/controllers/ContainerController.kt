package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.ContainerDto
import io.sandfish.parcels.dtos.ContainerStatisticsDto
import io.sandfish.parcels.dtos.ContainerXMLPayload
import io.sandfish.parcels.services.container.ContainerPayloadProcessor
import io.sandfish.parcels.services.container.ContainerService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * RestController for endpoint related to containers
 */
@RestController
@RequestMapping("/api/containers")
@PreAuthorize("hasRole('ADMIN')")
class ContainerController(
    private val containerService: ContainerService,
    private val containerPayloadProcessor: ContainerPayloadProcessor
) {

    /**
     * Endpoint for process XML container input into the system
     */
    @PostMapping(consumes = ["application/xml"])
    fun uploadContainer(@RequestBody body: ContainerXMLPayload): ResponseEntity<Unit> {
        containerPayloadProcessor.processContainer(body)

        return ResponseEntity.noContent().build()
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
