package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.Department
import org.springframework.data.repository.CrudRepository

interface DepartmentRepository : CrudRepository<Department, Long> {
    fun findByName(name: String): Department
}
