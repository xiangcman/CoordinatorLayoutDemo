package com.single.coordinatorlayoutdemo.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewHolderImpl<T> implements IViewHolder<T> {
    private View view;
    private Context context;

    /****************************************************/
    protected abstract int getItemLayoutId();

    @Override
    public View createItemView(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(getItemLayoutId(), null, false);
        context = parent.getContext();
        return view;
    }

    protected <V extends View> V findById(int id) {
        return (V) view.findViewById(id);
    }

    protected Context getContext() {
        return context;
    }

    protected View getItemView() {
        return view;
    }

    @Override
    public void onClick() {
    }
}
