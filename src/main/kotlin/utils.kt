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
    val vector = Vector(2f, 2f)
    println(vector.magnitude)
    vector.setMagnitude(10f)
    println(vector)
    println(vector.magnitude)
}

abstract class VectorCollection(
    open var x: Float,
    open var y: Float,
) {

    var magnitude: Float = 0.0f
        get() = sqrt(x.pow(2) + y.pow(2))
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


    operator fun plus(that: VectorCollection) {
        this.x += that.x
        this.y += that.y
    }

    operator fun minus(that: VectorCollection) {
        this.x -= that.x
        this.y -= that.y
    }

    operator fun times(value: Float) {
        this.x *= value
        this.y *= value
    }

    operator fun div(value: Float) {
        this.x /= value
        this.y /= value
    }
}

data class Vector(
    override var x: Float,
    override var y: Float,
) : VectorCollection(x, y)

data class UnitVector(
    override var x: Float,
    override var y: Float,
) : VectorCollection(x, y) {




}

fun randomFloat(from: Float, to: Float) = Random.nextDouble(from.toDouble(), to.toDouble()).toFloat()

fun createRandomUnitVector(uptoX: Float, uptoY: Float) =
    Vector(randomFloat(0f, uptoX), randomFloat(0f, uptoY)).toUnitVector()

fun Vector.toUnitVector(): UnitVector {
    val unit = UnitVector(this.x, this.y)
    unit.setMagnitude(1f)
    return unit
}

fun UnitVector.toVector(): Vector = Vector(this.x, this.y)