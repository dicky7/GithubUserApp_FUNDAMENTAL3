package com.example.githubuserapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbfavapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavoriteColumns.USERNAME} TEXT PRIMARY KEY,"+
                "${DatabaseContract.FavoriteColumns.AVATAR} TEXT,"+
                "${DatabaseContract.FavoriteColumns.COMPANY} TEXT,"+
                "${DatabaseContract.FavoriteColumns.LOCATION} TEXT,"+
                "${DatabaseContract.FavoriteColumns.REPOSITORY} INTEGER,"+
                " ${DatabaseContract.FavoriteColumns.FOLLOWERS} INTEGER," +
                " ${DatabaseContract.FavoriteColumns.FOLLOWING} INTEGER," +
                "${DatabaseContract.FavoriteColumns.FAVORITE} TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}