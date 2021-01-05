package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import pl.integrable.dusterserver.repository.PmMeasurementRepository
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.repository.TemperatureMeasurementRepository
import java.time.LocalDateTime

@Controller
class SensorController {

    @Autowired
    lateinit var sensorRepository: SensorRepository

    @Autowired
    lateinit var pmMeasurementRepository: PmMeasurementRepository

    @Autowired
    lateinit var temperatureMeasurementRepository: TemperatureMeasurementRepository

    @GetMapping("/sensors")
    fun sensors() : String {
        return "sensors"
    }

    @GetMapping("/sensor/{sensorId}")
    fun sensor(@PathVariable sensorId: Long,
               @RequestParam(required = true, name = "time-range") timeRange: String?,
               model: Model
    ) : String {

        var localTimeDate = LocalDateTime.now()
        var pattern = "yyyy-MM-dd HH:mm"
        var averageType = "none"

        if (timeRange == "hour") {
            localTimeDate = localTimeDate.minusHours(1)
            averageType = "none"
            pattern = "HH:mm"
        }
        else if (timeRange == "day") {
            localTimeDate = localTimeDate.minusDays(1)
            averageType = "none"
            pattern = "HH:mm"
        }
        else if (timeRange == "week") {
            localTimeDate = localTimeDate.minusWeeks(1)
            averageType = "hourly"
            pattern = "yyyy-MM-dd HH:mm"
        }
        else if (timeRange == "month") {
            localTimeDate = localTimeDate.minusMonths(1)
            averageType = "daily"
            pattern = "yyyy-MM-dd"
        }
        else if (timeRange == "year") {
            localTimeDate = localTimeDate.minusYears(1)
            averageType = "daily"
            pattern = "yyyy-MM-dd"
        }

        // Get sensor
        val sensor = sensorRepository.findById(sensorId)
        if (sensor.isPresent) {


            val pmMeasurements = pmMeasurementRepository.findAllByDateBetweenAndSensor(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                sensor.get()
                )

            println(pmMeasurements.size)

            model.addAttribute("sensorId", sensorId)
            model.addAttribute("timeRange", timeRange)

            return "sensor"
        }

        return "redirect:/error"
    }
}