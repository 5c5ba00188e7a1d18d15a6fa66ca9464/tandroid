package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Text;

/* loaded from: classes3.dex */
public class LinkPreview extends View {
    private boolean animated;
    public int backgroundColor;
    private final RectF bounds;
    private final AnimatedFloat captionAbove;
    private int currentAccount;
    public final float density;
    private StaticLayout descriptionLayout;
    private float descriptionLayoutLeft;
    private float descriptionLayoutWidth;
    private final TextPaint descriptionPaint;
    private final float flagIconPadding;
    public float h;
    private boolean hasDescription;
    public boolean hasPhoto;
    private boolean hasSiteName;
    private boolean hasTitle;
    private final AnimatedFloat height;
    private final Drawable icon;
    private final float iconPadding;
    private final float iconSize;
    private StaticLayout layout;
    private float layoutLeft;
    private final TextPaint layoutPaint;
    private float layoutWidth;
    public int maxWidth;
    private boolean messageAbove;
    private Text messageText;
    private final Paint outlinePaint;
    private final RectF padding;
    public final int padx;
    public final int pady;
    private final Path path;
    private final Path path2;
    private final AnimatedFloat photoAlphaProgress;
    private float photoHeight;
    private final ImageReceiver photoImage;
    private final AnimatedFloat photoSmallProgress;
    private float previewHeight;
    private final AnimatedFloat previewHeightProgress;
    private Paint previewPaint;
    private final AnimatedFloat previewProgress;
    private final AnimatedFloat previewTheme;
    public int previewType;
    private final RectF rect;
    private final RectF rect1;
    private final RectF rect2;
    private boolean relayout;
    private Text siteNameText;
    private boolean smallPhoto;
    private float textScale;
    private Text titleText;
    public int type;
    private boolean video;
    public float w;
    private WebPagePreview webpage;
    private final AnimatedFloat width;

    public static class WebPagePreview extends TLObject {
        public boolean captionAbove = true;
        public int flags;
        public boolean largePhoto;
        public String name;
        public int photoSize;
        public String url;
        public TLRPC.WebPage webpage;

        public static WebPagePreview TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
            if (-625858389 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in WebPagePreview", Integer.valueOf(i)));
                }
                return null;
            }
            WebPagePreview webPagePreview = new WebPagePreview();
            webPagePreview.readParams(abstractSerializedData, z);
            return webPagePreview;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
            int readInt32 = abstractSerializedData.readInt32(z);
            this.flags = readInt32;
            this.largePhoto = (readInt32 & 8) != 0;
            this.captionAbove = (readInt32 & 16) != 0;
            this.url = abstractSerializedData.readString(z);
            if ((this.flags & 1) != 0) {
                this.webpage = TLRPC.WebPage.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            }
            if ((this.flags & 2) != 0) {
                this.name = abstractSerializedData.readString(z);
            }
            if ((this.flags & 4) != 0) {
                this.photoSize = abstractSerializedData.readInt32(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
            abstractSerializedData.writeInt32(-625858389);
            this.flags = this.webpage != null ? this.flags | 1 : this.flags & (-2);
            int i = !TextUtils.isEmpty(this.name) ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            int i2 = this.largePhoto ? i | 8 : i & (-9);
            this.flags = i2;
            int i3 = this.captionAbove ? i2 | 16 : i2 & (-17);
            this.flags = i3;
            abstractSerializedData.writeInt32(i3);
            abstractSerializedData.writeString(this.url);
            if ((this.flags & 1) != 0) {
                this.webpage.serializeToStream(abstractSerializedData);
            }
            if ((this.flags & 2) != 0) {
                abstractSerializedData.writeString(this.name);
            }
            if ((this.flags & 4) != 0) {
                abstractSerializedData.writeInt32(this.photoSize);
            }
        }
    }

    public LinkPreview(Context context, float f) {
        super(context);
        this.relayout = true;
        this.textScale = 1.0f;
        TextPaint textPaint = new TextPaint(1);
        this.layoutPaint = textPaint;
        this.padding = new RectF(4.0f, 4.33f, 7.66f, 3.0f);
        this.iconPadding = 3.25f;
        this.flagIconPadding = 2.25f;
        this.iconSize = 30.0f;
        this.outlinePaint = new Paint(1);
        this.previewPaint = new Paint(1);
        this.descriptionPaint = new TextPaint(1);
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.photoImage = imageReceiver;
        this.bounds = new RectF();
        this.rect = new RectF();
        this.path = new Path();
        this.path2 = new Path();
        this.rect1 = new RectF();
        this.rect2 = new RectF();
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.captionAbove = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.photoAlphaProgress = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.photoSmallProgress = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.previewProgress = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.previewTheme = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.previewHeightProgress = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.width = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.height = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.density = f;
        imageReceiver.setInvalidateAll(true);
        this.padx = (int) (f * 3.0f);
        this.pady = (int) (f * 1.0f);
        this.icon = context.getResources().getDrawable(R.drawable.story_link).mutate();
        textPaint.setTextSize(24.0f * f);
        textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rcondensedbold.ttf"));
    }

    public static String fromUrl(String str) {
        return str;
    }

    public static String fromUrlWithoutSchema(String str) {
        return str.startsWith("https://") ? str.substring(8) : str;
    }

    @Override // android.view.View
    protected void dispatchDraw(Canvas canvas) {
        drawInternal(canvas);
    }

    public void drawInternal(Canvas canvas) {
        int i;
        Text text;
        Text text2;
        setupLayout();
        float f = this.width.set(this.w);
        float f2 = this.height.set(this.h);
        float f3 = this.previewTheme.set(this.previewType == 0);
        float f4 = this.previewProgress.set(withPreview());
        float lerp = AndroidUtilities.lerp(0.2f * f2, this.density * 16.66f, f4);
        RectF rectF = this.bounds;
        float f5 = this.padx;
        float f6 = this.pady;
        rectF.set(f5, f6, f5 + f, f6 + f2);
        this.outlinePaint.setColor(ColorUtils.blendARGB(this.backgroundColor, ColorUtils.blendARGB(-1, -14670807, f3), f4));
        this.path2.rewind();
        Path path = this.path2;
        RectF rectF2 = this.bounds;
        Path.Direction direction = Path.Direction.CW;
        path.addRoundRect(rectF2, lerp, lerp, direction);
        canvas.drawPath(this.path2, this.outlinePaint);
        if (f4 > 0.0f) {
            canvas.save();
            canvas.clipPath(this.path2);
            canvas.translate(this.padx, this.pady);
            float f7 = this.captionAbove.set(this.messageAbove);
            float f8 = this.density;
            float f9 = (7.33f * f8) + 0.0f;
            Text text3 = this.messageText;
            if (text3 != null && f7 > 0.0f) {
                text3.draw(canvas, f8 * 10.0f, (f9 + (text3.getHeight() / 2.0f)) - ((this.messageText.getHeight() + (this.density * 15.0f)) * (1.0f - f7)), -15033089, f4);
                f9 += (this.messageText.getHeight() + (this.density * 7.0f)) * f7;
            }
            float f10 = f9;
            float f11 = this.previewHeightProgress.set(this.previewHeight);
            this.previewPaint.setAlpha(25);
            RectF rectF3 = this.rect;
            float f12 = this.density * 10.0f;
            float f13 = f10 + f11;
            rectF3.set(f12, f10, f - f12, f13);
            this.path.rewind();
            Path path2 = this.path;
            RectF rectF4 = this.rect;
            float f14 = this.density * 5.0f;
            path2.addRoundRect(rectF4, f14, f14, direction);
            canvas.drawPath(this.path, this.previewPaint);
            canvas.save();
            canvas.clipPath(this.path);
            this.previewPaint.setAlpha(NotificationCenter.notificationsCountUpdated);
            float f15 = this.density;
            canvas.drawRect(f15 * 10.0f, f10, f15 * 13.0f, f13, this.previewPaint);
            canvas.restore();
            float f16 = this.density;
            float f17 = f10 + (5.66f * f16);
            if (this.hasSiteName && (text2 = this.siteNameText) != null) {
                text2.draw(canvas, f16 * 20.0f, f17 + (text2.getHeight() / 2.0f), this.previewPaint.getColor(), f4);
                f17 += this.siteNameText.getHeight() + (this.density * 2.66f);
            }
            if (!this.hasTitle || (text = this.titleText) == null) {
                i = -13421773;
            } else {
                i = -13421773;
                text.draw(canvas, this.density * 20.0f, f17 + (text.getHeight() / 2.0f), ColorUtils.blendARGB(-13421773, -1, f3), f4);
                f17 += this.titleText.getHeight() + (this.density * 2.66f);
            }
            if (this.hasDescription && this.descriptionLayout != null) {
                canvas.save();
                canvas.translate((this.density * 20.0f) - this.descriptionLayoutLeft, f17);
                this.descriptionPaint.setColor(ColorUtils.blendARGB(i, -1, f3));
                this.descriptionPaint.setAlpha((int) (f4 * 255.0f));
                this.descriptionLayout.draw(canvas);
                canvas.restore();
                f17 += this.descriptionLayout.getHeight() + (this.density * 2.66f);
            }
            float f18 = this.photoAlphaProgress.set(this.hasPhoto);
            if (f18 > 0.0f) {
                float f19 = this.photoSmallProgress.set(this.smallPhoto);
                RectF rectF5 = this.rect1;
                float f20 = this.density;
                float f21 = f20 * 20.0f;
                float f22 = (f20 * 2.66f) + f17;
                rectF5.set(f21, f22, f - f21, this.photoHeight + f22);
                RectF rectF6 = this.rect2;
                float f23 = this.density;
                float f24 = 6.0f * f23;
                float f25 = (f - (f23 * 10.0f)) - f24;
                float f26 = f23 * 48.0f;
                float f27 = f10 + f24;
                rectF6.set(f25 - f26, f27, f25, f26 + f27);
                AndroidUtilities.lerp(this.rect1, this.rect2, f19, this.rect);
                ImageReceiver imageReceiver = this.photoImage;
                RectF rectF7 = this.rect;
                imageReceiver.setImageCoords(rectF7.left, rectF7.top, rectF7.width(), this.rect.height());
                this.photoImage.setAlpha(f18 * f4);
                this.photoImage.draw(canvas);
                f17 += (1.0f - f19) * ((this.density * 2.66f) + this.photoHeight);
            }
            float f28 = this.density;
            float f29 = f17 + (7.0f * f28) + (5.0f * f28);
            Text text4 = this.messageText;
            if (text4 != null && 1.0f - f7 > 0.0f) {
                text4.draw(canvas, f28 * 10.0f, f29 + (text4.getHeight() / 2.0f) + ((this.messageText.getHeight() + (this.density * 15.0f)) * f7), -15033089, f4);
                this.messageText.getHeight();
            }
            canvas.restore();
        }
        if (f4 < 1.0f) {
            Drawable drawable = this.icon;
            int i2 = this.padx;
            float f30 = this.padding.left;
            float f31 = this.density;
            int i3 = this.pady;
            float f32 = f31 * 30.0f;
            drawable.setBounds(((int) (f30 * f31)) + i2, ((int) ((f2 - f32) / 2.0f)) + i3, i2 + ((int) ((f30 + 30.0f) * f31)), i3 + ((int) ((f32 + f2) / 2.0f)));
            int i4 = (int) ((1.0f - f4) * 255.0f);
            this.icon.setAlpha(i4);
            this.icon.draw(canvas);
            if (this.layout != null) {
                canvas.save();
                canvas.translate(this.padx + ((this.padding.left + 30.0f + 3.25f) * this.density), this.pady + (f2 / 2.0f));
                float f33 = this.textScale;
                canvas.scale(f33, f33);
                canvas.translate(-this.layoutLeft, (-this.layout.getHeight()) / 2.0f);
                this.layoutPaint.setAlpha(i4);
                this.layout.draw(canvas);
                canvas.restore();
            }
        }
    }

    public int getPhotoSide() {
        float f;
        if (this.smallPhoto) {
            f = 48.0f;
        } else {
            int i = this.maxWidth;
            int i2 = this.padx;
            f = (((i - i2) - i2) / this.density) - 40.0f;
        }
        return ((int) f) * 2;
    }

    public int getPreviewType() {
        return this.previewType;
    }

    public float getRadius() {
        float f;
        float f2;
        if (withPreview()) {
            f = this.density;
            f2 = 16.66f;
        } else {
            f = this.h;
            f2 = 0.2f;
        }
        return f * f2;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.photoImage.onAttachedToWindow();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.photoImage.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setupLayout();
        setMeasuredDimension(this.padx + ((int) Math.ceil(this.w)) + this.padx, this.pady + ((int) Math.ceil(this.h)) + this.pady);
    }

    public void pushPhotoToCache() {
        if (this.hasPhoto && this.photoImage.hasImageLoaded() && this.photoImage.getBitmap() != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(this.photoImage.getBitmap()), this.photoImage.getImageKey(), false);
        }
    }

    public void set(int i, WebPagePreview webPagePreview) {
        set(i, webPagePreview, false);
    }

    public void set(int i, WebPagePreview webPagePreview, boolean z) {
        this.currentAccount = i;
        if (this.webpage != webPagePreview || z) {
            this.webpage = webPagePreview;
            this.relayout = true;
            this.animated = z;
            requestLayout();
        }
    }

    public void setMaxWidth(int i) {
        this.maxWidth = i;
        this.relayout = true;
    }

    public void setPreviewType(int i) {
        this.previewType = i;
        invalidate();
    }

    public void setType(int i, int i2) {
        Drawable drawable;
        PorterDuffColorFilter porterDuffColorFilter;
        if (this.type == 1) {
            return;
        }
        if (i == 0) {
            this.backgroundColor = i2;
            int i3 = AndroidUtilities.computePerceivedBrightness(i2) < 0.721f ? -1 : -16777216;
            this.layoutPaint.setColor(i3);
            drawable = this.icon;
            porterDuffColorFilter = new PorterDuffColorFilter(i3, PorterDuff.Mode.SRC_IN);
        } else if (i == 1) {
            this.backgroundColor = -16777216;
            this.layoutPaint.setColor(-1);
            drawable = this.icon;
            porterDuffColorFilter = new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN);
        } else {
            if (i != 2) {
                this.backgroundColor = -1;
                this.layoutPaint.setColor(-13397548);
                this.icon.setColorFilter(new PorterDuffColorFilter(-13397548, PorterDuff.Mode.SRC_IN));
                invalidate();
            }
            this.backgroundColor = 1275068416;
            this.layoutPaint.setColor(-1);
            drawable = this.icon;
            porterDuffColorFilter = null;
        }
        drawable.setColorFilter(porterDuffColorFilter);
        invalidate();
    }

    public void setVideoTexture() {
        this.video = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x020d  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x03c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setupLayout() {
        int color1;
        int i;
        int i2;
        ImageLocation forDocument;
        String str;
        ImageLocation forDocument2;
        ImageLocation imageLocation;
        ImageReceiver imageReceiver;
        ImageReceiver imageReceiver2;
        boolean z;
        int i3;
        boolean z2;
        boolean z3;
        if (!this.relayout || this.webpage == null) {
            return;
        }
        if (withPreview()) {
            String fromUrl = TextUtils.isEmpty(this.webpage.name) ? fromUrl(this.webpage.url) : this.webpage.name;
            TLRPC.WebPage webPage = this.webpage.webpage;
            int i4 = this.maxWidth;
            int i5 = this.padx;
            float f = (i4 - i5) - i5;
            this.h = 0.0f;
            this.w = 0.0f;
            this.previewHeight = 0.0f;
            int colorId = UserObject.getColorId(UserConfig.getInstance(this.currentAccount).getCurrentUser());
            MessagesController.PeerColors peerColors = MessagesController.getInstance(this.currentAccount).peerColors;
            String str2 = null;
            MessagesController.PeerColor color = (peerColors == null || colorId < 7) ? null : peerColors.getColor(colorId);
            Paint paint = this.previewPaint;
            if (color == null) {
                int[] iArr = Theme.keys_avatar_nameInMessage;
                color1 = Theme.getColor(iArr[colorId % iArr.length]);
            } else {
                color1 = color.getColor1();
            }
            paint.setColor(color1);
            this.h += this.density * 7.33f;
            this.messageAbove = this.webpage.captionAbove;
            Text maxWidth = new Text(fromUrl, 16.0f).setTextSizePx(this.density * 16.0f).setMaxWidth(f - (this.density * 20.0f));
            this.messageText = maxWidth;
            this.w = Math.max(this.w, Math.min(maxWidth.getCurrentWidth() + (this.density * 20.0f), f));
            this.h = this.h + this.messageText.getHeight() + (this.density * 7.0f);
            this.hasPhoto = webPage.photo != null || MessageObject.isVideoDocument(webPage.document);
            WebPagePreview webPagePreview = this.webpage;
            boolean z4 = !webPagePreview.largePhoto;
            this.smallPhoto = z4;
            int i6 = (!this.video || (webPagePreview.flags & 4) == 0) ? ((int) (z4 ? 48.0f : (f / this.density) - 40.0f)) * 2 : webPagePreview.photoSize;
            this.photoImage.setRoundRadius((int) (this.density * 4.0f));
            TLRPC.Photo photo = webPage.photo;
            if (photo != null) {
                TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 1, false, null, false);
                TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(webPage.photo.sizes, (int) (i6 * this.density), false, closestPhotoSizeWithSize, false);
                if (closestPhotoSizeWithSize2 != null) {
                    i = closestPhotoSizeWithSize2.w;
                    i2 = closestPhotoSizeWithSize2.h;
                } else {
                    i = 0;
                    i2 = 0;
                }
                imageReceiver2 = this.photoImage;
                forDocument = ImageLocation.getForPhoto(closestPhotoSizeWithSize2, webPage.photo);
                str = i6 + "_" + i6;
                forDocument2 = this.video ? null : ImageLocation.getForPhoto(closestPhotoSizeWithSize, webPage.photo);
                if (!this.video) {
                    str2 = i6 + "_" + i6;
                }
            } else {
                TLRPC.Document document = webPage.document;
                if (document != null) {
                    TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 1, false, null, false);
                    TLRPC.PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(webPage.document.thumbs, (int) (i6 * this.density), false, closestPhotoSizeWithSize3, false);
                    if (closestPhotoSizeWithSize4 != null) {
                        i = closestPhotoSizeWithSize4.w;
                        i2 = closestPhotoSizeWithSize4.h;
                    } else {
                        i = 0;
                        i2 = 0;
                    }
                    ImageReceiver imageReceiver3 = this.photoImage;
                    forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize4, webPage.document);
                    str = i6 + "_" + i6;
                    forDocument2 = this.video ? null : ImageLocation.getForDocument(closestPhotoSizeWithSize3, webPage.document);
                    if (this.video) {
                        imageReceiver2 = imageReceiver3;
                    } else {
                        str2 = i6 + "_" + i6;
                        imageLocation = forDocument2;
                        imageReceiver = imageReceiver3;
                        imageReceiver.setImage(forDocument, str, imageLocation, str2, 0L, null, null, 0);
                        this.previewHeight += this.density * 5.66f;
                        z = !TextUtils.isEmpty(webPage.site_name);
                        this.hasSiteName = z;
                        if (z) {
                            Text textSizePx = new Text(webPage.site_name, 14.0f, AndroidUtilities.bold()).setTextSizePx(this.density * 14.0f);
                            float f2 = this.density;
                            Text maxWidth2 = textSizePx.setMaxWidth((int) Math.ceil((f - (f2 * 40.0f)) - ((this.hasPhoto && this.smallPhoto) ? f2 * 60.0f : 0.0f)));
                            this.siteNameText = maxWidth2;
                            float f3 = this.w;
                            float currentWidth = maxWidth2.getCurrentWidth();
                            float f4 = this.density;
                            this.w = Math.max(f3, Math.min(currentWidth + (f4 * 40.0f) + ((this.hasPhoto && this.smallPhoto) ? f4 * 60.0f : 0.0f), f));
                            this.previewHeight = this.previewHeight + this.siteNameText.getHeight() + (this.density * 2.66f);
                            i3 = this.siteNameText.getLineCount();
                        } else {
                            i3 = 0;
                        }
                        z2 = !TextUtils.isEmpty(webPage.title);
                        this.hasTitle = z2;
                        if (z2) {
                            Text textSizePx2 = new Text(webPage.title, 14.0f, AndroidUtilities.bold()).setTextSizePx(this.density * 14.0f);
                            float f5 = this.density;
                            Text maxWidth3 = textSizePx2.setMaxWidth((int) Math.ceil((f - (f5 * 40.0f)) - ((this.hasPhoto && this.smallPhoto) ? f5 * 60.0f : 0.0f)));
                            this.titleText = maxWidth3;
                            float f6 = this.w;
                            float currentWidth2 = maxWidth3.getCurrentWidth();
                            float f7 = this.density;
                            this.w = Math.max(f6, Math.min(currentWidth2 + (f7 * 40.0f) + ((this.hasPhoto && this.smallPhoto) ? 60.0f * f7 : 0.0f), f));
                            this.previewHeight = this.previewHeight + this.titleText.getHeight() + (this.density * 2.66f);
                            i3 += this.titleText.getLineCount();
                        }
                        z3 = !TextUtils.isEmpty(webPage.description);
                        this.hasDescription = z3;
                        if (z3) {
                            this.descriptionPaint.setTextSize(this.density * 14.0f);
                            int i7 = 3 - i3;
                            this.descriptionLayout = ChatMessageCell.generateStaticLayout(webPage.description, this.descriptionPaint, (int) Math.ceil(Math.max(1.0f, f - (this.density * 40.0f))), (int) Math.ceil(Math.max(1.0f, f - ((40 + ((this.hasPhoto && this.smallPhoto) ? 60 : 0)) * this.density))), i7, 4);
                            this.descriptionLayoutWidth = 0.0f;
                            this.descriptionLayoutLeft = Float.MAX_VALUE;
                            int i8 = 0;
                            while (i8 < this.descriptionLayout.getLineCount()) {
                                this.descriptionLayoutWidth = Math.max(this.descriptionLayoutWidth, this.descriptionLayout.getLineWidth(i8) + (this.hasPhoto && this.smallPhoto && i8 < i7 ? this.density * 48.0f : 0.0f));
                                this.descriptionLayoutLeft = Math.min(this.descriptionLayoutLeft, this.descriptionLayout.getLineLeft(i8));
                                i8++;
                            }
                            this.w = Math.max(this.w, Math.min(this.descriptionLayoutWidth + (this.density * 40.0f), f));
                            this.previewHeight = this.previewHeight + this.descriptionLayout.getHeight() + (this.density * 2.66f);
                        }
                        if (this.hasPhoto && !this.smallPhoto) {
                            this.photoHeight = (i > 0 || i2 <= 0) ? this.density * 120.0f : Math.min((Math.max(0.0f, this.w - (this.density * 40.0f)) / i) * i2, this.density * 200.0f);
                            this.previewHeight = this.previewHeight + this.photoHeight + (this.density * 2.66f);
                        }
                        float f8 = this.previewHeight;
                        float f9 = this.density;
                        float f10 = f8 + (f9 * 7.0f);
                        this.previewHeight = f10;
                        this.h = this.h + f10 + (f9 * 11.0f);
                    }
                } else {
                    i = 0;
                    i2 = 0;
                    this.previewHeight += this.density * 5.66f;
                    z = !TextUtils.isEmpty(webPage.site_name);
                    this.hasSiteName = z;
                    if (z) {
                    }
                    z2 = !TextUtils.isEmpty(webPage.title);
                    this.hasTitle = z2;
                    if (z2) {
                    }
                    z3 = !TextUtils.isEmpty(webPage.description);
                    this.hasDescription = z3;
                    if (z3) {
                    }
                    if (this.hasPhoto) {
                        this.photoHeight = (i > 0 || i2 <= 0) ? this.density * 120.0f : Math.min((Math.max(0.0f, this.w - (this.density * 40.0f)) / i) * i2, this.density * 200.0f);
                        this.previewHeight = this.previewHeight + this.photoHeight + (this.density * 2.66f);
                    }
                    float f82 = this.previewHeight;
                    float f92 = this.density;
                    float f102 = f82 + (f92 * 7.0f);
                    this.previewHeight = f102;
                    this.h = this.h + f102 + (f92 * 11.0f);
                }
            }
            imageReceiver = imageReceiver2;
            imageLocation = forDocument2;
            imageReceiver.setImage(forDocument, str, imageLocation, str2, 0L, null, null, 0);
            this.previewHeight += this.density * 5.66f;
            z = !TextUtils.isEmpty(webPage.site_name);
            this.hasSiteName = z;
            if (z) {
            }
            z2 = !TextUtils.isEmpty(webPage.title);
            this.hasTitle = z2;
            if (z2) {
            }
            z3 = !TextUtils.isEmpty(webPage.description);
            this.hasDescription = z3;
            if (z3) {
            }
            if (this.hasPhoto) {
            }
            float f822 = this.previewHeight;
            float f922 = this.density;
            float f1022 = f822 + (f922 * 7.0f);
            this.previewHeight = f1022;
            this.h = this.h + f1022 + (f922 * 11.0f);
        } else {
            String upperCase = TextUtils.isEmpty(this.webpage.name) ? fromUrlWithoutSchema(this.webpage.url).toUpperCase() : this.webpage.name;
            int i9 = this.maxWidth;
            int i10 = this.padx;
            float f11 = (i9 - i10) - i10;
            RectF rectF = this.padding;
            float f12 = f11 - ((((rectF.left + 30.0f) + 3.25f) + rectF.right) * this.density);
            this.textScale = 1.0f;
            this.layout = new StaticLayout(TextUtils.ellipsize(upperCase, this.layoutPaint, (int) Math.ceil(r10), TextUtils.TruncateAt.END), this.layoutPaint, (int) Math.ceil(f12), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.layoutWidth = 0.0f;
            this.layoutLeft = Float.MAX_VALUE;
            for (int i11 = 0; i11 < this.layout.getLineCount(); i11++) {
                this.layoutWidth = Math.max(this.layoutWidth, this.layout.getLineWidth(i11));
                this.layoutLeft = Math.min(this.layoutLeft, this.layout.getLineLeft(i11));
            }
            this.textScale = this.layout.getLineCount() > 2 ? 0.3f : Math.min(1.0f, f12 / this.layoutWidth);
            RectF rectF2 = this.padding;
            float f13 = rectF2.left + 30.0f + 3.25f + rectF2.right;
            float f14 = this.density;
            this.w = (f13 * f14) + (this.layoutWidth * this.textScale);
            this.h = ((rectF2.top + rectF2.bottom) * f14) + Math.max(f14 * 30.0f, this.layout.getHeight() * this.textScale);
        }
        if (this.animated) {
            invalidate();
        } else {
            this.captionAbove.set(this.messageAbove, true);
            this.photoSmallProgress.set(this.smallPhoto, true);
            this.photoAlphaProgress.set(this.hasPhoto, true);
            this.previewHeightProgress.set(this.previewHeight, true);
        }
        this.relayout = false;
    }

    public boolean withPreview() {
        WebPagePreview webPagePreview = this.webpage;
        return (webPagePreview == null || webPagePreview.webpage == null) ? false : true;
    }
}
