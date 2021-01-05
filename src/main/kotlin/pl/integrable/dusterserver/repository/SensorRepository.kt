package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.Sensor
import java.util.*

interface SensorRepository : JpaRepository<Sensor, Long> {

    override fun findAll() : List<Sensor>

    fun findByName(name: String) : Optional<Sensor>
}