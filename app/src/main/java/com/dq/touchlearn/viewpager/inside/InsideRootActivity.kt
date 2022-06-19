package com.dq.touchlearn.viewpager.inside;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dq.touchlearn.R
import com.dq.touchlearn.viewpager.RightFragment

//可以侧滑到个人主页Demo。我这里用kotlin写的，你要是用java的话自己转
class InsideRootActivity : AppCompatActivity() {

    private lateinit var leftFragment: InsideLeftFragment
    private lateinit var rightFragment: RightFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside_large_viewpager)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState != null && supportFragmentManager.fragments.size > 2) {
            //mainActivity内存重启。leftFragment==null， leftFragment。而并不是new
            //但是如果new，有的tagertsdk会出现Fragment重影，有的会出现无法给fragment传参
            leftFragment = supportFragmentManager.fragments[0] as InsideLeftFragment
            rightFragment = supportFragmentManager.fragments[1] as RightFragment
        } else {
            leftFragment = InsideLeftFragment()
            rightFragment = RightFragment()
        }

        val myAdapter = LargeViewPagerAdapter(supportFragmentManager)
        val viewpager = findViewById<ViewPager>(R.id.viewpager)
        viewpager.setAdapter(myAdapter)
    }

    // 会高频触发，切换fragment甚至都会触发
    // Fg.onActivityCreated -> Fg.onResume -> Root.onWindowFocusChanged
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //如果你在这里removeView(transitionShareOuterView)，会导致无跳转动画
    }

    //kotlin 内部类默认是static ,前面加上inner为非静态
    private inner class LargeViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> leftFragment
                else -> rightFragment
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

}
