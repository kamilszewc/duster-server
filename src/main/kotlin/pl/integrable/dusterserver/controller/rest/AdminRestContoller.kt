package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.service.JwtTokenService

@RestController
class AdminRestContoller {

    @Autowired
    lateinit var jwtTokenService: JwtTokenService

    @Autowired
    lateinit var sensorRepository: SensorRepository

    @GetMapping("/api/v1/admin/generateToken/{sensorName}")
    fun generateToken(@PathVariable sensorName: String) : ResponseEntity<String> {

        val sensor = sensorRepository.findByName(sensorName)
        val token = sensor?.let { jwtTokenService.generateToken(it) }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(token)
    }
}