package com.caltyfarm.caltyfarm.customElements

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class BebasNeueTextView : TextView {
    constructor(context: Context) : super(context) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setFont()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "font/BebasNeue.ttf")
        setTypeface(font, Typeface.NORMAL)
    }
}