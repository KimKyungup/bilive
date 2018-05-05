package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.qtum.wallet.model.AddressWithBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface AddressListView extends BaseFragmentView {
    void updateAddressList(List<AddressWithBalance> deterministicKeyWithBalance);
}
