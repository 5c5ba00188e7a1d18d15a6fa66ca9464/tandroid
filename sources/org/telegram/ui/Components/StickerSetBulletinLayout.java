package org.telegram.ui.Components;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PremiumPreviewFragment;
/* loaded from: classes3.dex */
public class StickerSetBulletinLayout extends Bulletin.TwoLineLayout {
    /* JADX WARN: Removed duplicated region for block: B:107:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x031b  */
    /* JADX WARN: Removed duplicated region for block: B:132:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0071 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x024d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public StickerSetBulletinLayout(final Context context, TLObject tLObject, int i, int i2, TLRPC$Document tLRPC$Document, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        TLRPC$Document tLRPC$Document2;
        TLRPC$StickerSet tLRPC$StickerSet;
        TLRPC$Document tLRPC$Document3;
        ArrayList arrayList;
        BackupImageView backupImageView;
        String str;
        Drawable drawable;
        ImageLocation imageLocation;
        String str2;
        TextView textView;
        String formatString;
        TextView textView2;
        int i3;
        String formatString2;
        Runnable runnable;
        int i4;
        TLRPC$StickerSet tLRPC$StickerSet2;
        ImageLocation forSticker;
        TLRPC$TL_messages_stickerSet stickerSet;
        boolean z = tLObject instanceof TLRPC$TL_messages_stickerSet;
        if (z) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
            tLRPC$StickerSet = tLRPC$TL_messages_stickerSet.set;
            arrayList = tLRPC$TL_messages_stickerSet.documents;
            if (arrayList == null || arrayList.isEmpty()) {
                tLRPC$Document2 = null;
                if (tLRPC$StickerSet == null && tLRPC$Document2 != null && (stickerSet = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSet(MessageObject.getInputStickerSet(tLRPC$Document2), true)) != null) {
                    tLRPC$StickerSet = stickerSet.set;
                }
                TLRPC$StickerSet tLRPC$StickerSet3 = tLRPC$StickerSet;
                if (tLRPC$Document2 != null) {
                    TLRPC$PhotoSize closestPhotoSizeWithSize = tLRPC$StickerSet3 != null ? FileLoader.getClosestPhotoSizeWithSize(tLRPC$StickerSet3.thumbs, 90) : null;
                    closestPhotoSizeWithSize = closestPhotoSizeWithSize == null ? tLRPC$Document2 : closestPhotoSizeWithSize;
                    boolean z2 = closestPhotoSizeWithSize instanceof TLRPC$Document;
                    if (z2) {
                        forSticker = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document2.thumbs, 90), tLRPC$Document2);
                    } else {
                        TLRPC$PhotoSize tLRPC$PhotoSize = closestPhotoSizeWithSize;
                        if (tLObject instanceof TLRPC$StickerSetCovered) {
                            tLRPC$StickerSet2 = ((TLRPC$StickerSetCovered) tLObject).set;
                        } else if (z) {
                            tLRPC$StickerSet2 = ((TLRPC$TL_messages_stickerSet) tLObject).set;
                        } else {
                            i4 = 0;
                            forSticker = ImageLocation.getForSticker(tLRPC$PhotoSize, tLRPC$Document2, i4);
                        }
                        i4 = tLRPC$StickerSet2.thumb_version;
                        forSticker = ImageLocation.getForSticker(tLRPC$PhotoSize, tLRPC$Document2, i4);
                    }
                    ImageLocation imageLocation2 = forSticker;
                    if (z2 && (MessageObject.isAnimatedStickerDocument(tLRPC$Document2, true) || MessageObject.isVideoSticker(tLRPC$Document2) || MessageObject.isGifDocument(tLRPC$Document2))) {
                        this.imageView.setImage(ImageLocation.getForDocument(tLRPC$Document2), "50_50", imageLocation2, (String) null, 0, tLObject);
                        if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
                            this.imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                        }
                        switch (i2) {
                            case 0:
                                if (tLRPC$StickerSet3 != null) {
                                    if (tLRPC$StickerSet3.masks) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.MasksRemoved));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("MasksRemovedInfo", R.string.MasksRemovedInfo, tLRPC$StickerSet3.title);
                                    } else if (tLRPC$StickerSet3.emojis) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.EmojiRemoved));
                                        textView = this.subtitleTextView;
                                        formatString = i > 1 ? LocaleController.formatPluralString("EmojiRemovedMultipleInfo", i, new Object[0]) : LocaleController.formatString("EmojiRemovedInfo", R.string.EmojiRemovedInfo, tLRPC$StickerSet3.title);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString(R.string.StickersRemoved));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("StickersRemovedInfo", R.string.StickersRemovedInfo, tLRPC$StickerSet3.title);
                                    }
                                    textView.setText(formatString);
                                    return;
                                }
                                return;
                            case 1:
                                if (tLRPC$StickerSet3 != null) {
                                    if (tLRPC$StickerSet3.masks) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.MasksArchived));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("MasksArchivedInfo", R.string.MasksArchivedInfo, tLRPC$StickerSet3.title);
                                    } else if (tLRPC$StickerSet3.emojis) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.EmojiArchived));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("EmojiArchivedInfo", R.string.EmojiArchivedInfo, tLRPC$StickerSet3.title);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString(R.string.StickersArchived));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("StickersArchivedInfo", R.string.StickersArchivedInfo, tLRPC$StickerSet3.title);
                                    }
                                    textView.setText(formatString);
                                    return;
                                }
                                return;
                            case 2:
                                if (tLRPC$StickerSet3 != null) {
                                    if (tLRPC$StickerSet3.masks) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.AddMasksInstalled));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("AddMasksInstalledInfo", R.string.AddMasksInstalledInfo, tLRPC$StickerSet3.title);
                                    } else if (tLRPC$StickerSet3.emojis) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.AddEmojiInstalled));
                                        textView = this.subtitleTextView;
                                        formatString = i > 1 ? LocaleController.formatPluralString("AddEmojiMultipleInstalledInfo", i, new Object[0]) : LocaleController.formatString("AddEmojiInstalledInfo", R.string.AddEmojiInstalledInfo, tLRPC$StickerSet3.title);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString(R.string.AddStickersInstalled));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("AddStickersInstalledInfo", R.string.AddStickersInstalledInfo, tLRPC$StickerSet3.title);
                                    }
                                    textView.setText(formatString);
                                    return;
                                }
                                return;
                            case 3:
                                textView2 = this.titleTextView;
                                i3 = R.string.RemovedFromRecent;
                                textView2.setText(LocaleController.getString(i3));
                                this.subtitleTextView.setVisibility(8);
                                return;
                            case 4:
                                textView2 = this.titleTextView;
                                i3 = R.string.RemovedFromFavorites;
                                textView2.setText(LocaleController.getString(i3));
                                this.subtitleTextView.setVisibility(8);
                                return;
                            case 5:
                                textView2 = this.titleTextView;
                                i3 = R.string.AddedToFavorites;
                                textView2.setText(LocaleController.getString(i3));
                                this.subtitleTextView.setVisibility(8);
                                return;
                            case 6:
                                if (UserConfig.getInstance(UserConfig.selectedAccount).isPremium() || MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked()) {
                                    this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickers", R.string.LimitReachedFavoriteStickers, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitPremium)));
                                    textView = this.subtitleTextView;
                                    formatString = LocaleController.formatString("LimitReachedFavoriteStickersSubtitlePremium", R.string.LimitReachedFavoriteStickersSubtitlePremium, new Object[0]);
                                    textView.setText(formatString);
                                    return;
                                }
                                this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickers", R.string.LimitReachedFavoriteStickers, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitDefault)));
                                formatString2 = LocaleController.formatString("LimitReachedFavoriteStickersSubtitle", R.string.LimitReachedFavoriteStickersSubtitle, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitPremium));
                                runnable = new Runnable() { // from class: org.telegram.ui.Components.StickerSetBulletinLayout$$ExternalSyntheticLambda1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StickerSetBulletinLayout.lambda$new$0(context);
                                    }
                                };
                                this.subtitleTextView.setText(AndroidUtilities.premiumText(formatString2, runnable));
                                return;
                            case 7:
                                boolean isPremium = UserConfig.getInstance(UserConfig.selectedAccount).isPremium();
                                if (MessagesController.getInstance(UserConfig.selectedAccount).premiumFeaturesBlocked() || isPremium) {
                                    this.titleTextView.setText(LocaleController.formatString(R.string.LimitReachedFavoriteGifs, Integer.valueOf(isPremium ? MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitPremium : MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitDefault)));
                                    textView = this.subtitleTextView;
                                    formatString = LocaleController.getString(R.string.LimitReachedFavoriteGifsSubtitlePremium);
                                    textView.setText(formatString);
                                    return;
                                }
                                this.titleTextView.setText(LocaleController.formatString(R.string.LimitReachedFavoriteGifs, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitDefault)));
                                formatString2 = LocaleController.formatString(R.string.LimitReachedFavoriteGifsSubtitle, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitPremium));
                                runnable = new Runnable() { // from class: org.telegram.ui.Components.StickerSetBulletinLayout$$ExternalSyntheticLambda0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StickerSetBulletinLayout.lambda$new$1(context);
                                    }
                                };
                                this.subtitleTextView.setText(AndroidUtilities.premiumText(formatString2, runnable));
                                return;
                            default:
                                return;
                        }
                    }
                    if (imageLocation2 == null || imageLocation2.imageType != 1) {
                        backupImageView = this.imageView;
                        str = "webp";
                    } else {
                        backupImageView = this.imageView;
                        str = "tgs";
                    }
                    str2 = "50_50";
                    imageLocation = imageLocation2;
                    drawable = null;
                } else {
                    backupImageView = this.imageView;
                    str = "webp";
                    drawable = null;
                    imageLocation = null;
                    str2 = null;
                }
                backupImageView.setImage(imageLocation, str2, str, drawable, tLObject);
                if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
                }
                switch (i2) {
                }
            }
            tLRPC$Document3 = (TLRPC$Document) arrayList.get(0);
        } else if (tLObject instanceof TLRPC$StickerSetCovered) {
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) tLObject;
            tLRPC$StickerSet = tLRPC$StickerSetCovered.set;
            TLRPC$Document tLRPC$Document4 = tLRPC$StickerSetCovered.cover;
            if (tLRPC$Document4 != null) {
                tLRPC$Document3 = tLRPC$Document4;
            } else if (tLRPC$StickerSetCovered.covers.isEmpty()) {
                tLRPC$Document3 = null;
            } else {
                arrayList = tLRPC$StickerSetCovered.covers;
                tLRPC$Document3 = (TLRPC$Document) arrayList.get(0);
            }
        } else if (tLRPC$Document == null && tLObject != null && BuildVars.DEBUG_VERSION) {
            throw new IllegalArgumentException("Invalid type of the given setObject: " + tLObject.getClass());
        } else {
            tLRPC$Document2 = tLRPC$Document;
            tLRPC$StickerSet = null;
            if (tLRPC$StickerSet == null) {
                tLRPC$StickerSet = stickerSet.set;
            }
            TLRPC$StickerSet tLRPC$StickerSet32 = tLRPC$StickerSet;
            if (tLRPC$Document2 != null) {
            }
            backupImageView.setImage(imageLocation, str2, str, drawable, tLObject);
            if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
            }
            switch (i2) {
            }
        }
        tLRPC$Document2 = tLRPC$Document3;
        if (tLRPC$StickerSet == null) {
        }
        TLRPC$StickerSet tLRPC$StickerSet322 = tLRPC$StickerSet;
        if (tLRPC$Document2 != null) {
        }
        backupImageView.setImage(imageLocation, str2, str, drawable, tLObject);
        if (MessageObject.isTextColorEmoji(tLRPC$Document2)) {
        }
        switch (i2) {
        }
    }

    public StickerSetBulletinLayout(Context context, TLObject tLObject, int i, TLRPC$Document tLRPC$Document, Theme.ResourcesProvider resourcesProvider) {
        this(context, tLObject, 1, i, tLRPC$Document, resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$91(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(10)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$91(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(9)));
        }
    }
}
