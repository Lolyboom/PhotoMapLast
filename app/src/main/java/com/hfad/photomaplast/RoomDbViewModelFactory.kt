package com.hfad.photomaplast

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hfad.photomaplast.database.RoomDb
import com.hfad.photomaplast.database.UserDao
import java.lang.IllegalArgumentException

class RoomDbViewModelFactory(
    private val dao: UserDao,
private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomDb::class.java)) {
            return RoomDbViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}