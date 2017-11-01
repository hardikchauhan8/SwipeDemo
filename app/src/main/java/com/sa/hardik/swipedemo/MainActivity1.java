package com.sa.hardik.swipedemo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity1 extends Activity implements View.OnTouchListener {
    private ViewGroup rlParent;
    private int firstX;
    private int secondX;
    private int leftLimit1;
    private int rightLimit1;
    private int leftLimit2;
    private SparseArray<Boolean> filledChildren;
    private List<TextView> leftChain, rightChain;
    private TextView leftView, rightView, lastElementLeftChain, lastElementRightChain;

    @SuppressLint("UseSparseArrays")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        rlParent = findViewById(R.id.rlParent);
        filledChildren = new SparseArray<>();
        Add_Image();
        leftChain = new ArrayList<>();
        rightChain = new ArrayList<>();
    }


    // To add dynamic views
    @SuppressLint("ClickableViewAccessibility")
    private void Add_Image() {
        for (int i = 0; i < 8; i++) {
            final TextView iv = new TextView(this);

            iv.setTextColor(Color.WHITE);
            iv.setTextSize(28f);
            iv.setGravity(Gravity.CENTER);

            // To add blank and filled views
            if (i == 2 || i == 3 || i == 4 || i == 5) {
                iv.setBackgroundColor(Color.parseColor("#000000"));
                iv.setText(String.valueOf(i));
                filledChildren.put(i, true);
            } else {
                iv.setBackgroundColor(Color.parseColor("#10000000"));
                filledChildren.put(i, false);
            }
            iv.setId(i + 1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            if (i > 0) {
                int leftMargin = rlParent.getChildAt(i - 1).getLayoutParams().width * i + (60 * (i + 1));
                layoutParams.setMargins(leftMargin, 0, 0, 0);
            } else {
                layoutParams.setMargins(60, 0, 0, 0);
            }
            iv.setLayoutParams(layoutParams);
            rlParent.addView(iv, layoutParams);
            iv.setOnTouchListener(this);
        }
    }

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    public boolean onTouch(final View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int maxWidth = rlParent.getWidth();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                int rightLimit2;
                if (filledChildren.get(view.getId() - 1)) { // To check weather touched view is filled or blank if blank than not allow the touch

                    if (leftView == null) {
                        rightView = null;
                        leftView = (TextView) view;

                        // Assign the first touched view into leftView
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
                        firstX = Math.abs(X - layoutParams.leftMargin);
                        leftLimit1 = layoutParams.leftMargin;
                        rightLimit1 = layoutParams.leftMargin + leftView.getWidth();

                        createLeftChain(leftView);
                    } else {

                        View nextChild = getNextChild(view.getId()); // get the previews view of second touched view
                        View prevChild = getPrevChild(view.getId()); // get the next view of second touched view


                        if (nextChild != null && nextChild.getId() == leftView.getId()) { // To check weather the First touched view is next view of second touched view

                            // assign the leftview to rightview
                            rightView = leftView;
                            RelativeLayout.LayoutParams secondParams = (RelativeLayout.LayoutParams) rightView.getLayoutParams();
                            secondX = Math.abs(X - secondParams.leftMargin);
                            leftLimit2 = secondParams.leftMargin;
                            rightLimit2 = secondParams.leftMargin + rightView.getWidth();
                            createRightChain(rightView);

                            // assign the second touched view to left view
                            leftView = (TextView) view;
                            RelativeLayout.LayoutParams firstParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
                            firstX = Math.abs(X - firstParams.leftMargin);
                            leftLimit1 = firstParams.leftMargin;
                            rightLimit1 = firstParams.leftMargin + leftView.getWidth();
                            createLeftChain(leftView);
                            // Note : This will add the left view to the left finger and right view to the right finger touched

                        } else if (prevChild != null && prevChild.getId() == leftView.getId()) {// To check weather the First touched view is previous view of second touched view

                            // assign second touched view to the right view
                            rightView = (TextView) view;
                            RelativeLayout.LayoutParams secondParams = (RelativeLayout.LayoutParams) rightView.getLayoutParams();
                            secondX = Math.abs(X - secondParams.leftMargin);
                            leftLimit2 = secondParams.leftMargin;
                            rightLimit2 = secondParams.leftMargin + rightView.getWidth();
                            createRightChain(rightView);
                        } else {

                            // if all the cases are wrong or some invalid case,
                            // for example, touched view are not near most neighbours to each other or there is a third touch than it will just invalidate the second touched view to avoid any ambiguty
                            rightView = null;
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_UP:

                if (filledChildren.get(view.getId() - 1)) { // To check weather touched view is filled or blank if blank than not allow the touch

                    final RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) view.getLayoutParams();

                    Observable<Boolean> observable = Observable.create(
                            new ObservableOnSubscribe<Boolean>() {
                                @Override

                                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                    //Use onNext to emit each item in the stream//
                                    e.onNext(leftViewUpEvent(view, Params));
                                    e.onNext(rightViewUpEvent(view, Params));

                                    //Once the Observable has emitted all items in the sequence, call onComplete//
                                    e.onComplete();
                                }
                            }
                    );

                    Observer<Boolean> observer = new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Boolean value) {
                            Log.e("value", "" + value);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    reloadViews();
                                }
                            });
                        }
                    };

                    observable.subscribeOn(Schedulers.newThread());
                    observable.observeOn(AndroidSchedulers.mainThread());
                    observable.subscribe(observer);
                }
                break;


            case MotionEvent.ACTION_MOVE:


                if (leftView != null && rightView != null && filledChildren.get(view.getId() - 1)) { // To check weather left and right view are not null and touched view is filled or blank if blank than not allow the touch

                    if (view.getId() == leftView.getId()) { // For the left view

                        RelativeLayout.LayoutParams leftViewParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
                        RelativeLayout.LayoutParams lastLeftChainElemParam = (RelativeLayout.LayoutParams) lastElementLeftChain.getLayoutParams();

                        leftViewParams.leftMargin = X - firstX;
                        leftView.setLayoutParams(leftViewParams);

                        if (leftViewParams.leftMargin < Math.abs(lastLeftChainElemParam.leftMargin) + 60) {
                            leftViewParams.leftMargin = Math.abs(lastLeftChainElemParam.leftMargin) + 60;
                            leftView.setLayoutParams(leftViewParams);
                        } else if ((leftViewParams.leftMargin + view.getWidth()) < rightLimit1) { // If moving view is left view than restrict that view to move right of its current position
                            leftViewParams.leftMargin = X - firstX;
                            leftView.setLayoutParams(leftViewParams);
                        } else {
                            leftViewParams.leftMargin = rightLimit1 - view.getWidth();
                            leftView.setLayoutParams(leftViewParams);
                        }

                    }


                    if (view.getId() == rightView.getId()) { // For the left view
                        RelativeLayout.LayoutParams rightViewParams = (RelativeLayout.LayoutParams) rightView.getLayoutParams();
                        RelativeLayout.LayoutParams lastRightChainElemParam = (RelativeLayout.LayoutParams) lastElementRightChain.getLayoutParams();

                        rightViewParams.leftMargin = X - secondX;
                        rightView.setLayoutParams(rightViewParams);

                        rightLimit2 = (maxWidth - (rightChain.size() * (lastElementLeftChain.getWidth() + lastRightChainElemParam.leftMargin))) + 60;

                        if (rightViewParams.leftMargin > rightLimit2) {
                            rightViewParams.leftMargin = rightLimit2;
                            rightView.setLayoutParams(rightViewParams);
                        } else if (rightViewParams.leftMargin > leftLimit2) { // If moving view is right view than restrict that view to move left of its current position
                            rightViewParams.leftMargin = X - secondX;
                            rightView.setLayoutParams(rightViewParams);
                        } else {
                            rightViewParams.leftMargin = leftLimit2;
                            rightView.setLayoutParams(rightViewParams);
                        }

                    }

                } else {
                    Log.d("ON_TOUCH", "false");
                }

                break;
        }

        // Schedules a repaint for the root Layout.
        rlParent.invalidate();
        return true;
    }

    private boolean rightViewUpEvent(View view, RelativeLayout.LayoutParams params) {
        if (rightView != null && view.getId() == rightView.getId()) { // If the right view is released than move to the relative position and release that object
            validateRightUpEvent();
            params.leftMargin = leftLimit2;
            rightView = null;
        }
        return true;
    }

    private boolean leftViewUpEvent(View view, RelativeLayout.LayoutParams params) {
        if (leftView != null && view.getId() == leftView.getId()) { // If the Left view is released than move to the relative position and release that object
            validateLeftUpEvent();
            params.leftMargin = leftLimit1;
            leftView = null;
        }

        return true;
    }

    private void reloadViews() {

        SparseArray<OldViewsModel> oldViews = new SparseArray<>();
        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView tempView = (TextView) rlParent.getChildAt(i);
            oldViews.put(i, new OldViewsModel(tempView.getId(), tempView.getText().toString(), !tempView.getText().toString().isEmpty()));
        }
        repopulateViews(oldViews);
    }

    // To add dynamic views
    @SuppressLint("ClickableViewAccessibility")
    private void repopulateViews(final SparseArray<OldViewsModel> oldViews) {

        clearAll();
        for (int i = 0; i < oldViews.size(); i++) {
            final TextView iv = new TextView(MainActivity1.this);

            iv.setTextColor(Color.WHITE);
            iv.setTextSize(28f);
            iv.setGravity(Gravity.CENTER);

            // To add blank and filled views
            if (oldViews.get(i).isFilled()) {
                iv.setBackgroundColor(Color.parseColor("#000000"));
                filledChildren.put(i, true);
            } else {
                iv.setBackgroundColor(Color.parseColor("#10000000"));
                filledChildren.put(i, false);
            }
            iv.setText(oldViews.get(i).getText());
            iv.setId(i + 1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            if (i > 0) {
                int leftMargin = rlParent.getChildAt(i - 1).getLayoutParams().width * i + (60 * (i + 1));
                layoutParams.setMargins(leftMargin, 0, 0, 0);
            } else {
                layoutParams.setMargins(60, 0, 0, 0);
            }
            iv.setLayoutParams(layoutParams);
            rlParent.addView(iv);
            iv.setOnTouchListener(MainActivity1.this);
        }
    }

    private void clearAll() {

        for (int i = 0; i < rlParent.getChildCount(); i++) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlParent.getChildAt(i).getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_TOP);
            params.removeRule(RelativeLayout.ALIGN_LEFT);
            params.removeRule(RelativeLayout.RIGHT_OF);
            rlParent.getChildAt(i).setLayoutParams(params);
        }

        rlParent.removeAllViews();
//        firstX = 0;
//        secondX = 0;
//        leftLimit1 = 0;
//        rightLimit1 = 0;
//        leftLimit2 = 0;
//        filledChildren.clear();
//        leftChain.clear();
//        rightChain.clear();
//        leftView = null;
//        rightView = null;
//        lastElementLeftChain = null;
//        lastElementRightChain = null;
    }

    @SuppressLint("ResourceType")
    private boolean validateLeftUpEvent() {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) leftChain.get(0).getLayoutParams();
        if (params.leftMargin == leftLimit1) {
            return true;
        } else {
            for (int i = leftChain.size() - 1; i >= 0; i--) {
                TextView temp = getChild(leftChain.get(i).getId(), leftChain.get(i).getX());
                if (temp != null && !filledChildren.get(temp.getId() - 1)) {
                    updateView(leftChain.get(i), temp);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("ResourceType")
    private boolean validateRightUpEvent() {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rightChain.get(0).getLayoutParams();
        if (params.leftMargin == rightLimit1) {
            return true;
        } else {
            for (int i = rightChain.size() - 1; i >= 0; i--) {
                TextView temp = getChild(rightChain.get(i).getId(), rightChain.get(i).getX());
                if (temp != null && !filledChildren.get(temp.getId() - 1)) {
                    updateView(rightChain.get(i), temp);
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    @SuppressLint("ResourceType")
    private void createLeftChain(TextView leftView) {

        int id = leftView.getId() - 1;

        if (leftChain != null) {
            leftChain.clear();
        } else {
            leftChain = new ArrayList<>();
        }
        lastElementLeftChain = leftView;
        leftChain.add(leftView);
        while (filledChildren.get(id - 1) != null && filledChildren.get(id - 1)) {
            TextView tempPrev = getPrevChild(id + 1);
            leftChain.add(tempPrev);
            RelativeLayout.LayoutParams tempParams;
            if (tempPrev != null) {

                Log.e("leftView text ----> ", leftView.getText().toString());
                Log.e("tempPrev text ----> ", tempPrev.getText().toString());

                tempParams = (RelativeLayout.LayoutParams) tempPrev.getLayoutParams();
                tempParams.addRule(RelativeLayout.ALIGN_TOP, id + 1);
                tempParams.addRule(RelativeLayout.ALIGN_LEFT, id + 1);
                tempParams.setMargins(-210, 0, 210, 0);
                tempPrev.setLayoutParams(tempParams);
                lastElementLeftChain = tempPrev;
                id = tempPrev.getId() - 1;
            }
        }
    }

    @SuppressLint("ResourceType")
    private void createRightChain(TextView rightView) {

        int id = rightView.getId();
        if (rightChain != null) {
            rightChain.clear();
        } else {
            rightChain = new ArrayList<>();
        }
        lastElementRightChain = rightView;
        rightChain.add(rightView);
        while (filledChildren.get(id) != null && filledChildren.get(id)) {
            TextView tempNext = getNextChild(id);
            RelativeLayout.LayoutParams tempParams;
            if (tempNext != null) {

                Log.e("rightView text ----> ", rightView.getText().toString());
                Log.e("tempNext text ----> ", tempNext.getText().toString());

                tempParams = (RelativeLayout.LayoutParams) tempNext.getLayoutParams();
                tempParams.addRule(RelativeLayout.ALIGN_TOP, id);
                tempParams.addRule(RelativeLayout.RIGHT_OF, id);
                tempParams.setMargins(60, 0, 0, 0);
                tempNext.setLayoutParams(tempParams);
                lastElementRightChain = tempNext;
                rightChain.add(tempNext);
                id = tempNext.getId();
            }
        }
    }

    // Update the style view of srcview and destview
    @SuppressLint("ResourceType")
    private void updateView(TextView srcView, TextView destView) {

        destView.setText(srcView.getText().toString());
        destView.setBackgroundColor(Color.parseColor("#000000"));
        filledChildren.put(destView.getId() - 1, true);

        srcView.setText("");
        srcView.setBackgroundColor(Color.parseColor("#10000000"));
        filledChildren.put(srcView.getId() - 1, false);
    }

    // get the view based on the id and its position
    private TextView getChild(int id, float leftMargin) {

        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView temp = (TextView) rlParent.getChildAt(i);
            if (!filledChildren.get(i) && temp.getLeft() <= leftMargin && temp.getRight() >= leftMargin && id != temp.getId()) {
                return temp;
            }
        }
        return null;
    }

    // get the next view based on the current view id
    private TextView getNextChild(int id) {

        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (rlParent.getChildAt(i).getId() == id && i < childCount - 1) {
                return (TextView) rlParent.getChildAt(i + 1);
            }
        }
        return null;
    }

    // get the previous view based on the current view id
    private TextView getPrevChild(int id) {

        int childCount = rlParent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (rlParent.getChildAt(i).getId() == id && i > 0) {
                return (TextView) rlParent.getChildAt(i - 1);
            }
        }
        return null;
    }


}