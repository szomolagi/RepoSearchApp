package android.reposearchapp.retrofit

import android.reposearchapp.model.GitHubResponse
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com/search/"

private val repoRetrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(JacksonConverterFactory.create())
    .build()

object RepoApi {
    val repoRetrofitService: RepoSearchApiService by lazy {
        repoRetrofit.create(RepoSearchApiService::class.java)
    }
}

interface RepoSearchApiService {
    @GET("repositories")
    suspend fun getRepos(@Query("q") q: String, @Query("page") page: Int) : GitHubResponse

    @GET("repositories")
    suspend fun getRepo(@Query("q") q: String) : GitHubResponse
}