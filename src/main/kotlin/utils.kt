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
        MaterialTheme(typography = Typography) {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}

val StarWars = FontFamily(
    Font("fonts/starwars.otf", FontWeight.Normal),
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = StarWars,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        color = Color.White
    )
)

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
        this.x /= value
        this.y /= value
        this.z /= value
    }
}


fun vector(x: Float = 0f, y: Float = 0f, z:Float = 0f) = Vector(x, y, z)
fun vectorRandom2D(): Vector {
    val vector = vector(Random.nextFloat(),Random.nextFloat(),0f)
    vector.normalize()
    return vector
}