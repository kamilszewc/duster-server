package pl.integrable.dusterserver.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.payload.PmMeasurementExchange
import pl.integrable.dusterserver.repository.PmMeasurementRepository
import java.time.LocalDateTime

@Service
class PmMeasurementProvider @Autowired constructor(val pmMeasurementRepository: PmMeasurementRepository) {

    fun provideLastMeasurements(fromlocalDateTime: LocalDateTime, averageType: String, sensor: Sensor) : List<PmMeasurementExchange> {

        if (averageType == "30min") {
            val averagedPmMeasurements : MutableList<PmMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now()

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusMinutes(30L)
                val measurement = PmMeasurementExchange(0.0, 0.0, 0.0, untilMoment.plusMinutes(15L))
                val measurements = pmMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.pm10 += it.pm10
                    measurement.pm25 += it.pm25
                    measurement.pm100 += it.pm100
                }

                measurement.pm10 /= measurements.size
                measurement.pm25 /= measurements.size
                measurement.pm100 /= measurements.size

                averagedPmMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedPmMeasurements.reversed();
        }
        if (averageType == "4hourly") {
            val averagedPmMeasurements : MutableList<PmMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusHours(4).withMinute(0).withSecond(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusHours(4)
                val measurement = PmMeasurementExchange(0.0, 0.0, 0.0, untilMoment.plusHours(2))
                val measurements = pmMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.pm10 += it.pm10
                    measurement.pm25 += it.pm25
                    measurement.pm100 += it.pm100
                }

                measurement.pm10 /= measurements.size
                measurement.pm25 /= measurements.size
                measurement.pm100 /= measurements.size

                averagedPmMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedPmMeasurements.reversed();

        }
        if (averageType == "daily") {
            val averagedPmMeasurements : MutableList<PmMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusDays(1)
                val measurement = PmMeasurementExchange(0.0, 0.0, 0.0, untilMoment.withHour(12))
                val measurements = pmMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.pm10 += it.pm10
                    measurement.pm25 += it.pm25
                    measurement.pm100 += it.pm100
                }

                measurement.pm10 /= measurements.size
                measurement.pm25 /= measurements.size
                measurement.pm100 /= measurements.size

                averagedPmMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedPmMeasurements.reversed();

        } else {
            val measurements = pmMeasurementRepository.findAllByDateBetweenAndSensor(fromlocalDateTime, LocalDateTime.now(), sensor)

            val pmMeasurements : MutableList<PmMeasurementExchange> = mutableListOf()

            measurements.forEach {
                val measurement = PmMeasurementExchange(it.pm10, it.pm25, it.pm100, it.date)
                pmMeasurements.add(measurement)
            }

            return pmMeasurements
        }
    }
}