import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

sealed class SceneEntity {
    abstract fun update(scene: Scene)
}

data class Bird(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var radius: Float = 10f
) : SceneEntity() {
    override fun update(scene: Scene) {

    }

}

fun DrawScope.drawBird(bird: Bird){
    val canvasWidth = size.width
    val canvasHeight = size.height

    drawCircle(
        color = Color.White,
        radius = bird.radius,
        center = Offset(canvasWidth/2 - bird.x ,canvasHeight/2 - bird.y)
    )
}