package org.qtum.wallet.ui.fragment.fragment_my_wallet_total;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.fragment_scribble.ScribbleItem;

import java.util.ArrayList;

public class ListViewMyWalletAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<MyWalletHistoryItem> mListPostData = new ArrayList<MyWalletHistoryItem>();

    public ListViewMyWalletAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mListPostData.size();
    }

    @Override
    public MyWalletHistoryItem getItem(int i) {
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
            convertView = inflater.inflate(R.layout.item_listview_row_my_wallet, null);

            holder.imageViewWalletHistory = (ImageView) convertView.findViewById(R.id.imageViewWalletHistory);
            holder.textViewHistorySummary = (TextView) convertView.findViewById(R.id.textViewHistorySummary);
            holder.textViewHistoryDate = (TextView) convertView.findViewById(R.id.textViewHistoryDate);
            holder.textViewValue = (TextView) convertView.findViewById(R.id.textViewValue);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        MyWalletHistoryItem myWalletHistoryItem = mListPostData.get(i);

        switch (myWalletHistoryItem.type) {
            case MyWalletHistoryItem.TYPE_NONE: {
                holder.imageViewWalletHistory.setImageDrawable(null);
                break;
            }
            case MyWalletHistoryItem.TYPE_SEND: {
                holder.imageViewWalletHistory.setImageResource(R.drawable.wallet_history_send);
                holder.textViewValue.setTextColor(ContextCompat.getColor(mContext, R.color.my_wallet_balance_decrease_color));
                break;
            }
            case MyWalletHistoryItem.TYPE_RECV: {
                holder.imageViewWalletHistory.setImageResource(R.drawable.wallet_history_recieve);
                holder.textViewValue.setTextColor(ContextCompat.getColor(mContext, R.color.my_wallet_balance_increase_color));
                break;
            }
            case MyWalletHistoryItem.TYPE_WRITE: {
                holder.imageViewWalletHistory.setImageResource(R.drawable.wallet_history_write);
                holder.textViewValue.setTextColor(ContextCompat.getColor(mContext, R.color.my_wallet_balance_decrease_color));
                break;
            }
        }

        holder.textViewHistorySummary.setText(myWalletHistoryItem.summary);
        holder.textViewHistoryDate.setText(myWalletHistoryItem.date);
        holder.textViewValue.setText(myWalletHistoryItem.value);

        return convertView;
    }

    public void addItem(int type, String summary, String date, String value) {
        MyWalletHistoryItem myWalletHistoryItem = new MyWalletHistoryItem(type, summary, date, value);
        mListPostData.add(myWalletHistoryItem);
    }

    public void removeItem(int position) {
        mListPostData.remove(position);
    }

    private class ViewHolder {
        public ImageView imageViewWalletHistory;
        public TextView textViewHistorySummary;
        public TextView textViewHistoryDate;
        public TextView textViewValue;
    }
}
