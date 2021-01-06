package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.PmMeasurement
import pl.integrable.dusterserver.payload.PmMeasurementExchange
import pl.integrable.dusterserver.payload.Status
import pl.integrable.dusterserver.repository.PmMeasurementRepository
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.service.JwtTokenPrincipal

@RestController
class PmMeasurementRestController @Autowired constructor(
    val pmMeasurementRepository: PmMeasurementRepository,
    val sensorRepository: SensorRepository
) {

    @PostMapping("/api/v1/record/pm")
    fun recordPmMeasurement(@RequestBody pmMeasurementExchange: PmMeasurementExchange,
                            authentication: Authentication) : ResponseEntity<Status<String>> {

        val name = (authentication.credentials as JwtTokenPrincipal).servicename

        val sensor = sensorRepository.findByName(name)
        if (sensor.isPresent) {

            val pmMeasurement = PmMeasurement(
                pmMeasurementExchange.pm10,
                pmMeasurementExchange.pm25,
                pmMeasurementExchange.pm100,
                pmMeasurementExchange.date,
                sensor.get()
            )

            pmMeasurementRepository.save(pmMeasurement)

            return ResponseEntity.ok().body(Status("ok"))
        } else {
            return ResponseEntity.ok().body(Status("sensor is not present", errorCode = 1))
        }
    }
}