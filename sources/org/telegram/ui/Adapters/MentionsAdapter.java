package org.telegram.ui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_bots;
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
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.PremiumPreviewFragment;

/* loaded from: classes4.dex */
public class MentionsAdapter extends RecyclerListView.SelectionAdapter implements NotificationCenter.NotificationCenterDelegate {
    private LongSparseArray botInfo;
    private int botsCount;
    private HashtagHint bottomHint;
    private Runnable cancelDelayRunnable;
    private int channelLastReqId;
    private int channelReqId;
    public TLRPC.Chat chat;
    private Runnable checkAgainRunnable;
    private boolean contextMedia;
    private int contextQueryReqid;
    private Runnable contextQueryRunnable;
    private int contextUsernameReqid;
    private boolean delayLocalResults;
    private MentionsAdapterDelegate delegate;
    private long dialog_id;
    private TLRPC.User foundContextBot;
    private String hintHashtag;
    private boolean hintHashtagDivider;
    private TLRPC.ChatFull info;
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
    private final Context mContext;
    private EmojiView.ChooseStickerActionTracker mentionsStickersActionTracker;
    private ArrayList messages;
    private String nextQueryOffset;
    private boolean noUserName;
    public ChatActivity parentFragment;
    private ArrayList quickReplies;
    private String quickRepliesQuery;
    private final Theme.ResourcesProvider resourcesProvider;
    private int resultLength;
    private int resultStartPosition;
    private SearchAdapterHelper searchAdapterHelper;
    private Runnable searchGlobalRunnable;
    private ArrayList searchResultBotContext;
    private TLRPC.TL_inlineBotSwitchPM searchResultBotContextSwitch;
    private long searchResultBotContextSwitchUserId;
    private TLRPC.TL_inlineBotWebView searchResultBotWebViewSwitch;
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
    private final boolean stories;
    private long threadMessageId;
    private HashtagHint topHint;
    private TLRPC.User user;
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
        public /* synthetic */ void lambda$run$0(String str, TLRPC.TL_error tL_error, TLObject tLObject, MessagesController messagesController, MessagesStorage messagesStorage) {
            if (MentionsAdapter.this.searchingContextUsername == null || !MentionsAdapter.this.searchingContextUsername.equals(str)) {
                return;
            }
            TLRPC.User user = null;
            if (tL_error == null) {
                TLRPC.TL_contacts_resolvedPeer tL_contacts_resolvedPeer = (TLRPC.TL_contacts_resolvedPeer) tLObject;
                if (!tL_contacts_resolvedPeer.users.isEmpty()) {
                    TLRPC.User user2 = tL_contacts_resolvedPeer.users.get(0);
                    messagesController.putUser(user2, false);
                    messagesStorage.putUsersAndChats(tL_contacts_resolvedPeer.users, null, true, true);
                    user = user2;
                }
            }
            MentionsAdapter.this.processFoundUser(user);
            MentionsAdapter.this.contextUsernameReqid = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$1(final String str, final MessagesController messagesController, final MessagesStorage messagesStorage, final TLObject tLObject, final TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.4.this.lambda$run$0(str, tL_error, tLObject, messagesController, messagesStorage);
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
            if (userOrChat instanceof TLRPC.User) {
                MentionsAdapter.this.processFoundUser((TLRPC.User) userOrChat);
                return;
            }
            TLRPC.TL_contacts_resolveUsername tL_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
            tL_contacts_resolveUsername.username = MentionsAdapter.this.searchingContextUsername;
            MentionsAdapter mentionsAdapter2 = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter2.currentAccount);
            final String str = this.val$username;
            final MessagesController messagesController = this.val$messagesController;
            final MessagesStorage messagesStorage = this.val$messagesStorage;
            mentionsAdapter2.contextUsernameReqid = connectionsManager.sendRequest(tL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$4$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MentionsAdapter.4.this.lambda$run$1(str, messagesController, messagesStorage, tLObject, tL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 7 implements Runnable {
        final /* synthetic */ TLRPC.Chat val$chat;
        final /* synthetic */ MessagesController val$messagesController;
        final /* synthetic */ LongSparseArray val$newMap;
        final /* synthetic */ ArrayList val$newResult;
        final /* synthetic */ long val$threadId;
        final /* synthetic */ String val$usernameString;

        7(TLRPC.Chat chat, String str, long j, ArrayList arrayList, LongSparseArray longSparseArray, MessagesController messagesController) {
            this.val$chat = chat;
            this.val$usernameString = str;
            this.val$threadId = j;
            this.val$newResult = arrayList;
            this.val$newMap = longSparseArray;
            this.val$messagesController = messagesController;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int i, ArrayList arrayList, LongSparseArray longSparseArray, TLRPC.TL_error tL_error, TLObject tLObject, MessagesController messagesController) {
            TLObject chat;
            if (MentionsAdapter.this.channelReqId != 0 && i == MentionsAdapter.this.channelLastReqId && MentionsAdapter.this.searchResultUsernamesMap != null && MentionsAdapter.this.searchResultUsernames != null) {
                MentionsAdapter.this.showUsersResult(arrayList, longSparseArray, false);
                if (tL_error == null) {
                    TLRPC.TL_channels_channelParticipants tL_channels_channelParticipants = (TLRPC.TL_channels_channelParticipants) tLObject;
                    messagesController.putUsers(tL_channels_channelParticipants.users, false);
                    messagesController.putChats(tL_channels_channelParticipants.chats, false);
                    MentionsAdapter.this.searchResultUsernames.isEmpty();
                    if (!tL_channels_channelParticipants.participants.isEmpty()) {
                        long clientUserId = UserConfig.getInstance(MentionsAdapter.this.currentAccount).getClientUserId();
                        for (int i2 = 0; i2 < tL_channels_channelParticipants.participants.size(); i2++) {
                            long peerId = MessageObject.getPeerId(tL_channels_channelParticipants.participants.get(i2).peer);
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
        public /* synthetic */ void lambda$run$1(final int i, final ArrayList arrayList, final LongSparseArray longSparseArray, final MessagesController messagesController, final TLObject tLObject, final TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MentionsAdapter.7.this.lambda$run$0(i, arrayList, longSparseArray, tL_error, tLObject, messagesController);
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            if (MentionsAdapter.this.searchGlobalRunnable != this) {
                return;
            }
            TLRPC.TL_channels_getParticipants tL_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
            tL_channels_getParticipants.channel = MessagesController.getInputChannel(this.val$chat);
            tL_channels_getParticipants.limit = 20;
            tL_channels_getParticipants.offset = 0;
            TLRPC.TL_channelParticipantsMentions tL_channelParticipantsMentions = new TLRPC.TL_channelParticipantsMentions();
            int i = tL_channelParticipantsMentions.flags;
            tL_channelParticipantsMentions.flags = i | 1;
            tL_channelParticipantsMentions.q = this.val$usernameString;
            long j = this.val$threadId;
            if (j != 0) {
                tL_channelParticipantsMentions.flags = i | 3;
                tL_channelParticipantsMentions.top_msg_id = (int) j;
            }
            tL_channels_getParticipants.filter = tL_channelParticipantsMentions;
            final int access$1704 = MentionsAdapter.access$1704(MentionsAdapter.this);
            MentionsAdapter mentionsAdapter = MentionsAdapter.this;
            ConnectionsManager connectionsManager = ConnectionsManager.getInstance(mentionsAdapter.currentAccount);
            final ArrayList arrayList = this.val$newResult;
            final LongSparseArray longSparseArray = this.val$newMap;
            final MessagesController messagesController = this.val$messagesController;
            mentionsAdapter.channelReqId = connectionsManager.sendRequest(tL_channels_getParticipants, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$7$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MentionsAdapter.7.this.lambda$run$1(access$1704, arrayList, longSparseArray, messagesController, tLObject, tL_error);
                }
            });
        }
    }

    /* loaded from: classes4.dex */
    public static class HashtagHint extends LinearLayout {
        private final AvatarDrawable avatarDrawable;
        private final BackupImageView imageView;
        private final Theme.ResourcesProvider resourcesProvider;
        private final LinearLayout textLayout;
        private final TextView textView;
        private final TextView titleView;
        private final boolean transparent;

        public HashtagHint(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.avatarDrawable = new AvatarDrawable();
            this.resourcesProvider = resourcesProvider;
            this.transparent = z;
            setOrientation(0);
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(28.0f));
            addView(backupImageView, LayoutHelper.createLinear(28, 28, 19, 12, 0, 12, 0));
            LinearLayout linearLayout = new LinearLayout(context);
            this.textLayout = linearLayout;
            linearLayout.setOrientation(1);
            addView(linearLayout, LayoutHelper.createLinear(-1, -2, 55, 0, 4, 12, 4));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 15.0f);
            int i = Theme.key_windowBackgroundWhiteBlackText;
            textView.setTextColor(Theme.getColor(i, resourcesProvider));
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
            TextView textView2 = new TextView(context);
            this.textView = textView2;
            textView2.setTextSize(1, 13.0f);
            textView2.setTextColor(z ? Theme.multAlpha(Theme.getColor(i, resourcesProvider), 0.5f) : Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider));
            linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2));
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
        }

        public void set(int i, String str, TLRPC.Chat chat) {
            TextView textView;
            int i2;
            if (str == null) {
                return;
            }
            if (i == 0) {
                CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(28.0f), Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider)), getContext().getResources().getDrawable(R.drawable.menu_hashtag).mutate());
                combinedDrawable.setIconOffset(AndroidUtilities.dp(-0.66f), 0);
                combinedDrawable.setIconSize(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(20.0f));
                this.imageView.setImageDrawable(combinedDrawable);
                this.titleView.setText(LocaleController.formatString(R.string.HashtagSuggestion1Title, str));
                textView = this.textView;
                i2 = R.string.HashtagSuggestion1Text;
            } else {
                this.avatarDrawable.setInfo(chat);
                this.imageView.setForUserOrChat(chat, this.avatarDrawable);
                this.titleView.setText(PremiumPreviewFragment.applyNewSpan(LocaleController.formatString(R.string.HashtagSuggestion2Title, str + "@" + ChatObject.getPublicUsername(chat)), 8));
                textView = this.textView;
                i2 = R.string.HashtagSuggestion2Text;
            }
            textView.setText(LocaleController.getString(i2));
        }
    }

    /* loaded from: classes4.dex */
    public interface MentionsAdapterDelegate {
        void needChangePanelVisibility(boolean z);

        void onContextClick(TLRPC.BotInlineResult botInlineResult);

        void onContextSearch(boolean z);

        void onItemCountUpdate(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class StickerResult {
        public Object parent;
        public TLRPC.Document sticker;

        public StickerResult(TLRPC.Document document, Object obj) {
            this.sticker = document;
            this.parent = obj;
        }
    }

    public MentionsAdapter(Context context, boolean z, long j, long j2, MentionsAdapterDelegate mentionsAdapterDelegate, Theme.ResourcesProvider resourcesProvider, boolean z2) {
        this.resourcesProvider = resourcesProvider;
        this.mContext = context;
        this.delegate = mentionsAdapterDelegate;
        this.isDarkTheme = z;
        this.dialog_id = j;
        this.stories = z2;
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

    private void addStickerToResult(TLRPC.Document document, Object obj) {
        if (document == null) {
            return;
        }
        String str = document.dc_id + "_" + document.id;
        HashMap hashMap = this.stickersMap;
        if (hashMap == null || !hashMap.containsKey(str)) {
            if (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(document)) {
                if (this.stickers == null) {
                    this.stickers = new ArrayList();
                    this.stickersMap = new HashMap();
                }
                this.stickers.add(new StickerResult(document, obj));
                this.stickersMap.put(str, document);
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
            TLRPC.Document document = (TLRPC.Document) arrayList.get(i);
            String str = document.dc_id + "_" + document.id;
            HashMap hashMap = this.stickersMap;
            if ((hashMap == null || !hashMap.containsKey(str)) && (UserConfig.getInstance(this.currentAccount).isPremium() || !MessageObject.isPremiumSticker(document))) {
                int size2 = document.attributes.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size2) {
                        break;
                    }
                    TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i2);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                        obj = documentAttribute.stickerset;
                        break;
                    }
                    i2++;
                }
                if (this.stickers == null) {
                    this.stickers = new ArrayList();
                    this.stickersMap = new HashMap();
                }
                this.stickers.add(new StickerResult(document, obj));
                this.stickersMap.put(str, document);
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
        TLRPC.User user = this.foundContextBot;
        if (user == null || !user.bot_inline_geo) {
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
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(stickerResult.sticker.thumbs, 90);
            if (((closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) || (closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeProgressive)) && !FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, "webp", true).exists()) {
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

    private boolean isValidSticker(TLRPC.Document document, String str) {
        int size = document.attributes.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                String str2 = documentAttribute.alt;
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
        if ((obj instanceof TLRPC.User) && (obj2 instanceof TLRPC.User) && ((TLRPC.User) obj).id == ((TLRPC.User) obj2).id) {
            return true;
        }
        if ((obj instanceof TLRPC.Chat) && (obj2 instanceof TLRPC.Chat) && ((TLRPC.Chat) obj).id == ((TLRPC.Chat) obj2).id) {
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
    public /* synthetic */ void lambda$processFoundUser$2(boolean[] zArr, TLRPC.User user, DialogInterface dialogInterface, int i) {
        zArr[0] = true;
        if (user != null) {
            MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean("inlinegeo_" + user.id, true).commit();
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
    public /* synthetic */ void lambda$searchForContextBotResults$5(String str, boolean z, TLObject tLObject, TLRPC.User user, String str2, MessagesStorage messagesStorage, String str3) {
        boolean z2;
        if (str.equals(this.searchingContextQuery)) {
            this.contextQueryReqid = 0;
            if (z && tLObject == null) {
                searchForContextBotResults(false, user, str, str2);
            } else {
                MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
                if (mentionsAdapterDelegate != null) {
                    mentionsAdapterDelegate.onContextSearch(false);
                }
            }
            if (tLObject instanceof TLRPC.TL_messages_botResults) {
                TLRPC.TL_messages_botResults tL_messages_botResults = (TLRPC.TL_messages_botResults) tLObject;
                if (!z && tL_messages_botResults.cache_time != 0) {
                    messagesStorage.saveBotCache(str3, tL_messages_botResults);
                }
                this.nextQueryOffset = tL_messages_botResults.next_offset;
                if (this.searchResultBotContextSwitch == null) {
                    this.searchResultBotContextSwitch = tL_messages_botResults.switch_pm;
                }
                this.searchResultBotWebViewSwitch = tL_messages_botResults.switch_webview;
                int i = 0;
                while (i < tL_messages_botResults.results.size()) {
                    TLRPC.BotInlineResult botInlineResult = tL_messages_botResults.results.get(i);
                    if (!(botInlineResult.document instanceof TLRPC.TL_document) && !(botInlineResult.photo instanceof TLRPC.TL_photo) && !"game".equals(botInlineResult.type) && botInlineResult.content == null && (botInlineResult.send_message instanceof TLRPC.TL_botInlineMessageMediaAuto)) {
                        tL_messages_botResults.results.remove(i);
                        i--;
                    }
                    botInlineResult.query_id = tL_messages_botResults.query_id;
                    i++;
                }
                if (this.searchResultBotContext == null || str2.length() == 0) {
                    this.searchResultBotContext = tL_messages_botResults.results;
                    this.contextMedia = tL_messages_botResults.gallery;
                    z2 = false;
                } else {
                    this.searchResultBotContext.addAll(tL_messages_botResults.results);
                    if (tL_messages_botResults.results.isEmpty()) {
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
                int i2 = (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? 0 : 1;
                notifyItemChanged(((this.searchResultBotContext.size() - tL_messages_botResults.results.size()) + i2) - 1);
                notifyItemRangeInserted((this.searchResultBotContext.size() - tL_messages_botResults.results.size()) + i2, tL_messages_botResults.results.size());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchForContextBotResults$6(final String str, final boolean z, final TLRPC.User user, final String str2, final MessagesStorage messagesStorage, final String str3, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MentionsAdapter.this.lambda$searchForContextBotResults$5(str, z, tLObject, user, str2, messagesStorage, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchServerStickers$0(String str, TLObject tLObject) {
        ArrayList arrayList;
        this.lastReqId = 0;
        if (str.equals(this.lastSticker) && (tLObject instanceof TLRPC.TL_messages_stickers)) {
            this.delayLocalResults = false;
            TLRPC.TL_messages_stickers tL_messages_stickers = (TLRPC.TL_messages_stickers) tLObject;
            ArrayList arrayList2 = this.stickers;
            int size = arrayList2 != null ? arrayList2.size() : 0;
            addStickersToResult(tL_messages_stickers.stickers, "sticker_search_" + str);
            ArrayList arrayList3 = this.stickers;
            int size2 = arrayList3 != null ? arrayList3.size() : 0;
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
    public /* synthetic */ void lambda$searchServerStickers$1(final String str, final TLObject tLObject, TLRPC.TL_error tL_error) {
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
        TLRPC.User user = this.foundContextBot;
        if (user == null || !user.bot_inline_geo) {
            return;
        }
        Location location = new Location("network");
        this.lastKnownLocation = location;
        location.setLatitude(-1000.0d);
        this.lastKnownLocation.setLongitude(-1000.0d);
        searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processFoundUser(TLRPC.User user) {
        ChatActivity chatActivity;
        TLRPC.Chat currentChat;
        this.contextUsernameReqid = 0;
        this.locationProvider.stop();
        if (user == null || !user.bot || user.bot_inline_placeholder == null) {
            this.foundContextBot = null;
            this.searchResultBotContextSwitch = null;
            this.inlineMediaEnabled = true;
        } else {
            this.foundContextBot = user;
            long j = user.id;
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
                if (MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("inlinegeo_" + this.foundContextBot.id, false) || (chatActivity = this.parentFragment) == null || chatActivity.getParentActivity() == null) {
                    checkLocationPermissionsOrStart();
                } else {
                    final TLRPC.User user2 = this.foundContextBot;
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentFragment.getParentActivity());
                    builder.setTitle(LocaleController.getString(R.string.ShareYouLocationTitle));
                    builder.setMessage(LocaleController.getString(R.string.ShareYouLocationInline));
                    final boolean[] zArr = new boolean[1];
                    builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            MentionsAdapter.this.lambda$processFoundUser$2(zArr, user2, dialogInterface, i);
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
        } else {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(true);
            }
            searchForContextBotResults(true, this.foundContextBot, this.searchingContextQuery, "");
        }
    }

    private void searchForContextBot(String str, String str2) {
        String str3;
        String str4;
        String str5;
        TLRPC.User user = this.foundContextBot;
        if (user == null || (str4 = user.username) == null || !str4.equals(str) || (str5 = this.searchingContextQuery) == null || !str5.equals(str2)) {
            if (this.foundContextBot != null) {
                if (!this.inlineMediaEnabled && str != null && str2 != null) {
                    return;
                } else {
                    this.delegate.needChangePanelVisibility(false);
                }
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
    public void searchForContextBotResults(final boolean z, final TLRPC.User user, final String str, final String str2) {
        Location location;
        if (this.contextQueryReqid != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.contextQueryReqid, true);
            this.contextQueryReqid = 0;
        }
        if (!this.inlineMediaEnabled || !this.allowBots) {
            MentionsAdapterDelegate mentionsAdapterDelegate = this.delegate;
            if (mentionsAdapterDelegate != null) {
                mentionsAdapterDelegate.onContextSearch(false);
                return;
            }
            return;
        }
        if (str == null || user == null) {
            this.searchingContextQuery = null;
            return;
        }
        if (user.bot_inline_geo && this.lastKnownLocation == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.dialog_id);
        sb.append("_");
        sb.append(str);
        sb.append("_");
        sb.append(str2);
        sb.append("_");
        sb.append(this.dialog_id);
        sb.append("_");
        sb.append(user.id);
        sb.append("_");
        sb.append((!user.bot_inline_geo || this.lastKnownLocation.getLatitude() == -1000.0d) ? "" : Double.valueOf(this.lastKnownLocation.getLatitude() + this.lastKnownLocation.getLongitude()));
        final String sb2 = sb.toString();
        final MessagesStorage messagesStorage = MessagesStorage.getInstance(this.currentAccount);
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda7
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MentionsAdapter.this.lambda$searchForContextBotResults$6(str, z, user, str2, messagesStorage, sb2, tLObject, tL_error);
            }
        };
        long j = user.id;
        if (j != this.searchResultBotContextSwitchUserId) {
            this.searchResultBotContextSwitch = null;
            this.searchResultBotContextSwitchUserId = j;
        }
        if (z) {
            messagesStorage.getBotCache(sb2, requestDelegate);
            return;
        }
        TLRPC.TL_messages_getInlineBotResults tL_messages_getInlineBotResults = new TLRPC.TL_messages_getInlineBotResults();
        tL_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(user);
        tL_messages_getInlineBotResults.query = str;
        tL_messages_getInlineBotResults.offset = str2;
        if (user.bot_inline_geo && (location = this.lastKnownLocation) != null && location.getLatitude() != -1000.0d) {
            tL_messages_getInlineBotResults.flags |= 1;
            TLRPC.TL_inputGeoPoint tL_inputGeoPoint = new TLRPC.TL_inputGeoPoint();
            tL_messages_getInlineBotResults.geo_point = tL_inputGeoPoint;
            tL_inputGeoPoint.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
            tL_messages_getInlineBotResults.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
        }
        tL_messages_getInlineBotResults.peer = DialogObject.isEncryptedDialog(this.dialog_id) ? new TLRPC.TL_inputPeerEmpty() : MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialog_id);
        this.contextQueryReqid = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getInlineBotResults, requestDelegate, 2);
    }

    private void searchServerStickers(final String str, String str2) {
        TLRPC.TL_messages_getStickers tL_messages_getStickers = new TLRPC.TL_messages_getStickers();
        tL_messages_getStickers.emoticon = str2;
        tL_messages_getStickers.hash = 0L;
        this.lastReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getStickers, new RequestDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda9
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MentionsAdapter.this.lambda$searchServerStickers$1(str, tLObject, tL_error);
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
                if (!(tLObject instanceof TLRPC.Chat) || this.allowChats) {
                    if (tLObject instanceof TLRPC.User) {
                        TLRPC.User user = (TLRPC.User) tLObject;
                        if (!user.bot && !UserObject.isService(user.id)) {
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
                EmojiView.ChooseStickerActionTracker chooseStickerActionTracker = new EmojiView.ChooseStickerActionTracker(this.currentAccount, this.dialog_id, this.threadMessageId) { // from class: org.telegram.ui.Adapters.MentionsAdapter.9
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
        TLRPC.User user = this.foundContextBot;
        if (user != null) {
            return user.bot_inline_placeholder;
        }
        String str = this.searchingContextUsername;
        if (str == null || !str.equals("gif")) {
            return null;
        }
        return LocaleController.getString(R.string.SearchGifsTitle);
    }

    public TLRPC.TL_inlineBotSwitchPM getBotContextSwitch() {
        TLRPC.User user = this.foundContextBot;
        if (user == null || user.id == this.searchResultBotContextSwitchUserId) {
            return this.searchResultBotContextSwitch;
        }
        return null;
    }

    public TLRPC.TL_inlineBotWebView getBotWebViewSwitch() {
        return this.searchResultBotWebViewSwitch;
    }

    public long getContextBotId() {
        TLRPC.User user = this.foundContextBot;
        if (user != null) {
            return user.id;
        }
        return 0L;
    }

    public String getContextBotName() {
        TLRPC.User user = this.foundContextBot;
        return user != null ? user.username : "";
    }

    public TLRPC.User getContextBotUser() {
        return this.foundContextBot;
    }

    public TLRPC.User getFoundContextBot() {
        return this.foundContextBot;
    }

    public String getHashtagHint() {
        return this.hintHashtag;
    }

    public Object getItem(int i) {
        if (this.hintHashtag != null) {
            if (i < 2) {
                return null;
            }
            i -= 2;
        }
        ArrayList arrayList = this.stickers;
        if (arrayList != null) {
            if (i < 0 || i >= arrayList.size()) {
                return null;
            }
            return ((StickerResult) this.stickers.get(i)).sticker;
        }
        ArrayList arrayList2 = this.searchResultBotContext;
        if (arrayList2 != null) {
            TLRPC.TL_inlineBotWebView tL_inlineBotWebView = this.searchResultBotWebViewSwitch;
            if (tL_inlineBotWebView == null) {
                TLRPC.TL_inlineBotSwitchPM tL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
                if (tL_inlineBotSwitchPM != null) {
                    if (i == 0) {
                        return tL_inlineBotSwitchPM;
                    }
                }
                if (i >= 0 || i >= arrayList2.size()) {
                    return null;
                }
                return this.searchResultBotContext.get(i);
            }
            if (i == 0) {
                return tL_inlineBotWebView;
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
                if (arrayList9 == null || (this.botsCount == 1 && !(this.info instanceof TLRPC.TL_channelFull))) {
                    return this.searchResultCommands.get(i);
                }
                if (arrayList9.get(i) != null) {
                    return String.format("%s@%s", this.searchResultCommands.get(i), this.searchResultCommandsUsers.get(i) != null ? ((TLRPC.User) this.searchResultCommandsUsers.get(i)).username : "");
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

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0025, code lost:
    
        if (r5.searchResultBotWebViewSwitch == null) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getItemCountInternal() {
        int size;
        int size2;
        int i = 1;
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 1;
        }
        int i2 = this.hintHashtag != null ? 2 : 0;
        ArrayList arrayList = this.stickers;
        if (arrayList == null) {
            ArrayList arrayList2 = this.searchResultBotContext;
            if (arrayList2 != null) {
                size2 = arrayList2.size();
                if (this.searchResultBotContextSwitch == null) {
                }
                return i2 + size2 + i;
            }
            arrayList = this.searchResultUsernames;
            if (arrayList == null && (arrayList = this.searchResultHashtags) == null) {
                if (this.searchResultCommands == null && this.quickReplies == null) {
                    ArrayList arrayList3 = this.searchResultSuggestions;
                    if (arrayList3 == null) {
                        return i2;
                    }
                    size = arrayList3.size();
                    return i2 + size;
                }
                ArrayList arrayList4 = this.quickReplies;
                size2 = arrayList4 == null ? 0 : arrayList4.size();
                ArrayList arrayList5 = this.searchResultCommands;
                if (arrayList5 != null) {
                    i = arrayList5.size();
                    return i2 + size2 + i;
                }
                i = 0;
                return i2 + size2 + i;
            }
        }
        size = arrayList.size();
        return i2 + size;
    }

    public Object getItemParent(int i) {
        if (this.hintHashtag != null) {
            if (i < 2) {
                return null;
            }
            i -= 2;
        }
        ArrayList arrayList = this.stickers;
        if (arrayList == null || i < 0 || i >= arrayList.size()) {
            return null;
        }
        return ((StickerResult) this.stickers.get(i)).parent;
    }

    public int getItemPosition(int i) {
        if (this.hintHashtag != null) {
            if (i < 2) {
                return 0;
            }
            i -= 2;
        }
        return this.searchResultBotContext != null ? (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? i : i - 1 : i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.hintHashtag != null) {
            if (i < 2) {
                return 6;
            }
            i -= 2;
        }
        if (this.stickers != null) {
            return 4;
        }
        if (this.foundContextBot != null && !this.inlineMediaEnabled) {
            return 3;
        }
        if (this.searchResultBotContext == null) {
            ArrayList arrayList = this.quickReplies;
            return (arrayList == null || i < 0 || i >= arrayList.size()) ? 0 : 5;
        }
        if (i == 0) {
            return (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? 1 : 2;
        }
        return 1;
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

    public boolean isGlobalHashtagHint(int i) {
        return this.hintHashtag != null && i == 0;
    }

    public boolean isLocalHashtagHint(int i) {
        return this.hintHashtag != null && i == 1;
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
        ChatActivity chatActivity;
        String formatString;
        int i2;
        TLRPC.TL_chatBannedRights tL_chatBannedRights;
        if (this.hintHashtag != null) {
            i -= 2;
        }
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 4) {
            StickerCell stickerCell = (StickerCell) viewHolder.itemView;
            if (i < 0 || i >= this.stickers.size()) {
                return;
            }
            StickerResult stickerResult = (StickerResult) this.stickers.get(i);
            stickerCell.setSticker(stickerResult.sticker, stickerResult.parent);
            stickerCell.setClearsInputField(true);
            return;
        }
        if (itemViewType == 3) {
            TextView textView = (TextView) viewHolder.itemView;
            TLRPC.Chat currentChat = this.parentFragment.getCurrentChat();
            if (currentChat != null) {
                if (!ChatObject.hasAdminRights(currentChat) && (tL_chatBannedRights = currentChat.default_banned_rights) != null && tL_chatBannedRights.send_inline) {
                    i2 = R.string.GlobalAttachInlineRestricted;
                } else {
                    if (!AndroidUtilities.isBannedForever(currentChat.banned_rights)) {
                        formatString = LocaleController.formatString("AttachInlineRestricted", R.string.AttachInlineRestricted, LocaleController.formatDateForBan(currentChat.banned_rights.until_date));
                        textView.setText(formatString);
                        return;
                    }
                    i2 = R.string.AttachInlineRestrictedForever;
                }
                formatString = LocaleController.getString(i2);
                textView.setText(formatString);
                return;
            }
            return;
        }
        if (itemViewType == 5) {
            QuickRepliesActivity.QuickReplyView quickReplyView = (QuickRepliesActivity.QuickReplyView) viewHolder.itemView;
            ArrayList arrayList = this.quickReplies;
            if (arrayList == null || i < 0 || i >= arrayList.size()) {
                return;
            }
            quickReplyView.set((QuickRepliesController.QuickReply) this.quickReplies.get(i), this.quickRepliesQuery, false);
            return;
        }
        if (this.searchResultBotContext != null) {
            boolean z = (this.searchResultBotContextSwitch == null && this.searchResultBotWebViewSwitch == null) ? false : true;
            if (viewHolder.getItemViewType() == 2) {
                if (z) {
                    BotSwitchCell botSwitchCell = (BotSwitchCell) viewHolder.itemView;
                    TLRPC.TL_inlineBotSwitchPM tL_inlineBotSwitchPM = this.searchResultBotContextSwitch;
                    botSwitchCell.setText(tL_inlineBotSwitchPM != null ? tL_inlineBotSwitchPM.text : this.searchResultBotWebViewSwitch.text);
                    return;
                }
                return;
            }
            if (z) {
                i--;
            }
            if (i < 0 || i >= this.searchResultBotContext.size()) {
                return;
            }
            ((ContextLinkCell) viewHolder.itemView).setLink((TLRPC.BotInlineResult) this.searchResultBotContext.get(i), this.foundContextBot, this.contextMedia, i != this.searchResultBotContext.size() - 1, z && i == 0, "gif".equals(this.searchingContextUsername));
            return;
        }
        if (itemViewType == 6) {
            HashtagHint hashtagHint = (HashtagHint) viewHolder.itemView;
            int i3 = i + 2;
            if (i3 == 0) {
                this.topHint = hashtagHint;
            } else {
                this.bottomHint = hashtagHint;
            }
            TLRPC.Chat chat = this.chat;
            if (chat == null && (chatActivity = this.parentFragment) != null) {
                chat = chatActivity.getCurrentChat();
            }
            hashtagHint.set(i3, this.hintHashtag, chat);
            return;
        }
        if (itemViewType == 7) {
            return;
        }
        MentionCell mentionCell = (MentionCell) viewHolder.itemView;
        ArrayList arrayList2 = this.searchResultUsernames;
        if (arrayList2 != null) {
            TLObject tLObject = (TLObject) arrayList2.get(i);
            if (tLObject instanceof TLRPC.User) {
                mentionCell.setUser((TLRPC.User) tLObject);
            } else if (tLObject instanceof TLRPC.Chat) {
                mentionCell.setChat((TLRPC.Chat) tLObject);
            }
        } else {
            ArrayList arrayList3 = this.searchResultHashtags;
            if (arrayList3 == null || i < 0 || i >= arrayList3.size()) {
                ArrayList arrayList4 = this.searchResultSuggestions;
                if (arrayList4 == null || i < 0 || i >= arrayList4.size()) {
                    ArrayList arrayList5 = this.searchResultCommands;
                    if (arrayList5 != null && i >= 0 && i < arrayList5.size()) {
                        ArrayList arrayList6 = this.searchResultCommandsHelp;
                        TLRPC.User user = null;
                        String str = (arrayList6 == null || i < 0 || i >= arrayList6.size()) ? null : (String) this.searchResultCommandsHelp.get(i);
                        ArrayList arrayList7 = this.searchResultCommandsUsers;
                        if (arrayList7 != null && i >= 0 && i < arrayList7.size()) {
                            user = (TLRPC.User) this.searchResultCommandsUsers.get(i);
                        }
                        mentionCell.setBotCommand((String) this.searchResultCommands.get(i), str, user);
                    }
                } else {
                    mentionCell.setEmojiSuggestion((MediaDataController.KeywordResult) this.searchResultSuggestions.get(i));
                }
            } else {
                mentionCell.setText((String) this.searchResultHashtags.get(i));
            }
        }
        mentionCell.setDivider(false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        View view2;
        if (i == 0) {
            MentionCell mentionCell = new MentionCell(this.mContext, this.resourcesProvider);
            mentionCell.setIsDarkTheme(this.isDarkTheme);
            view = mentionCell;
        } else if (i == 1) {
            ContextLinkCell contextLinkCell = new ContextLinkCell(this.mContext);
            contextLinkCell.setDelegate(new ContextLinkCell.ContextLinkCellDelegate() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda6
                @Override // org.telegram.ui.Cells.ContextLinkCell.ContextLinkCellDelegate
                public final void didPressedImage(ContextLinkCell contextLinkCell2) {
                    MentionsAdapter.this.lambda$onCreateViewHolder$10(contextLinkCell2);
                }
            });
            view = contextLinkCell;
        } else if (i != 2) {
            if (i == 3) {
                TextView textView = new TextView(this.mContext);
                textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                textView.setTextSize(1, 14.0f);
                textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
                view2 = textView;
            } else if (i == 5) {
                view = new QuickRepliesActivity.QuickReplyView(this.mContext, false, this.resourcesProvider);
            } else if (i == 6) {
                view = new HashtagHint(this.mContext, this.stories, this.resourcesProvider);
            } else if (i != 7) {
                view = new StickerCell(this.mContext, this.resourcesProvider);
            } else {
                View view3 = new View(this.mContext) { // from class: org.telegram.ui.Adapters.MentionsAdapter.8
                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(8.0f), 1073741824));
                    }
                };
                CombinedDrawable combinedDrawable = new CombinedDrawable(new ColorDrawable(this.stories ? Theme.multAlpha(-1, 0.15f) : Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider)), Theme.getThemedDrawable(this.mContext, R.drawable.greydivider, Theme.getColor(Theme.key_windowBackgroundGrayShadow, this.resourcesProvider)), 0, 0);
                combinedDrawable.setFullsize(true);
                view3.setBackground(combinedDrawable);
                view2 = view3;
            }
            view = view2;
        } else {
            view = new BotSwitchCell(this.mContext);
        }
        return new RecyclerListView.Holder(view);
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
        TLRPC.User user;
        if (i == 2 && (user = this.foundContextBot) != null && user.bot_inline_geo) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                onLocationUnavailable();
            } else {
                this.locationProvider.start();
            }
        }
    }

    public void searchForContextBotForNextOffset() {
        String str;
        TLRPC.User user;
        String str2;
        if (this.contextQueryReqid != 0 || (str = this.nextQueryOffset) == null || str.length() == 0 || (user = this.foundContextBot) == null || (str2 = this.searchingContextQuery) == null) {
            return;
        }
        searchForContextBotResults(true, user, str2, this.nextQueryOffset);
    }

    /* JADX WARN: Code restructure failed: missing block: B:245:0x0671, code lost:
    
        if (r6.user_id == r0.id) goto L378;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0776, code lost:
    
        if (r16.toLowerCase().startsWith(r3) == false) goto L434;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x07a8, code lost:
    
        r8.add(r7);
        r12.put(r14, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x0786, code lost:
    
        if (r10.toLowerCase().startsWith(r3) == false) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x0796, code lost:
    
        if (r11.toLowerCase().startsWith(r3) == false) goto L442;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x07a6, code lost:
    
        if (org.telegram.messenger.ContactsController.formatName(r10, r11).toLowerCase().startsWith(r3) != false) goto L445;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x07f7, code lost:
    
        if (r10.toLowerCase().startsWith(r3) == false) goto L462;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0807, code lost:
    
        if (r5.toLowerCase().startsWith(r3) != false) goto L445;
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x03d1, code lost:
    
        if (r2 != ':') goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x03ee, code lost:
    
        if (r29.info != null) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x03f0, code lost:
    
        if (r12 == 0) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x03f2, code lost:
    
        r29.lastText = r0;
        r29.lastPosition = r31;
        r29.messages = r32;
        r29.delegate.needChangePanelVisibility(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x03fe, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:494:0x03ff, code lost:
    
        r29.resultStartPosition = r12;
        r29.resultLength = r4.length() + 1;
        r0 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0495, code lost:
    
        r29.resultStartPosition = r12;
        r29.resultLength = r4.length() + r14;
        r0 = -1;
        r5 = 0;
        r6 = 3;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:143:0x04e2  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x06b8  */
    /* JADX WARN: Removed duplicated region for block: B:456:0x04cb  */
    /* JADX WARN: Removed duplicated region for block: B:459:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x04d3  */
    /* JADX WARN: Type inference failed for: r0v55, types: [org.telegram.tgnet.TLRPC$User] */
    /* JADX WARN: Type inference failed for: r0v71, types: [org.telegram.tgnet.TLRPC$User] */
    /* JADX WARN: Type inference failed for: r0v84 */
    /* JADX WARN: Type inference failed for: r0v90, types: [org.telegram.ui.Adapters.MentionsAdapter$MentionsAdapterDelegate] */
    /* JADX WARN: Type inference failed for: r15v35 */
    /* JADX WARN: Type inference failed for: r15v36, types: [boolean] */
    /* JADX WARN: Type inference failed for: r15v38 */
    /* JADX WARN: Type inference failed for: r15v45 */
    /* JADX WARN: Type inference failed for: r29v0, types: [org.telegram.ui.Adapters.MentionsAdapter, androidx.recyclerview.widget.RecyclerView$Adapter] */
    /* JADX WARN: Type inference failed for: r2v31, types: [org.telegram.ui.Adapters.MentionsAdapter$HashtagHint] */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v22, types: [androidx.collection.LongSparseArray, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r3v23 */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v29 */
    /* JADX WARN: Type inference failed for: r5v31 */
    /* JADX WARN: Type inference failed for: r5v36, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r5v94 */
    /* JADX WARN: Type inference failed for: r6v34, types: [org.telegram.tgnet.TLRPC$User, java.lang.Object] */
    /* renamed from: searchUsernameOrHashtag, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$searchUsernameOrHashtag$7(final CharSequence charSequence, final int i, final ArrayList arrayList, final boolean z, final boolean z2) {
        String str;
        StringBuilder sb;
        String str2;
        String str3;
        TLRPC.Chat chat;
        StringBuilder sb2;
        int i2;
        String str4;
        char c;
        String str5;
        String str6;
        char c2;
        String str7;
        String str8;
        String str9;
        StringBuilder sb3;
        ?? r5;
        char c3;
        int i3;
        int i4;
        int i5;
        int i6;
        ?? r2;
        TLRPC.Chat chat2;
        HashtagHint hashtagHint;
        ?? r3;
        MentionsAdapterDelegate mentionsAdapterDelegate;
        ArrayList arrayList2;
        String str10;
        boolean z3;
        String str11;
        TLRPC.Chat chat3;
        TLRPC.Chat chat4;
        long j;
        TLRPC.Chat chat5;
        TLRPC.Chat chat6;
        long j2;
        TLRPC.Chat chat7;
        TLRPC.Chat chat8;
        TLRPC.ChatFull chatFull;
        long j3;
        int i7;
        String publicUsername;
        long j4;
        String str12;
        String str13;
        Object obj;
        TLRPC.Chat chat9;
        TLRPC.Chat chat10;
        int i8;
        boolean z4;
        String str14;
        int i9;
        ?? r15;
        boolean z5;
        int i10;
        boolean z6 = z;
        boolean z7 = z2;
        String str15 = "";
        String charSequence2 = charSequence == null ? "" : charSequence.toString();
        TLRPC.Chat chat11 = this.chat;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            chat11 = chatActivity.getCurrentChat();
            this.parentFragment.getCurrentUser();
        }
        TLRPC.Chat chat12 = chat11;
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
        int i11 = charSequence2.length() > 0 ? i - 1 : i;
        this.lastText = null;
        this.lastUsernameOnly = z6;
        this.lastForSearch = z7;
        StringBuilder sb4 = new StringBuilder();
        boolean z8 = !z6 && charSequence2.length() > 0 && charSequence2.length() <= 14;
        if (z8) {
            int length = charSequence2.length();
            CharSequence charSequence3 = charSequence2;
            int i12 = 0;
            while (true) {
                str = str15;
                if (i12 >= length) {
                    break;
                }
                char charAt = charSequence3.charAt(i12);
                StringBuilder sb5 = sb4;
                int i13 = length - 1;
                String str16 = charSequence2;
                char charAt2 = i12 < i13 ? charSequence3.charAt(i12 + 1) : (char) 0;
                if (i12 < i13 && charAt == 55356 && charAt2 >= 57339 && charAt2 <= 57343) {
                    charSequence3 = TextUtils.concat(charSequence3.subSequence(0, i12), charSequence3.subSequence(i12 + 2, charSequence3.length()));
                    length -= 2;
                    i12--;
                } else if (charAt == 65039) {
                    i10 = 1;
                    charSequence3 = TextUtils.concat(charSequence3.subSequence(0, i12), charSequence3.subSequence(i12 + 1, charSequence3.length()));
                    length--;
                    i12--;
                    i12 += i10;
                    str15 = str;
                    sb4 = sb5;
                    charSequence2 = str16;
                }
                i10 = 1;
                i12 += i10;
                str15 = str;
                sb4 = sb5;
                charSequence2 = str16;
            }
            sb = sb4;
            str2 = charSequence2;
            this.lastSticker = charSequence3.toString().trim();
            str3 = str2;
        } else {
            str = "";
            sb = sb4;
            str2 = charSequence2;
            str3 = str;
        }
        boolean z9 = z8 && (Emoji.isValidEmoji(str3) || Emoji.isValidEmoji(this.lastSticker));
        if (z9 && (charSequence instanceof Spanned)) {
            AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class);
            z9 = animatedEmojiSpanArr == null || animatedEmojiSpanArr.length == 0;
        }
        if (this.allowStickers && z9 && (chat12 == null || ChatObject.canSendStickers(chat12))) {
            this.stickersToLoad.clear();
            int i14 = SharedConfig.suggestStickers;
            if (i14 == 2 || !z9) {
                if (this.visibleByStickersSearch && i14 == 2) {
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
                z4 = false;
                this.lastReqId = 0;
            } else {
                z4 = false;
            }
            boolean z10 = MessagesController.getInstance(this.currentAccount).suggestStickersApiOnly;
            this.delayLocalResults = z4;
            if (z10) {
                chat = chat12;
                sb2 = sb;
                str14 = str2;
                i9 = 5;
                r15 = 1;
                z5 = z10;
            } else {
                sb2 = sb;
                z5 = z10;
                i9 = 5;
                chat = chat12;
                str14 = str2;
                r15 = 1;
                r15 = 1;
                this.checkAgainRunnable = new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        MentionsAdapter.this.lambda$searchUsernameOrHashtag$7(charSequence, i, arrayList, z, z2);
                    }
                };
                MediaDataController.getInstance(this.currentAccount).loadRecents(0, false, true, false);
                MediaDataController.getInstance(this.currentAccount).loadRecents(2, false, true, false);
                final ArrayList<TLRPC.Document> recentStickersNoCopy = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(0);
                final ArrayList<TLRPC.Document> recentStickersNoCopy2 = MediaDataController.getInstance(this.currentAccount).getRecentStickersNoCopy(2);
                int min = Math.min(20, recentStickersNoCopy.size());
                int i15 = 0;
                for (int i16 = 0; i16 < min; i16++) {
                    TLRPC.Document document = recentStickersNoCopy.get(i16);
                    if (isValidSticker(document, this.lastSticker)) {
                        addStickerToResult(document, "recent");
                        i15++;
                        if (i15 >= 5) {
                            break;
                        }
                    }
                }
                int size = recentStickersNoCopy2.size();
                for (int i17 = 0; i17 < size; i17++) {
                    TLRPC.Document document2 = recentStickersNoCopy2.get(i17);
                    if (isValidSticker(document2, this.lastSticker)) {
                        addStickerToResult(document2, "fav");
                    }
                }
                MediaDataController.getInstance(this.currentAccount).checkStickers(0);
                HashMap<String, ArrayList<TLRPC.Document>> allStickers = MediaDataController.getInstance(this.currentAccount).getAllStickers();
                ArrayList<TLRPC.Document> arrayList3 = allStickers != null ? allStickers.get(this.lastSticker) : null;
                if (arrayList3 != null && !arrayList3.isEmpty()) {
                    addStickersToResult(arrayList3, null);
                }
                ArrayList arrayList4 = this.stickers;
                if (arrayList4 != null) {
                    Collections.sort(arrayList4, new Comparator() { // from class: org.telegram.ui.Adapters.MentionsAdapter.5
                        private int getIndex(StickerResult stickerResult) {
                            for (int i18 = 0; i18 < recentStickersNoCopy2.size(); i18++) {
                                if (((TLRPC.Document) recentStickersNoCopy2.get(i18)).id == stickerResult.sticker.id) {
                                    return i18 + 2000000;
                                }
                            }
                            for (int i19 = 0; i19 < Math.min(20, recentStickersNoCopy.size()); i19++) {
                                if (((TLRPC.Document) recentStickersNoCopy.get(i19)).id == stickerResult.sticker.id) {
                                    return (recentStickersNoCopy.size() - i19) + MediaController.VIDEO_BITRATE_480;
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
            if (SharedConfig.suggestStickers == 0 || z5) {
                searchServerStickers(this.lastSticker, str3);
            }
            ArrayList arrayList5 = this.stickers;
            if (arrayList5 != null && !arrayList5.isEmpty()) {
                if (SharedConfig.suggestStickers != 0 || this.stickers.size() >= i9) {
                    checkStickerFilesExistAndDownload();
                    this.delegate.needChangePanelVisibility(this.stickersToLoad.isEmpty());
                    this.visibleByStickersSearch = r15;
                } else {
                    this.delayLocalResults = r15;
                    this.delegate.needChangePanelVisibility(false);
                    this.visibleByStickersSearch = false;
                }
                notifyDataSetChanged();
            } else if (this.visibleByStickersSearch) {
                this.delegate.needChangePanelVisibility(false);
                this.visibleByStickersSearch = false;
                str9 = str14;
                c = ' ';
                str5 = null;
                c2 = 4;
                i2 = r15;
            }
            str9 = str14;
            c = ' ';
            str5 = null;
            c2 = 4;
            i2 = r15;
        } else {
            chat = chat12;
            sb2 = sb;
            String str17 = str2;
            i2 = 1;
            if (z6 || !this.needBotContext) {
                str4 = str17;
            } else {
                String str18 = str17;
                char charAt3 = str18.charAt(0);
                str4 = str18;
                if (charAt3 == '@') {
                    c = ' ';
                    int indexOf = str18.indexOf(32);
                    int length2 = str18.length();
                    if (indexOf > 0) {
                        str8 = str18.substring(1, indexOf);
                        str7 = str18.substring(indexOf + 1);
                    } else if (str18.charAt(length2 - 1) == 't' && str18.charAt(length2 - 2) == 'o' && str18.charAt(length2 - 3) == 'b') {
                        str8 = str18.substring(1);
                        str7 = str;
                    } else {
                        searchForContextBot(null, null);
                        str7 = null;
                        str8 = null;
                    }
                    if (str8 != null && str8.length() >= 1) {
                        for (int i18 = 1; i18 < str8.length(); i18++) {
                            char charAt4 = str8.charAt(i18);
                            if ((charAt4 >= '0' && charAt4 <= '9') || ((charAt4 >= 'a' && charAt4 <= 'z') || ((charAt4 >= 'A' && charAt4 <= 'Z') || charAt4 == '_'))) {
                            }
                        }
                        searchForContextBot(str8, str7);
                        str5 = null;
                        str6 = str18;
                        c2 = 65535;
                        str9 = str6;
                    }
                    str8 = str;
                    searchForContextBot(str8, str7);
                    str5 = null;
                    str6 = str18;
                    c2 = 65535;
                    str9 = str6;
                }
            }
            c = ' ';
            str5 = null;
            searchForContextBot(null, null);
            str6 = str4;
            c2 = 65535;
            str9 = str6;
        }
        if (this.foundContextBot != null) {
            return;
        }
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        String str19 = this.hintHashtag;
        this.hintHashtag = str5;
        this.hintHashtagDivider = false;
        if (z6) {
            sb3 = sb2;
            sb3.append(str9.substring(i2));
            this.resultStartPosition = 0;
            this.resultLength = sb3.length();
            i3 = -1;
        } else {
            sb3 = sb2;
            while (true) {
                if (i11 < 0) {
                    r5 = 0;
                    c3 = 4;
                    i3 = -1;
                    break;
                }
                if (i11 < str9.length()) {
                    char charAt5 = str9.charAt(i11);
                    if (i11 != 0) {
                        int i19 = i11 - 1;
                        if (str9.charAt(i19) != c) {
                            if (str9.charAt(i19) != '\n') {
                            }
                        }
                    }
                    if (charAt5 == '@') {
                        boolean z11 = this.searchInDailogs;
                        if (z11 || this.needUsernames || (this.needBotContext && i11 == 0)) {
                            break;
                        }
                        i4 = 0;
                        sb3.insert(i4, charAt5);
                        i6 = -1;
                    } else if (charAt5 != '#') {
                        c3 = 4;
                        if (i11 == 0 && this.botInfo != null && charAt5 == '/') {
                            this.resultStartPosition = i11;
                            this.resultLength = sb3.length() + 1;
                            i3 = -1;
                            r5 = 0;
                            c2 = 2;
                            break;
                        }
                        if (charAt5 == ':' && sb3.length() > 0) {
                            if (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\n".indexOf(sb3.charAt(0)) >= 0) {
                                i5 = 1;
                                if (sb3.length() > 1) {
                                    break;
                                }
                            } else {
                                i5 = 1;
                                break;
                            }
                        }
                        i4 = 0;
                        sb3.insert(i4, charAt5);
                        i6 = -1;
                    } else {
                        if (!ChatObject.isChannelAndNotMegaGroup(chat) || TextUtils.isEmpty(ChatObject.getPublicUsername(chat))) {
                            c3 = 4;
                        } else {
                            String substring = str9.substring(i11);
                            this.hintHashtag = substring;
                            c3 = 4;
                            if (substring.length() < 4 || !this.hintHashtag.matches("^[#$][a-zA-Z0-9_-]+$")) {
                                this.hintHashtag = null;
                            }
                        }
                        if (!this.searchAdapterHelper.loadRecentHashtags()) {
                            this.lastText = str9;
                            this.lastPosition = i;
                            this.messages = arrayList;
                            return;
                        } else {
                            this.resultStartPosition = i11;
                            this.resultLength = sb3.length() + 1;
                            sb3.insert(0, charAt5);
                            i3 = -1;
                            r5 = 0;
                            c2 = 1;
                        }
                    }
                } else {
                    i6 = -1;
                }
                i11 += i6;
            }
            if (str19 == null || this.hintHashtag == null) {
                if (str19 != null || this.hintHashtag != null) {
                    r2 = this.topHint;
                    if (r2 == 0) {
                        chat2 = chat;
                        r2.set(r5, this.hintHashtag, chat2);
                    } else {
                        chat2 = chat;
                    }
                    hashtagHint = this.bottomHint;
                    if (hashtagHint != null) {
                        hashtagHint.set(1, this.hintHashtag, chat2);
                    }
                    if (c2 == 65535) {
                        this.contextMedia = r5;
                        this.searchResultBotContext = null;
                        this.delegate.needChangePanelVisibility(r5);
                        return;
                    }
                    if (c2 != 0) {
                        if (c2 == 1) {
                            ArrayList arrayList6 = new ArrayList();
                            String lowerCase = sb3.toString().toLowerCase();
                            ArrayList hashtags = this.searchAdapterHelper.getHashtags();
                            for (int i20 = 0; i20 < hashtags.size(); i20++) {
                                SearchAdapterHelper.HashtagObject hashtagObject = (SearchAdapterHelper.HashtagObject) hashtags.get(i20);
                                if (hashtagObject != null && (str11 = hashtagObject.hashtag) != null && str11.startsWith(lowerCase)) {
                                    arrayList6.add(hashtagObject.hashtag);
                                }
                            }
                            this.searchResultHashtags = arrayList6;
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
                            mentionsAdapterDelegate = this.delegate;
                            z3 = (this.searchResultHashtags.isEmpty() && this.hintHashtag == null) ? false : true;
                        } else {
                            if (c2 != 2) {
                                if (c2 == 3) {
                                    String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                                    if (!Arrays.equals(currentKeyboardLanguage, this.lastSearchKeyboardLanguage)) {
                                        MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
                                    }
                                    this.lastSearchKeyboardLanguage = currentKeyboardLanguage;
                                    MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(this.lastSearchKeyboardLanguage, sb3.toString(), false, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda5
                                        @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
                                        public final void run(ArrayList arrayList7, String str20) {
                                            MentionsAdapter.this.lambda$searchUsernameOrHashtag$9(arrayList7, str20);
                                        }
                                    }, SharedConfig.suggestAnimatedEmoji && UserConfig.getInstance(this.currentAccount).isPremium());
                                    return;
                                }
                                if (c2 == c3) {
                                    this.searchResultHashtags = null;
                                    this.searchResultUsernames = null;
                                    this.searchResultUsernamesMap = null;
                                    this.searchResultSuggestions = null;
                                    this.searchResultCommands = null;
                                    this.quickReplies = null;
                                    this.searchResultCommandsHelp = null;
                                    this.searchResultCommandsUsers = null;
                                    return;
                                }
                                return;
                            }
                            ArrayList arrayList7 = new ArrayList();
                            ArrayList arrayList8 = new ArrayList();
                            ArrayList arrayList9 = new ArrayList();
                            String lowerCase2 = sb3.toString().toLowerCase();
                            for (int i21 = 0; i21 < this.botInfo.size(); i21++) {
                                TL_bots.BotInfo botInfo = (TL_bots.BotInfo) this.botInfo.valueAt(i21);
                                for (int i22 = 0; i22 < botInfo.commands.size(); i22++) {
                                    TLRPC.TL_botCommand tL_botCommand = botInfo.commands.get(i22);
                                    if (tL_botCommand != null && (str10 = tL_botCommand.command) != null && str10.startsWith(lowerCase2)) {
                                        arrayList7.add("/" + tL_botCommand.command);
                                        arrayList8.add(tL_botCommand.description);
                                        arrayList9.add(messagesController.getUser(Long.valueOf(botInfo.user_id)));
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
                                for (int i23 = 0; i23 < quickRepliesController.replies.size(); i23++) {
                                    QuickRepliesController.QuickReply quickReply = (QuickRepliesController.QuickReply) quickRepliesController.replies.get(i23);
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
                            this.searchResultCommands = arrayList7;
                            this.searchResultCommandsHelp = arrayList8;
                            this.searchResultCommandsUsers = arrayList9;
                            this.contextMedia = false;
                            this.searchResultBotContext = r3;
                            notifyDataSetChanged();
                            MentionsAdapterDelegate mentionsAdapterDelegate2 = this.delegate;
                            if (arrayList7.isEmpty() && ((arrayList2 = this.quickReplies) == null || arrayList2.isEmpty())) {
                                mentionsAdapterDelegate = mentionsAdapterDelegate2;
                            } else {
                                mentionsAdapterDelegate = mentionsAdapterDelegate2;
                            }
                        }
                        mentionsAdapterDelegate.needChangePanelVisibility(z3);
                        return;
                    }
                    this.contextMedia = r5;
                    this.searchResultBotContext = null;
                    final ArrayList arrayList10 = new ArrayList();
                    if (arrayList != null) {
                        for (int i24 = 0; i24 < Math.min(100, arrayList.size()); i24++) {
                            long fromChatId = ((MessageObject) arrayList.get(i24)).getFromChatId();
                            if (fromChatId > 0 && !arrayList10.contains(Long.valueOf(fromChatId))) {
                                arrayList10.add(Long.valueOf(fromChatId));
                            }
                        }
                    }
                    String lowerCase3 = sb3.toString().toLowerCase();
                    boolean z12 = lowerCase3.indexOf(c) >= 0;
                    final ArrayList arrayList11 = new ArrayList();
                    LongSparseArray longSparseArray = new LongSparseArray();
                    final LongSparseArray longSparseArray2 = new LongSparseArray();
                    ArrayList<TLRPC.TL_topPeer> arrayList12 = MediaDataController.getInstance(this.currentAccount).inlineBots;
                    if (!z6 && this.needBotContext && i3 == 0 && !arrayList12.isEmpty()) {
                        int i25 = 0;
                        int i26 = 0;
                        while (i25 < arrayList12.size()) {
                            TLRPC.User user = messagesController.getUser(Long.valueOf(arrayList12.get(i25).peer.user_id));
                            if (user != null) {
                                String publicUsername2 = UserObject.getPublicUsername(user);
                                if (TextUtils.isEmpty(publicUsername2) || !(lowerCase3.length() == 0 || publicUsername2.toLowerCase().startsWith(lowerCase3))) {
                                    chat3 = chat2;
                                    i8 = 1;
                                } else {
                                    arrayList11.add(user);
                                    chat3 = chat2;
                                    longSparseArray.put(user.id, user);
                                    longSparseArray2.put(user.id, user);
                                    i8 = 1;
                                    i26++;
                                }
                                if (i26 == 5) {
                                    break;
                                }
                            } else {
                                chat3 = chat2;
                                i8 = 1;
                            }
                            i25 += i8;
                            chat2 = chat3;
                        }
                    }
                    chat3 = chat2;
                    ChatActivity chatActivity2 = this.parentFragment;
                    if (chatActivity2 != null) {
                        TLRPC.Chat currentChat = chatActivity2.getCurrentChat();
                        j = this.parentFragment.getThreadId();
                        chat4 = currentChat;
                    } else {
                        TLRPC.ChatFull chatFull2 = this.info;
                        chat4 = chatFull2 != null ? messagesController.getChat(Long.valueOf(chatFull2.id)) : chat3;
                        j = 0;
                    }
                    ?? currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
                    if (chat4 != null && (chatFull = this.info) != null && chatFull.participants != null && (!ChatObject.isChannel(chat4) || chat4.megagroup)) {
                        int i27 = -2;
                        while (i27 < this.info.participants.participants.size()) {
                            if (i27 != -2) {
                                j3 = j;
                                if (i27 == -1) {
                                    if (z7) {
                                        if (lowerCase3.length() == 0) {
                                            arrayList11.add(chat4);
                                        } else {
                                            String str20 = chat4.title;
                                            publicUsername = ChatObject.getPublicUsername(chat4);
                                            str13 = str20;
                                            str12 = null;
                                            j4 = -chat4.id;
                                            chat9 = chat4;
                                            i7 = i27;
                                            chat10 = chat9;
                                            long j5 = j4;
                                            obj = currentUser;
                                            if (!TextUtils.isEmpty(publicUsername)) {
                                                arrayList11.add(chat10);
                                                longSparseArray2.put(j5, chat10);
                                            }
                                            arrayList11.add(chat10);
                                            longSparseArray2.put(j5, chat10);
                                        }
                                    }
                                    obj = currentUser;
                                    i7 = i27;
                                } else {
                                    TLRPC.ChatParticipant chatParticipant = this.info.participants.participants.get(i27);
                                    if (currentUser != null) {
                                        i7 = i27;
                                    } else {
                                        i7 = i27;
                                    }
                                    ?? user2 = messagesController.getUser(Long.valueOf(chatParticipant.user_id));
                                    if (user2 != 0 && !UserObject.isUserSelf(user2) && longSparseArray.indexOfKey(user2.id) < 0) {
                                        if (lowerCase3.length() != 0 || user2.deleted) {
                                            String str21 = user2.first_name;
                                            String str22 = user2.last_name;
                                            publicUsername = UserObject.getPublicUsername(user2);
                                            j4 = user2.id;
                                            str12 = str22;
                                            str13 = str21;
                                            chat10 = user2;
                                            long j52 = j4;
                                            obj = currentUser;
                                            if (!TextUtils.isEmpty(publicUsername)) {
                                            }
                                            arrayList11.add(chat10);
                                            longSparseArray2.put(j52, chat10);
                                        } else {
                                            arrayList11.add(user2);
                                        }
                                    }
                                    obj = currentUser;
                                }
                            } else if (currentUser == null) {
                                j3 = j;
                                obj = currentUser;
                                i7 = i27;
                            } else if (z6) {
                                String str23 = currentUser.first_name;
                                str12 = currentUser.last_name;
                                publicUsername = UserObject.getPublicUsername(currentUser);
                                j3 = j;
                                str13 = str23;
                                j4 = currentUser.id;
                                chat9 = currentUser;
                                i7 = i27;
                                chat10 = chat9;
                                long j522 = j4;
                                obj = currentUser;
                                if ((!TextUtils.isEmpty(publicUsername) && publicUsername.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str13) && str13.toLowerCase().startsWith(lowerCase3)) || ((!TextUtils.isEmpty(str12) && str12.toLowerCase().startsWith(lowerCase3)) || (z12 && ContactsController.formatName(str13, str12).toLowerCase().startsWith(lowerCase3))))) {
                                    arrayList11.add(chat10);
                                    longSparseArray2.put(j522, chat10);
                                }
                            } else {
                                obj = currentUser;
                                j3 = j;
                                i7 = i27;
                            }
                            i27 = i7 + 1;
                            currentUser = obj;
                            z6 = z;
                            z7 = z2;
                            j = j3;
                        }
                    }
                    long j6 = j;
                    if (this.searchInDailogs) {
                        ArrayList<TLRPC.Dialog> allDialogs = MessagesController.getInstance(this.currentAccount).getAllDialogs();
                        int i28 = 0;
                        while (i28 < allDialogs.size()) {
                            if (allDialogs.get(i28).id > 0) {
                                TLRPC.User user3 = messagesController.getUser(Long.valueOf(allDialogs.get(i28).id));
                                if (user3 == null || UserObject.isUserSelf(user3) || longSparseArray.indexOfKey(user3.id) >= 0) {
                                    chat5 = chat4;
                                } else if (lowerCase3.length() != 0 || user3.deleted) {
                                    String str24 = user3.first_name;
                                    String str25 = user3.last_name;
                                    String publicUsername3 = UserObject.getPublicUsername(user3);
                                    j2 = user3.id;
                                    chat5 = chat4;
                                    if (!TextUtils.isEmpty(publicUsername3)) {
                                        chat7 = user3;
                                    }
                                    if (!TextUtils.isEmpty(str24)) {
                                        chat7 = user3;
                                    }
                                    if (!TextUtils.isEmpty(str25)) {
                                        chat7 = user3;
                                    }
                                    if (z12) {
                                        chat7 = user3;
                                    }
                                } else {
                                    chat5 = chat4;
                                    chat8 = user3;
                                    arrayList11.add(chat8);
                                }
                            } else {
                                chat5 = chat4;
                                if (!TextUtils.isEmpty(lowerCase3) && (chat6 = messagesController.getChat(Long.valueOf(-allDialogs.get(i28).id))) != null && chat6.username != null && longSparseArray.indexOfKey(chat6.id) < 0) {
                                    chat8 = chat6;
                                    if (lowerCase3.length() != 0) {
                                        String str26 = chat6.title;
                                        String str27 = chat6.username;
                                        j2 = chat6.id;
                                        if (!TextUtils.isEmpty(str27)) {
                                            chat7 = chat6;
                                        }
                                        if (!TextUtils.isEmpty(str26)) {
                                            chat7 = chat6;
                                        }
                                    }
                                    arrayList11.add(chat8);
                                }
                            }
                            i28++;
                            chat4 = chat5;
                        }
                    }
                    TLRPC.Chat chat13 = chat4;
                    Collections.sort(arrayList11, new Comparator() { // from class: org.telegram.ui.Adapters.MentionsAdapter.6
                        private long getId(TLObject tLObject) {
                            return tLObject instanceof TLRPC.User ? ((TLRPC.User) tLObject).id : -((TLRPC.Chat) tLObject).id;
                        }

                        @Override // java.util.Comparator
                        public int compare(TLObject tLObject, TLObject tLObject2) {
                            long id = getId(tLObject);
                            long id2 = getId(tLObject2);
                            if (longSparseArray2.indexOfKey(id) >= 0 && longSparseArray2.indexOfKey(id2) >= 0) {
                                return 0;
                            }
                            if (longSparseArray2.indexOfKey(id) >= 0) {
                                return -1;
                            }
                            if (longSparseArray2.indexOfKey(id2) >= 0) {
                                return 1;
                            }
                            int indexOf2 = arrayList10.indexOf(Long.valueOf(id));
                            int indexOf3 = arrayList10.indexOf(Long.valueOf(id2));
                            if (indexOf2 != -1 && indexOf3 != -1) {
                                if (indexOf2 < indexOf3) {
                                    return -1;
                                }
                                return indexOf2 == indexOf3 ? 0 : 1;
                            }
                            if (indexOf2 == -1 || indexOf3 != -1) {
                                return (indexOf2 != -1 || indexOf3 == -1) ? 0 : 1;
                            }
                            return -1;
                        }
                    });
                    this.searchResultHashtags = null;
                    this.stickers = null;
                    this.quickReplies = null;
                    this.searchResultCommands = null;
                    this.searchResultCommandsHelp = null;
                    this.searchResultCommandsUsers = null;
                    this.searchResultSuggestions = null;
                    if (((chat13 == null || !chat13.megagroup) && !this.searchInDailogs) || lowerCase3.length() <= 0) {
                        showUsersResult(arrayList11, longSparseArray2, true);
                        return;
                    }
                    if (arrayList11.size() < 5) {
                        Runnable runnable4 = new Runnable() { // from class: org.telegram.ui.Adapters.MentionsAdapter$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                MentionsAdapter.this.lambda$searchUsernameOrHashtag$8(arrayList11, longSparseArray2);
                            }
                        };
                        this.cancelDelayRunnable = runnable4;
                        AndroidUtilities.runOnUIThread(runnable4, 1000L);
                    } else {
                        showUsersResult(arrayList11, longSparseArray2, true);
                    }
                    7 r10 = new 7(chat13, lowerCase3, j6, arrayList11, longSparseArray2, messagesController);
                    this.searchGlobalRunnable = r10;
                    AndroidUtilities.runOnUIThread(r10, 200L);
                    return;
                }
                notifyItemRangeRemoved(r5, 2);
            } else {
                notifyItemRangeInserted(r5, 2);
            }
            chat2 = chat;
            if (c2 == 65535) {
            }
        }
        r5 = 0;
        c2 = 0;
        c3 = 4;
        if (str19 == null) {
        }
        if (str19 != null) {
        }
        r2 = this.topHint;
        if (r2 == 0) {
        }
        hashtagHint = this.bottomHint;
        if (hashtagHint != null) {
        }
        if (c2 == 65535) {
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

    public void setChatInfo(TLRPC.ChatFull chatFull) {
        ChatActivity chatActivity;
        TLRPC.Chat currentChat;
        this.currentAccount = UserConfig.selectedAccount;
        this.info = chatFull;
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

    public void setUserOrChat(TLRPC.User user, TLRPC.Chat chat) {
        this.user = user;
        this.chat = chat;
    }
}
