package android.reposearchapp.viewmodel

import android.content.ContentValues.TAG
import android.reposearchapp.model.GitHubResponse
import android.reposearchapp.model.Repo
import android.reposearchapp.retrofit.RepoApi
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed interface RepoDetailsState {
    data object Loading: RepoDetailsState
    data object Failed: RepoDetailsState
    data class Success(var response: GitHubResponse?): RepoDetailsState
}

class RepoDetailsViewModel : ViewModel() {
    private var currentRepoResponse = GitHubResponse()
    var currentRepo = Repo()
    var repoDetailsState: RepoDetailsState by mutableStateOf(RepoDetailsState.Loading)

    fun getRepo(fullName: String) {
        viewModelScope.launch {
            repoDetailsState =try {
                currentRepoResponse = RepoApi.repoRetrofitService.getRepo("repo:$fullName")
                currentRepo = currentRepoResponse.items[0]
                RepoDetailsState.Success(currentRepoResponse)
            } catch (e: Exception) {
                Log.d(TAG, "Getting repository failed: $e")
                RepoDetailsState.Failed
            }
        }
    }

    fun prettifyDateAndTime(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss")
        val inputDate = LocalDateTime.parse(dateString, inputFormatter)
        return inputDate.format(outputFormatter)
    }
}