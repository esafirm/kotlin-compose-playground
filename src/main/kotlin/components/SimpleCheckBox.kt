package components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment

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