package com.dq.touchlearn.viewpager.inside;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

//大控件
public class InsideParentViewPager extends ViewPager {

    public InsideParentViewPager(@NonNull Context context) {
        super(context);
    }

    public InsideParentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //这里要是return true，孩子啥也摸不到。所以只能return false。
                //return false会导致本类永远吃不到onTouchEvent的down。最多只能吃到onTouchEvent的move
                return false;
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return true;
    }

    //我为了查看dispatchTouchEvent时候的 mFirstTouchTarget 是否为null
    private Object getFieldValueByFieldName(String fieldName, Object object) {
        Field field = null;
        try {
            field = getFieldByClass(fieldName, object);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            Log.e("dz","getFieldValueByFieldName 异常1 ，"+e.getMessage());
            e.printStackTrace();
        }
        return "没找到这个属性";
    }

    //考虑到fieldName可能是object父类的属性。所以向上遍历，直到找到这个属性
    private Field getFieldByClass(String fieldName, Object object) {
        Field field = null;
        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                //这里会有大量的logcat的警告，No field 'fieldName' in class。所以正式开发最好不要用这个方法
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return field;
    }

    //大VP的super.dispatchTouchEvent(ev)永远都 == true。因为即便他没吃到，他的孩子也要吃到。孩子吃到，他就会return true
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Object mFirstTouchTarget = getFieldValueByFieldName("mFirstTouchTarget",this);
        if (mFirstTouchTarget == null){
            Log.e("dz","大 dispatchTouchEvent " +ev.getAction() +" 是null");
        } else {
            Log.e("dz","大 dispatchTouchEvent " +ev.getAction() +" 不是null");
        }
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e("zzz", "MyViewGroup onTouchEvent--ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("zzz", "MyViewGroup onTouchEvent--ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.w("zzz", "MyViewGroup onTouchEvent--ACTION_UP");
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                Log.w("zzz", "MyViewGroup onTouchEvent--ACTION_CANCEL");
//                break;
//        }
//        boolean touch = super.onTouchEvent(event);
//        Log.e("zzz","大 MyViewGroup.super.onTouchEvent return >>> " + touch);
//        return touch;
//    }
}
