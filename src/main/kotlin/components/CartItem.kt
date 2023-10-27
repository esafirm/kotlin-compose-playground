package components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartItem(
    itemName: String,
    quantity: Int,
    increaseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit,
) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(itemName, modifier = Modifier.weight(1F))

        Button(onClick = decreaseQuantity) {
            Text("-")
        }

        Text(quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))

        Button(onClick = increaseQuantity) {
            Text("+")
        }
    }
}

@Composable
fun ManagedCartItem(
    itemName: String
) {
    val (quantity, setQuantity) = remember { mutableStateOf(0) }
    val decreaseQuantity = { setQuantity(quantity - 1) }
    val increaseQuantity = { setQuantity(quantity + 1) }

    Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        Text(itemName, modifier = Modifier.weight(1F))

        Button(onClick = decreaseQuantity) {
            Text("-")
        }

        Text(quantity.toString(), modifier = Modifier.padding(horizontal = 16.dp))

        Button(onClick = increaseQuantity) {
            Text("+")
        }
    }
}
