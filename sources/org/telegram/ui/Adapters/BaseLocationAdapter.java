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
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes.dex */
public abstract class BaseLocationAdapter extends RecyclerListView.SelectionAdapter {
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

    /* loaded from: classes.dex */
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
            notifyDataSetChanged();
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
        if (location != null || this.stories) {
            Location location2 = this.lastSearchLocation;
            if (location2 == null || location == null || location.distanceTo(location2) >= 200.0f) {
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
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            BaseLocationAdapter.this.lambda$searchPlacesWithQuery$5(currentLocale, str, location, str);
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
                notifyDataSetChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00d5 A[Catch: Exception -> 0x02da, TryCatch #1 {Exception -> 0x02da, blocks: (B:3:0x0007, B:7:0x0010, B:8:0x002a, B:10:0x0030, B:12:0x003c, B:15:0x0044, B:17:0x0065, B:18:0x0069, B:22:0x007f, B:24:0x0089, B:26:0x008f, B:27:0x0092, B:45:0x00cf, B:47:0x00d5, B:49:0x00db, B:50:0x00de, B:52:0x00e3, B:54:0x00e9, B:55:0x00ec, B:57:0x00f1, B:59:0x00fb, B:61:0x0109, B:63:0x0115, B:65:0x0121, B:76:0x0172, B:78:0x0178, B:79:0x017b, B:81:0x0184, B:82:0x0187, B:84:0x0191, B:87:0x0199, B:92:0x01ac, B:94:0x01b2, B:98:0x01e4, B:100:0x01ea, B:102:0x0206, B:104:0x0215, B:109:0x0228, B:111:0x0234, B:115:0x0274, B:117:0x027a, B:119:0x0286, B:103:0x020d, B:69:0x0134, B:71:0x0148, B:73:0x0152, B:75:0x016b, B:29:0x0098, B:31:0x00a2, B:33:0x00a8, B:34:0x00ab, B:36:0x00b0, B:38:0x00ba, B:40:0x00c0, B:42:0x00c6, B:43:0x00c9), top: B:131:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00fb A[Catch: Exception -> 0x02da, TryCatch #1 {Exception -> 0x02da, blocks: (B:3:0x0007, B:7:0x0010, B:8:0x002a, B:10:0x0030, B:12:0x003c, B:15:0x0044, B:17:0x0065, B:18:0x0069, B:22:0x007f, B:24:0x0089, B:26:0x008f, B:27:0x0092, B:45:0x00cf, B:47:0x00d5, B:49:0x00db, B:50:0x00de, B:52:0x00e3, B:54:0x00e9, B:55:0x00ec, B:57:0x00f1, B:59:0x00fb, B:61:0x0109, B:63:0x0115, B:65:0x0121, B:76:0x0172, B:78:0x0178, B:79:0x017b, B:81:0x0184, B:82:0x0187, B:84:0x0191, B:87:0x0199, B:92:0x01ac, B:94:0x01b2, B:98:0x01e4, B:100:0x01ea, B:102:0x0206, B:104:0x0215, B:109:0x0228, B:111:0x0234, B:115:0x0274, B:117:0x027a, B:119:0x0286, B:103:0x020d, B:69:0x0134, B:71:0x0148, B:73:0x0152, B:75:0x016b, B:29:0x0098, B:31:0x00a2, B:33:0x00a8, B:34:0x00ab, B:36:0x00b0, B:38:0x00ba, B:40:0x00c0, B:42:0x00c6, B:43:0x00c9), top: B:131:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0178 A[Catch: Exception -> 0x02da, TryCatch #1 {Exception -> 0x02da, blocks: (B:3:0x0007, B:7:0x0010, B:8:0x002a, B:10:0x0030, B:12:0x003c, B:15:0x0044, B:17:0x0065, B:18:0x0069, B:22:0x007f, B:24:0x0089, B:26:0x008f, B:27:0x0092, B:45:0x00cf, B:47:0x00d5, B:49:0x00db, B:50:0x00de, B:52:0x00e3, B:54:0x00e9, B:55:0x00ec, B:57:0x00f1, B:59:0x00fb, B:61:0x0109, B:63:0x0115, B:65:0x0121, B:76:0x0172, B:78:0x0178, B:79:0x017b, B:81:0x0184, B:82:0x0187, B:84:0x0191, B:87:0x0199, B:92:0x01ac, B:94:0x01b2, B:98:0x01e4, B:100:0x01ea, B:102:0x0206, B:104:0x0215, B:109:0x0228, B:111:0x0234, B:115:0x0274, B:117:0x027a, B:119:0x0286, B:103:0x020d, B:69:0x0134, B:71:0x0148, B:73:0x0152, B:75:0x016b, B:29:0x0098, B:31:0x00a2, B:33:0x00a8, B:34:0x00ab, B:36:0x00b0, B:38:0x00ba, B:40:0x00c0, B:42:0x00c6, B:43:0x00c9), top: B:131:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0184 A[Catch: Exception -> 0x02da, TryCatch #1 {Exception -> 0x02da, blocks: (B:3:0x0007, B:7:0x0010, B:8:0x002a, B:10:0x0030, B:12:0x003c, B:15:0x0044, B:17:0x0065, B:18:0x0069, B:22:0x007f, B:24:0x0089, B:26:0x008f, B:27:0x0092, B:45:0x00cf, B:47:0x00d5, B:49:0x00db, B:50:0x00de, B:52:0x00e3, B:54:0x00e9, B:55:0x00ec, B:57:0x00f1, B:59:0x00fb, B:61:0x0109, B:63:0x0115, B:65:0x0121, B:76:0x0172, B:78:0x0178, B:79:0x017b, B:81:0x0184, B:82:0x0187, B:84:0x0191, B:87:0x0199, B:92:0x01ac, B:94:0x01b2, B:98:0x01e4, B:100:0x01ea, B:102:0x0206, B:104:0x0215, B:109:0x0228, B:111:0x0234, B:115:0x0274, B:117:0x027a, B:119:0x0286, B:103:0x020d, B:69:0x0134, B:71:0x0148, B:73:0x0152, B:75:0x016b, B:29:0x0098, B:31:0x00a2, B:33:0x00a8, B:34:0x00ab, B:36:0x00b0, B:38:0x00ba, B:40:0x00c0, B:42:0x00c6, B:43:0x00c9), top: B:131:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0199 A[Catch: Exception -> 0x02da, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x02da, blocks: (B:3:0x0007, B:7:0x0010, B:8:0x002a, B:10:0x0030, B:12:0x003c, B:15:0x0044, B:17:0x0065, B:18:0x0069, B:22:0x007f, B:24:0x0089, B:26:0x008f, B:27:0x0092, B:45:0x00cf, B:47:0x00d5, B:49:0x00db, B:50:0x00de, B:52:0x00e3, B:54:0x00e9, B:55:0x00ec, B:57:0x00f1, B:59:0x00fb, B:61:0x0109, B:63:0x0115, B:65:0x0121, B:76:0x0172, B:78:0x0178, B:79:0x017b, B:81:0x0184, B:82:0x0187, B:84:0x0191, B:87:0x0199, B:92:0x01ac, B:94:0x01b2, B:98:0x01e4, B:100:0x01ea, B:102:0x0206, B:104:0x0215, B:109:0x0228, B:111:0x0234, B:115:0x0274, B:117:0x027a, B:119:0x0286, B:103:0x020d, B:69:0x0134, B:71:0x0148, B:73:0x0152, B:75:0x016b, B:29:0x0098, B:31:0x00a2, B:33:0x00a8, B:34:0x00ab, B:36:0x00b0, B:38:0x00ba, B:40:0x00c0, B:42:0x00c6, B:43:0x00c9), top: B:131:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$searchPlacesWithQuery$5(Locale locale, String str, final Location location, final String str2) {
        int i;
        List<Address> list;
        HashSet hashSet;
        HashSet hashSet2;
        int i2;
        HashSet hashSet3;
        boolean z;
        String countryName;
        int i3;
        boolean z2;
        HashSet hashSet4;
        String str3;
        String str4;
        String[] strArr;
        final ArrayList arrayList = new ArrayList();
        try {
            int i4 = this.biz ? 10 : 5;
            List<Address> fromLocationName = new Geocoder(ApplicationLoader.applicationContext, locale).getFromLocationName(str, 5);
            HashSet hashSet5 = new HashSet();
            HashSet hashSet6 = new HashSet();
            int i5 = 0;
            while (i5 < fromLocationName.size()) {
                Address address = fromLocationName.get(i5);
                if (address.hasLatitude() && address.hasLongitude()) {
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    String locality = address.getLocality();
                    if (TextUtils.isEmpty(locality)) {
                        locality = address.getAdminArea();
                    }
                    list = fromLocationName;
                    String str5 = locality;
                    String thoroughfare = address.getThoroughfare();
                    boolean z3 = true;
                    i2 = i5;
                    if (TextUtils.isEmpty(thoroughfare)) {
                        hashSet3 = hashSet5;
                    } else {
                        hashSet3 = hashSet5;
                        if (!TextUtils.equals(thoroughfare, address.getAdminArea())) {
                            if (sb3.length() > 0) {
                                sb3.append(", ");
                            }
                            sb3.append(thoroughfare);
                            z = false;
                            if (!TextUtils.isEmpty(str5)) {
                                if (sb2.length() > 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(str5);
                                if (sb3 != null) {
                                    if (sb3.length() > 0) {
                                        sb3.append(", ");
                                    }
                                    sb3.append(str5);
                                }
                                z3 = false;
                            }
                            countryName = address.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                                i3 = i4;
                                z2 = z;
                                hashSet4 = hashSet6;
                            } else {
                                hashSet4 = hashSet6;
                                if (!"US".equals(address.getCountryCode()) && !"AE".equals(address.getCountryCode()) && (!"GB".equals(address.getCountryCode()) || !"en".equals(locale.getLanguage()))) {
                                    i3 = i4;
                                    str3 = countryName;
                                    z2 = z;
                                    if (sb2.length() > 0) {
                                        sb2.append(", ");
                                    }
                                    sb2.append(str3);
                                    if (sb.length() > 0) {
                                        sb.append(", ");
                                    }
                                    sb.append(countryName);
                                }
                                String[] split = countryName.split(" ");
                                int length = split.length;
                                i3 = i4;
                                str3 = "";
                                z2 = z;
                                int i6 = 0;
                                while (i6 < length) {
                                    int i7 = length;
                                    if (split[i6].length() > 0) {
                                        strArr = split;
                                        str3 = str3 + str4.charAt(0);
                                    } else {
                                        strArr = split;
                                    }
                                    i6++;
                                    length = i7;
                                    split = strArr;
                                }
                                if (sb2.length() > 0) {
                                }
                                sb2.append(str3);
                                if (sb.length() > 0) {
                                }
                                sb.append(countryName);
                            }
                            if (this.biz) {
                                StringBuilder sb4 = new StringBuilder();
                                try {
                                    String addressLine = address.getAddressLine(0);
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
                                hashSet2 = hashSet4;
                                i = i3;
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
                                    tLRPC$TL_messageMediaVenue2.address = z2 ? LocaleController.getString("PassportCity", R.string.PassportCity) : LocaleController.getString("PassportStreet1", R.string.PassportStreet1);
                                    arrayList.add(tLRPC$TL_messageMediaVenue2);
                                    i = i3;
                                    if (arrayList.size() >= i) {
                                        break;
                                    }
                                } else {
                                    i = i3;
                                }
                                if (!z3) {
                                    hashSet2 = hashSet4;
                                    if (!hashSet2.contains(sb2.toString())) {
                                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue3 = new TLRPC$TL_messageMediaVenue();
                                        TLRPC$TL_geoPoint tLRPC$TL_geoPoint3 = new TLRPC$TL_geoPoint();
                                        tLRPC$TL_messageMediaVenue3.geo = tLRPC$TL_geoPoint3;
                                        tLRPC$TL_geoPoint3.lat = latitude;
                                        tLRPC$TL_geoPoint3._long = longitude;
                                        tLRPC$TL_messageMediaVenue3.query_id = -1L;
                                        tLRPC$TL_messageMediaVenue3.title = sb2.toString();
                                        tLRPC$TL_messageMediaVenue3.icon = "https://ss3.4sqi.net/img/categories_v2/travel/hotel_64.png";
                                        tLRPC$TL_messageMediaVenue3.emoji = LocationController.countryCodeToEmoji(address.getCountryCode());
                                        hashSet2.add(tLRPC$TL_messageMediaVenue3.title);
                                        tLRPC$TL_messageMediaVenue3.address = LocaleController.getString("PassportCity", R.string.PassportCity);
                                        arrayList.add(tLRPC$TL_messageMediaVenue3);
                                        if (arrayList.size() >= i) {
                                            break;
                                        }
                                    }
                                } else {
                                    hashSet2 = hashSet4;
                                }
                                if (sb.length() > 0) {
                                    hashSet = hashSet3;
                                    if (hashSet.contains(sb.toString())) {
                                        continue;
                                    } else {
                                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue4 = new TLRPC$TL_messageMediaVenue();
                                        TLRPC$TL_geoPoint tLRPC$TL_geoPoint4 = new TLRPC$TL_geoPoint();
                                        tLRPC$TL_messageMediaVenue4.geo = tLRPC$TL_geoPoint4;
                                        tLRPC$TL_geoPoint4.lat = latitude;
                                        tLRPC$TL_geoPoint4._long = longitude;
                                        tLRPC$TL_messageMediaVenue4.query_id = -1L;
                                        tLRPC$TL_messageMediaVenue4.title = sb.toString();
                                        tLRPC$TL_messageMediaVenue4.icon = "https://ss3.4sqi.net/img/categories_v2/building/government_capitolbuilding_64.png";
                                        tLRPC$TL_messageMediaVenue4.emoji = LocationController.countryCodeToEmoji(address.getCountryCode());
                                        hashSet.add(tLRPC$TL_messageMediaVenue4.title);
                                        tLRPC$TL_messageMediaVenue4.address = LocaleController.getString("Country", R.string.Country);
                                        arrayList.add(tLRPC$TL_messageMediaVenue4);
                                        if (arrayList.size() >= i) {
                                            break;
                                        }
                                    }
                                } else {
                                    hashSet = hashSet3;
                                }
                            }
                            i5 = i2 + 1;
                            hashSet5 = hashSet;
                            i4 = i;
                            hashSet6 = hashSet2;
                            fromLocationName = list;
                        }
                    }
                    String subLocality = address.getSubLocality();
                    if (!TextUtils.isEmpty(subLocality)) {
                        if (sb3.length() > 0) {
                            sb3.append(", ");
                        }
                        sb3.append(subLocality);
                    } else {
                        String locality2 = address.getLocality();
                        if (!TextUtils.isEmpty(locality2) && !TextUtils.equals(locality2, str5)) {
                            if (sb3.length() > 0) {
                                sb3.append(", ");
                            }
                            sb3.append(locality2);
                        } else {
                            sb3 = null;
                            z = true;
                            if (!TextUtils.isEmpty(str5)) {
                            }
                            countryName = address.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                            }
                            if (this.biz) {
                            }
                            i5 = i2 + 1;
                            hashSet5 = hashSet;
                            i4 = i;
                            hashSet6 = hashSet2;
                            fromLocationName = list;
                        }
                    }
                    z = false;
                    if (!TextUtils.isEmpty(str5)) {
                    }
                    countryName = address.getCountryName();
                    if (TextUtils.isEmpty(countryName)) {
                    }
                    if (this.biz) {
                    }
                    i5 = i2 + 1;
                    hashSet5 = hashSet;
                    i4 = i;
                    hashSet6 = hashSet2;
                    fromLocationName = list;
                }
                i = i4;
                list = fromLocationName;
                hashSet = hashSet5;
                hashSet2 = hashSet6;
                i2 = i5;
                i5 = i2 + 1;
                hashSet5 = hashSet;
                i4 = i;
                hashSet6 = hashSet2;
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
        notifyDataSetChanged();
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
        notifyDataSetChanged();
    }
}
