package be.vanlooverenkoen.riddle.util

import android.content.pm.PackageManager

/**
 * @author Koen Van Looveren
 */
internal object AppHelper {
    fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}