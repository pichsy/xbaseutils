package com.pichs.app.xwidget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.pichs.common.utils.recyclerview.RecyclerGridDivider;
import com.pichs.common.utils.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: 吴波
 * @CreateDate: 3/15/21 3:17 PM
 * @UpdateUser: 吴波
 * @UpdateDate: 3/15/21 3:17 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ListActivity extends AppCompatActivity {

    RecyclerView mRecycler;
    List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        mRecycler = findViewById(R.id.mRecycler);
//        mRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        for (int i = 0; i < 10; i++) {
            mData.add("我是内容" + i);
        }
//        RecyclerListDivider divider = new RecyclerListDivider(RecyclerView.HORIZONTAL, 30, 0);
        RecyclerGridDivider divider = new RecyclerGridDivider(RecyclerView.VERTICAL, 20);
//        GridItemDecoration divider = new GridItemDecoration(2,20);
        mRecycler.addItemDecoration(divider);
        mRecycler.setAdapter(new MyAdapter(mData, this));

    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        private List<String> list;
        private Context mContext;

        public MyAdapter(List<String> list, Context context) {
            this.list = list;
            mContext = context;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.tv.setText(list.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(mContext, "pos:" + position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class MyHolder extends RecyclerView.ViewHolder {
            private TextView tv;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv_title);
            }
        }
    }
}
