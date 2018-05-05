package org.qtum.wallet.ui.fragment.other_tokens.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensFragment;
import org.qtum.wallet.ui.fragment.token_fragment.TokenFragment;

import java.util.List;

public class OtherTokensFragmentDark extends OtherTokensFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_other_tokens;
    }

    @Override
    public void setTokensData(List<Token> tokensData) {
        tokensList.setAdapter(new TokensAdapterDark(tokensData, this, this));
    }

    @Override
    public void onTokenClick(int adapterPosition) {
        if (tokensList.getAdapter() != null) {
            BaseFragment tokenFragment = TokenFragment.newInstance(getContext(), ((TokensAdapterDark) tokensList.getAdapter()).get(adapterPosition));
            openFragment(tokenFragment);
        }
    }
}
