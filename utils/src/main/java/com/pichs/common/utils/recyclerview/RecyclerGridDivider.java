package com.pichs.common.utils.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerGridDivider extends RecyclerView.ItemDecoration {

    private int mOrientation = RecyclerView.VERTICAL;
    private int mSpanCount = 2;
    private int mDividerSize = 6;

    public RecyclerGridDivider(int orientation, int dividerHeight) {
        this.mSpanCount = 2;
        this.mDividerSize = dividerHeight;
        if (mOrientation != RecyclerView.HORIZONTAL) {
            mOrientation = RecyclerView.VERTICAL;
        } else {
            mOrientation = orientation;
        }
    }

    /**
     * @param outRect 边界
     * @param view    recyclerView ItemView
     * @param parent  recyclerView
     * @param state   recycler 内部数据管理
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (mOrientation == RecyclerView.VERTICAL) {
            if (position % mSpanCount == 0) {
                // 左面
                outRect.set(0, mDividerSize / 2, mDividerSize / 2, mDividerSize / 2);
            } else {
                // 右面
                outRect.set(mDividerSize / 2, mDividerSize / 2, 0, mDividerSize / 2);
            }
        } else {
            if (position % mSpanCount == 0) {
                // 上面
                outRect.set(mDividerSize / 2, 0, mDividerSize / 2, mDividerSize / 2);
            } else {
                // 下面
                outRect.set(mDividerSize / 2, mDividerSize / 2, mDividerSize / 2, 0);
            }
        }
    }
}