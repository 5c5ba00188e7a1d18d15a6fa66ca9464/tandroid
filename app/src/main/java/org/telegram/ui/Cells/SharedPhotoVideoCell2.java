package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes4.dex */
public class SharedPhotoVideoCell2 extends View {
    static boolean lastAutoDownload;
    static long lastUpdateDownloadSettingsTime;
    ValueAnimator animator;
    private boolean attached;
    CheckBoxBase checkBoxBase;
    float checkBoxProgress;
    float crossfadeProgress;
    float crossfadeToColumnsCount;
    SharedPhotoVideoCell2 crossfadeView;
    int currentAccount;
    MessageObject currentMessageObject;
    int currentParentColumnsCount;
    FlickerLoadingView globalGradientView;
    float highlightProgress;
    SharedResources sharedResources;
    boolean showVideoLayout;
    StaticLayout videoInfoLayot;
    String videoText;
    public ImageReceiver imageReceiver = new ImageReceiver();
    float imageAlpha = 1.0f;
    float imageScale = 1.0f;

    public SharedPhotoVideoCell2(Context context, SharedResources sharedResources, int currentAccount) {
        super(context);
        this.sharedResources = sharedResources;
        this.currentAccount = currentAccount;
        setChecked(false, false);
        this.imageReceiver.setParentView(this);
    }

    public void setMessageObject(MessageObject messageObject, int parentColumnsCount) {
        int stride;
        TLRPC.PhotoSize currentPhotoObjectThumb;
        TLRPC.PhotoSize qualityThumb;
        int stride2;
        String imageFilter;
        int oldParentColumsCount = this.currentParentColumnsCount;
        this.currentParentColumnsCount = parentColumnsCount;
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 == null && messageObject == null) {
            return;
        }
        if (messageObject2 != null && messageObject != null && messageObject2.getId() == messageObject.getId() && oldParentColumsCount == parentColumnsCount) {
            return;
        }
        this.currentMessageObject = messageObject;
        if (messageObject == null) {
            this.imageReceiver.onDetachedFromWindow();
            this.videoText = null;
            this.videoInfoLayot = null;
            this.showVideoLayout = false;
            return;
        }
        if (this.attached) {
            this.imageReceiver.onAttachedToWindow();
        }
        String restrictionReason = MessagesController.getRestrictionReason(messageObject.messageOwner.restriction_reason);
        int width = (int) ((AndroidUtilities.displaySize.x / parentColumnsCount) / AndroidUtilities.density);
        String imageFilter2 = this.sharedResources.getFilterString(width);
        boolean showImageStub = false;
        if (parentColumnsCount <= 2) {
            stride = AndroidUtilities.getPhotoSize();
        } else if (parentColumnsCount == 3) {
            stride = 320;
        } else if (parentColumnsCount == 5) {
            stride = 320;
        } else {
            stride = 320;
        }
        this.videoText = null;
        this.videoInfoLayot = null;
        this.showVideoLayout = false;
        if (!TextUtils.isEmpty(restrictionReason)) {
            showImageStub = true;
        } else if (messageObject.isVideo()) {
            this.showVideoLayout = true;
            if (parentColumnsCount != 9) {
                this.videoText = AndroidUtilities.formatShortDuration(messageObject.getDuration());
            }
            if (messageObject.mediaThumb != null) {
                if (messageObject.strippedThumb != null) {
                    this.imageReceiver.setImage(messageObject.mediaThumb, imageFilter2, messageObject.strippedThumb, null, messageObject, 0);
                } else {
                    this.imageReceiver.setImage(messageObject.mediaThumb, imageFilter2, messageObject.mediaSmallThumb, imageFilter2 + "_b", null, 0L, null, messageObject, 0);
                }
            } else {
                TLRPC.Document document = messageObject.getDocument();
                TLRPC.PhotoSize thumb = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                TLRPC.PhotoSize qualityThumb2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, stride);
                if (thumb != qualityThumb2) {
                    qualityThumb = qualityThumb2;
                } else {
                    qualityThumb = null;
                }
                if (thumb == null) {
                    stride2 = stride;
                    imageFilter = imageFilter2;
                    showImageStub = true;
                } else if (messageObject.strippedThumb != null) {
                    this.imageReceiver.setImage(ImageLocation.getForDocument(qualityThumb, document), imageFilter2, messageObject.strippedThumb, null, messageObject, 0);
                    stride2 = stride;
                    imageFilter = imageFilter2;
                } else {
                    imageFilter = imageFilter2;
                    stride2 = stride;
                    this.imageReceiver.setImage(ImageLocation.getForDocument(qualityThumb, document), imageFilter2, ImageLocation.getForDocument(thumb, document), imageFilter2 + "_b", null, 0L, null, messageObject, 0);
                }
            }
        } else {
            int stride3 = stride;
            if ((messageObject.messageOwner.media instanceof TLRPC.TL_messageMediaPhoto) && messageObject.messageOwner.media.photo != null && !messageObject.photoThumbs.isEmpty()) {
                if (messageObject.mediaExists || canAutoDownload(messageObject)) {
                    if (messageObject.mediaThumb != null) {
                        if (messageObject.strippedThumb != null) {
                            this.imageReceiver.setImage(messageObject.mediaThumb, imageFilter2, messageObject.strippedThumb, null, messageObject, 0);
                        } else {
                            this.imageReceiver.setImage(messageObject.mediaThumb, imageFilter2, messageObject.mediaSmallThumb, imageFilter2 + "_b", null, 0L, null, messageObject, 0);
                        }
                    } else {
                        TLRPC.PhotoSize currentPhotoObjectThumb2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
                        TLRPC.PhotoSize currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, stride3, false, currentPhotoObjectThumb2, false);
                        if (currentPhotoObject != currentPhotoObjectThumb2) {
                            currentPhotoObjectThumb = currentPhotoObjectThumb2;
                        } else {
                            currentPhotoObjectThumb = null;
                        }
                        if (messageObject.strippedThumb == null) {
                            this.imageReceiver.setImage(ImageLocation.getForObject(currentPhotoObject, messageObject.photoThumbsObject), imageFilter2, ImageLocation.getForObject(currentPhotoObjectThumb, messageObject.photoThumbsObject), imageFilter2 + "_b", currentPhotoObject != null ? currentPhotoObject.size : 0L, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                        } else {
                            this.imageReceiver.setImage(ImageLocation.getForObject(currentPhotoObject, messageObject.photoThumbsObject), imageFilter2, null, null, messageObject.strippedThumb, currentPhotoObject != null ? currentPhotoObject.size : 0L, null, messageObject, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                        }
                    }
                } else if (messageObject.strippedThumb != null) {
                    this.imageReceiver.setImage(null, null, null, null, messageObject.strippedThumb, 0L, null, messageObject, 0);
                } else {
                    this.imageReceiver.setImage(null, null, ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50), messageObject.photoThumbsObject), "b", null, 0L, null, messageObject, 0);
                }
            } else {
                showImageStub = true;
            }
        }
        if (showImageStub) {
            this.imageReceiver.setImageBitmap(ContextCompat.getDrawable(getContext(), R.drawable.photo_placeholder_in));
        }
        invalidate();
    }

    private boolean canAutoDownload(MessageObject messageObject) {
        if (System.currentTimeMillis() - lastUpdateDownloadSettingsTime > DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
            lastUpdateDownloadSettingsTime = System.currentTimeMillis();
            lastAutoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject);
        }
        return lastAutoDownload;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x010c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x010d  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float padding;
        float imageWidth;
        float imageHeight;
        float imageWidth2;
        float imageHeight2;
        float imageHeight3;
        float imageWidth3;
        float imageWidth4;
        int width;
        float localPadding;
        super.onDraw(canvas);
        if (this.crossfadeProgress != 0.0f) {
            float f = this.crossfadeToColumnsCount;
            if (f == 9.0f || this.currentParentColumnsCount == 9) {
                padding = f == 9.0f ? (AndroidUtilities.dp(0.5f) * this.crossfadeProgress) + (AndroidUtilities.dpf2(1.0f) * (1.0f - this.crossfadeProgress)) : (AndroidUtilities.dp(1.0f) * this.crossfadeProgress) + (AndroidUtilities.dpf2(0.5f) * (1.0f - this.crossfadeProgress));
                imageWidth = (getMeasuredWidth() - (padding * 2.0f)) * this.imageScale;
                imageHeight = (getMeasuredHeight() - (padding * 2.0f)) * this.imageScale;
                if (this.crossfadeProgress <= 0.5f && this.crossfadeToColumnsCount != 9.0f && this.currentParentColumnsCount != 9) {
                    imageWidth2 = imageWidth - 2.0f;
                    imageHeight2 = imageHeight - 2.0f;
                } else {
                    imageWidth2 = imageWidth;
                    imageHeight2 = imageHeight;
                }
                if (this.currentMessageObject == null && this.imageReceiver.hasBitmapImage() && this.imageReceiver.getCurrentAlpha() == 1.0f && this.imageAlpha == 1.0f) {
                    imageHeight3 = imageHeight2;
                } else {
                    if (getParent() != null) {
                        imageHeight3 = imageHeight2;
                    } else {
                        this.globalGradientView.setParentSize(((View) getParent()).getMeasuredWidth(), getMeasuredHeight(), -getX());
                        this.globalGradientView.updateColors();
                        this.globalGradientView.updateGradient();
                        float localPadding2 = padding;
                        if (this.crossfadeProgress > 0.5f && this.crossfadeToColumnsCount != 9.0f && this.currentParentColumnsCount != 9) {
                            localPadding = localPadding2 + 1.0f;
                        } else {
                            localPadding = localPadding2;
                        }
                        imageHeight3 = imageHeight2;
                        canvas.drawRect(localPadding, localPadding, localPadding + imageWidth2, localPadding + imageHeight2, this.globalGradientView.getPaint());
                    }
                    invalidate();
                }
                if (this.currentMessageObject != null) {
                    return;
                }
                float f2 = this.imageAlpha;
                if (f2 != 1.0f) {
                    imageWidth3 = imageWidth2;
                    canvas.saveLayerAlpha(0.0f, 0.0f, (padding * 2.0f) + imageWidth2, (padding * 2.0f) + imageHeight3, (int) (f2 * 255.0f), 31);
                } else {
                    imageWidth3 = imageWidth2;
                    canvas.save();
                }
                CheckBoxBase checkBoxBase = this.checkBoxBase;
                if ((checkBoxBase != null && checkBoxBase.isChecked()) || PhotoViewer.isShowingImage(this.currentMessageObject)) {
                    canvas.drawRect(padding, padding, imageWidth3, imageHeight3, this.sharedResources.backgroundPaint);
                }
                if (this.currentMessageObject == null) {
                    imageWidth4 = imageWidth3;
                } else {
                    if (this.checkBoxProgress > 0.0f) {
                        float offset = AndroidUtilities.dp(10.0f) * this.checkBoxProgress;
                        imageWidth4 = imageWidth3;
                        this.imageReceiver.setImageCoords(padding + offset, padding + offset, imageWidth4 - (offset * 2.0f), imageHeight3 - (offset * 2.0f));
                    } else {
                        imageWidth4 = imageWidth3;
                        float localPadding3 = padding;
                        if (this.crossfadeProgress > 0.5f && this.crossfadeToColumnsCount != 9.0f && this.currentParentColumnsCount != 9) {
                            localPadding3 += 1.0f;
                        }
                        this.imageReceiver.setImageCoords(localPadding3, localPadding3, imageWidth4, imageHeight3);
                    }
                    if (!PhotoViewer.isShowingImage(this.currentMessageObject)) {
                        this.imageReceiver.draw(canvas);
                        if (this.highlightProgress > 0.0f) {
                            this.sharedResources.highlightPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (this.highlightProgress * 0.5f * 255.0f)));
                            canvas.drawRect(this.imageReceiver.getDrawRegion(), this.sharedResources.highlightPaint);
                        }
                    }
                }
                if (this.showVideoLayout) {
                    canvas.save();
                    canvas.clipRect(padding, padding, padding + imageWidth4, padding + imageHeight3);
                    if (this.currentParentColumnsCount != 9 && this.videoInfoLayot == null && this.videoText != null) {
                        int textWidth = (int) Math.ceil(this.sharedResources.textPaint.measureText(this.videoText));
                        this.videoInfoLayot = new StaticLayout(this.videoText, this.sharedResources.textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    if (this.videoInfoLayot == null) {
                        width = AndroidUtilities.dp(17.0f);
                    } else {
                        int width2 = AndroidUtilities.dp(14.0f);
                        width = width2 + this.videoInfoLayot.getWidth() + AndroidUtilities.dp(4.0f);
                    }
                    canvas.translate(AndroidUtilities.dp(5.0f), ((AndroidUtilities.dp(1.0f) + imageHeight3) - AndroidUtilities.dp(17.0f)) - AndroidUtilities.dp(4.0f));
                    AndroidUtilities.rectTmp.set(0.0f, 0.0f, width, AndroidUtilities.dp(17.0f));
                    canvas.drawRoundRect(AndroidUtilities.rectTmp, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    canvas.save();
                    canvas.translate(this.videoInfoLayot == null ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(4.0f), (AndroidUtilities.dp(17.0f) - this.sharedResources.playDrawable.getIntrinsicHeight()) / 2.0f);
                    this.sharedResources.playDrawable.setAlpha((int) (this.imageAlpha * 255.0f));
                    this.sharedResources.playDrawable.draw(canvas);
                    canvas.restore();
                    if (this.videoInfoLayot != null) {
                        canvas.translate(AndroidUtilities.dp(14.0f), (AndroidUtilities.dp(17.0f) - this.videoInfoLayot.getHeight()) / 2.0f);
                        this.videoInfoLayot.draw(canvas);
                    }
                    canvas.restore();
                }
                CheckBoxBase checkBoxBase2 = this.checkBoxBase;
                if (checkBoxBase2 != null && checkBoxBase2.getProgress() != 0.0f) {
                    canvas.save();
                    canvas.translate((imageWidth4 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(25.0f), 0.0f);
                    this.checkBoxBase.draw(canvas);
                    canvas.restore();
                }
                canvas.restore();
                return;
            }
        }
        padding = this.currentParentColumnsCount == 9 ? AndroidUtilities.dpf2(0.5f) : AndroidUtilities.dpf2(1.0f);
        imageWidth = (getMeasuredWidth() - (padding * 2.0f)) * this.imageScale;
        imageHeight = (getMeasuredHeight() - (padding * 2.0f)) * this.imageScale;
        if (this.crossfadeProgress <= 0.5f) {
        }
        imageWidth2 = imageWidth;
        imageHeight2 = imageHeight;
        if (this.currentMessageObject == null) {
        }
        if (getParent() != null) {
        }
        invalidate();
        if (this.currentMessageObject != null) {
        }
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
        }
    }

    public void setGradientView(FlickerLoadingView globalGradientView) {
        this.globalGradientView = globalGradientView;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), C.BUFFER_FLAG_ENCRYPTED));
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

    public void setImageAlpha(float alpha, boolean invalidate) {
        if (this.imageAlpha != alpha) {
            this.imageAlpha = alpha;
            if (invalidate) {
                invalidate();
            }
        }
    }

    public void setImageScale(float scale, boolean invalidate) {
        if (this.imageScale != scale) {
            this.imageScale = scale;
            if (invalidate) {
                invalidate();
            }
        }
    }

    public void setCrossfadeView(SharedPhotoVideoCell2 cell, float crossfadeProgress, int crossfadeToColumnsCount) {
        this.crossfadeView = cell;
        this.crossfadeProgress = crossfadeProgress;
        this.crossfadeToColumnsCount = crossfadeToColumnsCount;
    }

    public void drawCrossafadeImage(Canvas canvas) {
        if (this.crossfadeView != null) {
            canvas.save();
            canvas.translate(getX(), getY());
            float scale = ((getMeasuredWidth() - AndroidUtilities.dp(2.0f)) * this.imageScale) / (this.crossfadeView.getMeasuredWidth() - AndroidUtilities.dp(2.0f));
            this.crossfadeView.setImageScale(scale, false);
            this.crossfadeView.draw(canvas);
            canvas.restore();
        }
    }

    public View getCrossfadeView() {
        return this.crossfadeView;
    }

    public void setChecked(final boolean checked, boolean animated) {
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        boolean currentIsChecked = checkBoxBase != null && checkBoxBase.isChecked();
        if (currentIsChecked == checked) {
            return;
        }
        if (this.checkBoxBase == null) {
            CheckBoxBase checkBoxBase2 = new CheckBoxBase(this, 21, null);
            this.checkBoxBase = checkBoxBase2;
            checkBoxBase2.setColor(null, Theme.key_sharedMedia_photoPlaceholder, Theme.key_checkboxCheck);
            this.checkBoxBase.setDrawUnchecked(false);
            this.checkBoxBase.setBackgroundType(1);
            this.checkBoxBase.setBounds(0, 0, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
            if (this.attached) {
                this.checkBoxBase.onAttachedToWindow();
            }
        }
        this.checkBoxBase.setChecked(checked, animated);
        if (this.animator != null) {
            ValueAnimator animatorFinal = this.animator;
            this.animator = null;
            animatorFinal.cancel();
        }
        float f = 1.0f;
        if (animated) {
            float[] fArr = new float[2];
            fArr[0] = this.checkBoxProgress;
            if (!checked) {
                f = 0.0f;
            }
            fArr[1] = f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.animator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SharedPhotoVideoCell2.this.checkBoxProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    SharedPhotoVideoCell2.this.invalidate();
                }
            });
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    if (SharedPhotoVideoCell2.this.animator != null && SharedPhotoVideoCell2.this.animator.equals(animation)) {
                        SharedPhotoVideoCell2.this.checkBoxProgress = checked ? 1.0f : 0.0f;
                        SharedPhotoVideoCell2.this.animator = null;
                    }
                }
            });
            this.animator.start();
        } else {
            if (!checked) {
                f = 0.0f;
            }
            this.checkBoxProgress = f;
        }
        invalidate();
    }

    public void startHighlight() {
    }

    public void setHighlightProgress(float p) {
        if (this.highlightProgress != p) {
            this.highlightProgress = p;
            invalidate();
        }
    }

    public void moveImageToFront() {
        this.imageReceiver.moveImageToFront();
    }

    /* loaded from: classes4.dex */
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

        public String getFilterString(int width) {
            String str = this.imageFilters.get(width);
            if (str == null) {
                String str2 = width + "_" + width + "_isc";
                this.imageFilters.put(width, str2);
                return str2;
            }
            return str;
        }
    }
}
