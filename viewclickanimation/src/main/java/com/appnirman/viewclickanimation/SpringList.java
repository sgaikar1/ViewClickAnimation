package com.appnirman.viewclickanimation;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

public class SpringList {
    private final Spring spring;
    private final SheetLayoutCustom mSheetLayout;
    private final boolean ramSizeCheck;
    Context context;
    View view;

    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
    private SpringCompleteListsener springCompleteListener;
    private boolean useFloatSheetAnim = true;
    private boolean useSpinAnim = true;

    public void useSpringAnim(boolean useSpinAnim) {
        this.useSpinAnim = useSpinAnim;
    }

    public void useFloatingSheetAnim(boolean useFloatSheetAnim) {
        this.useFloatSheetAnim = useFloatSheetAnim;
    }

    public void setView(View view) {
        this.view = view;
    }


    public static class Builder {
        Context context;
        View view;
        private SheetLayoutCustom mSheetLayout;
        private boolean ramSizeCheck = true;
        private SpringCompleteListsener springCompleteListener;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }


        public Builder setSheet(SheetLayoutCustom mSheetLayout) {
            this.mSheetLayout = mSheetLayout;
            return this;
        }


        public Builder registerSpringListener(SpringCompleteListsener springCompleteListsener) {
            this.springCompleteListener = springCompleteListsener;
            return this;
        }

        public SpringList build() {
            return new SpringList(this);
        }


        public Builder checkRam(boolean check) {
            this.ramSizeCheck = check;
            return this;
        }
    }

    private SpringList(Builder builder) {
        this.context = builder.context;
        this.view = builder.view;
        this.mSheetLayout = builder.mSheetLayout;
        this.ramSizeCheck = builder.ramSizeCheck;
        this.springCompleteListener = builder.springCompleteListener;

        spring = mSpringSystem.createSpring();
        spring.addListener(mSpringListener);
    }

    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            if (ramSizeCheck) {
                if (AppUtils.isLowRAM()) {
                    springCompleteListener.onSpringCompleteListener(view);
                } else {
                    if(useSpinAnim) {
                        double value = spring.getCurrentValue();
                        float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 1, 0.5);
                        view.setScaleX(mappedValue);
                        view.setScaleY(mappedValue);
                    }
                }
            } else {
                if(useSpinAnim) {
                    double value = spring.getCurrentValue();
                    float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 1, 0.5);
                    view.setScaleX(mappedValue);
                    view.setScaleY(mappedValue);
                }
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {
            super.onSpringAtRest(spring);

            if (ramSizeCheck) {
                if (AppUtils.isLowRAM()) {
                    springCompleteListener.onSpringCompleteListener(view);
                } else {
                    if(useFloatSheetAnim) {
                        mSheetLayout.setFab(view);
                        mSheetLayout.expandFab();
                    }else{
                        springCompleteListener.onSpringCompleteListener(view);
                    }
                }
            }else{
                if(useFloatSheetAnim) {
                    mSheetLayout.setFab(view);
                    mSheetLayout.expandFab();
                }else{
                    springCompleteListener.onSpringCompleteListener(view);
                }
            }
            if(useFloatSheetAnim) {
                mSheetLayout.setFabAnimationEndListener(new SheetLayoutCustom.OnFabAnimationEndListener() {
                    @Override
                    public void onFabAnimationEnd() {
                        springCompleteListener.onSpringCompleteListener(view);
                    }
                });
            }
        }
    }

    public boolean onTouch(MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                spring.setEndValue(1);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                spring.setEndValue(0);
                break;
        }
        return true;
    }

    public void registerSpringListener(SpringCompleteListsener springCompleteListsener) {
        this.springCompleteListener = springCompleteListsener;
    }

    public interface SpringCompleteListsener {
        void onSpringCompleteListener(View view);
    }
}
