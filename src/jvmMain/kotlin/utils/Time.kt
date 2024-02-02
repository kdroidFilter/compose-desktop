package utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun unixToDateString(unixTime: Long): String {
    // Create an Instant instance from the Unix time
    val instant = Instant.ofEpochSecond(unixTime)
    return DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault()).format(instant)
}