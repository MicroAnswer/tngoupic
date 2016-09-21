package microanswer.tngoupic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import microanswer.tngoupic.R;
import microanswer.tngoupic.TngouPic;
import microanswer.tngoupic.bean.PicListItem;

/**
 * 由 Micro 创建于 2016/9/20.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<PicListItem> data;

    private int itemwidth = 0;

    public void setData(List<PicListItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<PicListItem> data) {
        if (this.data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        } else {
            setData(data);
        }

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (itemwidth == 0)
            itemwidth = ((WindowManager) viewGroup.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 3;
        if (view == null) {
            view = View.inflate(viewGroup.getContext(), R.layout.pic_list_item, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(itemwidth, itemwidth));
            vh = new ViewHolder();
            vh.imageView = (ImageView) view.findViewById(R.id.pic_item_img);
            vh.title = (TextView) view.findViewById(R.id.pic_item_title);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(TngouPic.PIC_URL + data.get(i).getImg() + "_" + itemwidth + "x", vh.imageView);
        vh.title.setText(data.get(i).getTitle());
        return view;
    }


    public class ViewHolder {
        public ImageView imageView;
        public TextView title;
    }

}
