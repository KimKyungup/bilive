package org.qtum.wallet.ui.fragment.fragment_scribble;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.writeblock.WriteBlock;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewScribbleAdapter extends RecyclerView.Adapter<RecyclerViewScribbleAdapter.RecyclerViewScribbleViewHolder> {

    private List<WriteBlock> items;
    private OnItemClickListener onItemClickLister;

    public RecyclerViewScribbleAdapter(List<WriteBlock> items) {
        super();
        this.items = items;
    }

    @Override
    public RecyclerViewScribbleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_row_scribble, parent, false);

        return new RecyclerViewScribbleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewScribbleViewHolder holder, int position) {
        WriteBlock item = items.get(position);

        StringBuilder testBuilder = new StringBuilder();
        testBuilder.append(item.getTXHash());
        testBuilder.append("\n\n");
        testBuilder.append(item.getWrite());

        holder.textViewPostInfo.setText(item.getBlockTime());
        holder.textViewPostBody.setText(testBuilder.toString());

        if (items.size() == (position + 1)) {
            holder.textViewListViewDivider.setVisibility(View.INVISIBLE);
        }
        else {
            holder.textViewListViewDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    class RecyclerViewScribbleViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPostBody;
        public TextView textViewPostInfo;
        public TextView textViewListViewDivider;

        private RecyclerViewScribbleViewHolder(View itemView) {
            super(itemView);
            textViewPostBody =  (TextView) itemView.findViewById(R.id.textViewPostBody);
            textViewPostInfo =  (TextView) itemView.findViewById(R.id.textViewPostInfo);
            textViewListViewDivider =  (TextView) itemView.findViewById(R.id.textViewListViewDivider);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLister.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
