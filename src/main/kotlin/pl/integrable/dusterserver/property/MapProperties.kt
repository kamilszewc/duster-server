package pl.integrable.dusterserver.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "map")
class MapProperties {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var zoom: Int = 12
    var apikey: String = ""
}