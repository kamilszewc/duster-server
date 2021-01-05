package pl.integrable.dusterserver.payload

import java.time.LocalDateTime

class PmMeasurementExchange(
    var pm10: Double,
    var pm25: Double,
    var pm100: Double,
    var date: LocalDateTime
) {
}