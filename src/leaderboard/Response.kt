package com.raphaelbussa.leaderboard

import com.google.gson.annotations.SerializedName

data class Welcome (
    @SerializedName("owner_id") val ownerID: String,
    @SerializedName("event") val event: String,
    @SerializedName("members") val members: Map<String, Member>
)

data class Member (
    @SerializedName("local_score") val localScore: Long,
    @SerializedName("global_score") val globalScore: Long,
    @SerializedName("id") val id: String,
    @SerializedName("last_star_ts") val lastStarTs: String,
    @SerializedName("name") val name: String,
    @SerializedName("stars") val stars: Long,
    @SerializedName("completion_day_level") val completionDayLevel: Map<String, Map<String, CompletionDayLevel>>
)

data class CompletionDayLevel(
    @SerializedName("get_star_ts") val getStarTs: String
)
