package be.vanlooverenkoen.riddle.network.interceptor.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Koen Van Looveren
 */
class Headers : Parcelable {
    private val mutableMap = mutableMapOf<String, List<String>>()

    constructor(map: Map<String, List<String>>) {
        mutableMap.putAll(map)
    }

    constructor(parcel: Parcel) {
        val mapSize = parcel.readInt()
        for (i in 0..mapSize) {
            val key = parcel.readString()
            if (key != null) {
                val size = parcel.readInt()
                val list = mutableListOf<String>()
                for (y in 0..size) {
                    val value = parcel.readString()
                    if (value != null) {
                        list.add(value)
                    }
                }
                mutableMap[key] = list
            }
        }
    }

    fun getHeaders(key: String): List<String> {
        return mutableMap[key] ?: emptyList()
    }

    fun getHeaders(): MutableMap<String, List<String>> {
        return mutableMap
    }

    fun hasHeaders(key: String): Boolean {
        return (mutableMap[key] ?: emptyList()).isNotEmpty()
    }

    fun hasHeaders(): Boolean {
        return mutableMap.isNotEmpty()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mutableMap.size)
        mutableMap.forEach { mapItem ->
            parcel.writeString(mapItem.key)
            parcel.writeInt(mapItem.value.size)
            mapItem.value.forEach { parcel.writeString(it) }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Headers> {
        override fun createFromParcel(parcel: Parcel): Headers {
            return Headers(parcel)
        }

        override fun newArray(size: Int): Array<Headers?> {
            return arrayOfNulls(size)
        }

        fun empty(): Headers {
            return Headers(mapOf())
        }
    }
}