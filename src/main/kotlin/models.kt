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
    var position: Vector = Vector(0f, 0f),
    var velocity: UnitVector = UnitVector(2f, 2f),
    var acceleration: UnitVector = createRandomUnitVector(12f, 12f),
) : SceneEntity() {

    var canvasWidth = 0f
    var canvasHeight = 0f

    override fun update(scene: Scene) {
        acceleration = UnitVector(randomFloat(100f,200f), randomFloat(100f,200f))
        position + velocity
        velocity + acceleration
        velocity.limit(10f)

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