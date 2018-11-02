package be.vanlooverenkoen.riddle

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Koen Van Looveren
 */
class RiddleLogTypeTest {

    @Test
    fun testRiddleLogTypeDebug() {
        val type = RiddleLogType.DEBUG
        testRiddleLogType(type)
    }

    @Test
    fun testRiddleLogTypeInfo() {
        val type = RiddleLogType.INFO
        testRiddleLogType(type)
    }

    @Test
    fun testRiddleLogTypeWarn() {
        val type = RiddleLogType.WARN
        testRiddleLogType(type)
    }

    @Test
    fun testRiddleLogTypeVerbose() {
        val type = RiddleLogType.VERBOSE
        testRiddleLogType(type)
    }

    @Test
    fun testRiddleLogTypeError() {
        val type = RiddleLogType.ERROR
        testRiddleLogType(type)
    }

    @Test
    fun testRiddleLogTypeAssert() {
        val type = RiddleLogType.ASSERT
        testRiddleLogType(type)
    }

    private fun testRiddleLogType(type: RiddleLogType) {
        val typeInt = type.code
        val typeString = type.toString()

        val newType = RiddleLogType.getRiddleLogTypeFromCode(typeInt)
        assertEquals(newType, type)
        assertEquals(newType.toString(), typeString)
        assertEquals(newType.code, typeInt)
    }

}