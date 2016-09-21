package microanswer.tngoupic.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import microanswer.tngoupic.R;
import microanswer.tngoupic.TngouPic;
import microanswer.tngoupic.bean.Pic;

/**
 * 由 Micro 创建于 2016/9/20.
 */

public class ShowPicViewPagerAdapter extends PagerAdapter {

    private List<Pic> data;


    public List<Pic> getData() {
        return data;
    }

    public void setData(List<Pic> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void addData(List<Pic> data) {
        if (this.data == null) {
            setData(data);
        } else {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), R.layout.pic, null);
        ImageViewTouch img = (ImageViewTouch) view.findViewById(R.id.imagetouch);
        if (img.getTag() == null) {
            ImageLoader.getInstance().displayImage(TngouPic.PIC_URL + data.get(position).getSrc(), img, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, final View view, Bitmap loadedImage) {
                    Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int c = palette.getDominantSwatch().getRgb();
                            view.setTag(c);
                            view.setBackgroundColor(c);
                        }
                    });
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else {
            img.setBackgroundColor((int) img.getTag());
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
