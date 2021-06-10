import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    var birds = mutableListOf<Bird>()

    fun setupScene() {
        sceneEntity.clear()
        repeat(1) {
            birds.add(Bird())
        }
        sceneEntity.addAll(birds)
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

                for (bird in birds) {
                    drawBird(bird)
                }
            }
        }

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
