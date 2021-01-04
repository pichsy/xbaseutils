package com.pichs.common.utils.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;
    private int mDividerSize;

    public GridItemDecoration(int spanCont, int dividerSize) {
        this.mSpanCount = spanCont;
        this.mDividerSize = dividerSize;
    }

    public GridItemDecoration() {
        mSpanCount = 2;
        mDividerSize = 6;
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
        if (position % mSpanCount == 0) {// 最右边
            outRect.set(0, mDividerSize / 2, mDividerSize / 2, mDividerSize / 2);
        } else if ((position + mSpanCount - 1) % mSpanCount == 0) {// 最左边
            outRect.set(mDividerSize / 2, mDividerSize / 2, 0, mDividerSize / 2);
        } else { // 中间其他位置
            outRect.set(mDividerSize / 2, mDividerSize / 2, mDividerSize / 2, mDividerSize / 2);
        }
    }
}