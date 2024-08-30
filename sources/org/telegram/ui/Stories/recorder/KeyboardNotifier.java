package org.telegram.ui.Stories.recorder;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
/* loaded from: classes4.dex */
public class KeyboardNotifier {
    private boolean awaitingKeyboard;
    public boolean ignoring;
    private int keyboardHeight;
    private int lastKeyboardHeight;
    private final Utilities.Callback listener;
    private final ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private final View.OnLayoutChangeListener onLayoutChangeListener;
    private View realRootView;
    private final Rect rect;
    private final View rootView;

    public KeyboardNotifier(View view, Utilities.Callback callback) {
        this(view, false, callback);
    }

    public KeyboardNotifier(final View view, final boolean z, Utilities.Callback callback) {
        this.rect = new Rect();
        View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: org.telegram.ui.Stories.recorder.KeyboardNotifier$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                KeyboardNotifier.this.lambda$new$0(view2, i, i2, i3, i4, i5, i6, i7, i8);
            }
        };
        this.onLayoutChangeListener = onLayoutChangeListener;
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.Stories.recorder.KeyboardNotifier$$ExternalSyntheticLambda1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                KeyboardNotifier.this.update();
            }
        };
        this.onGlobalLayoutListener = onGlobalLayoutListener;
        this.rootView = view;
        this.listener = callback;
        this.realRootView = view;
        if (view.isAttachedToWindow()) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
            view.addOnLayoutChangeListener(onLayoutChangeListener);
        }
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: org.telegram.ui.Stories.recorder.KeyboardNotifier.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view2) {
                if (z) {
                    KeyboardNotifier.this.realRootView = view2.getRootView();
                }
                view.getViewTreeObserver().addOnGlobalLayoutListener(KeyboardNotifier.this.onGlobalLayoutListener);
                view.addOnLayoutChangeListener(KeyboardNotifier.this.onLayoutChangeListener);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view2) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(KeyboardNotifier.this.onGlobalLayoutListener);
                view.removeOnLayoutChangeListener(KeyboardNotifier.this.onLayoutChangeListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        update();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update() {
        if (this.ignoring) {
            return;
        }
        this.rootView.getWindowVisibleDisplayFrame(this.rect);
        View view = this.realRootView;
        if (view == null) {
            view = this.rootView;
        }
        int height = view.getHeight() - this.rect.bottom;
        this.keyboardHeight = height;
        boolean z = this.lastKeyboardHeight != height;
        this.lastKeyboardHeight = height;
        if (z) {
            fire();
        }
    }

    public void awaitKeyboard() {
        this.awaitingKeyboard = true;
    }

    public void fire() {
        if (this.awaitingKeyboard) {
            if (this.keyboardHeight < AndroidUtilities.navigationBarHeight + AndroidUtilities.dp(20.0f)) {
                return;
            }
            this.awaitingKeyboard = false;
        }
        Utilities.Callback callback = this.listener;
        if (callback != null) {
            callback.run(Integer.valueOf(this.keyboardHeight));
        }
    }

    public int getKeyboardHeight() {
        return this.keyboardHeight;
    }

    public void ignore(boolean z) {
        this.ignoring = z;
        update();
    }

    public boolean keyboardVisible() {
        return this.keyboardHeight > AndroidUtilities.navigationBarHeight + AndroidUtilities.dp(20.0f) || this.awaitingKeyboard;
    }
}
