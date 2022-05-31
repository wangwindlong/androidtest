package net.wangyl.test.ipc

import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.util.Log

val DESCRIPTOR = "net.wangyl.test.PersonService"
val TRANSACTION_addUser = Binder.FIRST_CALL_TRANSACTION + 0
val TRANSACTION_getPersonList = Binder.FIRST_CALL_TRANSACTION + 1

//server 服务端和客户端读写需对应，顺序相同类型相同
abstract class PersonServer: Binder(), PersonManager {
    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when (code) {
            INTERFACE_TRANSACTION -> {
                reply?.writeString(DESCRIPTOR) //安全验证
                return true
            }
            TRANSACTION_addUser -> {
                data.enforceInterface(DESCRIPTOR)
//                this.addUser(Person.CREATOR.createFromParcel(data))
                this.addUser(Person(data))
                reply?.writeNoException() //写入错误信息
                return true
            }
            TRANSACTION_getPersonList -> {
                data.enforceInterface(DESCRIPTOR)
                val list = this.getPersonList()
                reply?.writeNoException()
                reply?.writeTypedList(list)
                return true
            }
        }
        return super.onTransact(code, data, reply, flags)
    }

    override fun asBinder(): IBinder {
        return this
    }

    companion object {
        @JvmStatic
        fun asInterface(obj: IBinder?): PersonManager? {
            if (obj == null) return null
            val ii = obj.queryLocalInterface(DESCRIPTOR)
            if (ii != null && ii is PersonManager) {
                Log.d("PersonServer", "我是本地的")
                return ii
            }
            return PersonProxy(obj)
        }
    }
}