package com.example.jvmori.repobrowser.utils

import androidx.room.TypeConverter
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        @JvmStatic
        var gson = Gson()

        @TypeConverter
        @JvmStatic
        fun stringToReposList(data: String?): List<Repo> {
            if (data == null) {
                mutableListOf<Repo>()
            }

            val listType = object : TypeToken<List<Repo>>() {

            }.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun reposListToString(movie: List<Repo>): String {
            return gson.toJson(movie)
        }
    }
}