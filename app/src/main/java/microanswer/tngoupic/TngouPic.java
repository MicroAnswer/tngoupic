package microanswer.tngoupic;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.x;

/**
 * 由 Micro 创建于 2016/9/19.
 */

public class TngouPic extends Application {
    public static final String PIC_URL = "http://tnfs.tngou.net/image";
    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnFail(R.mipmap.fail)
                .showImageOnLoading(R.mipmap.loading)
                .build();

        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this)
                .diskCacheFileCount(Integer.MAX_VALUE)
                .diskCacheSize(Integer.MAX_VALUE)
                .memoryCacheSize(1024 * 1024 * 8)
                .threadPoolSize(5)
                .defaultDisplayImageOptions(displayImageOptions)
                .build();

        ImageLoader.getInstance().init(imageLoaderConfiguration);

        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
