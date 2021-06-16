import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

sealed class SceneEntity {
    abstract fun update(scene: Scene)
}

data class Boid(
    var id: Int
) : SceneEntity() {

    private var acceleration: Vector = vector()
    private var velocity: Vector = vectorRandom2D()
    var position: Vector = vector()
    private val maxSpeed = 3f
    private val maxForce = 0.05f

    private var _isConfigured = false
    private var canvasWidth: Float = 0f
    private var canvasHeight: Float = 0f

    val isConfigured get() = _isConfigured

    fun setup(canvasSize: Size) = _isConfigured.onFalse {
        // use this as constructor
        val (height, width) = canvasSize
        canvasHeight = height
        canvasWidth = width

        position = vector(randomFloat(canvasWidth, randomSign = true), randomFloat(canvasHeight, randomSign = true))
        velocity = vectorRandom2D()
        velocity.setMagnitude(randomFloat(2f, 4f, randomSign = true))
        _isConfigured = true
    }

    override fun update(scene: Scene) {
        velocity + acceleration
        velocity.limit(maxSpeed)
        position + velocity
        acceleration.times(0f)
        if (position.x < 0 || position.x > canvasWidth || position.y < 0 || position.y > canvasHeight) {
            reset()
        }
    }

    // alignment
    private fun steering(boids: List<Boid>): Vector {
        val perceptionRadius = 50
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
            steeringForceVector.limit(maxForce)
        }

        return steeringForceVector
    }

    // cohesion
    private fun cohesion(boids: List<Boid>): Vector {
        val perceptionRadius = 50
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
            steeringPositionVector.limit(maxForce)
        }

        return steeringPositionVector
    }

    private fun separation(boids: List<Boid>): Vector {
        val perceptionRadius = 50
        val separationVector = vector()
        var averageCounter = 0

        boids.filter { it.id != id }.forEach { boid ->
            val (currX, curry) = this.position
            val (boidX, boidY) = boid.position
            val distance = distance(currX, boidX, curry, boidY)
            if (distance < perceptionRadius) {
                val difference = this.position.copy()
                difference.minus(boid.position)
                difference.normalize()
                difference.div(distance)
                separationVector + difference
                ++averageCounter
            }
        }

        if (averageCounter > 0) {
            separationVector.div(averageCounter.toFloat())
        }

        if (separationVector.magnitude > 0) {
            separationVector.setMagnitude(maxSpeed)
            separationVector.minus(velocity)
            separationVector.setMagnitude(maxForce)
        }

        return separationVector
    }

    fun applyNature(boids: List<Boid>, forces: Triple<Float, Float, Float>) {
        val steeringForce = steering(boids)
        val positionForce = cohesion(boids)
        val separationForce = separation(boids)
        // random weight these forces
        val (weightSep, weightAlig, weightCohision) = forces
        separationForce.times(weightSep)
        steeringForce.times(weightAlig)
        positionForce.times(weightCohision)
        // apply forces
        this.acceleration + separationForce
        this.acceleration + steeringForce
        this.acceleration + positionForce
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