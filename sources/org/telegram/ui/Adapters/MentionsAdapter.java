package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$ChannelParticipant;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_botCommand;
import org.telegram.tgnet.TLRPC$TL_botInlineMessageMediaAuto;
import org.telegram.tgnet.TLRPC$TL_channelFull;
import org.telegram.tgnet.TLRPC$TL_channelParticipantsMentions;
import org.telegram.tgnet.TLRPC$TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC$TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC$TL_chatBannedRights;
import org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inlineBotSwitchPM;
import org.telegram.tgnet.TLRPC$TL_inlineBotWebView;
import org.telegram.tgnet.TLRPC$TL_inputGeoPoint;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_botResults;
import org.telegram.tgnet.TLRPC$TL_messages_getInlineBotResults;
import org.telegram.tgnet.TLRPC$TL_messages_getStickers;
import org.telegram.tgnet.TLRPC$TL_messages_stickers;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoSize;
import org.telegram.tgnet.TLRPC$TL_photoSizeProgressive;
import org.telegram.tgnet.TLRPC$TL_topPeer;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_bots$BotInfo;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import org.telegram.ui.Business.QuickRepliesActivity;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes4.dex */
public class MentionsAdapter extends RecyclerListView.SelectionAdapter implements NotificationCenter.NotificationCenterDelegate {
    private LongSparseArray botInfo;
    private int botsCount;
    private Runnable cancelDelayRunnable;
    private int channelLastReqId;
    private int channelReqId;
    private TLRPC$Chat chat;
    private Runnable checkAgainRunnable;
    private boolean contextMedia;
    private int contextQueryReqid;
    private Runnable contextQueryRunnable;
    private int contextUsernameReqid;
    private boolean delayLocalResults;
    private MentionsAdapterDelegate delegate;
    private long dialog_id;
    private TLRPC$User foundContextBot;
    private TLRPC$ChatFull info;
    private boolean isDarkTheme;
    private boolean isSearchingMentions;
    private Object[] lastData;
    private boolean lastForSearch;
    private Location lastKnownLocation;
    private int lastPosition;
    private int lastReqId;
    private String[] lastSearchKeyboardLanguage;
    private String lastSticker;
    private String lastText;
    private boolean lastUsernameOnly;
    private Context mContext;
    private EmojiView.ChooseStickerActionTracker mentionsStickersActionTracker;
    private ArrayList messages;
    private String nextQueryOffset;
    private boolean noUserName;
    private ChatActivity parentFragment;
    private ArrayList quickReplies;
    private String quickRepliesQuery;
    private final Theme.ResourcesProvider resourcesProvider;
    private int resultLength;
    private int resultStartPosition;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchGlobalRunnable;
    private ArrayList searchResultBotContext;
    private TLRPC$TL_inlineBotSwitchPM searchResultBotContextSwitch;
    private long searchResultBotContextSwitchUserId;
    private TLRPC$TL_inlineBotWebView searchResultBotWebViewSwitch;
    private ArrayList searchResultCommands;
    private ArrayList searchResultCommandsHelp;
    private ArrayList searchResultCommandsUsers;
    private ArrayList searchResultHashtags;
    private ArrayList searchResultSuggestions;
    private ArrayList searchResultUsernames;
    private LongSparseArray searchResultUsernamesMap;
    private String searchingContextQuery;
    private String searchingContextUsername;
    private ArrayList stickers;
    private HashMap stickersMap;
    private long threadMessageId;
    private TLRPC$User user;
    private boolean visibleByStickersSearch;
    private boolean allowStickers = true;
    private boolean allowBots = true;
    private boolean allowChats = true;
    private final boolean USE_DIVIDERS = false;
    private int currentAccount = UserConfig.selectedAccount;
    private boolean needUsernames = true;
    private boolean needBotContext = true;
    private boolean inlineMediaEnabled = true;
    private boolean searchInDailogs = false;
    private ArrayList stickersToLoad = new ArrayList();
    private SendMessagesHelper.LocationProvider locationProvider = new SendMessagesHelper.LocationProvider(new SendMessagesHelper.LocationProvider.LocationProviderDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter.1
        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onLocationAcquired(Location location) {
            if (MentionsAdapter.this.foundContextBot == null || !MentionsAdapter.this.foundContextBot.bot_inline_geo) {
                return;
            }
            MentionsAdapter.this.lastKnownLocation = location;
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            mentionsAdapter.searchForContextBotResults(true, mentionsAdapter.foundContextBot, MentionsAdapter.this.searchingContextQuery, "");
        }

        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider.LocationProviderDelegate
        public void onUnableLocationAcquire() {
            MentionsAdapter.this.onLocationUnavailable();
        }
    }) { // from class: org.telegram.ui.Adapters.MentionsAdapter.2
        @Override // org.telegram.messenger.SendMessagesHelper.LocationProvider
        public void stop() {
            super.stop();
            MentionsAdapter.this.lastKnownLocation = null;
        }
    };
    private boolean isReversed = false;
    private int lastItemCount = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 4 implements Runnable {
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ MessagesStorage val$messagesStorage;
        final /* synthetic */ String val$query;
        final /* synthetic */ String val$username;

        4(String str, String str2, MessagesController messagesController, MessagesStorage messagesStorage) {
            this.val$query = str;
            this.val$username = str2;
            this.val$messagesController = messagesController;
            this.val$messagesStorage = messagesStorage;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(String str, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, MessagesController messagesController, MessagesStorage messagesStorage) {
            if (MentionsAdapter.this.searchingContextUsername == null || !MentionsAdapter.this.searchingContextUsername.equals(str)) {
                return;
            }
            TLRPC$User tLRPC$User = null;
            if (tLRPC$TL_error == null) {
                TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
                if (!tLRPC$TL_contacts_resolvedPeer.users.isEmpty()) {
                    TLRPC$User tLRPC$User2 = (TLRPC$User) tLRPC$TL_contacts_resolvedPeer.users.get(0);
                    messagesController.putUser(tLRPC$User2, false);
                    messagesStorage.putUsersAndChats(tLRPC$TL_contacts_resolvedPeer.users, null, true, true);
                    tLRPC$User = tLRPC$User2;
                }
            }
            MentionsAdapter.this.processFoundUser(tLRPC$User);
            MentionsAdapter.this.contextUsernameReqid = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final String str, final MessagesController messagesController, final MessagesStorage messagesStorage, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.4.this.lambda$run$0(str, tLRPC$TL_error, tLObject, messagesController, messagesStorage);
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MentionsAdapter.this.contextQueryRunnable != this) {
                return;
            }
            MentionsAdapter.this.contextQueryRunnable = null;
            if (MentionsAdapter.this.foundContextBot != null || MentionsAdapter.this.noUserName) {
                if (MentionsAdapter.this.noUserName) {
                    return;
                }
                MentionsAdapter mentionsAdapter = MentionsAdapter.this;
                mentionsAdapter.searchForContextBotResults(true, mentionsAdapter.foundContextBot, this.val$query, "");
                return;
            }
            MentionsAdapter.this.searchingContextUsername = this.val$username;
            TLObject userOrChat = this.val$messagesController.getUserOrChat(MentionsAdapter.this.searchingContextUsername);
            if (userOrChat instanceof TLRPC$User) {
                MentionsAdapter.this.processFoundUser((TLRPC$User) userOrChat);
                return;
            }
            TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername = new TLRPC$TL_contacts_resolveUsername();
            tLRPC$TL_contacts_resolveUsername.username = MentionsAdapter.this.searchingContextUsername;
            MentionsAdapter mentionsAdapter2 = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter2.currentAccount);
            final String str = this.val$username;
            final MessagesController messagesController = this.val$messagesController;
            final MessagesStorage messagesStorage = this.val$messagesStorage;
            mentionsAdapter2.contextUsernameReqid = connectionsManager.sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MentionsAdapter.4.this.lambda$run$1(str, messagesController, messagesStorage, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 7 implements Runnable {
        final /* synthetic */ TLRPC$Chat val$chat;
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ LongSparseArray val$newMap;
        final /* synthetic */ ArrayList val$newResult;
        final /* synthetic */ long val$threadId;
        final /* synthetic */ String val$usernameString;

        7(TLRPC$Chat tLRPC$Chat, String str, long j, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController) {
            this.val$chat = tLRPC$Chat;
            this.val$usernameString = str;
            this.val$threadId = j;
            this.val$newResult = arrayList;
            this.val$newMap = longSparseArray;
            this.val$messagesController = messagesController;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int i, ArrayList arrayList, LongSparseArray longSparseArray, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, MessagesController messagesController) {
            TLObject chat;
            if (MentionsAdapter.this.channelReqId != 0 && i == MentionsAdapter.this.channelLastReqId && MentionsAdapter.this.searchResultUsernamesMap != null && MentionsAdapter.this.searchResultUsernames != null) {
                MentionsAdapter.this.showUsersResult(arrayList, longSparseArray, false);
                if (tLRPC$TL_error == null) {
                    TLRPC$TL_channels_channelParticipants tLRPC$TL_channels_channelParticipants = (TLRPC$TL_channels_channelParticipants) tLObject;
                    messagesController.putUsers(tLRPC$TL_channels_channelParticipants.users, false);
                    messagesController.putChats(tLRPC$TL_channels_channelParticipants.chats, false);
                    MentionsAdapter.this.searchResultUsernames.isEmpty();
                    if (!tLRPC$TL_channels_channelParticipants.participants.isEmpty()) {
                        long clientUserId = UserConfig.getInstance(MentionsAdapter.this.currentAccount).getClientUserId();
                        for (int i2 = 0; i2 < tLRPC$TL_channels_channelParticipants.participants.size(); i2++) {
                            long peerId = MessageObject.getPeerId(((TLRPC$ChannelParticipant) tLRPC$TL_channels_channelParticipants.participants.get(i2)).peer);
                            if (MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(peerId) < 0 && ((peerId != 0 || MentionsAdapter.this.searchResultUsernamesMap.indexOfKey(clientUserId) < 0) && (MentionsAdapter.this.isSearchingMentions || (peerId != clientUserId && peerId != 0)))) {
                                if (peerId >= 0) {
                                    chat = messagesController.getUser(Long.valueOf(peerId));
                                    if (chat == null) {
                                        return;
                                    }
                                } else {
                                    chat = messagesController.getChat(Long.valueOf(-peerId));
                                    if (chat == null) {
                                        return;
                                    }
                                }
                                MentionsAdapter.this.searchResultUsernames.add(chat);
                            }
                        }
                    }
                }
                MentionsAdapter.this.notifyDataSetChanged();
                MentionsAdapter.this.delegate.needChangePanelVisibility(!MentionsAdapter.this.searchResultUsernames.isEmpty());
            }
            MentionsAdapter.this.channelReqId = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final int i, final ArrayList arrayList, final LongSparseArray longSparseArray, final MessagesController messagesController, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.7.this.lambda$run$0(i, arrayList, longSparseArray, tLRPC$TL_error, tLObject, messagesController);
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MentionsAdapter.this.searchGlobalRunnable != this) {
                return;
            }
            TLRPC$TL_channels_getParticipants tLRPC$TL_channels_getParticipants = new TLRPC$TL_channels_getParticipants();
            tLRPC$TL_channels_getParticipants.channel = MessagesController.getInputChannel(this.val$chat);
            tLRPC$TL_channels_getParticipants.limit = 20;
            tLRPC$TL_channels_getParticipants.offset = 0;
            TLRPC$TL_channelParticipantsMentions tLRPC$TL_channelParticipantsMentions = new TLRPC$TL_channelParticipantsMentions();
            int i = tLRPC$TL_channelParticipantsMentions.flags;
            tLRPC$TL_channelParticipantsMentions.flags = i | 1;
            tLRPC$TL_channelParticipantsMentions.q = this.val$usernameString;
            long j = this.val$threadId;
            if (j != 0) {
                tLRPC$TL_channelParticipantsMentions.flags = i | 3;
                tLRPC$TL_channelParticipantsMentions.top_msg_id = (int) j;
            }
            tLRPC$TL_channels_getParticipants.filter = tLRPC$TL_channelParticipantsMentions;
            final int access$1704 = MentionsAdapter.access$1704(MentionsAdapter.this);
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter.currentAccount);
            final ArrayList arrayList = this.val$newResult;
            final LongSparseArray longSparseArray = this.val$newMap;
            final MessagesController messagesController = this.val$messagesController;
            mentionsAdapter.channelReqId = connectionsManager.sendRequest(tLRPC$TL_channels_getParticipants, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MentionsAdapter.7.this.lambda$run$1(access$1704, arrayList, longSparseArray, messagesController, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* loaded from: classes4.dex */
    public interface MentionsAdapterDelegate {
        void needChangePanelVisibility(boolean z);

        void onContextClick(TLRPC$BotInlineResult tLRPC$BotInlineResult);

        void onContextSearch(boolean z);

        void onItemCountUpdate(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class StickerResult {
        public Object parent;
        public TLRPC$Document sticker;

        public StickerResult(TLRPC$Document tLRPC$Document, Object obj) {
            this.sticker = tLRPC$Document;
            this.parent = obj;
        }
    }

    public MentionsAdapter(Context context, boolean z, long j, long j2, MentionsAdapterDelegate mentionsAdapterDelegate, Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        this.mContext = context;
        this.delegate = mentionsAdapterDelegate;
        this.isDarkTheme = z;
        this.dialog_id = j;
        this.threadMessageId = j2;
        SearchAdapterHelper searchAdapterHelper = new SearchAdapterHelper(true);
        this.searchAdapterHelper = searchAdapterHelper;
        searchAdapterHelper.setDelegate(new SearchAdapterHelper.SearchAdapterHelperDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter.3
            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ boolean canApplySearchResults(int i) {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$canApplySearchResults(this, i);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeCallParticipants() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeCallParticipants(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public /* synthetic */ LongSparseArray getExcludeUsers() {
                return SearchAdapterHelper.SearchAdapterHelperDelegate.-CC.$default$getExcludeUsers(this);
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onDataSetChanged(int i) {
                MentionsAdapter.this.notifyDataSetChanged();
            }

            @Override // org.telegram.ui.Adapters.SearchAdapterHelper.SearchAdapterHelperDelegate
            public void onSetHashtags(ArrayList arrayList, HashMap hashMap) {
                if (MentionsAdapter.this.lastText != null) {
                    MentionsAdapter mentionsAdapter = MentionsAdapter.this;
                    mentionsAdapter.lambda$searchUsernameOrHashtag$7(mentionsAdapter.lastText, MentionsAdapter.this.lastPosition, MentionsAdapter.this.messages, MentionsAdapter.this.lastUsernameOnly, MentionsAdapter.this.lastForSearch);
                }
            }
        });
        if (!z) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadFailed);
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentDocumentsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
    }

    static /* synthetic */ int access$1704(MentionsAdapter mentionsAdapter) {
        int i = mentionsAdapter.channelLastReqId + 1;
        mentionsAdapter.channelLastReqId = i;
        return i;
    }

    private void addStickerToResult(TLRPC$Document tLRPC$Document, Object obj) {
        if (tLRPC$Document == null) {
            return;
        }
        String str = tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
        HashMap hashMap = this.stickersMap;
        if (hashMap == null || !hashMap.containsKey(str)) {
            if (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(tLRPC$Document)) {
                if (this.stickers == null) {
                    this.stickers = new ArrayList();
                    this.stickersMap = new HashMap();
                }
                this.stickers.add(new StickerResult(tLRPC$Document, obj));
                this.stickersMap.put(str, tLRPC$Document);
                EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = this.mentionsStickersActionTracker;
                if (chooseStickerActionTracker != null) {
                    chooseStickerActionTracker.checkVisibility();
                }
            }
        }
    }

    private void addStickersToResult(ArrayList arrayList, Object obj) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            TLRPC$Document tLRPC$Document = (TLRPC$Document) arrayList.get(i);
            String str = tLRPC$Document.dc_id + "_" + tLRPC$Document.id;
            HashMap hashMap = this.stickersMap;
            if ((hashMap == null || !hashMap.containsKey(str)) && (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(tLRPC$Document))) {
                int size2 = tLRPC$Document.attributes.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size2) {
                        break;
                    }
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i2);
                    if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                        obj = tLRPC$DocumentAttribute.stickerset;
                        break;
                    }
                    i2++;
                }
                if (this.stickers == null) {
                    this.stickers = new ArrayList();
                    this.stickersMap = new HashMap();
                }
                this.stickers.add(new StickerResult(tLRPC$Document, obj));
                this.stickersMap.put(str, tLRPC$Document);
            }
        }
    }

    private void checkLocationPermissionsOrStart() {
        int checkSelfPermission;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getParentActivity() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            checkSelfPermission = this.parentFragment.getParentActivity().checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if (checkSelfPermission != 0) {
                this.parentFragment.getParentActivity().requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                return;
            }
        }
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || !tLRPC$User.bot_inline_geo) {
            return;
        }
        this.locationProvider.start();
    }

    private boolean checkStickerFilesExistAndDownload() {
        if (this.stickers == null) {
            return false;
        }
        this.stickersToLoad.clear();
        int min = Math.min(6, this.stickers.size());
        for (int i = 0; i < min; i++) {
            StickerResult stickerResult = (StickerResult) this.stickers.get(i);
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(stickerResult.sticker.thumbs, 90);
            if (((closestPhotoSizeWithSize instanceof TLRPC$TL_photoSize) || (closestPhotoSizeWithSize instanceof TLRPC$TL_photoSizeProgressive)) && !FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, "webp", true).exists()) {
                this.stickersToLoad.add(FileLoader.getAttachFileName(closestPhotoSizeWithSize, "webp"));
                FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForDocument(closestPhotoSizeWithSize, stickerResult.sticker), stickerResult.parent, "webp", 1, 1);
            }
        }
        return this.stickersToLoad.isEmpty();
    }

    private void clearStickers() {
        this.lastSticker = null;
        this.stickers = null;
        this.stickersMap = null;
        notifyDataSetChanged();
        if (this.lastReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
            this.lastReqId = 0;
        }
        EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = this.mentionsStickersActionTracker;
        if (chooseStickerActionTracker != null) {
            chooseStickerActionTracker.checkVisibility();
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    private boolean isValidSticker(TLRPC$Document tLRPC$Document, String str) {
        int size = tLRPC$Document.attributes.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                String str2 = tLRPC$DocumentAttribute.alt;
                if (str2 == null || !str2.contains(str)) {
                    break;
                }
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean itemsEqual(Object obj, Object obj2) {
        MediaDataController.KeywordResult keywordResult;
        String str;
        String str2;
        if (obj instanceof QuickRepliesController.QuickReply) {
            return false;
        }
        if (obj == obj2) {
            return true;
        }
        if ((obj instanceof StickerResult) && (obj2 instanceof StickerResult) && ((StickerResult) obj).sticker == ((StickerResult) obj2).sticker) {
            return true;
        }
        if ((obj instanceof TLRPC$User) && (obj2 instanceof TLRPC$User) && ((TLRPC$User) obj).id == ((TLRPC$User) obj2).id) {
            return true;
        }
        if ((obj instanceof TLRPC$Chat) && (obj2 instanceof TLRPC$Chat) && ((TLRPC$Chat) obj).id == ((TLRPC$Chat) obj2).id) {
            return true;
        }
        if ((obj instanceof String) && (obj2 instanceof String) && obj.equals(obj2)) {
            return true;
        }
        if ((obj instanceof MediaDataController.KeywordResult) && (obj2 instanceof MediaDataController.KeywordResult) && (str = (keywordResult = (MediaDataController.KeywordResult) obj).keyword) != null) {
            MediaDataController.KeywordResult keywordResult2 = (MediaDataController.KeywordResult) obj2;
            if (str.equals(keywordResult2.keyword) && (str2 = keywordResult.emoji) != null && str2.equals(keywordResult2.emoji)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateViewHolder$10(ContextLinkCell contextLinkCell) {
        this.delegate.onContextClick(contextLinkCell.getResult());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processFoundUser$2(boolean[] zArr, TLRPC$User tLRPC$User, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        if (tLRPC$User != null) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            edit.putBoolean("inlinegeo_" + tLRPC$User.id, true).commit();
            checkLocationPermissionsOrStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processFoundUser$3(boolean[] zArr, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        onLocationUnavailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processFoundUser$4(boolean[] zArr, DialogInterface dialogInterface) {
        if (zArr[0]) {
            return;
        }
        onLocationUnavailable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForContextBotResults$5(String str, boolean z, TLObject tLObject, TLRPC$User tLRPC$User, String str2, MessagesStorage messagesStorage, String str3) {
        boolean z2;
        if (str.equals(this.searchingContextQuery)) {
            int i = 0;
            this.contextQueryReqid = 0;
            if (z && tLObject == null) {
                searchForContextBotResults(false, tLRPC$User, str, str2);
            } else {
                MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
                if (mentionsAdapterDelegate != null) {
                    mentionsAdapterDelegate.onContextSearch(false);
                }
            }
            if (tLObject instanceof TLRPC$TL_messages_botResults) {
                TLRPC$TL_messages_botResults tLRPC$TL_messages_botResults = (TLRPC$TL_messages_botResults) tLObject;
                if (!z && tLRPC$TL_messages_botResults.cache_time != 0) {
                    messagesStorage.saveBotCache(str3, tLRPC$TL_messages_botResults);
                }
                this.nextQueryOffset = tLRPC$TL_messages_botResults.next_offset;
                if (this.searchResultBotContextSwitch == null) {
                    this.searchResultBotContextSwitch = tLRPC$TL_messages_botResults.switch_pm;
                }
                this.searchResultBotWebViewSwitch = tLRPC$TL_messages_botResults.switch_webview;
                int i2 = 0;
                while (i2 < tLRPC$TL_messages_botResults.results.size()) {
                    TLRPC$BotInlineResult tLRPC$BotInlineResult = (TLRPC$BotInlineResult) tLRPC$TL_messages_botResults.results.get(i2);
                    if (!(tLRPC$BotInlineResult.document instanceof TLRPC$TL_document) && !(tLRPC$BotInlineResult.photo instanceof TLRPC$TL_photo) && !"game".equals(tLRPC$BotInlineResult.type) && tLRPC$BotInlineResult.content == null && (tLRPC$BotInlineResult.send_message instanceof TLRPC$TL_botInlineMessageMediaAuto)) {
                        tLRPC$TL_messages_botResults.results.remove(i2);
                        i2--;
                    }
                    tLRPC$BotInlineResult.query_id = tLRPC$TL_messages_botResults.query_id;
                    i2++;
                }
                if (this.searchResultBotContext == null || str2.length() == 0) {
                    this.searchResultBotContext = tLRPC$TL_messages_botResults.results;
                    this.contextMedia = tLRPC$TL_messages_botResults.gallery;
                    z2 = false;
                } else {
                    this.searchResultBotContext.addAll(tLRPC$TL_messages_botResults.results);
                    if (tLRPC$TL_messages_botResults.results.isEmpty()) {
                        this.nextQueryOffset = "";
                    }
                    z2 = true;
                }
                Runnable runnable = this.cancelDelayRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.cancelDelayRunnable = null;
                }
                this.searchResultHashtags = null;
                this.stickers = null;
                this.searchResultUsernames = null;
                this.searchResultUsernamesMap = null;
                this.searchResultCommands = null;
                this.quickReplies = null;
                this.searchResultSuggestions = null;
                this.searchResultCommandsHelp = null;
                this.searchResultCommandsUsers = null;
                this.delegate.needChangePanelVisibility((this.searchResultBotContext.isEmpty() && this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? false : true);
                if (!z2) {
                    notifyDataSetChanged();
                    return;
                }
                i = (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? 1 : 1;
                notifyItemChanged(((this.searchResultBotContext.size() - tLRPC$TL_messages_botResults.results.size()) + i) - 1);
                notifyItemRangeInserted((this.searchResultBotContext.size() - tLRPC$TL_messages_botResults.results.size()) + i, tLRPC$TL_messages_botResults.results.size());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForContextBotResults$6(final String str, final boolean z, final TLRPC$User tLRPC$User, final String str2, final MessagesStorage messagesStorage, final String str3, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.lambda$searchForContextBotResults$5(str, z, tLObject, tLRPC$User, str2, messagesStorage, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchServerStickers$0(String str, TLObject tLObject) {
        ArrayList arrayList;
        this.lastReqId = 0;
        if (str.equals(this.lastSticker) && (tLObject instanceof TLRPC$TL_messages_stickers)) {
            this.delayLocalResults = false;
            TLRPC$TL_messages_stickers tLRPC$TL_messages_stickers = (TLRPC$TL_messages_stickers) tLObject;
            ArrayList arrayList2 = this.stickers;
            int size = arrayList2 != null ? arrayList2.size() : 0;
            ArrayList arrayList3 = tLRPC$TL_messages_stickers.stickers;
            addStickersToResult(arrayList3, "sticker_search_" + str);
            ArrayList arrayList4 = this.stickers;
            int size2 = arrayList4 != null ? arrayList4.size() : 0;
            if (!this.visibleByStickersSearch && (arrayList = this.stickers) != null && !arrayList.isEmpty()) {
                checkStickerFilesExistAndDownload();
                this.delegate.needChangePanelVisibility(getItemCountInternal() > 0);
                this.visibleByStickersSearch = true;
            }
            if (size != size2) {
                notifyDataSetChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchServerStickers$1(final String str, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.lambda$searchServerStickers$0(str, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchUsernameOrHashtag$8(ArrayList arrayList, LongSparseArray longSparseArray) {
        this.cancelDelayRunnable = null;
        showUsersResult(arrayList, longSparseArray, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchUsernameOrHashtag$9(ArrayList arrayList, String str) {
        this.searchResultSuggestions = arrayList;
        this.searchResultHashtags = null;
        this.stickers = null;
        this.searchResultUsernames = null;
        this.searchResultUsernamesMap = null;
        this.searchResultCommands = null;
        this.quickReplies = null;
        this.searchResultCommandsHelp = null;
        this.searchResultCommandsUsers = null;
        notifyDataSetChanged();
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        ArrayList arrayList2 = this.searchResultSuggestions;
        mentionsAdapterDelegate.needChangePanelVisibility((arrayList2 == null || arrayList2.isEmpty()) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationUnavailable() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || !tLRPC$User.bot_inline_geo) {
            return;
        }
        Location location = new Location("network");
        this.lastKnownLocation = location;
        location.setLatitude(-1000.0d);
        this.lastKnownLocation.setLongitude(-1000.0d);
        searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processFoundUser(TLRPC$User tLRPC$User) {
        ChatActivity chatActivity;
        TLRPC$Chat currentChat;
        this.contextUsernameReqid = 0;
        this.locationProvider.stop();
        if (tLRPC$User == null || !tLRPC$User.bot || tLRPC$User.bot_inline_placeholder == null) {
            this.foundContextBot = null;
            this.searchResultBotContextSwitch = null;
            this.inlineMediaEnabled = true;
        } else {
            this.foundContextBot = tLRPC$User;
            long j = tLRPC$User.id;
            if (j != this.searchResultBotContextSwitchUserId) {
                this.searchResultBotContextSwitch = null;
                this.searchResultBotContextSwitchUserId = j;
            }
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && (currentChat = chatActivity2.getCurrentChat()) != null) {
                boolean canSendStickers = ChatObject.canSendStickers(currentChat);
                this.inlineMediaEnabled = canSendStickers;
                if (!canSendStickers) {
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(true);
                    return;
                }
            }
            if (this.foundContextBot.bot_inline_geo) {
                SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                if (notificationsSettings.getBoolean("inlinegeo_" + this.foundContextBot.id, false) || (chatActivity = this.parentFragment) == null || chatActivity.getParentActivity() == null) {
                    checkLocationPermissionsOrStart();
                } else {
                    final TLRPC$User tLRPC$User2 = this.foundContextBot;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                    builder.setTitle(LocaleController.getString(R.string.ShareYouLocationTitle));
                    builder.setMessage(LocaleController.getString(R.string.ShareYouLocationInline));
                    final boolean[] zArr = new boolean[1];
                    builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.lambda$processFoundUser$2(zArr, tLRPC$User2, dialogInterface, i);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.lambda$processFoundUser$3(zArr, dialogInterface, i);
                        }
                    });
                    this.parentFragment.showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            MentionsAdapter.this.lambda$processFoundUser$4(zArr, dialogInterface);
                        }
                    });
                }
            }
        }
        if (this.foundContextBot == null) {
            this.noUserName = true;
            this.searchResultBotContextSwitch = null;
            return;
        }
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        if (mentionsAdapterDelegate != null) {
            mentionsAdapterDelegate.onContextSearch(true);
        }
        searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
    }

    private void searchForContextBot(String str, String str2) {
        String str3;
        String str4;
        String str5;
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || (str4 = tLRPC$User.username) == null || !str4.equals(str) || (str5 = this.searchingContextQuery) == null || !str5.equals(str2)) {
            if (this.foundContextBot != null) {
                if (!this.inlineMediaEnabled && str != null && str2 != null) {
                    return;
                }
                this.delegate.needChangePanelVisibility(false);
            }
            Runnable runnable = this.contextQueryRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.contextQueryRunnable = null;
            }
            if (TextUtils.isEmpty(str) || ((str3 = this.searchingContextUsername) != null && !str3.equals(str))) {
                if (this.contextUsernameReqid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
                    this.contextUsernameReqid = 0;
                }
                if (this.contextQueryReqid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                    this.contextQueryReqid = 0;
                }
                this.foundContextBot = null;
                this.searchResultBotContextSwitch = null;
                this.inlineMediaEnabled = true;
                this.searchingContextUsername = null;
                this.searchingContextQuery = null;
                this.locationProvider.stop();
                this.noUserName = false;
                MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
                if (mentionsAdapterDelegate != null) {
                    mentionsAdapterDelegate.onContextSearch(false);
                }
                if (str == null || str.length() == 0) {
                    return;
                }
            }
            if (str2 == null) {
                if (this.contextQueryReqid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
                    this.contextQueryReqid = 0;
                }
                this.searchingContextQuery = null;
                MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
                if (mentionsAdapterDelegate2 != null) {
                    mentionsAdapterDelegate2.onContextSearch(false);
                    return;
                }
                return;
            }
            MentionsAdapterDelegate mentionsAdapterDelegate3 = this.delegate;
            if (mentionsAdapterDelegate3 != null) {
                if (this.foundContextBot != null) {
                    mentionsAdapterDelegate3.onContextSearch(true);
                } else if (str.equals("gif")) {
                    this.searchingContextUsername = "gif";
                    this.delegate.onContextSearch(false);
                }
            }
            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            this.searchingContextQuery = str2;
            4 r0 = new 4(str2, str, messagesController, messagesStorage);
            this.contextQueryRunnable = r0;
            AndroidUtilities.runOnUIThread(r0, 400L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchForContextBotResults(final boolean z, final TLRPC$User tLRPC$User, final String str, final String str2) {
        Location location;
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        if (!this.inlineMediaEnabled || !this.allowBots) {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
            }
        } else if (str == null || tLRPC$User == null) {
            this.searchingContextQuery = null;
        } else if (tLRPC$User.bot_inline_geo && this.lastKnownLocation == null) {
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.dialog_id);
            sb.append("_");
            sb.append(str);
            sb.append("_");
            sb.append(str2);
            sb.append("_");
            sb.append(this.dialog_id);
            sb.append("_");
            sb.append(tLRPC$User.id);
            sb.append("_");
            sb.append((!tLRPC$User.bot_inline_geo || this.lastKnownLocation.getLatitude() == -1000.0d) ? "" : Double.valueOf(this.lastKnownLocation.getLatitude() + this.lastKnownLocation.getLongitude()));
            final String sb2 = sb.toString();
            final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda7
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    MentionsAdapter.this.lambda$searchForContextBotResults$6(str, z, tLRPC$User, str2, messagesStorage, sb2, tLObject, tLRPC$TL_error);
                }
            };
            long j = tLRPC$User.id;
            if (j != this.searchResultBotContextSwitchUserId) {
                this.searchResultBotContextSwitch = null;
                this.searchResultBotContextSwitchUserId = j;
            }
            if (z) {
                messagesStorage.getBotCache(sb2, requestDelegate);
                return;
            }
            TLRPC$TL_messages_getInlineBotResults tLRPC$TL_messages_getInlineBotResults = new TLRPC$TL_messages_getInlineBotResults();
            tLRPC$TL_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(tLRPC$User);
            tLRPC$TL_messages_getInlineBotResults.query = str;
            tLRPC$TL_messages_getInlineBotResults.offset = str2;
            if (tLRPC$User.bot_inline_geo && (location = this.lastKnownLocation) != null && location.getLatitude() != -1000.0d) {
                tLRPC$TL_messages_getInlineBotResults.flags |= 1;
                TLRPC$TL_inputGeoPoint tLRPC$TL_inputGeoPoint = new TLRPC$TL_inputGeoPoint();
                tLRPC$TL_messages_getInlineBotResults.geo_point = tLRPC$TL_inputGeoPoint;
                tLRPC$TL_inputGeoPoint.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
                tLRPC$TL_messages_getInlineBotResults.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
            }
            tLRPC$TL_messages_getInlineBotResults.peer = DialogObject.isEncryptedDialog(this.dialog_id) ? new TLRPC$TL_inputPeerEmpty() : MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialog_id);
            this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getInlineBotResults, requestDelegate, 2);
        }
    }

    private void searchServerStickers(final String str, String str2) {
        TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers = new TLRPC$TL_messages_getStickers();
        tLRPC$TL_messages_getStickers.emoticon = str2;
        tLRPC$TL_messages_getStickers.hash = 0L;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getStickers, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda9
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                MentionsAdapter.this.lambda$searchServerStickers$1(str, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUsersResult(ArrayList arrayList, LongSparseArray longSparseArray, boolean z) {
        this.searchResultUsernames = arrayList;
        if ((!this.allowBots || !this.allowChats) && arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                TLObject tLObject = (TLObject) it.next();
                if (!(tLObject instanceof TLRPC$Chat) || this.allowChats) {
                    if (tLObject instanceof TLRPC$User) {
                        TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                        if (!tLRPC$User.bot && !UserObject.isService(tLRPC$User.id)) {
                        }
                    }
                }
                it.remove();
            }
        }
        this.searchResultUsernamesMap = longSparseArray;
        Runnable runnable = this.cancelDelayRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.cancelDelayRunnable = null;
        }
        this.searchResultBotContext = null;
        this.stickers = null;
        if (z) {
            notifyDataSetChanged();
            this.delegate.needChangePanelVisibility(!this.searchResultUsernames.isEmpty());
        }
    }

    public void addHashtagsFromMessage(CharSequence charSequence) {
        this.searchAdapterHelper.addHashtagsFromMessage(charSequence);
    }

    public void clearRecentHashtags() {
        this.searchAdapterHelper.clearRecentHashtags();
        this.searchResultHashtags.clear();
        notifyDataSetChanged();
        MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
        if (mentionsAdapterDelegate != null) {
            mentionsAdapterDelegate.needChangePanelVisibility(false);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        Runnable runnable;
        if (i == NotificationCenter.fileLoaded || i == NotificationCenter.fileLoadFailed) {
            ArrayList arrayList = this.stickers;
            if (arrayList == null || arrayList.isEmpty() || this.stickersToLoad.isEmpty() || !this.visibleByStickersSearch) {
                return;
            }
            this.stickersToLoad.remove((String) objArr[0]);
            if (this.stickersToLoad.isEmpty()) {
                this.delegate.needChangePanelVisibility(getItemCountInternal() > 0);
                return;
            }
            return;
        }
        if (i == NotificationCenter.recentDocumentsDidLoad) {
            runnable = this.checkAgainRunnable;
            if (runnable == null) {
                return;
            }
        } else if (i != NotificationCenter.stickersDidLoad || ((Integer) objArr[0]).intValue() != 0 || (runnable = this.checkAgainRunnable) == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable);
        this.checkAgainRunnable = null;
    }

    public void doSomeStickersAction() {
        if (isStickers()) {
            if (this.mentionsStickersActionTracker == null) {
                EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = new EmojiView.ChooseStickerActionTracker(this.currentAccount, this.dialog_id, this.threadMessageId) { // from class: org.telegram.ui.Adapters.MentionsAdapter.8
                    @Override // org.telegram.ui.Components.EmojiView.ChooseStickerActionTracker
                    public boolean isShown() {
                        return MentionsAdapter.this.isStickers();
                    }
                };
                this.mentionsStickersActionTracker = chooseStickerActionTracker;
                chooseStickerActionTracker.checkVisibility();
            }
            this.mentionsStickersActionTracker.doSomeAction();
        }
    }

    public String getBotCaption() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User != null) {
            return tLRPC$User.bot_inline_placeholder;
        }
        String str = this.searchingContextUsername;
        if (str == null || !str.equals("gif")) {
            return null;
        }
        return LocaleController.getString(R.string.SearchGifsTitle);
    }

    public TLRPC$TL_inlineBotSwitchPM getBotContextSwitch() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User == null || tLRPC$User.id == this.searchResultBotContextSwitchUserId) {
            return this.searchResultBotContextSwitch;
        }
        return null;
    }

    public TLRPC$TL_inlineBotWebView getBotWebViewSwitch() {
        return this.searchResultBotWebViewSwitch;
    }

    public long getContextBotId() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        if (tLRPC$User != null) {
            return tLRPC$User.id;
        }
        return 0L;
    }

    public String getContextBotName() {
        TLRPC$User tLRPC$User = this.foundContextBot;
        return tLRPC$User != null ? tLRPC$User.username : "";
    }

    public TLRPC$User getContextBotUser() {
        return this.foundContextBot;
    }

    public TLRPC$User getFoundContextBot() {
        return this.foundContextBot;
    }

    public Object getItem(int i) {
        ArrayList arrayList = this.stickers;
        if (arrayList != null) {
            if (i < 0 || i >= arrayList.size()) {
                return null;
            }
            return ((StickerResult) this.stickers.get(i)).sticker;
        }
        ArrayList arrayList2 = this.searchResultBotContext;
        if (arrayList2 != null) {
            TLRPC$TL_inlineBotWebView tLRPC$TL_inlineBotWebView = this.searchResultBotWebViewSwitch;
            if (tLRPC$TL_inlineBotWebView == null) {
                TLRPC$TL_inlineBotSwitchPM tLRPC$TL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
                if (tLRPC$TL_inlineBotSwitchPM != null) {
                    if (i == 0) {
                        return tLRPC$TL_inlineBotSwitchPM;
                    }
                }
                if (i >= 0 || i >= arrayList2.size()) {
                    return null;
                }
                return this.searchResultBotContext.get(i);
            } else if (i == 0) {
                return tLRPC$TL_inlineBotWebView;
            }
            i--;
            if (i >= 0) {
            }
            return null;
        }
        ArrayList arrayList3 = this.searchResultUsernames;
        if (arrayList3 != null) {
            if (i < 0 || i >= arrayList3.size()) {
                return null;
            }
            return this.searchResultUsernames.get(i);
        }
        ArrayList arrayList4 = this.searchResultHashtags;
        if (arrayList4 != null) {
            if (i < 0 || i >= arrayList4.size()) {
                return null;
            }
            return this.searchResultHashtags.get(i);
        }
        ArrayList arrayList5 = this.searchResultSuggestions;
        if (arrayList5 != null) {
            if (i < 0 || i >= arrayList5.size()) {
                return null;
            }
            return this.searchResultSuggestions.get(i);
        }
        ArrayList arrayList6 = this.quickReplies;
        if (arrayList6 != null || this.searchResultCommands != null) {
            if (arrayList6 != null) {
                if (i >= 0 && i < arrayList6.size()) {
                    return this.quickReplies.get(i);
                }
                ArrayList arrayList7 = this.quickReplies;
                if (arrayList7 != null) {
                    i -= arrayList7.size();
                }
            }
            ArrayList arrayList8 = this.searchResultCommands;
            if (arrayList8 != null && i >= 0 && i < arrayList8.size()) {
                ArrayList arrayList9 = this.searchResultCommandsUsers;
                if (arrayList9 == null || (this.botsCount == 1 && !(this.info instanceof TLRPC$TL_channelFull))) {
                    return this.searchResultCommands.get(i);
                }
                if (arrayList9.get(i) != null) {
                    return String.format("%s@%s", this.searchResultCommands.get(i), this.searchResultCommandsUsers.get(i) != null ? ((TLRPC$User) this.searchResultCommandsUsers.get(i)).username : "");
                }
                return String.format("%s", this.searchResultCommands.get(i));
            }
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int itemCountInternal = getItemCountInternal();
        this.lastItemCount = itemCountInternal;
        return itemCountInternal;
    }

    public int getItemCountInternal() {
        int i = 1;
        if (this.foundContextBot == null || this.inlineMediaEnabled) {
            ArrayList arrayList = this.stickers;
            if (arrayList != null) {
                return arrayList.size();
            }
            ArrayList arrayList2 = this.searchResultBotContext;
            if (arrayList2 != null) {
                int size = arrayList2.size();
                if (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) {
                    i = 0;
                }
                return size + i;
            }
            ArrayList arrayList3 = this.searchResultUsernames;
            if (arrayList3 != null) {
                return arrayList3.size();
            }
            ArrayList arrayList4 = this.searchResultHashtags;
            if (arrayList4 != null) {
                return arrayList4.size();
            }
            if (this.searchResultCommands == null && this.quickReplies == null) {
                ArrayList arrayList5 = this.searchResultSuggestions;
                if (arrayList5 != null) {
                    return arrayList5.size();
                }
                return 0;
            }
            ArrayList arrayList6 = this.quickReplies;
            int size2 = arrayList6 == null ? 0 : arrayList6.size();
            ArrayList arrayList7 = this.searchResultCommands;
            return size2 + (arrayList7 != null ? arrayList7.size() : 0);
        }
        return 1;
    }

    public Object getItemParent(int i) {
        ArrayList arrayList = this.stickers;
        if (arrayList == null || i < 0 || i >= arrayList.size()) {
            return null;
        }
        return ((StickerResult) this.stickers.get(i)).parent;
    }

    public int getItemPosition(int i) {
        return this.searchResultBotContext != null ? (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? i : i - 1 : i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.stickers != null) {
            return 4;
        }
        if (this.foundContextBot == null || this.inlineMediaEnabled) {
            if (this.searchResultBotContext == null) {
                ArrayList arrayList = this.quickReplies;
                return (arrayList == null || i < 0 || i >= arrayList.size()) ? 0 : 5;
            } else if (i == 0) {
                return (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? 1 : 2;
            } else {
                return 1;
            }
        }
        return 3;
    }

    public int getLastItemCount() {
        return this.lastItemCount;
    }

    public int getResultLength() {
        return this.resultLength;
    }

    public int getResultStartPosition() {
        return this.resultStartPosition;
    }

    public ArrayList getSearchResultBotContext() {
        return this.searchResultBotContext;
    }

    public boolean isBannedInline() {
        return (this.foundContextBot == null || this.inlineMediaEnabled) ? false : true;
    }

    public boolean isBotCommands() {
        return this.searchResultCommands != null;
    }

    public boolean isBotContext() {
        return this.searchResultBotContext != null;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        return (this.foundContextBot == null || this.inlineMediaEnabled) && this.stickers == null;
    }

    public boolean isLongClickEnabled() {
        return (this.searchResultHashtags == null && this.searchResultCommands == null) ? false : true;
    }

    public boolean isMediaLayout() {
        return this.contextMedia || this.stickers != null;
    }

    public boolean isStickers() {
        return this.stickers != null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void notifyDataSetChanged() {
        MentionsAdapterDelegate mentionsAdapterDelegate;
        int i = this.lastItemCount;
        if (i != -1 && this.lastData != null) {
            int itemCount = getItemCount();
            boolean z = i != itemCount;
            int min = Math.min(i, itemCount);
            Object[] objArr = new Object[itemCount];
            for (int i2 = 0; i2 < itemCount; i2++) {
                objArr[i2] = getItem(i2);
            }
            while (r2 < min) {
                if (r2 >= 0) {
                    Object[] objArr2 = this.lastData;
                    r2 = (r2 < objArr2.length && r2 < itemCount && itemsEqual(objArr2[r2], objArr[r2])) ? r2 + 1 : 0;
                }
                notifyItemChanged(r2);
                z = true;
            }
            notifyItemRangeRemoved(min, i - min);
            notifyItemRangeInserted(min, itemCount - min);
            if (z && (mentionsAdapterDelegate = this.delegate) != null) {
                mentionsAdapterDelegate.onItemCountUpdate(i, itemCount);
            }
            this.lastData = objArr;
            return;
        }
        MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
        if (mentionsAdapterDelegate2 != null) {
            mentionsAdapterDelegate2.onItemCountUpdate(0, getItemCount());
        }
        super.notifyDataSetChanged();
        this.lastData = new Object[getItemCount()];
        while (true) {
            Object[] objArr3 = this.lastData;
            if (r2 >= objArr3.length) {
                return;
            }
            objArr3[r2] = getItem(r2);
            r2++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        String formatString;
        int i2;
        TLRPC$TL_chatBannedRights tLRPC$TL_chatBannedRights;
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 4) {
            StickerCell stickerCell = (StickerCell) viewHolder.itemView;
            StickerResult stickerResult = (StickerResult) this.stickers.get(i);
            stickerCell.setSticker(stickerResult.sticker, stickerResult.parent);
            stickerCell.setClearsInputField(true);
        } else if (itemViewType == 3) {
            TextView textView = (TextView) viewHolder.itemView;
            TLRPC$Chat currentChat = this.parentFragment.getCurrentChat();
            if (currentChat != null) {
                if (!ChatObject.hasAdminRights(currentChat) && (tLRPC$TL_chatBannedRights = currentChat.default_banned_rights) != null && tLRPC$TL_chatBannedRights.send_inline) {
                    i2 = R.string.GlobalAttachInlineRestricted;
                } else if (!AndroidUtilities.isBannedForever(currentChat.banned_rights)) {
                    formatString = LocaleController.formatString("AttachInlineRestricted", R.string.AttachInlineRestricted, LocaleController.formatDateForBan(currentChat.banned_rights.until_date));
                    textView.setText(formatString);
                } else {
                    i2 = R.string.AttachInlineRestrictedForever;
                }
                formatString = LocaleController.getString(i2);
                textView.setText(formatString);
            }
        } else if (itemViewType == 5) {
            QuickRepliesActivity.QuickReplyView quickReplyView = (QuickRepliesActivity.QuickReplyView) viewHolder.itemView;
            ArrayList arrayList = this.quickReplies;
            if (arrayList == null || i < 0 || i >= arrayList.size()) {
                return;
            }
            quickReplyView.set((QuickRepliesController.QuickReply) this.quickReplies.get(i), this.quickRepliesQuery, false);
        } else if (this.searchResultBotContext != null) {
            boolean z = (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? false : true;
            if (viewHolder.getItemViewType() != 2) {
                if (z) {
                    i--;
                }
                ((ContextLinkCell) viewHolder.itemView).setLink((TLRPC$BotInlineResult) this.searchResultBotContext.get(i), this.foundContextBot, this.contextMedia, i != this.searchResultBotContext.size() - 1, z && i == 0, "gif".equals(this.searchingContextUsername));
            } else if (z) {
                BotSwitchCell botSwitchCell = (BotSwitchCell) viewHolder.itemView;
                TLRPC$TL_inlineBotSwitchPM tLRPC$TL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
                botSwitchCell.setText(tLRPC$TL_inlineBotSwitchPM != null ? tLRPC$TL_inlineBotSwitchPM.text : this.searchResultBotWebViewSwitch.text);
            }
        } else {
            MentionCell mentionCell = (MentionCell) viewHolder.itemView;
            ArrayList arrayList2 = this.searchResultUsernames;
            if (arrayList2 != null) {
                TLObject tLObject = (TLObject) arrayList2.get(i);
                if (tLObject instanceof TLRPC$User) {
                    mentionCell.setUser((TLRPC$User) tLObject);
                } else if (tLObject instanceof TLRPC$Chat) {
                    mentionCell.setChat((TLRPC$Chat) tLObject);
                }
            } else {
                ArrayList arrayList3 = this.searchResultHashtags;
                if (arrayList3 != null) {
                    mentionCell.setText((String) arrayList3.get(i));
                } else {
                    ArrayList arrayList4 = this.searchResultSuggestions;
                    if (arrayList4 != null) {
                        mentionCell.setEmojiSuggestion((MediaDataController.KeywordResult) arrayList4.get(i));
                    } else {
                        ArrayList arrayList5 = this.searchResultCommands;
                        if (arrayList5 != null) {
                            String str = (String) arrayList5.get(i);
                            String str2 = (String) this.searchResultCommandsHelp.get(i);
                            ArrayList arrayList6 = this.searchResultCommandsUsers;
                            mentionCell.setBotCommand(str, str2, arrayList6 != null ? (TLRPC$User) arrayList6.get(i) : null);
                        }
                    }
                }
            }
            mentionCell.setDivider(false);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ContextLinkCell contextLinkCell;
        if (i == 0) {
            MentionCell mentionCell = new MentionCell(this.mContext, this.resourcesProvider);
            mentionCell.setIsDarkTheme(this.isDarkTheme);
            contextLinkCell = mentionCell;
        } else if (i == 1) {
            ContextLinkCell contextLinkCell2 = new ContextLinkCell(this.mContext);
            contextLinkCell2.setDelegate(new ContextLinkCell.ContextLinkCellDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda6
                @Override // org.telegram.ui.Cells.ContextLinkCell.ContextLinkCellDelegate
                public final void didPressedImage(ContextLinkCell contextLinkCell3) {
                    MentionsAdapter.this.lambda$onCreateViewHolder$10(contextLinkCell3);
                }
            });
            contextLinkCell = contextLinkCell2;
        } else if (i == 2) {
            contextLinkCell = new BotSwitchCell(this.mContext);
        } else if (i != 3) {
            contextLinkCell = i != 5 ? new StickerCell(this.mContext, this.resourcesProvider) : new QuickRepliesActivity.QuickReplyView(this.mContext, false, this.resourcesProvider);
        } else {
            TextView textView = new TextView(this.mContext);
            textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
            textView.setTextSize(1, 14.0f);
            textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
            contextLinkCell = textView;
        }
        return new RecyclerListView.Holder(contextLinkCell);
    }

    public void onDestroy() {
        SendMessagesHelper.LocationProvider locationProvider = this.locationProvider;
        if (locationProvider != null) {
            locationProvider.stop();
        }
        Runnable runnable = this.contextQueryRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.contextQueryRunnable = null;
        }
        if (this.contextUsernameReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextUsernameReqid, true);
            this.contextUsernameReqid = 0;
        }
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        this.foundContextBot = null;
        this.searchResultBotContextSwitch = null;
        this.inlineMediaEnabled = true;
        this.searchingContextUsername = null;
        this.searchingContextQuery = null;
        this.noUserName = false;
        if (!this.isDarkTheme) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadFailed);
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentDocumentsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        TLRPC$User tLRPC$User;
        if (i == 2 && (tLRPC$User = this.foundContextBot) != null && tLRPC$User.bot_inline_geo) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                onLocationUnavailable();
            } else {
                this.locationProvider.start();
            }
        }
    }

    public void searchForContextBotForNextOffset() {
        String str;
        TLRPC$User tLRPC$User;
        String str2;
        if (this.contextQueryReqid != 0 || (str = this.nextQueryOffset) == null || str.length() == 0 || (tLRPC$User = this.foundContextBot) == null || (str2 = this.searchingContextQuery) == null) {
            return;
        }
        searchForContextBotResults(true, tLRPC$User, str2, this.nextQueryOffset);
    }

    /* JADX WARN: Code restructure failed: missing block: B:224:0x03d5, code lost:
        if (r25.info != null) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03d7, code lost:
        if (r2 == 0) goto L483;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x03d9, code lost:
        r25.lastText = r0;
        r25.lastPosition = r27;
        r25.messages = r28;
        r25.delegate.needChangePanelVisibility(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x03e5, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x03e6, code lost:
        r25.resultStartPosition = r2;
        r25.resultLength = r14.length() + r15;
        r0 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0449, code lost:
        r25.resultStartPosition = r2;
        r25.resultLength = r14.length() + r15;
        r0 = -1;
        r3 = 65535;
        r4 = false;
        r6 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x06ed, code lost:
        if (r3 == false) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0709, code lost:
        if (r10.toLowerCase().startsWith(r5) == false) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0719, code lost:
        if (r3.toLowerCase().startsWith(r5) == false) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x0729, code lost:
        if (r4.toLowerCase().startsWith(r5) == false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0786, code lost:
        if (r4.toLowerCase().startsWith(r5) == false) goto L314;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0464  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x046f  */
    /* JADX WARN: Type inference failed for: r0v43, types: [org.telegram.ui.Adapters.MentionsAdapter$MentionsAdapterDelegate] */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r12v9 */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v15 */
    /* JADX WARN: Type inference failed for: r15v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v33, types: [boolean] */
    /* JADX WARN: Type inference failed for: r25v0, types: [org.telegram.ui.Adapters.MentionsAdapter] */
    /* JADX WARN: Type inference failed for: r3v25 */
    /* JADX WARN: Type inference failed for: r3v26, types: [androidx.collection.LongSparseArray, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r3v27 */
    /* renamed from: searchUsernameOrHashtag */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$searchUsernameOrHashtag$7(final CharSequence charSequence, final int i, final ArrayList arrayList, final boolean z, final boolean z2) {
        String str;
        String str2;
        StringBuilder sb;
        ?? r15;
        String str3;
        char c;
        char c2;
        String str4;
        String str5;
        char c3;
        boolean z3;
        int i2;
        int i3;
        int i4;
        ?? r3;
        ArrayList arrayList2;
        String str6;
        String str7;
        boolean z4;
        ArrayList arrayList3;
        TLRPC$Chat chat;
        long j;
        TLRPC$Chat chat2;
        long j2;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat tLRPC$Chat2;
        TLRPC$ChatFull tLRPC$ChatFull;
        ArrayList arrayList4;
        ArrayList arrayList5;
        TLRPC$Chat tLRPC$Chat3;
        long j3;
        String publicUsername;
        String str8;
        long j4;
        String str9;
        TLRPC$Chat tLRPC$Chat4;
        boolean z5;
        boolean z6;
        String str10;
        int i5;
        boolean z7;
        int i6;
        CharSequence concat;
        boolean z8 = z;
        boolean z9 = z2;
        String str11 = "";
        String charSequence2 = charSequence == null ? "" : charSequence.toString();
        TLRPC$Chat tLRPC$Chat5 = this.chat;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            tLRPC$Chat5 = chatActivity.getCurrentChat();
            this.parentFragment.getCurrentUser();
        }
        TLRPC$Chat tLRPC$Chat6 = tLRPC$Chat5;
        Runnable runnable = this.cancelDelayRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.cancelDelayRunnable = null;
        }
        if (this.channelReqId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.channelReqId, true);
            this.channelReqId = 0;
        }
        Runnable runnable2 = this.searchGlobalRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.searchGlobalRunnable = null;
        }
        Runnable runnable3 = this.checkAgainRunnable;
        if (runnable3 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable3);
            this.checkAgainRunnable = null;
        }
        if (TextUtils.isEmpty(charSequence2) || charSequence2.length() > MessagesController.getInstance(this.currentAccount).maxMessageLength) {
            searchForContextBot(null, null);
            this.delegate.needChangePanelVisibility(false);
            this.lastText = null;
            clearStickers();
            return;
        }
        int i7 = charSequence2.length() > 0 ? i - 1 : i;
        this.lastText = null;
        this.lastUsernameOnly = z8;
        this.lastForSearch = z9;
        StringBuilder sb2 = new StringBuilder();
        boolean z10 = !z8 && charSequence2.length() > 0 && charSequence2.length() <= 14;
        if (z10) {
            int length = charSequence2.length();
            String str12 = charSequence2;
            int i8 = 0;
            while (i8 < length) {
                char charAt = str12.charAt(i8);
                int i9 = length - 1;
                String str13 = str11;
                char charAt2 = i8 < i9 ? str12.charAt(i8 + 1) : (char) 0;
                if (i8 >= i9 || charAt != 55356 || charAt2 < 57339 || charAt2 > 57343) {
                    concat = str12;
                    if (charAt == 65039) {
                        i6 = 1;
                        concat = TextUtils.concat(str12.subSequence(0, i8), str12.subSequence(i8 + 1, str12.length()));
                        length--;
                        i8--;
                        i8 += i6;
                        str11 = str13;
                        str12 = concat;
                    }
                } else {
                    length -= 2;
                    i8--;
                    concat = TextUtils.concat(str12.subSequence(0, i8), str12.subSequence(i8 + 2, str12.length()));
                }
                i6 = 1;
                i8 += i6;
                str11 = str13;
                str12 = concat;
            }
            str = str11;
            this.lastSticker = str12.toString().trim();
            str2 = charSequence2;
        } else {
            str = "";
            str2 = str;
        }
        boolean z11 = z10 && (Emoji.isValidEmoji(str2) || Emoji.isValidEmoji(this.lastSticker));
        if (z11 && (charSequence instanceof Spanned)) {
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class);
            z11 = animatedEmojiSpanArr == null || animatedEmojiSpanArr.length == 0;
        }
        if (this.allowStickers && z11 && (tLRPC$Chat6 == null || ChatObject.canSendStickers(tLRPC$Chat6))) {
            this.stickersToLoad.clear();
            int i10 = SharedConfig.suggestStickers;
            if (i10 == 2 || !z11) {
                if (this.visibleByStickersSearch && i10 == 2) {
                    this.visibleByStickersSearch = false;
                    this.delegate.needChangePanelVisibility(false);
                    notifyDataSetChanged();
                    return;
                }
                return;
            }
            this.stickers = null;
            this.stickersMap = null;
            if (this.lastReqId != 0) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.lastReqId, true);
                z5 = false;
                this.lastReqId = 0;
            } else {
                z5 = false;
            }
            boolean z12 = MessagesController.getInstance(this.currentAccount).suggestStickersApiOnly;
            this.delayLocalResults = z5;
            if (z12) {
                z6 = z12;
                sb = sb2;
                str10 = charSequence2;
                i5 = 5;
                z7 = true;
            } else {
                z6 = z12;
                sb = sb2;
                i5 = 5;
                str10 = charSequence2;
                z7 = true;
                z7 = true;
                this.checkAgainRunnable = new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        MentionsAdapter.this.lambda$searchUsernameOrHashtag$7(charSequence, i, arrayList, z, z2);
                    }
                };
                MediaDataController.getInstance(this.currentAccount).loadRecents(0, false, true, false);
                MediaDataController.getInstance(this.currentAccount).loadRecents(2, false, true, false);
                final ArrayList<TLRPC$Document> recentStickersNoCopy = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(0);
                final ArrayList<TLRPC$Document> recentStickersNoCopy2 = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
                int min = Math.min(20, recentStickersNoCopy.size());
                int i11 = 0;
                for (int i12 = 0; i12 < min; i12++) {
                    TLRPC$Document tLRPC$Document = recentStickersNoCopy.get(i12);
                    if (isValidSticker(tLRPC$Document, this.lastSticker)) {
                        addStickerToResult(tLRPC$Document, "recent");
                        i11++;
                        if (i11 >= 5) {
                            break;
                        }
                    }
                }
                int size = recentStickersNoCopy2.size();
                for (int i13 = 0; i13 < size; i13++) {
                    TLRPC$Document tLRPC$Document2 = recentStickersNoCopy2.get(i13);
                    if (isValidSticker(tLRPC$Document2, this.lastSticker)) {
                        addStickerToResult(tLRPC$Document2, "fav");
                    }
                }
                MediaDataController.getInstance(this.currentAccount).checkStickers(0);
                HashMap<String, ArrayList<TLRPC$Document>> allStickers = MediaDataController.getInstance(this.currentAccount).getAllStickers();
                ArrayList<TLRPC$Document> arrayList6 = allStickers != null ? allStickers.get(this.lastSticker) : null;
                if (arrayList6 != null && !arrayList6.isEmpty()) {
                    addStickersToResult(arrayList6, null);
                }
                ArrayList arrayList7 = this.stickers;
                if (arrayList7 != null) {
                    Collections.sort(arrayList7, new Comparator() { // from class: org.telegram.ui.Adapters.MentionsAdapter.5
                        private int getIndex(StickerResult stickerResult) {
                            for (int i14 = 0; i14 < recentStickersNoCopy2.size(); i14++) {
                                if (((TLRPC$Document) recentStickersNoCopy2.get(i14)).id == stickerResult.sticker.id) {
                                    return i14 + 2000000;
                                }
                            }
                            for (int i15 = 0; i15 < Math.min(20, recentStickersNoCopy.size()); i15++) {
                                if (((TLRPC$Document) recentStickersNoCopy.get(i15)).id == stickerResult.sticker.id) {
                                    return (recentStickersNoCopy.size() - i15) + MediaController.VIDEO_BITRATE_480;
                                }
                            }
                            return -1;
                        }

                        @Override // java.util.Comparator
                        public int compare(StickerResult stickerResult, StickerResult stickerResult2) {
                            boolean isAnimatedStickerDocument = MessageObject.isAnimatedStickerDocument(stickerResult.sticker, true);
                            if (isAnimatedStickerDocument != MessageObject.isAnimatedStickerDocument(stickerResult2.sticker, true)) {
                                return isAnimatedStickerDocument ? -1 : 1;
                            }
                            int index = getIndex(stickerResult);
                            int index2 = getIndex(stickerResult2);
                            if (index > index2) {
                                return -1;
                            }
                            return index < index2 ? 1 : 0;
                        }
                    });
                }
            }
            if (SharedConfig.suggestStickers == 0 || z6) {
                searchServerStickers(this.lastSticker, str2);
            }
            ArrayList arrayList8 = this.stickers;
            if (arrayList8 != null && !arrayList8.isEmpty()) {
                if (SharedConfig.suggestStickers != 0 || this.stickers.size() >= i5) {
                    checkStickerFilesExistAndDownload();
                    this.delegate.needChangePanelVisibility(this.stickersToLoad.isEmpty());
                    this.visibleByStickersSearch = z7;
                } else {
                    this.delayLocalResults = z7;
                    this.delegate.needChangePanelVisibility(false);
                    this.visibleByStickersSearch = false;
                }
                notifyDataSetChanged();
            } else if (this.visibleByStickersSearch) {
                this.delegate.needChangePanelVisibility(false);
                this.visibleByStickersSearch = false;
                str3 = str10;
                c = ' ';
                c2 = 4;
                r15 = z7;
            }
            str3 = str10;
            c = ' ';
            c2 = 4;
            r15 = z7;
        } else {
            sb = sb2;
            String str14 = charSequence2;
            r15 = 1;
            if (z8 || !this.needBotContext) {
                str3 = str14;
            } else {
                str3 = str14;
                if (str3.charAt(0) == '@') {
                    c = ' ';
                    int indexOf = str3.indexOf(32);
                    int length2 = str3.length();
                    if (indexOf > 0) {
                        str5 = str3.substring(1, indexOf);
                        str4 = str3.substring(indexOf + 1);
                    } else if (str3.charAt(length2 - 1) == 't' && str3.charAt(length2 - 2) == 'o' && str3.charAt(length2 - 3) == 'b') {
                        str5 = str3.substring(1);
                        str4 = str;
                    } else {
                        searchForContextBot(null, null);
                        str4 = null;
                        str5 = null;
                    }
                    if (str5 != null && str5.length() >= 1) {
                        for (int i14 = 1; i14 < str5.length(); i14++) {
                            char charAt3 = str5.charAt(i14);
                            if ((charAt3 >= '0' && charAt3 <= '9') || ((charAt3 >= 'a' && charAt3 <= 'z') || ((charAt3 >= 'A' && charAt3 <= 'Z') || charAt3 == '_'))) {
                            }
                        }
                        searchForContextBot(str5, str4);
                        c2 = 65535;
                    }
                    str5 = str;
                    searchForContextBot(str5, str4);
                    c2 = 65535;
                }
            }
            c = ' ';
            searchForContextBot(null, null);
            c2 = 65535;
        }
        if (this.foundContextBot != null) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (!z8) {
            int i15 = i7;
            while (true) {
                if (i15 < 0) {
                    c3 = 65535;
                    z3 = false;
                    i2 = -1;
                    break;
                }
                if (i15 >= str3.length()) {
                    i4 = -1;
                } else {
                    char charAt4 = str3.charAt(i15);
                    if (i15 != 0) {
                        int i16 = i15 - 1;
                        if (str3.charAt(i16) != c && str3.charAt(i16) != '\n' && charAt4 != ':') {
                            i3 = 0;
                            sb.insert(i3, charAt4);
                            i4 = -1;
                        }
                    }
                    if (charAt4 == '@') {
                        boolean z13 = this.searchInDailogs;
                        if (z13 || this.needUsernames || (this.needBotContext && i15 == 0)) {
                            break;
                        }
                    } else if (charAt4 != '#') {
                        if (i15 != 0 || this.botInfo == null || charAt4 != '/') {
                            if (charAt4 == ':' && sb.length() > 0 && (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(sb.charAt(0)) < 0 || sb.length() > r15)) {
                                break;
                            }
                        } else {
                            this.resultStartPosition = i15;
                            this.resultLength = sb.length() + r15;
                            i2 = -1;
                            c3 = 65535;
                            z3 = false;
                            c2 = 2;
                            break;
                        }
                    } else if (!this.searchAdapterHelper.loadRecentHashtags()) {
                        this.lastText = str3;
                        this.lastPosition = i;
                        this.messages = arrayList;
                        return;
                    } else {
                        this.resultStartPosition = i15;
                        this.resultLength = sb.length() + r15;
                        sb.insert(0, charAt4);
                        i2 = -1;
                        c3 = 65535;
                        z3 = false;
                        c2 = 1;
                    }
                    i3 = 0;
                    sb.insert(i3, charAt4);
                    i4 = -1;
                }
                i15 += i4;
            }
            if (c2 != c3) {
                this.contextMedia = z3;
                this.searchResultBotContext = null;
                this.delegate.needChangePanelVisibility(z3);
                return;
            } else if (c2 != 0) {
                if (c2 == r15) {
                    ArrayList arrayList9 = new ArrayList();
                    String lowerCase = sb.toString().toLowerCase();
                    ArrayList hashtags = this.searchAdapterHelper.getHashtags();
                    for (int i17 = 0; i17 < hashtags.size(); i17 += r15) {
                        SearchAdapterHelper.HashtagObject hashtagObject = (SearchAdapterHelper.HashtagObject) hashtags.get(i17);
                        if (hashtagObject != null && (str7 = hashtagObject.hashtag) != null && str7.startsWith(lowerCase)) {
                            arrayList9.add(hashtagObject.hashtag);
                        }
                    }
                    this.searchResultHashtags = arrayList9;
                    this.stickers = null;
                    this.searchResultUsernames = null;
                    this.searchResultUsernamesMap = null;
                    this.quickReplies = null;
                    this.searchResultCommands = null;
                    this.searchResultCommandsHelp = null;
                    this.searchResultCommandsUsers = null;
                    this.searchResultSuggestions = null;
                    this.contextMedia = false;
                    this.searchResultBotContext = null;
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility(this.searchResultHashtags.isEmpty() ^ r15);
                    return;
                } else if (c2 != 2) {
                    if (c2 == 3) {
                        String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                        if (!Arrays.equals(currentKeyboardLanguage, this.lastSearchKeyboardLanguage)) {
                            MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                        }
                        this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, sb.toString(), false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda5
                            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
                            public final void run(ArrayList arrayList10, String str15) {
                                MentionsAdapter.this.lambda$searchUsernameOrHashtag$9(arrayList10, str15);
                            }
                        }, SharedConfig.suggestAnimatedEmoji && UserConfig.getInstance(this.currentAccount).isPremium());
                        return;
                    } else if (c2 == 4) {
                        this.searchResultHashtags = null;
                        this.searchResultUsernames = null;
                        this.searchResultUsernamesMap = null;
                        this.searchResultSuggestions = null;
                        this.searchResultCommands = null;
                        this.quickReplies = null;
                        this.searchResultCommandsHelp = null;
                        this.searchResultCommandsUsers = null;
                        return;
                    } else {
                        return;
                    }
                } else {
                    ArrayList arrayList10 = new ArrayList();
                    ArrayList arrayList11 = new ArrayList();
                    ArrayList arrayList12 = new ArrayList();
                    String lowerCase2 = sb.toString().toLowerCase();
                    for (int i18 = 0; i18 < this.botInfo.size(); i18 += r15) {
                        TL_bots$BotInfo tL_bots$BotInfo = (TL_bots$BotInfo) this.botInfo.valueAt(i18);
                        for (int i19 = 0; i19 < tL_bots$BotInfo.commands.size(); i19 += r15) {
                            TLRPC$TL_botCommand tLRPC$TL_botCommand = (TLRPC$TL_botCommand) tL_bots$BotInfo.commands.get(i19);
                            if (tLRPC$TL_botCommand != null && (str6 = tLRPC$TL_botCommand.command) != null && str6.startsWith(lowerCase2)) {
                                arrayList10.add("/" + tLRPC$TL_botCommand.command);
                                arrayList11.add(tLRPC$TL_botCommand.description);
                                arrayList12.add(messagesController.getUser(Long.valueOf(tL_bots$BotInfo.user_id)));
                            }
                        }
                    }
                    if (this.parentFragment == null || DialogObject.isEncryptedDialog(this.dialog_id) || this.parentFragment.getChatMode() != 0 || this.parentFragment.getCurrentUser() == null || this.parentFragment.getCurrentUser().bot || UserObject.isReplyUser(this.parentFragment.getCurrentUser()) || UserObject.isService(this.parentFragment.getCurrentUser().id)) {
                        r3 = 0;
                        this.quickRepliesQuery = null;
                        this.quickReplies = null;
                    } else {
                        QuickRepliesController quickRepliesController = QuickRepliesController.getInstance(this.currentAccount);
                        quickRepliesController.load();
                        this.quickRepliesQuery = lowerCase2;
                        this.quickReplies = new ArrayList();
                        for (int i20 = 0; i20 < quickRepliesController.replies.size(); i20 += r15) {
                            QuickRepliesController.QuickReply quickReply = (QuickRepliesController.QuickReply) quickRepliesController.replies.get(i20);
                            if (!quickReply.isSpecial() && quickReply.name.startsWith(lowerCase2)) {
                                this.quickReplies.add(quickReply);
                            }
                        }
                        r3 = 0;
                    }
                    this.searchResultHashtags = r3;
                    this.stickers = r3;
                    this.searchResultUsernames = r3;
                    this.searchResultUsernamesMap = r3;
                    this.searchResultSuggestions = r3;
                    this.searchResultCommands = arrayList10;
                    this.searchResultCommandsHelp = arrayList11;
                    this.searchResultCommandsUsers = arrayList12;
                    this.contextMedia = false;
                    this.searchResultBotContext = r3;
                    notifyDataSetChanged();
                    this.delegate.needChangePanelVisibility((arrayList10.isEmpty() && ((arrayList2 = this.quickReplies) == null || arrayList2.isEmpty())) ? false : true);
                    return;
                }
            } else {
                this.contextMedia = z3;
                this.searchResultBotContext = null;
                ArrayList arrayList13 = new ArrayList();
                if (arrayList != null) {
                    for (int i21 = 0; i21 < Math.min(100, arrayList.size()); i21 += r15) {
                        long fromChatId = ((MessageObject) arrayList.get(i21)).getFromChatId();
                        if (fromChatId > 0 && !arrayList13.contains(Long.valueOf(fromChatId))) {
                            arrayList13.add(Long.valueOf(fromChatId));
                        }
                    }
                }
                String lowerCase3 = sb.toString().toLowerCase();
                boolean z14 = lowerCase3.indexOf(c) >= 0;
                ArrayList arrayList14 = new ArrayList();
                LongSparseArray longSparseArray = new LongSparseArray();
                final LongSparseArray longSparseArray2 = new LongSparseArray();
                ArrayList<TLRPC$TL_topPeer> arrayList15 = MediaDataController.getInstance(this.currentAccount).inlineBots;
                if (!z8 && this.needBotContext && i2 == 0 && !arrayList15.isEmpty()) {
                    int i22 = 0;
                    int i23 = 0;
                    while (i22 < arrayList15.size()) {
                        TLRPC$User user = messagesController.getUser(Long.valueOf(arrayList15.get(i22).peer.user_id));
                        if (user != null) {
                            String publicUsername2 = UserObject.getPublicUsername(user);
                            if (TextUtils.isEmpty(publicUsername2) || !(lowerCase3.length() == 0 || publicUsername2.toLowerCase().startsWith(lowerCase3))) {
                                z4 = z14;
                                arrayList3 = arrayList14;
                            } else {
                                arrayList14.add(user);
                                z4 = z14;
                                arrayList3 = arrayList14;
                                longSparseArray.put(user.id, user);
                                longSparseArray2.put(user.id, user);
                                i23 += r15;
                            }
                            if (i23 == 5) {
                                break;
                            }
                        } else {
                            z4 = z14;
                            arrayList3 = arrayList14;
                        }
                        i22 += r15;
                        z14 = z4;
                        arrayList14 = arrayList3;
                    }
                }
                z4 = z14;
                arrayList3 = arrayList14;
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2 != null) {
                    TLRPC$Chat currentChat = chatActivity2.getCurrentChat();
                    j = this.parentFragment.getThreadId();
                    chat = currentChat;
                } else {
                    TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
                    chat = tLRPC$ChatFull2 != null ? messagesController.getChat(Long.valueOf(tLRPC$ChatFull2.id)) : tLRPC$Chat6;
                    j = 0;
                }
                TLRPC$User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                if (chat != null && (tLRPC$ChatFull = this.info) != null && tLRPC$ChatFull.participants != null && (!ChatObject.isChannel(chat) || chat.megagroup)) {
                    int i24 = -2;
                    while (i24 < this.info.participants.participants.size()) {
                        if (i24 == -2) {
                            if (currentUser != null && z8) {
                                String str15 = currentUser.first_name;
                                String str16 = currentUser.last_name;
                                publicUsername = UserObject.getPublicUsername(currentUser);
                                arrayList5 = arrayList13;
                                j3 = j;
                                j4 = currentUser.id;
                                str9 = str15;
                                str8 = str16;
                                arrayList4 = arrayList3;
                                tLRPC$Chat4 = currentUser;
                                tLRPC$Chat3 = chat;
                                if ((!TextUtils.isEmpty(publicUsername) && publicUsername.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str9) && str9.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str8) && str8.toLowerCase().startsWith(lowerCase3)) || (z4 && ContactsController.formatName(str9, str8).toLowerCase().startsWith(lowerCase3))))) {
                                    arrayList4.add(tLRPC$Chat4);
                                    longSparseArray2.put(j4, tLRPC$Chat4);
                                }
                            }
                            arrayList4 = arrayList3;
                            arrayList5 = arrayList13;
                            tLRPC$Chat3 = chat;
                            j3 = j;
                        } else {
                            if (i24 == -1) {
                                if (z9) {
                                    if (lowerCase3.length() == 0) {
                                        arrayList4 = arrayList3;
                                        arrayList4.add(chat);
                                        arrayList5 = arrayList13;
                                        tLRPC$Chat3 = chat;
                                        j3 = j;
                                    } else {
                                        arrayList4 = arrayList3;
                                        str9 = chat.title;
                                        publicUsername = ChatObject.getPublicUsername(chat);
                                        tLRPC$Chat3 = chat;
                                        j3 = j;
                                        j4 = -chat.id;
                                        str8 = null;
                                        arrayList5 = arrayList13;
                                        tLRPC$Chat4 = tLRPC$Chat3;
                                    }
                                }
                                arrayList4 = arrayList3;
                                arrayList5 = arrayList13;
                                tLRPC$Chat3 = chat;
                                j3 = j;
                            } else {
                                arrayList4 = arrayList3;
                                TLRPC$ChatParticipant tLRPC$ChatParticipant = (TLRPC$ChatParticipant) this.info.participants.participants.get(i24);
                                if (currentUser != null) {
                                    j3 = j;
                                    arrayList5 = arrayList13;
                                    tLRPC$Chat3 = chat;
                                    if (tLRPC$ChatParticipant.user_id == currentUser.id) {
                                    }
                                } else {
                                    arrayList5 = arrayList13;
                                    tLRPC$Chat3 = chat;
                                    j3 = j;
                                }
                                TLRPC$User user2 = messagesController.getUser(Long.valueOf(tLRPC$ChatParticipant.user_id));
                                if (user2 != null && !UserObject.isUserSelf(user2) && longSparseArray.indexOfKey(user2.id) < 0) {
                                    if (lowerCase3.length() != 0 || user2.deleted) {
                                        String str17 = user2.first_name;
                                        String str18 = user2.last_name;
                                        publicUsername = UserObject.getPublicUsername(user2);
                                        str8 = str18;
                                        j4 = user2.id;
                                        str9 = str17;
                                        tLRPC$Chat4 = user2;
                                    } else {
                                        arrayList4.add(user2);
                                    }
                                }
                            }
                            if (!TextUtils.isEmpty(publicUsername)) {
                                arrayList4.add(tLRPC$Chat4);
                                longSparseArray2.put(j4, tLRPC$Chat4);
                            }
                            arrayList4.add(tLRPC$Chat4);
                            longSparseArray2.put(j4, tLRPC$Chat4);
                        }
                        i24 += r15;
                        chat = tLRPC$Chat3;
                        z8 = z;
                        arrayList13 = arrayList5;
                        arrayList3 = arrayList4;
                        j = j3;
                        z9 = z2;
                    }
                }
                final ArrayList arrayList16 = arrayList3;
                final ArrayList arrayList17 = arrayList13;
                TLRPC$Chat tLRPC$Chat7 = chat;
                long j5 = j;
                if (this.searchInDailogs) {
                    ArrayList<TLRPC$Dialog> allDialogs = MessagesController.getInstance(this.currentAccount).getAllDialogs();
                    for (int i25 = 0; i25 < allDialogs.size(); i25 += r15) {
                        if (allDialogs.get(i25).id > 0) {
                            TLRPC$User user3 = messagesController.getUser(Long.valueOf(allDialogs.get(i25).id));
                            if (user3 != null && !UserObject.isUserSelf(user3) && longSparseArray.indexOfKey(user3.id) < 0) {
                                if (lowerCase3.length() == 0) {
                                    boolean z15 = user3.deleted;
                                    tLRPC$Chat2 = user3;
                                }
                                String str19 = user3.first_name;
                                String str20 = user3.last_name;
                                String publicUsername3 = UserObject.getPublicUsername(user3);
                                j2 = user3.id;
                                if (!TextUtils.isEmpty(publicUsername3)) {
                                    tLRPC$Chat = user3;
                                }
                                if (!TextUtils.isEmpty(str19)) {
                                    tLRPC$Chat = user3;
                                }
                                if (!TextUtils.isEmpty(str20)) {
                                    tLRPC$Chat = user3;
                                }
                                if (z4) {
                                    tLRPC$Chat = user3;
                                    if (!ContactsController.formatName(str19, str20).toLowerCase().startsWith(lowerCase3)) {
                                    }
                                    arrayList16.add(tLRPC$Chat);
                                    longSparseArray2.put(j2, tLRPC$Chat);
                                }
                            }
                        } else if (!TextUtils.isEmpty(lowerCase3) && (chat2 = messagesController.getChat(Long.valueOf(-allDialogs.get(i25).id))) != null && chat2.username != null && longSparseArray.indexOfKey(chat2.id) < 0) {
                            tLRPC$Chat2 = chat2;
                            if (lowerCase3.length() != 0) {
                                String str21 = chat2.title;
                                String str22 = chat2.username;
                                j2 = chat2.id;
                                if (!TextUtils.isEmpty(str22)) {
                                    tLRPC$Chat = chat2;
                                }
                                if (!TextUtils.isEmpty(str21)) {
                                    tLRPC$Chat = chat2;
                                    if (!str21.toLowerCase().startsWith(lowerCase3)) {
                                    }
                                    arrayList16.add(tLRPC$Chat);
                                    longSparseArray2.put(j2, tLRPC$Chat);
                                }
                            }
                            arrayList16.add(tLRPC$Chat2);
                        }
                    }
                }
                Collections.sort(arrayList16, new Comparator() { // from class: org.telegram.ui.Adapters.MentionsAdapter.6
                    private long getId(TLObject tLObject) {
                        return tLObject instanceof TLRPC$User ? ((TLRPC$User) tLObject).id : -((TLRPC$Chat) tLObject).id;
                    }

                    @Override // java.util.Comparator
                    public int compare(TLObject tLObject, TLObject tLObject2) {
                        long id = getId(tLObject);
                        long id2 = getId(tLObject2);
                        if (longSparseArray2.indexOfKey(id) < 0 || longSparseArray2.indexOfKey(id2) < 0) {
                            if (longSparseArray2.indexOfKey(id) >= 0) {
                                return -1;
                            }
                            if (longSparseArray2.indexOfKey(id2) >= 0) {
                                return 1;
                            }
                            int indexOf2 = arrayList17.indexOf(Long.valueOf(id));
                            int indexOf3 = arrayList17.indexOf(Long.valueOf(id2));
                            if (indexOf2 != -1 && indexOf3 != -1) {
                                if (indexOf2 < indexOf3) {
                                    return -1;
                                }
                                return indexOf2 == indexOf3 ? 0 : 1;
                            } else if (indexOf2 == -1 || indexOf3 != -1) {
                                return (indexOf2 != -1 || indexOf3 == -1) ? 0 : 1;
                            } else {
                                return -1;
                            }
                        }
                        return 0;
                    }
                });
                this.searchResultHashtags = null;
                this.stickers = null;
                this.quickReplies = null;
                this.searchResultCommands = null;
                this.searchResultCommandsHelp = null;
                this.searchResultCommandsUsers = null;
                this.searchResultSuggestions = null;
                if (((tLRPC$Chat7 == null || !tLRPC$Chat7.megagroup) && !this.searchInDailogs) || lowerCase3.length() <= 0) {
                    showUsersResult(arrayList16, longSparseArray2, r15);
                    return;
                }
                if (arrayList16.size() < 5) {
                    Runnable runnable4 = new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            MentionsAdapter.this.lambda$searchUsernameOrHashtag$8(arrayList16, longSparseArray2);
                        }
                    };
                    this.cancelDelayRunnable = runnable4;
                    AndroidUtilities.runOnUIThread(runnable4, 1000L);
                } else {
                    showUsersResult(arrayList16, longSparseArray2, r15);
                }
                7 r10 = new 7(tLRPC$Chat7, lowerCase3, j5, arrayList16, longSparseArray2, messagesController);
                this.searchGlobalRunnable = r10;
                AndroidUtilities.runOnUIThread(r10, 200L);
                return;
            }
        }
        sb.append(str3.substring(r15));
        this.resultStartPosition = 0;
        this.resultLength = sb.length();
        i2 = -1;
        c3 = 65535;
        z3 = false;
        c2 = 0;
        if (c2 != c3) {
        }
    }

    public void setAllowBots(boolean z) {
        this.allowBots = z;
    }

    public void setAllowChats(boolean z) {
        this.allowChats = z;
    }

    public void setAllowStickers(boolean z) {
        this.allowStickers = z;
    }

    public void setBotInfo(LongSparseArray longSparseArray) {
        this.botInfo = longSparseArray;
    }

    public void setBotsCount(int i) {
        this.botsCount = i;
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        ChatActivity chatActivity;
        TLRPC$Chat currentChat;
        this.currentAccount = UserConfig.selectedAccount;
        this.info = tLRPC$ChatFull;
        if (!this.inlineMediaEnabled && this.foundContextBot != null && (chatActivity = this.parentFragment) != null && (currentChat = chatActivity.getCurrentChat()) != null) {
            boolean canSendStickers = ChatObject.canSendStickers(currentChat);
            this.inlineMediaEnabled = canSendStickers;
            if (canSendStickers) {
                this.searchResultUsernames = null;
                notifyDataSetChanged();
                this.delegate.needChangePanelVisibility(false);
                processFoundUser(this.foundContextBot);
            }
        }
        String str = this.lastText;
        if (str != null) {
            lambda$searchUsernameOrHashtag$7(str, this.lastPosition, this.messages, this.lastUsernameOnly, this.lastForSearch);
        }
    }

    public void setDialogId(long j) {
        this.dialog_id = j;
    }

    public void setIsReversed(boolean z) {
        if (this.isReversed != z) {
            this.isReversed = z;
            int lastItemCount = getLastItemCount();
            if (lastItemCount > 0) {
                notifyItemChanged(0);
            }
            if (lastItemCount > 1) {
                notifyItemChanged(lastItemCount - 1);
            }
        }
    }

    public void setNeedBotContext(boolean z) {
        this.needBotContext = z;
    }

    public void setNeedUsernames(boolean z) {
        this.needUsernames = z;
    }

    public void setParentFragment(ChatActivity chatActivity) {
        this.parentFragment = chatActivity;
    }

    public void setSearchInDailogs(boolean z) {
        this.searchInDailogs = z;
    }

    public void setSearchingMentions(boolean z) {
        this.isSearchingMentions = z;
    }

    public void setUserOrChat(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        this.user = tLRPC$User;
        this.chat = tLRPC$Chat;
    }
}
