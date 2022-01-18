package com.hfad.photomaplast.database

import androidx.room.*

@Dao
interface UserDao {
    @Query ("SELECT * FROM userinfo ORDER BY id DESC")
    fun getAllUserInfo(): List<UserEntity>?

    @Query ("SELECT * FROM userinfo ORDER BY email DESC")

    @Insert
    fun insertUser(user: UserEntity?)

    @Delete
    fun deleteUser(user: UserEntity?)

    @Update
    fun updateUser(user: UserEntity?)
}