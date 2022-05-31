package net.wangyl.test.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class LocalService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}