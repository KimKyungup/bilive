package org.qtum.wallet.ui.fragment.write_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.write_fragment.WriteFragment;

import java.util.List;

public class WriteFragmentDark extends WriteFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_write;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).showBottomNavigationView(R.color.colorPrimary);
        super.initializeViews();
    }

    @Override
    public void updateNews(List<News> newses) {
        mWriteAdapter = new WriteAdapter(newses, R.layout.item_write);
        mRecyclerView.setAdapter(mWriteAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
