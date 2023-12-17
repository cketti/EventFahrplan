package info.metadude.android.eventfahrplan.network.temporal

import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

/**
 * Parser for the duration string used in the schedule.
 */
object DurationParser {
    @JvmStatic
    fun getMinutes(durationString: String): Int {
        val parts = durationString.split(':')
        return when (parts.size) {
            1 -> extractMinutesOnly(minutesString = parts[0])
            2 -> extractHoursAndMinutes(hoursString = parts[0], minutesString = parts[1])
            3 -> extractDaysAndHoursAndMinutes(
                daysString = parts[0],
                hoursString = parts[1],
                minutesString = parts[2]
            )

            else -> error("Unknown duration format: $durationString")
        }
    }

    private fun extractMinutesOnly(minutesString: String): Int {
        return minutesString.toInt()
    }

    private fun extractHoursAndMinutes(hoursString: String, minutesString: String): Int {
        val hours = hoursString.toInt()
        val minutes = minutesString.toInt()

        return hours.hours.inWholeMinutes.toInt() + minutes
    }

    // This format was introduced for the CCCamp 2023 schedule. Hopefully support for it can be removed soon.
    // See https://github.com/EventFahrplan/EventFahrplan/pull/561
    private fun extractDaysAndHoursAndMinutes(daysString: String, hoursString: String, minutesString: String): Int {
        val days = daysString.toInt()

        return days.days.inWholeMinutes.toInt() + extractHoursAndMinutes(hoursString, minutesString)
    }
}
