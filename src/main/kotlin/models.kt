import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

sealed class SceneEntity {
    abstract fun update(scene: Scene)
}

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
        position = vector(randomFloat(canvasWidth, randomSign = true), randomFloat(canvasHeight, randomSign = true))
        //position = vector(canvasWidth / 2, canvasHeight / 2)
        velocity = vectorRandom2D()
        velocity.setMagnitude(randomFloat(2f, 4f, randomSign = true))
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
        val perceptionRadius = 50
        val maxForce = 0.2f
        val maxSpeed = 4f
        val steeringForceVector = vector()
        var averageCounter = 0
        boids.filter { it.id != id }.forEach { boid ->
            val (currX, curry) = this.position
            val (boidX, boidY) = boid.position
            val distance = distance(currX, boidX, curry, boidY)
            if (distance < perceptionRadius) {
                steeringForceVector + boid.velocity
                ++averageCounter
            }
        }
        if (averageCounter > 0) {
            steeringForceVector.div(averageCounter.toFloat())
            steeringForceVector.setMagnitude(maxSpeed)
            steeringForceVector.minus(this.velocity)
            steeringForceVector.setMagnitude(maxForce)
        }
        return steeringForceVector
    }

    private fun cohesion(boids: List<Boid>): Vector {
        val perceptionRadius = 50
        val maxForce = 0.2f
        val maxSpeed = 4f
        val steeringPositionVector = vector()
        var averageCounter = 0
        boids.filter { it.id != id }.forEach { boid ->
            val (currX, curry) = this.position
            val (boidX, boidY) = boid.position
            val distance = distance(currX, boidX, curry, boidY)
            if (distance < perceptionRadius) {
                steeringPositionVector + boid.position
                ++averageCounter
            }
        }
        if (averageCounter > 0) {
            steeringPositionVector.div(averageCounter.toFloat())
            steeringPositionVector.minus(this.position)
            steeringPositionVector.setMagnitude(maxSpeed)
            steeringPositionVector.minus(this.velocity)
            steeringPositionVector.setMagnitude(maxForce)
        }
        return steeringPositionVector
    }

    private fun seperation(boids: List<Boid>): Vector {
        val seperationVector = vector()
        var averageCounter = 0
        boids.filter { it.id != id }.forEach { boid ->
            val (currX, curry) = this.position
            val (boidX, boidY) = boid.position
            val distance = distance(currX, boidX, curry, boidY)
//            if (distance < VISIBLE_DISTANCE) {
//                val difference = this.copy().position
//                difference.minus(boid.position)
//                difference.div(distance)
//                seperationVector + boid.position
//                ++averageCounter
//            }
        }
        if (averageCounter > 0) {
            seperationVector.div(averageCounter.toFloat())
            // seperationVector.limit(MAX_FORCE)
           // seperationVector.setMagnitude(MAX_SPEED)
        }
        return seperationVector
    }

    fun applyNature(boids: List<Boid>) {
        acceleration * 0f
        val steeringForce = steering(boids)
        val steeringPosition = cohesion(boids)
        ////val seperationPosition = seperation(boids)
//        this.acceleration + seperationPosition
        this.acceleration + steeringForce
        this.acceleration + steeringPosition
    }


    fun reset() {
        //_isConfigured = false
        when {
            position.x > canvasWidth -> position.x = 0f
            position.x < 0 -> position.x = canvasWidth
        }
        when {
            position.y > canvasHeight -> position.y = 0f
            position.y < 0 -> position.y = canvasHeight
        }

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