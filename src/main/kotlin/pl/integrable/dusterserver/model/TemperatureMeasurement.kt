package pl.integrable.dusterserver.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity(name="db_temperature")
class TemperatureMeasurement(
    var temperature: Double,
    var date: LocalDateTime,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="sensor_id", nullable = false)
    var sensor: Sensor,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
}