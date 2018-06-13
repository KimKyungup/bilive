package org.qtum.wallet.ui.fragment.fragment_scribble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.qtum.wallet.R;

import java.util.ArrayList;

public class ListViewScribbleAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<ScribbleItem> mListPostData = new ArrayList<ScribbleItem>();

    public ListViewScribbleAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mListPostData.size();
    }

    @Override
    public ScribbleItem getItem(int i) {
        return mListPostData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listview_row_scribble, null);

            holder.textViewPostBody = (TextView) convertView.findViewById(R.id.textViewPostBody);
            holder.textViewPostInfo = (TextView) convertView.findViewById(R.id.textViewPostInfo);
            holder.textViewListViewDivider = (TextView) convertView.findViewById(R.id.textViewListViewDivider);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ScribbleItem post = mListPostData.get(i);

        holder.textViewPostBody.setText(post.getBody());
        holder.textViewPostInfo.setText(post.getInfo());

        if (mListPostData.size() == (i + 1)) {
            holder.textViewListViewDivider.setVisibility(View.INVISIBLE);
        }
        else {
            holder.textViewListViewDivider.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void addItem(String body, String info) {
        ScribbleItem post = new ScribbleItem(body, info);
        mListPostData.add(post);
    }

    public void removeItem(int position) {
        mListPostData.remove(position);
    }

    private class ViewHolder {
        public TextView textViewPostBody;
        public TextView textViewPostInfo;
        public TextView textViewListViewDivider;
    }
}
