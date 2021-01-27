package pl.integrable.dusterserver.provider

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.payload.PressureMeasurementExchange
import pl.integrable.dusterserver.repository.PressureMeasurementRepository
import java.time.LocalDateTime

@Service
class PressureMeasurementProvider @Autowired constructor(val pressureMeasurementRepository: PressureMeasurementRepository) {

    fun provideLastMeasurements(
        fromlocalDateTime: LocalDateTime,
        averageType: String,
        sensor: Sensor
    ): List<PressureMeasurementExchange> {

        if (averageType == "30min") {
            val averagedPressureMeasurements: MutableList<PressureMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now()

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusMinutes(30L)
                val measurement = PressureMeasurementExchange(
                    0.0,
                    untilMoment.plusMinutes(15L)
                )
                val measurements = pressureMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.pressure += it.pressure
                }

                measurement.pressure /= measurements.size

                averagedPressureMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedPressureMeasurements.reversed();

        }
        if (averageType == "4hourly") {
            val averagedPressureMeasurements: MutableList<PressureMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusHours(4).withMinute(0).withSecond(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusHours(4)
                val measurement = PressureMeasurementExchange(
                    0.0,
                    untilMoment.withMinute(120)
                )
                val measurements = pressureMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.pressure += it.pressure
                }

                measurement.pressure /= measurements.size

                averagedPressureMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagedPressureMeasurements.reversed();

        }
        if (averageType == "daily") {
            val averagePressureMeasurements: MutableList<PressureMeasurementExchange> = mutableListOf()

            var untilMoment = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)

            while (untilMoment.isAfter(fromlocalDateTime)) {

                val fromMoment = untilMoment.minusDays(1)
                val measurement = PressureMeasurementExchange(
                    0.0,
                    untilMoment.withHour(12)
                )
                val measurements = pressureMeasurementRepository.findAllByDateBetweenAndSensor(fromMoment, untilMoment, sensor)

                measurements.forEach {
                    measurement.pressure += it.pressure
                }

                measurement.pressure /= measurements.size

                averagePressureMeasurements.add(measurement)

                untilMoment = fromMoment
            }
            return averagePressureMeasurements.reversed();

        } else {
            val measurements =
                pressureMeasurementRepository.findAllByDateBetweenAndSensor(fromlocalDateTime, LocalDateTime.now(), sensor)

            val pressureMeasurements: MutableList<PressureMeasurementExchange> = mutableListOf()

            measurements.forEach {
                val measurement = PressureMeasurementExchange(it.pressure, it.date)
                pressureMeasurements.add(measurement)
            }

            return pressureMeasurements
        }
    }
}
