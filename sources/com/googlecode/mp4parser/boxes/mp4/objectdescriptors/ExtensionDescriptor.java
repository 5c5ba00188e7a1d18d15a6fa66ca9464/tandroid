package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import org.telegram.messenger.NotificationCenter;
@Descriptor(tags = {19, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, NotificationCenter.dialogTranslate, 128, NotificationCenter.walletPendingTransactionsChanged, NotificationCenter.walletSyncProgressChanged, NotificationCenter.httpFileDidLoad, NotificationCenter.httpFileDidFailedLoad, NotificationCenter.didUpdateConnectionState, NotificationCenter.fileUploaded, NotificationCenter.fileUploadFailed, NotificationCenter.fileUploadProgressChanged, NotificationCenter.fileLoadProgressChanged, NotificationCenter.fileLoaded, NotificationCenter.fileLoadFailed, NotificationCenter.filePreparingStarted, NotificationCenter.fileNewChunkAvailable, NotificationCenter.filePreparingFailed, NotificationCenter.dialogsUnreadCounterChanged, NotificationCenter.messagePlayingProgressDidChanged, NotificationCenter.messagePlayingDidReset, NotificationCenter.messagePlayingPlayStateChanged, NotificationCenter.messagePlayingDidStart, NotificationCenter.messagePlayingDidSeek, NotificationCenter.messagePlayingGoingToStop, 150, NotificationCenter.recordStarted, NotificationCenter.recordStartError, NotificationCenter.recordStopped, NotificationCenter.recordPaused, 155, NotificationCenter.screenshotTook, NotificationCenter.albumsDidLoad, NotificationCenter.audioDidSent, NotificationCenter.audioRecordTooShort, NotificationCenter.audioRouteChanged, NotificationCenter.didStartedCall, NotificationCenter.groupCallUpdated, NotificationCenter.groupCallSpeakingUsersUpdated, NotificationCenter.groupCallScreencastStateChanged, NotificationCenter.activeGroupCallsUpdated, NotificationCenter.applyGroupCallVisibleParticipants, NotificationCenter.groupCallTypingsUpdated, NotificationCenter.didEndCall, NotificationCenter.closeInCallActivity, NotificationCenter.groupCallVisibilityChanged, NotificationCenter.appDidLogout, NotificationCenter.configLoaded, NotificationCenter.needDeleteDialog, NotificationCenter.newEmojiSuggestionsAvailable, NotificationCenter.themeUploadedToServer, NotificationCenter.themeUploadError, NotificationCenter.dialogFiltersUpdated, NotificationCenter.filterSettingsUpdated, NotificationCenter.suggestedFiltersLoaded, NotificationCenter.updateBotMenuButton, NotificationCenter.giftsToUserSent, NotificationCenter.didStartedMultiGiftsSelector, NotificationCenter.boostedChannelByUser, NotificationCenter.boostByChannelCreated, NotificationCenter.didUpdatePremiumGiftStickers, NotificationCenter.didUpdatePremiumGiftFieldIcon, NotificationCenter.storiesEnabledUpdate, NotificationCenter.storiesBlocklistUpdate, NotificationCenter.storiesLimitUpdate, NotificationCenter.storiesSendAsUpdate, NotificationCenter.unconfirmedAuthUpdate, NotificationCenter.dialogPhotosUpdate, NotificationCenter.channelRecommendationsLoaded, NotificationCenter.savedMessagesDialogsUpdate, NotificationCenter.savedReactionTagsUpdate, NotificationCenter.userIsPremiumBlockedUpadted, NotificationCenter.savedMessagesForwarded, NotificationCenter.emojiKeywordsLoaded, NotificationCenter.smsJobStatusUpdate, NotificationCenter.storyQualityUpdate, NotificationCenter.openBoostForUsersDialog, NotificationCenter.groupRestrictionsUnlockedByBoosts, 203, NotificationCenter.groupPackUpdated, NotificationCenter.timezonesUpdated, NotificationCenter.customStickerCreated, NotificationCenter.premiumFloodWaitReceived, NotificationCenter.availableEffectsUpdate, NotificationCenter.starOptionsLoaded, NotificationCenter.starGiftOptionsLoaded, NotificationCenter.starBalanceUpdated, NotificationCenter.starTransactionsLoaded, NotificationCenter.starSubscriptionsLoaded, NotificationCenter.factCheckLoaded, NotificationCenter.botStarsUpdated, NotificationCenter.botStarsTransactionsLoaded, NotificationCenter.channelStarsUpdated, NotificationCenter.webViewResolved, NotificationCenter.updateAllMessages, NotificationCenter.pushMessagesUpdated, NotificationCenter.wallpapersDidLoad, NotificationCenter.wallpapersNeedReload, NotificationCenter.didReceiveSmsCode, NotificationCenter.didReceiveCall, NotificationCenter.emojiLoaded, NotificationCenter.invalidateMotionBackground, NotificationCenter.closeOtherAppActivities, NotificationCenter.cameraInitied, NotificationCenter.didReplacedPhotoInMemCache, NotificationCenter.didSetNewTheme, NotificationCenter.themeListUpdated, NotificationCenter.didApplyNewTheme, NotificationCenter.themeAccentListUpdated, NotificationCenter.needCheckSystemBarColors, NotificationCenter.needShareTheme, NotificationCenter.needSetDayNightTheme, NotificationCenter.goingToPreviewTheme, NotificationCenter.locationPermissionGranted, NotificationCenter.locationPermissionDenied, NotificationCenter.reloadInterface, NotificationCenter.suggestedLangpack, NotificationCenter.didSetNewWallpapper, NotificationCenter.proxySettingsChanged, NotificationCenter.proxyCheckDone, NotificationCenter.proxyChangedByRotation, NotificationCenter.liveLocationsChanged, NotificationCenter.newLocationAvailable, NotificationCenter.liveLocationsCacheChanged, NotificationCenter.notificationsCountUpdated, NotificationCenter.playerDidStartPlaying, NotificationCenter.closeSearchByActiveAction, NotificationCenter.messagePlayingSpeedChanged, NotificationCenter.screenStateChanged})
/* loaded from: classes.dex */
public class ExtensionDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ExtensionDescriptor.class.getName());
    byte[] bytes;

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public void parseDetail(ByteBuffer byteBuffer) throws IOException {
        if (getSize() > 0) {
            byte[] bArr = new byte[this.sizeOfInstance];
            this.bytes = bArr;
            byteBuffer.get(bArr);
        }
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtensionDescriptor");
        sb.append("{bytes=");
        byte[] bArr = this.bytes;
        sb.append(bArr == null ? "null" : Hex.encodeHex(bArr));
        sb.append('}');
        return sb.toString();
    }
}
