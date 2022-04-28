package com.example.numpuz

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBAdapter {

    private val dbName = "dbNumpuz"
    private val dbTable = "Leaderboard"
    private val colNamaPemain = "NamaPemain"
    private val colLevelPermainan = "LevelPermainan"
    private val colLangkahPemain = "LangkahPemain"
    private val dbVersion = 1

    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " +
            dbTable + " ("+ "INTEGER PRIMARY KEY," + colNamaPemain + " TEXT, " +
            colLevelPermainan + " INTEGER, " + colLangkahPemain + " INTEGER);"
    private var db: SQLiteDatabase? = null

    constructor(context: Context){
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    fun allQuery(): Cursor {
        return db!!.rawQuery("select * from " + dbTable, null)
    }

    fun insert(values: ContentValues): Long{
        val ID = db!!.insert(dbTable, "", values)
        return ID
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int{
        val count = db!!.update(dbTable, values, selection, selectionArgs)
        return count
    }

//    fun delete(selection: String, selectionArgs: Array<String>):Int{
//        val count = db!!.delete(dbTable, selection, selectionArgs)
//        return count
//    }

    fun delete():Int{
        val count = db!!.delete(dbTable, null,null)
        return count
    }

    inner class DatabaseHelper : SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CREATE_TABLE_SQL)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + dbTable)
        }

    }
}