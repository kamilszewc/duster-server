package pl.integrable.dusterserver

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.property.CredentialsProperties
import pl.integrable.dusterserver.repository.SensorRepository

@SpringBootApplication
@EnableConfigurationProperties(
    CredentialsProperties::class
)
class DusterServerApplication {

    @Bean
    fun init(sensorRepository: SensorRepository) = CommandLineRunner {

        var sensor = Sensor("name", "email@email", 100.0, 100.0, 100.0)
        sensorRepository.save(sensor)
    }
}

fun main(args: Array<String>) {
    runApplication<DusterServerApplication>(*args)
}
