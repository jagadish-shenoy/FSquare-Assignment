package com.abnamroassignment.ui

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.abnamroassignment.R
import kotlinx.android.synthetic.main.layout_details_card.view.*

class DetailsCard:CardView {

    constructor(context: Context) : this(context, null, 0) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        // Set the CardView layoutParams
        val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(0, getDimensionInPixel(4f),0, getDimensionInPixel(4f))

        this.layoutParams = layoutParams
        elevation = getDimensionInPixel(2f).toFloat()
        radius = getDimensionInPixel(2f).toFloat()

        val layoutInflater  = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.layout_details_card, this, true)
    }

    fun setTitle(@StringRes titleResource: Int) {
        setTitle(resources.getString(titleResource))
    }

    fun setTitle(titleText:String) {
        title.text = titleText
    }

    fun setDescription(descriptionText:String) {
        description.text = descriptionText
    }

    private fun getDimensionInPixel(dp:Float):Int
         = TypedValue.applyDimension(
                 TypedValue.COMPLEX_UNIT_DIP,
                 dp,
                 resources.displayMetrics).toInt()
}