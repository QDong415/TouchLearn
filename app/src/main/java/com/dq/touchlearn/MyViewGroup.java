package com.dq.touchlearn;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyViewGroup extends RelativeLayout {
    private String TAG = "zzz";

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TAG += getTag();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "MyViewGroup 父分发 dispatchTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "MyViewGroup 父分发 dispatchTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "MyViewGroup 父分发 dispatchTouchEvent--ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "MyViewGroup 父分发 dispatchTouchEvent--ACTION_CANCEL");
                break;
        }

        //1、无论是VG还是MyView, 它的super.dispatchTouchEvent(ev) 都是调用 系统源代码的View的dispatchTouchEvent，因为super都是View。
        //2、而系统源代码的View的dispatchTouchEvent 会调用 this.当前View（或者VG）的 onTouchEvent。（目前我发现onTouchEvent只有这一个入口）
        //3、基于2，如果你在 override dispatchTouchEvent()里 不写super.dispatchTouchEvent(ev)。那么当前View（或者VG和VG的子View）永远不走onTouchEvent和onTouchListener。所以这样做很危险。VG和View都是一样的
        //4、基于3，继续讲这种危险情况：如果 override dispatchTouchEvent() 直接return true (不用super)。那么VG的dispatchTouchEvent依然会走完一整套触摸事件
        //5、基于3，继续讲这种危险情况：如果 override dispatchTouchEvent() 直接return false (不用super)。那么VG的dispatchTouchEvent只走了Down，后续的Down和Move up被Activity吃了
        boolean dispatchTouch = false; //super.dispatchTouchEvent(ev);
        Log.w(TAG, "大 MyViewGroup.super.dispatchTouchEvent return >>> " + dispatchTouch);
        return dispatchTouch;

//        return true;
        // 如果你把VG的
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//        } else {
//
//        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "MyViewGroup 父拦截 onInterceptTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "MyViewGroup 父拦截 onInterceptTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "MyViewGroup 父拦截 onInterceptTouchEvent--ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "MyViewGroup 父拦截 onInterceptTouchEvent--ACTION_CANCEL");
                break;
        }

        //1、如果VG的 <拦截方法>onInterceptTouchEvent 的 ACTION_DOWN return true了，那么子View永远吃不到任何事件
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        } else {
//        boolean intercept = super.onInterceptTouchEvent(ev);
        // 普通VG的super.onInterceptTouchEvent(ev)都是返回false
//            boolean intercept = super.onInterceptTouchEvent(ev);
//            Log.w(TAG, "大 MyViewGroup.super.onInterceptTouchEvent return >>> " + intercept);
//            return intercept;

            //1、return true表示VG想把事件从子View拉回本VG，同时丢给子View一个ACTION_CANCEL事件（不给子View Move事件，子View 的流程是 Down -> Cancel）
            //2、但是1的前提是子View 没有 getParent().requestDisallowInterceptTouchEvent(true);，否则这里VG无法拉回View的事件。毕竟View已经吃了Down（View的 dispatchTouchEvent true了）
            //3、基于1，如果VG抢夺成功，会导致VG的onTouchEvent吃掉后面的Move和Up事件。原因很好理解：
            // 因为：一开始Down的时候，小View的override dispatchTouchEvent return为true (他想吃事件)，小的return true就导致了容器VG的super.dispatchTouchEvent 的down也为true
            // 后来Move的时候VG抢夺成功,丢给View一个Cancel。但是由于VG 的down时候 return super.dispatchTouchEvent 是true，于是事件就强塞给了VG的onTouchEvent
            return true;
        }

    //2、上面的Down事件 return false了，表示 大容器把down发给了孩子View
//        boolean intercept = super.onInterceptTouchEvent(ev);
//        Log.w(TAG, "大 MyViewGroup.super.onInterceptTouchEvent return >>> " + intercept);
//        return intercept;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.w(TAG, "MyViewGroup onTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w(TAG, "MyViewGroup onTouchEvent--ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.w(TAG, "MyViewGroup onTouchEvent--ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w(TAG, "MyViewGroup onTouchEvent--ACTION_CANCEL");
                break;
        }

        //1、无论是VG还是MyView, 它的super.onTouchEvent(ev) 都是调用 系统源代码的View的onTouchEvent，因为super都是View。
        //2、系统源代码的View的onTouchEvent是终点，没再调用别的方法了。其内容主要是判断如果clickAble或者longClickAble，true就return true，否则return false
        //3、无论是VG还是MyView，在override onTouchEvent 的代码里 你return的bool值会导致 当前类的super.dispatchTouchEvent(ev) 的return 也是这个bool值
        //4、基于3，无论是VG还是MyView, 你 override dispatchTouchEvent的Down事件 return的bool值 可以理解为"我要不要吃掉这个事件"。
        // 如果Down事件 return true了 事件会全部强塞给当前类的onTouchEvent，无论onTouchEvent return什么。也无论后续的Move return false, 都会走当前类的onTouchEvent
        // 如果Down事件 return false 了具体看MyView - 1。

        //5、基于3，MyView 的dispatchTouchEvent return true了，表示"小View要吃掉这个事件" 。同时会导致它的容器VG的 super.dispatchTouchEvent(ev) 也return true
        //6、基于5，这种情况下，如果你还是故意把VG的dispatchTouchEvent 的Down事件 return false，等于"小View要吃掉事件，但是容器VG却要放弃"。最后孩子和容器VG只吃到down.后面的move吃不到了
        //7、基于6，只要 VG的dispatchTouchEvent 的Down事件 return true了，那么即便VG后面的move return false，也不耽误子View或者当前VG的onTouchEvent继续吃事件
        boolean touch = super.onTouchEvent(event);
        Log.w(TAG, "大 MyViewGroup.super.onTouchEvent return >>> " + touch);
        return touch;

        //1、如果容器VG和子View 的 onTouchEvent（或dispatchTouchEvent）都同时return true，表示"小View和容器VG都要吃掉事件" 。这种情况下最后是小View吃掉了事件
        //2、基于1，VG可以在override onInterceptTouchEvent return true来把事件抢回来
//              return true;
    }
}
