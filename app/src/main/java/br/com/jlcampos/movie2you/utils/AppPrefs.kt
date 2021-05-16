package br.com.jlcampos.movie2you.utils

import android.content.Context
import android.content.SharedPreferences

class AppPrefs(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.IDENTIFICATION, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun setFavorites(favorites: String?) {
        editor.putString(Constants.MY_FAVORITE, favorites)
        editor.commit()
    }

    fun getFavorites() = prefs.getString(Constants.MY_FAVORITE, "{}")

}