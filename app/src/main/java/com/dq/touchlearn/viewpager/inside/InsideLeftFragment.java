package com.dq.touchlearn.viewpager.inside;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dq.touchlearn.R;
import com.dq.touchlearn.viewpager.BottomIndicatorView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class InsideLeftFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private BottomIndicatorView indicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_inside_small_viewpager,container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Integer> photos = new ArrayList<>();
        photos.add(R.mipmap.banner1);
        photos.add(R.mipmap.banner2);
        photos.add(R.mipmap.banner3);

        ViewPager viewpager = getView().findViewById(R.id.viewpager);
        SmallViewPagerAdapter viewPagerAdapter = new SmallViewPagerAdapter(getActivity(), photos);
        viewpager.setAdapter(viewPagerAdapter);

        indicatorView = (BottomIndicatorView) getView().findViewById(R.id.indicator_view);
        indicatorView.init(photos.size());

        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicatorView.selectTo(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //详情界面的HeaderView的Adapter（实际开发一般是banner）。我这里只是demo，你可以随意修改
    private static class SmallViewPagerAdapter extends PagerAdapter {

        private Context context;
        private List<Integer> photoList;

        public SmallViewPagerAdapter(Context context, List<Integer> photoList) {
            this.context = context;
            this.photoList = photoList;
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(photoList.get(position));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

    }
}
