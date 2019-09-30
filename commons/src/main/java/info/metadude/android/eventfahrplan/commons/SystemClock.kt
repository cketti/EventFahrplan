package info.metadude.android.eventfahrplan.commons

import android.text.format.Time

class SystemClock(

        initializeNow: Boolean = true

) : Clock {

    constructor(milliseconds: Long) : this(false) {
        setToMilliseconds(milliseconds)
    }

    private val time = Time()

    init {
        if (initializeNow) {
            time.setToNow()
        }
    }

    override var year: Int
        get() = time.year
        set(value) {
            time.year = value
        }

    override var month: Int
        get() = time.month
        set(value) {
            time.month = value
        }

    override var monthDay: Int
        get() = time.monthDay
        set(value) {
            time.monthDay = value
        }

    override var hour: Int
        get() = time.hour
        set(value) {
            time.hour = value
        }

    override var minute: Int
        get() = time.minute
        set(value) {
            time.minute = value
        }

    override fun setToNow() {
        time.setToNow()
    }

    override fun setToMilliseconds(milliseconds: Long) {
        time.set(milliseconds)
    }

    override fun toMilliseconds() = time.toMillis(true)

    override fun toFormattedString(pattern: String): String = time.format(pattern)

    override fun minusHours(hours: Int) {
        time.hour -= hours
    }

    override fun minusMinutes(minutes: Int) {
        time.minute -= minutes
    }

    override fun plusSeconds(seconds: Int) {
        time.second += seconds
    }

    override fun normalize(): Long {
        return time.normalize(true)
    }

    override fun isBefore(clock: Clock): Boolean {
        val otherTime = Time()
        otherTime.set(clock.toMilliseconds())
        return time.before(otherTime)
    }

}
