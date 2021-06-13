import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.Velocity

sealed class SceneEntity {
    abstract fun update(scene: Scene)
}

fun randomVelocity() = (2..5).random().toFloat()
fun randomAcceleration() = (1..2).random().toFloat()

data class Boid(
    var position: Vector = vector(),
    var velocity: Vector = vectorRandom2D(),
    var acceleration: Vector = vector(),
) : SceneEntity() {

    var canvasWidth: Float = 0f
    var canvasHeight: Float = 0f

    override fun update(scene: Scene) {
        position + velocity
        velocity + acceleration

    }

    fun reset() {
        position = vector(canvasWidth / 2, canvasHeight / 2)
        velocity = Vector(0f, 0f, 0f)
        acceleration = Vector(0f, 0f, 0f)
    }


}

fun DrawScope.drawBoid(boid: Boid) {
    val canvasWidth = size.width
    val canvasHeight = size.height
    boid.canvasWidth = canvasWidth
    boid.canvasHeight = canvasHeight
    inset(canvasWidth / 2, canvasHeight / 2) {
        drawCircle(
            color = Color.White,
            radius = 10f,
            center = Offset(boid.position.x, boid.position.y)
        )
    }

}