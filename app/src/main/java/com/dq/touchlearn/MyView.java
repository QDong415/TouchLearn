package com.dq.touchlearn;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyView extends androidx.appcompat.widget.AppCompatButton {
    private String TAG = "zzz";

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TAG += getTag();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "MyView 小分发 dispatchTouchEvent--ACTION_DOWN");

                //这一句requestDisallowInterceptTouchEvent是设置VG的全局变量flag，设置为true之后，本次事件链就由子View主宰
                //而且VG的dispatchTouchEvent先判断flag 再判断VG的onInterceptTouchEvent，所以requestDisallowInterceptTouchEvent(true)很厉害
                //但是VG的Down会重置这个flag，所以flag的作用范围只是本次触摸事件
                //请记住，无论什么情况，无论什么事件（除了cancel），永远是Ac先拿到，再交给最大的容器VG的dispatchTouchEvent，再判断flag，再看看孩子要不要吃事件
//                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "MyView 小分发 dispatchTouchEvent--ACTION_MOVE");
//                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "MyView 小分发 dispatchTouchEvent--ACTION_UP");
                break;
             case MotionEvent.ACTION_CANCEL:
                 Log.e(TAG, "MyView 小分发 dispatchTouchEvent--ACTION_CANCEL");
                 break;
        }

        boolean dispatchTouch = super.dispatchTouchEvent(ev);
        Log.e(TAG, "小 MyView.super.dispatchTouchEvent return >>> " + dispatchTouch);
        return dispatchTouch;

        //1、如果myview或者VG 的 dispatchTouchEvent 的 MotionEvent.ACTION_DOWN 时候 return false了，那么后续的move事件就跟myview无关了。
        //2、myview 的 super.dispatchTouchEvent 的 return值是由myview 的 onTouchEvent（或者onTouchListener）的return值决定的
        //3、对于vg，除了上面的条件2之外，如果它的当前命中子View dispatchTouchEvent return true ，那么vg的 super.dispatchTouchEvent 也为 true。所以vg是二有一即可为true
        //4、对于最原汁原味的View或者TextView，他走的流程就是1。。而对于Button，button的默认clickable为true，所以button的onTouchEvent return true，所以button可以走完move和up
        //5、如果你刻意修改了dispatchTouchEvent 的 return值（不用 super.dispatchTouchEvent 的 return），那么第2条当我没说。具体看MyViewGroup的注释


//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else {
//            return false;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "MyView onTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "MyView onTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "MyView onTouchEvent--ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "MyView onTouchEvent--ACTION_CANCEL");
                break;
        }
        boolean touch = super.onTouchEvent(event);
        Log.e(TAG, "小 MyView.super.onTouchEvent return >>> " + touch);
//        return touch;
        return true;
    }
}
