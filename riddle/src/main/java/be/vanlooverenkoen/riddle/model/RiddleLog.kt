package be.vanlooverenkoen.riddle.model

import android.os.Parcel
import android.os.Parcelable
import be.vanlooverenkoen.riddle.RiddleLogType
import java.util.Calendar

/**
 * @author Koen Van Looveren
 */
class RiddleLog private constructor(private val type: RiddleLogType,
                                    val tag: String,
                                    val content: String,
                                    val date: Long,
                                    var packageName: String) : Parcelable {

    fun isDebugLog(): Boolean {
        return RiddleLogType.DEBUG == type
    }

    fun isErrorLog(): Boolean {
        return RiddleLogType.ERROR == type
    }

    fun isVerboseLog(): Boolean {
        return RiddleLogType.VERBOSE == type
    }

    fun isInfoLog(): Boolean {
        return RiddleLogType.INFO == type
    }

    fun isWarnLog(): Boolean {
        return RiddleLogType.WARN == type
    }

    fun isAssertLog(): Boolean {
        return RiddleLogType.ASSERT == type
    }
    //regino Parcelabel

    constructor(parcel: Parcel) : this(
            RiddleLogType.valueOf(parcel.readString() ?: RiddleLogType.VERBOSE.toString()),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type.toString())
        parcel.writeString(tag)
        parcel.writeString(content)
        parcel.writeLong(date)
        parcel.writeString(packageName)
    }

    override fun describeContents(): Int {
        return 0
    }
    //endregion

    companion object {

        internal fun d(tag: String?, content: String): RiddleLog {
            return RiddleLog(RiddleLogType.DEBUG, tag ?: "", content, Calendar.getInstance().timeInMillis, "")
        }

        internal fun e(tag: String?, content: String): RiddleLog {
            return RiddleLog(RiddleLogType.ERROR, tag ?: "", content, Calendar.getInstance().timeInMillis, "")
        }

        internal fun i(tag: String?, content: String): RiddleLog {
            return RiddleLog(RiddleLogType.INFO, tag ?: "", content, Calendar.getInstance().timeInMillis, "")
        }

        internal fun w(tag: String?, content: String): RiddleLog {
            return RiddleLog(RiddleLogType.WARN, tag ?: "", content, Calendar.getInstance().timeInMillis, "")
        }

        internal fun a(tag: String?, content: String): RiddleLog {
            return RiddleLog(RiddleLogType.ASSERT, tag ?: "", content, Calendar.getInstance().timeInMillis, "")
        }

        internal fun v(tag: String?, content: String): RiddleLog {
            return RiddleLog(RiddleLogType.VERBOSE, tag ?: "", content, Calendar.getInstance().timeInMillis, "")
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<RiddleLog> {
            override fun createFromParcel(parcel: Parcel): RiddleLog {
                return RiddleLog(parcel)
            }

            override fun newArray(size: Int): Array<RiddleLog?> {
                return arrayOfNulls(size)
            }
        }
    }

}
