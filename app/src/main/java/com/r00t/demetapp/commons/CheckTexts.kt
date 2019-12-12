package com.r00t.demetapp.commons

import android.widget.EditText

class CheckTexts {
    companion object {
        fun isEmpty(vararg editTexts: EditText?):Boolean {
            editTexts.forEach { editText ->
                if(editText?.text.toString().trim().equals("")) {
                    return true
                }
            }
            return false
        }
    }
}