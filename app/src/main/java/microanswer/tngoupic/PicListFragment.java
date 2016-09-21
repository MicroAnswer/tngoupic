package microanswer.tngoupic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import microanswer.tngoupic.adapter.RecyclerViewAdapter;
import microanswer.tngoupic.bean.PicListItem;
import microanswer.tngoupic.bean.PicClassifyItem;

/**
 * 由 Micro 创建于 2016/9/19.
 */

@Deprecated
public class PicListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final int ROWS = 30;

    private PicClassifyItem picClassifyItem;
    private int page = 1;

    public PicClassifyItem getPicClassifyItem() {
        return picClassifyItem;
    }

    public void setPicClassifyItem(PicClassifyItem picClassifyItem) {
        this.picClassifyItem = picClassifyItem;
    }

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_piclist, null);
            swipeRefreshLayout.setOnRefreshListener(this);
            recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.fragment_piclist_recyclerview);
            adapter = new RecyclerViewAdapter();
            recyclerView.addOnScrollListener(scrollListener);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
            requestData();

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

    private void requestData() {

        if (!isrequestData) {
            isrequestData = true;
            swipeRefreshLayout.setRefreshing(true);
            //请求列表数据
            RequestParams requestParams = new RequestParams("http://www.tngou.net/tnfs/api/list?id=" + picClassifyItem.getId() + "&rows=" + ROWS + "&page=" + page);
            x.http().get(requestParams, new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    decodeString(result);
                    return true;
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

                        adapter.addData(pics);
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

    /**
     * 滑动监听器
     */
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isSlideToBottom(recyclerView)) {
                requestData();
            }
        }
    };

    /**
     * 是否滑动到底部【这里做了修改，只要达到距离底部400dp的位置就开始加载更多内容】
     *
     * @param recyclerView
     * @return
     */
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        return recyclerView != null && recyclerView.getAdapter() != null && getActivity() != null && recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= (recyclerView.computeVerticalScrollRange());
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestData();
    }
}
