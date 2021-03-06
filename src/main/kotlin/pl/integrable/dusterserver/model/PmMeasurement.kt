package pl.integrable.dusterserver.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name="db_pm")
class PmMeasurement(
    var pm10: Double,
    var pm25: Double,
    var pm100: Double,
    var date: LocalDateTime,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="sensor_id", nullable = false)
    var sensor: Sensor,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
}