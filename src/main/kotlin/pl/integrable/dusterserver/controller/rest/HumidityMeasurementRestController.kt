package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.HumidityMeasurement
import pl.integrable.dusterserver.payload.HumidityMeasurementExchange
import pl.integrable.dusterserver.payload.Status
import pl.integrable.dusterserver.repository.HumidityMeasurementRepository
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.service.JwtTokenPrincipal

@RestController
class HumidityMeasurementRestController @Autowired constructor(
    val humidityMeasurementRepository: HumidityMeasurementRepository,
    val sensorRepository: SensorRepository
) {

    @PostMapping("/api/v1/record/humidity")
    fun recordPressureMeasurement(@RequestBody humidityMeasurementExchange: HumidityMeasurementExchange,
                                  authentication: Authentication) : ResponseEntity<Status<String>>{

        val name = (authentication.credentials as JwtTokenPrincipal).servicename

        val sensor = sensorRepository.findByName(name)
        if (sensor.isPresent) {

            val humidityMeasurement = HumidityMeasurement(
                humidityMeasurementExchange.humidity,
                humidityMeasurementExchange.date,
                sensor.get()
            )

            humidityMeasurementRepository.save(humidityMeasurement)

            return ResponseEntity.ok().body(Status("ok"))
        } else {
            return ResponseEntity.ok().body(Status("sensor is not present", errorCode = 1))
        }
    }
}