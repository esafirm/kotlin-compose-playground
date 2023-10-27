import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

class Store {
    val simpleState = mutableStateOf("A")
}

fun MutableState<String>.changeState() {
    value = if (value == "A") "B" else "A"
}

val globalStore = Store()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        MaterialTheme {
            Column {
                val store = remember { Store() }
                val localState = remember { mutableStateOf("A") }

                val enableLocal = remember { mutableStateOf(false) }
                val enableLocalStore = remember { mutableStateOf(false) }
                val enableGlobalStore = remember { mutableStateOf(false) }

                Row {
                    Button(
                        onClick = {
                            if (enableLocal.value) {
                                localState.changeState()
                            }
                            if (enableLocalStore.value) {
                                store.simpleState.changeState()
                            }
                            if (enableGlobalStore.value) {
                                globalStore.simpleState.changeState()
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Change State")
                    }

                    Column {
                        SimpleCheckBox(
                            text = "Enable local state",
                            state = enableLocal
                        )
                        SimpleCheckBox(
                            text = "Enable local store state",
                            state = enableLocalStore
                        )
                        SimpleCheckBox(
                            text = "Enable global store state",
                            state = enableGlobalStore
                        )
                    }

                }

                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colors.secondary, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    color = Color.White,
                    text = "Local state: ${localState.value}"
                )

                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    color = Color.White,
                    text = "Local Store state: ${store.simpleState.value}"
                )

                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    color = Color.White,
                    text = "Global Store state: ${globalStore.simpleState.value}"
                )
            }
        }
    }
}

@Composable
fun SimpleCheckBox(
    text: String,
    state: MutableState<Boolean>
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = state.value,
            onCheckedChange = { state.value = state.value.not() }
        )
        Text(text = text)
    }
}
