package android.reposearchapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Owner(
    @JsonProperty("login") val ownerName: String,
    @JsonProperty("avatar_url") val ownerAvatar: String,
    @JsonProperty("html_url") val ownerGithubLink: String
) {
    constructor() : this("", "", "")
}
