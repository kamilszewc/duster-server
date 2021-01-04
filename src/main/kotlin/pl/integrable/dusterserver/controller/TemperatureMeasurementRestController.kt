package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.TemperatureMeasurement
import pl.integrable.dusterserver.repository.TemperatureMeasurementRepository

@RestController
class TemperatureMeasurementRestController @Autowired constructor(
    val temperatureMeasurementRepository: TemperatureMeasurementRepository
) {

    @PostMapping("/api/v1/record/temperature")
    fun recordPmMeasurement(@RequestBody temperatureMeasurement: TemperatureMeasurement) {

        temperatureMeasurementRepository.save(temperatureMeasurement)
    }
}