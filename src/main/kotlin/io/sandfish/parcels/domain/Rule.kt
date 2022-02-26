package io.sandfish.parcels.domain

import javax.persistence.*

@Entity
@Table(name = "rule")
class Rule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "key", nullable = false) val key: String,
    @Column(name = "sign", nullable = false) val sign: String,
    @Column(name = "value", nullable = false) val value: Double,

    @ManyToOne
    @JoinColumn(name = "department_id")
    var department: Department?
) {

    constructor(
        key: String,
        sign: String,
        value: Double
    ) : this(null, key, sign, value, null)
}
