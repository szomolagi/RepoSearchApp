package android.reposearchapp.composables

import android.reposearchapp.model.Repo
import android.reposearchapp.navigation.Routes
import android.reposearchapp.viewmodel.RepoListState
import android.reposearchapp.viewmodel.RepoListViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun RepoListScreen(
    navController: NavController,
    queryString: String,
    repoListViewModel: RepoListViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        repoListViewModel.searchRepos(queryString)
    }
    when (repoListViewModel.repoListState) {
        is RepoListState.Loading -> { LoadingProgressIndicator() }
        is RepoListState.Failed -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Something went wrong")
            }
        }
        is RepoListState.Success, RepoListState.LoadingMore, RepoListState.FailedLoadingMore -> {
            Surface {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(repoListViewModel.response.totalCount == 0){
                        Text(text = "No repositories were found")
                    } else {
                        Text(
                            text = "Number of results: ${repoListViewModel.response.totalCount}",
                            modifier = Modifier.padding(bottom = 10.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        items(repoListViewModel.responseItems) { repo ->
                            RepoListBlock(repo, navController)
                        }
                        when (repoListViewModel.repoListState) {
                            RepoListState.LoadingMore -> {
                                item {
                                    CircularProgressIndicator()
                                }
                            }
                            RepoListState.Success -> {
                                if(repoListViewModel.responseItems.size < repoListViewModel.response.totalCount) {
                                    item {
                                        TextButton(
                                            onClick = { repoListViewModel.searchRepos(queryString)},
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        ) {
                                            Text(text = "Load more repositories", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }
                            RepoListState.FailedLoadingMore -> {
                                item {
                                    Text(
                                        text = "Something went wrong while loading more repositories.",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            else -> {}
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListBlock(repo: Repo, navController: NavController) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(7.dp),
        onClick = {
            navController.navigate(Routes.RepoDetails.route + "/${repo.owner.ownerName}/${repo.name}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = repo.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            if(!repo.description.isNullOrBlank()){
                Text(text = "Description: ${repo.description}")
            }
            Text(text = "Last updated: ${repo.lastUpdateAt}")
            Row {
                Icon(imageVector = Icons.Filled.Star, contentDescription = "Stars", modifier = Modifier.alignByBaseline())
                Text(text = repo.starCount.toString(), modifier = Modifier.alignByBaseline())
            }
        }
    }
}