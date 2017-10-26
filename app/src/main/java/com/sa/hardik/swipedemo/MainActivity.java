package com.sa.hardik.swipedemo;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnTouchListener {
    int clickCount;
    private ViewGroup rlParent;
    private int firPosX, secPosX, firstMarginLeft, secondMarginLeft, firstLimit, secondLimit;
    private List<TextView> ivList;
    private TextView firstTouchedView, secondTouchedView;
    private int fingerCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        ivList = new ArrayList<>();
        rlParent = findViewById(R.id.rlParent);

        Add_Image();
        clickCount = 0;
    }


    private void Add_Image() {
        for (int i = 0; i < 5; i++) {
            final TextView iv = new TextView(this);
            iv.setBackgroundColor(Color.parseColor("#000000"));
            iv.setTextColor(Color.WHITE);
            iv.setTextSize(28f);
            iv.setGravity(Gravity.CENTER);
            iv.setText("" + i);
            iv.setId(i + 1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            if (ivList.size() == 0) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            if (ivList.size() >= 1) {
                layoutParams.addRule(RelativeLayout.RIGHT_OF, ivList.get(i - 1).getId());
            }
            layoutParams.setMargins(40, 0, 0, 0);
            iv.setLayoutParams(layoutParams);
            rlParent.addView(iv, layoutParams);
            iv.setOnTouchListener(this);
            ivList.add(iv);
        }
    }


    public boolean onTouch(final View view, MotionEvent event) {
        final int X = (int) event.getRawX();

        View nextChild = getNextChild(view.getId());
        View prevChild = getPrevChild(view.getId());

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                if (fingerCount <= 2) {
                    if (firstTouchedView == null) {
                        fingerCount = 0;
                        secondTouchedView = null;

                        firstTouchedView = (TextView) view;
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) firstTouchedView.getLayoutParams();
                        firPosX = X - layoutParams.leftMargin;
                        firstLimit = layoutParams.leftMargin;
                        layoutParams.setMargins(firstTouchedView.getLeft(), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
                        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        layoutParams.removeRule(RelativeLayout.RIGHT_OF);

                        firstTouchedView.setLayoutParams(layoutParams);

                    } else {
                        if (nextChild != null && nextChild.getId() == firstTouchedView.getId()) {

                            secondTouchedView = firstTouchedView;

                            firstTouchedView = (TextView) view;
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) firstTouchedView.getLayoutParams();
                            firPosX = X - layoutParams.leftMargin;
                            firstLimit = layoutParams.leftMargin;
                            layoutParams.setMargins(firstTouchedView.getLeft(), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
                            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams.removeRule(RelativeLayout.RIGHT_OF);

                            firstTouchedView.setLayoutParams(layoutParams);

                            printPositions();

                        } else if (prevChild != null && prevChild.getId() == firstTouchedView.getId() && secondTouchedView == null) {

                            secondTouchedView = (TextView) view;
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) secondTouchedView.getLayoutParams();
                            secPosX = X - layoutParams.leftMargin;
                            secondLimit = layoutParams.leftMargin;
                            layoutParams.setMargins(secondTouchedView.getLeft(), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
                            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams.removeRule(RelativeLayout.RIGHT_OF);

                            secondTouchedView.setLayoutParams(layoutParams);

                            printPositions();

                        } else if (secondTouchedView != null && firstTouchedView == null) {

                            firstTouchedView = secondTouchedView;
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) firstTouchedView.getLayoutParams();
                            firPosX = X - layoutParams.leftMargin;
                            firstLimit = layoutParams.leftMargin;
                            layoutParams.setMargins(firstTouchedView.getLeft(), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
                            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams.removeRule(RelativeLayout.RIGHT_OF);
                            firstTouchedView.setLayoutParams(layoutParams);

                            secondTouchedView = (TextView) view;
                            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) secondTouchedView.getLayoutParams();
                            secPosX = X - layoutParams.leftMargin;
                            secondLimit = layoutParams1.leftMargin;
                            layoutParams1.setMargins(secondTouchedView.getLeft(), layoutParams1.topMargin, layoutParams1.rightMargin, layoutParams1.bottomMargin);
                            layoutParams1.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            layoutParams1.removeRule(RelativeLayout.RIGHT_OF);
                            secondTouchedView.setLayoutParams(layoutParams1);

                            printPositions();
                        } else {
                            secondTouchedView = null;
                        }

                        if (firstTouchedView != null && secondTouchedView != null) {
//                            updateRules();
                        }
                    }
                }
                fingerCount++;
                break;

            case MotionEvent.ACTION_UP:


//                if (lastMove.equals("left")) {
//
//                } else if (lastMove.equals("right")) {
//
//                }
//
//                if (nextChild != null && view.getRight() - nextChild.getLeft() < 150) {
//
//                } else {
//
//                }

                fingerCount--;
                if (fingerCount == 0) {
                    firstTouchedView = null;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:

                if (firstTouchedView != null && secondTouchedView != null && fingerCount == 2) {

                    RelativeLayout.LayoutParams firstChildParams = (RelativeLayout.LayoutParams) firstTouchedView.getLayoutParams();

                    firstChildParams.leftMargin = X - firPosX;
                    firstTouchedView.setLayoutParams(firstChildParams);

//                // Left Restrict
                    if (firstChildParams.leftMargin > firstLimit) {
                        firstChildParams.leftMargin = X - firPosX;
                        firstTouchedView.setLayoutParams(firstChildParams);
                    } else {
                        firstChildParams.leftMargin = firstLimit;
                        firstTouchedView.setLayoutParams(firstChildParams);
                    }


//                if (firstChildParams.leftMargin > firstMarginLeft) {
//
//                    Log.d("ON_TOUCH", "Moving to right!");
//                    firstMarginLeft = firstChildParams.leftMargin;
//                } else {
//                    Log.d("ON_TOUCH", "Moving to left!");
//                    firstMarginLeft = firstChildParams.leftMargin;
//                }


//                RelativeLayout.LayoutParams secondChildParams = (RelativeLayout.LayoutParams) secondTouchedView.getLayoutParams();
//                if (firstChildParams.leftMargin < firstLimit) {
//                    firstChildParams.leftMargin = X - firPosX;
//                    secondTouchedView.setLayoutParams(secondChildParams);
//                } else {
//                    firstChildParams.leftMargin = firstLimit;
//                    secondTouchedView.setLayoutParams(secondChildParams);
//                }
//
//                    if (secondChildParams.leftMargin > secondMarginLeft) {
//                        Log.d("ON_TOUCH", "Moving to right!");
//                        secondMarginLeft = secondChildParams.leftMargin;
//                    } else {
//
//                        secondChildParams.leftMargin = X + secPosX;
//                        secondTouchedView.setLayoutParams(secondChildParams);
//
//                        Log.d("ON_TOUCH", "Moving to left!");
//                        secondMarginLeft = secondChildParams.leftMargin;
//                    }

                }

                break;
        }

        rlParent.invalidate();
        return true;
    }

    private void printPositions() {

        Log.e("firstTouchedView", firstTouchedView.getText().toString());
        Log.e("firstTouchedView", "" + firstTouchedView.getLeft());
        Log.e("firstTouchedView", "" + firstTouchedView.getRight());


        Log.e("secondTouchedView", secondTouchedView.getText().toString());
        Log.e("secondTouchedView", "" + secondTouchedView.getLeft());
        Log.e("secondTouchedView", "" + secondTouchedView.getRight());

    }

    private void updateRules() {

        boolean isFirstViewFound = false;
        ArrayList<TextView> tempLeftList = new ArrayList<>();
        ArrayList<TextView> tempRightList = new ArrayList<>();
        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!isFirstViewFound) {
                tempLeftList.add((TextView) rlParent.getChildAt(i));
                if (rlParent.getChildAt(i).getId() == firstTouchedView.getId()) {
                    isFirstViewFound = true;
                }
            } else {
                tempRightList.add((TextView) rlParent.getChildAt(i));
            }
        }

        for (int i = tempLeftList.size(); i > 1; i--) {
            TextView tempTextView = tempLeftList.get(i);
            RelativeLayout.LayoutParams tempParams = (RelativeLayout.LayoutParams) tempTextView.getLayoutParams();


        }
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

    private int getNextLimit(int id) {
        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (rlParent.getChildAt(i).getId() == id && i > 0) {
                return rlParent.getChildAt(i + 1).getLeft();
            }
        }
        return 10;
    }

    private int getPrevLimit(int id) {
        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (rlParent.getChildAt(i).getId() == id && i > 0) {
                return rlParent.getChildAt(i - 1).getRight();
            }
        }
        return 0;
    }
}