package com.TBI.Client.Bluff.bubbleactions;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.view.ViewPropertyAnimatorCompatSet;
import androidx.core.view.ViewCompat;

import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.MyDragShadowBuilder;

/**
 * A view that implements an overlay that animates up to 5 circular icons radially
 * around a fixed point
 */
class BubbleActionOverlay extends FrameLayout {

    static final int MAX_ACTIONS = 5;
    private static final String TAG = BubbleActionOverlay.class.getSimpleName();
    private static final float OVERSHOOT_TENSION = 1.5f;
    private static final long BASE_ANIMATION_DURATION = 150;
    private float[] actionStartX = new float[MAX_ACTIONS];
    private float[] actionStartY = new float[MAX_ACTIONS];
    private float[] actionEndX = new float[MAX_ACTIONS];
    private float[] actionEndY = new float[MAX_ACTIONS];
    private Interpolator interpolator;
    private long animationDuration;
    private ClipData dragData;
    private MyDragShadowBuilder dragShadowBuilder;
    private float startActionDistanceFromCenter;
    private float stopActionDistanceFromCenter;
    private float bubbleDimension;
    private RectF contentClipRect;
    private ImageView bubbleActionIndicator;
    private int numActions = 0;
    private ObjectAnimator backgroundAnimator;
    private OnAttachStateChangeListener onAttachStateChangeListener;
    BubbleActionOverlay(Context context) {
        super(context);
        contentClipRect = new RectF();
        dragShadowBuilder = new MyDragShadowBuilder();
        dragData = DragUtils.getClipData();

        LayoutInflater inflater = LayoutInflater.from(context);
        bubbleActionIndicator = (ImageView) inflater.inflate(R.layout.bubble_actions_indicator, this, false);
        bubbleActionIndicator.setAlpha(0f);
        addView(bubbleActionIndicator, -1);

        interpolator = new OvershootInterpolator(OVERSHOOT_TENSION);

        int transparentBackgroundColor = getResources().getColor(R.color.bubble_actions_background_transparent);
        int darkenedBackgroundColor = getResources().getColor(R.color.bubble_actions_background_darkened);
        setBackgroundColor(transparentBackgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundAnimator = ObjectAnimator.ofArgb(this, "backgroundColor", transparentBackgroundColor, darkenedBackgroundColor);
        } else {
            backgroundAnimator = ObjectAnimator.ofObject(this, "backgroundColor", new BackgroundAlphaTypeEvaluator(), transparentBackgroundColor, darkenedBackgroundColor);
        }

        animationDuration = BASE_ANIMATION_DURATION;

        bubbleDimension = (int) getResources().getDimension(R.dimen.bubble_actions_indicator_dimension);
        startActionDistanceFromCenter = getResources().getDimension(R.dimen.bubble_actions_start_distance);
        stopActionDistanceFromCenter = getResources().getDimension(R.dimen.bubble_actions_stop_distance);

        for (int i = 0; i < MAX_ACTIONS; i++) {
            BubbleView itemView = new BubbleView(getContext());
            itemView.setVisibility(INVISIBLE);
            itemView.setAlpha(0f);
            addView(itemView, -1, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (onAttachStateChangeListener != null) {
            onAttachStateChangeListener.onViewAttachedToWindow(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (onAttachStateChangeListener != null) {
            onAttachStateChangeListener.onViewDetachedFromWindow(this);
        }
    }

    public void setOnAttachStateChangeListener(OnAttachStateChangeListener onAttachStateChangeListener) {
        this.onAttachStateChangeListener = onAttachStateChangeListener;
    }

    void setLabelTypeface(Typeface typeface) {
        for (int i = 1; i <= MAX_ACTIONS; i++) {
            BubbleView itemView = (BubbleView) getChildAt(i);
            itemView.textView.setTypeface(typeface);
        }
    }

    void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    void setupOverlay(float originX, float originY, BubbleActions bubbleActions) {
        backgroundAnimator.setDuration(animationDuration);
        numActions = bubbleActions.numActions;
        if (numActions > MAX_ACTIONS) {
            throw new IllegalArgumentException(TAG + ": actions cannot have more than " + MAX_ACTIONS + " actions. ");
        }

        if (bubbleActions.indicator != null) {
            bubbleActionIndicator.setImageDrawable(bubbleActions.indicator);
        } else {
            bubbleActionIndicator.setImageResource(R.drawable.bubble_indicator);
        }

        contentClipRect.set(0, 0, getWidth(), getHeight());
        bubbleActionIndicator.setX(originX - (bubbleActionIndicator.getWidth() / 2.0f));
        bubbleActionIndicator.setY(originY - (bubbleActionIndicator.getHeight() / 2.0f));
        // check if we're too short on any of the sides
        double angleDelta = Math.PI / (numActions);
        float requiredSpace = (float) Math.cos(angleDelta) * (stopActionDistanceFromCenter + bubbleDimension);
        boolean leftOk = contentClipRect.contains(originX - requiredSpace, originY);
        boolean rightOk = contentClipRect.contains(originX + requiredSpace, originY);

        // if this statement is true then we don't have enough space on the sides
        if (!leftOk && !rightOk) {
            throw new IllegalStateException(BubbleActionOverlay.class.toString() + ": view has no space to expand actions.");
        }

        // Kind of tricky logic
        double startingAngle;
        if (rightOk && leftOk) {
            startingAngle = Math.PI + angleDelta;
        } else if (rightOk) {
            startingAngle = -Math.acos((contentClipRect.left - originX) / (stopActionDistanceFromCenter + bubbleDimension));
        } else {
            startingAngle = -Math.acos((contentClipRect.right - originX) / (stopActionDistanceFromCenter + bubbleDimension)) - (numActions - 1) * angleDelta;
        }

        // this looks a little complicated, but it's necessary to maintain the correct z ordering
        // so that the labels do not appear underneath a bubble
        double angle = startingAngle;
        int actionIndex = 0;
        int start = rightOk ? 0 : numActions - 1;
        int end = rightOk ? numActions : -1;
        int delta = rightOk ? 1 : -1;
        for (int i = start; i != end; i += delta) {
            BubbleView bubbleView = (BubbleView) getChildAt(i + 1);

            // Bind action specifics to BubbleView
            Action action = bubbleActions.actions[actionIndex];
            bubbleView.textView.setText(action.actionName);
            bubbleView.imageView.setImageDrawable(action.bubble);
            bubbleView.callback = action.callback;

            // Calculate and set the locations of the BubbleView
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            float halfWidth = bubbleView.getWidth();
            float halfHeight = bubbleView.getHeight();
            float cosAngle = (float) Math.cos(angle);
            float sinAngle = (float) Math.sin(angle);
            actionEndX[i] = originX + stopActionDistanceFromCenter * cosAngle - halfWidth;
            actionEndY[i] = originY + stopActionDistanceFromCenter * sinAngle - halfHeight;
            actionStartX[i] = originX + startActionDistanceFromCenter * cosAngle - halfWidth;
            actionStartY[i] = originY + startActionDistanceFromCenter * sinAngle - halfHeight;
            bubbleView.setX(actionStartX[i]);
            bubbleView.setY(actionStartY[i]);

            angle += angleDelta;
            actionIndex++;
        }

    }

    void startDrag() {
        startDrag(dragData, dragShadowBuilder, null, 0);
    }

    void animateDimBackground() {
        backgroundAnimator.start();

    }

    void animateUndimBackground() {
        backgroundAnimator.reverse();

    }

    void resetBubbleViews() {
        for (int i = 0; i < numActions; i++) {
            ((BubbleView) getChildAt(i + 1)).resetAppearance();
        }
    }

    @SuppressLint("RestrictedApi")
    ViewPropertyAnimatorCompatSet getAnimateSetShow() {
        @SuppressLint("RestrictedApi") ViewPropertyAnimatorCompatSet resultSet = new ViewPropertyAnimatorCompatSet();
        resultSet.play(ViewCompat.animate(bubbleActionIndicator)
                .alpha(1f)
                .setDuration(animationDuration));

        for (int i = 0; i < numActions; i++) {
            final BubbleView child = (BubbleView) getChildAt(i + 1);
            child.setVisibility(VISIBLE);
            resultSet.play(ViewCompat.animate(child)
                    .translationX(actionEndX[i])
                    .translationY(actionEndY[i])
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(animationDuration));
        }

        //Log.d("mn13tag", actionEndX.toString() + "\n" + actionEndY.toString());
        return resultSet;
    }

    @SuppressLint("RestrictedApi")
    ViewPropertyAnimatorCompatSet getAnimateSetHide() {
        @SuppressLint("RestrictedApi") ViewPropertyAnimatorCompatSet resultSet = new ViewPropertyAnimatorCompatSet();
        resultSet.play(ViewCompat.animate(bubbleActionIndicator)
                .alpha(0f)
                .setDuration(animationDuration));

        for (int i = 0; i < numActions; i++) {
            final BubbleView child = (BubbleView) getChildAt(i + 1);
            resultSet.play(ViewCompat.animate(child)
                    .translationX(actionStartX[i])
                    .translationY(actionStartY[i])
                    .alpha(0f)
                    .setInterpolator(null)
                    .setDuration(animationDuration));

        }

        return resultSet;
    }

    /**
     * For back-porting OnAttachStateChangeListener back to api 11
     */
    interface OnAttachStateChangeListener {
        void onViewAttachedToWindow(View view);

        void onViewDetachedFromWindow(View view);
    }

    /**
     * Ripped straight from v21 AOSP. No idea why this is v21+
     */
    static class BackgroundAlphaTypeEvaluator implements TypeEvaluator<Integer> {

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startInt = startValue;
            int startA = (startInt >> 24) & 0xff;
            int startR = (startInt >> 16) & 0xff;
            int startG = (startInt >> 8) & 0xff;
            int startB = startInt & 0xff;

            int endInt = endValue;
            int endA = (endInt >> 24) & 0xff;
            int endR = (endInt >> 16) & 0xff;
            int endG = (endInt >> 8) & 0xff;
            int endB = endInt & 0xff;

            return ((startA + (int) (fraction * (endA - startA))) << 24) |
                    ((startR + (int) (fraction * (endR - startR))) << 16) |
                    ((startG + (int) (fraction * (endG - startG))) << 8) |
                    ((startB + (int) (fraction * (endB - startB))));
        }
    }

}