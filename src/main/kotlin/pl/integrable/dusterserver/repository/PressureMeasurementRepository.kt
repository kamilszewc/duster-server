package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.PressureMeasurement
import pl.integrable.dusterserver.model.Sensor
import java.time.LocalDateTime

interface PressureMeasurementRepository : JpaRepository<PressureMeasurement, Long> {

    fun findAllByDateBetween(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime) : List<PressureMeasurement>

    fun findAllByDateBetweenAndSensor(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime, sensor: Sensor) : List<PressureMeasurement>
}