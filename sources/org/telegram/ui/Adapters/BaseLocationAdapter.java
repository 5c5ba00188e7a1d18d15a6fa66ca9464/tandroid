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
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda1
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda2
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
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda6
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BaseLocationAdapter.this.lambda$searchBotUser$3(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchBotUser$3(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda4
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
                            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda3
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
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda3
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
                this.currentRequestNum = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getInlineBotResults, new RequestDelegate() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda7
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
    /* JADX WARN: Removed duplicated region for block: B:103:0x01d8 A[Catch: Exception -> 0x04b3, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0309 A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x032b A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:177:0x034f  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x036d A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x037f A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0390 A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0432  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x043b A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0497  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x010b A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0134 A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b4 A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01c0 A[Catch: Exception -> 0x04b3, TryCatch #1 {Exception -> 0x04b3, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:25:0x0067, B:27:0x008c, B:30:0x0094, B:32:0x009e, B:33:0x00a1, B:37:0x00b5, B:39:0x00bf, B:41:0x00c5, B:42:0x00c8, B:60:0x0105, B:62:0x010b, B:64:0x0111, B:65:0x0114, B:67:0x0119, B:69:0x011f, B:70:0x0122, B:73:0x012a, B:75:0x0134, B:77:0x0144, B:79:0x0150, B:81:0x015c, B:92:0x01ae, B:94:0x01b4, B:95:0x01b7, B:97:0x01c0, B:98:0x01c3, B:100:0x01cf, B:103:0x01d8, B:108:0x01eb, B:110:0x01f1, B:114:0x0223, B:116:0x0229, B:118:0x0245, B:120:0x0254, B:122:0x0258, B:124:0x026c, B:126:0x0272, B:128:0x0278, B:129:0x027c, B:131:0x0282, B:132:0x0286, B:134:0x0295, B:135:0x029f, B:137:0x02a5, B:139:0x02b1, B:141:0x02b8, B:143:0x02c4, B:145:0x02d2, B:148:0x02da, B:150:0x02e0, B:152:0x02ea, B:154:0x02fa, B:157:0x0303, B:159:0x0309, B:161:0x0313, B:163:0x0321, B:164:0x0325, B:166:0x032b, B:168:0x0331, B:170:0x033b, B:172:0x0341, B:173:0x0344, B:175:0x0349, B:178:0x0350, B:180:0x0355, B:185:0x0367, B:187:0x036d, B:190:0x037f, B:195:0x0390, B:197:0x039c, B:199:0x03d3, B:201:0x03e7, B:203:0x03ed, B:205:0x03f3, B:206:0x03f7, B:208:0x03fd, B:209:0x0401, B:211:0x040b, B:212:0x0415, B:214:0x041b, B:216:0x0427, B:221:0x0435, B:223:0x043b, B:225:0x0447, B:227:0x0480, B:228:0x048d, B:183:0x0363, B:119:0x024c, B:85:0x016f, B:87:0x0183, B:89:0x018d, B:91:0x01a7, B:44:0x00ce, B:46:0x00d8, B:48:0x00de, B:49:0x00e1, B:51:0x00e6, B:53:0x00f0, B:55:0x00f6, B:57:0x00fc, B:58:0x00ff), top: B:241:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01c7  */
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
        boolean z4;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String[] strArr;
        BaseLocationAdapter baseLocationAdapter = this;
        final ArrayList arrayList = new ArrayList();
        try {
            int i4 = baseLocationAdapter.biz ? 10 : 5;
            List<Address> fromLocationName = baseLocationAdapter.stories ? new Geocoder(ApplicationLoader.applicationContext, locale2).getFromLocationName(str, 5) : null;
            HashSet hashSet4 = new HashSet();
            HashSet hashSet5 = new HashSet();
            int i5 = 0;
            for (List<Address> fromLocationName2 = new Geocoder(ApplicationLoader.applicationContext, locale).getFromLocationName(str, 5); i5 < fromLocationName2.size(); fromLocationName2 = list2) {
                Address address2 = fromLocationName2.get(i5);
                Address address3 = (fromLocationName == null || i5 >= fromLocationName.size()) ? null : fromLocationName.get(i5);
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
                    String str10 = locality;
                    if (address3 != null && TextUtils.isEmpty(address3.getLocality())) {
                        address3.getAdminArea();
                    }
                    i = i5;
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
                            if (TextUtils.isEmpty(str10)) {
                                z2 = true;
                            } else {
                                if (sb2.length() > 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(str10);
                                if (sb3 != null) {
                                    if (sb3.length() > 0) {
                                        sb3.append(", ");
                                    }
                                    sb3.append(str10);
                                }
                                z2 = false;
                            }
                            countryName = address2.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                                i3 = i4;
                                z3 = z;
                                str3 = str10;
                                address = address3;
                            } else {
                                i3 = i4;
                                str3 = str10;
                                if (!"US".equals(address2.getCountryCode()) && !"AE".equals(address2.getCountryCode()) && (!"GB".equals(address2.getCountryCode()) || !"en".equals(locale.getLanguage()))) {
                                    z3 = z;
                                    address = address3;
                                    str8 = countryName;
                                    if (sb2.length() > 0) {
                                        sb2.append(", ");
                                    }
                                    sb2.append(str8);
                                    if (sb.length() > 0) {
                                        sb.append(", ");
                                    }
                                    sb.append(countryName);
                                }
                                String[] split = countryName.split(" ");
                                int length = split.length;
                                address = address3;
                                str8 = "";
                                z3 = z;
                                int i6 = 0;
                                while (i6 < length) {
                                    int i7 = length;
                                    if (split[i6].length() > 0) {
                                        strArr = split;
                                        str8 = str8 + str9.charAt(0);
                                    } else {
                                        strArr = split;
                                    }
                                    i6++;
                                    length = i7;
                                    split = strArr;
                                }
                                if (sb2.length() > 0) {
                                }
                                sb2.append(str8);
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
                                i5 = i + 1;
                                baseLocationAdapter = this;
                                hashSet5 = hashSet;
                                i4 = i2;
                                hashSet4 = hashSet2;
                                fromLocationName = list;
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
                                    tLRPC$TL_messageMediaVenue2.address = z3 ? LocaleController.getString("PassportCity", R.string.PassportCity) : LocaleController.getString("PassportStreet1", R.string.PassportStreet1);
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
                                                str4 = str3;
                                                if (!TextUtils.equals(address.getThoroughfare(), str4) && !TextUtils.equals(address.getThoroughfare(), address.getCountryName())) {
                                                    str5 = address.getThoroughfare();
                                                    if (TextUtils.isEmpty(str5) || TextUtils.equals(address.getSubLocality(), str4)) {
                                                        str6 = str5;
                                                    } else {
                                                        str6 = str5;
                                                        if (!TextUtils.equals(address.getSubLocality(), address.getCountryName())) {
                                                            str7 = address.getSubLocality();
                                                            if (TextUtils.isEmpty(str7) && !TextUtils.equals(address.getLocality(), str4) && !TextUtils.equals(address.getLocality(), address.getCountryName())) {
                                                                str7 = address.getLocality();
                                                            }
                                                            if (!TextUtils.isEmpty(str7) || TextUtils.equals(str7, adminArea) || TextUtils.equals(str7, address.getCountryName())) {
                                                                sb5 = null;
                                                            } else {
                                                                if (sb5.length() > 0) {
                                                                    sb5.append(", ");
                                                                }
                                                                sb5.append(str7);
                                                            }
                                                            if (!TextUtils.isEmpty(sb5)) {
                                                                int i8 = 0;
                                                                while (true) {
                                                                    String[] strArr2 = LocationController.unnamedRoads;
                                                                    if (i8 >= strArr2.length) {
                                                                        break;
                                                                    } else if (strArr2[i8].equalsIgnoreCase(sb5.toString())) {
                                                                        z4 = true;
                                                                        break;
                                                                    } else {
                                                                        i8++;
                                                                    }
                                                                }
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
                                                                            i5 = i + 1;
                                                                            baseLocationAdapter = this;
                                                                            hashSet5 = hashSet;
                                                                            i4 = i2;
                                                                            hashSet4 = hashSet2;
                                                                            fromLocationName = list;
                                                                        }
                                                                    } else {
                                                                        hashSet = hashSet3;
                                                                    }
                                                                    if (sb.length() > 0) {
                                                                    }
                                                                    i5 = i + 1;
                                                                    baseLocationAdapter = this;
                                                                    hashSet5 = hashSet;
                                                                    i4 = i2;
                                                                    hashSet4 = hashSet2;
                                                                    fromLocationName = list;
                                                                }
                                                            }
                                                            z4 = false;
                                                            if (!TextUtils.isEmpty(sb5)) {
                                                            }
                                                            if (!z4) {
                                                            }
                                                        }
                                                    }
                                                    str7 = str6;
                                                    if (TextUtils.isEmpty(str7)) {
                                                        str7 = address.getLocality();
                                                    }
                                                    if (TextUtils.isEmpty(str7)) {
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
                                                str4 = str3;
                                            }
                                            str5 = null;
                                            if (TextUtils.isEmpty(str5)) {
                                            }
                                            str6 = str5;
                                            str7 = str6;
                                            if (TextUtils.isEmpty(str7)) {
                                            }
                                            if (TextUtils.isEmpty(str7)) {
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
                                i5 = i + 1;
                                baseLocationAdapter = this;
                                hashSet5 = hashSet;
                                i4 = i2;
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
                        if (!TextUtils.isEmpty(locality4) && !TextUtils.equals(locality4, str10)) {
                            if (sb3.length() > 0) {
                                sb3.append(", ");
                            }
                            sb3.append(locality4);
                        } else {
                            sb3 = null;
                            z = true;
                            if (TextUtils.isEmpty(str10)) {
                            }
                            countryName = address2.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                            }
                            if (baseLocationAdapter.biz) {
                            }
                        }
                    }
                    z = false;
                    if (TextUtils.isEmpty(str10)) {
                    }
                    countryName = address2.getCountryName();
                    if (TextUtils.isEmpty(countryName)) {
                    }
                    if (baseLocationAdapter.biz) {
                    }
                }
                list = fromLocationName;
                list2 = fromLocationName2;
                hashSet = hashSet5;
                i = i5;
                hashSet2 = hashSet4;
                i2 = i4;
                i5 = i + 1;
                baseLocationAdapter = this;
                hashSet5 = hashSet;
                i4 = i2;
                hashSet4 = hashSet2;
                fromLocationName = list;
            }
        } catch (Exception unused2) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda0
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda5
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
