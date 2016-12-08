package cc.bitky.bitkyshop.utils;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;
import cc.bitky.bitkyshop.bean.Commodity;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

public class BitkyRecyclerAdapter
    extends RecyclerView.Adapter<BitkyRecyclerAdapter.BitkyViewHolder> {
  private BitkyOnClickListener listener;
  private List<Commodity> mDatas;

  public BitkyRecyclerAdapter(List<Commodity> mDatas) {
    this.mDatas = mDatas;
  }

  public void setOnClickListener(BitkyOnClickListener listener) {
    this.listener = listener;
  }

  @Override public BitkyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_homefragment_show, parent, false);
    return new BitkyViewHolder(view);
  }

  @Override public void onBindViewHolder(BitkyViewHolder holder, int position) {
    Commodity item = mDatas.get(position);
    // KLog.d("位置: " + position + "; name: " + item.getName() + "; image: " + item.getCoverPhotoUrl());
    holder.textViewTitle.setText(item.getName());
    holder.draweeView.setImageURI(Uri.parse(item.getCoverPhotoUrl()));
    holder.textViewPrice.setText(item.getPrice().toString() + " 元");
  }

  @Override public int getItemCount() {
    return mDatas.size();
  }

  public void reloadData(List<Commodity> list) {
    if (mDatas != null) {
      mDatas.clear();
      mDatas.addAll(list);
      notifyDataSetChanged();
    }
  }

  public void loadMoreData(List<Commodity> list) {
    int count = mDatas.size();
    mDatas.addAll(list);
    notifyItemRangeChanged(count, list.size());
  }

  public class BitkyViewHolder extends RecyclerView.ViewHolder {
    View itemView;
    TextView textViewTitle;
    TextView textViewPrice;
    SimpleDraweeView draweeView;

    public BitkyViewHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
      textViewTitle = (TextView) itemView.findViewById(R.id.recycler_homeshow_text_title);
      textViewPrice = (TextView) itemView.findViewById(R.id.recycler_homeshow_text_price);
      draweeView = (SimpleDraweeView) itemView.findViewById(R.id.recycler_homeshow_draweeview);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (listener != null) {
            listener.Onclick(view, getAdapterPosition(),
                mDatas.get(getAdapterPosition()).getName());
          }
        }
      });
    }
  }

  public interface BitkyOnClickListener {
    void Onclick(View v, int positon, String msg);
  }
}
