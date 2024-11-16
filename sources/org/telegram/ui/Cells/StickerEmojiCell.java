package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.RecyclerListViewWithOverlayDraw;
import org.telegram.ui.Components.Premium.PremiumLockIconView;

/* loaded from: classes4.dex */
public abstract class StickerEmojiCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate, RecyclerListViewWithOverlayDraw.OverlayView {
    private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5f);
    private float alpha;
    private boolean changingAlpha;
    private int currentAccount;
    private String currentEmoji;
    private boolean drawInParentView;
    public ImageView editModeIcon;
    private int editModeIconColor;
    private TextView emojiTextView;
    private boolean fromEmojiPanel;
    private ImageReceiver imageView;
    private boolean isPremiumSticker;
    private long lastUpdateTime;
    private Object parentObject;
    private float premiumAlpha;
    private PremiumLockIconView premiumIconView;
    private boolean recent;
    private final Theme.ResourcesProvider resourceProvider;
    private float scale;
    private boolean scaled;
    private boolean showPremiumLock;
    private TLRPC.Document sticker;
    private SendMessagesHelper.ImportingSticker stickerPath;
    private long time;

    public StickerEmojiCell(Context context, boolean z, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.alpha = 1.0f;
        this.currentAccount = UserConfig.selectedAccount;
        this.premiumAlpha = 1.0f;
        this.resourceProvider = resourcesProvider;
        this.fromEmojiPanel = z;
        ImageReceiver imageReceiver = new ImageReceiver() { // from class: org.telegram.ui.Cells.StickerEmojiCell.1
            @Override // org.telegram.messenger.ImageReceiver
            protected boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z2, int i2) {
                if ((drawable instanceof BitmapDrawable) && StickerEmojiCell.this.editModeIconColor == 0) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    StickerEmojiCell.this.editModeIconColor = AndroidUtilities.getDominantColor(bitmap);
                    if (StickerEmojiCell.this.editModeIconColor == -1 || StickerEmojiCell.this.editModeIconColor == 0) {
                        StickerEmojiCell.this.editModeIconColor = Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider);
                    }
                    StickerEmojiCell.this.editModeIcon.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.0f), StickerEmojiCell.this.editModeIconColor));
                    invalidate();
                }
                return super.setImageBitmapByKey(drawable, str, i, z2, i2);
            }
        };
        this.imageView = imageReceiver;
        imageReceiver.setAspectFit(true);
        this.imageView.setAllowLoadingOnAttachedOnly(true);
        this.imageView.setLayerNum(1);
        TextView textView = new TextView(context);
        this.emojiTextView = textView;
        textView.setTextSize(1, 16.0f);
        new Paint(1).setColor(Theme.getColor(Theme.key_featuredStickers_addButton));
        PremiumLockIconView premiumLockIconView = new PremiumLockIconView(context, PremiumLockIconView.TYPE_STICKERS_PREMIUM_LOCKED);
        this.premiumIconView = premiumLockIconView;
        premiumLockIconView.setImageReceiver(this.imageView);
        this.premiumIconView.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
        this.premiumIconView.setImageReceiver(this.imageView);
        addView(this.premiumIconView, LayoutHelper.createFrame(24, 24.0f, 81, 0.0f, 0.0f, 0.0f, 0.0f));
        ImageView imageView = new ImageView(context);
        this.editModeIcon = imageView;
        imageView.setImageResource(R.drawable.mini_more_dots);
        this.editModeIcon.setPadding(AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f));
        this.editModeIcon.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.0f), Theme.getColor(Theme.key_dialogTextGray2, resourcesProvider)));
        this.editModeIcon.setAlpha(0.0f);
        addView(this.editModeIcon, LayoutHelper.createFrame(-2, -2, 5));
        setFocusable(true);
    }

    private void drawInternal(View view, Canvas canvas) {
        boolean z;
        if (this.changingAlpha || (((z = this.scaled) && this.scale != 0.8f) || (!z && this.scale != 1.0f))) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.lastUpdateTime;
            this.lastUpdateTime = currentTimeMillis;
            if (this.changingAlpha) {
                long j2 = this.time + j;
                this.time = j2;
                if (j2 > 1050) {
                    this.time = 1050L;
                }
                float interpolation = (interpolator.getInterpolation(this.time / 150.0f) * 0.5f) + 0.5f;
                this.alpha = interpolation;
                if (interpolation >= 1.0f) {
                    this.changingAlpha = false;
                    this.alpha = 1.0f;
                }
                this.imageView.setAlpha(this.alpha * this.premiumAlpha);
            } else {
                if (this.scaled) {
                    float f = this.scale;
                    if (f != 0.8f) {
                        float f2 = f - (j / 400.0f);
                        this.scale = f2;
                        if (f2 < 0.8f) {
                            this.scale = 0.8f;
                        }
                    }
                }
                float f3 = this.scale + (j / 400.0f);
                this.scale = f3;
                if (f3 > 1.0f) {
                    this.scale = 1.0f;
                }
            }
            view.invalidate();
        }
        int min = Math.min(AndroidUtilities.dp(66.0f), Math.min(getMeasuredHeight(), getMeasuredWidth()));
        float measuredWidth = getMeasuredWidth() >> 1;
        float f4 = min;
        float f5 = f4 / 2.0f;
        float measuredHeight = getMeasuredHeight() >> 1;
        this.imageView.setImageCoords(measuredWidth - f5, measuredHeight - f5, f4, f4);
        this.imageView.setAlpha(this.alpha * this.premiumAlpha);
        if (this.scale == 1.0f) {
            this.imageView.draw(canvas);
            return;
        }
        canvas.save();
        float f6 = this.scale;
        canvas.scale(f6, f6, measuredWidth, measuredHeight);
        this.imageView.draw(canvas);
        canvas.restore();
    }

    private void updatePremiumStatus(boolean z) {
        PremiumLockIconView premiumLockIconView;
        float f;
        if (this.isPremiumSticker) {
            this.showPremiumLock = true;
        } else {
            this.showPremiumLock = false;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.premiumIconView.getLayoutParams();
        if (UserConfig.getInstance(this.currentAccount).isPremium()) {
            int dp = AndroidUtilities.dp(16.0f);
            layoutParams.width = dp;
            layoutParams.height = dp;
            layoutParams.gravity = 85;
            layoutParams.bottomMargin = AndroidUtilities.dp(8.0f);
            layoutParams.rightMargin = AndroidUtilities.dp(8.0f);
            premiumLockIconView = this.premiumIconView;
            f = 1.0f;
        } else {
            int dp2 = AndroidUtilities.dp(24.0f);
            layoutParams.width = dp2;
            layoutParams.height = dp2;
            layoutParams.gravity = 81;
            layoutParams.rightMargin = 0;
            layoutParams.bottomMargin = AndroidUtilities.dp(8.0f);
            premiumLockIconView = this.premiumIconView;
            f = 4.0f;
        }
        premiumLockIconView.setPadding(AndroidUtilities.dp(f), AndroidUtilities.dp(f), AndroidUtilities.dp(f), AndroidUtilities.dp(f));
        this.premiumIconView.setLocked(!UserConfig.getInstance(this.currentAccount).isPremium());
        AndroidUtilities.updateViewVisibilityAnimated(this.premiumIconView, this.showPremiumLock, 0.9f, z);
        invalidate();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.currentUserPremiumStatusChanged) {
            updatePremiumStatus(true);
        }
    }

    public void disable() {
        this.changingAlpha = true;
        this.alpha = 0.5f;
        this.time = 0L;
        this.imageView.setAlpha(this.premiumAlpha * 0.5f);
        this.imageView.invalidate();
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public void disableEditMode(boolean z) {
        if (z) {
            this.editModeIcon.animate().alpha(0.0f).scaleX(0.4f).scaleY(0.4f).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
        } else {
            this.editModeIcon.setAlpha(0.0f);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (!this.drawInParentView) {
            drawInternal(this, canvas);
        }
        super.dispatchDraw(canvas);
    }

    public void enableEditMode(boolean z) {
        if (!z) {
            this.editModeIcon.setAlpha(1.0f);
            this.editModeIcon.setScaleX(1.0f);
            this.editModeIcon.setScaleY(1.0f);
        } else {
            this.editModeIcon.setAlpha(0.0f);
            this.editModeIcon.setScaleX(0.4f);
            this.editModeIcon.setScaleY(0.4f);
            this.editModeIcon.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
        }
    }

    public String getEmoji() {
        return this.currentEmoji;
    }

    public ImageReceiver getImageView() {
        return this.imageView;
    }

    public Object getParentObject() {
        return this.parentObject;
    }

    public MessageObject.SendAnimationData getSendAnimationData() {
        ImageReceiver imageReceiver = this.imageView;
        if (!imageReceiver.hasNotThumb()) {
            return null;
        }
        MessageObject.SendAnimationData sendAnimationData = new MessageObject.SendAnimationData();
        getLocationInWindow(new int[2]);
        sendAnimationData.x = imageReceiver.getCenterX() + r2[0];
        sendAnimationData.y = imageReceiver.getCenterY() + r2[1];
        sendAnimationData.width = imageReceiver.getImageWidth();
        sendAnimationData.height = imageReceiver.getImageHeight();
        return sendAnimationData;
    }

    public TLRPC.Document getSticker() {
        return this.sticker;
    }

    public SendMessagesHelper.ImportingSticker getStickerPath() {
        SendMessagesHelper.ImportingSticker importingSticker = this.stickerPath;
        if (importingSticker == null || !importingSticker.validated) {
            return null;
        }
        return importingSticker;
    }

    @Override // android.view.View
    public void invalidate() {
        if (this.drawInParentView && getParent() != null) {
            ((View) getParent()).invalidate();
        }
        this.emojiTextView.invalidate();
        super.invalidate();
    }

    public boolean isDisabled() {
        return this.changingAlpha;
    }

    public boolean isRecent() {
        return this.recent;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawInParentView) {
            this.imageView.setInvalidateAll(true);
            this.imageView.setParentView((View) getParent());
        } else {
            this.imageView.setParentView(this);
        }
        this.imageView.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.imageView.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        String string = LocaleController.getString(R.string.AttachSticker);
        if (this.sticker != null) {
            int i = 0;
            while (true) {
                if (i >= this.sticker.attributes.size()) {
                    break;
                }
                TLRPC.DocumentAttribute documentAttribute = this.sticker.attributes.get(i);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                    String str = documentAttribute.alt;
                    if (str != null && str.length() > 0) {
                        TextView textView = this.emojiTextView;
                        textView.setText(Emoji.replaceEmoji((CharSequence) documentAttribute.alt, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false));
                        string = documentAttribute.alt + " " + string;
                    }
                } else {
                    i++;
                }
            }
        }
        accessibilityNodeInfo.setContentDescription(string);
        accessibilityNodeInfo.setEnabled(true);
    }

    @Override // org.telegram.ui.Components.ListView.RecyclerListViewWithOverlayDraw.OverlayView
    public void preDraw(View view, Canvas canvas) {
        if (this.drawInParentView) {
            drawInternal(view, canvas);
        }
    }

    public void setRecent(boolean z) {
        this.recent = z;
    }

    public void setScaled(boolean z) {
        this.scaled = z;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public void setSticker(SendMessagesHelper.ImportingSticker importingSticker) {
        String str = importingSticker.emoji;
        setSticker(null, importingSticker, null, str, str != null, false);
    }

    public void setSticker(TLRPC.Document document, Object obj, boolean z) {
        setSticker(document, null, obj, null, z, false);
    }

    public void setSticker(TLRPC.Document document, SendMessagesHelper.ImportingSticker importingSticker, Object obj, String str, boolean z) {
        setSticker(document, importingSticker, obj, str, z, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x009d, code lost:
    
        if (r28 != null) goto L65;
     */
    /* JADX WARN: Removed duplicated region for block: B:53:0x017d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSticker(TLRPC.Document document, SendMessagesHelper.ImportingSticker importingSticker, Object obj, String str, boolean z, boolean z2) {
        ImageReceiver imageReceiver;
        ImageLocation forDocument;
        Object obj2;
        String str2;
        TextView textView;
        String emojiForSticker;
        TextPaint paint;
        CharSequence replaceEmoji;
        ImageReceiver imageReceiver2;
        SvgHelper.SvgDrawable svgRectThumb;
        ImageLocation imageLocation;
        String str3;
        ImageReceiver imageReceiver3;
        String str4;
        SvgHelper.SvgDrawable svgDrawable;
        ImageLocation imageLocation2;
        this.currentEmoji = str;
        this.isPremiumSticker = MessageObject.isPremiumSticker(document);
        this.drawInParentView = false;
        ImageLocation imageLocation3 = null;
        this.imageView.setColorFilter(null);
        this.editModeIconColor = 0;
        this.editModeIcon.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.0f), Theme.getColor(Theme.key_dialogTextGray2)));
        if (z2) {
            enableEditMode(false);
        } else {
            disableEditMode(false);
        }
        if (this.isPremiumSticker) {
            this.premiumIconView.setColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            this.premiumIconView.setWaitingImage();
        }
        if (importingSticker != null) {
            this.stickerPath = importingSticker;
            if (importingSticker.validated) {
                imageReceiver2 = this.imageView;
                imageLocation = ImageLocation.getForPath(importingSticker.path);
                svgRectThumb = DocumentObject.getSvgRectThumb(Theme.key_dialogBackgroundGray, 1.0f);
                str3 = "80_80";
                if (importingSticker.animated) {
                    imageLocation3 = imageLocation;
                    imageReceiver3 = imageReceiver2;
                    str4 = "tgs";
                    imageLocation2 = imageLocation3;
                    svgDrawable = svgRectThumb;
                }
                imageReceiver3 = imageReceiver2;
                str4 = null;
                svgDrawable = svgRectThumb;
                imageLocation2 = imageLocation;
            } else {
                imageReceiver2 = this.imageView;
                svgRectThumb = DocumentObject.getSvgRectThumb(Theme.key_dialogBackgroundGray, 1.0f);
                if (importingSticker.animated) {
                    str3 = null;
                    imageReceiver3 = imageReceiver2;
                    str4 = "tgs";
                    imageLocation2 = imageLocation3;
                    svgDrawable = svgRectThumb;
                } else {
                    imageLocation = null;
                    str3 = null;
                    imageReceiver3 = imageReceiver2;
                    str4 = null;
                    svgDrawable = svgRectThumb;
                    imageLocation2 = imageLocation;
                }
            }
            imageReceiver3.setImage(imageLocation2, str3, null, null, svgDrawable, 0L, str4, 0, 1);
        } else if (document != null) {
            this.sticker = document;
            this.parentObject = obj;
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
            boolean z3 = this.fromEmojiPanel;
            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(document, z3 ? Theme.key_emptyListPlaceholder : Theme.key_windowBackgroundGray, z3 ? 0.2f : 1.0f, 1.0f, this.resourceProvider);
            String str5 = this.fromEmojiPanel ? "66_66_pcache_compress" : "66_66";
            if (MessageObject.isTextColorEmoji(document)) {
                this.imageView.setColorFilter(Theme.getAnimatedEmojiColorFilter(this.resourceProvider));
            }
            if (MessageObject.canAutoplayAnimatedSticker(document)) {
                if (this.fromEmojiPanel) {
                    this.drawInParentView = true;
                }
                if (svgThumb != null) {
                    this.imageView.setImage(ImageLocation.getForDocument(document), str5, ImageLocation.getForDocument(closestPhotoSizeWithSize, document), null, null, null, svgThumb, 0L, null, this.parentObject, 1);
                } else {
                    imageReceiver = this.imageView;
                    forDocument = ImageLocation.getForDocument(document);
                    if (closestPhotoSizeWithSize != null) {
                        imageReceiver.setImage(forDocument, str5, ImageLocation.getForDocument(closestPhotoSizeWithSize, document), (String) null, (String) null, this.parentObject, 1);
                    } else {
                        obj2 = this.parentObject;
                        str2 = null;
                        imageReceiver.setImage(forDocument, str5, null, str2, obj2, 1);
                    }
                }
                if (str == null) {
                    if (z) {
                        int i = 0;
                        while (true) {
                            if (i >= document.attributes.size()) {
                                break;
                            }
                            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                                String str6 = documentAttribute.alt;
                                if (str6 != null && str6.length() > 0) {
                                    textView = this.emojiTextView;
                                    emojiForSticker = documentAttribute.alt;
                                    paint = textView.getPaint();
                                }
                            } else {
                                i++;
                            }
                        }
                        textView = this.emojiTextView;
                        emojiForSticker = MediaDataController.getInstance(this.currentAccount).getEmojiForSticker(this.sticker.id);
                        paint = this.emojiTextView.getPaint();
                        replaceEmoji = Emoji.replaceEmoji((CharSequence) emojiForSticker, paint.getFontMetricsInt(), AndroidUtilities.dp(16.0f), false);
                        textView.setText(replaceEmoji);
                        this.emojiTextView.setVisibility(0);
                    }
                    this.emojiTextView.setVisibility(4);
                }
                textView = this.emojiTextView;
                replaceEmoji = Emoji.replaceEmoji((CharSequence) str, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false);
                textView.setText(replaceEmoji);
                this.emojiTextView.setVisibility(0);
            } else if (svgThumb != null) {
                this.imageView.setImage(closestPhotoSizeWithSize != null ? ImageLocation.getForDocument(closestPhotoSizeWithSize, document) : ImageLocation.getForDocument(document), str5, svgThumb, "webp", this.parentObject, 1);
                if (str == null) {
                }
                textView = this.emojiTextView;
                replaceEmoji = Emoji.replaceEmoji((CharSequence) str, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false);
                textView.setText(replaceEmoji);
                this.emojiTextView.setVisibility(0);
            } else {
                imageReceiver = this.imageView;
                forDocument = closestPhotoSizeWithSize != null ? ImageLocation.getForDocument(closestPhotoSizeWithSize, document) : ImageLocation.getForDocument(document);
                obj2 = this.parentObject;
                str2 = "webp";
                imageReceiver.setImage(forDocument, str5, null, str2, obj2, 1);
                if (str == null) {
                }
                textView = this.emojiTextView;
                replaceEmoji = Emoji.replaceEmoji((CharSequence) str, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16.0f), false);
                textView.setText(replaceEmoji);
                this.emojiTextView.setVisibility(0);
            }
        }
        updatePremiumStatus(false);
        this.imageView.setAlpha(this.alpha * this.premiumAlpha);
        if (!this.drawInParentView) {
            this.imageView.setParentView(this);
        } else {
            this.imageView.setInvalidateAll(true);
            this.imageView.setParentView((View) getParent());
        }
    }

    public boolean showingBitmap() {
        return this.imageView.hasNotThumb();
    }
}
