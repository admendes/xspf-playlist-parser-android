package com.musiq;

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

public class DBHelper(context:Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        private val DATABASE_NAME = "MUZIQX.db"
        private val DATABASE_VER = 1

        private val TABLE_NAME = "Music"
        private val COL_ID = "Id"
        private val COL_NAME = "Name"
        private val COL_EMAIL = "Email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY,$COL_NAME TEXT,$COL_EMAIL TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db!!)
    }

    val allArtist: List<Artist>
        get() {
            val listArtists = ArrayList<Artist>()
            val selectQUery = "SELECT * FROM $TABLE_NAME"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQUery, null)
            if (cursor.moveToFirst()){
                do {
                    val artist = Artist()
                    artist.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                    listArtists.add(artist)
                } while (cursor.moveToNext())
            }
            return listArtists
        }

    fun addArtist(artist: Artist)
    {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, artist.name)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    /**
    fun updateArtist(artist: Artist): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, artist.name)
        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(artist.id.toString()))
    }

    fun deleteArtist(artist: Artist) {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(artist.id.toString()))
        db.close()
    }
    **/
}