package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.PmMeasurement
import java.time.LocalDateTime

interface PmMeasurementRepository : JpaRepository<PmMeasurement, Long> {

    fun findAllByDateBetween(fromLocalDateTime: LocalDateTime, untilLocalDateTime: LocalDateTime) : List<PmMeasurement>;
}