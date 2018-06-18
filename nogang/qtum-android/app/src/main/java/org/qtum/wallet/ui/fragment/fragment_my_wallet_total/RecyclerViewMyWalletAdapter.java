package org.qtum.wallet.ui.fragment.fragment_my_wallet_total;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;

import java.util.ArrayList;

public class RecyclerViewMyWalletAdapter extends RecyclerView.Adapter<RecyclerViewMyWalletAdapter.RecyclerViewMyWalletHolder> {

    private Context context = null;
    private ArrayList<MyWalletHistoryItem> items = new ArrayList<MyWalletHistoryItem>();
    private OnItemClickListener onItemClickLister;

    public RecyclerViewMyWalletAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public RecyclerViewMyWalletHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_row_my_wallet, parent, false);

        return new RecyclerViewMyWalletHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewMyWalletHolder holder, int position) {
        MyWalletHistoryItem item = items.get(position);

        holder.textViewHistorySummary.setText(item.summary);
        holder.textViewHistoryDate.setText(item.date);
        holder.textViewValue.setText(item.value);

        switch (item.type) {
            case MyWalletHistoryItem.TYPE_NONE: {
                holder.imageViewWalletHistory.setImageDrawable(null);
                break;
            }
            case MyWalletHistoryItem.TYPE_SEND: {
                holder.imageViewWalletHistory.setImageResource(R.drawable.wallet_history_send);
                holder.textViewValue.setTextColor(ContextCompat.getColor(context, R.color.my_wallet_balance_decrease_color));
                break;
            }
            case MyWalletHistoryItem.TYPE_RECV: {
                holder.imageViewWalletHistory.setImageResource(R.drawable.wallet_history_recieve);
                holder.textViewValue.setTextColor(ContextCompat.getColor(context, R.color.my_wallet_balance_increase_color));
                break;
            }
            case MyWalletHistoryItem.TYPE_WRITE: {
                holder.imageViewWalletHistory.setImageResource(R.drawable.wallet_history_write);
                holder.textViewValue.setTextColor(ContextCompat.getColor(context, R.color.my_wallet_balance_decrease_color));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(int type, String summary, String date, String value) {
        MyWalletHistoryItem myWalletHistoryItem = new MyWalletHistoryItem(type, summary, date, value);
        items.add(myWalletHistoryItem);
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    class RecyclerViewMyWalletHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewWalletHistory;
        public TextView textViewHistorySummary;
        public TextView textViewHistoryDate;
        public TextView textViewValue;

        private RecyclerViewMyWalletHolder(View itemView) {
            super(itemView);
            imageViewWalletHistory =  (ImageView) itemView.findViewById(R.id.imageViewWalletHistory);
            textViewHistorySummary =  (TextView) itemView.findViewById(R.id.textViewHistorySummary);
            textViewHistoryDate =  (TextView) itemView.findViewById(R.id.textViewHistoryDate);
            textViewValue =  (TextView) itemView.findViewById(R.id.textViewValue);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLister.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}

