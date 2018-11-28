package be.vanlooverenkoen.riddle.network.interceptor.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Koen Van Looveren
 */
data class NetworkResponse(val callId: String,
                           val method: String,
                           val baseUrl: String?,
                           val url: String,
                           val status: Int,
                           val body: String,
                           val packageName: String,
                           val headers: Headers) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(Headers::class.java.classLoader) ?: Headers.empty())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(callId)
        parcel.writeString(method)
        parcel.writeString(baseUrl)
        parcel.writeString(url)
        parcel.writeInt(status)
        parcel.writeString(body)
        parcel.writeString(packageName)
        parcel.writeParcelable(headers, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NetworkResponse> {
        override fun createFromParcel(parcel: Parcel): NetworkResponse {
            return NetworkResponse(parcel)
        }

        override fun newArray(size: Int): Array<NetworkResponse?> {
            return arrayOfNulls(size)
        }
    }
}