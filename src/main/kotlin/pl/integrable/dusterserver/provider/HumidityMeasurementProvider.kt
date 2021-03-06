package pl.integrable.dusterserver.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.payload.HumidityMeasurementExchange
import pl.integrable.dusterserver.repository.HumidityMeasurementRepository
import java.time.LocalDateTime

@Service
class HumidityMeasurementProvider @Autowired constructor(val humidityMeasurementRepository: HumidityMeasurementRepository) {

    fun provideLastMeasurements(
        fromlocalDateTime: LocalDateTime,
        averageType: String,
        sensor: Sensor
    ): List<HumidityMeasurementExchange> {


        if (averageType == "30min") {
            val averagedHumidityMeasurements: MutableList<HumidityMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now()

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusMinutes(30L)
                val measurement = HumidityMeasurementExchange(
                    0.0,
                    untilMoment.plusMinutes(15L)
                )
                val measurements = humidityMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.humidity += it.humidity
                }

                measurement.humidity /= measurements.size

                averagedHumidityMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedHumidityMeasurements.reversed();
        }
        if (averageType == "4hourly") {
            val averagedHumidityMeasurements: MutableList<HumidityMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusHours(4).withMinute(0).withSecond(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusHours(4)
                val measurement = HumidityMeasurementExchange(
                    0.0,
                    untilMoment.plusHours(2)
                )
                val measurements = humidityMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.humidity += it.humidity
                }

                measurement.humidity /= measurements.size

                averagedHumidityMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedHumidityMeasurements.reversed();

        }
        if (averageType == "daily") {
            val averageHumidityMeasurements: MutableList<HumidityMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusDays(1)
                val measurement = HumidityMeasurementExchange(
                    0.0,
                    untilMoment.withHour(12)
                )
                val measurements = humidityMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.humidity += it.humidity
                }

                measurement.humidity /= measurements.size

                averageHumidityMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averageHumidityMeasurements.reversed();

        } else {
            val measurements =
                humidityMeasurementRepository.findAllByDateBetweenAndSensor(fromlocalDateTime, LocalDateTime.now(), sensor)

            val humidityMeasurements: MutableList<HumidityMeasurementExchange> = mutableListOf()

            measurements.forEach {
                val measurement = HumidityMeasurementExchange(it.humidity, it.date)
                humidityMeasurements.add(measurement)
            }

            return humidityMeasurements
        }
    }
}
