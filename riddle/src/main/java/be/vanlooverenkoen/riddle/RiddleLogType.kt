package be.vanlooverenkoen.riddle

import java.lang.IllegalArgumentException

/**
 * @author Koen Van Looveren
 */
enum class RiddleLogType(val code: Int) {
    DEBUG(0),
    ERROR(1),
    VERBOSE(2),
    INFO(3),
    WARN(4),
    ASSERT(5);

    companion object {
        fun getRiddleLogTypeFromCode(code: Int): RiddleLogType {
            return when (code) {
                DEBUG.code -> DEBUG
                ERROR.code -> ERROR
                VERBOSE.code -> VERBOSE
                INFO.code -> INFO
                WARN.code -> WARN
                ASSERT.code -> ASSERT
                else -> throw IllegalArgumentException("Invalid code")
            }
        }
    }
}