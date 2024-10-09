package org.telegram.ui.Components.Reactions;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Stories.RoundRectOutlineProvider;

/* loaded from: classes3.dex */
public class BackSpaceButtonView extends FrameLayout {
    private final ImageView backspaceButton;
    private boolean backspaceOnce;
    private boolean backspacePressed;
    private Utilities.Callback onBackspace;
    private final Theme.ResourcesProvider resourcesProvider;

    public BackSpaceButtonView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        ImageView imageView = new ImageView(context) { // from class: org.telegram.ui.Components.Reactions.BackSpaceButtonView.1
            private long lastClick = 0;

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (System.currentTimeMillis() < this.lastClick + 350) {
                        return false;
                    }
                    this.lastClick = System.currentTimeMillis();
                    BackSpaceButtonView.this.backspacePressed = true;
                    BackSpaceButtonView.this.backspaceOnce = false;
                    BackSpaceButtonView.this.postBackspaceRunnable(350);
                } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                    BackSpaceButtonView.this.backspacePressed = false;
                    if (!BackSpaceButtonView.this.backspaceOnce && BackSpaceButtonView.this.onBackspace != null) {
                        BackSpaceButtonView.this.onBackspace.run(Boolean.FALSE);
                        BackSpaceButtonView.this.backspaceButton.performHapticFeedback(3);
                    }
                }
                super.onTouchEvent(motionEvent);
                return true;
            }
        };
        this.backspaceButton = imageView;
        imageView.setHapticFeedbackEnabled(true);
        imageView.setImageResource(R.drawable.smiles_tab_clear);
        int themedColor = getThemedColor(Theme.key_chat_emojiPanelBackspace);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        imageView.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setContentDescription(LocaleController.getString(R.string.AccDescrBackspace));
        imageView.setFocusable(true);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.Reactions.BackSpaceButtonView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BackSpaceButtonView.lambda$new$0(view);
            }
        });
        addView(imageView, LayoutHelper.createFrame(36, 36, 17));
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(36.0f), getThemedColor(Theme.key_windowBackgroundWhite), Theme.getColor(Theme.key_listSelector));
        if (Build.VERSION.SDK_INT >= 21) {
            imageView.setBackground(createSimpleSelectorCircleDrawable);
            imageView.setOutlineProvider(new RoundRectOutlineProvider(18));
            imageView.setElevation(AndroidUtilities.dp(1.0f));
            imageView.setClipToOutline(true);
        } else {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            imageView.setBackground(combinedDrawable);
        }
        setClickable(true);
    }

    private int getThemedColor(int i) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        return resourcesProvider != null ? resourcesProvider.getColor(i) : Theme.getColor(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(View view) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postBackspaceRunnable$1(int i) {
        if (this.backspacePressed) {
            Utilities.Callback callback = this.onBackspace;
            if (callback != null) {
                callback.run(Boolean.valueOf(i < 300));
                this.backspaceButton.performHapticFeedback(3);
            }
            this.backspaceOnce = true;
            postBackspaceRunnable(Math.max(50, i - 100));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postBackspaceRunnable(final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Reactions.BackSpaceButtonView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BackSpaceButtonView.this.lambda$postBackspaceRunnable$1(i);
            }
        }, i);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
    }

    public void setOnBackspace(Utilities.Callback<Boolean> callback) {
        this.onBackspace = callback;
    }
}
