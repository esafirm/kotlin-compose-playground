import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.logging.Level
import java.util.logging.Logger

private val logger = Logger.getLogger("Main")

fun main() = Window {
    val (text, setText) = remember { mutableStateOf("") }
    val (items, setItems) = remember { mutableStateOf(listOf<String>()) }
    MaterialTheme {
        Row {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                InputForm(
                    text = text,
                    onValueChange = { input -> setText(input) },
                    onAddItem = { input -> setItems(items + listOf(input)) }
                )
            }

            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                FormContent(items) { selected ->
                    logger.log(Level.INFO, "onItemSelected: $selected")
                    setText(selected)
                }
            }
        }
    }
}

@Composable
fun InputForm(text: String, onValueChange: (String) -> Unit, onAddItem: (String) -> Unit) {
    Column {
        Text("What is your name?")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = text,
            onValueChange = onValueChange
        )
        Button(onClick = {
            onAddItem(text)
        }) {
            Text(text = "Add")
        }
    }
}

@Composable
fun FormContent(items: List<String>, onItemClicked: (String) -> Unit) {
    LazyColumn {
        items(
            count = items.size,
            itemContent = { index ->
                val item = items[index]
                Button(
                    onClick = {
                        onItemClicked(item)
                    },
                    modifier = Modifier.padding(8.dp)
                        .width(200.dp)
                ) {
                    Text(text = item)
                }
            })
    }
}