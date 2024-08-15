package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ShareLocationDrawable;
/* loaded from: classes4.dex */
public class SendLocationCell extends FrameLayout {
    private SimpleTextView accurateTextView;
    private int currentAccount;
    private long dialogId;
    private ImageView imageView;
    private Runnable invalidateRunnable;
    private boolean live;
    private boolean liveDisable;
    private final AnimatedFloat progress;
    private final AnimatedFloat progressAlpha;
    private final AnimatedFloat progressScale;
    private RectF rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private final AnimatedTextView.AnimatedTextDrawable textDrawable;
    private SimpleTextView titleTextView;
    public boolean useDivider;

    public SendLocationCell(Context context, boolean z, boolean z2, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.invalidateRunnable = new Runnable() { // from class: org.telegram.ui.Cells.SendLocationCell.1
            @Override // java.lang.Runnable
            public void run() {
                SendLocationCell.this.checkText();
                SendLocationCell sendLocationCell = SendLocationCell.this;
                sendLocationCell.invalidate(((int) sendLocationCell.rect.left) - 5, ((int) SendLocationCell.this.rect.top) - 5, ((int) SendLocationCell.this.rect.right) + 5, ((int) SendLocationCell.this.rect.bottom) + 5);
                AndroidUtilities.runOnUIThread(SendLocationCell.this.invalidateRunnable, 1000L);
            }
        };
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.progress = new AnimatedFloat(this, 350L, cubicBezierInterpolator);
        this.progressAlpha = new AnimatedFloat(this, 350L, cubicBezierInterpolator);
        this.progressScale = new AnimatedFloat(this, 350L, cubicBezierInterpolator);
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(false, true, false);
        this.textDrawable = animatedTextDrawable;
        animatedTextDrawable.setAnimationProperties(0.3f, 0L, 320L, cubicBezierInterpolator);
        animatedTextDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        animatedTextDrawable.setTypeface(Typeface.DEFAULT_BOLD);
        animatedTextDrawable.setGravity(17);
        animatedTextDrawable.setCallback(this);
        this.resourcesProvider = resourcesProvider;
        this.live = z;
        this.liveDisable = z2;
        ImageView imageView = new ImageView(context);
        this.imageView = imageView;
        boolean z3 = LocaleController.isRTL;
        addView(imageView, LayoutHelper.createFrame(46, 46.0f, (z3 ? 5 : 3) | 16, z3 ? 0.0f : 13.0f, 0.0f, z3 ? 13.0f : 0.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.titleTextView = simpleTextView;
        simpleTextView.setTextSize(16);
        this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        this.titleTextView.setTypeface(AndroidUtilities.bold());
        View view = this.titleTextView;
        boolean z4 = LocaleController.isRTL;
        addView(view, LayoutHelper.createFrame(-1, 20.0f, (z4 ? 5 : 3) | 48, z4 ? 16.0f : 73.0f, 9.33f, z4 ? 73.0f : 16.0f, 0.0f));
        SimpleTextView simpleTextView2 = new SimpleTextView(context);
        this.accurateTextView = simpleTextView2;
        simpleTextView2.setTextSize(14);
        this.accurateTextView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.accurateTextView.setGravity(LocaleController.isRTL ? 5 : 3);
        View view2 = this.accurateTextView;
        boolean z5 = LocaleController.isRTL;
        addView(view2, LayoutHelper.createFrame(-1, 20.0f, (z5 ? 5 : 3) | 48, z5 ? 16.0f : 73.0f, 33.0f, z5 ? 73.0f : 16.0f, 0.0f));
        updateImage();
        setWillNotDraw(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00d3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateImage() {
        int i;
        int i2;
        int i3;
        this.titleTextView.setTag(Integer.valueOf(this.live ? this.liveDisable ? Theme.key_text_RedBold : Theme.key_location_sendLiveLocationText : Theme.key_location_sendLocationText));
        this.titleTextView.setTextColor(getThemedColor(this.live ? this.liveDisable ? Theme.key_text_RedBold : Theme.key_location_sendLiveLocationText : Theme.key_location_sendLocationText));
        ImageView imageView = this.imageView;
        if (!this.live) {
            i = Theme.key_location_sendLocationBackground;
            i2 = Theme.key_location_sendLocationIcon;
        } else if (this.liveDisable) {
            i3 = Theme.key_color_red;
            imageView.setTag(Integer.valueOf(i3));
            Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(46.0f), getThemedColor(!this.live ? this.liveDisable ? Theme.key_color_red : Theme.key_location_sendLiveLocationBackground : Theme.key_location_sendLocationBackground), getThemedColor(!this.live ? this.liveDisable ? Theme.key_color_red : Theme.key_location_sendLiveLocationBackground : Theme.key_location_sendLocationBackground));
            if (!this.live) {
                this.rect = new RectF();
                ShareLocationDrawable shareLocationDrawable = new ShareLocationDrawable(getContext(), this.liveDisable ? 5 : 4);
                shareLocationDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_location_sendLiveLocationIcon), PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable = new CombinedDrawable(createSimpleSelectorCircleDrawable, shareLocationDrawable);
                combinedDrawable.setCustomSize(AndroidUtilities.dp(46.0f), AndroidUtilities.dp(46.0f));
                this.imageView.setBackgroundDrawable(combinedDrawable);
                if (this.liveDisable) {
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
                AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
                return;
            }
            Drawable mutate = getResources().getDrawable(R.drawable.pin).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_location_sendLocationIcon), PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable2 = new CombinedDrawable(createSimpleSelectorCircleDrawable, mutate);
            combinedDrawable2.setCustomSize(AndroidUtilities.dp(46.0f), AndroidUtilities.dp(46.0f));
            combinedDrawable2.setIconSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            this.imageView.setBackgroundDrawable(combinedDrawable2);
            return;
        } else {
            i = Theme.key_location_sendLiveLocationBackground;
            i2 = Theme.key_location_sendLiveLocationIcon;
        }
        i3 = i + i2;
        imageView.setTag(Integer.valueOf(i3));
        Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(46.0f), getThemedColor(!this.live ? this.liveDisable ? Theme.key_color_red : Theme.key_location_sendLiveLocationBackground : Theme.key_location_sendLocationBackground), getThemedColor(!this.live ? this.liveDisable ? Theme.key_color_red : Theme.key_location_sendLiveLocationBackground : Theme.key_location_sendLocationBackground));
        if (!this.live) {
        }
    }

    private ImageView getImageView() {
        return this.imageView;
    }

    public void setHasLocation(boolean z) {
        if (LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId) == null) {
            this.titleTextView.setAlpha(z ? 1.0f : 0.5f);
            this.accurateTextView.setAlpha(z ? 1.0f : 0.5f);
            this.imageView.setAlpha(z ? 1.0f : 0.5f);
        }
        if (this.live) {
            checkText();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.rect != null) {
            AndroidUtilities.cancelRunOnUIThread(this.invalidateRunnable);
            AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000L);
        }
    }

    public void setText(String str, String str2) {
        this.titleTextView.setText(str);
        this.accurateTextView.setText(str2);
    }

    public void setDialogId(long j) {
        this.dialogId = j;
        if (this.live) {
            checkText();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkText() {
        LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
        if (sharingLocationInfo != null) {
            if (this.liveDisable) {
                String string = LocaleController.getString(R.string.StopLiveLocation);
                TLRPC$Message tLRPC$Message = sharingLocationInfo.messageObject.messageOwner;
                int i = tLRPC$Message.edit_date;
                setText(string, LocaleController.formatLocationUpdateDate(i != 0 ? i : tLRPC$Message.date));
                return;
            }
            setText(LocaleController.getString(R.string.SharingLiveLocation), LocaleController.getString(R.string.SharingLiveLocationAdd));
            return;
        }
        setText(LocaleController.getString("SendLiveLocation", R.string.SendLiveLocation), LocaleController.getString("SendLiveLocationInfo", R.string.SendLiveLocationInfo));
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.textDrawable || super.verifyDrawable(drawable);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f;
        int i;
        Paint themePaint;
        if (this.useDivider && (themePaint = Theme.getThemePaint("paintDivider", this.resourcesProvider)) != null) {
            canvas.drawRect(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(73.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(73.0f) : 0), getMeasuredHeight(), themePaint);
        }
        if (this.liveDisable) {
            return;
        }
        LocationController.SharingLocationInfo sharingLocationInfo = LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId);
        float f2 = this.progress.get();
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        if (sharingLocationInfo != null && (i = sharingLocationInfo.stopTime) >= currentTime && sharingLocationInfo.period != Integer.MAX_VALUE) {
            f2 = Math.abs(i - currentTime) / sharingLocationInfo.period;
            f = this.progressAlpha.set(true);
        } else {
            f = this.progressAlpha.set(false);
        }
        float f3 = f2;
        float f4 = f;
        if (f4 <= 0.0f) {
            return;
        }
        if (LocaleController.isRTL) {
            this.rect.set(AndroidUtilities.dp(13.0f), (getMeasuredHeight() / 2.0f) - AndroidUtilities.dp(15.0f), AndroidUtilities.dp(43.0f), (getMeasuredHeight() / 2.0f) + AndroidUtilities.dp(15.0f));
        } else {
            this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(43.0f), (getMeasuredHeight() / 2.0f) - AndroidUtilities.dp(15.0f), getMeasuredWidth() - AndroidUtilities.dp(13.0f), (getMeasuredHeight() / 2.0f) + AndroidUtilities.dp(15.0f));
        }
        canvas.save();
        float f5 = 1.0f;
        float lerp = AndroidUtilities.lerp(0.6f, 1.0f, f4);
        canvas.scale(lerp, lerp, this.rect.centerX(), this.rect.centerY());
        int themedColor = getThemedColor(Theme.key_location_liveLocationProgress);
        Theme.chat_radialProgress2Paint.setColor(themedColor);
        int alpha = Theme.chat_radialProgress2Paint.getAlpha();
        float f6 = alpha;
        Theme.chat_radialProgress2Paint.setAlpha((int) (0.2f * f6 * f4));
        canvas.drawArc(this.rect, -90.0f, 360.0f, false, Theme.chat_radialProgress2Paint);
        Theme.chat_radialProgress2Paint.setAlpha((int) (f6 * f4));
        canvas.drawArc(this.rect, -90.0f, (-360.0f) * this.progress.set(f3), false, Theme.chat_radialProgress2Paint);
        Theme.chat_radialProgress2Paint.setAlpha(alpha);
        if (sharingLocationInfo != null) {
            this.textDrawable.setText(LocaleController.formatLocationLeftTime(Math.abs(sharingLocationInfo.stopTime - currentTime)));
        }
        int length = this.textDrawable.getText().length();
        AnimatedFloat animatedFloat = this.progressScale;
        if (length > 4) {
            f5 = 0.75f;
        } else if (length > 3) {
            f5 = 0.85f;
        }
        float f7 = animatedFloat.set(f5);
        canvas.scale(f7, f7, this.rect.centerX(), this.rect.centerY());
        this.textDrawable.setTextColor(themedColor);
        this.textDrawable.setAlpha((int) (f4 * 255.0f));
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.textDrawable;
        RectF rectF = this.rect;
        RectF rectF2 = this.rect;
        animatedTextDrawable.setBounds((int) rectF.left, (int) (rectF.centerY() - AndroidUtilities.dp(13.0f)), (int) rectF2.right, (int) (rectF2.centerY() + AndroidUtilities.dp(12.0f)));
        this.textDrawable.draw(canvas);
        canvas.restore();
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }
}
