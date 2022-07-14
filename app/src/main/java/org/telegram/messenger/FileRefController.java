package org.telegram.messenger;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.telegram.messenger.FileLoadOperation;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes4.dex */
public class FileRefController extends BaseController {
    private static volatile FileRefController[] Instance = new FileRefController[4];
    private HashMap<String, ArrayList<Requester>> locationRequester = new HashMap<>();
    private HashMap<String, ArrayList<Requester>> parentRequester = new HashMap<>();
    private HashMap<String, CachedResult> responseCache = new HashMap<>();
    private HashMap<TLRPC.TL_messages_sendMultiMedia, Object[]> multiMediaCache = new HashMap<>();
    private long lastCleanupTime = SystemClock.elapsedRealtime();
    private ArrayList<Waiter> wallpaperWaiters = new ArrayList<>();
    private ArrayList<Waiter> savedGifsWaiters = new ArrayList<>();
    private ArrayList<Waiter> recentStickersWaiter = new ArrayList<>();
    private ArrayList<Waiter> favStickersWaiter = new ArrayList<>();

    /* loaded from: classes4.dex */
    public static class Requester {
        private Object[] args;
        private boolean completed;
        private TLRPC.InputFileLocation location;
        private String locationKey;

        private Requester() {
        }
    }

    /* loaded from: classes4.dex */
    public static class CachedResult {
        private long firstQueryTime;
        private long lastQueryTime;
        private TLObject response;

        private CachedResult() {
        }
    }

    /* loaded from: classes4.dex */
    public static class Waiter {
        private String locationKey;
        private String parentKey;

        public Waiter(String loc, String parent) {
            this.locationKey = loc;
            this.parentKey = parent;
        }
    }

    public static FileRefController getInstance(int num) {
        FileRefController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (FileRefController.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    FileRefController[] fileRefControllerArr = Instance;
                    FileRefController fileRefController = new FileRefController(num);
                    localInstance = fileRefController;
                    fileRefControllerArr[num] = fileRefController;
                }
            }
        }
        return localInstance;
    }

    public FileRefController(int instance) {
        super(instance);
    }

    public static String getKeyForParentObject(Object parentObject) {
        if (parentObject instanceof TLRPC.TL_availableReaction) {
            return "available_reaction_" + ((TLRPC.TL_availableReaction) parentObject).reaction;
        } else if (parentObject instanceof TLRPC.BotInfo) {
            TLRPC.BotInfo botInfo = (TLRPC.BotInfo) parentObject;
            return "bot_info_" + botInfo.user_id;
        } else if (parentObject instanceof TLRPC.TL_attachMenuBot) {
            TLRPC.TL_attachMenuBot bot = (TLRPC.TL_attachMenuBot) parentObject;
            long botId = bot.bot_id;
            return "attach_menu_bot_" + botId;
        } else if (parentObject instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) parentObject;
            long channelId = messageObject.getChannelId();
            return "message" + messageObject.getRealId() + "_" + channelId + "_" + messageObject.scheduled;
        } else if (parentObject instanceof TLRPC.Message) {
            TLRPC.Message message = (TLRPC.Message) parentObject;
            long channelId2 = message.peer_id != null ? message.peer_id.channel_id : 0L;
            return "message" + message.id + "_" + channelId2 + "_" + message.from_scheduled;
        } else if (parentObject instanceof TLRPC.WebPage) {
            TLRPC.WebPage webPage = (TLRPC.WebPage) parentObject;
            return "webpage" + webPage.id;
        } else if (parentObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) parentObject;
            return "user" + user.id;
        } else if (parentObject instanceof TLRPC.Chat) {
            TLRPC.Chat chat = (TLRPC.Chat) parentObject;
            return "chat" + chat.id;
        } else if (parentObject instanceof String) {
            String string = (String) parentObject;
            return "str" + string;
        } else if (parentObject instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet stickerSet = (TLRPC.TL_messages_stickerSet) parentObject;
            return "set" + stickerSet.set.id;
        } else if (parentObject instanceof TLRPC.StickerSetCovered) {
            TLRPC.StickerSetCovered stickerSet2 = (TLRPC.StickerSetCovered) parentObject;
            return "set" + stickerSet2.set.id;
        } else if (parentObject instanceof TLRPC.InputStickerSet) {
            TLRPC.InputStickerSet inputStickerSet = (TLRPC.InputStickerSet) parentObject;
            return "set" + inputStickerSet.id;
        } else if (parentObject instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper wallPaper = (TLRPC.TL_wallPaper) parentObject;
            return "wallpaper" + wallPaper.id;
        } else if (parentObject instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme theme = (TLRPC.TL_theme) parentObject;
            return "theme" + theme.id;
        } else if (parentObject == null) {
            return null;
        } else {
            return "" + parentObject;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x03e1  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x032e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void requestReference(Object parentObject, Object... args) {
        TLRPC.InputFileLocation location;
        String locationKey;
        Object parentObject2;
        String parentKey;
        ArrayList<Requester> arrayList;
        int added;
        String cacheKey;
        CachedResult cachedResult;
        TLRPC.InputFileLocation inputFileLocation;
        TLRPC.InputFileLocation inputFileLocation2;
        TLRPC.InputFileLocation inputFileLocation3;
        TLRPC.InputFileLocation inputFileLocation4;
        TLRPC.InputFileLocation location2;
        String locationKey2;
        TLRPC.InputFileLocation location3;
        String locationKey3;
        TLRPC.InputFileLocation location4;
        String locationKey4;
        TLRPC.InputFileLocation location5;
        String locationKey5;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start loading request reference for parent = " + parentObject + " args = " + args[0]);
        }
        if (args[0] instanceof TLRPC.TL_inputSingleMedia) {
            TLRPC.TL_inputSingleMedia req = (TLRPC.TL_inputSingleMedia) args[0];
            if (req.media instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument mediaDocument = (TLRPC.TL_inputMediaDocument) req.media;
                locationKey5 = "file_" + mediaDocument.id.id;
                location5 = new TLRPC.TL_inputDocumentFileLocation();
                location5.id = mediaDocument.id.id;
            } else if (req.media instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto mediaPhoto = (TLRPC.TL_inputMediaPhoto) req.media;
                location5 = new TLRPC.TL_inputPhotoFileLocation();
                location5.id = mediaPhoto.id.id;
                locationKey5 = "photo_" + mediaPhoto.id.id;
            } else {
                sendErrorToObject(args, 0);
                return;
            }
            location = location5;
            locationKey = locationKey5;
        } else if (args[0] instanceof TLRPC.TL_messages_sendMultiMedia) {
            TLRPC.TL_messages_sendMultiMedia req2 = (TLRPC.TL_messages_sendMultiMedia) args[0];
            ArrayList<Object> parentObjects = (ArrayList) parentObject;
            this.multiMediaCache.put(req2, args);
            int size = req2.multi_media.size();
            for (int a = 0; a < size; a++) {
                TLRPC.TL_inputSingleMedia media = req2.multi_media.get(a);
                Object parentObject3 = parentObjects.get(a);
                if (parentObject3 != null) {
                    requestReference(parentObject3, media, req2);
                }
            }
            return;
        } else if (args[0] instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_messages_sendMedia req3 = (TLRPC.TL_messages_sendMedia) args[0];
            if (req3.media instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument mediaDocument2 = (TLRPC.TL_inputMediaDocument) req3.media;
                locationKey4 = "file_" + mediaDocument2.id.id;
                location4 = new TLRPC.TL_inputDocumentFileLocation();
                location4.id = mediaDocument2.id.id;
            } else if (req3.media instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto mediaPhoto2 = (TLRPC.TL_inputMediaPhoto) req3.media;
                location4 = new TLRPC.TL_inputPhotoFileLocation();
                location4.id = mediaPhoto2.id.id;
                locationKey4 = "photo_" + mediaPhoto2.id.id;
            } else {
                sendErrorToObject(args, 0);
                return;
            }
            location = location4;
            locationKey = locationKey4;
        } else if (args[0] instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.TL_messages_editMessage req4 = (TLRPC.TL_messages_editMessage) args[0];
            if (req4.media instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument mediaDocument3 = (TLRPC.TL_inputMediaDocument) req4.media;
                locationKey3 = "file_" + mediaDocument3.id.id;
                location3 = new TLRPC.TL_inputDocumentFileLocation();
                location3.id = mediaDocument3.id.id;
            } else if (req4.media instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto mediaPhoto3 = (TLRPC.TL_inputMediaPhoto) req4.media;
                location3 = new TLRPC.TL_inputPhotoFileLocation();
                location3.id = mediaPhoto3.id.id;
                locationKey3 = "photo_" + mediaPhoto3.id.id;
            } else {
                sendErrorToObject(args, 0);
                return;
            }
            location = location3;
            locationKey = locationKey3;
        } else if (args[0] instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif req5 = (TLRPC.TL_messages_saveGif) args[0];
            TLRPC.InputFileLocation tL_inputDocumentFileLocation = new TLRPC.TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation.id = req5.id.id;
            location = tL_inputDocumentFileLocation;
            locationKey = "file_" + req5.id.id;
        } else if (args[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker req6 = (TLRPC.TL_messages_saveRecentSticker) args[0];
            TLRPC.InputFileLocation tL_inputDocumentFileLocation2 = new TLRPC.TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation2.id = req6.id.id;
            location = tL_inputDocumentFileLocation2;
            locationKey = "file_" + req6.id.id;
        } else if (args[0] instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker req7 = (TLRPC.TL_messages_faveSticker) args[0];
            TLRPC.InputFileLocation tL_inputDocumentFileLocation3 = new TLRPC.TL_inputDocumentFileLocation();
            tL_inputDocumentFileLocation3.id = req7.id.id;
            location = tL_inputDocumentFileLocation3;
            locationKey = "file_" + req7.id.id;
        } else if (args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.TL_messages_getAttachedStickers req8 = (TLRPC.TL_messages_getAttachedStickers) args[0];
            if (req8.media instanceof TLRPC.TL_inputStickeredMediaDocument) {
                TLRPC.TL_inputStickeredMediaDocument mediaDocument4 = (TLRPC.TL_inputStickeredMediaDocument) req8.media;
                locationKey2 = "file_" + mediaDocument4.id.id;
                location2 = new TLRPC.TL_inputDocumentFileLocation();
                location2.id = mediaDocument4.id.id;
            } else if (req8.media instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                TLRPC.TL_inputStickeredMediaPhoto mediaPhoto4 = (TLRPC.TL_inputStickeredMediaPhoto) req8.media;
                location2 = new TLRPC.TL_inputPhotoFileLocation();
                location2.id = mediaPhoto4.id.id;
                locationKey2 = "photo_" + mediaPhoto4.id.id;
            } else {
                sendErrorToObject(args, 0);
                return;
            }
            location = location2;
            locationKey = locationKey2;
        } else if (args[0] instanceof TLRPC.TL_inputFileLocation) {
            location = (TLRPC.TL_inputFileLocation) args[0];
            locationKey = "loc_" + inputFileLocation4.local_id + "_" + inputFileLocation4.volume_id;
        } else if (args[0] instanceof TLRPC.TL_inputDocumentFileLocation) {
            location = (TLRPC.TL_inputDocumentFileLocation) args[0];
            locationKey = "file_" + inputFileLocation3.id;
        } else if (args[0] instanceof TLRPC.TL_inputPhotoFileLocation) {
            location = (TLRPC.TL_inputPhotoFileLocation) args[0];
            locationKey = "photo_" + inputFileLocation2.id;
        } else if (args[0] instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
            location = (TLRPC.TL_inputPeerPhotoFileLocation) args[0];
            locationKey = "avatar_" + inputFileLocation.id;
        } else {
            sendErrorToObject(args, 0);
            return;
        }
        if (parentObject instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) parentObject;
            if (messageObject.getRealId() < 0 && messageObject.messageOwner.media.webpage != null) {
                parentObject2 = messageObject.messageOwner.media.webpage;
                parentKey = getKeyForParentObject(parentObject2);
                if (parentKey != null) {
                    sendErrorToObject(args, 0);
                    return;
                }
                Requester requester = new Requester();
                requester.args = args;
                requester.location = location;
                requester.locationKey = locationKey;
                int added2 = 0;
                ArrayList<Requester> arrayList2 = this.locationRequester.get(locationKey);
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList<>();
                    this.locationRequester.put(locationKey, arrayList2);
                    added2 = 0 + 1;
                }
                arrayList2.add(requester);
                ArrayList<Requester> arrayList3 = this.parentRequester.get(parentKey);
                if (arrayList3 != null) {
                    added = added2;
                    arrayList = arrayList3;
                } else {
                    ArrayList<Requester> arrayList4 = new ArrayList<>();
                    this.parentRequester.put(parentKey, arrayList4);
                    added = added2 + 1;
                    arrayList = arrayList4;
                }
                arrayList.add(requester);
                if (added != 2) {
                    return;
                }
                String cacheKey2 = locationKey;
                if (parentObject2 instanceof String) {
                    String string = (String) parentObject2;
                    if ("wallpaper".equals(string)) {
                        cacheKey = "wallpaper";
                    } else if (string.startsWith("gif")) {
                        cacheKey = "gif";
                    } else if ("recent".equals(string)) {
                        cacheKey = "recent";
                    } else if ("fav".equals(string)) {
                        cacheKey = "fav";
                    } else if ("update".equals(string)) {
                        cacheKey = "update";
                    }
                    cleanupCache();
                    cachedResult = getCachedResponse(cacheKey);
                    if (cachedResult == null) {
                        if (!onRequestComplete(locationKey, parentKey, cachedResult.response, false, true)) {
                            this.responseCache.remove(locationKey);
                        } else {
                            return;
                        }
                    } else {
                        CachedResult cachedResult2 = getCachedResponse(parentKey);
                        if (cachedResult2 != null) {
                            if (!onRequestComplete(locationKey, parentKey, cachedResult2.response, false, true)) {
                                this.responseCache.remove(parentKey);
                            } else {
                                return;
                            }
                        }
                    }
                    requestReferenceFromServer(parentObject2, locationKey, parentKey, args);
                    return;
                }
                cacheKey = cacheKey2;
                cleanupCache();
                cachedResult = getCachedResponse(cacheKey);
                if (cachedResult == null) {
                }
                requestReferenceFromServer(parentObject2, locationKey, parentKey, args);
                return;
            }
        }
        parentObject2 = parentObject;
        parentKey = getKeyForParentObject(parentObject2);
        if (parentKey != null) {
        }
    }

    private void broadcastWaitersData(ArrayList<Waiter> waiters, TLObject response) {
        int a = 0;
        int N = waiters.size();
        while (a < N) {
            Waiter waiter = waiters.get(a);
            onRequestComplete(waiter.locationKey, waiter.parentKey, response, a == N + (-1), false);
            a++;
        }
        waiters.clear();
    }

    private void requestReferenceFromServer(Object parentObject, final String locationKey, final String parentKey, Object[] args) {
        if (parentObject instanceof TLRPC.TL_availableReaction) {
            TLRPC.TL_messages_getAvailableReactions req = new TLRPC.TL_messages_getAvailableReactions();
            req.hash = 0;
            getConnectionsManager().sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda5
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m243xedaf0ec8(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof TLRPC.BotInfo) {
            TLRPC.BotInfo botInfo = (TLRPC.BotInfo) parentObject;
            TLRPC.TL_users_getFullUser req2 = new TLRPC.TL_users_getFullUser();
            req2.id = getMessagesController().getInputUser(botInfo.user_id);
            getConnectionsManager().sendRequest(req2, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda6
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m244xf51443e7(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof TLRPC.TL_attachMenuBot) {
            TLRPC.TL_attachMenuBot bot = (TLRPC.TL_attachMenuBot) parentObject;
            TLRPC.TL_messages_getAttachMenuBot req3 = new TLRPC.TL_messages_getAttachMenuBot();
            req3.bot = getMessagesController().getInputUser(bot.bot_id);
            getConnectionsManager().sendRequest(req3, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m255xfc797906(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof MessageObject) {
            MessageObject messageObject = (MessageObject) parentObject;
            long channelId = messageObject.getChannelId();
            if (messageObject.scheduled) {
                TLRPC.TL_messages_getScheduledMessages req4 = new TLRPC.TL_messages_getScheduledMessages();
                req4.peer = getMessagesController().getInputPeer(messageObject.getDialogId());
                req4.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(req4, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda19
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m260x3deae25(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            } else if (channelId != 0) {
                TLRPC.TL_channels_getMessages req5 = new TLRPC.TL_channels_getMessages();
                req5.channel = getMessagesController().getInputChannel(channelId);
                req5.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(req5, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda20
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m261xb43e344(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            } else {
                TLRPC.TL_messages_getMessages req6 = new TLRPC.TL_messages_getMessages();
                req6.id.add(Integer.valueOf(messageObject.getRealId()));
                getConnectionsManager().sendRequest(req6, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda21
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m262x12a91863(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            }
        } else if (parentObject instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper wallPaper = (TLRPC.TL_wallPaper) parentObject;
            TLRPC.TL_account_getWallPaper req7 = new TLRPC.TL_account_getWallPaper();
            TLRPC.TL_inputWallPaper inputWallPaper = new TLRPC.TL_inputWallPaper();
            inputWallPaper.id = wallPaper.id;
            inputWallPaper.access_hash = wallPaper.access_hash;
            req7.wallpaper = inputWallPaper;
            getConnectionsManager().sendRequest(req7, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda23
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m263x1a0e4d82(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme theme = (TLRPC.TL_theme) parentObject;
            TLRPC.TL_account_getTheme req8 = new TLRPC.TL_account_getTheme();
            TLRPC.TL_inputTheme inputTheme = new TLRPC.TL_inputTheme();
            inputTheme.id = theme.id;
            inputTheme.access_hash = theme.access_hash;
            req8.theme = inputTheme;
            req8.format = "android";
            getConnectionsManager().sendRequest(req8, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m264x217382a1(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof TLRPC.WebPage) {
            TLRPC.WebPage webPage = (TLRPC.WebPage) parentObject;
            TLRPC.TL_messages_getWebPage req9 = new TLRPC.TL_messages_getWebPage();
            req9.url = webPage.url;
            req9.hash = 0;
            getConnectionsManager().sendRequest(req9, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda25
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m265x28d8b7c0(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) parentObject;
            TLRPC.TL_users_getUsers req10 = new TLRPC.TL_users_getUsers();
            req10.id.add(getMessagesController().getInputUser(user));
            getConnectionsManager().sendRequest(req10, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda26
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    FileRefController.this.m266x303decdf(locationKey, parentKey, tLObject, tL_error);
                }
            });
        } else if (parentObject instanceof TLRPC.Chat) {
            TLRPC.Chat chat = (TLRPC.Chat) parentObject;
            if (chat instanceof TLRPC.TL_chat) {
                TLRPC.TL_messages_getChats req11 = new TLRPC.TL_messages_getChats();
                req11.id.add(Long.valueOf(chat.id));
                getConnectionsManager().sendRequest(req11, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda7
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m245x3c3ad4fd(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            } else if (chat instanceof TLRPC.TL_channel) {
                TLRPC.TL_channels_getChannels req12 = new TLRPC.TL_channels_getChannels();
                req12.id.add(MessagesController.getInputChannel(chat));
                getConnectionsManager().sendRequest(req12, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda8
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m246x43a00a1c(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            }
        } else if (!(parentObject instanceof String)) {
            if (parentObject instanceof TLRPC.TL_messages_stickerSet) {
                TLRPC.TL_messages_stickerSet stickerSet = (TLRPC.TL_messages_stickerSet) parentObject;
                TLRPC.TL_messages_getStickerSet req13 = new TLRPC.TL_messages_getStickerSet();
                req13.stickerset = new TLRPC.TL_inputStickerSetID();
                req13.stickerset.id = stickerSet.set.id;
                req13.stickerset.access_hash = stickerSet.set.access_hash;
                getConnectionsManager().sendRequest(req13, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda16
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m257x28e178dd(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            } else if (!(parentObject instanceof TLRPC.StickerSetCovered)) {
                if (parentObject instanceof TLRPC.InputStickerSet) {
                    TLRPC.TL_messages_getStickerSet req14 = new TLRPC.TL_messages_getStickerSet();
                    req14.stickerset = (TLRPC.InputStickerSet) parentObject;
                    getConnectionsManager().sendRequest(req14, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda18
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m259x37abe31b(locationKey, parentKey, tLObject, tL_error);
                        }
                    });
                    return;
                }
                sendErrorToObject(args, 0);
            } else {
                TLRPC.StickerSetCovered stickerSet2 = (TLRPC.StickerSetCovered) parentObject;
                TLRPC.TL_messages_getStickerSet req15 = new TLRPC.TL_messages_getStickerSet();
                req15.stickerset = new TLRPC.TL_inputStickerSetID();
                req15.stickerset.id = stickerSet2.set.id;
                req15.stickerset.access_hash = stickerSet2.set.access_hash;
                getConnectionsManager().sendRequest(req15, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda17
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m258x3046adfc(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            }
        } else {
            String string = (String) parentObject;
            if ("wallpaper".equals(string)) {
                if (this.wallpaperWaiters.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_account_getWallPapers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda1
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m247x4b053f3b(tLObject, tL_error);
                        }
                    });
                }
                this.wallpaperWaiters.add(new Waiter(locationKey, parentKey));
            } else if (string.startsWith("gif")) {
                if (this.savedGifsWaiters.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_messages_getSavedGifs(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda2
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m248x526a745a(tLObject, tL_error);
                        }
                    });
                }
                this.savedGifsWaiters.add(new Waiter(locationKey, parentKey));
            } else if ("recent".equals(string)) {
                if (this.recentStickersWaiter.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_messages_getRecentStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda3
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m249x59cfa979(tLObject, tL_error);
                        }
                    });
                }
                this.recentStickersWaiter.add(new Waiter(locationKey, parentKey));
            } else if ("fav".equals(string)) {
                if (this.favStickersWaiter.isEmpty()) {
                    getConnectionsManager().sendRequest(new TLRPC.TL_messages_getFavedStickers(), new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda4
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m250x6134de98(tLObject, tL_error);
                        }
                    });
                }
                this.favStickersWaiter.add(new Waiter(locationKey, parentKey));
            } else if ("update".equals(string)) {
                TLRPC.TL_help_getAppUpdate req16 = new TLRPC.TL_help_getAppUpdate();
                try {
                    req16.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
                } catch (Exception e) {
                }
                if (req16.source == null) {
                    req16.source = "";
                }
                getConnectionsManager().sendRequest(req16, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda9
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m251x689a13b7(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            } else if (string.startsWith("avatar_")) {
                long id = Utilities.parseLong(string).longValue();
                if (id > 0) {
                    TLRPC.TL_photos_getUserPhotos req17 = new TLRPC.TL_photos_getUserPhotos();
                    req17.limit = 80;
                    req17.offset = 0;
                    req17.max_id = 0L;
                    req17.user_id = getMessagesController().getInputUser(id);
                    getConnectionsManager().sendRequest(req17, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda10
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m252x6fff48d6(locationKey, parentKey, tLObject, tL_error);
                        }
                    });
                    return;
                }
                TLRPC.TL_messages_search req18 = new TLRPC.TL_messages_search();
                req18.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
                req18.limit = 80;
                req18.offset_id = 0;
                req18.q = "";
                req18.peer = getMessagesController().getInputPeer(id);
                getConnectionsManager().sendRequest(req18, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda12
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        FileRefController.this.m253x77647df5(locationKey, parentKey, tLObject, tL_error);
                    }
                });
            } else if (string.startsWith("sent_")) {
                String[] params = string.split("_");
                if (params.length == 3) {
                    long channelId2 = Utilities.parseLong(params[1]).longValue();
                    if (channelId2 != 0) {
                        TLRPC.TL_channels_getMessages req19 = new TLRPC.TL_channels_getMessages();
                        req19.channel = getMessagesController().getInputChannel(channelId2);
                        req19.id.add(Utilities.parseInt((CharSequence) params[2]));
                        getConnectionsManager().sendRequest(req19, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda13
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                FileRefController.this.m254x7ec9b314(locationKey, parentKey, tLObject, tL_error);
                            }
                        });
                        return;
                    }
                    TLRPC.TL_messages_getMessages req20 = new TLRPC.TL_messages_getMessages();
                    req20.id.add(Utilities.parseInt((CharSequence) params[2]));
                    getConnectionsManager().sendRequest(req20, new RequestDelegate() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda15
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            FileRefController.this.m256x217c43be(locationKey, parentKey, tLObject, tL_error);
                        }
                    });
                    return;
                }
                sendErrorToObject(args, 0);
            } else {
                sendErrorToObject(args, 0);
            }
        }
    }

    /* renamed from: lambda$requestReferenceFromServer$0$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m243xedaf0ec8(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$1$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m244xf51443e7(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$2$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m255xfc797906(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$3$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m260x3deae25(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$4$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m261xb43e344(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$5$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m262x12a91863(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$6$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m263x1a0e4d82(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$7$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m264x217382a1(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$8$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m265x28d8b7c0(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$9$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m266x303decdf(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$10$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m245x3c3ad4fd(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$11$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m246x43a00a1c(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$12$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m247x4b053f3b(TLObject response, TLRPC.TL_error error) {
        broadcastWaitersData(this.wallpaperWaiters, response);
    }

    /* renamed from: lambda$requestReferenceFromServer$13$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m248x526a745a(TLObject response, TLRPC.TL_error error) {
        broadcastWaitersData(this.savedGifsWaiters, response);
    }

    /* renamed from: lambda$requestReferenceFromServer$14$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m249x59cfa979(TLObject response, TLRPC.TL_error error) {
        broadcastWaitersData(this.recentStickersWaiter, response);
    }

    /* renamed from: lambda$requestReferenceFromServer$15$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m250x6134de98(TLObject response, TLRPC.TL_error error) {
        broadcastWaitersData(this.favStickersWaiter, response);
    }

    /* renamed from: lambda$requestReferenceFromServer$16$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m251x689a13b7(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$17$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m252x6fff48d6(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$18$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m253x77647df5(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$19$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m254x7ec9b314(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, false, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$20$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m256x217c43be(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, false, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$21$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m257x28e178dd(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$22$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m258x3046adfc(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    /* renamed from: lambda$requestReferenceFromServer$23$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m259x37abe31b(String locationKey, String parentKey, TLObject response, TLRPC.TL_error error) {
        onRequestComplete(locationKey, parentKey, response, true, false);
    }

    private boolean isSameReference(byte[] oldRef, byte[] newRef) {
        return Arrays.equals(oldRef, newRef);
    }

    private boolean onUpdateObjectReference(final Requester requester, byte[] file_reference, TLRPC.InputFileLocation locationReplacement, boolean fromCache) {
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("fileref updated for " + requester.args[0] + " " + requester.locationKey);
        }
        if (requester.args[0] instanceof TLRPC.TL_inputSingleMedia) {
            final TLRPC.TL_messages_sendMultiMedia multiMedia = (TLRPC.TL_messages_sendMultiMedia) requester.args[1];
            final Object[] objects = this.multiMediaCache.get(multiMedia);
            if (objects == null) {
                return true;
            }
            TLRPC.TL_inputSingleMedia req = (TLRPC.TL_inputSingleMedia) requester.args[0];
            if (req.media instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument mediaDocument = (TLRPC.TL_inputMediaDocument) req.media;
                if (fromCache && isSameReference(mediaDocument.id.file_reference, file_reference)) {
                    return false;
                }
                mediaDocument.id.file_reference = file_reference;
            } else if (req.media instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto mediaPhoto = (TLRPC.TL_inputMediaPhoto) req.media;
                if (fromCache && isSameReference(mediaPhoto.id.file_reference, file_reference)) {
                    return false;
                }
                mediaPhoto.id.file_reference = file_reference;
            }
            int index = multiMedia.multi_media.indexOf(req);
            if (index < 0) {
                return true;
            }
            ArrayList<Object> parentObjects = (ArrayList) objects[3];
            parentObjects.set(index, null);
            boolean done = true;
            for (int a = 0; a < parentObjects.size(); a++) {
                if (parentObjects.get(a) != null) {
                    done = false;
                }
            }
            if (done) {
                this.multiMediaCache.remove(multiMedia);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda31
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.m240xe1584ddb(multiMedia, objects);
                    }
                });
            }
        } else if (requester.args[0] instanceof TLRPC.TL_messages_sendMedia) {
            TLRPC.TL_messages_sendMedia req2 = (TLRPC.TL_messages_sendMedia) requester.args[0];
            if (req2.media instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument mediaDocument2 = (TLRPC.TL_inputMediaDocument) req2.media;
                if (fromCache && isSameReference(mediaDocument2.id.file_reference, file_reference)) {
                    return false;
                }
                mediaDocument2.id.file_reference = file_reference;
            } else if (req2.media instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto mediaPhoto2 = (TLRPC.TL_inputMediaPhoto) req2.media;
                if (fromCache && isSameReference(mediaPhoto2.id.file_reference, file_reference)) {
                    return false;
                }
                mediaPhoto2.id.file_reference = file_reference;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FileRefController.this.m241xe8bd82fa(requester);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_editMessage) {
            TLRPC.TL_messages_editMessage req3 = (TLRPC.TL_messages_editMessage) requester.args[0];
            if (req3.media instanceof TLRPC.TL_inputMediaDocument) {
                TLRPC.TL_inputMediaDocument mediaDocument3 = (TLRPC.TL_inputMediaDocument) req3.media;
                if (fromCache && isSameReference(mediaDocument3.id.file_reference, file_reference)) {
                    return false;
                }
                mediaDocument3.id.file_reference = file_reference;
            } else if (req3.media instanceof TLRPC.TL_inputMediaPhoto) {
                TLRPC.TL_inputMediaPhoto mediaPhoto3 = (TLRPC.TL_inputMediaPhoto) req3.media;
                if (fromCache && isSameReference(mediaPhoto3.id.file_reference, file_reference)) {
                    return false;
                }
                mediaPhoto3.id.file_reference = file_reference;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    FileRefController.this.m242xf022b819(requester);
                }
            });
        } else if (requester.args[0] instanceof TLRPC.TL_messages_saveGif) {
            TLRPC.TL_messages_saveGif req4 = (TLRPC.TL_messages_saveGif) requester.args[0];
            if (fromCache && isSameReference(req4.id.file_reference, file_reference)) {
                return false;
            }
            req4.id.file_reference = file_reference;
            getConnectionsManager().sendRequest(req4, FileRefController$$ExternalSyntheticLambda27.INSTANCE);
        } else if (requester.args[0] instanceof TLRPC.TL_messages_saveRecentSticker) {
            TLRPC.TL_messages_saveRecentSticker req5 = (TLRPC.TL_messages_saveRecentSticker) requester.args[0];
            if (fromCache && isSameReference(req5.id.file_reference, file_reference)) {
                return false;
            }
            req5.id.file_reference = file_reference;
            getConnectionsManager().sendRequest(req5, FileRefController$$ExternalSyntheticLambda28.INSTANCE);
        } else if (requester.args[0] instanceof TLRPC.TL_messages_faveSticker) {
            TLRPC.TL_messages_faveSticker req6 = (TLRPC.TL_messages_faveSticker) requester.args[0];
            if (fromCache && isSameReference(req6.id.file_reference, file_reference)) {
                return false;
            }
            req6.id.file_reference = file_reference;
            getConnectionsManager().sendRequest(req6, FileRefController$$ExternalSyntheticLambda29.INSTANCE);
        } else if (requester.args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
            TLRPC.TL_messages_getAttachedStickers req7 = (TLRPC.TL_messages_getAttachedStickers) requester.args[0];
            if (req7.media instanceof TLRPC.TL_inputStickeredMediaDocument) {
                TLRPC.TL_inputStickeredMediaDocument mediaDocument4 = (TLRPC.TL_inputStickeredMediaDocument) req7.media;
                if (fromCache && isSameReference(mediaDocument4.id.file_reference, file_reference)) {
                    return false;
                }
                mediaDocument4.id.file_reference = file_reference;
            } else if (req7.media instanceof TLRPC.TL_inputStickeredMediaPhoto) {
                TLRPC.TL_inputStickeredMediaPhoto mediaPhoto4 = (TLRPC.TL_inputStickeredMediaPhoto) req7.media;
                if (fromCache && isSameReference(mediaPhoto4.id.file_reference, file_reference)) {
                    return false;
                }
                mediaPhoto4.id.file_reference = file_reference;
            }
            getConnectionsManager().sendRequest(req7, (RequestDelegate) requester.args[1]);
        } else if (requester.args[1] instanceof FileLoadOperation) {
            FileLoadOperation fileLoadOperation = (FileLoadOperation) requester.args[1];
            if (locationReplacement != null) {
                if (fromCache && isSameReference(fileLoadOperation.location.file_reference, locationReplacement.file_reference)) {
                    return false;
                }
                fileLoadOperation.location = locationReplacement;
            } else if (fromCache && isSameReference(requester.location.file_reference, file_reference)) {
                return false;
            } else {
                requester.location.file_reference = file_reference;
            }
            fileLoadOperation.requestingReference = false;
            fileLoadOperation.startDownloadRequest();
        }
        return true;
    }

    /* renamed from: lambda$onUpdateObjectReference$24$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m240xe1584ddb(TLRPC.TL_messages_sendMultiMedia multiMedia, Object[] objects) {
        getSendMessagesHelper().performSendMessageRequestMulti(multiMedia, (ArrayList) objects[1], (ArrayList) objects[2], null, (SendMessagesHelper.DelayedMessage) objects[4], ((Boolean) objects[5]).booleanValue());
    }

    /* renamed from: lambda$onUpdateObjectReference$25$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m241xe8bd82fa(Requester requester) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    /* renamed from: lambda$onUpdateObjectReference$26$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m242xf022b819(Requester requester) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) requester.args[0], (MessageObject) requester.args[1], (String) requester.args[2], (SendMessagesHelper.DelayedMessage) requester.args[3], ((Boolean) requester.args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) requester.args[5], null, null, ((Boolean) requester.args[6]).booleanValue());
    }

    public static /* synthetic */ void lambda$onUpdateObjectReference$27(TLObject response, TLRPC.TL_error error) {
    }

    public static /* synthetic */ void lambda$onUpdateObjectReference$28(TLObject response, TLRPC.TL_error error) {
    }

    public static /* synthetic */ void lambda$onUpdateObjectReference$29(TLObject response, TLRPC.TL_error error) {
    }

    private void sendErrorToObject(final Object[] args, int reason) {
        if (!(args[0] instanceof TLRPC.TL_inputSingleMedia)) {
            if ((args[0] instanceof TLRPC.TL_messages_sendMedia) || (args[0] instanceof TLRPC.TL_messages_editMessage)) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda35
                    @Override // java.lang.Runnable
                    public final void run() {
                        FileRefController.this.m268xabdc4175(args);
                    }
                });
                return;
            } else if (!(args[0] instanceof TLRPC.TL_messages_saveGif)) {
                if (!(args[0] instanceof TLRPC.TL_messages_saveRecentSticker)) {
                    if (!(args[0] instanceof TLRPC.TL_messages_faveSticker)) {
                        if (args[0] instanceof TLRPC.TL_messages_getAttachedStickers) {
                            getConnectionsManager().sendRequest((TLRPC.TL_messages_getAttachedStickers) args[0], (RequestDelegate) args[1]);
                            return;
                        } else if (reason == 0) {
                            TLRPC.TL_error error = new TLRPC.TL_error();
                            error.text = "not found parent object to request reference";
                            error.code = 400;
                            if (args[1] instanceof FileLoadOperation) {
                                FileLoadOperation fileLoadOperation = (FileLoadOperation) args[1];
                                fileLoadOperation.requestingReference = false;
                                fileLoadOperation.processRequestResult((FileLoadOperation.RequestInfo) args[2], error);
                                return;
                            }
                            return;
                        } else if (reason == 1 && (args[1] instanceof FileLoadOperation)) {
                            FileLoadOperation fileLoadOperation2 = (FileLoadOperation) args[1];
                            fileLoadOperation2.requestingReference = false;
                            fileLoadOperation2.onFail(false, 0);
                            return;
                        } else {
                            return;
                        }
                    }
                    TLRPC.TL_messages_faveSticker tL_messages_faveSticker = (TLRPC.TL_messages_faveSticker) args[0];
                    return;
                }
                TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = (TLRPC.TL_messages_saveRecentSticker) args[0];
                return;
            } else {
                TLRPC.TL_messages_saveGif tL_messages_saveGif = (TLRPC.TL_messages_saveGif) args[0];
                return;
            }
        }
        final TLRPC.TL_messages_sendMultiMedia req = (TLRPC.TL_messages_sendMultiMedia) args[1];
        final Object[] objects = this.multiMediaCache.get(req);
        if (objects != null) {
            this.multiMediaCache.remove(req);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    FileRefController.this.m267xa4770c56(req, objects);
                }
            });
        }
    }

    /* renamed from: lambda$sendErrorToObject$30$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m267xa4770c56(TLRPC.TL_messages_sendMultiMedia req, Object[] objects) {
        getSendMessagesHelper().performSendMessageRequestMulti(req, (ArrayList) objects[1], (ArrayList) objects[2], null, (SendMessagesHelper.DelayedMessage) objects[4], ((Boolean) objects[5]).booleanValue());
    }

    /* renamed from: lambda$sendErrorToObject$31$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m268xabdc4175(Object[] args) {
        getSendMessagesHelper().performSendMessageRequest((TLObject) args[0], (MessageObject) args[1], (String) args[2], (SendMessagesHelper.DelayedMessage) args[3], ((Boolean) args[4]).booleanValue(), (SendMessagesHelper.DelayedMessage) args[5], null, null, ((Boolean) args[6]).booleanValue());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0165 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0183 A[LOOP:2: B:55:0x00d9->B:82:0x0183, LOOP_END] */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r12v4 */
    /* JADX WARN: Type inference failed for: r12v41 */
    /* JADX WARN: Type inference failed for: r12v56 */
    /* JADX WARN: Type inference failed for: r12v6 */
    /* JADX WARN: Type inference failed for: r12v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean onRequestComplete(String locationKey, String parentKey, TLObject response, boolean cache, boolean fromCache) {
        String cacheKey;
        int N;
        ArrayList<Requester> arrayList;
        boolean found;
        TLRPC.InputFileLocation[] locationReplacement;
        boolean[] needReplacement;
        TLRPC.InputFileLocation inputFileLocation;
        byte[] result;
        int size10;
        TLRPC.Vector vector;
        byte[] result2;
        byte[] result3;
        boolean[] needReplacement2;
        TLRPC.InputFileLocation[] locationReplacement2;
        byte[] result4;
        byte[] result5;
        byte[] result6;
        ArrayList<Requester> arrayList2;
        int q;
        boolean found2 = false;
        if (response instanceof TLRPC.TL_account_wallPapers) {
            cacheKey = "wallpaper";
        } else if (response instanceof TLRPC.TL_messages_savedGifs) {
            cacheKey = "gif";
        } else if (response instanceof TLRPC.TL_messages_recentStickers) {
            cacheKey = "recent";
        } else if (!(response instanceof TLRPC.TL_messages_favedStickers)) {
            cacheKey = parentKey;
        } else {
            cacheKey = "fav";
        }
        int i = 1;
        if (parentKey != null && (arrayList2 = this.parentRequester.get(parentKey)) != null) {
            int N2 = arrayList2.size();
            boolean found3 = false;
            int q2 = 0;
            while (q2 < N2) {
                Requester requester = arrayList2.get(q2);
                if (requester.completed) {
                    q = q2;
                } else {
                    q = q2;
                    if (onRequestComplete(requester.locationKey, null, response, cache && !found3, fromCache)) {
                        found3 = true;
                    }
                }
                q2 = q + 1;
            }
            if (found3) {
                putReponseToCache(cacheKey, response);
            }
            this.parentRequester.remove(parentKey);
            found2 = found3;
        }
        byte[] result7 = null;
        TLRPC.InputFileLocation[] locationReplacement3 = null;
        boolean[] needReplacement3 = null;
        ArrayList<Requester> arrayList3 = this.locationRequester.get(locationKey);
        if (arrayList3 == null) {
            return found2;
        }
        int q3 = 0;
        int N3 = arrayList3.size();
        while (q3 < N3) {
            Requester requester2 = arrayList3.get(q3);
            if (!requester2.completed) {
                if ((requester2.location instanceof TLRPC.TL_inputFileLocation) || (requester2.location instanceof TLRPC.TL_inputPeerPhotoFileLocation)) {
                    locationReplacement3 = new TLRPC.InputFileLocation[i];
                    needReplacement3 = new boolean[i];
                }
                requester2.completed = i;
                if (response instanceof TLRPC.messages_Messages) {
                    TLRPC.messages_Messages res = (TLRPC.messages_Messages) response;
                    if (res.messages.isEmpty()) {
                        arrayList = arrayList3;
                        N = N3;
                    } else {
                        int i2 = 0;
                        int size3 = res.messages.size();
                        while (true) {
                            if (i2 >= size3) {
                                arrayList = arrayList3;
                                N = N3;
                                break;
                            }
                            byte[] result8 = result7;
                            TLRPC.Message message = res.messages.get(i2);
                            arrayList = arrayList3;
                            if (message.media != null) {
                                if (message.media.document != null) {
                                    result5 = getFileReference(message.media.document, requester2.location, needReplacement3, locationReplacement3);
                                } else if (message.media.game != null) {
                                    result5 = getFileReference(message.media.game.document, requester2.location, needReplacement3, locationReplacement3);
                                    if (result5 == null) {
                                        result5 = getFileReference(message.media.game.photo, requester2.location, needReplacement3, locationReplacement3);
                                    }
                                } else if (message.media.photo != null) {
                                    result5 = getFileReference(message.media.photo, requester2.location, needReplacement3, locationReplacement3);
                                } else {
                                    if (message.media.webpage != null) {
                                        result5 = getFileReference(message.media.webpage, requester2.location, needReplacement3, locationReplacement3);
                                    }
                                    result5 = result8;
                                }
                                if (result5 == null) {
                                    i2++;
                                    result7 = result5;
                                    arrayList3 = arrayList;
                                } else {
                                    if (!cache) {
                                        result6 = result5;
                                        N = N3;
                                    } else {
                                        result6 = result5;
                                        N = N3;
                                        getMessagesStorage().replaceMessageIfExists(message, res.users, res.chats, false);
                                    }
                                    result7 = result6;
                                }
                            } else {
                                if (message.action instanceof TLRPC.TL_messageActionChatEditPhoto) {
                                    result5 = getFileReference(message.action.photo, requester2.location, needReplacement3, locationReplacement3);
                                    if (result5 == null) {
                                    }
                                }
                                result5 = result8;
                                if (result5 == null) {
                                }
                            }
                        }
                        if (result7 == null) {
                            getMessagesStorage().replaceMessageIfExists(res.messages.get(0), res.users, res.chats, true);
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("file ref not found in messages, replacing message");
                            }
                        }
                    }
                    found = found2;
                    locationReplacement = locationReplacement3;
                    needReplacement = needReplacement3;
                    inputFileLocation = null;
                } else {
                    arrayList = arrayList3;
                    N = N3;
                    if (response instanceof TLRPC.TL_messages_availableReactions) {
                        TLRPC.TL_messages_availableReactions availableReactions = (TLRPC.TL_messages_availableReactions) response;
                        getMediaDataController().processLoadedReactions(availableReactions.reactions, availableReactions.hash, (int) (System.currentTimeMillis() / 1000), false);
                        Iterator<TLRPC.TL_availableReaction> it = availableReactions.reactions.iterator();
                        while (it.hasNext()) {
                            TLRPC.TL_availableReaction reaction = it.next();
                            result7 = getFileReference(reaction.static_icon, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                            result7 = getFileReference(reaction.appear_animation, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                            result7 = getFileReference(reaction.select_animation, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                            result7 = getFileReference(reaction.activate_animation, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                            result7 = getFileReference(reaction.effect_animation, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                            result7 = getFileReference(reaction.around_animation, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                            result7 = getFileReference(reaction.center_icon, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                        }
                        found = found2;
                        locationReplacement = locationReplacement3;
                        needReplacement = needReplacement3;
                        inputFileLocation = null;
                    } else if (response instanceof TLRPC.TL_users_userFull) {
                        TLRPC.TL_users_userFull usersFull = (TLRPC.TL_users_userFull) response;
                        getMessagesController().putUsers(usersFull.users, false);
                        getMessagesController().putChats(usersFull.chats, false);
                        TLRPC.UserFull userFull = usersFull.full_user;
                        TLRPC.BotInfo botInfo = userFull.bot_info;
                        if (botInfo != null) {
                            getMessagesStorage().updateUserInfo(userFull, true);
                            result7 = getFileReference(botInfo.description_document, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 == null) {
                                result7 = getFileReference(botInfo.description_photo, requester2.location, needReplacement3, locationReplacement3);
                            } else {
                                found = found2;
                                i = 1;
                            }
                        }
                        found = found2;
                        locationReplacement = locationReplacement3;
                        needReplacement = needReplacement3;
                        inputFileLocation = null;
                    } else if (response instanceof TLRPC.TL_attachMenuBotsBot) {
                        TLRPC.TL_attachMenuBot bot = ((TLRPC.TL_attachMenuBotsBot) response).bot;
                        Iterator<TLRPC.TL_attachMenuBotIcon> it2 = bot.icons.iterator();
                        while (it2.hasNext()) {
                            TLRPC.TL_attachMenuBotIcon icon = it2.next();
                            result7 = getFileReference(icon.icon, requester2.location, needReplacement3, locationReplacement3);
                            if (result7 != null) {
                                break;
                            }
                        }
                        if (!cache) {
                            found = found2;
                            result4 = result7;
                            locationReplacement2 = locationReplacement3;
                            needReplacement2 = needReplacement3;
                        } else {
                            TLRPC.TL_attachMenuBots bots = getMediaDataController().getAttachMenuBots();
                            ArrayList<TLRPC.TL_attachMenuBot> newBotsList = new ArrayList<>(bots.bots);
                            int i3 = 0;
                            while (true) {
                                if (i3 >= newBotsList.size()) {
                                    found = found2;
                                    result4 = result7;
                                    locationReplacement2 = locationReplacement3;
                                    needReplacement2 = needReplacement3;
                                    break;
                                }
                                TLRPC.TL_attachMenuBot wasBot = newBotsList.get(i3);
                                found = found2;
                                result4 = result7;
                                locationReplacement2 = locationReplacement3;
                                needReplacement2 = needReplacement3;
                                if (wasBot.bot_id != bot.bot_id) {
                                    i3++;
                                    found2 = found;
                                    result7 = result4;
                                    locationReplacement3 = locationReplacement2;
                                    needReplacement3 = needReplacement2;
                                } else {
                                    newBotsList.set(i3, bot);
                                    break;
                                }
                            }
                            bots.bots = newBotsList;
                            getMediaDataController().processLoadedMenuBots(bots, bots.hash, (int) (System.currentTimeMillis() / 1000), false);
                        }
                        result7 = result4;
                        locationReplacement = locationReplacement2;
                        needReplacement = needReplacement2;
                        inputFileLocation = null;
                    } else {
                        found = found2;
                        TLRPC.InputFileLocation[] locationReplacement4 = locationReplacement3;
                        boolean[] needReplacement4 = needReplacement3;
                        if (response instanceof TLRPC.TL_help_appUpdate) {
                            TLRPC.TL_help_appUpdate appUpdate = (TLRPC.TL_help_appUpdate) response;
                            locationReplacement = locationReplacement4;
                            needReplacement = needReplacement4;
                            result7 = getFileReference(appUpdate.document, requester2.location, needReplacement, locationReplacement);
                            if (result7 == null) {
                                result7 = getFileReference(appUpdate.sticker, requester2.location, needReplacement, locationReplacement);
                            }
                            inputFileLocation = null;
                        } else {
                            locationReplacement = locationReplacement4;
                            needReplacement = needReplacement4;
                            if (response instanceof TLRPC.WebPage) {
                                result7 = getFileReference((TLRPC.WebPage) response, requester2.location, needReplacement, locationReplacement);
                                inputFileLocation = null;
                            } else if (response instanceof TLRPC.TL_account_wallPapers) {
                                TLRPC.TL_account_wallPapers accountWallPapers = (TLRPC.TL_account_wallPapers) response;
                                int size102 = accountWallPapers.wallpapers.size();
                                for (int i4 = 0; i4 < size102; i4++) {
                                    result7 = getFileReference(accountWallPapers.wallpapers.get(i4).document, requester2.location, needReplacement, locationReplacement);
                                    if (result7 != null) {
                                        break;
                                    }
                                }
                                if (result7 != null && cache) {
                                    getMessagesStorage().putWallpapers(accountWallPapers.wallpapers, 1);
                                }
                                inputFileLocation = null;
                            } else if (response instanceof TLRPC.TL_wallPaper) {
                                TLRPC.TL_wallPaper wallPaper = (TLRPC.TL_wallPaper) response;
                                result7 = getFileReference(wallPaper.document, requester2.location, needReplacement, locationReplacement);
                                if (result7 != null && cache) {
                                    ArrayList<TLRPC.WallPaper> wallpapers = new ArrayList<>();
                                    wallpapers.add(wallPaper);
                                    getMessagesStorage().putWallpapers(wallpapers, 0);
                                }
                                inputFileLocation = null;
                            } else if (response instanceof TLRPC.TL_theme) {
                                final TLRPC.TL_theme theme = (TLRPC.TL_theme) response;
                                result7 = getFileReference(theme.document, requester2.location, needReplacement, locationReplacement);
                                if (result7 != null && cache) {
                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda36
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            Theme.setThemeFileReference(TLRPC.TL_theme.this);
                                        }
                                    });
                                }
                                inputFileLocation = null;
                            } else if (response instanceof TLRPC.Vector) {
                                TLRPC.Vector vector2 = (TLRPC.Vector) response;
                                if (!vector2.objects.isEmpty()) {
                                    int i5 = 0;
                                    int size103 = vector2.objects.size();
                                    while (i5 < size103) {
                                        Object object = vector2.objects.get(i5);
                                        if (object instanceof TLRPC.User) {
                                            final TLRPC.User user = (TLRPC.User) object;
                                            byte[] result9 = getFileReference(user, requester2.location, needReplacement, locationReplacement);
                                            if (!cache || result9 == null) {
                                                vector = vector2;
                                                result3 = result9;
                                                size10 = size103;
                                            } else {
                                                ArrayList<TLRPC.User> arrayList1 = new ArrayList<>();
                                                arrayList1.add(user);
                                                vector = vector2;
                                                result3 = result9;
                                                size10 = size103;
                                                getMessagesStorage().putUsersAndChats(arrayList1, null, true, true);
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda34
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        FileRefController.this.m236x97cc7922(user);
                                                    }
                                                });
                                            }
                                            result7 = result3;
                                        } else {
                                            vector = vector2;
                                            size10 = size103;
                                            if (object instanceof TLRPC.Chat) {
                                                final TLRPC.Chat chat = (TLRPC.Chat) object;
                                                byte[] result10 = getFileReference(chat, requester2.location, needReplacement, locationReplacement);
                                                if (!cache || result10 == null) {
                                                    result2 = result10;
                                                } else {
                                                    ArrayList<TLRPC.Chat> arrayList12 = new ArrayList<>();
                                                    arrayList12.add(chat);
                                                    result2 = result10;
                                                    getMessagesStorage().putUsersAndChats(null, arrayList12, true, true);
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda22
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            FileRefController.this.m237x9f31ae41(chat);
                                                        }
                                                    });
                                                }
                                                result7 = result2;
                                            }
                                        }
                                        if (result7 != null) {
                                            break;
                                        }
                                        i5++;
                                        vector2 = vector;
                                        size103 = size10;
                                    }
                                }
                                inputFileLocation = null;
                            } else if (response instanceof TLRPC.TL_messages_chats) {
                                TLRPC.TL_messages_chats res2 = (TLRPC.TL_messages_chats) response;
                                if (!res2.chats.isEmpty()) {
                                    int i6 = 0;
                                    int size104 = res2.chats.size();
                                    while (true) {
                                        if (i6 >= size104) {
                                            inputFileLocation = null;
                                            break;
                                        }
                                        final TLRPC.Chat chat2 = res2.chats.get(i6);
                                        result7 = getFileReference(chat2, requester2.location, needReplacement, locationReplacement);
                                        if (result7 == null) {
                                            i6++;
                                            res2 = res2;
                                        } else {
                                            if (cache) {
                                                ArrayList<TLRPC.Chat> arrayList13 = new ArrayList<>();
                                                arrayList13.add(chat2);
                                                result = result7;
                                                inputFileLocation = null;
                                                getMessagesStorage().putUsersAndChats(null, arrayList13, true, true);
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda30
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        FileRefController.this.m238xa696e360(chat2);
                                                    }
                                                });
                                            } else {
                                                result = result7;
                                                inputFileLocation = null;
                                            }
                                            result7 = result;
                                        }
                                    }
                                } else {
                                    inputFileLocation = null;
                                }
                            } else {
                                inputFileLocation = null;
                                if (response instanceof TLRPC.TL_messages_savedGifs) {
                                    TLRPC.TL_messages_savedGifs savedGifs = (TLRPC.TL_messages_savedGifs) response;
                                    int size2 = savedGifs.gifs.size();
                                    for (int b = 0; b < size2; b++) {
                                        result7 = getFileReference(savedGifs.gifs.get(b), requester2.location, needReplacement, locationReplacement);
                                        if (result7 != null) {
                                            break;
                                        }
                                    }
                                    if (cache) {
                                        getMediaDataController().processLoadedRecentDocuments(0, savedGifs.gifs, true, 0, true);
                                    }
                                } else if (response instanceof TLRPC.TL_messages_stickerSet) {
                                    final TLRPC.TL_messages_stickerSet stickerSet = (TLRPC.TL_messages_stickerSet) response;
                                    if (result7 == null) {
                                        int size22 = stickerSet.documents.size();
                                        for (int b2 = 0; b2 < size22; b2++) {
                                            result7 = getFileReference(stickerSet.documents.get(b2), requester2.location, needReplacement, locationReplacement);
                                            if (result7 != null) {
                                                break;
                                            }
                                        }
                                    }
                                    if (cache) {
                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.FileRefController$$ExternalSyntheticLambda33
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                FileRefController.this.m239xadfc187f(stickerSet);
                                            }
                                        });
                                    }
                                } else if (response instanceof TLRPC.TL_messages_recentStickers) {
                                    TLRPC.TL_messages_recentStickers recentStickers = (TLRPC.TL_messages_recentStickers) response;
                                    int size23 = recentStickers.stickers.size();
                                    for (int b3 = 0; b3 < size23; b3++) {
                                        result7 = getFileReference(recentStickers.stickers.get(b3), requester2.location, needReplacement, locationReplacement);
                                        if (result7 != null) {
                                            break;
                                        }
                                    }
                                    if (cache) {
                                        getMediaDataController().processLoadedRecentDocuments(0, recentStickers.stickers, false, 0, true);
                                    }
                                } else if (response instanceof TLRPC.TL_messages_favedStickers) {
                                    TLRPC.TL_messages_favedStickers favedStickers = (TLRPC.TL_messages_favedStickers) response;
                                    int size24 = favedStickers.stickers.size();
                                    for (int b4 = 0; b4 < size24; b4++) {
                                        result7 = getFileReference(favedStickers.stickers.get(b4), requester2.location, needReplacement, locationReplacement);
                                        if (result7 != null) {
                                            break;
                                        }
                                    }
                                    if (cache) {
                                        getMediaDataController().processLoadedRecentDocuments(2, favedStickers.stickers, false, 0, true);
                                    }
                                } else if (response instanceof TLRPC.photos_Photos) {
                                    TLRPC.photos_Photos res3 = (TLRPC.photos_Photos) response;
                                    int size = res3.photos.size();
                                    for (int b5 = 0; b5 < size; b5++) {
                                        result7 = getFileReference(res3.photos.get(b5), requester2.location, needReplacement, locationReplacement);
                                        if (result7 != null) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (result7 != null) {
                    if (!onUpdateObjectReference(requester2, result7, locationReplacement != null ? locationReplacement[0] : inputFileLocation, fromCache)) {
                        i = 1;
                    } else {
                        found = true;
                        needReplacement3 = needReplacement;
                        locationReplacement3 = locationReplacement;
                        i = 1;
                    }
                } else {
                    i = 1;
                    sendErrorToObject(requester2.args, 1);
                }
                needReplacement3 = needReplacement;
                locationReplacement3 = locationReplacement;
            } else {
                found = found2;
                arrayList = arrayList3;
                N = N3;
            }
            q3++;
            found2 = found;
            arrayList3 = arrayList;
            N3 = N;
            i = i;
        }
        boolean found4 = found2;
        this.locationRequester.remove(locationKey);
        if (found4) {
            putReponseToCache(locationKey, response);
        }
        return found4;
    }

    /* renamed from: lambda$onRequestComplete$33$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m236x97cc7922(TLRPC.User user) {
        getMessagesController().putUser(user, false);
    }

    /* renamed from: lambda$onRequestComplete$34$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m237x9f31ae41(TLRPC.Chat chat) {
        getMessagesController().putChat(chat, false);
    }

    /* renamed from: lambda$onRequestComplete$35$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m238xa696e360(TLRPC.Chat chat) {
        getMessagesController().putChat(chat, false);
    }

    /* renamed from: lambda$onRequestComplete$36$org-telegram-messenger-FileRefController */
    public /* synthetic */ void m239xadfc187f(TLRPC.TL_messages_stickerSet stickerSet) {
        getMediaDataController().replaceStickerSet(stickerSet);
    }

    private void cleanupCache() {
        if (Math.abs(SystemClock.elapsedRealtime() - this.lastCleanupTime) < 600000) {
            return;
        }
        this.lastCleanupTime = SystemClock.elapsedRealtime();
        ArrayList<String> keysToDelete = null;
        for (Map.Entry<String, CachedResult> entry : this.responseCache.entrySet()) {
            CachedResult cachedResult = entry.getValue();
            if (Math.abs(SystemClock.elapsedRealtime() - cachedResult.firstQueryTime) >= 600000) {
                if (keysToDelete == null) {
                    keysToDelete = new ArrayList<>();
                }
                keysToDelete.add(entry.getKey());
            }
        }
        if (keysToDelete != null) {
            int size = keysToDelete.size();
            for (int a = 0; a < size; a++) {
                this.responseCache.remove(keysToDelete.get(a));
            }
        }
    }

    private CachedResult getCachedResponse(String key) {
        CachedResult cachedResult = this.responseCache.get(key);
        if (cachedResult != null && Math.abs(SystemClock.elapsedRealtime() - cachedResult.firstQueryTime) >= 600000) {
            this.responseCache.remove(key);
            return null;
        }
        return cachedResult;
    }

    private void putReponseToCache(String key, TLObject response) {
        CachedResult cachedResult = this.responseCache.get(key);
        if (cachedResult == null) {
            cachedResult = new CachedResult();
            cachedResult.response = response;
            cachedResult.firstQueryTime = SystemClock.uptimeMillis();
            this.responseCache.put(key, cachedResult);
        }
        cachedResult.lastQueryTime = SystemClock.uptimeMillis();
    }

    private byte[] getFileReference(TLRPC.Document document, TLRPC.InputFileLocation location, boolean[] needReplacement, TLRPC.InputFileLocation[] replacement) {
        if (document == null || location == null) {
            return null;
        }
        if (location instanceof TLRPC.TL_inputDocumentFileLocation) {
            if (document.id == location.id) {
                return document.file_reference;
            }
        } else {
            int size = document.thumbs.size();
            for (int a = 0; a < size; a++) {
                TLRPC.PhotoSize photoSize = document.thumbs.get(a);
                byte[] result = getFileReference(photoSize, location, needReplacement);
                if (needReplacement != null && needReplacement[0]) {
                    replacement[0] = new TLRPC.TL_inputDocumentFileLocation();
                    replacement[0].id = document.id;
                    replacement[0].volume_id = location.volume_id;
                    replacement[0].local_id = location.local_id;
                    replacement[0].access_hash = document.access_hash;
                    replacement[0].file_reference = document.file_reference;
                    replacement[0].thumb_size = photoSize.type;
                    return document.file_reference;
                } else if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean getPeerReferenceReplacement(TLRPC.User user, TLRPC.Chat chat, boolean big, TLRPC.InputFileLocation location, TLRPC.InputFileLocation[] replacement, boolean[] needReplacement) {
        TLRPC.InputPeer peer;
        TLRPC.InputPeer inputPeer;
        if (needReplacement == null || !needReplacement[0]) {
            return false;
        }
        TLRPC.TL_inputPeerPhotoFileLocation inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
        inputPeerPhotoFileLocation.id = location.volume_id;
        inputPeerPhotoFileLocation.volume_id = location.volume_id;
        inputPeerPhotoFileLocation.local_id = location.local_id;
        inputPeerPhotoFileLocation.big = big;
        if (user != null) {
            TLRPC.TL_inputPeerUser inputPeerUser = new TLRPC.TL_inputPeerUser();
            inputPeerUser.user_id = user.id;
            inputPeerUser.access_hash = user.access_hash;
            inputPeerPhotoFileLocation.photo_id = user.photo.photo_id;
            peer = inputPeerUser;
        } else {
            if (ChatObject.isChannel(chat)) {
                TLRPC.TL_inputPeerChannel inputPeerChannel = new TLRPC.TL_inputPeerChannel();
                inputPeerChannel.channel_id = chat.id;
                inputPeerChannel.access_hash = chat.access_hash;
                inputPeer = inputPeerChannel;
            } else {
                TLRPC.TL_inputPeerChat inputPeerChat = new TLRPC.TL_inputPeerChat();
                inputPeerChat.chat_id = chat.id;
                inputPeer = inputPeerChat;
            }
            inputPeerPhotoFileLocation.photo_id = chat.photo.photo_id;
            peer = inputPeer;
        }
        inputPeerPhotoFileLocation.peer = peer;
        replacement[0] = inputPeerPhotoFileLocation;
        return true;
    }

    private byte[] getFileReference(TLRPC.User user, TLRPC.InputFileLocation location, boolean[] needReplacement, TLRPC.InputFileLocation[] replacement) {
        if (user == null || user.photo == null || !(location instanceof TLRPC.TL_inputFileLocation)) {
            return null;
        }
        byte[] result = getFileReference(user.photo.photo_small, location, needReplacement);
        if (getPeerReferenceReplacement(user, null, false, location, replacement, needReplacement)) {
            return new byte[0];
        }
        if (result == null) {
            result = getFileReference(user.photo.photo_big, location, needReplacement);
            if (getPeerReferenceReplacement(user, null, true, location, replacement, needReplacement)) {
                return new byte[0];
            }
        }
        return result;
    }

    private byte[] getFileReference(TLRPC.Chat chat, TLRPC.InputFileLocation location, boolean[] needReplacement, TLRPC.InputFileLocation[] replacement) {
        if (chat == null || chat.photo == null || (!(location instanceof TLRPC.TL_inputFileLocation) && !(location instanceof TLRPC.TL_inputPeerPhotoFileLocation))) {
            return null;
        }
        if (location instanceof TLRPC.TL_inputPeerPhotoFileLocation) {
            needReplacement[0] = true;
            if (!getPeerReferenceReplacement(null, chat, false, location, replacement, needReplacement)) {
                return null;
            }
            return new byte[0];
        }
        byte[] result = getFileReference(chat.photo.photo_small, location, needReplacement);
        if (getPeerReferenceReplacement(null, chat, false, location, replacement, needReplacement)) {
            return new byte[0];
        }
        if (result == null) {
            result = getFileReference(chat.photo.photo_big, location, needReplacement);
            if (getPeerReferenceReplacement(null, chat, true, location, replacement, needReplacement)) {
                return new byte[0];
            }
        }
        return result;
    }

    private byte[] getFileReference(TLRPC.Photo photo, TLRPC.InputFileLocation location, boolean[] needReplacement, TLRPC.InputFileLocation[] replacement) {
        if (photo == null) {
            return null;
        }
        if (location instanceof TLRPC.TL_inputPhotoFileLocation) {
            if (photo.id != location.id) {
                return null;
            }
            return photo.file_reference;
        }
        if (location instanceof TLRPC.TL_inputFileLocation) {
            int size = photo.sizes.size();
            for (int a = 0; a < size; a++) {
                TLRPC.PhotoSize photoSize = photo.sizes.get(a);
                byte[] result = getFileReference(photoSize, location, needReplacement);
                if (needReplacement != null && needReplacement[0]) {
                    replacement[0] = new TLRPC.TL_inputPhotoFileLocation();
                    replacement[0].id = photo.id;
                    replacement[0].volume_id = location.volume_id;
                    replacement[0].local_id = location.local_id;
                    replacement[0].access_hash = photo.access_hash;
                    replacement[0].file_reference = photo.file_reference;
                    replacement[0].thumb_size = photoSize.type;
                    return photo.file_reference;
                } else if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private byte[] getFileReference(TLRPC.PhotoSize photoSize, TLRPC.InputFileLocation location, boolean[] needReplacement) {
        if (photoSize == null || !(location instanceof TLRPC.TL_inputFileLocation)) {
            return null;
        }
        return getFileReference(photoSize.location, location, needReplacement);
    }

    private byte[] getFileReference(TLRPC.FileLocation fileLocation, TLRPC.InputFileLocation location, boolean[] needReplacement) {
        if (fileLocation == null || !(location instanceof TLRPC.TL_inputFileLocation) || fileLocation.local_id != location.local_id || fileLocation.volume_id != location.volume_id) {
            return null;
        }
        if (fileLocation.file_reference == null && needReplacement != null) {
            needReplacement[0] = true;
        }
        return fileLocation.file_reference;
    }

    private byte[] getFileReference(TLRPC.WebPage webpage, TLRPC.InputFileLocation location, boolean[] needReplacement, TLRPC.InputFileLocation[] replacement) {
        byte[] result = getFileReference(webpage.document, location, needReplacement, replacement);
        if (result != null) {
            return result;
        }
        byte[] result2 = getFileReference(webpage.photo, location, needReplacement, replacement);
        if (result2 != null) {
            return result2;
        }
        if (!webpage.attributes.isEmpty()) {
            int size1 = webpage.attributes.size();
            for (int a = 0; a < size1; a++) {
                TLRPC.TL_webPageAttributeTheme attribute = webpage.attributes.get(a);
                int size2 = attribute.documents.size();
                for (int b = 0; b < size2; b++) {
                    byte[] result3 = getFileReference(attribute.documents.get(b), location, needReplacement, replacement);
                    if (result3 != null) {
                        return result3;
                    }
                }
            }
        }
        if (webpage.cached_page != null) {
            int size22 = webpage.cached_page.documents.size();
            for (int b2 = 0; b2 < size22; b2++) {
                byte[] result4 = getFileReference(webpage.cached_page.documents.get(b2), location, needReplacement, replacement);
                if (result4 != null) {
                    return result4;
                }
            }
            int size23 = webpage.cached_page.photos.size();
            for (int b3 = 0; b3 < size23; b3++) {
                byte[] result5 = getFileReference(webpage.cached_page.photos.get(b3), location, needReplacement, replacement);
                if (result5 != null) {
                    return result5;
                }
            }
            return null;
        }
        return null;
    }

    public static boolean isFileRefError(String error) {
        return "FILEREF_EXPIRED".equals(error) || "FILE_REFERENCE_EXPIRED".equals(error) || "FILE_REFERENCE_EMPTY".equals(error) || (error != null && error.startsWith("FILE_REFERENCE_"));
    }
}
