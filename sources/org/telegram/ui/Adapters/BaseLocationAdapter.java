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
    protected ArrayList locations = new ArrayList();
    protected ArrayList places = new ArrayList();
    private int currentAccount = UserConfig.selectedAccount;

    /* loaded from: classes4.dex */
    public interface BaseLocationAdapterDelegate {
        void didLoadSearchResult(ArrayList arrayList);
    }

    public BaseLocationAdapter(boolean z, boolean z2) {
        this.stories = z;
        this.biz = z2;
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
    public /* synthetic */ void lambda$searchDelayed$0(String str, Location location) {
        this.searchRunnable = null;
        this.lastSearchLocation = null;
        searchPlacesWithQuery(str, location, true);
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
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00e5, code lost:
        if (r5.length() > 0) goto L233;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01dc A[Catch: Exception -> 0x049c, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0362 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0374 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0385 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0432 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:229:0x048c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x010f A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0138 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b8 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01c4 A[Catch: Exception -> 0x049c, TryCatch #1 {Exception -> 0x049c, blocks: (B:3:0x0009, B:7:0x0012, B:9:0x0023, B:11:0x0032, B:12:0x003d, B:14:0x0043, B:16:0x004b, B:18:0x0051, B:20:0x0059, B:22:0x005f, B:26:0x0070, B:28:0x0095, B:31:0x009d, B:33:0x00a7, B:34:0x00aa, B:38:0x00be, B:40:0x00c8, B:42:0x00ce, B:43:0x00d1, B:60:0x0109, B:62:0x010f, B:64:0x0115, B:65:0x0118, B:67:0x011d, B:69:0x0123, B:70:0x0126, B:73:0x012e, B:75:0x0138, B:77:0x0148, B:79:0x0154, B:81:0x0160, B:92:0x01b2, B:94:0x01b8, B:95:0x01bb, B:97:0x01c4, B:98:0x01c7, B:100:0x01d3, B:103:0x01dc, B:108:0x01ef, B:110:0x01f5, B:114:0x0225, B:116:0x022b, B:118:0x0247, B:120:0x024c, B:122:0x0254, B:124:0x0268, B:126:0x026e, B:128:0x0274, B:129:0x0278, B:131:0x027e, B:132:0x0282, B:134:0x0291, B:135:0x029b, B:137:0x02a1, B:139:0x02ad, B:141:0x02b4, B:143:0x02c0, B:145:0x02ce, B:148:0x02d6, B:150:0x02dc, B:152:0x02e6, B:154:0x02f4, B:155:0x02f8, B:157:0x02fe, B:159:0x0308, B:161:0x0316, B:162:0x031a, B:164:0x0320, B:166:0x0326, B:168:0x0330, B:170:0x0336, B:171:0x0339, B:173:0x033e, B:176:0x0345, B:178:0x034a, B:183:0x035c, B:185:0x0362, B:188:0x0374, B:193:0x0385, B:195:0x0391, B:197:0x03c8, B:199:0x03dc, B:201:0x03e2, B:203:0x03e8, B:204:0x03ec, B:206:0x03f2, B:207:0x03f6, B:209:0x0400, B:210:0x040a, B:212:0x0410, B:214:0x041c, B:219:0x042c, B:221:0x0432, B:223:0x043e, B:225:0x0475, B:226:0x0482, B:181:0x0358, B:119:0x024a, B:85:0x0173, B:87:0x0187, B:89:0x0191, B:91:0x01ab, B:45:0x00d7, B:47:0x00e1, B:49:0x00e7, B:50:0x00ea, B:51:0x00ee, B:53:0x00f8, B:55:0x00fe), top: B:237:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01cb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$searchPlacesWithQuery$5(Locale locale, String str, Locale locale2, final Location location, final String str2) {
        List<Address> list;
        List<Address> list2;
        HashSet hashSet;
        int i;
        HashSet hashSet2;
        HashSet hashSet3;
        boolean z;
        boolean z2;
        String countryName;
        int i2;
        boolean z3;
        String str3;
        Address address;
        boolean z4;
        String str4;
        String str5;
        String str6;
        String str7;
        String[] strArr;
        BaseLocationAdapter baseLocationAdapter = this;
        final ArrayList arrayList = new ArrayList();
        try {
            int i3 = baseLocationAdapter.biz ? 10 : 5;
            List<Address> fromLocationName = baseLocationAdapter.stories ? new Geocoder(ApplicationLoader.applicationContext, locale2).getFromLocationName(str, 5) : null;
            HashSet hashSet4 = new HashSet();
            HashSet hashSet5 = new HashSet();
            int i4 = 0;
            for (List<Address> fromLocationName2 = new Geocoder(ApplicationLoader.applicationContext, locale).getFromLocationName(str, 5); i4 < fromLocationName2.size(); fromLocationName2 = list2) {
                Address address2 = fromLocationName2.get(i4);
                Address address3 = (fromLocationName == null || i4 >= fromLocationName.size()) ? null : fromLocationName.get(i4);
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
                    String str8 = locality;
                    if (address3 != null && TextUtils.isEmpty(address3.getLocality())) {
                        address3.getAdminArea();
                    }
                    i = i4;
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
                            if (TextUtils.isEmpty(str8)) {
                                z2 = true;
                            } else {
                                if (sb2.length() > 0) {
                                    sb2.append(", ");
                                }
                                sb2.append(str8);
                                if (sb3 != null) {
                                    if (sb3.length() > 0) {
                                        sb3.append(", ");
                                    }
                                    sb3.append(str8);
                                }
                                z2 = false;
                            }
                            countryName = address2.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                                i2 = i3;
                                z3 = z;
                                str3 = str8;
                                address = address3;
                            } else {
                                i2 = i3;
                                str3 = str8;
                                if (!"US".equals(address2.getCountryCode()) && !"AE".equals(address2.getCountryCode()) && (!"GB".equals(address2.getCountryCode()) || !"en".equals(locale.getLanguage()))) {
                                    z3 = z;
                                    address = address3;
                                    str6 = countryName;
                                    if (sb2.length() > 0) {
                                        sb2.append(", ");
                                    }
                                    sb2.append(str6);
                                    if (sb.length() > 0) {
                                        sb.append(", ");
                                    }
                                    sb.append(countryName);
                                }
                                String[] split = countryName.split(" ");
                                int length = split.length;
                                address = address3;
                                str6 = "";
                                z3 = z;
                                int i5 = 0;
                                while (i5 < length) {
                                    int i6 = length;
                                    if (split[i5].length() > 0) {
                                        strArr = split;
                                        str6 = str6 + str7.charAt(0);
                                    } else {
                                        strArr = split;
                                    }
                                    i5++;
                                    length = i6;
                                    split = strArr;
                                }
                                if (sb2.length() > 0) {
                                }
                                sb2.append(str6);
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
                                i3 = i2;
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
                                    tLRPC$TL_messageMediaVenue2.address = LocaleController.getString(z3 ? R.string.PassportCity : R.string.PassportStreet1);
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
                                                    if (TextUtils.isEmpty(str5) && !TextUtils.equals(address.getSubLocality(), str4) && !TextUtils.equals(address.getSubLocality(), address.getCountryName())) {
                                                        str5 = address.getSubLocality();
                                                    }
                                                    if (TextUtils.isEmpty(str5) && !TextUtils.equals(address.getLocality(), str4) && !TextUtils.equals(address.getLocality(), address.getCountryName())) {
                                                        str5 = address.getLocality();
                                                    }
                                                    if (!TextUtils.isEmpty(str5) || TextUtils.equals(str5, adminArea) || TextUtils.equals(str5, address.getCountryName())) {
                                                        sb5 = null;
                                                    } else {
                                                        if (sb5.length() > 0) {
                                                            sb5.append(", ");
                                                        }
                                                        sb5.append(str5);
                                                    }
                                                    if (!TextUtils.isEmpty(sb5)) {
                                                        int i7 = 0;
                                                        while (true) {
                                                            String[] strArr2 = LocationController.unnamedRoads;
                                                            if (i7 >= strArr2.length) {
                                                                break;
                                                            } else if (strArr2[i7].equalsIgnoreCase(sb5.toString())) {
                                                                z4 = true;
                                                                break;
                                                            } else {
                                                                i7++;
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
                                                        i3 = i2;
                                                        if (arrayList.size() >= i3) {
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
                                                                tLRPC$TL_messageMediaVenue3.address = LocaleController.getString(R.string.PassportCity);
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
                                                                if (arrayList.size() >= i3) {
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
                                                                        tLRPC$TL_messageMediaVenue4.address = LocaleController.getString(R.string.Country);
                                                                        if (address != null) {
                                                                            TL_stories$TL_geoPointAddress tL_stories$TL_geoPointAddress8 = new TL_stories$TL_geoPointAddress();
                                                                            tLRPC$TL_messageMediaVenue4.geoAddress = tL_stories$TL_geoPointAddress8;
                                                                            tL_stories$TL_geoPointAddress8.country_iso2 = address.getCountryCode();
                                                                        }
                                                                        arrayList.add(tLRPC$TL_messageMediaVenue4);
                                                                        if (arrayList.size() >= i3) {
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        continue;
                                                                    }
                                                                } else {
                                                                    hashSet2 = hashSet6;
                                                                }
                                                                i4 = i + 1;
                                                                baseLocationAdapter = this;
                                                                hashSet5 = hashSet;
                                                                hashSet4 = hashSet2;
                                                                fromLocationName = list;
                                                            }
                                                        } else {
                                                            hashSet = hashSet3;
                                                        }
                                                        if (sb.length() > 0) {
                                                        }
                                                        i4 = i + 1;
                                                        baseLocationAdapter = this;
                                                        hashSet5 = hashSet;
                                                        hashSet4 = hashSet2;
                                                        fromLocationName = list;
                                                    }
                                                }
                                            } else {
                                                str4 = str3;
                                            }
                                            str5 = null;
                                            if (TextUtils.isEmpty(str5)) {
                                                str5 = address.getSubLocality();
                                            }
                                            if (TextUtils.isEmpty(str5)) {
                                                str5 = address.getLocality();
                                            }
                                            if (TextUtils.isEmpty(str5)) {
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
                                i3 = i2;
                                if (z2) {
                                }
                                if (sb.length() > 0) {
                                }
                                i4 = i + 1;
                                baseLocationAdapter = this;
                                hashSet5 = hashSet;
                                hashSet4 = hashSet2;
                                fromLocationName = list;
                            }
                        }
                    }
                    String subLocality = address2.getSubLocality();
                    if (TextUtils.isEmpty(subLocality)) {
                        subLocality = address2.getLocality();
                        if (TextUtils.isEmpty(subLocality) || TextUtils.equals(subLocality, str8)) {
                            sb3 = null;
                            z = true;
                            if (TextUtils.isEmpty(str8)) {
                            }
                            countryName = address2.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                            }
                            if (baseLocationAdapter.biz) {
                            }
                        } else {
                            if (sb3.length() > 0) {
                                sb3.append(", ");
                            }
                            sb3.append(subLocality);
                        }
                    }
                    z = false;
                    if (TextUtils.isEmpty(str8)) {
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
                    i = i4;
                    hashSet2 = hashSet4;
                }
                i4 = i + 1;
                baseLocationAdapter = this;
                hashSet5 = hashSet;
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
                TLRPC$BotInlineResult tLRPC$BotInlineResult = (TLRPC$BotInlineResult) tLRPC$messages_BotResults.results.get(i);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchPlacesWithQuery$7(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                BaseLocationAdapter.this.lambda$searchPlacesWithQuery$6(tLRPC$TL_error, str, tLObject);
            }
        });
    }

    private void searchBotUser() {
        if (this.searchingUser) {
            return;
        }
        this.searchingUser = true;
        TLRPC$TL_contacts_resolveUsername tLRPC$TL_contacts_resolveUsername = new TLRPC$TL_contacts_resolveUsername();
        tLRPC$TL_contacts_resolveUsername.username = this.stories ? MessagesController.getInstance(this.currentAccount).storyVenueSearchBot : MessagesController.getInstance(this.currentAccount).venueSearchBot;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_contacts_resolveUsername, new RequestDelegate() { // from class: org.telegram.ui.Adapters.BaseLocationAdapter$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BaseLocationAdapter.this.lambda$searchBotUser$3(tLObject, tLRPC$TL_error);
            }
        });
    }

    public void destroy() {
        if (this.currentRequestNum != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.currentRequestNum, true);
            this.currentRequestNum = 0;
        }
    }

    public String getLastSearchString() {
        return this.lastFoundQuery;
    }

    public boolean isSearching() {
        return this.searchInProgress;
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

    public void searchPlacesWithQuery(String str, Location location, boolean z) {
        searchPlacesWithQuery(str, location, z, false);
    }

    public void searchPlacesWithQuery(final String str, final Location location, boolean z, boolean z2) {
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
                TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(this.stories ? MessagesController.getInstance(this.currentAccount).storyVenueSearchBot : MessagesController.getInstance(this.currentAccount).venueSearchBot);
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
                tLRPC$TL_messages_getInlineBotResults.peer = DialogObject.isEncryptedDialog(this.dialogId) ? new TLRPC$TL_inputPeerEmpty() : MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
                if (TextUtils.isEmpty(str) || !(this.stories || this.biz)) {
                    this.searchingLocations = false;
                } else {
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

    public void setDelegate(long j, BaseLocationAdapterDelegate baseLocationAdapterDelegate) {
        this.dialogId = j;
        this.delegate = baseLocationAdapterDelegate;
    }

    protected void update(boolean z) {
        notifyDataSetChanged();
    }
}
