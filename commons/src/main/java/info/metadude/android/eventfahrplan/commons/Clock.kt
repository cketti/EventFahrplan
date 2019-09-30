package info.metadude.android.eventfahrplan.commons

interface Clock {

    var year: Int

    var month: Int

    var monthDay: Int

    var hour: Int

    var minute: Int

    fun setToNow()

    fun setToMilliseconds(milliseconds: Long)

    fun toMilliseconds(): Long

    fun toFormattedString(pattern: String): String

    fun minusHours(hours: Int)

    fun minusMinutes(minutes: Int)

    fun plusSeconds(seconds: Int)

    fun normalize(): Long

    fun isBefore(clock: Clock): Boolean

}
