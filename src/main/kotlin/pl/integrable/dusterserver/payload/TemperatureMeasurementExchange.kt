package pl.integrable.dusterserver.payload

import java.time.LocalDateTime

class TemperatureMeasurementExchange(
    var temperature: Double,
    var date: LocalDateTime
) {
}