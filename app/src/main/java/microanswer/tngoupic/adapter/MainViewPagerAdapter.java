package microanswer.tngoupic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import microanswer.tngoupic.PicListFragment2;
import microanswer.tngoupic.bean.PicClassifyItem;

/**
 * 由 Micro 创建于 2016/9/19.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<PicClassifyItem> picClassifyItems;
    private PicListFragment2[] fragments;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        if (null == fragments) {
            fragments = new PicListFragment2[picClassifyItems.size()];
        }

        if (null == fragments[position]) {
            fragments[position] = new PicListFragment2();
        }
        fragments[position].setPicClassifyItem(picClassifyItems.get(position));
        return fragments[position];
    }

    public void setPicClassifyItems(List<PicClassifyItem> picClassifyItems) {
        this.picClassifyItems = picClassifyItems;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return picClassifyItems == null ? 0 : picClassifyItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return picClassifyItems == null ? "无数据" : picClassifyItems.get(position).getTitle();
    }
}
