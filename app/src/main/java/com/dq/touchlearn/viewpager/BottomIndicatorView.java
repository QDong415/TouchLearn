package com.dq.touchlearn.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dq.touchlearn.R;

public class BottomIndicatorView extends LinearLayout{

    private Context context;
    private Bitmap selectedBitmap;
    private Bitmap unselectedBitmap;
    
    private List<ImageView> dotViews;
    
    private int dotHeight = 30;

    public BottomIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        this(context, null);
    }

    public BottomIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomIndicatorView(Context context) {
        this(context,null);
    }
    
    private void init(Context context, AttributeSet attrs){
        this.context = context;
        dotHeight = dip2px(context, dotHeight);
        selectedBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.banner_select_icon);
        unselectedBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.banner_unselect_icon);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
    public void init(int count){
        dotViews = new ArrayList<ImageView>();
        for(int i = 0 ; i < count ; i++){
            RelativeLayout rl = new RelativeLayout(context);
            LayoutParams params = new LayoutParams(dotHeight,dotHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageView = new ImageView(context);

            if(i == 0){
                imageView.setImageBitmap(selectedBitmap);
                rl.addView(imageView, layoutParams);
            }
            else{
                imageView.setImageBitmap(unselectedBitmap);
                rl.addView(imageView, layoutParams);
            }
            this.addView(rl, params);
            dotViews.add(imageView);
        }
    }
    
    public void updateIndicator(int count) {
        if(dotViews == null){
            return;
        }
        for(int i = 0 ; i < dotViews.size() ; i++){
            if(i >= count){
                dotViews.get(i).setVisibility(GONE);
                ((View)dotViews.get(i).getParent()).setVisibility(GONE);
            }
            else {
                dotViews.get(i).setVisibility(VISIBLE);
                ((View)dotViews.get(i).getParent()).setVisibility(VISIBLE);
            }
        }
        if(count > dotViews.size()){
            int diff = count - dotViews.size();
            for(int i = 0 ; i < diff ; i++){
                RelativeLayout rl = new RelativeLayout(context);
                LayoutParams params = new LayoutParams(dotHeight,dotHeight);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(unselectedBitmap);
                rl.addView(imageView, layoutParams);
                rl.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                this.addView(rl, params);
                dotViews.add(imageView);
            }
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(selectedBitmap != null){
            selectedBitmap.recycle();
        }
        if(unselectedBitmap != null){
            unselectedBitmap.recycle();
        }
    }
    
    public void selectTo(int position){
        for(ImageView iv : dotViews){
            iv.setImageBitmap(unselectedBitmap);
        }
        dotViews.get(position).setImageBitmap(selectedBitmap);
    }
    
    
    public void selectTo(int startPosition, int targetPostion){
        ImageView startView = dotViews.get(startPosition);
        ImageView targetView = dotViews.get(targetPostion);
        startView.setImageBitmap(unselectedBitmap);
        targetView.setImageBitmap(selectedBitmap);
    }
    
}   
