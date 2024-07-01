package org.telegram.ui.Cells;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.Stars.StarsIntroActivity;
/* loaded from: classes4.dex */
public class GroupMedia {
    private final AnimatedFloat animatedHidden;
    public boolean attached;
    private Bitmap blurBitmap;
    private int blurBitmapHeight;
    private int blurBitmapMessageId;
    private Paint blurBitmapPaint;
    private int blurBitmapState;
    private int blurBitmapWidth;
    private final ButtonBounce bounce;
    private Text buttonText;
    public final ChatMessageCell cell;
    public int height;
    public boolean hidden;
    private GroupedMessages layout;
    private LoadingDrawable loadingDrawable;
    public int maxWidth;
    private int overrideWidth;
    private boolean pressButton;
    private MediaHolder pressHolder;
    private Text priceText;
    private long priceTextPrice;
    SpoilerEffect2 spoilerEffect;
    public int width;
    public int x;
    public int y;
    public final ArrayList<MediaHolder> holders = new ArrayList<>();
    private Path clipPath = new Path();
    private Path clipPath2 = new Path();
    private RectF clipRect = new RectF();

    public GroupMedia(ChatMessageCell chatMessageCell) {
        this.cell = chatMessageCell;
        this.spoilerEffect = SpoilerEffect2.getInstance(chatMessageCell);
        this.animatedHidden = new AnimatedFloat(chatMessageCell, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.bounce = new ButtonBounce(chatMessageCell);
    }

    public void setOverrideWidth(int i) {
        this.overrideWidth = i;
    }

    public void setMessageObject(MessageObject messageObject, boolean z, boolean z2) {
        TLRPC$Message tLRPC$Message;
        MessageObject.GroupedMessagePosition position;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
            TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia;
            if (this.layout == null) {
                this.layout = new GroupedMessages();
            }
            this.layout.medias.clear();
            this.layout.medias.addAll(tLRPC$TL_messageMediaPaidMedia.extended_media);
            this.layout.calculate();
            int i = this.overrideWidth;
            if (i > 0) {
                this.maxWidth = i;
            } else {
                if (AndroidUtilities.isTablet()) {
                    this.maxWidth = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(122.0f);
                } else {
                    this.maxWidth = Math.min(this.cell.getParentWidth(), AndroidUtilities.displaySize.y) - AndroidUtilities.dp((this.cell.checkNeedDrawShareButton(messageObject) ? 10 : 0) + 64);
                }
                if (this.cell.needDrawAvatar()) {
                    this.maxWidth -= AndroidUtilities.dp(52.0f);
                }
            }
            int i2 = 0;
            while (true) {
                if (i2 >= tLRPC$TL_messageMediaPaidMedia.extended_media.size()) {
                    break;
                }
                TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i2);
                MediaHolder mediaHolder = i2 >= this.holders.size() ? null : this.holders.get(i2);
                if (mediaHolder == null) {
                    MediaHolder mediaHolder2 = new MediaHolder(this.cell, messageObject, tLRPC$MessageExtendedMedia, tLRPC$TL_messageMediaPaidMedia.extended_media.size() != 1, (int) ((position.pw / 1000.0f) * this.maxWidth), (int) (this.layout.getPosition(tLRPC$MessageExtendedMedia).ph * this.layout.maxSizeHeight));
                    String str = tLRPC$MessageExtendedMedia.attachPath;
                    if (str != null) {
                        mediaHolder2.attachPath = str;
                    } else if (tLRPC$TL_messageMediaPaidMedia.extended_media.size() == 1) {
                        TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                        mediaHolder2.attachPath = tLRPC$Message2 != null ? tLRPC$Message2.attachPath : null;
                    }
                    if (!TextUtils.isEmpty(mediaHolder2.attachPath)) {
                        DownloadController.getInstance(this.cell.currentAccount).addLoadingFileObserver(mediaHolder2.attachPath, messageObject, mediaHolder2);
                        if (messageObject.isSending()) {
                            mediaHolder2.radialProgress.setProgress(tLRPC$MessageExtendedMedia.uploadProgress, false);
                        }
                    }
                    if (this.cell.isCellAttachedToWindow()) {
                        mediaHolder2.attach();
                    }
                    this.holders.add(mediaHolder2);
                } else {
                    mediaHolder.updateMedia(tLRPC$MessageExtendedMedia, messageObject);
                }
                i2++;
            }
            int size = tLRPC$TL_messageMediaPaidMedia.extended_media.size();
            while (size < this.holders.size()) {
                MediaHolder mediaHolder3 = size >= this.holders.size() ? null : this.holders.get(size);
                if (mediaHolder3 != null) {
                    mediaHolder3.detach();
                    this.holders.remove(size);
                    size--;
                }
                size++;
            }
            updateHolders(messageObject);
            GroupedMessages groupedMessages = this.layout;
            this.width = (int) ((groupedMessages.width / 1000.0f) * this.maxWidth);
            this.height = (int) (groupedMessages.height * groupedMessages.maxSizeHeight);
            if (this.hidden) {
                Text text = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContent", (int) tLRPC$TL_messageMediaPaidMedia.stars_amount), 0.7f), 14.0f, AndroidUtilities.bold());
                this.buttonText = text;
                if (text.getCurrentWidth() > this.width - AndroidUtilities.dp(30.0f)) {
                    this.buttonText = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContentShort", (int) tLRPC$TL_messageMediaPaidMedia.stars_amount), 0.7f), 14.0f, AndroidUtilities.bold());
                }
            }
            if (this.priceText == null || this.priceTextPrice != tLRPC$TL_messageMediaPaidMedia.stars_amount) {
                long j = tLRPC$TL_messageMediaPaidMedia.stars_amount;
                this.priceTextPrice = j;
                this.priceText = new Text(StarsIntroActivity.replaceStars(LocaleController.formatPluralStringComma("PaidMediaPrice", (int) j), 0.9f), 12.0f, AndroidUtilities.bold());
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:126:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateHolders(MessageObject messageObject) {
        boolean z;
        int i;
        float f;
        int i2;
        int i3;
        int i4;
        ChatMessageCell chatMessageCell = this.cell;
        boolean z2 = chatMessageCell.namesOffset > 0 || (chatMessageCell.captionAbove && !TextUtils.isEmpty(messageObject.caption));
        if (this.cell.captionAbove || TextUtils.isEmpty(messageObject.caption)) {
            ChatMessageCell chatMessageCell2 = this.cell;
            if (chatMessageCell2.reactionsLayoutInBubble.isEmpty && !chatMessageCell2.hasCommentLayout()) {
                z = false;
                i = this.overrideWidth;
                float f2 = 1000.0f;
                if (i <= 0) {
                    f = 1000.0f / this.layout.width;
                    this.maxWidth = i;
                } else {
                    if (AndroidUtilities.isTablet()) {
                        this.maxWidth = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(122.0f);
                    } else {
                        this.maxWidth = Math.min(this.cell.getParentWidth(), AndroidUtilities.displaySize.y) - AndroidUtilities.dp((this.cell.checkNeedDrawShareButton(messageObject) ? 10 : 0) + 64);
                    }
                    if (this.cell.needDrawAvatar()) {
                        this.maxWidth -= AndroidUtilities.dp(52.0f);
                    }
                    f = 1.0f;
                }
                GroupedMessages groupedMessages = this.layout;
                this.width = (int) ((groupedMessages.width / 1000.0f) * f * this.maxWidth);
                this.height = (int) (groupedMessages.height * groupedMessages.maxSizeHeight);
                this.hidden = false;
                int dp = AndroidUtilities.dp(1.0f);
                int dp2 = AndroidUtilities.dp(4.0f);
                int dp3 = AndroidUtilities.dp(i2 - (SharedConfig.bubbleRadius <= 2 ? 2 : 0));
                int min = Math.min(AndroidUtilities.dp(3.0f), dp3);
                i3 = 0;
                while (i3 < this.holders.size()) {
                    MediaHolder mediaHolder = this.holders.get(i3);
                    MessageObject.GroupedMessagePosition position = this.layout.getPosition(mediaHolder.media);
                    if (position == null) {
                        i4 = dp2;
                    } else {
                        int i5 = this.maxWidth;
                        int i6 = (int) ((position.left / f2) * f * i5);
                        float f3 = position.top;
                        float f4 = this.layout.maxSizeHeight;
                        int i7 = (int) (f3 * f4);
                        i4 = dp2;
                        int i8 = (int) ((position.pw / 1000.0f) * f * i5);
                        int i9 = (int) (position.ph * f4);
                        int i10 = position.flags;
                        if ((i10 & 1) == 0) {
                            i6 += dp;
                            i8 -= dp;
                        }
                        if ((i10 & 4) == 0) {
                            i7 += dp;
                            i9 -= dp;
                        }
                        if ((i10 & 2) == 0) {
                            i8 -= dp;
                        }
                        if ((i10 & 8) == 0) {
                            i9 -= dp;
                        }
                        mediaHolder.l = i6;
                        mediaHolder.t = i7;
                        mediaHolder.r = i6 + i8;
                        mediaHolder.b = i7 + i9;
                        mediaHolder.imageReceiver.setImageCoords(i6, i7, i8, i9);
                        int i11 = position.flags;
                        int i12 = ((i11 & 4) == 0 || (i11 & 1) == 0 || z2) ? i4 : dp3;
                        int i13 = ((i11 & 4) == 0 || (i11 & 2) == 0 || z2) ? i4 : dp3;
                        int i14 = ((i11 & 8) == 0 || (i11 & 1) == 0 || z) ? i4 : dp3;
                        int i15 = ((i11 & 8) == 0 || (i11 & 2) == 0 || z) ? i4 : dp3;
                        if (!z) {
                            if (messageObject.isOutOwner()) {
                                i15 = i4;
                            } else {
                                i14 = i4;
                            }
                        }
                        if (!z2 && this.cell.pinnedTop) {
                            if (messageObject.isOutOwner()) {
                                i13 = min;
                            } else {
                                i12 = min;
                            }
                        }
                        mediaHolder.imageReceiver.setRoundRadius(i12, i13, i15, i14);
                        float[] fArr = mediaHolder.radii;
                        float f5 = i12;
                        fArr[1] = f5;
                        fArr[0] = f5;
                        float f6 = i13;
                        fArr[3] = f6;
                        fArr[2] = f6;
                        float f7 = i15;
                        fArr[5] = f7;
                        fArr[4] = f7;
                        float f8 = i14;
                        fArr[7] = f8;
                        fArr[6] = f8;
                        if (messageObject != null && messageObject.isSending()) {
                            mediaHolder.setIcon(3);
                        }
                        this.hidden = this.hidden || mediaHolder.hidden;
                    }
                    i3++;
                    dp2 = i4;
                    f2 = 1000.0f;
                }
                if (this.hidden) {
                    return;
                }
                TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = messageObject == null ? null : (TLRPC$TL_messageMediaPaidMedia) messageObject.messageOwner.media;
                if (tLRPC$TL_messageMediaPaidMedia != null) {
                    Text text = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContent", (int) tLRPC$TL_messageMediaPaidMedia.stars_amount), 0.7f), 14.0f, AndroidUtilities.bold());
                    this.buttonText = text;
                    if (text.getCurrentWidth() > this.width - AndroidUtilities.dp(30.0f)) {
                        this.buttonText = new Text(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatPluralStringComma("UnlockPaidContentShort", (int) tLRPC$TL_messageMediaPaidMedia.stars_amount), 0.7f), 14.0f, AndroidUtilities.bold());
                        return;
                    }
                    return;
                }
                return;
            }
        }
        z = true;
        i = this.overrideWidth;
        float f22 = 1000.0f;
        if (i <= 0) {
        }
        GroupedMessages groupedMessages2 = this.layout;
        this.width = (int) ((groupedMessages2.width / 1000.0f) * f * this.maxWidth);
        this.height = (int) (groupedMessages2.height * groupedMessages2.maxSizeHeight);
        this.hidden = false;
        int dp4 = AndroidUtilities.dp(1.0f);
        int dp22 = AndroidUtilities.dp(4.0f);
        int dp32 = AndroidUtilities.dp(i2 - (SharedConfig.bubbleRadius <= 2 ? 2 : 0));
        int min2 = Math.min(AndroidUtilities.dp(3.0f), dp32);
        i3 = 0;
        while (i3 < this.holders.size()) {
        }
        if (this.hidden) {
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (motionEvent.getAction() == 0) {
            MediaHolder holderAt = getHolderAt(x, y);
            this.pressHolder = holderAt;
            this.pressButton = (holderAt == null || holderAt.radialProgress.getIcon() == 4 || !this.pressHolder.radialProgress.getProgressRect().contains(x, y)) ? false : true;
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            MediaHolder holderAt2 = getHolderAt(x, y);
            boolean z = (holderAt2 == null || holderAt2.radialProgress.getIcon() == 4 || !holderAt2.radialProgress.getProgressRect().contains(x, y)) ? false : true;
            MediaHolder mediaHolder = this.pressHolder;
            if (mediaHolder != null && mediaHolder == holderAt2 && this.cell.getDelegate() != null && motionEvent.getAction() == 1) {
                MessageObject messageObject = this.cell.getMessageObject();
                if (this.pressButton && z && holderAt2.radialProgress.getIcon() == 3 && messageObject != null) {
                    if (messageObject.isSending()) {
                        SendMessagesHelper.getInstance(messageObject.currentAccount).cancelSendingMessage(messageObject);
                    }
                } else {
                    ChatMessageCell.ChatMessageCellDelegate delegate = this.cell.getDelegate();
                    ChatMessageCell chatMessageCell = this.cell;
                    MediaHolder mediaHolder2 = this.pressHolder;
                    delegate.didPressGroupImage(chatMessageCell, mediaHolder2.imageReceiver, mediaHolder2.media, motionEvent.getX(), motionEvent.getY());
                }
            }
            this.pressButton = false;
            this.pressHolder = null;
        }
        this.bounce.setPressed(this.pressHolder != null);
        return this.pressHolder != null;
    }

    public MediaHolder getHolderAt(float f, float f2) {
        for (int i = 0; i < this.holders.size(); i++) {
            if (this.holders.get(i).imageReceiver.isInsideImage(f, f2)) {
                return this.holders.get(i);
            }
        }
        return null;
    }

    public ImageReceiver getPhotoImage(int i) {
        GroupedMessages groupedMessages = this.layout;
        if (groupedMessages != null && i >= 0 && i < groupedMessages.medias.size()) {
            TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = this.layout.medias.get(i);
            for (int i2 = 0; i2 < this.holders.size(); i2++) {
                if (this.holders.get(i2).media == tLRPC$MessageExtendedMedia) {
                    return this.holders.get(i2).imageReceiver;
                }
            }
        }
        return null;
    }

    public boolean allVisible() {
        Iterator<MediaHolder> it = this.holders.iterator();
        while (it.hasNext()) {
            if (!it.next().imageReceiver.getVisible()) {
                return false;
            }
        }
        return true;
    }

    public void draw(Canvas canvas) {
        if (this.layout == null) {
            return;
        }
        float f = this.animatedHidden.set(this.hidden);
        drawImages(canvas, true);
        if (this.buttonText != null && f > 0.0f) {
            float scale = this.bounce.getScale(0.05f);
            float dp = AndroidUtilities.dp(28.0f) + this.buttonText.getCurrentWidth();
            float dp2 = AndroidUtilities.dp(32.0f);
            RectF rectF = this.clipRect;
            int i = this.x;
            int i2 = this.width;
            int i3 = this.y;
            int i4 = this.height;
            rectF.set(i + ((i2 - dp) / 2.0f), i3 + ((i4 - dp2) / 2.0f), i + ((i2 + dp) / 2.0f), i3 + ((i4 + dp2) / 2.0f));
            this.clipPath.rewind();
            float f2 = dp2 / 2.0f;
            this.clipPath.addRoundRect(this.clipRect, f2, f2, Path.Direction.CW);
            canvas.save();
            canvas.scale(scale, scale, this.x + (this.width / 2.0f), this.y + (this.height / 2.0f));
            canvas.save();
            canvas.clipPath(this.clipPath);
            drawBlurred(canvas, f);
            canvas.drawColor(Theme.multAlpha(805306368, f));
            this.buttonText.draw(canvas, ((this.x + (this.width / 2.0f)) - (dp / 2.0f)) + AndroidUtilities.dp(14.0f), (this.height / 2.0f) + this.y, -1, f);
            canvas.restore();
            if (isLoading()) {
                LoadingDrawable loadingDrawable = this.loadingDrawable;
                if (loadingDrawable == null) {
                    LoadingDrawable loadingDrawable2 = new LoadingDrawable();
                    this.loadingDrawable = loadingDrawable2;
                    loadingDrawable2.setCallback(this.cell);
                    this.loadingDrawable.setColors(Theme.multAlpha(-1, 0.1f), Theme.multAlpha(-1, 0.3f), Theme.multAlpha(-1, 0.35f), Theme.multAlpha(-1, 0.8f));
                    this.loadingDrawable.setAppearByGradient(true);
                    this.loadingDrawable.strokePaint.setStrokeWidth(AndroidUtilities.dpf2(1.25f));
                } else if (loadingDrawable.isDisappeared() || this.loadingDrawable.isDisappearing()) {
                    this.loadingDrawable.reset();
                    this.loadingDrawable.resetDisappear();
                }
            } else {
                LoadingDrawable loadingDrawable3 = this.loadingDrawable;
                if (loadingDrawable3 != null && !loadingDrawable3.isDisappearing() && !this.loadingDrawable.isDisappeared()) {
                    this.loadingDrawable.disappear();
                }
            }
            LoadingDrawable loadingDrawable4 = this.loadingDrawable;
            if (loadingDrawable4 != null) {
                loadingDrawable4.setBounds(this.clipRect);
                this.loadingDrawable.setRadiiDp(f2);
                this.loadingDrawable.setAlpha((int) (255.0f * f));
                this.loadingDrawable.draw(canvas);
            }
            canvas.restore();
        }
        if (this.priceText == null || f >= 1.0f || !allVisible()) {
            return;
        }
        float timeAlpha = (1.0f - f) * this.cell.getTimeAlpha();
        float dp3 = AndroidUtilities.dp(11.32f) + this.priceText.getCurrentWidth();
        float dp4 = AndroidUtilities.dp(17.0f);
        float dp5 = AndroidUtilities.dp(5.0f);
        RectF rectF2 = this.clipRect;
        int i5 = this.x;
        int i6 = this.width;
        int i7 = this.y;
        rectF2.set(((i5 + i6) - dp3) - dp5, i7 + dp5, (i5 + i6) - dp5, i7 + dp5 + dp4);
        this.clipPath.rewind();
        float f3 = dp4 / 2.0f;
        this.clipPath.addRoundRect(this.clipRect, f3, f3, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(this.clipPath);
        canvas.drawColor(Theme.multAlpha(1073741824, timeAlpha));
        this.priceText.draw(canvas, (((this.x + this.width) - dp3) - dp5) + AndroidUtilities.dp(5.66f), this.y + dp5 + f3, -1, timeAlpha);
        canvas.restore();
    }

    public boolean isLoading() {
        return this.cell.getDelegate() != null && this.cell.getDelegate().isProgressLoading(this.cell, 5);
    }

    public void drawBlurRect(Canvas canvas, RectF rectF, float f, float f2) {
        canvas.save();
        this.clipPath.rewind();
        this.clipPath.addRoundRect(rectF, f, f, Path.Direction.CW);
        canvas.clipPath(this.clipPath);
        canvas.drawColor(1073741824);
        canvas.restore();
    }

    public void checkBlurBitmap() {
        int i;
        int i2;
        int id = this.cell.getMessageObject() != null ? this.cell.getMessageObject().getId() : 0;
        int i3 = this.width;
        int i4 = this.height;
        int max = (int) Math.max(1.0f, i3 > i4 ? 100.0f : (i3 / i4) * 100.0f);
        int i5 = this.height;
        int i6 = this.width;
        int max2 = (int) Math.max(1.0f, i5 <= i6 ? 100.0f * (i5 / i6) : 100.0f);
        int i7 = 0;
        for (int i8 = 0; i8 < this.holders.size(); i8++) {
            MediaHolder mediaHolder = this.holders.get(i8);
            if (mediaHolder.imageReceiver.hasImageSet() && mediaHolder.imageReceiver.getBitmap() != null) {
                i7 |= 1 << i8;
            }
        }
        Bitmap bitmap = this.blurBitmap;
        if (bitmap != null && this.blurBitmapMessageId == id && this.blurBitmapState == i7 && this.blurBitmapWidth == max && this.blurBitmapHeight == max2) {
            return;
        }
        this.blurBitmapState = i7;
        this.blurBitmapMessageId = id;
        this.blurBitmapWidth = max;
        this.blurBitmapHeight = max2;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.blurBitmap = Bitmap.createBitmap(max, max2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.blurBitmap);
        float f = max;
        int i9 = this.width;
        canvas.scale(f / i9, f / i9);
        for (int i10 = 0; i10 < this.holders.size(); i10++) {
            MediaHolder mediaHolder2 = this.holders.get(i10);
            mediaHolder2.imageReceiver.setImageCoords(mediaHolder2.l, mediaHolder2.t, mediaHolder2.r - i, mediaHolder2.b - i2);
            mediaHolder2.imageReceiver.draw(canvas);
        }
        Utilities.stackBlurBitmap(this.blurBitmap, 12);
        if (this.blurBitmapPaint == null) {
            this.blurBitmapPaint = new Paint(3);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(1.5f);
            this.blurBitmapPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    public void drawBlurred(Canvas canvas, float f) {
        if (this.layout == null) {
            return;
        }
        checkBlurBitmap();
        if (this.blurBitmap != null) {
            canvas.save();
            canvas.translate(this.x, this.y);
            canvas.scale(this.width / this.blurBitmap.getWidth(), this.width / this.blurBitmap.getWidth());
            this.blurBitmapPaint.setAlpha((int) (f * 255.0f));
            canvas.drawBitmap(this.blurBitmap, 0.0f, 0.0f, this.blurBitmapPaint);
            canvas.restore();
        }
    }

    public void drawImages(Canvas canvas, boolean z) {
        float f = this.animatedHidden.set(this.hidden);
        MessageObject messageObject = this.cell.getMessageObject();
        this.clipPath2.rewind();
        float f2 = Float.MIN_VALUE;
        float f3 = Float.MIN_VALUE;
        float f4 = Float.MAX_VALUE;
        float f5 = Float.MAX_VALUE;
        int i = 0;
        while (i < this.holders.size()) {
            MediaHolder mediaHolder = this.holders.get(i);
            ImageReceiver imageReceiver = mediaHolder.imageReceiver;
            int i2 = this.x;
            int i3 = mediaHolder.l;
            int i4 = this.y;
            int i5 = mediaHolder.t;
            imageReceiver.setImageCoords(i2 + i3, i4 + i5, mediaHolder.r - i3, mediaHolder.b - i5);
            mediaHolder.imageReceiver.draw(canvas);
            if (mediaHolder.imageReceiver.getAnimation() != null) {
                mediaHolder.setTime(Math.round(((float) mediaHolder.imageReceiver.getAnimation().currentTime) / 1000.0f));
            }
            if (f > 0.0f) {
                f4 = Math.min(this.x + mediaHolder.l, f4);
                f5 = Math.min(this.y + mediaHolder.t, f5);
                f2 = Math.max(this.x + mediaHolder.r, f2);
                f3 = Math.max(this.y + mediaHolder.b, f3);
                RectF rectF = AndroidUtilities.rectTmp;
                int i6 = this.x;
                int i7 = this.y;
                rectF.set(mediaHolder.l + i6, mediaHolder.t + i7, i6 + mediaHolder.r, i7 + mediaHolder.b);
                this.clipPath2.addRoundRect(rectF, mediaHolder.radii, Path.Direction.CW);
            }
            mediaHolder.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
            float f6 = f2;
            mediaHolder.radialProgress.setProgressRect(mediaHolder.imageReceiver.getImageX() + ((mediaHolder.imageReceiver.getImageWidth() / 2.0f) - mediaHolder.radialProgress.getRadius()), mediaHolder.imageReceiver.getImageY() + ((mediaHolder.imageReceiver.getImageHeight() / 2.0f) - mediaHolder.radialProgress.getRadius()), mediaHolder.imageReceiver.getImageX() + (mediaHolder.imageReceiver.getImageWidth() / 2.0f) + mediaHolder.radialProgress.getRadius(), mediaHolder.imageReceiver.getImageY() + (mediaHolder.imageReceiver.getImageHeight() / 2.0f) + mediaHolder.radialProgress.getRadius());
            if (messageObject.isSending()) {
                SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(messageObject.currentAccount);
                long[] fileProgressSizes = ImageLoader.getInstance().getFileProgressSizes(mediaHolder.attachPath);
                boolean isSendingPaidMessage = sendMessagesHelper.isSendingPaidMessage(messageObject.getId(), i);
                if (fileProgressSizes == null && isSendingPaidMessage) {
                    mediaHolder.radialProgress.setProgress(1.0f, true);
                    mediaHolder.setIcon(mediaHolder.album ? 6 : mediaHolder.getDefaultIcon());
                }
            } else if (FileLoader.getInstance(messageObject.currentAccount).isLoadingFile(mediaHolder.filename)) {
                mediaHolder.setIcon(3);
            } else {
                mediaHolder.setIcon(mediaHolder.getDefaultIcon());
            }
            canvas.saveLayerAlpha(mediaHolder.radialProgress.getProgressRect(), (int) ((1.0f - f) * 255.0f), 31);
            mediaHolder.radialProgress.draw(canvas);
            canvas.restore();
            i++;
            f2 = f6;
        }
        if (f > 0.0f && z) {
            canvas.save();
            canvas.clipPath(this.clipPath2);
            canvas.translate(f4, f5);
            int i8 = (int) (f2 - f4);
            int i9 = (int) (f3 - f5);
            canvas.saveLayerAlpha(0.0f, 0.0f, i8, i9, (int) (255.0f * f), 31);
            SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
            ChatMessageCell chatMessageCell = this.cell;
            spoilerEffect2.draw(canvas, chatMessageCell, i8, i9, 1.0f, chatMessageCell.drawingToBitmap);
            canvas.restore();
            canvas.restore();
            this.cell.invalidate();
        }
        for (int i10 = 0; i10 < this.holders.size(); i10++) {
            MediaHolder mediaHolder2 = this.holders.get(i10);
            if (mediaHolder2.durationText != null) {
                float dp = AndroidUtilities.dp(11.4f) + mediaHolder2.durationText.getCurrentWidth();
                float dp2 = AndroidUtilities.dp(17.0f);
                float dp3 = AndroidUtilities.dp(5.0f);
                RectF rectF2 = this.clipRect;
                int i11 = this.x;
                int i12 = mediaHolder2.l;
                int i13 = this.y;
                int i14 = mediaHolder2.t;
                rectF2.set(i11 + i12 + dp3, i13 + i14 + dp3, i11 + i12 + dp3 + dp, i13 + i14 + dp3 + dp2);
                if (this.priceText == null || this.clipRect.right <= ((this.x + this.width) - (AndroidUtilities.dp(11.32f) + this.priceText.getCurrentWidth())) - dp3 || this.clipRect.top > this.y + dp3) {
                    this.clipPath.rewind();
                    float f7 = dp2 / 2.0f;
                    this.clipPath.addRoundRect(this.clipRect, f7, f7, Path.Direction.CW);
                    canvas.save();
                    canvas.clipPath(this.clipPath);
                    drawBlurred(canvas, f);
                    canvas.drawColor(Theme.multAlpha(1073741824, 1.0f));
                    mediaHolder2.durationText.draw(canvas, this.x + mediaHolder2.l + dp3 + AndroidUtilities.dp(5.66f), this.y + mediaHolder2.t + dp3 + f7, -1, 1.0f);
                    canvas.restore();
                }
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class MediaHolder implements DownloadController.FileDownloadProgressListener {
        private final int TAG;
        public boolean album;
        public String attachPath;
        public boolean attached;
        public boolean autoplay;
        public int b;
        public final ChatMessageCell cell;
        private int duration;
        private Text durationText;
        private int durationValue;
        public String filename;
        private final int h;
        public boolean hidden;
        public int icon;
        public final ImageReceiver imageReceiver;
        public int l;
        public TLRPC$MessageExtendedMedia media;
        public int r;
        public final RadialProgress2 radialProgress;
        public final float[] radii = new float[8];
        public int t;
        public boolean video;
        private final int w;

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onFailedDownload(String str, boolean z) {
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onSuccessDownload(String str) {
        }

        public void setIcon(int i) {
            if (i != this.icon) {
                RadialProgress2 radialProgress2 = this.radialProgress;
                this.icon = i;
                radialProgress2.setIcon(i, true, true);
            }
        }

        public void setTime(int i) {
            int max;
            if (this.video || this.durationValue == (max = Math.max(0, this.duration - i))) {
                return;
            }
            this.durationValue = max;
            this.durationText = new Text(AndroidUtilities.formatLongDuration(max), 12.0f);
        }

        public MediaHolder(ChatMessageCell chatMessageCell, MessageObject messageObject, TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia, boolean z, int i, int i2) {
            new RectF();
            new Path();
            this.icon = 4;
            this.duration = 0;
            this.durationValue = 0;
            this.cell = chatMessageCell;
            this.album = z;
            this.video = false;
            if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                this.video = ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && MessageObject.isVideoDocument(tLRPC$MessageMedia.document)) ? false : false;
                this.duration = (int) Math.max(1L, Math.round(MessageObject.getDocumentDuration(tLRPC$MessageMedia.document)));
            } else if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia;
                this.video = (4 & tLRPC$TL_messageExtendedMediaPreview.flags) != 0;
                this.duration = tLRPC$TL_messageExtendedMediaPreview.video_duration;
            }
            if (this.video) {
                int i3 = this.duration;
                this.durationValue = i3;
                this.durationText = new Text(AndroidUtilities.formatLongDuration(i3), 12.0f);
            }
            ImageReceiver imageReceiver = new ImageReceiver(chatMessageCell);
            this.imageReceiver = imageReceiver;
            imageReceiver.setColorFilter(null);
            this.w = i;
            this.h = i2;
            this.TAG = DownloadController.getInstance(chatMessageCell.currentAccount).generateObserverTag();
            updateMedia(tLRPC$MessageExtendedMedia, messageObject);
            RadialProgress2 radialProgress2 = new RadialProgress2(chatMessageCell, chatMessageCell.getResourcesProvider());
            this.radialProgress = radialProgress2;
            int defaultIcon = getDefaultIcon();
            this.icon = defaultIcon;
            radialProgress2.setIcon(defaultIcon, false, false);
        }

        public void updateMedia(TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia, MessageObject messageObject) {
            TLRPC$Document tLRPC$Document;
            if (this.media == tLRPC$MessageExtendedMedia) {
                return;
            }
            this.media = tLRPC$MessageExtendedMedia;
            this.autoplay = false;
            String str = this.w + "_" + this.h;
            if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                this.hidden = true;
                this.filename = null;
                this.imageReceiver.setImage(ImageLocation.getForObject(((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).thumb, messageObject.messageOwner), str + "_b2", null, null, messageObject, 0);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(1.4f);
                AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, -0.1f);
                this.imageReceiver.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            } else if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                boolean z = messageObject.isRepostPreview;
                this.hidden = z;
                if (z) {
                    str = str + "_b3";
                }
                String str2 = str;
                this.imageReceiver.setColorFilter(null);
                TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                this.filename = MessageObject.getFileName(tLRPC$MessageMedia);
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                    TLRPC$TL_messageMediaPhoto tLRPC$TL_messageMediaPhoto = (TLRPC$TL_messageMediaPhoto) tLRPC$MessageMedia;
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_messageMediaPhoto.photo.sizes, AndroidUtilities.getPhotoSize(), true, null, true);
                    this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$TL_messageMediaPhoto.photo), str2, ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_messageMediaPhoto.photo.sizes, Math.min(this.w, this.h) / 100, false, closestPhotoSizeWithSize, false), tLRPC$TL_messageMediaPhoto.photo), str2, 0L, null, messageObject, 0);
                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = (TLRPC$TL_messageMediaDocument) tLRPC$MessageMedia;
                    this.autoplay = !this.hidden && !this.album && this.video && SharedConfig.isAutoplayVideo();
                    if (!this.album && this.video && (tLRPC$Document = tLRPC$TL_messageMediaDocument.document) != null) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, AndroidUtilities.getPhotoSize(), true, null, true);
                        TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_messageMediaDocument.document.thumbs, Math.min(this.w, this.h), false, closestPhotoSizeWithSize2, false);
                        ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_messageMediaDocument.document);
                        ImageLocation forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize2, tLRPC$TL_messageMediaDocument.document);
                        ImageLocation forDocument3 = ImageLocation.getForDocument(closestPhotoSizeWithSize3, tLRPC$TL_messageMediaDocument.document);
                        ImageReceiver imageReceiver = this.imageReceiver;
                        ImageLocation imageLocation = this.autoplay ? forDocument : null;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(this.autoplay ? "_g" : "");
                        imageReceiver.setImage(imageLocation, sb.toString(), forDocument2, str2, forDocument3, str2, null, 0L, null, messageObject, 0);
                        return;
                    }
                    TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageMediaDocument.document;
                    if (tLRPC$Document2 != null) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document2.thumbs, AndroidUtilities.getPhotoSize(), true, null, true);
                        this.imageReceiver.setImage(ImageLocation.getForDocument(closestPhotoSizeWithSize4, tLRPC$TL_messageMediaDocument.document), str2, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_messageMediaDocument.document.thumbs, Math.min(this.w, this.h), false, closestPhotoSizeWithSize4, false), tLRPC$TL_messageMediaDocument.document), str2, 0L, null, messageObject, 0);
                    }
                }
            }
        }

        public void attach() {
            if (this.attached) {
                return;
            }
            this.attached = true;
            this.imageReceiver.onAttachedToWindow();
        }

        public void detach() {
            if (this.attached) {
                this.attached = false;
                this.imageReceiver.onDetachedFromWindow();
            }
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onProgressDownload(String str, long j, long j2) {
            float min = j2 == 0 ? 0.0f : Math.min(1.0f, ((float) j) / ((float) j2));
            RadialProgress2 radialProgress2 = this.radialProgress;
            this.media.downloadProgress = min;
            radialProgress2.setProgress(min, true);
            setIcon(min < 1.0f ? 3 : getDefaultIcon());
            this.cell.invalidate();
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public void onProgressUpload(String str, long j, long j2, boolean z) {
            int defaultIcon;
            float min = j2 == 0 ? 0.0f : Math.min(1.0f, ((float) j) / ((float) j2));
            RadialProgress2 radialProgress2 = this.radialProgress;
            this.media.uploadProgress = min;
            radialProgress2.setProgress(min, true);
            if (min < 1.0f) {
                defaultIcon = 3;
            } else {
                defaultIcon = this.album ? 6 : getDefaultIcon();
            }
            setIcon(defaultIcon);
            this.cell.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getDefaultIcon() {
            return (!this.video || this.autoplay) ? 4 : 0;
        }

        @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
        public int getObserverTag() {
            return this.TAG;
        }
    }

    public void onAttachedToWindow() {
        if (this.attached) {
            return;
        }
        this.attached = true;
        SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
        if (spoilerEffect2 != null) {
            spoilerEffect2.detach(this.cell);
        }
        for (int i = 0; i < this.holders.size(); i++) {
            this.holders.get(i).attach();
        }
    }

    public void onDetachedFromWindow() {
        if (this.attached) {
            this.attached = false;
            SpoilerEffect2 spoilerEffect2 = this.spoilerEffect;
            if (spoilerEffect2 != null) {
                spoilerEffect2.attach(this.cell);
            }
            for (int i = 0; i < this.holders.size(); i++) {
                this.holders.get(i).detach();
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class GroupedMessages {
        float height;
        int maxX;
        int maxY;
        int width;
        public ArrayList<TLRPC$MessageExtendedMedia> medias = new ArrayList<>();
        public ArrayList<MessageObject.GroupedMessagePosition> posArray = new ArrayList<>();
        public HashMap<TLRPC$MessageExtendedMedia, MessageObject.GroupedMessagePosition> positions = new HashMap<>();
        public int maxSizeWidth = 800;
        public float maxSizeHeight = 814.0f;

        /* loaded from: classes4.dex */
        public static class TransitionParams {
        }

        public GroupedMessages() {
            new TransitionParams();
        }

        public MessageObject.GroupedMessagePosition getPosition(TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia) {
            if (tLRPC$MessageExtendedMedia == null) {
                return null;
            }
            return this.positions.get(tLRPC$MessageExtendedMedia);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class MessageGroupedLayoutAttempt {
            public float[] heights;
            public int[] lineCounts;

            public MessageGroupedLayoutAttempt(int i, int i2, float f, float f2) {
                this.lineCounts = new int[]{i, i2};
                this.heights = new float[]{f, f2};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, float f, float f2, float f3) {
                this.lineCounts = new int[]{i, i2, i3};
                this.heights = new float[]{f, f2, f3};
            }

            public MessageGroupedLayoutAttempt(int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
                this.lineCounts = new int[]{i, i2, i3, i4};
                this.heights = new float[]{f, f2, f3, f4};
            }
        }

        private float multiHeight(float[] fArr, int i, int i2) {
            float f = 0.0f;
            while (i < i2) {
                f += fArr[i];
                i++;
            }
            return this.maxSizeWidth / f;
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0099  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00a0  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void calculate() {
            int i;
            float f;
            float f2;
            float f3;
            TLRPC$Document tLRPC$Document;
            TLRPC$PhotoSize closestPhotoSizeWithSize;
            this.posArray.clear();
            this.positions.clear();
            this.maxX = 0;
            int size = this.medias.size();
            if (size == 0) {
                this.width = 0;
                this.height = 0.0f;
                this.maxY = 0;
                return;
            }
            this.maxSizeWidth = 800;
            StringBuilder sb = new StringBuilder();
            int i2 = 0;
            float f4 = 1.0f;
            boolean z = false;
            while (i2 < size) {
                TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = this.medias.get(i2);
                MessageObject.GroupedMessagePosition groupedMessagePosition = new MessageObject.GroupedMessagePosition();
                groupedMessagePosition.last = i2 == size + (-1);
                if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                    TLRPC$TL_messageExtendedMediaPreview tLRPC$TL_messageExtendedMediaPreview = (TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia;
                    groupedMessagePosition.photoWidth = tLRPC$TL_messageExtendedMediaPreview.w;
                    groupedMessagePosition.photoHeight = tLRPC$TL_messageExtendedMediaPreview.h;
                } else if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                    TLRPC$MessageMedia tLRPC$MessageMedia = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media;
                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
                        TLRPC$Photo tLRPC$Photo = ((TLRPC$TL_messageMediaPhoto) tLRPC$MessageMedia).photo;
                        if (tLRPC$Photo != null) {
                            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, AndroidUtilities.getPhotoSize());
                            groupedMessagePosition.photoWidth = closestPhotoSizeWithSize != null ? 100 : closestPhotoSizeWithSize.w;
                            groupedMessagePosition.photoHeight = closestPhotoSizeWithSize != null ? closestPhotoSizeWithSize.h : 100;
                        }
                        closestPhotoSizeWithSize = null;
                        groupedMessagePosition.photoWidth = closestPhotoSizeWithSize != null ? 100 : closestPhotoSizeWithSize.w;
                        groupedMessagePosition.photoHeight = closestPhotoSizeWithSize != null ? closestPhotoSizeWithSize.h : 100;
                    } else {
                        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$Document = ((TLRPC$TL_messageMediaDocument) tLRPC$MessageMedia).document) != null) {
                            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, AndroidUtilities.getPhotoSize());
                            groupedMessagePosition.photoWidth = closestPhotoSizeWithSize != null ? 100 : closestPhotoSizeWithSize.w;
                            groupedMessagePosition.photoHeight = closestPhotoSizeWithSize != null ? closestPhotoSizeWithSize.h : 100;
                        }
                        closestPhotoSizeWithSize = null;
                        groupedMessagePosition.photoWidth = closestPhotoSizeWithSize != null ? 100 : closestPhotoSizeWithSize.w;
                        groupedMessagePosition.photoHeight = closestPhotoSizeWithSize != null ? closestPhotoSizeWithSize.h : 100;
                    }
                } else {
                    groupedMessagePosition.photoWidth = 100;
                    groupedMessagePosition.photoHeight = 100;
                }
                float f5 = groupedMessagePosition.photoWidth / groupedMessagePosition.photoHeight;
                groupedMessagePosition.aspectRatio = f5;
                if (f5 > 1.2f) {
                    sb.append("w");
                } else if (f5 < 0.8f) {
                    sb.append("n");
                } else {
                    sb.append("q");
                }
                float f6 = groupedMessagePosition.aspectRatio;
                f4 += f6;
                if (f6 > 2.0f) {
                    z = true;
                }
                this.positions.put(tLRPC$MessageExtendedMedia, groupedMessagePosition);
                this.posArray.add(groupedMessagePosition);
                i2++;
            }
            int dp = AndroidUtilities.dp(120.0f);
            Point point = AndroidUtilities.displaySize;
            int dp2 = (int) (AndroidUtilities.dp(120.0f) / (Math.min(point.x, point.y) / this.maxSizeWidth));
            Point point2 = AndroidUtilities.displaySize;
            int i3 = this.maxSizeWidth;
            int dp3 = (int) (AndroidUtilities.dp(40.0f) / (Math.min(point2.x, point2.y) / i3));
            float f7 = i3 / this.maxSizeHeight;
            float f8 = f4 / size;
            float dp4 = AndroidUtilities.dp(100.0f) / this.maxSizeHeight;
            if (size == 1) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(0);
                float f9 = groupedMessagePosition2.aspectRatio;
                if (f9 >= 1.0f) {
                    int i4 = this.maxSizeWidth;
                    f2 = i4;
                    f3 = ((f2 / f9) / i4) * this.maxSizeHeight;
                } else {
                    float f10 = this.maxSizeHeight;
                    f2 = this.maxSizeWidth * ((f9 * f10) / f10);
                    f3 = f10;
                }
                groupedMessagePosition2.set(0, 0, 0, 0, (int) f2, f3 / this.maxSizeHeight, 15);
            } else if (z || !(size == 2 || size == 3 || size == 4)) {
                int size2 = this.posArray.size();
                float[] fArr = new float[size2];
                for (int i5 = 0; i5 < size; i5++) {
                    if (f8 > 1.1f) {
                        fArr[i5] = Math.max(1.0f, this.posArray.get(i5).aspectRatio);
                    } else {
                        fArr[i5] = Math.min(1.0f, this.posArray.get(i5).aspectRatio);
                    }
                    fArr[i5] = Math.max(0.66667f, Math.min(1.7f, fArr[i5]));
                }
                ArrayList arrayList = new ArrayList();
                for (int i6 = 1; i6 < size2; i6++) {
                    int i7 = size2 - i6;
                    if (i6 <= 3 && i7 <= 3) {
                        arrayList.add(new MessageGroupedLayoutAttempt(i6, i7, multiHeight(fArr, 0, i6), multiHeight(fArr, i6, size2)));
                    }
                }
                for (int i8 = 1; i8 < size2 - 1; i8++) {
                    int i9 = 1;
                    while (true) {
                        int i10 = size2 - i8;
                        if (i9 < i10) {
                            int i11 = i10 - i9;
                            if (i8 <= 3) {
                                if (i9 <= (f8 < 0.85f ? 4 : 3) && i11 <= 3) {
                                    int i12 = i8 + i9;
                                    arrayList.add(new MessageGroupedLayoutAttempt(i8, i9, i11, multiHeight(fArr, 0, i8), multiHeight(fArr, i8, i12), multiHeight(fArr, i12, size2)));
                                }
                            }
                            i9++;
                        }
                    }
                }
                for (int i13 = 1; i13 < size2 - 2; i13++) {
                    int i14 = 1;
                    while (true) {
                        int i15 = size2 - i13;
                        if (i14 < i15) {
                            int i16 = 1;
                            while (true) {
                                int i17 = i15 - i14;
                                if (i16 < i17) {
                                    int i18 = i17 - i16;
                                    if (i13 <= 3 && i14 <= 3 && i16 <= 3 && i18 <= 3) {
                                        int i19 = i13 + i14;
                                        int i20 = i19 + i16;
                                        arrayList.add(new MessageGroupedLayoutAttempt(i13, i14, i16, i18, multiHeight(fArr, 0, i13), multiHeight(fArr, i13, i19), multiHeight(fArr, i19, i20), multiHeight(fArr, i20, size2)));
                                    }
                                    i16++;
                                }
                            }
                            i14++;
                        }
                    }
                }
                float f11 = (this.maxSizeWidth / 3) * 4;
                int i21 = 0;
                MessageGroupedLayoutAttempt messageGroupedLayoutAttempt = null;
                float f12 = 0.0f;
                while (i21 < arrayList.size()) {
                    MessageGroupedLayoutAttempt messageGroupedLayoutAttempt2 = (MessageGroupedLayoutAttempt) arrayList.get(i21);
                    int i22 = 0;
                    float f13 = Float.MAX_VALUE;
                    float f14 = 0.0f;
                    while (true) {
                        float[] fArr2 = messageGroupedLayoutAttempt2.heights;
                        if (i22 >= fArr2.length) {
                            break;
                        }
                        f14 += fArr2[i22];
                        if (fArr2[i22] < f13) {
                            f13 = fArr2[i22];
                        }
                        i22++;
                    }
                    float abs = Math.abs(f14 - f11);
                    int[] iArr = messageGroupedLayoutAttempt2.lineCounts;
                    float f15 = f11;
                    if (iArr.length > 1) {
                        if (iArr[0] <= iArr[1]) {
                            if (iArr.length > 2 && iArr[1] > iArr[2]) {
                                f = 1.2f;
                                abs *= f;
                            } else if (iArr.length <= 3 || iArr[2] <= iArr[3]) {
                            }
                        }
                        f = 1.2f;
                        abs *= f;
                    }
                    if (f13 < dp2) {
                        abs *= 1.5f;
                    }
                    if (messageGroupedLayoutAttempt == null || abs < f12) {
                        messageGroupedLayoutAttempt = messageGroupedLayoutAttempt2;
                        f12 = abs;
                    }
                    i21++;
                    f11 = f15;
                }
                if (messageGroupedLayoutAttempt == null) {
                    return;
                }
                int i23 = 0;
                int i24 = 0;
                while (true) {
                    int[] iArr2 = messageGroupedLayoutAttempt.lineCounts;
                    if (i24 >= iArr2.length) {
                        break;
                    }
                    int i25 = iArr2[i24];
                    float f16 = messageGroupedLayoutAttempt.heights[i24];
                    int i26 = this.maxSizeWidth;
                    int i27 = i25 - 1;
                    this.maxX = Math.max(this.maxX, i27);
                    int i28 = i26;
                    MessageObject.GroupedMessagePosition groupedMessagePosition3 = null;
                    for (int i29 = 0; i29 < i25; i29++) {
                        int i30 = (int) (fArr[i23] * f16);
                        i28 -= i30;
                        MessageObject.GroupedMessagePosition groupedMessagePosition4 = this.posArray.get(i23);
                        int i31 = i24 == 0 ? 4 : 0;
                        if (i24 == messageGroupedLayoutAttempt.lineCounts.length - 1) {
                            i31 |= 8;
                        }
                        if (i29 == 0) {
                            i31 |= 1;
                        }
                        if (i29 == i27) {
                            i = i31 | 2;
                            groupedMessagePosition3 = groupedMessagePosition4;
                        } else {
                            i = i31;
                        }
                        groupedMessagePosition4.set(i29, i29, i24, i24, i30, Math.max(dp4, f16 / this.maxSizeHeight), i);
                        i23++;
                    }
                    groupedMessagePosition3.pw += i28;
                    groupedMessagePosition3.spanSize += i28;
                    i24++;
                }
            } else if (size == 2) {
                MessageObject.GroupedMessagePosition groupedMessagePosition5 = this.posArray.get(0);
                MessageObject.GroupedMessagePosition groupedMessagePosition6 = this.posArray.get(1);
                String sb2 = sb.toString();
                if (sb2.equals("ww")) {
                    double d = f7;
                    Double.isNaN(d);
                    if (f8 > d * 1.4d) {
                        float f17 = groupedMessagePosition5.aspectRatio;
                        float f18 = groupedMessagePosition6.aspectRatio;
                        if (f17 - f18 < 0.2d) {
                            int i32 = this.maxSizeWidth;
                            float round = Math.round(Math.min(i32 / f17, Math.min(i32 / f18, this.maxSizeHeight / 2.0f))) / this.maxSizeHeight;
                            groupedMessagePosition5.set(0, 0, 0, 0, this.maxSizeWidth, round, 7);
                            groupedMessagePosition6.set(0, 0, 1, 1, this.maxSizeWidth, round, 11);
                            size = size;
                        }
                    }
                }
                if (sb2.equals("ww") || sb2.equals("qq")) {
                    int i33 = this.maxSizeWidth / 2;
                    float f19 = i33;
                    float round2 = Math.round(Math.min(f19 / groupedMessagePosition5.aspectRatio, Math.min(f19 / groupedMessagePosition6.aspectRatio, this.maxSizeHeight))) / this.maxSizeHeight;
                    groupedMessagePosition5.set(0, 0, 0, 0, i33, round2, 13);
                    groupedMessagePosition6.set(1, 1, 0, 0, i33, round2, 14);
                    this.maxX = 1;
                } else {
                    int i34 = this.maxSizeWidth;
                    float f20 = groupedMessagePosition5.aspectRatio;
                    int max = (int) Math.max(i34 * 0.4f, Math.round((i34 / f20) / ((1.0f / f20) + (1.0f / groupedMessagePosition6.aspectRatio))));
                    int i35 = this.maxSizeWidth - max;
                    if (i35 < dp2) {
                        max -= dp2 - i35;
                        i35 = dp2;
                    }
                    float min = Math.min(this.maxSizeHeight, Math.round(Math.min(i35 / groupedMessagePosition5.aspectRatio, max / groupedMessagePosition6.aspectRatio))) / this.maxSizeHeight;
                    groupedMessagePosition5.set(0, 0, 0, 0, i35, min, 13);
                    groupedMessagePosition6.set(1, 1, 0, 0, max, min, 14);
                    this.maxX = 1;
                }
                size = size;
            } else if (size == 3) {
                MessageObject.GroupedMessagePosition groupedMessagePosition7 = this.posArray.get(0);
                MessageObject.GroupedMessagePosition groupedMessagePosition8 = this.posArray.get(1);
                MessageObject.GroupedMessagePosition groupedMessagePosition9 = this.posArray.get(2);
                if (sb.charAt(0) == 'n') {
                    float f21 = groupedMessagePosition8.aspectRatio;
                    float min2 = Math.min(this.maxSizeHeight * 0.5f, Math.round((this.maxSizeWidth * f21) / (groupedMessagePosition9.aspectRatio + f21)));
                    float f22 = this.maxSizeHeight - min2;
                    int max2 = (int) Math.max(dp2, Math.min(this.maxSizeWidth * 0.5f, Math.round(Math.min(groupedMessagePosition9.aspectRatio * min2, groupedMessagePosition8.aspectRatio * f22))));
                    int round3 = Math.round(Math.min((this.maxSizeHeight * groupedMessagePosition7.aspectRatio) + dp3, this.maxSizeWidth - max2));
                    groupedMessagePosition7.set(0, 0, 0, 1, round3, 1.0f, 13);
                    groupedMessagePosition8.set(1, 1, 0, 0, max2, f22 / this.maxSizeHeight, 6);
                    groupedMessagePosition9.set(1, 1, 1, 1, max2, min2 / this.maxSizeHeight, 10);
                    int i36 = this.maxSizeWidth;
                    groupedMessagePosition9.spanSize = i36;
                    float f23 = this.maxSizeHeight;
                    groupedMessagePosition7.siblingHeights = new float[]{min2 / f23, f22 / f23};
                    groupedMessagePosition8.spanSize = i36 - round3;
                    groupedMessagePosition9.leftSpanOffset = round3;
                    this.maxX = 1;
                } else {
                    float round4 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition7.aspectRatio, this.maxSizeHeight * 0.66f)) / this.maxSizeHeight;
                    groupedMessagePosition7.set(0, 1, 0, 0, this.maxSizeWidth, round4, 7);
                    int i37 = this.maxSizeWidth / 2;
                    float f24 = this.maxSizeHeight - round4;
                    float f25 = i37;
                    float min3 = Math.min(f24, Math.round(Math.min(f25 / groupedMessagePosition8.aspectRatio, f25 / groupedMessagePosition9.aspectRatio))) / this.maxSizeHeight;
                    if (min3 < dp4) {
                        min3 = dp4;
                    }
                    float f26 = min3;
                    groupedMessagePosition8.set(0, 0, 1, 1, i37, f26, 9);
                    groupedMessagePosition9.set(1, 1, 1, 1, i37, f26, 10);
                    this.maxX = 1;
                }
            } else {
                MessageObject.GroupedMessagePosition groupedMessagePosition10 = this.posArray.get(0);
                MessageObject.GroupedMessagePosition groupedMessagePosition11 = this.posArray.get(1);
                MessageObject.GroupedMessagePosition groupedMessagePosition12 = this.posArray.get(2);
                MessageObject.GroupedMessagePosition groupedMessagePosition13 = this.posArray.get(3);
                if (sb.charAt(0) == 'w') {
                    float round5 = Math.round(Math.min(this.maxSizeWidth / groupedMessagePosition10.aspectRatio, this.maxSizeHeight * 0.66f)) / this.maxSizeHeight;
                    groupedMessagePosition10.set(0, 2, 0, 0, this.maxSizeWidth, round5, 7);
                    float round6 = Math.round(this.maxSizeWidth / ((groupedMessagePosition11.aspectRatio + groupedMessagePosition12.aspectRatio) + groupedMessagePosition13.aspectRatio));
                    float f27 = dp2;
                    int max3 = (int) Math.max(f27, Math.min(this.maxSizeWidth * 0.4f, groupedMessagePosition11.aspectRatio * round6));
                    int max4 = (int) Math.max(Math.max(f27, this.maxSizeWidth * 0.33f), groupedMessagePosition13.aspectRatio * round6);
                    int i38 = (this.maxSizeWidth - max3) - max4;
                    if (i38 < AndroidUtilities.dp(58.0f)) {
                        int dp5 = AndroidUtilities.dp(58.0f) - i38;
                        i38 = AndroidUtilities.dp(58.0f);
                        int i39 = dp5 / 2;
                        max3 -= i39;
                        max4 -= dp5 - i39;
                    }
                    int i40 = max3;
                    float min4 = Math.min(this.maxSizeHeight - round5, round6) / this.maxSizeHeight;
                    if (min4 < dp4) {
                        min4 = dp4;
                    }
                    float f28 = min4;
                    groupedMessagePosition11.set(0, 0, 1, 1, i40, f28, 9);
                    groupedMessagePosition12.set(1, 1, 1, 1, i38, f28, 8);
                    groupedMessagePosition13.set(2, 2, 1, 1, max4, f28, 10);
                    this.maxX = 2;
                } else {
                    int max5 = Math.max(dp2, Math.round(this.maxSizeHeight / (((1.0f / groupedMessagePosition11.aspectRatio) + (1.0f / groupedMessagePosition12.aspectRatio)) + (1.0f / groupedMessagePosition13.aspectRatio))));
                    float f29 = dp;
                    float f30 = max5;
                    float min5 = Math.min(0.33f, Math.max(f29, f30 / groupedMessagePosition11.aspectRatio) / this.maxSizeHeight);
                    float min6 = Math.min(0.33f, Math.max(f29, f30 / groupedMessagePosition12.aspectRatio) / this.maxSizeHeight);
                    float f31 = (1.0f - min5) - min6;
                    int round7 = Math.round(Math.min((this.maxSizeHeight * groupedMessagePosition10.aspectRatio) + dp3, this.maxSizeWidth - max5));
                    groupedMessagePosition10.set(0, 0, 0, 2, round7, min5 + min6 + f31, 13);
                    groupedMessagePosition11.set(1, 1, 0, 0, max5, min5, 6);
                    groupedMessagePosition12.set(1, 1, 1, 1, max5, min6, 2);
                    groupedMessagePosition12.spanSize = this.maxSizeWidth;
                    groupedMessagePosition13.set(1, 1, 2, 2, max5, f31, 10);
                    int i41 = this.maxSizeWidth;
                    groupedMessagePosition13.spanSize = i41;
                    groupedMessagePosition11.spanSize = i41 - round7;
                    groupedMessagePosition12.leftSpanOffset = round7;
                    groupedMessagePosition13.leftSpanOffset = round7;
                    groupedMessagePosition10.siblingHeights = new float[]{min5, min6, f31};
                    this.maxX = 1;
                }
            }
            for (int i42 = 0; i42 < size; i42++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition14 = this.posArray.get(i42);
                if (groupedMessagePosition14.maxX == this.maxX || (groupedMessagePosition14.flags & 2) != 0) {
                    groupedMessagePosition14.spanSize += 200;
                }
                if ((groupedMessagePosition14.flags & 1) != 0) {
                    groupedMessagePosition14.edge = true;
                }
                this.medias.get(i42);
                if (groupedMessagePosition14.edge) {
                    int i43 = groupedMessagePosition14.spanSize;
                    if (i43 != 1000) {
                        groupedMessagePosition14.spanSize = i43 + R.styleable.AppCompatTheme_textAppearanceSearchResultTitle;
                    }
                    groupedMessagePosition14.pw += R.styleable.AppCompatTheme_textAppearanceSearchResultTitle;
                } else if ((groupedMessagePosition14.flags & 2) != 0) {
                    int i44 = groupedMessagePosition14.spanSize;
                    if (i44 != 1000) {
                        groupedMessagePosition14.spanSize = i44 - 108;
                    } else {
                        int i45 = groupedMessagePosition14.leftSpanOffset;
                        if (i45 != 0) {
                            groupedMessagePosition14.leftSpanOffset = i45 + R.styleable.AppCompatTheme_textAppearanceSearchResultTitle;
                        }
                    }
                }
            }
            for (int i46 = 0; i46 < size; i46++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition15 = this.posArray.get(i46);
                if (groupedMessagePosition15.minX == 0) {
                    groupedMessagePosition15.spanSize += 200;
                }
                if ((groupedMessagePosition15.flags & 2) != 0) {
                    groupedMessagePosition15.edge = true;
                }
                this.maxX = Math.max(this.maxX, (int) groupedMessagePosition15.maxX);
                this.maxY = Math.max(this.maxY, (int) groupedMessagePosition15.maxY);
                groupedMessagePosition15.left = getLeft(groupedMessagePosition15, groupedMessagePosition15.minY, groupedMessagePosition15.maxY, groupedMessagePosition15.minX);
            }
            for (int i47 = 0; i47 < size; i47++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition16 = this.posArray.get(i47);
                groupedMessagePosition16.top = getTop(groupedMessagePosition16, groupedMessagePosition16.minY);
            }
            this.width = getWidth();
            this.height = getHeight();
        }

        public int getWidth() {
            int[] iArr = new int[10];
            Arrays.fill(iArr, 0);
            int size = this.posArray.size();
            for (int i = 0; i < size; i++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = this.posArray.get(i);
                int i2 = groupedMessagePosition.pw;
                for (int i3 = groupedMessagePosition.minY; i3 <= groupedMessagePosition.maxY; i3++) {
                    iArr[i3] = iArr[i3] + i2;
                }
            }
            int i4 = iArr[0];
            for (int i5 = 1; i5 < 10; i5++) {
                if (i4 < iArr[i5]) {
                    i4 = iArr[i5];
                }
            }
            return i4;
        }

        public float getHeight() {
            float[] fArr = new float[10];
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i = 0; i < size; i++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition = this.posArray.get(i);
                float f = groupedMessagePosition.ph;
                for (int i2 = groupedMessagePosition.minX; i2 <= groupedMessagePosition.maxX; i2++) {
                    fArr[i2] = fArr[i2] + f;
                }
            }
            float f2 = fArr[0];
            for (int i3 = 1; i3 < 10; i3++) {
                if (f2 < fArr[i3]) {
                    f2 = fArr[i3];
                }
            }
            return f2;
        }

        private float getLeft(MessageObject.GroupedMessagePosition groupedMessagePosition, int i, int i2, int i3) {
            int i4 = (i2 - i) + 1;
            float[] fArr = new float[i4];
            float f = 0.0f;
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i5 = 0; i5 < size; i5++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(i5);
                if (groupedMessagePosition2 != groupedMessagePosition && groupedMessagePosition2.maxX < i3) {
                    int min = Math.min((int) groupedMessagePosition2.maxY, i2) - i;
                    for (int max = Math.max(groupedMessagePosition2.minY - i, 0); max <= min; max++) {
                        fArr[max] = fArr[max] + groupedMessagePosition2.pw;
                    }
                }
            }
            for (int i6 = 0; i6 < i4; i6++) {
                if (f < fArr[i6]) {
                    f = fArr[i6];
                }
            }
            return f;
        }

        private float getTop(MessageObject.GroupedMessagePosition groupedMessagePosition, int i) {
            int i2 = this.maxX + 1;
            float[] fArr = new float[i2];
            float f = 0.0f;
            Arrays.fill(fArr, 0.0f);
            int size = this.posArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                MessageObject.GroupedMessagePosition groupedMessagePosition2 = this.posArray.get(i3);
                if (groupedMessagePosition2 != groupedMessagePosition && groupedMessagePosition2.maxY < i) {
                    for (int i4 = groupedMessagePosition2.minX; i4 <= groupedMessagePosition2.maxX; i4++) {
                        fArr[i4] = fArr[i4] + groupedMessagePosition2.ph;
                    }
                }
            }
            for (int i5 = 0; i5 < i2; i5++) {
                if (f < fArr[i5]) {
                    f = fArr[i5];
                }
            }
            return f;
        }
    }
}
