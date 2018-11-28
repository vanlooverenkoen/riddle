package be.vanlooverenkoen.riddle.network.interceptor.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Koen Van Looveren
 */
data class NetworkRequest(val callId: String,
                          val method: String,
                          val baseUrl: String?,
                          val url: String,
                          val body: String,
                          val packageName: String,
                          val headers: Headers) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(Headers::class.java.classLoader) ?: Headers.empty())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(callId)
        parcel.writeString(method)
        parcel.writeString(baseUrl)
        parcel.writeString(url)
        parcel.writeString(body)
        parcel.writeString(packageName)
        parcel.writeParcelable(headers, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NetworkRequest> {
        override fun createFromParcel(parcel: Parcel): NetworkRequest {
            return NetworkRequest(parcel)
        }

        override fun newArray(size: Int): Array<NetworkRequest?> {
            return arrayOfNulls(size)
        }
    }

}