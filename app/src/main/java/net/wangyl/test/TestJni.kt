package net.wangyl.test

class TestJni {

    companion object {

        private external fun nativeInit(): String
        init {
            System.loadLibrary("test")
            println(nativeInit())
        }
    }
}