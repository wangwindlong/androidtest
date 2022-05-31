package net.wangyl.test.ipc

import android.os.IBinder
import android.os.IInterface


interface PersonManager : IInterface {
    fun addUser(person: Person)
    fun getPersonList() : List<Person>
}