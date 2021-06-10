import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp

/**
 * To support instant preview (replacement for android's @Preview annotation)
 */


object Window {
    val DEBUG = true
    val WIDTH = if (DEBUG) 800 else 1200
    val HEIGHT = if (DEBUG) 800 else 800
}


fun Preview(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Window(
        title = "Compose-Bird-Debug",
        size = IntSize(Window.WIDTH, Window.HEIGHT),
        resizable = false,
        centered = true,
    ) {
        MaterialTheme(typography = Typography) {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}

val StarWars = FontFamily(
    Font("fonts/starwars.otf", FontWeight.Normal),
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = StarWars,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        color = Color.White
    )
)
