package be.vanlooverenkoen.riddle.network.interceptor.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Koen Van Looveren
 */
class NetworkRequest(val callId: String,
                     val url: String,
                     val body: String,
                     var packageName: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(callId)
        parcel.writeString(url)
        parcel.writeString(body)
        parcel.writeString(packageName)
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