package com.example.newsdaily

import android.content.Context
import android.content.SharedPreferences

class IntroManager( context: Context) {

    private var sharedPreferences : SharedPreferences = context.getSharedPreferences("first",0)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setFirst(isFirst : Boolean)
    {
        editor.putBoolean("check",isFirst)
        editor.commit()
    }

    fun check() : Boolean
    {
        return sharedPreferences.getBoolean("check",true)
    }
}