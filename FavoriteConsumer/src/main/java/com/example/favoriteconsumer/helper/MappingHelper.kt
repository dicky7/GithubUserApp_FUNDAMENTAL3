package com.example.favoriteconsumer.helper

import android.database.Cursor
import com.example.favoriteconsumer.db.DatabaseContract
import com.example.favoriteconsumer.model.Favorite

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite>{
        val favoriteList = ArrayList<Favorite>()
        notesCursor?.apply {
            while (moveToNext()){
                val login_username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COMPANY))
                val loacation = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOCATION))
                val repo = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY))
                val follower = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING))
                val fav = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAVORITE))
                favoriteList.add(Favorite(login_username,avatar_url,company,loacation,repo,follower,following,fav))

            }
        }
        return favoriteList
    }

}