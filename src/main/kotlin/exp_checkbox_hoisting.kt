import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

fun main() = Window {
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
