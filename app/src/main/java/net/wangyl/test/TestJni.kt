package net.wangyl.test

class TestJni {

    companion object {

        public external fun nativeInit(): String
        public external fun getString(str: String): String
        public external fun doubleInt(count: Int): Long
        init {
            System.loadLibrary("test")
            println(nativeInit())
        }
    }
}