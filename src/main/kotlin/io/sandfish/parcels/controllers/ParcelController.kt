package io.sandfish.parcels.controllers

import io.sandfish.parcels.controllers.exceptions.UnauthorizedUserForDepartmentException
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
    fun getAllParcels(@RequestParam department: String): List<ParcelDto> {
        if (!verifyHasRole(department)) {
            throw UnauthorizedUserForDepartmentException("User is not authorized to retrieved parcels for this department.")
        }

        val parcels = parcelService.findParcelByDepartmentName(department)
        return parcels.map { it.toTransferObject() }
    }

    @PostMapping("/{id}/command/process")
    fun processParcel(@PathVariable id: Long, @RequestBody body: Unit): ParcelDto {
        val department = parcelService.findParcelById(id).department

        if (!verifyHasRole(department)) {
            throw UnauthorizedUserForDepartmentException("User is not authorized to process parcels for this department.")
        }

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
     * Check if the user has the required role for the provided department
     */
    private fun verifyHasRole(departmentType: String): Boolean {
        val authorities: List<GrantedAuthority> = SecurityContextHolder.getContext()
            .authentication.authorities.toList()

        if (!authorities.map { it.authority }.contains("ROLE_${departmentType.uppercase()}")) {
            return false
        }

        return true
    }
}
