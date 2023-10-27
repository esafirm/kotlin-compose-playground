import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

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

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        MaterialTheme {
            Column(modifier = Modifier.padding(16.dp)) {
                val (isUseAnimated, setUseAnimated) = remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isUseAnimated,
                        onCheckedChange = { setUseAnimated(isUseAnimated.not()) }
                    )
                    Text("Use animated visibility")
                }

                Spacer(Modifier.height(16.dp))
                RouterHandler(isUseAnimated)
            }
        }
    }
}

@Composable
fun RouterHandler(isUseAnimatedVisibility: Boolean) {
    val router = remember { Router() }
    CompositionLocalProvider(ActiveRouter provides router) {
        val currentPage = router.current()

        if (isUseAnimatedVisibility) {
            // Because there can be two or more components drawn in the layout, we need Box to make sure
            // there is no alignment or arrangement affect our components
            Box {
                SlideInSlideOut(isVisible = currentPage == Page.FIRST) {
                    FirstScreen()
                }
                SlideInSlideOut(isVisible = currentPage == Page.SECOND) {
                    SecondScreen()
                }
            }
        } else {
            // Just use simple cross fade animation. This *should* ensure that only one component is available/drawn
            // Can we do the same thing with different behavior?
            Crossfade(targetState = currentPage) { page ->
                when (page) {
                    Page.FIRST -> FirstScreen()
                    Page.SECOND -> SecondScreen()
                }
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

@Composable
fun SlideInSlideOut(isVisible: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        isVisible,
        enter = slideIn(
            tween(400, easing = LinearOutSlowInEasing)
        ) { fullSize -> IntOffset(fullSize.width, 0) },
        exit = slideOut(
            tween(300, easing = FastOutSlowInEasing)
        ) { fullSize -> IntOffset(-fullSize.width, 0) }
    ) {
        content()
    }
}
