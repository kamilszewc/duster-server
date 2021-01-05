package pl.integrable.dusterserver.payload

import pl.integrable.dusterserver.model.TemperatureMeasurement
import java.time.LocalDateTime

class TemperatureMeasurementExchange(
    var temperature: Double,
    var unit: TemperatureMeasurement.TemperatureUnit,
    var date: LocalDateTime
) {
}