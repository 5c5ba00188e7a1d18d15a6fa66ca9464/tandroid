package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.DotDividerSpan;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.FilteredSearchView;

/* loaded from: classes4.dex */
public abstract class SharedAudioCell extends FrameLayout implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private int TAG;
    private boolean buttonPressed;
    private int buttonState;
    private int buttonX;
    private int buttonY;
    private StaticLayout captionLayout;
    AnimatedEmojiSpan.EmojiGroupedSpans captionLayoutEmojis;
    private float captionLayoutLeft;
    private float captionLayoutWidth;
    private TextPaint captionTextPaint;
    private int captionY;
    private CheckBox2 checkBox;
    private boolean checkForButtonPress;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private StaticLayout dateLayout;
    private int dateLayoutX;
    private TextPaint description2TextPaint;
    private StaticLayout descriptionLayout;
    AnimatedEmojiSpan.EmojiGroupedSpans descriptionLayoutEmojis;
    private float descriptionLayoutLeft;
    private float descriptionLayoutWidth;
    private int descriptionY;
    private SpannableStringBuilder dotSpan;
    float enterAlpha;
    FlickerLoadingView globalGradientView;
    private int hasMiniProgress;
    private boolean miniButtonPressed;
    private int miniButtonState;
    private boolean needDivider;
    private RadialProgress2 radialProgress;
    private final Theme.ResourcesProvider resourcesProvider;
    boolean showName;
    float showNameProgress;
    boolean showReorderIcon;
    float showReorderIconProgress;
    private StaticLayout titleLayout;
    AnimatedEmojiSpan.EmojiGroupedSpans titleLayoutEmojis;
    private float titleLayoutLeft;
    private float titleLayoutWidth;
    TextPaint titlePaint;
    private int titleY;
    private int viewType;

    public SharedAudioCell(Context context) {
        this(context, 0, null);
    }

    public SharedAudioCell(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.titleY = AndroidUtilities.dp(9.0f);
        this.descriptionY = AndroidUtilities.dp(29.0f);
        this.captionY = AndroidUtilities.dp(29.0f);
        this.currentAccount = UserConfig.selectedAccount;
        this.showName = true;
        this.showNameProgress = 0.0f;
        this.enterAlpha = 1.0f;
        this.resourcesProvider = resourcesProvider;
        this.viewType = i;
        setFocusable(true);
        setImportantForAccessibility(1);
        RadialProgress2 radialProgress2 = new RadialProgress2(this, resourcesProvider);
        this.radialProgress = radialProgress2;
        radialProgress2.setColorKeys(Theme.key_chat_inLoader, Theme.key_chat_inLoaderSelected, Theme.key_chat_inMediaIcon, Theme.key_chat_inMediaIconSelected);
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        setWillNotDraw(false);
        CheckBox2 checkBox2 = new CheckBox2(context, 22, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setVisibility(4);
        this.checkBox.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(3);
        CheckBox2 checkBox22 = this.checkBox;
        boolean z = LocaleController.isRTL;
        addView(checkBox22, LayoutHelper.createFrame(24, 24.0f, (z ? 5 : 3) | 48, z ? 0.0f : 38.1f, 32.1f, z ? 6.0f : 0.0f, 0.0f));
        if (i == 1) {
            TextPaint textPaint = new TextPaint(1);
            this.description2TextPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(13.0f));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(".");
            this.dotSpan = spannableStringBuilder;
            spannableStringBuilder.setSpan(new DotDividerSpan(), 0, 1, 0);
        }
        TextPaint textPaint2 = new TextPaint(1);
        this.captionTextPaint = textPaint2;
        textPaint2.setTextSize(AndroidUtilities.dp(13.0f));
        if (resourcesProvider != null) {
            TextPaint textPaint3 = new TextPaint(1);
            this.titlePaint = textPaint3;
            textPaint3.setTypeface(AndroidUtilities.bold());
            this.titlePaint.setTextSize(AndroidUtilities.dp(15.0f));
            this.titlePaint.setColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        }
    }

    public SharedAudioCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        this(context, 0, resourcesProvider);
    }

    private boolean checkAudioMotionEvent(MotionEvent motionEvent) {
        boolean z;
        int dp;
        int dp2;
        int i;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int dp3 = AndroidUtilities.dp(36.0f);
        boolean z2 = this.miniButtonState >= 0 && x >= (dp2 = this.buttonX + (dp = AndroidUtilities.dp(27.0f))) && x <= dp2 + dp3 && y >= (i = this.buttonY + dp) && y <= i + dp3;
        if (motionEvent.getAction() == 0) {
            if (z2) {
                this.miniButtonPressed = true;
                this.radialProgress.setPressed(true, true);
            } else if (this.checkForButtonPress && this.radialProgress.getProgressRect().contains(x, y)) {
                requestDisallowInterceptTouchEvent(true);
                this.buttonPressed = true;
                this.radialProgress.setPressed(true, false);
            }
            invalidate();
            z = true;
            this.radialProgress.setPressed(this.miniButtonPressed, true);
            return z || this.buttonPressed;
        }
        if (motionEvent.getAction() == 1) {
            if (this.miniButtonPressed) {
                this.miniButtonPressed = false;
                playSoundEffect(0);
                didPressedMiniButton(true);
            } else {
                if (this.buttonPressed) {
                    this.buttonPressed = false;
                    playSoundEffect(0);
                    didPressedButton();
                }
                requestDisallowInterceptTouchEvent(false);
            }
            invalidate();
            requestDisallowInterceptTouchEvent(false);
        } else {
            if (motionEvent.getAction() == 3) {
                requestDisallowInterceptTouchEvent(false);
                this.miniButtonPressed = false;
                this.buttonPressed = false;
            } else if (motionEvent.getAction() == 2 && !z2 && this.miniButtonPressed) {
                this.miniButtonPressed = false;
            }
            invalidate();
        }
        z = false;
        this.radialProgress.setPressed(this.miniButtonPressed, true);
        if (z) {
            return true;
        }
    }

    private void didPressedMiniButton(boolean z) {
        int i = this.miniButtonState;
        if (i == 0) {
            this.miniButtonState = 1;
            this.radialProgress.setProgress(0.0f, false);
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
        } else {
            if (i != 1) {
                return;
            }
            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            this.miniButtonState = 0;
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
        }
        this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
        invalidate();
    }

    private void drawInternal(Canvas canvas) {
        float height;
        float width;
        float height2;
        Paint themePaint;
        Canvas canvas2;
        float f;
        StaticLayout staticLayout;
        if (this.viewType == 1) {
            this.description2TextPaint.setColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText3));
        }
        int i = 0;
        if (this.dateLayout != null) {
            canvas.save();
            canvas.translate(AndroidUtilities.dp(LocaleController.isRTL ? 24.0f : AndroidUtilities.leftBaseline) + (LocaleController.isRTL ? 0 : this.dateLayoutX), this.titleY);
            this.dateLayout.draw(canvas);
            canvas.restore();
        }
        if (this.titleLayout != null) {
            int alpha = Theme.chat_contextResult_titleTextPaint.getAlpha();
            float f2 = this.showNameProgress;
            if (f2 != 1.0f) {
                Theme.chat_contextResult_titleTextPaint.setAlpha((int) (alpha * f2));
            }
            canvas.save();
            int dp = AndroidUtilities.dp(LocaleController.isRTL ? 24.0f : AndroidUtilities.leftBaseline);
            if (LocaleController.isRTL && (staticLayout = this.dateLayout) != null) {
                i = staticLayout.getWidth() + AndroidUtilities.dp(LocaleController.isRTL ? 12.0f : 4.0f);
            }
            canvas.translate(((dp + i) + (LocaleController.isRTL ? this.titleLayout.getWidth() - this.titleLayoutWidth : 0.0f)) - this.titleLayoutLeft, this.titleY);
            this.titleLayout.draw(canvas);
            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.titleLayout, this.titleLayoutEmojis, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            canvas.restore();
            if (this.showNameProgress != 1.0f) {
                Theme.chat_contextResult_titleTextPaint.setAlpha(alpha);
            }
        }
        if (this.captionLayout != null) {
            this.captionTextPaint.setColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
            canvas.save();
            canvas.translate((AndroidUtilities.dp(LocaleController.isRTL ? 24.0f : AndroidUtilities.leftBaseline) + (LocaleController.isRTL ? this.captionLayout.getWidth() - this.captionLayoutWidth : 0.0f)) - this.captionLayoutLeft, this.captionY);
            this.captionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
            int alpha2 = Theme.chat_contextResult_descriptionTextPaint.getAlpha();
            float f3 = this.showNameProgress;
            if (f3 != 1.0f) {
                Theme.chat_contextResult_descriptionTextPaint.setAlpha((int) (alpha2 * f3));
            }
            canvas.save();
            canvas.translate((AndroidUtilities.dp(LocaleController.isRTL ? 24.0f : AndroidUtilities.leftBaseline) + (LocaleController.isRTL ? this.descriptionLayout.getWidth() - this.descriptionLayoutWidth : 0.0f)) - this.descriptionLayoutLeft, this.descriptionY);
            this.descriptionLayout.draw(canvas);
            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.descriptionLayout, this.descriptionLayoutEmojis, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            canvas.restore();
            if (this.showNameProgress != 1.0f) {
                Theme.chat_contextResult_descriptionTextPaint.setAlpha(alpha2);
            }
        }
        this.radialProgress.setProgressColor(getThemedColor(this.buttonPressed ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress));
        this.radialProgress.setOverlayImageAlpha(this.showNameProgress);
        this.radialProgress.draw(canvas);
        if (this.needDivider) {
            if (LocaleController.isRTL) {
                height = getHeight() - 1;
                width = (getWidth() - AndroidUtilities.dp(72.0f)) - getPaddingRight();
                height2 = getHeight() - 1;
                themePaint = Theme.getThemePaint("paintDivider", this.resourcesProvider);
                f = 0.0f;
                canvas2 = canvas;
            } else {
                float dp2 = AndroidUtilities.dp(72.0f);
                height = getHeight() - 1;
                width = getWidth() - getPaddingRight();
                height2 = getHeight() - 1;
                themePaint = Theme.getThemePaint("paintDivider", this.resourcesProvider);
                canvas2 = canvas;
                f = dp2;
            }
            canvas2.drawLine(f, height, width, height2, themePaint);
        }
    }

    private void drawReorder(Canvas canvas) {
        boolean z = this.showReorderIcon;
        if (z || this.showReorderIconProgress != 0.0f) {
            if (z) {
                float f = this.showReorderIconProgress;
                if (f != 1.0f) {
                    this.showReorderIconProgress = f + 0.10666667f;
                    invalidate();
                    this.showReorderIconProgress = Utilities.clamp(this.showReorderIconProgress, 1.0f, 0.0f);
                    int measuredWidth = (getMeasuredWidth() - AndroidUtilities.dp(12.0f)) - Theme.dialogs_reorderDrawable.getIntrinsicWidth();
                    int measuredHeight = (getMeasuredHeight() - Theme.dialogs_reorderDrawable.getIntrinsicHeight()) >> 1;
                    canvas.save();
                    float f2 = this.showReorderIconProgress;
                    canvas.scale(f2, f2, measuredWidth + (Theme.dialogs_reorderDrawable.getIntrinsicWidth() / 2.0f), measuredHeight + (Theme.dialogs_reorderDrawable.getIntrinsicHeight() / 2.0f));
                    Drawable drawable = Theme.dialogs_reorderDrawable;
                    drawable.setBounds(measuredWidth, measuredHeight, drawable.getIntrinsicWidth() + measuredWidth, Theme.dialogs_reorderDrawable.getIntrinsicHeight() + measuredHeight);
                    Theme.dialogs_reorderDrawable.draw(canvas);
                    canvas.restore();
                }
            }
            if (!z) {
                float f3 = this.showReorderIconProgress;
                if (f3 != 0.0f) {
                    this.showReorderIconProgress = f3 - 0.10666667f;
                    invalidate();
                }
            }
            this.showReorderIconProgress = Utilities.clamp(this.showReorderIconProgress, 1.0f, 0.0f);
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(12.0f)) - Theme.dialogs_reorderDrawable.getIntrinsicWidth();
            int measuredHeight2 = (getMeasuredHeight() - Theme.dialogs_reorderDrawable.getIntrinsicHeight()) >> 1;
            canvas.save();
            float f22 = this.showReorderIconProgress;
            canvas.scale(f22, f22, measuredWidth2 + (Theme.dialogs_reorderDrawable.getIntrinsicWidth() / 2.0f), measuredHeight2 + (Theme.dialogs_reorderDrawable.getIntrinsicHeight() / 2.0f));
            Drawable drawable2 = Theme.dialogs_reorderDrawable;
            drawable2.setBounds(measuredWidth2, measuredHeight2, drawable2.getIntrinsicWidth() + measuredWidth2, Theme.dialogs_reorderDrawable.getIntrinsicHeight() + measuredHeight2);
            Theme.dialogs_reorderDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private int getIconForCurrentState() {
        int i = this.buttonState;
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        return i == 4 ? 3 : 0;
    }

    private int getMiniIconForCurrentState() {
        int i = this.miniButtonState;
        if (i < 0) {
            return 4;
        }
        return i == 0 ? 2 : 3;
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public void didPressedButton() {
        int i = this.buttonState;
        int i2 = 2;
        if (i == 0) {
            if (this.miniButtonState == 0) {
                this.currentMessageObject.putInDownloadsStore = true;
                FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            }
            if (!needPlayMessage(this.currentMessageObject)) {
                return;
            }
            if (this.hasMiniProgress == 2 && this.miniButtonState != 1) {
                this.miniButtonState = 1;
                this.radialProgress.setProgress(0.0f, false);
                this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), false, true);
            }
            i2 = 1;
        } else if (i == 1) {
            if (!MediaController.getInstance().lambda$startAudioAgain$7(this.currentMessageObject)) {
                return;
            } else {
                i2 = 0;
            }
        } else if (i == 2) {
            this.radialProgress.setProgress(0.0f, false);
            this.currentMessageObject.putInDownloadsStore = true;
            FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), this.currentMessageObject, 1, 0);
            i2 = 4;
        } else if (i != 4) {
            return;
        } else {
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.getDocument());
        }
        this.buttonState = i2;
        this.radialProgress.setIcon(getIconForCurrentState(), false, true);
        invalidate();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        updateButtonState(false, true);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        boolean z = this.showName;
        if (z) {
            float f = this.showNameProgress;
            if (f != 1.0f) {
                this.showNameProgress = f + 0.10666667f;
                invalidate();
                this.showNameProgress = Utilities.clamp(this.showNameProgress, 1.0f, 0.0f);
                if (this.enterAlpha != 1.0f || this.globalGradientView == null) {
                    drawInternal(canvas);
                    drawReorder(canvas);
                    super.dispatchDraw(canvas);
                }
                canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.enterAlpha) * 255.0f), 31);
                this.globalGradientView.setViewType(4);
                this.globalGradientView.updateColors();
                this.globalGradientView.updateGradient();
                this.globalGradientView.draw(canvas);
                canvas.restore();
                canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) (this.enterAlpha * 255.0f), 31);
                drawInternal(canvas);
                super.dispatchDraw(canvas);
                drawReorder(canvas);
                canvas.restore();
                return;
            }
        }
        if (!z) {
            float f2 = this.showNameProgress;
            if (f2 != 0.0f) {
                this.showNameProgress = f2 - 0.10666667f;
                invalidate();
            }
        }
        this.showNameProgress = Utilities.clamp(this.showNameProgress, 1.0f, 0.0f);
        if (this.enterAlpha != 1.0f) {
        }
        drawInternal(canvas);
        drawReorder(canvas);
        super.dispatchDraw(canvas);
    }

    public MessageObject getMessage() {
        return this.currentMessageObject;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    public void initStreamingIcons() {
        this.radialProgress.initMiniIcons();
    }

    protected boolean needPlayMessage(MessageObject messageObject) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.radialProgress.onAttachedToWindow();
        updateButtonState(false, false);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        this.titleLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.titleLayoutEmojis, this.titleLayout);
        this.descriptionLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.descriptionLayoutEmojis, this.descriptionLayout);
        this.captionLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.captionLayoutEmojis, this.captionLayout);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.radialProgress.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        AnimatedEmojiSpan.release(this, this.titleLayoutEmojis);
        AnimatedEmojiSpan.release(this, this.descriptionLayoutEmojis);
        AnimatedEmojiSpan.release(this, this.captionLayoutEmojis);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0060  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        String str;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(true);
        if (!this.currentMessageObject.isMusic()) {
            if (this.titleLayout != null && this.descriptionLayout != null) {
                str = ((Object) this.titleLayout.getText()) + ", " + ((Object) this.descriptionLayout.getText());
            }
            if (this.checkBox.isChecked()) {
                return;
            }
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setChecked(true);
            return;
        }
        str = LocaleController.formatString("AccDescrMusicInfo", R.string.AccDescrMusicInfo, this.currentMessageObject.getMusicAuthor(), this.currentMessageObject.getMusicTitle());
        accessibilityNodeInfo.setText(str);
        if (this.checkBox.isChecked()) {
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return onTouchEvent(motionEvent);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(42:1|(1:3)(1:101)|4|(2:5|6)|(33:97|14|(1:16)|17|(1:96)|19|20|(1:22)(1:95)|23|(1:25)(1:94)|26|27|(3:29|(5:31|(1:33)(1:39)|34|(1:36)(1:38)|37)|40)|41|42|(21:78|(1:80)|81|(1:83)|84|(1:86)(1:89)|87|88|54|(1:56)(1:76)|57|(1:59)|60|61|(1:63)|64|(1:66)(1:74)|67|(1:69)(1:73)|70|71)|49|(1:51)(1:77)|52|53|54|(0)(0)|57|(0)|60|61|(0)|64|(0)(0)|67|(0)(0)|70|71)|13|14|(0)|17|(0)|19|20|(0)(0)|23|(0)(0)|26|27|(0)|41|42|(2:44|46)|78|(0)|81|(0)|84|(0)(0)|87|88|54|(0)(0)|57|(0)|60|61|(0)|64|(0)(0)|67|(0)(0)|70|71) */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01ad, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0271, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00cd A[Catch: Exception -> 0x007d, TryCatch #1 {Exception -> 0x007d, blocks: (B:6:0x0068, B:8:0x006c, B:10:0x0074, B:13:0x0080, B:14:0x0091, B:17:0x009e, B:20:0x00a7, B:22:0x00cd, B:23:0x00d5, B:25:0x00df, B:26:0x00e7, B:96:0x00a4, B:97:0x0087), top: B:5:0x0068 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00df A[Catch: Exception -> 0x007d, TryCatch #1 {Exception -> 0x007d, blocks: (B:6:0x0068, B:8:0x006c, B:10:0x0074, B:13:0x0080, B:14:0x0091, B:17:0x009e, B:20:0x00a7, B:22:0x00cd, B:23:0x00d5, B:25:0x00df, B:26:0x00e7, B:96:0x00a4, B:97:0x0087), top: B:5:0x0068 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0248 A[Catch: Exception -> 0x01ad, TryCatch #0 {Exception -> 0x01ad, blocks: (B:42:0x0198, B:44:0x019c, B:46:0x01a4, B:49:0x01b0, B:51:0x01bf, B:53:0x01c6, B:54:0x0240, B:56:0x0248, B:57:0x0250, B:59:0x025a, B:60:0x0260, B:77:0x01c3, B:78:0x01e2, B:81:0x01f9, B:83:0x01fd, B:84:0x021a, B:86:0x021e, B:88:0x0225, B:89:0x0222), top: B:41:0x0198 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x025a A[Catch: Exception -> 0x01ad, TryCatch #0 {Exception -> 0x01ad, blocks: (B:42:0x0198, B:44:0x019c, B:46:0x01a4, B:49:0x01b0, B:51:0x01bf, B:53:0x01c6, B:54:0x0240, B:56:0x0248, B:57:0x0250, B:59:0x025a, B:60:0x0260, B:77:0x01c3, B:78:0x01e2, B:81:0x01f9, B:83:0x01fd, B:84:0x021a, B:86:0x021e, B:88:0x0225, B:89:0x0222), top: B:41:0x0198 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01fd A[Catch: Exception -> 0x01ad, TryCatch #0 {Exception -> 0x01ad, blocks: (B:42:0x0198, B:44:0x019c, B:46:0x01a4, B:49:0x01b0, B:51:0x01bf, B:53:0x01c6, B:54:0x0240, B:56:0x0248, B:57:0x0250, B:59:0x025a, B:60:0x0260, B:77:0x01c3, B:78:0x01e2, B:81:0x01f9, B:83:0x01fd, B:84:0x021a, B:86:0x021e, B:88:0x0225, B:89:0x0222), top: B:41:0x0198 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x021e A[Catch: Exception -> 0x01ad, TryCatch #0 {Exception -> 0x01ad, blocks: (B:42:0x0198, B:44:0x019c, B:46:0x01a4, B:49:0x01b0, B:51:0x01bf, B:53:0x01c6, B:54:0x0240, B:56:0x0248, B:57:0x0250, B:59:0x025a, B:60:0x0260, B:77:0x01c3, B:78:0x01e2, B:81:0x01f9, B:83:0x01fd, B:84:0x021a, B:86:0x021e, B:88:0x0225, B:89:0x0222), top: B:41:0x0198 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0222 A[Catch: Exception -> 0x01ad, TryCatch #0 {Exception -> 0x01ad, blocks: (B:42:0x0198, B:44:0x019c, B:46:0x01a4, B:49:0x01b0, B:51:0x01bf, B:53:0x01c6, B:54:0x0240, B:56:0x0248, B:57:0x0250, B:59:0x025a, B:60:0x0260, B:77:0x01c3, B:78:0x01e2, B:81:0x01f9, B:83:0x01fd, B:84:0x021a, B:86:0x021e, B:88:0x0225, B:89:0x0222), top: B:41:0x0198 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00a4 A[Catch: Exception -> 0x007d, TryCatch #1 {Exception -> 0x007d, blocks: (B:6:0x0068, B:8:0x006c, B:10:0x0074, B:13:0x0080, B:14:0x0091, B:17:0x009e, B:20:0x00a7, B:22:0x00cd, B:23:0x00d5, B:25:0x00df, B:26:0x00e7, B:96:0x00a4, B:97:0x0087), top: B:5:0x0068 }] */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        int i3;
        int dp;
        CharSequence highlightText;
        StaticLayout staticLayout;
        CharSequence replace;
        CharSequence highlightText2;
        TextPaint textPaint;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.captionLayout = null;
        int size = (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(28.0f);
        if (this.viewType == 1) {
            String stringForMessageListDate = LocaleController.stringForMessageListDate(this.currentMessageObject.messageOwner.date);
            int ceil = (int) Math.ceil(this.description2TextPaint.measureText(stringForMessageListDate));
            this.dateLayout = ChatMessageCell.generateStaticLayout(stringForMessageListDate, this.description2TextPaint, ceil, ceil, 0, 1);
            this.dateLayoutX = ((size - ceil) - AndroidUtilities.dp(8.0f)) + AndroidUtilities.dp(20.0f);
            i3 = ceil + AndroidUtilities.dp(12.0f);
        } else {
            i3 = 0;
        }
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (this.viewType != 1 || (!this.currentMessageObject.isVoice() && !this.currentMessageObject.isRoundVideo())) {
            replace = this.currentMessageObject.getMusicTitle().replace('\n', ' ');
            highlightText2 = AndroidUtilities.highlightText(replace, this.currentMessageObject.highlightedWords, this.resourcesProvider);
            if (highlightText2 != null) {
                replace = highlightText2;
            }
            textPaint = this.titlePaint;
            if (textPaint == null) {
                textPaint = Theme.chat_contextResult_titleTextPaint;
            }
            TextPaint textPaint2 = textPaint;
            StaticLayout staticLayout2 = new StaticLayout(TextUtils.ellipsize(replace, textPaint2, size - i3, TextUtils.TruncateAt.END), textPaint2, (AndroidUtilities.dp(4.0f) + size) - i3, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.titleLayout = staticLayout2;
            this.titleLayoutLeft = staticLayout2.getLineCount() <= 0 ? this.titleLayout.getLineLeft(0) : 0.0f;
            this.titleLayoutWidth = this.titleLayout.getLineCount() <= 0 ? this.titleLayout.getLineWidth(0) : 0.0f;
            this.titleLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.titleLayoutEmojis, this.titleLayout);
            if (this.currentMessageObject.hasHighlightedWords()) {
                CharSequence highlightText3 = AndroidUtilities.highlightText(Emoji.replaceEmoji((CharSequence) this.currentMessageObject.messageOwner.message.replace("\n", " ").replaceAll(" +", " ").trim(), Theme.chat_msgTextPaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false), this.currentMessageObject.highlightedWords, this.resourcesProvider);
                if (highlightText3 != null) {
                    StaticLayout staticLayout3 = new StaticLayout(TextUtils.ellipsize(AndroidUtilities.ellipsizeCenterEnd(highlightText3, this.currentMessageObject.highlightedWords.get(0), size, this.captionTextPaint, NotificationCenter.walletSyncProgressChanged), this.captionTextPaint, size, TextUtils.TruncateAt.END), this.captionTextPaint, size + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.captionLayout = staticLayout3;
                    this.captionLayoutLeft = staticLayout3.getLineCount() > 0 ? this.captionLayout.getLineLeft(0) : 0.0f;
                    this.captionLayoutWidth = this.captionLayout.getLineCount() > 0 ? this.captionLayout.getLineWidth(0) : 0.0f;
                }
                this.captionLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.captionLayoutEmojis, this.captionLayout);
            }
            if (this.viewType == 1 || (!this.currentMessageObject.isVoice() && !this.currentMessageObject.isRoundVideo())) {
                CharSequence replace2 = this.currentMessageObject.getMusicAuthor().replace('\n', ' ');
                highlightText = AndroidUtilities.highlightText(replace2, this.currentMessageObject.highlightedWords, this.resourcesProvider);
                if (highlightText != null) {
                    replace2 = highlightText;
                }
                if (this.viewType == 1) {
                    replace2 = new SpannableStringBuilder(replace2).append(' ').append((CharSequence) this.dotSpan).append(' ').append(FilteredSearchView.createFromInfoString(this.currentMessageObject, 1));
                }
                TextPaint textPaint3 = this.viewType != 1 ? this.description2TextPaint : Theme.chat_contextResult_descriptionTextPaint;
                staticLayout = new StaticLayout(TextUtils.ellipsize(replace2, textPaint3, size, TextUtils.TruncateAt.END), textPaint3, size + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.descriptionLayout = staticLayout;
                this.descriptionLayoutLeft = staticLayout.getLineCount() > 0 ? this.descriptionLayout.getLineLeft(0) : 0.0f;
                this.descriptionLayoutWidth = this.descriptionLayout.getLineCount() > 0 ? this.descriptionLayout.getLineWidth(0) : 0.0f;
                this.descriptionLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.descriptionLayoutEmojis, this.descriptionLayout);
                setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(56.0f) + (this.captionLayout != null ? AndroidUtilities.dp(18.0f) : 0) + (this.needDivider ? 1 : 0));
                int size2 = LocaleController.isRTL ? (View.MeasureSpec.getSize(i) - AndroidUtilities.dp(8.0f)) - AndroidUtilities.dp(52.0f) : AndroidUtilities.dp(8.0f);
                RadialProgress2 radialProgress2 = this.radialProgress;
                int dp2 = AndroidUtilities.dp(4.0f) + size2;
                this.buttonX = dp2;
                int dp3 = AndroidUtilities.dp(6.0f);
                this.buttonY = dp3;
                radialProgress2.setProgressRect(dp2, dp3, size2 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(50.0f));
                measureChildWithMargins(this.checkBox, i, 0, i2, 0);
                if (this.captionLayout != null) {
                    this.captionY = AndroidUtilities.dp(29.0f);
                    dp = AndroidUtilities.dp(29.0f) + AndroidUtilities.dp(18.0f);
                } else {
                    dp = AndroidUtilities.dp(29.0f);
                }
                this.descriptionY = dp;
            }
            String formatDuration = AndroidUtilities.formatDuration((int) this.currentMessageObject.getDuration(), false);
            TextPaint textPaint4 = this.viewType == 1 ? this.description2TextPaint : Theme.chat_contextResult_descriptionTextPaint;
            staticLayout = new StaticLayout(TextUtils.ellipsize(formatDuration, textPaint4, size, TextUtils.TruncateAt.END), textPaint4, size + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.descriptionLayout = staticLayout;
            this.descriptionLayoutLeft = staticLayout.getLineCount() > 0 ? this.descriptionLayout.getLineLeft(0) : 0.0f;
            this.descriptionLayoutWidth = this.descriptionLayout.getLineCount() > 0 ? this.descriptionLayout.getLineWidth(0) : 0.0f;
            this.descriptionLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.descriptionLayoutEmojis, this.descriptionLayout);
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(56.0f) + (this.captionLayout != null ? AndroidUtilities.dp(18.0f) : 0) + (this.needDivider ? 1 : 0));
            if (LocaleController.isRTL) {
            }
            RadialProgress2 radialProgress22 = this.radialProgress;
            int dp22 = AndroidUtilities.dp(4.0f) + size2;
            this.buttonX = dp22;
            int dp32 = AndroidUtilities.dp(6.0f);
            this.buttonY = dp32;
            radialProgress22.setProgressRect(dp22, dp32, size2 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(50.0f));
            measureChildWithMargins(this.checkBox, i, 0, i2, 0);
            if (this.captionLayout != null) {
            }
            this.descriptionY = dp;
        }
        replace = FilteredSearchView.createFromInfoString(this.currentMessageObject, 1);
        highlightText2 = AndroidUtilities.highlightText(replace, this.currentMessageObject.highlightedWords, this.resourcesProvider);
        if (highlightText2 != null) {
        }
        textPaint = this.titlePaint;
        if (textPaint == null) {
        }
        TextPaint textPaint22 = textPaint;
        StaticLayout staticLayout22 = new StaticLayout(TextUtils.ellipsize(replace, textPaint22, size - i3, TextUtils.TruncateAt.END), textPaint22, (AndroidUtilities.dp(4.0f) + size) - i3, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.titleLayout = staticLayout22;
        this.titleLayoutLeft = staticLayout22.getLineCount() <= 0 ? this.titleLayout.getLineLeft(0) : 0.0f;
        this.titleLayoutWidth = this.titleLayout.getLineCount() <= 0 ? this.titleLayout.getLineWidth(0) : 0.0f;
        this.titleLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.titleLayoutEmojis, this.titleLayout);
        if (this.currentMessageObject.hasHighlightedWords()) {
        }
        if (this.viewType == 1) {
        }
        CharSequence replace22 = this.currentMessageObject.getMusicAuthor().replace('\n', ' ');
        highlightText = AndroidUtilities.highlightText(replace22, this.currentMessageObject.highlightedWords, this.resourcesProvider);
        if (highlightText != null) {
        }
        if (this.viewType == 1) {
        }
        TextPaint textPaint32 = this.viewType != 1 ? this.description2TextPaint : Theme.chat_contextResult_descriptionTextPaint;
        staticLayout = new StaticLayout(TextUtils.ellipsize(replace22, textPaint32, size, TextUtils.TruncateAt.END), textPaint32, size + AndroidUtilities.dp(4.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        this.descriptionLayout = staticLayout;
        this.descriptionLayoutLeft = staticLayout.getLineCount() > 0 ? this.descriptionLayout.getLineLeft(0) : 0.0f;
        this.descriptionLayoutWidth = this.descriptionLayout.getLineCount() > 0 ? this.descriptionLayout.getLineWidth(0) : 0.0f;
        this.descriptionLayoutEmojis = AnimatedEmojiSpan.update(0, this, this.descriptionLayoutEmojis, this.descriptionLayout);
        setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(56.0f) + (this.captionLayout != null ? AndroidUtilities.dp(18.0f) : 0) + (this.needDivider ? 1 : 0));
        if (LocaleController.isRTL) {
        }
        RadialProgress2 radialProgress222 = this.radialProgress;
        int dp222 = AndroidUtilities.dp(4.0f) + size2;
        this.buttonX = dp222;
        int dp322 = AndroidUtilities.dp(6.0f);
        this.buttonY = dp322;
        radialProgress222.setProgressRect(dp222, dp322, size2 + AndroidUtilities.dp(48.0f), AndroidUtilities.dp(50.0f));
        measureChildWithMargins(this.checkBox, i, 0, i2, 0);
        if (this.captionLayout != null) {
        }
        this.descriptionY = dp;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.radialProgress.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
        if (this.hasMiniProgress != 0) {
            if (this.miniButtonState == 1) {
                return;
            }
        } else if (this.buttonState == 4) {
            return;
        }
        updateButtonState(false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        this.radialProgress.setProgress(1.0f, true);
        updateButtonState(false, true);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.currentMessageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        boolean checkAudioMotionEvent = checkAudioMotionEvent(motionEvent);
        if (motionEvent.getAction() != 3) {
            return checkAudioMotionEvent;
        }
        this.miniButtonPressed = false;
        this.buttonPressed = false;
        this.radialProgress.setPressed(false, false);
        this.radialProgress.setPressed(this.miniButtonPressed, true);
        return false;
    }

    public void setCheckForButtonPress(boolean z) {
        this.checkForButtonPress = z;
    }

    public void setChecked(boolean z, boolean z2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
    }

    public void setEnterAnimationAlpha(float f) {
        if (this.enterAlpha != f) {
            this.enterAlpha = f;
            invalidate();
        }
    }

    public void setGlobalGradientView(FlickerLoadingView flickerLoadingView) {
        this.globalGradientView = flickerLoadingView;
    }

    public void setMessageObject(MessageObject messageObject, boolean z) {
        this.needDivider = z;
        this.currentMessageObject = messageObject;
        TLRPC.Document document = messageObject.getDocument();
        TLRPC.PhotoSize closestPhotoSizeWithSize = document != null ? FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 360) : null;
        if ((closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeProgressive)) {
            this.radialProgress.setImageOverlay(closestPhotoSizeWithSize, document, messageObject);
        } else {
            String artworkUrl = messageObject.getArtworkUrl(true);
            if (TextUtils.isEmpty(artworkUrl)) {
                this.radialProgress.setImageOverlay(null, null, null);
            } else {
                this.radialProgress.setImageOverlay(artworkUrl);
            }
        }
        updateButtonState(false, false);
        requestLayout();
    }

    public void showName(boolean z, boolean z2) {
        if (!z2) {
            this.showNameProgress = z ? 1.0f : 0.0f;
        }
        if (this.showName == z) {
            return;
        }
        this.showName = z;
        invalidate();
    }

    public void showReorderIcon(boolean z, boolean z2) {
        if (this.showReorderIcon == z) {
            return;
        }
        this.showReorderIcon = z;
        if (!z2) {
            this.showReorderIconProgress = z ? 1.0f : 0.0f;
        }
        invalidate();
    }

    public void updateButtonState(boolean z, boolean z2) {
        String fileName = this.currentMessageObject.getFileName();
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        MessageObject messageObject = this.currentMessageObject;
        boolean z3 = messageObject.attachPathExists || messageObject.mediaExists;
        if (SharedConfig.streamMedia && messageObject.isMusic() && ((int) this.currentMessageObject.getDialogId()) != 0) {
            this.hasMiniProgress = z3 ? 1 : 2;
            z3 = true;
        } else {
            this.hasMiniProgress = 0;
            this.miniButtonState = -1;
        }
        if (this.hasMiniProgress == 0) {
            if (z3) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                boolean isPlayingMessage = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (!isPlayingMessage || (isPlayingMessage && MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 0;
                } else {
                    this.buttonState = 1;
                }
                this.radialProgress.setProgress(1.0f, z2);
            } else {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
                if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                    this.buttonState = 4;
                    Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
                    if (fileProgress != null) {
                        this.radialProgress.setProgress(fileProgress.floatValue(), z2);
                    }
                } else {
                    this.buttonState = 2;
                }
                this.radialProgress.setProgress(0.0f, z2);
            }
            this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
            invalidate();
            return;
        }
        this.radialProgress.setMiniProgressBackgroundColor(getThemedColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outLoader : Theme.key_chat_inLoader));
        boolean isPlayingMessage2 = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
        if (!isPlayingMessage2 || (isPlayingMessage2 && MediaController.getInstance().isMessagePaused())) {
            this.buttonState = 0;
        } else {
            this.buttonState = 1;
        }
        this.radialProgress.setIcon(getIconForCurrentState(), z, z2);
        if (this.hasMiniProgress == 1) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.miniButtonState = -1;
        } else {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
            if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                this.miniButtonState = 1;
                this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), z, z2);
                Float fileProgress2 = ImageLoader.getInstance().getFileProgress(fileName);
                if (fileProgress2 != null) {
                    this.radialProgress.setProgress(fileProgress2.floatValue(), z2);
                    return;
                } else {
                    this.radialProgress.setProgress(0.0f, z2);
                    return;
                }
            }
            this.miniButtonState = 0;
        }
        this.radialProgress.setMiniIcon(getMiniIconForCurrentState(), z, z2);
    }
}
