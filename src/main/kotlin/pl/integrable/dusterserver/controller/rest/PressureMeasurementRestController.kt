package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.PressureMeasurement
import pl.integrable.dusterserver.payload.PressureMeasurementExchange
import pl.integrable.dusterserver.payload.Status
import pl.integrable.dusterserver.repository.PressureMeasurementRepository
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.service.JwtTokenPrincipal

@RestController
class PressureMeasurementRestController @Autowired constructor(
    val pressureMeasurementRepository: PressureMeasurementRepository,
    val sensorRepository: SensorRepository
) {

    @PostMapping("/api/v1/record/pressure")
    fun recordPressureMeasurement(@RequestBody pressureMeasurementExchange: PressureMeasurementExchange,
                                  authentication: Authentication) : ResponseEntity<Status<String>>{

        val name = (authentication.credentials as JwtTokenPrincipal).servicename

        val sensor = sensorRepository.findByName(name)
        if (sensor.isPresent) {

            val pressureMeasurement = PressureMeasurement(
                pressureMeasurementExchange.pressure,
                pressureMeasurementExchange.date,
                sensor.get()
            )

            pressureMeasurementRepository.save(pressureMeasurement)

            return ResponseEntity.ok().body(Status("ok"))
        } else {
            return ResponseEntity.ok().body(Status("sensor is not present", errorCode = 1))
        }
    }
}