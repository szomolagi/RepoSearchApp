package android.reposearchapp.composables

import android.reposearchapp.R
import android.reposearchapp.navigation.Routes
import android.reposearchapp.viewmodel.SearchViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
){
    var showQualifierDialog by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.github_search_image),
                contentDescription = "Github search image",
                Modifier
                    .size(120.dp)
                    .padding(20.dp)
            )
            Text("Search repositories!", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                value = searchViewModel.searchText,
                onValueChange = {searchViewModel.searchText = it},
                label = { Text(text = "Search among GitHub repositories")}
            )

            ElevatedButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = {
                    searchViewModel.qualifiersSelected = false
                    searchViewModel.selectedQualifiersList.clear()
                    showQualifierDialog = true
                },
            )
            {
                Text("Add qualifiers +", fontSize = 15.sp)
            }
            if (searchViewModel.qualifiersSelected) {
                LazyRow (
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 10.dp)
                ) {
                    items(searchViewModel.selectedQualifiersList) { qualifier ->
                        SelectedQualifierBlock(qualifier)
                    }
                }
            }
            Button(
                onClick = {
                    if (searchViewModel.searchText.isBlank()){
                        Toast.makeText(context, "Please fill search field!", Toast.LENGTH_LONG).show()
                    } else{
                        navController.navigate(Routes.RepoList.route + "/${searchViewModel.constructQueryString()}")
                    }
                },
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth()
            )
            {
                Text("Search", fontSize = 20.sp)
            }
        }

        if(showQualifierDialog) {
            QualifierDialog(
                onQualifiersSelected = {
                    searchViewModel.qualifiersSelected = true
                    showQualifierDialog = false
                },
                onCancel = { showQualifierDialog = false },
                searchViewModel = searchViewModel
            )
        }
    }
}

@Composable
fun SelectedQualifierBlock(qualifier: String) {
    Text(
        text = qualifier,
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 25.dp)
            )
            .padding(10.dp)
    )
}