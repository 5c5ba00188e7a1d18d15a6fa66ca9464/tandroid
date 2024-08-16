package org.telegram.ui.Adapters;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineMessage;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$TL_botInlineMessageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC$TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_geoPoint;
import org.telegram.tgnet.TLRPC$TL_inputGeoPoint;
import org.telegram.tgnet.TLRPC$TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC$TL_messages_getInlineBotResults;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$messages_BotResults;
import org.telegram.tgnet.tl.TL_stories$TL_geoPointAddress;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
/* loaded from: classes4.dex */
public abstract class BaseLocationAdapter extends AdapterWithDiffUtils {
    public final boolean biz;
    private int currentRequestNum;
    private BaseLocationAdapterDelegate delegate;
    private long dialogId;
    private String lastFoundQuery;
    private Location lastSearchLocation;
    private String lastSearchQuery;
    protected boolean searchInProgress;
    private Runnable searchRunnable;
    protected boolean searching;
    protected boolean searchingLocations;
    private boolean searchingUser;
    public final boolean stories;
    protected boolean searched = false;
    protected ArrayList<TLRPC$TL_messageMediaVenue> locations = new ArrayList<>();
    protected ArrayList<TLRPC$TL_messageMediaVenue> places = new ArrayList<>();
    private int currentAccount = UserConfig.selectedAccount;

    /* loaded from: classes4.dex */
    public interface BaseLocationAdapterDelegate {
        void didLoadSearchResult(ArrayList<TLRPC$TL_messageMediaVenue> arrayList);
    }

    public BaseLocationAdapter(boolean z, boolean z2) {
        this.stories = z;
        this.biz = z2;
    }

    public void destroy() {
        if (this.currentRequestNum != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
            this.currentRequestNum = 0;
        }
    }

    public void setDelegate(long j, BaseLocationAdapterDelegate baseLocationAdapterDelegate) {
        this.dialogId = j;
        this.delegate = baseLocationAdapterDelegate;
    }

    public void searchDelayed(final String str, final Location location) {
        if (str == null || str.length() == 0) {
            this.places.clear();
            this.locations.clear();
            this.searchInProgress = false;
            update(true);
            return;
        }
        if (this.searchRunnable != null) {
            Utilities.searchQueue.cancelRunnable(this.searchRunnable);
            this.searchRunnable = null;
        }
        this.searchInProgress = true;
        DispatchQueue dispatchQueue = Utilities.searchQueue;
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseLocationAdapter.this.lambda$searchDelayed$1(str, location);
            }
        };
        this.searchRunnable = runnable;
        dispatchQueue.postRunnable(runnable, 400L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDelayed$1(final String str, final Location location) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                BaseLocationAdapter.this.lambda$searchDelayed$0(str, location);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchDelayed$0(String str, Location location) {
        this.searchRunnable = null;
        this.lastSearchLocation = null;
        searchPlacesWithQuery(str, location, true);
    }

    private void searchBotUser() {
        String str;
        if (this.searchingUser) {
            return;
        }
        this.searchingUser = true;
        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername = new TLRPC$TL_contacts_resolveUsername();
        if (this.stories) {
            str = MessagesController.getInstance(this.currentAccount).storyVenueSearchBot;
        } else {
            str = MessagesController.getInstance(this.currentAccount).venueSearchBot;
        }
        tLRPC$TL_contacts_resolveUsername.username = str;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BaseLocationAdapter.this.lambda$searchBotUser$3(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchBotUser$3(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    BaseLocationAdapter.this.lambda$searchBotUser$2(tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchBotUser$2(TLObject tLObject) {
        TLRPC$TL_contacts_resolvedPeer tLRPC$TL_contacts_resolvedPeer = (TLRPC$TL_contacts_resolvedPeer) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tLRPC$TL_contacts_resolvedPeer.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tLRPC$TL_contacts_resolvedPeer.chats, false);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tLRPC$TL_contacts_resolvedPeer.users, tLRPC$TL_contacts_resolvedPeer.chats, true, true);
        Location location = this.lastSearchLocation;
        this.lastSearchLocation = null;
        searchPlacesWithQuery(this.lastSearchQuery, location, false);
    }

    public boolean isSearching() {
        return this.searchInProgress;
    }

    public String getLastSearchString() {
        return this.lastFoundQuery;
    }

    public void searchPlacesWithQuery(String str, Location location, boolean z) {
        searchPlacesWithQuery(str, location, z, false);
    }

    public void searchPlacesWithQuery(final String str, final Location location, boolean z, boolean z2) {
        String str2;
        final Locale locale;
        if (location != null || this.stories) {
            Location location2 = this.lastSearchLocation;
            if (location2 == null || location == null || location.distanceTo(location2) >= 200.0f) {
                Locale locale2 = null;
                this.lastSearchLocation = location == null ? null : new Location(location);
                this.lastSearchQuery = str;
                if (this.searching) {
                    this.searching = false;
                    if (this.currentRequestNum != 0) {
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
                        this.currentRequestNum = 0;
                    }
                }
                getItemCount();
                this.searching = true;
                this.searched = true;
                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                if (this.stories) {
                    str2 = MessagesController.getInstance(this.currentAccount).storyVenueSearchBot;
                } else {
                    str2 = MessagesController.getInstance(this.currentAccount).venueSearchBot;
                }
                TLObject userOrChat = messagesController.getUserOrChat(str2);
                if (!(userOrChat instanceof TLRPC$User)) {
                    if (z) {
                        searchBotUser();
                        return;
                    }
                    return;
                }
                TLRPC$User tLRPC$User = (TLRPC$User) userOrChat;
                TLRPC$TL_messages_getInlineBotResults tLRPC$TL_messages_getInlineBotResults = new TLRPC$TL_messages_getInlineBotResults();
                tLRPC$TL_messages_getInlineBotResults.query = str == null ? "" : str;
                tLRPC$TL_messages_getInlineBotResults.bot = MessagesController.getInstance(this.currentAccount).getInputUser(tLRPC$User);
                tLRPC$TL_messages_getInlineBotResults.offset = "";
                if (location != null) {
                    TLRPC$TL_inputGeoPoint tLRPC$TL_inputGeoPoint = new TLRPC$TL_inputGeoPoint();
                    tLRPC$TL_messages_getInlineBotResults.geo_point = tLRPC$TL_inputGeoPoint;
                    tLRPC$TL_inputGeoPoint.lat = AndroidUtilities.fixLocationCoord(location.getLatitude());
                    tLRPC$TL_messages_getInlineBotResults.geo_point._long = AndroidUtilities.fixLocationCoord(location.getLongitude());
                    tLRPC$TL_messages_getInlineBotResults.flags |= 1;
                }
                if (DialogObject.isEncryptedDialog(this.dialogId)) {
                    tLRPC$TL_messages_getInlineBotResults.peer = new TLRPC$TL_inputPeerEmpty();
                } else {
                    tLRPC$TL_messages_getInlineBotResults.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
                }
                if (!TextUtils.isEmpty(str) && (this.stories || this.biz)) {
                    this.searchingLocations = true;
                    final Locale currentLocale = LocaleController.getInstance().getCurrentLocale();
                    if (this.stories) {
                        if (currentLocale.getLanguage().contains("en")) {
                            locale = currentLocale;
                            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    BaseLocationAdapter.this.lambda$searchPlacesWithQuery$5(currentLocale, str, locale, location, str);
                                }
                            });
                        } else {
                            locale2 = Locale.US;
                        }
                    }
                    locale = locale2;
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            BaseLocationAdapter.this.lambda$searchPlacesWithQuery$5(currentLocale, str, locale, location, str);
                        }
                    });
                } else {
                    this.searchingLocations = false;
                }
                if (location == null) {
                    return;
                }
                this.currentRequestNum = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getInlineBotResults, new RequestDelegate() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda2
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        BaseLocationAdapter.this.lambda$searchPlacesWithQuery$7(str, tLObject, tLRPC$TL_error);
                    }
                });
                update(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01d1  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x01e2 A[Catch: Exception -> 0x04b2, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0311 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0333 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0375 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0387 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0398 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x043c  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0445 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:232:0x04a1  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0115 A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x013e A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01be A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01ca A[Catch: Exception -> 0x04b2, TryCatch #1 {Exception -> 0x04b2, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0071, B:28:0x0096, B:31:0x009e, B:33:0x00a8, B:34:0x00ab, B:38:0x00bf, B:40:0x00c9, B:42:0x00cf, B:43:0x00d2, B:61:0x010f, B:63:0x0115, B:65:0x011b, B:66:0x011e, B:68:0x0123, B:70:0x0129, B:71:0x012c, B:74:0x0134, B:76:0x013e, B:78:0x014e, B:80:0x015a, B:82:0x0166, B:93:0x01b8, B:95:0x01be, B:96:0x01c1, B:98:0x01ca, B:99:0x01cd, B:101:0x01d9, B:104:0x01e2, B:109:0x01f5, B:111:0x01fb, B:115:0x022d, B:117:0x0233, B:119:0x024f, B:121:0x0258, B:123:0x0260, B:125:0x0274, B:127:0x027a, B:129:0x0280, B:130:0x0284, B:132:0x028a, B:133:0x028e, B:135:0x029d, B:136:0x02a7, B:138:0x02ad, B:140:0x02b9, B:142:0x02c0, B:144:0x02cc, B:146:0x02da, B:149:0x02e2, B:151:0x02e8, B:153:0x02f2, B:155:0x0302, B:158:0x030b, B:160:0x0311, B:162:0x031b, B:164:0x0329, B:165:0x032d, B:167:0x0333, B:169:0x0339, B:171:0x0343, B:173:0x0349, B:174:0x034c, B:176:0x0351, B:179:0x0358, B:181:0x035d, B:186:0x036f, B:188:0x0375, B:191:0x0387, B:196:0x0398, B:198:0x03a4, B:200:0x03db, B:202:0x03ef, B:204:0x03f5, B:206:0x03fb, B:207:0x03ff, B:209:0x0405, B:210:0x0409, B:212:0x0413, B:213:0x041d, B:215:0x0423, B:217:0x042f, B:222:0x043f, B:224:0x0445, B:226:0x0451, B:228:0x048a, B:229:0x0497, B:184:0x036b, B:120:0x0254, B:86:0x0179, B:88:0x018d, B:90:0x0197, B:92:0x01b1, B:45:0x00d8, B:47:0x00e2, B:49:0x00e8, B:50:0x00eb, B:51:0x00ef, B:53:0x00f9, B:55:0x00ff, B:57:0x0105, B:58:0x0108), top: B:240:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$searchPlacesWithQuery$5(Locale locale, String str, Locale locale2, final Location location, final String str2) {
        List<Address> list;
        List<Address> list2;
        HashSet hashSet;
        int i;
        HashSet hashSet2;
        int i2;
        HashSet hashSet3;
        boolean z;
        boolean z2;
        String countryName;
        int i3;
        boolean z3;
        String str3;
        Address address;
        String str4;
        int i4;
        boolean z4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String[] strArr;
        BaseLocationAdapter baseLocationAdapter = this;
        final ArrayList arrayList = new ArrayList();
        try {
            int i5 = baseLocationAdapter.biz ? 10 : 5;
            List<Address> fromLocationName = baseLocationAdapter.stories ? new Geocoder(ApplicationLoader.applicationContext, locale2).getFromLocationName(str, 5) : null;
            HashSet hashSet4 = new HashSet();
            HashSet hashSet5 = new HashSet();
            int i6 = 0;
            for (List<Address> fromLocationName2 = new Geocoder(ApplicationLoader.applicationContext, locale).getFromLocationName(str, 5); i6 < fromLocationName2.size(); fromLocationName2 = list2) {
                Address address2 = fromLocationName2.get(i6);
                Address address3 = (fromLocationName == null || i6 >= fromLocationName.size()) ? null : fromLocationName.get(i6);
                if (address2.hasLatitude() && address2.hasLongitude()) {
                    double latitude = address2.getLatitude();
                    double longitude = address2.getLongitude();
                    StringBuilder sb = new StringBuilder();
                    list = fromLocationName;
                    StringBuilder sb2 = new StringBuilder();
                    list2 = fromLocationName2;
                    StringBuilder sb3 = new StringBuilder();
                    String locality = address2.getLocality();
                    if (TextUtils.isEmpty(locality)) {
                        locality = address2.getAdminArea();
                    }
                    String str11 = locality;
                    if (address3 != null && TextUtils.isEmpty(address3.getLocality())) {
                        address3.getAdminArea();
                    }
                    i = i6;
                    String thoroughfare = address2.getThoroughfare();
                    HashSet hashSet6 = hashSet4;
                    if (TextUtils.isEmpty(thoroughfare)) {
                        hashSet3 = hashSet5;
                    } else {
                        hashSet3 = hashSet5;
                        if (!TextUtils.equals(thoroughfare, address2.getAdminArea())) {
                            if (sb3.length() > 0) {
                                sb3.append(", ");
                            }
                            sb3.append(thoroughfare);
                            z = false;
                            if (TextUtils.isEmpty(str11)) {
                                z2 = true;
                            } else {
                                if (sb2.length() > 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(str11);
                                if (sb3 != null) {
                                    if (sb3.length() > 0) {
                                        sb3.append(", ");
                                    }
                                    sb3.append(str11);
                                }
                                z2 = false;
                            }
                            countryName = address2.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                                i3 = i5;
                                z3 = z;
                                str3 = str11;
                                address = address3;
                            } else {
                                i3 = i5;
                                str3 = str11;
                                if (!"US".equals(address2.getCountryCode()) && !"AE".equals(address2.getCountryCode()) && (!"GB".equals(address2.getCountryCode()) || !"en".equals(locale.getLanguage()))) {
                                    z3 = z;
                                    address = address3;
                                    str9 = countryName;
                                    if (sb2.length() > 0) {
                                        sb2.append(", ");
                                    }
                                    sb2.append(str9);
                                    if (sb.length() > 0) {
                                        sb.append(", ");
                                    }
                                    sb.append(countryName);
                                }
                                String[] split = countryName.split(" ");
                                int length = split.length;
                                address = address3;
                                str9 = "";
                                z3 = z;
                                int i7 = 0;
                                while (i7 < length) {
                                    int i8 = length;
                                    if (split[i7].length() > 0) {
                                        strArr = split;
                                        str9 = str9 + str10.charAt(0);
                                    } else {
                                        strArr = split;
                                    }
                                    i7++;
                                    length = i8;
                                    split = strArr;
                                }
                                if (sb2.length() > 0) {
                                }
                                sb2.append(str9);
                                if (sb.length() > 0) {
                                }
                                sb.append(countryName);
                            }
                            if (baseLocationAdapter.biz) {
                                StringBuilder sb4 = new StringBuilder();
                                try {
                                    String addressLine = address2.getAddressLine(0);
                                    if (!TextUtils.isEmpty(addressLine)) {
                                        sb4.append(addressLine);
                                    }
                                } catch (Exception unused) {
                                }
                                if (sb4.length() > 0) {
                                    TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue = new TLRPC$TL_messageMediaVenue();
                                    TLRPC$TL_geoPoint tLRPC$TL_geoPoint = new TLRPC$TL_geoPoint();
                                    tLRPC$TL_messageMediaVenue.geo = tLRPC$TL_geoPoint;
                                    tLRPC$TL_geoPoint.lat = latitude;
                                    tLRPC$TL_geoPoint._long = longitude;
                                    tLRPC$TL_messageMediaVenue.query_id = -1L;
                                    tLRPC$TL_messageMediaVenue.title = sb4.toString();
                                    tLRPC$TL_messageMediaVenue.icon = "pin";
                                    tLRPC$TL_messageMediaVenue.address = LocaleController.getString(R.string.PassportAddress);
                                    arrayList.add(tLRPC$TL_messageMediaVenue);
                                }
                                hashSet = hashSet3;
                                hashSet2 = hashSet6;
                                i2 = i3;
                            } else {
                                if (sb3 != null && sb3.length() > 0) {
                                    TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue2 = new TLRPC$TL_messageMediaVenue();
                                    TLRPC$TL_geoPoint tLRPC$TL_geoPoint2 = new TLRPC$TL_geoPoint();
                                    tLRPC$TL_messageMediaVenue2.geo = tLRPC$TL_geoPoint2;
                                    tLRPC$TL_geoPoint2.lat = latitude;
                                    tLRPC$TL_geoPoint2._long = longitude;
                                    tLRPC$TL_messageMediaVenue2.query_id = -1L;
                                    tLRPC$TL_messageMediaVenue2.title = sb3.toString();
                                    tLRPC$TL_messageMediaVenue2.icon = "pin";
                                    if (z3) {
                                        i4 = R.string.PassportCity;
                                        str4 = "PassportCity";
                                    } else {
                                        str4 = "PassportStreet1";
                                        i4 = R.string.PassportStreet1;
                                    }
                                    tLRPC$TL_messageMediaVenue2.address = LocaleController.getString(str4, i4);
                                    if (address != null) {
                                        TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress = new TL_stories$TL_geoPointAddress();
                                        tLRPC$TL_messageMediaVenue2.geoAddress = tL_stories$TL_geoPointAddress;
                                        tL_stories$TL_geoPointAddress.country_iso2 = address.getCountryCode();
                                        String locality2 = TextUtils.isEmpty(null) ? address.getLocality() : null;
                                        if (TextUtils.isEmpty(locality2)) {
                                            locality2 = address.getAdminArea();
                                        }
                                        if (TextUtils.isEmpty(locality2)) {
                                            locality2 = address.getSubAdminArea();
                                        }
                                        String adminArea = address.getAdminArea();
                                        StringBuilder sb5 = new StringBuilder();
                                        if (!TextUtils.isEmpty(adminArea)) {
                                            TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress2 = tLRPC$TL_messageMediaVenue2.geoAddress;
                                            tL_stories$TL_geoPointAddress2.state = adminArea;
                                            tL_stories$TL_geoPointAddress2.flags |= 1;
                                        }
                                        if (!TextUtils.isEmpty(locality2)) {
                                            TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress3 = tLRPC$TL_messageMediaVenue2.geoAddress;
                                            tL_stories$TL_geoPointAddress3.city = locality2;
                                            tL_stories$TL_geoPointAddress3.flags |= 2;
                                        }
                                        if (!z3) {
                                            if (TextUtils.isEmpty(null)) {
                                                str5 = str3;
                                                if (!TextUtils.equals(address.getThoroughfare(), str5) && !TextUtils.equals(address.getThoroughfare(), address.getCountryName())) {
                                                    str6 = address.getThoroughfare();
                                                    if (TextUtils.isEmpty(str6) || TextUtils.equals(address.getSubLocality(), str5)) {
                                                        str7 = str6;
                                                    } else {
                                                        str7 = str6;
                                                        if (!TextUtils.equals(address.getSubLocality(), address.getCountryName())) {
                                                            str8 = address.getSubLocality();
                                                            if (TextUtils.isEmpty(str8) && !TextUtils.equals(address.getLocality(), str5) && !TextUtils.equals(address.getLocality(), address.getCountryName())) {
                                                                str8 = address.getLocality();
                                                            }
                                                            if (!TextUtils.isEmpty(str8) || TextUtils.equals(str8, adminArea) || TextUtils.equals(str8, address.getCountryName())) {
                                                                sb5 = null;
                                                            } else {
                                                                if (sb5.length() > 0) {
                                                                    sb5.append(", ");
                                                                }
                                                                sb5.append(str8);
                                                            }
                                                            if (!TextUtils.isEmpty(sb5)) {
                                                                int i9 = 0;
                                                                while (true) {
                                                                    String[] strArr2 = LocationController.unnamedRoads;
                                                                    if (i9 >= strArr2.length) {
                                                                        break;
                                                                    } else if (strArr2[i9].equalsIgnoreCase(sb5.toString())) {
                                                                        z4 = true;
                                                                        break;
                                                                    } else {
                                                                        i9++;
                                                                    }
                                                                }
                                                            }
                                                            z4 = false;
                                                            if (!TextUtils.isEmpty(sb5)) {
                                                                TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress4 = tLRPC$TL_messageMediaVenue2.geoAddress;
                                                                tL_stories$TL_geoPointAddress4.flags |= 4;
                                                                tL_stories$TL_geoPointAddress4.street = sb5.toString();
                                                            }
                                                            if (!z4) {
                                                                arrayList.add(tLRPC$TL_messageMediaVenue2);
                                                                i2 = i3;
                                                                if (arrayList.size() >= i2) {
                                                                    break;
                                                                }
                                                                if (z2) {
                                                                    String sb6 = sb2.toString();
                                                                    hashSet = hashSet3;
                                                                    if (!hashSet.contains(sb6)) {
                                                                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue3 = new TLRPC$TL_messageMediaVenue();
                                                                        TLRPC$TL_geoPoint tLRPC$TL_geoPoint3 = new TLRPC$TL_geoPoint();
                                                                        tLRPC$TL_messageMediaVenue3.geo = tLRPC$TL_geoPoint3;
                                                                        tLRPC$TL_geoPoint3.lat = latitude;
                                                                        tLRPC$TL_geoPoint3._long = longitude;
                                                                        tLRPC$TL_messageMediaVenue3.query_id = -1L;
                                                                        tLRPC$TL_messageMediaVenue3.title = sb2.toString();
                                                                        tLRPC$TL_messageMediaVenue3.icon = "https://ss3.4sqi.net/img/categories_v2/travel/hotel_64.png";
                                                                        tLRPC$TL_messageMediaVenue3.emoji = LocationController.countryCodeToEmoji(address2.getCountryCode());
                                                                        hashSet.add(tLRPC$TL_messageMediaVenue3.title);
                                                                        tLRPC$TL_messageMediaVenue3.address = LocaleController.getString("PassportCity", R.string.PassportCity);
                                                                        if (address != null) {
                                                                            TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress5 = new TL_stories$TL_geoPointAddress();
                                                                            tLRPC$TL_messageMediaVenue3.geoAddress = tL_stories$TL_geoPointAddress5;
                                                                            tL_stories$TL_geoPointAddress5.country_iso2 = address.getCountryCode();
                                                                            String locality3 = TextUtils.isEmpty(null) ? address.getLocality() : null;
                                                                            if (TextUtils.isEmpty(locality3)) {
                                                                                locality3 = address.getAdminArea();
                                                                            }
                                                                            if (TextUtils.isEmpty(locality3)) {
                                                                                locality3 = address.getSubAdminArea();
                                                                            }
                                                                            String adminArea2 = address.getAdminArea();
                                                                            if (!TextUtils.isEmpty(adminArea2)) {
                                                                                TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress6 = tLRPC$TL_messageMediaVenue3.geoAddress;
                                                                                tL_stories$TL_geoPointAddress6.state = adminArea2;
                                                                                tL_stories$TL_geoPointAddress6.flags |= 1;
                                                                            }
                                                                            if (!TextUtils.isEmpty(locality3)) {
                                                                                TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress7 = tLRPC$TL_messageMediaVenue3.geoAddress;
                                                                                tL_stories$TL_geoPointAddress7.city = locality3;
                                                                                tL_stories$TL_geoPointAddress7.flags |= 2;
                                                                            }
                                                                        }
                                                                        arrayList.add(tLRPC$TL_messageMediaVenue3);
                                                                        if (arrayList.size() >= i2) {
                                                                            break;
                                                                        }
                                                                        if (sb.length() > 0) {
                                                                            hashSet2 = hashSet6;
                                                                            if (!hashSet2.contains(sb.toString())) {
                                                                                TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue4 = new TLRPC$TL_messageMediaVenue();
                                                                                TLRPC$TL_geoPoint tLRPC$TL_geoPoint4 = new TLRPC$TL_geoPoint();
                                                                                tLRPC$TL_messageMediaVenue4.geo = tLRPC$TL_geoPoint4;
                                                                                tLRPC$TL_geoPoint4.lat = latitude;
                                                                                tLRPC$TL_geoPoint4._long = longitude;
                                                                                tLRPC$TL_messageMediaVenue4.query_id = -1L;
                                                                                tLRPC$TL_messageMediaVenue4.title = sb.toString();
                                                                                tLRPC$TL_messageMediaVenue4.icon = "https://ss3.4sqi.net/img/categories_v2/building/government_capitolbuilding_64.png";
                                                                                tLRPC$TL_messageMediaVenue4.emoji = LocationController.countryCodeToEmoji(address2.getCountryCode());
                                                                                hashSet2.add(tLRPC$TL_messageMediaVenue4.title);
                                                                                tLRPC$TL_messageMediaVenue4.address = LocaleController.getString("Country", R.string.Country);
                                                                                if (address != null) {
                                                                                    TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress8 = new TL_stories$TL_geoPointAddress();
                                                                                    tLRPC$TL_messageMediaVenue4.geoAddress = tL_stories$TL_geoPointAddress8;
                                                                                    tL_stories$TL_geoPointAddress8.country_iso2 = address.getCountryCode();
                                                                                }
                                                                                arrayList.add(tLRPC$TL_messageMediaVenue4);
                                                                                if (arrayList.size() >= i2) {
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                continue;
                                                                            }
                                                                        } else {
                                                                            hashSet2 = hashSet6;
                                                                        }
                                                                        i6 = i + 1;
                                                                        baseLocationAdapter = this;
                                                                        hashSet5 = hashSet;
                                                                        i5 = i2;
                                                                        hashSet4 = hashSet2;
                                                                        fromLocationName = list;
                                                                    }
                                                                } else {
                                                                    hashSet = hashSet3;
                                                                }
                                                                if (sb.length() > 0) {
                                                                }
                                                                i6 = i + 1;
                                                                baseLocationAdapter = this;
                                                                hashSet5 = hashSet;
                                                                i5 = i2;
                                                                hashSet4 = hashSet2;
                                                                fromLocationName = list;
                                                            }
                                                        }
                                                    }
                                                    str8 = str7;
                                                    if (TextUtils.isEmpty(str8)) {
                                                        str8 = address.getLocality();
                                                    }
                                                    if (TextUtils.isEmpty(str8)) {
                                                    }
                                                    sb5 = null;
                                                    if (!TextUtils.isEmpty(sb5)) {
                                                    }
                                                    z4 = false;
                                                    if (!TextUtils.isEmpty(sb5)) {
                                                    }
                                                    if (!z4) {
                                                    }
                                                }
                                            } else {
                                                str5 = str3;
                                            }
                                            str6 = null;
                                            if (TextUtils.isEmpty(str6)) {
                                            }
                                            str7 = str6;
                                            str8 = str7;
                                            if (TextUtils.isEmpty(str8)) {
                                            }
                                            if (TextUtils.isEmpty(str8)) {
                                            }
                                            sb5 = null;
                                            if (!TextUtils.isEmpty(sb5)) {
                                            }
                                            z4 = false;
                                            if (!TextUtils.isEmpty(sb5)) {
                                            }
                                            if (!z4) {
                                            }
                                        }
                                    }
                                    z4 = false;
                                    if (!z4) {
                                    }
                                }
                                i2 = i3;
                                if (z2) {
                                }
                                if (sb.length() > 0) {
                                }
                                i6 = i + 1;
                                baseLocationAdapter = this;
                                hashSet5 = hashSet;
                                i5 = i2;
                                hashSet4 = hashSet2;
                                fromLocationName = list;
                            }
                        }
                    }
                    String subLocality = address2.getSubLocality();
                    if (!TextUtils.isEmpty(subLocality)) {
                        if (sb3.length() > 0) {
                            sb3.append(", ");
                        }
                        sb3.append(subLocality);
                    } else {
                        String locality4 = address2.getLocality();
                        if (!TextUtils.isEmpty(locality4) && !TextUtils.equals(locality4, str11)) {
                            if (sb3.length() > 0) {
                                sb3.append(", ");
                            }
                            sb3.append(locality4);
                        } else {
                            sb3 = null;
                            z = true;
                            if (TextUtils.isEmpty(str11)) {
                            }
                            countryName = address2.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                            }
                            if (baseLocationAdapter.biz) {
                            }
                        }
                    }
                    z = false;
                    if (TextUtils.isEmpty(str11)) {
                    }
                    countryName = address2.getCountryName();
                    if (TextUtils.isEmpty(countryName)) {
                    }
                    if (baseLocationAdapter.biz) {
                    }
                } else {
                    list = fromLocationName;
                    list2 = fromLocationName2;
                    hashSet = hashSet5;
                    i = i6;
                    hashSet2 = hashSet4;
                    i2 = i5;
                }
                i6 = i + 1;
                baseLocationAdapter = this;
                hashSet5 = hashSet;
                i5 = i2;
                hashSet4 = hashSet2;
                fromLocationName = list;
            }
        } catch (Exception unused2) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                BaseLocationAdapter.this.lambda$searchPlacesWithQuery$4(location, str2, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchPlacesWithQuery$4(Location location, String str, ArrayList arrayList) {
        this.searchingLocations = false;
        if (location == null) {
            this.currentRequestNum = 0;
            this.searching = false;
            this.places.clear();
            this.searchInProgress = false;
            this.lastFoundQuery = str;
        }
        this.locations.clear();
        this.locations.addAll(arrayList);
        update(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchPlacesWithQuery$7(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                BaseLocationAdapter.this.lambda$searchPlacesWithQuery$6(tLRPC$TL_error, str, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchPlacesWithQuery$6(TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            this.currentRequestNum = 0;
            this.searching = false;
            this.places.clear();
            this.searchInProgress = false;
            this.lastFoundQuery = str;
            TLRPC$messages_BotResults tLRPC$messages_BotResults = (TLRPC$messages_BotResults) tLObject;
            int size = tLRPC$messages_BotResults.results.size();
            for (int i = 0; i < size; i++) {
                TLRPC$BotInlineResult tLRPC$BotInlineResult = tLRPC$messages_BotResults.results.get(i);
                if ("venue".equals(tLRPC$BotInlineResult.type)) {
                    TLRPC$BotInlineMessage tLRPC$BotInlineMessage = tLRPC$BotInlineResult.send_message;
                    if (tLRPC$BotInlineMessage instanceof TLRPC$TL_botInlineMessageMediaVenue) {
                        TLRPC$TL_botInlineMessageMediaVenue tLRPC$TL_botInlineMessageMediaVenue = (TLRPC$TL_botInlineMessageMediaVenue) tLRPC$BotInlineMessage;
                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue = new TLRPC$TL_messageMediaVenue();
                        tLRPC$TL_messageMediaVenue.geo = tLRPC$TL_botInlineMessageMediaVenue.geo;
                        tLRPC$TL_messageMediaVenue.address = tLRPC$TL_botInlineMessageMediaVenue.address;
                        tLRPC$TL_messageMediaVenue.title = tLRPC$TL_botInlineMessageMediaVenue.title;
                        tLRPC$TL_messageMediaVenue.icon = "https://ss3.4sqi.net/img/categories_v2/" + tLRPC$TL_botInlineMessageMediaVenue.venue_type + "_64.png";
                        tLRPC$TL_messageMediaVenue.venue_type = tLRPC$TL_botInlineMessageMediaVenue.venue_type;
                        tLRPC$TL_messageMediaVenue.venue_id = tLRPC$TL_botInlineMessageMediaVenue.venue_id;
                        tLRPC$TL_messageMediaVenue.provider = tLRPC$TL_botInlineMessageMediaVenue.provider;
                        tLRPC$TL_messageMediaVenue.query_id = tLRPC$messages_BotResults.query_id;
                        tLRPC$TL_messageMediaVenue.result_id = tLRPC$BotInlineResult.id;
                        this.places.add(tLRPC$TL_messageMediaVenue);
                    }
                }
            }
        }
        BaseLocationAdapterDelegate baseLocationAdapterDelegate = this.delegate;
        if (baseLocationAdapterDelegate != null) {
            baseLocationAdapterDelegate.didLoadSearchResult(this.places);
        }
        update(true);
    }

    protected void update(boolean z) {
        notifyDataSetChanged();
    }
}
