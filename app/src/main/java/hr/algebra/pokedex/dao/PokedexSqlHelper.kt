package hr.algebra.pokedex.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.pokedex.model.Pokemon

private const val DB_NAME = "pokedex.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "Pokemon"

private val CREATE = "create table $TABLE_NAME(" +
        "${Pokemon::_id.name} integer primary key autoincrement, " +
        "${Pokemon::name.name} text not null, " +
        "${Pokemon::weight.name} text not null, " +
        "${Pokemon::height.name} text not null, " +
        "${Pokemon::spritePath.name} text not null, " +
        "${Pokemon::abilities.name} text not null, " +
        "${Pokemon::types.name} text not null," +
        "${Pokemon::moves.name} text not null" +
        ")"

private const val DROP = "drop table $TABLE_NAME"

class PokedexSqlHelper(context: Context?)
        : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), PokedexRepository {
        override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL(DROP)
                onCreate(db)
        }

        override fun delete(selection: String?, selectionArgs: Array<String>?)
        = writableDatabase.delete(TABLE_NAME, selection, selectionArgs)

        override fun update(
                values: ContentValues?, selection: String?,
                selectionArgs: Array<String>?
        ) = writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)

        override fun insert(values: ContentValues?)
        = writableDatabase.insert(TABLE_NAME, null, values)

        override fun query(
                projection: Array<String>?, selection: String?,
                selectionArgs: Array<String>?, sortOrder: String?
        ): Cursor = readableDatabase.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder)
}