import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import components.CartItem
import components.ManagedCartItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utils.Rebugger
import kotlin.random.Random

// ################################################
// Architecture and State -- Juara Android 2023
// Esa Firman
// Follow me on LinkedIn: https://nolambda.stream/linkedin
// ################################################

/**
 * The launcher and entry point of this example.
 */
fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Compose for Desktop",
            state = rememberWindowState(width = 600.dp, height = 600.dp)
        ) {
            DerivedStateExample()
        }
    }
}

/**
 * ## Compose Phases
 *
 * Compose have 3 phases:
 *
 *    ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
 *    │              │     │              │     │              │
 *    │ Composition  ├─────►    Layout    ├─────►   Drawing    │
 *    │              │     │              │     │              │
 *    └──────────────┘     └──────────────┘     └──────────────┘
 *
 * - **Composition**: create the UI tree from the composable functions.
 * - **Layout**: calculate the size and position of each element in the UI tree.
 * - **Drawing**: draw the UI tree on the screen.
 *
 */
@Composable
private fun ComposePhases() {
    Row {
        Image(
            painter = painterResource("sample_image.jpg"),
            contentDescription = "sample",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp).clip(shape = RoundedCornerShape(4.dp))
        )

        Column {
            Text("This is a sample image", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            Text(
                text = "And this is the caption text",
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

/**
 * In Compose the UI is **immutable** .There's no way to update it after it's been drawn.
 *
 * What you can control is the state of your UI.
 *
 * Every time the state of the UI changes, Compose recreates the parts of the UI tree that have changed.
 *
 * Composables can accept state and expose events—for example, a TextField accepts a value and exposes a callback onValueChange that requests the callback handler to change the value.
 */
@Composable
private fun ChangingState() {
    val (name, setName) = remember { mutableStateOf("") }

    // Another way to declare readable and writable state is by using delegate property:
    var nameState by remember { mutableStateOf("") }

    OutlinedTextField(
        value = name,
        onValueChange = {
            // Every changes on state will re-trigger the recomposition (re-evaluation of composable function)
            setName(it)
        },
        label = { Text("Name") }
    )
}


/**
 * Composables work based on state and events.
 *
 * For example, a `TextField` is only updated when its value parameter is updated and it exposes
 * an `onValueChange` callback—an event that requests the value to be changed to a new one
 *
 *
 * The UI update loop for an app using unidirectional data flow looks like this:
 *
 * **Event**: Part of the UI generates an event and passes it upward, such as a button click passed to the ViewModel to handle; or an event is passed from other layers of your app, such as indicating that the user session has expired.
 * **Update state**: An event handler might change the state.
 * **Display state**: The state holder passes down the state, and the UI displays it.
 */
@Composable
private fun UDFExample() {
    val (state1, setState1) = rememberSaveable { mutableStateOf("") }

    Column(Modifier.padding(32.dp)) {
        Text("This is the value from state 1: $state1")

        // Instead of referring the text and change the text directly, we change the state1
        Button(onClick = { setState1(Random.nextInt().toString()) }) {
            Text("Change State")
        }
    }
}

/**
 * State hoisting in Compose is a pattern of moving state to a composable's caller to make a composable stateless.
 *
 * The general pattern for state hoisting in Jetpack Compose is to replace the state variable with two parameters:
 *
 * `value: T`: the current value to display
 *
 * `onValueChange: (T) -> Unit`: an event that requests the value to change, where T is the proposed new value
 *
 * In practice, this is similar with inversion of control design pattern in OOP
 *
 * Further ref: https://developer.android.com/jetpack/compose/state-hoisting
 */
@Composable
private fun StateHoistingExample() {

    data class GroceryItem(
        val name: String,
        var quantity: Int,
    )

    val groceries = mutableStateListOf(
        GroceryItem("Apple", 10),
        GroceryItem("Orange", 5),
        GroceryItem("Banana", 3)
    )

    val changeGrocery = { item: GroceryItem, quantity: Int ->
        val index = groceries.indexOf(item)
        groceries.set(index, item.copy(quantity = quantity))
    }

    Row(Modifier.padding(16.dp)) {
        Column(Modifier.weight(0.5F)) {
            Text(text = "Hoisted", fontWeight = FontWeight.W500, fontSize = MaterialTheme.typography.h3.fontSize)
            LazyColumn {
                items(groceries) { item ->
                    CartItem(
                        itemName = item.name,
                        quantity = item.quantity,
                        increaseQuantity = { changeGrocery(item, item.quantity + 1) },
                        decreaseQuantity = { changeGrocery(item, item.quantity - 1) }
                    )
                }
            }
        }

        Column(Modifier.weight(0.5F)) {
            Text("Managed", fontWeight = FontWeight.W500, fontSize = MaterialTheme.typography.h3.fontSize)
            LazyColumn {
                items(groceries) { item ->
                    ManagedCartItem(
                        itemName = item.name
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvanceStateExample() {
    val (showContent, setShowContent) = remember { mutableStateOf(false) }
    val (note, setNote) = remember { mutableStateOf("") }

    val onTimeout = { setShowContent(true) }

    Rebugger(
        trackMap = mapOf(
            "note" to note,
            "showContent" to showContent,
            "onTimeOut" to onTimeout
        ),
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (showContent) {
            AdvanceStateCoroutineScope(note)
        } else {

            Column {
                OutlinedTextField(
                    value = note,
                    onValueChange = setNote,
                    label = { Text("Note") }
                )

                LandingScreen(onTimeout = onTimeout)
            }
        }
    }
}

/**
 * A composable that shows a splash image and will trigger a callback after a delay.
 */
@Composable
private fun LandingScreen(onTimeout: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        // This will always refer to the latest onTimeout function that
        // LandingScreen was recomposed with
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        // Start a side effect to load things in the background and call onTimeout() when finished.
        // Will be recomposed if onTimeout changes.

        LaunchedEffect(Unit) {

            // The lambda will be invoked when `LaunchedEffect` enters tkhe composition
            // and will be cancelled when `LaunchedEffect` leaves the composition

            delay(4_000) // Simulates loading things
            onTimeout()
        }
        Image(
            painter = painterResource("sample_image.jpg"),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * A composable function that contains a scaffold with a drawer.
 *
 * Suspend functions, in addition to being able to run asynchronous code,
 * also help represent concepts that happen over time.
 *
 * As opening the drawer requires some time, movement, and potential animations,
 * that's perfectly reflected with the suspend function,
 * which will suspend the execution of the coroutine where
 * it's been called until it finishes and resumes execution.
 */
@Composable
private fun AdvanceStateCoroutineScope(note: String) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Text("Your note is: $note")
        }
    ) {
        val scope = rememberCoroutineScope()
        Button(onClick = {

            // You cannot use `LaunchedEffect` in here because it's outside the scope of composable function
            // It will throw a compile error

            scope.launch {
                scaffoldState.drawerState.open()
            }
        }) {
            Text("Open Drawer")
        }
    }
}

/**
 * If the state getting to complex, especially in hoisted case, it is a good practice to create
 * a separated plain class to hold manage this state. We call this as a "State Holder"
 *
 * This is also a good example that the state, can be changed from outside compose scope
 */
@Composable
private fun StateHolderExample() {
    Column(Modifier.padding(16.dp)) {
        UserInput(state = UserInputState(hint = "First Name"))
        UserInput(state = UserInputState(hint = "Last Name"))
    }
}

@Composable
private fun UserInput(
    state: UserInputState
) {
    TextField(
        value = state.text,
        onValueChange = state::updateText,
        textStyle = if (state.isHint) {
            MaterialTheme.typography.body2.copy(color = Color.Gray)
        } else {
            MaterialTheme.typography.body2
        },
        modifier = Modifier.onFocusChanged { focusState ->
            if (focusState.hasFocus) {
                if (state.isHint) {
                    state.updateText("")
                }
            } else {
                if (state.text.isEmpty()) {
                    state.setHint()
                }
            }
        }
    )
}

private class UserInputState(
    val hint: String
) {
    private val textState = mutableStateOf(hint)

    val text get() = textState.value

    fun updateText(newText: String) {
        textState.value = newText
    }

    fun setHint() {
        textState.value = hint
    }

    val isHint: Boolean
        get() = text == hint

    companion object {
        // A custom saver for our state holder, saving and restoring the list of items.
        // Usage:
        // val state = rememberSaveable(saver = UserInputState.Saver) { UserInputState(hint = "First Name") }
        val Saver: Saver<UserInputState, *> = listSaver(
            save = { listOf(it) },
            restore = {
                UserInputState(hint = it[0].hint)
            }
        )
    }
}

/**
 * `DisposableEffect` is a composable that will call `onDispose`
 * when the composable leaves the composition.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DisposableEffectExample() {
    val (showOtherScreen, setShowOtherScreen) = remember { mutableStateOf(false) }
    val (data, setData) = remember { mutableStateOf("") }

    if (showOtherScreen) {
        Text("This is the other screen. Data: $data")
    } else {
        // This will be called when the composable is first created
        DisposableEffect(Unit) {
            setData("DisposableEffect created")

            // This will be called when the composable is disposed
            onDispose {
                println("onDispose called")
            }
        }

        Text("This is the initial screen. Click to go to the other screen. Data: $data",
            modifier = Modifier.onClick {
                println("onTimeout called")
                setShowOtherScreen(true)
            })
    }
}

/**
 * Product state is a convenience function that allows you to create a state that is updated by a coroutine.
 *
 * Under the hood, it use `LaunchedEffect` to update the state
 */
@Composable
private fun ProduceStateExample() {
    val state by produceState(initialValue = "Loading...") {
        delay(2000)
        value = "Loaded"
    }

    Box(modifier = Modifier.padding(32.dp)) {
        Text("State: $state")
    }
}

/**
 * `derivedStateOf {}` is used when your state or key is changing more than you want to update your UI.
 *
 * It's like `distinctUntilChanged` in Flow or Rx
 */
@Composable
private fun DerivedStateExample() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val enableButton = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    println("Enable button: ${enableButton.value}")

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(100) {
                Text(
                    text = "Item #$it",
                    fontSize = 22.sp,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                        .fillMaxSize()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                )
            }
        }
        Button(
            onClick = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            enabled = enableButton.value,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text("Scroll to top")
        }
    }
}
