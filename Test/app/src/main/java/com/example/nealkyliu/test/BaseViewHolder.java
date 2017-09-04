package com.example.nealkyliu.test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by nealkyliu on 2017/8/17.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    private View mRootView;
    private ImageView mImageView;

    public BaseViewHolder(View itemView) {
        super(itemView);

        mRootView = $(itemView, R.id.itemRoot);
        mImageView = $(itemView, R.id.imageView);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private <T> T $(View view, int resId) {
        if (null == view) {
            return null;
        }
        return (T) view.findViewById(resId);
    }
}
