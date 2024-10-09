package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;

/* loaded from: classes3.dex */
public class PaintingOverlay extends FrameLayout {
    private Drawable backgroundDrawable;
    public boolean drawChildren;
    private boolean ignoreLayout;
    private HashMap mediaEntityViews;
    private Bitmap paintBitmap;

    public PaintingOverlay(Context context) {
        super(context);
        this.drawChildren = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setEntities$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        RLottieDrawable lottieAnimation;
        if (!z || z2 || (lottieAnimation = imageReceiver.getLottieAnimation()) == null) {
            return;
        }
        lottieAnimation.start();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (this.drawChildren) {
            return super.drawChild(canvas, view, j);
        }
        return false;
    }

    public Bitmap getBitmap() {
        return this.paintBitmap;
    }

    public Bitmap getThumb() {
        float measuredWidth = getMeasuredWidth();
        float measuredHeight = getMeasuredHeight();
        float max = Math.max(measuredWidth / AndroidUtilities.dp(120.0f), measuredHeight / AndroidUtilities.dp(120.0f));
        Bitmap createBitmap = Bitmap.createBitmap((int) (measuredWidth / max), (int) (measuredHeight / max), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f = 1.0f / max;
        canvas.scale(f, f);
        draw(canvas);
        return createBitmap;
    }

    public void hideBitmap() {
        setBackground(null);
    }

    public void hideEntities() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(4);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        float f;
        if (this.mediaEntityViews != null) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int childCount = getChildCount();
            for (int i7 = 0; i7 < childCount; i7++) {
                View childAt = getChildAt(i7);
                VideoEditedInfo.MediaEntity mediaEntity = (VideoEditedInfo.MediaEntity) this.mediaEntityViews.get(childAt);
                if (mediaEntity != null) {
                    int measuredWidth2 = childAt.getMeasuredWidth();
                    int measuredHeight2 = childAt.getMeasuredHeight();
                    if (childAt instanceof EditTextOutline) {
                        if (mediaEntity.customTextView) {
                            i5 = ((int) (measuredWidth * (mediaEntity.x + (mediaEntity.width / 2.0f)))) - (childAt.getMeasuredWidth() / 2);
                            f = measuredHeight * (mediaEntity.y + (mediaEntity.height / 2.0f));
                        } else {
                            i5 = ((int) (measuredWidth * mediaEntity.textViewX)) - (childAt.getMeasuredWidth() / 2);
                            f = measuredHeight * mediaEntity.textViewY;
                        }
                        i6 = ((int) f) - (childAt.getMeasuredHeight() / 2);
                    } else {
                        i5 = (int) (measuredWidth * mediaEntity.x);
                        i6 = (int) (measuredHeight * mediaEntity.y);
                    }
                    childAt.layout(i5, i6, measuredWidth2 + i5, measuredHeight2 + i6);
                }
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        this.ignoreLayout = true;
        setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        if (this.mediaEntityViews != null) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                VideoEditedInfo.MediaEntity mediaEntity = (VideoEditedInfo.MediaEntity) this.mediaEntityViews.get(childAt);
                if (mediaEntity != null) {
                    if (childAt instanceof EditTextOutline) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(mediaEntity.viewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
                        float measuredWidth2 = mediaEntity.customTextView ? (mediaEntity.width * getMeasuredWidth()) / mediaEntity.viewWidth : mediaEntity.scale * ((mediaEntity.textViewWidth * measuredWidth) / mediaEntity.viewWidth);
                        childAt.setScaleX(measuredWidth2);
                        childAt.setScaleY(measuredWidth2);
                    } else {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec((int) (measuredWidth * mediaEntity.width), 1073741824), View.MeasureSpec.makeMeasureSpec((int) (measuredHeight * mediaEntity.height), 1073741824));
                    }
                }
            }
        }
        this.ignoreLayout = false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    public void reset() {
        this.paintBitmap = null;
        this.backgroundDrawable = null;
        setBackground(null);
        HashMap hashMap = this.mediaEntityViews;
        if (hashMap != null) {
            hashMap.clear();
        }
        removeAllViews();
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        super.setAlpha(f);
        Drawable drawable = this.backgroundDrawable;
        if (drawable != null) {
            drawable.setAlpha((int) (255.0f * f));
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt.getParent() == this) {
                childAt.setAlpha(f);
            }
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.paintBitmap = bitmap;
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        this.backgroundDrawable = bitmapDrawable;
        setBackground(bitmapDrawable);
    }

    public void setData(String str, ArrayList arrayList, boolean z, boolean z2, boolean z3) {
        BitmapDrawable bitmapDrawable;
        setEntities(arrayList, z, z2, z3);
        if (str != null) {
            this.paintBitmap = BitmapFactory.decodeFile(str);
            bitmapDrawable = new BitmapDrawable(this.paintBitmap);
        } else {
            bitmapDrawable = null;
            this.paintBitmap = null;
        }
        this.backgroundDrawable = bitmapDrawable;
        setBackground(bitmapDrawable);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x01f3 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setEntities(ArrayList arrayList, boolean z, boolean z2, boolean z3) {
        BackupImageView backupImageView;
        BackupImageView backupImageView2;
        setClipChildren(z3);
        reset();
        this.mediaEntityViews = new HashMap();
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            VideoEditedInfo.MediaEntity mediaEntity = (VideoEditedInfo.MediaEntity) arrayList.get(i);
            byte b = mediaEntity.type;
            if (b == 0) {
                BackupImageView backupImageView3 = new BackupImageView(getContext());
                backupImageView3.setLayerNum(12);
                backupImageView3.setAspectFit(true);
                ImageReceiver imageReceiver = backupImageView3.getImageReceiver();
                if (z) {
                    imageReceiver.setAllowDecodeSingleFrame(true);
                    imageReceiver.setAllowStartLottieAnimation(false);
                    if (z2) {
                        imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Components.PaintingOverlay$$ExternalSyntheticLambda0
                            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                            public final void didSetImage(ImageReceiver imageReceiver2, boolean z4, boolean z5, boolean z6) {
                                PaintingOverlay.lambda$setEntities$0(imageReceiver2, z4, z5, z6);
                            }

                            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                            public /* synthetic */ void didSetImageBitmap(int i2, String str, Drawable drawable) {
                                ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i2, str, drawable);
                            }

                            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver2) {
                                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver2);
                            }
                        });
                    }
                }
                imageReceiver.setImage(ImageLocation.getForDocument(mediaEntity.document), null, null, null, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(mediaEntity.document.thumbs, 90), mediaEntity.document), null, null, 0L, "webp", mediaEntity.parentObject, 1);
                backupImageView2 = backupImageView3;
                if ((2 & mediaEntity.subType) != 0) {
                    backupImageView3.setScaleX(-1.0f);
                    backupImageView2 = backupImageView3;
                }
            } else if (b == 1) {
                EditTextOutline editTextOutline = new EditTextOutline(getContext()) { // from class: org.telegram.ui.Components.PaintingOverlay.1
                    @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
                    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                        return false;
                    }

                    @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        return false;
                    }
                };
                editTextOutline.setBackgroundColor(0);
                editTextOutline.setPadding(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f));
                editTextOutline.setTextSize(0, mediaEntity.fontSize);
                editTextOutline.setTypeface(mediaEntity.textTypeface.getTypeface());
                SpannableString spannableString = new SpannableString(Emoji.replaceEmoji((CharSequence) mediaEntity.text, editTextOutline.getPaint().getFontMetricsInt(), (int) (editTextOutline.getTextSize() * 0.8f), false));
                Iterator<VideoEditedInfo.EmojiEntity> it = mediaEntity.entities.iterator();
                while (it.hasNext()) {
                    VideoEditedInfo.EmojiEntity next = it.next();
                    AnimatedEmojiSpan animatedEmojiSpan = new AnimatedEmojiSpan(next.document_id, editTextOutline.getPaint().getFontMetricsInt());
                    int i2 = next.offset;
                    spannableString.setSpan(animatedEmojiSpan, i2, next.length + i2, 33);
                }
                Emoji.EmojiSpan[] emojiSpanArr = (Emoji.EmojiSpan[]) spannableString.getSpans(0, spannableString.length(), Emoji.EmojiSpan.class);
                if (emojiSpanArr != null) {
                    for (Emoji.EmojiSpan emojiSpan : emojiSpanArr) {
                        emojiSpan.scale = 0.85f;
                    }
                }
                editTextOutline.setText(spannableString);
                editTextOutline.setGravity(17);
                int i3 = mediaEntity.textAlign;
                editTextOutline.setGravity(i3 != 1 ? i3 != 2 ? 19 : 21 : 17);
                int i4 = Build.VERSION.SDK_INT;
                int i5 = mediaEntity.textAlign;
                editTextOutline.setTextAlignment(i5 != 1 ? (i5 == 2 ? !LocaleController.isRTL : LocaleController.isRTL) ? 3 : 2 : 4);
                editTextOutline.setHorizontallyScrolling(false);
                editTextOutline.setImeOptions(268435456);
                editTextOutline.setFocusableInTouchMode(true);
                editTextOutline.setEnabled(false);
                editTextOutline.setInputType(editTextOutline.getInputType() | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                if (i4 >= 23) {
                    editTextOutline.setBreakStrategy(0);
                }
                editTextOutline.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                int i6 = mediaEntity.color;
                byte b2 = mediaEntity.subType;
                int i7 = -1;
                if (b2 == 0) {
                    editTextOutline.setFrameColor(i6);
                    i6 = AndroidUtilities.computePerceivedBrightness(mediaEntity.color) >= 0.721f ? -16777216 : -1;
                } else {
                    if (b2 == 1) {
                        i7 = AndroidUtilities.computePerceivedBrightness(i6) >= 0.25f ? -1728053248 : -1711276033;
                    } else if (b2 != 2) {
                        editTextOutline.setFrameColor(0);
                    } else if (AndroidUtilities.computePerceivedBrightness(i6) >= 0.25f) {
                        i7 = -16777216;
                    }
                    editTextOutline.setFrameColor(i7);
                }
                editTextOutline.setTextColor(i6);
                editTextOutline.setCursorColor(i6);
                editTextOutline.setHandlesColor(i6);
                editTextOutline.setHighlightColor(Theme.multAlpha(i6, 0.4f));
                backupImageView2 = editTextOutline;
            } else {
                backupImageView = null;
                if (backupImageView == null) {
                    addView(backupImageView);
                    double d = -mediaEntity.rotation;
                    Double.isNaN(d);
                    backupImageView.setRotation((float) ((d / 3.141592653589793d) * 180.0d));
                    this.mediaEntityViews.put(backupImageView, mediaEntity);
                }
            }
            mediaEntity.view = backupImageView2;
            backupImageView = backupImageView2;
            if (backupImageView == null) {
            }
        }
    }

    public void showAll() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(0);
        }
        setBackground(this.backgroundDrawable);
    }
}
