package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.HumidityMeasurement
import pl.integrable.dusterserver.model.Sensor
import java.time.LocalDateTime

interface HumidityMeasurementRepository : JpaRepository<HumidityMeasurement, Long> {

    fun findAllByDateBetween(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime) : List<HumidityMeasurement>

    fun findAllByDateBetweenAndSensor(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime, sensor: Sensor) : List<HumidityMeasurement>
}