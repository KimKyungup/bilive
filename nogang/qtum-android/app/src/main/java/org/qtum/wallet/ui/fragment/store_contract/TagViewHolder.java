package org.qtum.wallet.ui.fragment.store_contract;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.utils.FontButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagViewHolder extends RecyclerView.ViewHolder {

    @BindView(org.qtum.wallet.R.id.bt_title)
    FontButton title;

    String tagValue;

    public TagViewHolder(View itemView, final TagClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTagClick(tagValue);
            }
        });
    }

    public void bind(String tag) {
        tagValue = tag;
        title.setText(String.format("#%s", tagValue));
    }
}
