package be.vanlooverenkoen.riddle.util

import android.content.pm.PackageManager

/**
 * @author Koen Van Looveren
 */
object AppHelper {
    fun isPackageInstalled(packagename: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packagename, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}