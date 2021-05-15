import androidx.compose.animation.Crossfade
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class Page {
    FIRST,
    SECOND
}

val ActiveRouter = compositionLocalOf<Router> { error("No active router provided") }

class Router {
    private val currentPage = mutableStateOf(Page.FIRST)

    fun current() = currentPage.value

    fun push(page: Page) {
        currentPage.value = page
    }
}

fun main() = Window {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            RouterHandler()
        }
    }
}

@Composable
fun RouterHandler() {
    val router = remember { Router() }
    CompositionLocalProvider(ActiveRouter provides router) {
        val currentPage = router.current()
        Crossfade(targetState = currentPage) { page ->
            when (page) {
                Page.FIRST -> FirstScreen()
                Page.SECOND -> SecondScreen()
            }
        }
    }
}

@Composable
fun FirstScreen() {
    val router = ActiveRouter.current
    Column {
        Text(text = "This is the first screen")
        Button(
            onClick = { router.push(Page.SECOND) },
        ) {
            Text(text = "Navigate to second screen")
        }
    }
}

@Composable
fun SecondScreen() {
    val router = ActiveRouter.current
    Column {
        Text(text = "This is the second screen")
        Button(
            onClick = { router.push(Page.FIRST) }
        ) {
            Text("Go Back")
        }
    }
}