package pl.integrable.dusterserver.payload

import java.time.LocalDateTime

class PressureMeasurementExchange(
    var pressure: Double,
    var date: LocalDateTime
) {
}