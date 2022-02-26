package io.sandfish.parcels.services.container

import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.domain.Rule
import io.sandfish.parcels.dtos.ContainerParcel
import io.sandfish.parcels.dtos.ContainerXMLPayload
import io.sandfish.parcels.dtos.ParcelReceipient
import io.sandfish.parcels.dtos.ReceipientAddress
import io.sandfish.parcels.repositories.ContainerRepository
import io.sandfish.parcels.services.department.DepartmentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.ZonedDateTime

internal class ContainerPayloadProcessorTest {

    @Test
    fun processContainer() {
        val containerRepository: ContainerRepository = spy(ContainerRepository::class.java)
        val departmentService: DepartmentService = mock(DepartmentService::class.java)

        val containerPayloadProcessor = ContainerPayloadProcessor(
            containerRepository, departmentService
        )

        `when`(departmentService.getDepartments()).thenReturn(
            listOf(
                Department(1, "Above 10", 1, mutableSetOf(), mutableSetOf(), mutableSetOf(
                    Rule("weight", ">", 10.0)
                )),
                Department(2, "Below 10", 1, mutableSetOf(), mutableSetOf(), mutableSetOf(
                    Rule("weight", "<", 10.0)
                )),
                Department(2, "Exact", 1, mutableSetOf(), mutableSetOf(), mutableSetOf(
                    Rule("value", "=", 1234.0)
                )),
            )
        )

        //Return method input back as output
        `when`(containerRepository.save(any())).thenAnswer { i -> i.arguments[0] }


        val payload = ContainerXMLPayload(
            100,
            ZonedDateTime.now(),
            listOf(
                ContainerParcel(10.0, 9.0, defaultReceipient()),
                ContainerParcel(10.0, 9.0, defaultReceipient()),
                ContainerParcel(10.0, 11.0, defaultReceipient()),
                ContainerParcel(10.0, 100.0, defaultReceipient()),
                ContainerParcel(1234.0, 10.0, defaultReceipient()),
            )
        )

        val processedContainer = containerPayloadProcessor.processContainer(
            payload
        )

        assertThat(processedContainer.containerId).isEqualTo(100)
        assertThat(processedContainer.parcels).hasSize(5)
        assertThat(processedContainer.parcels.count { it.department === "Above 10" }).isEqualTo(2)
        assertThat(processedContainer.parcels.count { it.department === "Below 10" }).isEqualTo(2)
        assertThat(processedContainer.parcels.count { it.department === "Exact" }).isEqualTo(1)
    }


    private fun defaultReceipient(): ParcelReceipient {
        return ParcelReceipient("Sander",
            ReceipientAddress("A", "B", "C", "D")
        )
    }
}
