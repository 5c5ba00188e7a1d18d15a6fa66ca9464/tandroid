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
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PremiumPreviewFragment;

/* loaded from: classes3.dex */
public class StickerSetBulletinLayout extends Bulletin.TwoLineLayout {
    /* JADX WARN: Failed to find 'out' block for switch in B:37:0x0123. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0071 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0243  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x031b  */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public StickerSetBulletinLayout(final Context context, TLObject tLObject, int i, int i2, TLRPC.Document document, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        TLRPC.Document document2;
        TLRPC.StickerSet stickerSet;
        TLRPC.Document document3;
        ArrayList<TLRPC.Document> arrayList;
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
        TLRPC.StickerSet stickerSet2;
        ImageLocation forSticker;
        TLRPC.TL_messages_stickerSet stickerSet3;
        boolean z = tLObject instanceof TLRPC.TL_messages_stickerSet;
        if (z) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            stickerSet = tL_messages_stickerSet.set;
            arrayList = tL_messages_stickerSet.documents;
            if (arrayList == null || arrayList.isEmpty()) {
                document2 = null;
                if (stickerSet == null && document2 != null && (stickerSet3 = MediaDataController.getInstance(UserConfig.selectedAccount).getStickerSet(MessageObject.getInputStickerSet(document2), true)) != null) {
                    stickerSet = stickerSet3.set;
                }
                TLRPC.StickerSet stickerSet4 = stickerSet;
                if (document2 != null) {
                    TLObject closestPhotoSizeWithSize = stickerSet4 != null ? FileLoader.getClosestPhotoSizeWithSize(stickerSet4.thumbs, 90) : null;
                    closestPhotoSizeWithSize = closestPhotoSizeWithSize == null ? document2 : closestPhotoSizeWithSize;
                    boolean z2 = closestPhotoSizeWithSize instanceof TLRPC.Document;
                    if (z2) {
                        forSticker = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document2.thumbs, 90), document2);
                    } else {
                        TLRPC.PhotoSize photoSize = (TLRPC.PhotoSize) closestPhotoSizeWithSize;
                        if (tLObject instanceof TLRPC.StickerSetCovered) {
                            stickerSet2 = ((TLRPC.StickerSetCovered) tLObject).set;
                        } else if (z) {
                            stickerSet2 = ((TLRPC.TL_messages_stickerSet) tLObject).set;
                        } else {
                            i4 = 0;
                            forSticker = ImageLocation.getForSticker(photoSize, document2, i4);
                        }
                        i4 = stickerSet2.thumb_version;
                        forSticker = ImageLocation.getForSticker(photoSize, document2, i4);
                    }
                    ImageLocation imageLocation2 = forSticker;
                    if (z2 && (MessageObject.isAnimatedStickerDocument(document2, true) || MessageObject.isVideoSticker(document2) || MessageObject.isGifDocument(document2))) {
                        this.imageView.setImage(ImageLocation.getForDocument(document2), "50_50", imageLocation2, (String) null, 0, tLObject);
                        if (MessageObject.isTextColorEmoji(document2)) {
                            this.imageView.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
                        }
                        switch (i2) {
                            case 0:
                                if (stickerSet4 != null) {
                                    if (stickerSet4.masks) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.MasksRemoved));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("MasksRemovedInfo", R.string.MasksRemovedInfo, stickerSet4.title);
                                    } else if (stickerSet4.emojis) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.EmojiRemoved));
                                        textView = this.subtitleTextView;
                                        formatString = i > 1 ? LocaleController.formatPluralString("EmojiRemovedMultipleInfo", i, new Object[0]) : LocaleController.formatString("EmojiRemovedInfo", R.string.EmojiRemovedInfo, stickerSet4.title);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString(R.string.StickersRemoved));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("StickersRemovedInfo", R.string.StickersRemovedInfo, stickerSet4.title);
                                    }
                                    textView.setText(formatString);
                                    return;
                                }
                                return;
                            case 1:
                                if (stickerSet4 != null) {
                                    if (stickerSet4.masks) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.MasksArchived));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("MasksArchivedInfo", R.string.MasksArchivedInfo, stickerSet4.title);
                                    } else if (stickerSet4.emojis) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.EmojiArchived));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("EmojiArchivedInfo", R.string.EmojiArchivedInfo, stickerSet4.title);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString(R.string.StickersArchived));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("StickersArchivedInfo", R.string.StickersArchivedInfo, stickerSet4.title);
                                    }
                                    textView.setText(formatString);
                                    return;
                                }
                                return;
                            case 2:
                                if (stickerSet4 != null) {
                                    if (stickerSet4.masks) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.AddMasksInstalled));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("AddMasksInstalledInfo", R.string.AddMasksInstalledInfo, stickerSet4.title);
                                    } else if (stickerSet4.emojis) {
                                        this.titleTextView.setText(LocaleController.getString(R.string.AddEmojiInstalled));
                                        textView = this.subtitleTextView;
                                        formatString = i > 1 ? LocaleController.formatPluralString("AddEmojiMultipleInstalledInfo", i, new Object[0]) : LocaleController.formatString("AddEmojiInstalledInfo", R.string.AddEmojiInstalledInfo, stickerSet4.title);
                                    } else {
                                        this.titleTextView.setText(LocaleController.getString(R.string.AddStickersInstalled));
                                        textView = this.subtitleTextView;
                                        formatString = LocaleController.formatString("AddStickersInstalledInfo", R.string.AddStickersInstalledInfo, stickerSet4.title);
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
                if (MessageObject.isTextColorEmoji(document2)) {
                }
                switch (i2) {
                }
            }
            document3 = arrayList.get(0);
        } else if (tLObject instanceof TLRPC.StickerSetCovered) {
            TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered) tLObject;
            stickerSet = stickerSetCovered.set;
            TLRPC.Document document4 = stickerSetCovered.cover;
            if (document4 != null) {
                document3 = document4;
            } else if (stickerSetCovered.covers.isEmpty()) {
                document3 = null;
            } else {
                arrayList = stickerSetCovered.covers;
                document3 = arrayList.get(0);
            }
        } else {
            if (document == null && tLObject != null && BuildVars.DEBUG_VERSION) {
                throw new IllegalArgumentException("Invalid type of the given setObject: " + tLObject.getClass());
            }
            document2 = document;
            stickerSet = null;
            if (stickerSet == null) {
                stickerSet = stickerSet3.set;
            }
            TLRPC.StickerSet stickerSet42 = stickerSet;
            if (document2 != null) {
            }
            backupImageView.setImage(imageLocation, str2, str, drawable, tLObject);
            if (MessageObject.isTextColorEmoji(document2)) {
            }
            switch (i2) {
            }
        }
        document2 = document3;
        if (stickerSet == null) {
        }
        TLRPC.StickerSet stickerSet422 = stickerSet;
        if (document2 != null) {
        }
        backupImageView.setImage(imageLocation, str2, str, drawable, tLObject);
        if (MessageObject.isTextColorEmoji(document2)) {
        }
        switch (i2) {
        }
    }

    public StickerSetBulletinLayout(Context context, TLObject tLObject, int i, TLRPC.Document document, Theme.ResourcesProvider resourcesProvider) {
        this(context, tLObject, 1, i, document, resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$93(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(10)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$1(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$93(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(9)));
        }
    }
}
