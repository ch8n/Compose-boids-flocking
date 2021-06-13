import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

fun main() {
    Preview {
        val scene = remember { Scene() }
        scene.setupScene()
        val frameState = StepFrame {
            scene.update()
        }
        scene.render(frameState)
    }
}

class Scene {

    var sceneEntity = mutableStateListOf<SceneEntity>()

    var boids = mutableListOf<Boid>()

    fun setupScene() {
        sceneEntity.clear()
        repeat( 1) {
            boids.add(Boid(it))
        }
        sceneEntity.addAll(boids)
    }


    fun update() {
        for (entity in sceneEntity) {
            entity.update(this)
        }
    }

    @Composable
    fun render(frameState: State<Long>) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
            ) {
                val step = frameState.value

                for (boid in boids) {
                    if (boid.isConfigured) {
                        boid.applyAlignment(boids)
                    }
                    drawBoid(boid)
                }
            }
        }

    }
}

fun Boolean.onFalse(action: () -> Unit) {
    if (!this) {
        action.invoke()
    }
}

fun Boolean.onTrue(action: () -> Unit) {
    if (this) {
        action.invoke()
    }
}

@Composable
fun StepFrame(callback: () -> Unit): State<Long> {
    val millis = remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        val startTime = withFrameMillis { it }
        while (true) {
            withFrameMillis { frameTime ->
                millis.value = frameTime - startTime
            }
            callback.invoke()
        }
    }
    return millis
}
