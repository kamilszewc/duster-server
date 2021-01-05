package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.Sensor

interface SensorRepository : JpaRepository<Sensor, Long> {

    override fun findAll() : List<Sensor>
    fun findByName(name: String) : Sensor
}