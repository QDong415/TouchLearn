# TouchLearn

## 简介：

用`内部拦截法` 和 `外部拦截法` 两种方式处理两个横向的ViewPager嵌套的手势冲突

最终实现效果：按住右侧部分横滑就是滑动大ViewPager。类似抖音的首页

这个demo看懂你就明白手势处理的流程了

## 安装体验：
![](https://upload-images.jianshu.io/upload_images/26002059-94273eadb7cf0295.png)

## 功能：
- ✅Demo展示了 `内部拦截法` 和 `外部拦截法` 两种方式
- ✅你在网上看到别人的demo，他们会写多一些无用代码，你以为这句代码发挥作用了，其实没有。本demo是最简洁的，一句多余代码都没
- ✅海量的代码注释，且注明了为什么要调用这行代码，如果不调用会怎么样
- ✅看懂这个demo，所有的手势处理你都会了

## 作者说明：
- Android系统的Activity过场动画会让shareElementImageView.setAlpha(0)；然后回退动画结束再进行.setAlpha(1)<bar />
- 这样会导致一个问题：我们下拉返回的时候，由于弹回动画是我们自己做的。但是系统依然会再进行一遍.setAlpha(1)，导致回弹动画结束时候图片会闪一下。参考下面的第1个gif<bar />
- 为了解决"闪一下"的问题，我用这种方法把他提前设为.setAlpha(1)<bar />


## 效果gif图
![](https://upload-images.jianshu.io/upload_images/26002059-771ce6bc9b798289.gif)


## 部分代码注释参考

```java
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = ev.getRawX();
                //down的地方是在最右边
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
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
```

## Author：DQ

有问题联系QQ：285275534, 285275534@qq.com