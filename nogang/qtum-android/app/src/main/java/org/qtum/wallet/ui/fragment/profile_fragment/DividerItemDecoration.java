package org.qtum.wallet.ui.fragment.profile_fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable divider;
    private Drawable sectionDivider;
    private List<SettingObject> settings;

    /**
     * Default divider will be used
     */
    public DividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context, int dividerRes, int sectionRes, List<SettingObject> settings) {
        divider = ContextCompat.getDrawable(context, dividerRes);
        sectionDivider = ContextCompat.getDrawable(context, sectionRes);
        this.settings = settings;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            int pos = parent.getChildAdapterPosition(parent.getChildAt(i));
            if (settings.get(pos).getSectionNumber() != settings.get(pos + 1).getSectionNumber()) {
                outRect.top = sectionDivider.getIntrinsicHeight();
            } else {
                outRect.top = divider.getIntrinsicHeight();
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int pos = parent.getChildAdapterPosition(parent.getChildAt(i));

            try {
                if (settings.get(pos).getSectionNumber() == settings.get(pos + 1).getSectionNumber()) {
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + divider.getIntrinsicHeight();

                    divider.setBounds(left, top, right, bottom);
                    divider.draw(c);
                } else {
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + sectionDivider.getIntrinsicHeight();

                    sectionDivider.setBounds(left, top, right, bottom);
                    sectionDivider.draw(c);
                }
            } catch (Exception e) {
                Log.d("TAG", e.getMessage());
            }
        }
    }
}