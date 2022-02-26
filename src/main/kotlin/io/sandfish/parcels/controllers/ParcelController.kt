package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.ParcelDto
import io.sandfish.parcels.dtos.ParcelStatisticsDto
import io.sandfish.parcels.services.ParcelService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/parcels")
@PreAuthorize("hasRole('USER')")
class ParcelController(private val parcelService: ParcelService) {

    @GetMapping
    fun getAllParcels(@RequestParam departmentType: String): ResponseEntity<List<ParcelDto>> {
        if (verifyHasRole(departmentType)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() //TODO throw exception instead
        }

        val parcels = parcelService.findParcelByDepartmentName(departmentType)
        return ResponseEntity.ok(parcels.map { it.toTransferObject() })
    }

    @PostMapping("/{id}/command/process")
    fun processParcel(@PathVariable id: Long, @RequestBody body: Unit): ParcelDto {
        verifyHasRole(parcelService.findParcelById(id).department)

        return parcelService.processParcel(id).toTransferObject()
    }

    /**
     * Get statistics to show on the dashboard screen
     */
    @GetMapping("/statistics")
    fun getDashboardStatistics(): ParcelStatisticsDto {
        return parcelService.getStatistics()
    }

    /**
     * TODO add javadoc
     */
    private fun verifyHasRole(departmentType: String): Boolean {
        val authorities: List<GrantedAuthority> = SecurityContextHolder.getContext()
            .authentication.authorities.toList()

        if (!authorities.map { it.authority }.contains("ROLE_${departmentType.uppercase()}")) {
            return true
        }

        return false
    }
}
