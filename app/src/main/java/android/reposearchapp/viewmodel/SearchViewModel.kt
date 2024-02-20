package android.reposearchapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    var searchText: String by mutableStateOf("")
    var selectedQualifiersList = mutableStateListOf<String>()
    val allQualifiers = mutableStateListOf("Name", "Description", "Topics", "README")
    var qualifiersSelected: Boolean by mutableStateOf(false)

    fun constructQueryString() : String {
        var queryString = searchText
        if(!(selectedQualifiersList.isEmpty())) {
            for(qualifier in selectedQualifiersList) {
                queryString += " in:${qualifier.lowercase()}"
            }
        }
        return queryString
    }
}
