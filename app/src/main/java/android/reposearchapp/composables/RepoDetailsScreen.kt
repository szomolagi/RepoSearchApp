package android.reposearchapp.composables

import android.reposearchapp.model.Owner
import android.reposearchapp.model.Repo
import android.reposearchapp.viewmodel.RepoDetailsState
import android.reposearchapp.viewmodel.RepoDetailsViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage

@Composable
fun RepoDetailsScreen(
    ownerName: String,
    repoName: String,
    repoDetailsViewModel: RepoDetailsViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        repoDetailsViewModel.getRepo("$ownerName/$repoName")
    }
    when(repoDetailsViewModel.repoDetailsState) {
        is RepoDetailsState.Loading -> { LoadingProgressIndicator() }
        is RepoDetailsState.Failed -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Something went wrong.")
            }
        }
        is RepoDetailsState.Success -> {
            Surface {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 15.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = repoDetailsViewModel.currentRepo.name,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 15.dp)
                        )
                        Row {
                            Icon(imageVector = Icons.Filled.Star, contentDescription = "Stars", modifier = Modifier.alignByBaseline())
                            Text(text = repoDetailsViewModel.currentRepo.starCount.toString())
                        }
                    }

                    OwnerBlock(owner = repoDetailsViewModel.currentRepo.owner)
                    RepoDetailsBlock(repo = repoDetailsViewModel.currentRepo, repoDetailsViewModel = repoDetailsViewModel)
                }

            }
        }
    }
}

@Composable
fun OwnerBlock(owner: Owner) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
    ) {
        SubcomposeAsyncImage(
            model = owner.ownerAvatar,
            loading = {CircularProgressIndicator()},
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = "Repository owner's avatar"
        )
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
            Row {
                Text(text = "Owner: ", fontWeight = FontWeight.ExtraLight)
                Text(text = owner.ownerName)
            }
            LinkText(
                text = "View owner's profile on GitHub",
                urlText = owner.ownerGithubLink
            )
        }
    }
}

@Composable
fun RepoDetailsBlock(repo: Repo, repoDetailsViewModel: RepoDetailsViewModel) {
    if(!repo.description.isNullOrBlank()) {
        Row {
            Text(text = "Description: ", fontWeight = FontWeight.ExtraLight)
            Text(text = repo.description)
        }
    }

    Row {
        Text(text = "Number of forks: ", fontWeight = FontWeight.ExtraLight)
        Text(text = repo.forkCount.toString())
    }
    Row {
        Text(text = "Created at: ", fontWeight = FontWeight.ExtraLight)
        Text(text = repoDetailsViewModel.prettifyDateAndTime(repo.createdAt))
    }
    Row {
        Text(text = "Last updated at: ", fontWeight = FontWeight.ExtraLight)
        Text(text = repoDetailsViewModel.prettifyDateAndTime(repo.lastUpdateAt))
    }
    LinkText(text = "View repository on GitHub", urlText = repo.repoGithubLink)
}