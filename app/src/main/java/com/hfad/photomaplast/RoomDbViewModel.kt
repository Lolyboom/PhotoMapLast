package com.hfad.photomaplast

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.hfad.photomaplast.database.UserDao

class RoomDbViewModel(val dao: UserDao, application: Application) : AndroidViewModel(application) {
}