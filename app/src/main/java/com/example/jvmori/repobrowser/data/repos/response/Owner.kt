package com.example.jvmori.repobrowser.data.repos.response


import com.google.gson.annotations.SerializedName

data class Owner(

    @SerializedName("avatar_url")
    var avatarUrl: String,

    @SerializedName("gravatar_id")
    var gravatarId: String,

    @SerializedName("id")
    var id: Int,

    @SerializedName("login")
    var login: String,


    @SerializedName("node_id")
    var nodeId: String,

    @SerializedName("received_events_url")
    var receivedEventsUrl: String,

    @SerializedName("type")
    var type: String,

    @SerializedName("url")
    var url: String
)