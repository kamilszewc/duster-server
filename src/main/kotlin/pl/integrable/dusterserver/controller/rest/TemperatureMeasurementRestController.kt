package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.TemperatureMeasurement
import pl.integrable.dusterserver.payload.Status
import pl.integrable.dusterserver.payload.TemperatureMeasurementExchange
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.repository.TemperatureMeasurementRepository
import pl.integrable.dusterserver.service.JwtTokenPrincipal

@RestController
class TemperatureMeasurementRestController @Autowired constructor(
    val temperatureMeasurementRepository: TemperatureMeasurementRepository,
    val sensorRepository: SensorRepository
) {

    @PostMapping("/api/v1/record/temperature")
    fun recordPmMeasurement(@RequestBody temperatureMeasurementExchange: TemperatureMeasurementExchange,
                            authentication: Authentication) : ResponseEntity<Status<String>>{

        val name = (authentication.credentials as JwtTokenPrincipal).servicename
        println(name)

        val sensor = sensorRepository.findByName(name)
        if (sensor.isPresent) {

            print(temperatureMeasurementExchange.temperature)

            val temperatureMeasurement = TemperatureMeasurement(
                temperatureMeasurementExchange.temperature,
                temperatureMeasurementExchange.date,
                sensor.get()
            )

            temperatureMeasurementRepository.save(temperatureMeasurement)

            return ResponseEntity.ok().body(Status("ok"))
        } else {
            return ResponseEntity.ok().body(Status("sensor is not present", errorCode = 1))
        }
    }
}