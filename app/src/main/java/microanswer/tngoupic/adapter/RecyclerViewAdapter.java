package microanswer.tngoupic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import microanswer.tngoupic.R;
import microanswer.tngoupic.TngouPic;
import microanswer.tngoupic.bean.PicListItem;

/**
 * 由 Micro 创建于 2016/9/19.
 */
@Deprecated
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PicListItemHolder> {
    private List<PicListItem> data;
    private SparseArray<Integer> heights;
    private int itemwidth = 0;

    public List<PicListItem> getData() {
        return data;
    }

    public void setData(List<PicListItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<PicListItem> data) {
        if (this.data != null) {
            this.data.addAll(data);
            notifyItemRangeInserted(this.data.size(), data.size());
        } else {
            setData(data);
        }

    }

    @Override
    public PicListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (itemwidth == 0)
            itemwidth = ((WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 3;

        return new PicListItemHolder(View.inflate(parent.getContext(), R.layout.pic_list_item, null));
    }

    @Override
    public void onBindViewHolder(final PicListItemHolder holder, final int position) {

        if (heights == null) {
            heights = new SparseArray<>();
        }

        final Integer height = heights.get(position);

        if (height != null) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

            if (layoutParams == null) {
                layoutParams = new RecyclerView.LayoutParams(itemwidth, height);
            }

            layoutParams.height = height;
            holder.itemView.setLayoutParams(layoutParams);
        }
        int w = holder.itemView.getWidth();

        ImageLoader.getInstance().displayImage(TngouPic.PIC_URL + data.get(position).getImg() + "_" + (w == 0 ? itemwidth : w) + "x", holder.img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (height == null) {
                    heights.put(position, loadedImage.getHeight());
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

      /*  x.image().bind(holder.img, TngouPic.PIC_URL + data.get(position).getImg() + "_" + (w == 0 ? itemwidth : w) + "x", new Callback.CacheCallback<Drawable>() {
            @Override
            public boolean onCache(Drawable result) {
                if (height == null) {
                    heights.put(position, result.getBounds().height());
                }
                return true;
            }

            @Override
            public void onSuccess(Drawable result) {
                if (height == null) {
                    heights.put(position, result.getBounds().height());
                }
//                holder.img.setImageDrawable(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
     */
        holder.title.setText(data.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class PicListItemHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView title;

        public PicListItemHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.pic_item_img);
            title = (TextView) itemView.findViewById(R.id.pic_item_title);
        }
    }

}
