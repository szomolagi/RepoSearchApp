package android.reposearchapp.composables

import android.reposearchapp.viewmodel.SearchViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun QualifierDialog(
    onQualifiersSelected: () -> Unit,
    onCancel: () -> Unit,
    searchViewModel: SearchViewModel
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card {
            LazyColumn(Modifier.padding(10.dp)) {
                items(searchViewModel.allQualifiers) { qualifier ->
                    QualifierSelectionBlock(qualifier, searchViewModel.selectedQualifiersList)
                }
                item {
                    Row {
                        ElevatedButton(
                            onClick = { onCancel() }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { onQualifiersSelected() }
                        ) {
                            Text("Select")
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun QualifierSelectionBlock(qualifier: String, qualifierList: MutableList<String>) {
    val checkedState = remember { mutableStateOf(false) }
    Row {
        Text(qualifier)
        Spacer(Modifier.weight(1f))
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                if(checkedState.value){
                    qualifierList.add(qualifier)
                } else {
                    qualifierList.remove(qualifier)
                }
            }
        )

    }
}