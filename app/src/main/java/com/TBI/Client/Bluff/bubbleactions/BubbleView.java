package com.TBI.Client.Bluff.bubbleactions;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.R;

/**
 * Created by sam on 11/2/15.
 */
public class BubbleView extends LinearLayout {

    // In order to prevent clipping, the bubble starts out smaller than the space it's given
    private static final float DESELECTED_SCALE = 0.70f;

    private static final float SELECTED_SCALE = 0.80f;

    private static final int ANIMATION_DURATION = 150;

    Callback callback;
    TextView textView;
    ImageView imageView;
    View view;
    /**
     * OnDragListener for the ImageView. The correct behavior is only to register a drag enter only
     * if we enter the ImageView (otherwise it would still register a drag enter if we touch the
     * TextView).
     */
    OnDragListener dragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            // Gotcha: you need to return true for drag start
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return DragUtils.isDragForMe(event.getClipDescription().getLabel());
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    imageView.setSelected(true);


                   /* if (Look_Fragment.txtlook != null) {
                        Look_Fragment.txtlook.setText(textView.getText().toString().toUpperCase());
                    }*/

                    imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                    ViewCompat.animate(imageView)
                            .translationX(imageView.getScaleX() - 15)
                            .translationY(imageView.getScaleY() - 50)
                            .scaleX(SELECTED_SCALE)
                            .scaleY(SELECTED_SCALE)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(View view) {
                                    super.onAnimationStart(view);
                                    textView.setVisibility(INVISIBLE);
                                    ViewCompat.animate(textView)
                                            .alpha(2f)
                                            .setListener(null)
                                            .setDuration(ANIMATION_DURATION);
                                }
                            })
                            .setDuration(ANIMATION_DURATION);

                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    imageView.setSelected(false);
                  /*  if (Look_Fragment.txtlook != null) {
                        Look_Fragment.txtlook.setText("LOOK");
                    }

                    if (Look_Fragment.imglook != null) {
                        Look_Fragment.imglook.setImageResource(RM.drawable.look_black_striped);
                    }*/

                    imageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    Log.d("mn13sacle", imageView.getScaleX() + "\n" + imageView.getScaleY());
                    ViewCompat.animate(imageView)
                            .translationX(imageView.getScaleX())
                            .translationY(imageView.getScaleY())
                            .scaleX(DESELECTED_SCALE)
                            .scaleY(DESELECTED_SCALE)
                            .setDuration(ANIMATION_DURATION)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(View view) {
                                    super.onAnimationStart(view);
                                    ViewCompat.animate(textView)
                                            .alpha(0f)
                                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(View view) {
                                                    super.onAnimationEnd(view);
                                                    textView.setVisibility(INVISIBLE);
                                                }
                                            })
                                            .setDuration(ANIMATION_DURATION);
                                }
                            });

                    return true;
                case DragEvent.ACTION_DROP:
                  /*  if (Look_Fragment.imglook != null) {
                        Look_Fragment.imglook.setImageDrawable(getResources().getDrawable(RM.drawable.look_black_striped));
                    }

                    if (Look_Fragment.txtlook != null) {
                        Look_Fragment.txtlook.setText("LOOK");
                    }*/

                    callback.doAction();

                    // we return false here so we are notified in the BubbleActionOverlay
                    return true;
            }
            return false;
        }
    };

    public BubbleView(Context context) {
        super(context);

        setOrientation(VERTICAL);
        view = LayoutInflater.from(context).inflate(R.layout.bubble_actions_bubble_item, this, true);
        textView = (TextView) getChildAt(0);
        imageView = (ImageView) getChildAt(1);
        imageView.setOnDragListener(dragListener);
        imageView.setScaleX(DESELECTED_SCALE);
        imageView.setScaleY(DESELECTED_SCALE);
       /* LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(100, 50, 100, 50);
        imageView.setLayoutParams(lp);*/

    }

    void resetAppearance() {
        setVisibility(INVISIBLE);
        imageView.setScaleX(DESELECTED_SCALE);
        imageView.setScaleY(DESELECTED_SCALE);
        imageView.setSelected(false);
        textView.setVisibility(VISIBLE);
    }

}
