package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.Rule
import org.springframework.data.repository.CrudRepository

interface RuleRepository : CrudRepository<Rule, Long>
