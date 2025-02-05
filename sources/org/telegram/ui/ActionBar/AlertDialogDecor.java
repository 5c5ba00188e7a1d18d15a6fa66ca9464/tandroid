package org.telegram.ui.ActionBar;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

/* loaded from: classes4.dex */
public class AlertDialogDecor extends AlertDialog {
    private static final int[] ATTRS = {R.attr.windowEnterAnimation, R.attr.windowExitAnimation};
    private View contentView;
    private View dimView;
    private boolean isDismissed;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnShowListener onShowListener;
    private long openDelay;
    private int resEnterAnimation;
    private int resExitAnimation;
    private View rootView;
    private final Runnable showRunnable;

    public static class Builder extends AlertDialog.Builder {
        public Builder(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, 0, resourcesProvider);
        }

        @Override // org.telegram.ui.ActionBar.AlertDialog.Builder
        protected AlertDialog createAlertDialog(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            return new AlertDialogDecor(context, i, resourcesProvider);
        }
    }

    public AlertDialogDecor(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, i, resourcesProvider);
        this.isDismissed = false;
        this.openDelay = 0L;
        this.showRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.AlertDialogDecor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AlertDialogDecor.this.lambda$new$0();
            }
        };
    }

    private void extractAnimations() {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.windowAnimationStyle, typedValue, true);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(typedValue.resourceId, ATTRS);
        this.resEnterAnimation = obtainStyledAttributes.getResourceId(0, -1);
        this.resExitAnimation = obtainStyledAttributes.getResourceId(1, -1);
        obtainStyledAttributes.recycle();
    }

    private Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextThemeWrapper) {
            return getActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ViewGroup getDecorView() {
        return (ViewGroup) getActivity(getContext()).getWindow().getDecorView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.rootView.setVisibility(0);
        this.dimView.setAlpha(0.0f);
        this.contentView.startAnimation(AnimationUtils.loadAnimation(getContext(), this.resEnterAnimation));
        this.dimView.animate().setDuration(300L).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.AlertDialogDecor.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (AlertDialogDecor.this.onShowListener != null) {
                    AlertDialogDecor.this.onShowListener.onShow(AlertDialogDecor.this);
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$1(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsetsCompat lambda$show$2(FrameLayout frameLayout, View view, WindowInsetsCompat windowInsetsCompat) {
        Rect rect = new Rect();
        if (Build.VERSION.SDK_INT >= 30) {
            Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.ime() | WindowInsetsCompat.Type.systemBars());
            rect.set(insets.left, insets.top, insets.right, insets.bottom);
        } else {
            rect.set(windowInsetsCompat.getStableInsetLeft(), windowInsetsCompat.getStableInsetTop(), windowInsetsCompat.getStableInsetRight(), windowInsetsCompat.getStableInsetBottom());
        }
        frameLayout.setPadding(rect.left, rect.top, rect.right, rect.bottom + AndroidUtilities.navigationBarHeight);
        frameLayout.requestLayout();
        return windowInsetsCompat;
    }

    @Override // org.telegram.ui.ActionBar.AlertDialog, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (isShowing() && !this.isDismissed) {
            this.isDismissed = true;
            AndroidUtilities.cancelRunOnUIThread(this.showRunnable);
            if (this.rootView.getVisibility() != 0) {
                getDecorView().removeView(this.rootView);
                return;
            }
            Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), this.resExitAnimation);
            loadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: org.telegram.ui.ActionBar.AlertDialogDecor.2
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    AlertDialogDecor.this.contentView.setAlpha(0.0f);
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }
            });
            this.contentView.clearAnimation();
            this.contentView.startAnimation(loadAnimation);
            this.dimView.animate().setListener(null).cancel();
            this.dimView.animate().setDuration(300L).alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ActionBar.AlertDialogDecor.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AlertDialogDecor.this.getDecorView().removeView(AlertDialogDecor.this.rootView);
                    if (AlertDialogDecor.this.onDismissListener != null) {
                        AlertDialogDecor.this.onDismissListener.onDismiss(AlertDialogDecor.this);
                    }
                }
            }).start();
        }
    }

    @Override // android.app.Dialog
    public boolean isShowing() {
        return (getDecorView().indexOfChild(this.rootView) == -1 || this.isDismissed) ? false : true;
    }

    @Override // android.app.Dialog
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override // android.app.Dialog
    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    @Override // org.telegram.ui.ActionBar.AlertDialog, android.app.Dialog
    public void show() {
        extractAnimations();
        setDismissDialogByButtons(true);
        View inflateContent = inflateContent(false);
        this.contentView = inflateContent;
        inflateContent.setClickable(true);
        this.contentView.setFitsSystemWindows(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ActionBar.AlertDialogDecor$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlertDialogDecor.this.lambda$show$1(view);
            }
        });
        View view = new View(getContext());
        this.dimView = view;
        view.setBackgroundColor(Theme.multAlpha(-16777216, attributes.dimAmount));
        frameLayout.addView(this.dimView, new FrameLayout.LayoutParams(-1, -1));
        final FrameLayout frameLayout2 = new FrameLayout(getContext());
        frameLayout2.addView(this.contentView, new FrameLayout.LayoutParams(-1, -2, 17));
        frameLayout.addView(frameLayout2, new FrameLayout.LayoutParams(attributes.width, -2, 17));
        this.rootView = frameLayout;
        getDecorView().addView(this.rootView);
        ViewCompat.requestApplyInsets(this.rootView);
        ViewCompat.setOnApplyWindowInsetsListener(this.rootView, new OnApplyWindowInsetsListener() { // from class: org.telegram.ui.ActionBar.AlertDialogDecor$$ExternalSyntheticLambda2
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view2, WindowInsetsCompat windowInsetsCompat) {
                WindowInsetsCompat lambda$show$2;
                lambda$show$2 = AlertDialogDecor.lambda$show$2(frameLayout2, view2, windowInsetsCompat);
                return lambda$show$2;
            }
        });
        this.rootView.setVisibility(4);
        long j = this.openDelay;
        if (j == 0) {
            this.showRunnable.run();
        } else {
            AndroidUtilities.runOnUIThread(this.showRunnable, j);
        }
    }

    @Override // org.telegram.ui.ActionBar.AlertDialog
    public void showDelayed(long j) {
        if (isShowing()) {
            return;
        }
        this.openDelay = j;
        show();
    }

    @Override // org.telegram.ui.ActionBar.AlertDialog
    protected boolean supportsNativeBlur() {
        return false;
    }
}
