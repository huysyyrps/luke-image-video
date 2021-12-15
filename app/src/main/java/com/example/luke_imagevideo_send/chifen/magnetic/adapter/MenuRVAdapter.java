package com.example.luke_imagevideo_send.chifen.magnetic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.mingle.entity.MenuEntity;
import com.mingle.listener.SingleClickListener;
import com.mingle.sweetpick.SweetSheet;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

public class MenuRVAdapter extends RecyclerView.Adapter<MenuRVAdapter.MenuVH> {

    List<MenuEntity> mDataList;
    private boolean mIsAnimation;
    private int mItemLayoutId;
    public View mHeaderView;
    public static final int TYPE_HEADER = 0; //说明是带有Header的
    public static final int TYPE_FOOTER = 1; //说明是带有Footer的
    public static final int TYPE_NORMAL = 2; //说明是不带有header和footer的

    public MenuRVAdapter(List<MenuEntity> dataLis, SweetSheet.Type type) {
        mDataList = dataLis;


        if (type == SweetSheet.Type.RecyclerView) {
            mItemLayoutId = R.layout.item_horizon_rv;
        } else {
            mItemLayoutId = R.layout.item_vertical_rv;
        }
    }

    @Override
    public MenuVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        if(mHeaderView != null && viewType == TYPE_HEADER) {
//            return new MenuVH(mHeaderView);
//        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mItemLayoutId, null, false);
        return new MenuVH(view);
    }

    @Override
    public void onBindViewHolder(MenuVH menuVH, int i) {



        menuVH.itemRl.setOnClickListener(mSingleClickListener);
        menuVH.itemRl.setTag(menuVH.getAdapterPosition());
        MenuEntity menuEntity = mDataList.get(i);
        if (menuEntity.iconId != 0) {

            menuVH.iv.setVisibility(View.VISIBLE);
            menuVH.iv.setImageResource(menuEntity.iconId);
        } else if (menuEntity.icon != null) {

            menuVH.iv.setVisibility(View.VISIBLE);
            menuVH.iv.setImageDrawable(menuEntity.icon);

        } else {
            menuVH.iv.setVisibility(View.GONE);
        }
        menuVH.nameTV.setText(menuEntity.title);
        if (mIsAnimation) {
            animation(menuVH);
        }
    }

    private void animation(MenuVH menuVH) {

        ViewHelper.setAlpha(menuVH.itemView, 0);

        ViewHelper.setTranslationY(menuVH.itemView, 300);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(menuVH.itemView, "translationY", 500, 0);
        translationY.setDuration(300);
        translationY.setInterpolator(new OvershootInterpolator(1.6f));
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(menuVH.itemView, "alpha", 0, 1);
        alphaIn.setDuration(100);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationY, alphaIn);
        animatorSet.setStartDelay(30 * menuVH.getAdapterPosition());
        animatorSet.start();
    }

    @Override
    public int getItemCount() {

        if(mHeaderView == null){
            return mDataList.size();
        }else if (mHeaderView != null){
            return mDataList.size() + 1;
        }
        return mDataList.size();
    }

    public void notifyAnimation() {
        mIsAnimation = true;
        this.notifyDataSetChanged();
    }

    public void notifyNoAimation() {
        mIsAnimation = false;
        this.notifyDataSetChanged();
    }


    class MenuVH extends RecyclerView.ViewHolder {

        public ImageView iv;
        public TextView nameTV;
        public LinearLayout itemRl;

        public MenuVH(View itemView) {
            super(itemView);
            if (itemView == mHeaderView){
                return;
            }
            iv = (ImageView) itemView.findViewById(R.id.imageView);
            nameTV = (TextView) itemView.findViewById(R.id.textView);
            itemRl = (LinearLayout) itemView.findViewById(R.id.linearLayout);


        }
    }

    public AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private SingleClickListener mSingleClickListener = new SingleClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();

            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(null, v, position, position);
            }

        }
    });
}

