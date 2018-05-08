package org.qtum.wallet.ui.fragment_factory;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.processing_dialog.ProcessingDialogFragment;

public class Factory {
    public static BaseFragment instantiateFragment(Context context, Class fragment) {
        return (BaseFragment) Fragment.instantiate(context, getThemedFargment(fragment));
    }

    private static String getThemedFargment(Class fragment) {
        String fullname = String.format("%s.%s", fragment.getPackage().getName(), fragment.getSimpleName());
        return fullname;
    }
}
