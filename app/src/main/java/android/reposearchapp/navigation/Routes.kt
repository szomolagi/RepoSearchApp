package android.reposearchapp.navigation

sealed class Routes(val route: String) {
    data object Search: Routes("search")
    data object RepoList: Routes("repoList")
    data object RepoDetails: Routes("repoDetails")
}