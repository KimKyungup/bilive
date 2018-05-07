package org.qtum.wallet.ui.fragment_factory;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.processing_dialog.ProcessingDialogFragmentDark;
import org.qtum.wallet.ui.fragment.processing_dialog.ProcessingDialogFragment;

public class Factory {

    public static final String DARK_POSTFIX = "Dark";

    public static BaseFragment instantiateFragment(Context context, Class fragment) {
        return (BaseFragment) Fragment.instantiate(context, getThemedFargment(context, fragment));
    }

    public static Fragment instantiateDefaultFragment(Context context, Class fragment) {
        return Fragment.instantiate(context, getThemedFargment(context, fragment));
    }

    private static String getThemedFargment(Context context, Class fragment) {
        String postfix = DARK_POSTFIX;
        String fullname = String.format("%s.%s%s", fragment.getPackage().getName(), fragment.getSimpleName(), postfix);
        return fullname;
    }
}
