package org.telegram.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.PopupSwipeBackLayout;
import org.telegram.ui.Components.VideoPlayer;
/* loaded from: classes4.dex */
public class ChooseQualityLayout {
    public final LinearLayout buttonsLayout;
    private final Callback callback;
    public final ActionBarPopupWindow.ActionBarPopupWindowLayout layout;

    /* loaded from: classes4.dex */
    public interface Callback {
        void onQualitySelected(int i, boolean z, boolean z2);
    }

    /* loaded from: classes4.dex */
    public static class QualityIcon extends Drawable {
        private final Drawable base;
        private final Paint bgPaint = new Paint(1);
        private final RectF rect = new RectF();
        private float rotation;
        public final AnimatedTextView.AnimatedTextDrawable text;

        public QualityIcon(Context context) {
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable();
            this.text = animatedTextDrawable;
            this.base = context.getResources().getDrawable(R.drawable.msg_settings).mutate();
            animatedTextDrawable.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
            animatedTextDrawable.setTextColor(-1);
            animatedTextDrawable.setTextSize(AndroidUtilities.dp(8.0f));
            animatedTextDrawable.setCallback(new Drawable.Callback() { // from class: org.telegram.ui.ChooseQualityLayout.QualityIcon.1
                @Override // android.graphics.drawable.Drawable.Callback
                public void invalidateDrawable(Drawable drawable) {
                    QualityIcon.this.invalidateSelf();
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                    QualityIcon.this.scheduleSelf(runnable, j);
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                    QualityIcon.this.unscheduleSelf(runnable);
                }
            });
            animatedTextDrawable.setGravity(17);
            animatedTextDrawable.setOverrideFullWidth(AndroidUtilities.displaySize.x);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            this.base.setBounds(bounds);
            canvas.save();
            canvas.rotate(this.rotation * (-180.0f), bounds.centerX(), bounds.centerY());
            this.base.draw(canvas);
            canvas.restore();
            this.bgPaint.setColor(Theme.getColor(Theme.key_featuredStickers_addButton));
            float width = bounds.left + (bounds.width() * 0.97f);
            float height = bounds.top + (bounds.height() * 0.75f);
            float dp = (AndroidUtilities.dp(5.0f) * this.text.isNotEmpty()) + this.text.getCurrentWidth();
            float dp2 = AndroidUtilities.dp(11.0f) / 2.0f;
            this.rect.set(width - dp, height - dp2, width, height + dp2);
            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), this.bgPaint);
            this.text.setBounds(this.rect);
            this.text.draw(canvas);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.base.getIntrinsicHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.base.getIntrinsicWidth();
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return this.base.getOpacity();
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            this.base.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.base.setColorFilter(colorFilter);
        }

        public void setRotation(float f) {
            this.rotation = f;
            invalidateSelf();
        }
    }

    public ChooseQualityLayout(Context context, final PopupSwipeBackLayout popupSwipeBackLayout, Callback callback) {
        this.callback = callback;
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context, 0, null);
        this.layout = actionBarPopupWindowLayout;
        actionBarPopupWindowLayout.setFitItems(true);
        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_arrow_back, LocaleController.getString(R.string.Back), false, null);
        addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChooseQualityLayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PopupSwipeBackLayout.this.closeForeground();
            }
        });
        addItem.setColors(-328966, -328966);
        addItem.setSelectorColor(268435455);
        View view = new FrameLayout(context) { // from class: org.telegram.ui.ChooseQualityLayout.1
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
            }
        };
        view.setMinimumWidth(AndroidUtilities.dp(196.0f));
        view.setBackgroundColor(-15198184);
        actionBarPopupWindowLayout.addView(view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (LocaleController.isRTL) {
            layoutParams.gravity = 5;
        }
        layoutParams.width = -1;
        layoutParams.height = AndroidUtilities.dp(8.0f);
        view.setLayoutParams(layoutParams);
        LinearLayout linearLayout = new LinearLayout(context);
        this.buttonsLayout = linearLayout;
        linearLayout.setOrientation(1);
        actionBarPopupWindowLayout.addView(linearLayout);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$1(int i, View view) {
        this.callback.onQualitySelected(i, true, true);
    }

    public boolean update(VideoPlayer videoPlayer) {
        String str;
        if (videoPlayer != null && videoPlayer.getQualitiesCount() > 1) {
            this.buttonsLayout.removeAllViews();
            final int i = -1;
            while (i < videoPlayer.getQualitiesCount()) {
                VideoPlayer.QualityUri quality = i == -1 ? null : videoPlayer.getQuality(i);
                String str2 = "";
                if (quality == null) {
                    str = LocaleController.getString(R.string.QualityAuto);
                } else if (SharedConfig.debugVideoQualities) {
                    String str3 = quality.width + "x" + quality.height;
                    if (quality.original) {
                        str3 = str3 + " (" + LocaleController.getString(R.string.QualityOriginal).toLowerCase() + ")";
                    }
                    str2 = "" + AndroidUtilities.formatFileSize((long) quality.bitrate).replace(" ", "") + "/s";
                    if (quality.codec != null) {
                        str2 = str2 + ", " + quality.codec;
                    }
                    str = str3;
                } else {
                    int min = Math.min(quality.width, quality.height);
                    if (Math.abs(min - 1080) < 30) {
                        min = 1080;
                    } else if (Math.abs(min - 720) < 30) {
                        min = 720;
                    } else if (Math.abs(min - 360) < 30) {
                        min = 360;
                    } else if (Math.abs(min - 240) < 30) {
                        min = NotificationCenter.goingToPreviewTheme;
                    } else if (Math.abs(min - 144) < 30) {
                        min = NotificationCenter.messagePlayingProgressDidChanged;
                    }
                    str = min + "p";
                }
                ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(this.buttonsLayout, 0, str, true, null);
                if (!TextUtils.isEmpty(str2)) {
                    addItem.setSubtext(str2);
                }
                addItem.setChecked(i == videoPlayer.getSelectedQuality());
                addItem.setColors(-328966, -328966);
                addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChooseQualityLayout$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ChooseQualityLayout.this.lambda$update$1(i, view);
                    }
                });
                addItem.setSelectorColor(268435455);
                i++;
            }
            return true;
        }
        return false;
    }
}
