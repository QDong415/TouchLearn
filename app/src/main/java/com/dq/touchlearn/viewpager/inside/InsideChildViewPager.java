package com.dq.touchlearn.viewpager.inside;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

//子控件
public class InsideChildViewPager extends ViewPager {

    private float downX;

    //down的按下坐标，是不是在最右侧部分
    private boolean downXAlignParentRight = false;

    public InsideChildViewPager(@NonNull Context context) {
        super(context);
    }

    public InsideChildViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = ev.getRawX();
                downXAlignParentRight = ev.getX() > getWidth() - 300;

                //接到ACTION_DOWN就需要开始取消父容器的事件拦截。不然后面事件全是大容器的onTouchEvent的
                //因为：第一帧的move，他先看flag，即孩子有没有request。发现孩子没有request（因为down的时候给child机会了，就是这里）
                //然后就看他自己有onInterceptTouchEvent true。所以就传给他自己的onTouchEvent了
                getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getX() < downX && downXAlignParentRight) {
                    //手指向左滑动 && 落点在最右测
                    //传false == 让父类处理。true == 本child我还要
                    getParent().requestDisallowInterceptTouchEvent(false);

                } else if (ev.getX() < downX && getCurrentItem() == getAdapter().getCount() - 1){
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    // 下面这一句加不加都一样了
                    // getParent().requestDisallowInterceptTouchEvent(true);

                    //因为：如果走到了上面requestDisallow(false)之后，触摸事件就被父VG收回了，然后丢给本child一个Cancel
                    //同时父VG会把mFirstTouchTarget = null。后面的move就压根就不看mGroupFlags了。 然后child本次touch就再也吃不到事件了（即使requestDisallow(true)）

                    //如果不走上面的requestDisallow(false)，那么就一直mGroupFlags == true；mFirstTouchTarget == 本child
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                // 然后child本次touch就再也吃不到事件了（即使requestDisallow(true)）
                Log.e("dz","小 ACTION_CANCEL");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
