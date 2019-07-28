package com.single.coordinatorlayoutdemo.holder;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.single.coordinatorlayoutdemo.R;
import com.single.coordinatorlayoutdemo.bean.BookShelfItem;

public class BookShelfHolder extends ViewHolderImpl<BookShelfItem> {

    private ImageView bookCoverimg;
    private TextView bookName;

    @Override
    protected int getItemLayoutId() {
        return R.layout.book_shelf_item;
    }

    @Override
    public void initView() {
        bookCoverimg = findById(R.id.bookCoverimg);
        bookName = findById(R.id.bookName);
    }

    @Override
    public void onBind(BookShelfItem data, int pos) {
        Glide.with(getContext()).load(data.bookCoverimg).placeholder(R.mipmap.covering).into(bookCoverimg);
        bookName.setText(data.bookName);
    }

}
