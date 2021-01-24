package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import pl.integrable.dusterserver.model.PmMeasurement
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.property.MapProperties
import pl.integrable.dusterserver.repository.ContentRepository
import pl.integrable.dusterserver.repository.PmMeasurementRepository
import pl.integrable.dusterserver.repository.SensorRepository
import java.time.LocalDateTime

@Controller
class SensorsController {

    @Autowired
    lateinit var sensorRepository: SensorRepository

    @Autowired
    lateinit var mapProperties: MapProperties

    @Autowired
    lateinit var pmMeasurementRepository: PmMeasurementRepository

    @Autowired
    lateinit var contentRepository: ContentRepository

    @GetMapping("/sensors")
    fun sensors(model: Model) : String {

        val sensors = sensorRepository.findAll()

        model.addAttribute("sensors", sensors)

        val longitudes: MutableList<Double> = mutableListOf();
        val latitudes: MutableList<Double> = mutableListOf();
        val colors: MutableList<String> = mutableListOf();
        val links: MutableList<String> = mutableListOf();
        sensors.forEach {

            val pmMeasurements = pmMeasurementRepository.findAllByDateBetweenAndSensor(LocalDateTime.now().minusMinutes(61), LocalDateTime.now(), it)
            var pm10 = 0.0
            var pm25 = 0.0
            var pm100 = 0.0
            pmMeasurements.forEach {
                pm10 += it.pm10
                pm25 += it.pm25
                pm100 += it.pm100
            }
            pm10 /= pmMeasurements.size
            pm25 /= pmMeasurements.size
            pm100 /= pmMeasurements.size

            var color: String = "black" // General is green
            if (pm10 > 0.0 && pm25 > 0.0 && pm100 > 0.0) color = "green" // Sensor is dead
            if (pm10 > 40.0 || pm25 > 20.0 || pm100 > 20.0) color = "yellow" // Mediom
            if (pm10 > 60.0 || pm25 > 40.0 || pm100 > 40.0) color = "orange"
            if (pm10 > 80.0 || pm25 > 60.0 || pm100 > 60.0) color = "red"

            longitudes.add(it.longitude)
            latitudes.add(it.latitude)
            colors.add(color)
            links.add("sensor/" + it.id + "?time-range=day")
        }

        model.addAttribute("longitudes", longitudes)
        model.addAttribute("latitudes", latitudes)
        model.addAttribute("colors", colors)
        model.addAttribute("links", links)

        model.addAttribute("longitude", mapProperties.longitude)
        model.addAttribute("latitude", mapProperties.latitude)
        model.addAttribute("zoom", mapProperties.zoom)
        model.addAttribute("apikey", mapProperties.apikey)

        val allContent = contentRepository.findAll();
        model.addAttribute("allContent", allContent)

        return "sensors"
    }
}