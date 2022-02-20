package io.sandfish.parcels.services.department.strategy

import io.sandfish.parcels.dtos.ParcelReceipient
import io.sandfish.parcels.dtos.ReceipientAddress

class ContainerParcelTestHelper {
    companion object {
        fun defaultReceipient(): ParcelReceipient {
            return ParcelReceipient(
                "Sander Visser",
                ReceipientAddress(
                    "Test",
                    "42",
                    "6037AA",
                    "Rotterdam"
                )
            )
        }
    }

}
