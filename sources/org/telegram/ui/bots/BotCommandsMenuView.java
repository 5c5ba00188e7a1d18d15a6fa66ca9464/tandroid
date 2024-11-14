package org.telegram.ui.bots;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StaticLayoutEx;

/* loaded from: classes5.dex */
public class BotCommandsMenuView extends View {
    final MenuDrawable backDrawable;
    Drawable backgroundDrawable;
    boolean drawBackgroundDrawable;
    float expandProgress;
    public boolean expanded;
    boolean isOpened;
    public boolean isWebView;
    boolean isWebViewOpened;
    int lastSize;
    private String menuText;
    StaticLayout menuTextLayout;
    private float menuTextWidth;
    final Paint paint;
    final RectF rectTmp;
    final TextPaint textPaint;
    RLottieDrawable webViewAnimation;

    /* loaded from: classes5.dex */
    public static class BotCommandView extends LinearLayout {
        TextView command;
        String commandStr;
        TextView description;

        public BotCommandView(Context context) {
            super(context);
            setOrientation(0);
            setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(8.0f));
            TextView textView = new TextView(context) { // from class: org.telegram.ui.bots.BotCommandsMenuView.BotCommandView.1
                @Override // android.widget.TextView
                public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                    super.setText(Emoji.replaceEmoji(charSequence, getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false), bufferType);
                }
            };
            this.description = textView;
            NotificationCenter.listenEmojiLoading(textView);
            this.description.setTextSize(1, 16.0f);
            TextView textView2 = this.description;
            int i = Theme.key_windowBackgroundWhiteBlackText;
            textView2.setTextColor(Theme.getColor(i));
            this.description.setTag(Integer.valueOf(i));
            this.description.setMaxLines(2);
            this.description.setEllipsize(TextUtils.TruncateAt.END);
            addView(this.description, LayoutHelper.createLinear(-1, -2, 1.0f, 16, 0, 0, AndroidUtilities.dp(8.0f), 0));
            TextView textView3 = new TextView(context);
            this.command = textView3;
            textView3.setTextSize(1, 14.0f);
            TextView textView4 = this.command;
            int i2 = Theme.key_windowBackgroundWhiteGrayText;
            textView4.setTextColor(Theme.getColor(i2));
            this.command.setTag(Integer.valueOf(i2));
            addView(this.command, LayoutHelper.createLinear(-2, -2, 0.0f, 16));
        }

        public String getCommand() {
            return this.commandStr;
        }
    }

    /* loaded from: classes5.dex */
    public static class BotCommandsAdapter extends RecyclerListView.SelectionAdapter {
        ArrayList newResult = new ArrayList();
        ArrayList newResultHelp = new ArrayList();

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.newResult.size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            BotCommandView botCommandView = (BotCommandView) viewHolder.itemView;
            botCommandView.command.setText((CharSequence) this.newResult.get(i));
            botCommandView.description.setText((CharSequence) this.newResultHelp.get(i));
            botCommandView.commandStr = (String) this.newResult.get(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            BotCommandView botCommandView = new BotCommandView(viewGroup.getContext());
            botCommandView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(botCommandView);
        }

        public void setBotInfo(LongSparseArray longSparseArray) {
            this.newResult.clear();
            this.newResultHelp.clear();
            for (int i = 0; i < longSparseArray.size(); i++) {
                TL_bots.BotInfo botInfo = (TL_bots.BotInfo) longSparseArray.valueAt(i);
                for (int i2 = 0; i2 < botInfo.commands.size(); i2++) {
                    TLRPC.TL_botCommand tL_botCommand = botInfo.commands.get(i2);
                    if (tL_botCommand != null && tL_botCommand.command != null) {
                        this.newResult.add("/" + tL_botCommand.command);
                        this.newResultHelp.add(tL_botCommand.description);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public BotCommandsMenuView(Context context) {
        super(context);
        this.rectTmp = new RectF();
        this.paint = new Paint(1);
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        MenuDrawable menuDrawable = new MenuDrawable() { // from class: org.telegram.ui.bots.BotCommandsMenuView.1
            @Override // android.graphics.drawable.Drawable
            public void invalidateSelf() {
                super.invalidateSelf();
                BotCommandsMenuView.this.invalidate();
            }
        };
        this.backDrawable = menuDrawable;
        int i = R.raw.bot_webview_sheet_to_cross;
        this.webViewAnimation = new RLottieDrawable(i, String.valueOf(i) + hashCode(), AndroidUtilities.dp(20.0f), AndroidUtilities.dp(20.0f)) { // from class: org.telegram.ui.bots.BotCommandsMenuView.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RLottieDrawable
            public void invalidateInternal() {
                super.invalidateInternal();
                BotCommandsMenuView.this.invalidate();
            }

            @Override // android.graphics.drawable.Drawable
            public void invalidateSelf() {
                super.invalidateSelf();
                BotCommandsMenuView.this.invalidate();
            }
        };
        this.menuText = LocaleController.getString(R.string.BotsMenuTitle);
        this.drawBackgroundDrawable = true;
        updateColors();
        menuDrawable.setMiniIcon(true);
        menuDrawable.setRotateToBack(false);
        menuDrawable.setRotation(0.0f, false);
        menuDrawable.setCallback(this);
        textPaint.setTypeface(AndroidUtilities.bold());
        menuDrawable.setRoundCap();
        Drawable createSimpleSelectorRoundRectDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(16.0f), 0, Theme.getColor(Theme.key_featuredStickers_addButtonPressed));
        this.backgroundDrawable = createSimpleSelectorRoundRectDrawable;
        createSimpleSelectorRoundRectDrawable.setCallback(this);
        setContentDescription(LocaleController.getString("AccDescrBotMenu", R.string.AccDescrBotMenu));
    }

    private void updateColors() {
        this.paint.setColor(Theme.getColor(Theme.key_chat_messagePanelVoiceBackground));
        int color = Theme.getColor(Theme.key_chat_messagePanelVoiceDuration);
        this.backDrawable.setBackColor(color);
        this.backDrawable.setIconColor(color);
        RLottieDrawable rLottieDrawable = this.webViewAnimation;
        if (rLottieDrawable != null) {
            rLottieDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
        this.textPaint.setColor(color);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00d1  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void dispatchDraw(Canvas canvas) {
        boolean z;
        float interpolation;
        boolean z2;
        if (this.menuTextLayout != null) {
            boolean z3 = this.expanded;
            if (z3) {
                float f = this.expandProgress;
                if (f != 1.0f) {
                    float f2 = f + 0.10666667f;
                    this.expandProgress = f2;
                    if (f2 > 1.0f) {
                        this.expandProgress = 1.0f;
                        z = true;
                        interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.expandProgress);
                        if (z && interpolation > 0.0f) {
                            this.textPaint.setAlpha((int) (255.0f * interpolation));
                        }
                        if (this.drawBackgroundDrawable) {
                            this.rectTmp.set(0.0f, 0.0f, AndroidUtilities.dp(40.0f) + ((this.menuTextWidth + AndroidUtilities.dp(4.0f)) * interpolation), getMeasuredHeight());
                            canvas.drawRoundRect(this.rectTmp, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), this.paint);
                            Drawable drawable = this.backgroundDrawable;
                            RectF rectF = this.rectTmp;
                            drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                            this.backgroundDrawable.draw(canvas);
                        }
                        z2 = this.isWebView;
                        canvas.save();
                        if (z2) {
                            canvas.translate(AndroidUtilities.dp(9.5f), AndroidUtilities.dp(6.0f));
                            RLottieDrawable rLottieDrawable = this.webViewAnimation;
                            rLottieDrawable.setBounds(0, 0, rLottieDrawable.getMinimumWidth(), rLottieDrawable.getMinimumHeight());
                            rLottieDrawable.draw(canvas);
                            canvas.restore();
                            if (rLottieDrawable.isRunning()) {
                                invalidate();
                            }
                        } else {
                            canvas.translate(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(4.0f));
                            this.backDrawable.draw(canvas);
                            canvas.restore();
                        }
                        if (interpolation > 0.0f) {
                            canvas.save();
                            canvas.translate(AndroidUtilities.dp(34.0f), (getMeasuredHeight() - this.menuTextLayout.getHeight()) / 2.0f);
                            this.menuTextLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (z) {
                            onTranslationChanged((this.menuTextWidth + AndroidUtilities.dp(4.0f)) * interpolation);
                        }
                    }
                    invalidate();
                    z = true;
                    interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.expandProgress);
                    if (z) {
                        this.textPaint.setAlpha((int) (255.0f * interpolation));
                    }
                    if (this.drawBackgroundDrawable) {
                    }
                    z2 = this.isWebView;
                    canvas.save();
                    if (z2) {
                    }
                    if (interpolation > 0.0f) {
                    }
                    if (z) {
                    }
                }
            }
            if (!z3) {
                float f3 = this.expandProgress;
                if (f3 != 0.0f) {
                    float f4 = f3 - 0.10666667f;
                    this.expandProgress = f4;
                    if (f4 < 0.0f) {
                        this.expandProgress = 0.0f;
                        z = true;
                        interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.expandProgress);
                        if (z) {
                        }
                        if (this.drawBackgroundDrawable) {
                        }
                        z2 = this.isWebView;
                        canvas.save();
                        if (z2) {
                        }
                        if (interpolation > 0.0f) {
                        }
                        if (z) {
                        }
                    }
                    invalidate();
                    z = true;
                    interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.expandProgress);
                    if (z) {
                    }
                    if (this.drawBackgroundDrawable) {
                    }
                    z2 = this.isWebView;
                    canvas.save();
                    if (z2) {
                    }
                    if (interpolation > 0.0f) {
                    }
                    if (z) {
                    }
                }
            }
            z = false;
            interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.expandProgress);
            if (z) {
            }
            if (this.drawBackgroundDrawable) {
            }
            z2 = this.isWebView;
            canvas.save();
            if (z2) {
            }
            if (interpolation > 0.0f) {
            }
            if (z) {
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.backgroundDrawable.setState(getDrawableState());
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.backgroundDrawable.jumpToCurrentState();
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.webViewAnimation.setMasterParent(this);
        this.webViewAnimation.setCurrentParentView(this);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.webViewAnimation.setMasterParent(this);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = (View.MeasureSpec.getSize(i) + View.MeasureSpec.getSize(i2)) << 16;
        if (this.lastSize != size || this.menuTextLayout == null) {
            this.backDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.textPaint.setTextSize(AndroidUtilities.dp(15.0f));
            this.lastSize = size;
            int i3 = (int) (AndroidUtilities.displaySize.x * 0.6f);
            StaticLayout createStaticLayout = StaticLayoutEx.createStaticLayout(Emoji.replaceEmoji(this.menuText, this.textPaint.getFontMetricsInt(), false), this.textPaint, i3, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, i3, 1);
            this.menuTextLayout = createStaticLayout;
            this.menuTextWidth = createStaticLayout.getLineCount() > 0 ? this.menuTextLayout.getLineWidth(0) : 0.0f;
        }
        onTranslationChanged((this.menuTextWidth + AndroidUtilities.dp(4.0f)) * this.expandProgress);
        int dp = AndroidUtilities.dp(40.0f);
        if (this.expanded) {
            dp += ((int) this.menuTextWidth) + AndroidUtilities.dp(4.0f);
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(dp, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
    }

    protected void onTranslationChanged(float f) {
    }

    public void setDrawBackgroundDrawable(boolean z) {
        this.drawBackgroundDrawable = z;
        invalidate();
    }

    public void setExpanded(boolean z, boolean z2) {
        if (this.expanded != z) {
            this.expanded = z;
            if (!z2) {
                this.expandProgress = z ? 1.0f : 0.0f;
            }
            requestLayout();
            invalidate();
        }
    }

    public boolean setMenuText(String str) {
        if (str == null) {
            str = LocaleController.getString(R.string.BotsMenuTitle);
        }
        String str2 = this.menuText;
        boolean z = str2 == null || !str2.equals(str);
        this.menuText = str;
        this.menuTextLayout = null;
        requestLayout();
        return z;
    }

    public void setOpened(boolean z) {
        if (this.isOpened != z) {
            this.isOpened = z;
        }
        if (!this.isWebView) {
            this.backDrawable.setRotation(z ? 1.0f : 0.0f, true);
            return;
        }
        if (this.isWebViewOpened != z) {
            RLottieDrawable rLottieDrawable = this.webViewAnimation;
            rLottieDrawable.stop();
            rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
            rLottieDrawable.setCustomEndFrame(z ? rLottieDrawable.getFramesCount() : 1);
            rLottieDrawable.start();
            this.isWebViewOpened = z;
        }
    }

    public void setWebView(boolean z) {
        this.isWebView = z;
        invalidate();
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || this.backgroundDrawable == drawable;
    }
}
