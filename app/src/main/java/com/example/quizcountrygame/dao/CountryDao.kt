package com.example.quizcountrygame.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.quizcountrygame.model.Country

class CountryDao(private var db: SQLiteDatabase) {
    fun getRandomCountries(numberCountries: Int): List<Country> {
        val list: MutableList<Country> = arrayListOf()
        val queryString = "SELECT * FROM countries ORDER BY RANDOM() LIMIT $numberCountries"
        val cursor: Cursor = db.rawQuery(queryString, null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    list.add(
                        Country(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                        )
                    )
                } while (cursor.moveToNext())
            } else {
                // Handle failure case (optional)
            }
        }

        return list.toList()
    }

    fun getRandomCountriesNames(id: String): List<String> {
        val list: MutableList<String> = arrayListOf()
        val queryString = "SELECT name FROM countries" +
                " WHERE id != $id" +
                " ORDER BY RANDOM() LIMIT 3"
        val cursor: Cursor = db.rawQuery(queryString, null)
        cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0))
                } while (cursor.moveToNext())
            } else {
                // Handle failure case (optional)
            }
        }

        return list.toList()
    }
}