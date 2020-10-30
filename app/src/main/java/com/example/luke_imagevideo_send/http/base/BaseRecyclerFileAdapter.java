package com.example.luke_imagevideo_send.http.base;

/**
 * Created by Administrator on 2019/4/12.
 */

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2019/4/12.
 * 普通类型的适配器
 */

public abstract class BaseRecyclerFileAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mData;
    private File[] files;


    public BaseRecyclerFileAdapter(Context mContext, int mLayoutId, List<T> mData) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mData = mData;
    }

    public BaseRecyclerFileAdapter(Context mContext, int mLayoutId, File[] files) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.files = files;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = BaseViewHolder.getRecyclerHolder(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convertFile(holder, files[position]);
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    /**
     * 对外提供的方法
     */

    public abstract void convertFile(BaseViewHolder holder, File file);

}
