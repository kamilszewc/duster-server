package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.PmMeasurement
import pl.integrable.dusterserver.model.TemperatureMeasurement
import pl.integrable.dusterserver.payload.TemperatureMeasurementExchange
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.repository.TemperatureMeasurementRepository
import pl.integrable.dusterserver.service.JwtTokenPrincipal
import java.security.Principal

@RestController
class TemperatureMeasurementRestController @Autowired constructor(
    val temperatureMeasurementRepository: TemperatureMeasurementRepository,
    val sensorRepository: SensorRepository
) {

    @PostMapping("/api/v1/record/temperature")
    fun recordPmMeasurement(@RequestBody temperatureMeasurementExchange: TemperatureMeasurementExchange,
                            authentication: Authentication) {

        val name = (authentication.credentials as JwtTokenPrincipal).servicename

        val sensor = sensorRepository.findByName(name)
        if (sensor != null) {

            val temperatureMeasurement = TemperatureMeasurement(
                temperatureMeasurementExchange.temperature,
                temperatureMeasurementExchange.unit,
                temperatureMeasurementExchange.date,
                sensor
            )

            temperatureMeasurementRepository.save(temperatureMeasurement)
        }
    }
}