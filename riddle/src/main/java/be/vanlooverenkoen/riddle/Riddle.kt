package be.vanlooverenkoen.riddle

import android.content.Context
import android.content.Intent
import android.os.Build
import be.vanlooverenkoen.riddle.model.RiddleLog
import be.vanlooverenkoen.riddle.util.AppHelper

/**
 * @author Koen Van Looveren
 */
object Riddle {

    const val ARG_LOG = "RIDDLE_ARG_LOG"
    private const val ACTION = "be.vanlooverenkoen.riddle.app.LOG_DATA"
    private const val PACKAGE = "be.vanlooverenkoen.riddle.app"

    private var listener: ((RiddleLog) -> Unit)? = null

    private val logTypes = mutableListOf(
            RiddleLogType.VERBOSE,
            RiddleLogType.INFO,
            RiddleLogType.DEBUG,
            RiddleLogType.WARN,
            RiddleLogType.ERROR,
            RiddleLogType.ASSERT
    )

    //region Log Functions
    fun d(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.DEBUG))
            listener?.invoke(RiddleLog.d(tag, content))
    }

    fun d(content: String) {
        d(getTag(), content)
    }

    fun e(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.ERROR))
            listener?.invoke(RiddleLog.e(tag, content))
    }

    fun e(content: String) {
        e(getTag(), content)
    }

    fun i(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.INFO))
            listener?.invoke(RiddleLog.i(tag, content))
    }

    fun i(content: String) {
        i(getTag(), content)
    }

    fun w(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.WARN))
            listener?.invoke(RiddleLog.w(tag, content))
    }

    fun w(content: String) {
        w(getTag(), content)
    }

    fun a(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.ASSERT))
            listener?.invoke(RiddleLog.a(tag, content))
    }

    fun a(content: String) {
        a(getTag(), content)
    }

    fun v(tag: String?, content: String) {
        if (logTypes.contains(RiddleLogType.VERBOSE))
            listener?.invoke(RiddleLog.v(tag, content))
    }

    fun v(content: String) {
        v(getTag(), content)
    }
    //endregion

    //region Helper Functions
    private fun getTag(): String {
        val splittedClassName = Thread.currentThread().stackTrace[4].className.split(".")
        return splittedClassName[splittedClassName.size - 1]
    }
    //endregion

    //region Settings
    fun setLogListener(listener: (RiddleLog) -> Unit): Riddle {
        this.listener = listener
        return this
    }

    fun setLogType(vararg logTypes: RiddleLogType): Riddle {
        this.logTypes.clear()
        this.logTypes.addAll(logTypes)
        return this
    }

    fun startNewSession(): Riddle {
        listener?.invoke(RiddleLog.v("NEW_SESSION", "Riddle started a new session"))
        return this
    }
    //endregion

    fun sendLog(context: Context, log: RiddleLog) {
        if (!AppHelper.isPackageInstalled(PACKAGE, context.packageManager)) return
        log.packageName = context.packageName
        val intent = Intent().apply {
            action = Riddle.ACTION
            putExtra(Riddle.ARG_LOG, log)
            setPackage(PACKAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}