package microanswer.tngoupic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import microanswer.tngoupic.adapter.GridViewAdapter;
import microanswer.tngoupic.bean.PicListItem;
import microanswer.tngoupic.bean.PicClassifyItem;

/**
 * 由 Micro 创建于 2016/9/19.
 */

public class PicListFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private final int ROWS = 30;

    private PicClassifyItem picClassifyItem;
    private int page = 1;

    public PicClassifyItem getPicClassifyItem() {
        return picClassifyItem;
    }

    public void setPicClassifyItem(PicClassifyItem picClassifyItem) {
        this.picClassifyItem = picClassifyItem;
    }

    private GridView gridView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridViewAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_piclist2, null);
            swipeRefreshLayout.setOnRefreshListener(this);

            gridView = (GridView) swipeRefreshLayout.findViewById(R.id.fragment_piclist_gridview);
            gridView.setOnScrollListener(this);
            gridView.setOnItemLongClickListener(this);
            gridView.setOnItemClickListener(this);
            adapter = new GridViewAdapter();

            gridView.setAdapter(adapter);

            requestData(true);

        } else {

            ViewParent parent = swipeRefreshLayout.getParent();
            ViewGroup group = (ViewGroup) parent;
            if (group != null) {
                group.removeView(swipeRefreshLayout);
            }

        }
        return swipeRefreshLayout;
    }

    private boolean isrequestData = false;

    private void requestData(final boolean isRefresh) {

        if (!isrequestData) {
            isrequestData = true;
            swipeRefreshLayout.setRefreshing(true);
            //请求列表数据
            RequestParams requestParams = new RequestParams("http://www.tngou.net/tnfs/api/list?id=" + picClassifyItem.getId() + "&rows=" + ROWS + "&page=" + page);
            x.http().get(requestParams, new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    decodeString(result);
                    return !isRefresh;
                }

                private void decodeString(String result) {
                    if (result == null) {
                        swipeRefreshLayout.setRefreshing(false);
                        isrequestData = false;
                        return;
                    }

                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject.getBooleanValue("status")) {

                        String arraydata = jsonObject.getJSONArray("tngou").toJSONString();

                        List<PicListItem> pics = JSON.parseArray(arraydata, PicListItem.class);

                        if (isRefresh) {
                            adapter.setData(pics);
                        } else {
                            adapter.addData(pics);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        isrequestData = false;
                        page++;
                    }

                }

                @Override
                public void onSuccess(String result) {
                    decodeString(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("PicListFragment", ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i("PicListFragment", cex.getMessage());
                }

                @Override
                public void onFinished() {

                }
            });
        }

    }

    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            //滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = (View) view.getChildAt(view.getChildCount() - 1);
                int[] location = new int[2];
                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                int y = location[1];

                if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y)//第一次拖至底部
                {
                    getLastVisiblePosition = view.getLastVisiblePosition();
                    lastVisiblePositionY = y;
                    return;
                } else if (view.getLastVisiblePosition() == getLastVisiblePosition && lastVisiblePositionY == y)//第二次拖至底部
                {
//                    mCallback.execute();
                    requestData(false);
                }
            }

            //未滚动到底部，第二次拖至底部都初始化
            getLastVisiblePosition = 0;
            lastVisiblePositionY = 0;
        }
    }

    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

    }


    @Override
    public void onRefresh() {
        page = 1;
        requestData(true);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        PicListItem item = (PicListItem) adapterView.getItemAtPosition(i);
        Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PicListItem item = (PicListItem) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(getActivity(), ShowPicActivity.class);
        intent.putExtra("data", item);
        startActivity(intent);
    }
}
