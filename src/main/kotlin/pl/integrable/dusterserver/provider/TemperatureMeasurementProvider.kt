package pl.integrable.dusterserver.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.model.TemperatureMeasurement
import pl.integrable.dusterserver.payload.TemperatureMeasurementExchange
import pl.integrable.dusterserver.repository.TemperatureMeasurementRepository
import java.time.LocalDateTime

@Service
class TemperatureMeasurementProvider @Autowired constructor(val temperatureMeasurementRepository: TemperatureMeasurementRepository) {

    fun provideLastMeasurements(
        fromlocalDateTime: LocalDateTime,
        averageType: String,
        sensor: Sensor
    ): List<TemperatureMeasurementExchange> {

        if (averageType == "30min") {
            val averagedTemperatureMeasurements: MutableList<TemperatureMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now()

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusMinutes(30L)
                val measurement = TemperatureMeasurementExchange(
                    0.0,
                    untilMoment.plusMinutes(15L)
                )
                val measurements = temperatureMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.temperature += it.temperature
                }

                measurement.temperature /= measurements.size

                averagedTemperatureMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedTemperatureMeasurements.reversed();

        }
        if (averageType == "4hourly") {
            val averagedTemperatureMeasurements: MutableList<TemperatureMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusHours(4).withMinute(0).withSecond(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusHours(4)
                val measurement = TemperatureMeasurementExchange(
                    0.0,
                    untilMoment.withMinute(120)
                )
                val measurements = temperatureMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.temperature += it.temperature
                }

                measurement.temperature /= measurements.size

                averagedTemperatureMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedTemperatureMeasurements.reversed();

        }
        if (averageType == "daily") {
            val averageTemperatureMeasurements: MutableList<TemperatureMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusDays(1)
                val measurement = TemperatureMeasurementExchange(
                    0.0,
                    untilMoment.withHour(12)
                )
                val measurements = temperatureMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.temperature += it.temperature
                }

                measurement.temperature /= measurements.size

                averageTemperatureMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averageTemperatureMeasurements.reversed();

        } else {
            val measurements =
                temperatureMeasurementRepository.findAllByDateBetweenAndSensor(fromlocalDateTime, LocalDateTime.now(), sensor)

            val temperatureMeasurements: MutableList<TemperatureMeasurementExchange> = mutableListOf()

            measurements.forEach {
                val measurement = TemperatureMeasurementExchange(it.temperature, it.date)
                temperatureMeasurements.add(measurement)
            }

            return temperatureMeasurements
        }
    }
}
