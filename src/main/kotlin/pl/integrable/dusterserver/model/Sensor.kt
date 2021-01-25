package pl.integrable.dusterserver.model

import javax.persistence.*

@Entity(name = "db_sensor")
class Sensor(
    @Column(nullable = false, unique = true)
    var name: String,
    var email: String,
    var longitude: Double,
    var latitude: Double,
    var altitude: Double,
    var type: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
}