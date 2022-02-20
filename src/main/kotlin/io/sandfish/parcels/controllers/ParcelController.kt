package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.ParcelDto
import io.sandfish.parcels.dtos.ParcelStatisticsDto
import io.sandfish.parcels.services.ParcelService
import io.sandfish.parcels.services.department.DepartmentType
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
    fun getAllParcels(@RequestParam departmentType: DepartmentType): ResponseEntity<List<ParcelDto>> {
        val authorities: List<GrantedAuthority> = SecurityContextHolder.getContext()
            .authentication.authorities.toList()

        if(!authorities.map { it.authority }.contains("ROLE_${departmentType.name.uppercase()}")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val parcels = parcelService.findParcelByDepartment(departmentType)
        return ResponseEntity.ok(parcels.map { it.toTransferObject() })
    }

    @GetMapping("/{id}")
    fun getParcelById(@PathVariable id: Long): ParcelDto {
        val parcel = parcelService.findParcelById(id)

        return parcel.toTransferObject()
    }

    @PostMapping("/{id}/command/process")
    fun processParcel(@PathVariable id: Long, @RequestBody body: Unit): ParcelDto {
        return parcelService.processParcel(id).toTransferObject()
    }


    /**
     * Get statistics to show on the dashboard screen
     */
    @GetMapping("/statistics")
    fun getDashboardStatistics(): ParcelStatisticsDto {
        return parcelService.getStatistics()
    }
}
