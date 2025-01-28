package org.telegram.messenger;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.StoriesController;

/* loaded from: classes3.dex */
public class FileRefController extends BaseController {
    private static volatile FileRefController[] Instance = new FileRefController[4];
    private ArrayList<Waiter> favStickersWaiter;
    private long lastCleanupTime;
    private HashMap<String, ArrayList<Requester>> locationRequester;
    private HashMap<TLObject, Object[]> multiMediaCache;
    private HashMap<String, ArrayList<Requester>> parentRequester;
    private ArrayList<Waiter> recentStickersWaiter;
    private HashMap<String, CachedResult> responseCache;
    private ArrayList<Waiter> savedGifsWaiters;
    private ArrayList<Waiter> wallpaperWaiters;

    private static class CachedResult {
        private long firstQueryTime;
        private TLObject response;

        private CachedResult() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Requester {
        private Object[] args;
        private boolean completed;
        private TLRPC.InputFileLocation location;
        private String locationKey;

        private Requester() {
        }
    }

    private static class Waiter {
        private String locationKey;
        private String parentKey;

        public Waiter(String str, String str2) {
            this.locationKey = str;
            this.parentKey = str2;
        }
    }

    public FileRefController(int i) {
        super(i);
        this.locationRequester = new HashMap<>();
        this.parentRequester = new HashMap<>();
        this.responseCache = new HashMap<>();
        this.multiMediaCache = new HashMap<>();
        this.lastCleanupTime = SystemClock.elapsedRealtime();
        this.wallpaperWaiters = new ArrayList<>();
        this.savedGifsWaiters = new ArrayList<>();
        this.recentStickersWaiter = new ArrayList<>();
        this.favStickersWaiter = new ArrayList<>();
    }

    private void broadcastWaitersData(ArrayList<Waiter> arrayList, TLObject tLObject, TLRPC.TL_error tL_error) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Waiter waiter = arrayList.get(i);
            onRequestComplete(waiter.locationKey, waiter.parentKey, tLObject, tL_error, i == size + (-1), false);
            i++;
        }
        arrayList.clear();
    }

    private void cleanupCache() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastCleanupTime) < 600000) {
            return;
        }
        this.lastCleanupTime = SystemClock.elapsedRealtime();
        ArrayList arrayList = null;
        for (Map.Entry<String, CachedResult> entry : this.responseCache.entrySet()) {
            if (Math.abs(System.currentTimeMillis() - entry.getValue().firstQueryTime) >= 60000) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(entry.getKey());
            }
        }
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.responseCache.remove(arrayList.get(i));
            }
        }
    }

    private CachedResult getCachedResponse(String str) {
        CachedResult cachedResult = this.responseCache.get(str);
        if (cachedResult == null || Math.abs(System.currentTimeMillis() - cachedResult.firstQueryTime) < 60000) {
            return cachedResult;
        }
        this.responseCache.remove(str);
        return null;
    }

    public static int getFileRefErrorIndex(String str) {
        if (str != null && str.startsWith("FILE_REFERENCE_") && str.endsWith("_EXPIRED")) {
            try {
                return Integer.parseInt(str.substring(15, str.length() - 8));
            } catch (Exception unused) {
            }
        }
        return -1;
    }

    private byte[] getFileReference(TLRPC.Chat chat, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        TLRPC.ChatPhoto chatPhoto;
        byte[] bArr = null;
        if (chat != null && (chatPhoto = chat.photo) != null && ((inputFileLocation instanceof TLRPC.TL_inputFileLocation) || (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation))) {
            if (inputFileLocation instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
                zArr[0] = true;
                if (getPeerReferenceReplacement(null, chat, false, inputFileLocation, inputFileLocationArr, zArr)) {
                    return new byte[0];
                }
                return null;
            }
            bArr = getFileReference(chatPhoto.photo_small, inputFileLocation, zArr);
            if (getPeerReferenceReplacement(null, chat, false, inputFileLocation, inputFileLocationArr, zArr)) {
                return new byte[0];
            }
            if (bArr == null) {
                bArr = getFileReference(chat.photo.photo_big, inputFileLocation, zArr);
                if (getPeerReferenceReplacement(null, chat, true, inputFileLocation, inputFileLocationArr, zArr)) {
                    return new byte[0];
                }
            }
        }
        return bArr;
    }

    private byte[] getFileReference(TLRPC.Document document, ArrayList<TLRPC.Document> arrayList, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        if (document != null && inputFileLocation != null) {
            if (!(inputFileLocation instanceof TLRPC.TL_inputDocumentFileLocation)) {
                int size = document.thumbs.size();
                for (int i = 0; i < size; i++) {
                    TLRPC.PhotoSize photoSize = document.thumbs.get(i);
                    byte[] fileReference = getFileReference(photoSize, inputFileLocation, zArr);
                    if (zArr != null && zArr[0]) {
                        TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                        inputFileLocationArr[0] = tL_inputDocumentFileLocation;
                        tL_inputDocumentFileLocation.id = document.id;
                        tL_inputDocumentFileLocation.volume_id = inputFileLocation.volume_id;
                        tL_inputDocumentFileLocation.local_id = inputFileLocation.local_id;
                        tL_inputDocumentFileLocation.access_hash = document.access_hash;
                        byte[] bArr = document.file_reference;
                        tL_inputDocumentFileLocation.file_reference = bArr;
                        tL_inputDocumentFileLocation.thumb_size = photoSize.type;
                        return bArr;
                    }
                    if (fileReference != null) {
                        return fileReference;
                    }
                }
            } else if (document.id == inputFileLocation.id) {
                return document.file_reference;
            }
            if (arrayList != null) {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    byte[] fileReference2 = getFileReference(arrayList.get(i2), null, inputFileLocation, zArr, inputFileLocationArr);
                    if (fileReference2 != null) {
                        return fileReference2;
                    }
                }
            }
        }
        return null;
    }

    private byte[] getFileReference(TLRPC.FileLocation fileLocation, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr) {
        if (fileLocation == null || !(inputFileLocation instanceof TLRPC.TL_inputFileLocation) || fileLocation.local_id != inputFileLocation.local_id || fileLocation.volume_id != inputFileLocation.volume_id) {
            return null;
        }
        byte[] bArr = fileLocation.file_reference;
        if (bArr == null && zArr != null) {
            zArr[0] = true;
        }
        return bArr;
    }

    private byte[] getFileReference(TLRPC.Photo photo, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        if (photo == null) {
            return null;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputPhotoFileLocation) {
            if (photo.id == inputFileLocation.id) {
                return photo.file_reference;
            }
            return null;
        }
        if (inputFileLocation instanceof TLRPC.TL_inputFileLocation) {
            int size = photo.sizes.size();
            for (int i = 0; i < size; i++) {
                TLRPC.PhotoSize photoSize = photo.sizes.get(i);
                byte[] fileReference = getFileReference(photoSize, inputFileLocation, zArr);
                if (zArr != null && zArr[0]) {
                    TLRPC.TL_inputPhotoFileLocation tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                    inputFileLocationArr[0] = tL_inputPhotoFileLocation;
                    tL_inputPhotoFileLocation.id = photo.id;
                    tL_inputPhotoFileLocation.volume_id = inputFileLocation.volume_id;
                    tL_inputPhotoFileLocation.local_id = inputFileLocation.local_id;
                    tL_inputPhotoFileLocation.access_hash = photo.access_hash;
                    byte[] bArr = photo.file_reference;
                    tL_inputPhotoFileLocation.file_reference = bArr;
                    tL_inputPhotoFileLocation.thumb_size = photoSize.type;
                    return bArr;
                }
                if (fileReference != null) {
                    return fileReference;
                }
            }
        }
        return null;
    }

    private byte[] getFileReference(TLRPC.PhotoSize photoSize, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr) {
        if (photoSize == null || !(inputFileLocation instanceof TLRPC.TL_inputFileLocation)) {
            return null;
        }
        return getFileReference(photoSize.location, inputFileLocation, zArr);
    }

    private byte[] getFileReference(TLRPC.User user, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        TLRPC.UserProfilePhoto userProfilePhoto;
        if (user == null || (userProfilePhoto = user.photo) == null || !(inputFileLocation instanceof TLRPC.TL_inputFileLocation)) {
            return null;
        }
        byte[] fileReference = getFileReference(userProfilePhoto.photo_small, inputFileLocation, zArr);
        if (getPeerReferenceReplacement(user, null, false, inputFileLocation, inputFileLocationArr, zArr)) {
            return new byte[0];
        }
        if (fileReference == null) {
            fileReference = getFileReference(user.photo.photo_big, inputFileLocation, zArr);
            if (getPeerReferenceReplacement(user, null, true, inputFileLocation, inputFileLocationArr, zArr)) {
                return new byte[0];
            }
        }
        return fileReference;
    }

    private byte[] getFileReference(TLRPC.WebPage webPage, TLRPC.InputFileLocation inputFileLocation, boolean[] zArr, TLRPC.InputFileLocation[] inputFileLocationArr) {
        byte[] fileReference = getFileReference(webPage.document, null, inputFileLocation, zArr, inputFileLocationArr);
        if (fileReference != null) {
            return fileReference;
        }
        byte[] fileReference2 = getFileReference(webPage.photo, inputFileLocation, zArr, inputFileLocationArr);
        if (fileReference2 != null) {
            return fileReference2;
        }
        if (!webPage.attributes.isEmpty()) {
            int size = webPage.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC.WebPageAttribute webPageAttribute = webPage.attributes.get(i);
                if (webPageAttribute instanceof TLRPC.TL_webPageAttributeTheme) {
                    TLRPC.TL_webPageAttributeTheme tL_webPageAttributeTheme = (TLRPC.TL_webPageAttributeTheme) webPageAttribute;
                    int size2 = tL_webPageAttributeTheme.documents.size();
                    int i2 = 0;
                    while (i2 < size2) {
                        int i3 = i2;
                        byte[] fileReference3 = getFileReference(tL_webPageAttributeTheme.documents.get(i2), null, inputFileLocation, zArr, inputFileLocationArr);
                        if (fileReference3 != null) {
                            return fileReference3;
                        }
                        i2 = i3 + 1;
                    }
                }
            }
        }
        TLRPC.Page page = webPage.cached_page;
        if (page == null) {
            return null;
        }
        int size3 = page.documents.size();
        int i4 = 0;
        while (true) {
            TLRPC.Page page2 = webPage.cached_page;
            if (i4 >= size3) {
                int size4 = page2.photos.size();
                for (int i5 = 0; i5 < size4; i5++) {
                    byte[] fileReference4 = getFileReference(webPage.cached_page.photos.get(i5), inputFileLocation, zArr, inputFileLocationArr);
                    if (fileReference4 != null) {
                        return fileReference4;
                    }
                }
                return null;
            }
            byte[] fileReference5 = getFileReference(page2.documents.get(i4), null, inputFileLocation, zArr, inputFileLocationArr);
            if (fileReference5 != null) {
                return fileReference5;
            }
            i4++;
        }
    }

    public static FileRefController getInstance(int i) {
        FileRefController fileRefController = Instance[i];
        if (fileRefController == null) {
            synchronized (FileRefController.class) {
                try {
                    fileRefController = Instance[i];
                    if (fileRefController == null) {
                        FileRefController[] fileRefControllerArr = Instance;
                        FileRefController fileRefController2 = new FileRefController(i);
                        fileRefControllerArr[i] = fileRefController2;
                        fileRefController = fileRefController2;
                    }
                } finally {
                }
            }
        }
        return fileRefController;
    }

    public static String getKeyForParentObject(Object obj) {
        TLRPC.Message message;
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        String str;
        if (obj instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj;
            if (botPreview.list != null) {
                TLRPC.MessageMedia messageMedia = botPreview.media;
                if (messageMedia.document != null) {
                    return "botstory_doc_" + botPreview.media.document.id;
                }
                if (messageMedia.photo != null) {
                    return "botstory_photo_" + botPreview.media.photo.id;
                }
                return "botstory_" + botPreview.id;
            }
            str = "failed request reference can't find list in botpreview";
        } else {
            if (!(obj instanceof TL_stories.StoryItem)) {
                if (obj instanceof TLRPC.TL_help_premiumPromo) {
                    return "premium_promo";
                }
                if (obj instanceof TLRPC.TL_availableReaction) {
                    return "available_reaction_" + ((TLRPC.TL_availableReaction) obj).reaction;
                }
                if (obj instanceof TL_bots.BotInfo) {
                    return "bot_info_" + ((TL_bots.BotInfo) obj).user_id;
                }
                if (obj instanceof TLRPC.TL_attachMenuBot) {
                    return "attach_menu_bot_" + ((TLRPC.TL_attachMenuBot) obj).bot_id;
                }
                if (obj instanceof MessageObject) {
                    MessageObject messageObject = (MessageObject) obj;
                    long channelId = messageObject.getChannelId();
                    if (messageObject.type == 29 && (message = messageObject.messageOwner) != null && (messageFwdHeader = message.fwd_from) != null && (peer = messageFwdHeader.from_id) != null) {
                        channelId = DialogObject.getPeerDialogId(peer);
                    }
                    return "message" + messageObject.getRealId() + "_" + channelId + "_" + messageObject.scheduled + "_" + messageObject.getQuickReplyId();
                }
                if (obj instanceof TLRPC.Message) {
                    TLRPC.Message message2 = (TLRPC.Message) obj;
                    TLRPC.Peer peer2 = message2.peer_id;
                    return "message" + message2.id + "_" + (peer2 != null ? peer2.channel_id : 0L) + "_" + message2.from_scheduled;
                }
                if (obj instanceof TLRPC.WebPage) {
                    return "webpage" + ((TLRPC.WebPage) obj).id;
                }
                if (obj instanceof TLRPC.User) {
                    return "user" + ((TLRPC.User) obj).id;
                }
                if (obj instanceof TLRPC.Chat) {
                    return "chat" + ((TLRPC.Chat) obj).id;
                }
                if (obj instanceof String) {
                    return "str" + ((String) obj);
                }
                if (obj instanceof TLRPC.TL_messages_stickerSet) {
                    return "set" + ((TLRPC.TL_messages_stickerSet) obj).set.id;
                }
                if (obj instanceof TLRPC.StickerSetCovered) {
                    return "set" + ((TLRPC.StickerSetCovered) obj).set.id;
                }
                if (obj instanceof TLRPC.InputStickerSet) {
                    return "set" + ((TLRPC.InputStickerSet) obj).id;
                }
                if (obj instanceof TLRPC.TL_wallPaper) {
                    return "wallpaper" + ((TLRPC.TL_wallPaper) obj).id;
                }
                if (obj instanceof TLRPC.TL_theme) {
                    return "theme" + ((TLRPC.TL_theme) obj).id;
                }
                if (obj == null) {
                    return null;
                }
                return "" + obj;
            }
            TL_stories.StoryItem storyItem = (TL_stories.StoryItem) obj;
            if (storyItem.dialogId != 0) {
                return "story_" + storyItem.dialogId + "_" + storyItem.id;
            }
            str = "failed request reference can't find dialogId";
        }
        FileLog.d(str);
        return null;
    }

    private String getObjectString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof TL_stories.StoryItem) {
            TL_stories.StoryItem storyItem = (TL_stories.StoryItem) obj;
            return "story(dialogId=" + storyItem.dialogId + " id=" + storyItem.id + ")";
        }
        if (!(obj instanceof MessageObject)) {
            if (obj == null) {
                return null;
            }
            return obj.getClass().getSimpleName();
        }
        MessageObject messageObject = (MessageObject) obj;
        return "message(dialogId=" + messageObject.getDialogId() + "messageId" + messageObject.getId() + ")";
    }

    private boolean getPeerReferenceReplacement(TLRPC.User user, TLRPC.Chat chat, boolean z, TLRPC.InputFileLocation inputFileLocation, TLRPC.InputFileLocation[] inputFileLocationArr, boolean[] zArr) {
        TLRPC.InputPeer tL_inputPeerChat;
        TLRPC.InputPeer inputPeer;
        if (zArr == null || !zArr[0]) {
            return false;
        }
        TLRPC.TL_inputPeerPhotoFileLocation tL_inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
        long j = inputFileLocation.volume_id;
        tL_inputPeerPhotoFileLocation.id = j;
        tL_inputPeerPhotoFileLocation.volume_id = j;
        tL_inputPeerPhotoFileLocation.local_id = inputFileLocation.local_id;
        tL_inputPeerPhotoFileLocation.big = z;
        if (user != null) {
            inputPeer = new TLRPC.TL_inputPeerUser();
            inputPeer.user_id = user.id;
            inputPeer.access_hash = user.access_hash;
            tL_inputPeerPhotoFileLocation.photo_id = user.photo.photo_id;
        } else {
            if (ChatObject.isChannel(chat)) {
                tL_inputPeerChat = new TLRPC.TL_inputPeerChannel();
                tL_inputPeerChat.channel_id = chat.id;
                tL_inputPeerChat.access_hash = chat.access_hash;
            } else {
                tL_inputPeerChat = new TLRPC.TL_inputPeerChat();
                tL_inputPeerChat.chat_id = chat.id;
            }
            tL_inputPeerPhotoFileLocation.photo_id = chat.photo.photo_id;
            inputPeer = tL_inputPeerChat;
        }
        tL_inputPeerPhotoFileLocation.peer = inputPeer;
        inputFileLocationArr[0] = tL_inputPeerPhotoFileLocation;
        return true;
    }

    public static boolean isFileRefError(String str) {
        return "FILEREF_EXPIRED".equals(str) || "FILE_REFERENCE_EXPIRED".equals(str) || "FILE_REFERENCE_EMPTY".equals(str) || (str != null && str.startsWith("FILE_REFERENCE_"));
    }

    public static boolean isFileRefErrorCover(String str) {
        return str != null && isFileRefError(str) && str.endsWith("COVER_EXPIRED");
    }

    private boolean isSameReference(byte[] bArr, byte[] bArr2) {
        return Arrays.equals(bArr, bArr2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$41(TLRPC.User user) {
        getMessagesController().putUser(user, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$42(TLRPC.Chat chat) {
        getMessagesController().putChat(chat, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$43(TLRPC.Chat chat) {
        getMessagesController().putChat(chat, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$44(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        getMediaDataController().replaceStickerSet(tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$29(TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$30(TLRPC.TL_messages_sendMedia tL_messages_sendMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tL_messages_sendMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$31(Requester requester) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$32(Requester requester) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$33(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$34(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$35(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$36(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$0(String str, String str2, StoriesController.BotPreview botPreview) {
        onRequestComplete(str, str2, botPreview, null, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$1(final String str, final String str2, final StoriesController.BotPreview botPreview) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                FileRefController.this.lambda$requestReferenceFromServer$0(str, str2, botPreview);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$10(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$11(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$12(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$13(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$14(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$15(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$16(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$17(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.wallpaperWaiters, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$18(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.savedGifsWaiters, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$19(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.recentStickersWaiter, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$2(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$20(TLObject tLObject, TLRPC.TL_error tL_error) {
        broadcastWaitersData(this.favStickersWaiter, tLObject, tL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$21(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$22(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$23(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$24(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$25(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$26(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$27(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$28(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$3(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC.TL_help_premiumPromo) {
            getMediaDataController().processLoadedPremiumPromo((TLRPC.TL_help_premiumPromo) tLObject, currentTimeMillis, false);
        }
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$4(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$5(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$6(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$7(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$8(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$9(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        onRequestComplete(str, str2, tLObject, tL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$37(TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$38(TLRPC.TL_messages_sendMedia tL_messages_sendMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tL_messages_sendMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$39(Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) objArr[0], (MessageObject) objArr[1], (String) objArr[2], (SendMessagesHelper.DelayedMessage) objArr[3], ((Boolean) objArr[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) objArr[5], null, null, ((Boolean) objArr[6]).booleanValue());
    }

    /* JADX WARN: Code restructure failed: missing block: B:143:0x02d9, code lost:
    
        if (r1 != null) goto L136;
     */
    /* JADX WARN: Removed duplicated region for block: B:312:0x0696 A[LOOP:9: B:303:0x0632->B:312:0x0696, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:313:0x0693 A[EDGE_INSN: B:313:0x0693->B:79:0x0693 BREAK  A[LOOP:9: B:303:0x0632->B:312:0x0696], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x009f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x091a  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0936  */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v20 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean onRequestComplete(String str, String str2, TLObject tLObject, TLRPC.TL_error tL_error, boolean z, boolean z2) {
        String str3;
        String str4;
        boolean z3;
        ArrayList<Requester> arrayList;
        Requester requester;
        int i;
        ArrayList<Requester> arrayList2;
        TLRPC.InputFileLocation[] inputFileLocationArr;
        boolean z4;
        TLRPC.InputFileLocation inputFileLocation;
        TL_stories.StoryItem storyItem;
        TL_stories.StoryItem storyItem2;
        TLRPC.MessageMedia messageMedia;
        TLRPC.MessageMedia messageMedia2;
        TLRPC.Document document;
        TLRPC.Photo photo;
        TLRPC.Photo photo2;
        MediaDataController mediaDataController;
        ArrayList<TLRPC.Document> arrayList3;
        int i2;
        boolean z5;
        int i3;
        boolean z6;
        Runnable runnable;
        byte[] fileReference;
        TLRPC.WebPage webPage;
        byte[] bArr;
        TLRPC.InputFileLocation[] inputFileLocationArr2;
        boolean z7;
        int i4;
        TLRPC.Message message;
        int i5;
        int i6;
        TLRPC.Photo photo3;
        TLRPC.MessageMedia messageMedia3;
        TLRPC.MessageMedia messageMedia4;
        TLRPC.Photo photo4;
        byte[] fileReference2;
        TLRPC.InputFileLocation inputFileLocation2;
        TLRPC.Photo photo5;
        Requester requester2;
        boolean z8;
        TLRPC.InputFileLocation inputFileLocation3;
        ArrayList<Requester> arrayList4;
        int i7;
        int i8;
        ArrayList<Requester> arrayList5;
        TLRPC.TL_error tL_error2 = tL_error;
        int i9 = 1;
        boolean z9 = tLObject instanceof TLRPC.TL_help_premiumPromo;
        if (z9) {
            str4 = "premium_promo";
        } else if (tLObject instanceof TL_account.TL_wallPapers) {
            str4 = "wallpaper";
        } else if (tLObject instanceof TLRPC.TL_messages_savedGifs) {
            str4 = "gif";
        } else if (tLObject instanceof TLRPC.TL_messages_recentStickers) {
            str4 = "recent";
        } else {
            if (!(tLObject instanceof TLRPC.TL_messages_favedStickers)) {
                str3 = str2;
                if (str2 != null || (arrayList4 = this.parentRequester.get(str2)) == null) {
                    z3 = false;
                } else {
                    int size = arrayList4.size();
                    int i10 = 0;
                    z3 = false;
                    while (i10 < size) {
                        Requester requester3 = arrayList4.get(i10);
                        if (requester3.completed) {
                            i7 = i10;
                            i8 = size;
                            arrayList5 = arrayList4;
                        } else {
                            i7 = i10;
                            i8 = size;
                            arrayList5 = arrayList4;
                            if (onRequestComplete(requester3.locationKey, null, tLObject, tL_error, z && !z3, z2)) {
                                z3 = true;
                            }
                        }
                        i10 = i7 + 1;
                        arrayList4 = arrayList5;
                        size = i8;
                    }
                    if (z3) {
                        putReponseToCache(str3, tLObject);
                    }
                    this.parentRequester.remove(str2);
                }
                arrayList = this.locationRequester.get(str);
                if (arrayList != null) {
                    return z3;
                }
                int size2 = arrayList.size();
                boolean[] zArr = null;
                TLRPC.InputFileLocation[] inputFileLocationArr3 = null;
                byte[] bArr2 = null;
                int i11 = 0;
                while (i11 < size2) {
                    Requester requester4 = arrayList.get(i11);
                    if (requester4.completed) {
                        inputFileLocationArr2 = inputFileLocationArr3;
                        i = i11;
                        arrayList2 = arrayList;
                    } else {
                        if (tL_error2 != null && BuildVars.LOGS_ENABLED && requester4.args.length > i9 && (requester4.args[i9] instanceof FileLoadOperation)) {
                            FileLog.e("debug_loading: " + ((FileLoadOperation) requester4.args[i9]).getCacheFileFinal().getName() + " can't update file reference: " + tL_error2.code + " " + tL_error2.text);
                        }
                        if ((requester4.location instanceof TLRPC.TL_inputFileLocation) || (requester4.location instanceof TLRPC.TL_inputPeerPhotoFileLocation)) {
                            inputFileLocationArr3 = new TLRPC.InputFileLocation[i9];
                            zArr = new boolean[i9];
                        }
                        boolean[] zArr2 = zArr;
                        TLRPC.InputFileLocation[] inputFileLocationArr4 = inputFileLocationArr3;
                        requester4.completed = i9;
                        if (tLObject instanceof StoriesController.BotPreview) {
                            TLRPC.MessageMedia messageMedia5 = ((StoriesController.BotPreview) tLObject).media;
                            TLRPC.Document document2 = messageMedia5.document;
                            if (document2 != null) {
                                i = i11;
                                requester = requester4;
                                bArr2 = getFileReference(document2, messageMedia5.alt_documents, requester4.location, zArr2, inputFileLocationArr4);
                                arrayList2 = arrayList;
                                z4 = z9;
                                inputFileLocationArr2 = inputFileLocationArr4;
                                inputFileLocation = null;
                                if (bArr2 == null) {
                                    if (inputFileLocationArr2 != null) {
                                        inputFileLocation3 = inputFileLocationArr2[0];
                                        requester2 = requester;
                                        z8 = z2;
                                    } else {
                                        requester2 = requester;
                                        z8 = z2;
                                        inputFileLocation3 = inputFileLocation;
                                    }
                                    if (onUpdateObjectReference(requester2, bArr2, inputFileLocation3, z8)) {
                                        zArr = zArr2;
                                        z3 = true;
                                    }
                                } else {
                                    sendErrorToObject(requester.args, 1);
                                }
                                zArr = zArr2;
                            } else {
                                requester = requester4;
                                i = i11;
                                TLRPC.Photo photo6 = messageMedia5.photo;
                                if (photo6 != null) {
                                    bArr2 = getFileReference(photo6, requester.location, zArr2, inputFileLocationArr4);
                                    inputFileLocationArr2 = inputFileLocationArr4;
                                    arrayList2 = arrayList;
                                    z4 = z9;
                                    inputFileLocation = null;
                                    if (bArr2 == null) {
                                    }
                                    zArr = zArr2;
                                } else {
                                    arrayList2 = arrayList;
                                    z4 = z9;
                                    inputFileLocationArr = inputFileLocationArr4;
                                    inputFileLocation = null;
                                    inputFileLocationArr2 = inputFileLocationArr;
                                    if (bArr2 == null) {
                                    }
                                    zArr = zArr2;
                                }
                            }
                        } else {
                            TLRPC.InputFileLocation[] inputFileLocationArr5 = inputFileLocationArr4;
                            requester = requester4;
                            i = i11;
                            if (tLObject instanceof TLRPC.messages_Messages) {
                                TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
                                if (messages_messages.messages.isEmpty()) {
                                    arrayList2 = arrayList;
                                    inputFileLocationArr = inputFileLocationArr5;
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.d("empty messages, file ref not found");
                                    }
                                    z4 = z9;
                                    inputFileLocation = null;
                                    inputFileLocationArr2 = inputFileLocationArr;
                                } else {
                                    int size3 = messages_messages.messages.size();
                                    int i12 = 0;
                                    while (true) {
                                        if (i12 >= size3) {
                                            arrayList2 = arrayList;
                                            z7 = true;
                                            i4 = 0;
                                            inputFileLocationArr = inputFileLocationArr5;
                                            break;
                                        }
                                        TLRPC.Message message2 = messages_messages.messages.get(i12);
                                        TLRPC.MessageMedia messageMedia6 = message2.media;
                                        if (messageMedia6 instanceof TLRPC.TL_messageMediaPaidMedia) {
                                            TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia6;
                                            byte[] bArr3 = bArr2;
                                            int i13 = 0;
                                            while (true) {
                                                if (i13 >= tL_messageMediaPaidMedia.extended_media.size()) {
                                                    message = message2;
                                                    i5 = i12;
                                                    i6 = size3;
                                                    arrayList2 = arrayList;
                                                    break;
                                                }
                                                TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i13);
                                                int i14 = i13;
                                                if (!(messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) || (messageMedia3 = ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media) == null) {
                                                    message = message2;
                                                    i5 = i12;
                                                    i6 = size3;
                                                    arrayList2 = arrayList;
                                                } else {
                                                    TLRPC.Document document3 = messageMedia3.document;
                                                    if (document3 != null) {
                                                        arrayList2 = arrayList;
                                                        messageMedia4 = messageMedia3;
                                                        message = message2;
                                                        i5 = i12;
                                                        i6 = size3;
                                                        fileReference2 = getFileReference(document3, messageMedia3.alt_documents, requester.location, zArr2, inputFileLocationArr5);
                                                    } else {
                                                        message = message2;
                                                        i5 = i12;
                                                        i6 = size3;
                                                        TLRPC.InputFileLocation[] inputFileLocationArr6 = inputFileLocationArr5;
                                                        arrayList2 = arrayList;
                                                        messageMedia4 = messageMedia3;
                                                        TLRPC.TL_game tL_game = messageMedia4.game;
                                                        if (tL_game != null) {
                                                            fileReference2 = getFileReference(tL_game.document, null, requester.location, zArr2, inputFileLocationArr6);
                                                            if (fileReference2 == null) {
                                                                photo4 = messageMedia4.game.photo;
                                                                inputFileLocation2 = requester.location;
                                                                inputFileLocationArr5 = inputFileLocationArr6;
                                                                fileReference2 = getFileReference(photo4, inputFileLocation2, zArr2, inputFileLocationArr5);
                                                            } else {
                                                                inputFileLocationArr5 = inputFileLocationArr6;
                                                            }
                                                        } else {
                                                            inputFileLocationArr5 = inputFileLocationArr6;
                                                            photo4 = messageMedia4.photo;
                                                            if (photo4 != null) {
                                                                inputFileLocation2 = requester.location;
                                                                fileReference2 = getFileReference(photo4, inputFileLocation2, zArr2, inputFileLocationArr5);
                                                            } else {
                                                                TLRPC.WebPage webPage2 = messageMedia4.webpage;
                                                                fileReference2 = webPage2 != null ? getFileReference(webPage2, requester.location, zArr2, inputFileLocationArr5) : bArr3;
                                                            }
                                                        }
                                                    }
                                                    bArr3 = (fileReference2 != null || (photo5 = messageMedia4.video_cover) == null) ? fileReference2 : getFileReference(photo5, requester.location, zArr2, inputFileLocationArr5);
                                                }
                                                if (bArr3 != null) {
                                                    break;
                                                }
                                                i13 = i14 + 1;
                                                message2 = message;
                                                i12 = i5;
                                                size3 = i6;
                                                arrayList = arrayList2;
                                            }
                                            inputFileLocationArr = inputFileLocationArr5;
                                            bArr2 = bArr3;
                                        } else {
                                            message = message2;
                                            i5 = i12;
                                            i6 = size3;
                                            arrayList2 = arrayList;
                                            if (messageMedia6 != null) {
                                                TLRPC.Document document4 = messageMedia6.document;
                                                if (document4 != null) {
                                                    inputFileLocationArr = inputFileLocationArr5;
                                                    bArr2 = getFileReference(document4, messageMedia6.alt_documents, requester.location, zArr2, inputFileLocationArr5);
                                                } else {
                                                    inputFileLocationArr = inputFileLocationArr5;
                                                    TLRPC.TL_game tL_game2 = messageMedia6.game;
                                                    if (tL_game2 != null) {
                                                        bArr2 = getFileReference(tL_game2.document, null, requester.location, zArr2, inputFileLocationArr);
                                                        if (bArr2 == null) {
                                                            bArr2 = getFileReference(message.media.game.photo, requester.location, zArr2, inputFileLocationArr);
                                                        }
                                                    } else {
                                                        TLRPC.Photo photo7 = messageMedia6.photo;
                                                        if (photo7 != null) {
                                                            bArr2 = getFileReference(photo7, requester.location, zArr2, inputFileLocationArr);
                                                        } else {
                                                            TLRPC.WebPage webPage3 = messageMedia6.webpage;
                                                            if (webPage3 != null) {
                                                                bArr2 = getFileReference(webPage3, requester.location, zArr2, inputFileLocationArr);
                                                            }
                                                        }
                                                    }
                                                }
                                                if (bArr2 == null) {
                                                    photo3 = message.media.video_cover;
                                                }
                                            } else {
                                                inputFileLocationArr = inputFileLocationArr5;
                                                TLRPC.MessageAction messageAction = message.action;
                                                if ((messageAction instanceof TLRPC.TL_messageActionChatEditPhoto) || (messageAction instanceof TLRPC.TL_messageActionSuggestProfilePhoto)) {
                                                    photo3 = messageAction.photo;
                                                    bArr2 = getFileReference(photo3, requester.location, zArr2, inputFileLocationArr);
                                                }
                                            }
                                            inputFileLocationArr2 = inputFileLocationArr;
                                            z4 = z9;
                                            inputFileLocation = null;
                                        }
                                        if (bArr2 != null) {
                                            if (z) {
                                                i4 = 0;
                                                getMessagesStorage().replaceMessageIfExists(message, messages_messages.users, messages_messages.chats, false);
                                            } else {
                                                i4 = 0;
                                            }
                                            z7 = true;
                                        } else {
                                            i12 = i5 + 1;
                                            inputFileLocationArr5 = inputFileLocationArr;
                                            size3 = i6;
                                            arrayList = arrayList2;
                                        }
                                    }
                                    if (bArr2 == null) {
                                        getMessagesStorage().replaceMessageIfExists(messages_messages.messages.get(i4), messages_messages.users, messages_messages.chats, z7);
                                        if (BuildVars.DEBUG_VERSION) {
                                            FileLog.d("file ref not found in messages, replacing message");
                                        }
                                    }
                                    inputFileLocationArr2 = inputFileLocationArr;
                                    z4 = z9;
                                    inputFileLocation = null;
                                }
                            } else {
                                arrayList2 = arrayList;
                                inputFileLocationArr = inputFileLocationArr5;
                                if (z9) {
                                    Iterator<TLRPC.Document> it = ((TLRPC.TL_help_premiumPromo) tLObject).videos.iterator();
                                    while (it.hasNext() && (bArr2 = getFileReference(it.next(), null, requester.location, zArr2, inputFileLocationArr)) == null) {
                                    }
                                } else if (tLObject instanceof TLRPC.TL_messages_availableReactions) {
                                    TLRPC.TL_messages_availableReactions tL_messages_availableReactions = (TLRPC.TL_messages_availableReactions) tLObject;
                                    getMediaDataController().processLoadedReactions(tL_messages_availableReactions.reactions, tL_messages_availableReactions.hash, (int) (System.currentTimeMillis() / 1000), false);
                                    Iterator<TLRPC.TL_availableReaction> it2 = tL_messages_availableReactions.reactions.iterator();
                                    while (it2.hasNext()) {
                                        TLRPC.TL_availableReaction next = it2.next();
                                        byte[] fileReference3 = getFileReference(next.static_icon, null, requester.location, zArr2, inputFileLocationArr);
                                        if (fileReference3 != null || (fileReference3 = getFileReference(next.appear_animation, null, requester.location, zArr2, inputFileLocationArr)) != null || (fileReference3 = getFileReference(next.select_animation, null, requester.location, zArr2, inputFileLocationArr)) != null || (fileReference3 = getFileReference(next.activate_animation, null, requester.location, zArr2, inputFileLocationArr)) != null || (fileReference3 = getFileReference(next.effect_animation, null, requester.location, zArr2, inputFileLocationArr)) != null || (fileReference3 = getFileReference(next.around_animation, null, requester.location, zArr2, inputFileLocationArr)) != null) {
                                            bArr2 = fileReference3;
                                            break;
                                        }
                                        bArr2 = getFileReference(next.center_icon, null, requester.location, zArr2, inputFileLocationArr);
                                        if (bArr2 != null) {
                                            break;
                                        }
                                    }
                                } else if (tLObject instanceof TLRPC.TL_users_userFull) {
                                    TLRPC.TL_users_userFull tL_users_userFull = (TLRPC.TL_users_userFull) tLObject;
                                    getMessagesController().putUsers(tL_users_userFull.users, false);
                                    getMessagesController().putChats(tL_users_userFull.chats, false);
                                    TLRPC.UserFull userFull = tL_users_userFull.full_user;
                                    TL_bots.BotInfo botInfo = userFull.bot_info;
                                    if (botInfo != null) {
                                        getMessagesStorage().updateUserInfo(userFull, true);
                                        byte[] fileReference4 = getFileReference(botInfo.description_document, null, requester.location, zArr2, inputFileLocationArr);
                                        if (fileReference4 != null) {
                                            bArr2 = fileReference4;
                                            inputFileLocationArr2 = inputFileLocationArr;
                                            zArr = zArr2;
                                        } else {
                                            bArr2 = getFileReference(botInfo.description_photo, requester.location, zArr2, inputFileLocationArr);
                                        }
                                    }
                                    z4 = z9;
                                    inputFileLocation = null;
                                    inputFileLocationArr2 = inputFileLocationArr;
                                } else {
                                    if (tLObject instanceof TLRPC.TL_attachMenuBotsBot) {
                                        TLRPC.TL_attachMenuBot tL_attachMenuBot = ((TLRPC.TL_attachMenuBotsBot) tLObject).bot;
                                        Iterator<TLRPC.TL_attachMenuBotIcon> it3 = tL_attachMenuBot.icons.iterator();
                                        while (it3.hasNext() && (bArr2 = getFileReference(it3.next().icon, null, requester.location, zArr2, inputFileLocationArr)) == null) {
                                        }
                                        if (z) {
                                            TLRPC.TL_attachMenuBots attachMenuBots = getMediaDataController().getAttachMenuBots();
                                            ArrayList<TLRPC.TL_attachMenuBot> arrayList6 = new ArrayList<>(attachMenuBots.bots);
                                            int i15 = 0;
                                            while (true) {
                                                if (i15 >= arrayList6.size()) {
                                                    z4 = z9;
                                                    break;
                                                }
                                                z4 = z9;
                                                if (arrayList6.get(i15).bot_id == tL_attachMenuBot.bot_id) {
                                                    arrayList6.set(i15, tL_attachMenuBot);
                                                    break;
                                                }
                                                i15++;
                                                z9 = z4;
                                            }
                                            attachMenuBots.bots = arrayList6;
                                            getMediaDataController().processLoadedMenuBots(attachMenuBots, attachMenuBots.hash, (int) (System.currentTimeMillis() / 1000), false);
                                        } else {
                                            z4 = z9;
                                        }
                                    } else {
                                        z4 = z9;
                                        if (tLObject instanceof TLRPC.TL_help_appUpdate) {
                                            TLRPC.TL_help_appUpdate tL_help_appUpdate = (TLRPC.TL_help_appUpdate) tLObject;
                                            try {
                                                SharedConfig.pendingAppUpdate = tL_help_appUpdate;
                                                SharedConfig.saveConfig();
                                            } catch (Exception e) {
                                                FileLog.e(e);
                                            }
                                            try {
                                                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.appUpdateAvailable, new Object[0]);
                                            } catch (Exception e2) {
                                                FileLog.e(e2);
                                            }
                                            try {
                                                TLRPC.Document document5 = tL_help_appUpdate.document;
                                                if (document5 != null) {
                                                    bArr = document5.file_reference;
                                                    TLRPC.TL_inputDocumentFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                                    TLRPC.Document document6 = tL_help_appUpdate.document;
                                                    tL_inputDocumentFileLocation.id = document6.id;
                                                    tL_inputDocumentFileLocation.access_hash = document6.access_hash;
                                                    tL_inputDocumentFileLocation.file_reference = document6.file_reference;
                                                    tL_inputDocumentFileLocation.thumb_size = "";
                                                    inputFileLocationArr = new TLRPC.InputFileLocation[]{tL_inputDocumentFileLocation};
                                                } else {
                                                    bArr = bArr2;
                                                }
                                            } catch (Exception e3) {
                                                FileLog.e(e3);
                                                bArr = null;
                                            }
                                            bArr2 = bArr == null ? getFileReference(tL_help_appUpdate.document, null, requester.location, zArr2, inputFileLocationArr) : bArr;
                                            if (bArr2 == null) {
                                                bArr2 = getFileReference(tL_help_appUpdate.sticker, null, requester.location, zArr2, inputFileLocationArr);
                                            }
                                        } else {
                                            if (tLObject instanceof TLRPC.TL_messages_webPage) {
                                                TLRPC.TL_messages_webPage tL_messages_webPage = (TLRPC.TL_messages_webPage) tLObject;
                                                getMessagesController().putChats(tL_messages_webPage.chats, false);
                                                getMessagesController().putUsers(tL_messages_webPage.users, false);
                                                webPage = tL_messages_webPage.webpage;
                                            } else if (tLObject instanceof TLRPC.WebPage) {
                                                webPage = (TLRPC.WebPage) tLObject;
                                            } else if (tLObject instanceof TL_account.TL_wallPapers) {
                                                TL_account.TL_wallPapers tL_wallPapers = (TL_account.TL_wallPapers) tLObject;
                                                int size4 = tL_wallPapers.wallpapers.size();
                                                for (int i16 = 0; i16 < size4; i16++) {
                                                    bArr2 = getFileReference(tL_wallPapers.wallpapers.get(i16).document, null, requester.location, zArr2, inputFileLocationArr);
                                                    if (bArr2 != null) {
                                                        break;
                                                    }
                                                }
                                                if (bArr2 != null && z) {
                                                    getMessagesStorage().putWallpapers(tL_wallPapers.wallpapers, 1);
                                                }
                                            } else {
                                                if (tLObject instanceof TLRPC.TL_wallPaper) {
                                                    TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) tLObject;
                                                    fileReference = getFileReference(tL_wallPaper.document, null, requester.location, zArr2, inputFileLocationArr);
                                                    if (fileReference != null && z) {
                                                        ArrayList<TLRPC.WallPaper> arrayList7 = new ArrayList<>();
                                                        arrayList7.add(tL_wallPaper);
                                                        getMessagesStorage().putWallpapers(arrayList7, 0);
                                                    }
                                                } else if (tLObject instanceof TLRPC.TL_theme) {
                                                    final TLRPC.TL_theme tL_theme = (TLRPC.TL_theme) tLObject;
                                                    fileReference = getFileReference(tL_theme.document, null, requester.location, zArr2, inputFileLocationArr);
                                                    if (fileReference != null && z) {
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda0
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                Theme.setThemeFileReference(TLRPC.TL_theme.this);
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    if (tLObject instanceof Vector) {
                                                        Vector vector = (Vector) tLObject;
                                                        if (!vector.objects.isEmpty()) {
                                                            int size5 = vector.objects.size();
                                                            for (int i17 = 0; i17 < size5; i17++) {
                                                                Object obj = vector.objects.get(i17);
                                                                if (obj instanceof TLRPC.User) {
                                                                    final TLRPC.User user = (TLRPC.User) obj;
                                                                    bArr2 = getFileReference(user, requester.location, zArr2, inputFileLocationArr);
                                                                    if (!z || bArr2 == null) {
                                                                        inputFileLocation = null;
                                                                        if (bArr2 != null) {
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        ArrayList arrayList8 = new ArrayList();
                                                                        arrayList8.add(user);
                                                                        inputFileLocation = null;
                                                                        getMessagesStorage().putUsersAndChats(arrayList8, null, true, true);
                                                                        runnable = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda1
                                                                            @Override // java.lang.Runnable
                                                                            public final void run() {
                                                                                FileRefController.this.lambda$onRequestComplete$41(user);
                                                                            }
                                                                        };
                                                                        AndroidUtilities.runOnUIThread(runnable);
                                                                        if (bArr2 != null) {
                                                                        }
                                                                    }
                                                                } else {
                                                                    inputFileLocation = null;
                                                                    if (obj instanceof TLRPC.Chat) {
                                                                        final TLRPC.Chat chat = (TLRPC.Chat) obj;
                                                                        bArr2 = getFileReference(chat, requester.location, zArr2, inputFileLocationArr);
                                                                        if (z && bArr2 != null) {
                                                                            ArrayList arrayList9 = new ArrayList();
                                                                            arrayList9.add(chat);
                                                                            getMessagesStorage().putUsersAndChats(null, arrayList9, true, true);
                                                                            runnable = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda2
                                                                                @Override // java.lang.Runnable
                                                                                public final void run() {
                                                                                    FileRefController.this.lambda$onRequestComplete$42(chat);
                                                                                }
                                                                            };
                                                                            AndroidUtilities.runOnUIThread(runnable);
                                                                        }
                                                                    }
                                                                    if (bArr2 != null) {
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        inputFileLocation = null;
                                                    } else {
                                                        inputFileLocation = null;
                                                        if (tLObject instanceof TLRPC.TL_messages_chats) {
                                                            TLRPC.TL_messages_chats tL_messages_chats = (TLRPC.TL_messages_chats) tLObject;
                                                            if (!tL_messages_chats.chats.isEmpty()) {
                                                                int size6 = tL_messages_chats.chats.size();
                                                                int i18 = 0;
                                                                while (true) {
                                                                    if (i18 >= size6) {
                                                                        break;
                                                                    }
                                                                    final TLRPC.Chat chat2 = tL_messages_chats.chats.get(i18);
                                                                    byte[] fileReference5 = getFileReference(chat2, requester.location, zArr2, inputFileLocationArr);
                                                                    if (fileReference5 != null) {
                                                                        if (z) {
                                                                            ArrayList arrayList10 = new ArrayList();
                                                                            arrayList10.add(chat2);
                                                                            getMessagesStorage().putUsersAndChats(null, arrayList10, true, true);
                                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda3
                                                                                @Override // java.lang.Runnable
                                                                                public final void run() {
                                                                                    FileRefController.this.lambda$onRequestComplete$43(chat2);
                                                                                }
                                                                            });
                                                                        }
                                                                        bArr2 = fileReference5;
                                                                    } else {
                                                                        i18++;
                                                                        bArr2 = fileReference5;
                                                                    }
                                                                }
                                                            }
                                                        } else if (tLObject instanceof TLRPC.TL_messages_savedGifs) {
                                                            TLRPC.TL_messages_savedGifs tL_messages_savedGifs = (TLRPC.TL_messages_savedGifs) tLObject;
                                                            int size7 = tL_messages_savedGifs.gifs.size();
                                                            int i19 = 0;
                                                            while (i19 < size7) {
                                                                int i20 = i19;
                                                                bArr2 = getFileReference(tL_messages_savedGifs.gifs.get(i19), null, requester.location, zArr2, inputFileLocationArr);
                                                                if (bArr2 != null) {
                                                                    break;
                                                                }
                                                                i19 = i20 + 1;
                                                            }
                                                            if (z) {
                                                                mediaDataController = getMediaDataController();
                                                                arrayList3 = tL_messages_savedGifs.gifs;
                                                                i2 = 0;
                                                                z5 = true;
                                                                i3 = 0;
                                                                z6 = true;
                                                                mediaDataController.processLoadedRecentDocuments(i3, arrayList3, z6, i2, z5);
                                                            }
                                                        } else if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
                                                            final TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
                                                            if (bArr2 == null) {
                                                                int size8 = tL_messages_stickerSet.documents.size();
                                                                int i21 = 0;
                                                                while (i21 < size8) {
                                                                    int i22 = i21;
                                                                    bArr2 = getFileReference(tL_messages_stickerSet.documents.get(i21), null, requester.location, zArr2, inputFileLocationArr);
                                                                    if (bArr2 != null) {
                                                                        break;
                                                                    }
                                                                    i21 = i22 + 1;
                                                                }
                                                            }
                                                            if (z) {
                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda4
                                                                    @Override // java.lang.Runnable
                                                                    public final void run() {
                                                                        FileRefController.this.lambda$onRequestComplete$44(tL_messages_stickerSet);
                                                                    }
                                                                });
                                                            }
                                                        } else if (tLObject instanceof TLRPC.TL_messages_recentStickers) {
                                                            TLRPC.TL_messages_recentStickers tL_messages_recentStickers = (TLRPC.TL_messages_recentStickers) tLObject;
                                                            int size9 = tL_messages_recentStickers.stickers.size();
                                                            int i23 = 0;
                                                            while (i23 < size9) {
                                                                int i24 = i23;
                                                                bArr2 = getFileReference(tL_messages_recentStickers.stickers.get(i23), null, requester.location, zArr2, inputFileLocationArr);
                                                                if (bArr2 != null) {
                                                                    break;
                                                                }
                                                                i23 = i24 + 1;
                                                            }
                                                            if (z) {
                                                                mediaDataController = getMediaDataController();
                                                                arrayList3 = tL_messages_recentStickers.stickers;
                                                                i2 = 0;
                                                                z5 = true;
                                                                i3 = 0;
                                                                z6 = false;
                                                                mediaDataController.processLoadedRecentDocuments(i3, arrayList3, z6, i2, z5);
                                                            }
                                                        } else if (tLObject instanceof TLRPC.TL_messages_favedStickers) {
                                                            TLRPC.TL_messages_favedStickers tL_messages_favedStickers = (TLRPC.TL_messages_favedStickers) tLObject;
                                                            int size10 = tL_messages_favedStickers.stickers.size();
                                                            int i25 = 0;
                                                            while (i25 < size10) {
                                                                int i26 = i25;
                                                                bArr2 = getFileReference(tL_messages_favedStickers.stickers.get(i25), null, requester.location, zArr2, inputFileLocationArr);
                                                                if (bArr2 != null) {
                                                                    break;
                                                                }
                                                                i25 = i26 + 1;
                                                            }
                                                            if (z) {
                                                                mediaDataController = getMediaDataController();
                                                                arrayList3 = tL_messages_favedStickers.stickers;
                                                                i2 = 0;
                                                                z5 = true;
                                                                i3 = 2;
                                                                z6 = false;
                                                                mediaDataController.processLoadedRecentDocuments(i3, arrayList3, z6, i2, z5);
                                                            }
                                                        } else if (tLObject instanceof TLRPC.photos_Photos) {
                                                            TLRPC.photos_Photos photos_photos = (TLRPC.photos_Photos) tLObject;
                                                            int size11 = photos_photos.photos.size();
                                                            for (int i27 = 0; i27 < size11; i27++) {
                                                                bArr2 = getFileReference(photos_photos.photos.get(i27), requester.location, zArr2, inputFileLocationArr);
                                                                if (bArr2 != null) {
                                                                    break;
                                                                }
                                                            }
                                                        } else if (tLObject instanceof TL_stories.TL_stories_stories) {
                                                            TL_stories.TL_stories_stories tL_stories_stories = (TL_stories.TL_stories_stories) tLObject;
                                                            if (tL_stories_stories.stories.isEmpty() || (messageMedia = (storyItem2 = tL_stories_stories.stories.get(0)).media) == null) {
                                                                storyItem = null;
                                                            } else {
                                                                if (bArr2 == null && (photo2 = messageMedia.photo) != null) {
                                                                    bArr2 = getFileReference(photo2, requester.location, zArr2, inputFileLocationArr);
                                                                }
                                                                if (bArr2 == null && (photo = storyItem2.media.video_cover) != null) {
                                                                    bArr2 = getFileReference(photo, requester.location, zArr2, inputFileLocationArr);
                                                                }
                                                                if (bArr2 == null && (document = (messageMedia2 = storyItem2.media).document) != null) {
                                                                    bArr2 = getFileReference(document, messageMedia2.alt_documents, requester.location, zArr2, inputFileLocationArr);
                                                                }
                                                                storyItem = storyItem2;
                                                            }
                                                            if (requester.args[1] instanceof FileLoadOperation) {
                                                                Object obj2 = ((FileLoadOperation) requester.args[1]).parentObject;
                                                                if (obj2 instanceof TL_stories.StoryItem) {
                                                                    TL_stories.StoryItem storyItem3 = (TL_stories.StoryItem) obj2;
                                                                    if (storyItem == null) {
                                                                        TL_stories.TL_updateStory tL_updateStory = new TL_stories.TL_updateStory();
                                                                        tL_updateStory.peer = getMessagesController().getPeer(storyItem3.dialogId);
                                                                        TL_stories.TL_storyItemDeleted tL_storyItemDeleted = new TL_stories.TL_storyItemDeleted();
                                                                        tL_updateStory.story = tL_storyItemDeleted;
                                                                        tL_storyItemDeleted.id = storyItem3.id;
                                                                        ArrayList<TLRPC.Update> arrayList11 = new ArrayList<>();
                                                                        arrayList11.add(tL_updateStory);
                                                                        getMessagesController().processUpdateArray(arrayList11, null, null, false, 0);
                                                                    } else {
                                                                        TLRPC.User user2 = getMessagesController().getUser(Long.valueOf(storyItem3.dialogId));
                                                                        if (user2 != null && user2.contact) {
                                                                            MessagesController.getInstance(this.currentAccount).getStoriesController().getStoriesStorage().updateStoryItem(storyItem3.dialogId, storyItem);
                                                                        }
                                                                    }
                                                                    if (storyItem != null && bArr2 == null) {
                                                                        TL_stories.TL_updateStory tL_updateStory2 = new TL_stories.TL_updateStory();
                                                                        tL_updateStory2.peer = MessagesController.getInstance(this.currentAccount).getPeer(storyItem3.dialogId);
                                                                        tL_updateStory2.story = storyItem;
                                                                        ArrayList<TLRPC.Update> arrayList12 = new ArrayList<>();
                                                                        arrayList12.add(tL_updateStory2);
                                                                        MessagesController.getInstance(this.currentAccount).processUpdateArray(arrayList12, null, null, false, 0);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    inputFileLocationArr2 = inputFileLocationArr;
                                                }
                                                bArr2 = fileReference;
                                            }
                                            bArr2 = getFileReference(webPage, requester.location, zArr2, inputFileLocationArr);
                                        }
                                    }
                                    inputFileLocationArr2 = inputFileLocationArr;
                                    inputFileLocation = null;
                                }
                                inputFileLocationArr2 = inputFileLocationArr;
                                z4 = z9;
                                inputFileLocation = null;
                            }
                            if (bArr2 == null) {
                            }
                            zArr = zArr2;
                        }
                        inputFileLocationArr3 = inputFileLocationArr2;
                        i11 = i + 1;
                        z9 = z4;
                        arrayList = arrayList2;
                        i9 = 1;
                        tL_error2 = tL_error;
                    }
                    z4 = z9;
                    inputFileLocationArr3 = inputFileLocationArr2;
                    i11 = i + 1;
                    z9 = z4;
                    arrayList = arrayList2;
                    i9 = 1;
                    tL_error2 = tL_error;
                }
                this.locationRequester.remove(str);
                if (z3) {
                    putReponseToCache(str, tLObject);
                }
                return z3;
            }
            str4 = "fav";
        }
        str3 = str4;
        if (str2 != null) {
        }
        z3 = false;
        arrayList = this.locationRequester.get(str);
        if (arrayList != null) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean onUpdateObjectReference(final Requester requester, byte[] bArr, TLRPC.InputFileLocation inputFileLocation, boolean z) {
        String str;
        RequestDelegate requestDelegate;
        ConnectionsManager connectionsManager;
        TLRPC.TL_messages_faveSticker tL_messages_faveSticker;
        Runnable runnable;
        TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto;
        Runnable runnable2;
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("fileref updated for " + requester.args[0] + " " + requester.locationKey);
        }
        if (requester.args[0] instanceof TL_stories.TL_storyItem) {
            ((TL_stories.TL_storyItem) requester.args[0]).media.document.file_reference = bArr;
            return true;
        }
        if (requester.args[0] instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) requester.args[1];
            final Object[] objArr = this.multiMediaCache.get(tL_messages_sendMultiMedia);
            if (objArr == null) {
                return true;
            }
            TLRPC.TL_inputSingleMedia tL_inputSingleMedia = (TLRPC.TL_inputSingleMedia) requester.args[0];
            TLRPC.InputMedia inputMedia = tL_inputSingleMedia.media;
            if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia;
                if (z && isSameReference(tL_inputMediaDocument.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument.id.file_reference = bArr;
            } else if (inputMedia instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto2 = (TLRPC.TL_inputMediaPhoto) inputMedia;
                if (z && isSameReference(tL_inputMediaPhoto2.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto2.id.file_reference = bArr;
            }
            int indexOf = tL_messages_sendMultiMedia.multi_media.indexOf(tL_inputSingleMedia);
            if (indexOf < 0) {
                return true;
            }
            ArrayList arrayList = (ArrayList) objArr[3];
            arrayList.set(indexOf, null);
            boolean z2 = true;
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) != null) {
                    z2 = false;
                }
            }
            if (z2) {
                this.multiMediaCache.remove(tL_messages_sendMultiMedia);
                runnable2 = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$onUpdateObjectReference$29(tL_messages_sendMultiMedia, objArr);
                    }
                };
                AndroidUtilities.runOnUIThread(runnable2);
            }
            return true;
        }
        if (requester.args.length >= 2 && (requester.args[1] instanceof TLRPC.TL_messages_sendMedia) && (((TLRPC.TL_messages_sendMedia) requester.args[1]).media instanceof TLRPC.TL_inputMediaPaidMedia) && ((requester.args[0] instanceof TLRPC.TL_inputMediaPhoto) || (requester.args[0] instanceof TLRPC.TL_inputMediaDocument))) {
            final TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) requester.args[1];
            final Object[] objArr2 = this.multiMediaCache.get(tL_messages_sendMedia);
            if (objArr2 == null) {
                return true;
            }
            if (requester.args[0] instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument) requester.args[0];
                if (z && isSameReference(tL_inputMediaDocument2.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaDocument2.id.file_reference = bArr;
                tL_inputMediaPhoto = tL_inputMediaDocument2;
            } else if (requester.args[0] instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto3 = (TLRPC.TL_inputMediaPhoto) requester.args[0];
                if (z && isSameReference(tL_inputMediaPhoto3.id.file_reference, bArr)) {
                    return false;
                }
                tL_inputMediaPhoto3.id.file_reference = bArr;
                tL_inputMediaPhoto = tL_inputMediaPhoto3;
            } else {
                tL_inputMediaPhoto = null;
            }
            int indexOf2 = ((TLRPC.TL_inputMediaPaidMedia) tL_messages_sendMedia.media).extended_media.indexOf(tL_inputMediaPhoto);
            if (indexOf2 < 0) {
                return true;
            }
            ArrayList arrayList2 = (ArrayList) objArr2[3];
            arrayList2.set(indexOf2, null);
            boolean z3 = true;
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                if (arrayList2.get(i2) != null) {
                    z3 = false;
                }
            }
            if (z3) {
                this.multiMediaCache.remove(tL_messages_sendMedia);
                runnable2 = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$onUpdateObjectReference$30(tL_messages_sendMedia, objArr2);
                    }
                };
                AndroidUtilities.runOnUIThread(runnable2);
            }
        } else {
            if (requester.args[0] instanceof TLRPC.TL_messages_sendMedia) {
                TLRPC.InputMedia inputMedia2 = ((TLRPC.TL_messages_sendMedia) requester.args[0]).media;
                if (inputMedia2 instanceof TLRPC.TL_inputMediaDocument) {
                    TLRPC.TL_inputMediaDocument tL_inputMediaDocument3 = (TLRPC.TL_inputMediaDocument) inputMedia2;
                    if (z && isSameReference(tL_inputMediaDocument3.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_inputMediaDocument3.id.file_reference = bArr;
                } else if (inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) {
                    TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto4 = (TLRPC.TL_inputMediaPhoto) inputMedia2;
                    if (z && isSameReference(tL_inputMediaPhoto4.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_inputMediaPhoto4.id.file_reference = bArr;
                }
                runnable = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$onUpdateObjectReference$31(requester);
                    }
                };
            } else if (requester.args[0] instanceof TLRPC.TL_messages_editMessage) {
                TLRPC.InputMedia inputMedia3 = ((TLRPC.TL_messages_editMessage) requester.args[0]).media;
                if (inputMedia3 instanceof TLRPC.TL_inputMediaDocument) {
                    TLRPC.TL_inputMediaDocument tL_inputMediaDocument4 = (TLRPC.TL_inputMediaDocument) inputMedia3;
                    if (z && isSameReference(tL_inputMediaDocument4.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_inputMediaDocument4.id.file_reference = bArr;
                } else if (inputMedia3 instanceof TLRPC.TL_inputMediaPhoto) {
                    TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto5 = (TLRPC.TL_inputMediaPhoto) inputMedia3;
                    if (z && isSameReference(tL_inputMediaPhoto5.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_inputMediaPhoto5.id.file_reference = bArr;
                }
                runnable = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$onUpdateObjectReference$32(requester);
                    }
                };
            } else {
                if (requester.args[0] instanceof TLRPC.TL_messages_saveGif) {
                    TLRPC.TL_messages_saveGif tL_messages_saveGif = (TLRPC.TL_messages_saveGif) requester.args[0];
                    if (z && isSameReference(tL_messages_saveGif.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_messages_saveGif.id.file_reference = bArr;
                    ConnectionsManager connectionsManager2 = getConnectionsManager();
                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda12
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.lambda$onUpdateObjectReference$33(tLObject, tL_error);
                        }
                    };
                    tL_messages_faveSticker = tL_messages_saveGif;
                    connectionsManager = connectionsManager2;
                } else if (requester.args[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
                    TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker) requester.args[0];
                    if (z && isSameReference(tL_messages_saveRecentSticker.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_messages_saveRecentSticker.id.file_reference = bArr;
                    ConnectionsManager connectionsManager3 = getConnectionsManager();
                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda13
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.lambda$onUpdateObjectReference$34(tLObject, tL_error);
                        }
                    };
                    tL_messages_faveSticker = tL_messages_saveRecentSticker;
                    connectionsManager = connectionsManager3;
                } else if (requester.args[0] instanceof TLRPC.TL_stickers_addStickerToSet) {
                    TLRPC.TL_stickers_addStickerToSet tL_stickers_addStickerToSet = (TLRPC.TL_stickers_addStickerToSet) requester.args[0];
                    if (z && isSameReference(tL_stickers_addStickerToSet.sticker.document.file_reference, bArr)) {
                        return false;
                    }
                    tL_stickers_addStickerToSet.sticker.document.file_reference = bArr;
                    ConnectionsManager connectionsManager4 = getConnectionsManager();
                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda14
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.lambda$onUpdateObjectReference$35(tLObject, tL_error);
                        }
                    };
                    tL_messages_faveSticker = tL_stickers_addStickerToSet;
                    connectionsManager = connectionsManager4;
                } else if (requester.args[0] instanceof TLRPC.TL_messages_faveSticker) {
                    TLRPC.TL_messages_faveSticker tL_messages_faveSticker2 = (TLRPC.TL_messages_faveSticker) requester.args[0];
                    if (z && isSameReference(tL_messages_faveSticker2.id.file_reference, bArr)) {
                        return false;
                    }
                    tL_messages_faveSticker2.id.file_reference = bArr;
                    ConnectionsManager connectionsManager5 = getConnectionsManager();
                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda15
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.lambda$onUpdateObjectReference$36(tLObject, tL_error);
                        }
                    };
                    tL_messages_faveSticker = tL_messages_faveSticker2;
                    connectionsManager = connectionsManager5;
                } else if (requester.args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
                    TLRPC.TL_messages_getAttachedStickers tL_messages_getAttachedStickers = (TLRPC.TL_messages_getAttachedStickers) requester.args[0];
                    TLRPC.InputStickeredMedia inputStickeredMedia = tL_messages_getAttachedStickers.media;
                    if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaDocument) {
                        TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument = (TLRPC.TL_inputStickeredMediaDocument) inputStickeredMedia;
                        if (z && isSameReference(tL_inputStickeredMediaDocument.id.file_reference, bArr)) {
                            return false;
                        }
                        tL_inputStickeredMediaDocument.id.file_reference = bArr;
                    } else if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                        TLRPC.TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = (TLRPC.TL_inputStickeredMediaPhoto) inputStickeredMedia;
                        if (z && isSameReference(tL_inputStickeredMediaPhoto.id.file_reference, bArr)) {
                            return false;
                        }
                        tL_inputStickeredMediaPhoto.id.file_reference = bArr;
                    }
                    getConnectionsManager().sendRequest(tL_messages_getAttachedStickers, (RequestDelegate) requester.args[1]);
                } else if (requester.args[1] instanceof FileLoadOperation) {
                    FileLoadOperation fileLoadOperation = (FileLoadOperation) requester.args[1];
                    if (inputFileLocation != null) {
                        if (z && isSameReference(fileLoadOperation.location.file_reference, inputFileLocation.file_reference)) {
                            return false;
                        }
                        str = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                        fileLoadOperation.location = inputFileLocation;
                        if (BuildVars.LOGS_ENABLED) {
                            r5 = Utilities.bytesToHex(inputFileLocation.file_reference);
                        }
                    } else {
                        if (z && isSameReference(requester.location.file_reference, bArr)) {
                            return false;
                        }
                        String bytesToHex = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                        TLRPC.InputFileLocation inputFileLocation2 = fileLoadOperation.location;
                        requester.location.file_reference = bArr;
                        inputFileLocation2.file_reference = bArr;
                        r5 = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                        str = bytesToHex;
                    }
                    fileLoadOperation.requestingReference = false;
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("debug_loading: " + fileLoadOperation.getCacheFileFinal().getName() + " " + str + " " + r5 + " reference updated resume download");
                    }
                    fileLoadOperation.startDownloadRequest(-1);
                }
                connectionsManager.sendRequest(tL_messages_faveSticker, requestDelegate);
            }
            AndroidUtilities.runOnUIThread(runnable);
        }
        return true;
    }

    private void putReponseToCache(String str, TLObject tLObject) {
        if (this.responseCache.get(str) == null) {
            CachedResult cachedResult = new CachedResult();
            cachedResult.response = tLObject;
            cachedResult.firstQueryTime = System.currentTimeMillis();
            this.responseCache.put(str, cachedResult);
        }
    }

    private void requestReferenceFromServer(Object obj, final String str, final String str2, Object[] objArr) {
        RequestDelegate requestDelegate;
        TLObject tLObject;
        ArrayList<Waiter> arrayList;
        Waiter waiter;
        ConnectionsManager connectionsManager;
        if (obj instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj;
            StoriesController.BotPreviewsList botPreviewsList = botPreview.list;
            if (botPreviewsList == null) {
                sendErrorToObject(objArr, 0);
                return;
            } else {
                botPreviewsList.requestReference(botPreview, new Utilities.Callback() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda16
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj2) {
                        FileRefController.this.lambda$requestReferenceFromServer$1(str, str2, (StoriesController.BotPreview) obj2);
                    }
                });
                return;
            }
        }
        if (obj instanceof TL_stories.StoryItem) {
            TL_stories.StoryItem storyItem = (TL_stories.StoryItem) obj;
            TL_stories.TL_stories_getStoriesByID tL_stories_getStoriesByID = new TL_stories.TL_stories_getStoriesByID();
            tL_stories_getStoriesByID.peer = getMessagesController().getInputPeer(storyItem.dialogId);
            tL_stories_getStoriesByID.id.add(Integer.valueOf(storyItem.id));
            ConnectionsManager connectionsManager2 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda27
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$2(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager2;
            tLObject = tL_stories_getStoriesByID;
        } else if (obj instanceof TLRPC.TL_help_premiumPromo) {
            TLRPC.TL_help_getPremiumPromo tL_help_getPremiumPromo = new TLRPC.TL_help_getPremiumPromo();
            ConnectionsManager connectionsManager3 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda36
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$3(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager3;
            tLObject = tL_help_getPremiumPromo;
        } else if (obj instanceof TLRPC.TL_availableReaction) {
            TLRPC.TL_messages_getAvailableReactions tL_messages_getAvailableReactions = new TLRPC.TL_messages_getAvailableReactions();
            tL_messages_getAvailableReactions.hash = 0;
            ConnectionsManager connectionsManager4 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda37
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$4(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager4;
            tLObject = tL_messages_getAvailableReactions;
        } else if (obj instanceof TL_bots.BotInfo) {
            TLRPC.TL_users_getFullUser tL_users_getFullUser = new TLRPC.TL_users_getFullUser();
            tL_users_getFullUser.id = getMessagesController().getInputUser(((TL_bots.BotInfo) obj).user_id);
            ConnectionsManager connectionsManager5 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda38
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$5(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager5;
            tLObject = tL_users_getFullUser;
        } else if (obj instanceof TLRPC.TL_attachMenuBot) {
            TLRPC.TL_messages_getAttachMenuBot tL_messages_getAttachMenuBot = new TLRPC.TL_messages_getAttachMenuBot();
            tL_messages_getAttachMenuBot.bot = getMessagesController().getInputUser(((TLRPC.TL_attachMenuBot) obj).bot_id);
            ConnectionsManager connectionsManager6 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda39
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$6(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager6;
            tLObject = tL_messages_getAttachMenuBot;
        } else if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            long channelId = messageObject.getChannelId();
            if (messageObject.scheduled) {
                TLRPC.TL_messages_getScheduledMessages tL_messages_getScheduledMessages = new TLRPC.TL_messages_getScheduledMessages();
                tL_messages_getScheduledMessages.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                tL_messages_getScheduledMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                ConnectionsManager connectionsManager7 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda40
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$7(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager7;
                tLObject = tL_messages_getScheduledMessages;
            } else if (messageObject.isQuickReply()) {
                TLRPC.TL_messages_getQuickReplyMessages tL_messages_getQuickReplyMessages = new TLRPC.TL_messages_getQuickReplyMessages();
                tL_messages_getQuickReplyMessages.shortcut_id = messageObject.getQuickReplyId();
                tL_messages_getQuickReplyMessages.flags |= 1;
                tL_messages_getQuickReplyMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                ConnectionsManager connectionsManager8 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda41
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$8(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager8;
                tLObject = tL_messages_getQuickReplyMessages;
            } else if (channelId != 0) {
                TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
                tL_channels_getMessages.channel = getMessagesController().getInputChannel(channelId);
                tL_channels_getMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                ConnectionsManager connectionsManager9 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda42
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$9(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager9;
                tLObject = tL_channels_getMessages;
            } else {
                TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
                tL_messages_getMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                ConnectionsManager connectionsManager10 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda43
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$10(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager10;
                tLObject = tL_messages_getMessages;
            }
        } else if (obj instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj;
            TL_account.getWallPaper getwallpaper = new TL_account.getWallPaper();
            TLRPC.TL_inputWallPaper tL_inputWallPaper = new TLRPC.TL_inputWallPaper();
            tL_inputWallPaper.id = tL_wallPaper.id;
            tL_inputWallPaper.access_hash = tL_wallPaper.access_hash;
            getwallpaper.wallpaper = tL_inputWallPaper;
            ConnectionsManager connectionsManager11 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda17
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$11(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager11;
            tLObject = getwallpaper;
        } else if (obj instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme tL_theme = (TLRPC.TL_theme) obj;
            TL_account.getTheme gettheme = new TL_account.getTheme();
            TLRPC.TL_inputTheme tL_inputTheme = new TLRPC.TL_inputTheme();
            tL_inputTheme.id = tL_theme.id;
            tL_inputTheme.access_hash = tL_theme.access_hash;
            gettheme.theme = tL_inputTheme;
            gettheme.format = "android";
            ConnectionsManager connectionsManager12 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda18
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$12(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager12;
            tLObject = gettheme;
        } else if (obj instanceof TLRPC.WebPage) {
            TLRPC.TL_messages_getWebPage tL_messages_getWebPage = new TLRPC.TL_messages_getWebPage();
            tL_messages_getWebPage.url = ((TLRPC.WebPage) obj).url;
            tL_messages_getWebPage.hash = 0;
            ConnectionsManager connectionsManager13 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda19
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$13(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager13;
            tLObject = tL_messages_getWebPage;
        } else if (obj instanceof TLRPC.User) {
            TLRPC.TL_users_getUsers tL_users_getUsers = new TLRPC.TL_users_getUsers();
            tL_users_getUsers.id.add(getMessagesController().getInputUser((TLRPC.User) obj));
            ConnectionsManager connectionsManager14 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda20
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$14(str, str2, tLObject2, tL_error);
                }
            };
            connectionsManager = connectionsManager14;
            tLObject = tL_users_getUsers;
        } else if (obj instanceof TLRPC.Chat) {
            TLRPC.Chat chat = (TLRPC.Chat) obj;
            if (chat instanceof TLRPC.TL_chat) {
                TLRPC.TL_messages_getChats tL_messages_getChats = new TLRPC.TL_messages_getChats();
                tL_messages_getChats.id.add(Long.valueOf(chat.id));
                ConnectionsManager connectionsManager15 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda21
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$15(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager15;
                tLObject = tL_messages_getChats;
            } else {
                if (!(chat instanceof TLRPC.TL_channel)) {
                    return;
                }
                TLRPC.TL_channels_getChannels tL_channels_getChannels = new TLRPC.TL_channels_getChannels();
                tL_channels_getChannels.id.add(MessagesController.getInputChannel(chat));
                ConnectionsManager connectionsManager16 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda22
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$16(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager16;
                tLObject = tL_channels_getChannels;
            }
        } else {
            if (obj instanceof String) {
                String str3 = (String) obj;
                if ("wallpaper".equals(str3)) {
                    if (this.wallpaperWaiters.isEmpty()) {
                        getConnectionsManager().sendRequest(new TL_account.getWallPapers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda23
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$17(tLObject2, tL_error);
                            }
                        });
                    }
                    arrayList = this.wallpaperWaiters;
                    waiter = new Waiter(str, str2);
                } else if (str3.startsWith("gif")) {
                    if (this.savedGifsWaiters.isEmpty()) {
                        getConnectionsManager().sendRequest(new TLRPC.TL_messages_getSavedGifs(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda24
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$18(tLObject2, tL_error);
                            }
                        });
                    }
                    arrayList = this.savedGifsWaiters;
                    waiter = new Waiter(str, str2);
                } else if ("recent".equals(str3)) {
                    if (this.recentStickersWaiter.isEmpty()) {
                        getConnectionsManager().sendRequest(new TLRPC.TL_messages_getRecentStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda25
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$19(tLObject2, tL_error);
                            }
                        });
                    }
                    arrayList = this.recentStickersWaiter;
                    waiter = new Waiter(str, str2);
                } else if ("fav".equals(str3)) {
                    if (this.favStickersWaiter.isEmpty()) {
                        getConnectionsManager().sendRequest(new TLRPC.TL_messages_getFavedStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda26
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$20(tLObject2, tL_error);
                            }
                        });
                    }
                    arrayList = this.favStickersWaiter;
                    waiter = new Waiter(str, str2);
                } else if ("update".equals(str3)) {
                    TLRPC.TL_help_getAppUpdate tL_help_getAppUpdate = new TLRPC.TL_help_getAppUpdate();
                    try {
                        tL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
                    } catch (Exception unused) {
                    }
                    if (tL_help_getAppUpdate.source == null) {
                        tL_help_getAppUpdate.source = "";
                    }
                    ConnectionsManager connectionsManager17 = getConnectionsManager();
                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda28
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$21(str, str2, tLObject2, tL_error);
                        }
                    };
                    connectionsManager = connectionsManager17;
                    tLObject = tL_help_getAppUpdate;
                } else {
                    if (!str3.startsWith("avatar_")) {
                        if (str3.startsWith("sent_")) {
                            String[] split = str3.split("_");
                            if (split.length >= 3) {
                                long longValue = Utilities.parseLong(split[1]).longValue();
                                if (longValue != 0) {
                                    TLRPC.TL_channels_getMessages tL_channels_getMessages2 = new TLRPC.TL_channels_getMessages();
                                    tL_channels_getMessages2.channel = getMessagesController().getInputChannel(longValue);
                                    tL_channels_getMessages2.id.add(Utilities.parseInt((CharSequence) split[2]));
                                    ConnectionsManager connectionsManager18 = getConnectionsManager();
                                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda31
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                            FileRefController.this.lambda$requestReferenceFromServer$24(str, str2, tLObject2, tL_error);
                                        }
                                    };
                                    tLObject = tL_channels_getMessages2;
                                    connectionsManager = connectionsManager18;
                                } else {
                                    TLRPC.TL_messages_getMessages tL_messages_getMessages2 = new TLRPC.TL_messages_getMessages();
                                    tL_messages_getMessages2.id.add(Utilities.parseInt((CharSequence) split[2]));
                                    ConnectionsManager connectionsManager19 = getConnectionsManager();
                                    requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda32
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                            FileRefController.this.lambda$requestReferenceFromServer$25(str, str2, tLObject2, tL_error);
                                        }
                                    };
                                    tLObject = tL_messages_getMessages2;
                                    connectionsManager = connectionsManager19;
                                }
                            }
                        }
                        sendErrorToObject(objArr, 0);
                        return;
                    }
                    long longValue2 = Utilities.parseLong(str3).longValue();
                    if (longValue2 > 0) {
                        TLRPC.TL_photos_getUserPhotos tL_photos_getUserPhotos = new TLRPC.TL_photos_getUserPhotos();
                        tL_photos_getUserPhotos.limit = 80;
                        tL_photos_getUserPhotos.offset = 0;
                        tL_photos_getUserPhotos.max_id = 0L;
                        tL_photos_getUserPhotos.user_id = getMessagesController().getInputUser(longValue2);
                        ConnectionsManager connectionsManager20 = getConnectionsManager();
                        requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda29
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$22(str, str2, tLObject2, tL_error);
                            }
                        };
                        connectionsManager = connectionsManager20;
                        tLObject = tL_photos_getUserPhotos;
                    } else {
                        TLRPC.TL_messages_search tL_messages_search = new TLRPC.TL_messages_search();
                        tL_messages_search.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
                        tL_messages_search.limit = 80;
                        tL_messages_search.offset_id = 0;
                        tL_messages_search.q = "";
                        tL_messages_search.peer = getMessagesController().getInputPeer(longValue2);
                        ConnectionsManager connectionsManager21 = getConnectionsManager();
                        requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda30
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$23(str, str2, tLObject2, tL_error);
                            }
                        };
                        connectionsManager = connectionsManager21;
                        tLObject = tL_messages_search;
                    }
                }
                arrayList.add(waiter);
                return;
            }
            if (obj instanceof TLRPC.TL_messages_stickerSet) {
                TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
                TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
                tL_messages_getStickerSet.stickerset = tL_inputStickerSetID;
                TLRPC.StickerSet stickerSet = ((TLRPC.TL_messages_stickerSet) obj).set;
                tL_inputStickerSetID.id = stickerSet.id;
                tL_inputStickerSetID.access_hash = stickerSet.access_hash;
                ConnectionsManager connectionsManager22 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda33
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$26(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager22;
                tLObject = tL_messages_getStickerSet;
            } else {
                if (!(obj instanceof TLRPC.StickerSetCovered)) {
                    if (obj instanceof TLRPC.InputStickerSet) {
                        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet2 = new TLRPC.TL_messages_getStickerSet();
                        tL_messages_getStickerSet2.stickerset = (TLRPC.InputStickerSet) obj;
                        ConnectionsManager connectionsManager23 = getConnectionsManager();
                        requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda35
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$28(str, str2, tLObject2, tL_error);
                            }
                        };
                        connectionsManager = connectionsManager23;
                        tLObject = tL_messages_getStickerSet2;
                    }
                    sendErrorToObject(objArr, 0);
                    return;
                }
                TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet3 = new TLRPC.TL_messages_getStickerSet();
                TLRPC.TL_inputStickerSetID tL_inputStickerSetID2 = new TLRPC.TL_inputStickerSetID();
                tL_messages_getStickerSet3.stickerset = tL_inputStickerSetID2;
                TLRPC.StickerSet stickerSet2 = ((TLRPC.StickerSetCovered) obj).set;
                tL_inputStickerSetID2.id = stickerSet2.id;
                tL_inputStickerSetID2.access_hash = stickerSet2.access_hash;
                ConnectionsManager connectionsManager24 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda34
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$27(str, str2, tLObject2, tL_error);
                    }
                };
                connectionsManager = connectionsManager24;
                tLObject = tL_messages_getStickerSet3;
            }
        }
        connectionsManager.sendRequest(tLObject, requestDelegate);
    }

    private void sendErrorToObject(final Object[] objArr, int i) {
        Runnable runnable;
        Object obj = objArr[0];
        if (obj instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) objArr[1];
            final Object[] objArr2 = this.multiMediaCache.get(tL_messages_sendMultiMedia);
            if (objArr2 != null) {
                this.multiMediaCache.remove(tL_messages_sendMultiMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$sendErrorToObject$37(tL_messages_sendMultiMedia, objArr2);
                    }
                });
                return;
            }
            return;
        }
        if ((obj instanceof TLRPC.TL_inputMediaDocument) || (obj instanceof TLRPC.TL_inputMediaPhoto)) {
            Object obj2 = objArr[1];
            if (obj2 instanceof TLRPC.TL_messages_sendMedia) {
                final TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) obj2;
                final Object[] objArr3 = this.multiMediaCache.get(tL_messages_sendMedia);
                if (objArr3 != null) {
                    this.multiMediaCache.remove(tL_messages_sendMedia);
                    runnable = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileRefController.this.lambda$sendErrorToObject$38(tL_messages_sendMedia, objArr3);
                        }
                    };
                    AndroidUtilities.runOnUIThread(runnable);
                    return;
                }
                return;
            }
        }
        if (((obj instanceof TLRPC.TL_messages_sendMedia) && !(((TLRPC.TL_messages_sendMedia) obj).media instanceof TLRPC.TL_inputMediaPaidMedia)) || (obj instanceof TLRPC.TL_messages_editMessage)) {
            runnable = new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    FileRefController.this.lambda$sendErrorToObject$39(objArr);
                }
            };
            AndroidUtilities.runOnUIThread(runnable);
            return;
        }
        if ((obj instanceof TLRPC.TL_messages_saveGif) || (obj instanceof TLRPC.TL_messages_saveRecentSticker) || (obj instanceof TLRPC.TL_stickers_addStickerToSet) || (obj instanceof TLRPC.TL_messages_faveSticker)) {
            return;
        }
        if (obj instanceof TLRPC.TL_messages_getAttachedStickers) {
            getConnectionsManager().sendRequest((TLRPC.TL_messages_getAttachedStickers) obj, (RequestDelegate) objArr[1]);
            return;
        }
        Object obj3 = objArr[1];
        if (obj3 instanceof FileLoadOperation) {
            FileLoadOperation fileLoadOperation = (FileLoadOperation) obj3;
            fileLoadOperation.requestingReference = false;
            FileLog.e("debug_loading: " + fileLoadOperation.getCacheFileFinal().getName() + " reference can't update: fail operation ");
            fileLoadOperation.onFail(false, 0);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x0483, code lost:
    
        if ("update".equals(r0) != false) goto L174;
     */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0490  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x04a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void requestReference(Object obj, Object... objArr) {
        TLRPC.InputFileLocation inputFileLocation;
        StringBuilder sb;
        long j;
        String sb2;
        String str;
        TLRPC.InputFileLocation tL_inputPhotoFileLocation;
        TLRPC.InputPhoto inputPhoto;
        TLRPC.InputDocument inputDocument;
        TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto;
        TLRPC.TL_inputMediaDocument tL_inputMediaDocument;
        long j2;
        String str2;
        CachedResult cachedResponse;
        TLRPC.Message message;
        TLRPC.MessageMedia messageMedia;
        Object obj2;
        StringBuilder sb3;
        long j3;
        int i = 0;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start loading request reference parent " + getObjectString(obj) + " args = " + objArr[0]);
        }
        Object obj3 = objArr[0];
        if (obj3 instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj3;
            TLRPC.MessageMedia messageMedia2 = botPreview.media;
            if (messageMedia2.document != null) {
                inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                inputFileLocation.id = botPreview.media.document.id;
                sb3 = new StringBuilder();
                sb3.append("botstory_doc_");
                j3 = botPreview.media.document.id;
            } else if (messageMedia2.photo != null) {
                inputFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                inputFileLocation.id = botPreview.media.photo.id;
                sb3 = new StringBuilder();
                sb3.append("botstory_photo_");
                j3 = botPreview.media.photo.id;
            } else {
                sb2 = "botstory_" + botPreview.id;
                inputFileLocation = new TLRPC.TL_inputDocumentFileLocation();
            }
            sb3.append(j3);
            sb2 = sb3.toString();
        } else {
            if (obj3 instanceof TL_stories.TL_storyItem) {
                TL_stories.TL_storyItem tL_storyItem = (TL_stories.TL_storyItem) obj3;
                str = "story_" + tL_storyItem.id;
                tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                j2 = tL_storyItem.media.document.id;
            } else {
                String str3 = "photo_";
                if (obj3 instanceof TLRPC.TL_inputSingleMedia) {
                    TLRPC.InputMedia inputMedia = ((TLRPC.TL_inputSingleMedia) obj3).media;
                    if (inputMedia instanceof TLRPC.TL_inputMediaDocument) {
                        tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia;
                        str = "file_" + tL_inputMediaDocument.id.id;
                        tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                        inputDocument = tL_inputMediaDocument.id;
                        j2 = inputDocument.id;
                    } else {
                        if (!(inputMedia instanceof TLRPC.TL_inputMediaPhoto)) {
                            sendErrorToObject(objArr, 0);
                            return;
                        }
                        tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) inputMedia;
                        str = "photo_" + tL_inputMediaPhoto.id.id;
                        tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                        inputPhoto = tL_inputMediaPhoto.id;
                        j2 = inputPhoto.id;
                    }
                } else {
                    if (obj3 instanceof TLRPC.TL_inputMediaDocument) {
                        tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) obj3;
                        str = "file_" + tL_inputMediaDocument.id.id;
                        tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                    } else {
                        if (obj3 instanceof TLRPC.TL_inputMediaPhoto) {
                            tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) obj3;
                            str = "photo_" + tL_inputMediaPhoto.id.id;
                            tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                        } else {
                            if (obj3 instanceof TLRPC.TL_messages_sendMultiMedia) {
                                TLRPC.TL_messages_sendMultiMedia tL_messages_sendMultiMedia = (TLRPC.TL_messages_sendMultiMedia) obj3;
                                ArrayList arrayList = (ArrayList) obj;
                                this.multiMediaCache.put(tL_messages_sendMultiMedia, objArr);
                                int size = tL_messages_sendMultiMedia.multi_media.size();
                                for (int i2 = 0; i2 < size; i2++) {
                                    Object obj4 = (TLRPC.TL_inputSingleMedia) tL_messages_sendMultiMedia.multi_media.get(i2);
                                    Object obj5 = arrayList.get(i2);
                                    if (obj5 != null) {
                                        requestReference(obj5, obj4, tL_messages_sendMultiMedia);
                                    }
                                }
                                return;
                            }
                            if (obj3 instanceof TLRPC.TL_messages_sendMedia) {
                                TLRPC.TL_messages_sendMedia tL_messages_sendMedia = (TLRPC.TL_messages_sendMedia) obj3;
                                TLRPC.InputMedia inputMedia2 = tL_messages_sendMedia.media;
                                if (inputMedia2 instanceof TLRPC.TL_inputMediaDocument) {
                                    TLRPC.TL_inputMediaDocument tL_inputMediaDocument2 = (TLRPC.TL_inputMediaDocument) inputMedia2;
                                    sb2 = "file_" + tL_inputMediaDocument2.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    j2 = tL_inputMediaDocument2.id.id;
                                } else if (inputMedia2 instanceof TLRPC.TL_inputMediaPhoto) {
                                    TLRPC.TL_inputMediaPhoto tL_inputMediaPhoto2 = (TLRPC.TL_inputMediaPhoto) inputMedia2;
                                    sb2 = "photo_" + tL_inputMediaPhoto2.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                                    j2 = tL_inputMediaPhoto2.id.id;
                                } else {
                                    if (!(inputMedia2 instanceof TLRPC.TL_inputMediaPaidMedia)) {
                                        sendErrorToObject(objArr, 0);
                                        return;
                                    }
                                    TLRPC.TL_inputMediaPaidMedia tL_inputMediaPaidMedia = (TLRPC.TL_inputMediaPaidMedia) inputMedia2;
                                    if (obj instanceof ArrayList) {
                                        ArrayList arrayList2 = (ArrayList) obj;
                                        this.multiMediaCache.put(tL_messages_sendMedia, objArr);
                                        int size2 = tL_inputMediaPaidMedia.extended_media.size();
                                        for (int i3 = 0; i3 < size2; i3++) {
                                            Object obj6 = (TLRPC.InputMedia) tL_inputMediaPaidMedia.extended_media.get(i3);
                                            Object obj7 = arrayList2.get(i3);
                                            if (obj7 != null) {
                                                requestReference(obj7, obj6, tL_messages_sendMedia);
                                            }
                                        }
                                        return;
                                    }
                                    if (tL_inputMediaPaidMedia.extended_media.size() != 1) {
                                        sendErrorToObject(objArr, 0);
                                        return;
                                    }
                                    TLRPC.InputMedia inputMedia3 = tL_inputMediaPaidMedia.extended_media.get(0);
                                    if (inputMedia3 instanceof TLRPC.TL_inputMediaDocument) {
                                        tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia3;
                                        str = "file_" + tL_inputMediaDocument.id.id;
                                        tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    } else {
                                        if (!(inputMedia3 instanceof TLRPC.TL_inputMediaPhoto)) {
                                            sendErrorToObject(objArr, 0);
                                            return;
                                        }
                                        tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) inputMedia3;
                                        str = "photo_" + tL_inputMediaPhoto.id.id;
                                        tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                                    }
                                }
                                inputFileLocation = tL_inputPhotoFileLocation;
                                inputFileLocation.id = j2;
                            } else if (obj3 instanceof TLRPC.TL_messages_editMessage) {
                                TLRPC.InputMedia inputMedia4 = ((TLRPC.TL_messages_editMessage) obj3).media;
                                if (inputMedia4 instanceof TLRPC.TL_inputMediaDocument) {
                                    tL_inputMediaDocument = (TLRPC.TL_inputMediaDocument) inputMedia4;
                                    str = "file_" + tL_inputMediaDocument.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                } else {
                                    if (!(inputMedia4 instanceof TLRPC.TL_inputMediaPhoto)) {
                                        sendErrorToObject(objArr, 0);
                                        return;
                                    }
                                    tL_inputMediaPhoto = (TLRPC.TL_inputMediaPhoto) inputMedia4;
                                    str = "photo_" + tL_inputMediaPhoto.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                                }
                            } else {
                                if (obj3 instanceof TLRPC.TL_messages_saveGif) {
                                    TLRPC.TL_messages_saveGif tL_messages_saveGif = (TLRPC.TL_messages_saveGif) obj3;
                                    str = "file_" + tL_messages_saveGif.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    inputDocument = tL_messages_saveGif.id;
                                } else if (obj3 instanceof TLRPC.TL_messages_saveRecentSticker) {
                                    TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker) obj3;
                                    str = "file_" + tL_messages_saveRecentSticker.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    inputDocument = tL_messages_saveRecentSticker.id;
                                } else if (obj3 instanceof TLRPC.TL_stickers_addStickerToSet) {
                                    TLRPC.TL_stickers_addStickerToSet tL_stickers_addStickerToSet = (TLRPC.TL_stickers_addStickerToSet) obj3;
                                    str = "file_" + tL_stickers_addStickerToSet.sticker.document.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    inputDocument = tL_stickers_addStickerToSet.sticker.document;
                                } else if (obj3 instanceof TLRPC.TL_messages_faveSticker) {
                                    TLRPC.TL_messages_faveSticker tL_messages_faveSticker = (TLRPC.TL_messages_faveSticker) obj3;
                                    str = "file_" + tL_messages_faveSticker.id.id;
                                    tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                    inputDocument = tL_messages_faveSticker.id;
                                } else if (obj3 instanceof TLRPC.TL_messages_getAttachedStickers) {
                                    TLRPC.InputStickeredMedia inputStickeredMedia = ((TLRPC.TL_messages_getAttachedStickers) obj3).media;
                                    if (inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaDocument) {
                                        TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument = (TLRPC.TL_inputStickeredMediaDocument) inputStickeredMedia;
                                        str = "file_" + tL_inputStickeredMediaDocument.id.id;
                                        tL_inputPhotoFileLocation = new TLRPC.TL_inputDocumentFileLocation();
                                        inputDocument = tL_inputStickeredMediaDocument.id;
                                    } else {
                                        if (!(inputStickeredMedia instanceof TLRPC.TL_inputStickeredMediaPhoto)) {
                                            sendErrorToObject(objArr, 0);
                                            return;
                                        }
                                        TLRPC.TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = (TLRPC.TL_inputStickeredMediaPhoto) inputStickeredMedia;
                                        str = "photo_" + tL_inputStickeredMediaPhoto.id.id;
                                        tL_inputPhotoFileLocation = new TLRPC.TL_inputPhotoFileLocation();
                                        inputPhoto = tL_inputStickeredMediaPhoto.id;
                                        j2 = inputPhoto.id;
                                    }
                                } else {
                                    if (obj3 instanceof TLRPC.TL_inputFileLocation) {
                                        inputFileLocation = (TLRPC.TL_inputFileLocation) obj3;
                                        sb = new StringBuilder();
                                        sb.append("loc_");
                                        sb.append(inputFileLocation.local_id);
                                        sb.append("_");
                                        j = inputFileLocation.volume_id;
                                    } else {
                                        if (obj3 instanceof TLRPC.TL_inputDocumentFileLocation) {
                                            inputFileLocation = (TLRPC.TL_inputDocumentFileLocation) obj3;
                                            sb = new StringBuilder();
                                            sb.append("file_");
                                        } else {
                                            if (obj3 instanceof TLRPC.TL_inputPhotoFileLocation) {
                                                inputFileLocation = (TLRPC.TL_inputPhotoFileLocation) obj3;
                                                sb = new StringBuilder();
                                            } else if (!(obj3 instanceof TLRPC.TL_inputPeerPhotoFileLocation)) {
                                                sendErrorToObject(objArr, 0);
                                                return;
                                            } else {
                                                inputFileLocation = (TLRPC.TL_inputPeerPhotoFileLocation) obj3;
                                                sb = new StringBuilder();
                                                str3 = "avatar_";
                                            }
                                            sb.append(str3);
                                        }
                                        j = inputFileLocation.id;
                                    }
                                    sb.append(j);
                                    sb2 = sb.toString();
                                }
                                j2 = inputDocument.id;
                            }
                        }
                        inputPhoto = tL_inputMediaPhoto.id;
                        j2 = inputPhoto.id;
                    }
                    inputDocument = tL_inputMediaDocument.id;
                    j2 = inputDocument.id;
                }
            }
            sb2 = str;
            inputFileLocation = tL_inputPhotoFileLocation;
            inputFileLocation.id = j2;
        }
        if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            if (messageObject.getRealId() < 0 && (message = messageObject.messageOwner) != null && (messageMedia = message.media) != null && (obj2 = messageMedia.webpage) != null) {
                obj = obj2;
            }
        }
        String keyForParentObject = getKeyForParentObject(obj);
        if (keyForParentObject == null) {
            sendErrorToObject(objArr, 0);
            return;
        }
        Requester requester = new Requester();
        requester.args = objArr;
        requester.location = inputFileLocation;
        requester.locationKey = sb2;
        ArrayList<Requester> arrayList3 = this.locationRequester.get(sb2);
        if (arrayList3 == null) {
            arrayList3 = new ArrayList<>();
            this.locationRequester.put(sb2, arrayList3);
            i = 1;
        }
        arrayList3.add(requester);
        ArrayList<Requester> arrayList4 = this.parentRequester.get(keyForParentObject);
        if (arrayList4 == null) {
            arrayList4 = new ArrayList<>();
            this.parentRequester.put(keyForParentObject, arrayList4);
            i++;
        }
        arrayList4.add(requester);
        if (i != 2) {
            return;
        }
        if (obj instanceof String) {
            String str4 = (String) obj;
            str2 = "wallpaper";
            if (!"wallpaper".equals(str4)) {
                str2 = "gif";
                if (!str4.startsWith("gif")) {
                    str2 = "recent";
                    if (!"recent".equals(str4)) {
                        str2 = "fav";
                        if (!"fav".equals(str4)) {
                            str2 = "update";
                        }
                    }
                }
            }
            cleanupCache();
            cachedResponse = getCachedResponse(str2);
            if (cachedResponse != null) {
                CachedResult cachedResponse2 = getCachedResponse(keyForParentObject);
                if (cachedResponse2 != null) {
                    if (onRequestComplete(sb2, keyForParentObject, cachedResponse2.response, null, false, true)) {
                        return;
                    } else {
                        this.responseCache.remove(keyForParentObject);
                    }
                }
            } else if (onRequestComplete(sb2, keyForParentObject, cachedResponse.response, null, false, true)) {
                return;
            } else {
                this.responseCache.remove(sb2);
            }
            requestReferenceFromServer(obj, sb2, keyForParentObject, objArr);
        }
        str2 = sb2;
        cleanupCache();
        cachedResponse = getCachedResponse(str2);
        if (cachedResponse != null) {
        }
        requestReferenceFromServer(obj, sb2, keyForParentObject, objArr);
    }
}
