import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

sealed class SceneEntity {
    abstract fun update(scene: Scene)
}

fun randomVelocity() = (2..5).random().toFloat()
fun randomAcceleration() = (1..2).random().toFloat()

const val VISIBLE_DISTANCE = 100
const val MAX_FORCE = 1.1f

data class Boid(
    var id: Int,
    var position: Vector = vector(),
    var velocity: Vector = vector(),
    var acceleration: Vector = vector(),
) : SceneEntity() {

    private var _isConfigured = false
    val isConfigured get() = _isConfigured
    private var canvasWidth: Float = 0f
    private var canvasHeight: Float = 0f

    fun setup(canvasSize: Size) = _isConfigured.onFalse {
        // use this as constructor
        val (height, width) = canvasSize
        canvasHeight = height
        canvasWidth = width
        position = vector(randomFloat(canvasWidth), randomFloat(canvasHeight))
        velocity = vectorRandom2D()
        velocity.setMagnitude(randomFloat(2f, 4f))
        _isConfigured = true
    }

    override fun update(scene: Scene) {
        velocity + acceleration
        position + velocity
        if (position.x < 0 || position.x > canvasWidth || position.y < 0 || position.y > canvasHeight) {
            reset()
        }
    }

    private fun steering(boids: List<Boid>): Vector {
        val steeringForceVector = vector()
        var averageCounter = 0
        boids.filter { it.id != id }.forEach { boid ->
            val (currX, curry) = this.position
            val (boidX, boidY) = boid.position
            val distance = distance(currX, boidX, curry, boidY)
            if (distance < VISIBLE_DISTANCE) {
                steeringForceVector + boid.velocity
                ++averageCounter
            }
        }
        if (averageCounter > 0) {
            steeringForceVector.div(averageCounter.toFloat())
            steeringForceVector.minus(this.velocity)
            steeringForceVector.limit(MAX_FORCE)
        }
        return steeringForceVector
    }

    fun applyAlignment(boids: List<Boid>) {
        val steeringForce = steering(boids)
        this.acceleration = steeringForce
    }

    fun reset() {
        _isConfigured = false
    }


}


fun DrawScope.drawBoid(boid: Boid) {
    boid.setup(size)
    drawCircle(
        color = if (boid.id == 1) Color.Cyan else Color.White,
        radius = 10f,
        center = Offset(boid.position.x, boid.position.y)
    )
}