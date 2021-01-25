package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import pl.integrable.dusterserver.model.TemperatureMeasurement
import pl.integrable.dusterserver.provider.HumidityMeasurementProvider
import pl.integrable.dusterserver.provider.TemperatureMeasurementProvider
import pl.integrable.dusterserver.provider.PmMeasurementProvider
import pl.integrable.dusterserver.provider.PressureMeasurementProvider
import pl.integrable.dusterserver.repository.ContentRepository
import pl.integrable.dusterserver.repository.SensorRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
class SensorController {

    @Autowired
    lateinit var sensorRepository: SensorRepository

    @Autowired
    lateinit var pmMeasurementProvider: PmMeasurementProvider

    @Autowired
    lateinit var temperatureMeasurementProvider: TemperatureMeasurementProvider

    @Autowired
    lateinit var pressureMeasurementProvider: PressureMeasurementProvider

    @Autowired
    lateinit var humidityMeasurementProvider: HumidityMeasurementProvider

    @Autowired
    lateinit var contentRepository: ContentRepository


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
            averageType = "15min"
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

            // PM

            val pmMeasurements = pmMeasurementProvider.provideLastMeasurements(localTimeDate, averageType, sensor.get())

            var havePm = false
            pmMeasurements.forEach {
                if (it.pm10 == it.pm10 || it.pm25 == it.pm10 || it.pm100 == it.pm10) {
                    havePm = true
                }
            }
            model.addAttribute("havePm", havePm)

            val plotPmDate: MutableList<String> = mutableListOf()
            val plotPm10: MutableList<Double> = mutableListOf()
            val plotPm25: MutableList<Double> = mutableListOf()
            val plotPm100: MutableList<Double> = mutableListOf()

            pmMeasurements.forEach { measurement ->
                measurement.date?.let { plotPmDate.add(it.format(DateTimeFormatter.ofPattern(pattern))) }
                if (measurement.pm10 == measurement.pm10 && measurement.pm10 != 0.0)
                    plotPm10.add(measurement.pm10)
                if (measurement.pm25 == measurement.pm25 && measurement.pm25 != 0.0)
                    plotPm25.add(measurement.pm25)
                if (measurement.pm100 == measurement.pm100 && measurement.pm100 != 0.0)
                    plotPm100.add(measurement.pm100)
            }

            model.addAttribute("plotPmDate", plotPmDate)
            model.addAttribute("plotPm10", plotPm10)
            model.addAttribute("plotPm25", plotPm25)
            model.addAttribute("plotPm100", plotPm100)


            // Temperature

            val temperatureMeasurements = temperatureMeasurementProvider.provideLastMeasurements(localTimeDate, averageType, sensor.get())

            var haveTemperature = false
            temperatureMeasurements.forEach {
                println("" + it.date + " " + it.temperature);
                if (it.temperature == it.temperature) {
                    haveTemperature = true
                }
            }
            model.addAttribute("haveTemperature", haveTemperature)

            val plotTemperatureDate: MutableList<String> = mutableListOf()
            val plotTemperature: MutableList<Double> = mutableListOf()

            temperatureMeasurements.forEach { measurement ->
                measurement.date?.let { plotTemperatureDate.add(it.format(DateTimeFormatter.ofPattern(pattern))) }
                plotTemperature.add(measurement.temperature)
            }

            model.addAttribute("plotTemperatureDate", plotTemperatureDate)
            model.addAttribute("plotTemperature", plotTemperature)


            // Pressure

            val pressureMeasurements = pressureMeasurementProvider.provideLastMeasurements(localTimeDate, averageType, sensor.get())

            var havePressure = false
            pressureMeasurements.forEach {
                if (it.pressure == it.pressure) {
                    havePressure = true
                }
            }
            model.addAttribute("havePressure", havePressure)

            val plotPressureDate: MutableList<String> = mutableListOf()
            val plotPressure: MutableList<Double> = mutableListOf()

            pressureMeasurements.forEach { measurement ->
                measurement.date?.let { plotPressureDate.add(it.format(DateTimeFormatter.ofPattern(pattern))) }
                plotPressure.add(measurement.pressure)
            }

            model.addAttribute("plotPressureDate", plotPressureDate)
            model.addAttribute("plotPressure", plotPressure)

            // Humidity

            val humidityMeasurements = humidityMeasurementProvider.provideLastMeasurements(localTimeDate, averageType, sensor.get())

            var haveHumidity = false
            humidityMeasurements.forEach {
                if (it.humidity == it.humidity) {
                    haveHumidity = true
                }
            }
            model.addAttribute("haveHumidity", haveHumidity)

            val plotHumidityDate: MutableList<String> = mutableListOf()
            val plotHumidity: MutableList<Double> = mutableListOf()

            humidityMeasurements.forEach { measurement ->
                measurement.date?.let { plotHumidityDate.add(it.format(DateTimeFormatter.ofPattern(pattern))) }
                plotHumidity.add(measurement.humidity)
            }

            model.addAttribute("plotHumidityDate", plotHumidityDate)
            model.addAttribute("plotHumidity", plotHumidity)

            model.addAttribute("sensorId", sensorId)
            model.addAttribute("timeRange", timeRange)

            val allContent = contentRepository.findAll();
            model.addAttribute("allContent", allContent)

            return "sensor"
        }

        return "redirect:/error"
    }
}