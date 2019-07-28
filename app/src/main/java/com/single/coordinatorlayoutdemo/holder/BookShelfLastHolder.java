package com.single.coordinatorlayoutdemo.holder;

import com.single.coordinatorlayoutdemo.R;
import com.single.coordinatorlayoutdemo.bean.BookShelfItem;

public class BookShelfLastHolder extends ViewHolderImpl<BookShelfItem> {

    @Override
    protected int getItemLayoutId() {
        return R.layout.book_shelf_lastitem;
    }

    @Override
    public void initView() {
    }

    @Override
    public void onBind(BookShelfItem data, int pos) {
    }

}
