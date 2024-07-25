package org.telegram.messenger;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFileLocation;
import org.telegram.tgnet.TLRPC$InputMedia;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$InputStickeredMedia;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Page;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_account_getTheme;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_getWallPapers;
import org.telegram.tgnet.TLRPC$TL_account_wallPapers;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotIcon;
import org.telegram.tgnet.TLRPC$TL_attachMenuBots;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsBot;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_channel;
import org.telegram.tgnet.TLRPC$TL_channels_getChannels;
import org.telegram.tgnet.TLRPC$TL_channels_getMessages;
import org.telegram.tgnet.TLRPC$TL_chat;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_game;
import org.telegram.tgnet.TLRPC$TL_help_appUpdate;
import org.telegram.tgnet.TLRPC$TL_help_getAppUpdate;
import org.telegram.tgnet.TLRPC$TL_help_getPremiumPromo;
import org.telegram.tgnet.TLRPC$TL_help_premiumPromo;
import org.telegram.tgnet.TLRPC$TL_inputDocumentFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputMediaDocument;
import org.telegram.tgnet.TLRPC$TL_inputMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_inputMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterChatPhotos;
import org.telegram.tgnet.TLRPC$TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC$TL_inputPeerChat;
import org.telegram.tgnet.TLRPC$TL_inputPeerPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputPeerUser;
import org.telegram.tgnet.TLRPC$TL_inputPhotoFileLocation;
import org.telegram.tgnet.TLRPC$TL_inputSingleMedia;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_inputStickeredMediaDocument;
import org.telegram.tgnet.TLRPC$TL_inputStickeredMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_inputTheme;
import org.telegram.tgnet.TLRPC$TL_inputWallPaper;
import org.telegram.tgnet.TLRPC$TL_messageActionChatEditPhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionSuggestProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messages_availableReactions;
import org.telegram.tgnet.TLRPC$TL_messages_chats;
import org.telegram.tgnet.TLRPC$TL_messages_editMessage;
import org.telegram.tgnet.TLRPC$TL_messages_faveSticker;
import org.telegram.tgnet.TLRPC$TL_messages_favedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getAvailableReactions;
import org.telegram.tgnet.TLRPC$TL_messages_getChats;
import org.telegram.tgnet.TLRPC$TL_messages_getFavedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getMessages;
import org.telegram.tgnet.TLRPC$TL_messages_getQuickReplyMessages;
import org.telegram.tgnet.TLRPC$TL_messages_getRecentStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getSavedGifs;
import org.telegram.tgnet.TLRPC$TL_messages_getScheduledMessages;
import org.telegram.tgnet.TLRPC$TL_messages_getStickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPage;
import org.telegram.tgnet.TLRPC$TL_messages_recentStickers;
import org.telegram.tgnet.TLRPC$TL_messages_saveGif;
import org.telegram.tgnet.TLRPC$TL_messages_saveRecentSticker;
import org.telegram.tgnet.TLRPC$TL_messages_savedGifs;
import org.telegram.tgnet.TLRPC$TL_messages_search;
import org.telegram.tgnet.TLRPC$TL_messages_sendMedia;
import org.telegram.tgnet.TLRPC$TL_messages_sendMultiMedia;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_webPage;
import org.telegram.tgnet.TLRPC$TL_photos_getUserPhotos;
import org.telegram.tgnet.TLRPC$TL_stickers_addStickerToSet;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_users_getFullUser;
import org.telegram.tgnet.TLRPC$TL_users_getUsers;
import org.telegram.tgnet.TLRPC$TL_users_userFull;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$TL_webPageAttributeTheme;
import org.telegram.tgnet.TLRPC$Update;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.TLRPC$WebPageAttribute;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.tgnet.TLRPC$photos_Photos;
import org.telegram.tgnet.tl.TL_bots$BotInfo;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_stories_getStoriesByID;
import org.telegram.tgnet.tl.TL_stories$TL_stories_stories;
import org.telegram.tgnet.tl.TL_stories$TL_storyItem;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.tgnet.tl.TL_stories$TL_updateStory;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$33(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$34(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$35(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onUpdateObjectReference$36(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Requester {
        private Object[] args;
        private boolean completed;
        private TLRPC$InputFileLocation location;
        private String locationKey;

        private Requester() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class CachedResult {
        private long firstQueryTime;
        private TLObject response;

        private CachedResult() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Waiter {
        private String locationKey;
        private String parentKey;

        public Waiter(String str, String str2) {
            this.locationKey = str;
            this.parentKey = str2;
        }
    }

    public static FileRefController getInstance(int i) {
        FileRefController fileRefController = Instance[i];
        if (fileRefController == null) {
            synchronized (FileRefController.class) {
                fileRefController = Instance[i];
                if (fileRefController == null) {
                    FileRefController[] fileRefControllerArr = Instance;
                    FileRefController fileRefController2 = new FileRefController(i);
                    fileRefControllerArr[i] = fileRefController2;
                    fileRefController = fileRefController2;
                }
            }
        }
        return fileRefController;
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

    public static String getKeyForParentObject(Object obj) {
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        TLRPC$Peer tLRPC$Peer;
        if (obj instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj;
            if (botPreview.list == null) {
                FileLog.d("failed request reference can't find list in botpreview");
                return null;
            }
            TLRPC$MessageMedia tLRPC$MessageMedia = botPreview.media;
            if (tLRPC$MessageMedia.document != null) {
                return "botstory_doc_" + botPreview.media.document.id;
            } else if (tLRPC$MessageMedia.photo != null) {
                return "botstory_photo_" + botPreview.media.photo.id;
            } else {
                return "botstory_" + botPreview.id;
            }
        }
        if (obj instanceof TL_stories$StoryItem) {
            TL_stories$StoryItem tL_stories$StoryItem = (TL_stories$StoryItem) obj;
            if (tL_stories$StoryItem.dialogId == 0) {
                FileLog.d("failed request reference can't find dialogId");
                return null;
            }
            return "story_" + tL_stories$StoryItem.dialogId + "_" + tL_stories$StoryItem.id;
        } else if (obj instanceof TLRPC$TL_help_premiumPromo) {
            return "premium_promo";
        } else {
            if (obj instanceof TLRPC$TL_availableReaction) {
                return "available_reaction_" + ((TLRPC$TL_availableReaction) obj).reaction;
            } else if (obj instanceof TL_bots$BotInfo) {
                return "bot_info_" + ((TL_bots$BotInfo) obj).user_id;
            } else if (obj instanceof TLRPC$TL_attachMenuBot) {
                long j = ((TLRPC$TL_attachMenuBot) obj).bot_id;
                return "attach_menu_bot_" + j;
            } else if (obj instanceof MessageObject) {
                MessageObject messageObject = (MessageObject) obj;
                long channelId = messageObject.getChannelId();
                if (messageObject.type == 29 && (tLRPC$Message = messageObject.messageOwner) != null && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && (tLRPC$Peer = tLRPC$MessageFwdHeader.from_id) != null) {
                    channelId = DialogObject.getPeerDialogId(tLRPC$Peer);
                }
                return "message" + messageObject.getRealId() + "_" + channelId + "_" + messageObject.scheduled + "_" + messageObject.getQuickReplyId();
            } else if (obj instanceof TLRPC$Message) {
                TLRPC$Message tLRPC$Message2 = (TLRPC$Message) obj;
                TLRPC$Peer tLRPC$Peer2 = tLRPC$Message2.peer_id;
                long j2 = tLRPC$Peer2 != null ? tLRPC$Peer2.channel_id : 0L;
                return "message" + tLRPC$Message2.id + "_" + j2 + "_" + tLRPC$Message2.from_scheduled;
            } else if (obj instanceof TLRPC$WebPage) {
                return "webpage" + ((TLRPC$WebPage) obj).id;
            } else if (obj instanceof TLRPC$User) {
                return "user" + ((TLRPC$User) obj).id;
            } else if (obj instanceof TLRPC$Chat) {
                return "chat" + ((TLRPC$Chat) obj).id;
            } else if (obj instanceof String) {
                return "str" + ((String) obj);
            } else if (obj instanceof TLRPC$TL_messages_stickerSet) {
                return "set" + ((TLRPC$TL_messages_stickerSet) obj).set.id;
            } else if (obj instanceof TLRPC$StickerSetCovered) {
                return "set" + ((TLRPC$StickerSetCovered) obj).set.id;
            } else if (obj instanceof TLRPC$InputStickerSet) {
                return "set" + ((TLRPC$InputStickerSet) obj).id;
            } else if (obj instanceof TLRPC$TL_wallPaper) {
                return "wallpaper" + ((TLRPC$TL_wallPaper) obj).id;
            } else if (obj instanceof TLRPC$TL_theme) {
                return "theme" + ((TLRPC$TL_theme) obj).id;
            } else if (obj != null) {
                return "" + obj;
            } else {
                return null;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:164:0x053e, code lost:
        if ("update".equals(r0) != false) goto L40;
     */
    /* JADX WARN: Removed duplicated region for block: B:126:0x04a2  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x04c4  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x054b  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0562  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void requestReference(Object obj, Object... objArr) {
        TLRPC$InputFileLocation tLRPC$InputFileLocation;
        String str;
        TLRPC$InputFileLocation tLRPC$InputFileLocation2;
        String str2;
        TLRPC$TL_inputStickeredMediaPhoto tLRPC$TL_inputStickeredMediaPhoto;
        String str3;
        TLRPC$InputFileLocation tLRPC$TL_inputPhotoFileLocation;
        TLRPC$TL_inputStickeredMediaDocument tLRPC$TL_inputStickeredMediaDocument;
        String str4;
        TLRPC$InputFileLocation tLRPC$TL_inputDocumentFileLocation;
        TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto;
        TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument;
        TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto2;
        TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument2;
        TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto3;
        TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument3;
        TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto4;
        TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument4;
        Object obj2;
        String keyForParentObject;
        CachedResult cachedResponse;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        Object obj3;
        int i = 0;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start loading request reference parent " + getObjectString(obj) + " args = " + objArr[0]);
        }
        if (objArr[0] instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) objArr[0];
            TLRPC$MessageMedia tLRPC$MessageMedia2 = botPreview.media;
            if (tLRPC$MessageMedia2.document != null) {
                tLRPC$InputFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                tLRPC$InputFileLocation.id = botPreview.media.document.id;
                str = "botstory_doc_" + botPreview.media.document.id;
            } else if (tLRPC$MessageMedia2.photo != null) {
                tLRPC$InputFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                tLRPC$InputFileLocation.id = botPreview.media.photo.id;
                str = "botstory_photo_" + botPreview.media.photo.id;
            } else {
                str = "botstory_" + botPreview.id;
                tLRPC$InputFileLocation = new TLRPC$TL_inputDocumentFileLocation();
            }
        } else {
            if (objArr[0] instanceof TL_stories$TL_storyItem) {
                TL_stories$TL_storyItem tL_stories$TL_storyItem = (TL_stories$TL_storyItem) objArr[0];
                str4 = "story_" + tL_stories$TL_storyItem.id;
                tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                tLRPC$TL_inputDocumentFileLocation.id = tL_stories$TL_storyItem.media.document.id;
            } else {
                if (objArr[0] instanceof TLRPC$TL_inputSingleMedia) {
                    TLRPC$InputMedia tLRPC$InputMedia = ((TLRPC$TL_inputSingleMedia) objArr[0]).media;
                    if (tLRPC$InputMedia instanceof TLRPC$TL_inputMediaDocument) {
                        str3 = "file_" + tLRPC$TL_inputMediaDocument4.id.id;
                        tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                        tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputMediaDocument) tLRPC$InputMedia).id.id;
                    } else if (tLRPC$InputMedia instanceof TLRPC$TL_inputMediaPhoto) {
                        str3 = "photo_" + tLRPC$TL_inputMediaPhoto4.id.id;
                        tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                        tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia).id.id;
                    } else {
                        sendErrorToObject(objArr, 0);
                        return;
                    }
                } else if (objArr[0] instanceof TLRPC$TL_inputMediaDocument) {
                    TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument5 = (TLRPC$TL_inputMediaDocument) objArr[0];
                    str4 = "file_" + tLRPC$TL_inputMediaDocument5.id.id;
                    tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                    tLRPC$TL_inputDocumentFileLocation.id = tLRPC$TL_inputMediaDocument5.id.id;
                } else if (objArr[0] instanceof TLRPC$TL_inputMediaPhoto) {
                    TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto5 = (TLRPC$TL_inputMediaPhoto) objArr[0];
                    str4 = "photo_" + tLRPC$TL_inputMediaPhoto5.id.id;
                    tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                    tLRPC$TL_inputDocumentFileLocation.id = tLRPC$TL_inputMediaPhoto5.id.id;
                } else if (objArr[0] instanceof TLRPC$TL_messages_sendMultiMedia) {
                    TLRPC$TL_messages_sendMultiMedia tLRPC$TL_messages_sendMultiMedia = (TLRPC$TL_messages_sendMultiMedia) objArr[0];
                    ArrayList arrayList = (ArrayList) obj;
                    this.multiMediaCache.put(tLRPC$TL_messages_sendMultiMedia, objArr);
                    int size = tLRPC$TL_messages_sendMultiMedia.multi_media.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        Object obj4 = (TLRPC$TL_inputSingleMedia) tLRPC$TL_messages_sendMultiMedia.multi_media.get(i2);
                        Object obj5 = arrayList.get(i2);
                        if (obj5 != null) {
                            requestReference(obj5, obj4, tLRPC$TL_messages_sendMultiMedia);
                        }
                    }
                    return;
                } else if (objArr[0] instanceof TLRPC$TL_messages_sendMedia) {
                    TLRPC$TL_messages_sendMedia tLRPC$TL_messages_sendMedia = (TLRPC$TL_messages_sendMedia) objArr[0];
                    TLRPC$InputMedia tLRPC$InputMedia2 = tLRPC$TL_messages_sendMedia.media;
                    if (tLRPC$InputMedia2 instanceof TLRPC$TL_inputMediaDocument) {
                        str = "file_" + tLRPC$TL_inputMediaDocument3.id.id;
                        tLRPC$InputFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                        tLRPC$InputFileLocation.id = ((TLRPC$TL_inputMediaDocument) tLRPC$InputMedia2).id.id;
                    } else if (tLRPC$InputMedia2 instanceof TLRPC$TL_inputMediaPhoto) {
                        str = "photo_" + tLRPC$TL_inputMediaPhoto3.id.id;
                        tLRPC$InputFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                        tLRPC$InputFileLocation.id = ((TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia2).id.id;
                    } else if (tLRPC$InputMedia2 instanceof TLRPC$TL_inputMediaPaidMedia) {
                        TLRPC$TL_inputMediaPaidMedia tLRPC$TL_inputMediaPaidMedia = (TLRPC$TL_inputMediaPaidMedia) tLRPC$InputMedia2;
                        if (obj instanceof ArrayList) {
                            ArrayList arrayList2 = (ArrayList) obj;
                            this.multiMediaCache.put(tLRPC$TL_messages_sendMedia, objArr);
                            int size2 = tLRPC$TL_inputMediaPaidMedia.extended_media.size();
                            for (int i3 = 0; i3 < size2; i3++) {
                                Object obj6 = (TLRPC$InputMedia) tLRPC$TL_inputMediaPaidMedia.extended_media.get(i3);
                                Object obj7 = arrayList2.get(i3);
                                if (obj7 != null) {
                                    requestReference(obj7, obj6, tLRPC$TL_messages_sendMedia);
                                }
                            }
                            return;
                        } else if (tLRPC$TL_inputMediaPaidMedia.extended_media.size() == 1) {
                            TLRPC$InputMedia tLRPC$InputMedia3 = tLRPC$TL_inputMediaPaidMedia.extended_media.get(0);
                            if (tLRPC$InputMedia3 instanceof TLRPC$TL_inputMediaDocument) {
                                str3 = "file_" + tLRPC$TL_inputMediaDocument2.id.id;
                                tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                                tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputMediaDocument) tLRPC$InputMedia3).id.id;
                            } else if (tLRPC$InputMedia3 instanceof TLRPC$TL_inputMediaPhoto) {
                                str3 = "photo_" + tLRPC$TL_inputMediaPhoto2.id.id;
                                tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                                tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia3).id.id;
                            } else {
                                sendErrorToObject(objArr, 0);
                                return;
                            }
                        } else {
                            sendErrorToObject(objArr, 0);
                            return;
                        }
                    } else {
                        sendErrorToObject(objArr, 0);
                        return;
                    }
                } else if (objArr[0] instanceof TLRPC$TL_messages_editMessage) {
                    TLRPC$InputMedia tLRPC$InputMedia4 = ((TLRPC$TL_messages_editMessage) objArr[0]).media;
                    if (tLRPC$InputMedia4 instanceof TLRPC$TL_inputMediaDocument) {
                        str3 = "file_" + tLRPC$TL_inputMediaDocument.id.id;
                        tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                        tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputMediaDocument) tLRPC$InputMedia4).id.id;
                    } else if (tLRPC$InputMedia4 instanceof TLRPC$TL_inputMediaPhoto) {
                        str3 = "photo_" + tLRPC$TL_inputMediaPhoto.id.id;
                        tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                        tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia4).id.id;
                    } else {
                        sendErrorToObject(objArr, 0);
                        return;
                    }
                } else if (objArr[0] instanceof TLRPC$TL_messages_saveGif) {
                    TLRPC$TL_messages_saveGif tLRPC$TL_messages_saveGif = (TLRPC$TL_messages_saveGif) objArr[0];
                    str4 = "file_" + tLRPC$TL_messages_saveGif.id.id;
                    tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                    tLRPC$TL_inputDocumentFileLocation.id = tLRPC$TL_messages_saveGif.id.id;
                } else if (objArr[0] instanceof TLRPC$TL_messages_saveRecentSticker) {
                    TLRPC$TL_messages_saveRecentSticker tLRPC$TL_messages_saveRecentSticker = (TLRPC$TL_messages_saveRecentSticker) objArr[0];
                    str4 = "file_" + tLRPC$TL_messages_saveRecentSticker.id.id;
                    tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                    tLRPC$TL_inputDocumentFileLocation.id = tLRPC$TL_messages_saveRecentSticker.id.id;
                } else if (objArr[0] instanceof TLRPC$TL_stickers_addStickerToSet) {
                    TLRPC$TL_stickers_addStickerToSet tLRPC$TL_stickers_addStickerToSet = (TLRPC$TL_stickers_addStickerToSet) objArr[0];
                    str4 = "file_" + tLRPC$TL_stickers_addStickerToSet.sticker.document.id;
                    tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                    tLRPC$TL_inputDocumentFileLocation.id = tLRPC$TL_stickers_addStickerToSet.sticker.document.id;
                } else if (objArr[0] instanceof TLRPC$TL_messages_faveSticker) {
                    TLRPC$TL_messages_faveSticker tLRPC$TL_messages_faveSticker = (TLRPC$TL_messages_faveSticker) objArr[0];
                    str4 = "file_" + tLRPC$TL_messages_faveSticker.id.id;
                    tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                    tLRPC$TL_inputDocumentFileLocation.id = tLRPC$TL_messages_faveSticker.id.id;
                } else if (objArr[0] instanceof TLRPC$TL_messages_getAttachedStickers) {
                    TLRPC$InputStickeredMedia tLRPC$InputStickeredMedia = ((TLRPC$TL_messages_getAttachedStickers) objArr[0]).media;
                    if (tLRPC$InputStickeredMedia instanceof TLRPC$TL_inputStickeredMediaDocument) {
                        str3 = "file_" + tLRPC$TL_inputStickeredMediaDocument.id.id;
                        tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                        tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputStickeredMediaDocument) tLRPC$InputStickeredMedia).id.id;
                    } else if (tLRPC$InputStickeredMedia instanceof TLRPC$TL_inputStickeredMediaPhoto) {
                        str3 = "photo_" + tLRPC$TL_inputStickeredMediaPhoto.id.id;
                        tLRPC$TL_inputPhotoFileLocation = new TLRPC$TL_inputPhotoFileLocation();
                        tLRPC$TL_inputPhotoFileLocation.id = ((TLRPC$TL_inputStickeredMediaPhoto) tLRPC$InputStickeredMedia).id.id;
                    } else {
                        sendErrorToObject(objArr, 0);
                        return;
                    }
                } else if (objArr[0] instanceof TLRPC$TL_inputFileLocation) {
                    tLRPC$InputFileLocation = (TLRPC$TL_inputFileLocation) objArr[0];
                    str = "loc_" + tLRPC$InputFileLocation.local_id + "_" + tLRPC$InputFileLocation.volume_id;
                } else if (objArr[0] instanceof TLRPC$TL_inputDocumentFileLocation) {
                    tLRPC$InputFileLocation = (TLRPC$TL_inputDocumentFileLocation) objArr[0];
                    str = "file_" + tLRPC$InputFileLocation.id;
                } else if (objArr[0] instanceof TLRPC$TL_inputPhotoFileLocation) {
                    str2 = "photo_" + tLRPC$InputFileLocation2.id;
                    tLRPC$InputFileLocation = (TLRPC$TL_inputPhotoFileLocation) objArr[0];
                    if (obj instanceof MessageObject) {
                        MessageObject messageObject = (MessageObject) obj;
                        if (messageObject.getRealId() < 0 && (tLRPC$Message = messageObject.messageOwner) != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && (obj3 = tLRPC$MessageMedia.webpage) != null) {
                            obj2 = obj3;
                            keyForParentObject = getKeyForParentObject(obj2);
                            if (keyForParentObject != null) {
                                sendErrorToObject(objArr, 0);
                                return;
                            }
                            Requester requester = new Requester();
                            requester.args = objArr;
                            requester.location = tLRPC$InputFileLocation;
                            requester.locationKey = str2;
                            ArrayList<Requester> arrayList3 = this.locationRequester.get(str2);
                            if (arrayList3 == null) {
                                arrayList3 = new ArrayList<>();
                                this.locationRequester.put(str2, arrayList3);
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
                            String str5 = "update";
                            if (obj2 instanceof String) {
                                String str6 = (String) obj2;
                                if ("wallpaper".equals(str6)) {
                                    str5 = "wallpaper";
                                } else if (str6.startsWith("gif")) {
                                    str5 = "gif";
                                } else if ("recent".equals(str6)) {
                                    str5 = "recent";
                                } else if ("fav".equals(str6)) {
                                    str5 = "fav";
                                }
                                cleanupCache();
                                cachedResponse = getCachedResponse(str5);
                                if (cachedResponse == null) {
                                    if (onRequestComplete(str2, keyForParentObject, cachedResponse.response, null, false, true)) {
                                        return;
                                    }
                                    this.responseCache.remove(str2);
                                } else {
                                    CachedResult cachedResponse2 = getCachedResponse(keyForParentObject);
                                    if (cachedResponse2 != null) {
                                        if (onRequestComplete(str2, keyForParentObject, cachedResponse2.response, null, false, true)) {
                                            return;
                                        }
                                        this.responseCache.remove(keyForParentObject);
                                    }
                                }
                                requestReferenceFromServer(obj2, str2, keyForParentObject, objArr);
                                return;
                            }
                            str5 = str2;
                            cleanupCache();
                            cachedResponse = getCachedResponse(str5);
                            if (cachedResponse == null) {
                            }
                            requestReferenceFromServer(obj2, str2, keyForParentObject, objArr);
                            return;
                        }
                    }
                    obj2 = obj;
                    keyForParentObject = getKeyForParentObject(obj2);
                    if (keyForParentObject != null) {
                    }
                } else if (objArr[0] instanceof TLRPC$TL_inputPeerPhotoFileLocation) {
                    tLRPC$InputFileLocation = (TLRPC$TL_inputPeerPhotoFileLocation) objArr[0];
                    str = "avatar_" + tLRPC$InputFileLocation.id;
                } else {
                    sendErrorToObject(objArr, 0);
                    return;
                }
                str = str3;
                tLRPC$InputFileLocation = tLRPC$TL_inputPhotoFileLocation;
            }
            str2 = str4;
            tLRPC$InputFileLocation = tLRPC$TL_inputDocumentFileLocation;
            if (obj instanceof MessageObject) {
            }
            obj2 = obj;
            keyForParentObject = getKeyForParentObject(obj2);
            if (keyForParentObject != null) {
            }
        }
        str2 = str;
        if (obj instanceof MessageObject) {
        }
        obj2 = obj;
        keyForParentObject = getKeyForParentObject(obj2);
        if (keyForParentObject != null) {
        }
    }

    private String getObjectString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof TL_stories$StoryItem) {
            TL_stories$StoryItem tL_stories$StoryItem = (TL_stories$StoryItem) obj;
            return "story(dialogId=" + tL_stories$StoryItem.dialogId + " id=" + tL_stories$StoryItem.id + ")";
        } else if (!(obj instanceof MessageObject)) {
            if (obj == null) {
                return null;
            }
            return obj.getClass().getSimpleName();
        } else {
            MessageObject messageObject = (MessageObject) obj;
            return "message(dialogId=" + messageObject.getDialogId() + "messageId" + messageObject.getId() + ")";
        }
    }

    private void broadcastWaitersData(ArrayList<Waiter> arrayList, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Waiter waiter = arrayList.get(i);
            onRequestComplete(waiter.locationKey, waiter.parentKey, tLObject, tLRPC$TL_error, i == size + (-1), false);
            i++;
        }
        arrayList.clear();
    }

    private void requestReferenceFromServer(Object obj, final String str, final String str2, Object[] objArr) {
        if (obj instanceof StoriesController.BotPreview) {
            StoriesController.BotPreview botPreview = (StoriesController.BotPreview) obj;
            StoriesController.BotPreviewsList botPreviewsList = botPreview.list;
            if (botPreviewsList == null) {
                sendErrorToObject(objArr, 0);
            } else {
                botPreviewsList.requestReference(botPreview, new Utilities.Callback() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda13
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj2) {
                        FileRefController.this.lambda$requestReferenceFromServer$1(str, str2, (StoriesController.BotPreview) obj2);
                    }
                });
            }
        } else if (obj instanceof TL_stories$StoryItem) {
            TL_stories$StoryItem tL_stories$StoryItem = (TL_stories$StoryItem) obj;
            TL_stories$TL_stories_getStoriesByID tL_stories$TL_stories_getStoriesByID = new TL_stories$TL_stories_getStoriesByID();
            tL_stories$TL_stories_getStoriesByID.peer = getMessagesController().getInputPeer(tL_stories$StoryItem.dialogId);
            tL_stories$TL_stories_getStoriesByID.id.add(Integer.valueOf(tL_stories$StoryItem.id));
            getConnectionsManager().sendRequest(tL_stories$TL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda26
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$2(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$TL_help_premiumPromo) {
            getConnectionsManager().sendRequest(new TLRPC$TL_help_getPremiumPromo(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda33
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$3(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$TL_availableReaction) {
            TLRPC$TL_messages_getAvailableReactions tLRPC$TL_messages_getAvailableReactions = new TLRPC$TL_messages_getAvailableReactions();
            tLRPC$TL_messages_getAvailableReactions.hash = 0;
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getAvailableReactions, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda40
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$4(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TL_bots$BotInfo) {
            TLRPC$TL_users_getFullUser tLRPC$TL_users_getFullUser = new TLRPC$TL_users_getFullUser();
            tLRPC$TL_users_getFullUser.id = getMessagesController().getInputUser(((TL_bots$BotInfo) obj).user_id);
            getConnectionsManager().sendRequest(tLRPC$TL_users_getFullUser, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda38
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$5(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$TL_attachMenuBot) {
            TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
            tLRPC$TL_messages_getAttachMenuBot.bot = getMessagesController().getInputUser(((TLRPC$TL_attachMenuBot) obj).bot_id);
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda36
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$6(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) obj;
            long channelId = messageObject.getChannelId();
            if (messageObject.scheduled) {
                TLRPC$TL_messages_getScheduledMessages tLRPC$TL_messages_getScheduledMessages = new TLRPC$TL_messages_getScheduledMessages();
                tLRPC$TL_messages_getScheduledMessages.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                tLRPC$TL_messages_getScheduledMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tLRPC$TL_messages_getScheduledMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda30
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$7(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (messageObject.isQuickReply()) {
                TLRPC$TL_messages_getQuickReplyMessages tLRPC$TL_messages_getQuickReplyMessages = new TLRPC$TL_messages_getQuickReplyMessages();
                tLRPC$TL_messages_getQuickReplyMessages.shortcut_id = messageObject.getQuickReplyId();
                tLRPC$TL_messages_getQuickReplyMessages.flags |= 1;
                tLRPC$TL_messages_getQuickReplyMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tLRPC$TL_messages_getQuickReplyMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda23
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$8(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (channelId != 0) {
                TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
                tLRPC$TL_channels_getMessages.channel = getMessagesController().getInputChannel(channelId);
                tLRPC$TL_channels_getMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tLRPC$TL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda22
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$9(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            } else {
                TLRPC$TL_messages_getMessages tLRPC$TL_messages_getMessages = new TLRPC$TL_messages_getMessages();
                tLRPC$TL_messages_getMessages.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(tLRPC$TL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda39
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$10(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            }
        } else if (obj instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) obj;
            TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
            TLRPC$TL_inputWallPaper tLRPC$TL_inputWallPaper = new TLRPC$TL_inputWallPaper();
            tLRPC$TL_inputWallPaper.id = tLRPC$TL_wallPaper.id;
            tLRPC$TL_inputWallPaper.access_hash = tLRPC$TL_wallPaper.access_hash;
            tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaper;
            getConnectionsManager().sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda21
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$11(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$TL_theme) {
            TLRPC$TL_theme tLRPC$TL_theme = (TLRPC$TL_theme) obj;
            TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
            TLRPC$TL_inputTheme tLRPC$TL_inputTheme = new TLRPC$TL_inputTheme();
            tLRPC$TL_inputTheme.id = tLRPC$TL_theme.id;
            tLRPC$TL_inputTheme.access_hash = tLRPC$TL_theme.access_hash;
            tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputTheme;
            tLRPC$TL_account_getTheme.format = "android";
            getConnectionsManager().sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$12(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$WebPage) {
            TLRPC$TL_messages_getWebPage tLRPC$TL_messages_getWebPage = new TLRPC$TL_messages_getWebPage();
            tLRPC$TL_messages_getWebPage.url = ((TLRPC$WebPage) obj).url;
            tLRPC$TL_messages_getWebPage.hash = 0;
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getWebPage, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda31
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$13(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$User) {
            TLRPC$TL_users_getUsers tLRPC$TL_users_getUsers = new TLRPC$TL_users_getUsers();
            tLRPC$TL_users_getUsers.id.add(getMessagesController().getInputUser((TLRPC$User) obj));
            getConnectionsManager().sendRequest(tLRPC$TL_users_getUsers, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda29
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$14(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$Chat) {
            TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
            if (tLRPC$Chat instanceof TLRPC$TL_chat) {
                TLRPC$TL_messages_getChats tLRPC$TL_messages_getChats = new TLRPC$TL_messages_getChats();
                tLRPC$TL_messages_getChats.id.add(Long.valueOf(tLRPC$Chat.id));
                getConnectionsManager().sendRequest(tLRPC$TL_messages_getChats, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda32
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$15(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (tLRPC$Chat instanceof TLRPC$TL_channel) {
                TLRPC$TL_channels_getChannels tLRPC$TL_channels_getChannels = new TLRPC$TL_channels_getChannels();
                tLRPC$TL_channels_getChannels.id.add(MessagesController.getInputChannel(tLRPC$Chat));
                getConnectionsManager().sendRequest(tLRPC$TL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda18
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$16(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            }
        } else if (obj instanceof String) {
            String str3 = (String) obj;
            if ("wallpaper".equals(str3)) {
                if (this.wallpaperWaiters.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC$TL_account_getWallPapers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda15
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$17(tLObject, tLRPC$TL_error);
                        }
                    });
                }
                this.wallpaperWaiters.add(new Waiter(str, str2));
            } else if (str3.startsWith("gif")) {
                if (this.savedGifsWaiters.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC$TL_messages_getSavedGifs(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda17
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$18(tLObject, tLRPC$TL_error);
                        }
                    });
                }
                this.savedGifsWaiters.add(new Waiter(str, str2));
            } else if ("recent".equals(str3)) {
                if (this.recentStickersWaiter.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC$TL_messages_getRecentStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda16
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$19(tLObject, tLRPC$TL_error);
                        }
                    });
                }
                this.recentStickersWaiter.add(new Waiter(str, str2));
            } else if ("fav".equals(str3)) {
                if (this.favStickersWaiter.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC$TL_messages_getFavedStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda14
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$20(tLObject, tLRPC$TL_error);
                        }
                    });
                }
                this.favStickersWaiter.add(new Waiter(str, str2));
            } else if ("update".equals(str3)) {
                TLRPC$TL_help_getAppUpdate tLRPC$TL_help_getAppUpdate = new TLRPC$TL_help_getAppUpdate();
                try {
                    tLRPC$TL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
                } catch (Exception unused) {
                }
                if (tLRPC$TL_help_getAppUpdate.source == null) {
                    tLRPC$TL_help_getAppUpdate.source = "";
                }
                getConnectionsManager().sendRequest(tLRPC$TL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda27
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$21(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str3.startsWith("avatar_")) {
                long longValue = Utilities.parseLong(str3).longValue();
                if (longValue > 0) {
                    TLRPC$TL_photos_getUserPhotos tLRPC$TL_photos_getUserPhotos = new TLRPC$TL_photos_getUserPhotos();
                    tLRPC$TL_photos_getUserPhotos.limit = 80;
                    tLRPC$TL_photos_getUserPhotos.offset = 0;
                    tLRPC$TL_photos_getUserPhotos.max_id = 0L;
                    tLRPC$TL_photos_getUserPhotos.user_id = getMessagesController().getInputUser(longValue);
                    getConnectionsManager().sendRequest(tLRPC$TL_photos_getUserPhotos, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda28
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$22(str, str2, tLObject, tLRPC$TL_error);
                        }
                    });
                    return;
                }
                TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterChatPhotos();
                tLRPC$TL_messages_search.limit = 80;
                tLRPC$TL_messages_search.offset_id = 0;
                tLRPC$TL_messages_search.q = "";
                tLRPC$TL_messages_search.peer = getMessagesController().getInputPeer(longValue);
                getConnectionsManager().sendRequest(tLRPC$TL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda37
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        FileRefController.this.lambda$requestReferenceFromServer$23(str, str2, tLObject, tLRPC$TL_error);
                    }
                });
            } else if (str3.startsWith("sent_")) {
                String[] split = str3.split("_");
                if (split.length >= 3) {
                    long longValue2 = Utilities.parseLong(split[1]).longValue();
                    if (longValue2 != 0) {
                        TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages2 = new TLRPC$TL_channels_getMessages();
                        tLRPC$TL_channels_getMessages2.channel = getMessagesController().getInputChannel(longValue2);
                        tLRPC$TL_channels_getMessages2.id.add(Utilities.parseInt((CharSequence) split[2]));
                        getConnectionsManager().sendRequest(tLRPC$TL_channels_getMessages2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda25
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                FileRefController.this.lambda$requestReferenceFromServer$24(str, str2, tLObject, tLRPC$TL_error);
                            }
                        });
                        return;
                    }
                    TLRPC$TL_messages_getMessages tLRPC$TL_messages_getMessages2 = new TLRPC$TL_messages_getMessages();
                    tLRPC$TL_messages_getMessages2.id.add(Utilities.parseInt((CharSequence) split[2]));
                    getConnectionsManager().sendRequest(tLRPC$TL_messages_getMessages2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda20
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            FileRefController.this.lambda$requestReferenceFromServer$25(str, str2, tLObject, tLRPC$TL_error);
                        }
                    });
                    return;
                }
                sendErrorToObject(objArr, 0);
            } else {
                sendErrorToObject(objArr, 0);
            }
        } else if (obj instanceof TLRPC$TL_messages_stickerSet) {
            TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
            TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
            tLRPC$TL_messages_getStickerSet.stickerset = tLRPC$TL_inputStickerSetID;
            TLRPC$StickerSet tLRPC$StickerSet = ((TLRPC$TL_messages_stickerSet) obj).set;
            tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
            tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda35
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$26(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$StickerSetCovered) {
            TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet2 = new TLRPC$TL_messages_getStickerSet();
            TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID2 = new TLRPC$TL_inputStickerSetID();
            tLRPC$TL_messages_getStickerSet2.stickerset = tLRPC$TL_inputStickerSetID2;
            TLRPC$StickerSet tLRPC$StickerSet2 = ((TLRPC$StickerSetCovered) obj).set;
            tLRPC$TL_inputStickerSetID2.id = tLRPC$StickerSet2.id;
            tLRPC$TL_inputStickerSetID2.access_hash = tLRPC$StickerSet2.access_hash;
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda19
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$27(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else if (obj instanceof TLRPC$InputStickerSet) {
            TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet3 = new TLRPC$TL_messages_getStickerSet();
            tLRPC$TL_messages_getStickerSet3.stickerset = (TLRPC$InputStickerSet) obj;
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet3, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda34
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    FileRefController.this.lambda$requestReferenceFromServer$28(str, str2, tLObject, tLRPC$TL_error);
                }
            });
        } else {
            sendErrorToObject(objArr, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$1(final String str, final String str2, final StoriesController.BotPreview botPreview) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileRefController.this.lambda$requestReferenceFromServer$0(str, str2, botPreview);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$0(String str, String str2, StoriesController.BotPreview botPreview) {
        onRequestComplete(str, str2, botPreview, null, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$2(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$3(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC$TL_help_premiumPromo) {
            getMediaDataController().processLoadedPremiumPromo((TLRPC$TL_help_premiumPromo) tLObject, currentTimeMillis, false);
        }
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$4(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$5(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$6(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$7(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$8(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$9(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$10(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$11(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$12(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$13(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$14(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$15(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$16(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$17(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        broadcastWaitersData(this.wallpaperWaiters, tLObject, tLRPC$TL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$18(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        broadcastWaitersData(this.savedGifsWaiters, tLObject, tLRPC$TL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$19(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        broadcastWaitersData(this.recentStickersWaiter, tLObject, tLRPC$TL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$20(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        broadcastWaitersData(this.favStickersWaiter, tLObject, tLRPC$TL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$21(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$22(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$23(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$24(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$25(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$26(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$27(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestReferenceFromServer$28(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        onRequestComplete(str, str2, tLObject, tLRPC$TL_error, true, false);
    }

    private boolean isSameReference(byte[] bArr, byte[] bArr2) {
        return Arrays.equals(bArr, bArr2);
    }

    private boolean onUpdateObjectReference(final Requester requester, byte[] bArr, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean z) {
        String str;
        TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument;
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("fileref updated for " + requester.args[0] + " " + requester.locationKey);
        }
        if (!(requester.args[0] instanceof TL_stories$TL_storyItem)) {
            if (requester.args[0] instanceof TLRPC$TL_inputSingleMedia) {
                final TLRPC$TL_messages_sendMultiMedia tLRPC$TL_messages_sendMultiMedia = (TLRPC$TL_messages_sendMultiMedia) requester.args[1];
                final Object[] objArr = this.multiMediaCache.get(tLRPC$TL_messages_sendMultiMedia);
                if (objArr == null) {
                    return true;
                }
                TLRPC$TL_inputSingleMedia tLRPC$TL_inputSingleMedia = (TLRPC$TL_inputSingleMedia) requester.args[0];
                TLRPC$InputMedia tLRPC$InputMedia = tLRPC$TL_inputSingleMedia.media;
                if (tLRPC$InputMedia instanceof TLRPC$TL_inputMediaDocument) {
                    TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument2 = (TLRPC$TL_inputMediaDocument) tLRPC$InputMedia;
                    if (z && isSameReference(tLRPC$TL_inputMediaDocument2.id.file_reference, bArr)) {
                        return false;
                    }
                    tLRPC$TL_inputMediaDocument2.id.file_reference = bArr;
                } else if (tLRPC$InputMedia instanceof TLRPC$TL_inputMediaPhoto) {
                    TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto = (TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia;
                    if (z && isSameReference(tLRPC$TL_inputMediaPhoto.id.file_reference, bArr)) {
                        return false;
                    }
                    tLRPC$TL_inputMediaPhoto.id.file_reference = bArr;
                }
                int indexOf = tLRPC$TL_messages_sendMultiMedia.multi_media.indexOf(tLRPC$TL_inputSingleMedia);
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
                    this.multiMediaCache.remove(tLRPC$TL_messages_sendMultiMedia);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileRefController.this.lambda$onUpdateObjectReference$29(tLRPC$TL_messages_sendMultiMedia, objArr);
                        }
                    });
                }
            } else if (requester.args.length < 2 || !(requester.args[1] instanceof TLRPC$TL_messages_sendMedia) || !(((TLRPC$TL_messages_sendMedia) requester.args[1]).media instanceof TLRPC$TL_inputMediaPaidMedia) || (!(requester.args[0] instanceof TLRPC$TL_inputMediaPhoto) && !(requester.args[0] instanceof TLRPC$TL_inputMediaDocument))) {
                if (!(requester.args[0] instanceof TLRPC$TL_messages_sendMedia)) {
                    if (!(requester.args[0] instanceof TLRPC$TL_messages_editMessage)) {
                        if (requester.args[0] instanceof TLRPC$TL_messages_saveGif) {
                            TLRPC$TL_messages_saveGif tLRPC$TL_messages_saveGif = (TLRPC$TL_messages_saveGif) requester.args[0];
                            if (z && isSameReference(tLRPC$TL_messages_saveGif.id.file_reference, bArr)) {
                                return false;
                            }
                            tLRPC$TL_messages_saveGif.id.file_reference = bArr;
                            getConnectionsManager().sendRequest(tLRPC$TL_messages_saveGif, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda44
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    FileRefController.lambda$onUpdateObjectReference$33(tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (requester.args[0] instanceof TLRPC$TL_messages_saveRecentSticker) {
                            TLRPC$TL_messages_saveRecentSticker tLRPC$TL_messages_saveRecentSticker = (TLRPC$TL_messages_saveRecentSticker) requester.args[0];
                            if (z && isSameReference(tLRPC$TL_messages_saveRecentSticker.id.file_reference, bArr)) {
                                return false;
                            }
                            tLRPC$TL_messages_saveRecentSticker.id.file_reference = bArr;
                            getConnectionsManager().sendRequest(tLRPC$TL_messages_saveRecentSticker, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda42
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    FileRefController.lambda$onUpdateObjectReference$34(tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (requester.args[0] instanceof TLRPC$TL_stickers_addStickerToSet) {
                            TLRPC$TL_stickers_addStickerToSet tLRPC$TL_stickers_addStickerToSet = (TLRPC$TL_stickers_addStickerToSet) requester.args[0];
                            if (z && isSameReference(tLRPC$TL_stickers_addStickerToSet.sticker.document.file_reference, bArr)) {
                                return false;
                            }
                            tLRPC$TL_stickers_addStickerToSet.sticker.document.file_reference = bArr;
                            getConnectionsManager().sendRequest(tLRPC$TL_stickers_addStickerToSet, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda41
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    FileRefController.lambda$onUpdateObjectReference$35(tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (requester.args[0] instanceof TLRPC$TL_messages_faveSticker) {
                            TLRPC$TL_messages_faveSticker tLRPC$TL_messages_faveSticker = (TLRPC$TL_messages_faveSticker) requester.args[0];
                            if (z && isSameReference(tLRPC$TL_messages_faveSticker.id.file_reference, bArr)) {
                                return false;
                            }
                            tLRPC$TL_messages_faveSticker.id.file_reference = bArr;
                            getConnectionsManager().sendRequest(tLRPC$TL_messages_faveSticker, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda43
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    FileRefController.lambda$onUpdateObjectReference$36(tLObject, tLRPC$TL_error);
                                }
                            });
                        } else if (requester.args[0] instanceof TLRPC$TL_messages_getAttachedStickers) {
                            TLRPC$TL_messages_getAttachedStickers tLRPC$TL_messages_getAttachedStickers = (TLRPC$TL_messages_getAttachedStickers) requester.args[0];
                            TLRPC$InputStickeredMedia tLRPC$InputStickeredMedia = tLRPC$TL_messages_getAttachedStickers.media;
                            if (tLRPC$InputStickeredMedia instanceof TLRPC$TL_inputStickeredMediaDocument) {
                                TLRPC$TL_inputStickeredMediaDocument tLRPC$TL_inputStickeredMediaDocument = (TLRPC$TL_inputStickeredMediaDocument) tLRPC$InputStickeredMedia;
                                if (z && isSameReference(tLRPC$TL_inputStickeredMediaDocument.id.file_reference, bArr)) {
                                    return false;
                                }
                                tLRPC$TL_inputStickeredMediaDocument.id.file_reference = bArr;
                            } else if (tLRPC$InputStickeredMedia instanceof TLRPC$TL_inputStickeredMediaPhoto) {
                                TLRPC$TL_inputStickeredMediaPhoto tLRPC$TL_inputStickeredMediaPhoto = (TLRPC$TL_inputStickeredMediaPhoto) tLRPC$InputStickeredMedia;
                                if (z && isSameReference(tLRPC$TL_inputStickeredMediaPhoto.id.file_reference, bArr)) {
                                    return false;
                                }
                                tLRPC$TL_inputStickeredMediaPhoto.id.file_reference = bArr;
                            }
                            getConnectionsManager().sendRequest(tLRPC$TL_messages_getAttachedStickers, (RequestDelegate) requester.args[1]);
                        } else if (requester.args[1] instanceof FileLoadOperation) {
                            FileLoadOperation fileLoadOperation = (FileLoadOperation) requester.args[1];
                            if (tLRPC$InputFileLocation == null) {
                                if (z && isSameReference(requester.location.file_reference, bArr)) {
                                    return false;
                                }
                                String bytesToHex = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                                TLRPC$InputFileLocation tLRPC$InputFileLocation2 = fileLoadOperation.location;
                                requester.location.file_reference = bArr;
                                tLRPC$InputFileLocation2.file_reference = bArr;
                                r5 = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                                str = bytesToHex;
                            } else if (z && isSameReference(fileLoadOperation.location.file_reference, tLRPC$InputFileLocation.file_reference)) {
                                return false;
                            } else {
                                str = BuildVars.LOGS_ENABLED ? Utilities.bytesToHex(fileLoadOperation.location.file_reference) : null;
                                fileLoadOperation.location = tLRPC$InputFileLocation;
                                if (BuildVars.LOGS_ENABLED) {
                                    r5 = Utilities.bytesToHex(tLRPC$InputFileLocation.file_reference);
                                }
                            }
                            fileLoadOperation.requestingReference = false;
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("debug_loading: " + fileLoadOperation.getCacheFileFinal().getName() + " " + str + " " + r5 + " reference updated resume download");
                            }
                            fileLoadOperation.startDownloadRequest(-1);
                        }
                    } else {
                        TLRPC$InputMedia tLRPC$InputMedia2 = ((TLRPC$TL_messages_editMessage) requester.args[0]).media;
                        if (tLRPC$InputMedia2 instanceof TLRPC$TL_inputMediaDocument) {
                            TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument3 = (TLRPC$TL_inputMediaDocument) tLRPC$InputMedia2;
                            if (z && isSameReference(tLRPC$TL_inputMediaDocument3.id.file_reference, bArr)) {
                                return false;
                            }
                            tLRPC$TL_inputMediaDocument3.id.file_reference = bArr;
                        } else if (tLRPC$InputMedia2 instanceof TLRPC$TL_inputMediaPhoto) {
                            TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto2 = (TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia2;
                            if (z && isSameReference(tLRPC$TL_inputMediaPhoto2.id.file_reference, bArr)) {
                                return false;
                            }
                            tLRPC$TL_inputMediaPhoto2.id.file_reference = bArr;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                FileRefController.this.lambda$onUpdateObjectReference$32(requester);
                            }
                        });
                    }
                } else {
                    TLRPC$InputMedia tLRPC$InputMedia3 = ((TLRPC$TL_messages_sendMedia) requester.args[0]).media;
                    if (tLRPC$InputMedia3 instanceof TLRPC$TL_inputMediaDocument) {
                        TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument4 = (TLRPC$TL_inputMediaDocument) tLRPC$InputMedia3;
                        if (z && isSameReference(tLRPC$TL_inputMediaDocument4.id.file_reference, bArr)) {
                            return false;
                        }
                        tLRPC$TL_inputMediaDocument4.id.file_reference = bArr;
                    } else if (tLRPC$InputMedia3 instanceof TLRPC$TL_inputMediaPhoto) {
                        TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto3 = (TLRPC$TL_inputMediaPhoto) tLRPC$InputMedia3;
                        if (z && isSameReference(tLRPC$TL_inputMediaPhoto3.id.file_reference, bArr)) {
                            return false;
                        }
                        tLRPC$TL_inputMediaPhoto3.id.file_reference = bArr;
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileRefController.this.lambda$onUpdateObjectReference$31(requester);
                        }
                    });
                }
            } else {
                final TLRPC$TL_messages_sendMedia tLRPC$TL_messages_sendMedia = (TLRPC$TL_messages_sendMedia) requester.args[1];
                final Object[] objArr2 = this.multiMediaCache.get(tLRPC$TL_messages_sendMedia);
                if (objArr2 == null) {
                    return true;
                }
                if (requester.args[0] instanceof TLRPC$TL_inputMediaDocument) {
                    TLRPC$TL_inputMediaDocument tLRPC$TL_inputMediaDocument5 = (TLRPC$TL_inputMediaDocument) requester.args[0];
                    if (z && isSameReference(tLRPC$TL_inputMediaDocument5.id.file_reference, bArr)) {
                        return false;
                    }
                    tLRPC$TL_inputMediaDocument5.id.file_reference = bArr;
                    tLRPC$TL_inputMediaDocument = tLRPC$TL_inputMediaDocument5;
                } else if (requester.args[0] instanceof TLRPC$TL_inputMediaPhoto) {
                    TLRPC$TL_inputMediaPhoto tLRPC$TL_inputMediaPhoto4 = (TLRPC$TL_inputMediaPhoto) requester.args[0];
                    if (z && isSameReference(tLRPC$TL_inputMediaPhoto4.id.file_reference, bArr)) {
                        return false;
                    }
                    tLRPC$TL_inputMediaPhoto4.id.file_reference = bArr;
                    tLRPC$TL_inputMediaDocument = tLRPC$TL_inputMediaPhoto4;
                } else {
                    tLRPC$TL_inputMediaDocument = null;
                }
                int indexOf2 = ((TLRPC$TL_inputMediaPaidMedia) tLRPC$TL_messages_sendMedia.media).extended_media.indexOf(tLRPC$TL_inputMediaDocument);
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
                    this.multiMediaCache.remove(tLRPC$TL_messages_sendMedia);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            FileRefController.this.lambda$onUpdateObjectReference$30(tLRPC$TL_messages_sendMedia, objArr2);
                        }
                    });
                }
            }
            return true;
        }
        ((TL_stories$TL_storyItem) requester.args[0]).media.document.file_reference = bArr;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$29(TLRPC$TL_messages_sendMultiMedia tLRPC$TL_messages_sendMultiMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tLRPC$TL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$30(TLRPC$TL_messages_sendMedia tLRPC$TL_messages_sendMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tLRPC$TL_messages_sendMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$31(Requester requester) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUpdateObjectReference$32(Requester requester) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    private void sendErrorToObject(final Object[] objArr, int i) {
        if (objArr[0] instanceof TLRPC$TL_inputSingleMedia) {
            final TLRPC$TL_messages_sendMultiMedia tLRPC$TL_messages_sendMultiMedia = (TLRPC$TL_messages_sendMultiMedia) objArr[1];
            final Object[] objArr2 = this.multiMediaCache.get(tLRPC$TL_messages_sendMultiMedia);
            if (objArr2 != null) {
                this.multiMediaCache.remove(tLRPC$TL_messages_sendMultiMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$sendErrorToObject$37(tLRPC$TL_messages_sendMultiMedia, objArr2);
                    }
                });
            }
        } else if (((objArr[0] instanceof TLRPC$TL_inputMediaDocument) || (objArr[0] instanceof TLRPC$TL_inputMediaPhoto)) && (objArr[1] instanceof TLRPC$TL_messages_sendMedia)) {
            final TLRPC$TL_messages_sendMedia tLRPC$TL_messages_sendMedia = (TLRPC$TL_messages_sendMedia) objArr[1];
            final Object[] objArr3 = this.multiMediaCache.get(tLRPC$TL_messages_sendMedia);
            if (objArr3 != null) {
                this.multiMediaCache.remove(tLRPC$TL_messages_sendMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.lambda$sendErrorToObject$38(tLRPC$TL_messages_sendMedia, objArr3);
                    }
                });
            }
        } else if (((objArr[0] instanceof TLRPC$TL_messages_sendMedia) && !(((TLRPC$TL_messages_sendMedia) objArr[0]).media instanceof TLRPC$TL_inputMediaPaidMedia)) || (objArr[0] instanceof TLRPC$TL_messages_editMessage)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    FileRefController.this.lambda$sendErrorToObject$39(objArr);
                }
            });
        } else if (objArr[0] instanceof TLRPC$TL_messages_saveGif) {
            TLRPC$TL_messages_saveGif tLRPC$TL_messages_saveGif = (TLRPC$TL_messages_saveGif) objArr[0];
        } else if (objArr[0] instanceof TLRPC$TL_messages_saveRecentSticker) {
            TLRPC$TL_messages_saveRecentSticker tLRPC$TL_messages_saveRecentSticker = (TLRPC$TL_messages_saveRecentSticker) objArr[0];
        } else if (objArr[0] instanceof TLRPC$TL_stickers_addStickerToSet) {
            TLRPC$TL_stickers_addStickerToSet tLRPC$TL_stickers_addStickerToSet = (TLRPC$TL_stickers_addStickerToSet) objArr[0];
        } else if (objArr[0] instanceof TLRPC$TL_messages_faveSticker) {
            TLRPC$TL_messages_faveSticker tLRPC$TL_messages_faveSticker = (TLRPC$TL_messages_faveSticker) objArr[0];
        } else if (objArr[0] instanceof TLRPC$TL_messages_getAttachedStickers) {
            getConnectionsManager().sendRequest((TLRPC$TL_messages_getAttachedStickers) objArr[0], (RequestDelegate) objArr[1]);
        } else if (objArr[1] instanceof FileLoadOperation) {
            FileLoadOperation fileLoadOperation = (FileLoadOperation) objArr[1];
            fileLoadOperation.requestingReference = false;
            FileLog.e("debug_loading: " + fileLoadOperation.getCacheFileFinal().getName() + " reference can't update: fail operation ");
            fileLoadOperation.onFail(false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$37(TLRPC$TL_messages_sendMultiMedia tLRPC$TL_messages_sendMultiMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tLRPC$TL_messages_sendMultiMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$38(TLRPC$TL_messages_sendMedia tLRPC$TL_messages_sendMedia, Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequestMulti(tLRPC$TL_messages_sendMedia, (ArrayList) objArr[1], (ArrayList) objArr[2], null, (SendMessagesHelper.DelayedMessage) objArr[4], ((Boolean) objArr[5]).booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendErrorToObject$39(Object[] objArr) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) objArr[0], (MessageObject) objArr[1], (String) objArr[2], (SendMessagesHelper.DelayedMessage) objArr[3], ((Boolean) objArr[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) objArr[5], null, null, ((Boolean) objArr[6]).booleanValue());
    }

    /* JADX WARN: Removed duplicated region for block: B:227:0x047c  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x0488  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x0834  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x084c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x009f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a0  */
    /* JADX WARN: Type inference failed for: r15v0 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean onRequestComplete(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, boolean z, boolean z2) {
        String str3;
        String str4;
        boolean z3;
        ArrayList<Requester> arrayList;
        ArrayList<Requester> arrayList2;
        int i;
        TL_stories$StoryItem tL_stories$StoryItem;
        int i2;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        TLRPC$Document tLRPC$Document;
        TLRPC$Document tLRPC$Document2;
        TLRPC$Photo tLRPC$Photo;
        int i3;
        byte[] fileReference;
        byte[] fileReference2;
        int i4;
        TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr;
        byte[] fileReference3;
        TLRPC$MessageMedia tLRPC$MessageMedia2;
        byte[] fileReference4;
        boolean z4;
        TLRPC$InputFileLocation tLRPC$InputFileLocation;
        ArrayList<Requester> arrayList3;
        int i5;
        int i6;
        ArrayList<Requester> arrayList4;
        TLRPC$TL_error tLRPC$TL_error2 = tLRPC$TL_error;
        boolean z5 = tLObject instanceof TLRPC$TL_help_premiumPromo;
        if (z5) {
            str4 = "premium_promo";
        } else if (tLObject instanceof TLRPC$TL_account_wallPapers) {
            str4 = "wallpaper";
        } else if (tLObject instanceof TLRPC$TL_messages_savedGifs) {
            str4 = "gif";
        } else if (tLObject instanceof TLRPC$TL_messages_recentStickers) {
            str4 = "recent";
        } else if (!(tLObject instanceof TLRPC$TL_messages_favedStickers)) {
            str3 = str2;
            int i7 = 1;
            if (str2 != null || (arrayList3 = this.parentRequester.get(str2)) == null) {
                z3 = false;
            } else {
                int size = arrayList3.size();
                int i8 = 0;
                z3 = false;
                while (i8 < size) {
                    Requester requester = arrayList3.get(i8);
                    if (requester.completed) {
                        i5 = i8;
                        i6 = size;
                        arrayList4 = arrayList3;
                    } else {
                        i5 = i8;
                        i6 = size;
                        arrayList4 = arrayList3;
                        if (onRequestComplete(requester.locationKey, null, tLObject, tLRPC$TL_error, z && !z3, z2)) {
                            z3 = true;
                        }
                    }
                    i8 = i5 + 1;
                    arrayList3 = arrayList4;
                    size = i6;
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
            int i9 = 0;
            TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr2 = null;
            byte[] bArr = null;
            while (i9 < size2) {
                Requester requester2 = arrayList.get(i9);
                if (requester2.completed) {
                    arrayList2 = arrayList;
                    i = size2;
                    i2 = i9;
                } else {
                    if (tLRPC$TL_error2 != null && BuildVars.LOGS_ENABLED && requester2.args.length > i7 && (requester2.args[i7] instanceof FileLoadOperation)) {
                        FileLog.e("debug_loading: " + ((FileLoadOperation) requester2.args[i7]).getCacheFileFinal().getName() + " can't update file reference: " + tLRPC$TL_error2.code + " " + tLRPC$TL_error2.text);
                    }
                    if ((requester2.location instanceof TLRPC$TL_inputFileLocation) || (requester2.location instanceof TLRPC$TL_inputPeerPhotoFileLocation)) {
                        tLRPC$InputFileLocationArr2 = new TLRPC$InputFileLocation[i7];
                        zArr = new boolean[i7];
                    }
                    boolean[] zArr2 = zArr;
                    requester2.completed = i7;
                    if (tLObject instanceof StoriesController.BotPreview) {
                        TLRPC$MessageMedia tLRPC$MessageMedia3 = ((StoriesController.BotPreview) tLObject).media;
                        TLRPC$Document tLRPC$Document3 = tLRPC$MessageMedia3.document;
                        if (tLRPC$Document3 != null) {
                            bArr = getFileReference(tLRPC$Document3, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                        } else {
                            TLRPC$Photo tLRPC$Photo2 = tLRPC$MessageMedia3.photo;
                            if (tLRPC$Photo2 != null) {
                                bArr = getFileReference(tLRPC$Photo2, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                            }
                        }
                        arrayList2 = arrayList;
                        i = size2;
                    } else if (tLObject instanceof TLRPC$messages_Messages) {
                        TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                        if (!tLRPC$messages_Messages.messages.isEmpty()) {
                            int size3 = tLRPC$messages_Messages.messages.size();
                            int i10 = 0;
                            while (true) {
                                if (i10 >= size3) {
                                    arrayList2 = arrayList;
                                    i = size2;
                                    break;
                                }
                                TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i10);
                                arrayList2 = arrayList;
                                TLRPC$MessageMedia tLRPC$MessageMedia4 = tLRPC$Message.media;
                                i = size2;
                                if (tLRPC$MessageMedia4 instanceof TLRPC$TL_messageMediaPaidMedia) {
                                    TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia4;
                                    byte[] bArr2 = bArr;
                                    int i11 = 0;
                                    while (i11 < tLRPC$TL_messageMediaPaidMedia.extended_media.size()) {
                                        TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i11);
                                        TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia2 = tLRPC$TL_messageMediaPaidMedia;
                                        if ((tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) && (tLRPC$MessageMedia2 = ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media) != null) {
                                            TLRPC$Document tLRPC$Document4 = tLRPC$MessageMedia2.document;
                                            if (tLRPC$Document4 != null) {
                                                fileReference4 = getFileReference(tLRPC$Document4, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                            } else {
                                                TLRPC$TL_game tLRPC$TL_game = tLRPC$MessageMedia2.game;
                                                if (tLRPC$TL_game != null) {
                                                    byte[] fileReference5 = getFileReference(tLRPC$TL_game.document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    if (fileReference5 == null) {
                                                        fileReference4 = getFileReference(tLRPC$MessageMedia2.game.photo, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    } else {
                                                        bArr2 = fileReference5;
                                                    }
                                                } else {
                                                    TLRPC$Photo tLRPC$Photo3 = tLRPC$MessageMedia2.photo;
                                                    if (tLRPC$Photo3 != null) {
                                                        fileReference4 = getFileReference(tLRPC$Photo3, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    } else {
                                                        TLRPC$WebPage tLRPC$WebPage = tLRPC$MessageMedia2.webpage;
                                                        if (tLRPC$WebPage != null) {
                                                            fileReference4 = getFileReference(tLRPC$WebPage, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                        }
                                                    }
                                                }
                                            }
                                            bArr2 = fileReference4;
                                        }
                                        if (bArr2 != null) {
                                            break;
                                        }
                                        i11++;
                                        tLRPC$TL_messageMediaPaidMedia = tLRPC$TL_messageMediaPaidMedia2;
                                    }
                                    bArr = bArr2;
                                } else if (tLRPC$MessageMedia4 != null) {
                                    TLRPC$Document tLRPC$Document5 = tLRPC$MessageMedia4.document;
                                    if (tLRPC$Document5 != null) {
                                        fileReference3 = getFileReference(tLRPC$Document5, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                    } else {
                                        TLRPC$TL_game tLRPC$TL_game2 = tLRPC$MessageMedia4.game;
                                        if (tLRPC$TL_game2 != null) {
                                            fileReference3 = getFileReference(tLRPC$TL_game2.document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                            if (fileReference3 == null) {
                                                fileReference3 = getFileReference(tLRPC$Message.media.game.photo, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                            }
                                        } else {
                                            TLRPC$Photo tLRPC$Photo4 = tLRPC$MessageMedia4.photo;
                                            if (tLRPC$Photo4 != null) {
                                                fileReference3 = getFileReference(tLRPC$Photo4, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                            } else {
                                                TLRPC$WebPage tLRPC$WebPage2 = tLRPC$MessageMedia4.webpage;
                                                if (tLRPC$WebPage2 != null) {
                                                    fileReference3 = getFileReference(tLRPC$WebPage2, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                }
                                            }
                                        }
                                    }
                                    bArr = fileReference3;
                                } else {
                                    TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
                                    if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionChatEditPhoto) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSuggestProfilePhoto)) {
                                        fileReference3 = getFileReference(tLRPC$MessageAction.photo, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                        bArr = fileReference3;
                                    }
                                }
                                if (bArr == null) {
                                    i10++;
                                    arrayList = arrayList2;
                                    size2 = i;
                                } else if (z) {
                                    getMessagesStorage().replaceMessageIfExists(tLRPC$Message, tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, false);
                                }
                            }
                            if (bArr == null) {
                                getMessagesStorage().replaceMessageIfExists(tLRPC$messages_Messages.messages.get(0), tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true);
                                if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("file ref not found in messages, replacing message");
                                }
                            }
                        } else {
                            arrayList2 = arrayList;
                            i = size2;
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("empty messages, file ref not found");
                            }
                        }
                    } else {
                        arrayList2 = arrayList;
                        i = size2;
                        if (z5) {
                            Iterator<TLRPC$Document> it = ((TLRPC$TL_help_premiumPromo) tLObject).videos.iterator();
                            while (it.hasNext() && (bArr = getFileReference(it.next(), requester2.location, zArr2, tLRPC$InputFileLocationArr2)) == null) {
                            }
                        } else if (tLObject instanceof TLRPC$TL_messages_availableReactions) {
                            TLRPC$TL_messages_availableReactions tLRPC$TL_messages_availableReactions = (TLRPC$TL_messages_availableReactions) tLObject;
                            getMediaDataController().processLoadedReactions(tLRPC$TL_messages_availableReactions.reactions, tLRPC$TL_messages_availableReactions.hash, (int) (System.currentTimeMillis() / 1000), false);
                            Iterator<TLRPC$TL_availableReaction> it2 = tLRPC$TL_messages_availableReactions.reactions.iterator();
                            while (it2.hasNext()) {
                                TLRPC$TL_availableReaction next = it2.next();
                                bArr = getFileReference(next.static_icon, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                                bArr = getFileReference(next.appear_animation, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                                bArr = getFileReference(next.select_animation, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                                bArr = getFileReference(next.activate_animation, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                                bArr = getFileReference(next.effect_animation, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                                bArr = getFileReference(next.around_animation, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                                bArr = getFileReference(next.center_icon, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    break;
                                }
                            }
                        } else if (tLObject instanceof TLRPC$TL_users_userFull) {
                            TLRPC$TL_users_userFull tLRPC$TL_users_userFull = (TLRPC$TL_users_userFull) tLObject;
                            getMessagesController().putUsers(tLRPC$TL_users_userFull.users, false);
                            getMessagesController().putChats(tLRPC$TL_users_userFull.chats, false);
                            TLRPC$UserFull tLRPC$UserFull = tLRPC$TL_users_userFull.full_user;
                            TL_bots$BotInfo tL_bots$BotInfo = tLRPC$UserFull.bot_info;
                            if (tL_bots$BotInfo != null) {
                                getMessagesStorage().updateUserInfo(tLRPC$UserFull, true);
                                bArr = getFileReference(tL_bots$BotInfo.description_document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                if (bArr != null) {
                                    zArr = zArr2;
                                    i2 = i9;
                                } else {
                                    bArr = getFileReference(tL_bots$BotInfo.description_photo, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                }
                            }
                        } else {
                            if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
                                TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = ((TLRPC$TL_attachMenuBotsBot) tLObject).bot;
                                Iterator<TLRPC$TL_attachMenuBotIcon> it3 = tLRPC$TL_attachMenuBot.icons.iterator();
                                while (it3.hasNext() && (bArr = getFileReference(it3.next().icon, requester2.location, zArr2, tLRPC$InputFileLocationArr2)) == null) {
                                }
                                if (z) {
                                    TLRPC$TL_attachMenuBots attachMenuBots = getMediaDataController().getAttachMenuBots();
                                    ArrayList<TLRPC$TL_attachMenuBot> arrayList5 = new ArrayList<>(attachMenuBots.bots);
                                    int i12 = 0;
                                    while (true) {
                                        if (i12 >= arrayList5.size()) {
                                            i4 = i9;
                                            tLRPC$InputFileLocationArr = tLRPC$InputFileLocationArr2;
                                            break;
                                        }
                                        i4 = i9;
                                        tLRPC$InputFileLocationArr = tLRPC$InputFileLocationArr2;
                                        if (arrayList5.get(i12).bot_id == tLRPC$TL_attachMenuBot.bot_id) {
                                            arrayList5.set(i12, tLRPC$TL_attachMenuBot);
                                            break;
                                        }
                                        i12++;
                                        i9 = i4;
                                        tLRPC$InputFileLocationArr2 = tLRPC$InputFileLocationArr;
                                    }
                                    attachMenuBots.bots = arrayList5;
                                    getMediaDataController().processLoadedMenuBots(attachMenuBots, attachMenuBots.hash, (int) (System.currentTimeMillis() / 1000), false);
                                } else {
                                    i4 = i9;
                                    tLRPC$InputFileLocationArr = tLRPC$InputFileLocationArr2;
                                }
                                i2 = i4;
                                tLRPC$InputFileLocationArr2 = tLRPC$InputFileLocationArr;
                            } else {
                                int i13 = i9;
                                TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr3 = tLRPC$InputFileLocationArr2;
                                if (tLObject instanceof TLRPC$TL_help_appUpdate) {
                                    TLRPC$TL_help_appUpdate tLRPC$TL_help_appUpdate = (TLRPC$TL_help_appUpdate) tLObject;
                                    try {
                                        SharedConfig.pendingAppUpdate = tLRPC$TL_help_appUpdate;
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
                                        TLRPC$Document tLRPC$Document6 = tLRPC$TL_help_appUpdate.document;
                                        if (tLRPC$Document6 != null) {
                                            bArr = tLRPC$Document6.file_reference;
                                            TLRPC$TL_inputDocumentFileLocation tLRPC$TL_inputDocumentFileLocation = new TLRPC$TL_inputDocumentFileLocation();
                                            TLRPC$Document tLRPC$Document7 = tLRPC$TL_help_appUpdate.document;
                                            tLRPC$TL_inputDocumentFileLocation.id = tLRPC$Document7.id;
                                            tLRPC$TL_inputDocumentFileLocation.access_hash = tLRPC$Document7.access_hash;
                                            tLRPC$TL_inputDocumentFileLocation.file_reference = tLRPC$Document7.file_reference;
                                            tLRPC$TL_inputDocumentFileLocation.thumb_size = "";
                                            tLRPC$InputFileLocationArr2 = new TLRPC$InputFileLocation[1];
                                            try {
                                                tLRPC$InputFileLocationArr2[0] = tLRPC$TL_inputDocumentFileLocation;
                                            } catch (Exception e3) {
                                                e = e3;
                                                FileLog.e(e);
                                                bArr = null;
                                                if (bArr == null) {
                                                }
                                                if (bArr == null) {
                                                }
                                                i2 = i13;
                                                if (bArr != null) {
                                                }
                                                zArr = zArr2;
                                                tLRPC$TL_error2 = tLRPC$TL_error;
                                                i9 = i2 + 1;
                                                arrayList = arrayList2;
                                                size2 = i;
                                                i7 = 1;
                                            }
                                        } else {
                                            tLRPC$InputFileLocationArr2 = tLRPC$InputFileLocationArr3;
                                        }
                                    } catch (Exception e4) {
                                        e = e4;
                                        tLRPC$InputFileLocationArr2 = tLRPC$InputFileLocationArr3;
                                    }
                                    if (bArr == null) {
                                        bArr = getFileReference(tLRPC$TL_help_appUpdate.document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                    }
                                    if (bArr == null) {
                                        bArr = getFileReference(tLRPC$TL_help_appUpdate.sticker, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                    }
                                } else {
                                    if (tLObject instanceof TLRPC$TL_messages_webPage) {
                                        TLRPC$TL_messages_webPage tLRPC$TL_messages_webPage = (TLRPC$TL_messages_webPage) tLObject;
                                        getMessagesController().putChats(tLRPC$TL_messages_webPage.chats, false);
                                        getMessagesController().putUsers(tLRPC$TL_messages_webPage.users, false);
                                        tLRPC$InputFileLocationArr2 = tLRPC$InputFileLocationArr3;
                                        fileReference2 = getFileReference(tLRPC$TL_messages_webPage.webpage, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                    } else {
                                        tLRPC$InputFileLocationArr2 = tLRPC$InputFileLocationArr3;
                                        if (tLObject instanceof TLRPC$WebPage) {
                                            fileReference2 = getFileReference((TLRPC$WebPage) tLObject, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                        } else if (tLObject instanceof TLRPC$TL_account_wallPapers) {
                                            TLRPC$TL_account_wallPapers tLRPC$TL_account_wallPapers = (TLRPC$TL_account_wallPapers) tLObject;
                                            int size4 = tLRPC$TL_account_wallPapers.wallpapers.size();
                                            for (int i14 = 0; i14 < size4; i14++) {
                                                bArr = getFileReference(tLRPC$TL_account_wallPapers.wallpapers.get(i14).document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                if (bArr != null) {
                                                    break;
                                                }
                                            }
                                            if (bArr != null && z) {
                                                getMessagesStorage().putWallpapers(tLRPC$TL_account_wallPapers.wallpapers, 1);
                                            }
                                        } else {
                                            if (tLObject instanceof TLRPC$TL_wallPaper) {
                                                TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLObject;
                                                fileReference = getFileReference(tLRPC$TL_wallPaper.document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                if (fileReference != null && z) {
                                                    ArrayList<TLRPC$WallPaper> arrayList6 = new ArrayList<>();
                                                    arrayList6.add(tLRPC$TL_wallPaper);
                                                    getMessagesStorage().putWallpapers(arrayList6, 0);
                                                }
                                            } else if (tLObject instanceof TLRPC$TL_theme) {
                                                final TLRPC$TL_theme tLRPC$TL_theme = (TLRPC$TL_theme) tLObject;
                                                fileReference = getFileReference(tLRPC$TL_theme.document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                if (fileReference != null && z) {
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda12
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            Theme.setThemeFileReference(TLRPC$TL_theme.this);
                                                        }
                                                    });
                                                }
                                            } else if (tLObject instanceof TLRPC$Vector) {
                                                TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
                                                if (!tLRPC$Vector.objects.isEmpty()) {
                                                    int size5 = tLRPC$Vector.objects.size();
                                                    int i15 = 0;
                                                    while (i15 < size5) {
                                                        Object obj = tLRPC$Vector.objects.get(i15);
                                                        if (obj instanceof TLRPC$User) {
                                                            final TLRPC$User tLRPC$User = (TLRPC$User) obj;
                                                            bArr = getFileReference(tLRPC$User, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                            if (!z || bArr == null) {
                                                                i3 = size5;
                                                            } else {
                                                                ArrayList arrayList7 = new ArrayList();
                                                                arrayList7.add(tLRPC$User);
                                                                i3 = size5;
                                                                getMessagesStorage().putUsersAndChats(arrayList7, null, true, true);
                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda10
                                                                    @Override // java.lang.Runnable
                                                                    public final void run() {
                                                                        FileRefController.this.lambda$onRequestComplete$41(tLRPC$User);
                                                                    }
                                                                });
                                                            }
                                                        } else {
                                                            i3 = size5;
                                                            if (obj instanceof TLRPC$Chat) {
                                                                final TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) obj;
                                                                byte[] fileReference6 = getFileReference(tLRPC$Chat, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                                if (z && fileReference6 != null) {
                                                                    ArrayList arrayList8 = new ArrayList();
                                                                    arrayList8.add(tLRPC$Chat);
                                                                    getMessagesStorage().putUsersAndChats(null, arrayList8, true, true);
                                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda3
                                                                        @Override // java.lang.Runnable
                                                                        public final void run() {
                                                                            FileRefController.this.lambda$onRequestComplete$42(tLRPC$Chat);
                                                                        }
                                                                    });
                                                                }
                                                                bArr = fileReference6;
                                                            }
                                                        }
                                                        if (bArr != null) {
                                                            break;
                                                        }
                                                        i15++;
                                                        size5 = i3;
                                                    }
                                                }
                                            } else if (tLObject instanceof TLRPC$TL_messages_chats) {
                                                TLRPC$TL_messages_chats tLRPC$TL_messages_chats = (TLRPC$TL_messages_chats) tLObject;
                                                if (!tLRPC$TL_messages_chats.chats.isEmpty()) {
                                                    int size6 = tLRPC$TL_messages_chats.chats.size();
                                                    int i16 = 0;
                                                    while (true) {
                                                        if (i16 >= size6) {
                                                            break;
                                                        }
                                                        final TLRPC$Chat tLRPC$Chat2 = tLRPC$TL_messages_chats.chats.get(i16);
                                                        bArr = getFileReference(tLRPC$Chat2, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                        if (bArr == null) {
                                                            i16++;
                                                        } else if (z) {
                                                            ArrayList arrayList9 = new ArrayList();
                                                            arrayList9.add(tLRPC$Chat2);
                                                            getMessagesStorage().putUsersAndChats(null, arrayList9, true, true);
                                                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda4
                                                                @Override // java.lang.Runnable
                                                                public final void run() {
                                                                    FileRefController.this.lambda$onRequestComplete$43(tLRPC$Chat2);
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            } else if (tLObject instanceof TLRPC$TL_messages_savedGifs) {
                                                TLRPC$TL_messages_savedGifs tLRPC$TL_messages_savedGifs = (TLRPC$TL_messages_savedGifs) tLObject;
                                                int size7 = tLRPC$TL_messages_savedGifs.gifs.size();
                                                for (int i17 = 0; i17 < size7; i17++) {
                                                    bArr = getFileReference(tLRPC$TL_messages_savedGifs.gifs.get(i17), requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    if (bArr != null) {
                                                        break;
                                                    }
                                                }
                                                if (z) {
                                                    getMediaDataController().processLoadedRecentDocuments(0, tLRPC$TL_messages_savedGifs.gifs, true, 0, true);
                                                }
                                            } else if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
                                                final TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
                                                if (bArr == null) {
                                                    int size8 = tLRPC$TL_messages_stickerSet.documents.size();
                                                    for (int i18 = 0; i18 < size8; i18++) {
                                                        bArr = getFileReference(tLRPC$TL_messages_stickerSet.documents.get(i18), requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                        if (bArr != null) {
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (z) {
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda9
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            FileRefController.this.lambda$onRequestComplete$44(tLRPC$TL_messages_stickerSet);
                                                        }
                                                    });
                                                }
                                            } else if (tLObject instanceof TLRPC$TL_messages_recentStickers) {
                                                TLRPC$TL_messages_recentStickers tLRPC$TL_messages_recentStickers = (TLRPC$TL_messages_recentStickers) tLObject;
                                                int size9 = tLRPC$TL_messages_recentStickers.stickers.size();
                                                for (int i19 = 0; i19 < size9; i19++) {
                                                    bArr = getFileReference(tLRPC$TL_messages_recentStickers.stickers.get(i19), requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    if (bArr != null) {
                                                        break;
                                                    }
                                                }
                                                if (z) {
                                                    getMediaDataController().processLoadedRecentDocuments(0, tLRPC$TL_messages_recentStickers.stickers, false, 0, true);
                                                }
                                            } else if (tLObject instanceof TLRPC$TL_messages_favedStickers) {
                                                TLRPC$TL_messages_favedStickers tLRPC$TL_messages_favedStickers = (TLRPC$TL_messages_favedStickers) tLObject;
                                                int size10 = tLRPC$TL_messages_favedStickers.stickers.size();
                                                for (int i20 = 0; i20 < size10; i20++) {
                                                    bArr = getFileReference(tLRPC$TL_messages_favedStickers.stickers.get(i20), requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    if (bArr != null) {
                                                        break;
                                                    }
                                                }
                                                if (z) {
                                                    getMediaDataController().processLoadedRecentDocuments(2, tLRPC$TL_messages_favedStickers.stickers, false, 0, true);
                                                }
                                            } else if (tLObject instanceof TLRPC$photos_Photos) {
                                                TLRPC$photos_Photos tLRPC$photos_Photos = (TLRPC$photos_Photos) tLObject;
                                                int size11 = tLRPC$photos_Photos.photos.size();
                                                for (int i21 = 0; i21 < size11; i21++) {
                                                    bArr = getFileReference(tLRPC$photos_Photos.photos.get(i21), requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    if (bArr != null) {
                                                        break;
                                                    }
                                                }
                                            } else if (tLObject instanceof TL_stories$TL_stories_stories) {
                                                TL_stories$TL_stories_stories tL_stories$TL_stories_stories = (TL_stories$TL_stories_stories) tLObject;
                                                if (tL_stories$TL_stories_stories.stories.isEmpty() || (tLRPC$MessageMedia = (tL_stories$StoryItem = tL_stories$TL_stories_stories.stories.get(0)).media) == null) {
                                                    tL_stories$StoryItem = null;
                                                } else {
                                                    if (bArr == null && (tLRPC$Photo = tLRPC$MessageMedia.photo) != null) {
                                                        bArr = getFileReference(tLRPC$Photo, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    }
                                                    if (bArr == null && (tLRPC$Document2 = tL_stories$StoryItem.media.document) != null) {
                                                        bArr = getFileReference(tLRPC$Document2, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    }
                                                    if (bArr == null && (tLRPC$Document = tL_stories$StoryItem.media.alt_document) != null) {
                                                        bArr = getFileReference(tLRPC$Document, requester2.location, zArr2, tLRPC$InputFileLocationArr2);
                                                    }
                                                }
                                                if (requester2.args[1] instanceof FileLoadOperation) {
                                                    Object obj2 = ((FileLoadOperation) requester2.args[1]).parentObject;
                                                    if (obj2 instanceof TL_stories$StoryItem) {
                                                        TL_stories$StoryItem tL_stories$StoryItem2 = (TL_stories$StoryItem) obj2;
                                                        if (tL_stories$StoryItem == null) {
                                                            TL_stories$TL_updateStory tL_stories$TL_updateStory = new TL_stories$TL_updateStory();
                                                            i2 = i13;
                                                            tL_stories$TL_updateStory.peer = getMessagesController().getPeer(tL_stories$StoryItem2.dialogId);
                                                            TL_stories$TL_storyItemDeleted tL_stories$TL_storyItemDeleted = new TL_stories$TL_storyItemDeleted();
                                                            tL_stories$TL_updateStory.story = tL_stories$TL_storyItemDeleted;
                                                            tL_stories$TL_storyItemDeleted.id = tL_stories$StoryItem2.id;
                                                            ArrayList<TLRPC$Update> arrayList10 = new ArrayList<>();
                                                            arrayList10.add(tL_stories$TL_updateStory);
                                                            getMessagesController().processUpdateArray(arrayList10, null, null, false, 0);
                                                        } else {
                                                            i2 = i13;
                                                            TLRPC$User user = getMessagesController().getUser(Long.valueOf(tL_stories$StoryItem2.dialogId));
                                                            if (user != null && user.contact) {
                                                                MessagesController.getInstance(this.currentAccount).getStoriesController().getStoriesStorage().updateStoryItem(tL_stories$StoryItem2.dialogId, tL_stories$StoryItem);
                                                            }
                                                        }
                                                        if (tL_stories$StoryItem != null && bArr == null) {
                                                            TL_stories$TL_updateStory tL_stories$TL_updateStory2 = new TL_stories$TL_updateStory();
                                                            tL_stories$TL_updateStory2.peer = MessagesController.getInstance(this.currentAccount).getPeer(tL_stories$StoryItem2.dialogId);
                                                            tL_stories$TL_updateStory2.story = tL_stories$StoryItem;
                                                            ArrayList<TLRPC$Update> arrayList11 = new ArrayList<>();
                                                            arrayList11.add(tL_stories$TL_updateStory2);
                                                            MessagesController.getInstance(this.currentAccount).processUpdateArray(arrayList11, null, null, false, 0);
                                                        }
                                                    }
                                                }
                                            }
                                            bArr = fileReference;
                                        }
                                    }
                                    bArr = fileReference2;
                                }
                                i2 = i13;
                            }
                            if (bArr != null) {
                                if (tLRPC$InputFileLocationArr2 != null) {
                                    tLRPC$InputFileLocation = tLRPC$InputFileLocationArr2[0];
                                    z4 = z2;
                                } else {
                                    z4 = z2;
                                    tLRPC$InputFileLocation = null;
                                }
                                if (onUpdateObjectReference(requester2, bArr, tLRPC$InputFileLocation, z4)) {
                                    zArr = zArr2;
                                    z3 = true;
                                }
                            } else {
                                sendErrorToObject(requester2.args, 1);
                            }
                            zArr = zArr2;
                        }
                    }
                    i2 = i9;
                    if (bArr != null) {
                    }
                    zArr = zArr2;
                }
                tLRPC$TL_error2 = tLRPC$TL_error;
                i9 = i2 + 1;
                arrayList = arrayList2;
                size2 = i;
                i7 = 1;
            }
            this.locationRequester.remove(str);
            if (z3) {
                putReponseToCache(str, tLObject);
            }
            return z3;
        } else {
            str4 = "fav";
        }
        str3 = str4;
        int i72 = 1;
        if (str2 != null) {
        }
        z3 = false;
        arrayList = this.locationRequester.get(str);
        if (arrayList != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$41(TLRPC$User tLRPC$User) {
        getMessagesController().putUser(tLRPC$User, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$42(TLRPC$Chat tLRPC$Chat) {
        getMessagesController().putChat(tLRPC$Chat, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$43(TLRPC$Chat tLRPC$Chat) {
        getMessagesController().putChat(tLRPC$Chat, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestComplete$44(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        getMediaDataController().replaceStickerSet(tLRPC$TL_messages_stickerSet);
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

    private void putReponseToCache(String str, TLObject tLObject) {
        if (this.responseCache.get(str) == null) {
            CachedResult cachedResult = new CachedResult();
            cachedResult.response = tLObject;
            cachedResult.firstQueryTime = System.currentTimeMillis();
            this.responseCache.put(str, cachedResult);
        }
    }

    private byte[] getFileReference(TLRPC$Document tLRPC$Document, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr, TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr) {
        if (tLRPC$Document != null && tLRPC$InputFileLocation != null) {
            if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputDocumentFileLocation) {
                if (tLRPC$Document.id == tLRPC$InputFileLocation.id) {
                    return tLRPC$Document.file_reference;
                }
            } else {
                int size = tLRPC$Document.thumbs.size();
                for (int i = 0; i < size; i++) {
                    TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Document.thumbs.get(i);
                    byte[] fileReference = getFileReference(tLRPC$PhotoSize, tLRPC$InputFileLocation, zArr);
                    if (zArr != null && zArr[0]) {
                        tLRPC$InputFileLocationArr[0] = new TLRPC$TL_inputDocumentFileLocation();
                        tLRPC$InputFileLocationArr[0].id = tLRPC$Document.id;
                        tLRPC$InputFileLocationArr[0].volume_id = tLRPC$InputFileLocation.volume_id;
                        tLRPC$InputFileLocationArr[0].local_id = tLRPC$InputFileLocation.local_id;
                        tLRPC$InputFileLocationArr[0].access_hash = tLRPC$Document.access_hash;
                        TLRPC$InputFileLocation tLRPC$InputFileLocation2 = tLRPC$InputFileLocationArr[0];
                        byte[] bArr = tLRPC$Document.file_reference;
                        tLRPC$InputFileLocation2.file_reference = bArr;
                        tLRPC$InputFileLocationArr[0].thumb_size = tLRPC$PhotoSize.type;
                        return bArr;
                    } else if (fileReference != null) {
                        return fileReference;
                    }
                }
            }
        }
        return null;
    }

    private boolean getPeerReferenceReplacement(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean z, TLRPC$InputFileLocation tLRPC$InputFileLocation, TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr, boolean[] zArr) {
        TLRPC$InputPeer tLRPC$TL_inputPeerChat;
        TLRPC$InputPeer tLRPC$InputPeer;
        if (zArr == null || !zArr[0]) {
            return false;
        }
        TLRPC$TL_inputPeerPhotoFileLocation tLRPC$TL_inputPeerPhotoFileLocation = new TLRPC$TL_inputPeerPhotoFileLocation();
        long j = tLRPC$InputFileLocation.volume_id;
        tLRPC$TL_inputPeerPhotoFileLocation.id = j;
        tLRPC$TL_inputPeerPhotoFileLocation.volume_id = j;
        tLRPC$TL_inputPeerPhotoFileLocation.local_id = tLRPC$InputFileLocation.local_id;
        tLRPC$TL_inputPeerPhotoFileLocation.big = z;
        if (tLRPC$User != null) {
            tLRPC$InputPeer = new TLRPC$TL_inputPeerUser();
            tLRPC$InputPeer.user_id = tLRPC$User.id;
            tLRPC$InputPeer.access_hash = tLRPC$User.access_hash;
            tLRPC$TL_inputPeerPhotoFileLocation.photo_id = tLRPC$User.photo.photo_id;
        } else {
            if (ChatObject.isChannel(tLRPC$Chat)) {
                tLRPC$TL_inputPeerChat = new TLRPC$TL_inputPeerChannel();
                tLRPC$TL_inputPeerChat.channel_id = tLRPC$Chat.id;
                tLRPC$TL_inputPeerChat.access_hash = tLRPC$Chat.access_hash;
            } else {
                tLRPC$TL_inputPeerChat = new TLRPC$TL_inputPeerChat();
                tLRPC$TL_inputPeerChat.chat_id = tLRPC$Chat.id;
            }
            tLRPC$TL_inputPeerPhotoFileLocation.photo_id = tLRPC$Chat.photo.photo_id;
            tLRPC$InputPeer = tLRPC$TL_inputPeerChat;
        }
        tLRPC$TL_inputPeerPhotoFileLocation.peer = tLRPC$InputPeer;
        tLRPC$InputFileLocationArr[0] = tLRPC$TL_inputPeerPhotoFileLocation;
        return true;
    }

    private byte[] getFileReference(TLRPC$User tLRPC$User, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr, TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr) {
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        if (tLRPC$User == null || (tLRPC$UserProfilePhoto = tLRPC$User.photo) == null || !(tLRPC$InputFileLocation instanceof TLRPC$TL_inputFileLocation)) {
            return null;
        }
        byte[] fileReference = getFileReference(tLRPC$UserProfilePhoto.photo_small, tLRPC$InputFileLocation, zArr);
        if (getPeerReferenceReplacement(tLRPC$User, null, false, tLRPC$InputFileLocation, tLRPC$InputFileLocationArr, zArr)) {
            return new byte[0];
        }
        if (fileReference == null) {
            fileReference = getFileReference(tLRPC$User.photo.photo_big, tLRPC$InputFileLocation, zArr);
            if (getPeerReferenceReplacement(tLRPC$User, null, true, tLRPC$InputFileLocation, tLRPC$InputFileLocationArr, zArr)) {
                return new byte[0];
            }
        }
        return fileReference;
    }

    private byte[] getFileReference(TLRPC$Chat tLRPC$Chat, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr, TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr) {
        TLRPC$ChatPhoto tLRPC$ChatPhoto;
        byte[] bArr = null;
        if (tLRPC$Chat != null && (tLRPC$ChatPhoto = tLRPC$Chat.photo) != null && ((tLRPC$InputFileLocation instanceof TLRPC$TL_inputFileLocation) || (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation))) {
            if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPeerPhotoFileLocation) {
                zArr[0] = true;
                if (getPeerReferenceReplacement(null, tLRPC$Chat, false, tLRPC$InputFileLocation, tLRPC$InputFileLocationArr, zArr)) {
                    return new byte[0];
                }
                return null;
            }
            bArr = getFileReference(tLRPC$ChatPhoto.photo_small, tLRPC$InputFileLocation, zArr);
            if (getPeerReferenceReplacement(null, tLRPC$Chat, false, tLRPC$InputFileLocation, tLRPC$InputFileLocationArr, zArr)) {
                return new byte[0];
            }
            if (bArr == null) {
                bArr = getFileReference(tLRPC$Chat.photo.photo_big, tLRPC$InputFileLocation, zArr);
                if (getPeerReferenceReplacement(null, tLRPC$Chat, true, tLRPC$InputFileLocation, tLRPC$InputFileLocationArr, zArr)) {
                    return new byte[0];
                }
            }
        }
        return bArr;
    }

    private byte[] getFileReference(TLRPC$Photo tLRPC$Photo, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr, TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr) {
        if (tLRPC$Photo == null) {
            return null;
        }
        if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputPhotoFileLocation) {
            if (tLRPC$Photo.id == tLRPC$InputFileLocation.id) {
                return tLRPC$Photo.file_reference;
            }
            return null;
        }
        if (tLRPC$InputFileLocation instanceof TLRPC$TL_inputFileLocation) {
            int size = tLRPC$Photo.sizes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$PhotoSize tLRPC$PhotoSize = tLRPC$Photo.sizes.get(i);
                byte[] fileReference = getFileReference(tLRPC$PhotoSize, tLRPC$InputFileLocation, zArr);
                if (zArr != null && zArr[0]) {
                    tLRPC$InputFileLocationArr[0] = new TLRPC$TL_inputPhotoFileLocation();
                    tLRPC$InputFileLocationArr[0].id = tLRPC$Photo.id;
                    tLRPC$InputFileLocationArr[0].volume_id = tLRPC$InputFileLocation.volume_id;
                    tLRPC$InputFileLocationArr[0].local_id = tLRPC$InputFileLocation.local_id;
                    tLRPC$InputFileLocationArr[0].access_hash = tLRPC$Photo.access_hash;
                    TLRPC$InputFileLocation tLRPC$InputFileLocation2 = tLRPC$InputFileLocationArr[0];
                    byte[] bArr = tLRPC$Photo.file_reference;
                    tLRPC$InputFileLocation2.file_reference = bArr;
                    tLRPC$InputFileLocationArr[0].thumb_size = tLRPC$PhotoSize.type;
                    return bArr;
                } else if (fileReference != null) {
                    return fileReference;
                }
            }
        }
        return null;
    }

    private byte[] getFileReference(TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr) {
        if (tLRPC$PhotoSize == null || !(tLRPC$InputFileLocation instanceof TLRPC$TL_inputFileLocation)) {
            return null;
        }
        return getFileReference(tLRPC$PhotoSize.location, tLRPC$InputFileLocation, zArr);
    }

    private byte[] getFileReference(TLRPC$FileLocation tLRPC$FileLocation, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr) {
        if (tLRPC$FileLocation != null && (tLRPC$InputFileLocation instanceof TLRPC$TL_inputFileLocation) && tLRPC$FileLocation.local_id == tLRPC$InputFileLocation.local_id && tLRPC$FileLocation.volume_id == tLRPC$InputFileLocation.volume_id) {
            byte[] bArr = tLRPC$FileLocation.file_reference;
            if (bArr == null && zArr != null) {
                zArr[0] = true;
            }
            return bArr;
        }
        return null;
    }

    private byte[] getFileReference(TLRPC$WebPage tLRPC$WebPage, TLRPC$InputFileLocation tLRPC$InputFileLocation, boolean[] zArr, TLRPC$InputFileLocation[] tLRPC$InputFileLocationArr) {
        byte[] fileReference = getFileReference(tLRPC$WebPage.document, tLRPC$InputFileLocation, zArr, tLRPC$InputFileLocationArr);
        if (fileReference != null) {
            return fileReference;
        }
        byte[] fileReference2 = getFileReference(tLRPC$WebPage.photo, tLRPC$InputFileLocation, zArr, tLRPC$InputFileLocationArr);
        if (fileReference2 != null) {
            return fileReference2;
        }
        if (!tLRPC$WebPage.attributes.isEmpty()) {
            int size = tLRPC$WebPage.attributes.size();
            for (int i = 0; i < size; i++) {
                TLRPC$WebPageAttribute tLRPC$WebPageAttribute = tLRPC$WebPage.attributes.get(i);
                if (tLRPC$WebPageAttribute instanceof TLRPC$TL_webPageAttributeTheme) {
                    TLRPC$TL_webPageAttributeTheme tLRPC$TL_webPageAttributeTheme = (TLRPC$TL_webPageAttributeTheme) tLRPC$WebPageAttribute;
                    int size2 = tLRPC$TL_webPageAttributeTheme.documents.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        byte[] fileReference3 = getFileReference(tLRPC$TL_webPageAttributeTheme.documents.get(i2), tLRPC$InputFileLocation, zArr, tLRPC$InputFileLocationArr);
                        if (fileReference3 != null) {
                            return fileReference3;
                        }
                    }
                    continue;
                }
            }
        }
        TLRPC$Page tLRPC$Page = tLRPC$WebPage.cached_page;
        if (tLRPC$Page != null) {
            int size3 = tLRPC$Page.documents.size();
            for (int i3 = 0; i3 < size3; i3++) {
                byte[] fileReference4 = getFileReference(tLRPC$WebPage.cached_page.documents.get(i3), tLRPC$InputFileLocation, zArr, tLRPC$InputFileLocationArr);
                if (fileReference4 != null) {
                    return fileReference4;
                }
            }
            int size4 = tLRPC$WebPage.cached_page.photos.size();
            for (int i4 = 0; i4 < size4; i4++) {
                byte[] fileReference5 = getFileReference(tLRPC$WebPage.cached_page.photos.get(i4), tLRPC$InputFileLocation, zArr, tLRPC$InputFileLocationArr);
                if (fileReference5 != null) {
                    return fileReference5;
                }
            }
            return null;
        }
        return null;
    }

    public static boolean isFileRefError(String str) {
        return "FILEREF_EXPIRED".equals(str) || "FILE_REFERENCE_EXPIRED".equals(str) || "FILE_REFERENCE_EMPTY".equals(str) || (str != null && str.startsWith("FILE_REFERENCE_"));
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
}
