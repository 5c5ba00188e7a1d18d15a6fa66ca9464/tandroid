package org.telegram.ui.Components;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class OverlayActionBarLayoutDialog extends Dialog implements ActionBarLayout.ActionBarLayoutDelegate {
    private ActionBarLayout actionBarLayout;
    private FrameLayout frameLayout;
    private Theme.ResourcesProvider resourcesProvider;

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needAddFragmentToStack(BaseFragment baseFragment, ActionBarLayout actionBarLayout) {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, ActionBarLayout actionBarLayout) {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean onPreIme() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public void onRebuildAllFragments(ActionBarLayout actionBarLayout, boolean z) {
    }

    public OverlayActionBarLayoutDialog(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, 2131689510);
        this.resourcesProvider = resourcesProvider;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, context);
        this.actionBarLayout = anonymousClass1;
        anonymousClass1.init(new ArrayList<>());
        this.actionBarLayout.presentFragment(new EmptyFragment(null), false, true, false, false);
        this.actionBarLayout.setDelegate(this);
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.frameLayout.addView(this.actionBarLayout, new FrameLayout.LayoutParams(-1, -1, 17));
        if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
            this.frameLayout.setBackgroundColor(-1728053248);
            this.frameLayout.setOnClickListener(new OverlayActionBarLayoutDialog$$ExternalSyntheticLambda1(this));
            this.actionBarLayout.setRemoveActionBarExtraHeight(true);
            VerticalPositionAutoAnimator.attach(this.actionBarLayout);
        }
        setContentView(this.frameLayout);
    }

    /* renamed from: org.telegram.ui.Components.OverlayActionBarLayoutDialog$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ActionBarLayout {
        AnonymousClass1(OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, Context context) {
            super(context);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0f), View.MeasureSpec.getSize(i)), 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0f), View.MeasureSpec.getSize(i2)), 1073741824));
            } else {
                super.onMeasure(i, i2);
            }
        }
    }

    public /* synthetic */ void lambda$new$0(View view) {
        onBackPressed();
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        int i = Build.VERSION.SDK_INT;
        if (i >= 30) {
            window.addFlags(-2147483392);
        } else if (i >= 21) {
            window.addFlags(-2147417856);
        }
        window.setWindowAnimations(2131689478);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.gravity = 51;
        attributes.dimAmount = 0.0f;
        attributes.flags &= -3;
        attributes.softInputMode = 16;
        attributes.height = -1;
        boolean z = true;
        if (i >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        if (i >= 23) {
            window.setStatusBarColor(0);
        }
        this.frameLayout.setSystemUiVisibility(1280);
        if (i >= 21) {
            this.frameLayout.setOnApplyWindowInsetsListener(OverlayActionBarLayoutDialog$$ExternalSyntheticLambda0.INSTANCE);
        }
        if (i >= 26) {
            if (ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) < 0.9d) {
                z = false;
            }
            AndroidUtilities.setLightNavigationBar(window, z);
        }
    }

    public static /* synthetic */ WindowInsets lambda$onCreate$1(View view, WindowInsets windowInsets) {
        view.setPadding(0, 0, 0, windowInsets.getSystemWindowInsetBottom());
        return windowInsets;
    }

    public void addFragment(BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment, AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet());
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        this.actionBarLayout.onBackPressed();
        if (this.actionBarLayout.fragmentsStack.size() <= 1) {
            dismiss();
        }
    }

    @Override // org.telegram.ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate
    public boolean needCloseLastFragment(ActionBarLayout actionBarLayout) {
        if (actionBarLayout.fragmentsStack.size() <= 1) {
            dismiss();
        }
        return true;
    }

    /* loaded from: classes3.dex */
    public static final class EmptyFragment extends BaseFragment {
        private EmptyFragment() {
        }

        /* synthetic */ EmptyFragment(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public View createView(Context context) {
            this.actionBar.setAddToContainer(false);
            View view = new View(context);
            view.setBackgroundColor(0);
            return view;
        }
    }
}
