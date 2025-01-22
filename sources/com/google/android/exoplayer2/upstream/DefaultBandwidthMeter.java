package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.os.Handler;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.NetworkTypeObserver;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public final class DefaultBandwidthMeter implements BandwidthMeter, TransferListener {
    private static DefaultBandwidthMeter singletonInstance;
    private volatile long bitrateEstimate;
    private final Clock clock;
    private final BandwidthMeter.EventListener.EventDispatcher eventDispatcher;
    private final ImmutableMap initialBitrateEstimates;
    private long lastReportedBitrateEstimate;
    private int networkType;
    private int networkTypeOverride;
    private boolean networkTypeOverrideSet;
    private final boolean resetOnNetworkTypeChange;
    private long sampleBytesTransferred;
    private long sampleStartTimeMs;
    private final SlidingPercentile slidingPercentile;
    private int streamCount;
    private long totalBytesTransferred;
    private long totalElapsedTimeMs;
    public static final ImmutableList DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI = ImmutableList.of((Object) 4400000L, (Object) 3200000L, (Object) 2300000L, (Object) 1600000L, (Object) 810000L);
    public static final ImmutableList DEFAULT_INITIAL_BITRATE_ESTIMATES_2G = ImmutableList.of((Object) 1400000L, (Object) 990000L, (Object) 730000L, (Object) 510000L, (Object) 230000L);
    public static final ImmutableList DEFAULT_INITIAL_BITRATE_ESTIMATES_3G = ImmutableList.of((Object) 2100000L, (Object) 1400000L, (Object) 1000000L, (Object) 890000L, (Object) 640000L);
    public static final ImmutableList DEFAULT_INITIAL_BITRATE_ESTIMATES_4G = ImmutableList.of((Object) 2600000L, (Object) 1700000L, (Object) 1300000L, (Object) 1000000L, (Object) 700000L);
    public static final ImmutableList DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_NSA = ImmutableList.of((Object) 5700000L, (Object) 3700000L, (Object) 2300000L, (Object) 1700000L, (Object) 990000L);
    public static final ImmutableList DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_SA = ImmutableList.of((Object) 2800000L, (Object) 1800000L, (Object) 1400000L, (Object) 1100000L, (Object) 870000L);

    public static final class Builder {
        private Clock clock;
        private final Context context;
        private Map initialBitrateEstimates;
        private boolean resetOnNetworkTypeChange;
        private int slidingWindowMaxWeight;

        public Builder(Context context) {
            this.context = context == null ? null : context.getApplicationContext();
            this.initialBitrateEstimates = getInitialBitrateEstimatesForCountry(Util.getCountryCode(context));
            this.slidingWindowMaxWeight = 2000;
            this.clock = Clock.DEFAULT;
            this.resetOnNetworkTypeChange = true;
        }

        private static Map getInitialBitrateEstimatesForCountry(String str) {
            int[] initialBitrateCountryGroupAssignment = DefaultBandwidthMeter.getInitialBitrateCountryGroupAssignment(str);
            HashMap hashMap = new HashMap(8);
            hashMap.put(0, 1000000L);
            ImmutableList immutableList = DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_WIFI;
            hashMap.put(2, (Long) immutableList.get(initialBitrateCountryGroupAssignment[0]));
            hashMap.put(3, (Long) DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_2G.get(initialBitrateCountryGroupAssignment[1]));
            hashMap.put(4, (Long) DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_3G.get(initialBitrateCountryGroupAssignment[2]));
            hashMap.put(5, (Long) DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_4G.get(initialBitrateCountryGroupAssignment[3]));
            hashMap.put(10, (Long) DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_NSA.get(initialBitrateCountryGroupAssignment[4]));
            hashMap.put(9, (Long) DefaultBandwidthMeter.DEFAULT_INITIAL_BITRATE_ESTIMATES_5G_SA.get(initialBitrateCountryGroupAssignment[5]));
            hashMap.put(7, (Long) immutableList.get(initialBitrateCountryGroupAssignment[0]));
            return hashMap;
        }

        public DefaultBandwidthMeter build() {
            return new DefaultBandwidthMeter(this.context, this.initialBitrateEstimates, this.slidingWindowMaxWeight, this.clock, this.resetOnNetworkTypeChange);
        }
    }

    private DefaultBandwidthMeter(Context context, Map map, int i, Clock clock, boolean z) {
        this.initialBitrateEstimates = ImmutableMap.copyOf(map);
        this.eventDispatcher = new BandwidthMeter.EventListener.EventDispatcher();
        this.slidingPercentile = new SlidingPercentile(i);
        this.clock = clock;
        this.resetOnNetworkTypeChange = z;
        if (context == null) {
            this.networkType = 0;
            this.bitrateEstimate = getInitialBitrateEstimateForNetworkType(0);
            return;
        }
        NetworkTypeObserver networkTypeObserver = NetworkTypeObserver.getInstance(context);
        int networkType = networkTypeObserver.getNetworkType();
        this.networkType = networkType;
        this.bitrateEstimate = getInitialBitrateEstimateForNetworkType(networkType);
        networkTypeObserver.register(new NetworkTypeObserver.Listener() { // from class: com.google.android.exoplayer2.upstream.DefaultBandwidthMeter$$ExternalSyntheticLambda0
            @Override // com.google.android.exoplayer2.util.NetworkTypeObserver.Listener
            public final void onNetworkTypeChanged(int i2) {
                DefaultBandwidthMeter.this.onNetworkTypeChanged(i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:1053:0x0cf8, code lost:
    
        if (r8.equals("AD") == false) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int[] getInitialBitrateCountryGroupAssignment(String str) {
        char c = 0;
        str.hashCode();
        switch (str.hashCode()) {
            case 2083:
                break;
            case 2084:
                if (str.equals("AE")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 2085:
                if (str.equals("AF")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2086:
                if (str.equals("AG")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 2088:
                if (str.equals("AI")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 2091:
                if (str.equals("AL")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 2092:
                if (str.equals("AM")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 2094:
                if (str.equals("AO")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 2096:
                if (str.equals("AQ")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 2098:
                if (str.equals("AS")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 2099:
                if (str.equals("AT")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 2100:
                if (str.equals("AU")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 2102:
                if (str.equals("AW")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 2103:
                if (str.equals("AX")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 2105:
                if (str.equals("AZ")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 2111:
                if (str.equals("BA")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 2112:
                if (str.equals("BB")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 2114:
                if (str.equals("BD")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 2115:
                if (str.equals("BE")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 2116:
                if (str.equals("BF")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 2117:
                if (str.equals("BG")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 2118:
                if (str.equals("BH")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 2119:
                if (str.equals("BI")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 2120:
                if (str.equals("BJ")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case 2122:
                if (str.equals("BL")) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case 2123:
                if (str.equals("BM")) {
                    c = 25;
                    break;
                }
                c = 65535;
                break;
            case 2124:
                if (str.equals("BN")) {
                    c = 26;
                    break;
                }
                c = 65535;
                break;
            case 2125:
                if (str.equals("BO")) {
                    c = 27;
                    break;
                }
                c = 65535;
                break;
            case 2127:
                if (str.equals("BQ")) {
                    c = 28;
                    break;
                }
                c = 65535;
                break;
            case 2128:
                if (str.equals("BR")) {
                    c = 29;
                    break;
                }
                c = 65535;
                break;
            case 2129:
                if (str.equals("BS")) {
                    c = 30;
                    break;
                }
                c = 65535;
                break;
            case 2130:
                if (str.equals("BT")) {
                    c = 31;
                    break;
                }
                c = 65535;
                break;
            case 2133:
                if (str.equals("BW")) {
                    c = ' ';
                    break;
                }
                c = 65535;
                break;
            case 2135:
                if (str.equals("BY")) {
                    c = '!';
                    break;
                }
                c = 65535;
                break;
            case 2136:
                if (str.equals("BZ")) {
                    c = '\"';
                    break;
                }
                c = 65535;
                break;
            case 2142:
                if (str.equals("CA")) {
                    c = '#';
                    break;
                }
                c = 65535;
                break;
            case 2145:
                if (str.equals("CD")) {
                    c = '$';
                    break;
                }
                c = 65535;
                break;
            case 2147:
                if (str.equals("CF")) {
                    c = '%';
                    break;
                }
                c = 65535;
                break;
            case 2148:
                if (str.equals("CG")) {
                    c = '&';
                    break;
                }
                c = 65535;
                break;
            case 2149:
                if (str.equals("CH")) {
                    c = '\'';
                    break;
                }
                c = 65535;
                break;
            case 2150:
                if (str.equals("CI")) {
                    c = '(';
                    break;
                }
                c = 65535;
                break;
            case 2152:
                if (str.equals("CK")) {
                    c = ')';
                    break;
                }
                c = 65535;
                break;
            case 2153:
                if (str.equals("CL")) {
                    c = '*';
                    break;
                }
                c = 65535;
                break;
            case 2154:
                if (str.equals("CM")) {
                    c = '+';
                    break;
                }
                c = 65535;
                break;
            case 2155:
                if (str.equals("CN")) {
                    c = ',';
                    break;
                }
                c = 65535;
                break;
            case 2156:
                if (str.equals("CO")) {
                    c = '-';
                    break;
                }
                c = 65535;
                break;
            case 2159:
                if (str.equals("CR")) {
                    c = '.';
                    break;
                }
                c = 65535;
                break;
            case 2162:
                if (str.equals("CU")) {
                    c = '/';
                    break;
                }
                c = 65535;
                break;
            case 2163:
                if (str.equals("CV")) {
                    c = '0';
                    break;
                }
                c = 65535;
                break;
            case 2164:
                if (str.equals("CW")) {
                    c = '1';
                    break;
                }
                c = 65535;
                break;
            case 2165:
                if (str.equals("CX")) {
                    c = '2';
                    break;
                }
                c = 65535;
                break;
            case 2166:
                if (str.equals("CY")) {
                    c = '3';
                    break;
                }
                c = 65535;
                break;
            case 2167:
                if (str.equals("CZ")) {
                    c = '4';
                    break;
                }
                c = 65535;
                break;
            case 2177:
                if (str.equals("DE")) {
                    c = '5';
                    break;
                }
                c = 65535;
                break;
            case 2182:
                if (str.equals("DJ")) {
                    c = '6';
                    break;
                }
                c = 65535;
                break;
            case 2183:
                if (str.equals("DK")) {
                    c = '7';
                    break;
                }
                c = 65535;
                break;
            case 2185:
                if (str.equals("DM")) {
                    c = '8';
                    break;
                }
                c = 65535;
                break;
            case 2187:
                if (str.equals("DO")) {
                    c = '9';
                    break;
                }
                c = 65535;
                break;
            case 2198:
                if (str.equals("DZ")) {
                    c = ':';
                    break;
                }
                c = 65535;
                break;
            case 2206:
                if (str.equals("EC")) {
                    c = ';';
                    break;
                }
                c = 65535;
                break;
            case 2208:
                if (str.equals("EE")) {
                    c = '<';
                    break;
                }
                c = 65535;
                break;
            case 2210:
                if (str.equals("EG")) {
                    c = '=';
                    break;
                }
                c = 65535;
                break;
            case 2221:
                if (str.equals("ER")) {
                    c = '>';
                    break;
                }
                c = 65535;
                break;
            case 2222:
                if (str.equals("ES")) {
                    c = '?';
                    break;
                }
                c = 65535;
                break;
            case 2223:
                if (str.equals("ET")) {
                    c = '@';
                    break;
                }
                c = 65535;
                break;
            case 2243:
                if (str.equals("FI")) {
                    c = 'A';
                    break;
                }
                c = 65535;
                break;
            case 2244:
                if (str.equals("FJ")) {
                    c = 'B';
                    break;
                }
                c = 65535;
                break;
            case 2247:
                if (str.equals("FM")) {
                    c = 'C';
                    break;
                }
                c = 65535;
                break;
            case 2249:
                if (str.equals("FO")) {
                    c = 'D';
                    break;
                }
                c = 65535;
                break;
            case 2252:
                if (str.equals("FR")) {
                    c = 'E';
                    break;
                }
                c = 65535;
                break;
            case 2266:
                if (str.equals("GA")) {
                    c = 'F';
                    break;
                }
                c = 65535;
                break;
            case 2267:
                if (str.equals("GB")) {
                    c = 'G';
                    break;
                }
                c = 65535;
                break;
            case 2269:
                if (str.equals("GD")) {
                    c = 'H';
                    break;
                }
                c = 65535;
                break;
            case 2270:
                if (str.equals("GE")) {
                    c = 'I';
                    break;
                }
                c = 65535;
                break;
            case 2271:
                if (str.equals("GF")) {
                    c = 'J';
                    break;
                }
                c = 65535;
                break;
            case 2272:
                if (str.equals("GG")) {
                    c = 'K';
                    break;
                }
                c = 65535;
                break;
            case 2273:
                if (str.equals("GH")) {
                    c = 'L';
                    break;
                }
                c = 65535;
                break;
            case 2274:
                if (str.equals("GI")) {
                    c = 'M';
                    break;
                }
                c = 65535;
                break;
            case 2277:
                if (str.equals("GL")) {
                    c = 'N';
                    break;
                }
                c = 65535;
                break;
            case 2278:
                if (str.equals("GM")) {
                    c = 'O';
                    break;
                }
                c = 65535;
                break;
            case 2279:
                if (str.equals("GN")) {
                    c = 'P';
                    break;
                }
                c = 65535;
                break;
            case 2281:
                if (str.equals("GP")) {
                    c = 'Q';
                    break;
                }
                c = 65535;
                break;
            case 2282:
                if (str.equals("GQ")) {
                    c = 'R';
                    break;
                }
                c = 65535;
                break;
            case 2283:
                if (str.equals("GR")) {
                    c = 'S';
                    break;
                }
                c = 65535;
                break;
            case 2285:
                if (str.equals("GT")) {
                    c = 'T';
                    break;
                }
                c = 65535;
                break;
            case 2286:
                if (str.equals("GU")) {
                    c = 'U';
                    break;
                }
                c = 65535;
                break;
            case 2288:
                if (str.equals("GW")) {
                    c = 'V';
                    break;
                }
                c = 65535;
                break;
            case 2290:
                if (str.equals("GY")) {
                    c = 'W';
                    break;
                }
                c = 65535;
                break;
            case 2307:
                if (str.equals("HK")) {
                    c = 'X';
                    break;
                }
                c = 65535;
                break;
            case 2310:
                if (str.equals("HN")) {
                    c = 'Y';
                    break;
                }
                c = 65535;
                break;
            case 2314:
                if (str.equals("HR")) {
                    c = 'Z';
                    break;
                }
                c = 65535;
                break;
            case 2316:
                if (str.equals("HT")) {
                    c = '[';
                    break;
                }
                c = 65535;
                break;
            case 2317:
                if (str.equals("HU")) {
                    c = '\\';
                    break;
                }
                c = 65535;
                break;
            case 2331:
                if (str.equals("ID")) {
                    c = ']';
                    break;
                }
                c = 65535;
                break;
            case 2332:
                if (str.equals("IE")) {
                    c = '^';
                    break;
                }
                c = 65535;
                break;
            case 2339:
                if (str.equals("IL")) {
                    c = '_';
                    break;
                }
                c = 65535;
                break;
            case 2340:
                if (str.equals("IM")) {
                    c = '`';
                    break;
                }
                c = 65535;
                break;
            case 2341:
                if (str.equals("IN")) {
                    c = 'a';
                    break;
                }
                c = 65535;
                break;
            case 2342:
                if (str.equals("IO")) {
                    c = 'b';
                    break;
                }
                c = 65535;
                break;
            case 2344:
                if (str.equals("IQ")) {
                    c = 'c';
                    break;
                }
                c = 65535;
                break;
            case 2345:
                if (str.equals("IR")) {
                    c = 'd';
                    break;
                }
                c = 65535;
                break;
            case 2346:
                if (str.equals("IS")) {
                    c = 'e';
                    break;
                }
                c = 65535;
                break;
            case 2347:
                if (str.equals("IT")) {
                    c = 'f';
                    break;
                }
                c = 65535;
                break;
            case 2363:
                if (str.equals("JE")) {
                    c = 'g';
                    break;
                }
                c = 65535;
                break;
            case 2371:
                if (str.equals("JM")) {
                    c = 'h';
                    break;
                }
                c = 65535;
                break;
            case 2373:
                if (str.equals("JO")) {
                    c = 'i';
                    break;
                }
                c = 65535;
                break;
            case 2374:
                if (str.equals("JP")) {
                    c = 'j';
                    break;
                }
                c = 65535;
                break;
            case 2394:
                if (str.equals("KE")) {
                    c = 'k';
                    break;
                }
                c = 65535;
                break;
            case 2396:
                if (str.equals("KG")) {
                    c = 'l';
                    break;
                }
                c = 65535;
                break;
            case 2397:
                if (str.equals("KH")) {
                    c = 'm';
                    break;
                }
                c = 65535;
                break;
            case 2398:
                if (str.equals("KI")) {
                    c = 'n';
                    break;
                }
                c = 65535;
                break;
            case 2402:
                if (str.equals("KM")) {
                    c = 'o';
                    break;
                }
                c = 65535;
                break;
            case 2403:
                if (str.equals("KN")) {
                    c = 'p';
                    break;
                }
                c = 65535;
                break;
            case 2407:
                if (str.equals("KR")) {
                    c = 'q';
                    break;
                }
                c = 65535;
                break;
            case 2412:
                if (str.equals("KW")) {
                    c = 'r';
                    break;
                }
                c = 65535;
                break;
            case 2414:
                if (str.equals("KY")) {
                    c = 's';
                    break;
                }
                c = 65535;
                break;
            case 2415:
                if (str.equals("KZ")) {
                    c = 't';
                    break;
                }
                c = 65535;
                break;
            case 2421:
                if (str.equals("LA")) {
                    c = 'u';
                    break;
                }
                c = 65535;
                break;
            case 2422:
                if (str.equals("LB")) {
                    c = 'v';
                    break;
                }
                c = 65535;
                break;
            case 2423:
                if (str.equals("LC")) {
                    c = 'w';
                    break;
                }
                c = 65535;
                break;
            case 2429:
                if (str.equals("LI")) {
                    c = 'x';
                    break;
                }
                c = 65535;
                break;
            case 2431:
                if (str.equals("LK")) {
                    c = 'y';
                    break;
                }
                c = 65535;
                break;
            case 2438:
                if (str.equals("LR")) {
                    c = 'z';
                    break;
                }
                c = 65535;
                break;
            case 2439:
                if (str.equals("LS")) {
                    c = '{';
                    break;
                }
                c = 65535;
                break;
            case 2440:
                if (str.equals("LT")) {
                    c = '|';
                    break;
                }
                c = 65535;
                break;
            case 2441:
                if (str.equals("LU")) {
                    c = '}';
                    break;
                }
                c = 65535;
                break;
            case 2442:
                if (str.equals("LV")) {
                    c = '~';
                    break;
                }
                c = 65535;
                break;
            case 2445:
                if (str.equals("LY")) {
                    c = 127;
                    break;
                }
                c = 65535;
                break;
            case 2452:
                if (str.equals("MA")) {
                    c = 128;
                    break;
                }
                c = 65535;
                break;
            case 2454:
                if (str.equals("MC")) {
                    c = 129;
                    break;
                }
                c = 65535;
                break;
            case 2455:
                if (str.equals("MD")) {
                    c = 130;
                    break;
                }
                c = 65535;
                break;
            case 2456:
                if (str.equals("ME")) {
                    c = 131;
                    break;
                }
                c = 65535;
                break;
            case 2457:
                if (str.equals("MF")) {
                    c = 132;
                    break;
                }
                c = 65535;
                break;
            case 2458:
                if (str.equals("MG")) {
                    c = 133;
                    break;
                }
                c = 65535;
                break;
            case 2459:
                if (str.equals("MH")) {
                    c = 134;
                    break;
                }
                c = 65535;
                break;
            case 2462:
                if (str.equals("MK")) {
                    c = 135;
                    break;
                }
                c = 65535;
                break;
            case 2463:
                if (str.equals("ML")) {
                    c = 136;
                    break;
                }
                c = 65535;
                break;
            case 2464:
                if (str.equals("MM")) {
                    c = 137;
                    break;
                }
                c = 65535;
                break;
            case 2465:
                if (str.equals("MN")) {
                    c = 138;
                    break;
                }
                c = 65535;
                break;
            case 2466:
                if (str.equals("MO")) {
                    c = 139;
                    break;
                }
                c = 65535;
                break;
            case 2467:
                if (str.equals("MP")) {
                    c = 140;
                    break;
                }
                c = 65535;
                break;
            case 2468:
                if (str.equals("MQ")) {
                    c = 141;
                    break;
                }
                c = 65535;
                break;
            case 2469:
                if (str.equals("MR")) {
                    c = 142;
                    break;
                }
                c = 65535;
                break;
            case 2470:
                if (str.equals("MS")) {
                    c = 143;
                    break;
                }
                c = 65535;
                break;
            case 2471:
                if (str.equals("MT")) {
                    c = 144;
                    break;
                }
                c = 65535;
                break;
            case 2472:
                if (str.equals("MU")) {
                    c = 145;
                    break;
                }
                c = 65535;
                break;
            case 2473:
                if (str.equals("MV")) {
                    c = 146;
                    break;
                }
                c = 65535;
                break;
            case 2474:
                if (str.equals("MW")) {
                    c = 147;
                    break;
                }
                c = 65535;
                break;
            case 2475:
                if (str.equals("MX")) {
                    c = 148;
                    break;
                }
                c = 65535;
                break;
            case 2476:
                if (str.equals("MY")) {
                    c = 149;
                    break;
                }
                c = 65535;
                break;
            case 2477:
                if (str.equals("MZ")) {
                    c = 150;
                    break;
                }
                c = 65535;
                break;
            case 2483:
                if (str.equals("NA")) {
                    c = 151;
                    break;
                }
                c = 65535;
                break;
            case 2485:
                if (str.equals("NC")) {
                    c = 152;
                    break;
                }
                c = 65535;
                break;
            case 2487:
                if (str.equals("NE")) {
                    c = 153;
                    break;
                }
                c = 65535;
                break;
            case 2489:
                if (str.equals("NG")) {
                    c = 154;
                    break;
                }
                c = 65535;
                break;
            case 2491:
                if (str.equals("NI")) {
                    c = 155;
                    break;
                }
                c = 65535;
                break;
            case 2494:
                if (str.equals("NL")) {
                    c = 156;
                    break;
                }
                c = 65535;
                break;
            case 2497:
                if (str.equals("NO")) {
                    c = 157;
                    break;
                }
                c = 65535;
                break;
            case 2498:
                if (str.equals("NP")) {
                    c = 158;
                    break;
                }
                c = 65535;
                break;
            case 2500:
                if (str.equals("NR")) {
                    c = 159;
                    break;
                }
                c = 65535;
                break;
            case 2503:
                if (str.equals("NU")) {
                    c = 160;
                    break;
                }
                c = 65535;
                break;
            case 2508:
                if (str.equals("NZ")) {
                    c = 161;
                    break;
                }
                c = 65535;
                break;
            case 2526:
                if (str.equals("OM")) {
                    c = 162;
                    break;
                }
                c = 65535;
                break;
            case 2545:
                if (str.equals("PA")) {
                    c = 163;
                    break;
                }
                c = 65535;
                break;
            case 2549:
                if (str.equals("PE")) {
                    c = 164;
                    break;
                }
                c = 65535;
                break;
            case 2550:
                if (str.equals("PF")) {
                    c = 165;
                    break;
                }
                c = 65535;
                break;
            case 2551:
                if (str.equals("PG")) {
                    c = 166;
                    break;
                }
                c = 65535;
                break;
            case 2552:
                if (str.equals("PH")) {
                    c = 167;
                    break;
                }
                c = 65535;
                break;
            case 2555:
                if (str.equals("PK")) {
                    c = 168;
                    break;
                }
                c = 65535;
                break;
            case 2556:
                if (str.equals("PL")) {
                    c = 169;
                    break;
                }
                c = 65535;
                break;
            case 2557:
                if (str.equals("PM")) {
                    c = 170;
                    break;
                }
                c = 65535;
                break;
            case 2562:
                if (str.equals("PR")) {
                    c = 171;
                    break;
                }
                c = 65535;
                break;
            case 2563:
                if (str.equals("PS")) {
                    c = 172;
                    break;
                }
                c = 65535;
                break;
            case 2564:
                if (str.equals("PT")) {
                    c = 173;
                    break;
                }
                c = 65535;
                break;
            case 2567:
                if (str.equals("PW")) {
                    c = 174;
                    break;
                }
                c = 65535;
                break;
            case 2569:
                if (str.equals("PY")) {
                    c = 175;
                    break;
                }
                c = 65535;
                break;
            case 2576:
                if (str.equals("QA")) {
                    c = 176;
                    break;
                }
                c = 65535;
                break;
            case 2611:
                if (str.equals("RE")) {
                    c = 177;
                    break;
                }
                c = 65535;
                break;
            case 2621:
                if (str.equals("RO")) {
                    c = 178;
                    break;
                }
                c = 65535;
                break;
            case 2625:
                if (str.equals("RS")) {
                    c = 179;
                    break;
                }
                c = 65535;
                break;
            case 2627:
                if (str.equals("RU")) {
                    c = 180;
                    break;
                }
                c = 65535;
                break;
            case 2629:
                if (str.equals("RW")) {
                    c = 181;
                    break;
                }
                c = 65535;
                break;
            case 2638:
                if (str.equals("SA")) {
                    c = 182;
                    break;
                }
                c = 65535;
                break;
            case 2639:
                if (str.equals("SB")) {
                    c = 183;
                    break;
                }
                c = 65535;
                break;
            case 2640:
                if (str.equals("SC")) {
                    c = 184;
                    break;
                }
                c = 65535;
                break;
            case 2641:
                if (str.equals("SD")) {
                    c = 185;
                    break;
                }
                c = 65535;
                break;
            case 2642:
                if (str.equals("SE")) {
                    c = 186;
                    break;
                }
                c = 65535;
                break;
            case 2644:
                if (str.equals("SG")) {
                    c = 187;
                    break;
                }
                c = 65535;
                break;
            case 2645:
                if (str.equals("SH")) {
                    c = 188;
                    break;
                }
                c = 65535;
                break;
            case 2646:
                if (str.equals("SI")) {
                    c = 189;
                    break;
                }
                c = 65535;
                break;
            case 2647:
                if (str.equals("SJ")) {
                    c = 190;
                    break;
                }
                c = 65535;
                break;
            case 2648:
                if (str.equals("SK")) {
                    c = 191;
                    break;
                }
                c = 65535;
                break;
            case 2649:
                if (str.equals("SL")) {
                    c = 192;
                    break;
                }
                c = 65535;
                break;
            case 2650:
                if (str.equals("SM")) {
                    c = 193;
                    break;
                }
                c = 65535;
                break;
            case 2651:
                if (str.equals("SN")) {
                    c = 194;
                    break;
                }
                c = 65535;
                break;
            case 2652:
                if (str.equals("SO")) {
                    c = 195;
                    break;
                }
                c = 65535;
                break;
            case 2655:
                if (str.equals("SR")) {
                    c = 196;
                    break;
                }
                c = 65535;
                break;
            case 2656:
                if (str.equals("SS")) {
                    c = 197;
                    break;
                }
                c = 65535;
                break;
            case 2657:
                if (str.equals("ST")) {
                    c = 198;
                    break;
                }
                c = 65535;
                break;
            case 2659:
                if (str.equals("SV")) {
                    c = 199;
                    break;
                }
                c = 65535;
                break;
            case 2661:
                if (str.equals("SX")) {
                    c = 200;
                    break;
                }
                c = 65535;
                break;
            case 2662:
                if (str.equals("SY")) {
                    c = 201;
                    break;
                }
                c = 65535;
                break;
            case 2663:
                if (str.equals("SZ")) {
                    c = 202;
                    break;
                }
                c = 65535;
                break;
            case 2671:
                if (str.equals("TC")) {
                    c = 203;
                    break;
                }
                c = 65535;
                break;
            case 2672:
                if (str.equals("TD")) {
                    c = 204;
                    break;
                }
                c = 65535;
                break;
            case 2675:
                if (str.equals("TG")) {
                    c = 205;
                    break;
                }
                c = 65535;
                break;
            case 2676:
                if (str.equals("TH")) {
                    c = 206;
                    break;
                }
                c = 65535;
                break;
            case 2678:
                if (str.equals("TJ")) {
                    c = 207;
                    break;
                }
                c = 65535;
                break;
            case 2679:
                if (str.equals("TK")) {
                    c = 208;
                    break;
                }
                c = 65535;
                break;
            case 2680:
                if (str.equals("TL")) {
                    c = 209;
                    break;
                }
                c = 65535;
                break;
            case 2681:
                if (str.equals("TM")) {
                    c = 210;
                    break;
                }
                c = 65535;
                break;
            case 2682:
                if (str.equals("TN")) {
                    c = 211;
                    break;
                }
                c = 65535;
                break;
            case 2683:
                if (str.equals("TO")) {
                    c = 212;
                    break;
                }
                c = 65535;
                break;
            case 2686:
                if (str.equals("TR")) {
                    c = 213;
                    break;
                }
                c = 65535;
                break;
            case 2688:
                if (str.equals("TT")) {
                    c = 214;
                    break;
                }
                c = 65535;
                break;
            case 2690:
                if (str.equals("TV")) {
                    c = 215;
                    break;
                }
                c = 65535;
                break;
            case 2691:
                if (str.equals("TW")) {
                    c = 216;
                    break;
                }
                c = 65535;
                break;
            case 2694:
                if (str.equals("TZ")) {
                    c = 217;
                    break;
                }
                c = 65535;
                break;
            case 2700:
                if (str.equals("UA")) {
                    c = 218;
                    break;
                }
                c = 65535;
                break;
            case 2706:
                if (str.equals("UG")) {
                    c = 219;
                    break;
                }
                c = 65535;
                break;
            case 2718:
                if (str.equals("US")) {
                    c = 220;
                    break;
                }
                c = 65535;
                break;
            case 2724:
                if (str.equals("UY")) {
                    c = 221;
                    break;
                }
                c = 65535;
                break;
            case 2725:
                if (str.equals("UZ")) {
                    c = 222;
                    break;
                }
                c = 65535;
                break;
            case 2731:
                if (str.equals("VA")) {
                    c = 223;
                    break;
                }
                c = 65535;
                break;
            case 2733:
                if (str.equals("VC")) {
                    c = 224;
                    break;
                }
                c = 65535;
                break;
            case 2735:
                if (str.equals("VE")) {
                    c = 225;
                    break;
                }
                c = 65535;
                break;
            case 2737:
                if (str.equals("VG")) {
                    c = 226;
                    break;
                }
                c = 65535;
                break;
            case 2739:
                if (str.equals("VI")) {
                    c = 227;
                    break;
                }
                c = 65535;
                break;
            case 2744:
                if (str.equals("VN")) {
                    c = 228;
                    break;
                }
                c = 65535;
                break;
            case 2751:
                if (str.equals("VU")) {
                    c = 229;
                    break;
                }
                c = 65535;
                break;
            case 2767:
                if (str.equals("WF")) {
                    c = 230;
                    break;
                }
                c = 65535;
                break;
            case 2780:
                if (str.equals("WS")) {
                    c = 231;
                    break;
                }
                c = 65535;
                break;
            case 2803:
                if (str.equals("XK")) {
                    c = 232;
                    break;
                }
                c = 65535;
                break;
            case 2828:
                if (str.equals("YE")) {
                    c = 233;
                    break;
                }
                c = 65535;
                break;
            case 2843:
                if (str.equals("YT")) {
                    c = 234;
                    break;
                }
                c = 65535;
                break;
            case 2855:
                if (str.equals("ZA")) {
                    c = 235;
                    break;
                }
                c = 65535;
                break;
            case 2867:
                if (str.equals("ZM")) {
                    c = 236;
                    break;
                }
                c = 65535;
                break;
            case 2877:
                if (str.equals("ZW")) {
                    c = 237;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        int[] iArr = {2, 2, 0, 0, 2, 2};
        switch (c) {
            case 0:
            case '1':
                break;
            case 1:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case 2:
            case NotificationCenter.applyGroupCallVisibleParticipants /* 166 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 3:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 4:
            case 16:
            case 25:
            case 28:
            case '8':
            case 'D':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 5:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 6:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 7:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '\b':
            case '>':
            case NotificationCenter.storiesBlocklistUpdate /* 188 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '\t':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '\n':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 4;
                iArr[4] = 1;
                iArr[5] = 4;
                break;
            case 11:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 0;
                break;
            case '\f':
            case 'U':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '\r':
            case '2':
            case 'x':
            case NotificationCenter.filePreparingStarted /* 140 */:
            case NotificationCenter.dialogsUnreadCounterChanged /* 143 */:
            case NotificationCenter.groupCallVisibilityChanged /* 170 */:
            case NotificationCenter.channelRecommendationsLoaded /* 193 */:
            case NotificationCenter.starGiftSoldOut /* 223 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 14:
            case 19:
            case ':':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 15:
            case '^':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 17:
            case 't':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 18:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case 20:
            case '?':
            case 'S':
            case NotificationCenter.storiesLimitUpdate /* 189 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case 21:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case 22:
            case '[':
            case NotificationCenter.didUpdateConnectionState /* 133 */:
            case NotificationCenter.recordStopped /* 153 */:
            case NotificationCenter.groupPackUpdated /* 204 */:
            case NotificationCenter.botDownloadsUpdate /* 225 */:
            case NotificationCenter.emojiLoaded /* 233 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 23:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 24:
            case NotificationCenter.httpFileDidFailedLoad /* 132 */:
            case NotificationCenter.themeUploadedToServer /* 175 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 26:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 27:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 29:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 0;
                break;
            case 30:
            case 'v':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 31:
            case 150:
            case NotificationCenter.didReceiveSmsCode /* 231 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case ' ':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '!':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '\"':
            case ')':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '#':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 3;
                break;
            case '$':
            case 'o':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '%':
            case NotificationCenter.boostedChannelByUser /* 183 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '&':
            case 'L':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '\'':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 3;
                break;
            case '(':
            case '=':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '*':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case '+':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case ',':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 1;
                break;
            case '-':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '.':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '/':
            case 'n':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '0':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '3':
            case 'Z':
            case '~':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case '4':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 2;
                iArr[3] = 0;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case '5':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '6':
            case NotificationCenter.openBoostForUsersDialog /* 201 */:
            case NotificationCenter.premiumFloodWaitReceived /* 207 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '7':
            case '<':
            case '\\':
            case '|':
            case NotificationCenter.messagePlayingProgressDidChanged /* 144 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case '9':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case ';':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '@':
            case NotificationCenter.savedMessagesDialogsUpdate /* 194 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'A':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 2;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case 'B':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'C':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'E':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case 'F':
            case NotificationCenter.timezonesUpdated /* 205 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'G':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case 'H':
            case 'p':
            case 's':
            case 'w':
            case NotificationCenter.storyQualityUpdate /* 200 */:
            case NotificationCenter.updateStories /* 224 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'I':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'J':
            case NotificationCenter.didEndCall /* 168 */:
            case NotificationCenter.dialogPhotosUpdate /* 192 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'K':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'M':
            case 'g':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'N':
            case NotificationCenter.availableEffectsUpdate /* 208 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'O':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'P':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'Q':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'R':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'T':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case 'V':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'W':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'X':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 0;
                break;
            case 'Y':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case ']':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case '_':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case '`':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'a':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 1;
                break;
            case 'b':
            case NotificationCenter.factCheckLoaded /* 215 */:
            case NotificationCenter.wallpapersNeedReload /* 230 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'c':
            case NotificationCenter.storiesSendAsUpdate /* 190 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'd':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case 'e':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case 'f':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case 'h':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'i':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'j':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 4;
                break;
            case 'k':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'l':
            case NotificationCenter.fileNewChunkAvailable /* 141 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'm':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'q':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 4;
                break;
            case 'r':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case 'u':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 'y':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case 'z':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '{':
            case NotificationCenter.webViewResolved /* 219 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case '}':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case NotificationCenter.dialogTranslate /* 127 */:
            case NotificationCenter.starBalanceUpdated /* 212 */:
            case NotificationCenter.didReplacedPhotoInMemCache /* 237 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 128:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.walletPendingTransactionsChanged /* 129 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.walletSyncProgressChanged /* 130 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.httpFileDidLoad /* 131 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case NotificationCenter.fileUploaded /* 134 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.fileUploadFailed /* 135 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case NotificationCenter.fileUploadProgressChanged /* 136 */:
            case NotificationCenter.botStarsTransactionsLoaded /* 217 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.fileLoadProgressChanged /* 137 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.fileLoaded /* 138 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.fileLoadFailed /* 139 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.filePreparingFailed /* 142 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.messagePlayingDidReset /* 145 */:
            case NotificationCenter.didStartedMultiGiftsSelector /* 182 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.messagePlayingPlayStateChanged /* 146 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case NotificationCenter.messagePlayingDidStart /* 147 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.messagePlayingDidSeek /* 148 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.messagePlayingGoingToStop /* 149 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 4;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.recordStarted /* 151 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.recordStartError /* 152 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.recordPaused /* 154 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.recordResumed /* 155 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.screenshotTook /* 156 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 0;
                iArr[5] = 4;
                break;
            case NotificationCenter.albumsDidLoad /* 157 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case NotificationCenter.audioDidSent /* 158 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.audioRecordTooShort /* 159 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 0;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.audioRouteChanged /* 160 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.didStartedCall /* 161 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case 162:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.groupCallSpeakingUsersUpdated /* 163 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.groupCallScreencastStateChanged /* 164 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case NotificationCenter.activeGroupCallsUpdated /* 165 */:
            case NotificationCenter.smsJobStatusUpdate /* 199 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.groupCallTypingsUpdated /* 167 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 0;
                break;
            case NotificationCenter.closeInCallActivity /* 169 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.appDidLogout /* 171 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 2;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 1;
                break;
            case NotificationCenter.configLoaded /* 172 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 1;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.needDeleteDialog /* 173 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case NotificationCenter.newEmojiSuggestionsAvailable /* 174 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.themeUploadError /* 176 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.dialogFiltersUpdated /* 177 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case NotificationCenter.filterSettingsUpdated /* 178 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 1;
                iArr[5] = 2;
                break;
            case NotificationCenter.suggestedFiltersLoaded /* 179 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.updateBotMenuButton /* 180 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 3;
                iArr[5] = 3;
                break;
            case NotificationCenter.giftsToUserSent /* 181 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.boostByChannelCreated /* 184 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.didUpdatePremiumGiftStickers /* 185 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.didUpdatePremiumGiftFieldIcon /* 186 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 0;
                iArr[5] = 2;
                break;
            case NotificationCenter.storiesEnabledUpdate /* 187 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 3;
                break;
            case NotificationCenter.unconfirmedAuthUpdate /* 191 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case NotificationCenter.savedReactionTagsUpdate /* 195 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.userIsPremiumBlockedUpadted /* 196 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.savedMessagesForwarded /* 197 */:
            case NotificationCenter.starGiftOptionsLoaded /* 210 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 198:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.groupRestrictionsUnlockedByBoosts /* 202 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case 203:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.customStickerCreated /* 206 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.starOptionsLoaded /* 209 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.starGiveawayOptionsLoaded /* 211 */:
            case NotificationCenter.starGiftsLoaded /* 221 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.starTransactionsLoaded /* 213 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case NotificationCenter.starSubscriptionsLoaded /* 214 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 4;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.botStarsUpdated /* 216 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 0;
                break;
            case NotificationCenter.channelStarsUpdated /* 218 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 4;
                iArr[5] = 2;
                break;
            case NotificationCenter.updateAllMessages /* 220 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 1;
                break;
            case NotificationCenter.starUserGiftsLoaded /* 222 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            case NotificationCenter.channelSuggestedBotsUpdate /* 226 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.channelConnectedBotsUpdate /* 227 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.pushMessagesUpdated /* 228 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 1;
                break;
            case NotificationCenter.wallpapersDidLoad /* 229 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.didReceiveCall /* 232 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.invalidateMotionBackground /* 234 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.closeOtherAppActivities /* 235 */:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
            case NotificationCenter.cameraInitied /* 236 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 2;
                break;
            default:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                break;
        }
        return iArr;
    }

    private long getInitialBitrateEstimateForNetworkType(int i) {
        Long l = (Long) this.initialBitrateEstimates.get(Integer.valueOf(i));
        if (l == null) {
            l = (Long) this.initialBitrateEstimates.get(0);
        }
        if (l == null) {
            l = 1000000L;
        }
        return l.longValue();
    }

    public static synchronized DefaultBandwidthMeter getSingletonInstance(Context context) {
        DefaultBandwidthMeter defaultBandwidthMeter;
        synchronized (DefaultBandwidthMeter.class) {
            try {
                if (singletonInstance == null) {
                    singletonInstance = new Builder(context).build();
                }
                defaultBandwidthMeter = singletonInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return defaultBandwidthMeter;
    }

    private static boolean isTransferAtFullNetworkSpeed(DataSpec dataSpec, boolean z) {
        return z && (dataSpec == null || !dataSpec.isFlagSet(8));
    }

    private void maybeNotifyBandwidthSample(int i, long j, long j2) {
        if (i == 0 && j == 0 && j2 == this.lastReportedBitrateEstimate) {
            return;
        }
        this.lastReportedBitrateEstimate = j2;
        this.eventDispatcher.bandwidthSample(i, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onNetworkTypeChanged(int i) {
        int i2 = this.networkType;
        if (i2 == 0 || this.resetOnNetworkTypeChange) {
            if (this.networkTypeOverrideSet) {
                i = this.networkTypeOverride;
            }
            if (i2 == i) {
                return;
            }
            this.networkType = i;
            if (i != 1 && i != 0 && i != 8) {
                this.bitrateEstimate = getInitialBitrateEstimateForNetworkType(i);
                long elapsedRealtime = this.clock.elapsedRealtime();
                maybeNotifyBandwidthSample(this.streamCount > 0 ? (int) (elapsedRealtime - this.sampleStartTimeMs) : 0, this.sampleBytesTransferred, this.bitrateEstimate);
                this.sampleStartTimeMs = elapsedRealtime;
                this.sampleBytesTransferred = 0L;
                this.totalBytesTransferred = 0L;
                this.totalElapsedTimeMs = 0L;
                this.slidingPercentile.reset();
            }
        }
    }

    @Override // com.google.android.exoplayer2.upstream.BandwidthMeter
    public void addEventListener(Handler handler, BandwidthMeter.EventListener eventListener) {
        Assertions.checkNotNull(handler);
        Assertions.checkNotNull(eventListener);
        this.eventDispatcher.addListener(handler, eventListener);
    }

    @Override // com.google.android.exoplayer2.upstream.BandwidthMeter
    public synchronized long getBitrateEstimate() {
        return this.bitrateEstimate;
    }

    @Override // com.google.android.exoplayer2.upstream.BandwidthMeter
    public /* synthetic */ long getTimeToFirstByteEstimateUs() {
        return BandwidthMeter.-CC.$default$getTimeToFirstByteEstimateUs(this);
    }

    @Override // com.google.android.exoplayer2.upstream.BandwidthMeter
    public TransferListener getTransferListener() {
        return this;
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public synchronized void onBytesTransferred(DataSource dataSource, DataSpec dataSpec, boolean z, int i) {
        if (isTransferAtFullNetworkSpeed(dataSpec, z)) {
            this.sampleBytesTransferred += i;
        }
    }

    public synchronized void onTransfer(long j, long j2) {
        try {
            long elapsedRealtime = this.clock.elapsedRealtime();
            this.totalElapsedTimeMs += (int) (elapsedRealtime - this.sampleStartTimeMs);
            this.totalBytesTransferred += j;
            if (j2 > 0 && j > 0) {
                FileLog.d("debug_loading: bandwidth meter on transfer " + AndroidUtilities.formatFileSize(j) + " per " + j2 + "ms");
                this.slidingPercentile.addSample((int) Math.sqrt((double) j), (((float) j) * 8000.0f) / ((float) j2));
                if (this.totalElapsedTimeMs < 2000) {
                    if (this.totalBytesTransferred >= 524288) {
                    }
                    maybeNotifyBandwidthSample((int) j2, j, this.bitrateEstimate);
                    this.sampleStartTimeMs = elapsedRealtime;
                    this.sampleBytesTransferred = 0L;
                }
                this.bitrateEstimate = (long) this.slidingPercentile.getPercentile(0.5f);
                FileLog.d("debug_loading: bandwidth meter (onTransfer), bitrate estimate = " + this.bitrateEstimate);
                maybeNotifyBandwidthSample((int) j2, j, this.bitrateEstimate);
                this.sampleStartTimeMs = elapsedRealtime;
                this.sampleBytesTransferred = 0L;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public synchronized void onTransferEnd(DataSource dataSource, DataSpec dataSpec, boolean z) {
        try {
            if (isTransferAtFullNetworkSpeed(dataSpec, z)) {
                Assertions.checkState(this.streamCount > 0);
                long elapsedRealtime = this.clock.elapsedRealtime();
                int i = (int) (elapsedRealtime - this.sampleStartTimeMs);
                this.totalElapsedTimeMs += i;
                long j = this.totalBytesTransferred;
                long j2 = this.sampleBytesTransferred;
                this.totalBytesTransferred = j + j2;
                if (i > 0) {
                    this.slidingPercentile.addSample((int) Math.sqrt(j2), (j2 * 8000.0f) / i);
                    if (this.totalElapsedTimeMs < 2000) {
                        if (this.totalBytesTransferred >= 524288) {
                        }
                        maybeNotifyBandwidthSample(i, this.sampleBytesTransferred, this.bitrateEstimate);
                        this.sampleStartTimeMs = elapsedRealtime;
                        this.sampleBytesTransferred = 0L;
                    }
                    this.bitrateEstimate = (long) this.slidingPercentile.getPercentile(0.5f);
                    FileLog.d("debug_loading: bandwidth meter (onTransferEnd), bitrate estimate = " + this.bitrateEstimate);
                    maybeNotifyBandwidthSample(i, this.sampleBytesTransferred, this.bitrateEstimate);
                    this.sampleStartTimeMs = elapsedRealtime;
                    this.sampleBytesTransferred = 0L;
                }
                this.streamCount--;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public void onTransferInitializing(DataSource dataSource, DataSpec dataSpec, boolean z) {
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public synchronized void onTransferStart(DataSource dataSource, DataSpec dataSpec, boolean z) {
        try {
            if (isTransferAtFullNetworkSpeed(dataSpec, z)) {
                if (this.streamCount == 0) {
                    this.sampleStartTimeMs = this.clock.elapsedRealtime();
                }
                this.streamCount++;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.BandwidthMeter
    public void removeEventListener(BandwidthMeter.EventListener eventListener) {
        this.eventDispatcher.removeListener(eventListener);
    }
}
