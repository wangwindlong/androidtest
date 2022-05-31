package net.wangyl.test.ipc

import android.os.Parcel
import android.os.Parcelable

//参考 https://blog.csdn.net/weixin_39079048/article/details/105774312
class Person(var name: String? = "", var id: String? = "", var age: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(), parcel.readInt()) {

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(id)
        dest?.writeInt(age)
    }

    override fun toString(): String {
        return "Person(name=$name, id=$id, age=$age)"
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }


}