package com.willlockwood.edict.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.willlockwood.edict.data.converter.EdictTypeConverters
import com.willlockwood.edict.data.converter.MapConverters
import com.willlockwood.edict.data.converter.TimeConverters
import com.willlockwood.edict.data.dao.EdictDao
import com.willlockwood.edict.data.dao.EdictSessionDao
import com.willlockwood.edict.data.dao.NewEdictDao
import com.willlockwood.edict.data.dao.NewEdictSessionDao
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.data.model.NewEdictSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database (
    entities = [Edict::class, EdictSession::class, NewEdict::class, NewEdictSession::class],
    version = 24
)
@TypeConverters(value= [TimeConverters::class, MapConverters::class, EdictTypeConverters::class])
abstract class EdictDatabase : RoomDatabase() {

    abstract fun edictDao(): EdictDao
    abstract fun edictSessionDao(): EdictSessionDao
    abstract fun newEdictDao(): NewEdictDao
    abstract fun newEdictSessionDao(): NewEdictSessionDao

    companion object {
        @Volatile
        private var INSTANCE: EdictDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): EdictDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EdictDatabase::class.java,
                    "edict_database")
                    .addCallback(EdictDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class EdictDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.edictDao())
                    }
                }
            }
        }

        fun populateDatabase(edictDao: EdictDao) {

        }
    }
}