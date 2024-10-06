package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public class RLottieImageView extends ImageView {
    private boolean attachedToWindow;
    private boolean autoRepeat;
    public boolean cached;
    private RLottieDrawable drawable;
    private ImageReceiver imageReceiver;
    private HashMap layerColors;
    private Integer layerNum;
    private boolean onlyLastFrame;
    private boolean playing;
    private boolean startOnAttach;

    public RLottieImageView(Context context) {
        super(context);
    }

    public void clearAnimationDrawable() {
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.stop();
        }
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.onDetachedFromWindow();
            this.imageReceiver = null;
        }
        this.drawable = null;
        setImageDrawable(null);
    }

    public void clearLayerColors() {
        this.layerColors.clear();
    }

    public RLottieDrawable getAnimatedDrawable() {
        return this.drawable;
    }

    public ImageReceiver getImageReceiver() {
        return this.imageReceiver;
    }

    public boolean isPlaying() {
        RLottieDrawable rLottieDrawable = this.drawable;
        return rLottieDrawable != null && rLottieDrawable.isRunning();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.onAttachedToWindow();
            if (this.playing) {
                this.imageReceiver.startAnimation();
            }
        }
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.setCallback(this);
            if (this.playing) {
                this.drawable.start();
            }
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.stop();
        }
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.onDetachedFromWindow();
        }
    }

    protected void onLoaded() {
    }

    public void playAnimation() {
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable == null && this.imageReceiver == null) {
            return;
        }
        this.playing = true;
        if (!this.attachedToWindow) {
            this.startOnAttach = true;
            return;
        }
        if (rLottieDrawable != null) {
            rLottieDrawable.start();
        }
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.startAnimation();
        }
    }

    public void replaceColors(int[] iArr) {
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.replaceColors(iArr);
        }
    }

    public void setAnimation(int i, int i2, int i3) {
        setAnimation(i, i2, i3, null);
    }

    public void setAnimation(int i, int i2, int i3, int[] iArr) {
        setAnimation(new RLottieDrawable(i, "" + i, AndroidUtilities.dp(i2), AndroidUtilities.dp(i3), false, iArr));
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x014f  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0183  */
    /* JADX WARN: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setAnimation(TLRPC.Document document, final int i, final int i2) {
        ImageLocation imageLocation;
        String str;
        SvgHelper.SvgDrawable svgThumb;
        String str2;
        int i3;
        ImageReceiver imageReceiver;
        ImageLocation imageLocation2;
        String str3;
        ImageLocation imageLocation3;
        String str4;
        long j;
        TLRPC.Document document2;
        ImageReceiver imageReceiver2 = this.imageReceiver;
        if (imageReceiver2 != null) {
            imageReceiver2.onDetachedFromWindow();
            this.imageReceiver = null;
        }
        if (document == null) {
            return;
        }
        ImageReceiver imageReceiver3 = new ImageReceiver() { // from class: org.telegram.ui.Components.RLottieImageView.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.ImageReceiver
            public boolean setImageBitmapByKey(Drawable drawable, String str5, int i4, boolean z, int i5) {
                if (drawable != null) {
                    RLottieImageView.this.onLoaded();
                }
                return super.setImageBitmapByKey(drawable, str5, i4, z, i5);
            }
        };
        this.imageReceiver = imageReceiver3;
        imageReceiver3.setAllowLoadingOnAttachedOnly(true);
        String str5 = document.localThumbPath;
        if (str5 != null) {
            imageLocation = ImageLocation.getForPath(str5);
            str = i + "_" + i2;
        } else {
            imageLocation = null;
            str = null;
        }
        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
        if (this.onlyLastFrame) {
            ImageReceiver imageReceiver4 = this.imageReceiver;
            ImageLocation forDocument = ImageLocation.getForDocument(document);
            ImageLocation forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
            str2 = null;
            imageReceiver = imageReceiver4;
            imageLocation2 = forDocument;
            str3 = i + "_" + i2 + "_lastframe";
            imageLocation3 = forDocument2;
            str4 = i + "_" + i2;
            svgThumb = null;
            j = 0;
            document2 = document;
            i3 = 1;
        } else {
            if ("video/webm".equals(document.mime_type)) {
                ImageReceiver imageReceiver5 = this.imageReceiver;
                ImageLocation forDocument3 = ImageLocation.getForDocument(document);
                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append("_");
                sb.append(i2);
                sb.append(this.cached ? "_pcache" : "");
                sb.append("_");
                sb.append(ImageLoader.AUTOPLAY_FILTER);
                imageReceiver5.setImage(forDocument3, sb.toString(), imageLocation != null ? imageLocation : ImageLocation.getForDocument(closestPhotoSizeWithSize, document), str, null, document.size, null, document, 1);
                this.imageReceiver.setAspectFit(true);
                this.imageReceiver.setParentView(this);
                if (this.autoRepeat) {
                    this.imageReceiver.setAutoRepeat(0);
                } else {
                    this.imageReceiver.setAutoRepeat(1);
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setAllowStartAnimation(true);
                }
                ImageReceiver imageReceiver6 = this.imageReceiver;
                Integer num = this.layerNum;
                imageReceiver6.setLayerNum(num == null ? num.intValue() : 7);
                this.imageReceiver.clip = false;
                setImageDrawable(new Drawable() { // from class: org.telegram.ui.Components.RLottieImageView.2
                    @Override // android.graphics.drawable.Drawable
                    public void draw(Canvas canvas) {
                        android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                        rect.set(getBounds().centerX() - (AndroidUtilities.dp(i) / 2), getBounds().centerY() - (AndroidUtilities.dp(i2) / 2), getBounds().centerX() + (AndroidUtilities.dp(i) / 2), getBounds().centerY() + (AndroidUtilities.dp(i2) / 2));
                        RLottieImageView.this.imageReceiver.setImageCoords(rect);
                        RLottieImageView.this.imageReceiver.draw(canvas);
                    }

                    @Override // android.graphics.drawable.Drawable
                    public int getOpacity() {
                        return -2;
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setAlpha(int i4) {
                        RLottieImageView.this.imageReceiver.setAlpha(i4 / 255.0f);
                    }

                    @Override // android.graphics.drawable.Drawable
                    public void setColorFilter(ColorFilter colorFilter) {
                        RLottieImageView.this.imageReceiver.setColorFilter(colorFilter);
                    }
                });
                if (this.attachedToWindow) {
                    return;
                }
                this.imageReceiver.onAttachedToWindow();
                return;
            }
            svgThumb = DocumentObject.getSvgThumb(document.thumbs, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f);
            if (svgThumb != null) {
                svgThumb.overrideWidthAndHeight(512, 512);
            }
            ImageReceiver imageReceiver7 = this.imageReceiver;
            ImageLocation forDocument4 = ImageLocation.getForDocument(document);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(i);
            sb2.append("_");
            sb2.append(i2);
            sb2.append(this.cached ? "_pcache" : "");
            String sb3 = sb2.toString();
            ImageLocation forDocument5 = ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
            str2 = null;
            i3 = 1;
            imageReceiver = imageReceiver7;
            imageLocation2 = forDocument4;
            str3 = sb3;
            imageLocation3 = forDocument5;
            str4 = i + "_" + i2;
            j = 0;
            document2 = document;
        }
        imageReceiver.setImage(imageLocation2, str3, imageLocation3, str4, imageLocation, str, svgThumb, j, str2, document2, i3);
        this.imageReceiver.setAspectFit(true);
        this.imageReceiver.setParentView(this);
        if (this.autoRepeat) {
        }
        ImageReceiver imageReceiver62 = this.imageReceiver;
        Integer num2 = this.layerNum;
        imageReceiver62.setLayerNum(num2 == null ? num2.intValue() : 7);
        this.imageReceiver.clip = false;
        setImageDrawable(new Drawable() { // from class: org.telegram.ui.Components.RLottieImageView.2
            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                rect.set(getBounds().centerX() - (AndroidUtilities.dp(i) / 2), getBounds().centerY() - (AndroidUtilities.dp(i2) / 2), getBounds().centerX() + (AndroidUtilities.dp(i) / 2), getBounds().centerY() + (AndroidUtilities.dp(i2) / 2));
                RLottieImageView.this.imageReceiver.setImageCoords(rect);
                RLottieImageView.this.imageReceiver.draw(canvas);
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -2;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i4) {
                RLottieImageView.this.imageReceiver.setAlpha(i4 / 255.0f);
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
                RLottieImageView.this.imageReceiver.setColorFilter(colorFilter);
            }
        });
        if (this.attachedToWindow) {
        }
    }

    public void setAnimation(RLottieDrawable rLottieDrawable) {
        if (this.drawable == rLottieDrawable) {
            return;
        }
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.onDetachedFromWindow();
            this.imageReceiver = null;
        }
        this.drawable = rLottieDrawable;
        rLottieDrawable.setMasterParent(this);
        if (this.autoRepeat) {
            this.drawable.setAutoRepeat(1);
        }
        if (this.layerColors != null) {
            this.drawable.beginApplyLayerColors();
            for (Map.Entry entry : this.layerColors.entrySet()) {
                this.drawable.setLayerColor((String) entry.getKey(), ((Integer) entry.getValue()).intValue());
            }
            this.drawable.commitApplyLayerColors();
        }
        this.drawable.setAllowDecodeSingleFrame(true);
        setImageDrawable(this.drawable);
    }

    public void setAutoRepeat(boolean z) {
        this.autoRepeat = z;
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        super.setImageResource(i);
        this.drawable = null;
    }

    public void setLayerColor(String str, int i) {
        if (this.layerColors == null) {
            this.layerColors = new HashMap();
        }
        this.layerColors.put(str, Integer.valueOf(i));
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.setLayerColor(str, i);
        }
    }

    public void setLayerNum(Integer num) {
        this.layerNum = num;
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.setLayerNum(num.intValue());
        }
    }

    public void setOnAnimationEndListener(Runnable runnable) {
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.setOnAnimationEndListener(runnable);
        }
    }

    public void setOnlyLastFrame(boolean z) {
        this.onlyLastFrame = z;
    }

    public void setProgress(float f) {
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.setProgress(f);
        }
    }

    public void stopAnimation() {
        RLottieDrawable rLottieDrawable = this.drawable;
        if (rLottieDrawable == null && this.imageReceiver == null) {
            return;
        }
        this.playing = false;
        if (!this.attachedToWindow) {
            this.startOnAttach = false;
            return;
        }
        if (rLottieDrawable != null) {
            rLottieDrawable.stop();
        }
        ImageReceiver imageReceiver = this.imageReceiver;
        if (imageReceiver != null) {
            imageReceiver.stopAnimation();
        }
    }
}
