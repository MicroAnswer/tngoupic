package microanswer.tngoupic;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import microanswer.tngoupic.adapter.MainViewPagerAdapter;
import microanswer.tngoupic.bean.PicClassifyItem;

/**
 * 由 Micro 创建于 2016/9/19.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private ImageView imageViewmenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        imageViewmenu = (ImageView) findViewById(R.id.activity_main_menu);
        imageViewmenu.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.activity_main_tablayout);
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewager);
        viewPager.setAdapter(mainViewPagerAdapter);

        //请求图片分类
        RequestParams rp = new RequestParams("http://www.tngou.net/tnfs/api/classify");
        x.http().get(rp, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                decodeResult(result);
                return true;
            }

            @Override
            public void onSuccess(String result) {
                decodeResult(result);
            }

            private void decodeResult(String result) {
                if (null == result) {
                    return;
                }

                JSONObject resultJson = JSON.parseObject(result);
                if (resultJson.getBooleanValue("status")) {
                    String tngou = resultJson.getJSONArray("tngou").toJSONString();
                    List<PicClassifyItem> picClassifyItems = JSON.parseArray(tngou, PicClassifyItem.class);
                    mainViewPagerAdapter.setPicClassifyItems(picClassifyItems);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                AlertDialog.Builder abd = new AlertDialog.Builder(MainActivity.this);
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
    protected void onDestroy() {
        if (pw != null) {
            pw.dismiss();
        }

        super.onDestroy();
    }

    private PopupWindow pw;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_main_menu) {
            if (pw == null) {
                pw = new PopupWindow(this);
                pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        pw = null;
                    }
                });
                View popview = View.inflate(this, R.layout.menu, null);
                TextView clear = (TextView) popview.findViewById(R.id.menu_clear);
                TextView about = (TextView) popview.findViewById(R.id.menu_about);
                clear.setOnClickListener(this);
                about.setOnClickListener(this);
                pw.setContentView(popview);
                pw.setWidth(getWindowManager().getDefaultDisplay().getWidth() * 3 / 5);
                pw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                pw.setTouchable(true);
                pw.setFocusable(true);
                pw.setOutsideTouchable(true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                pw.showAsDropDown(view);
            }
        } else if (R.id.menu_clear == view.getId()) {
            if (pw != null) {
                pw.dismiss();
                pw = null;
            }


            AlertDialog.Builder abd = new AlertDialog.Builder(this).setTitle("清除缓存").setMessage("清除缓存会减少手机空间的占用，但会增加网络消耗，确定清除缓存吗？");
            abd.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    x.image().clearCacheFiles();
                    ImageLoader.getInstance().clearDiskCache();
                    Toast.makeText(MainActivity.this, "缓存已清除", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("取消", null);
            abd.show();
        } else if (view.getId() == R.id.menu_about) {
            if (pw != null) {
                pw.dismiss();
                pw = null;
            }
            new AlertDialog.Builder(MainActivity.this).setMessage("天狗乐图\n\n依托于www.tngou.net提供的免费数据\n\n作者信息：\nname:范雪蛟\nqq:274288225\ntel:13541227014\n")
                    .setPositiveButton("确定", null).show();

        }
    }
}
