package com.example.nativeandroidapp

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var mainActivity: MainActivity;

    @Before
    fun createMainActivity() {
        mainActivity = MainActivity()
    }
    @Test
    fun text_input_works() {
        mainActivity.apply{
            onCreate()
        }
    }
}