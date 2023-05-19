

@file:Suppress("unused")
package com.github.neoturak.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

abstract class UpdateQueryBuilder(
        val tableName: String,
        val values: Array<out Pair<String, Any?>>
) {

    private var selectionApplied = false
    private var useNativeSelection = false
    private var selection: String? = null
    private var nativeSelectionArgs: Array<out String>? = null

    @Deprecated("Use whereArgs() instead.", ReplaceWith("whereArgs(select, *args)"))
    fun where(select: String, vararg args: Pair<String, Any>) = whereArgs(select, *args)

    fun whereArgs(select: String, vararg args: Pair<String, Any>): UpdateQueryBuilder {
        if (selectionApplied) {
            throw Exception("Query selection was already applied.")
        }

        selectionApplied = true
        useNativeSelection = false
        val argsMap = args.fold(hashMapOf<String, Any>()) { map, arg ->
            map.put(arg.first, arg.second)
            map
        }
        selection = applyArguments(select, argsMap)
        return this
    }

    @Deprecated("Use whereArgs() instead.", ReplaceWith("whereArgs(select)"))
    fun where(select: String) = whereArgs(select)

    fun whereArgs(select: String): UpdateQueryBuilder {
        if (selectionApplied)
            throw Exception("Query selection was already applied.")

        selectionApplied = true
        useNativeSelection = false
        selection = select
        return this
    }

    fun whereSimple(select: String, vararg args: String): UpdateQueryBuilder {
        if (selectionApplied)
            throw Exception("Query selection was already applied.")

        selectionApplied = true
        useNativeSelection = true
        selection = select
        nativeSelectionArgs = args
        return this
    }

    @Deprecated("Use whereSimple() instead", replaceWith = ReplaceWith("whereSimple(select, *args)"))
    fun whereSupport(select: String, vararg args: String): UpdateQueryBuilder {
        return whereSimple(select, *args)
    }

    fun exec(): Int {
        val finalSelection = if (selectionApplied) selection else null
        val finalSelectionArgs = if (selectionApplied && useNativeSelection) nativeSelectionArgs else null
        return execUpdate(tableName, values.toContentValues(), finalSelection, finalSelectionArgs)
    }

    abstract fun execUpdate(
            table: String,
            values: ContentValues,
            whereClause: String?,
            whereArgs: Array<out String>?
    ): Int

}

class AndroidSdkDatabaseUpdateQueryBuilder(
        private val db: SQLiteDatabase,
        table: String,
        values: Array<out Pair<String, Any?>>
) : UpdateQueryBuilder(table, values) {

    override fun execUpdate(
            table: String,
            values: ContentValues,
            whereClause: String?,
            whereArgs: Array<out String>?
    ) = db.update(table, values, whereClause, whereArgs)

}
