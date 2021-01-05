package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.PmMeasurement
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.payload.PmMeasurementExchange
import pl.integrable.dusterserver.repository.PmMeasurementRepository
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.service.JwtTokenPrincipal
import java.net.Authenticator
import java.security.Principal

@RestController
class PmMeasurementRestController @Autowired constructor(
    val pmMeasurementRepository: PmMeasurementRepository,
    val sensorRepository: SensorRepository
) {

    @PostMapping("/api/v1/record/pm")
    fun recordPmMeasurement(@RequestBody pmMeasurementExchange: PmMeasurementExchange,
                            authentication: Authentication) {

        val name = (authentication.credentials as JwtTokenPrincipal).servicename

        val sensor = sensorRepository.findByName(name)
        if (sensor != null) {

            val pmMeasurement = PmMeasurement(
                pmMeasurementExchange.pm10,
                pmMeasurementExchange.pm25,
                pmMeasurementExchange.pm100,
                pmMeasurementExchange.date,
                sensor
            )

            pmMeasurementRepository.save(pmMeasurement)
        }
    }
}