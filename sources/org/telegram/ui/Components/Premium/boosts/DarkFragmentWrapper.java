package org.telegram.ui.Components.Premium.boosts;

import android.app.Activity;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.WrappedResourceProvider;

/* loaded from: classes3.dex */
public class DarkFragmentWrapper extends BaseFragment {
    private final BaseFragment parentFragment;

    DarkFragmentWrapper(BaseFragment baseFragment) {
        this.parentFragment = baseFragment;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Activity getParentActivity() {
        return this.parentFragment.getParentActivity();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return new WrappedResourceProvider(new DarkThemeResourceProvider());
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment baseFragment) {
        return false;
    }
}
