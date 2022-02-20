package io.sandfish.parcels.domain

import javax.persistence.*


@Entity
@Table(name = "role")
class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    @Column val name: String? = null,
    @Column val description: String? = null
)
