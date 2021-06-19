import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import org.jetbrains.skija.ImageInfo
import java.lang.Math.abs

sealed class SceneEntity {
    abstract fun update(scene: Scene)
}

data class Forces(
    val weightSeparation: Float,
    val weightAlignment: Float,
    val weightCohesion: Float,
    val weightPush: Float
)

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

    //push force
    fun push(playerPosition: Vector): Vector {
        val perceptionRadius = 50
        val pushDistance = vector()
        val (playerX, playerY) = playerPosition
        val (boidX, boidY) = this.position
        val distance = distance(playerX, boidX, playerY, boidY)
        if (distance < perceptionRadius) {
            pushDistance + position
            pushDistance - playerPosition
            pushDistance.setMagnitude(maxSpeed)
            pushDistance - velocity
            pushDistance.limit(maxForce)
        }
        return pushDistance
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

    fun applyNature(boids: List<Boid>, forces: Forces, playerPosition: Vector) {
        val steeringForce = steering(boids)
        val positionForce = cohesion(boids)
        val separationForce = separation(boids)
        val pushForce = push(playerPosition)
        // random weight these forces
        separationForce.times(forces.weightSeparation)
        steeringForce.times(forces.weightAlignment)
        positionForce.times(forces.weightCohesion)
        pushForce.times(forces.weightPush)
        // apply forces
        this.acceleration + separationForce
        this.acceleration + steeringForce
        this.acceleration + positionForce
        this.acceleration + pushForce
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
    val (boidX, boidY) = boid.position
    val offset = Offset(boidX, boidY)
    drawLine(
        color = Color.White,
        start = offset,
        end = offset.copy(y = offset.y + 8f),
        strokeWidth = 3f
    )
}

fun DrawScope.drawPlayer(posVector: Vector) {
    val offset = Offset(posVector.x, posVector.y)
    drawCircle(
        color = Color.White,
        radius = 10f,
        center = offset
    )
}

