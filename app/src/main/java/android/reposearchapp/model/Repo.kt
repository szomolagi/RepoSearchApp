package android.reposearchapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repo(
    @JsonProperty("owner") val owner: Owner,
    @JsonProperty("name") val name: String,
    @JsonProperty("full_name") val fullName: String,
    @JsonProperty("description") val description: String?,
    @JsonProperty("html_url") val repoGithubLink: String,
    @JsonProperty("stargazers_count") val starCount: Int,
    @JsonProperty("forks_count") val forkCount: Int,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("updated_at") val lastUpdateAt: String
) {
    constructor() : this(Owner(), "", "", "", "", 0,0, "", "")
}
