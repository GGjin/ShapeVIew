package com.gg.shapeview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ui_load_view.view.*

/**
 * Creator : GG
 * Time    : 2018/12/15
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
class LoadView : LinearLayout {

    private var mShapeView: ShapeView
    private var mShadowView: View

    private var mTranslationDistance = 0f

    private val ANIMATOR_DURATION = 350L

    private var mStopAnimation = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        View.inflate(context, R.layout.ui_load_view, this)
        setBackgroundColor(Color.WHITE)
        mShapeView = shapeView
        mShadowView = shadowView

        mTranslationDistance = dip2sp(80f)


        post {
            startFullAnimation()
        }
    }

    private fun startFullAnimation() {
        if (mStopAnimation)
            return
        val translationAnimation = ObjectAnimator.ofFloat(mShapeView, "translationY", 0f, mTranslationDistance)

        val scaleAnimation = ObjectAnimator.ofFloat(mShadowView, "scaleX", 1f, 0.3f)

        AnimatorSet().apply {
            duration = ANIMATOR_DURATION
            interpolator = AccelerateInterpolator()
            playTogether(translationAnimation, scaleAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mShapeView.exchange()
                    startUpAnimation()
                }
            })
            start()
        }
    }

    private fun startUpAnimation() {
        if (mStopAnimation)
            return
        val translationAnimation = ObjectAnimator.ofFloat(mShapeView, "translationY", mTranslationDistance, 0f)

        val scaleAnimation = ObjectAnimator.ofFloat(mShadowView, "scaleX", 0.3f, 1f)

        AnimatorSet().apply {
            duration = ANIMATOR_DURATION
            interpolator = DecelerateInterpolator()
            playTogether(translationAnimation, scaleAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    startFullAnimation()
                }

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    startRotationAnimation()
                }
            })
            start()
        }
    }

    private fun startRotationAnimation() {
        when (mShapeView.getShape()) {
            ShapeView.Shape.Circle, ShapeView.Shape.Square -> {
                ObjectAnimator.ofFloat(mShapeView, "rotation", 0f, 180f)
            }
            ShapeView.Shape.Triangle -> {
                ObjectAnimator.ofFloat(mShapeView, "rotation", 0f, 120f)
            }
        }.apply {
            duration = ANIMATOR_DURATION
            interpolator = DecelerateInterpolator()
            start()
        }


    }


    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mShapeView.clearAnimation()
            mShadowView.clearAnimation()
            val loadViewParent = parent as ViewGroup
            if (loadViewParent!=null){
                loadViewParent.removeView(this)
                removeAllViews()
            }

            mStopAnimation = true
        }

    }

    private fun dip2sp(dip: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
    }
}