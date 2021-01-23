package pl.integrable.dusterserver

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.LocaleResolver
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.property.CredentialsProperties
import pl.integrable.dusterserver.property.MapProperties
import pl.integrable.dusterserver.repository.SensorRepository
import java.util.Locale

import org.springframework.web.servlet.i18n.SessionLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import pl.integrable.dusterserver.model.Content
import pl.integrable.dusterserver.model.PmMeasurement
import pl.integrable.dusterserver.repository.ContentRepository
import pl.integrable.dusterserver.repository.PmMeasurementRepository
import java.time.LocalDateTime


@SpringBootApplication
@EnableConfigurationProperties(
    CredentialsProperties::class,
    MapProperties::class
)
class DusterServerApplication {

    @Bean
    fun init(sensorRepository: SensorRepository, pmMeasurementRepository: PmMeasurementRepository, contentRepository: ContentRepository) = CommandLineRunner {

        var sensor = Sensor("name", "email@email", 18.59814, 53.01375, 100.0)
        sensorRepository.save(sensor)

        var pmMeasurement = PmMeasurement(1.0, 1.0, 1.0, LocalDateTime.now(), sensor)
        pmMeasurementRepository.save(pmMeasurement)

        var content = Content("Przyłącz się", "<p class=\"text-center\">BleBleble</p><p class=\"text-center\">Blebleble</p>")
        contentRepository.save(content)

    }

    @Bean
    fun localeResolver(): LocaleResolver? {
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.US)
        return slr
    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor? {
        val lci = LocaleChangeInterceptor()
        lci.paramName = "lang"
        return lci
    }
}

fun main(args: Array<String>) {
    runApplication<DusterServerApplication>(*args)
}
