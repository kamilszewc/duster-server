package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.model.TemperatureMeasurement
import java.time.LocalDateTime

interface TemperatureMeasurementRepository : JpaRepository<TemperatureMeasurement, Long> {

    fun findAllByDateBetween(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime) : List<TemperatureMeasurement>

    fun findAllByDateBetweenAndSensor(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime, sensor: Sensor) : List<TemperatureMeasurement>
}