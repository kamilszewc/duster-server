package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import pl.integrable.dusterserver.property.MapProperties
import pl.integrable.dusterserver.repository.SensorRepository

@Controller
class SensorsController {

    @Autowired
    lateinit var sensorRepository: SensorRepository

    @Autowired
    lateinit var mapProperties: MapProperties

    @GetMapping("/sensors")
    fun sensors(model: Model) : String {

        val sensors = sensorRepository.findAll()

        model.addAttribute("sensors", sensors)

        val longitudes: MutableList<Double> = mutableListOf();
        val latitudes: MutableList<Double> = mutableListOf();
        val colors: MutableList<String> = mutableListOf();
        val links: MutableList<String> = mutableListOf();
        sensors.forEach {
            longitudes.add(it.longitude)
            latitudes.add(it.latitude)
            colors.add("black")
            links.add("sensor/" + it.id)
        }

        model.addAttribute("longitudes", longitudes)
        model.addAttribute("latitudes", latitudes)
        model.addAttribute("colors", colors)
        model.addAttribute("links", links)

        model.addAttribute("longitude", mapProperties.longitude)
        model.addAttribute("latitude", mapProperties.latitude)
        model.addAttribute("zoom", mapProperties.zoom)
        model.addAttribute("apikey", mapProperties.apikey)

        return "sensors"
    }
}