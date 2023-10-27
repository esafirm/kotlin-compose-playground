import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        MaterialTheme {
            Column {
                val states = remember {
                    listOf(
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false),
                        mutableStateOf(false)
                    )
                }

                states.forEachIndexed { index, mutableState ->
                    SimpleCheckBox("#${index + 1}", mutableState)
                }
            }
        }
    }
}
