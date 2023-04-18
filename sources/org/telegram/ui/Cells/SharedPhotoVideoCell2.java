package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public class SharedPhotoVideoCell2 extends View {
    static boolean lastAutoDownload;
    static long lastUpdateDownloadSettingsTime;
    ValueAnimator animator;
    private boolean attached;
    public ImageReceiver blurImageReceiver;
    CanvasButton canvasButton;
    CheckBoxBase checkBoxBase;
    float checkBoxProgress;
    float crossfadeProgress;
    float crossfadeToColumnsCount;
    SharedPhotoVideoCell2 crossfadeView;
    int currentAccount;
    MessageObject currentMessageObject;
    int currentParentColumnsCount;
    boolean drawVideoIcon;
    FlickerLoadingView globalGradientView;
    float highlightProgress;
    float imageAlpha;
    public ImageReceiver imageReceiver;
    float imageScale;
    private SpoilerEffect mediaSpoilerEffect;
    private Path path;
    SharedResources sharedResources;
    boolean showVideoLayout;
    private float spoilerMaxRadius;
    private float spoilerRevealProgress;
    private float spoilerRevealX;
    private float spoilerRevealY;
    private int style;
    StaticLayout videoInfoLayot;
    String videoText;

    /* renamed from: onCheckBoxPressed */
    public void lambda$setStyle$1() {
    }

    public SharedPhotoVideoCell2(Context context, SharedResources sharedResources, int i) {
        super(context);
        this.imageReceiver = new ImageReceiver();
        this.blurImageReceiver = new ImageReceiver();
        this.imageAlpha = 1.0f;
        this.imageScale = 1.0f;
        this.drawVideoIcon = true;
        this.path = new Path();
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.style = 0;
        this.sharedResources = sharedResources;
        this.currentAccount = i;
        setChecked(false, false);
        this.imageReceiver.setParentView(this);
        this.blurImageReceiver.setParentView(this);
        this.imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                SharedPhotoVideoCell2.this.lambda$new$0(imageReceiver, z, z2, z3);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        MessageObject messageObject;
        if (!z || z2 || (messageObject = this.currentMessageObject) == null || !messageObject.hasMediaSpoilers() || this.imageReceiver.getBitmap() == null) {
            return;
        }
        if (this.blurImageReceiver.getBitmap() != null) {
            this.blurImageReceiver.getBitmap().recycle();
        }
        this.blurImageReceiver.setImageBitmap(Utilities.stackBlurBitmapMax(this.imageReceiver.getBitmap()));
    }

    public void setStyle(int i) {
        if (this.style == i) {
            return;
        }
        this.style = i;
        if (i == 1) {
            CheckBoxBase checkBoxBase = new CheckBoxBase(this, 21, null);
            this.checkBoxBase = checkBoxBase;
            checkBoxBase.setColor(-1, Theme.key_sharedMedia_photoPlaceholder, Theme.key_checkboxCheck);
            this.checkBoxBase.setDrawUnchecked(true);
            this.checkBoxBase.setBackgroundType(0);
            this.checkBoxBase.setBounds(0, 0, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            if (this.attached) {
                this.checkBoxBase.onAttachedToWindow();
            }
            CanvasButton canvasButton = new CanvasButton(this);
            this.canvasButton = canvasButton;
            canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SharedPhotoVideoCell2.this.lambda$setStyle$1();
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:88:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x025b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, int i) {
        int i2 = this.currentParentColumnsCount;
        this.currentParentColumnsCount = i;
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 == null && messageObject == null) {
            return;
        }
        if (messageObject2 == null || messageObject == null || messageObject2.getId() != messageObject.getId() || i2 != i) {
            this.currentMessageObject = messageObject;
            boolean z = false;
            if (messageObject == null) {
                this.imageReceiver.onDetachedFromWindow();
                this.blurImageReceiver.onDetachedFromWindow();
                this.videoText = null;
                this.videoInfoLayot = null;
                this.showVideoLayout = false;
                return;
            }
            if (this.attached) {
                this.imageReceiver.onAttachedToWindow();
                this.blurImageReceiver.onAttachedToWindow();
            }
            String restrictionReason = MessagesController.getRestrictionReason(messageObject.messageOwner.restriction_reason);
            String filterString = this.sharedResources.getFilterString((int) ((AndroidUtilities.displaySize.x / i) / AndroidUtilities.density));
            int i3 = 320;
            if (i <= 2) {
                i3 = AndroidUtilities.getPhotoSize();
            }
            this.videoText = null;
            this.videoInfoLayot = null;
            this.showVideoLayout = false;
            if (TextUtils.isEmpty(restrictionReason)) {
                if (messageObject.isVideo()) {
                    this.showVideoLayout = true;
                    if (i != 9) {
                        this.videoText = AndroidUtilities.formatShortDuration(messageObject.getDuration());
                    }
                    ImageLocation imageLocation = messageObject.mediaThumb;
                    if (imageLocation != null) {
                        BitmapDrawable bitmapDrawable = messageObject.strippedThumb;
                        if (bitmapDrawable != null) {
                            this.imageReceiver.setImage(imageLocation, filterString, bitmapDrawable, null, messageObject, 0);
                        } else {
                            this.imageReceiver.setImage(imageLocation, filterString, messageObject.mediaSmallThumb, filterString + "_b", null, 0L, null, messageObject, 0);
                        }
                    } else {
                        TLRPC$Document document = messageObject.getDocument();
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, i3);
                        if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
                            closestPhotoSizeWithSize2 = null;
                        }
                        if (closestPhotoSizeWithSize != null) {
                            if (messageObject.strippedThumb != null) {
                                this.imageReceiver.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize2, document), filterString, messageObject.strippedThumb, null, messageObject, 0);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize2, document), filterString, ImageLocation.getForDocument(closestPhotoSizeWithSize, document), filterString + "_b", null, 0L, null, messageObject, 0);
                            }
                        }
                    }
                    if (z) {
                        this.imageReceiver.setImageBitmap(ContextCompat.getDrawable(getContext(), R.drawable.photo_placeholder_in));
                    }
                    if (this.blurImageReceiver.getBitmap() != null) {
                        this.blurImageReceiver.getBitmap().recycle();
                        this.blurImageReceiver.setImageBitmap((Bitmap) null);
                    }
                    if (this.imageReceiver.getBitmap() != null && this.currentMessageObject.hasMediaSpoilers() && !this.currentMessageObject.isMediaSpoilersRevealed) {
                        this.blurImageReceiver.setImageBitmap(Utilities.stackBlurBitmapMax(this.imageReceiver.getBitmap()));
                    }
                    invalidate();
                } else if ((MessageObject.getMedia(messageObject.messageOwner) instanceof TLRPC$TL_messageMediaPhoto) && MessageObject.getMedia(messageObject.messageOwner).photo != null && !messageObject.photoThumbs.isEmpty()) {
                    if (messageObject.mediaExists || canAutoDownload(messageObject)) {
                        ImageLocation imageLocation2 = messageObject.mediaThumb;
                        if (imageLocation2 != null) {
                            BitmapDrawable bitmapDrawable2 = messageObject.strippedThumb;
                            if (bitmapDrawable2 != null) {
                                this.imageReceiver.setImage(imageLocation2, filterString, bitmapDrawable2, null, messageObject, 0);
                            } else {
                                this.imageReceiver.setImage(imageLocation2, filterString, messageObject.mediaSmallThumb, filterString + "_b", null, 0L, null, messageObject, 0);
                            }
                        } else {
                            TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
                            TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, i3, false, closestPhotoSizeWithSize3, false);
                            if (closestPhotoSizeWithSize4 == closestPhotoSizeWithSize3) {
                                closestPhotoSizeWithSize3 = null;
                            }
                            if (messageObject.strippedThumb != null) {
                                this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject), filterString, null, null, messageObject.strippedThumb, closestPhotoSizeWithSize4 != null ? closestPhotoSizeWithSize4.size : 0L, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject), filterString, ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject), filterString + "_b", closestPhotoSizeWithSize4 != null ? closestPhotoSizeWithSize4.size : 0L, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                            }
                        }
                    } else {
                        BitmapDrawable bitmapDrawable3 = messageObject.strippedThumb;
                        if (bitmapDrawable3 != null) {
                            this.imageReceiver.setImage(null, null, null, null, bitmapDrawable3, 0L, null, messageObject, 0);
                        } else {
                            this.imageReceiver.setImage(null, null, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50), messageObject.photoThumbsObject), "b", null, 0L, null, messageObject, 0);
                        }
                    }
                    if (z) {
                    }
                    if (this.blurImageReceiver.getBitmap() != null) {
                    }
                    if (this.imageReceiver.getBitmap() != null) {
                        this.blurImageReceiver.setImageBitmap(Utilities.stackBlurBitmapMax(this.imageReceiver.getBitmap()));
                    }
                    invalidate();
                }
            }
            z = true;
            if (z) {
            }
            if (this.blurImageReceiver.getBitmap() != null) {
            }
            if (this.imageReceiver.getBitmap() != null) {
            }
            invalidate();
        }
    }

    private boolean canAutoDownload(MessageObject messageObject) {
        if (System.currentTimeMillis() - lastUpdateDownloadSettingsTime > 5000) {
            lastUpdateDownloadSettingsTime = System.currentTimeMillis();
            lastAutoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject);
        }
        return lastAutoDownload;
    }

    public void setVideoText(String str, boolean z) {
        StaticLayout staticLayout;
        this.videoText = str;
        boolean z2 = str != null;
        this.showVideoLayout = z2;
        if (z2 && (staticLayout = this.videoInfoLayot) != null && !staticLayout.getText().toString().equals(str)) {
            this.videoInfoLayot = null;
        }
        this.drawVideoIcon = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x034b  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0361  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0393  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0148  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0241  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float dpf2;
        float f;
        float measuredWidth;
        float measuredHeight;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        CheckBoxBase checkBoxBase;
        CheckBoxBase checkBoxBase2;
        float dp;
        int dp2;
        String str;
        FlickerLoadingView flickerLoadingView;
        float dp3;
        float dpf22;
        float f7;
        super.onDraw(canvas);
        float f8 = 0.0f;
        if (this.crossfadeProgress != 0.0f) {
            float f9 = this.crossfadeToColumnsCount;
            if (f9 == 9.0f || this.currentParentColumnsCount == 9) {
                if (f9 == 9.0f) {
                    dp3 = AndroidUtilities.dp(0.5f) * this.crossfadeProgress;
                    dpf22 = AndroidUtilities.dpf2(1.0f);
                    f7 = this.crossfadeProgress;
                } else {
                    dp3 = AndroidUtilities.dp(1.0f) * this.crossfadeProgress;
                    dpf22 = AndroidUtilities.dpf2(0.5f);
                    f7 = this.crossfadeProgress;
                }
                dpf2 = dp3 + (dpf22 * (1.0f - f7));
                f = dpf2;
                float f10 = f * 2.0f;
                measuredWidth = (getMeasuredWidth() - f10) * this.imageScale;
                measuredHeight = (getMeasuredHeight() - f10) * this.imageScale;
                if (this.crossfadeProgress > 0.5f && this.crossfadeToColumnsCount != 9.0f && this.currentParentColumnsCount != 9) {
                    measuredWidth -= 2.0f;
                    measuredHeight -= 2.0f;
                }
                f2 = measuredWidth;
                f3 = measuredHeight;
                if ((this.currentMessageObject == null || this.style == 1) && this.imageReceiver.hasBitmapImage() && this.imageReceiver.getCurrentAlpha() == 1.0f && this.imageAlpha == 1.0f) {
                    f4 = f3;
                    f5 = f2;
                } else {
                    if (getParent() == null || (flickerLoadingView = this.globalGradientView) == null) {
                        f4 = f3;
                        f5 = f2;
                    } else {
                        flickerLoadingView.setParentSize(((View) getParent()).getMeasuredWidth(), getMeasuredHeight(), -getX());
                        this.globalGradientView.updateColors();
                        this.globalGradientView.updateGradient();
                        float f11 = (this.crossfadeProgress <= 0.5f || this.crossfadeToColumnsCount == 9.0f || this.currentParentColumnsCount == 9) ? f : f + 1.0f;
                        f4 = f3;
                        f5 = f2;
                        canvas.drawRect(f11, f11, f11 + f2, f11 + f3, this.globalGradientView.getPaint());
                    }
                    invalidate();
                }
                f6 = this.imageAlpha;
                if (f6 == 1.0f) {
                    canvas.saveLayerAlpha(0.0f, 0.0f, f10 + f5, f10 + f4, (int) (f6 * 255.0f), 31);
                } else {
                    canvas.save();
                }
                checkBoxBase = this.checkBoxBase;
                if ((checkBoxBase != null && checkBoxBase.isChecked()) || PhotoViewer.isShowingImage(this.currentMessageObject)) {
                    canvas.drawRect(f, f, f5, f4, this.sharedResources.backgroundPaint);
                }
                if (this.checkBoxProgress > 0.0f) {
                    float dp4 = AndroidUtilities.dp(10.0f) * this.checkBoxProgress;
                    float f12 = f + dp4;
                    float f13 = dp4 * 2.0f;
                    float f14 = f5 - f13;
                    float f15 = f4 - f13;
                    this.imageReceiver.setImageCoords(f12, f12, f14, f15);
                    this.blurImageReceiver.setImageCoords(f12, f12, f14, f15);
                } else {
                    float f16 = (this.crossfadeProgress <= 0.5f || this.crossfadeToColumnsCount == 9.0f || this.currentParentColumnsCount == 9) ? f : f + 1.0f;
                    this.imageReceiver.setImageCoords(f16, f16, f5, f4);
                    this.blurImageReceiver.setImageCoords(f16, f16, f5, f4);
                }
                if (!PhotoViewer.isShowingImage(this.currentMessageObject)) {
                    this.imageReceiver.draw(canvas);
                    MessageObject messageObject = this.currentMessageObject;
                    if (messageObject != null && messageObject.hasMediaSpoilers() && !this.currentMessageObject.isMediaSpoilersRevealedInSharedMedia) {
                        canvas.save();
                        canvas.clipRect(f, f, f + f5, f + f4);
                        if (this.spoilerRevealProgress != 0.0f) {
                            this.path.rewind();
                            this.path.addCircle(this.spoilerRevealX, this.spoilerRevealY, this.spoilerMaxRadius * this.spoilerRevealProgress, Path.Direction.CW);
                            canvas.clipPath(this.path, Region.Op.DIFFERENCE);
                        }
                        this.blurImageReceiver.draw(canvas);
                        this.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
                        this.mediaSpoilerEffect.setBounds((int) this.imageReceiver.getImageX(), (int) this.imageReceiver.getImageY(), (int) this.imageReceiver.getImageX2(), (int) this.imageReceiver.getImageY2());
                        this.mediaSpoilerEffect.draw(canvas);
                        canvas.restore();
                        invalidate();
                    }
                    float f17 = this.highlightProgress;
                    if (f17 > 0.0f) {
                        this.sharedResources.highlightPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (f17 * 0.5f * 255.0f)));
                        canvas.drawRect(this.imageReceiver.getDrawRegion(), this.sharedResources.highlightPaint);
                    }
                }
                if (this.showVideoLayout) {
                    canvas.save();
                    canvas.clipRect(f, f, f + f5, f + f4);
                    if (this.currentParentColumnsCount != 9 && this.videoInfoLayot == null && (str = this.videoText) != null) {
                        this.videoInfoLayot = new StaticLayout(this.videoText, this.sharedResources.textPaint, (int) Math.ceil(this.sharedResources.textPaint.measureText(str)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    if (this.videoInfoLayot == null) {
                        dp2 = AndroidUtilities.dp(17.0f);
                    } else {
                        dp2 = AndroidUtilities.dp(4.0f) + this.videoInfoLayot.getWidth() + AndroidUtilities.dp(4.0f);
                    }
                    if (this.drawVideoIcon) {
                        dp2 += AndroidUtilities.dp(10.0f);
                    }
                    canvas.translate(AndroidUtilities.dp(5.0f), ((AndroidUtilities.dp(1.0f) + f4) - AndroidUtilities.dp(17.0f)) - AndroidUtilities.dp(4.0f));
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, dp2, AndroidUtilities.dp(17.0f));
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    if (this.drawVideoIcon) {
                        canvas.save();
                        canvas.translate(this.videoInfoLayot == null ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(4.0f), (AndroidUtilities.dp(17.0f) - this.sharedResources.playDrawable.getIntrinsicHeight()) / 2.0f);
                        this.sharedResources.playDrawable.setAlpha((int) (this.imageAlpha * 255.0f));
                        this.sharedResources.playDrawable.draw(canvas);
                        canvas.restore();
                    }
                    if (this.videoInfoLayot != null) {
                        canvas.translate(AndroidUtilities.dp((this.drawVideoIcon ? 10 : 0) + 4), (AndroidUtilities.dp(17.0f) - this.videoInfoLayot.getHeight()) / 2.0f);
                        this.videoInfoLayot.draw(canvas);
                    }
                    canvas.restore();
                }
                checkBoxBase2 = this.checkBoxBase;
                if (checkBoxBase2 != null && (this.style == 1 || checkBoxBase2.getProgress() != 0.0f)) {
                    canvas.save();
                    if (this.style != 1) {
                        dp = ((f5 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(25.0f)) - AndroidUtilities.dp(4.0f);
                        f8 = AndroidUtilities.dp(4.0f);
                    } else {
                        dp = (f5 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(25.0f);
                    }
                    canvas.translate(dp, f8);
                    this.checkBoxBase.draw(canvas);
                    if (this.canvasButton != null) {
                        RectF rectF2 = AndroidUtilities.rectTmp;
                        rectF2.set(dp, f8, this.checkBoxBase.bounds.width() + dp, this.checkBoxBase.bounds.height() + f8);
                        this.canvasButton.setRect(rectF2);
                    }
                    canvas.restore();
                }
                canvas.restore();
            }
        }
        dpf2 = this.currentParentColumnsCount == 9 ? AndroidUtilities.dpf2(0.5f) : AndroidUtilities.dpf2(1.0f);
        f = dpf2;
        float f102 = f * 2.0f;
        measuredWidth = (getMeasuredWidth() - f102) * this.imageScale;
        measuredHeight = (getMeasuredHeight() - f102) * this.imageScale;
        if (this.crossfadeProgress > 0.5f) {
            measuredWidth -= 2.0f;
            measuredHeight -= 2.0f;
        }
        f2 = measuredWidth;
        f3 = measuredHeight;
        if (this.currentMessageObject == null) {
        }
        f4 = f3;
        f5 = f2;
        f6 = this.imageAlpha;
        if (f6 == 1.0f) {
        }
        checkBoxBase = this.checkBoxBase;
        if (checkBoxBase != null) {
            canvas.drawRect(f, f, f5, f4, this.sharedResources.backgroundPaint);
            if (this.checkBoxProgress > 0.0f) {
            }
            if (!PhotoViewer.isShowingImage(this.currentMessageObject)) {
            }
            if (this.showVideoLayout) {
            }
            checkBoxBase2 = this.checkBoxBase;
            if (checkBoxBase2 != null) {
                canvas.save();
                if (this.style != 1) {
                }
                canvas.translate(dp, f8);
                this.checkBoxBase.draw(canvas);
                if (this.canvasButton != null) {
                }
                canvas.restore();
            }
            canvas.restore();
        }
        canvas.drawRect(f, f, f5, f4, this.sharedResources.backgroundPaint);
        if (this.checkBoxProgress > 0.0f) {
        }
        if (!PhotoViewer.isShowingImage(this.currentMessageObject)) {
        }
        if (this.showVideoLayout) {
        }
        checkBoxBase2 = this.checkBoxBase;
        if (checkBoxBase2 != null) {
        }
        canvas.restore();
    }

    public boolean canRevealSpoiler() {
        MessageObject messageObject = this.currentMessageObject;
        return messageObject != null && messageObject.hasMediaSpoilers() && this.spoilerRevealProgress == 0.0f && !this.currentMessageObject.isMediaSpoilersRevealedInSharedMedia;
    }

    public void startRevealMedia(float f, float f2) {
        this.spoilerRevealX = f;
        this.spoilerRevealY = f2;
        this.spoilerMaxRadius = (float) Math.sqrt(Math.pow(getWidth(), 2.0d) + Math.pow(getHeight(), 2.0d));
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(MathUtils.clamp(this.spoilerMaxRadius * 0.3f, 250.0f, 550.0f));
        duration.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SharedPhotoVideoCell2.this.lambda$startRevealMedia$2(valueAnimator);
            }
        });
        duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SharedPhotoVideoCell2 sharedPhotoVideoCell2 = SharedPhotoVideoCell2.this;
                sharedPhotoVideoCell2.currentMessageObject.isMediaSpoilersRevealedInSharedMedia = true;
                sharedPhotoVideoCell2.invalidate();
            }
        });
        duration.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRevealMedia$2(ValueAnimator valueAnimator) {
        this.spoilerRevealProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attached = true;
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if (checkBoxBase != null) {
            checkBoxBase.onAttachedToWindow();
        }
        if (this.currentMessageObject != null) {
            this.imageReceiver.onAttachedToWindow();
            this.blurImageReceiver.onAttachedToWindow();
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attached = false;
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if (checkBoxBase != null) {
            checkBoxBase.onDetachedFromWindow();
        }
        if (this.currentMessageObject != null) {
            this.imageReceiver.onDetachedFromWindow();
            this.blurImageReceiver.onDetachedFromWindow();
        }
    }

    public void setGradientView(FlickerLoadingView flickerLoadingView) {
        this.globalGradientView = flickerLoadingView;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
    }

    public int getMessageId() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            return messageObject.getId();
        }
        return 0;
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public void setImageAlpha(float f, boolean z) {
        if (this.imageAlpha != f) {
            this.imageAlpha = f;
            if (z) {
                invalidate();
            }
        }
    }

    public void setImageScale(float f, boolean z) {
        if (this.imageScale != f) {
            this.imageScale = f;
            if (z) {
                invalidate();
            }
        }
    }

    public void setCrossfadeView(SharedPhotoVideoCell2 sharedPhotoVideoCell2, float f, int i) {
        this.crossfadeView = sharedPhotoVideoCell2;
        this.crossfadeProgress = f;
        this.crossfadeToColumnsCount = i;
    }

    public void drawCrossafadeImage(Canvas canvas) {
        if (this.crossfadeView != null) {
            canvas.save();
            canvas.translate(getX(), getY());
            this.crossfadeView.setImageScale(((getMeasuredWidth() - AndroidUtilities.dp(2.0f)) * this.imageScale) / (this.crossfadeView.getMeasuredWidth() - AndroidUtilities.dp(2.0f)), false);
            this.crossfadeView.draw(canvas);
            canvas.restore();
        }
    }

    public View getCrossfadeView() {
        return this.crossfadeView;
    }

    public void setChecked(final boolean z, boolean z2) {
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if ((checkBoxBase != null && checkBoxBase.isChecked()) == z) {
            return;
        }
        if (this.checkBoxBase == null) {
            CheckBoxBase checkBoxBase2 = new CheckBoxBase(this, 21, null);
            this.checkBoxBase = checkBoxBase2;
            checkBoxBase2.setColor(-1, Theme.key_sharedMedia_photoPlaceholder, Theme.key_checkboxCheck);
            this.checkBoxBase.setDrawUnchecked(false);
            this.checkBoxBase.setBackgroundType(1);
            this.checkBoxBase.setBounds(0, 0, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            if (this.attached) {
                this.checkBoxBase.onAttachedToWindow();
            }
        }
        this.checkBoxBase.setChecked(z, z2);
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            this.animator = null;
            valueAnimator.cancel();
        }
        if (z2) {
            float[] fArr = new float[2];
            fArr[0] = this.checkBoxProgress;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.animator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    SharedPhotoVideoCell2.this.checkBoxProgress = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                    SharedPhotoVideoCell2.this.invalidate();
                }
            });
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ValueAnimator valueAnimator2 = SharedPhotoVideoCell2.this.animator;
                    if (valueAnimator2 == null || !valueAnimator2.equals(animator)) {
                        return;
                    }
                    SharedPhotoVideoCell2 sharedPhotoVideoCell2 = SharedPhotoVideoCell2.this;
                    sharedPhotoVideoCell2.checkBoxProgress = z ? 1.0f : 0.0f;
                    sharedPhotoVideoCell2.animator = null;
                }
            });
            this.animator.start();
        } else {
            this.checkBoxProgress = z ? 1.0f : 0.0f;
        }
        invalidate();
    }

    public void setHighlightProgress(float f) {
        if (this.highlightProgress != f) {
            this.highlightProgress = f;
            invalidate();
        }
    }

    /* loaded from: classes3.dex */
    public static class SharedResources {
        Drawable playDrawable;
        TextPaint textPaint = new TextPaint(1);
        private Paint backgroundPaint = new Paint();
        Paint highlightPaint = new Paint();
        SparseArray<String> imageFilters = new SparseArray<>();

        public SharedResources(Context context, Theme.ResourcesProvider resourcesProvider) {
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.textPaint.setColor(-1);
            this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.play_mini_video);
            this.playDrawable = drawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.playDrawable.getIntrinsicHeight());
            this.backgroundPaint.setColor(Theme.getColor(Theme.key_sharedMedia_photoPlaceholder, resourcesProvider));
        }

        public String getFilterString(int i) {
            String str = this.imageFilters.get(i);
            if (str == null) {
                String str2 = i + "_" + i + "_isc";
                this.imageFilters.put(i, str2);
                return str2;
            }
            return str;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton = this.canvasButton;
        if (canvasButton == null || !canvasButton.checkTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }
}
