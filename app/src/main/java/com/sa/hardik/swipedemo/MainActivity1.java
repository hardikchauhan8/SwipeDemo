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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity1 extends Activity implements View.OnTouchListener {
    int clickCount;
    private ViewGroup rlParent;
    private int Position_X, leftLimit1, rightLimit1, leftLimit2, rightLimit2;
    private SparseArray<Boolean> filledChildren;
    private View firstView, secondView;

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

            if (i % 3 == 0 || i == 4) {
                iv.setBackgroundColor(Color.parseColor("#000000"));
                iv.setText("" + i);
                filledChildren.put(i, true);
            } else {
                iv.setBackgroundColor(Color.parseColor("#10000000"));
                filledChildren.put(i, false);
            }
            iv.setId(i + 1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            if (i > 0) {
                int leftMargin = rlParent.getChildAt(i - 1).getLayoutParams().width * i + (40 * (i + 1));
                layoutParams.setMargins(leftMargin, 0, 0, 0);
            } else {
                layoutParams.setMargins(40, 0, 0, 0);
            }
            iv.setLayoutParams(layoutParams);
            rlParent.addView(iv, layoutParams);
            iv.setOnTouchListener(this);
        }
    }


    public boolean onTouch(final View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        View nextChild = getNextChild(view.getId());
        View prevChild = getPrevChild(view.getId());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                if (filledChildren.get(((Integer) view.getId()) - 1)) {
//                    if (firstView == null) {
//                        secondView = null;
//                        firstView = view;
//
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) firstView.getLayoutParams();
//                        Position_X = X - layoutParams.leftMargin;
//                        leftLimit1 = layoutParams.leftMargin;
//                        rightLimit1 = layoutParams.leftMargin + firstView.getWidth();
//
//                    } else {
//                        secondView = view;
//
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) secondView.getLayoutParams();
//                        Position_X = X - layoutParams.leftMargin;
//                        leftLimit2 = layoutParams.leftMargin;
//                        rightLimit2 = layoutParams.leftMargin + secondView.getWidth();
//                    }


                    if (firstView == null) {
                        secondView = null;
                        firstView = view;

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) firstView.getLayoutParams();
                        Position_X = X - layoutParams.leftMargin;
                        leftLimit1 = layoutParams.leftMargin;
                        rightLimit1 = layoutParams.leftMargin + firstView.getWidth();

                    } else {
                        secondView = view;
                        if (nextChild != null && nextChild.getId() == firstView.getId()) {

                            firstView = secondView;
                            RelativeLayout.LayoutParams firstParams = (RelativeLayout.LayoutParams) firstView.getLayoutParams();
                            Position_X = X - firstParams.leftMargin;
                            leftLimit1 = firstParams.leftMargin;
                            rightLimit1 = firstParams.leftMargin + firstView.getWidth();

                            secondView = nextChild;
                            RelativeLayout.LayoutParams secondParams = (RelativeLayout.LayoutParams) secondView.getLayoutParams();
                            Position_X = X - secondParams.leftMargin;
                            leftLimit2 = secondParams.leftMargin;
                            rightLimit2 = secondParams.leftMargin + secondView.getWidth();

                        } else if (prevChild != null && prevChild.getId() == firstView.getId()) {

                            secondView = firstView;
                            RelativeLayout.LayoutParams secondParams = (RelativeLayout.LayoutParams) secondView.getLayoutParams();
                            Position_X = X - secondParams.leftMargin;
                            leftLimit2 = secondParams.leftMargin;
                            rightLimit2 = secondParams.leftMargin + secondView.getWidth();

                            firstView = prevChild;
                            RelativeLayout.LayoutParams firstParams = (RelativeLayout.LayoutParams) firstView.getLayoutParams();
                            Position_X = X - firstParams.leftMargin;
                            leftLimit1 = firstParams.leftMargin;
                            rightLimit1 = firstParams.leftMargin + firstView.getWidth();

                        } else {
                            secondView = null;
                        }
                    }

                }

                break;

            case MotionEvent.ACTION_UP:

                if (filledChildren.get((Integer) view.getId() - 1)) {

                    RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) view.getLayoutParams();

                    View temp = getChild(view.getId(), (Params.leftMargin + (view.getWidth() / 2)));
                    if (temp != null) {
                        updateView((TextView) view, (TextView) temp);
                    }

                    if (firstView != null && view.getId() == firstView.getId()) {
                        Params.leftMargin = leftLimit1;
                        firstView = null;
                    }

                    if (secondView != null && view.getId() == secondView.getId()) {
                        Params.leftMargin = leftLimit2;
                        secondView = null;
                    }
                    view.setLayoutParams(Params);
                }
                break;


            case MotionEvent.ACTION_MOVE:


                if (firstView != null && secondView != null && filledChildren.get(((Integer) view.getId()) - 1)) {

                    if (view.getId() == firstView.getId()) {
                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) firstView.getLayoutParams();

                        layoutParams1.leftMargin = X - Position_X;
                        firstView.setLayoutParams(layoutParams1);

                        if (layoutParams1.leftMargin > leftLimit1) {
                            layoutParams1.leftMargin = X - Position_X;
                            firstView.setLayoutParams(layoutParams1);
                        } else {
                            layoutParams1.leftMargin = leftLimit1;
                            firstView.setLayoutParams(layoutParams1);
                        }
                    }


                    if (view.getId() == secondView.getId()) {
                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) secondView.getLayoutParams();

                        layoutParams2.leftMargin = X - Position_X;
                        secondView.setLayoutParams(layoutParams2);

                        if ((layoutParams2.leftMargin + view.getWidth()) < rightLimit2) {
                            layoutParams2.leftMargin = X - Position_X;
                            secondView.setLayoutParams(layoutParams2);
                        } else {
                            layoutParams2.leftMargin = rightLimit2 - view.getWidth();
                            secondView.setLayoutParams(layoutParams2);
                        }
                    }


//                    Log.d("leftMargin", "" + layoutParams1.leftMargin);
//                    Log.d("firstMarginLeft", "" + firstMarginLeft);

//                    if (layoutParams1.leftMargin > firstMarginLeft) {
//
//                        Log.d("ON_TOUCH", "Moving to right!");
//                        firstMarginLeft = layoutParams1.leftMargin;
//                    } else {
//                        Log.d("ON_TOUCH", "Moving to left!");
//                        firstMarginLeft = layoutParams1.leftMargin;
//                    }


                } else {
                    Log.d("ON_TOUCH", "false");
                }

                break;
        }

// Schedules a repaint for the root Layout.
        rlParent.invalidate();
        return true;
    }

    @SuppressLint("ResourceType")
    private void updateView(TextView srcView, TextView destView) {
        destView.setText(srcView.getText().toString());
        destView.setBackgroundColor(Color.parseColor("#000000"));
        filledChildren.put(destView.getId() - 1, true);

        srcView.setText("");
        srcView.setBackgroundColor(Color.parseColor("#10000000"));
        filledChildren.put(srcView.getId() - 1, false);
    }

    private View getChild(int id, float leftMargin) {

        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View temp = rlParent.getChildAt(i);
            if (!filledChildren.get(i) && temp.getLeft() < leftMargin && temp.getRight() > leftMargin && id != temp.getId()) {
                return temp;
            }
        }
        return null;
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