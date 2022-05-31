package net.wangyl.test.ipc

import android.os.IBinder
import android.os.Parcel

//client
class PersonProxy(val remote: IBinder) : PersonManager {

    override fun addUser(person: Person) {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()

        try {
            data.writeInterfaceToken(DESCRIPTOR) //安全验证
            person.writeToParcel(data, 0) //写入数据 person
            remote.transact(TRANSACTION_addUser, data, reply, 0) //传输
            reply.readException() //读取异常信息，对应服务端写入异常信息
        } finally {
            reply.recycle()
            data.recycle()
        }
    }

    override fun getPersonList(): List<Person> {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            data.writeInterfaceToken(DESCRIPTOR)
            remote.transact(TRANSACTION_getPersonList, data, reply, 0)
            reply.readException()
            return reply.createTypedArrayList(Person.CREATOR) as List<Person>
        } finally {
            reply.recycle()
            data.recycle()
        }
    }

    override fun asBinder(): IBinder {
        return remote
    }
}