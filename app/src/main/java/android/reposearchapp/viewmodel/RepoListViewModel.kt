package android.reposearchapp.viewmodel

import android.content.ContentValues.TAG
import android.reposearchapp.model.GitHubResponse
import android.reposearchapp.model.Repo
import android.reposearchapp.retrofit.RepoApi
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed interface RepoListState {
    data object Loading: RepoListState
    data object Failed: RepoListState
    data object LoadingMore: RepoListState
    data object FailedLoadingMore: RepoListState
    data object Success: RepoListState
}

class RepoListViewModel: ViewModel() {
    var response = GitHubResponse()
    var responseItems = mutableStateListOf<Repo>()
    var repoListState: RepoListState by mutableStateOf(RepoListState.Loading)
    private var pageCount: Int by mutableIntStateOf(1)

    fun searchRepos(queryString: String) {
        if(pageCount > 1){
            repoListState = RepoListState.LoadingMore
        }
        viewModelScope.launch {
            repoListState = try {
                response = RepoApi.repoRetrofitService.getRepos(queryString, pageCount)
                responseItems.addAll(response.items)
                pageCount++
                RepoListState.Success
            } catch (e: Exception) {
                if(pageCount > 1){
                    Log.d(TAG, "Getting repositories at page $pageCount failed: $e")
                    RepoListState.FailedLoadingMore
                } else {
                    Log.d(TAG, "Getting repositories failed: $e")
                    RepoListState.Failed
                }
            }
        }
    }
}