package com.willlockwood.edict.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.willlockwood.edict.data.dao.EdictDao
import com.willlockwood.edict.data.model.Edict
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database (
    entities = [Edict::class],
    version = 1
)
abstract class EdictDatabase : RoomDatabase() {

    abstract fun edictDao(): EdictDao

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

        private class EdictDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.edictDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(edictDao: EdictDao) {

        }
    }
}