package com.single.coordinatorlayoutdemo.adapter;

import android.content.Context;

import com.single.coordinatorlayoutdemo.bean.BookShelfItem;
import com.single.coordinatorlayoutdemo.holder.BookShelfHolder;
import com.single.coordinatorlayoutdemo.holder.BookShelfLastHolder;
import com.single.coordinatorlayoutdemo.holder.IViewHolder;

public class BookShelfAdapter extends BaseListAdapter<BookShelfItem> {
    public Context context;

    public BookShelfAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected IViewHolder<BookShelfItem> createViewHolder(int viewType) {
        if (viewType == 1) {
            return new BookShelfLastHolder();
        }
        return new BookShelfHolder();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {//最后一个
            return 1;
        } else {
            return 0;
        }
    }

}
