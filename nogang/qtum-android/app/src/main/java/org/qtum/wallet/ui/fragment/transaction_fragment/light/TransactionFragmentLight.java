package org.qtum.wallet.ui.fragment.transaction_fragment.light;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionFragment;

import java.util.List;

import butterknife.BindView;

public class TransactionFragmentLight extends TransactionFragment {

    @BindView(R.id.ic_confirm)
    ImageView confirmIcon;

    private boolean confirmed;
    private boolean isSend;

    @Override
    protected int getLayout() {
        return R.layout.fragment_transaction_light;
    }

    @Override
    public void recolorTab(int position) {
        switch (position){
            case 0:
                tabAddresses.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_selected));
                tabOverview.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_unselected));
                tabEventLog.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_unselected));
                break;
            case 1:
                tabAddresses.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_unselected));
                tabOverview.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_selected));
                tabEventLog.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_unselected));
                break;
            case 2:
                tabAddresses.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_unselected));
                tabOverview.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_unselected));
                tabEventLog.setTextColor(ContextCompat.getColor(getContext(),R.color.transaction_detail_selected));
                break;
        }
    }

    @Override
    public void setUpTransactionData(String value, String fee, String receivedTime, boolean confirmed, boolean isContractCall) {
        super.setUpTransactionData(value, fee, receivedTime, confirmed, isContractCall);
        this.confirmed = confirmed;
        this.isSend = Double.valueOf(value) > 0;
        checkConfirmation(confirmed);
        colorHeader();
    }


    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().recolorStatusBar(R.color.title_color_light);
    }

    @Override
    public void onUserResume() {
        super.onUserResume();
        checkConfirmation(this.confirmed);
        colorHeader();
    }

    void colorHeader() {
        if (isSend) {
            getMainActivity().recolorStatusBar(R.color.title_lt_green_color);
            toolbarLayout.setBackgroundResource(R.drawable.transaction_back_confirmed);
        } else {
            getMainActivity().recolorStatusBar(R.color.title_red_color);
            toolbarLayout.setBackgroundResource(R.drawable.transaction_back_not_confirmed);
        }
    }

    void checkConfirmation(boolean confirmed) {
        if (confirmed) {
            confirmIcon.setImageResource(R.drawable.ic_confirmed_light);
            notConfFlag.setText(R.string.confirmed);
        } else {
            confirmIcon.setImageResource(R.drawable.ic_confirmation_loader);
            notConfFlag.setText(R.string.not_confirmed_yet);
        }
    }
}
