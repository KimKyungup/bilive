package org.qtum.wallet.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

public class FontCheckBox extends AppCompatCheckBox {
    public FontCheckBox(Context context) {
        this(context, null);
    }

    public FontCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode())
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, org.qtum.wallet.R.styleable.FontCheckBox);

        if (ta != null) {
            String fontAsset = ta.getString(org.qtum.wallet.R.styleable.FontCheckBox_checkBoxTypeface);

            if (!TextUtils.isEmpty(fontAsset)) {
                Typeface tf = FontManager.getInstance().getFont(fontAsset);
                int style = Typeface.NORMAL;

                if (getTypeface() != null)
                    style = getTypeface().getStyle();

                if (tf != null)
                    setTypeface(tf, style);
                else
                    Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset));
            }
        }
    }
}
