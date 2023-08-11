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
/* loaded from: classes3.dex */
public abstract class BaseLocationAdapter extends RecyclerListView.SelectionAdapter {
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

    /* loaded from: classes3.dex */
    public interface BaseLocationAdapterDelegate {
        void didLoadSearchResult(ArrayList<TLRPC$TL_messageMediaVenue> arrayList);
    }

    public BaseLocationAdapter(boolean z) {
        this.stories = z;
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
            str = MessagesController.getInstance(this.currentAccount).venueSearchBot;
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
                    str2 = MessagesController.getInstance(this.currentAccount).venueSearchBot;
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
                if (!TextUtils.isEmpty(str) && this.stories) {
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
    /* JADX WARN: Removed duplicated region for block: B:114:0x0224 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0273 A[EDGE_INSN: B:117:0x0273->B:109:0x0273 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c5 A[Catch: Exception -> 0x0273, TryCatch #0 {Exception -> 0x0273, blocks: (B:3:0x0005, B:4:0x0020, B:6:0x0026, B:8:0x0032, B:11:0x003a, B:13:0x005b, B:14:0x005f, B:17:0x0071, B:19:0x007b, B:21:0x0081, B:22:0x0084, B:39:0x00bf, B:41:0x00c5, B:43:0x00cb, B:44:0x00ce, B:46:0x00d3, B:48:0x00d9, B:49:0x00dc, B:51:0x00e1, B:53:0x00eb, B:55:0x00f9, B:57:0x0105, B:59:0x0111, B:71:0x0166, B:73:0x016c, B:74:0x016f, B:76:0x0178, B:77:0x017b, B:79:0x0186, B:81:0x018e, B:83:0x0198, B:89:0x01dd, B:91:0x01e7, B:95:0x0227, B:97:0x022d, B:99:0x024b, B:100:0x024d, B:102:0x0257, B:101:0x0252, B:63:0x0125, B:65:0x0139, B:67:0x0143, B:69:0x015d, B:23:0x0088, B:25:0x0092, B:27:0x0098, B:28:0x009b, B:30:0x00a0, B:32:0x00aa, B:34:0x00b0, B:36:0x00b6, B:37:0x00b9), top: B:112:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00eb A[Catch: Exception -> 0x0273, TryCatch #0 {Exception -> 0x0273, blocks: (B:3:0x0005, B:4:0x0020, B:6:0x0026, B:8:0x0032, B:11:0x003a, B:13:0x005b, B:14:0x005f, B:17:0x0071, B:19:0x007b, B:21:0x0081, B:22:0x0084, B:39:0x00bf, B:41:0x00c5, B:43:0x00cb, B:44:0x00ce, B:46:0x00d3, B:48:0x00d9, B:49:0x00dc, B:51:0x00e1, B:53:0x00eb, B:55:0x00f9, B:57:0x0105, B:59:0x0111, B:71:0x0166, B:73:0x016c, B:74:0x016f, B:76:0x0178, B:77:0x017b, B:79:0x0186, B:81:0x018e, B:83:0x0198, B:89:0x01dd, B:91:0x01e7, B:95:0x0227, B:97:0x022d, B:99:0x024b, B:100:0x024d, B:102:0x0257, B:101:0x0252, B:63:0x0125, B:65:0x0139, B:67:0x0143, B:69:0x015d, B:23:0x0088, B:25:0x0092, B:27:0x0098, B:28:0x009b, B:30:0x00a0, B:32:0x00aa, B:34:0x00b0, B:36:0x00b6, B:37:0x00b9), top: B:112:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x016c A[Catch: Exception -> 0x0273, TryCatch #0 {Exception -> 0x0273, blocks: (B:3:0x0005, B:4:0x0020, B:6:0x0026, B:8:0x0032, B:11:0x003a, B:13:0x005b, B:14:0x005f, B:17:0x0071, B:19:0x007b, B:21:0x0081, B:22:0x0084, B:39:0x00bf, B:41:0x00c5, B:43:0x00cb, B:44:0x00ce, B:46:0x00d3, B:48:0x00d9, B:49:0x00dc, B:51:0x00e1, B:53:0x00eb, B:55:0x00f9, B:57:0x0105, B:59:0x0111, B:71:0x0166, B:73:0x016c, B:74:0x016f, B:76:0x0178, B:77:0x017b, B:79:0x0186, B:81:0x018e, B:83:0x0198, B:89:0x01dd, B:91:0x01e7, B:95:0x0227, B:97:0x022d, B:99:0x024b, B:100:0x024d, B:102:0x0257, B:101:0x0252, B:63:0x0125, B:65:0x0139, B:67:0x0143, B:69:0x015d, B:23:0x0088, B:25:0x0092, B:27:0x0098, B:28:0x009b, B:30:0x00a0, B:32:0x00aa, B:34:0x00b0, B:36:0x00b6, B:37:0x00b9), top: B:112:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0178 A[Catch: Exception -> 0x0273, TryCatch #0 {Exception -> 0x0273, blocks: (B:3:0x0005, B:4:0x0020, B:6:0x0026, B:8:0x0032, B:11:0x003a, B:13:0x005b, B:14:0x005f, B:17:0x0071, B:19:0x007b, B:21:0x0081, B:22:0x0084, B:39:0x00bf, B:41:0x00c5, B:43:0x00cb, B:44:0x00ce, B:46:0x00d3, B:48:0x00d9, B:49:0x00dc, B:51:0x00e1, B:53:0x00eb, B:55:0x00f9, B:57:0x0105, B:59:0x0111, B:71:0x0166, B:73:0x016c, B:74:0x016f, B:76:0x0178, B:77:0x017b, B:79:0x0186, B:81:0x018e, B:83:0x0198, B:89:0x01dd, B:91:0x01e7, B:95:0x0227, B:97:0x022d, B:99:0x024b, B:100:0x024d, B:102:0x0257, B:101:0x0252, B:63:0x0125, B:65:0x0139, B:67:0x0143, B:69:0x015d, B:23:0x0088, B:25:0x0092, B:27:0x0098, B:28:0x009b, B:30:0x00a0, B:32:0x00aa, B:34:0x00b0, B:36:0x00b6, B:37:0x00b9), top: B:112:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x017f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$searchPlacesWithQuery$5(Locale locale, String str, final Location location, final String str2) {
        List<Address> list;
        int i;
        boolean z;
        String countryName;
        boolean z2;
        StringBuilder sb;
        int i2;
        String str3;
        String str4;
        String[] strArr;
        final ArrayList arrayList = new ArrayList();
        try {
            List<Address> fromLocationName = new Geocoder(ApplicationLoader.applicationContext, locale).getFromLocationName(str, 5);
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            int i3 = 0;
            while (i3 < fromLocationName.size()) {
                Address address = fromLocationName.get(i3);
                if (address.hasLatitude() && address.hasLongitude()) {
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    String locality = address.getLocality();
                    if (TextUtils.isEmpty(locality)) {
                        locality = address.getAdminArea();
                    }
                    String str5 = locality;
                    String thoroughfare = address.getThoroughfare();
                    boolean z3 = true;
                    list = fromLocationName;
                    if (!TextUtils.isEmpty(thoroughfare) && !TextUtils.equals(thoroughfare, address.getAdminArea())) {
                        if (sb4.length() > 0) {
                            sb4.append(", ");
                        }
                        sb4.append(thoroughfare);
                    } else {
                        String subLocality = address.getSubLocality();
                        if (!TextUtils.isEmpty(subLocality)) {
                            if (sb4.length() > 0) {
                                sb4.append(", ");
                            }
                            sb4.append(subLocality);
                        } else {
                            String locality2 = address.getLocality();
                            if (!TextUtils.isEmpty(locality2) && !TextUtils.equals(locality2, str5)) {
                                if (sb4.length() > 0) {
                                    sb4.append(", ");
                                }
                                sb4.append(locality2);
                            } else {
                                sb4 = null;
                                z = true;
                                if (!TextUtils.isEmpty(str5)) {
                                    if (sb3.length() > 0) {
                                        sb3.append(", ");
                                    }
                                    sb3.append(str5);
                                    if (sb4 != null) {
                                        if (sb4.length() > 0) {
                                            sb4.append(", ");
                                        }
                                        sb4.append(str5);
                                    }
                                    z3 = false;
                                }
                                countryName = address.getCountryName();
                                if (TextUtils.isEmpty(countryName)) {
                                    i = i3;
                                    if (!"US".equals(address.getCountryCode()) && !"AE".equals(address.getCountryCode()) && (!"GB".equals(address.getCountryCode()) || !"en".equals(locale.getLanguage()))) {
                                        z2 = z;
                                        sb = sb4;
                                        str3 = countryName;
                                        if (sb3.length() > 0) {
                                            sb3.append(", ");
                                        }
                                        sb3.append(str3);
                                        if (sb2.length() > 0) {
                                            sb2.append(", ");
                                        }
                                        sb2.append(countryName);
                                    }
                                    String[] split = countryName.split(" ");
                                    int length = split.length;
                                    z2 = z;
                                    str3 = "";
                                    sb = sb4;
                                    int i4 = 0;
                                    while (i4 < length) {
                                        int i5 = length;
                                        if (split[i4].length() > 0) {
                                            strArr = split;
                                            str3 = str3 + str4.charAt(0);
                                        } else {
                                            strArr = split;
                                        }
                                        i4++;
                                        length = i5;
                                        split = strArr;
                                    }
                                    if (sb3.length() > 0) {
                                    }
                                    sb3.append(str3);
                                    if (sb2.length() > 0) {
                                    }
                                    sb2.append(countryName);
                                } else {
                                    z2 = z;
                                    i = i3;
                                    sb = sb4;
                                }
                                if (sb2.length() > 0 && !hashSet.contains(sb2.toString())) {
                                    TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue = new TLRPC$TL_messageMediaVenue();
                                    TLRPC$TL_geoPoint tLRPC$TL_geoPoint = new TLRPC$TL_geoPoint();
                                    tLRPC$TL_messageMediaVenue.geo = tLRPC$TL_geoPoint;
                                    tLRPC$TL_geoPoint.lat = latitude;
                                    tLRPC$TL_geoPoint._long = longitude;
                                    tLRPC$TL_messageMediaVenue.query_id = -1L;
                                    tLRPC$TL_messageMediaVenue.title = sb2.toString();
                                    tLRPC$TL_messageMediaVenue.icon = "https://ss3.4sqi.net/img/categories_v2/building/government_capitolbuilding_64.png";
                                    tLRPC$TL_messageMediaVenue.emoji = LocationController.countryCodeToEmoji(address.getCountryCode());
                                    hashSet.add(tLRPC$TL_messageMediaVenue.title);
                                    tLRPC$TL_messageMediaVenue.address = LocaleController.getString("Country", R.string.Country);
                                    arrayList.add(tLRPC$TL_messageMediaVenue);
                                    if (arrayList.size() >= 5) {
                                        break;
                                    }
                                }
                                String str6 = "PassportCity";
                                if (!z3 && !hashSet2.contains(sb3.toString())) {
                                    TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue2 = new TLRPC$TL_messageMediaVenue();
                                    TLRPC$TL_geoPoint tLRPC$TL_geoPoint2 = new TLRPC$TL_geoPoint();
                                    tLRPC$TL_messageMediaVenue2.geo = tLRPC$TL_geoPoint2;
                                    tLRPC$TL_geoPoint2.lat = latitude;
                                    tLRPC$TL_geoPoint2._long = longitude;
                                    tLRPC$TL_messageMediaVenue2.query_id = -1L;
                                    tLRPC$TL_messageMediaVenue2.title = sb3.toString();
                                    tLRPC$TL_messageMediaVenue2.icon = "https://ss3.4sqi.net/img/categories_v2/travel/hotel_64.png";
                                    tLRPC$TL_messageMediaVenue2.emoji = LocationController.countryCodeToEmoji(address.getCountryCode());
                                    hashSet2.add(tLRPC$TL_messageMediaVenue2.title);
                                    tLRPC$TL_messageMediaVenue2.address = LocaleController.getString("PassportCity", R.string.PassportCity);
                                    arrayList.add(tLRPC$TL_messageMediaVenue2);
                                    if (arrayList.size() >= 5) {
                                        break;
                                    }
                                }
                                if (sb == null && sb.length() > 0) {
                                    TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue3 = new TLRPC$TL_messageMediaVenue();
                                    TLRPC$TL_geoPoint tLRPC$TL_geoPoint3 = new TLRPC$TL_geoPoint();
                                    tLRPC$TL_messageMediaVenue3.geo = tLRPC$TL_geoPoint3;
                                    tLRPC$TL_geoPoint3.lat = latitude;
                                    tLRPC$TL_geoPoint3._long = longitude;
                                    tLRPC$TL_messageMediaVenue3.query_id = -1L;
                                    tLRPC$TL_messageMediaVenue3.title = sb.toString();
                                    tLRPC$TL_messageMediaVenue3.icon = "pin";
                                    if (z2) {
                                        i2 = R.string.PassportCity;
                                    } else {
                                        str6 = "PassportStreet1";
                                        i2 = R.string.PassportStreet1;
                                    }
                                    tLRPC$TL_messageMediaVenue3.address = LocaleController.getString(str6, i2);
                                    arrayList.add(tLRPC$TL_messageMediaVenue3);
                                    if (arrayList.size() >= 5) {
                                        break;
                                    }
                                }
                                i3 = i + 1;
                                fromLocationName = list;
                            }
                        }
                    }
                    z = false;
                    if (!TextUtils.isEmpty(str5)) {
                    }
                    countryName = address.getCountryName();
                    if (TextUtils.isEmpty(countryName)) {
                    }
                    if (sb2.length() > 0) {
                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue4 = new TLRPC$TL_messageMediaVenue();
                        TLRPC$TL_geoPoint tLRPC$TL_geoPoint4 = new TLRPC$TL_geoPoint();
                        tLRPC$TL_messageMediaVenue4.geo = tLRPC$TL_geoPoint4;
                        tLRPC$TL_geoPoint4.lat = latitude;
                        tLRPC$TL_geoPoint4._long = longitude;
                        tLRPC$TL_messageMediaVenue4.query_id = -1L;
                        tLRPC$TL_messageMediaVenue4.title = sb2.toString();
                        tLRPC$TL_messageMediaVenue4.icon = "https://ss3.4sqi.net/img/categories_v2/building/government_capitolbuilding_64.png";
                        tLRPC$TL_messageMediaVenue4.emoji = LocationController.countryCodeToEmoji(address.getCountryCode());
                        hashSet.add(tLRPC$TL_messageMediaVenue4.title);
                        tLRPC$TL_messageMediaVenue4.address = LocaleController.getString("Country", R.string.Country);
                        arrayList.add(tLRPC$TL_messageMediaVenue4);
                        if (arrayList.size() >= 5) {
                        }
                    }
                    String str62 = "PassportCity";
                    if (!z3) {
                        TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue22 = new TLRPC$TL_messageMediaVenue();
                        TLRPC$TL_geoPoint tLRPC$TL_geoPoint22 = new TLRPC$TL_geoPoint();
                        tLRPC$TL_messageMediaVenue22.geo = tLRPC$TL_geoPoint22;
                        tLRPC$TL_geoPoint22.lat = latitude;
                        tLRPC$TL_geoPoint22._long = longitude;
                        tLRPC$TL_messageMediaVenue22.query_id = -1L;
                        tLRPC$TL_messageMediaVenue22.title = sb3.toString();
                        tLRPC$TL_messageMediaVenue22.icon = "https://ss3.4sqi.net/img/categories_v2/travel/hotel_64.png";
                        tLRPC$TL_messageMediaVenue22.emoji = LocationController.countryCodeToEmoji(address.getCountryCode());
                        hashSet2.add(tLRPC$TL_messageMediaVenue22.title);
                        tLRPC$TL_messageMediaVenue22.address = LocaleController.getString("PassportCity", R.string.PassportCity);
                        arrayList.add(tLRPC$TL_messageMediaVenue22);
                        if (arrayList.size() >= 5) {
                        }
                    }
                    if (sb == null) {
                    }
                    i3 = i + 1;
                    fromLocationName = list;
                }
                list = fromLocationName;
                i = i3;
                i3 = i + 1;
                fromLocationName = list;
            }
        } catch (Exception unused) {
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
