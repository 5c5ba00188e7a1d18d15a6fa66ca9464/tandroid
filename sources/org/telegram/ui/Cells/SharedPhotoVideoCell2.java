package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.CheckBoxBase;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.Shaker;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoryWidgetsImageDecorator;
import org.telegram.ui.Stories.recorder.DominantColors;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;

/* loaded from: classes4.dex */
public class SharedPhotoVideoCell2 extends FrameLayout {
    static boolean lastAutoDownload;
    static long lastUpdateDownloadSettingsTime;
    private final AnimatedFloat animatedProgress;
    ValueAnimator animator;
    private boolean attached;
    private Text authorText;
    public ImageReceiver blurImageReceiver;
    private final RectF bounds;
    CanvasButton canvasButton;
    private boolean check2;
    CheckBoxBase checkBoxBase;
    float checkBoxProgress;
    float crossfadeProgress;
    float crossfadeToColumnsCount;
    SharedPhotoVideoCell2 crossfadeView;
    int currentAccount;
    MessageObject currentMessageObject;
    int currentParentColumnsCount;
    boolean drawVideoIcon;
    boolean drawViews;
    FlickerLoadingView globalGradientView;
    private Drawable gradientDrawable;
    private boolean gradientDrawableLoading;
    float highlightProgress;
    float imageAlpha;
    public ImageReceiver imageReceiver;
    public int imageReceiverColor;
    float imageScale;
    public boolean isFirst;
    public boolean isLast;
    public boolean isSearchingHashtag;
    public boolean isStory;
    public boolean isStoryPinned;
    public boolean isStoryUploading;
    private SpoilerEffect mediaSpoilerEffect;
    private SpoilerEffect2 mediaSpoilerEffect2;
    private Path path;
    private Bitmap privacyBitmap;
    private Paint privacyPaint;
    private int privacyType;
    private final Paint progressPaint;
    private boolean reorder;
    private final Paint scrimPaint;
    private Shaker shaker;
    SharedResources sharedResources;
    boolean showVideoLayout;
    private float spoilerMaxRadius;
    private float spoilerRevealProgress;
    private float spoilerRevealX;
    private float spoilerRevealY;
    public int storyId;
    private int style;
    StaticLayout videoInfoLayot;
    String videoText;
    AnimatedFloat viewsAlpha;
    AnimatedTextView.AnimatedTextDrawable viewsText;

    public static class SharedResources {
        Drawable playDrawable;
        Drawable viewDrawable;
        TextPaint textPaint = new TextPaint(1);
        private Paint backgroundPaint = new Paint();
        Paint highlightPaint = new Paint();
        SparseArray imageFilters = new SparseArray();
        private final HashMap privacyBitmaps = new HashMap();

        public SharedResources(Context context, Theme.ResourcesProvider resourcesProvider) {
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.textPaint.setColor(-1);
            this.textPaint.setTypeface(AndroidUtilities.bold());
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.play_mini_video);
            this.playDrawable = drawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.playDrawable.getIntrinsicHeight());
            Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.filled_views);
            this.viewDrawable = drawable2;
            drawable2.setBounds(0, 0, (int) (drawable2.getIntrinsicWidth() * 0.7f), (int) (this.viewDrawable.getIntrinsicHeight() * 0.7f));
            this.backgroundPaint.setColor(Theme.getColor(Theme.key_sharedMedia_photoPlaceholder, resourcesProvider));
        }

        public String getFilterString(int i) {
            String str = (String) this.imageFilters.get(i);
            if (str != null) {
                return str;
            }
            String str2 = i + "_" + i + "_isc";
            this.imageFilters.put(i, str2);
            return str2;
        }

        public Bitmap getPrivacyBitmap(Context context, int i) {
            Bitmap bitmap = (Bitmap) this.privacyBitmaps.get(Integer.valueOf(i));
            if (bitmap != null) {
                return bitmap;
            }
            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i);
            int width = decodeResource.getWidth();
            int height = decodeResource.getHeight();
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            Bitmap createBitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint(3);
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            paint.setColorFilter(new PorterDuffColorFilter(-10461088, mode));
            canvas.drawBitmap(decodeResource, 0.0f, 0.0f, paint);
            Utilities.stackBlurBitmap(createBitmap, AndroidUtilities.dp(1.0f));
            Bitmap createBitmap2 = Bitmap.createBitmap(decodeResource.getWidth(), decodeResource.getHeight(), config);
            Canvas canvas2 = new Canvas(createBitmap2);
            canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            paint.setColorFilter(new PorterDuffColorFilter(-1, mode));
            canvas2.drawBitmap(decodeResource, 0.0f, 0.0f, paint);
            createBitmap.recycle();
            decodeResource.recycle();
            this.privacyBitmaps.put(Integer.valueOf(i), createBitmap2);
            return createBitmap2;
        }
    }

    public SharedPhotoVideoCell2(Context context, SharedResources sharedResources, int i) {
        super(context);
        this.imageReceiverColor = 0;
        this.imageReceiver = new ImageReceiver();
        this.blurImageReceiver = new ImageReceiver();
        this.imageAlpha = 1.0f;
        this.imageScale = 1.0f;
        this.drawVideoIcon = true;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.viewsAlpha = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.viewsText = new AnimatedTextView.AnimatedTextDrawable(false, true, true);
        this.path = new Path();
        this.style = 0;
        this.scrimPaint = new Paint(1);
        this.progressPaint = new Paint(1);
        this.animatedProgress = new AnimatedFloat(this, 0L, 200L, cubicBezierInterpolator);
        this.bounds = new RectF();
        this.sharedResources = sharedResources;
        this.currentAccount = i;
        setChecked(false, false);
        this.imageReceiver.setParentView(this);
        this.blurImageReceiver.setParentView(this);
        this.imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda1
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                SharedPhotoVideoCell2.this.lambda$new$0(imageReceiver, z, z2, z3);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void didSetImageBitmap(int i2, String str, Drawable drawable) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i2, str, drawable);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
            }
        });
        this.viewsText.setCallback(this);
        this.viewsText.setTextSize(AndroidUtilities.dp(12.0f));
        this.viewsText.setTextColor(-1);
        this.viewsText.setTypeface(AndroidUtilities.bold());
        this.viewsText.setOverrideFullWidth(AndroidUtilities.displaySize.x);
        setWillNotDraw(false);
    }

    private boolean canAutoDownload(MessageObject messageObject) {
        if (System.currentTimeMillis() - lastUpdateDownloadSettingsTime > 5000) {
            lastUpdateDownloadSettingsTime = System.currentTimeMillis();
            lastAutoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject);
        }
        return lastAutoDownload;
    }

    private float getPadding() {
        float dpf2;
        float dpf22;
        if (this.crossfadeProgress != 0.0f) {
            float f = this.crossfadeToColumnsCount;
            if (f == 9.0f || this.currentParentColumnsCount == 9) {
                if (f == 9.0f) {
                    dpf2 = AndroidUtilities.dpf2(0.5f) * this.crossfadeProgress;
                    dpf22 = AndroidUtilities.dpf2(1.0f);
                } else {
                    dpf2 = AndroidUtilities.dpf2(1.0f) * this.crossfadeProgress;
                    dpf22 = AndroidUtilities.dpf2(0.5f);
                }
                return dpf2 + (dpf22 * (1.0f - this.crossfadeProgress));
            }
        }
        return this.currentParentColumnsCount == 9 ? AndroidUtilities.dpf2(0.5f) : AndroidUtilities.dpf2(1.0f);
    }

    private TLRPC.MessageMedia getStoryMedia(MessageObject messageObject) {
        TL_stories.StoryItem storyItem;
        if (messageObject == null || (storyItem = messageObject.storyItem) == null) {
            return null;
        }
        return storyItem.media;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        MessageObject messageObject;
        if (z && !z2 && (messageObject = this.currentMessageObject) != null && messageObject.hasMediaSpoilers() && this.imageReceiver.getBitmap() != null) {
            if (this.blurImageReceiver.getBitmap() != null) {
                this.blurImageReceiver.getBitmap().recycle();
            }
            this.blurImageReceiver.setImageBitmap(Utilities.stackBlurBitmapMax(this.imageReceiver.getBitmap()));
        }
        if (!z || z2 || !this.check2 || this.imageReceiver.getBitmap() == null) {
            return;
        }
        int dominantColor = AndroidUtilities.getDominantColor(this.imageReceiver.getBitmap());
        this.imageReceiverColor = dominantColor;
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if (checkBoxBase != null) {
            checkBoxBase.setBackgroundColor(Theme.blendOver(dominantColor, Theme.multAlpha(-1, 0.25f)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$2(int[] iArr) {
        if (this.gradientDrawableLoading) {
            this.gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, iArr);
            invalidate();
            this.gradientDrawableLoading = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startRevealMedia$3(ValueAnimator valueAnimator) {
        this.spoilerRevealProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    private boolean mediaEqual(TLRPC.MessageMedia messageMedia, TLRPC.MessageMedia messageMedia2) {
        TLRPC.Photo photo;
        if (messageMedia == null && messageMedia2 == null) {
            return true;
        }
        if (messageMedia != null && messageMedia2 != null) {
            TLRPC.Document document = messageMedia.document;
            if (document != null) {
                TLRPC.Document document2 = messageMedia2.document;
                return document2 != null && document2.id == document.id;
            }
            TLRPC.Photo photo2 = messageMedia.photo;
            return (photo2 == null || (photo = messageMedia2.photo) == null || photo.id != photo2.id) ? false : true;
        }
        return false;
    }

    private void setPrivacyType(int i, int i2) {
        if (this.privacyType == i) {
            return;
        }
        this.privacyType = i;
        this.privacyBitmap = null;
        if (i2 != 0) {
            this.privacyBitmap = this.sharedResources.getPrivacyBitmap(getContext(), i2);
        }
        invalidate();
    }

    private void updateSpoilers2() {
        SpoilerEffect2 spoilerEffect2;
        if (getMeasuredHeight() <= 0 || getMeasuredWidth() <= 0) {
            return;
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || !messageObject.hasMediaSpoilers() || !SpoilerEffect2.supports()) {
            SpoilerEffect2 spoilerEffect22 = this.mediaSpoilerEffect2;
            if (spoilerEffect22 == null) {
                return;
            }
            spoilerEffect22.detach(this);
            spoilerEffect2 = null;
        } else if (this.mediaSpoilerEffect2 != null) {
            return;
        } else {
            spoilerEffect2 = SpoilerEffect2.getInstance(this);
        }
        this.mediaSpoilerEffect2 = spoilerEffect2;
    }

    public boolean canRevealSpoiler() {
        MessageObject messageObject = this.currentMessageObject;
        return messageObject != null && messageObject.hasMediaSpoilers() && this.spoilerRevealProgress == 0.0f && !this.currentMessageObject.isMediaSpoilersRevealedInSharedMedia;
    }

    public void drawAuthor(Canvas canvas, RectF rectF, float f) {
        if (this.isStory) {
            ImageReceiver imageReceiver = this.imageReceiver;
            if ((imageReceiver == null || imageReceiver.getVisible()) && this.isSearchingHashtag && this.authorText != null) {
                this.authorText.ellipsize((int) (rectF.width() - (2.0f * r0))).setVerticalClipPadding(AndroidUtilities.dp(14.0f)).setShadow(0.4f * f).draw(canvas, rectF.left + AndroidUtilities.dp(5.33f), rectF.top + AndroidUtilities.dp(this.currentParentColumnsCount <= 2 ? 15.0f : 11.33f), Theme.multAlpha(-1, f), 1.0f);
            }
        }
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

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void drawDuration(Canvas canvas, RectF rectF, float f) {
        StaticLayout staticLayout;
        String str;
        float f2 = f;
        if (this.showVideoLayout) {
            ImageReceiver imageReceiver = this.imageReceiver;
            if (imageReceiver == null || imageReceiver.getVisible()) {
                float width = rectF.width() + (AndroidUtilities.dp(20.0f) * this.checkBoxProgress);
                float width2 = rectF.width() / width;
                if (f2 < 1.0f) {
                    f2 = (float) Math.pow(f2, 8.0d);
                }
                canvas.save();
                canvas.translate(rectF.left, rectF.top);
                canvas.scale(width2, width2, 0.0f, rectF.height());
                canvas.clipRect(0.0f, 0.0f, rectF.width(), rectF.height());
                int i = this.currentParentColumnsCount;
                if (i == 9 || this.videoInfoLayot != null || (str = this.videoText) == null) {
                    if ((i >= 9 || this.videoText == null) && this.videoInfoLayot != null) {
                        staticLayout = null;
                    }
                    boolean viewsOnLeft = viewsOnLeft(width);
                    int dp = AndroidUtilities.dp(8.0f);
                    StaticLayout staticLayout2 = this.videoInfoLayot;
                    int width3 = dp + (staticLayout2 == null ? staticLayout2.getWidth() : 0) + (!this.drawVideoIcon ? AndroidUtilities.dp(10.0f) : 0);
                    canvas.translate(AndroidUtilities.dp(5.0f), (((AndroidUtilities.dp(1.0f) + rectF.height()) - AndroidUtilities.dp(17.0f)) - AndroidUtilities.dp(4.0f)) - (!viewsOnLeft ? AndroidUtilities.dp(22.0f) : 0));
                    RectF rectF2 = AndroidUtilities.rectTmp;
                    rectF2.set(0.0f, 0.0f, width3, AndroidUtilities.dp(17.0f));
                    int alpha = Theme.chat_timeBackgroundPaint.getAlpha();
                    Theme.chat_timeBackgroundPaint.setAlpha((int) (alpha * f2));
                    canvas.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    Theme.chat_timeBackgroundPaint.setAlpha(alpha);
                    if (this.drawVideoIcon) {
                        canvas.save();
                        canvas.translate(this.videoInfoLayot == null ? AndroidUtilities.dp(5.0f) : AndroidUtilities.dp(4.0f), (AndroidUtilities.dp(17.0f) - this.sharedResources.playDrawable.getIntrinsicHeight()) / 2.0f);
                        this.sharedResources.playDrawable.setAlpha((int) (this.imageAlpha * 255.0f * f2));
                        this.sharedResources.playDrawable.draw(canvas);
                        canvas.restore();
                    }
                    if (this.videoInfoLayot != null) {
                        canvas.translate(AndroidUtilities.dp((this.drawVideoIcon ? 10 : 0) + 4), (AndroidUtilities.dp(17.0f) - this.videoInfoLayot.getHeight()) / 2.0f);
                        int alpha2 = this.sharedResources.textPaint.getAlpha();
                        this.sharedResources.textPaint.setAlpha((int) (alpha2 * f2));
                        this.videoInfoLayot.draw(canvas);
                        this.sharedResources.textPaint.setAlpha(alpha2);
                    }
                    canvas.restore();
                }
                staticLayout = new StaticLayout(this.videoText, this.sharedResources.textPaint, (int) Math.ceil(this.sharedResources.textPaint.measureText(str)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.videoInfoLayot = staticLayout;
                boolean viewsOnLeft2 = viewsOnLeft(width);
                int dp2 = AndroidUtilities.dp(8.0f);
                StaticLayout staticLayout22 = this.videoInfoLayot;
                int width32 = dp2 + (staticLayout22 == null ? staticLayout22.getWidth() : 0) + (!this.drawVideoIcon ? AndroidUtilities.dp(10.0f) : 0);
                canvas.translate(AndroidUtilities.dp(5.0f), (((AndroidUtilities.dp(1.0f) + rectF.height()) - AndroidUtilities.dp(17.0f)) - AndroidUtilities.dp(4.0f)) - (!viewsOnLeft2 ? AndroidUtilities.dp(22.0f) : 0));
                RectF rectF22 = AndroidUtilities.rectTmp;
                rectF22.set(0.0f, 0.0f, width32, AndroidUtilities.dp(17.0f));
                int alpha3 = Theme.chat_timeBackgroundPaint.getAlpha();
                Theme.chat_timeBackgroundPaint.setAlpha((int) (alpha3 * f2));
                canvas.drawRoundRect(rectF22, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                Theme.chat_timeBackgroundPaint.setAlpha(alpha3);
                if (this.drawVideoIcon) {
                }
                if (this.videoInfoLayot != null) {
                }
                canvas.restore();
            }
        }
    }

    public void drawPrivacy(Canvas canvas, RectF rectF, float f) {
        Bitmap bitmap;
        if (!this.isStory || (bitmap = this.privacyBitmap) == null || bitmap.isRecycled()) {
            return;
        }
        int dp = AndroidUtilities.dp((rectF.width() / (rectF.width() + (AndroidUtilities.dp(20.0f) * this.checkBoxProgress))) * 17.33f);
        canvas.save();
        float f2 = dp;
        canvas.translate((rectF.right - f2) - AndroidUtilities.dp(5.66f), rectF.top + AndroidUtilities.dp(5.66f));
        if (this.privacyPaint == null) {
            this.privacyPaint = new Paint(3);
        }
        this.privacyPaint.setAlpha((int) (f * 255.0f));
        RectF rectF2 = AndroidUtilities.rectTmp;
        rectF2.set(0.0f, 0.0f, f2, f2);
        canvas.drawBitmap(this.privacyBitmap, (Rect) null, rectF2, this.privacyPaint);
        canvas.restore();
    }

    public void drawViews(Canvas canvas, RectF rectF, float f) {
        if (this.isStory) {
            ImageReceiver imageReceiver = this.imageReceiver;
            if ((imageReceiver == null || imageReceiver.getVisible()) && this.currentParentColumnsCount < 5) {
                float width = rectF.width() + (AndroidUtilities.dp(20.0f) * this.checkBoxProgress);
                float width2 = rectF.width() / width;
                boolean viewsOnLeft = viewsOnLeft(width);
                float f2 = this.viewsAlpha.set(this.drawViews);
                float f3 = f * f2;
                if (f3 < 1.0f) {
                    f3 = (float) Math.pow(f3, 8.0d);
                }
                if (f2 <= 0.0f) {
                    return;
                }
                canvas.save();
                canvas.translate(rectF.left, rectF.top);
                canvas.scale(width2, width2, viewsOnLeft ? 0.0f : rectF.width(), rectF.height());
                canvas.clipRect(0.0f, 0.0f, rectF.width(), rectF.height());
                float dp = AndroidUtilities.dp(26.0f) + this.viewsText.getCurrentWidth();
                canvas.translate(viewsOnLeft ? AndroidUtilities.dp(5.0f) : (rectF.width() - AndroidUtilities.dp(5.0f)) - dp, ((AndroidUtilities.dp(1.0f) + rectF.height()) - AndroidUtilities.dp(17.0f)) - AndroidUtilities.dp(4.0f));
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(0.0f, 0.0f, dp, AndroidUtilities.dp(17.0f));
                int alpha = Theme.chat_timeBackgroundPaint.getAlpha();
                Theme.chat_timeBackgroundPaint.setAlpha((int) (alpha * f3));
                canvas.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                Theme.chat_timeBackgroundPaint.setAlpha(alpha);
                canvas.save();
                canvas.translate(AndroidUtilities.dp(3.0f), (AndroidUtilities.dp(17.0f) - this.sharedResources.viewDrawable.getBounds().height()) / 2.0f);
                this.sharedResources.viewDrawable.setAlpha((int) (this.imageAlpha * 255.0f * f3));
                this.sharedResources.viewDrawable.draw(canvas);
                canvas.restore();
                canvas.translate(AndroidUtilities.dp(22.0f), 0.0f);
                this.viewsText.setBounds(0, 0, (int) dp, AndroidUtilities.dp(17.0f));
                this.viewsText.setAlpha((int) (f3 * 255.0f));
                this.viewsText.draw(canvas);
                canvas.restore();
            }
        }
    }

    public View getCrossfadeView() {
        return this.crossfadeView;
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

    public int getStyle() {
        return this.style;
    }

    @Override // android.view.ViewGroup, android.view.View
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
        SpoilerEffect2 spoilerEffect2 = this.mediaSpoilerEffect2;
        if (spoilerEffect2 != null) {
            if (spoilerEffect2.destroyed) {
                this.mediaSpoilerEffect2 = SpoilerEffect2.getInstance(this);
            } else {
                spoilerEffect2.attach(this);
            }
        }
    }

    /* renamed from: onCheckBoxPressed, reason: merged with bridge method [inline-methods] */
    public void lambda$setStyle$1() {
    }

    @Override // android.view.ViewGroup, android.view.View
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
        SpoilerEffect2 spoilerEffect2 = this.mediaSpoilerEffect2;
        if (spoilerEffect2 != null) {
            spoilerEffect2.detach(this);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x0405, code lost:
    
        if (r1.getProgress() != 0.0f) goto L143;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        boolean z;
        float dp;
        float f4;
        FlickerLoadingView flickerLoadingView;
        super.onDraw(canvas);
        float padding = getPadding();
        boolean z2 = this.isStory;
        float f5 = (z2 && this.isFirst) ? 0.0f : padding;
        float f6 = (z2 && this.isLast) ? 0.0f : padding;
        float measuredWidth = ((getMeasuredWidth() - f5) - f6) * this.imageScale;
        float f7 = padding * 2.0f;
        float measuredHeight = (getMeasuredHeight() - f7) * this.imageScale;
        if (this.crossfadeProgress > 0.5f && this.crossfadeToColumnsCount != 9.0f && this.currentParentColumnsCount != 9) {
            measuredWidth -= 2.0f;
            measuredHeight -= 2.0f;
        }
        float f8 = measuredWidth;
        float f9 = measuredHeight;
        if ((this.currentMessageObject != null || this.style == 1) && this.imageReceiver.hasBitmapImage() && this.imageReceiver.getCurrentAlpha() == 1.0f && this.imageAlpha == 1.0f) {
            f = f9;
            f2 = f8;
            f3 = 1.0f;
            z = true;
        } else {
            if (getParent() == null || (flickerLoadingView = this.globalGradientView) == null) {
                f = f9;
                f2 = f8;
                f3 = 1.0f;
                z = true;
            } else {
                flickerLoadingView.setParentSize(((View) getParent()).getMeasuredWidth(), getMeasuredHeight(), -getX());
                this.globalGradientView.updateColors();
                this.globalGradientView.updateGradient();
                float f10 = (this.crossfadeProgress <= 0.5f || this.crossfadeToColumnsCount == 9.0f || this.currentParentColumnsCount == 9) ? 0.0f : 1.0f;
                float f11 = f5 + f10;
                float f12 = padding + f10;
                f3 = 1.0f;
                z = true;
                f = f9;
                f2 = f8;
                canvas.drawRect(f11, f12, f11 + f8, f12 + f9, this.globalGradientView.getPaint());
            }
            invalidate();
        }
        float f13 = this.imageAlpha;
        if (f13 != f3) {
            canvas.saveLayerAlpha(0.0f, 0.0f, f5 + f6 + f2, f7 + f, (int) (f13 * 255.0f), 31);
        } else {
            canvas.save();
        }
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if (((checkBoxBase != null && checkBoxBase.isChecked()) || PhotoViewer.isShowingImage(this.currentMessageObject)) && !this.check2) {
            canvas.drawRect(f5, padding, (f5 + f2) - f6, f, this.sharedResources.backgroundPaint);
        }
        if (this.isStory && this.currentParentColumnsCount == z) {
            float height = getHeight() * 0.72f;
            Drawable drawable = this.gradientDrawable;
            if (drawable != null) {
                drawable.setBounds(0, 0, getWidth(), getHeight());
                this.gradientDrawable.draw(canvas);
            } else if (!this.gradientDrawableLoading && this.imageReceiver.getBitmap() != null) {
                this.gradientDrawableLoading = z;
                DominantColors.getColors(false, this.imageReceiver.getBitmap(), Theme.isCurrentThemeDark(), new Utilities.Callback() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        SharedPhotoVideoCell2.this.lambda$onDraw$2((int[]) obj);
                    }
                });
            }
            this.imageReceiver.setImageCoords((f2 - height) / 2.0f, 0.0f, height, getHeight());
        } else if (this.checkBoxProgress > 0.0f) {
            float dp2 = AndroidUtilities.dp(this.check2 ? 7.0f : 10.0f) * this.checkBoxProgress;
            float f14 = f5 + dp2;
            float f15 = padding + dp2;
            float f16 = dp2 * 2.0f;
            float f17 = f2 - f16;
            float f18 = f - f16;
            this.imageReceiver.setImageCoords(f14, f15, f17, f18);
            this.blurImageReceiver.setImageCoords(f14, f15, f17, f18);
        } else {
            float f19 = (this.crossfadeProgress <= 0.5f || this.crossfadeToColumnsCount == 9.0f || this.currentParentColumnsCount == 9) ? 0.0f : 1.0f;
            float f20 = f5 + f19;
            float f21 = f19 + padding;
            this.imageReceiver.setImageCoords(f20, f21, f2, f);
            this.blurImageReceiver.setImageCoords(f20, f21, f2, f);
        }
        if (this.check2) {
            this.imageReceiver.setRoundRadius(AndroidUtilities.lerp(0, AndroidUtilities.dp(8.0f), this.checkBoxProgress));
            canvas.save();
            if (this.reorder) {
                canvas.translate(this.imageReceiver.getCenterX(), this.imageReceiver.getCenterY());
                if (this.shaker == null) {
                    this.shaker = new Shaker(this);
                }
                this.shaker.concat(canvas, this.checkBoxProgress);
                canvas.translate(-this.imageReceiver.getCenterX(), -this.imageReceiver.getCenterY());
            }
        }
        if (!PhotoViewer.isShowingImage(this.currentMessageObject)) {
            this.imageReceiver.draw(canvas);
            MessageObject messageObject = this.currentMessageObject;
            if (messageObject != null && messageObject.hasMediaSpoilers() && !this.currentMessageObject.isMediaSpoilersRevealedInSharedMedia) {
                canvas.save();
                canvas.clipRect(f5, padding, (f5 + f2) - f6, padding + f);
                if (this.spoilerRevealProgress != 0.0f) {
                    this.path.rewind();
                    this.path.addCircle(this.spoilerRevealX, this.spoilerRevealY, this.spoilerMaxRadius * this.spoilerRevealProgress, Path.Direction.CW);
                    canvas.clipPath(this.path, Region.Op.DIFFERENCE);
                }
                this.blurImageReceiver.draw(canvas);
                if (this.mediaSpoilerEffect2 != null) {
                    canvas.clipRect(this.imageReceiver.getImageX(), this.imageReceiver.getImageY(), this.imageReceiver.getImageX2(), this.imageReceiver.getImageY2());
                    this.mediaSpoilerEffect2.draw(canvas, this, (int) this.imageReceiver.getImageWidth(), (int) this.imageReceiver.getImageHeight());
                } else {
                    if (this.mediaSpoilerEffect == null) {
                        this.mediaSpoilerEffect = new SpoilerEffect();
                    }
                    this.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
                    this.mediaSpoilerEffect.setBounds((int) this.imageReceiver.getImageX(), (int) this.imageReceiver.getImageY(), (int) this.imageReceiver.getImageX2(), (int) this.imageReceiver.getImageY2());
                    this.mediaSpoilerEffect.draw(canvas);
                }
                canvas.restore();
                invalidate();
            }
            if (!this.isSearchingHashtag) {
                float f22 = this.highlightProgress;
                if (f22 > 0.0f) {
                    this.sharedResources.highlightPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (f22 * 0.5f * 255.0f)));
                    canvas.drawRect(this.imageReceiver.getDrawRegion(), this.sharedResources.highlightPaint);
                }
            }
        }
        if (this.isStoryUploading) {
            this.scrimPaint.setColor(805306368);
            canvas.drawRect(this.imageReceiver.getDrawRegion(), this.scrimPaint);
            this.progressPaint.setStyle(Paint.Style.STROKE);
            this.progressPaint.setColor(-1);
            this.progressPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
            this.progressPaint.setStrokeJoin(Paint.Join.ROUND);
            this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
            float dp3 = AndroidUtilities.dp(18.0f);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(this.imageReceiver.getCenterX() - dp3, this.imageReceiver.getCenterY() - dp3, this.imageReceiver.getCenterX() + dp3, this.imageReceiver.getCenterY() + dp3);
            float currentTimeMillis = ((System.currentTimeMillis() % 1500) / 1500.0f) * 360.0f;
            AnimatedFloat animatedFloat = this.animatedProgress;
            MessageObject messageObject2 = this.currentMessageObject;
            canvas.drawArc(rectF, currentTimeMillis, animatedFloat.set(AndroidUtilities.lerp(0.15f, 0.95f, messageObject2 != null ? messageObject2.getProgress() : 0.0f)) * 360.0f, false, this.progressPaint);
            invalidate();
        }
        this.bounds.set(this.imageReceiver.getImageX(), this.imageReceiver.getImageY(), this.imageReceiver.getImageX2(), this.imageReceiver.getImageY2());
        drawDuration(canvas, this.bounds, f3);
        drawViews(canvas, this.bounds, f3);
        if (this.isSearchingHashtag) {
            drawAuthor(canvas, this.bounds, f3);
        } else {
            drawPrivacy(canvas, this.bounds, f3);
        }
        if (this.check2) {
            canvas.restore();
        }
        CheckBoxBase checkBoxBase2 = this.checkBoxBase;
        if (checkBoxBase2 != null) {
            if (this.style != z) {
            }
            canvas.save();
            if (this.check2 && this.reorder) {
                canvas.translate(this.imageReceiver.getCenterX(), this.imageReceiver.getCenterY());
                if (this.shaker == null) {
                    this.shaker = new Shaker(this);
                }
                this.shaker.concat(canvas, this.checkBoxProgress * 0.5f);
                canvas.translate(-this.imageReceiver.getCenterX(), -this.imageReceiver.getCenterY());
            }
            if (this.style == z) {
                dp = ((f2 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(25.0f)) - AndroidUtilities.dp(4.0f);
                f4 = AndroidUtilities.dp(4.0f);
            } else if (this.check2) {
                dp = (f2 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp((this.checkBoxProgress * 5.0f) + 22.0f);
                f4 = AndroidUtilities.dp(-2.0f) + (AndroidUtilities.dp(5.0f) * this.checkBoxProgress);
            } else {
                dp = (f2 + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(25.0f);
                f4 = 0.0f;
            }
            canvas.translate(dp, f4);
            this.checkBoxBase.draw(canvas);
            if (this.canvasButton != null) {
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(dp, f4, this.checkBoxBase.bounds.width() + dp, this.checkBoxBase.bounds.height() + f4);
                this.canvasButton.setRect(rectF2);
            }
            canvas.restore();
        }
        canvas.restore();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        boolean z = this.isStory;
        int i3 = z ? (int) (size * 1.25f) : size;
        if (z && this.currentParentColumnsCount == 1) {
            i3 /= 2;
        }
        setMeasuredDimension(size, i3);
        updateSpoilers2();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton = this.canvasButton;
        if (canvasButton == null || !canvasButton.checkTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public void setCheck2() {
        this.check2 = true;
    }

    public void setChecked(final boolean z, boolean z2) {
        int i;
        CheckBoxBase checkBoxBase = this.checkBoxBase;
        if ((checkBoxBase != null && checkBoxBase.isChecked()) == z) {
            return;
        }
        if (this.checkBoxBase == null) {
            CheckBoxBase checkBoxBase2 = new CheckBoxBase(this, 21, null);
            this.checkBoxBase = checkBoxBase2;
            checkBoxBase2.setColor(-1, Theme.key_sharedMedia_photoPlaceholder, Theme.key_checkboxCheck);
            if (this.check2 && (i = this.imageReceiverColor) != 0) {
                this.checkBoxBase.setBackgroundColor(Theme.blendOver(i, Theme.multAlpha(-1, 0.25f)));
            }
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
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.checkBoxProgress, z ? 1.0f : 0.0f);
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

    public void setCrossfadeView(SharedPhotoVideoCell2 sharedPhotoVideoCell2, float f, int i) {
        this.crossfadeView = sharedPhotoVideoCell2;
        this.crossfadeProgress = f;
        this.crossfadeToColumnsCount = i;
    }

    public void setGradientView(FlickerLoadingView flickerLoadingView) {
        this.globalGradientView = flickerLoadingView;
    }

    public void setHighlightProgress(float f) {
        if (this.highlightProgress != f) {
            this.highlightProgress = f;
            invalidate();
        }
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

    /* JADX WARN: Removed duplicated region for block: B:103:0x0386  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0372  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0380  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x03be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, int i) {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable;
        TL_stories.StoryItem storyItem;
        TL_stories.StoryItem storyItem2;
        int i2;
        ImageLocation imageLocation;
        long j;
        int i3;
        ImageLocation imageLocation2;
        String str;
        String str2;
        ImageReceiver imageReceiver;
        BitmapDrawable bitmapDrawable;
        BitmapDrawable bitmapDrawable2;
        ImageLocation imageLocation3;
        StringBuilder sb;
        ImageLocation forObject;
        String str3;
        int i4;
        ImageLocation imageLocation4;
        String str4;
        String str5;
        BitmapDrawable bitmapDrawable3;
        long j2;
        ImageReceiver imageReceiver2;
        BitmapDrawable bitmapDrawable4;
        ImageReceiver imageReceiver3;
        String str6;
        int i5;
        String str7;
        TL_stories.StoryViews storyViews;
        int i6 = this.currentParentColumnsCount;
        this.currentParentColumnsCount = i;
        MessageObject messageObject2 = this.currentMessageObject;
        if (messageObject2 == null && messageObject == null) {
            return;
        }
        if (messageObject2 != null && messageObject != null && messageObject2.getId() == messageObject.getId()) {
            MessageObject messageObject3 = this.currentMessageObject;
            if ((messageObject3 != null ? messageObject3.uploadingStory : null) == messageObject.uploadingStory) {
                if ((messageObject3 != null ? messageObject3.parentStoriesList : null) == messageObject.parentStoriesList && mediaEqual(getStoryMedia(messageObject3), getStoryMedia(messageObject)) && i6 == i) {
                    if ((this.privacyType == 100) == this.isStoryPinned) {
                        return;
                    }
                }
            }
        }
        this.currentMessageObject = messageObject;
        this.isStory = messageObject != null && messageObject.isStory();
        MessageObject messageObject4 = this.currentMessageObject;
        this.isStoryUploading = (messageObject4 == null || messageObject4.uploadingStory == null) ? false : true;
        updateSpoilers2();
        String str8 = "";
        if (messageObject == null) {
            this.imageReceiver.onDetachedFromWindow();
            this.blurImageReceiver.onDetachedFromWindow();
            this.videoText = null;
            this.drawViews = false;
            this.viewsAlpha.set(0.0f, true);
            this.viewsText.setText("", false);
            this.videoInfoLayot = null;
            this.showVideoLayout = false;
            this.gradientDrawableLoading = false;
            this.gradientDrawable = null;
            this.privacyType = -1;
            this.privacyBitmap = null;
            this.authorText = null;
            return;
        }
        if (this.attached) {
            this.imageReceiver.onAttachedToWindow();
            this.blurImageReceiver.onAttachedToWindow();
        }
        String restrictionReason = MessagesController.getInstance(this.currentAccount).getRestrictionReason(messageObject.messageOwner.restriction_reason);
        String filterString = this.sharedResources.getFilterString((int) ((AndroidUtilities.displaySize.x / i) / AndroidUtilities.density));
        int photoSize = i <= 2 ? AndroidUtilities.getPhotoSize() : 320;
        this.videoText = null;
        this.videoInfoLayot = null;
        this.showVideoLayout = false;
        this.imageReceiver.clearDecorators();
        if (!this.isStory || (storyViews = messageObject.storyItem.views) == null) {
            this.drawViews = false;
            this.viewsAlpha.set(0.0f, true);
            animatedTextDrawable = this.viewsText;
        } else {
            int i7 = storyViews.views_count;
            this.drawViews = i7 > 0;
            animatedTextDrawable = this.viewsText;
            str8 = AndroidUtilities.formatWholeNumber(i7, 0);
        }
        animatedTextDrawable.setText(str8, false);
        this.viewsAlpha.set(this.drawViews ? 1.0f : 0.0f, true);
        Object obj = messageObject.parentStoriesList != null ? messageObject.storyItem : messageObject;
        if (TextUtils.isEmpty(restrictionReason)) {
            TL_stories.StoryItem storyItem3 = messageObject.storyItem;
            if (storyItem3 == null || !(storyItem3.media instanceof TLRPC.TL_messageMediaUnsupported)) {
                StoriesController.UploadingStory uploadingStory = messageObject.uploadingStory;
                if (uploadingStory != null && (str7 = uploadingStory.firstFramePath) != null) {
                    imageReceiver3 = this.imageReceiver;
                    imageLocation = ImageLocation.getForPath(str7);
                    str6 = null;
                    i5 = 0;
                    bitmapDrawable4 = null;
                } else if (messageObject.isVideo()) {
                    this.showVideoLayout = true;
                    if (i != 9) {
                        this.videoText = AndroidUtilities.formatShortDuration((int) messageObject.getDuration());
                    }
                    imageLocation = messageObject.mediaThumb;
                    if (imageLocation != null) {
                        bitmapDrawable2 = messageObject.strippedThumb;
                        if (bitmapDrawable2 == null) {
                            imageReceiver = this.imageReceiver;
                            imageLocation3 = messageObject.mediaSmallThumb;
                            sb = new StringBuilder();
                            sb.append(filterString);
                            sb.append("_b");
                            str = sb.toString();
                            str2 = null;
                            i3 = 0;
                            bitmapDrawable = null;
                            j = 0;
                            imageLocation2 = imageLocation3;
                            imageReceiver.setImage(imageLocation, filterString, imageLocation2, str, bitmapDrawable, j, str2, obj, i3);
                        }
                        bitmapDrawable4 = bitmapDrawable2;
                        imageReceiver3 = this.imageReceiver;
                        str6 = null;
                        i5 = 0;
                    } else {
                        TLRPC.Document document = messageObject.getDocument();
                        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 50);
                        TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, photoSize, false, null, this.isStory);
                        if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2 && !this.isStory) {
                            closestPhotoSizeWithSize2 = null;
                        }
                        if (closestPhotoSizeWithSize != null) {
                            if (messageObject.strippedThumb != null) {
                                imageReceiver3 = this.imageReceiver;
                                imageLocation = ImageLocation.getForDocument(closestPhotoSizeWithSize2, document);
                                str6 = null;
                                i5 = 0;
                                bitmapDrawable4 = messageObject.strippedThumb;
                            } else {
                                imageReceiver = this.imageReceiver;
                                imageLocation = ImageLocation.getForDocument(closestPhotoSizeWithSize2, document);
                                imageLocation2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
                                str = filterString + "_b";
                                str2 = null;
                                i3 = 0;
                                bitmapDrawable = null;
                                j = 0;
                                imageReceiver.setImage(imageLocation, filterString, imageLocation2, str, bitmapDrawable, j, str2, obj, i3);
                            }
                        }
                    }
                } else if ((MessageObject.getMedia(messageObject.messageOwner) instanceof TLRPC.TL_messageMediaPhoto) && MessageObject.getMedia(messageObject.messageOwner).photo != null && !messageObject.photoThumbs.isEmpty()) {
                    if (messageObject.mediaExists || canAutoDownload(messageObject) || this.isStory) {
                        imageLocation = messageObject.mediaThumb;
                        if (imageLocation != null) {
                            bitmapDrawable2 = messageObject.strippedThumb;
                            if (bitmapDrawable2 == null) {
                                imageReceiver = this.imageReceiver;
                                imageLocation3 = messageObject.mediaSmallThumb;
                                sb = new StringBuilder();
                                sb.append(filterString);
                                sb.append("_b");
                                str = sb.toString();
                                str2 = null;
                                i3 = 0;
                                bitmapDrawable = null;
                                j = 0;
                                imageLocation2 = imageLocation3;
                                imageReceiver.setImage(imageLocation, filterString, imageLocation2, str, bitmapDrawable, j, str2, obj, i3);
                            }
                            bitmapDrawable4 = bitmapDrawable2;
                            imageReceiver3 = this.imageReceiver;
                            str6 = null;
                            i5 = 0;
                        } else {
                            TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
                            TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, photoSize, false, closestPhotoSizeWithSize3, this.isStory);
                            if (closestPhotoSizeWithSize4 == closestPhotoSizeWithSize3) {
                                closestPhotoSizeWithSize3 = null;
                            }
                            if (messageObject.strippedThumb != null) {
                                ImageReceiver imageReceiver4 = this.imageReceiver;
                                ImageLocation forObject2 = ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject);
                                BitmapDrawable bitmapDrawable5 = messageObject.strippedThumb;
                                j = closestPhotoSizeWithSize4 != null ? closestPhotoSizeWithSize4.size : 0L;
                                i3 = messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1;
                                imageLocation2 = null;
                                str = null;
                                str2 = null;
                                imageReceiver = imageReceiver4;
                                imageLocation = forObject2;
                                bitmapDrawable = bitmapDrawable5;
                                imageReceiver.setImage(imageLocation, filterString, imageLocation2, str, bitmapDrawable, j, str2, obj, i3);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject.photoThumbsObject), filterString, ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject), filterString + "_b", closestPhotoSizeWithSize4 != null ? closestPhotoSizeWithSize4.size : 0L, null, obj, messageObject.shouldEncryptPhotoOrVideo() ? 2 : 1);
                            }
                        }
                    } else {
                        BitmapDrawable bitmapDrawable6 = messageObject.strippedThumb;
                        if (bitmapDrawable6 != null) {
                            str3 = null;
                            i4 = 0;
                            imageLocation4 = null;
                            str4 = null;
                            forObject = null;
                            str5 = null;
                            j2 = 0;
                            imageReceiver2 = this.imageReceiver;
                            bitmapDrawable3 = bitmapDrawable6;
                        } else {
                            TLRPC.PhotoSize closestPhotoSizeWithSize5 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 50);
                            ImageReceiver imageReceiver5 = this.imageReceiver;
                            forObject = ImageLocation.getForObject(closestPhotoSizeWithSize5, messageObject.photoThumbsObject);
                            str3 = null;
                            i4 = 0;
                            imageLocation4 = null;
                            str4 = null;
                            str5 = "b";
                            bitmapDrawable3 = null;
                            j2 = 0;
                            imageReceiver2 = imageReceiver5;
                        }
                        imageReceiver2.setImage(imageLocation4, str4, forObject, str5, bitmapDrawable3, j2, str3, obj, i4);
                    }
                }
                imageReceiver3.setImage(imageLocation, filterString, bitmapDrawable4, str6, obj, i5);
            } else {
                storyItem3.dialogId = messageObject.getDialogId();
                Drawable mutate = getContext().getResources().getDrawable(R.drawable.msg_emoji_recent).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(1090519039, PorterDuff.Mode.SRC_IN));
                this.imageReceiver.setImageBitmap(new CombinedDrawable(new ColorDrawable(-13421773), mutate));
            }
            if (this.blurImageReceiver.getBitmap() != null) {
                this.blurImageReceiver.getBitmap().recycle();
                this.blurImageReceiver.setImageBitmap((Bitmap) null);
            }
            if (this.imageReceiver.getBitmap() != null && this.currentMessageObject.hasMediaSpoilers() && !this.currentMessageObject.isMediaSpoilersRevealed) {
                this.blurImageReceiver.setImageBitmap(Utilities.stackBlurBitmapMax(this.imageReceiver.getBitmap()));
            }
            storyItem = messageObject.storyItem;
            if (storyItem != null) {
                this.imageReceiver.addDecorator(new StoryWidgetsImageDecorator(storyItem));
            }
            if (this.isStoryPinned) {
                if (this.isStory && (storyItem2 = messageObject.storyItem) != null) {
                    if (storyItem2.parsedPrivacy == null) {
                        storyItem2.parsedPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(this.currentAccount, storyItem2.privacy);
                    }
                    int i8 = messageObject.storyItem.parsedPrivacy.type;
                    if (i8 == 2) {
                        i2 = R.drawable.msg_folders_private;
                    } else if (i8 == 1) {
                        i2 = R.drawable.msg_stories_closefriends;
                    } else if (i8 == 3) {
                        i2 = R.drawable.msg_folders_groups;
                    }
                    setPrivacyType(i8, i2);
                }
                setPrivacyType(-1, 0);
            } else {
                setPrivacyType(100, R.drawable.msg_pin_mini);
            }
            if (this.isSearchingHashtag) {
                long dialogId = messageObject.getDialogId();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("x ");
                spannableStringBuilder.append((CharSequence) MessagesController.getInstance(this.currentAccount).getPeerName(dialogId));
                AvatarSpan avatarSpan = new AvatarSpan(this, this.currentAccount, i == 2 ? 16.0f : 13.66f);
                avatarSpan.setDialogId(dialogId);
                spannableStringBuilder.setSpan(avatarSpan, 0, 1, 33);
                this.authorText = new Text(spannableStringBuilder, i == 2 ? 14.0f : 10.1666f, AndroidUtilities.bold());
            }
            invalidate();
        }
        this.imageReceiver.setImageBitmap(ContextCompat.getDrawable(getContext(), R.drawable.photo_placeholder_in));
        if (this.blurImageReceiver.getBitmap() != null) {
        }
        if (this.imageReceiver.getBitmap() != null) {
            this.blurImageReceiver.setImageBitmap(Utilities.stackBlurBitmapMax(this.imageReceiver.getBitmap()));
        }
        storyItem = messageObject.storyItem;
        if (storyItem != null) {
        }
        if (this.isStoryPinned) {
        }
        if (this.isSearchingHashtag) {
        }
        invalidate();
    }

    public void setReorder(boolean z) {
        this.reorder = z;
        invalidate();
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
            canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    SharedPhotoVideoCell2.this.lambda$setStyle$1();
                }
            });
        }
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

    public void startRevealMedia(float f, float f2) {
        this.spoilerRevealX = f;
        this.spoilerRevealY = f2;
        this.spoilerMaxRadius = (float) Math.sqrt(Math.pow(getWidth(), 2.0d) + Math.pow(getHeight(), 2.0d));
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration((long) MathUtils.clamp(this.spoilerMaxRadius * 0.3f, 250.0f, 550.0f));
        duration.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.SharedPhotoVideoCell2$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SharedPhotoVideoCell2.this.lambda$startRevealMedia$3(valueAnimator);
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

    public void updateViews() {
        MessageObject messageObject;
        TL_stories.StoryItem storyItem;
        TL_stories.StoryViews storyViews;
        if (!this.isStory || (messageObject = this.currentMessageObject) == null || (storyItem = messageObject.storyItem) == null || (storyViews = storyItem.views) == null) {
            this.drawViews = false;
            this.viewsText.setText("", false);
        } else {
            int i = storyViews.views_count;
            this.drawViews = i > 0;
            this.viewsText.setText(AndroidUtilities.formatWholeNumber(i, 0), true);
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return this.viewsText == drawable || super.verifyDrawable(drawable);
    }

    public boolean viewsOnLeft(float f) {
        int i;
        if (!this.isStory || this.currentParentColumnsCount >= 5) {
            return false;
        }
        int dp = AndroidUtilities.dp(26.0f) + ((int) this.viewsText.getCurrentWidth());
        if (this.showVideoLayout) {
            int dp2 = AndroidUtilities.dp(8.0f);
            StaticLayout staticLayout = this.videoInfoLayot;
            i = dp2 + (staticLayout != null ? staticLayout.getWidth() : 0) + (this.drawVideoIcon ? AndroidUtilities.dp(10.0f) : 0);
        } else {
            i = 0;
        }
        return ((float) ((dp + ((dp <= 0 || i <= 0) ? 0 : AndroidUtilities.dp(8.0f))) + i)) > f;
    }
}
