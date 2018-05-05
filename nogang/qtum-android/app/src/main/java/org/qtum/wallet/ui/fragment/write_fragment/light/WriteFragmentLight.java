package org.qtum.wallet.ui.fragment.write_fragment.light;

import org.qtum.wallet.R;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.write_fragment.WriteFragment;

import java.util.List;

public class WriteFragmentLight extends WriteFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_write;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).showBottomNavigationView(R.color.title_color_light);
        super.initializeViews();
    }

    @Override
    public void updateWriteBlocks(List<WriteBlock> writeBlocks) {
        mWriteAdapter = new WriteAdapter(writeBlocks, R.layout.item_news_light);
        mRecyclerView.setAdapter(mWriteAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
