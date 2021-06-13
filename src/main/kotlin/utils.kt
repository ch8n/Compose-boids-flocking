import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * To support instant preview (replacement for android's @Preview annotation)
 */


object Window {
    val DEBUG = true
    val WIDTH = if (DEBUG) 800 else 1200
    val HEIGHT = if (DEBUG) 800 else 800
    val WIDTH_DP_VALUE = if (DEBUG) 800.dp.value else 1200.dp.value
    val HEIGHT_DP_VALUE = if (DEBUG) 800.dp.value else 800.dp.value
}

fun <T> withRandomSign(action: (sign: Int) -> T): T {
    val sign = if (Random.nextInt() % 2 == 0) 1 else -1
    return action.invoke(sign)
}

fun randomFloat(from: Float, to: Float, randomSign: Boolean = false): Float {
    return withRandomSign { Random.nextDouble(from.toDouble(), to.toDouble()).toFloat() * if (randomSign) it else 1 }
}

fun randomFloat(value: Float = 0f, randomSign: Boolean = false): Float = when (value) {
    0f -> withRandomSign { Random.nextFloat() * if (randomSign) it else 1 }
    else -> withRandomSign { Random.nextDouble(value.toDouble()).toFloat() * if (randomSign) it else 1 }
}

fun distance(x1: Float, x2: Float, y1: Float, y2: Float): Float = sqrt((y2 - y1).pow(2) + (x2 - x1).pow(2))

fun Preview(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Window(
        title = "Compose-Bird-Debug",
        size = IntSize(Window.WIDTH, Window.HEIGHT),
        resizable = false,
        centered = true,
    ) {
        MaterialTheme() {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}


fun main() {
    val vector = Vector(1f, 1f, 1f)
    println(vector.magnitude)
    vector.setMagnitude(10f)
    println(vector)
    println(vector.magnitude)
    val randomVector = vectorRandom2D()
    println(randomVector)
    println(randomVector.magnitude)
}

data class Vector(
    var x: Float,
    var y: Float,
    var z: Float,
) {
    var magnitude: Float = 0.0f
        get() = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
        private set

    fun normalize() {
        if (magnitude != 0f) {
            this / magnitude
        }
    }

    fun limit(maxMagnitude: Float) {
        if (magnitude > maxMagnitude) {
            this / maxMagnitude
        }
    }

    fun setMagnitude(times: Float) {
        normalize()
        this * times
    }


    operator fun plus(that: Vector) {
        this.x += that.x
        this.y += that.y
        this.z += that.z
    }

    operator fun minus(that: Vector) {
        this.x -= that.x
        this.y -= that.y
        this.z -= that.z
    }

    operator fun times(value: Float) {
        this.x *= value
        this.y *= value
        this.z *= value
    }

    operator fun div(value: Float) {
        if (value > 0) {
            this.x /= value
            this.y /= value
            this.z /= value
        }
    }
}


fun vector(x: Float = 0f, y: Float = 0f, z: Float = 0f) = Vector(x, y, z)

fun vectorRandom2D(): Vector {
    val vector = vector(randomFloat(randomSign = true), randomFloat(randomSign = true), 0f)
    vector.normalize()
    vector.normalize()
    return vector
}