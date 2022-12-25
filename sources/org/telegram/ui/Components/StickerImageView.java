package org.telegram.ui.Components;

import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SvgHelper;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
/* loaded from: classes3.dex */
public class StickerImageView extends BackupImageView implements NotificationCenter.NotificationCenterDelegate {
    int currentAccount;
    int stickerNum;
    String stickerPackName;

    public StickerImageView(Context context, int i) {
        super(context);
        this.stickerPackName = AndroidUtilities.STICKERS_PLACEHOLDER_PACK_NAME;
        this.currentAccount = i;
    }

    public void setStickerNum(int i) {
        if (this.stickerNum != i) {
            this.stickerNum = i;
            setSticker();
        }
    }

    public void setStickerPackName(String str) {
        this.stickerPackName = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.BackupImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSticker();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.BackupImageView, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.diceStickersDidLoad) {
            if (this.stickerPackName.equals((String) objArr[0])) {
                setSticker();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setSticker() {
        TLRPC$Document tLRPC$Document;
        SvgHelper.SvgDrawable svgThumb;
        TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(this.stickerPackName);
        if (stickerSetByName == null) {
            stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(this.stickerPackName);
        }
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSetByName;
        if (tLRPC$TL_messages_stickerSet != null) {
            int size = tLRPC$TL_messages_stickerSet.documents.size();
            int i = this.stickerNum;
            if (size > i) {
                tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i);
                svgThumb = tLRPC$Document != null ? DocumentObject.getSvgThumb(tLRPC$Document.thumbs, "emptyListPlaceholder", 0.2f) : null;
                if (svgThumb != null) {
                    svgThumb.overrideWidthAndHeight(512, 512);
                }
                if (tLRPC$Document == null) {
                    setImage(ImageLocation.getForDocument(tLRPC$Document), "130_130", "tgs", svgThumb, tLRPC$TL_messages_stickerSet);
                    return;
                }
                this.imageReceiver.clearImage();
                MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName(this.stickerPackName, false, tLRPC$TL_messages_stickerSet == null);
                return;
            }
        }
        tLRPC$Document = null;
        svgThumb = tLRPC$Document != null ? DocumentObject.getSvgThumb(tLRPC$Document.thumbs, "emptyListPlaceholder", 0.2f) : null;
        if (svgThumb != null) {
        }
        if (tLRPC$Document == null) {
        }
    }
}
