package pl.integrable.dusterserver.payload

import java.time.LocalDateTime

class HumidityMeasurementExchange(
    var humidity: Double,
    var date: LocalDateTime
) {
}