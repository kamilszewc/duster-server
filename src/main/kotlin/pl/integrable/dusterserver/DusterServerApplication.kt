package pl.integrable.dusterserver

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.property.CredentialsProperties
import pl.integrable.dusterserver.property.MapProperties
import pl.integrable.dusterserver.repository.SensorRepository

@SpringBootApplication
@EnableConfigurationProperties(
    CredentialsProperties::class,
    MapProperties::class
)
class DusterServerApplication {

    @Bean
    fun init(sensorRepository: SensorRepository) = CommandLineRunner {

        var sensor = Sensor("name", "email@email", 18.59814, 53.01375, 100.0)
        sensorRepository.save(sensor)

    }
}

fun main(args: Array<String>) {
    runApplication<DusterServerApplication>(*args)
}
