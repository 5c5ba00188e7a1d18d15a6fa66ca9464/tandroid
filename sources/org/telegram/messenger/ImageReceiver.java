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
import androidx.annotation.Keep;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC$TL_videoSizeEmojiMarkup;
import org.telegram.tgnet.TLRPC$TL_videoSizeStickerMarkup;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.AttachableDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ClipRoundedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LoadingStickerDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclableDrawable;
import org.telegram.ui.Components.VectorAvatarThumbDrawable;
/* loaded from: classes.dex */
public class ImageReceiver implements NotificationCenter.NotificationCenterDelegate {
    public static final int DEFAULT_CROSSFADE_DURATION = 150;
    private static final int TYPE_CROSSFDADE = 2;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_MEDIA = 3;
    public static final int TYPE_THUMB = 1;
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
    private int bufferedFrame;
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
    private View parentView;
    List<ImageReceiver> preloadReceivers;
    private float pressedProgress;
    private float previousAlpha;
    private TLRPC$Document qulityThumbDocument;
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
    private boolean useSharedAnimationQueue;
    private boolean videoThumbIsSame;
    private static PorterDuffColorFilter selectedColorFilter = new PorterDuffColorFilter(-2236963, PorterDuff.Mode.MULTIPLY);
    private static PorterDuffColorFilter selectedGroupColorFilter = new PorterDuffColorFilter(-4473925, PorterDuff.Mode.MULTIPLY);
    private static final float[] radii = new float[8];

    /* loaded from: classes.dex */
    public static abstract class Decorator {
        public void onAttachedToWindow(ImageReceiver imageReceiver) {
        }

        public void onDetachedFromWidnow() {
        }

        protected abstract void onDraw(Canvas canvas, ImageReceiver imageReceiver);
    }

    /* loaded from: classes.dex */
    public interface ImageReceiverDelegate {

        /* loaded from: classes.dex */
        public final /* synthetic */ class -CC {
            public static void $default$onAnimationReady(ImageReceiverDelegate imageReceiverDelegate, ImageReceiver imageReceiver) {
            }
        }

        void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3);

        void onAnimationReady(ImageReceiver imageReceiver);
    }

    private boolean hasRoundRadius() {
        return true;
    }

    public void skipDraw() {
    }

    public boolean updateThumbShaderMatrix() {
        BitmapShader bitmapShader;
        BitmapShader bitmapShader2;
        Drawable drawable = this.currentThumbDrawable;
        if (drawable != null && (bitmapShader2 = this.thumbShader) != null) {
            drawDrawable(null, drawable, 255, bitmapShader2, 0, 0, 0, null);
            return true;
        }
        Drawable drawable2 = this.staticThumbDrawable;
        if (drawable2 == null || (bitmapShader = this.staticThumbShader) == null) {
            return false;
        }
        drawDrawable(null, drawable2, 255, bitmapShader, 0, 0, 0, null);
        return true;
    }

    public void setPreloadingReceivers(List<ImageReceiver> list) {
        this.preloadReceivers = list;
    }

    public Drawable getImageDrawable() {
        return this.currentImageDrawable;
    }

    public Drawable getMediaDrawable() {
        return this.currentMediaDrawable;
    }

    public void updateStaticDrawableThump(Bitmap bitmap) {
        this.staticThumbShader = null;
        this.roundPaint.setShader(null);
        setStaticDrawable(new BitmapDrawable(bitmap));
    }

    public void setAllowDrawWhileCacheGenerating(boolean z) {
        this.allowDrawWhileCacheGenerating = z;
    }

    /* loaded from: classes.dex */
    public static class BitmapHolder {
        public Bitmap bitmap;
        public Drawable drawable;
        private String key;
        public int orientation;
        private boolean recycleOnRelease;

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

        public BitmapHolder(Bitmap bitmap) {
            this.bitmap = bitmap;
            this.recycleOnRelease = true;
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

        public int getHeight() {
            Bitmap bitmap = this.bitmap;
            if (bitmap != null) {
                return bitmap.getHeight();
            }
            return 0;
        }

        public boolean isRecycled() {
            Bitmap bitmap = this.bitmap;
            return bitmap == null || bitmap.isRecycled();
        }

        public void release() {
            Bitmap bitmap;
            if (this.key == null) {
                if (this.recycleOnRelease && (bitmap = this.bitmap) != null) {
                    bitmap.recycle();
                }
                this.bitmap = null;
                this.drawable = null;
                return;
            }
            boolean decrementUseCount = ImageLoader.getInstance().decrementUseCount(this.key);
            if (!ImageLoader.getInstance().isInMemCache(this.key, false) && decrementUseCount) {
                Bitmap bitmap2 = this.bitmap;
                if (bitmap2 != null) {
                    bitmap2.recycle();
                } else {
                    Drawable drawable = this.drawable;
                    if (drawable != null) {
                        if (drawable instanceof RLottieDrawable) {
                            ((RLottieDrawable) drawable).recycle(false);
                        } else if (drawable instanceof AnimatedFileDrawable) {
                            ((AnimatedFileDrawable) drawable).recycle();
                        } else if (drawable instanceof BitmapDrawable) {
                            ((BitmapDrawable) drawable).getBitmap().recycle();
                        }
                    }
                }
            }
            this.key = null;
            this.bitmap = null;
            this.drawable = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SetImageBackup {
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

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.imageLocation = null;
            this.thumbLocation = null;
            this.mediaLocation = null;
            this.thumb = null;
        }
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
        this.roundRadius = new int[4];
        this.isRoundRect = true;
        this.roundRect = new RectF();
        this.shaderMatrix = new Matrix();
        this.roundPath = new Path();
        this.overrideAlpha = 1.0f;
        this.previousAlpha = 1.0f;
        this.crossfadeAlpha = (byte) 1;
        this.crossfadeByScale = 0.05f;
        this.crossfadeDuration = DEFAULT_CROSSFADE_DURATION;
        this.loadingOperations = new ArrayList<>();
        this.allowLoadingOnAttachedOnly = false;
        this.clip = true;
        this.parentView = view;
        this.roundPaint = new Paint(3);
        this.currentAccount = UserConfig.selectedAccount;
    }

    public void cancelLoadImage() {
        this.forceLoding = false;
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
        this.canceledLoading = true;
    }

    public void setForceLoading(boolean z) {
        this.forceLoding = z;
    }

    public boolean isForceLoding() {
        return this.forceLoding;
    }

    public void setStrippedLocation(ImageLocation imageLocation) {
        this.strippedLocation = imageLocation;
    }

    public void setIgnoreImageSet(boolean z) {
        this.ignoreImageSet = z;
    }

    public ImageLocation getStrippedLocation() {
        return this.strippedLocation;
    }

    public void setImage(ImageLocation imageLocation, String str, Drawable drawable, String str2, Object obj, int i) {
        setImage(imageLocation, str, null, null, drawable, 0L, str2, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, Drawable drawable, long j, String str2, Object obj, int i) {
        setImage(imageLocation, str, null, null, drawable, j, str2, obj, i);
    }

    public void setImage(String str, String str2, Drawable drawable, String str3, long j) {
        setImage(ImageLocation.getForPath(str), str2, null, null, drawable, j, str3, null, 1);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, String str3, Object obj, int i) {
        setImage(imageLocation, str, imageLocation2, str2, null, 0L, str3, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, long j, String str3, Object obj, int i) {
        setImage(imageLocation, str, imageLocation2, str2, null, j, str3, obj, i);
    }

    public void setForUserOrChat(TLObject tLObject, Drawable drawable) {
        setForUserOrChat(tLObject, drawable, null);
    }

    public void setForUserOrChat(TLObject tLObject, Drawable drawable, Object obj) {
        setForUserOrChat(tLObject, drawable, obj, false, 0, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setForUserOrChat(TLObject tLObject, Drawable drawable, Object obj, boolean z, int i, boolean z2) {
        BitmapDrawable bitmapDrawable;
        TLRPC$VideoSize tLRPC$VideoSize;
        boolean z3;
        TLRPC$ChatPhoto tLRPC$ChatPhoto;
        boolean z4;
        ImageLocation forUserOrChat;
        String str;
        TLRPC$UserFull userFull;
        ArrayList<TLRPC$VideoSize> arrayList;
        TLRPC$UserFull userFull2;
        Object obj2 = obj == null ? tLObject : obj;
        setUseRoundForThumbDrawable(true);
        ImageLocation imageLocation = null;
        if (tLObject instanceof TLRPC$User) {
            TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
            z3 = tLRPC$User.premium;
            TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
            if (tLRPC$UserProfilePhoto != null) {
                bitmapDrawable = tLRPC$UserProfilePhoto.strippedBitmap;
                z4 = tLRPC$UserProfilePhoto.stripped_thumb != null;
                if (i == 3 && (userFull2 = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id)) != null) {
                    TLRPC$Photo tLRPC$Photo = tLRPC$User.photo.personal ? userFull2.personal_photo : userFull2.profile_photo;
                    if (tLRPC$Photo != null) {
                        tLRPC$VideoSize = FileLoader.getVectorMarkupVideoSize(tLRPC$Photo);
                        if (tLRPC$VideoSize == null && z && MessagesController.getInstance(this.currentAccount).isPremiumUser(tLRPC$User) && tLRPC$User.photo.has_video && LiteMode.isEnabled(1024)) {
                            userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
                            if (userFull != null) {
                                MessagesController.getInstance(this.currentAccount).loadFullUser(tLRPC$User, this.currentGuid, false);
                            } else {
                                TLRPC$Photo tLRPC$Photo2 = tLRPC$User.photo.personal ? userFull.personal_photo : userFull.profile_photo;
                                if (tLRPC$Photo2 != null && (tLRPC$VideoSize = FileLoader.getVectorMarkupVideoSize(tLRPC$Photo2)) == null && (arrayList = tLRPC$Photo2.video_sizes) != null && !arrayList.isEmpty()) {
                                    TLRPC$VideoSize closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(arrayList, 100);
                                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                                        TLRPC$VideoSize tLRPC$VideoSize2 = arrayList.get(i2);
                                        if ("p".equals(tLRPC$VideoSize2.type)) {
                                            closestVideoSizeWithSize = tLRPC$VideoSize2;
                                        }
                                        if ((tLRPC$VideoSize2 instanceof TLRPC$TL_videoSizeEmojiMarkup) || (tLRPC$VideoSize2 instanceof TLRPC$TL_videoSizeStickerMarkup)) {
                                            tLRPC$VideoSize = tLRPC$VideoSize2;
                                        }
                                    }
                                    imageLocation = ImageLocation.getForPhoto(closestVideoSizeWithSize, tLRPC$Photo2);
                                }
                            }
                        }
                    }
                }
                tLRPC$VideoSize = null;
                if (tLRPC$VideoSize == null) {
                    userFull = MessagesController.getInstance(this.currentAccount).getUserFull(tLRPC$User.id);
                    if (userFull != null) {
                    }
                }
            } else {
                bitmapDrawable = null;
                tLRPC$VideoSize = null;
                z4 = false;
            }
        } else if (!(tLObject instanceof TLRPC$Chat) || (tLRPC$ChatPhoto = ((TLRPC$Chat) tLObject).photo) == null) {
            bitmapDrawable = null;
            tLRPC$VideoSize = null;
            z3 = false;
            z4 = false;
        } else {
            BitmapDrawable bitmapDrawable2 = tLRPC$ChatPhoto.strippedBitmap;
            z4 = tLRPC$ChatPhoto.stripped_thumb != null;
            tLRPC$VideoSize = null;
            bitmapDrawable = bitmapDrawable2;
            z3 = false;
        }
        if (tLRPC$VideoSize != null && i != 0) {
            setImageBitmap(new VectorAvatarThumbDrawable(tLRPC$VideoSize, z3, i));
            return;
        }
        if (!z2) {
            forUserOrChat = ImageLocation.getForUserOrChat(tLObject, 1);
            str = "50_50";
        } else {
            forUserOrChat = ImageLocation.getForUserOrChat(tLObject, 0);
            str = "100_100";
        }
        ImageLocation imageLocation2 = forUserOrChat;
        String str2 = str;
        if (imageLocation != null) {
            setImage(imageLocation, "avatar", imageLocation2, str2, null, null, bitmapDrawable, 0L, null, obj2, 0);
            this.animatedFileDrawableRepeatMaxCount = 3;
        } else if (bitmapDrawable != null) {
            setImage(imageLocation2, str2, bitmapDrawable, null, obj2, 0);
        } else if (z4) {
            setImage(imageLocation2, str2, ImageLocation.getForUserOrChat(tLObject, 2), "50_50_b", drawable, obj2, 0);
        } else {
            setImage(imageLocation2, str2, drawable, null, obj2, 0);
        }
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, Drawable drawable, Object obj, int i) {
        setImage(null, null, imageLocation, str, imageLocation2, str2, drawable, 0L, null, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, Drawable drawable, long j, String str3, Object obj, int i) {
        setImage(null, null, imageLocation, str, imageLocation2, str2, drawable, j, str3, obj, i);
    }

    public void setImage(ImageLocation imageLocation, String str, ImageLocation imageLocation2, String str2, ImageLocation imageLocation3, String str3, Drawable drawable, long j, String str4, Object obj, int i) {
        String str5;
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
        } else if (!this.ignoreImageSet) {
            if (this.crossfadeWithOldImage && (setImageBackup = this.setImageBackup) != null && setImageBackup.isWebfileSet()) {
                setBackupImage();
            }
            SetImageBackup setImageBackup3 = this.setImageBackup;
            if (setImageBackup3 != null) {
                setImageBackup3.clear();
            }
            boolean z = true;
            if (imageLocation5 == null && imageLocation3 == null && imageLocation4 == null) {
                for (int i2 = 0; i2 < 4; i2++) {
                    recycleBitmap(null, i2);
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
                Bitmap bitmap = this.legacyBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
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
                ImageReceiverDelegate imageReceiverDelegate = this.delegate;
                if (imageReceiverDelegate != null) {
                    Drawable drawable2 = this.currentImageDrawable;
                    imageReceiverDelegate.didSetImage(this, (drawable2 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, (drawable2 == null && this.currentMediaDrawable == null) ? false : false, false);
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
                TLRPC$Document tLRPC$Document = this.qulityThumbDocument;
                if (tLRPC$Document == null) {
                    tLRPC$Document = ((MessageObject) obj).getDocument();
                }
                if (tLRPC$Document != null && tLRPC$Document.dc_id != 0 && tLRPC$Document.id != 0) {
                    key = "q_" + tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
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
                ImageReceiverDelegate imageReceiverDelegate2 = this.delegate;
                if (imageReceiverDelegate2 != null) {
                    Drawable drawable3 = this.currentImageDrawable;
                    imageReceiverDelegate2.didSetImage(this, (drawable3 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable3 == null && this.currentMediaDrawable == null, false);
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
                if ((obj2 instanceof MessageObject) && ((MessageObject) obj2).lastGeoWebFileSet != null && (MessageObject.getMedia((MessageObject) obj2) instanceof TLRPC$TL_messageMediaGeoLive)) {
                    Object obj3 = this.currentParentObject;
                    ((MessageObject) obj3).lastGeoWebFileLoaded = ((MessageObject) obj3).lastGeoWebFileSet;
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
                } else if (this.currentImageDrawable != null) {
                    recycleBitmap(key3, 1);
                    recycleBitmap(null, 2);
                    recycleBitmap(key2, 3);
                    this.crossfadeShader = this.imageShader;
                    this.crossfadeImage = this.currentImageDrawable;
                    this.crossfadeKey = this.currentImageKey;
                    this.crossfadingWithThumb = false;
                    this.currentImageDrawable = null;
                    this.currentImageKey = null;
                } else if (this.currentThumbDrawable != null) {
                    recycleBitmap(str7, 0);
                    recycleBitmap(null, 2);
                    recycleBitmap(key2, 3);
                    this.crossfadeShader = this.thumbShader;
                    this.crossfadeImage = this.currentThumbDrawable;
                    this.crossfadeKey = this.currentThumbKey;
                    this.crossfadingWithThumb = false;
                    this.currentThumbDrawable = null;
                    this.currentThumbKey = null;
                } else if (this.staticThumbDrawable != null) {
                    recycleBitmap(str7, 0);
                    recycleBitmap(key3, 1);
                    recycleBitmap(null, 2);
                    recycleBitmap(key2, 3);
                    this.crossfadeShader = this.staticThumbShader;
                    this.crossfadeImage = this.staticThumbDrawable;
                    this.crossfadingWithThumb = false;
                    this.crossfadeKey = null;
                    this.currentThumbDrawable = null;
                    this.currentThumbKey = null;
                } else {
                    recycleBitmap(str7, 0);
                    recycleBitmap(key3, 1);
                    recycleBitmap(null, 2);
                    recycleBitmap(key2, 3);
                    this.crossfadeShader = null;
                }
            } else {
                recycleBitmap(str7, 0);
                recycleBitmap(key3, 1);
                recycleBitmap(null, 2);
                recycleBitmap(key2, 3);
                this.crossfadeShader = null;
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
            Bitmap bitmap2 = this.legacyBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.legacyBitmap = null;
            }
            this.currentAlpha = 1.0f;
            this.previousAlpha = 1.0f;
            updateDrawableRadius(this.staticThumbDrawable);
            ImageReceiverDelegate imageReceiverDelegate3 = this.delegate;
            if (imageReceiverDelegate3 != null) {
                Drawable drawable5 = this.currentImageDrawable;
                imageReceiverDelegate3.didSetImage(this, (drawable5 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable5 == null && this.currentMediaDrawable == null, false);
            }
            loadImage();
            this.isRoundVideo = (obj instanceof MessageObject) && ((MessageObject) obj).isRoundVideo();
        }
    }

    private void loadImage() {
        ImageLoader.getInstance().loadImageForImageReceiver(this, this.preloadReceivers);
        invalidate();
    }

    public boolean canInvertBitmap() {
        return (this.currentMediaDrawable instanceof ExtendedBitmapDrawable) || (this.currentImageDrawable instanceof ExtendedBitmapDrawable) || (this.currentThumbDrawable instanceof ExtendedBitmapDrawable) || (this.staticThumbDrawable instanceof ExtendedBitmapDrawable);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
    }

    public void setDelegate(ImageReceiverDelegate imageReceiverDelegate) {
        this.delegate = imageReceiverDelegate;
    }

    public void setPressed(int i) {
        this.isPressed = i;
    }

    public boolean getPressed() {
        return this.isPressed != 0;
    }

    public void setOrientation(int i, boolean z) {
        setOrientation(i, 0, z);
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

    public void setInvalidateAll(boolean z) {
        this.invalidateAll = z;
    }

    public Drawable getStaticThumb() {
        return this.staticThumbDrawable;
    }

    public int getAnimatedOrientation() {
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            return animation.getOrientation();
        }
        return 0;
    }

    public int getOrientation() {
        return this.imageOrientation;
    }

    public int getInvert() {
        return this.imageInvert;
    }

    public void setLayerNum(int i) {
        this.currentLayerNum = i;
        if (this.attachedToWindow) {
            int currentHeavyOperationFlags = NotificationCenter.getGlobalInstance().getCurrentHeavyOperationFlags();
            this.currentOpenedLayerFlags = currentHeavyOperationFlags;
            this.currentOpenedLayerFlags = currentHeavyOperationFlags & (this.currentLayerNum ^ (-1));
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap != null ? new BitmapDrawable((Resources) null, bitmap) : null);
    }

    public void setImageBitmap(Drawable drawable) {
        boolean z = true;
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
        if (!this.crossfadeWithOldImage) {
            for (int i = 0; i < 4; i++) {
                recycleBitmap(null, i);
            }
        } else if (this.currentImageDrawable != null) {
            recycleBitmap(null, 1);
            recycleBitmap(null, 2);
            recycleBitmap(null, 3);
            this.crossfadeShader = this.imageShader;
            this.crossfadeImage = this.currentImageDrawable;
            this.crossfadeKey = this.currentImageKey;
            this.crossfadingWithThumb = true;
        } else if (this.currentThumbDrawable != null) {
            recycleBitmap(null, 0);
            recycleBitmap(null, 2);
            recycleBitmap(null, 3);
            this.crossfadeShader = this.thumbShader;
            this.crossfadeImage = this.currentThumbDrawable;
            this.crossfadeKey = this.currentThumbKey;
            this.crossfadingWithThumb = true;
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
            for (int i2 = 0; i2 < 4; i2++) {
                recycleBitmap(null, i2);
            }
            this.crossfadeShader = null;
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

    private void setStaticDrawable(Drawable drawable) {
        Drawable drawable2 = this.staticThumbDrawable;
        if (drawable == drawable2) {
            return;
        }
        AttachableDrawable attachableDrawable = null;
        if (drawable2 instanceof AttachableDrawable) {
            if (drawable2.equals(drawable)) {
                return;
            }
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

    private void setDrawableShader(Drawable drawable, BitmapShader bitmapShader) {
        if (drawable == this.currentThumbDrawable) {
            this.thumbShader = bitmapShader;
        } else if (drawable == this.staticThumbDrawable) {
            this.staticThumbShader = bitmapShader;
        } else if (drawable == this.currentMediaDrawable) {
            this.mediaShader = bitmapShader;
        } else if (drawable == this.currentImageDrawable) {
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

    private void updateDrawableRadius(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof ClipRoundedDrawable) {
            int[] iArr = this.roundRadius;
            ((ClipRoundedDrawable) drawable).setRadii(iArr[0], iArr[1], iArr[2], iArr[3]);
        } else if ((hasRoundRadius() || this.gradientShader != null) && ((drawable instanceof BitmapDrawable) || (drawable instanceof AvatarDrawable))) {
            if (drawable instanceof AvatarDrawable) {
                ((AvatarDrawable) drawable).setRoundRadius(this.roundRadius[0]);
                return;
            }
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable instanceof RLottieDrawable) {
                return;
            }
            if (bitmapDrawable instanceof AnimatedFileDrawable) {
                ((AnimatedFileDrawable) drawable).setRoundRadius(this.roundRadius);
            } else if (bitmapDrawable.getBitmap() != null) {
                Bitmap bitmap = bitmapDrawable.getBitmap();
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                setDrawableShader(drawable, new BitmapShader(bitmap, tileMode, tileMode));
            }
        } else {
            setDrawableShader(drawable, null);
        }
    }

    public void clearImage() {
        for (int i = 0; i < 4; i++) {
            recycleBitmap(null, i);
        }
        ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
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
            Drawable drawable = this.staticThumbDrawable;
            if (drawable instanceof AttachableDrawable) {
                ((AttachableDrawable) drawable).onDetachedFromWindow(this);
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
        if (!lottieAnimation.isHeavyDrawable() || this.currentOpenedLayerFlags == 0) {
            lottieAnimation.start();
            return true;
        }
        return true;
    }

    public void incrementFrames(int i) {
        Drawable drawable = this.currentMediaDrawable;
        if (!(drawable instanceof RLottieDrawable) && (drawable instanceof AnimatedFileDrawable)) {
            int i2 = this.bufferedFrame;
            int i3 = i + i2;
            this.bufferedFrame = i3;
            while (i2 != i3) {
                ((AnimatedFileDrawable) this.currentMediaDrawable).getNextFrame();
                i3--;
            }
        }
    }

    public boolean onAttachedToWindow() {
        if (this.attachedToWindow) {
            return false;
        }
        this.attachedToWindow = true;
        int currentHeavyOperationFlags = NotificationCenter.getGlobalInstance().getCurrentHeavyOperationFlags();
        this.currentOpenedLayerFlags = currentHeavyOperationFlags;
        this.currentOpenedLayerFlags = currentHeavyOperationFlags & (this.currentLayerNum ^ (-1));
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
            didReceivedNotification(NotificationCenter.stopAllHeavyOperations, this.currentAccount, Integer.valueOf((int) LiteMode.FLAG_CALLS_ANIMATIONS));
        }
        Drawable drawable = this.staticThumbDrawable;
        if (drawable instanceof AttachableDrawable) {
            ((AttachableDrawable) drawable).onAttachedToWindow(this);
        }
        if (this.decorators != null) {
            for (int i = 0; i < this.decorators.size(); i++) {
                this.decorators.get(i).onAttachedToWindow(this);
            }
        }
        return false;
    }

    private void drawDrawable(Canvas canvas, Drawable drawable, int i, BitmapShader bitmapShader, int i2, int i3, BackgroundThreadDrawHolder backgroundThreadDrawHolder) {
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
        int i4 = this.isPressed;
        if (i4 != 0) {
            this.pressedProgress = 1.0f;
            this.animateFromIsPressed = i4;
        }
        float f3 = this.pressedProgress;
        if (f3 == 0.0f || f3 == 1.0f) {
            drawDrawable(canvas, drawable, i, bitmapShader, i2, i3, i4, backgroundThreadDrawHolder);
            return;
        }
        drawDrawable(canvas, drawable, i, bitmapShader, i2, i3, i4, backgroundThreadDrawHolder);
        drawDrawable(canvas, drawable, (int) (i * this.pressedProgress), bitmapShader, i2, i3, this.animateFromIsPressed, backgroundThreadDrawHolder);
    }

    public void setUseRoundForThumbDrawable(boolean z) {
        this.useRoundForThumb = z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:400:0x08c2  */
    /* JADX WARN: Removed duplicated region for block: B:426:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v12 */
    /* JADX WARN: Type inference failed for: r15v3 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r15v8 */
    /* JADX WARN: Type inference failed for: r15v9 */
    /* JADX WARN: Type inference failed for: r31v0, types: [org.telegram.messenger.ImageReceiver] */
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
        SvgHelper.SvgDrawable svgDrawable;
        boolean z;
        Paint paint;
        int i5;
        int intrinsicHeight;
        int intrinsicWidth;
        boolean z2;
        boolean z3;
        float f5;
        int i6;
        BitmapDrawable bitmapDrawable;
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
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable2 = (BitmapDrawable) drawable;
            boolean z4 = drawable instanceof RLottieDrawable;
            if (z4) {
                z = z4;
                ((RLottieDrawable) drawable).skipFrameUpdate = this.skipUpdateFrame;
            } else {
                z = z4;
                if (drawable instanceof AnimatedFileDrawable) {
                    ((AnimatedFileDrawable) drawable).skipFrameUpdate = this.skipUpdateFrame;
                }
            }
            if (bitmapShader != null) {
                paint = this.roundPaint;
            } else {
                paint = bitmapDrawable2.getPaint();
            }
            int i7 = Build.VERSION.SDK_INT;
            if (i7 >= 29) {
                Object obj2 = this.blendMode;
                i5 = i7;
                if (obj2 != null && this.gradientShader == null) {
                    paint.setBlendMode((BlendMode) obj2);
                } else {
                    paint.setBlendMode(null);
                }
            } else {
                i5 = i7;
            }
            boolean z5 = (paint == null || paint.getColorFilter() == null) ? false : true;
            if (z5 && i4 == 0) {
                if (bitmapShader != null) {
                    obj = null;
                    this.roundPaint.setColorFilter(null);
                } else {
                    obj = null;
                    if (this.staticThumbDrawable != drawable) {
                        bitmapDrawable2.setColorFilter(null);
                    }
                }
            } else if (!z5 && i4 != 0) {
                if (i4 == 1) {
                    if (bitmapShader != null) {
                        this.roundPaint.setColorFilter(selectedColorFilter);
                    } else {
                        bitmapDrawable2.setColorFilter(selectedColorFilter);
                    }
                } else if (bitmapShader != null) {
                    this.roundPaint.setColorFilter(selectedGroupColorFilter);
                } else {
                    bitmapDrawable2.setColorFilter(selectedGroupColorFilter);
                }
            }
            if (colorFilter != null && this.gradientShader == null) {
                if (bitmapShader != null) {
                    this.roundPaint.setColorFilter(colorFilter);
                } else {
                    bitmapDrawable2.setColorFilter(colorFilter);
                }
            }
            boolean z6 = bitmapDrawable2 instanceof AnimatedFileDrawable;
            if (z6 || (bitmapDrawable2 instanceof RLottieDrawable)) {
                int i8 = i2 % 360;
                if (i8 == 90 || i8 == 270) {
                    intrinsicHeight = bitmapDrawable2.getIntrinsicHeight();
                    intrinsicWidth = bitmapDrawable2.getIntrinsicWidth();
                } else {
                    intrinsicHeight = bitmapDrawable2.getIntrinsicWidth();
                    intrinsicWidth = bitmapDrawable2.getIntrinsicHeight();
                }
                z2 = false;
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
                z2 = bitmapDrawable2 instanceof ReactionLastFrame;
            }
            float f6 = this.sideClip;
            float f7 = f4 - (f6 * 2.0f);
            float f8 = f3 - (f6 * 2.0f);
            float f9 = f4 == 0.0f ? 1.0f : intrinsicHeight / f7;
            if (f3 == 0.0f) {
                z3 = z6;
                f5 = 1.0f;
            } else {
                z3 = z6;
                f5 = intrinsicWidth / f8;
            }
            if (z2) {
                f9 /= 1.2f;
                f5 /= 1.2f;
            }
            boolean z7 = z2;
            if (bitmapShader != null && backgroundThreadDrawHolder == null) {
                if (this.isAspectFit) {
                    float max = Math.max(f9, f5);
                    float f10 = (int) (intrinsicHeight / max);
                    float f11 = (int) (intrinsicWidth / max);
                    rectF.set(((f4 - f10) / 2.0f) + f, ((f3 - f11) / 2.0f) + f2, f + ((f4 + f10) / 2.0f), f2 + ((f3 + f11) / 2.0f));
                    if (this.isVisible) {
                        this.shaderMatrix.reset();
                        this.shaderMatrix.setTranslate((int) rectF.left, (int) rectF.top);
                        if (i3 != 0) {
                            this.shaderMatrix.preScale(i3 == 1 ? -1.0f : 1.0f, i3 == 2 ? -1.0f : 1.0f, rectF.width() / 2.0f, rectF.height() / 2.0f);
                        }
                        if (i2 == 90) {
                            this.shaderMatrix.preRotate(90.0f);
                            this.shaderMatrix.preTranslate(0.0f, -rectF.width());
                        } else if (i2 == 180) {
                            this.shaderMatrix.preRotate(180.0f);
                            this.shaderMatrix.preTranslate(-rectF.width(), -rectF.height());
                        } else if (i2 == 270) {
                            this.shaderMatrix.preRotate(270.0f);
                            this.shaderMatrix.preTranslate(-rectF.height(), 0.0f);
                        }
                        float f12 = 1.0f / max;
                        this.shaderMatrix.preScale(f12, f12);
                        bitmapShader.setLocalMatrix(this.shaderMatrix);
                        this.roundPaint.setShader(bitmapShader);
                        this.roundPaint.setAlpha(i);
                        this.roundRect.set(rectF);
                        if (!this.isRoundRect) {
                            for (int i10 = 0; i10 < iArr2.length; i10++) {
                                float[] fArr = radii;
                                int i11 = i10 * 2;
                                fArr[i11] = iArr2[i10];
                                fArr[i11 + 1] = iArr2[i10];
                            }
                            this.roundPath.reset();
                            this.roundPath.addRoundRect(this.roundRect, radii, Path.Direction.CW);
                            this.roundPath.close();
                            if (canvas != null) {
                                canvas.drawPath(this.roundPath, this.roundPaint);
                            }
                        } else if (canvas != null) {
                            try {
                                if (iArr2[0] == 0) {
                                    canvas.drawRect(this.roundRect, this.roundPaint);
                                } else {
                                    canvas.drawRoundRect(this.roundRect, iArr2[0], iArr2[0], this.roundPaint);
                                }
                            } catch (Exception e) {
                                onBitmapException(bitmapDrawable2);
                                FileLog.e(e);
                            }
                        }
                    }
                } else {
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
                    if (bitmapShader == this.imageShader && this.gradientShader != null) {
                        ComposeShader composeShader = this.composeShader;
                        if (composeShader != null) {
                            this.roundPaint.setShader(composeShader);
                        } else {
                            this.roundPaint.setShader(this.legacyShader);
                        }
                    } else {
                        this.roundPaint.setShader(bitmapShader);
                    }
                    float min = 1.0f / Math.min(f9, f5);
                    RectF rectF2 = this.roundRect;
                    float f13 = this.sideClip;
                    float f14 = f4;
                    rectF2.set(f + f13, f2 + f13, (f + f4) - f13, (f2 + f3) - f13);
                    if (Math.abs(f9 - f5) > 5.0E-4f) {
                        float f15 = intrinsicHeight / f5;
                        if (f15 > f7) {
                            float f16 = (int) f15;
                            rectF.set(f - ((f16 - f7) / 2.0f), f2, ((f16 + f7) / 2.0f) + f, f2 + f8);
                        } else {
                            float f17 = (int) (i6 / f9);
                            rectF.set(f, f2 - ((f17 - f8) / 2.0f), f + f7, ((f17 + f8) / 2.0f) + f2);
                        }
                    } else {
                        rectF.set(f, f2, f + f7, f2 + f8);
                    }
                    if (this.isVisible) {
                        this.shaderMatrix.reset();
                        if (z7) {
                            this.shaderMatrix.setTranslate((rectF.left + this.sideClip) - (((rectF.width() * 1.2f) - rectF.width()) / 2.0f), (rectF.top + this.sideClip) - (((rectF.height() * 1.2f) - rectF.height()) / 2.0f));
                        } else {
                            Matrix matrix = this.shaderMatrix;
                            float f18 = rectF.left;
                            float f19 = this.sideClip;
                            matrix.setTranslate(f18 + f19, rectF.top + f19);
                        }
                        if (i3 != 0) {
                            this.shaderMatrix.preScale(i3 == 1 ? -1.0f : 1.0f, i3 == 2 ? -1.0f : 1.0f, rectF.width() / 2.0f, rectF.height() / 2.0f);
                        }
                        if (i2 == 90) {
                            this.shaderMatrix.preRotate(90.0f);
                            this.shaderMatrix.preTranslate(0.0f, -rectF.width());
                        } else if (i2 == 180) {
                            this.shaderMatrix.preRotate(180.0f);
                            this.shaderMatrix.preTranslate(-rectF.width(), -rectF.height());
                        } else if (i2 == 270) {
                            this.shaderMatrix.preRotate(270.0f);
                            this.shaderMatrix.preTranslate(-rectF.height(), 0.0f);
                        }
                        this.shaderMatrix.preScale(min, min);
                        if (this.isRoundVideo) {
                            float f20 = (f7 + (AndroidUtilities.roundMessageInset * 2)) / f7;
                            this.shaderMatrix.postScale(f20, f20, rectF.centerX(), rectF.centerY());
                        }
                        BitmapShader bitmapShader2 = this.legacyShader;
                        if (bitmapShader2 != null) {
                            bitmapShader2.setLocalMatrix(this.shaderMatrix);
                        }
                        bitmapShader.setLocalMatrix(this.shaderMatrix);
                        if (this.composeShader != null) {
                            int width = this.gradientBitmap.getWidth();
                            int height = this.gradientBitmap.getHeight();
                            float f21 = f14 == 0.0f ? 1.0f : width / f7;
                            float f22 = f3 == 0.0f ? 1.0f : height / f8;
                            if (Math.abs(f21 - f22) > 5.0E-4f) {
                                float f23 = width / f22;
                                if (f23 > f7) {
                                    width = (int) f23;
                                    float f24 = width;
                                    rectF.set(f - ((f24 - f7) / 2.0f), f2, f + ((f24 + f7) / 2.0f), f2 + f8);
                                } else {
                                    height = (int) (height / f21);
                                    float f25 = height;
                                    rectF.set(f, f2 - ((f25 - f8) / 2.0f), f + f7, f2 + ((f25 + f8) / 2.0f));
                                }
                            } else {
                                rectF.set(f, f2, f + f7, f2 + f8);
                            }
                            float min2 = 1.0f / Math.min(f14 == 0.0f ? 1.0f : width / f7, f3 == 0.0f ? 1.0f : height / f8);
                            this.shaderMatrix.reset();
                            Matrix matrix2 = this.shaderMatrix;
                            float f26 = rectF.left;
                            float f27 = this.sideClip;
                            matrix2.setTranslate(f26 + f27, rectF.top + f27);
                            this.shaderMatrix.preScale(min2, min2);
                            this.gradientShader.setLocalMatrix(this.shaderMatrix);
                        }
                        this.roundPaint.setAlpha(i);
                        if (!this.isRoundRect) {
                            for (int i12 = 0; i12 < iArr2.length; i12++) {
                                float[] fArr2 = radii;
                                int i13 = i12 * 2;
                                fArr2[i13] = iArr2[i12];
                                fArr2[i13 + 1] = iArr2[i12];
                            }
                            this.roundPath.reset();
                            this.roundPath.addRoundRect(this.roundRect, radii, Path.Direction.CW);
                            this.roundPath.close();
                            if (canvas != null) {
                                canvas.drawPath(this.roundPath, this.roundPaint);
                            }
                        } else if (canvas != null) {
                            try {
                                if (iArr2[0] != 0) {
                                    canvas.drawRoundRect(this.roundRect, iArr2[0], iArr2[0], this.roundPaint);
                                } else if (z7) {
                                    RectF rectF3 = AndroidUtilities.rectTmp;
                                    rectF3.set(this.roundRect);
                                    rectF3.inset((-((rectF.width() * 1.2f) - rectF.width())) / 2.0f, (-((rectF.height() * 1.2f) - rectF.height())) / 2.0f);
                                    canvas.drawRect(rectF3, this.roundPaint);
                                } else {
                                    canvas.drawRect(this.roundRect, this.roundPaint);
                                }
                            } catch (Exception e2) {
                                if (backgroundThreadDrawHolder == null) {
                                    onBitmapException(bitmapDrawable);
                                }
                                FileLog.e(e2);
                            }
                        }
                    }
                }
            } else {
                float f28 = f4;
                if (this.isAspectFit) {
                    float max2 = Math.max(f9, f5);
                    canvas.save();
                    int i14 = (int) (intrinsicHeight / max2);
                    int i15 = (int) (intrinsicWidth / max2);
                    if (backgroundThreadDrawHolder == null) {
                        float f29 = i14;
                        float f30 = i15;
                        rectF.set(((f28 - f29) / 2.0f) + f, ((f3 - f30) / 2.0f) + f2, ((f28 + f29) / 2.0f) + f, ((f30 + f3) / 2.0f) + f2);
                        bitmapDrawable2.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                        if (bitmapDrawable2 instanceof AnimatedFileDrawable) {
                            ((AnimatedFileDrawable) bitmapDrawable2).setActualDrawRect(rectF.left, rectF.top, rectF.width(), rectF.height());
                        }
                    }
                    if (backgroundThreadDrawHolder != null && iArr2 != null && iArr2[0] > 0) {
                        canvas.save();
                        Path path = backgroundThreadDrawHolder.roundPath == null ? backgroundThreadDrawHolder.roundPath = new Path() : backgroundThreadDrawHolder.roundPath;
                        path.rewind();
                        RectF rectF4 = AndroidUtilities.rectTmp;
                        rectF4.set(f, f2, f + f28, f3 + f2);
                        path.addRoundRect(rectF4, iArr2[0], iArr2[2], Path.Direction.CW);
                        canvas.clipPath(path);
                    }
                    if (this.isVisible) {
                        try {
                            bitmapDrawable2.setAlpha(i);
                            drawBitmapDrawable(canvas, bitmapDrawable2, backgroundThreadDrawHolder, i);
                        } catch (Exception e3) {
                            if (backgroundThreadDrawHolder == null) {
                                onBitmapException(bitmapDrawable2);
                            }
                            FileLog.e(e3);
                        }
                    }
                    canvas.restore();
                    if (backgroundThreadDrawHolder != null && iArr2 != null && iArr2[0] > 0) {
                        canvas.restore();
                    }
                } else if (canvas != null) {
                    if (Math.abs(f9 - f5) > 1.0E-5f) {
                        canvas.save();
                        if (this.clip) {
                            canvas.clipRect(f, f2, f + f28, f2 + f3);
                        }
                        if (i3 == 1) {
                            canvas.scale(-1.0f, 1.0f, f28 / 2.0f, f3 / 2.0f);
                        } else if (i3 == 2) {
                            canvas.scale(1.0f, -1.0f, f28 / 2.0f, f3 / 2.0f);
                        }
                        int i16 = i2 % 360;
                        if (i16 != 0) {
                            if (this.centerRotation) {
                                canvas.rotate(i2, f28 / 2.0f, f3 / 2.0f);
                            } else {
                                canvas.rotate(i2, 0.0f, 0.0f);
                            }
                        }
                        float f31 = intrinsicHeight / f5;
                        if (f31 > f28) {
                            float f32 = (int) f31;
                            rectF.set(f - ((f32 - f28) / 2.0f), f2, ((f32 + f28) / 2.0f) + f, f2 + f3);
                        } else {
                            float f33 = (int) (intrinsicWidth / f9);
                            rectF.set(f, f2 - ((f33 - f3) / 2.0f), f + f28, ((f33 + f3) / 2.0f) + f2);
                        }
                        if (z3) {
                            ((AnimatedFileDrawable) bitmapDrawable2).setActualDrawRect(f, f2, f28, f3);
                        }
                        if (backgroundThreadDrawHolder == null) {
                            if (i16 == 90 || i16 == 270) {
                                float width2 = rectF.width() / 2.0f;
                                float height2 = rectF.height() / 2.0f;
                                float centerX = rectF.centerX();
                                float centerY = rectF.centerY();
                                bitmapDrawable2.setBounds((int) (centerX - height2), (int) (centerY - width2), (int) (centerX + height2), (int) (centerY + width2));
                            } else {
                                bitmapDrawable2.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                            }
                        }
                        if (this.isVisible) {
                            if (i5 >= 29) {
                                try {
                                    if (this.blendMode != null) {
                                        bitmapDrawable2.getPaint().setBlendMode((BlendMode) this.blendMode);
                                    } else {
                                        bitmapDrawable2.getPaint().setBlendMode(null);
                                    }
                                } catch (Exception e4) {
                                    if (backgroundThreadDrawHolder == null) {
                                        onBitmapException(bitmapDrawable2);
                                    }
                                    FileLog.e(e4);
                                }
                            }
                            drawBitmapDrawable(canvas, bitmapDrawable2, backgroundThreadDrawHolder, i);
                        }
                        canvas.restore();
                    } else {
                        int i17 = i5;
                        canvas.save();
                        if (i3 == 1) {
                            canvas.scale(-1.0f, 1.0f, f28 / 2.0f, f3 / 2.0f);
                        } else if (i3 == 2) {
                            canvas.scale(1.0f, -1.0f, f28 / 2.0f, f3 / 2.0f);
                        }
                        int i18 = i2 % 360;
                        if (i18 != 0) {
                            if (this.centerRotation) {
                                canvas.rotate(i2, f28 / 2.0f, f3 / 2.0f);
                            } else {
                                canvas.rotate(i2, 0.0f, 0.0f);
                            }
                        }
                        rectF.set(f, f2, f + f28, f2 + f3);
                        if (this.isRoundVideo) {
                            int i19 = AndroidUtilities.roundMessageInset;
                            rectF.inset(-i19, -i19);
                        }
                        if (z3) {
                            ((AnimatedFileDrawable) bitmapDrawable2).setActualDrawRect(f, f2, f28, f3);
                        }
                        if (backgroundThreadDrawHolder == null) {
                            if (i18 == 90 || i18 == 270) {
                                float width3 = rectF.width() / 2.0f;
                                float height3 = rectF.height() / 2.0f;
                                float centerX2 = rectF.centerX();
                                float centerY2 = rectF.centerY();
                                bitmapDrawable2.setBounds((int) (centerX2 - height3), (int) (centerY2 - width3), (int) (centerX2 + height3), (int) (centerY2 + width3));
                            } else {
                                bitmapDrawable2.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                            }
                        }
                        if (this.isVisible) {
                            if (i17 >= 29) {
                                try {
                                    if (this.blendMode != null) {
                                        bitmapDrawable2.getPaint().setBlendMode((BlendMode) this.blendMode);
                                    } else {
                                        bitmapDrawable2.getPaint().setBlendMode(null);
                                    }
                                } catch (Exception e5) {
                                    onBitmapException(bitmapDrawable2);
                                    FileLog.e(e5);
                                }
                            }
                            drawBitmapDrawable(canvas, bitmapDrawable2, backgroundThreadDrawHolder, i);
                        }
                        canvas.restore();
                    }
                }
            }
            if (z) {
                ((RLottieDrawable) drawable).skipFrameUpdate = false;
                return;
            } else if (drawable instanceof AnimatedFileDrawable) {
                ((AnimatedFileDrawable) drawable).skipFrameUpdate = false;
                return;
            } else {
                return;
            }
        }
        ImageReceiver imageReceiver = 1065353216;
        float f34 = 1.0f;
        if (backgroundThreadDrawHolder == null) {
            if (this.isAspectFit) {
                int intrinsicWidth2 = drawable.getIntrinsicWidth();
                int intrinsicHeight2 = drawable.getIntrinsicHeight();
                float f35 = this.sideClip;
                float f36 = f4 - (f35 * 2.0f);
                float f37 = f3 - (f35 * 2.0f);
                float f38 = f4 == 0.0f ? 1.0f : intrinsicWidth2 / f36;
                float f39 = f3 != 0.0f ? intrinsicHeight2 / f37 : 1.0f;
                float max3 = Math.max(f38, f39);
                float f40 = (int) (intrinsicWidth2 / max3);
                float f41 = (int) (intrinsicHeight2 / max3);
                rectF.set(((f4 - f40) / 2.0f) + f, ((f3 - f41) / 2.0f) + f2, f + ((f4 + f40) / 2.0f), f2 + ((f3 + f41) / 2.0f));
                f34 = f39;
            } else {
                rectF.set(f, f2, f4 + f, f3 + f2);
            }
            drawable.setBounds((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
            imageReceiver = f34;
        }
        if (!this.isVisible || canvas == null) {
            return;
        }
        if (drawable instanceof SvgHelper.SvgDrawable) {
            SvgHelper.SvgDrawable svgDrawable2 = (SvgHelper.SvgDrawable) drawable;
            svgDrawable2.setParent(this);
            svgDrawable = svgDrawable2;
        } else {
            svgDrawable = null;
        }
        try {
            drawable.setAlpha(i);
        } catch (Exception e6) {
            e = e6;
            imageReceiver = null;
        }
        try {
            if (backgroundThreadDrawHolder == null) {
                imageReceiver = 0;
                drawable.draw(canvas);
            } else if (svgDrawable != 0) {
                long j = backgroundThreadDrawHolder.time;
                if (j == 0) {
                    j = System.currentTimeMillis();
                }
                imageReceiver = 0;
                ((SvgHelper.SvgDrawable) drawable).drawInternal(canvas, true, backgroundThreadDrawHolder.threadIndex, j, backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY, backgroundThreadDrawHolder.imageW, backgroundThreadDrawHolder.imageH);
            } else {
                imageReceiver = 0;
                drawable.draw(canvas);
            }
        } catch (Exception e7) {
            e = e7;
            FileLog.e(e);
            if (svgDrawable == null) {
            }
        }
        if (svgDrawable == null) {
            svgDrawable.setParent(imageReceiver);
        }
    }

    private void drawBitmapDrawable(Canvas canvas, BitmapDrawable bitmapDrawable, BackgroundThreadDrawHolder backgroundThreadDrawHolder, int i) {
        if (backgroundThreadDrawHolder != null) {
            if (bitmapDrawable instanceof RLottieDrawable) {
                ((RLottieDrawable) bitmapDrawable).drawInBackground(canvas, backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY, backgroundThreadDrawHolder.imageW, backgroundThreadDrawHolder.imageH, i, backgroundThreadDrawHolder.colorFilter, backgroundThreadDrawHolder.threadIndex);
                return;
            } else if (bitmapDrawable instanceof AnimatedFileDrawable) {
                ((AnimatedFileDrawable) bitmapDrawable).drawInBackground(canvas, backgroundThreadDrawHolder.imageX, backgroundThreadDrawHolder.imageY, backgroundThreadDrawHolder.imageW, backgroundThreadDrawHolder.imageH, i, backgroundThreadDrawHolder.colorFilter, backgroundThreadDrawHolder.threadIndex);
                return;
            } else {
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
                    return;
                }
                return;
            }
        }
        bitmapDrawable.setAlpha(i);
        if (bitmapDrawable instanceof RLottieDrawable) {
            ((RLottieDrawable) bitmapDrawable).drawInternal(canvas, null, false, this.currentTime, 0);
        } else if (bitmapDrawable instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) bitmapDrawable).drawInternal(canvas, false, this.currentTime, 0);
        } else {
            bitmapDrawable.draw(canvas);
        }
    }

    public void setBlendMode(Object obj) {
        this.blendMode = obj;
        invalidate();
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
                    this.currentAlpha += ((float) j2) / this.crossfadeDuration;
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.ImageReceiver$$ExternalSyntheticLambda0
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

    public boolean draw(Canvas canvas) {
        return draw(canvas, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x0220, code lost:
        if (r38.useRoundForThumb == false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0222, code lost:
        if (r3 != null) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0224, code lost:
        updateDrawableRadius(r12);
        r3 = r38.staticThumbShader;
     */
    /* JADX WARN: Removed duplicated region for block: B:117:0x024c A[Catch: Exception -> 0x0369, TryCatch #0 {Exception -> 0x0369, blocks: (B:11:0x002a, B:23:0x00ed, B:26:0x00f4, B:38:0x0116, B:42:0x0120, B:62:0x017e, B:64:0x018a, B:68:0x019a, B:75:0x01a8, B:78:0x01ae, B:79:0x01b3, B:81:0x01e3, B:84:0x01e9, B:147:0x0323, B:151:0x032e, B:117:0x024c, B:119:0x0250, B:122:0x0255, B:124:0x0264, B:126:0x0278, B:128:0x027c, B:131:0x0284, B:136:0x0292, B:137:0x02aa, B:139:0x02ad, B:140:0x02c0, B:142:0x02ef, B:144:0x0306, B:123:0x025b, B:100:0x021e, B:103:0x0224, B:110:0x023a, B:113:0x0240, B:145:0x030a, B:155:0x0339, B:157:0x033d, B:158:0x0343, B:159:0x0359, B:48:0x0134, B:51:0x0145, B:53:0x0151, B:54:0x015b, B:56:0x015f, B:59:0x0165, B:60:0x016a, B:31:0x0104, B:34:0x010a, B:36:0x0111, B:12:0x0088, B:14:0x00ba, B:17:0x00c2), top: B:186:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x035f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0372  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x037b  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x038c A[LOOP:0: B:182:0x0384->B:184:0x038c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x018a A[Catch: Exception -> 0x0369, TryCatch #0 {Exception -> 0x0369, blocks: (B:11:0x002a, B:23:0x00ed, B:26:0x00f4, B:38:0x0116, B:42:0x0120, B:62:0x017e, B:64:0x018a, B:68:0x019a, B:75:0x01a8, B:78:0x01ae, B:79:0x01b3, B:81:0x01e3, B:84:0x01e9, B:147:0x0323, B:151:0x032e, B:117:0x024c, B:119:0x0250, B:122:0x0255, B:124:0x0264, B:126:0x0278, B:128:0x027c, B:131:0x0284, B:136:0x0292, B:137:0x02aa, B:139:0x02ad, B:140:0x02c0, B:142:0x02ef, B:144:0x0306, B:123:0x025b, B:100:0x021e, B:103:0x0224, B:110:0x023a, B:113:0x0240, B:145:0x030a, B:155:0x0339, B:157:0x033d, B:158:0x0343, B:159:0x0359, B:48:0x0134, B:51:0x0145, B:53:0x0151, B:54:0x015b, B:56:0x015f, B:59:0x0165, B:60:0x016a, B:31:0x0104, B:34:0x010a, B:36:0x0111, B:12:0x0088, B:14:0x00ba, B:17:0x00c2), top: B:186:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x019a A[Catch: Exception -> 0x0369, TryCatch #0 {Exception -> 0x0369, blocks: (B:11:0x002a, B:23:0x00ed, B:26:0x00f4, B:38:0x0116, B:42:0x0120, B:62:0x017e, B:64:0x018a, B:68:0x019a, B:75:0x01a8, B:78:0x01ae, B:79:0x01b3, B:81:0x01e3, B:84:0x01e9, B:147:0x0323, B:151:0x032e, B:117:0x024c, B:119:0x0250, B:122:0x0255, B:124:0x0264, B:126:0x0278, B:128:0x027c, B:131:0x0284, B:136:0x0292, B:137:0x02aa, B:139:0x02ad, B:140:0x02c0, B:142:0x02ef, B:144:0x0306, B:123:0x025b, B:100:0x021e, B:103:0x0224, B:110:0x023a, B:113:0x0240, B:145:0x030a, B:155:0x0339, B:157:0x033d, B:158:0x0343, B:159:0x0359, B:48:0x0134, B:51:0x0145, B:53:0x0151, B:54:0x015b, B:56:0x015f, B:59:0x0165, B:60:0x016a, B:31:0x0104, B:34:0x010a, B:36:0x0111, B:12:0x0088, B:14:0x00ba, B:17:0x00c2), top: B:186:0x0028 }] */
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
        boolean z2;
        float f;
        BitmapShader bitmapShader3;
        Drawable drawable;
        float f2;
        boolean z3;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        Drawable drawable5;
        BitmapShader bitmapShader4;
        int[] iArr;
        float f3;
        BitmapShader bitmapShader5;
        BitmapShader bitmapShader6;
        boolean z4;
        boolean z5;
        Drawable drawable6;
        int i2;
        int i3;
        int i4;
        Drawable drawable7;
        boolean z6;
        BackgroundThreadDrawHolder backgroundThreadDrawHolder2;
        boolean z7;
        Drawable drawable8;
        float f4;
        Drawable drawable9;
        Drawable drawable10;
        boolean z8;
        BitmapShader bitmapShader7;
        int i5;
        if (this.gradientBitmap != null && this.currentImageKey != null) {
            canvas.save();
            float f5 = this.imageX;
            float f6 = this.imageY;
            canvas.clipRect(f5, f6, this.imageW + f5, this.imageH + f6);
            canvas.drawColor(-16777216);
        }
        boolean z9 = backgroundThreadDrawHolder != null;
        try {
            if (!z9) {
                animation = getAnimation();
                lottieAnimation = getLottieAnimation();
                int[] iArr2 = this.roundRadius;
                Drawable drawable11 = this.currentMediaDrawable;
                bitmapShader = this.mediaShader;
                Drawable drawable12 = this.currentImageDrawable;
                bitmapShader2 = this.imageShader;
                Drawable drawable13 = this.currentThumbDrawable;
                BitmapShader bitmapShader8 = this.thumbShader;
                BitmapShader bitmapShader9 = this.staticThumbShader;
                boolean z10 = this.crossfadingWithThumb;
                Drawable drawable14 = this.crossfadeImage;
                Drawable drawable15 = this.staticThumbDrawable;
                float f7 = this.currentAlpha;
                float f8 = this.previousAlpha;
                BitmapShader bitmapShader10 = this.crossfadeShader;
                float f9 = this.overrideAlpha;
                z2 = ((animation == null || animation.hasBitmap()) && (lottieAnimation == null || lottieAnimation.hasBitmap())) ? false : true;
                f = f9;
                bitmapShader3 = bitmapShader9;
                drawable = drawable11;
                f2 = f7;
                z3 = z10;
                drawable2 = drawable15;
                drawable3 = drawable12;
                drawable4 = drawable13;
                drawable5 = drawable14;
                bitmapShader4 = bitmapShader8;
                iArr = iArr2;
                f3 = f8;
                bitmapShader5 = bitmapShader10;
            } else {
                animation = backgroundThreadDrawHolder.animation;
                lottieAnimation = backgroundThreadDrawHolder.lottieDrawable;
                int[] iArr3 = backgroundThreadDrawHolder.roundRadius;
                Drawable drawable16 = backgroundThreadDrawHolder.mediaDrawable;
                bitmapShader = backgroundThreadDrawHolder.mediaShader;
                Drawable drawable17 = backgroundThreadDrawHolder.imageDrawable;
                bitmapShader2 = backgroundThreadDrawHolder.imageShader;
                BitmapShader bitmapShader11 = backgroundThreadDrawHolder.thumbShader;
                BitmapShader bitmapShader12 = backgroundThreadDrawHolder.staticThumbShader;
                Drawable drawable18 = backgroundThreadDrawHolder.crossfadeImage;
                boolean unused = backgroundThreadDrawHolder.crossfadeWithOldImage;
                boolean z11 = backgroundThreadDrawHolder.crossfadingWithThumb;
                Drawable drawable19 = backgroundThreadDrawHolder.thumbDrawable;
                Drawable drawable20 = backgroundThreadDrawHolder.staticThumbDrawable;
                float f10 = backgroundThreadDrawHolder.currentAlpha;
                f3 = backgroundThreadDrawHolder.previousAlpha;
                iArr = iArr3;
                bitmapShader3 = bitmapShader12;
                drawable = drawable16;
                f2 = f10;
                bitmapShader5 = backgroundThreadDrawHolder.crossfadeShader;
                z2 = backgroundThreadDrawHolder.animationNotReady;
                drawable2 = drawable20;
                z3 = z11;
                bitmapShader4 = bitmapShader11;
                drawable5 = drawable18;
                drawable3 = drawable17;
                drawable4 = drawable19;
                f = backgroundThreadDrawHolder.overrideAlpha;
            }
            if (animation != null) {
                animation.setRoundRadius(iArr);
            }
            if (lottieAnimation == null || z9) {
                bitmapShader6 = bitmapShader;
            } else {
                bitmapShader6 = bitmapShader;
                lottieAnimation.setCurrentParentView(this.parentView);
            }
            if ((animation != null || lottieAnimation != null) && !z2 && !this.animationReadySent && !z9) {
                this.animationReadySent = true;
                ImageReceiverDelegate imageReceiverDelegate = this.delegate;
                if (imageReceiverDelegate != null) {
                    imageReceiverDelegate.onAnimationReady(this);
                }
            }
            z4 = this.forcePreview;
        } catch (Exception e) {
            e = e;
            z = false;
        }
        if (!z4 && drawable != null && !z2) {
            i4 = this.imageOrientation;
            i3 = this.imageInvert;
            drawable6 = drawable;
        } else {
            if (!z4 && drawable3 != null && (!z2 || drawable != null)) {
                i2 = this.imageOrientation;
                i3 = this.imageInvert;
                bitmapShader6 = bitmapShader2;
                drawable6 = drawable3;
                z5 = false;
            } else if (drawable5 != null && !z3) {
                i4 = this.imageOrientation;
                i3 = this.imageInvert;
                drawable6 = drawable5;
                bitmapShader6 = bitmapShader5;
            } else if (drawable4 != null) {
                i4 = this.thumbOrientation;
                i3 = this.thumbInvert;
                drawable6 = drawable4;
                bitmapShader6 = bitmapShader4;
            } else if (drawable2 instanceof BitmapDrawable) {
                if (this.useRoundForThumb && bitmapShader3 == null) {
                    updateDrawableRadius(drawable2);
                    bitmapShader3 = this.staticThumbShader;
                }
                i4 = this.thumbOrientation;
                i3 = this.thumbInvert;
                bitmapShader6 = bitmapShader3;
                drawable6 = drawable2;
            } else {
                z5 = z2;
                drawable6 = null;
                bitmapShader6 = null;
                i2 = 0;
                i3 = 0;
            }
            float f11 = this.crossfadeByScale;
            boolean z12 = z9;
            float min = f11 <= 0.0f ? Math.min((f11 * f2) + f2, 1.0f) : f2;
            if (drawable6 == null) {
                if (this.crossfadeAlpha != 0) {
                    if (f3 == 1.0f || (!(drawable6 == drawable3 || drawable6 == drawable) || drawable2 == null)) {
                        drawable8 = drawable6;
                        f4 = f2;
                        drawable9 = drawable4;
                        z6 = z5;
                        drawable10 = drawable5;
                    } else {
                        if (this.useRoundForThumb && bitmapShader3 == null) {
                            updateDrawableRadius(drawable2);
                            bitmapShader3 = this.staticThumbShader;
                        }
                        BitmapShader bitmapShader13 = bitmapShader3;
                        drawable8 = drawable6;
                        f4 = f2;
                        drawable9 = drawable4;
                        z6 = z5;
                        drawable10 = drawable5;
                        drawDrawable(canvas, drawable2, (int) (f * 255.0f), bitmapShader13, i2, i3, backgroundThreadDrawHolder);
                        bitmapShader3 = bitmapShader13;
                    }
                    boolean z13 = this.crossfadeWithThumb;
                    if (z13 && z6) {
                        drawDrawable(canvas, drawable8, (int) (f * 255.0f), bitmapShader6, i2, i3, backgroundThreadDrawHolder);
                        drawable7 = drawable8;
                    } else {
                        if (!z13 || min == 1.0f) {
                            drawable7 = drawable8;
                        } else {
                            Drawable drawable21 = drawable8;
                            if (drawable21 != drawable3 && drawable21 != drawable) {
                                if (drawable21 != drawable9 && drawable21 != drawable10) {
                                    if (drawable21 == drawable2 && drawable10 != null) {
                                        drawable2 = drawable10;
                                        bitmapShader7 = bitmapShader5;
                                        if (drawable2 != null) {
                                            if (!(drawable2 instanceof SvgHelper.SvgDrawable) && !(drawable2 instanceof Emoji.EmojiDrawable)) {
                                                i5 = (int) (f3 * f * 255.0f);
                                                drawable7 = drawable21;
                                                drawDrawable(canvas, drawable2, i5, bitmapShader7, this.thumbOrientation, this.thumbInvert, backgroundThreadDrawHolder);
                                                if (i5 != 255 && (drawable2 instanceof Emoji.EmojiDrawable)) {
                                                    drawable2.setAlpha(255);
                                                }
                                            }
                                            i5 = (int) ((1.0f - min) * f * 255.0f);
                                            drawable7 = drawable21;
                                            drawDrawable(canvas, drawable2, i5, bitmapShader7, this.thumbOrientation, this.thumbInvert, backgroundThreadDrawHolder);
                                            if (i5 != 255) {
                                                drawable2.setAlpha(255);
                                            }
                                        } else {
                                            drawable7 = drawable21;
                                        }
                                    }
                                    bitmapShader7 = null;
                                    drawable2 = null;
                                    if (drawable2 != null) {
                                    }
                                }
                                bitmapShader7 = null;
                                drawable2 = null;
                                if (drawable2 != null) {
                                }
                            }
                            Drawable drawable22 = drawable9;
                            if (drawable10 == null) {
                                if (drawable22 != null) {
                                    drawable2 = drawable22;
                                    bitmapShader7 = bitmapShader4;
                                    if (drawable2 != null) {
                                    }
                                } else {
                                    if (drawable2 != null) {
                                        if (this.useRoundForThumb && bitmapShader3 == null) {
                                            updateDrawableRadius(drawable2);
                                            bitmapShader3 = this.staticThumbShader;
                                        }
                                        bitmapShader7 = bitmapShader3;
                                        if (drawable2 != null) {
                                        }
                                    }
                                    bitmapShader7 = null;
                                    drawable2 = null;
                                    if (drawable2 != null) {
                                    }
                                }
                            }
                            drawable2 = drawable10;
                            bitmapShader7 = bitmapShader5;
                            if (drawable2 != null) {
                            }
                        }
                        if (this.crossfadeByScale <= 0.0f || min >= 1.0f || !z3) {
                            z8 = false;
                        } else {
                            canvas.save();
                            this.roundPath.rewind();
                            RectF rectF = AndroidUtilities.rectTmp;
                            float f12 = this.imageX;
                            float f13 = this.imageY;
                            rectF.set(f12, f13, this.imageW + f12, this.imageH + f13);
                            for (int i6 = 0; i6 < iArr.length; i6++) {
                                float[] fArr = radii;
                                int i7 = i6 * 2;
                                fArr[i7] = iArr[i6];
                                fArr[i7 + 1] = iArr[i6];
                            }
                            this.roundPath.addRoundRect(AndroidUtilities.rectTmp, radii, Path.Direction.CW);
                            canvas.clipPath(this.roundPath);
                            float interpolation = (this.crossfadeByScale * (1.0f - CubicBezierInterpolator.EASE_IN.getInterpolation(f4))) + 1.0f;
                            canvas.scale(interpolation, interpolation, getCenterX(), getCenterY());
                            z8 = true;
                        }
                        drawDrawable(canvas, drawable7, (int) (f * min * 255.0f), bitmapShader6, i2, i3, backgroundThreadDrawHolder);
                        if (z8) {
                            canvas.restore();
                        }
                    }
                } else {
                    drawable7 = drawable6;
                    z6 = z5;
                    drawDrawable(canvas, drawable7, (int) (f * 255.0f), bitmapShader6, i2, i3, backgroundThreadDrawHolder);
                }
                if (z6 && this.crossfadeWithThumb) {
                    backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
                    z7 = true;
                } else {
                    backgroundThreadDrawHolder2 = backgroundThreadDrawHolder;
                    z7 = false;
                }
                checkAlphaAnimation(z7, backgroundThreadDrawHolder2);
            } else {
                drawable7 = drawable6;
                z6 = z5;
                if (drawable2 != null) {
                    if (drawable2 instanceof VectorAvatarThumbDrawable) {
                        ((VectorAvatarThumbDrawable) drawable2).setParent(this);
                    }
                    drawDrawable(canvas, drawable2, (int) (f * 255.0f), null, this.thumbOrientation, this.thumbInvert, backgroundThreadDrawHolder);
                    checkAlphaAnimation(z6, backgroundThreadDrawHolder);
                } else {
                    checkAlphaAnimation(z6, backgroundThreadDrawHolder);
                    z = false;
                    if (drawable7 == null && z6 && !z12) {
                        try {
                            invalidate();
                        } catch (Exception e2) {
                            e = e2;
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
                            this.decorators.get(i).onDraw(canvas, this);
                        }
                    }
                    return z;
                }
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
        z5 = z2;
        i2 = i4;
        float f112 = this.crossfadeByScale;
        boolean z122 = z9;
        if (f112 <= 0.0f) {
        }
        if (drawable6 == null) {
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

    public void setManualAlphaAnimator(boolean z) {
        this.manualAlphaAnimator = z;
    }

    @Keep
    public float getCurrentAlpha() {
        return this.currentAlpha;
    }

    @Keep
    public void setCurrentAlpha(float f) {
        this.currentAlpha = f;
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

    /* JADX WARN: Removed duplicated region for block: B:42:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
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
        } else if (animation != null && animation.hasBitmap()) {
            Bitmap animatedBitmap = animation.getAnimatedBitmap();
            i = animation.getOrientation();
            if (i != 0) {
                return new BitmapHolder(Bitmap.createBitmap(animatedBitmap), (String) null, i);
            }
            bitmap = animatedBitmap;
        } else {
            Drawable drawable = this.currentMediaDrawable;
            if ((drawable instanceof BitmapDrawable) && !(drawable instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
                str = this.currentMediaKey;
            } else {
                Drawable drawable2 = this.currentImageDrawable;
                if ((drawable2 instanceof BitmapDrawable) && !(drawable2 instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
                    bitmap = ((BitmapDrawable) drawable2).getBitmap();
                    str = this.currentImageKey;
                } else {
                    Drawable drawable3 = this.currentThumbDrawable;
                    if ((drawable3 instanceof BitmapDrawable) && !(drawable3 instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
                        bitmap = ((BitmapDrawable) drawable3).getBitmap();
                        str = this.currentThumbKey;
                    } else {
                        Drawable drawable4 = this.staticThumbDrawable;
                        if (drawable4 instanceof BitmapDrawable) {
                            bitmap = ((BitmapDrawable) drawable4).getBitmap();
                        } else {
                            bitmap = null;
                            str = null;
                        }
                    }
                }
            }
            if (bitmap == null) {
                return new BitmapHolder(bitmap, str, i);
            }
            return null;
        }
        str = null;
        if (bitmap == null) {
        }
    }

    public BitmapHolder getDrawableSafe() {
        String str;
        String str2;
        Drawable drawable = this.currentMediaDrawable;
        if ((drawable instanceof BitmapDrawable) && !(drawable instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
            str = this.currentMediaKey;
        } else {
            Drawable drawable2 = this.currentImageDrawable;
            if ((drawable2 instanceof BitmapDrawable) && !(drawable2 instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
                str2 = this.currentImageKey;
            } else {
                drawable2 = this.currentThumbDrawable;
                if ((drawable2 instanceof BitmapDrawable) && !(drawable2 instanceof AnimatedFileDrawable) && !(drawable instanceof RLottieDrawable)) {
                    str2 = this.currentThumbKey;
                } else {
                    drawable = this.staticThumbDrawable;
                    if (drawable instanceof BitmapDrawable) {
                        str = null;
                    } else {
                        drawable = null;
                        str = null;
                    }
                }
            }
            Drawable drawable3 = drawable2;
            str = str2;
            drawable = drawable3;
        }
        if (drawable != null) {
            return new BitmapHolder(drawable, str, 0);
        }
        return null;
    }

    public Drawable getThumb() {
        return this.currentThumbDrawable;
    }

    public Bitmap getThumbBitmap() {
        Drawable drawable = this.currentThumbDrawable;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Drawable drawable2 = this.staticThumbDrawable;
        if (drawable2 instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable2).getBitmap();
        }
        return null;
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

    public int getBitmapWidth() {
        getDrawable();
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            int i = this.imageOrientation;
            return (i % 360 == 0 || i % 360 == 180) ? animation.getIntrinsicWidth() : animation.getIntrinsicHeight();
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            return lottieAnimation.getIntrinsicWidth();
        }
        Bitmap bitmap = getBitmap();
        if (bitmap == null) {
            Drawable drawable = this.staticThumbDrawable;
            if (drawable != null) {
                return drawable.getIntrinsicWidth();
            }
            return 1;
        }
        int i2 = this.imageOrientation;
        return (i2 % 360 == 0 || i2 % 360 == 180) ? bitmap.getWidth() : bitmap.getHeight();
    }

    public int getBitmapHeight() {
        getDrawable();
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            int i = this.imageOrientation;
            return (i % 360 == 0 || i % 360 == 180) ? animation.getIntrinsicHeight() : animation.getIntrinsicWidth();
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation != null) {
            return lottieAnimation.getIntrinsicHeight();
        }
        Bitmap bitmap = getBitmap();
        if (bitmap == null) {
            Drawable drawable = this.staticThumbDrawable;
            if (drawable != null) {
                return drawable.getIntrinsicHeight();
            }
            return 1;
        }
        int i2 = this.imageOrientation;
        return (i2 % 360 == 0 || i2 % 360 == 180) ? bitmap.getHeight() : bitmap.getWidth();
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

    public void getParentPosition(int[] iArr) {
        View view = this.parentView;
        if (view == null) {
            return;
        }
        view.getLocationInWindow(iArr);
    }

    public boolean getVisible() {
        return this.isVisible;
    }

    @Keep
    public void setAlpha(float f) {
        this.overrideAlpha = f;
    }

    @Keep
    public float getAlpha() {
        return this.overrideAlpha;
    }

    public void setCrossfadeAlpha(byte b) {
        this.crossfadeAlpha = b;
    }

    public boolean hasImageSet() {
        return (this.currentImageDrawable == null && this.currentMediaDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentImageKey == null && this.currentMediaKey == null) ? false : true;
    }

    public boolean hasBitmapImage() {
        return (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true;
    }

    public boolean hasImageLoaded() {
        return (this.currentImageDrawable == null && this.currentMediaDrawable == null) ? false : true;
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

    public void setAspectFit(boolean z) {
        this.isAspectFit = z;
    }

    public boolean isAspectFit() {
        return this.isAspectFit;
    }

    public void setParentView(View view) {
        this.parentView = view;
        AnimatedFileDrawable animation = getAnimation();
        if (animation == null || !this.attachedToWindow) {
            return;
        }
        animation.setParentView(this.parentView);
    }

    public void setImageX(float f) {
        this.imageX = f;
    }

    public void setImageY(float f) {
        this.imageY = f;
    }

    public void setImageWidth(int i) {
        this.imageW = i;
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

    public void setSideClip(float f) {
        this.sideClip = f;
    }

    public float getCenterX() {
        return this.imageX + (this.imageW / 2.0f);
    }

    public float getCenterY() {
        return this.imageY + (this.imageH / 2.0f);
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

    public float getImageWidth() {
        return this.imageW;
    }

    public float getImageHeight() {
        return this.imageH;
    }

    public float getImageAspectRatio() {
        float width;
        float height;
        if (this.imageOrientation % 180 != 0) {
            width = this.drawRegion.height();
            height = this.drawRegion.width();
        } else {
            width = this.drawRegion.width();
            height = this.drawRegion.height();
        }
        return width / height;
    }

    public String getExt() {
        return this.currentExt;
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

    public RectF getDrawRegion() {
        return this.drawRegion;
    }

    public int getNewGuid() {
        int i = this.currentGuid + 1;
        this.currentGuid = i;
        return i;
    }

    public String getImageKey() {
        return this.currentImageKey;
    }

    public String getMediaKey() {
        return this.currentMediaKey;
    }

    public String getThumbKey() {
        return this.currentThumbKey;
    }

    public long getSize() {
        return this.currentSize;
    }

    public ImageLocation getMediaLocation() {
        return this.currentMediaLocation;
    }

    public ImageLocation getImageLocation() {
        return this.currentImageLocation;
    }

    public ImageLocation getThumbLocation() {
        return this.currentThumbLocation;
    }

    public String getMediaFilter() {
        return this.currentMediaFilter;
    }

    public String getImageFilter() {
        return this.currentImageFilter;
    }

    public String getThumbFilter() {
        return this.currentThumbFilter;
    }

    public int getCacheType() {
        return this.currentCacheType;
    }

    public void setForcePreview(boolean z) {
        this.forcePreview = z;
    }

    public void setForceCrossfade(boolean z) {
        this.forceCrossfade = z;
    }

    public boolean isForcePreview() {
        return this.forcePreview;
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
            if (iArr2[i2] != iArr[i2]) {
                z = true;
            }
            if (i != iArr[i2]) {
                this.isRoundRect = false;
            }
            iArr2[i2] = iArr[i2];
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

    public void setMark(Object obj) {
        this.mark = obj;
    }

    public Object getMark() {
        return this.mark;
    }

    public void setCurrentAccount(int i) {
        this.currentAccount = i;
    }

    public int[] getRoundRadius() {
        return this.roundRadius;
    }

    public Object getParentObject() {
        return this.currentParentObject;
    }

    public void setNeedsQualityThumb(boolean z) {
        this.needsQualityThumb = z;
    }

    public void setQualityThumbDocument(TLRPC$Document tLRPC$Document) {
        this.qulityThumbDocument = tLRPC$Document;
    }

    public TLRPC$Document getQualityThumbDocument() {
        return this.qulityThumbDocument;
    }

    public void setCrossfadeWithOldImage(boolean z) {
        this.crossfadeWithOldImage = z;
    }

    public boolean isCrossfadingWithOldImage() {
        return (!this.crossfadeWithOldImage || this.crossfadeImage == null || this.crossfadingWithThumb) ? false : true;
    }

    public boolean isNeedsQualityThumb() {
        return this.needsQualityThumb;
    }

    public boolean isCurrentKeyQuality() {
        return this.currentKeyQuality;
    }

    public int getCurrentAccount() {
        return this.currentAccount;
    }

    public void setShouldGenerateQualityThumb(boolean z) {
        this.shouldGenerateQualityThumb = z;
    }

    public boolean isShouldGenerateQualityThumb() {
        return this.shouldGenerateQualityThumb;
    }

    public void setAllowStartAnimation(boolean z) {
        this.allowStartAnimation = z;
    }

    public void setAllowLottieVibration(boolean z) {
        this.allowLottieVibration = z;
    }

    public boolean getAllowStartAnimation() {
        return this.allowStartAnimation;
    }

    public void setAllowStartLottieAnimation(boolean z) {
        this.allowStartLottieAnimation = z;
    }

    public void setAllowDecodeSingleFrame(boolean z) {
        this.allowDecodeSingleFrame = z;
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

    public void setUseSharedAnimationQueue(boolean z) {
        this.useSharedAnimationQueue = z;
    }

    public boolean isAllowStartAnimation() {
        return this.allowStartAnimation;
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

    public void stopAnimation() {
        AnimatedFileDrawable animation = getAnimation();
        if (animation != null) {
            animation.stop();
            return;
        }
        RLottieDrawable lottieAnimation = getLottieAnimation();
        if (lottieAnimation == null || lottieAnimation.isRunning()) {
            return;
        }
        lottieAnimation.stop();
    }

    public boolean isAnimationRunning() {
        AnimatedFileDrawable animation = getAnimation();
        return animation != null && animation.isRunning();
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

    /* JADX INFO: Access modifiers changed from: protected */
    public int getTag(int i) {
        if (i == 1) {
            return this.thumbTag;
        }
        if (i == 3) {
            return this.mediaTag;
        }
        return this.imageTag;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTag(int i, int i2) {
        if (i2 == 1) {
            this.thumbTag = i;
        } else if (i2 == 3) {
            this.mediaTag = i;
        } else {
            this.imageTag = i;
        }
    }

    public void setParam(int i) {
        this.param = i;
    }

    public int getParam() {
        return this.param;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00b7, code lost:
        if ((r9 instanceof org.telegram.messenger.Emoji.EmojiDrawable) == false) goto L37;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0074  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
        Drawable drawable2;
        boolean z2;
        boolean z3;
        Drawable drawable3;
        if (drawable == null || str == null || this.currentGuid != i2) {
            return false;
        }
        if (i == 0) {
            if (!str.equals(this.currentImageKey)) {
                return false;
            }
            if (!(drawable instanceof AnimatedFileDrawable)) {
                ImageLoader.getInstance().incrementUseCount(this.currentImageKey);
                if (this.videoThumbIsSame && (drawable == this.currentImageDrawable || this.currentAlpha < 1.0f)) {
                    z2 = false;
                    this.currentImageDrawable = drawable;
                    if (drawable instanceof ExtendedBitmapDrawable) {
                        ExtendedBitmapDrawable extendedBitmapDrawable = (ExtendedBitmapDrawable) drawable;
                        this.imageOrientation = extendedBitmapDrawable.getOrientation();
                        this.imageInvert = extendedBitmapDrawable.getInvert();
                    }
                    updateDrawableRadius(drawable);
                    if (!z2 && this.isVisible && (((!z && !this.forcePreview) || this.forceCrossfade) && this.crossfadeDuration != 0)) {
                        Drawable drawable4 = this.currentMediaDrawable;
                        if (!(drawable4 instanceof RLottieDrawable) || !((RLottieDrawable) drawable4).hasBitmap()) {
                            Drawable drawable5 = this.currentMediaDrawable;
                            if (!(drawable5 instanceof AnimatedFileDrawable) || !((AnimatedFileDrawable) drawable5).hasBitmap()) {
                                if (this.currentImageDrawable instanceof RLottieDrawable) {
                                    Drawable drawable6 = this.staticThumbDrawable;
                                    if (!(drawable6 instanceof LoadingStickerDrawable)) {
                                        if (!(drawable6 instanceof SvgHelper.SvgDrawable)) {
                                        }
                                    }
                                }
                                z3 = true;
                                if (z3 && ((drawable3 = this.currentThumbDrawable) != null || this.staticThumbDrawable != null || this.forceCrossfade)) {
                                    if (drawable3 == null && this.staticThumbDrawable != null) {
                                        this.previousAlpha = this.currentAlpha;
                                    } else {
                                        this.previousAlpha = 1.0f;
                                    }
                                    this.currentAlpha = 0.0f;
                                    this.lastUpdateAlphaTime = System.currentTimeMillis();
                                    this.crossfadeWithThumb = (this.crossfadeImage != null && this.currentThumbDrawable == null && this.staticThumbDrawable == null) ? false : true;
                                }
                            }
                        }
                        z3 = false;
                        if (z3) {
                            if (drawable3 == null) {
                            }
                            this.previousAlpha = 1.0f;
                            this.currentAlpha = 0.0f;
                            this.lastUpdateAlphaTime = System.currentTimeMillis();
                            this.crossfadeWithThumb = (this.crossfadeImage != null && this.currentThumbDrawable == null && this.staticThumbDrawable == null) ? false : true;
                        }
                    } else {
                        this.currentAlpha = 1.0f;
                        this.previousAlpha = 1.0f;
                    }
                }
                z2 = true;
                this.currentImageDrawable = drawable;
                if (drawable instanceof ExtendedBitmapDrawable) {
                }
                updateDrawableRadius(drawable);
                if (!z2) {
                }
                this.currentAlpha = 1.0f;
                this.previousAlpha = 1.0f;
            } else {
                AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                animatedFileDrawable.setStartEndTime(this.startTime, this.endTime);
                if (animatedFileDrawable.isWebmSticker) {
                    ImageLoader.getInstance().incrementUseCount(this.currentImageKey);
                }
                if (this.videoThumbIsSame) {
                    z2 = !animatedFileDrawable.hasBitmap();
                    this.currentImageDrawable = drawable;
                    if (drawable instanceof ExtendedBitmapDrawable) {
                    }
                    updateDrawableRadius(drawable);
                    if (!z2) {
                    }
                    this.currentAlpha = 1.0f;
                    this.previousAlpha = 1.0f;
                }
                z2 = true;
                this.currentImageDrawable = drawable;
                if (drawable instanceof ExtendedBitmapDrawable) {
                }
                updateDrawableRadius(drawable);
                if (!z2) {
                }
                this.currentAlpha = 1.0f;
                this.previousAlpha = 1.0f;
            }
        } else if (i == 3) {
            if (!str.equals(this.currentMediaKey)) {
                return false;
            }
            if (!(drawable instanceof AnimatedFileDrawable)) {
                ImageLoader.getInstance().incrementUseCount(this.currentMediaKey);
            } else {
                AnimatedFileDrawable animatedFileDrawable2 = (AnimatedFileDrawable) drawable;
                animatedFileDrawable2.setStartEndTime(this.startTime, this.endTime);
                if (animatedFileDrawable2.isWebmSticker) {
                    ImageLoader.getInstance().incrementUseCount(this.currentMediaKey);
                }
                if (this.videoThumbIsSame) {
                    Drawable drawable7 = this.currentThumbDrawable;
                    if ((drawable7 instanceof AnimatedFileDrawable) || (this.currentImageDrawable instanceof AnimatedFileDrawable)) {
                        animatedFileDrawable2.seekTo(drawable7 instanceof AnimatedFileDrawable ? ((AnimatedFileDrawable) drawable7).getLastFrameTimestamp() : 0L, true, true);
                    }
                }
            }
            this.currentMediaDrawable = drawable;
            updateDrawableRadius(drawable);
            if (this.currentImageDrawable == null) {
                if ((!z && !this.forcePreview) || this.forceCrossfade) {
                    Drawable drawable8 = this.currentThumbDrawable;
                    if ((drawable8 == null && this.staticThumbDrawable == null) || this.currentAlpha == 1.0f || this.forceCrossfade) {
                        if (drawable8 != null && this.staticThumbDrawable != null) {
                            this.previousAlpha = this.currentAlpha;
                        } else {
                            this.previousAlpha = 1.0f;
                        }
                        this.currentAlpha = 0.0f;
                        this.lastUpdateAlphaTime = System.currentTimeMillis();
                        this.crossfadeWithThumb = (this.crossfadeImage == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null) ? false : true;
                    }
                } else {
                    this.currentAlpha = 1.0f;
                    this.previousAlpha = 1.0f;
                }
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
                if ((obj instanceof MessageObject) && ((MessageObject) obj).isRoundVideo() && ((MessageObject) this.currentParentObject).isSending()) {
                    this.currentAlpha = 1.0f;
                    this.previousAlpha = 1.0f;
                } else {
                    this.currentAlpha = 0.0f;
                    this.previousAlpha = 1.0f;
                    this.lastUpdateAlphaTime = System.currentTimeMillis();
                    this.crossfadeWithThumb = this.staticThumbDrawable != null;
                }
            } else {
                this.currentAlpha = 1.0f;
                this.previousAlpha = 1.0f;
            }
        }
        ImageReceiverDelegate imageReceiverDelegate = this.delegate;
        if (imageReceiverDelegate != null) {
            Drawable drawable10 = this.currentImageDrawable;
            imageReceiverDelegate.didSetImage(this, (drawable10 == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) ? false : true, drawable10 == null && this.currentMediaDrawable == null, z);
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

    public void setMediaStartEndTime(long j, long j2) {
        this.startTime = j;
        this.endTime = j2;
        Drawable drawable = this.currentMediaDrawable;
        if (drawable instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable) drawable).setStartEndTime(j, j2);
        }
    }

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
                    } else if (decrementUseCount2) {
                        animatedFileDrawable.recycle();
                    }
                } else if (animatedFileDrawable.getParents().isEmpty()) {
                    animatedFileDrawable.recycle();
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

    public void setCrossfadeDuration(int i) {
        this.crossfadeDuration = i;
    }

    public void setCrossfadeByScale(float f) {
        this.crossfadeByScale = f;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3;
        if (i == NotificationCenter.didReplacedPhotoInMemCache) {
            String str = (String) objArr[0];
            String str2 = this.currentMediaKey;
            if (str2 != null && str2.equals(str)) {
                this.currentMediaKey = (String) objArr[1];
                this.currentMediaLocation = (ImageLocation) objArr[2];
                SetImageBackup setImageBackup = this.setImageBackup;
                if (setImageBackup != null) {
                    setImageBackup.mediaLocation = (ImageLocation) objArr[2];
                }
            }
            String str3 = this.currentImageKey;
            if (str3 != null && str3.equals(str)) {
                this.currentImageKey = (String) objArr[1];
                this.currentImageLocation = (ImageLocation) objArr[2];
                SetImageBackup setImageBackup2 = this.setImageBackup;
                if (setImageBackup2 != null) {
                    setImageBackup2.imageLocation = (ImageLocation) objArr[2];
                }
            }
            String str4 = this.currentThumbKey;
            if (str4 == null || !str4.equals(str)) {
                return;
            }
            this.currentThumbKey = (String) objArr[1];
            this.currentThumbLocation = (ImageLocation) objArr[2];
            SetImageBackup setImageBackup3 = this.setImageBackup;
            if (setImageBackup3 != null) {
                setImageBackup3.thumbLocation = (ImageLocation) objArr[2];
            }
        } else if (i == NotificationCenter.stopAllHeavyOperations) {
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
                }
            }
        } else if (i == NotificationCenter.startAllHeavyOperations) {
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

    public void setUniqKeyPrefix(String str) {
        this.uniqKeyPrefix = str;
    }

    public String getUniqKeyPrefix() {
        return this.uniqKeyPrefix;
    }

    public void addLoadingImageRunnable(Runnable runnable) {
        this.loadingOperations.add(runnable);
    }

    public ArrayList<Runnable> getLoadingOperations() {
        return this.loadingOperations;
    }

    public void moveImageToFront() {
        ImageLoader.getInstance().moveToFront(this.currentImageKey);
        ImageLoader.getInstance().moveToFront(this.currentThumbKey);
    }

    public void moveLottieToFront() {
        BitmapDrawable bitmapDrawable;
        BitmapDrawable bitmapDrawable2;
        String str;
        Drawable drawable = this.currentMediaDrawable;
        String str2 = null;
        if (drawable instanceof RLottieDrawable) {
            bitmapDrawable2 = (BitmapDrawable) drawable;
            str = this.currentMediaKey;
        } else {
            Drawable drawable2 = this.currentImageDrawable;
            if (!(drawable2 instanceof RLottieDrawable)) {
                bitmapDrawable = null;
                if (str2 != null || bitmapDrawable == null) {
                }
                ImageLoader.getInstance().moveToFront(str2);
                if (ImageLoader.getInstance().isInMemCache(str2, true)) {
                    return;
                }
                ImageLoader.getInstance().getLottieMemCahce().put(str2, bitmapDrawable);
                return;
            }
            bitmapDrawable2 = (BitmapDrawable) drawable2;
            str = this.currentImageKey;
        }
        BitmapDrawable bitmapDrawable3 = bitmapDrawable2;
        str2 = str;
        bitmapDrawable = bitmapDrawable3;
        if (str2 != null) {
        }
    }

    public View getParentView() {
        return this.parentView;
    }

    public boolean isAttachedToWindow() {
        return this.attachedToWindow;
    }

    public void setVideoThumbIsSame(boolean z) {
        this.videoThumbIsSame = z;
    }

    public void setAllowLoadingOnAttachedOnly(boolean z) {
        this.allowLoadingOnAttachedOnly = z;
    }

    public void setSkipUpdateFrame(boolean z) {
        this.skipUpdateFrame = z;
    }

    public void setCurrentTime(long j) {
        this.currentTime = j;
    }

    public void setFileLoadingPriority(int i) {
        if (this.fileLoadingPriority != i) {
            this.fileLoadingPriority = i;
            if (this.attachedToWindow && hasImageSet()) {
                ImageLoader.getInstance().changeFileLoadingPriorityForImageReceiver(this);
            }
        }
    }

    public void bumpPriority() {
        ImageLoader.getInstance().changeFileLoadingPriorityForImageReceiver(this);
    }

    public int getFileLoadingPriority() {
        return this.fileLoadingPriority;
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

    public void addDecorator(Decorator decorator) {
        if (this.decorators == null) {
            this.decorators = new ArrayList<>();
        }
        this.decorators.add(decorator);
        if (this.attachedToWindow) {
            decorator.onAttachedToWindow(this);
        }
    }

    /* loaded from: classes.dex */
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
    }

    /* loaded from: classes.dex */
    public static class ReactionLastFrame extends BitmapDrawable {
        public static final float LAST_FRAME_SCALE = 1.2f;

        public ReactionLastFrame(Bitmap bitmap) {
            super(bitmap);
        }
    }
}
