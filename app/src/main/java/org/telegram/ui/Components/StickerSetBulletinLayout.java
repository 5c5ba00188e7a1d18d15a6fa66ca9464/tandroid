package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
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
@SuppressLint({"ViewConstructor"})
/* loaded from: classes3.dex */
public class StickerSetBulletinLayout extends Bulletin.TwoLineLayout {
    public StickerSetBulletinLayout(Context context, TLObject tLObject, int i) {
        this(context, tLObject, i, null, null);
    }

    public StickerSetBulletinLayout(Context context, TLObject tLObject, int i, TLRPC$Document tLRPC$Document, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        TLRPC$StickerSet tLRPC$StickerSet;
        TLRPC$Document tLRPC$Document2;
        int i2;
        ImageLocation forSticker;
        TLRPC$StickerSet tLRPC$StickerSet2;
        boolean z = tLObject instanceof TLRPC$TL_messages_stickerSet;
        TLRPC$PhotoSize tLRPC$PhotoSize = null;
        if (z) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
            tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet.set;
            ArrayList<TLRPC$Document> arrayList = tLRPC$TL_messages_stickerSet.documents;
            if (arrayList != null && !arrayList.isEmpty()) {
                tLRPC$Document2 = arrayList.get(0);
                tLRPC$StickerSet = tLRPC$StickerSet2;
            }
            tLRPC$Document2 = null;
            tLRPC$StickerSet = tLRPC$StickerSet2;
        } else if (tLObject instanceof TLRPC$StickerSetCovered) {
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) tLObject;
            tLRPC$StickerSet2 = tLRPC$StickerSetCovered.set;
            TLRPC$Document tLRPC$Document3 = tLRPC$StickerSetCovered.cover;
            if (tLRPC$Document3 != null) {
                tLRPC$Document2 = tLRPC$Document3;
            } else {
                if (!tLRPC$StickerSetCovered.covers.isEmpty()) {
                    tLRPC$Document2 = tLRPC$StickerSetCovered.covers.get(0);
                }
                tLRPC$Document2 = null;
            }
            tLRPC$StickerSet = tLRPC$StickerSet2;
        } else if (tLRPC$Document == null && tLObject != null && BuildVars.DEBUG_VERSION) {
            throw new IllegalArgumentException("Invalid type of the given setObject: " + tLObject.getClass());
        } else {
            tLRPC$Document2 = tLRPC$Document;
            tLRPC$StickerSet = null;
        }
        if (tLRPC$Document2 != null) {
            tLRPC$PhotoSize = tLRPC$StickerSet != null ? FileLoader.getClosestPhotoSizeWithSize(tLRPC$StickerSet.thumbs, 90) : tLRPC$PhotoSize;
            tLRPC$PhotoSize = tLRPC$PhotoSize == null ? tLRPC$Document2 : tLRPC$PhotoSize;
            boolean z2 = tLRPC$PhotoSize instanceof TLRPC$Document;
            if (z2) {
                forSticker = ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document2.thumbs, 90), tLRPC$Document2);
            } else {
                TLRPC$PhotoSize tLRPC$PhotoSize2 = tLRPC$PhotoSize;
                if (tLObject instanceof TLRPC$StickerSetCovered) {
                    i2 = ((TLRPC$StickerSetCovered) tLObject).set.thumb_version;
                } else {
                    i2 = z ? ((TLRPC$TL_messages_stickerSet) tLObject).set.thumb_version : 0;
                }
                forSticker = ImageLocation.getForSticker(tLRPC$PhotoSize2, tLRPC$Document2, i2);
            }
            ImageLocation imageLocation = forSticker;
            if ((z2 && MessageObject.isAnimatedStickerDocument(tLRPC$Document2, true)) || MessageObject.isVideoSticker(tLRPC$Document2) || MessageObject.isGifDocument(tLRPC$Document2)) {
                this.imageView.setImage(ImageLocation.getForDocument(tLRPC$Document2), "50_50", imageLocation, (String) null, 0, tLObject);
            } else if (imageLocation != null && imageLocation.imageType == 1) {
                this.imageView.setImage(imageLocation, "50_50", "tgs", (Drawable) null, tLObject);
            } else {
                this.imageView.setImage(imageLocation, "50_50", "webp", (Drawable) null, tLObject);
            }
        } else {
            this.imageView.setImage((ImageLocation) null, (String) null, "webp", (Drawable) null, tLObject);
        }
        if (tLRPC$StickerSet == null) {
            return;
        }
        switch (i) {
            case 0:
                if (tLRPC$StickerSet.masks) {
                    this.titleTextView.setText(LocaleController.getString("MasksRemoved", 2131626601));
                    this.subtitleTextView.setText(LocaleController.formatString("MasksRemovedInfo", 2131626602, tLRPC$StickerSet.title));
                    return;
                } else if (tLRPC$StickerSet.emojis) {
                    this.titleTextView.setText(LocaleController.getString("EmojiRemoved", 2131625646));
                    this.subtitleTextView.setText(LocaleController.formatString("EmojiRemovedInfo", 2131625647, tLRPC$StickerSet.title));
                    return;
                } else {
                    this.titleTextView.setText(LocaleController.getString("StickersRemoved", 2131628515));
                    this.subtitleTextView.setText(LocaleController.formatString("StickersRemovedInfo", 2131628516, tLRPC$StickerSet.title));
                    return;
                }
            case 1:
                if (tLRPC$StickerSet.masks) {
                    this.titleTextView.setText(LocaleController.getString("MasksArchived", 2131626592));
                    this.subtitleTextView.setText(LocaleController.formatString("MasksArchivedInfo", 2131626593, tLRPC$StickerSet.title));
                    return;
                }
                this.titleTextView.setText(LocaleController.getString("StickersArchived", 2131628508));
                this.subtitleTextView.setText(LocaleController.formatString("StickersArchivedInfo", 2131628509, tLRPC$StickerSet.title));
                return;
            case 2:
                if (tLRPC$StickerSet.masks) {
                    this.titleTextView.setText(LocaleController.getString("AddMasksInstalled", 2131624275));
                    this.subtitleTextView.setText(LocaleController.formatString("AddMasksInstalledInfo", 2131624276, tLRPC$StickerSet.title));
                    return;
                } else if (tLRPC$StickerSet.emojis) {
                    this.titleTextView.setText(LocaleController.getString("AddEmojiInstalled", 2131624270));
                    this.subtitleTextView.setText(LocaleController.formatString("AddEmojiInstalledInfo", 2131624271, tLRPC$StickerSet.title));
                    return;
                } else {
                    this.titleTextView.setText(LocaleController.getString("AddStickersInstalled", 2131624292));
                    this.subtitleTextView.setText(LocaleController.formatString("AddStickersInstalledInfo", 2131624293, tLRPC$StickerSet.title));
                    return;
                }
            case 3:
                this.titleTextView.setText(LocaleController.getString("RemovedFromRecent", 2131627968));
                this.subtitleTextView.setVisibility(8);
                return;
            case 4:
                this.titleTextView.setText(LocaleController.getString("RemovedFromFavorites", 2131627967));
                this.subtitleTextView.setVisibility(8);
                return;
            case 5:
                this.titleTextView.setText(LocaleController.getString("AddedToFavorites", 2131624312));
                this.subtitleTextView.setVisibility(8);
                return;
            case 6:
                if (!UserConfig.getInstance(UserConfig.selectedAccount).isPremium() && !MessagesController.getInstance(UserConfig.selectedAccount).premiumLocked) {
                    this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickers", 2131626461, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitDefault)));
                    this.subtitleTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString("LimitReachedFavoriteStickersSubtitle", 2131626462, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitPremium)), new StickerSetBulletinLayout$$ExternalSyntheticLambda0(context)));
                    return;
                }
                this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickers", 2131626461, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).stickersFavedLimitPremium)));
                this.subtitleTextView.setText(LocaleController.formatString("LimitReachedFavoriteStickersSubtitlePremium", 2131626463, new Object[0]));
                return;
            case 7:
                if (!UserConfig.getInstance(UserConfig.selectedAccount).isPremium() && !MessagesController.getInstance(UserConfig.selectedAccount).premiumLocked) {
                    this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteGifs", 2131626458, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitDefault)));
                    this.subtitleTextView.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString("LimitReachedFavoriteGifsSubtitle", 2131626459, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitPremium)), new StickerSetBulletinLayout$$ExternalSyntheticLambda1(context)));
                    return;
                }
                this.titleTextView.setText(LocaleController.formatString("LimitReachedFavoriteGifs", 2131626458, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).savedGifsLimitPremium)));
                this.subtitleTextView.setText(LocaleController.formatString("LimitReachedFavoriteGifsSubtitlePremium", 2131626460, new Object[0]));
                return;
            default:
                return;
        }
    }

    public static /* synthetic */ void lambda$new$0(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$59(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(10)));
        }
    }

    public static /* synthetic */ void lambda$new$1(Context context) {
        Activity findActivity = AndroidUtilities.findActivity(context);
        if (findActivity instanceof LaunchActivity) {
            ((LaunchActivity) findActivity).lambda$runLinkRequest$59(new PremiumPreviewFragment(LimitReachedBottomSheet.limitTypeToServerString(9)));
        }
    }
}
