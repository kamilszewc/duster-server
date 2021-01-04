package pl.integrable.dusterserver.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name="db_pm")
class PmMeasurement(
    var pm10: Double,
    var pm25: Double,
    var pm100: Double,
    var date: LocalDateTime,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
}