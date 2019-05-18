package tech.blur.redline.core

import android.content.SharedPreferences
import com.google.gson.Gson
import org.json.JSONObject
import tech.blur.redline.core.model.User

class PreferencesApi {
    companion object {
        val sharedPreferencesName = "tripple.me.prefsTags"

        enum class PrefNames { USER }

        fun getJwt(prefs: SharedPreferences): String? {
            val gson = Gson()
            val jsonRoot = JSONObject(prefs.getString(Companion.PrefNames.USER.name, null))

            return gson.fromJson(jsonRoot.toString(), User::class.java).id
        }

        fun setUser(prefs: SharedPreferences, user: User) {
            val gson = Gson()
            val json = gson.toJson(user)
            prefs.edit().putString(Companion.PrefNames.USER.name, json).apply()
        }

        fun getUser(prefs: SharedPreferences): User {
            val gson = Gson()
            val jsonRoot = JSONObject(prefs.getString(Companion.PrefNames.USER.name, null))

            return gson.fromJson(jsonRoot.toString(), User::class.java)
        }

        fun delData(prefs: SharedPreferences) {
            prefs.edit()
                .clear()
                .apply()
        }
    }
}