package com.dq.touchlearn.viewpager.outside;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

//大控件
public class OutsideParentViewPager extends ViewPager {

    private float downX;

    //down的按下坐标，是不是在最右侧部分
    private boolean downXAlignParentRight = false;

    public OutsideParentViewPager(@NonNull Context context) {
        super(context);
    }

    public OutsideParentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean superInterceptTouchEvent = super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //这里要是return true，孩子啥也摸不到。所以只能return false。
                //return false会导致本类永远吃不到onTouchEvent的down。最多只能吃到onTouchEvent的move

                downX = ev.getX();
                downXAlignParentRight = ev.getX() > getWidth() - 300;

                return false;
            case MotionEvent.ACTION_MOVE:

                if (ev.getX() < downX && downXAlignParentRight) {
                    //手指向左滑动 && 落点在最右测
                    //本parent需要处理
                    return true;
                }
        }

        //系统他自己也会处理双ViewPager嵌套时候的手势冲突（比如child滚到最后一个再右划就触发parent）所以我们要保留
        return superInterceptTouchEvent;
    }


    //----------- 下面的代码只是为了打log让你明白系统源码里 mFirstTouchTarget 的处理
    //你实际开发的时候请删掉

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

}
