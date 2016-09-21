package microanswer.tngoupic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import microanswer.tngoupic.adapter.ShowPicViewPagerAdapter;
import microanswer.tngoupic.bean.Pic;
import microanswer.tngoupic.bean.PicListItem;

/**
 * 由 Micro 创建于 2016/9/20.
 */

public class ShowPicActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ShowPicViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        setContentView(viewPager);
        adapter = new ShowPicViewPagerAdapter();
        viewPager.setAdapter(adapter);

        PicListItem item = (PicListItem) getIntent().getSerializableExtra("data");
        setTitle(item.getTitle());
        setTitleColor(Color.WHITE);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        x.http().get(new RequestParams("http://www.tngou.net/tnfs/api/show?id=" + item.getId()), new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                decodeString(result);
                return true;
            }

            public void decodeString(String result) {
                if (result == null) {
                    return;
                }

                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getBooleanValue("status")) {
                    String jsonarray = jsonObject.getJSONArray("list").toJSONString();
                    List<Pic> pics = JSON.parseArray(jsonarray, Pic.class);
                    adapter.setData(pics);
                }
            }


            @Override
            public void onSuccess(String result) {
                decodeString(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                AlertDialog.Builder abd = new AlertDialog.Builder(ShowPicActivity.this);
                abd.setTitle("遇见错误");
                abd.setMessage("错误信息：\n" + ex.getMessage());
                abd.setPositiveButton("确定", null);
                abd.show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return
                    true;
        }

        return super.onOptionsItemSelected(item);
    }
}
