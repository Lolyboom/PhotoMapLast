package com.hfad.photomaplast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class],version = 1, exportSchema = true)
abstract class RoomDb: RoomDatabase() {

    abstract fun userDao():UserDao?

    companion object {
        @Volatile
        private var INSTANCE: RoomDb?= null

        fun getInstance(context: Context): RoomDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, RoomDb::class.java, "AppDB")
                        .build()
                    INSTANCE = instance

                }
                return instance

            }
        }

        }
    }