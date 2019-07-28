package com.single.coordinatorlayoutdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.single.coordinatorlayoutdemo.R;
import com.single.coordinatorlayoutdemo.adapter.BookShelfAdapter;
import com.single.coordinatorlayoutdemo.animation.InContentAnim;
import com.single.coordinatorlayoutdemo.bean.BookShelfItem;
import com.single.coordinatorlayoutdemo.utils.CoordinatorUtils;
import com.single.coordinatorlayoutdemo.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookShelfFragment extends Fragment {
    private static final String TAG = BookShelfFragment.class.getSimpleName();
    View root;
    Toolbar toolbar;
    View line;
    AppBarLayout appBarLayout;
    RecyclerView showShelf;
    View shelfContent;
    View progress;
    View content;
    private BookShelfAdapter bookShelfAdapter;
    //一个整体移动的动画库，平时项目中用的
    private InContentAnim inContentAnim;
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            String string = (String) msg.obj;
            Log.d(TAG, "string:" + string);
            try {
                inContentAnim.start();
                JSONArray jsonArray = new JSONArray(string);
                List<BookShelfItem> bookShelfItems = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    BookShelfItem bookShelfItem = new BookShelfItem();
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String bookCoverimg = (String) object.get("bookCoverimg");
                    String bookName = (String) object.get("bookName");
                    bookShelfItem.bookCoverimg = bookCoverimg;
                    bookShelfItem.bookName = bookName;
                    bookShelfItems.add(bookShelfItem);
                }
                bookShelfAdapter.refreshItems(bookShelfItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private final Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(2000);
                //获取json文件
                String json = JsonUtils.getJson(getContext(), "book.json");
                Message message = handler.obtainMessage();
                message.obj = json;
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_book_shelf, null);
        toolbar = root.findViewById(R.id.t_toolbar);
        line = root.findViewById(R.id.line);
        appBarLayout = root.findViewById(R.id.AppFragment_AppBarLayout);
        showShelf = root.findViewById(R.id.show_shelf);
        shelfContent = root.findViewById(R.id.shelf_content);
        progress = root.findViewById(R.id.progress);
        content = root.findViewById(R.id.content);
        inContentAnim = new InContentAnim(content, progress);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCoordinator();
        initBookShelfContent();
        getData();
    }

    private void getData() {
        thread.start();
    }

    private void initBookShelfContent() {
        showShelf.setLayoutManager(new GridLayoutManager(getContext(), 3));
        showShelf.setAdapter(bookShelfAdapter = new BookShelfAdapter(getContext()));
    }

    /**
     * 设置CoordinatorLayout部分
     */
    private void initCoordinator() {
        //不采用系统的toolbar，将自己的toolbar放到其中
        toolbar.removeAllViews();
        Toolbar.LayoutParams lps = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = getLayoutInflater().inflate(R.layout.toolbar_book_shelf, null);
        toolbar.addView(view, lps);
        //由于咱们用了statusbarUtils中状态栏沉浸式，因此下面的toolbar需要下移，距离是statusBar的高度
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(getContext());
        toolbar.setLayoutParams(layoutParams);

        //shelfContent是咋们的签到部分，移动距离是toolbar的高度+statusbar的高度
        RelativeLayout.LayoutParams shelfContentLp = (RelativeLayout.LayoutParams) shelfContent.getLayoutParams();
        shelfContentLp.topMargin = (int) (getResources().getDimension(R.dimen.dp_45) + StatusBarUtil.getStatusBarHeight(getContext()));
        shelfContent.setLayoutParams(shelfContentLp);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //设置toolbar渐变的效果，主要是监听appbarlayout滑动的距离,getTotalScrollRange:是appbarLayout可滑动的最大距离
                toolbar.setBackgroundColor(CoordinatorUtils.changeAlpha(getResources().getColor(R.color.white), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
                //分割线也采用渐变效果，在折叠的时候，完全显示分割线
                line.setAlpha(Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());
            }
        });
    }
}
