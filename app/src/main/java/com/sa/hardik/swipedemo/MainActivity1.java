package com.sa.hardik.swipedemo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity1 extends Activity implements View.OnTouchListener {
    int clickCount;
    private ViewGroup rlParent;
    private int Position_X, firstMarginLeft, leftLimit, rightLimit;
    private int Position_Y;
    private SparseArray<Boolean> filledChildren;

    @SuppressLint("UseSparseArrays")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        rlParent = findViewById(R.id.rlParent);
        filledChildren = new SparseArray<>();
        Add_Image();
        //new image
//        Button NewImage = (Button)findViewById(R.id.new_image_button);
//        NewImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        clickCount = 0;

    }


    private void Add_Image() {
        for (int i = 0; i < 9; i++) {
            final TextView iv = new TextView(this);

            iv.setTextColor(Color.WHITE);
            iv.setTextSize(28f);
            iv.setGravity(Gravity.CENTER);

            if (i % 3 == 0) {
                iv.setBackgroundColor(Color.parseColor("#000000"));
                iv.setText("" + i);
                filledChildren.put(i, true);
            } else {
                iv.setBackgroundColor(Color.parseColor("#10000000"));
                filledChildren.put(i, false);
            }
            iv.setId(i + 1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
            layoutParams.setMargins((150 * i) + 40, 0, 0, 0);
            iv.setLayoutParams(layoutParams);
            rlParent.addView(iv, layoutParams);
            iv.setOnTouchListener(this);
        }
    }


    public boolean onTouch(final View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        int pointerCount = event.getPointerCount();


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                Position_X = X - layoutParams.leftMargin;
                leftLimit = layoutParams.leftMargin;
                rightLimit = layoutParams.leftMargin + view.getWidth();
//                Position_Y = Y - layoutParams.topMargin;
                break;

            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:


                if (filledChildren.get(((Integer) view.getId()) - 1)) {
                    Log.d("ON_TOUCH", "true");

                    LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) view.getLayoutParams();

                    Params.leftMargin = X - Position_X;
                    view.setLayoutParams(Params);

                    if (Params.leftMargin > leftLimit) {
                        Params.leftMargin = X - Position_X;
                        view.setLayoutParams(Params);
                    } else {
                        Params.leftMargin = leftLimit;
                        view.setLayoutParams(Params);
                    }

                    if (Params.leftMargin > firstMarginLeft) {

                        Log.d("ON_TOUCH", "Moving to right!");
                        firstMarginLeft = Params.leftMargin;
                    } else {
                        Log.d("ON_TOUCH", "Moving to left!");
                        firstMarginLeft = Params.leftMargin;
                    }

//                if ((Params.leftMargin + view.getWidth()) < rightLimit) {
//                    Params.leftMargin = X - Position_X;
//                    view.setLayoutParams(Params);
//                } else {
//                    Params.leftMargin = rightLimit - view.getWidth();
//                    view.setLayoutParams(Params);
//                }

                } else {
                    Log.d("ON_TOUCH", "false");
                }

                break;
        }

// Schedules a repaint for the root Layout.
        rlParent.invalidate();
        return true;
    }

    private View getNextChild(int id) {

        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (rlParent.getChildAt(i).getId() == id && i < childCount - 1) {
                return rlParent.getChildAt(i + 1);
            }
        }
        return null;
    }

    private View getPrevChild(int id) {

        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (rlParent.getChildAt(i).getId() == id && i > 0) {
                return rlParent.getChildAt(i - 1);
            }
        }
        return null;
    }

}