package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.model.PmMeasurement
import pl.integrable.dusterserver.repository.PmMeasurementRepository

@RestController
class PmMeasurementRestController @Autowired constructor(
    val pmMeasurementRepository: PmMeasurementRepository
) {

    @PostMapping("/api/v1/record/pm")
    fun recordPmMeasurement(@RequestBody pmMeasurement: PmMeasurement) {

        println("Vum")

        pmMeasurementRepository.save(pmMeasurement)

    }
}