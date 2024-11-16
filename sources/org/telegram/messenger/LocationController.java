package org.telegram.messenger;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseIntArray;
import androidx.collection.LongSparseArray;
import androidx.core.util.Consumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.ILocationServiceProvider;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.Components.PermissionRequest;

/* loaded from: classes3.dex */
public class LocationController extends BaseController implements NotificationCenter.NotificationCenterDelegate, ILocationServiceProvider.IAPIConnectionCallbacks, ILocationServiceProvider.IAPIOnConnectionFailedListener {
    private static final int BACKGROUD_UPDATE_TIME = 30000;
    private static final long FASTEST_INTERVAL = 1000;
    private static final int FOREGROUND_UPDATE_TIME = 20000;
    private static final int LOCATION_ACQUIRE_TIME = 10000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int SEND_NEW_LOCATION_TIME = 2000;
    public static final int TYPE_BIZ = 1;
    public static final int TYPE_STORY = 2;
    private static final long UPDATE_INTERVAL = 1000;
    private static final int WATCH_LOCATION_TIMEOUT = 65000;
    private ILocationServiceProvider.IMapApiClient apiClient;
    private LongSparseArray cacheRequests;
    private FusedLocationListener fusedLocationListener;
    private GpsLocationListener gpsLocationListener;
    private Location lastKnownLocation;
    private boolean lastLocationByMaps;
    private long lastLocationSendTime;
    private long lastLocationStartTime;
    private LongSparseArray lastReadLocationTime;
    private long locationEndWatchTime;
    private LocationManager locationManager;
    private ILocationServiceProvider.ILocationRequest locationRequest;
    private boolean locationSentSinceLastMapUpdate;
    public LongSparseArray locationsCache;
    private GpsLocationListener networkLocationListener;
    private GpsLocationListener passiveLocationListener;
    private SparseIntArray requests;
    private Boolean servicesAvailable;
    private ArrayList<SharingLocationInfo> sharingLocations;
    private LongSparseArray sharingLocationsMap;
    private LongSparseArray sharingLocationsMapUI;
    public ArrayList<SharingLocationInfo> sharingLocationsUI;
    private boolean started;
    private boolean wasConnectedToPlayServices;
    private static volatile LocationController[] Instance = new LocationController[4];
    public static String[] unnamedRoads = {"Unnamed Road", "Вulicya bez nazvi", "Нeizvestnaya doroga", "İsimsiz Yol", "Ceļš bez nosaukuma", "Kelias be pavadinimo", "Droga bez nazwy", "Cesta bez názvu", "Silnice bez názvu", "Drum fără nume", "Route sans nom", "Vía sin nombre", "Estrada sem nome", "Οdos xoris onomasia", "Rrugë pa emër", "Пat bez ime", "Нeimenovani put", "Strada senza nome", "Straße ohne Straßennamen"};
    private static HashMap<LocationFetchCallback, Runnable> callbacks = new HashMap<>();

    private class FusedLocationListener implements ILocationServiceProvider.ILocationListener {
        private FusedLocationListener() {
        }

        @Override // org.telegram.messenger.ILocationServiceProvider.ILocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            LocationController.this.setLastKnownLocation(location);
        }
    }

    private class GpsLocationListener implements LocationListener {
        private GpsLocationListener() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            if (LocationController.this.lastKnownLocation == null || !(this == LocationController.this.networkLocationListener || this == LocationController.this.passiveLocationListener)) {
                LocationController.this.setLastKnownLocation(location);
            } else {
                if (LocationController.this.started || location.distanceTo(LocationController.this.lastKnownLocation) <= 20.0f) {
                    return;
                }
                LocationController.this.setLastKnownLocation(location);
                LocationController.this.lastLocationSendTime = SystemClock.elapsedRealtime() - 25000;
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    public interface LocationFetchCallback {
        void onLocationAddressAvailable(String str, String str2, TLRPC.TL_messageMediaVenue tL_messageMediaVenue, TLRPC.TL_messageMediaVenue tL_messageMediaVenue2, Location location);
    }

    public static class SharingLocationInfo {
        public int account;
        public long did;
        public int lastSentProximityMeters;
        public MessageObject messageObject;
        public int mid;
        public int period;
        public int proximityMeters;
        public int stopTime;
    }

    public LocationController(int i) {
        super(i);
        this.sharingLocationsMap = new LongSparseArray();
        this.sharingLocations = new ArrayList<>();
        this.locationsCache = new LongSparseArray();
        this.lastReadLocationTime = new LongSparseArray();
        this.gpsLocationListener = new GpsLocationListener();
        this.networkLocationListener = new GpsLocationListener();
        this.passiveLocationListener = new GpsLocationListener();
        this.fusedLocationListener = new FusedLocationListener();
        this.locationSentSinceLastMapUpdate = true;
        this.requests = new SparseIntArray();
        this.cacheRequests = new LongSparseArray();
        this.sharingLocationsUI = new ArrayList<>();
        this.sharingLocationsMapUI = new LongSparseArray();
        this.locationManager = (LocationManager) ApplicationLoader.applicationContext.getSystemService("location");
        this.apiClient = ApplicationLoader.getLocationServiceProvider().onCreateLocationServicesAPI(ApplicationLoader.applicationContext, this, this);
        ILocationServiceProvider.ILocationRequest onCreateLocationRequest = ApplicationLoader.getLocationServiceProvider().onCreateLocationRequest();
        this.locationRequest = onCreateLocationRequest;
        onCreateLocationRequest.setPriority(0);
        this.locationRequest.setInterval(1000L);
        this.locationRequest.setFastestInterval(1000L);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$new$0();
            }
        });
        loadSharingLocations();
    }

    private void broadcastLastKnownLocation(boolean z) {
        int i;
        TLRPC.GeoPoint geoPoint;
        if (this.lastKnownLocation == null) {
            return;
        }
        if (this.requests.size() != 0) {
            if (z) {
                for (int i2 = 0; i2 < this.requests.size(); i2++) {
                    getConnectionsManager().cancelRequest(this.requests.keyAt(i2), false);
                }
            }
            this.requests.clear();
        }
        if (!this.sharingLocations.isEmpty()) {
            int currentTime = getConnectionsManager().getCurrentTime();
            float[] fArr = new float[1];
            while (i < this.sharingLocations.size()) {
                final SharingLocationInfo sharingLocationInfo = this.sharingLocations.get(i);
                TLRPC.Message message = sharingLocationInfo.messageObject.messageOwner;
                TLRPC.MessageMedia messageMedia = message.media;
                if (messageMedia != null && (geoPoint = messageMedia.geo) != null && sharingLocationInfo.lastSentProximityMeters == sharingLocationInfo.proximityMeters) {
                    int i3 = message.edit_date;
                    if (i3 == 0) {
                        i3 = message.date;
                    }
                    if (Math.abs(currentTime - i3) < 10) {
                        Location.distanceBetween(geoPoint.lat, geoPoint._long, this.lastKnownLocation.getLatitude(), this.lastKnownLocation.getLongitude(), fArr);
                        i = fArr[0] < 1.0f ? i + 1 : 0;
                    }
                }
                final TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
                tL_messages_editMessage.peer = getMessagesController().getInputPeer(sharingLocationInfo.did);
                tL_messages_editMessage.id = sharingLocationInfo.mid;
                tL_messages_editMessage.flags |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
                TLRPC.TL_inputMediaGeoLive tL_inputMediaGeoLive = new TLRPC.TL_inputMediaGeoLive();
                tL_messages_editMessage.media = tL_inputMediaGeoLive;
                tL_inputMediaGeoLive.stopped = false;
                tL_inputMediaGeoLive.geo_point = new TLRPC.TL_inputGeoPoint();
                tL_messages_editMessage.media.geo_point.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
                tL_messages_editMessage.media.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
                tL_messages_editMessage.media.geo_point.accuracy_radius = (int) this.lastKnownLocation.getAccuracy();
                TLRPC.InputMedia inputMedia = tL_messages_editMessage.media;
                TLRPC.InputGeoPoint inputGeoPoint = inputMedia.geo_point;
                if (inputGeoPoint.accuracy_radius != 0) {
                    inputGeoPoint.flags |= 1;
                }
                int i4 = sharingLocationInfo.lastSentProximityMeters;
                int i5 = sharingLocationInfo.proximityMeters;
                if (i4 != i5) {
                    inputMedia.proximity_notification_radius = i5;
                    inputMedia.flags |= 8;
                }
                inputMedia.heading = getHeading(this.lastKnownLocation);
                tL_messages_editMessage.media.flags |= 4;
                int sendRequest = getConnectionsManager().sendRequest(tL_messages_editMessage, new RequestDelegate() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda22
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        LocationController.this.lambda$broadcastLastKnownLocation$7(sharingLocationInfo, r3, tL_messages_editMessage, tLObject, tL_error);
                    }
                });
                final int[] iArr = {sendRequest};
                this.requests.put(sendRequest, 0);
            }
        }
        getConnectionsManager().resumeNetworkMaybe();
        if (shouldStopGps()) {
            stop(false);
        }
    }

    private boolean checkServices() {
        if (this.servicesAvailable == null) {
            this.servicesAvailable = Boolean.valueOf(ApplicationLoader.getLocationServiceProvider().checkServices());
        }
        return this.servicesAvailable.booleanValue();
    }

    public static String countryCodeToEmoji(String str) {
        if (str == null) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int codePointCount = upperCase.codePointCount(0, upperCase.length());
        if (codePointCount > 2) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codePointCount; i++) {
            sb.append(Character.toChars(Character.codePointAt(upperCase, i) - (-127397)));
        }
        return sb.toString();
    }

    public static String detectOcean(double d, double d2) {
        if (d2 > 65.0d) {
            return "Arctic Ocean";
        }
        if (d > -88.0d && d < 40.0d && d2 > 0.0d) {
            return "Atlantic Ocean";
        }
        if (d > -60.0d && d < 20.0d && d2 <= 0.0d) {
            return "Atlantic Ocean";
        }
        if (d2 <= 30.0d && d >= 20.0d && d < 150.0d) {
            return "Indian Ocean";
        }
        if ((d > 106.0d || d < -60.0d) && d2 > 0.0d) {
            return "Pacific Ocean";
        }
        if ((d > 150.0d || d < -60.0d) && d2 <= 0.0d) {
            return "Pacific Ocean";
        }
        return null;
    }

    public static void fetchLocationAddress(final Location location, final int i, final LocationFetchCallback locationFetchCallback) {
        Locale systemDefaultLocale;
        if (locationFetchCallback == null) {
            return;
        }
        Runnable runnable = callbacks.get(locationFetchCallback);
        if (runnable != null) {
            Utilities.globalQueue.cancelRunnable(runnable);
            callbacks.remove(locationFetchCallback);
        }
        if (location == null) {
            locationFetchCallback.onLocationAddressAvailable(null, null, null, null, null);
            return;
        }
        try {
            systemDefaultLocale = LocaleController.getInstance().getCurrentLocale();
        } catch (Exception unused) {
            systemDefaultLocale = LocaleController.getInstance().getSystemDefaultLocale();
        }
        final Locale locale = systemDefaultLocale;
        final Locale locale2 = locale.getLanguage().contains("en") ? locale : Locale.US;
        DispatchQueue dispatchQueue = Utilities.globalQueue;
        Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.lambda$fetchLocationAddress$29(locale, location, i, locale2, locationFetchCallback);
            }
        };
        dispatchQueue.postRunnable(runnable2, 300L);
        callbacks.put(locationFetchCallback, runnable2);
    }

    public static void fetchLocationAddress(Location location, LocationFetchCallback locationFetchCallback) {
        fetchLocationAddress(location, 0, locationFetchCallback);
    }

    public static int getHeading(Location location) {
        float bearing = location.getBearing();
        return (bearing <= 0.0f || bearing >= 1.0f) ? (int) bearing : bearing < 0.5f ? 360 : 1;
    }

    public static LocationController getInstance(int i) {
        LocationController locationController = Instance[i];
        if (locationController == null) {
            synchronized (LocationController.class) {
                try {
                    locationController = Instance[i];
                    if (locationController == null) {
                        LocationController[] locationControllerArr = Instance;
                        LocationController locationController2 = new LocationController(i);
                        locationControllerArr[i] = locationController2;
                        locationController = locationController2;
                    }
                } finally {
                }
            }
        }
        return locationController;
    }

    public static int getLocationsCount() {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i += getInstance(i2).sharingLocationsUI.size();
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addSharingLocation$11(SharingLocationInfo sharingLocationInfo, SharingLocationInfo sharingLocationInfo2) {
        if (sharingLocationInfo != null) {
            this.sharingLocationsUI.remove(sharingLocationInfo);
        }
        this.sharingLocationsUI.add(sharingLocationInfo2);
        this.sharingLocationsMapUI.put(sharingLocationInfo2.did, sharingLocationInfo2);
        startService();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$broadcastLastKnownLocation$6(SharingLocationInfo sharingLocationInfo) {
        this.sharingLocationsUI.remove(sharingLocationInfo);
        this.sharingLocationsMapUI.remove(sharingLocationInfo.did);
        if (this.sharingLocationsUI.isEmpty()) {
            stopService();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$broadcastLastKnownLocation$7(final SharingLocationInfo sharingLocationInfo, int[] iArr, TLRPC.TL_messages_editMessage tL_messages_editMessage, TLObject tLObject, TLRPC.TL_error tL_error) {
        MessageObject messageObject;
        TLRPC.Message message;
        if (tL_error != null) {
            if (tL_error.text.equals("MESSAGE_ID_INVALID")) {
                this.sharingLocations.remove(sharingLocationInfo);
                this.sharingLocationsMap.remove(sharingLocationInfo.did);
                saveSharingLocation(sharingLocationInfo, 1);
                this.requests.delete(iArr[0]);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationController.this.lambda$broadcastLastKnownLocation$6(sharingLocationInfo);
                    }
                });
                return;
            }
            return;
        }
        if ((tL_messages_editMessage.flags & 8) != 0) {
            sharingLocationInfo.lastSentProximityMeters = tL_messages_editMessage.media.proximity_notification_radius;
        }
        TLRPC.Updates updates = (TLRPC.Updates) tLObject;
        boolean z = false;
        for (int i = 0; i < updates.updates.size(); i++) {
            TLRPC.Update update = updates.updates.get(i);
            if (update instanceof TLRPC.TL_updateEditMessage) {
                messageObject = sharingLocationInfo.messageObject;
                message = ((TLRPC.TL_updateEditMessage) update).message;
            } else if (update instanceof TLRPC.TL_updateEditChannelMessage) {
                messageObject = sharingLocationInfo.messageObject;
                message = ((TLRPC.TL_updateEditChannelMessage) update).message;
            }
            messageObject.messageOwner = message;
            z = true;
        }
        if (z) {
            saveSharingLocation(sharingLocationInfo, 0);
        }
        getMessagesController().processUpdates(updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$9() {
        this.locationEndWatchTime = 0L;
        this.requests.clear();
        this.sharingLocationsMap.clear();
        this.sharingLocations.clear();
        setLastKnownLocation(null);
        stop(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$fetchLocationAddress$28(LocationFetchCallback locationFetchCallback, String str, String str2, TLRPC.TL_messageMediaVenue tL_messageMediaVenue, TLRPC.TL_messageMediaVenue tL_messageMediaVenue2, Location location) {
        callbacks.remove(locationFetchCallback);
        locationFetchCallback.onLocationAddressAvailable(str, str2, tL_messageMediaVenue, tL_messageMediaVenue2, location);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x03b5, code lost:
    
        if ("ru".equals(r3) == false) goto L229;
     */
    /* JADX WARN: Removed duplicated region for block: B:186:0x01f4 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0214 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0291 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x02d3 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x02fa A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x030e A[Catch: Exception -> 0x05e4, TRY_ENTER, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:266:0x031d A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:280:0x0353 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:286:0x036d A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:303:0x03e2 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:312:0x0407 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0420 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0436 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0444 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:341:0x047e  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x0480 A[Catch: Exception -> 0x05e4, TryCatch #9 {Exception -> 0x05e4, blocks: (B:3:0x001d, B:7:0x004f, B:9:0x0055, B:11:0x005d, B:13:0x0063, B:16:0x006f, B:19:0x007a, B:21:0x009d, B:23:0x00a1, B:25:0x00a7, B:27:0x00ad, B:29:0x00ba, B:30:0x00be, B:32:0x00c1, B:34:0x00c9, B:39:0x00cc, B:41:0x00d2, B:43:0x00de, B:45:0x00e8, B:48:0x00fb, B:49:0x00f6, B:52:0x00fd, B:57:0x04be, B:59:0x04c4, B:123:0x0105, B:142:0x0114, B:144:0x012e, B:145:0x0135, B:147:0x013b, B:148:0x013f, B:150:0x0145, B:153:0x014d, B:155:0x0153, B:156:0x015a, B:158:0x0160, B:159:0x0164, B:161:0x016a, B:162:0x016e, B:163:0x0178, B:165:0x017e, B:167:0x0188, B:169:0x0196, B:170:0x019c, B:172:0x01a2, B:174:0x01ac, B:176:0x01bc, B:177:0x01c5, B:179:0x01cb, B:181:0x01d5, B:183:0x01e5, B:184:0x01ee, B:186:0x01f4, B:188:0x01fa, B:190:0x0204, B:192:0x020a, B:193:0x020d, B:195:0x0214, B:197:0x021b, B:199:0x0225, B:201:0x0233, B:202:0x0239, B:204:0x023f, B:206:0x0249, B:208:0x0259, B:209:0x0262, B:211:0x0268, B:213:0x0272, B:215:0x0282, B:216:0x028b, B:218:0x0291, B:220:0x0297, B:222:0x02a1, B:224:0x02a7, B:225:0x02aa, B:226:0x02af, B:229:0x02b6, B:231:0x02bb, B:233:0x02ca, B:243:0x02cd, B:245:0x02d3, B:247:0x02d9, B:248:0x02dc, B:250:0x02e1, B:252:0x02e7, B:253:0x02ea, B:255:0x02f0, B:257:0x02fa, B:258:0x0300, B:261:0x030e, B:263:0x0314, B:264:0x0317, B:266:0x031d, B:268:0x0327, B:270:0x032d, B:271:0x0330, B:272:0x0333, B:274:0x033d, B:276:0x0343, B:277:0x0346, B:278:0x0349, B:280:0x0353, B:282:0x0359, B:283:0x035c, B:284:0x035f, B:286:0x036d, B:288:0x0373, B:289:0x0376, B:292:0x038f, B:295:0x03a0, B:297:0x03a6, B:299:0x03af, B:301:0x03d7, B:303:0x03e2, B:305:0x03ea, B:307:0x03fe, B:310:0x0401, B:312:0x0407, B:313:0x040a, B:314:0x0416, B:316:0x0420, B:318:0x0426, B:319:0x0429, B:320:0x042c, B:322:0x0436, B:324:0x043c, B:325:0x043f, B:327:0x0444, B:329:0x044e, B:331:0x0454, B:332:0x0457, B:333:0x045a, B:335:0x0464, B:337:0x046a, B:338:0x046d, B:339:0x0470, B:343:0x0480, B:344:0x03b7, B:346:0x03c3, B:349:0x03ca, B:370:0x0498, B:371:0x0038), top: B:2:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:353:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x02ff  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x02ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$fetchLocationAddress$29(Locale locale, final Location location, int i, Locale locale2, final LocationFetchCallback locationFetchCallback) {
        TLRPC.TL_messageMediaVenue tL_messageMediaVenue;
        TLRPC.TL_messageMediaVenue tL_messageMediaVenue2;
        TLRPC.TL_messageMediaVenue tL_messageMediaVenue3;
        TLRPC.TL_messageMediaVenue tL_messageMediaVenue4;
        final String str;
        final String str2;
        final TLRPC.TL_messageMediaVenue tL_messageMediaVenue5;
        final TLRPC.TL_messageMediaVenue tL_messageMediaVenue6;
        TL_stories.TL_geoPointAddress tL_geoPointAddress;
        String format;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        boolean z;
        String detectOcean;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        boolean z2;
        String subThoroughfare;
        boolean z3;
        String thoroughfare;
        String locality;
        String countryName;
        String str14;
        String str15;
        StringBuilder sb;
        String countryName2;
        String locality2;
        boolean z4;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        StringBuilder sb2 = new StringBuilder();
        TL_stories.TL_geoPointAddress tL_geoPointAddress2 = new TL_stories.TL_geoPointAddress();
        TL_stories.TL_geoPointAddress tL_geoPointAddress3 = new TL_stories.TL_geoPointAddress();
        String str21 = null;
        try {
            List<Address> fromLocation = new Geocoder(ApplicationLoader.applicationContext, locale).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            List<Address> fromLocation2 = i == 2 ? locale2 == locale ? fromLocation : new Geocoder(ApplicationLoader.applicationContext, locale2).getFromLocation(location.getLatitude(), location.getLongitude(), 1) : null;
            if (fromLocation.size() > 0) {
                Address address = fromLocation.get(0);
                Address address2 = (fromLocation2 == null || fromLocation2.size() < 1) ? null : fromLocation2.get(0);
                if (i == 1) {
                    ArrayList arrayList = new ArrayList();
                    try {
                        str20 = address.getAddressLine(0);
                    } catch (Exception unused) {
                        str20 = null;
                    }
                    if (TextUtils.isEmpty(str20)) {
                        try {
                            arrayList.add(address.getSubThoroughfare());
                        } catch (Exception unused2) {
                        }
                        try {
                            arrayList.add(address.getThoroughfare());
                        } catch (Exception unused3) {
                        }
                        try {
                            arrayList.add(address.getAdminArea());
                        } catch (Exception unused4) {
                        }
                        try {
                            arrayList.add(address.getCountryName());
                        } catch (Exception unused5) {
                        }
                    } else {
                        arrayList.add(str20);
                    }
                    int i2 = 0;
                    while (i2 < arrayList.size()) {
                        if (arrayList.get(i2) != null) {
                            String[] split = ((String) arrayList.get(i2)).split(", ");
                            if (split.length > 1) {
                                arrayList.remove(i2);
                                for (String str22 : split) {
                                    arrayList.add(i2, str22);
                                    i2++;
                                }
                            }
                        }
                        i2++;
                    }
                    int i3 = 0;
                    while (i3 < arrayList.size()) {
                        if (TextUtils.isEmpty((CharSequence) arrayList.get(i3)) || arrayList.indexOf(arrayList.get(i3)) != i3 || ((String) arrayList.get(i3)).matches("^\\s*\\d{4,}\\s*$")) {
                            arrayList.remove(i3);
                            i3--;
                        }
                        i3++;
                    }
                    format = arrayList.isEmpty() ? null : TextUtils.join(", ", arrayList);
                    str3 = format;
                    tL_geoPointAddress = tL_geoPointAddress3;
                    str4 = null;
                    str5 = null;
                    str7 = null;
                    str9 = null;
                    z4 = true;
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    StringBuilder sb5 = new StringBuilder();
                    StringBuilder sb6 = new StringBuilder();
                    String locality3 = TextUtils.isEmpty(null) ? address.getLocality() : null;
                    if (TextUtils.isEmpty(locality3)) {
                        locality3 = address.getAdminArea();
                    }
                    if (TextUtils.isEmpty(locality3)) {
                        locality3 = address.getSubAdminArea();
                    }
                    String str23 = locality3;
                    if (address2 != null) {
                        str9 = TextUtils.isEmpty(null) ? address2.getLocality() : null;
                        if (TextUtils.isEmpty(str9)) {
                            str9 = address2.getAdminArea();
                        }
                        if (TextUtils.isEmpty(str9)) {
                            str9 = address2.getSubAdminArea();
                        }
                        str8 = address2.getAdminArea();
                    } else {
                        str8 = null;
                        str9 = null;
                    }
                    String thoroughfare2 = (!TextUtils.isEmpty(null) || TextUtils.equals(address.getThoroughfare(), str23) || TextUtils.equals(address.getThoroughfare(), address.getCountryName())) ? null : address.getThoroughfare();
                    if (!TextUtils.isEmpty(thoroughfare2) || TextUtils.equals(address.getSubLocality(), str23)) {
                        str10 = thoroughfare2;
                    } else {
                        str10 = thoroughfare2;
                        if (!TextUtils.equals(address.getSubLocality(), address.getCountryName())) {
                            str11 = address.getSubLocality();
                            if (TextUtils.isEmpty(str11) || TextUtils.equals(address.getLocality(), str23)) {
                                str12 = str11;
                            } else {
                                str12 = str11;
                                if (!TextUtils.equals(address.getLocality(), address.getCountryName())) {
                                    str13 = address.getLocality();
                                    if (!TextUtils.isEmpty(str13) || TextUtils.equals(str13, str23) || TextUtils.equals(str13, address.getCountryName())) {
                                        sb6 = null;
                                    } else {
                                        if (sb6.length() > 0) {
                                            sb6.append(", ");
                                        }
                                        sb6.append(str13);
                                    }
                                    if (address2 != null) {
                                        String thoroughfare3 = (!TextUtils.isEmpty(null) || TextUtils.equals(address2.getThoroughfare(), str23) || TextUtils.equals(address2.getThoroughfare(), address2.getCountryName())) ? null : address2.getThoroughfare();
                                        if (!TextUtils.isEmpty(thoroughfare3) || TextUtils.equals(address2.getSubLocality(), str23)) {
                                            str16 = thoroughfare3;
                                        } else {
                                            str16 = thoroughfare3;
                                            if (!TextUtils.equals(address2.getSubLocality(), address2.getCountryName())) {
                                                str17 = address2.getSubLocality();
                                                if (TextUtils.isEmpty(str17) || TextUtils.equals(address2.getLocality(), str23)) {
                                                    str18 = str17;
                                                } else {
                                                    str18 = str17;
                                                    if (!TextUtils.equals(address2.getLocality(), address2.getCountryName())) {
                                                        str19 = address2.getLocality();
                                                        if (!TextUtils.isEmpty(str19) || TextUtils.equals(str19, str8) || TextUtils.equals(str19, address2.getCountryName())) {
                                                            sb2 = null;
                                                        } else {
                                                            if (sb2.length() > 0) {
                                                                sb2.append(", ");
                                                            }
                                                            sb2.append(str19);
                                                        }
                                                        if (!TextUtils.isEmpty(sb2)) {
                                                            int i4 = 0;
                                                            while (true) {
                                                                String[] strArr = unnamedRoads;
                                                                if (i4 >= strArr.length) {
                                                                    break;
                                                                }
                                                                if (strArr[i4].equalsIgnoreCase(sb2.toString())) {
                                                                    sb6 = null;
                                                                    sb2 = null;
                                                                    break;
                                                                }
                                                                i4++;
                                                            }
                                                        }
                                                    }
                                                }
                                                str19 = str18;
                                                if (TextUtils.isEmpty(str19)) {
                                                }
                                                sb2 = null;
                                                if (!TextUtils.isEmpty(sb2)) {
                                                }
                                            }
                                        }
                                        str17 = str16;
                                        if (TextUtils.isEmpty(str17)) {
                                        }
                                        str18 = str17;
                                        str19 = str18;
                                        if (TextUtils.isEmpty(str19)) {
                                        }
                                        sb2 = null;
                                        if (!TextUtils.isEmpty(sb2)) {
                                        }
                                    }
                                    if (TextUtils.isEmpty(str23)) {
                                        z2 = true;
                                    } else {
                                        if (sb5.length() > 0) {
                                            sb5.append(", ");
                                        }
                                        sb5.append(str23);
                                        if (sb6 != null) {
                                            if (sb6.length() > 0) {
                                                sb6.append(", ");
                                            }
                                            sb6.append(str23);
                                        }
                                        z2 = false;
                                    }
                                    subThoroughfare = address.getSubThoroughfare();
                                    if (TextUtils.isEmpty(subThoroughfare)) {
                                        z3 = false;
                                    } else {
                                        sb3.append(subThoroughfare);
                                        z3 = true;
                                    }
                                    thoroughfare = address.getThoroughfare();
                                    boolean z5 = z2;
                                    if (!TextUtils.isEmpty(thoroughfare)) {
                                        if (sb3.length() > 0) {
                                            sb3.append(" ");
                                        }
                                        sb3.append(thoroughfare);
                                        z3 = true;
                                    }
                                    if (!z3) {
                                        String adminArea = address.getAdminArea();
                                        if (!TextUtils.isEmpty(adminArea)) {
                                            if (sb3.length() > 0) {
                                                sb3.append(", ");
                                            }
                                            sb3.append(adminArea);
                                        }
                                        String subAdminArea = address.getSubAdminArea();
                                        if (!TextUtils.isEmpty(subAdminArea)) {
                                            if (sb3.length() > 0) {
                                                sb3.append(", ");
                                            }
                                            sb3.append(subAdminArea);
                                        }
                                    }
                                    locality = address.getLocality();
                                    if (!TextUtils.isEmpty(locality)) {
                                        if (sb3.length() > 0) {
                                            sb3.append(", ");
                                        }
                                        sb3.append(locality);
                                    }
                                    String countryCode = address.getCountryCode();
                                    countryName = address.getCountryName();
                                    if (TextUtils.isEmpty(countryName)) {
                                        str14 = countryCode;
                                        str15 = str8;
                                        sb = sb2;
                                        tL_geoPointAddress = tL_geoPointAddress3;
                                    } else {
                                        if (sb3.length() > 0) {
                                            sb3.append(", ");
                                        }
                                        sb3.append(countryName);
                                        str14 = countryCode;
                                        String language = locale.getLanguage();
                                        str15 = str8;
                                        sb = sb2;
                                        if ("US".equals(address.getCountryCode())) {
                                            tL_geoPointAddress = tL_geoPointAddress3;
                                        } else {
                                            tL_geoPointAddress = tL_geoPointAddress3;
                                            if ("AE".equals(address.getCountryCode())) {
                                            }
                                            if ("GB".equals(address.getCountryCode()) && "en".equals(language)) {
                                                String[] split2 = countryName.split(" ");
                                                countryName = "";
                                                for (String str24 : split2) {
                                                    if (str24.length() > 0) {
                                                        countryName = countryName + str24.charAt(0);
                                                    }
                                                }
                                                if (sb5.length() > 0) {
                                                }
                                                sb5.append(countryName);
                                            } else {
                                                if ("US".equals(address.getCountryCode())) {
                                                    countryName = "USA";
                                                }
                                                if (sb5.length() > 0) {
                                                    sb5.append(", ");
                                                }
                                                sb5.append(countryName);
                                            }
                                        }
                                        if (!"en".equals(language)) {
                                            if (!"uk".equals(language)) {
                                            }
                                        }
                                        String[] split22 = countryName.split(" ");
                                        countryName = "";
                                        while (r3 < r5) {
                                        }
                                        if (sb5.length() > 0) {
                                        }
                                        sb5.append(countryName);
                                    }
                                    countryName2 = address.getCountryName();
                                    if (!TextUtils.isEmpty(countryName2)) {
                                        if (sb4.length() > 0) {
                                            sb4.append(", ");
                                        }
                                        sb4.append(countryName2);
                                    }
                                    locality2 = address.getLocality();
                                    if (!TextUtils.isEmpty(locality2)) {
                                        if (sb4.length() > 0) {
                                            sb4.append(", ");
                                        }
                                        sb4.append(locality2);
                                    }
                                    if (!z3) {
                                        String adminArea2 = address.getAdminArea();
                                        if (!TextUtils.isEmpty(adminArea2)) {
                                            if (sb4.length() > 0) {
                                                sb4.append(", ");
                                            }
                                            sb4.append(adminArea2);
                                        }
                                        String subAdminArea2 = address.getSubAdminArea();
                                        if (!TextUtils.isEmpty(subAdminArea2)) {
                                            if (sb4.length() > 0) {
                                                sb4.append(", ");
                                            }
                                            sb4.append(subAdminArea2);
                                        }
                                    }
                                    format = sb3.toString();
                                    str3 = sb4.toString();
                                    str4 = sb5.toString();
                                    String sb7 = sb6 == null ? null : sb6.toString();
                                    z4 = z5;
                                    str21 = sb7;
                                    str7 = str15;
                                    sb2 = sb;
                                    str5 = str14;
                                }
                            }
                            str13 = str12;
                            if (TextUtils.isEmpty(str13)) {
                            }
                            sb6 = null;
                            if (address2 != null) {
                            }
                            if (TextUtils.isEmpty(str23)) {
                            }
                            subThoroughfare = address.getSubThoroughfare();
                            if (TextUtils.isEmpty(subThoroughfare)) {
                            }
                            thoroughfare = address.getThoroughfare();
                            boolean z52 = z2;
                            if (!TextUtils.isEmpty(thoroughfare)) {
                            }
                            if (!z3) {
                            }
                            locality = address.getLocality();
                            if (!TextUtils.isEmpty(locality)) {
                            }
                            String countryCode2 = address.getCountryCode();
                            countryName = address.getCountryName();
                            if (TextUtils.isEmpty(countryName)) {
                            }
                            countryName2 = address.getCountryName();
                            if (!TextUtils.isEmpty(countryName2)) {
                            }
                            locality2 = address.getLocality();
                            if (!TextUtils.isEmpty(locality2)) {
                            }
                            if (!z3) {
                            }
                            format = sb3.toString();
                            str3 = sb4.toString();
                            str4 = sb5.toString();
                            if (sb6 == null) {
                            }
                            z4 = z52;
                            str21 = sb7;
                            str7 = str15;
                            sb2 = sb;
                            str5 = str14;
                        }
                    }
                    str11 = str10;
                    if (TextUtils.isEmpty(str11)) {
                    }
                    str12 = str11;
                    str13 = str12;
                    if (TextUtils.isEmpty(str13)) {
                    }
                    sb6 = null;
                    if (address2 != null) {
                    }
                    if (TextUtils.isEmpty(str23)) {
                    }
                    subThoroughfare = address.getSubThoroughfare();
                    if (TextUtils.isEmpty(subThoroughfare)) {
                    }
                    thoroughfare = address.getThoroughfare();
                    boolean z522 = z2;
                    if (!TextUtils.isEmpty(thoroughfare)) {
                    }
                    if (!z3) {
                    }
                    locality = address.getLocality();
                    if (!TextUtils.isEmpty(locality)) {
                    }
                    String countryCode22 = address.getCountryCode();
                    countryName = address.getCountryName();
                    if (TextUtils.isEmpty(countryName)) {
                    }
                    countryName2 = address.getCountryName();
                    if (!TextUtils.isEmpty(countryName2)) {
                    }
                    locality2 = address.getLocality();
                    if (!TextUtils.isEmpty(locality2)) {
                    }
                    if (!z3) {
                    }
                    format = sb3.toString();
                    str3 = sb4.toString();
                    str4 = sb5.toString();
                    if (sb6 == null) {
                    }
                    z4 = z522;
                    str21 = sb7;
                    str7 = str15;
                    sb2 = sb;
                    str5 = str14;
                }
                z = z4;
                str6 = str9;
            } else {
                tL_geoPointAddress = tL_geoPointAddress3;
                format = i == 1 ? null : String.format(Locale.US, "Unknown address (%f,%f)", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                str3 = format;
                str4 = null;
                str5 = null;
                str6 = null;
                str7 = null;
                z = true;
                str21 = null;
            }
            if (TextUtils.isEmpty(str4)) {
                tL_messageMediaVenue3 = null;
            } else {
                tL_messageMediaVenue3 = new TLRPC.TL_messageMediaVenue();
                try {
                    TLRPC.TL_geoPoint tL_geoPoint = new TLRPC.TL_geoPoint();
                    tL_messageMediaVenue3.geo = tL_geoPoint;
                    tL_geoPoint.lat = location.getLatitude();
                    tL_messageMediaVenue3.geo._long = location.getLongitude();
                    tL_messageMediaVenue3.query_id = -1L;
                    tL_messageMediaVenue3.title = str4;
                    tL_messageMediaVenue3.icon = z ? "https://ss3.4sqi.net/img/categories_v2/building/government_capitolbuilding_64.png" : "https://ss3.4sqi.net/img/categories_v2/travel/hotel_64.png";
                    tL_messageMediaVenue3.emoji = countryCodeToEmoji(str5);
                    tL_messageMediaVenue3.address = LocaleController.getString(z ? R.string.Country : R.string.PassportCity);
                    tL_messageMediaVenue3.geoAddress = tL_geoPointAddress2;
                    tL_geoPointAddress2.country_iso2 = str5;
                    if (!z) {
                        if (!TextUtils.isEmpty(str7)) {
                            tL_geoPointAddress2.flags |= 1;
                            tL_geoPointAddress2.state = str7;
                        }
                        if (!TextUtils.isEmpty(str6)) {
                            tL_geoPointAddress2.flags |= 2;
                            tL_geoPointAddress2.city = str6;
                        }
                    }
                } catch (Exception unused6) {
                    tL_messageMediaVenue4 = null;
                    str2 = String.format(Locale.US, "Unknown address (%f,%f)", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                    str = str2;
                    tL_messageMediaVenue5 = tL_messageMediaVenue3;
                    tL_messageMediaVenue6 = tL_messageMediaVenue4;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
                        }
                    });
                }
            }
            try {
                if (TextUtils.isEmpty(str21)) {
                    tL_messageMediaVenue4 = null;
                } else {
                    TLRPC.TL_messageMediaVenue tL_messageMediaVenue7 = new TLRPC.TL_messageMediaVenue();
                    try {
                        TLRPC.TL_geoPoint tL_geoPoint2 = new TLRPC.TL_geoPoint();
                        tL_messageMediaVenue7.geo = tL_geoPoint2;
                        tL_geoPoint2.lat = location.getLatitude();
                        tL_messageMediaVenue7.geo._long = location.getLongitude();
                        tL_messageMediaVenue7.query_id = -1L;
                        tL_messageMediaVenue7.title = str21;
                        tL_messageMediaVenue7.icon = "pin";
                        tL_messageMediaVenue7.address = LocaleController.getString(R.string.PassportStreet1);
                        TL_stories.TL_geoPointAddress tL_geoPointAddress4 = tL_geoPointAddress;
                        tL_messageMediaVenue7.geoAddress = tL_geoPointAddress4;
                        tL_geoPointAddress4.country_iso2 = str5;
                        if (!TextUtils.isEmpty(str7)) {
                            tL_geoPointAddress4.flags |= 1;
                            tL_geoPointAddress4.state = str7;
                        }
                        if (!TextUtils.isEmpty(str6)) {
                            tL_geoPointAddress4.flags |= 2;
                            tL_geoPointAddress4.city = str6;
                        }
                        if (!TextUtils.isEmpty(sb2)) {
                            tL_geoPointAddress4.flags |= 4;
                            tL_geoPointAddress4.street = sb2.toString();
                        }
                        tL_messageMediaVenue4 = tL_messageMediaVenue7;
                    } catch (Exception unused7) {
                        tL_messageMediaVenue4 = tL_messageMediaVenue7;
                        str2 = String.format(Locale.US, "Unknown address (%f,%f)", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                        str = str2;
                        tL_messageMediaVenue5 = tL_messageMediaVenue3;
                        tL_messageMediaVenue6 = tL_messageMediaVenue4;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
                            }
                        });
                    }
                }
            } catch (Exception unused8) {
                tL_messageMediaVenue = tL_messageMediaVenue3;
                tL_messageMediaVenue2 = null;
                tL_messageMediaVenue3 = tL_messageMediaVenue;
                tL_messageMediaVenue4 = tL_messageMediaVenue2;
                str2 = String.format(Locale.US, "Unknown address (%f,%f)", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                str = str2;
                tL_messageMediaVenue5 = tL_messageMediaVenue3;
                tL_messageMediaVenue6 = tL_messageMediaVenue4;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
                    }
                });
            }
        } catch (Exception unused9) {
            tL_messageMediaVenue = null;
        }
        if (tL_messageMediaVenue3 == null && tL_messageMediaVenue4 == null) {
            try {
                detectOcean = detectOcean(location.getLongitude(), location.getLatitude());
            } catch (Exception unused10) {
                tL_messageMediaVenue2 = tL_messageMediaVenue4;
                tL_messageMediaVenue = tL_messageMediaVenue3;
                tL_messageMediaVenue3 = tL_messageMediaVenue;
                tL_messageMediaVenue4 = tL_messageMediaVenue2;
                str2 = String.format(Locale.US, "Unknown address (%f,%f)", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                str = str2;
                tL_messageMediaVenue5 = tL_messageMediaVenue3;
                tL_messageMediaVenue6 = tL_messageMediaVenue4;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
                    }
                });
            }
            if (detectOcean != null) {
                TLRPC.TL_messageMediaVenue tL_messageMediaVenue8 = new TLRPC.TL_messageMediaVenue();
                try {
                    TLRPC.TL_geoPoint tL_geoPoint3 = new TLRPC.TL_geoPoint();
                    tL_messageMediaVenue8.geo = tL_geoPoint3;
                    tL_geoPoint3.lat = location.getLatitude();
                    tL_messageMediaVenue8.geo._long = location.getLongitude();
                    tL_messageMediaVenue8.query_id = -1L;
                    tL_messageMediaVenue8.title = detectOcean;
                    tL_messageMediaVenue8.icon = "pin";
                    tL_messageMediaVenue8.emoji = "🌊";
                    tL_messageMediaVenue8.address = "Ocean";
                    str = str3;
                    tL_messageMediaVenue5 = tL_messageMediaVenue8;
                    tL_messageMediaVenue6 = tL_messageMediaVenue4;
                    str2 = format;
                } catch (Exception unused11) {
                    tL_messageMediaVenue3 = tL_messageMediaVenue8;
                    str2 = String.format(Locale.US, "Unknown address (%f,%f)", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                    str = str2;
                    tL_messageMediaVenue5 = tL_messageMediaVenue3;
                    tL_messageMediaVenue6 = tL_messageMediaVenue4;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
                        }
                    });
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
                    }
                });
            }
        }
        str2 = format;
        str = str3;
        tL_messageMediaVenue5 = tL_messageMediaVenue3;
        tL_messageMediaVenue6 = tL_messageMediaVenue4;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.lambda$fetchLocationAddress$28(LocationController.LocationFetchCallback.this, str2, str, tL_messageMediaVenue5, tL_messageMediaVenue6, location);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLiveLocations$25(long j, TLObject tLObject) {
        this.cacheRequests.delete(j);
        TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
        int i = 0;
        while (i < messages_messages.messages.size()) {
            if (!(messages_messages.messages.get(i).media instanceof TLRPC.TL_messageMediaGeoLive)) {
                messages_messages.messages.remove(i);
                i--;
            }
            i++;
        }
        getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
        getMessagesController().putUsers(messages_messages.users, false);
        getMessagesController().putChats(messages_messages.chats, false);
        this.locationsCache.put(j, messages_messages.messages);
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsCacheChanged, Long.valueOf(j), Integer.valueOf(this.currentAccount));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLiveLocations$26(final long j, final TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$loadLiveLocations$25(j, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSharingLocations$14(ArrayList arrayList) {
        this.sharingLocationsUI.addAll(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            SharingLocationInfo sharingLocationInfo = (SharingLocationInfo) arrayList.get(i);
            this.sharingLocationsMapUI.put(sharingLocationInfo.did, sharingLocationInfo);
        }
        startService();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSharingLocations$15(final ArrayList arrayList) {
        this.sharingLocations.addAll(arrayList);
        for (int i = 0; i < this.sharingLocations.size(); i++) {
            SharingLocationInfo sharingLocationInfo = this.sharingLocations.get(i);
            this.sharingLocationsMap.put(sharingLocationInfo.did, sharingLocationInfo);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$loadSharingLocations$14(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSharingLocations$16(ArrayList arrayList, ArrayList arrayList2, final ArrayList arrayList3) {
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$loadSharingLocations$15(arrayList3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSharingLocations$17() {
        Long valueOf;
        ArrayList<Long> arrayList;
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList<TLRPC.User> arrayList3 = new ArrayList<>();
        final ArrayList<TLRPC.Chat> arrayList4 = new ArrayList<>();
        try {
            ArrayList<Long> arrayList5 = new ArrayList<>();
            ArrayList<Long> arrayList6 = new ArrayList<>();
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT uid, mid, date, period, message, proximity FROM sharing_locations WHERE 1", new Object[0]);
            while (queryFinalized.next()) {
                SharingLocationInfo sharingLocationInfo = new SharingLocationInfo();
                sharingLocationInfo.did = queryFinalized.longValue(0);
                sharingLocationInfo.mid = queryFinalized.intValue(1);
                sharingLocationInfo.stopTime = queryFinalized.intValue(2);
                sharingLocationInfo.period = queryFinalized.intValue(3);
                sharingLocationInfo.proximityMeters = queryFinalized.intValue(5);
                sharingLocationInfo.account = this.currentAccount;
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(4);
                if (byteBufferValue != null) {
                    MessageObject messageObject = new MessageObject(this.currentAccount, TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false), false, false);
                    sharingLocationInfo.messageObject = messageObject;
                    MessagesStorage.addUsersAndChatsFromMessage(messageObject.messageOwner, arrayList5, arrayList6, null);
                    byteBufferValue.reuse();
                }
                arrayList2.add(sharingLocationInfo);
                if (DialogObject.isChatDialog(sharingLocationInfo.did)) {
                    if (!arrayList6.contains(Long.valueOf(-sharingLocationInfo.did))) {
                        valueOf = Long.valueOf(-sharingLocationInfo.did);
                        arrayList = arrayList6;
                        arrayList.add(valueOf);
                    }
                } else if (DialogObject.isUserDialog(sharingLocationInfo.did) && !arrayList5.contains(Long.valueOf(sharingLocationInfo.did))) {
                    valueOf = Long.valueOf(sharingLocationInfo.did);
                    arrayList = arrayList5;
                    arrayList.add(valueOf);
                }
            }
            queryFinalized.dispose();
            if (!arrayList6.isEmpty()) {
                getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList6), arrayList4);
            }
            getMessagesStorage().getUsersInternal(arrayList5, arrayList3);
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (arrayList2.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$loadSharingLocations$16(arrayList3, arrayList4, arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$markLiveLoactionsAsRead$27(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.TL_messages_affectedMessages) {
            TLRPC.TL_messages_affectedMessages tL_messages_affectedMessages = (TLRPC.TL_messages_affectedMessages) tLObject;
            getMessagesController().processNewDifferenceParams(-1, tL_messages_affectedMessages.pts, -1, tL_messages_affectedMessages.pts_count);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        LocationController locationController = getAccountInstance().getLocationController();
        getNotificationCenter().addObserver(locationController, NotificationCenter.didReceiveNewMessages);
        getNotificationCenter().addObserver(locationController, NotificationCenter.messagesDeleted);
        getNotificationCenter().addObserver(locationController, NotificationCenter.replaceMessagesObjects);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnected$1(Integer num) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShowPlayServicesAlert, num);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnected$2(final Integer num) {
        if (this.sharingLocations.isEmpty()) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$onConnected$1(num);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnected$3() {
        this.servicesAvailable = Boolean.FALSE;
        try {
            this.apiClient.disconnect();
            start();
        } catch (Throwable unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConnected$4(final Integer num) {
        int intValue = num.intValue();
        if (intValue == 0) {
            startFusedLocationRequest(true);
        } else if (intValue == 1) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    LocationController.this.lambda$onConnected$2(num);
                }
            });
        } else {
            if (intValue != 2) {
                return;
            }
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    LocationController.this.lambda$onConnected$3();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllLocationSharings$22(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            return;
        }
        getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllLocationSharings$23() {
        this.sharingLocationsUI.clear();
        this.sharingLocationsMapUI.clear();
        stopService();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllLocationSharings$24() {
        for (int i = 0; i < this.sharingLocations.size(); i++) {
            SharingLocationInfo sharingLocationInfo = this.sharingLocations.get(i);
            TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
            tL_messages_editMessage.peer = getMessagesController().getInputPeer(sharingLocationInfo.did);
            tL_messages_editMessage.id = sharingLocationInfo.mid;
            tL_messages_editMessage.flags |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
            TLRPC.TL_inputMediaGeoLive tL_inputMediaGeoLive = new TLRPC.TL_inputMediaGeoLive();
            tL_messages_editMessage.media = tL_inputMediaGeoLive;
            tL_inputMediaGeoLive.stopped = true;
            tL_inputMediaGeoLive.geo_point = new TLRPC.TL_inputGeoPointEmpty();
            getConnectionsManager().sendRequest(tL_messages_editMessage, new RequestDelegate() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LocationController.this.lambda$removeAllLocationSharings$22(tLObject, tL_error);
                }
            });
        }
        this.sharingLocations.clear();
        this.sharingLocationsMap.clear();
        saveSharingLocation(null, 2);
        stop(true);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$removeAllLocationSharings$23();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeSharingLocation$19(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error != null) {
            return;
        }
        getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeSharingLocation$20(SharingLocationInfo sharingLocationInfo) {
        this.sharingLocationsUI.remove(sharingLocationInfo);
        this.sharingLocationsMapUI.remove(sharingLocationInfo.did);
        if (this.sharingLocationsUI.isEmpty()) {
            stopService();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeSharingLocation$21(long j) {
        final SharingLocationInfo sharingLocationInfo = (SharingLocationInfo) this.sharingLocationsMap.get(j);
        this.sharingLocationsMap.remove(j);
        if (sharingLocationInfo != null) {
            TLRPC.TL_messages_editMessage tL_messages_editMessage = new TLRPC.TL_messages_editMessage();
            tL_messages_editMessage.peer = getMessagesController().getInputPeer(sharingLocationInfo.did);
            tL_messages_editMessage.id = sharingLocationInfo.mid;
            tL_messages_editMessage.flags |= LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
            TLRPC.TL_inputMediaGeoLive tL_inputMediaGeoLive = new TLRPC.TL_inputMediaGeoLive();
            tL_messages_editMessage.media = tL_inputMediaGeoLive;
            tL_inputMediaGeoLive.stopped = true;
            tL_inputMediaGeoLive.geo_point = new TLRPC.TL_inputGeoPointEmpty();
            getConnectionsManager().sendRequest(tL_messages_editMessage, new RequestDelegate() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda15
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LocationController.this.lambda$removeSharingLocation$19(tLObject, tL_error);
                }
            });
            this.sharingLocations.remove(sharingLocationInfo);
            saveSharingLocation(sharingLocationInfo, 1);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    LocationController.this.lambda$removeSharingLocation$20(sharingLocationInfo);
                }
            });
            if (this.sharingLocations.isEmpty()) {
                stop(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveSharingLocation$18(int i, SharingLocationInfo sharingLocationInfo) {
        SQLitePreparedStatement executeFast;
        try {
            if (i == 2) {
                executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM sharing_locations WHERE 1");
            } else {
                if (i != 1) {
                    if (sharingLocationInfo == null) {
                        return;
                    }
                    SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("REPLACE INTO sharing_locations VALUES(?, ?, ?, ?, ?, ?)");
                    executeFast2.requery();
                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(sharingLocationInfo.messageObject.messageOwner.getObjectSize());
                    sharingLocationInfo.messageObject.messageOwner.serializeToStream(nativeByteBuffer);
                    executeFast2.bindLong(1, sharingLocationInfo.did);
                    executeFast2.bindInteger(2, sharingLocationInfo.mid);
                    executeFast2.bindInteger(3, sharingLocationInfo.stopTime);
                    executeFast2.bindInteger(4, sharingLocationInfo.period);
                    executeFast2.bindByteBuffer(5, nativeByteBuffer);
                    executeFast2.bindInteger(6, sharingLocationInfo.proximityMeters);
                    executeFast2.step();
                    executeFast2.dispose();
                    nativeByteBuffer.reuse();
                    return;
                }
                if (sharingLocationInfo == null) {
                    return;
                }
                executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM sharing_locations WHERE uid = " + sharingLocationInfo.did);
            }
            executeFast.stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setLastKnownLocation$10() {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.newLocationAvailable, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setProximityLocation$12(int i, long j) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE sharing_locations SET proximity = ? WHERE uid = ?");
            executeFast.requery();
            executeFast.bindInteger(1, i);
            executeFast.bindLong(2, j);
            executeFast.step();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setProximityLocation$13() {
        broadcastLastKnownLocation(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startFusedLocationRequest$5(boolean z) {
        if (!z) {
            this.servicesAvailable = Boolean.FALSE;
        }
        if (this.sharingLocations.isEmpty()) {
            return;
        }
        if (!z) {
            start();
            return;
        }
        try {
            ApplicationLoader.getLocationServiceProvider().getLastLocation(new Consumer() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda7
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    LocationController.this.setLastKnownLocation((Location) obj);
                }
            });
            ApplicationLoader.getLocationServiceProvider().requestLocationUpdates(this.locationRequest, this.fusedLocationListener);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$8(SharingLocationInfo sharingLocationInfo) {
        this.sharingLocationsUI.remove(sharingLocationInfo);
        this.sharingLocationsMapUI.remove(sharingLocationInfo.did);
        if (this.sharingLocationsUI.isEmpty()) {
            stopService();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsChanged, new Object[0]);
    }

    private void loadSharingLocations() {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$loadSharingLocations$17();
            }
        });
    }

    private void saveSharingLocation(final SharingLocationInfo sharingLocationInfo, final int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$saveSharingLocation$18(i, sharingLocationInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLastKnownLocation(Location location) {
        if (location == null || (SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos()) / 1000000000 <= 300) {
            this.lastKnownLocation = location;
            if (location != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationController.lambda$setLastKnownLocation$10();
                    }
                });
            }
        }
    }

    private boolean shouldSendLocationNow() {
        return shouldStopGps() && Math.abs(this.lastLocationSendTime - SystemClock.elapsedRealtime()) >= 2000;
    }

    private boolean shouldStopGps() {
        return SystemClock.elapsedRealtime() > this.locationEndWatchTime;
    }

    private void start() {
        if (this.started) {
            return;
        }
        this.lastLocationStartTime = SystemClock.elapsedRealtime();
        this.started = true;
        if (checkServices()) {
            try {
                this.apiClient.connect();
                return;
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        try {
            this.locationManager.requestLocationUpdates("gps", 1L, 0.0f, this.gpsLocationListener);
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            this.locationManager.requestLocationUpdates("network", 1L, 0.0f, this.networkLocationListener);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            this.locationManager.requestLocationUpdates("passive", 1L, 0.0f, this.passiveLocationListener);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        if (this.lastKnownLocation == null) {
            try {
                setLastKnownLocation(this.locationManager.getLastKnownLocation("gps"));
                if (this.lastKnownLocation == null) {
                    setLastKnownLocation(this.locationManager.getLastKnownLocation("network"));
                }
            } catch (Exception e4) {
                FileLog.e(e4);
            }
        }
    }

    private void startService() {
        try {
            if (!PermissionRequest.hasPermission("android.permission.ACCESS_COARSE_LOCATION") && !PermissionRequest.hasPermission("android.permission.ACCESS_FINE_LOCATION")) {
                return;
            }
            ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class<?>) LocationSharingService.class));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private void stop(boolean z) {
        this.started = false;
        if (checkServices()) {
            try {
                ApplicationLoader.getLocationServiceProvider().removeLocationUpdates(this.fusedLocationListener);
                this.apiClient.disconnect();
            } catch (Throwable th) {
                FileLog.e(th, false);
            }
        }
        this.locationManager.removeUpdates(this.gpsLocationListener);
        if (z) {
            this.locationManager.removeUpdates(this.networkLocationListener);
            this.locationManager.removeUpdates(this.passiveLocationListener);
        }
    }

    private void stopService() {
        ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class<?>) LocationSharingService.class));
    }

    protected void addSharingLocation(TLRPC.Message message) {
        final SharingLocationInfo sharingLocationInfo = new SharingLocationInfo();
        sharingLocationInfo.did = message.dialog_id;
        sharingLocationInfo.mid = message.id;
        TLRPC.MessageMedia messageMedia = message.media;
        sharingLocationInfo.period = messageMedia.period;
        int i = messageMedia.proximity_notification_radius;
        sharingLocationInfo.proximityMeters = i;
        sharingLocationInfo.lastSentProximityMeters = i;
        sharingLocationInfo.account = this.currentAccount;
        sharingLocationInfo.messageObject = new MessageObject(this.currentAccount, message, false, false);
        if (sharingLocationInfo.period == Integer.MAX_VALUE) {
            sharingLocationInfo.stopTime = ConnectionsManager.DEFAULT_DATACENTER_ID;
        } else {
            sharingLocationInfo.stopTime = getConnectionsManager().getCurrentTime() + sharingLocationInfo.period;
        }
        final SharingLocationInfo sharingLocationInfo2 = (SharingLocationInfo) this.sharingLocationsMap.get(sharingLocationInfo.did);
        this.sharingLocationsMap.put(sharingLocationInfo.did, sharingLocationInfo);
        if (sharingLocationInfo2 != null) {
            this.sharingLocations.remove(sharingLocationInfo2);
        }
        this.sharingLocations.add(sharingLocationInfo);
        saveSharingLocation(sharingLocationInfo, 0);
        this.lastLocationSendTime = SystemClock.elapsedRealtime() - 25000;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$addSharingLocation$11(sharingLocationInfo2, sharingLocationInfo);
            }
        });
    }

    public void cleanup() {
        this.sharingLocationsUI.clear();
        this.sharingLocationsMapUI.clear();
        this.locationsCache.clear();
        this.cacheRequests.clear();
        this.lastReadLocationTime.clear();
        stopService();
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$cleanup$9();
            }
        });
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        ArrayList arrayList;
        ArrayList arrayList2;
        if (i == NotificationCenter.didReceiveNewMessages) {
            if (((Boolean) objArr[2]).booleanValue()) {
                return;
            }
            Long l = (Long) objArr[0];
            long longValue = l.longValue();
            if (isSharingLocation(longValue) && (arrayList2 = (ArrayList) this.locationsCache.get(longValue)) != null) {
                ArrayList arrayList3 = (ArrayList) objArr[1];
                boolean z = false;
                for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                    MessageObject messageObject = (MessageObject) arrayList3.get(i3);
                    if (messageObject.isLiveLocation()) {
                        int i4 = 0;
                        while (true) {
                            if (i4 >= arrayList2.size()) {
                                arrayList2.add(messageObject.messageOwner);
                                break;
                            } else {
                                if (MessageObject.getFromChatId((TLRPC.Message) arrayList2.get(i4)) == messageObject.getFromChatId()) {
                                    arrayList2.set(i4, messageObject.messageOwner);
                                    break;
                                }
                                i4++;
                            }
                        }
                        z = true;
                    } else if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGeoProximityReached) {
                        long dialogId = messageObject.getDialogId();
                        if (DialogObject.isUserDialog(dialogId)) {
                            setProximityLocation(dialogId, 0, false);
                        }
                    }
                }
                if (z) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsCacheChanged, l, Integer.valueOf(this.currentAccount));
                    return;
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.messagesDeleted) {
            if (((Boolean) objArr[2]).booleanValue() || this.sharingLocationsUI.isEmpty()) {
                return;
            }
            ArrayList arrayList4 = (ArrayList) objArr[0];
            long longValue2 = ((Long) objArr[1]).longValue();
            ArrayList arrayList5 = null;
            for (int i5 = 0; i5 < this.sharingLocationsUI.size(); i5++) {
                SharingLocationInfo sharingLocationInfo = this.sharingLocationsUI.get(i5);
                MessageObject messageObject2 = sharingLocationInfo.messageObject;
                if (longValue2 == (messageObject2 != null ? messageObject2.getChannelId() : 0L) && arrayList4.contains(Integer.valueOf(sharingLocationInfo.mid))) {
                    if (arrayList5 == null) {
                        arrayList5 = new ArrayList();
                    }
                    arrayList5.add(Long.valueOf(sharingLocationInfo.did));
                }
            }
            if (arrayList5 != null) {
                for (int i6 = 0; i6 < arrayList5.size(); i6++) {
                    removeSharingLocation(((Long) arrayList5.get(i6)).longValue());
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.replaceMessagesObjects) {
            Long l2 = (Long) objArr[0];
            long longValue3 = l2.longValue();
            if (isSharingLocation(longValue3) && (arrayList = (ArrayList) this.locationsCache.get(longValue3)) != null) {
                ArrayList arrayList6 = (ArrayList) objArr[1];
                boolean z2 = false;
                for (int i7 = 0; i7 < arrayList6.size(); i7++) {
                    MessageObject messageObject3 = (MessageObject) arrayList6.get(i7);
                    int i8 = 0;
                    while (true) {
                        if (i8 >= arrayList.size()) {
                            break;
                        }
                        if (MessageObject.getFromChatId((TLRPC.Message) arrayList.get(i8)) == messageObject3.getFromChatId()) {
                            if (messageObject3.isLiveLocation()) {
                                arrayList.set(i8, messageObject3.messageOwner);
                            } else {
                                arrayList.remove(i8);
                            }
                            z2 = true;
                        } else {
                            i8++;
                        }
                    }
                }
                if (z2) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.liveLocationsCacheChanged, l2, Integer.valueOf(this.currentAccount));
                }
            }
        }
    }

    public Location getLastKnownLocation() {
        return this.lastKnownLocation;
    }

    public SharingLocationInfo getSharingLocationInfo(long j) {
        return (SharingLocationInfo) this.sharingLocationsMapUI.get(j);
    }

    public boolean isSharingLocation(long j) {
        return this.sharingLocationsMapUI.indexOfKey(j) >= 0;
    }

    public void loadLiveLocations(final long j) {
        if (this.cacheRequests.indexOfKey(j) >= 0) {
            return;
        }
        this.cacheRequests.put(j, Boolean.TRUE);
        TLRPC.TL_messages_getRecentLocations tL_messages_getRecentLocations = new TLRPC.TL_messages_getRecentLocations();
        tL_messages_getRecentLocations.peer = getMessagesController().getInputPeer(j);
        tL_messages_getRecentLocations.limit = 100;
        getConnectionsManager().sendRequest(tL_messages_getRecentLocations, new RequestDelegate() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda23
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LocationController.this.lambda$loadLiveLocations$26(j, tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [org.telegram.tgnet.TLRPC$TL_channels_readMessageContents] */
    /* JADX WARN: Type inference failed for: r1v6, types: [org.telegram.tgnet.TLRPC$TL_messages_readMessageContents] */
    /* JADX WARN: Type inference failed for: r1v7, types: [org.telegram.tgnet.TLObject] */
    /* JADX WARN: Type inference failed for: r7v2, types: [org.telegram.tgnet.ConnectionsManager] */
    public void markLiveLoactionsAsRead(long j) {
        ArrayList arrayList;
        ?? tL_messages_readMessageContents;
        if (DialogObject.isEncryptedDialog(j) || (arrayList = (ArrayList) this.locationsCache.get(j)) == null || arrayList.isEmpty()) {
            return;
        }
        Integer num = (Integer) this.lastReadLocationTime.get(j);
        int elapsedRealtime = (int) (SystemClock.elapsedRealtime() / 1000);
        if (num == null || num.intValue() + 60 <= elapsedRealtime) {
            this.lastReadLocationTime.put(j, Integer.valueOf(elapsedRealtime));
            int i = 0;
            if (DialogObject.isChatDialog(j)) {
                long j2 = -j;
                if (ChatObject.isChannel(j2, this.currentAccount)) {
                    tL_messages_readMessageContents = new TLRPC.TL_channels_readMessageContents();
                    int size = arrayList.size();
                    while (i < size) {
                        tL_messages_readMessageContents.id.add(Integer.valueOf(((TLRPC.Message) arrayList.get(i)).id));
                        i++;
                    }
                    tL_messages_readMessageContents.channel = getMessagesController().getInputChannel(j2);
                    getConnectionsManager().sendRequest(tL_messages_readMessageContents, new RequestDelegate() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda29
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LocationController.this.lambda$markLiveLoactionsAsRead$27(tLObject, tL_error);
                        }
                    });
                }
            }
            tL_messages_readMessageContents = new TLRPC.TL_messages_readMessageContents();
            int size2 = arrayList.size();
            while (i < size2) {
                tL_messages_readMessageContents.id.add(Integer.valueOf(((TLRPC.Message) arrayList.get(i)).id));
                i++;
            }
            getConnectionsManager().sendRequest(tL_messages_readMessageContents, new RequestDelegate() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda29
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LocationController.this.lambda$markLiveLoactionsAsRead$27(tLObject, tL_error);
                }
            });
        }
    }

    @Override // org.telegram.messenger.ILocationServiceProvider.IAPIConnectionCallbacks
    public void onConnected(Bundle bundle) {
        this.wasConnectedToPlayServices = true;
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                ApplicationLoader.getLocationServiceProvider().checkLocationSettings(this.locationRequest, new Consumer() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda5
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        LocationController.this.lambda$onConnected$4((Integer) obj);
                    }
                });
            } else {
                startFusedLocationRequest(true);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    @Override // org.telegram.messenger.ILocationServiceProvider.IAPIOnConnectionFailedListener
    public void onConnectionFailed() {
        if (this.wasConnectedToPlayServices) {
            return;
        }
        this.servicesAvailable = Boolean.FALSE;
        if (this.started) {
            this.started = false;
            start();
        }
    }

    @Override // org.telegram.messenger.ILocationServiceProvider.IAPIConnectionCallbacks
    public void onConnectionSuspended(int i) {
    }

    public void removeAllLocationSharings() {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$removeAllLocationSharings$24();
            }
        });
    }

    public void removeSharingLocation(final long j) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$removeSharingLocation$21(j);
            }
        });
    }

    public void setMapLocation(Location location, boolean z) {
        long elapsedRealtime;
        long j;
        Location location2;
        if (location == null) {
            return;
        }
        this.lastLocationByMaps = true;
        if (!z && ((location2 = this.lastKnownLocation) == null || location2.distanceTo(location) < 20.0f)) {
            if (this.locationSentSinceLastMapUpdate) {
                elapsedRealtime = SystemClock.elapsedRealtime();
                j = 10000;
            }
            setLastKnownLocation(location);
        }
        elapsedRealtime = SystemClock.elapsedRealtime();
        j = 30000;
        this.lastLocationSendTime = elapsedRealtime - j;
        this.locationSentSinceLastMapUpdate = false;
        setLastKnownLocation(location);
    }

    protected void setNewLocationEndWatchTime() {
        if (this.sharingLocations.isEmpty()) {
            return;
        }
        this.locationEndWatchTime = SystemClock.elapsedRealtime() + 65000;
        start();
    }

    public boolean setProximityLocation(final long j, final int i, boolean z) {
        SharingLocationInfo sharingLocationInfo = (SharingLocationInfo) this.sharingLocationsMapUI.get(j);
        if (sharingLocationInfo != null) {
            sharingLocationInfo.proximityMeters = i;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$setProximityLocation$12(i, j);
            }
        });
        if (z) {
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LocationController.this.lambda$setProximityLocation$13();
                }
            });
        }
        return sharingLocationInfo != null;
    }

    public void startFusedLocationRequest(final boolean z) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                LocationController.this.lambda$startFusedLocationRequest$5(z);
            }
        });
    }

    protected void update() {
        getUserConfig();
        if (!this.sharingLocations.isEmpty()) {
            int i = 0;
            while (i < this.sharingLocations.size()) {
                final SharingLocationInfo sharingLocationInfo = this.sharingLocations.get(i);
                if (sharingLocationInfo.stopTime <= getConnectionsManager().getCurrentTime()) {
                    this.sharingLocations.remove(i);
                    this.sharingLocationsMap.remove(sharingLocationInfo.did);
                    saveSharingLocation(sharingLocationInfo, 1);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.LocationController$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationController.this.lambda$update$8(sharingLocationInfo);
                        }
                    });
                    i--;
                }
                i++;
            }
        }
        if (!this.started) {
            if (this.sharingLocations.isEmpty() || Math.abs(this.lastLocationSendTime - SystemClock.elapsedRealtime()) <= 30000) {
                return;
            }
            this.lastLocationStartTime = SystemClock.elapsedRealtime();
            start();
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.lastLocationByMaps || Math.abs(this.lastLocationStartTime - elapsedRealtime) > 10000 || shouldSendLocationNow()) {
            this.lastLocationByMaps = false;
            this.locationSentSinceLastMapUpdate = true;
            boolean z = SystemClock.elapsedRealtime() - this.lastLocationSendTime > 2000;
            this.lastLocationStartTime = elapsedRealtime;
            this.lastLocationSendTime = SystemClock.elapsedRealtime();
            broadcastLastKnownLocation(z);
        }
    }
}
