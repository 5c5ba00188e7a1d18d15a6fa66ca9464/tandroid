package org.telegram.ui.Components;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class PasscodeViewDialog extends Dialog {
    public final Context context;
    public final PasscodeView passcodeView;
    private final FrameLayout windowView;

    public PasscodeViewDialog(Context context) {
        super(context, R.style.TransparentDialog);
        this.context = context;
        FrameLayout frameLayout = new FrameLayout(context);
        this.windowView = frameLayout;
        if (Build.VERSION.SDK_INT >= 21) {
            frameLayout.setFitsSystemWindows(true);
            frameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.Components.PasscodeViewDialog.1
                @Override // android.view.View.OnApplyWindowInsetsListener
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets consumeSystemWindowInsets;
                    WindowInsets windowInsets2;
                    if (Build.VERSION.SDK_INT >= 30) {
                        windowInsets2 = WindowInsets.CONSUMED;
                        return windowInsets2;
                    }
                    consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
                    return consumeSystemWindowInsets;
                }
            });
        }
        PasscodeView passcodeView = new PasscodeView(context) { // from class: org.telegram.ui.Components.PasscodeViewDialog.2
            @Override // org.telegram.ui.Components.PasscodeView
            protected void onAnimationUpdate(float f) {
                LaunchActivity launchActivity = LaunchActivity.instance;
                if (launchActivity == null) {
                    return;
                }
                DrawerLayoutContainer drawerLayoutContainer = launchActivity.drawerLayoutContainer;
                drawerLayoutContainer.setScaleX(AndroidUtilities.lerp(1.0f, 1.25f, f));
                drawerLayoutContainer.setScaleY(AndroidUtilities.lerp(1.0f, 1.25f, f));
            }

            @Override // org.telegram.ui.Components.PasscodeView
            protected void onHidden() {
                PasscodeViewDialog.super.dismiss();
                LaunchActivity launchActivity = LaunchActivity.instance;
                if (launchActivity == null) {
                    return;
                }
                DrawerLayoutContainer drawerLayoutContainer = launchActivity.drawerLayoutContainer;
                drawerLayoutContainer.setScaleX(1.0f);
                drawerLayoutContainer.setScaleY(1.0f);
            }
        };
        this.passcodeView = passcodeView;
        frameLayout.addView(passcodeView, LayoutHelper.createFrame(-1, -1, 119));
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        LaunchActivity launchActivity;
        if (!this.passcodeView.onBackPressed() || (launchActivity = LaunchActivity.instance) == null) {
            return;
        }
        launchActivity.moveTaskToBack(true);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        LaunchActivity launchActivity;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0) {
            if (this.passcodeView.onBackPressed() && (launchActivity = LaunchActivity.instance) != null) {
                launchActivity.moveTaskToBack(true);
            }
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        LaunchActivity launchActivity;
        if (!this.passcodeView.onBackPressed() || (launchActivity = LaunchActivity.instance) == null) {
            return;
        }
        launchActivity.moveTaskToBack(true);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogNoAnimation);
        setContentView(this.windowView, new ViewGroup.LayoutParams(-1, -1));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -1;
        attributes.gravity = 119;
        attributes.dimAmount = 0.0f;
        int i = attributes.flags & (-3);
        attributes.flags = i;
        attributes.softInputMode = 16;
        if (!BuildVars.DEBUG_PRIVATE_VERSION) {
            attributes.flags = i | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
        }
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            attributes.flags |= -2013200128;
        }
        attributes.flags |= 1152;
        if (i2 >= 28) {
            attributes.layoutInDisplayCutoutMode = 1;
        }
        window.setAttributes(attributes);
        this.windowView.setSystemUiVisibility(256);
        AndroidUtilities.setLightNavigationBar(window, false);
    }
}
