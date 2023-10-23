import androidx.compose.ui.input.key.Key.Companion.Window
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.example.draganddrop.isShifted
import userInterface.App

fun main() = application {
    Window(title = "lkdfjlafk"
        ,onCloseRequest = ::exitApplication
        , state = WindowState(width=1200.dp, height = 800.dp)
        ,onKeyEvent = {
            println("Key Event,  isShiftPressed = ${it.isShiftPressed}")
            if (it.isShiftPressed) {
                isShifted = true
            }
            false
        }) {

        App()
    }
}
