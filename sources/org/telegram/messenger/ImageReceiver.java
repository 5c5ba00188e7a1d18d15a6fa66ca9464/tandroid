package org.telegram.messenger;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AttachableDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ClipRoundedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LoadingStickerDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclableDrawable;
import org.telegram.ui.Components.VectorAvatarThumbDrawable;

/* loaded from: classes3.dex */
public class ImageReceiver implements NotificationCenter.NotificationCenterDelegate {
    public static final int DEFAULT_CROSSFADE_DURATION = 150;
    private static final int TYPE_CROSSFDADE = 2;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_MEDIA = 3;
    public static final int TYPE_THUMB = 1;
    private static final float[] radii;
    private static PorterDuffColorFilter selectedColorFilter;
    private static PorterDuffColorFilter selectedGroupColorFilter;
    private boolean allowCrossfadeWithImage;
    private boolean allowDecodeSingleFrame;
    private boolean allowDrawWhileCacheGenerating;
    private boolean allowLoadingOnAttachedOnly;
    private boolean allowLottieVibration;
    private boolean allowStartAnimation;
    private boolean allowStartLottieAnimation;
    private int animateFromIsPressed;
    public int animatedFileDrawableRepeatMaxCount;
    private boolean animationReadySent;
    private boolean attachedToWindow;
    private int autoRepeat;
    private int autoRepeatCount;
    private long autoRepeatTimeout;
    private Object blendMode;
    private boolean canceledLoading;
    private boolean centerRotation;
    public boolean clip;
    private ColorFilter colorFilter;
    private ComposeShader composeShader;
    private byte crossfadeAlpha;
    private float crossfadeByScale;
    private int crossfadeDuration;
    private Drawable crossfadeImage;
    private String crossfadeKey;
    private BitmapShader crossfadeShader;
    private boolean crossfadeWithOldImage;
    private boolean crossfadeWithThumb;
    private boolean crossfadingWithThumb;
    private int currentAccount;
    private float currentAlpha;
    private int currentCacheType;
    private String currentExt;
    private int currentGuid;
    private Drawable currentImageDrawable;
    private String currentImageFilter;
    private String currentImageKey;
    private ImageLocation currentImageLocation;
    private boolean currentKeyQuality;
    private int currentLayerNum;
    private Drawable currentMediaDrawable;
    private String currentMediaFilter;
    private String currentMediaKey;
    private ImageLocation currentMediaLocation;
    private int currentOpenedLayerFlags;
    private Object currentParentObject;
    private long currentSize;
    private Drawable currentThumbDrawable;
    private String currentThumbFilter;
    private String currentThumbKey;
    private ImageLocation currentThumbLocation;
    private long currentTime;
    private ArrayList<Decorator> decorators;
    private ImageReceiverDelegate delegate;
    private final RectF drawRegion;
    private boolean emojiPaused;
    private int[] emptyRoundRadius;
    private long endTime;
    private int fileLoadingPriority;
    private boolean forceCrossfade;
    private boolean forceLoding;
    private boolean forcePreview;
    private Bitmap gradientBitmap;
    private BitmapShader gradientShader;
    private boolean ignoreImageSet;
    public boolean ignoreNotifications;
    private float imageH;
    protected int imageInvert;
    protected int imageOrientation;
    private BitmapShader imageShader;
    private int imageTag;
    private float imageW;
    private float imageX;
    private float imageY;
    private boolean invalidateAll;
    private boolean isAspectFit;
    private int isLastFrame;
    private int isPressed;
    private boolean isRoundRect;
    private boolean isRoundVideo;
    private boolean isVisible;
    private long lastUpdateAlphaTime;
    private Bitmap legacyBitmap;
    private Canvas legacyCanvas;
    private Paint legacyPaint;
    private BitmapShader legacyShader;
    private ArrayList<Runnable> loadingOperations;
    private boolean manualAlphaAnimator;
    private Object mark;
    private BitmapShader mediaShader;
    private int mediaTag;
    private boolean needsQualityThumb;
    private float overrideAlpha;
    private int param;
    private Runnable parentRunnable;
    private View parentView;
    List<ImageReceiver> preloadReceivers;
    private float pressedProgress;
    private float previousAlpha;
    private TLRPC.Document qulityThumbDocument;
    private Paint roundPaint;
    private final Path roundPath;
    private final int[] roundRadius;
    private final RectF roundRect;
    private SetImageBackup setImageBackup;
    private final Matrix shaderMatrix;
    private boolean shouldGenerateQualityThumb;
    private float sideClip;
    private boolean skipUpdateFrame;
    private long startTime;
    private Drawable staticThumbDrawable;
    public BitmapShader staticThumbShader;
    private ImageLocation strippedLocation;
    private int thumbInvert;
    private int thumbOrientation;
    public BitmapShader thumbShader;
    private int thumbTag;
    private String uniqKeyPrefix;
    private boolean useRoundForThumb;
    private boolean useRoundRadius;
    public boolean useSharedAnimationQueue;
    private boolean videoThumbIsSame;

    public static class BackgroundThreadDrawHolder {
        private AnimatedFileDrawable animation;
        public boolean animationNotReady;
        public ColorFilter colorFilter;
        private Drawable crossfadeImage;
        private BitmapShader crossfadeShader;
        private boolean crossfadeWithOldImage;
        private boolean crossfadingWithThumb;
        private float currentAlpha;
        private Drawable imageDrawable;
        public float imageH;
        private BitmapShader imageShader;
        public float imageW;
        public float imageX;
        public float imageY;
        private RLottieDrawable lottieDrawable;
        private Drawable mediaDrawable;
        private BitmapShader mediaShader;
        public float overrideAlpha;
        Paint paint;
        private float previousAlpha;
        private Path roundPath;
        private Drawable staticThumbDrawable;
        public BitmapShader staticThumbShader;
        public int threadIndex;
        private Drawable thumbDrawable;
        private BitmapShader thumbShader;
        public long time;
        private int[] roundRadius = new int[4];
        public RectF drawRegion = new RectF();

        public void getBounds(Rect rect) {
            if (rect != null) {
                int i = (int) this.imageX;
                rect.left = i;
                int i2 = (int) this.imageY;
                rect.top = i2;
                rect.right = (int) (i + this.imageW);
                rect.bottom = (int) (i2 + this.imageH);
            }
        }

        public void getBounds(RectF rectF) {
            if (rectF != null) {
                float f = this.imageX;
                rectF.left = f;
                float f2 = this.imageY;
                rectF.top = f2;
                rectF.right = f + this.imageW;
                rectF.bottom = f2 + this.imageH;
            }
        }

        public void release() {
            this.animation = null;
            this.lottieDrawable = null;
            for (int i = 0; i < 4; i++) {
                int[] iArr = this.roundRadius;
                iArr[i] = iArr[i];
            }
            this.mediaDrawable = null;
            this.mediaShader = null;
            this.imageDrawable = null;
            this.imageShader = null;
            this.thumbDrawable = null;
            this.thumbShader = null;
            this.staticThumbShader = null;
            this.staticThumbDrawable = null;
            this.crossfadeImage = null;
            this.colorFilter = null;
        }

        public void setBounds(Rect rect) {
            if (rect != null) {
                this.imageX = rect.left;
                this.imageY = rect.top;
                this.imageW = rect.width();
                this.imageH = rect.height();
            }
        }
    }

    public static class BitmapHolder {
        public Bitmap bitmap;
        public Drawable drawable;
        private String key;
        public int orientation;
        private boolean recycleOnRelease;

        public BitmapHolder(Bitmap bitmap) {
            this.bitmap = bitmap;
            this.recycleOnRelease = true;
        }

        public BitmapHolder(Bitmap bitmap, String str, int i) {
            this.bitmap = bitmap;
            this.key = str;
            this.orientation = i;
            if (str != null) {
                ImageLoader.getInstance().incrementUseCount(this.key);
            }
        }

        public BitmapHolder(Drawable drawable, String str, int i) {
            this.drawable = drawable;
            this.key = str;
            this.orientation = i;
            if (str != null) {
                ImageLoader.getInstance().incrementUseCount(this.key);
            }
        }

        public int getHeight() {
            Bitmap bitmap = this.bitmap;
            if (bitmap != null) {
                return bitmap.getHeight();
            }
            return 0;
        }

        public String getKey() {
            return this.key;
        }

        public int getWidth() {
            Bitmap bitmap = this.bitmap;
            if (bitmap != null) {
                return bitmap.getWidth();
            }
            return 0;
        }

        public boolean isRecycled() {
            Bitmap bitmap = this.bitmap;
            return bitmap == null || bitmap.isRecycled();
        }

        public void release() {
            Bitmap bitmap;
            if (this.key != null) {
                boolean decrementUseCount = ImageLoader.getInstance().decrementUseCount(this.key);
                if (!ImageLoader.getInstance().isInMemCache(this.key, false) && decrementUseCount) {
                    Bitmap bitmap2 = this.bitmap;
                    if (bitmap2 == null) {
                        Drawable drawable = this.drawable;
                        if (drawable != null) {
                            if (drawable instanceof RLottieDrawable) {
                                ((RLottieDrawable) drawable).recycle(false);
                            } else if (drawable instanceof AnimatedFileDrawable) {
                                ((AnimatedFileDrawable) drawable).recycle();
                            } else if (drawable instanceof BitmapDrawable) {
                                bitmap2 = ((BitmapDrawable) drawable).getBitmap();
                            }
                        }
                    }
                    bitmap2.recycle();
                }
                this.key = null;
            } else if (this.recycleOnRelease && (bitmap = this.bitmap) != null) {
                bitmap.recycle();
            }
            this.bitmap = null;
            this.drawable = null;
        }
    }

    public static abstract class Decorator {
        public void onAttachedToWindow(ImageReceiver imageReceiver) {
        }

        public void onDetachedFromWidnow() {
        }

        protected abstract void onDraw(Canvas canvas, ImageReceiver imageReceiver);
    }

    public interface ImageReceiverDelegate {

        public abstract /* synthetic */ class -CC {
            public static void $default$didSetImageBitmap(ImageReceiverDelegate imageReceiverDelegate, int i, String str, Drawable drawable) {
            }

            public static void $default$onAnimationReady(ImageReceiverDelegate imageReceiverDelegate, ImageReceiver imageReceiver) {
            }
        }

        void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3);

        void didSetImageBitmap(int i, String str, Drawable drawable);

        void onAnimationReady(ImageReceiver imageReceiver);
    }

    public static class ReactionLastFrame extends BitmapDrawable {
        public static final float LAST_FRAME_SCALE = 1.2f;

        public ReactionLastFrame(Bitmap bitmap) {
            super(bitmap);
        }
    }

    private static class SetImageBackup {
        public int cacheType;
        public String ext;
        public String imageFilter;
        public ImageLocation imageLocation;
        public String mediaFilter;
        public ImageLocation mediaLocation;
        public Object parentObject;
        public long size;
        public Drawable thumb;
        public String thumbFilter;
        public ImageLocation thumbLocation;

        private SetImageBackup() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.imageLocation = null;
            this.thumbLocation = null;
            this.mediaLocation = null;
            this.thumb = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSet() {
            return (this.imageLocation == null && this.thumbLocation == null && this.mediaLocation == null && this.thumb == null) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isWebfileSet() {
            ImageLocation imageLocation;
            ImageLocation imageLocation2;
            ImageLocation imageLocation3 = this.imageLocation;
            return ((imageLocation3 == null || (imageLocation3.webFile == null && imageLocation3.path == null)) && ((imageLocation = this.thumbLocation) == null || (imageLocation.webFile == null && imageLocation.path == null)) && ((imageLocation2 = this.mediaLocation) == null || (imageLocation2.webFile == null && imageLocation2.path == null))) ? false : true;
        }
    }

    static {
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        selectedColorFilter = new PorterDuffColorFilter(-2236963, mode);
        selectedGroupColorFilter = new PorterDuffColorFilter(-4473925, mode);
        radii = new float[8];
    }

    public ImageReceiver() {
        this(null);
    }

    public ImageReceiver(View view) {
        this.allowCrossfadeWithImage = true;
        this.fileLoadingPriority = 1;
        this.useRoundForThumb = true;
        this.allowLottieVibration = true;
        this.allowStartAnimation = true;
        this.allowStartLottieAnimation = true;
        this.autoRepeat = 1;
        this.autoRepeatCount = -1;
        this.drawRegion = new RectF();
        this.isVisible = true;
        this.useRoundRadius = true;
        this.roundRadius = new int[4];
        this.isRoundRect = true;
        this.roundRect = new RectF();
        this.shaderMatrix = new Matrix();
        this.roundPath = new Path();
        this.overrideAlpha = 1.0f;
        this.previousAlpha = 1.0f;
        this.crossfadeAlpha = (byte) 1;
        this.crossfadeByScale = 0.05f;
        this.crossfadeDuration = 150;
        this.loadingOperations = new ArrayList<>();
        this.allowLoadingOnAttachedOnly = false;
        this.clip = true;
        this.parentView = view;
        this.roundPaint = new Paint(3);
        this.currentAccount = UserConfig.selectedAccount;
    }

    private void checkAlphaAnimation(boolean z, BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
        if (this.manualAlphaAnimator) {
            return;
        }
        float f = this.currentAlpha;
        if (f != 1.0f) {
            if (!z) {
                if (backgroundThreadDrawHolder != null) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long j = this.lastUpdateAlphaTime;
                    long j2 = currentTimeMillis - j;
                    if (j == 0) {
                        j2 = 16;
                    }
                    if (j2 > 30 && AndroidUtilities.screenRefreshRate > 60.0f) {
                        j2 = 30;
                    }
                    this.currentAlpha += j2 / this.crossfadeDuration;
                } else {
                    this.currentAlpha = f + (16.0f / this.crossfadeDuration);
                }
                if (this.currentAlpha > 1.0f) {
                    this.currentAlpha = 1.0f;
                    this.previousAlpha = 1.0f;
                    if (this.crossfadeImage != null) {
                        recycleBitmap(null, 2);
                        this.crossfadeShader = null;
                    }
                }
            }
            if (backgroundThreadDrawHolder != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageReceiver$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageReceiver.this.invalidate();
                    }
                });
            } else {
                invalidate();
            }
        }
    }

    private void drawBitmapDrawable(Canvas canvas, BitmapDrawable bitmapDrawable, BackgroundThreadDrawHolder backgroundThreadDrawHolder, int i) {
        if (backgroundThreadDrawHolder == null) {
            bitmapDrawable.setAlpha(i);
            if (bitmapDrawable instanceof RLottieDrawable) {
                ((RLottieDrawable) bitmapDrawable).drawInternal(canvas, null, false, this.currentTime, 0);
                return;
            } else if (bitmapDrawable instanceof AnimatedFileDrawable) {
                ((AnimatedFileDrawable) bitmapDrawable).drawInternal(canvas, false, this.currentTime, 0);
                return;
            } else {
                bitmapDrawable.draw(canvas);
                return;
            }
        }
        if (bitmapDrawable instanceof RLottieDrawable) {
            ((RLottieDrawable) bitmapDrawable).drawInBackground(canvas, backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY, backgroundThreadDrawHolder.imageW, backgroundThreadDrawHolder.imageH, i, backgroundThreadDrawHolder.colorFilter, backgroundThreadDrawHolder.threadIndex);
            return;
        }
        if (bitmapDrawable instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) bitmapDrawable).drawInBackground(canvas, backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY, backgroundThreadDrawHolder.imageW, backgroundThreadDrawHolder.imageH, i, backgroundThreadDrawHolder.colorFilter, backgroundThreadDrawHolder.threadIndex);
            return;
        }
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null) {
            if (backgroundThreadDrawHolder.paint == null) {
                backgroundThreadDrawHolder.paint = new Paint(1);
            }
            backgroundThreadDrawHolder.paint.setAlpha(i);
            backgroundThreadDrawHolder.paint.setColorFilter(backgroundThreadDrawHolder.colorFilter);
            canvas.save();
            canvas.translate(backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY);
            canvas.scale(backgroundThreadDrawHolder.imageW / bitmap.getWidth(), backgroundThreadDrawHolder.imageH / bitmap.getHeight());
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, backgroundThreadDrawHolder.paint);
            canvas.restore();
        }
    }

    private void drawDrawable(Canvas canvas, Drawable drawable, int i, BitmapShader bitmapShader, int i2, int i3, BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
        ImageReceiver imageReceiver;
        Canvas canvas2;
        Drawable drawable2;
        int i4;
        BitmapShader bitmapShader2;
        int i5;
        int i6;
        BackgroundThreadDrawHolder backgroundThreadDrawHolder2;
        if (this.isPressed == 0) {
            float f = this.pressedProgress;
            if (f != 0.0f) {
                float f2 = f - 0.10666667f;
                this.pressedProgress = f2;
                if (f2 < 0.0f) {
                    this.pressedProgress = 0.0f;
                }
                invalidate();
            }
        }
        int i7 = this.isPressed;
        if (i7 != 0) {
            this.pressedProgress = 1.0f;
            this.animateFromIsPressed = i7;
        }
        float f3 = this.pressedProgress;
        if (f3 == 0.0f || f3 == 1.0f) {
            imageReceiver = this;
            canvas2 = canvas;
            drawable2 = drawable;
            i4 = i;
            bitmapShader2 = bitmapShader;
            i5 = i2;
            i6 = i3;
            backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
        } else {
            drawable2 = drawable;
            bitmapShader2 = bitmapShader;
            i5 = i2;
            i6 = i3;
            backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
            drawDrawable(canvas, drawable2, i, bitmapShader2, i5, i6, i7, backgroundThreadDrawHolder2);
            i4 = (int) (i * this.pressedProgress);
            i7 = this.animateFromIsPressed;
            imageReceiver = this;
            canvas2 = canvas;
        }
        imageReceiver.drawDrawable(canvas2, drawable2, i4, bitmapShader2, i5, i6, i7, backgroundThreadDrawHolder2);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x00f4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00f5 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static File getAvatarLocalFile(int i, TLObject tLObject) {
        StringBuilder sb;
        StringBuilder sb2;
        File file;
        try {
            ImageLocation forUserOrChat = ImageLocation.getForUserOrChat(tLObject, 1);
            File localFile = FileLoader.getInstance(i).getLocalFile(forUserOrChat);
            if (localFile != null) {
                return localFile;
            }
            String key = forUserOrChat.getKey(tLObject, forUserOrChat, true);
            if (forUserOrChat.path != null) {
                sb = new StringBuilder();
                sb.append(key);
                sb.append(".");
                sb.append(ImageLoader.getHttpUrlExtension(forUserOrChat.path, "jpg"));
            } else {
                TLRPC.PhotoSize photoSize = forUserOrChat.photoSize;
                if (!(photoSize instanceof TLRPC.TL_photoStrippedSize) && !(photoSize instanceof TLRPC.TL_photoPathSize)) {
                    if (forUserOrChat.location != null) {
                        sb = new StringBuilder();
                        sb.append(key);
                        sb.append(".");
                    } else {
                        WebFile webFile = forUserOrChat.webFile;
                        if (webFile != null) {
                            String mimeTypePart = FileLoader.getMimeTypePart(webFile.mime_type);
                            sb2 = new StringBuilder();
                            sb2.append(key);
                            sb2.append(".");
                            sb2.append(ImageLoader.getHttpUrlExtension(forUserOrChat.webFile.url, mimeTypePart));
                        } else {
                            if (forUserOrChat.secureDocument == null) {
                                TLRPC.Document document = forUserOrChat.document;
                                if (document != null) {
                                    String documentFileName = FileLoader.getDocumentFileName(document);
                                    int lastIndexOf = documentFileName.lastIndexOf(46);
                                    String str = "";
                                    String substring = lastIndexOf == -1 ? "" : documentFileName.substring(lastIndexOf);
                                    if (substring.length() <= 1) {
                                        if ("video/mp4".equals(forUserOrChat.document.mime_type)) {
                                            str = ".mp4";
                                        } else if ("video/x-matroska".equals(forUserOrChat.document.mime_type)) {
                                            str = ".mkv";
                                        }
                                        substring = str;
                                    }
                                    sb2 = new StringBuilder();
                                    sb2.append(key);
                                    sb2.append(substring);
                                }
                                file = new File(FileLoader.getDirectory(4), key);
                                if (file.exists()) {
                                    return null;
                                }
                                return file;
                            }
                            sb = new StringBuilder();
                            sb.append(key);
                            sb.append(".");
                        }
                        sb = sb2;
                    }
                    sb.append("jpg");
                }
                sb = new StringBuilder();
                sb.append(key);
                sb.append(".");
                sb.append("jpg");
            }
            key = sb.toString();
            file = new File(FileLoader.getDirectory(4), key);
            if (file.exists()) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private boolean hasRoundRadius() {
        return true;
    }

    private void loadImage() {
        ImageLoader.getInstance().loadImageForImageReceiver(this, this.preloadReceivers);
        invalidate();
    }

    private void onBitmapException(Drawable drawable) {
        if (drawable == this.currentMediaDrawable && this.currentMediaKey != null) {
            ImageLoader.getInstance().removeImage(this.currentMediaKey);
            this.currentMediaKey = null;
        } else if (drawable == this.currentImageDrawable && this.currentImageKey != null) {
            ImageLoader.getInstance().removeImage(this.currentImageKey);
            this.currentImageKey = null;
        } else if (drawable == this.currentThumbDrawable && this.currentThumbKey != null) {
            ImageLoader.getInstance().removeImage(this.currentThumbKey);
            this.currentThumbKey = null;
        }
        setImage(this.currentMediaLocation, this.currentMediaFilter, this.currentImageLocation, this.currentImageFilter, this.currentThumbLocation, this.currentThumbFilter, this.currentThumbDrawable, this.currentSize, this.currentExt, this.currentParentObject, this.currentCacheType);
    }

    private void setDrawableShader(Drawable drawable, BitmapShader bitmapShader) {
        if (drawable == this.currentThumbDrawable) {
            this.thumbShader = bitmapShader;
            return;
        }
        if (drawable == this.staticThumbDrawable) {
            this.staticThumbShader = bitmapShader;
            return;
        }
        if (drawable == this.currentMediaDrawable) {
            this.mediaShader = bitmapShader;
            return;
        }
        if (drawable == this.currentImageDrawable) {
            this.imageShader = bitmapShader;
            if (this.gradientShader == null || !(drawable instanceof BitmapDrawable)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 28) {
                this.composeShader = new ComposeShader(this.gradientShader, this.imageShader, PorterDuff.Mode.DST_IN);
                return;
            }
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            int width = bitmapDrawable.getBitmap().getWidth();
            int height = bitmapDrawable.getBitmap().getHeight();
            Bitmap bitmap = this.legacyBitmap;
            if (bitmap != null && bitmap.getWidth() == width && this.legacyBitmap.getHeight() == height) {
                return;
            }
            Bitmap bitmap2 = this.legacyBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            this.legacyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.legacyCanvas = new Canvas(this.legacyBitmap);
            Bitmap bitmap3 = this.legacyBitmap;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            this.legacyShader = new BitmapShader(bitmap3, tileMode, tileMode);
            if (this.legacyPaint == null) {
                Paint paint = new Paint();
                this.legacyPaint = paint;
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setStaticDrawable(Drawable drawable) {
        AttachableDrawable attachableDrawable;
        Drawable drawable2 = this.staticThumbDrawable;
        if (drawable == drawable2) {
            return;
        }
        if (!(drawable2 instanceof AttachableDrawable)) {
            attachableDrawable = null;
        } else if (drawable2.equals(drawable)) {
            return;
        } else {
            attachableDrawable = (AttachableDrawable) this.staticThumbDrawable;
        }
        this.staticThumbDrawable = drawable;
        if (this.attachedToWindow && (drawable instanceof AttachableDrawable)) {
            ((AttachableDrawable) drawable).onAttachedToWindow(this);
        }
        if (!this.attachedToWindow || attachableDrawable == null) {
            return;
        }
        attachableDrawable.onDetachedFromWindow(this);
    }

    private void updateDrawableRadius(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        int[] roundRadius = getRoundRadius(true);
        if (drawable instanceof ClipRoundedDrawable) {
            ((ClipRoundedDrawable) drawable).setRadii(roundRadius[0], roundRadius[1], roundRadius[2], roundRadius[3]);
            return;
        }
        if ((!hasRoundRadius() && this.gradientShader == null) || (!(drawable instanceof BitmapDrawable) && !(drawable instanceof AvatarDrawable))) {
            setDrawableShader(drawable, null);
            return;
        }
        if (drawable instanceof AvatarDrawable) {
            ((AvatarDrawable) drawable).setRoundRadius(roundRadius[0]);
            return;
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        if (bitmapDrawable instanceof RLottieDrawable) {
            return;
        }
        if (bitmapDrawable instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) drawable).setRoundRadius(roundRadius);
        } else {
            if (bitmapDrawable.getBitmap() == null || bitmapDrawable.getBitmap().isRecycled()) {
                return;
            }
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            setDrawableShader(drawable, new BitmapShader(bitmap, tileMode, tileMode));
        }
    }

    public void addDecorator(Decorator decorator) {
        if (this.decorators == null) {
            this.decorators = new ArrayList<>();
        }
        this.decorators.add(decorator);
        if (this.attachedToWindow) {
            decorator.onAttachedToWindow(this);
        }
    }

    public void addLoadingImageRunnable(Runnable runnable) {
        this.loadingOperations.add(runnable);
    }

    public void bumpPriority() {
        ImageLoader.getInstance().changeFileLoadingPriorityForImageReceiver(this);
    }

    public boolean canInvertBitmap() {
        return (this.currentMediaDrawable instanceof ExtendedBitmapDrawable) || (this.currentImageDrawable instanceof ExtendedBitmapDrawable) || (this.currentThumbDrawable instanceof ExtendedBitmapDrawable) || (this.staticThumbDrawable instanceof ExtendedBitmapDrawable);
    }

    public void cancelLoadImage() {
        this.forceLoding = false;
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
        this.canceledLoading = true;
    }

    public void clearDecorators() {
        if (this.decorators != null) {
            if (this.attachedToWindow) {
                for (int i = 0; i < this.decorators.size(); i++) {
                    this.decorators.get(i).onDetachedFromWidnow();
                }
            }
            this.decorators.clear();
        }
    }

    public void clearImage() {
        for (int i = 0; i < 4; i++) {
            recycleBitmap(null, i);
        }
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3;
        if (i == NotificationCenter.didReplacedPhotoInMemCache) {
            String str = (String) objArr[0];
            String str2 = this.currentMediaKey;
            if (str2 != null && str2.equals(str)) {
                this.currentMediaKey = (String) objArr[1];
                ImageLocation imageLocation = (ImageLocation) objArr[2];
                this.currentMediaLocation = imageLocation;
                SetImageBackup setImageBackup = this.setImageBackup;
                if (setImageBackup != null) {
                    setImageBackup.mediaLocation = imageLocation;
                }
            }
            String str3 = this.currentImageKey;
            if (str3 != null && str3.equals(str)) {
                this.currentImageKey = (String) objArr[1];
                ImageLocation imageLocation2 = (ImageLocation) objArr[2];
                this.currentImageLocation = imageLocation2;
                SetImageBackup setImageBackup2 = this.setImageBackup;
                if (setImageBackup2 != null) {
                    setImageBackup2.imageLocation = imageLocation2;
                }
            }
            String str4 = this.currentThumbKey;
            if (str4 == null || !str4.equals(str)) {
                return;
            }
            this.currentThumbKey = (String) objArr[1];
            ImageLocation imageLocation3 = (ImageLocation) objArr[2];
            this.currentThumbLocation = imageLocation3;
            SetImageBackup setImageBackup3 = this.setImageBackup;
            if (setImageBackup3 != null) {
                setImageBackup3.thumbLocation = imageLocation3;
                return;
            }
            return;
        }
        if (i == NotificationCenter.stopAllHeavyOperations) {
            Integer num = (Integer) objArr[0];
            if (this.currentLayerNum >= num.intValue()) {
                return;
            }
            int intValue = num.intValue() | this.currentOpenedLayerFlags;
            this.currentOpenedLayerFlags = intValue;
            if (intValue != 0) {
                RLottieDrawable lottieAnimation = getLottieAnimation();
                if (lottieAnimation != null && lottieAnimation.isHeavyDrawable()) {
                    lottieAnimation.stop();
                }
                AnimatedFileDrawable animation = getAnimation();
                if (animation != null) {
                    animation.stop();
                    return;
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.startAllHeavyOperations) {
            Integer num2 = (Integer) objArr[0];
            if (this.currentLayerNum >= num2.intValue() || (i3 = this.currentOpenedLayerFlags) == 0) {
                return;
            }
            int intValue2 = (num2.intValue() ^ (-1)) & i3;
            this.currentOpenedLayerFlags = intValue2;
            if (intValue2 == 0) {
                RLottieDrawable lottieAnimation2 = getLottieAnimation();
                if (lottieAnimation2 != null) {
                    lottieAnimation2.setAllowVibration(this.allowLottieVibration);
                }
                if (this.allowStartLottieAnimation && lottieAnimation2 != null && lottieAnimation2.isHeavyDrawable()) {
                    lottieAnimation2.start();
                }
                AnimatedFileDrawable animation2 = getAnimation();
                if (!this.allowStartAnimation || animation2 == null) {
                    return;
                }
                animation2.checkRepeat();
                invalidate();
            }
        }
    }

    public boolean draw(Canvas canvas) {
        return draw(canvas, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:114:0x023f, code lost:
    
        if (r37.useRoundForThumb == false) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0241, code lost:
    
        if (r6 != null) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0243, code lost:
    
        updateDrawableRadius(r11);
     */
    /* JADX WARN: Removed duplicated region for block: B:149:0x034e  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x01bb  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x014f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0164 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0175 A[Catch: Exception -> 0x008c, TryCatch #0 {Exception -> 0x008c, blocks: (B:10:0x002a, B:11:0x0100, B:13:0x0104, B:15:0x010b, B:18:0x0112, B:23:0x0135, B:27:0x013f, B:28:0x01a7, B:30:0x01b1, B:33:0x01c1, B:44:0x01e4, B:51:0x032d, B:53:0x033f, B:56:0x034a, B:139:0x02e4, B:140:0x0308, B:142:0x031f, B:148:0x0323, B:151:0x0358, B:153:0x035c, B:154:0x0362, B:155:0x0379, B:162:0x0155, B:165:0x0166, B:167:0x0175, B:168:0x0182, B:170:0x0186, B:173:0x018c, B:174:0x0191, B:177:0x0125, B:180:0x012b, B:182:0x0132, B:185:0x0090, B:187:0x00c2, B:192:0x00ca), top: B:8:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0182 A[Catch: Exception -> 0x008c, TryCatch #0 {Exception -> 0x008c, blocks: (B:10:0x002a, B:11:0x0100, B:13:0x0104, B:15:0x010b, B:18:0x0112, B:23:0x0135, B:27:0x013f, B:28:0x01a7, B:30:0x01b1, B:33:0x01c1, B:44:0x01e4, B:51:0x032d, B:53:0x033f, B:56:0x034a, B:139:0x02e4, B:140:0x0308, B:142:0x031f, B:148:0x0323, B:151:0x0358, B:153:0x035c, B:154:0x0362, B:155:0x0379, B:162:0x0155, B:165:0x0166, B:167:0x0175, B:168:0x0182, B:170:0x0186, B:173:0x018c, B:174:0x0191, B:177:0x0125, B:180:0x012b, B:182:0x0132, B:185:0x0090, B:187:0x00c2, B:192:0x00ca), top: B:8:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x013b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x01b1 A[Catch: Exception -> 0x008c, TryCatch #0 {Exception -> 0x008c, blocks: (B:10:0x002a, B:11:0x0100, B:13:0x0104, B:15:0x010b, B:18:0x0112, B:23:0x0135, B:27:0x013f, B:28:0x01a7, B:30:0x01b1, B:33:0x01c1, B:44:0x01e4, B:51:0x032d, B:53:0x033f, B:56:0x034a, B:139:0x02e4, B:140:0x0308, B:142:0x031f, B:148:0x0323, B:151:0x0358, B:153:0x035c, B:154:0x0362, B:155:0x0379, B:162:0x0155, B:165:0x0166, B:167:0x0175, B:168:0x0182, B:170:0x0186, B:173:0x018c, B:174:0x0191, B:177:0x0125, B:180:0x012b, B:182:0x0132, B:185:0x0090, B:187:0x00c2, B:192:0x00ca), top: B:8:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01c1 A[Catch: Exception -> 0x008c, TRY_LEAVE, TryCatch #0 {Exception -> 0x008c, blocks: (B:10:0x002a, B:11:0x0100, B:13:0x0104, B:15:0x010b, B:18:0x0112, B:23:0x0135, B:27:0x013f, B:28:0x01a7, B:30:0x01b1, B:33:0x01c1, B:44:0x01e4, B:51:0x032d, B:53:0x033f, B:56:0x034a, B:139:0x02e4, B:140:0x0308, B:142:0x031f, B:148:0x0323, B:151:0x0358, B:153:0x035c, B:154:0x0362, B:155:0x0379, B:162:0x0155, B:165:0x0166, B:167:0x0175, B:168:0x0182, B:170:0x0186, B:173:0x018c, B:174:0x0191, B:177:0x0125, B:180:0x012b, B:182:0x0132, B:185:0x0090, B:187:0x00c2, B:192:0x00ca), top: B:8:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x037f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0398  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x03a9 A[LOOP:0: B:77:0x03a1->B:79:0x03a9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0269 A[Catch: Exception -> 0x0212, TryCatch #2 {Exception -> 0x0212, blocks: (B:41:0x01de, B:46:0x020c, B:47:0x0217, B:50:0x021d, B:98:0x0269, B:100:0x026d, B:103:0x0272, B:105:0x0281, B:107:0x0294, B:109:0x0298, B:110:0x0279, B:113:0x023d, B:116:0x0243, B:117:0x0246, B:123:0x0258, B:126:0x025e, B:127:0x029b, B:132:0x02a7, B:133:0x02c1, B:135:0x02c4, B:137:0x02d4), top: B:40:0x01de }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean draw(Canvas canvas, BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
        boolean z;
        int i;
        AnimatedFileDrawable animation;
        RLottieDrawable lottieAnimation;
        BitmapShader bitmapShader;
        BitmapShader bitmapShader2;
        float f;
        BitmapShader bitmapShader3;
        BitmapShader bitmapShader4;
        Drawable drawable;
        float f2;
        boolean z2;
        int[] iArr;
        float f3;
        Drawable drawable2;
        boolean z3;
        Drawable drawable3;
        BitmapShader bitmapShader5;
        Drawable drawable4;
        Drawable drawable5;
        int[] iArr2;
        boolean z4;
        boolean z5;
        Drawable drawable6;
        BitmapShader bitmapShader6;
        int i2;
        int i3;
        Drawable drawable7;
        boolean z6;
        boolean z7;
        int i4;
        BackgroundThreadDrawHolder backgroundThreadDrawHolder2;
        boolean z8;
        float f4;
        boolean z9;
        int[] iArr3;
        float f5;
        Drawable drawable8;
        boolean z10;
        BitmapShader bitmapShader7;
        int i5;
        int i6;
        Canvas canvas2 = canvas;
        if (this.gradientBitmap != null && this.currentImageKey != null) {
            canvas.save();
            float f6 = this.imageX;
            float f7 = this.imageY;
            canvas2.clipRect(f6, f7, this.imageW + f6, this.imageH + f7);
            canvas2.drawColor(-16777216);
        }
        boolean z11 = backgroundThreadDrawHolder != null;
        try {
            if (z11) {
                animation = backgroundThreadDrawHolder.animation;
                lottieAnimation = backgroundThreadDrawHolder.lottieDrawable;
                int[] iArr4 = backgroundThreadDrawHolder.roundRadius;
                Drawable drawable9 = backgroundThreadDrawHolder.mediaDrawable;
                bitmapShader = backgroundThreadDrawHolder.mediaShader;
                Drawable drawable10 = backgroundThreadDrawHolder.imageDrawable;
                bitmapShader2 = backgroundThreadDrawHolder.imageShader;
                BitmapShader bitmapShader8 = backgroundThreadDrawHolder.thumbShader;
                BitmapShader bitmapShader9 = backgroundThreadDrawHolder.staticThumbShader;
                Drawable drawable11 = backgroundThreadDrawHolder.crossfadeImage;
                boolean unused = backgroundThreadDrawHolder.crossfadeWithOldImage;
                boolean z12 = backgroundThreadDrawHolder.crossfadingWithThumb;
                Drawable drawable12 = backgroundThreadDrawHolder.thumbDrawable;
                Drawable drawable13 = backgroundThreadDrawHolder.staticThumbDrawable;
                float f8 = backgroundThreadDrawHolder.currentAlpha;
                f2 = backgroundThreadDrawHolder.previousAlpha;
                bitmapShader3 = backgroundThreadDrawHolder.crossfadeShader;
                f = f8;
                iArr = iArr4;
                drawable2 = drawable9;
                z3 = backgroundThreadDrawHolder.animationNotReady;
                drawable3 = drawable10;
                bitmapShader5 = bitmapShader9;
                bitmapShader4 = bitmapShader8;
                drawable5 = drawable11;
                drawable4 = drawable12;
                z2 = z12;
                f3 = backgroundThreadDrawHolder.overrideAlpha;
                drawable = drawable13;
            } else {
                animation = getAnimation();
                lottieAnimation = getLottieAnimation();
                int[] iArr5 = this.roundRadius;
                Drawable drawable14 = this.currentMediaDrawable;
                bitmapShader = this.mediaShader;
                Drawable drawable15 = this.currentImageDrawable;
                bitmapShader2 = this.imageShader;
                Drawable drawable16 = this.currentThumbDrawable;
                BitmapShader bitmapShader10 = this.thumbShader;
                BitmapShader bitmapShader11 = this.staticThumbShader;
                boolean z13 = this.crossfadingWithThumb;
                Drawable drawable17 = this.crossfadeImage;
                Drawable drawable18 = this.staticThumbDrawable;
                float f9 = this.currentAlpha;
                float f10 = this.previousAlpha;
                BitmapShader bitmapShader12 = this.crossfadeShader;
                float f11 = this.overrideAlpha;
                if ((animation == null || animation.hasBitmap()) && (lottieAnimation == null || lottieAnimation.hasBitmap())) {
                    f = f9;
                    bitmapShader3 = bitmapShader12;
                    bitmapShader4 = bitmapShader10;
                    drawable = drawable18;
                    f2 = f10;
                    z2 = z13;
                    iArr = iArr5;
                    f3 = f11;
                    drawable2 = drawable14;
                    z3 = false;
                } else {
                    f = f9;
                    bitmapShader3 = bitmapShader12;
                    bitmapShader4 = bitmapShader10;
                    drawable = drawable18;
                    f2 = f10;
                    z2 = z13;
                    iArr = iArr5;
                    f3 = f11;
                    drawable2 = drawable14;
                    z3 = true;
                }
                drawable3 = drawable15;
                bitmapShader5 = bitmapShader11;
                drawable4 = drawable16;
                drawable5 = drawable17;
            }
            int[] iArr6 = !this.useRoundRadius ? this.emptyRoundRadius : iArr;
            if (animation != null) {
                animation.setRoundRadius(iArr6);
            }
            if (lottieAnimation == null || z11) {
                iArr2 = iArr6;
            } else {
                iArr2 = iArr6;
                lottieAnimation.setCurrentParentView(this.parentView);
            }
        } catch (Exception e) {
            e = e;
        }
        if (animation == null) {
            if (lottieAnimation != null) {
            }
            z4 = this.forcePreview;
            if (z4 && drawable2 != null && !z3) {
                i2 = this.imageOrientation;
                i3 = this.imageInvert;
                z5 = z3;
                bitmapShader6 = bitmapShader;
                drawable6 = drawable2;
            } else if (z4 && drawable3 != null && (!z3 || drawable2 != null)) {
                i2 = this.imageOrientation;
                i3 = this.imageInvert;
                bitmapShader6 = bitmapShader2;
                drawable6 = drawable3;
                z5 = false;
            } else if (drawable5 == null && !z2) {
                i2 = this.imageOrientation;
                i3 = this.imageInvert;
                z5 = z3;
                drawable6 = drawable5;
                bitmapShader6 = bitmapShader3;
            } else if (drawable4 != null) {
                i2 = this.thumbOrientation;
                i3 = this.thumbInvert;
                z5 = z3;
                drawable6 = drawable4;
                bitmapShader6 = bitmapShader4;
            } else if (drawable instanceof BitmapDrawable) {
                if (this.useRoundForThumb && bitmapShader5 == null) {
                    updateDrawableRadius(drawable);
                    bitmapShader5 = this.staticThumbShader;
                }
                i2 = this.thumbOrientation;
                i3 = this.thumbInvert;
                z5 = z3;
                bitmapShader6 = bitmapShader5;
                drawable6 = drawable;
            } else {
                z5 = z3;
                drawable6 = null;
                bitmapShader6 = null;
                i2 = 0;
                i3 = 0;
            }
            float f12 = this.crossfadeByScale;
            float min = f12 > 0.0f ? Math.min((f12 * f) + f, 1.0f) : f;
            if (drawable6 != null) {
                if (this.crossfadeAlpha != 0) {
                    if (f2 == 1.0f || (!(drawable6 == drawable3 || drawable6 == drawable2) || drawable == null)) {
                        f4 = f;
                        drawable7 = drawable6;
                        z9 = z5;
                        z7 = z11;
                        iArr3 = iArr2;
                        f5 = 1.0f;
                        drawable8 = drawable5;
                    } else {
                        try {
                            if (this.useRoundForThumb && bitmapShader5 == null) {
                                updateDrawableRadius(drawable);
                                bitmapShader5 = this.staticThumbShader;
                            }
                            BitmapShader bitmapShader13 = bitmapShader5;
                            z7 = z11;
                            f5 = 1.0f;
                            f4 = f;
                            drawable7 = drawable6;
                            z9 = z5;
                            iArr3 = iArr2;
                            drawable8 = drawable5;
                            drawDrawable(canvas, drawable, (int) (f3 * 255.0f), bitmapShader13, i2, i3, backgroundThreadDrawHolder);
                            bitmapShader5 = bitmapShader13;
                        } catch (Exception e2) {
                            e = e2;
                            canvas2 = canvas;
                            z = false;
                            FileLog.e(e);
                            if (this.gradientBitmap != null) {
                            }
                            if (z) {
                            }
                            return z;
                        }
                    }
                    boolean z14 = this.crossfadeWithThumb;
                    if (z14 && z9) {
                        i4 = (int) (f3 * 255.0f);
                        z6 = z9;
                        canvas2 = canvas;
                    } else {
                        if (z14 && min != f5) {
                            if (drawable7 != drawable3 && drawable7 != drawable2) {
                                if (drawable7 != drawable4 && drawable7 != drawable8) {
                                    if (drawable7 == drawable && drawable8 != null) {
                                        drawable = drawable8;
                                        bitmapShader7 = bitmapShader3;
                                        if (drawable != null) {
                                            if (!(drawable instanceof SvgHelper.SvgDrawable) && !(drawable instanceof Emoji.EmojiDrawable)) {
                                                i5 = (int) (f2 * f3 * 255.0f);
                                                i6 = i5;
                                                drawDrawable(canvas, drawable, i6, bitmapShader7, this.thumbOrientation, this.thumbInvert, backgroundThreadDrawHolder);
                                                if (i6 != 255 && (drawable instanceof Emoji.EmojiDrawable)) {
                                                    drawable.setAlpha(NotificationCenter.liveLocationsChanged);
                                                }
                                            }
                                            i5 = (int) ((f5 - min) * f3 * 255.0f);
                                            i6 = i5;
                                            drawDrawable(canvas, drawable, i6, bitmapShader7, this.thumbOrientation, this.thumbInvert, backgroundThreadDrawHolder);
                                            if (i6 != 255) {
                                                drawable.setAlpha(NotificationCenter.liveLocationsChanged);
                                            }
                                        }
                                    }
                                    bitmapShader7 = null;
                                    drawable = null;
                                    if (drawable != null) {
                                    }
                                }
                                bitmapShader7 = null;
                                drawable = null;
                                if (drawable != null) {
                                }
                            }
                            if (drawable4 != null) {
                                drawable = drawable4;
                                bitmapShader7 = bitmapShader4;
                                if (drawable != null) {
                                }
                            } else {
                                if (drawable != null) {
                                    if (this.useRoundForThumb && bitmapShader5 == null) {
                                        updateDrawableRadius(drawable);
                                        bitmapShader5 = this.staticThumbShader;
                                    }
                                    bitmapShader7 = bitmapShader5;
                                    if (drawable != null) {
                                    }
                                }
                                bitmapShader7 = null;
                                drawable = null;
                                if (drawable != null) {
                                }
                            }
                        }
                        if (this.crossfadeByScale <= 0.0f || min >= f5 || !z2) {
                            z6 = z9;
                            canvas2 = canvas;
                            z10 = false;
                        } else {
                            canvas.save();
                            this.roundPath.rewind();
                            RectF rectF = AndroidUtilities.rectTmp;
                            float f13 = this.imageX;
                            float f14 = this.imageY;
                            rectF.set(f13, f14, this.imageW + f13, this.imageH + f14);
                            int[] iArr7 = iArr3;
                            for (int i7 = 0; i7 < iArr7.length; i7++) {
                                float[] fArr = radii;
                                int i8 = i7 * 2;
                                float f15 = iArr7[i7];
                                fArr[i8] = f15;
                                fArr[i8 + 1] = f15;
                            }
                            this.roundPath.addRoundRect(AndroidUtilities.rectTmp, radii, Path.Direction.CW);
                            z6 = z9;
                            canvas2 = canvas;
                            canvas2.clipPath(this.roundPath);
                            float interpolation = (this.crossfadeByScale * (f5 - CubicBezierInterpolator.EASE_IN.getInterpolation(f4))) + f5;
                            canvas2.scale(interpolation, interpolation, getCenterX(), getCenterY());
                            z10 = true;
                        }
                        drawDrawable(canvas, drawable7, (int) (f3 * min * 255.0f), bitmapShader6, i2, i3, backgroundThreadDrawHolder);
                        if (z10) {
                            canvas.restore();
                        }
                        if (z6 || !this.crossfadeWithThumb) {
                            backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
                            z8 = false;
                        } else {
                            backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
                            z8 = true;
                        }
                        checkAlphaAnimation(z8, backgroundThreadDrawHolder2);
                    }
                } else {
                    drawable7 = drawable6;
                    z6 = z5;
                    z7 = z11;
                    i4 = (int) (f3 * 255.0f);
                }
                drawDrawable(canvas, drawable7, i4, bitmapShader6, i2, i3, backgroundThreadDrawHolder);
                if (z6) {
                }
                backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
                z8 = false;
                checkAlphaAnimation(z8, backgroundThreadDrawHolder2);
            } else {
                drawable7 = drawable6;
                z6 = z5;
                z7 = z11;
                if (drawable == null) {
                    checkAlphaAnimation(z6, backgroundThreadDrawHolder);
                    z = false;
                    if (drawable7 == null && z6 && !z7) {
                        try {
                            invalidate();
                        } catch (Exception e3) {
                            e = e3;
                            FileLog.e(e);
                            if (this.gradientBitmap != null) {
                            }
                            if (z) {
                            }
                            return z;
                        }
                    }
                    if (this.gradientBitmap != null && this.currentImageKey != null) {
                        canvas.restore();
                    }
                    if (z && this.isVisible && this.decorators != null) {
                        for (i = 0; i < this.decorators.size(); i++) {
                            this.decorators.get(i).onDraw(canvas2, this);
                        }
                    }
                    return z;
                }
                if (drawable instanceof VectorAvatarThumbDrawable) {
                    ((VectorAvatarThumbDrawable) drawable).setParent(this);
                }
                drawDrawable(canvas, drawable, (int) (f3 * 255.0f), null, this.thumbOrientation, this.thumbInvert, backgroundThreadDrawHolder);
                checkAlphaAnimation(z6, backgroundThreadDrawHolder);
            }
            z = true;
            if (drawable7 == null) {
                invalidate();
            }
            if (this.gradientBitmap != null) {
                canvas.restore();
            }
            if (z) {
                while (i < this.decorators.size()) {
                }
            }
            return z;
        }
        if (!z3 && !this.animationReadySent && !z11) {
            this.animationReadySent = true;
            ImageReceiverDelegate imageReceiverDelegate = this.delegate;
            if (imageReceiverDelegate != null) {
                imageReceiverDelegate.onAnimationReady(this);
            }
            z4 = this.forcePreview;
            if (z4) {
            }
            if (z4) {
            }
            if (drawable5 == null) {
            }
            if (drawable4 != null) {
            }
            float f122 = this.crossfadeByScale;
            if (f122 > 0.0f) {
            }
            if (drawable6 != null) {
            }
            z = true;
            if (drawable7 == null) {
            }
            if (this.gradientBitmap != null) {
            }
            if (z) {
            }
            return z;
        }
        z4 = this.forcePreview;
        if (z4) {
        }
        if (z4) {
        }
        if (drawable5 == null) {
        }
        if (drawable4 != null) {
        }
        float f1222 = this.crossfadeByScale;
        if (f1222 > 0.0f) {
        }
        if (drawable6 != null) {
        }
        z = true;
        if (drawable7 == null) {
        }
        if (this.gradientBitmap != null) {
        }
        if (z) {
        }
        return z;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:400|(1:402)(10:432|(2:434|(1:436))|437|(1:406)|407|408|409|(1:426)(6:412|(1:414)|415|416|417|418)|419|(2:421|422)(1:423))|403|(1:406)|407|408|409|(0)|426|419|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0547, code lost:
    
        if (r5 != null) goto L256;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0608, code lost:
    
        if (r15[0] > 0) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x06fa, code lost:
    
        if (r39 != null) goto L335;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x08a9, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x08aa, code lost:
    
        r15 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x08b0, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x088c, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x088d, code lost:
    
        r15 = 0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x07cb  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x07d3  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x08b5  */
    /* JADX WARN: Removed duplicated region for block: B:423:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v3, types: [org.telegram.messenger.SvgHelper$SvgDrawable] */
    /* JADX WARN: Type inference failed for: r12v4 */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v12 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v40 */
    /* JADX WARN: Type inference failed for: r15v41 */
    /* JADX WARN: Type inference failed for: r15v5 */
    /* JADX WARN: Type inference failed for: r15v6 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /* JADX WARN: Type inference failed for: r15v8, types: [org.telegram.messenger.ImageReceiver] */
    /* JADX WARN: Type inference failed for: r15v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void drawDrawable(Canvas canvas, Drawable drawable, int i, BitmapShader bitmapShader, int i2, int i3, int i4, BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
        float f;
        float f2;
        float f3;
        float f4;
        RectF rectF;
        ColorFilter colorFilter;
        int[] iArr;
        ?? r12;
        SvgHelper.SvgDrawable svgDrawable;
        int i5;
        PorterDuffColorFilter porterDuffColorFilter;
        Paint paint;
        PorterDuffColorFilter porterDuffColorFilter2;
        int intrinsicHeight;
        int intrinsicWidth;
        boolean z;
        boolean z2;
        float f5;
        Paint paint2;
        BlendMode blendMode;
        int i6;
        BitmapDrawable bitmapDrawable;
        Matrix matrix;
        float f6;
        float f7;
        Canvas canvas2;
        RectF rectF2;
        Paint paint3;
        Matrix matrix2;
        float f8;
        float f9;
        Object obj;
        if (backgroundThreadDrawHolder != null) {
            f = backgroundThreadDrawHolder.imageX;
            f2 = backgroundThreadDrawHolder.imageY;
            f3 = backgroundThreadDrawHolder.imageH;
            f4 = backgroundThreadDrawHolder.imageW;
            rectF = backgroundThreadDrawHolder.drawRegion;
            colorFilter = backgroundThreadDrawHolder.colorFilter;
            iArr = backgroundThreadDrawHolder.roundRadius;
        } else {
            f = this.imageX;
            f2 = this.imageY;
            f3 = this.imageH;
            f4 = this.imageW;
            rectF = this.drawRegion;
            colorFilter = this.colorFilter;
            iArr = this.roundRadius;
        }
        int[] iArr2 = iArr;
        if (!this.useRoundRadius) {
            iArr2 = this.emptyRoundRadius;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable2 = (BitmapDrawable) drawable;
            boolean z3 = drawable instanceof RLottieDrawable;
            if (z3) {
                ((RLottieDrawable) drawable).skipFrameUpdate = this.skipUpdateFrame;
            } else if (drawable instanceof AnimatedFileDrawable) {
                ((AnimatedFileDrawable) drawable).skipFrameUpdate = this.skipUpdateFrame;
            }
            Paint paint4 = bitmapShader != null ? this.roundPaint : bitmapDrawable2.getPaint();
            int i7 = Build.VERSION.SDK_INT;
            if (i7 >= 29) {
                Object obj2 = this.blendMode;
                i5 = i7;
                paint4.setBlendMode((obj2 == null || this.gradientShader != null) ? null : ImageReceiver$$ExternalSyntheticApiModelOutline0.m(obj2));
            } else {
                i5 = i7;
            }
            boolean z4 = (paint4 == null || paint4.getColorFilter() == null) ? false : true;
            if (z4 && i4 == 0) {
                if (bitmapShader != null) {
                    obj = null;
                    this.roundPaint.setColorFilter(null);
                } else {
                    obj = null;
                    if (this.staticThumbDrawable != drawable) {
                        bitmapDrawable2.setColorFilter(null);
                    }
                }
            } else if (!z4 && i4 != 0) {
                if (i4 == 1) {
                    if (bitmapShader != null) {
                        paint = this.roundPaint;
                        porterDuffColorFilter2 = selectedColorFilter;
                        paint.setColorFilter(porterDuffColorFilter2);
                    } else {
                        porterDuffColorFilter = selectedColorFilter;
                        bitmapDrawable2.setColorFilter(porterDuffColorFilter);
                    }
                } else if (bitmapShader != null) {
                    paint = this.roundPaint;
                    porterDuffColorFilter2 = selectedGroupColorFilter;
                    paint.setColorFilter(porterDuffColorFilter2);
                } else {
                    porterDuffColorFilter = selectedGroupColorFilter;
                    bitmapDrawable2.setColorFilter(porterDuffColorFilter);
                }
            }
            if (colorFilter != null && this.gradientShader == null) {
                if (bitmapShader != null) {
                    this.roundPaint.setColorFilter(colorFilter);
                } else {
                    bitmapDrawable2.setColorFilter(colorFilter);
                }
            }
            boolean z5 = bitmapDrawable2 instanceof AnimatedFileDrawable;
            if (z5 || (bitmapDrawable2 instanceof RLottieDrawable)) {
                int i8 = i2 % 360;
                if (i8 == 90 || i8 == 270) {
                    intrinsicHeight = bitmapDrawable2.getIntrinsicHeight();
                    intrinsicWidth = bitmapDrawable2.getIntrinsicWidth();
                } else {
                    intrinsicHeight = bitmapDrawable2.getIntrinsicWidth();
                    intrinsicWidth = bitmapDrawable2.getIntrinsicHeight();
                }
                z = false;
            } else {
                Bitmap bitmap = bitmapDrawable2.getBitmap();
                if (bitmap != null && bitmap.isRecycled()) {
                    return;
                }
                int i9 = i2 % 360;
                if (i9 == 90 || i9 == 270) {
                    intrinsicHeight = bitmap.getHeight();
                    intrinsicWidth = bitmap.getWidth();
                } else {
                    intrinsicHeight = bitmap.getWidth();
                    intrinsicWidth = bitmap.getHeight();
                }
                z = bitmapDrawable2 instanceof ReactionLastFrame;
            }
            float f10 = this.sideClip * 2.0f;
            float f11 = f4 - f10;
            float f12 = f3 - f10;
            float f13 = f4 == 0.0f ? 1.0f : intrinsicHeight / f11;
            if (f3 == 0.0f) {
                z2 = z5;
                f5 = 1.0f;
            } else {
                z2 = z5;
                f5 = intrinsicWidth / f12;
            }
            if (z) {
                f13 /= 1.2f;
                f5 /= 1.2f;
            }
            boolean z6 = z;
            if (bitmapShader == null || backgroundThreadDrawHolder != null) {
                float f14 = f4;
                int[] iArr3 = iArr2;
                if (this.isAspectFit) {
                    float max = Math.max(f13, f5);
                    canvas.save();
                    int i10 = (int) (intrinsicHeight / max);
                    int i11 = (int) (intrinsicWidth / max);
                    if (backgroundThreadDrawHolder == null) {
                        float f15 = i10;
                        float f16 = i11;
                        rectF.set(((f14 - f15) / 2.0f) + f, ((f3 - f16) / 2.0f) + f2, ((f14 + f15) / 2.0f) + f, ((f16 + f3) / 2.0f) + f2);
                        bitmapDrawable2.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                        if (bitmapDrawable2 instanceof AnimatedFileDrawable) {
                            ((AnimatedFileDrawable) bitmapDrawable2).setActualDrawRect(rectF.left, rectF.top, rectF.width(), rectF.height());
                        }
                    }
                    if (backgroundThreadDrawHolder != null && iArr3 != null && iArr3[0] > 0) {
                        canvas.save();
                        Path path = backgroundThreadDrawHolder.roundPath == null ? backgroundThreadDrawHolder.roundPath = new Path() : backgroundThreadDrawHolder.roundPath;
                        path.rewind();
                        RectF rectF3 = AndroidUtilities.rectTmp;
                        rectF3.set(f, f2, f + f14, f3 + f2);
                        path.addRoundRect(rectF3, iArr3[0], iArr3[2], Path.Direction.CW);
                        canvas.clipPath(path);
                    }
                    if (this.isVisible) {
                        try {
                            bitmapDrawable2.setAlpha(i);
                            drawBitmapDrawable(canvas, bitmapDrawable2, backgroundThreadDrawHolder, i);
                        } catch (Exception e) {
                            if (backgroundThreadDrawHolder == null) {
                                onBitmapException(bitmapDrawable2);
                            }
                            FileLog.e(e);
                        }
                    }
                    canvas.restore();
                    if (backgroundThreadDrawHolder != null) {
                        if (iArr3 != null) {
                        }
                    }
                } else if (canvas != null) {
                    if (Math.abs(f13 - f5) > 1.0E-5f) {
                        canvas.save();
                        if (this.clip) {
                            canvas.clipRect(f, f2, f + f14, f2 + f3);
                        }
                        if (i3 == 1) {
                            canvas.scale(-1.0f, 1.0f, f14 / 2.0f, f3 / 2.0f);
                        } else if (i3 == 2) {
                            canvas.scale(1.0f, -1.0f, f14 / 2.0f, f3 / 2.0f);
                        }
                        int i12 = i2 % 360;
                        if (i12 != 0) {
                            float f17 = i2;
                            if (this.centerRotation) {
                                canvas.rotate(f17, f14 / 2.0f, f3 / 2.0f);
                            } else {
                                canvas.rotate(f17, 0.0f, 0.0f);
                            }
                        }
                        float f18 = intrinsicHeight / f5;
                        if (f18 > f14) {
                            float f19 = (int) f18;
                            rectF.set(f - ((f19 - f14) / 2.0f), f2, ((f19 + f14) / 2.0f) + f, f2 + f3);
                        } else {
                            float f20 = (int) (intrinsicWidth / f13);
                            rectF.set(f, f2 - ((f20 - f3) / 2.0f), f + f14, ((f20 + f3) / 2.0f) + f2);
                        }
                        if (z2) {
                            ((AnimatedFileDrawable) bitmapDrawable2).setActualDrawRect(f, f2, f14, f3);
                        }
                        if (backgroundThreadDrawHolder == null) {
                            if (i12 == 90 || i12 == 270) {
                                float width = rectF.width() / 2.0f;
                                float height = rectF.height() / 2.0f;
                                float centerX = rectF.centerX();
                                float centerY = rectF.centerY();
                                bitmapDrawable2.setBounds((int) (centerX - height), (int) (centerY - width), (int) (centerX + height), (int) (centerY + width));
                            } else {
                                bitmapDrawable2.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                            }
                        }
                        if (this.isVisible) {
                            if (i5 >= 29) {
                                try {
                                    if (this.blendMode != null) {
                                        paint2 = bitmapDrawable2.getPaint();
                                        blendMode = ImageReceiver$$ExternalSyntheticApiModelOutline0.m(this.blendMode);
                                    } else {
                                        paint2 = bitmapDrawable2.getPaint();
                                        blendMode = null;
                                    }
                                    paint2.setBlendMode(blendMode);
                                } catch (Exception e2) {
                                    e = e2;
                                }
                            }
                            drawBitmapDrawable(canvas, bitmapDrawable2, backgroundThreadDrawHolder, i);
                        }
                    } else {
                        int i13 = i5;
                        canvas.save();
                        if (i3 == 1) {
                            canvas.scale(-1.0f, 1.0f, f14 / 2.0f, f3 / 2.0f);
                        } else if (i3 == 2) {
                            canvas.scale(1.0f, -1.0f, f14 / 2.0f, f3 / 2.0f);
                        }
                        int i14 = i2 % 360;
                        if (i14 != 0) {
                            float f21 = i2;
                            if (this.centerRotation) {
                                canvas.rotate(f21, f14 / 2.0f, f3 / 2.0f);
                            } else {
                                canvas.rotate(f21, 0.0f, 0.0f);
                            }
                        }
                        rectF.set(f, f2, f + f14, f2 + f3);
                        if (this.isRoundVideo) {
                            float f22 = -AndroidUtilities.roundMessageInset;
                            rectF.inset(f22, f22);
                        }
                        if (z2) {
                            ((AnimatedFileDrawable) bitmapDrawable2).setActualDrawRect(f, f2, f14, f3);
                        }
                        if (backgroundThreadDrawHolder == null) {
                            if (i14 == 90 || i14 == 270) {
                                float width2 = rectF.width() / 2.0f;
                                float height2 = rectF.height() / 2.0f;
                                float centerX2 = rectF.centerX();
                                float centerY2 = rectF.centerY();
                                bitmapDrawable2.setBounds((int) (centerX2 - height2), (int) (centerY2 - width2), (int) (centerX2 + height2), (int) (centerY2 + width2));
                            } else {
                                bitmapDrawable2.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                            }
                        }
                        if (this.isVisible) {
                            if (i13 >= 29) {
                                try {
                                    if (this.blendMode != null) {
                                        bitmapDrawable2.getPaint().setBlendMode(ImageReceiver$$ExternalSyntheticApiModelOutline0.m(this.blendMode));
                                    } else {
                                        bitmapDrawable2.getPaint().setBlendMode(null);
                                    }
                                } catch (Exception e3) {
                                    e = e3;
                                    onBitmapException(bitmapDrawable2);
                                    FileLog.e(e);
                                    canvas.restore();
                                    if (!z3) {
                                    }
                                }
                            }
                            drawBitmapDrawable(canvas, bitmapDrawable2, backgroundThreadDrawHolder, i);
                        }
                    }
                    canvas.restore();
                }
            } else if (this.isAspectFit) {
                float max2 = Math.max(f13, f5);
                float f23 = (int) (intrinsicHeight / max2);
                float f24 = (int) (intrinsicWidth / max2);
                rectF.set(((f4 - f23) / 2.0f) + f, ((f3 - f24) / 2.0f) + f2, f + ((f4 + f23) / 2.0f), f2 + ((f3 + f24) / 2.0f));
                if (this.isVisible) {
                    this.shaderMatrix.reset();
                    this.shaderMatrix.setTranslate((int) rectF.left, (int) rectF.top);
                    if (i3 != 0) {
                        this.shaderMatrix.preScale(i3 == 1 ? -1.0f : 1.0f, i3 == 2 ? -1.0f : 1.0f, rectF.width() / 2.0f, rectF.height() / 2.0f);
                    }
                    if (i2 == 90) {
                        this.shaderMatrix.preRotate(90.0f);
                        this.shaderMatrix.preTranslate(0.0f, -rectF.width());
                    } else {
                        if (i2 == 180) {
                            this.shaderMatrix.preRotate(180.0f);
                            matrix2 = this.shaderMatrix;
                            f8 = -rectF.width();
                            f9 = -rectF.height();
                        } else if (i2 == 270) {
                            this.shaderMatrix.preRotate(270.0f);
                            matrix2 = this.shaderMatrix;
                            f8 = -rectF.height();
                            f9 = 0.0f;
                        }
                        matrix2.preTranslate(f8, f9);
                    }
                    float f25 = 1.0f / max2;
                    this.shaderMatrix.preScale(f25, f25);
                    bitmapShader.setLocalMatrix(this.shaderMatrix);
                    this.roundPaint.setShader(bitmapShader);
                    this.roundPaint.setAlpha(i);
                    this.roundRect.set(rectF);
                    if (!this.isRoundRect || !this.useRoundRadius) {
                        int[] iArr4 = iArr2;
                        for (int i15 = 0; i15 < iArr4.length; i15++) {
                            float[] fArr = radii;
                            int i16 = i15 * 2;
                            float f26 = iArr4[i15];
                            fArr[i16] = f26;
                            fArr[i16 + 1] = f26;
                        }
                        this.roundPath.reset();
                        this.roundPath.addRoundRect(this.roundRect, radii, Path.Direction.CW);
                        this.roundPath.close();
                        if (canvas != null) {
                            canvas2 = canvas;
                            canvas2.drawPath(this.roundPath, this.roundPaint);
                        }
                    } else if (canvas != null) {
                        try {
                            int i17 = iArr2[0];
                            if (i17 == 0) {
                                canvas.drawRect(this.roundRect, this.roundPaint);
                            } else {
                                float f27 = i17;
                                canvas.drawRoundRect(this.roundRect, f27, f27, this.roundPaint);
                            }
                        } catch (Exception e4) {
                            e = e4;
                            onBitmapException(bitmapDrawable2);
                            FileLog.e(e);
                            if (!z3) {
                            }
                        }
                    }
                }
            } else {
                int[] iArr5 = iArr2;
                if (this.legacyCanvas != null) {
                    i6 = intrinsicWidth;
                    this.roundRect.set(0.0f, 0.0f, this.legacyBitmap.getWidth(), this.legacyBitmap.getHeight());
                    this.legacyCanvas.drawBitmap(this.gradientBitmap, (Rect) null, this.roundRect, (Paint) null);
                    bitmapDrawable = bitmapDrawable2;
                    this.legacyCanvas.drawBitmap(bitmapDrawable2.getBitmap(), (Rect) null, this.roundRect, this.legacyPaint);
                } else {
                    i6 = intrinsicWidth;
                    bitmapDrawable = bitmapDrawable2;
                }
                if (bitmapShader != this.imageShader || this.gradientShader == null) {
                    this.roundPaint.setShader(bitmapShader);
                } else {
                    ComposeShader composeShader = this.composeShader;
                    if (composeShader != null) {
                        this.roundPaint.setShader(composeShader);
                    } else {
                        this.roundPaint.setShader(this.legacyShader);
                    }
                }
                float min = 1.0f / Math.min(f13, f5);
                RectF rectF4 = this.roundRect;
                float f28 = this.sideClip;
                float f29 = f4;
                rectF4.set(f + f28, f2 + f28, (f + f4) - f28, (f2 + f3) - f28);
                if (Math.abs(f13 - f5) > 5.0E-4f) {
                    float f30 = intrinsicHeight / f5;
                    if (f30 > f11) {
                        float f31 = (int) f30;
                        rectF.set(f - ((f31 - f11) / 2.0f), f2, ((f31 + f11) / 2.0f) + f, f2 + f12);
                    } else {
                        float f32 = (int) (i6 / f13);
                        rectF.set(f, f2 - ((f32 - f12) / 2.0f), f + f11, ((f32 + f12) / 2.0f) + f2);
                    }
                } else {
                    rectF.set(f, f2, f + f11, f2 + f12);
                }
                if (this.isVisible) {
                    this.shaderMatrix.reset();
                    Matrix matrix3 = this.shaderMatrix;
                    float f33 = rectF.left;
                    float f34 = this.sideClip;
                    float f35 = f33 + f34;
                    if (z6) {
                        matrix3.setTranslate(f35 - (((rectF.width() * 1.2f) - rectF.width()) / 2.0f), (rectF.top + this.sideClip) - (((rectF.height() * 1.2f) - rectF.height()) / 2.0f));
                    } else {
                        matrix3.setTranslate(f35, rectF.top + f34);
                    }
                    if (i3 != 0) {
                        this.shaderMatrix.preScale(i3 == 1 ? -1.0f : 1.0f, i3 == 2 ? -1.0f : 1.0f, rectF.width() / 2.0f, rectF.height() / 2.0f);
                    }
                    if (i2 == 90) {
                        this.shaderMatrix.preRotate(90.0f);
                        this.shaderMatrix.preTranslate(0.0f, -rectF.width());
                    } else {
                        if (i2 == 180) {
                            this.shaderMatrix.preRotate(180.0f);
                            matrix = this.shaderMatrix;
                            f6 = -rectF.width();
                            f7 = -rectF.height();
                        } else if (i2 == 270) {
                            this.shaderMatrix.preRotate(270.0f);
                            matrix = this.shaderMatrix;
                            f6 = -rectF.height();
                            f7 = 0.0f;
                        }
                        matrix.preTranslate(f6, f7);
                    }
                    this.shaderMatrix.preScale(min, min);
                    if (this.isRoundVideo) {
                        float f36 = (f11 + (AndroidUtilities.roundMessageInset * 2)) / f11;
                        this.shaderMatrix.postScale(f36, f36, rectF.centerX(), rectF.centerY());
                    }
                    BitmapShader bitmapShader2 = this.legacyShader;
                    if (bitmapShader2 != null) {
                        bitmapShader2.setLocalMatrix(this.shaderMatrix);
                    }
                    bitmapShader.setLocalMatrix(this.shaderMatrix);
                    if (this.composeShader != null) {
                        int width3 = this.gradientBitmap.getWidth();
                        int height3 = this.gradientBitmap.getHeight();
                        float f37 = f29 == 0.0f ? 1.0f : width3 / f11;
                        float f38 = f3 == 0.0f ? 1.0f : height3 / f12;
                        if (Math.abs(f37 - f38) > 5.0E-4f) {
                            float f39 = width3 / f38;
                            if (f39 > f11) {
                                width3 = (int) f39;
                                float f40 = width3;
                                rectF.set(f - ((f40 - f11) / 2.0f), f2, f + ((f40 + f11) / 2.0f), f2 + f12);
                            } else {
                                height3 = (int) (height3 / f37);
                                float f41 = height3;
                                rectF.set(f, f2 - ((f41 - f12) / 2.0f), f + f11, f2 + ((f41 + f12) / 2.0f));
                            }
                        } else {
                            rectF.set(f, f2, f + f11, f2 + f12);
                        }
                        float min2 = 1.0f / Math.min(f29 == 0.0f ? 1.0f : width3 / f11, f3 == 0.0f ? 1.0f : height3 / f12);
                        this.shaderMatrix.reset();
                        Matrix matrix4 = this.shaderMatrix;
                        float f42 = rectF.left;
                        float f43 = this.sideClip;
                        matrix4.setTranslate(f42 + f43, rectF.top + f43);
                        this.shaderMatrix.preScale(min2, min2);
                        this.gradientShader.setLocalMatrix(this.shaderMatrix);
                    }
                    this.roundPaint.setAlpha(i);
                    if (!this.isRoundRect || !this.useRoundRadius) {
                        canvas2 = canvas;
                        for (int i18 = 0; i18 < iArr5.length; i18++) {
                            float[] fArr2 = radii;
                            int i19 = i18 * 2;
                            float f44 = iArr5[i18];
                            fArr2[i19] = f44;
                            fArr2[i19 + 1] = f44;
                        }
                        this.roundPath.reset();
                        this.roundPath.addRoundRect(this.roundRect, radii, Path.Direction.CW);
                        this.roundPath.close();
                    } else if (canvas != null) {
                        try {
                            int i20 = iArr5[0];
                            if (i20 == 0) {
                                if (z6) {
                                    rectF2 = AndroidUtilities.rectTmp;
                                    rectF2.set(this.roundRect);
                                    rectF2.inset((-((rectF.width() * 1.2f) - rectF.width())) / 2.0f, (-((rectF.height() * 1.2f) - rectF.height())) / 2.0f);
                                    paint3 = this.roundPaint;
                                } else {
                                    rectF2 = this.roundRect;
                                    paint3 = this.roundPaint;
                                }
                                canvas.drawRect(rectF2, paint3);
                            } else {
                                float f45 = i20;
                                canvas.drawRoundRect(this.roundRect, f45, f45, this.roundPaint);
                            }
                        } catch (Exception e5) {
                            e = e5;
                            if (backgroundThreadDrawHolder == null) {
                                onBitmapException(bitmapDrawable);
                            }
                            FileLog.e(e);
                            if (!z3) {
                            }
                        }
                    }
                }
            }
            if (!z3) {
                ((RLottieDrawable) drawable).skipFrameUpdate = false;
                return;
            } else {
                if (drawable instanceof AnimatedFileDrawable) {
                    ((AnimatedFileDrawable) drawable).skipFrameUpdate = false;
                    return;
                }
                return;
            }
        }
        ?? r15 = 1065353216;
        float f46 = 1.0f;
        if (backgroundThreadDrawHolder == null) {
            if (this.isAspectFit) {
                int intrinsicWidth2 = drawable.getIntrinsicWidth();
                int intrinsicHeight2 = drawable.getIntrinsicHeight();
                float f47 = this.sideClip * 2.0f;
                float f48 = f4 - f47;
                float f49 = f3 - f47;
                float f50 = f4 == 0.0f ? 1.0f : intrinsicWidth2 / f48;
                float f51 = f3 != 0.0f ? intrinsicHeight2 / f49 : 1.0f;
                float max3 = Math.max(f50, f51);
                float f52 = (int) (intrinsicWidth2 / max3);
                float f53 = (int) (intrinsicHeight2 / max3);
                rectF.set(((f4 - f52) / 2.0f) + f, ((f3 - f53) / 2.0f) + f2, f + ((f4 + f52) / 2.0f), f2 + ((f3 + f53) / 2.0f));
                f46 = f51;
            } else {
                rectF.set(f, f2, f4 + f, f3 + f2);
            }
            drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
            r15 = f46;
        }
        if (!this.isVisible || canvas == null) {
            return;
        }
        if (!(drawable instanceof SvgHelper.SvgDrawable)) {
            if (drawable instanceof ClipRoundedDrawable) {
                ClipRoundedDrawable clipRoundedDrawable = (ClipRoundedDrawable) drawable;
                if (clipRoundedDrawable.getDrawable() instanceof SvgHelper.SvgDrawable) {
                    svgDrawable = (SvgHelper.SvgDrawable) clipRoundedDrawable.getDrawable();
                }
            }
            r12 = 0;
            if (colorFilter != null && drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
            drawable.setAlpha(i);
            if (backgroundThreadDrawHolder != null || r12 == 0) {
                r15 = 0;
                drawable.draw(canvas);
            } else {
                long j = backgroundThreadDrawHolder.time;
                if (j == 0) {
                    j = System.currentTimeMillis();
                }
                r15 = 0;
                ((SvgHelper.SvgDrawable) drawable).drawInternal(canvas, true, backgroundThreadDrawHolder.threadIndex, j, backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY, backgroundThreadDrawHolder.imageW, backgroundThreadDrawHolder.imageH);
            }
            if (r12 != 0) {
                r12.setParent(r15);
                return;
            }
            return;
        }
        svgDrawable = (SvgHelper.SvgDrawable) drawable;
        svgDrawable.setParent(this);
        r12 = svgDrawable;
        if (colorFilter != null) {
            drawable.setColorFilter(colorFilter);
        }
        drawable.setAlpha(i);
        if (backgroundThreadDrawHolder != null) {
        }
        r15 = 0;
        drawable.draw(canvas);
        if (r12 != 0) {
        }
    }

    public boolean getAllowStartAnimation() {
        return this.allowStartAnimation;
    }

    public float getAlpha() {
        return this.overrideAlpha;
    }

    public int getAnimatedOrientation() {
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            return animation.getOrientation();
        }
        return 0;
    }

    public AnimatedFileDrawable getAnimation() {
        Drawable drawable = this.currentMediaDrawable;
        if (drawable instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable) drawable;
        }
        Drawable drawable2 = this.currentImageDrawable;
        if (drawable2 instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable) drawable2;
        }
        Drawable drawable3 = this.currentThumbDrawable;
        if (drawable3 instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable) drawable3;
        }
        Drawable drawable4 = this.staticThumbDrawable;
        if (drawable4 instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable) drawable4;
        }
        return null;
    }

    public int getAutoRepeat() {
        return this.autoRepeat;
    }

    public Bitmap getBitmap() {
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null && lottieAnimation.hasBitmap()) {
            return lottieAnimation.getAnimatedBitmap();
        }
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null && animation.hasBitmap()) {
            return animation.getAnimatedBitmap();
        }
        Drawable drawable = this.currentMediaDrawable;
        if ((drawable instanceof BitmapDrawable) && !(drawable instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Drawable drawable2 = this.currentImageDrawable;
        if ((drawable2 instanceof BitmapDrawable) && !(drawable2 instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
            return ((BitmapDrawable) drawable2).getBitmap();
        }
        Drawable drawable3 = this.currentThumbDrawable;
        if ((drawable3 instanceof BitmapDrawable) && !(drawable3 instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
            return ((BitmapDrawable) drawable3).getBitmap();
        }
        Drawable drawable4 = this.staticThumbDrawable;
        if (drawable4 instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable4).getBitmap();
        }
        return null;
    }

    public int getBitmapHeight() {
        getDrawable();
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            int i = this.imageOrientation % 360;
            return (i == 0 || i == 180) ? animation.getIntrinsicHeight() : animation.getIntrinsicWidth();
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            return lottieAnimation.getIntrinsicHeight();
        }
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            int i2 = this.imageOrientation % 360;
            return (i2 == 0 || i2 == 180) ? bitmap.getHeight() : bitmap.getWidth();
        }
        Drawable drawable = this.staticThumbDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BitmapHolder getBitmapSafe() {
        Bitmap bitmap;
        String str;
        AnimatedFileDrawable animation = getAnimation();
        RLottieDrawable lottieAnimation = getLottieAnimation();
        int i = 0;
        if (lottieAnimation != null && lottieAnimation.hasBitmap()) {
            bitmap = lottieAnimation.getAnimatedBitmap();
        } else {
            if (animation == null || !animation.hasBitmap()) {
                Drawable drawable = this.currentMediaDrawable;
                if (!(drawable instanceof BitmapDrawable) || (drawable instanceof AnimatedFileDrawable) || (drawable instanceof RLottieDrawable)) {
                    Drawable drawable2 = this.currentImageDrawable;
                    if (!(drawable2 instanceof BitmapDrawable) || (drawable2 instanceof AnimatedFileDrawable) || (drawable instanceof RLottieDrawable)) {
                        Drawable drawable3 = this.currentThumbDrawable;
                        if (!(drawable3 instanceof BitmapDrawable) || (drawable3 instanceof AnimatedFileDrawable) || (drawable instanceof RLottieDrawable)) {
                            Drawable drawable4 = this.staticThumbDrawable;
                            if (drawable4 instanceof BitmapDrawable) {
                                bitmap = ((BitmapDrawable) drawable4).getBitmap();
                            } else {
                                bitmap = null;
                                str = null;
                            }
                        } else {
                            bitmap = ((BitmapDrawable) drawable3).getBitmap();
                            str = this.currentThumbKey;
                        }
                    } else {
                        bitmap = ((BitmapDrawable) drawable2).getBitmap();
                        str = this.currentImageKey;
                    }
                } else {
                    bitmap = ((BitmapDrawable) drawable).getBitmap();
                    str = this.currentMediaKey;
                }
                if (bitmap == null) {
                    return new BitmapHolder(bitmap, str, i);
                }
                return null;
            }
            Bitmap animatedBitmap = animation.getAnimatedBitmap();
            i = animation.getOrientation();
            if (i != 0) {
                return new BitmapHolder(Bitmap.createBitmap(animatedBitmap), (String) null, i);
            }
            bitmap = animatedBitmap;
        }
        str = null;
        if (bitmap == null) {
        }
    }

    public int getBitmapWidth() {
        getDrawable();
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            int i = this.imageOrientation % 360;
            return (i == 0 || i == 180) ? animation.getIntrinsicWidth() : animation.getIntrinsicHeight();
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            return lottieAnimation.getIntrinsicWidth();
        }
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            int i2 = this.imageOrientation % 360;
            return (i2 == 0 || i2 == 180) ? bitmap.getWidth() : bitmap.getHeight();
        }
        Drawable drawable = this.staticThumbDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 1;
    }

    public int getCacheType() {
        return this.currentCacheType;
    }

    public float getCenterX() {
        return this.imageX + (this.imageW / 2.0f);
    }

    public float getCenterY() {
        return this.imageY + (this.imageH / 2.0f);
    }

    public int getCurrentAccount() {
        return this.currentAccount;
    }

    public float getCurrentAlpha() {
        return this.currentAlpha;
    }

    public RectF getDrawRegion() {
        return this.drawRegion;
    }

    public Drawable getDrawable() {
        Drawable drawable = this.currentMediaDrawable;
        if (drawable != null) {
            return drawable;
        }
        Drawable drawable2 = this.currentImageDrawable;
        if (drawable2 != null) {
            return drawable2;
        }
        Drawable drawable3 = this.currentThumbDrawable;
        if (drawable3 != null) {
            return drawable3;
        }
        Drawable drawable4 = this.staticThumbDrawable;
        if (drawable4 != null) {
            return drawable4;
        }
        return null;
    }

    public BitmapHolder getDrawableSafe() {
        String str;
        String str2;
        Drawable drawable = this.currentMediaDrawable;
        if (!(drawable instanceof BitmapDrawable) || (drawable instanceof AnimatedFileDrawable) || (drawable instanceof RLottieDrawable)) {
            Drawable drawable2 = this.currentImageDrawable;
            if (!(drawable2 instanceof BitmapDrawable) || (drawable2 instanceof AnimatedFileDrawable) || (drawable instanceof RLottieDrawable)) {
                drawable2 = this.currentThumbDrawable;
                if (!(drawable2 instanceof BitmapDrawable) || (drawable2 instanceof AnimatedFileDrawable) || (drawable instanceof RLottieDrawable)) {
                    drawable = this.staticThumbDrawable;
                    if (drawable instanceof BitmapDrawable) {
                        str = null;
                    } else {
                        drawable = null;
                        str = null;
                    }
                } else {
                    str2 = this.currentThumbKey;
                }
            } else {
                str2 = this.currentImageKey;
            }
            Drawable drawable3 = drawable2;
            str = str2;
            drawable = drawable3;
        } else {
            str = this.currentMediaKey;
        }
        if (drawable != null) {
            return new BitmapHolder(drawable, str, 0);
        }
        return null;
    }

    public String getExt() {
        return this.currentExt;
    }

    public int getFileLoadingPriority() {
        return this.fileLoadingPriority;
    }

    public float getImageAspectRatio() {
        float width;
        float height;
        if (this.imageOrientation % NotificationCenter.updateBotMenuButton != 0) {
            width = this.drawRegion.height();
            height = this.drawRegion.width();
        } else {
            width = this.drawRegion.width();
            height = this.drawRegion.height();
        }
        return width / height;
    }

    public Drawable getImageDrawable() {
        return this.currentImageDrawable;
    }

    public String getImageFilter() {
        return this.currentImageFilter;
    }

    public float getImageHeight() {
        return this.imageH;
    }

    public String getImageKey() {
        return this.currentImageKey;
    }

    public ImageLocation getImageLocation() {
        return this.currentImageLocation;
    }

    public float getImageWidth() {
        return this.imageW;
    }

    public float getImageX() {
        return this.imageX;
    }

    public float getImageX2() {
        return this.imageX + this.imageW;
    }

    public float getImageY() {
        return this.imageY;
    }

    public float getImageY2() {
        return this.imageY + this.imageH;
    }

    public int getInvert() {
        return this.imageInvert;
    }

    public ArrayList<Runnable> getLoadingOperations() {
        return this.loadingOperations;
    }

    public RLottieDrawable getLottieAnimation() {
        Drawable drawable = this.currentMediaDrawable;
        if (drawable instanceof RLottieDrawable) {
            return (RLottieDrawable) drawable;
        }
        Drawable drawable2 = this.currentImageDrawable;
        if (drawable2 instanceof RLottieDrawable) {
            return (RLottieDrawable) drawable2;
        }
        Drawable drawable3 = this.currentThumbDrawable;
        if (drawable3 instanceof RLottieDrawable) {
            return (RLottieDrawable) drawable3;
        }
        Drawable drawable4 = this.staticThumbDrawable;
        if (drawable4 instanceof RLottieDrawable) {
            return (RLottieDrawable) drawable4;
        }
        return null;
    }

    public Object getMark() {
        return this.mark;
    }

    public Drawable getMediaDrawable() {
        return this.currentMediaDrawable;
    }

    public String getMediaFilter() {
        return this.currentMediaFilter;
    }

    public String getMediaKey() {
        return this.currentMediaKey;
    }

    public ImageLocation getMediaLocation() {
        return this.currentMediaLocation;
    }

    public int getNewGuid() {
        int i = this.currentGuid + 1;
        this.currentGuid = i;
        return i;
    }

    public int getOrientation() {
        return this.imageOrientation;
    }

    public int getParam() {
        return this.param;
    }

    public Object getParentObject() {
        return this.currentParentObject;
    }

    public void getParentPosition(int[] iArr) {
        View view = this.parentView;
        if (view == null) {
            return;
        }
        view.getLocationInWindow(iArr);
    }

    public View getParentView() {
        return this.parentView;
    }

    public boolean getPressed() {
        return this.isPressed != 0;
    }

    public TLRPC.Document getQualityThumbDocument() {
        return this.qulityThumbDocument;
    }

    public int[] getRoundRadius() {
        return this.roundRadius;
    }

    public int[] getRoundRadius(boolean z) {
        return (this.useRoundRadius || !z) ? this.roundRadius : this.emptyRoundRadius;
    }

    public long getSize() {
        return this.currentSize;
    }

    public Drawable getStaticThumb() {
        return this.staticThumbDrawable;
    }

    public ImageLocation getStrippedLocation() {
        return this.strippedLocation;
    }

    protected int getTag(int i) {
        return i == 1 ? this.thumbTag : i == 3 ? this.mediaTag : this.imageTag;
    }

    public Drawable getThumb() {
        return this.currentThumbDrawable;
    }

    public Bitmap getThumbBitmap() {
        Drawable drawable = this.currentThumbDrawable;
        if (!(drawable instanceof BitmapDrawable)) {
            drawable = this.staticThumbDrawable;
            if (!(drawable instanceof BitmapDrawable)) {
                return null;
            }
        }
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public BitmapHolder getThumbBitmapSafe() {
        Bitmap bitmap;
        String str;
        Drawable drawable = this.currentThumbDrawable;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            str = this.currentThumbKey;
        } else {
            Drawable drawable2 = this.staticThumbDrawable;
            if (drawable2 instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable2).getBitmap();
                str = null;
            } else {
                bitmap = null;
                str = null;
            }
        }
        if (bitmap != null) {
            return new BitmapHolder(bitmap, str, 0);
        }
        return null;
    }

    public String getThumbFilter() {
        return this.currentThumbFilter;
    }

    public String getThumbKey() {
        return this.currentThumbKey;
    }

    public ImageLocation getThumbLocation() {
        return this.currentThumbLocation;
    }

    public String getUniqKeyPrefix() {
        return this.uniqKeyPrefix;
    }

    public boolean getVisible() {
        return this.isVisible;
    }

    public boolean hasBitmapImage() {
        return (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true;
    }

    public boolean hasImageLoaded() {
        return (this.currentImageDrawable == null && this.currentMediaDrawable == null) ? false : true;
    }

    public boolean hasImageSet() {
        return (this.currentImageDrawable == null && this.currentMediaDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentImageKey == null && this.currentMediaKey == null) ? false : true;
    }

    public boolean hasMediaSet() {
        return this.currentMediaDrawable != null;
    }

    public boolean hasNotThumb() {
        return (this.currentImageDrawable == null && this.currentMediaDrawable == null && !(this.staticThumbDrawable instanceof VectorAvatarThumbDrawable)) ? false : true;
    }

    public boolean hasNotThumbOrOnlyStaticThumb() {
        if (this.currentImageDrawable == null && this.currentMediaDrawable == null) {
            Drawable drawable = this.staticThumbDrawable;
            if (!(drawable instanceof VectorAvatarThumbDrawable) && (drawable == null || (drawable instanceof AvatarDrawable) || this.currentImageKey != null || this.currentMediaKey != null)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasStaticThumb() {
        return this.staticThumbDrawable != null;
    }

    public void invalidate() {
        View view = this.parentView;
        if (view == null) {
            return;
        }
        if (this.invalidateAll) {
            view.invalidate();
            return;
        }
        float f = this.imageX;
        float f2 = this.imageY;
        view.invalidate((int) f, (int) f2, (int) (f + this.imageW), (int) (f2 + this.imageH));
    }

    public boolean isAllowStartAnimation() {
        return this.allowStartAnimation;
    }

    public boolean isAnimationRunning() {
        AnimatedFileDrawable animation = getAnimation();
        return animation != null && animation.isRunning();
    }

    public boolean isAspectFit() {
        return this.isAspectFit;
    }

    public boolean isAttachedToWindow() {
        return this.attachedToWindow;
    }

    public boolean isCrossfadingWithOldImage() {
        return (!this.crossfadeWithOldImage || this.crossfadeImage == null || this.crossfadingWithThumb) ? false : true;
    }

    public boolean isCurrentKeyQuality() {
        return this.currentKeyQuality;
    }

    public boolean isForceLoding() {
        return this.forceLoding;
    }

    public boolean isForcePreview() {
        return this.forcePreview;
    }

    public boolean isInsideImage(float f, float f2) {
        float f3 = this.imageX;
        if (f >= f3 && f <= f3 + this.imageW) {
            float f4 = this.imageY;
            if (f2 >= f4 && f2 <= f4 + this.imageH) {
                return true;
            }
        }
        return false;
    }

    public boolean isNeedsQualityThumb() {
        return this.needsQualityThumb;
    }

    public boolean isShouldGenerateQualityThumb() {
        return this.shouldGenerateQualityThumb;
    }

    public void moveImageToFront() {
        ImageLoader.getInstance().moveToFront(this.currentImageKey);
        ImageLoader.getInstance().moveToFront(this.currentThumbKey);
    }

    public void moveLottieToFront() {
        BitmapDrawable bitmapDrawable;
        String str;
        Drawable drawable = this.currentMediaDrawable;
        if (drawable instanceof RLottieDrawable) {
            bitmapDrawable = (BitmapDrawable) drawable;
            str = this.currentMediaKey;
        } else {
            Drawable drawable2 = this.currentImageDrawable;
            if (drawable2 instanceof RLottieDrawable) {
                bitmapDrawable = (BitmapDrawable) drawable2;
                str = this.currentImageKey;
            } else {
                bitmapDrawable = null;
                str = null;
            }
        }
        if (str == null || bitmapDrawable == null) {
            return;
        }
        ImageLoader.getInstance().moveToFront(str);
        if (ImageLoader.getInstance().isInMemCache(str, true)) {
            return;
        }
        ImageLoader.getInstance().getLottieMemCahce().put(str, bitmapDrawable);
    }

    public boolean onAttachedToWindow() {
        if (this.attachedToWindow) {
            return false;
        }
        this.attachedToWindow = true;
        this.currentOpenedLayerFlags = NotificationCenter.getGlobalInstance().getCurrentHeavyOperationFlags() & (this.currentLayerNum ^ (-1));
        if (!this.ignoreNotifications) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReplacedPhotoInMemCache);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.stopAllHeavyOperations);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.startAllHeavyOperations);
        }
        if (setBackupImage()) {
            return true;
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            lottieAnimation.addParentView(this);
            lottieAnimation.setAllowVibration(this.allowLottieVibration);
        }
        if (lottieAnimation != null && this.allowStartLottieAnimation && (!lottieAnimation.isHeavyDrawable() || this.currentOpenedLayerFlags == 0)) {
            lottieAnimation.start();
        }
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            animation.addParent(this);
        }
        if (animation != null && this.allowStartAnimation && this.currentOpenedLayerFlags == 0) {
            animation.checkRepeat();
            invalidate();
        }
        if (NotificationCenter.getGlobalInstance().isAnimationInProgress()) {
            didReceivedNotification(NotificationCenter.stopAllHeavyOperations, this.currentAccount, 512);
        }
        Object obj = this.staticThumbDrawable;
        if (obj instanceof AttachableDrawable) {
            ((AttachableDrawable) obj).onAttachedToWindow(this);
        }
        if (this.decorators != null) {
            for (int i = 0; i < this.decorators.size(); i++) {
                this.decorators.get(i).onAttachedToWindow(this);
            }
        }
        return false;
    }

    public void onDetachedFromWindow() {
        if (this.attachedToWindow) {
            this.attachedToWindow = false;
            if (this.currentImageLocation != null || this.currentMediaLocation != null || this.currentThumbLocation != null || this.staticThumbDrawable != null) {
                if (this.setImageBackup == null) {
                    this.setImageBackup = new SetImageBackup();
                }
                SetImageBackup setImageBackup = this.setImageBackup;
                setImageBackup.mediaLocation = this.currentMediaLocation;
                setImageBackup.mediaFilter = this.currentMediaFilter;
                setImageBackup.imageLocation = this.currentImageLocation;
                setImageBackup.imageFilter = this.currentImageFilter;
                setImageBackup.thumbLocation = this.currentThumbLocation;
                setImageBackup.thumbFilter = this.currentThumbFilter;
                setImageBackup.thumb = this.staticThumbDrawable;
                setImageBackup.size = this.currentSize;
                setImageBackup.ext = this.currentExt;
                setImageBackup.cacheType = this.currentCacheType;
                setImageBackup.parentObject = this.currentParentObject;
            }
            if (!this.ignoreNotifications) {
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReplacedPhotoInMemCache);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.stopAllHeavyOperations);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.startAllHeavyOperations);
            }
            Object obj = this.staticThumbDrawable;
            if (obj instanceof AttachableDrawable) {
                ((AttachableDrawable) obj).onDetachedFromWindow(this);
            }
            if (this.staticThumbDrawable != null) {
                setStaticDrawable(null);
                this.staticThumbShader = null;
            }
            clearImage();
            this.roundPaint.setShader(null);
            if (this.isPressed == 0) {
                this.pressedProgress = 0.0f;
            }
            AnimatedFileDrawable animation = getAnimation();
            if (animation != null) {
                animation.removeParent(this);
            }
            RLottieDrawable lottieAnimation = getLottieAnimation();
            if (lottieAnimation != null) {
                lottieAnimation.removeParentView(this);
            }
            if (this.decorators != null) {
                for (int i = 0; i < this.decorators.size(); i++) {
                    this.decorators.get(i).onDetachedFromWidnow();
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0095, code lost:
    
        if (r8 != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a8, code lost:
    
        r4.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00a6, code lost:
    
        if (r4.getParents().isEmpty() != false) goto L47;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void recycleBitmap(String str, int i) {
        String str2;
        Drawable drawable;
        String replacedKey;
        if (i == 3) {
            str2 = this.currentMediaKey;
            drawable = this.currentMediaDrawable;
        } else if (i == 2) {
            str2 = this.crossfadeKey;
            drawable = this.crossfadeImage;
        } else if (i == 1) {
            str2 = this.currentThumbKey;
            drawable = this.currentThumbDrawable;
        } else {
            str2 = this.currentImageKey;
            drawable = this.currentImageDrawable;
        }
        if (str2 != null && ((str2.startsWith("-") || str2.startsWith("strippedmessage-")) && (replacedKey = ImageLoader.getInstance().getReplacedKey(str2)) != null)) {
            str2 = replacedKey;
        }
        if (drawable instanceof RLottieDrawable) {
            ((RLottieDrawable) drawable).removeParentView(this);
        }
        if (drawable instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) drawable).removeParent(this);
        }
        if (str2 != null && ((str == null || !str.equals(str2)) && drawable != null)) {
            if (drawable instanceof RLottieDrawable) {
                RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                boolean decrementUseCount = ImageLoader.getInstance().decrementUseCount(str2);
                if (!ImageLoader.getInstance().isInMemCache(str2, true) && decrementUseCount) {
                    rLottieDrawable.recycle(false);
                }
            } else if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                if (animatedFileDrawable.isWebmSticker) {
                    boolean decrementUseCount2 = ImageLoader.getInstance().decrementUseCount(str2);
                    if (ImageLoader.getInstance().isInMemCache(str2, true)) {
                        if (decrementUseCount2) {
                            animatedFileDrawable.stop();
                        }
                    }
                }
            } else if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                boolean decrementUseCount3 = ImageLoader.getInstance().decrementUseCount(str2);
                if (!ImageLoader.getInstance().isInMemCache(str2, false) && decrementUseCount3) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(bitmap);
                    AndroidUtilities.recycleBitmaps(arrayList);
                }
            }
        }
        if (i == 3) {
            this.currentMediaKey = null;
            this.currentMediaDrawable = null;
            this.mediaShader = null;
        } else if (i == 2) {
            this.crossfadeKey = null;
            this.crossfadeImage = null;
            this.crossfadeShader = null;
        } else if (i == 1) {
            this.currentThumbDrawable = null;
            this.currentThumbKey = null;
            this.thumbShader = null;
        } else {
            this.currentImageDrawable = null;
            this.currentImageKey = null;
            this.imageShader = null;
        }
    }

    public void setAllowDecodeSingleFrame(boolean z) {
        this.allowDecodeSingleFrame = z;
    }

    public void setAllowDrawWhileCacheGenerating(boolean z) {
        this.allowDrawWhileCacheGenerating = z;
    }

    public void setAllowLoadingOnAttachedOnly(boolean z) {
        this.allowLoadingOnAttachedOnly = z;
    }

    public void setAllowLottieVibration(boolean z) {
        this.allowLottieVibration = z;
    }

    public void setAllowStartAnimation(boolean z) {
        this.allowStartAnimation = z;
    }

    public void setAllowStartLottieAnimation(boolean z) {
        this.allowStartLottieAnimation = z;
    }

    public void setAlpha(float f) {
        this.overrideAlpha = f;
    }

    public void setAspectFit(boolean z) {
        this.isAspectFit = z;
    }

    public void setAutoRepeat(int i) {
        this.autoRepeat = i;
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            lottieAnimation.setAutoRepeat(i);
        }
    }

    public void setAutoRepeatCount(int i) {
        this.autoRepeatCount = i;
        if (getLottieAnimation() != null) {
            getLottieAnimation().setAutoRepeatCount(i);
            return;
        }
        this.animatedFileDrawableRepeatMaxCount = i;
        if (getAnimation() != null) {
            getAnimation().repeatCount = 0;
        }
    }

    public void setAutoRepeatTimeout(long j) {
        this.autoRepeatTimeout = j;
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            lottieAnimation.setAutoRepeatTimeout(this.autoRepeatTimeout);
        }
    }

    public boolean setBackupImage() {
        SetImageBackup setImageBackup = this.setImageBackup;
        if (setImageBackup == null || !setImageBackup.isSet()) {
            return false;
        }
        SetImageBackup setImageBackup2 = this.setImageBackup;
        this.setImageBackup = null;
        Drawable drawable = setImageBackup2.thumb;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (!(bitmapDrawable instanceof RLottieDrawable) && !(bitmapDrawable instanceof AnimatedFileDrawable) && bitmapDrawable.getBitmap() != null && bitmapDrawable.getBitmap().isRecycled()) {
                setImageBackup2.thumb = null;
            }
        }
        setImage(setImageBackup2.mediaLocation, setImageBackup2.mediaFilter, setImageBackup2.imageLocation, setImageBackup2.imageFilter, setImageBackup2.thumbLocation, setImageBackup2.thumbFilter, setImageBackup2.thumb, setImageBackup2.size, setImageBackup2.ext, setImageBackup2.parentObject, setImageBackup2.cacheType);
        setImageBackup2.clear();
        this.setImageBackup = setImageBackup2;
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            lottieAnimation.setAllowVibration(this.allowLottieVibration);
        }
        if (lottieAnimation == null || !this.allowStartLottieAnimation) {
            return true;
        }
        if (lottieAnimation.isHeavyDrawable() && this.currentOpenedLayerFlags != 0) {
            return true;
        }
        lottieAnimation.start();
        return true;
    }

    public void setBlendMode(Object obj) {
        this.blendMode = obj;
        invalidate();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
    }

    public void setCrossfadeAlpha(byte b) {
        this.crossfadeAlpha = b;
    }

    public void setCrossfadeByScale(float f) {
        this.crossfadeByScale = f;
    }

    public void setCrossfadeDuration(int i) {
        this.crossfadeDuration = i;
    }

    public void setCrossfadeWithOldImage(boolean z) {
        this.crossfadeWithOldImage = z;
    }

    public void setCurrentAccount(int i) {
        this.currentAccount = i;
    }

    public void setCurrentAlpha(float f) {
        this.currentAlpha = f;
    }

    public void setCurrentTime(long j) {
        this.currentTime = j;
    }

    public void setDelegate(ImageReceiverDelegate imageReceiverDelegate) {
        this.delegate = imageReceiverDelegate;
    }

    public BackgroundThreadDrawHolder setDrawInBackgroundThread(BackgroundThreadDrawHolder backgroundThreadDrawHolder, int i) {
        if (backgroundThreadDrawHolder == null) {
            backgroundThreadDrawHolder = new BackgroundThreadDrawHolder();
        }
        backgroundThreadDrawHolder.threadIndex = i;
        backgroundThreadDrawHolder.animation = getAnimation();
        backgroundThreadDrawHolder.lottieDrawable = getLottieAnimation();
        boolean z = false;
        for (int i2 = 0; i2 < 4; i2++) {
            backgroundThreadDrawHolder.roundRadius[i2] = this.roundRadius[i2];
        }
        backgroundThreadDrawHolder.mediaDrawable = this.currentMediaDrawable;
        backgroundThreadDrawHolder.mediaShader = this.mediaShader;
        backgroundThreadDrawHolder.imageDrawable = this.currentImageDrawable;
        backgroundThreadDrawHolder.imageShader = this.imageShader;
        backgroundThreadDrawHolder.thumbDrawable = this.currentThumbDrawable;
        backgroundThreadDrawHolder.thumbShader = this.thumbShader;
        backgroundThreadDrawHolder.staticThumbShader = this.staticThumbShader;
        backgroundThreadDrawHolder.staticThumbDrawable = this.staticThumbDrawable;
        backgroundThreadDrawHolder.crossfadeImage = this.crossfadeImage;
        backgroundThreadDrawHolder.colorFilter = this.colorFilter;
        backgroundThreadDrawHolder.crossfadingWithThumb = this.crossfadingWithThumb;
        backgroundThreadDrawHolder.crossfadeWithOldImage = this.crossfadeWithOldImage;
        backgroundThreadDrawHolder.currentAlpha = this.currentAlpha;
        backgroundThreadDrawHolder.previousAlpha = this.previousAlpha;
        backgroundThreadDrawHolder.crossfadeShader = this.crossfadeShader;
        if ((backgroundThreadDrawHolder.animation != null && !backgroundThreadDrawHolder.animation.hasBitmap()) || (backgroundThreadDrawHolder.lottieDrawable != null && !backgroundThreadDrawHolder.lottieDrawable.hasBitmap())) {
            z = true;
        }
        backgroundThreadDrawHolder.animationNotReady = z;
        backgroundThreadDrawHolder.imageX = this.imageX;
        backgroundThreadDrawHolder.imageY = this.imageY;
        backgroundThreadDrawHolder.imageW = this.imageW;
        backgroundThreadDrawHolder.imageH = this.imageH;
        backgroundThreadDrawHolder.overrideAlpha = this.overrideAlpha;
        return backgroundThreadDrawHolder;
    }

    public void setEmojiPaused(boolean z) {
        if (this.emojiPaused == z) {
            return;
        }
        this.emojiPaused = z;
        this.allowStartLottieAnimation = !z;
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            if (z) {
                lottieAnimation.stop();
            } else {
                if (lottieAnimation.isRunning()) {
                    return;
                }
                lottieAnimation.start();
            }
        }
    }

    public void setFileLoadingPriority(int i) {
        if (this.fileLoadingPriority != i) {
            this.fileLoadingPriority = i;
            if (this.attachedToWindow && hasImageSet()) {
                ImageLoader.getInstance().changeFileLoadingPriorityForImageReceiver(this);
            }
        }
    }

    public void setForUserOrChat(TLObject tLObject, Drawable drawable) {
        setForUserOrChat(tLObject, drawable, null);
    }

    public void setForUserOrChat(TLObject tLObject, Drawable drawable, Object obj) {
        setForUserOrChat(tLObject, drawable, obj, false, 0, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0084  */
    /* JADX WARN: Type inference failed for: r3v10, types: [org.telegram.tgnet.TLRPC$VideoSize] */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* JADX WARN: Type inference failed for: r3v9 */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v15 */
    /* JADX WARN: Type inference failed for: r9v16 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3, types: [org.telegram.tgnet.TLRPC$VideoSize] */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setForUserOrChat(TLObject tLObject, Drawable drawable, Object obj, boolean z, int i, boolean z2) {
        Object obj2;
        BitmapDrawable bitmapDrawable;
        boolean z3;
        boolean z4;
        TLRPC.ChatPhoto chatPhoto;
        ?? r3;
        ImageLocation forUserOrChat;
        String str;
        String str2;
        int i2;
        ImageReceiver imageReceiver;
        ImageLocation imageLocation;
        String str3;
        Drawable drawable2;
        ImageLocation imageLocation2;
        ?? r9;
        TLRPC.UserFull userFull;
        ArrayList<TLRPC.VideoSize> arrayList;
        TLRPC.UserFull userFull2;
        Object obj3 = obj == null ? tLObject : obj;
        setUseRoundForThumbDrawable(true);
        ImageLocation imageLocation3 = null;
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            z3 = user.premium;
            TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
            if (userProfilePhoto != null) {
                bitmapDrawable = userProfilePhoto.strippedBitmap;
                z4 = userProfilePhoto.stripped_thumb != null;
                if (i == 3 && (userFull2 = MessagesController.getInstance(this.currentAccount).getUserFull(user.id)) != null) {
                    TLRPC.Photo photo = user.photo.personal ? userFull2.personal_photo : userFull2.profile_photo;
                    if (photo != null) {
                        r9 = FileLoader.getVectorMarkupVideoSize(photo);
                        if (r9 == 0 && z && MessagesController.getInstance(this.currentAccount).isPremiumUser(user) && user.photo.has_video && LiteMode.isEnabled(1024)) {
                            userFull = MessagesController.getInstance(this.currentAccount).getUserFull(user.id);
                            if (userFull != null) {
                                MessagesController.getInstance(this.currentAccount).loadFullUser(user, this.currentGuid, false);
                            } else {
                                TLRPC.Photo photo2 = user.photo.personal ? userFull.personal_photo : userFull.profile_photo;
                                if (photo2 != null && (r9 = FileLoader.getVectorMarkupVideoSize(photo2)) == 0 && (arrayList = photo2.video_sizes) != null && !arrayList.isEmpty()) {
                                    TLRPC.VideoSize closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(arrayList, 100);
                                    int i3 = 0;
                                    r9 = r9;
                                    while (i3 < arrayList.size()) {
                                        TLRPC.VideoSize videoSize = arrayList.get(i3);
                                        if ("p".equals(videoSize.type)) {
                                            closestVideoSizeWithSize = videoSize;
                                        }
                                        if ((videoSize instanceof TLRPC.TL_videoSizeEmojiMarkup) || (videoSize instanceof TLRPC.TL_videoSizeStickerMarkup)) {
                                            r9 = videoSize;
                                        }
                                        i3++;
                                        r9 = r9;
                                    }
                                    imageLocation3 = ImageLocation.getForPhoto(closestVideoSizeWithSize, photo2);
                                }
                            }
                        }
                        imageLocation2 = imageLocation3;
                        imageLocation3 = r9;
                    }
                }
                r9 = 0;
                if (r9 == 0) {
                    userFull = MessagesController.getInstance(this.currentAccount).getUserFull(user.id);
                    if (userFull != null) {
                    }
                }
                imageLocation2 = imageLocation3;
                imageLocation3 = r9;
            } else {
                imageLocation2 = null;
                bitmapDrawable = null;
                z4 = false;
            }
            ImageLocation imageLocation4 = imageLocation3;
            imageLocation3 = imageLocation2;
            r3 = imageLocation4;
        } else {
            if (!(tLObject instanceof TLRPC.Chat) || (chatPhoto = ((TLRPC.Chat) tLObject).photo) == null) {
                obj2 = null;
                bitmapDrawable = null;
            } else {
                BitmapDrawable bitmapDrawable2 = chatPhoto.strippedBitmap;
                if (chatPhoto.stripped_thumb != null) {
                    r3 = 0;
                    bitmapDrawable = bitmapDrawable2;
                    z3 = false;
                    z4 = true;
                } else {
                    obj2 = null;
                    bitmapDrawable = bitmapDrawable2;
                }
            }
            z3 = false;
            z4 = false;
            r3 = obj2;
        }
        if (r3 != 0 && i != 0) {
            setImageBitmap(new VectorAvatarThumbDrawable(r3, z3, i));
            return;
        }
        if (z2) {
            forUserOrChat = ImageLocation.getForUserOrChat(tLObject, 0);
            str = "100_100";
        } else {
            forUserOrChat = ImageLocation.getForUserOrChat(tLObject, 1);
            str = "50_50";
        }
        ImageLocation imageLocation5 = forUserOrChat;
        String str4 = str;
        if (imageLocation3 != null) {
            setImage(imageLocation3, "avatar", imageLocation5, str4, null, null, bitmapDrawable, 0L, null, obj3, 0);
            this.animatedFileDrawableRepeatMaxCount = 3;
            return;
        }
        if (bitmapDrawable != null) {
            str2 = null;
            i2 = 0;
            imageReceiver = this;
            imageLocation = imageLocation5;
            str3 = str4;
            drawable2 = bitmapDrawable;
        } else {
            if (z4) {
                setImage(imageLocation5, str4, ImageLocation.getForUserOrChat(tLObject, 2), "50_50_b", drawable, obj3, 0);
                return;
            }
            str2 = null;
            i2 = 0;
            imageReceiver = this;
            imageLocation = imageLocation5;
            str3 = str4;
            drawable2 = drawable;
        }
        imageReceiver.setImage(imageLocation, str3, drawable2, str2, obj3, i2);
    }

    public void setForceCrossfade(boolean z) {
        this.forceCrossfade = z;
    }

    public void setForceLoading(boolean z) {
        this.forceLoding = z;
    }

    public void setForcePreview(boolean z) {
        this.forcePreview = z;
    }

    public void setGradientBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.gradientShader == null || this.gradientBitmap != bitmap) {
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                this.gradientShader = new BitmapShader(bitmap, tileMode, tileMode);
                updateDrawableRadius(this.currentImageDrawable);
            }
            this.isRoundRect = true;
        } else {
            this.gradientShader = null;
            this.composeShader = null;
            this.legacyShader = null;
            this.legacyCanvas = null;
            Bitmap bitmap2 = this.legacyBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.legacyBitmap = null;
            }
        }
        this.gradientBitmap = bitmap;
    }

    public void setIgnoreImageSet(boolean z) {
        this.ignoreImageSet = z;
    }

    public void setImage(String str, String str2, Drawable drawable, String str3, long j) {
        setImage(ImageLocation.getForPath(str), str2, null, null, drawable, j, str3, null, 1);
    }

    public void setImage(ImageLocation imageLocation, String str, Drawable drawable, long j, String str2, Object obj, int i) {
        setImage(imageLocation, str, null, null, drawable, j, str2, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, Drawable drawable, String str2, Object obj, int i) {
        setImage(imageLocation, str, null, null, drawable, 0L, str2, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, long j, String str3, Object obj, int i) {
        setImage(imageLocation, str, imageLocation2, str2, null, j, str3, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, Drawable drawable, long j, String str3, Object obj, int i) {
        setImage(null, null, imageLocation, str, imageLocation2, str2, drawable, j, str3, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, Drawable drawable, Object obj, int i) {
        setImage(null, null, imageLocation, str, imageLocation2, str2, drawable, 0L, null, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, String str3, Object obj, int i) {
        setImage(imageLocation, str, imageLocation2, str2, null, 0L, str3, obj, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:152:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0366  */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v3, types: [android.graphics.BitmapShader, java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, ImageLocation imageLocation3, String str3, Drawable drawable, long j, String str4, Object obj, int i) {
        String str5;
        ?? r8;
        int i2;
        Bitmap bitmap;
        ImageReceiverDelegate imageReceiverDelegate;
        String str6;
        SetImageBackup setImageBackup;
        ImageLocation imageLocation4 = imageLocation;
        ImageLocation imageLocation5 = imageLocation2;
        if (this.allowLoadingOnAttachedOnly && !this.attachedToWindow) {
            if (this.setImageBackup == null) {
                this.setImageBackup = new SetImageBackup();
            }
            SetImageBackup setImageBackup2 = this.setImageBackup;
            setImageBackup2.mediaLocation = imageLocation4;
            setImageBackup2.mediaFilter = str;
            setImageBackup2.imageLocation = imageLocation5;
            setImageBackup2.imageFilter = str2;
            setImageBackup2.thumbLocation = imageLocation3;
            setImageBackup2.thumbFilter = str3;
            setImageBackup2.thumb = drawable;
            setImageBackup2.size = j;
            setImageBackup2.ext = str4;
            setImageBackup2.cacheType = i;
            setImageBackup2.parentObject = obj;
            return;
        }
        if (this.ignoreImageSet) {
            return;
        }
        if (this.crossfadeWithOldImage && (setImageBackup = this.setImageBackup) != null && setImageBackup.isWebfileSet()) {
            setBackupImage();
        }
        SetImageBackup setImageBackup3 = this.setImageBackup;
        if (setImageBackup3 != null) {
            setImageBackup3.clear();
        }
        if (imageLocation5 == null && imageLocation3 == null && imageLocation4 == null) {
            for (int i3 = 0; i3 < 4; i3++) {
                recycleBitmap(null, i3);
            }
            this.currentImageLocation = null;
            this.currentImageFilter = null;
            this.currentImageKey = null;
            this.currentMediaLocation = null;
            this.currentMediaFilter = null;
            this.currentMediaKey = null;
            this.currentThumbLocation = null;
            this.currentThumbFilter = null;
            this.currentThumbKey = null;
            this.currentMediaDrawable = null;
            this.mediaShader = null;
            this.currentImageDrawable = null;
            this.imageShader = null;
            this.composeShader = null;
            this.thumbShader = null;
            this.crossfadeShader = null;
            this.legacyShader = null;
            this.legacyCanvas = null;
            Bitmap bitmap2 = this.legacyBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.legacyBitmap = null;
            }
            this.currentExt = str4;
            this.currentParentObject = null;
            this.currentCacheType = 0;
            this.roundPaint.setShader(null);
            setStaticDrawable(drawable);
            this.currentAlpha = 1.0f;
            this.previousAlpha = 1.0f;
            this.currentSize = 0L;
            updateDrawableRadius(this.staticThumbDrawable);
            ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
            invalidate();
            ImageReceiverDelegate imageReceiverDelegate2 = this.delegate;
            if (imageReceiverDelegate2 != null) {
                Drawable drawable2 = this.currentImageDrawable;
                imageReceiverDelegate2.didSetImage(this, (drawable2 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable2 == null && this.currentMediaDrawable == null, false);
                return;
            }
            return;
        }
        String key = imageLocation5 != null ? imageLocation5.getKey(obj, null, false) : null;
        if (key == null && imageLocation5 != null) {
            imageLocation5 = null;
        }
        this.animatedFileDrawableRepeatMaxCount = Math.max(this.autoRepeatCount, 0);
        this.currentKeyQuality = false;
        if (key == null && this.needsQualityThumb && ((obj instanceof MessageObject) || this.qulityThumbDocument != null)) {
            TLRPC.Document document = this.qulityThumbDocument;
            if (document == null) {
                document = ((MessageObject) obj).getDocument();
            }
            if (document != null && document.dc_id != 0 && document.id != 0) {
                key = "q_" + document.dc_id + "_" + document.id;
                this.currentKeyQuality = true;
            }
        }
        String str7 = key;
        if (str7 != null && str2 != null) {
            str7 = str7 + "@" + str2;
        }
        if (this.uniqKeyPrefix != null) {
            str7 = this.uniqKeyPrefix + str7;
        }
        String key2 = imageLocation4 != null ? imageLocation4.getKey(obj, null, false) : null;
        if (key2 == null && imageLocation4 != null) {
            imageLocation4 = null;
        }
        if (key2 != null && str != null) {
            key2 = key2 + "@" + str;
        }
        if (this.uniqKeyPrefix != null) {
            key2 = this.uniqKeyPrefix + key2;
        }
        if ((key2 == null && (str6 = this.currentImageKey) != null && str6.equals(str7)) || ((str5 = this.currentMediaKey) != null && str5.equals(key2))) {
            ImageReceiverDelegate imageReceiverDelegate3 = this.delegate;
            if (imageReceiverDelegate3 != null) {
                Drawable drawable3 = this.currentImageDrawable;
                imageReceiverDelegate3.didSetImage(this, (drawable3 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable3 == null && this.currentMediaDrawable == null, false);
            }
            if (!this.canceledLoading) {
                return;
            }
        }
        ImageLocation imageLocation6 = this.strippedLocation;
        if (imageLocation6 == null) {
            imageLocation6 = imageLocation4 != null ? imageLocation4 : imageLocation5;
        }
        if (imageLocation6 == null) {
            imageLocation6 = imageLocation3;
        }
        String key3 = imageLocation3 != null ? imageLocation3.getKey(obj, imageLocation6, false) : null;
        if (key3 != null && str3 != null) {
            key3 = key3 + "@" + str3;
        }
        if (this.crossfadeWithOldImage) {
            Object obj2 = this.currentParentObject;
            if (obj2 instanceof MessageObject) {
                MessageObject messageObject = (MessageObject) obj2;
                if (messageObject.lastGeoWebFileSet != null && (MessageObject.getMedia(messageObject) instanceof TLRPC.TL_messageMediaGeoLive)) {
                    MessageObject messageObject2 = (MessageObject) this.currentParentObject;
                    messageObject2.lastGeoWebFileLoaded = messageObject2.lastGeoWebFileSet;
                }
            }
            Drawable drawable4 = this.currentMediaDrawable;
            if (drawable4 != null) {
                if (drawable4 instanceof AnimatedFileDrawable) {
                    ((AnimatedFileDrawable) drawable4).stop();
                    ((AnimatedFileDrawable) this.currentMediaDrawable).removeParent(this);
                }
                recycleBitmap(key3, 1);
                recycleBitmap(null, 2);
                recycleBitmap(key2, 0);
                this.crossfadeImage = this.currentMediaDrawable;
                this.crossfadeShader = this.mediaShader;
                this.crossfadeKey = this.currentImageKey;
                this.crossfadingWithThumb = false;
                this.currentMediaDrawable = null;
                this.currentMediaKey = null;
            } else {
                r8 = 0;
                if (this.currentImageDrawable != null) {
                    recycleBitmap(key3, 1);
                    recycleBitmap(null, 2);
                    recycleBitmap(key2, 3);
                    this.crossfadeShader = this.imageShader;
                    this.crossfadeImage = this.currentImageDrawable;
                    this.crossfadeKey = this.currentImageKey;
                    this.crossfadingWithThumb = false;
                    this.currentImageDrawable = null;
                    this.currentImageKey = null;
                } else {
                    i2 = 0;
                    if (this.currentThumbDrawable != null) {
                        recycleBitmap(str7, 0);
                        recycleBitmap(null, 2);
                        recycleBitmap(key2, 3);
                        this.crossfadeShader = this.thumbShader;
                        this.crossfadeImage = this.currentThumbDrawable;
                        this.crossfadeKey = this.currentThumbKey;
                        this.crossfadingWithThumb = false;
                    } else if (this.staticThumbDrawable != null) {
                        recycleBitmap(str7, 0);
                        recycleBitmap(key3, 1);
                        recycleBitmap(null, 2);
                        recycleBitmap(key2, 3);
                        this.crossfadeShader = this.staticThumbShader;
                        this.crossfadeImage = this.staticThumbDrawable;
                        this.crossfadingWithThumb = false;
                        this.crossfadeKey = null;
                    }
                    this.currentThumbDrawable = null;
                    this.currentThumbKey = null;
                }
            }
            this.currentImageLocation = imageLocation5;
            this.currentImageFilter = str2;
            this.currentImageKey = str7;
            this.currentMediaLocation = imageLocation4;
            this.currentMediaFilter = str;
            this.currentMediaKey = key2;
            this.currentThumbLocation = imageLocation3;
            this.currentThumbFilter = str3;
            this.currentThumbKey = key3;
            this.currentParentObject = obj;
            this.currentExt = str4;
            this.currentSize = j;
            this.currentCacheType = i;
            setStaticDrawable(drawable);
            this.imageShader = null;
            this.composeShader = null;
            this.thumbShader = null;
            this.staticThumbShader = null;
            this.mediaShader = null;
            this.legacyShader = null;
            this.legacyCanvas = null;
            this.roundPaint.setShader(null);
            bitmap = this.legacyBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.legacyBitmap = null;
            }
            this.currentAlpha = 1.0f;
            this.previousAlpha = 1.0f;
            updateDrawableRadius(this.staticThumbDrawable);
            imageReceiverDelegate = this.delegate;
            if (imageReceiverDelegate != null) {
                Drawable drawable5 = this.currentImageDrawable;
                imageReceiverDelegate.didSetImage(this, (drawable5 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable5 == null && this.currentMediaDrawable == null, false);
            }
            loadImage();
            this.isRoundVideo = !(obj instanceof MessageObject) && ((MessageObject) obj).isRoundVideo();
        }
        r8 = 0;
        i2 = 0;
        recycleBitmap(str7, i2);
        recycleBitmap(key3, 1);
        recycleBitmap(r8, 2);
        recycleBitmap(key2, 3);
        this.crossfadeShader = r8;
        this.currentImageLocation = imageLocation5;
        this.currentImageFilter = str2;
        this.currentImageKey = str7;
        this.currentMediaLocation = imageLocation4;
        this.currentMediaFilter = str;
        this.currentMediaKey = key2;
        this.currentThumbLocation = imageLocation3;
        this.currentThumbFilter = str3;
        this.currentThumbKey = key3;
        this.currentParentObject = obj;
        this.currentExt = str4;
        this.currentSize = j;
        this.currentCacheType = i;
        setStaticDrawable(drawable);
        this.imageShader = null;
        this.composeShader = null;
        this.thumbShader = null;
        this.staticThumbShader = null;
        this.mediaShader = null;
        this.legacyShader = null;
        this.legacyCanvas = null;
        this.roundPaint.setShader(null);
        bitmap = this.legacyBitmap;
        if (bitmap != null) {
        }
        this.currentAlpha = 1.0f;
        this.previousAlpha = 1.0f;
        updateDrawableRadius(this.staticThumbDrawable);
        imageReceiverDelegate = this.delegate;
        if (imageReceiverDelegate != null) {
        }
        loadImage();
        this.isRoundVideo = !(obj instanceof MessageObject) && ((MessageObject) obj).isRoundVideo();
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap != null ? new BitmapDrawable((Resources) null, bitmap) : null);
    }

    public void setImageBitmap(Drawable drawable) {
        String str;
        boolean z = true;
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
        if (this.crossfadeWithOldImage) {
            if (this.currentImageDrawable != null) {
                recycleBitmap(null, 1);
                recycleBitmap(null, 2);
                recycleBitmap(null, 3);
                this.crossfadeShader = this.imageShader;
                this.crossfadeImage = this.currentImageDrawable;
                str = this.currentImageKey;
            } else if (this.currentThumbDrawable != null) {
                recycleBitmap(null, 0);
                recycleBitmap(null, 2);
                recycleBitmap(null, 3);
                this.crossfadeShader = this.thumbShader;
                this.crossfadeImage = this.currentThumbDrawable;
                str = this.currentThumbKey;
            } else if (this.staticThumbDrawable != null) {
                recycleBitmap(null, 0);
                recycleBitmap(null, 1);
                recycleBitmap(null, 2);
                recycleBitmap(null, 3);
                this.crossfadeShader = this.staticThumbShader;
                this.crossfadeImage = this.staticThumbDrawable;
                this.crossfadingWithThumb = true;
                this.crossfadeKey = null;
            } else {
                for (int i = 0; i < 4; i++) {
                    recycleBitmap(null, i);
                }
                this.crossfadeShader = null;
            }
            this.crossfadeKey = str;
            this.crossfadingWithThumb = true;
        } else {
            for (int i2 = 0; i2 < 4; i2++) {
                recycleBitmap(null, i2);
            }
        }
        Drawable drawable2 = this.staticThumbDrawable;
        if (drawable2 instanceof RecyclableDrawable) {
            ((RecyclableDrawable) drawable2).recycle();
        }
        if (drawable instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
            animatedFileDrawable.setParentView(this.parentView);
            if (this.attachedToWindow) {
                animatedFileDrawable.addParent(this);
            }
            animatedFileDrawable.setUseSharedQueue(this.useSharedAnimationQueue || animatedFileDrawable.isWebmSticker);
            if (this.allowStartAnimation && this.currentOpenedLayerFlags == 0) {
                animatedFileDrawable.checkRepeat();
            }
            animatedFileDrawable.setAllowDecodeSingleFrame(this.allowDecodeSingleFrame);
        } else if (drawable instanceof RLottieDrawable) {
            RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
            if (this.attachedToWindow) {
                rLottieDrawable.addParentView(this);
            }
            if (rLottieDrawable != null) {
                rLottieDrawable.setAllowVibration(this.allowLottieVibration);
            }
            if (this.allowStartLottieAnimation && (!rLottieDrawable.isHeavyDrawable() || this.currentOpenedLayerFlags == 0)) {
                rLottieDrawable.start();
            }
            rLottieDrawable.setAllowDecodeSingleFrame(true);
        }
        this.staticThumbShader = null;
        this.thumbShader = null;
        this.roundPaint.setShader(null);
        setStaticDrawable(drawable);
        updateDrawableRadius(drawable);
        this.currentMediaLocation = null;
        this.currentMediaFilter = null;
        Drawable drawable3 = this.currentMediaDrawable;
        if (drawable3 instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) drawable3).removeParent(this);
        }
        this.currentMediaDrawable = null;
        this.currentMediaKey = null;
        this.mediaShader = null;
        this.currentImageLocation = null;
        this.currentImageFilter = null;
        this.currentImageDrawable = null;
        this.currentImageKey = null;
        this.imageShader = null;
        this.composeShader = null;
        this.legacyShader = null;
        this.legacyCanvas = null;
        Bitmap bitmap = this.legacyBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.legacyBitmap = null;
        }
        this.currentThumbLocation = null;
        this.currentThumbFilter = null;
        this.currentThumbKey = null;
        this.currentKeyQuality = false;
        this.currentExt = null;
        this.currentSize = 0L;
        this.currentCacheType = 0;
        this.currentAlpha = 1.0f;
        this.previousAlpha = 1.0f;
        SetImageBackup setImageBackup = this.setImageBackup;
        if (setImageBackup != null) {
            setImageBackup.clear();
        }
        ImageReceiverDelegate imageReceiverDelegate = this.delegate;
        if (imageReceiverDelegate != null) {
            imageReceiverDelegate.didSetImage(this, (this.currentThumbDrawable == null && this.staticThumbDrawable == null) ? false : true, true, false);
        }
        invalidate();
        if (this.forceCrossfade && this.crossfadeWithOldImage && this.crossfadeImage != null) {
            this.currentAlpha = 0.0f;
            this.lastUpdateAlphaTime = System.currentTimeMillis();
            if (this.currentThumbDrawable == null && this.staticThumbDrawable == null) {
                z = false;
            }
            this.crossfadeWithThumb = z;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:113:0x00eb, code lost:
    
        if (r7.staticThumbDrawable == null) goto L176;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0217, code lost:
    
        r9 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0189, code lost:
    
        if (r7.staticThumbDrawable == null) goto L176;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0213, code lost:
    
        if (r7.staticThumbDrawable != null) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x00be, code lost:
    
        if ((r9 instanceof org.telegram.messenger.Emoji.EmojiDrawable) == false) goto L179;
     */
    /* JADX WARN: Removed duplicated region for block: B:110:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
        Drawable drawable2;
        Drawable drawable3;
        boolean z2;
        boolean z3;
        Drawable drawable4;
        Drawable drawable5;
        Drawable drawable6;
        if (drawable == null || str == null || this.currentGuid != i2) {
            return false;
        }
        if (i == 0) {
            if (!str.equals(this.currentImageKey)) {
                return false;
            }
            ImageReceiverDelegate imageReceiverDelegate = this.delegate;
            if (imageReceiverDelegate != null) {
                imageReceiverDelegate.didSetImageBitmap(i, str, drawable);
            }
            if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                animatedFileDrawable.setStartEndTime(this.startTime, this.endTime);
                if (animatedFileDrawable.isWebmSticker) {
                    ImageLoader.getInstance().incrementUseCount(this.currentImageKey);
                }
                if (this.videoThumbIsSame) {
                    z3 = !animatedFileDrawable.hasBitmap();
                    this.currentImageDrawable = drawable;
                    if (drawable instanceof ExtendedBitmapDrawable) {
                    }
                    updateDrawableRadius(drawable);
                    if (z3) {
                    }
                    this.currentAlpha = 1.0f;
                    this.previousAlpha = 1.0f;
                }
                z3 = true;
                this.currentImageDrawable = drawable;
                if (drawable instanceof ExtendedBitmapDrawable) {
                }
                updateDrawableRadius(drawable);
                if (z3) {
                }
                this.currentAlpha = 1.0f;
                this.previousAlpha = 1.0f;
            } else {
                ImageLoader.getInstance().incrementUseCount(this.currentImageKey);
                if (this.videoThumbIsSame && (drawable == this.currentImageDrawable || this.currentAlpha < 1.0f)) {
                    z3 = false;
                    this.currentImageDrawable = drawable;
                    if (drawable instanceof ExtendedBitmapDrawable) {
                        ExtendedBitmapDrawable extendedBitmapDrawable = (ExtendedBitmapDrawable) drawable;
                        this.imageOrientation = extendedBitmapDrawable.getOrientation();
                        this.imageInvert = extendedBitmapDrawable.getInvert();
                    }
                    updateDrawableRadius(drawable);
                    if (z3 && this.isVisible && (((!z && !this.forcePreview) || this.forceCrossfade) && this.crossfadeDuration != 0)) {
                        drawable4 = this.currentMediaDrawable;
                        if ((drawable4 instanceof RLottieDrawable) || !((RLottieDrawable) drawable4).hasBitmap()) {
                            drawable5 = this.currentMediaDrawable;
                            if ((drawable5 instanceof AnimatedFileDrawable) || !((AnimatedFileDrawable) drawable5).hasBitmap()) {
                                if (this.currentImageDrawable instanceof RLottieDrawable) {
                                    Drawable drawable7 = this.staticThumbDrawable;
                                    if (!(drawable7 instanceof LoadingStickerDrawable)) {
                                        if (!(drawable7 instanceof SvgHelper.SvgDrawable)) {
                                        }
                                    }
                                }
                                drawable6 = this.currentThumbDrawable;
                                if (drawable6 == null || this.staticThumbDrawable != null || this.forceCrossfade) {
                                    if (drawable6 != null || this.staticThumbDrawable == null) {
                                        this.previousAlpha = 1.0f;
                                    } else {
                                        this.previousAlpha = this.currentAlpha;
                                    }
                                    this.currentAlpha = 0.0f;
                                    this.lastUpdateAlphaTime = System.currentTimeMillis();
                                    if (this.crossfadeImage == null) {
                                        if (this.currentThumbDrawable == null) {
                                        }
                                    }
                                    boolean z4 = true;
                                    this.crossfadeWithThumb = z4;
                                }
                            }
                        }
                    }
                    this.currentAlpha = 1.0f;
                    this.previousAlpha = 1.0f;
                }
                z3 = true;
                this.currentImageDrawable = drawable;
                if (drawable instanceof ExtendedBitmapDrawable) {
                }
                updateDrawableRadius(drawable);
                if (z3) {
                    drawable4 = this.currentMediaDrawable;
                    if (drawable4 instanceof RLottieDrawable) {
                    }
                    drawable5 = this.currentMediaDrawable;
                    if (drawable5 instanceof AnimatedFileDrawable) {
                    }
                    if (this.currentImageDrawable instanceof RLottieDrawable) {
                    }
                    drawable6 = this.currentThumbDrawable;
                    if (drawable6 == null) {
                    }
                    if (drawable6 != null) {
                    }
                    this.previousAlpha = 1.0f;
                    this.currentAlpha = 0.0f;
                    this.lastUpdateAlphaTime = System.currentTimeMillis();
                    if (this.crossfadeImage == null) {
                    }
                    boolean z42 = true;
                    this.crossfadeWithThumb = z42;
                }
                this.currentAlpha = 1.0f;
                this.previousAlpha = 1.0f;
            }
        } else if (i == 3) {
            if (!str.equals(this.currentMediaKey)) {
                return false;
            }
            ImageReceiverDelegate imageReceiverDelegate2 = this.delegate;
            if (imageReceiverDelegate2 != null) {
                imageReceiverDelegate2.didSetImageBitmap(i, str, drawable);
            }
            if (drawable instanceof AnimatedFileDrawable) {
                AnimatedFileDrawable animatedFileDrawable2 = (AnimatedFileDrawable) drawable;
                animatedFileDrawable2.setStartEndTime(this.startTime, this.endTime);
                if (animatedFileDrawable2.isWebmSticker) {
                    ImageLoader.getInstance().incrementUseCount(this.currentMediaKey);
                }
                if (this.videoThumbIsSame && (((z2 = (drawable3 = this.currentThumbDrawable) instanceof AnimatedFileDrawable)) || (this.currentImageDrawable instanceof AnimatedFileDrawable))) {
                    animatedFileDrawable2.seekTo(z2 ? ((AnimatedFileDrawable) drawable3).getLastFrameTimestamp() : 0L, true, true);
                }
            } else {
                ImageLoader.getInstance().incrementUseCount(this.currentMediaKey);
            }
            this.currentMediaDrawable = drawable;
            updateDrawableRadius(drawable);
            if (this.currentImageDrawable == null) {
                if ((!z && !this.forcePreview) || this.forceCrossfade) {
                    Drawable drawable8 = this.currentThumbDrawable;
                    if ((drawable8 == null && this.staticThumbDrawable == null) || this.currentAlpha == 1.0f || this.forceCrossfade) {
                        if (drawable8 == null || this.staticThumbDrawable == null) {
                            this.previousAlpha = 1.0f;
                        } else {
                            this.previousAlpha = this.currentAlpha;
                        }
                        this.currentAlpha = 0.0f;
                        this.lastUpdateAlphaTime = System.currentTimeMillis();
                        if (this.crossfadeImage == null) {
                            if (this.currentThumbDrawable == null) {
                            }
                        }
                        boolean z422 = true;
                        this.crossfadeWithThumb = z422;
                    }
                }
                this.currentAlpha = 1.0f;
                this.previousAlpha = 1.0f;
            }
        } else if (i == 1) {
            if (this.currentThumbDrawable != null) {
                return false;
            }
            if (!this.forcePreview) {
                AnimatedFileDrawable animation = getAnimation();
                if (animation != null && animation.hasBitmap()) {
                    return false;
                }
                Drawable drawable9 = this.currentImageDrawable;
                if ((drawable9 != null && !(drawable9 instanceof AnimatedFileDrawable)) || ((drawable2 = this.currentMediaDrawable) != null && !(drawable2 instanceof AnimatedFileDrawable))) {
                    return false;
                }
            }
            if (!str.equals(this.currentThumbKey)) {
                return false;
            }
            ImageReceiverDelegate imageReceiverDelegate3 = this.delegate;
            if (imageReceiverDelegate3 != null) {
                imageReceiverDelegate3.didSetImageBitmap(i, str, drawable);
            }
            ImageLoader.getInstance().incrementUseCount(this.currentThumbKey);
            this.currentThumbDrawable = drawable;
            if (drawable instanceof ExtendedBitmapDrawable) {
                ExtendedBitmapDrawable extendedBitmapDrawable2 = (ExtendedBitmapDrawable) drawable;
                this.thumbOrientation = extendedBitmapDrawable2.getOrientation();
                this.thumbInvert = extendedBitmapDrawable2.getInvert();
            }
            updateDrawableRadius(drawable);
            if (!z && this.crossfadeAlpha != 2) {
                Object obj = this.currentParentObject;
                if (!(obj instanceof MessageObject) || !((MessageObject) obj).isRoundVideo() || !((MessageObject) this.currentParentObject).isSending()) {
                    this.currentAlpha = 0.0f;
                    this.previousAlpha = 1.0f;
                    this.lastUpdateAlphaTime = System.currentTimeMillis();
                }
            }
            this.currentAlpha = 1.0f;
            this.previousAlpha = 1.0f;
        }
        ImageReceiverDelegate imageReceiverDelegate4 = this.delegate;
        if (imageReceiverDelegate4 != null) {
            Drawable drawable10 = this.currentImageDrawable;
            imageReceiverDelegate4.didSetImage(this, (drawable10 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable10 == null && this.currentMediaDrawable == null, z);
        }
        if (drawable instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable animatedFileDrawable3 = (AnimatedFileDrawable) drawable;
            animatedFileDrawable3.setUseSharedQueue(this.useSharedAnimationQueue);
            if (this.attachedToWindow) {
                animatedFileDrawable3.addParent(this);
            }
            if (this.allowStartAnimation && this.currentOpenedLayerFlags == 0) {
                animatedFileDrawable3.checkRepeat();
            }
            animatedFileDrawable3.setAllowDecodeSingleFrame(this.allowDecodeSingleFrame);
            this.animationReadySent = false;
            View view = this.parentView;
            if (view != null) {
                view.invalidate();
            }
        } else if (drawable instanceof RLottieDrawable) {
            RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
            if (this.attachedToWindow) {
                rLottieDrawable.addParentView(this);
            }
            if (this.allowStartLottieAnimation && (!rLottieDrawable.isHeavyDrawable() || this.currentOpenedLayerFlags == 0)) {
                rLottieDrawable.start();
            }
            rLottieDrawable.setAllowDecodeSingleFrame(true);
            rLottieDrawable.setAutoRepeat(this.autoRepeat);
            rLottieDrawable.setAutoRepeatCount(this.autoRepeatCount);
            rLottieDrawable.setAutoRepeatTimeout(this.autoRepeatTimeout);
            rLottieDrawable.setAllowDrawFramesWhileCacheGenerating(this.allowDrawWhileCacheGenerating);
            this.animationReadySent = false;
        }
        invalidate();
        return true;
    }

    public void setImageCoords(float f, float f2, float f3, float f4) {
        this.imageX = f;
        this.imageY = f2;
        this.imageW = f3;
        this.imageH = f4;
    }

    public void setImageCoords(Rect rect) {
        if (rect != null) {
            this.imageX = rect.left;
            this.imageY = rect.top;
            this.imageW = rect.width();
            this.imageH = rect.height();
        }
    }

    public void setImageCoords(RectF rectF) {
        if (rectF != null) {
            this.imageX = rectF.left;
            this.imageY = rectF.top;
            this.imageW = rectF.width();
            this.imageH = rectF.height();
        }
    }

    public void setImageWidth(int i) {
        this.imageW = i;
    }

    public void setImageX(float f) {
        this.imageX = f;
    }

    public void setImageY(float f) {
        this.imageY = f;
    }

    public void setInvalidateAll(boolean z) {
        this.invalidateAll = z;
    }

    public void setLayerNum(int i) {
        this.currentLayerNum = i;
        if (this.attachedToWindow) {
            this.currentOpenedLayerFlags = NotificationCenter.getGlobalInstance().getCurrentHeavyOperationFlags() & (this.currentLayerNum ^ (-1));
        }
    }

    public void setManualAlphaAnimator(boolean z) {
        this.manualAlphaAnimator = z;
    }

    public void setMark(Object obj) {
        this.mark = obj;
    }

    public void setMediaStartEndTime(long j, long j2) {
        this.startTime = j;
        this.endTime = j2;
        Drawable drawable = this.currentMediaDrawable;
        if (drawable instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) drawable).setStartEndTime(j, j2);
        }
    }

    public void setNeedsQualityThumb(boolean z) {
        this.needsQualityThumb = z;
    }

    public void setOrientation(int i, int i2, boolean z) {
        while (i < 0) {
            i += 360;
        }
        while (i > 360) {
            i -= 360;
        }
        this.thumbOrientation = i;
        this.imageOrientation = i;
        this.thumbInvert = i2;
        this.imageInvert = i2;
        this.centerRotation = z;
    }

    public void setOrientation(int i, boolean z) {
        setOrientation(i, 0, z);
    }

    public void setParam(int i) {
        this.param = i;
    }

    public void setParentView(View view) {
        this.parentView = view;
        AnimatedFileDrawable animation = getAnimation();
        if (animation == null || !this.attachedToWindow) {
            return;
        }
        animation.setParentView(this.parentView);
    }

    public void setPreloadingReceivers(List<ImageReceiver> list) {
        this.preloadReceivers = list;
    }

    public void setPressed(int i) {
        this.isPressed = i;
    }

    public void setQualityThumbDocument(TLRPC.Document document) {
        this.qulityThumbDocument = document;
    }

    public void setRoundRadius(int i) {
        setRoundRadius(new int[]{i, i, i, i});
    }

    public void setRoundRadius(int i, int i2, int i3, int i4) {
        setRoundRadius(new int[]{i, i2, i3, i4});
    }

    public void setRoundRadius(int[] iArr) {
        int i = iArr[0];
        this.isRoundRect = true;
        int i2 = 0;
        boolean z = false;
        while (true) {
            int[] iArr2 = this.roundRadius;
            if (i2 >= iArr2.length) {
                break;
            }
            int i3 = iArr2[i2];
            int i4 = iArr[i2];
            if (i3 != i4) {
                z = true;
            }
            if (i != i4) {
                this.isRoundRect = false;
            }
            iArr2[i2] = i4;
            i2++;
        }
        if (z) {
            Drawable drawable = this.currentImageDrawable;
            if (drawable != null && this.imageShader == null) {
                updateDrawableRadius(drawable);
            }
            Drawable drawable2 = this.currentMediaDrawable;
            if (drawable2 != null && this.mediaShader == null) {
                updateDrawableRadius(drawable2);
            }
            Drawable drawable3 = this.currentThumbDrawable;
            if (drawable3 != null) {
                updateDrawableRadius(drawable3);
            }
            Drawable drawable4 = this.staticThumbDrawable;
            if (drawable4 != null) {
                updateDrawableRadius(drawable4);
            }
        }
    }

    public void setRoundRadiusEnabled(boolean z) {
        if (this.useRoundRadius != z) {
            this.useRoundRadius = z;
            if (!z && this.emptyRoundRadius == null) {
                this.emptyRoundRadius = new int[]{0, 0, 0, 0};
            }
            Drawable drawable = this.currentImageDrawable;
            if (drawable != null && this.imageShader == null) {
                updateDrawableRadius(drawable);
            }
            Drawable drawable2 = this.currentMediaDrawable;
            if (drawable2 != null && this.mediaShader == null) {
                updateDrawableRadius(drawable2);
            }
            Drawable drawable3 = this.currentThumbDrawable;
            if (drawable3 != null) {
                updateDrawableRadius(drawable3);
            }
            Drawable drawable4 = this.staticThumbDrawable;
            if (drawable4 != null) {
                updateDrawableRadius(drawable4);
            }
        }
    }

    public void setShouldGenerateQualityThumb(boolean z) {
        this.shouldGenerateQualityThumb = z;
    }

    public void setSideClip(float f) {
        this.sideClip = f;
    }

    public void setSkipUpdateFrame(boolean z) {
        this.skipUpdateFrame = z;
    }

    public void setStrippedLocation(ImageLocation imageLocation) {
        this.strippedLocation = imageLocation;
    }

    protected void setTag(int i, int i2) {
        if (i2 == 1) {
            this.thumbTag = i;
        } else if (i2 == 3) {
            this.mediaTag = i;
        } else {
            this.imageTag = i;
        }
    }

    public void setUniqKeyPrefix(String str) {
        this.uniqKeyPrefix = str;
    }

    public void setUseRoundForThumbDrawable(boolean z) {
        this.useRoundForThumb = z;
    }

    public void setUseSharedAnimationQueue(boolean z) {
        this.useSharedAnimationQueue = z;
    }

    public void setVideoThumbIsSame(boolean z) {
        this.videoThumbIsSame = z;
    }

    public void setVisible(boolean z, boolean z2) {
        if (this.isVisible == z) {
            return;
        }
        this.isVisible = z;
        if (z2) {
            invalidate();
        }
    }

    public void skipDraw() {
    }

    public void startAnimation() {
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            animation.setUseSharedQueue(this.useSharedAnimationQueue);
            animation.start();
            return;
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation == null || lottieAnimation.isRunning()) {
            return;
        }
        lottieAnimation.restart();
    }

    public void startCrossfadeFromStaticThumb(Bitmap bitmap) {
        startCrossfadeFromStaticThumb(new BitmapDrawable((Resources) null, bitmap));
    }

    public void startCrossfadeFromStaticThumb(Drawable drawable) {
        this.currentThumbKey = null;
        this.currentThumbDrawable = null;
        this.thumbShader = null;
        this.staticThumbShader = null;
        this.roundPaint.setShader(null);
        setStaticDrawable(drawable);
        this.crossfadeWithThumb = true;
        this.currentAlpha = 0.0f;
        updateDrawableRadius(this.staticThumbDrawable);
    }

    public void stopAnimation() {
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            animation.stop();
            return;
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            lottieAnimation.stop();
        }
    }

    public void updateStaticDrawableThump(Bitmap bitmap) {
        this.staticThumbShader = null;
        this.roundPaint.setShader(null);
        setStaticDrawable(new BitmapDrawable(bitmap));
    }

    public boolean updateThumbShaderMatrix() {
        BitmapShader bitmapShader;
        Drawable drawable = this.currentThumbDrawable;
        if ((drawable == null || (bitmapShader = this.thumbShader) == null) && ((drawable = this.staticThumbDrawable) == null || (bitmapShader = this.staticThumbShader) == null)) {
            return false;
        }
        drawDrawable(null, drawable, NotificationCenter.liveLocationsChanged, bitmapShader, 0, 0, 0, null);
        return true;
    }
}
