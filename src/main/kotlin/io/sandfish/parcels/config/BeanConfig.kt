package io.sandfish.parcels.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BeanConfig {

    @Bean
    fun encoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
