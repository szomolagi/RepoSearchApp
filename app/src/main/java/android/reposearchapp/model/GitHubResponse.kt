package android.reposearchapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GitHubResponse(
    @JsonProperty("total_count") val totalCount: Int,
    @JsonProperty("items") val items: List<Repo>
) {
    constructor() : this(0, listOf())
}