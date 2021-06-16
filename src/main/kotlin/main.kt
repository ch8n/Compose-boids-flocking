import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        repeat(100) {
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
        var forces by remember { mutableStateOf(Triple(2.5f, 1.0f, 1.0f)) }

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            Box {

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black),
                ) {
                    val step = frameState.value

                    for (boid in boids) {
                        if (boid.isConfigured) {
                            boid.applyNature(boids, forces)
                        }
                        drawBoid(boid)
                    }
                }

                Column(
                    modifier = Modifier.width(200.dp)
                        .padding(16.dp)
                        .offset(((Window.WIDTH + 100) / 2).dp)
                ) {
                    Text(text = "Alignment \n [${forces.first}]", color = Color.White)
                    Slider(
                        value = forces.first,
                        valueRange = 0f..10f,
                        onValueChange = { forces = forces.copy(first = it) },
                    )

                    Spacer(Modifier.height(18.dp))
                    Text(text = "Cohesion \n [${forces.second}]", color = Color.White)
                    Slider(
                        value = forces.second,
                        valueRange = 0f..10f,
                        onValueChange = { forces = forces.copy(second = it) }
                    )

                    Spacer(Modifier.height(18.dp))
                    Text(text = "Seperation \n [${forces.third}]", color = Color.White)
                    Slider(
                        value = forces.third,
                        valueRange = 0f..10f,
                        onValueChange = { forces = forces.copy(third = it) }
                    )

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
