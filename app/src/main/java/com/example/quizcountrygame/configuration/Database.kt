package com.example.quizcountrygame.configuration

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.sql.SQLException


class Database(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    private var DB_PATH: String? = null
    private var myContext: Context? = null
    private lateinit var myDataBase: SQLiteDatabase

    companion object {
        var DB_NAME = "quiz_countries.db"
    }

    init {
        DB_PATH = "/data/data/" + context.packageName + "/databases/"
        Log.d("MY_PATH", DB_PATH + "")
        myContext = context
    }


    private fun checkDataBase(): Boolean {
        var checkDb: SQLiteDatabase? = null
        try {
            val myPath: String = DB_PATH + DB_NAME
            checkDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (_: Exception) {
        }
        checkDb?.close()
        return checkDb != null
    }

    @Throws(IOException::class)
    private fun copyDataBase() {
        val myInput = myContext?.assets?.open(DB_NAME)
        val outFileName = DB_PATH + DB_NAME
        try {
            val myOutput: OutputStream = FileOutputStream(outFileName)
            val buffer = ByteArray(6128)
            var length: Int
            if (myInput != null) {
                while (myInput.read(buffer).also { length = it } > 0) {
                    myOutput.write(buffer, 0, length)
                }
            }
            myOutput.flush()
            myOutput.close()
            myInput?.close()
        } catch (e: Exception) {
            throw e
        }
    }

    fun createDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            try {
                copyDataBase()
            } catch (e: IOException) {
                Log.d("ERROR", e.message + "")
            }
        }
    }

    @Throws(SQLException::class)
    fun openDataBase() {
        val myPath = DB_PATH + DB_NAME
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)
        Log.d("Database", "Open successfully db")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS countries" +
                "(" +
                "    id    INT INCREMENT PRIMARY KEY," +
                "    name  varchar not null," +
                "    image varchar not null" +
                ")");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS countries")
        onCreate(db)
    }

    override fun close() {
        myDataBase.close()
        super.close()
    }
}