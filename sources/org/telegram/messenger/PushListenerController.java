package org.telegram.messenger;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseBooleanArray;
import androidx.collection.LongSparseArray;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.PushListenerController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

/* loaded from: classes.dex */
public class PushListenerController {
    public static final int NOTIFICATION_ID = 1;
    public static final int PUSH_TYPE_FIREBASE = 2;
    public static final int PUSH_TYPE_HUAWEI = 13;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static final class GooglePushListenerServiceProvider implements IPushListenerServiceProvider {
        public static final GooglePushListenerServiceProvider INSTANCE = new GooglePushListenerServiceProvider();
        private Boolean hasServices;

        private GooglePushListenerServiceProvider() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRequestPushToken$0(Task task) {
            SharedConfig.pushStringGetTimeEnd = SystemClock.elapsedRealtime();
            if (task.isSuccessful()) {
                String str = (String) task.getResult();
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                PushListenerController.sendRegistrationToServer(getPushType(), str);
                return;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Failed to get regid");
            }
            SharedConfig.pushStringStatus = "__FIREBASE_FAILED__";
            PushListenerController.sendRegistrationToServer(getPushType(), null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRequestPushToken$1() {
            try {
                SharedConfig.pushStringGetTimeStart = SystemClock.elapsedRealtime();
                FirebaseApp.initializeApp(ApplicationLoader.applicationContext);
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener() { // from class: org.telegram.messenger.PushListenerController$GooglePushListenerServiceProvider$$ExternalSyntheticLambda0
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    public final void onComplete(Task task) {
                        PushListenerController.GooglePushListenerServiceProvider.this.lambda$onRequestPushToken$0(task);
                    }
                });
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public String getLogTitle() {
            return "Google Play Services";
        }

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public int getPushType() {
            return 2;
        }

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public boolean hasServices() {
            if (this.hasServices == null) {
                try {
                    this.hasServices = Boolean.valueOf(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ApplicationLoader.applicationContext) == 0);
                } catch (Exception e) {
                    FileLog.e(e);
                    this.hasServices = Boolean.FALSE;
                }
            }
            return this.hasServices.booleanValue();
        }

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public void onRequestPushToken() {
            String str;
            String str2 = SharedConfig.pushString;
            if (TextUtils.isEmpty(str2)) {
                if (BuildVars.LOGS_ENABLED) {
                    str = "FCM Registration not found.";
                    FileLog.d(str);
                }
            } else if (BuildVars.DEBUG_PRIVATE_VERSION && BuildVars.LOGS_ENABLED) {
                str = "FCM regId = " + str2;
                FileLog.d(str);
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$GooglePushListenerServiceProvider$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PushListenerController.GooglePushListenerServiceProvider.this.lambda$onRequestPushToken$1();
                }
            });
        }
    }

    public interface IPushListenerServiceProvider {
        String getLogTitle();

        int getPushType();

        boolean hasServices();

        void onRequestPushToken();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PushType {
    }

    private static String getReactedText(String str, Object[] objArr) {
        int i;
        str.hashCode();
        switch (str) {
            case "CHAT_REACT_CONTACT":
                i = R.string.PushChatReactContact;
                break;
            case "REACT_GEOLIVE":
                i = R.string.PushReactGeoLocation;
                break;
            case "REACT_STORY_HIDDEN":
                i = R.string.PushReactStoryHidden;
                break;
            case "REACT_HIDDEN":
                i = R.string.PushReactHidden;
                break;
            case "CHAT_REACT_NOTEXT":
                i = R.string.PushChatReactNotext;
                break;
            case "REACT_NOTEXT":
                i = R.string.PushReactNoText;
                break;
            case "CHAT_REACT_INVOICE":
                i = R.string.PushChatReactInvoice;
                break;
            case "REACT_CONTACT":
                i = R.string.PushReactContect;
                break;
            case "CHAT_REACT_STICKER":
                i = R.string.PushChatReactSticker;
                break;
            case "REACT_GAME":
                i = R.string.PushReactGame;
                break;
            case "REACT_POLL":
                i = R.string.PushReactPoll;
                break;
            case "REACT_QUIZ":
                i = R.string.PushReactQuiz;
                break;
            case "REACT_TEXT":
                i = R.string.PushReactText;
                break;
            case "REACT_INVOICE":
                i = R.string.PushReactInvoice;
                break;
            case "CHAT_REACT_DOC":
                i = R.string.PushChatReactDoc;
                break;
            case "CHAT_REACT_GEO":
                i = R.string.PushChatReactGeo;
                break;
            case "CHAT_REACT_GIF":
                i = R.string.PushChatReactGif;
                break;
            case "REACT_STICKER":
                i = R.string.PushReactSticker;
                break;
            case "CHAT_REACT_AUDIO":
                i = R.string.PushChatReactAudio;
                break;
            case "CHAT_REACT_PHOTO":
                i = R.string.PushChatReactPhoto;
                break;
            case "CHAT_REACT_ROUND":
                i = R.string.PushChatReactRound;
                break;
            case "CHAT_REACT_VIDEO":
                i = R.string.PushChatReactVideo;
                break;
            case "CHAT_REACT_GIVEAWAY":
                i = R.string.NotificationChatReactGiveaway;
                break;
            case "REACT_GIVEAWAY":
                i = R.string.NotificationReactGiveaway;
                break;
            case "CHAT_REACT_GEOLIVE":
                i = R.string.PushChatReactGeoLive;
                break;
            case "REACT_AUDIO":
                i = R.string.PushReactAudio;
                break;
            case "REACT_PHOTO":
                i = R.string.PushReactPhoto;
                break;
            case "REACT_ROUND":
                i = R.string.PushReactRound;
                break;
            case "REACT_STORY":
                i = R.string.PushReactStory;
                break;
            case "REACT_VIDEO":
                i = R.string.PushReactVideo;
                break;
            case "REACT_DOC":
                i = R.string.PushReactDoc;
                break;
            case "REACT_GEO":
                i = R.string.PushReactGeo;
                break;
            case "REACT_GIF":
                i = R.string.PushReactGif;
                break;
            case "CHAT_REACT_GAME":
                i = R.string.PushChatReactGame;
                break;
            case "CHAT_REACT_POLL":
                i = R.string.PushChatReactPoll;
                break;
            case "CHAT_REACT_QUIZ":
                i = R.string.PushChatReactQuiz;
                break;
            case "CHAT_REACT_TEXT":
                i = R.string.PushChatReactText;
                break;
            default:
                return null;
        }
        return LocaleController.formatString(i, objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$2(int i, TLRPC.TL_updates tL_updates) {
        MessagesController.getInstance(i).processUpdates(tL_updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$3(int i) {
        if (UserConfig.getInstance(i).getClientUserId() != 0) {
            UserConfig.getInstance(i).clearConfig();
            MessagesController.getInstance(i).performLogout(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$4(int i) {
        LocationController.getInstance(i).setNewLocationEndWatchTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:1569:0x05ac, code lost:
    
        if (org.telegram.messenger.MessagesStorage.getInstance(r5).checkMessageByRandomId(r14) == false) goto L254;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:276:0x0716. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:282:0x12e7. Please report as an issue. */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:1541:0x06dd  */
    /* JADX WARN: Removed duplicated region for block: B:1546:0x06ec A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:1550:0x0677  */
    /* JADX WARN: Removed duplicated region for block: B:1558:0x05e6 A[Catch: all -> 0x030b, TRY_ENTER, TRY_LEAVE, TryCatch #8 {all -> 0x030b, blocks: (B:1580:0x0304, B:140:0x033c, B:144:0x0355, B:150:0x036a, B:152:0x0372, B:158:0x0386, B:160:0x0395, B:163:0x03b8, B:164:0x03e5, B:165:0x03c5, B:167:0x03d0, B:168:0x03e3, B:169:0x03da, B:172:0x0405, B:176:0x041f, B:180:0x0439, B:181:0x044c, B:183:0x044f, B:185:0x045b, B:187:0x0478, B:188:0x049a, B:189:0x0521, B:192:0x04a8, B:193:0x04c2, B:195:0x04c5, B:197:0x04dd, B:199:0x04fb, B:205:0x053e, B:208:0x054c, B:210:0x0563, B:212:0x0579, B:213:0x0593, B:220:0x05bf, B:226:0x05d7, B:1558:0x05e6, B:1568:0x05a4), top: B:1579:0x0304 }] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x037a A[Catch: all -> 0x25e2, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x25e2, blocks: (B:135:0x02fe, B:138:0x0310, B:142:0x034f, B:155:0x037a, B:170:0x03fd, B:173:0x0413, B:177:0x042b, B:190:0x04a0, B:200:0x0526, B:202:0x052c, B:206:0x0544, B:217:0x05b5, B:223:0x05cd, B:1556:0x05e0, B:1571:0x0537), top: B:134:0x02fe }] */
    /* JADX WARN: Removed duplicated region for block: B:1560:0x05ee  */
    /* JADX WARN: Removed duplicated region for block: B:1561:0x05f1  */
    /* JADX WARN: Removed duplicated region for block: B:1562:0x05eb  */
    /* JADX WARN: Removed duplicated region for block: B:1566:0x059a  */
    /* JADX WARN: Removed duplicated region for block: B:1570:0x055e  */
    /* JADX WARN: Removed duplicated region for block: B:1615:0x267a A[Catch: all -> 0x2617, TryCatch #12 {all -> 0x2617, blocks: (B:115:0x260b, B:1611:0x261f, B:1613:0x2630, B:1615:0x267a, B:1617:0x2693, B:1619:0x2699), top: B:103:0x02ae }] */
    /* JADX WARN: Removed duplicated region for block: B:1637:0x01ff A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:208:0x054c A[Catch: all -> 0x030b, TRY_ENTER, TryCatch #8 {all -> 0x030b, blocks: (B:1580:0x0304, B:140:0x033c, B:144:0x0355, B:150:0x036a, B:152:0x0372, B:158:0x0386, B:160:0x0395, B:163:0x03b8, B:164:0x03e5, B:165:0x03c5, B:167:0x03d0, B:168:0x03e3, B:169:0x03da, B:172:0x0405, B:176:0x041f, B:180:0x0439, B:181:0x044c, B:183:0x044f, B:185:0x045b, B:187:0x0478, B:188:0x049a, B:189:0x0521, B:192:0x04a8, B:193:0x04c2, B:195:0x04c5, B:197:0x04dd, B:199:0x04fb, B:205:0x053e, B:208:0x054c, B:210:0x0563, B:212:0x0579, B:213:0x0593, B:220:0x05bf, B:226:0x05d7, B:1558:0x05e6, B:1568:0x05a4), top: B:1579:0x0304 }] */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0563 A[Catch: all -> 0x030b, TryCatch #8 {all -> 0x030b, blocks: (B:1580:0x0304, B:140:0x033c, B:144:0x0355, B:150:0x036a, B:152:0x0372, B:158:0x0386, B:160:0x0395, B:163:0x03b8, B:164:0x03e5, B:165:0x03c5, B:167:0x03d0, B:168:0x03e3, B:169:0x03da, B:172:0x0405, B:176:0x041f, B:180:0x0439, B:181:0x044c, B:183:0x044f, B:185:0x045b, B:187:0x0478, B:188:0x049a, B:189:0x0521, B:192:0x04a8, B:193:0x04c2, B:195:0x04c5, B:197:0x04dd, B:199:0x04fb, B:205:0x053e, B:208:0x054c, B:210:0x0563, B:212:0x0579, B:213:0x0593, B:220:0x05bf, B:226:0x05d7, B:1558:0x05e6, B:1568:0x05a4), top: B:1579:0x0304 }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x05f5  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x065b A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:263:0x0695 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0700 A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x2462 A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:293:0x248d A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x2560 A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:327:0x259b A[Catch: all -> 0x0633, TryCatch #10 {all -> 0x0633, blocks: (B:234:0x05ff, B:240:0x0621, B:242:0x0629, B:245:0x063a, B:247:0x0642, B:250:0x064f, B:252:0x065b, B:254:0x066c, B:257:0x067d, B:260:0x0681, B:261:0x0687, B:264:0x0697, B:266:0x069a, B:268:0x06a0, B:271:0x06fa, B:273:0x0700, B:275:0x0712, B:276:0x0716, B:283:0x12ea, B:285:0x12ee, B:289:0x245e, B:291:0x2462, B:293:0x248d, B:297:0x249d, B:300:0x24a9, B:302:0x24b4, B:304:0x24bd, B:305:0x24c4, B:307:0x24cc, B:308:0x24f7, B:310:0x2503, B:315:0x253b, B:317:0x2560, B:318:0x2574, B:320:0x257e, B:322:0x2586, B:325:0x2591, B:327:0x259b, B:331:0x25a9, B:332:0x25ec, B:341:0x2513, B:344:0x2523, B:345:0x252f, B:348:0x24de, B:349:0x24ea, B:351:0x25e7, B:352:0x1309, B:356:0x131d, B:359:0x132e, B:362:0x133b, B:365:0x134c, B:366:0x1352, B:369:0x135e, B:372:0x136a, B:375:0x137b, B:377:0x1383, B:380:0x1394, B:381:0x139a, B:384:0x13a6, B:387:0x13b2, B:390:0x13c3, B:392:0x13cb, B:395:0x13dc, B:396:0x13e2, B:399:0x13ee, B:402:0x13fa, B:405:0x140b, B:407:0x1413, B:410:0x1424, B:411:0x142a, B:414:0x1436, B:417:0x1442, B:420:0x1453, B:422:0x145b, B:425:0x146c, B:426:0x1472, B:429:0x147e, B:432:0x148a, B:435:0x149b, B:437:0x14a3, B:440:0x14b4, B:441:0x14ba, B:444:0x14c6, B:447:0x14d2, B:450:0x14e3, B:452:0x14eb, B:455:0x1503, B:456:0x1509, B:459:0x151a, B:462:0x1526, B:465:0x1537, B:467:0x153f, B:470:0x1557, B:471:0x155d, B:474:0x156e, B:475:0x1574, B:478:0x1580, B:481:0x158c, B:484:0x159d, B:486:0x15a5, B:489:0x15bd, B:490:0x15c3, B:493:0x15d4, B:496:0x15e0, B:499:0x15f1, B:501:0x15f9, B:504:0x160a, B:505:0x1610, B:508:0x161c, B:511:0x1628, B:513:0x162c, B:515:0x1634, B:518:0x1644, B:519:0x164a, B:522:0x1656, B:524:0x165e, B:526:0x1662, B:528:0x166a, B:531:0x1681, B:532:0x1687, B:535:0x1698, B:536:0x169e, B:538:0x16a2, B:540:0x16aa, B:543:0x16ba, B:544:0x16c0, B:547:0x16cc, B:550:0x16d8, B:553:0x16e9, B:555:0x16f1, B:558:0x1702, B:559:0x1708, B:562:0x1714, B:565:0x1720, B:568:0x1731, B:570:0x1739, B:573:0x174a, B:574:0x1750, B:577:0x175c, B:580:0x1768, B:583:0x1779, B:585:0x1781, B:588:0x1792, B:589:0x1798, B:592:0x17a4, B:595:0x17b0, B:598:0x17c1, B:600:0x17c9, B:603:0x17da, B:604:0x17e0, B:607:0x17ec, B:610:0x17f8, B:613:0x1809, B:615:0x1811, B:618:0x1822, B:619:0x1828, B:622:0x1834, B:625:0x1840, B:628:0x1851, B:630:0x1859, B:633:0x1871, B:634:0x1877, B:637:0x1888, B:638:0x188e, B:641:0x189f, B:643:0x18a5, B:646:0x18c9, B:647:0x18cf, B:650:0x18f6, B:651:0x18fc, B:654:0x1923, B:655:0x1929, B:658:0x1950, B:659:0x1956, B:662:0x197f, B:663:0x1985, B:666:0x1996, B:667:0x199c, B:670:0x19ad, B:671:0x19b3, B:674:0x19c4, B:675:0x19ca, B:678:0x19db, B:679:0x19e1, B:682:0x19f2, B:683:0x19f8, B:687:0x1a17, B:688:0x1a08, B:690:0x1a1d, B:693:0x1a2e, B:694:0x1a34, B:697:0x1a45, B:698:0x1a4b, B:701:0x1a63, B:702:0x1a69, B:705:0x1a7a, B:706:0x1a80, B:709:0x1a98, B:710:0x1a9e, B:713:0x1aaf, B:714:0x1ab5, B:717:0x1ac6, B:718:0x1acc, B:721:0x1add, B:722:0x1ae3, B:725:0x1afb, B:726:0x1aff, B:727:0x2430, B:730:0x1b03, B:733:0x1b21, B:734:0x1b27, B:737:0x1b3f, B:738:0x1b43, B:739:0x1b47, B:742:0x1b58, B:743:0x1b5c, B:744:0x1b60, B:747:0x1b71, B:748:0x1b75, B:749:0x1b79, B:752:0x1b8a, B:753:0x1b8e, B:754:0x1b92, B:757:0x1baa, B:758:0x1bae, B:759:0x1bb2, B:762:0x1bca, B:763:0x1bd2, B:766:0x1bea, B:767:0x1bee, B:768:0x1bf2, B:771:0x1c03, B:772:0x1c07, B:773:0x1c0b, B:775:0x1c0f, B:777:0x1c17, B:780:0x1c2f, B:781:0x1c48, B:782:0x2250, B:783:0x1c4d, B:786:0x1c61, B:787:0x1c79, B:790:0x1c8a, B:791:0x1c8e, B:792:0x1c92, B:795:0x1ca3, B:796:0x1ca7, B:797:0x1cab, B:800:0x1cbc, B:801:0x1cc0, B:802:0x1cc4, B:805:0x1cd5, B:806:0x1cd9, B:807:0x1cdd, B:810:0x1ce9, B:811:0x1ced, B:812:0x1cf1, B:815:0x1d02, B:816:0x1d06, B:817:0x1d0a, B:820:0x1d22, B:823:0x1d2f, B:824:0x1d37, B:827:0x1d59, B:828:0x1d5d, B:831:0x1d61, B:834:0x1d7f, B:835:0x1d84, B:838:0x1d90, B:839:0x1d96, B:842:0x1db5, B:843:0x1dbb, B:846:0x1ddc, B:847:0x1de2, B:850:0x1e03, B:851:0x1e09, B:854:0x1e2a, B:855:0x1e30, B:858:0x1e55, B:859:0x1e5b, B:862:0x1e67, B:863:0x1e6d, B:866:0x1e79, B:867:0x1e7f, B:870:0x1e8b, B:871:0x1e91, B:874:0x1e9d, B:875:0x1ea3, B:878:0x1eb4, B:879:0x1eba, B:882:0x1ecb, B:883:0x1ecf, B:884:0x1ed3, B:887:0x1ee4, B:888:0x1eea, B:891:0x1ef6, B:892:0x1efc, B:894:0x1f02, B:896:0x1f0a, B:899:0x1f1b, B:900:0x1f34, B:903:0x1f40, B:904:0x1f44, B:905:0x1f48, B:908:0x1f54, B:909:0x1f5a, B:912:0x1f66, B:913:0x1f6c, B:916:0x1f78, B:917:0x1f7e, B:920:0x1f8a, B:921:0x1f90, B:924:0x1f9c, B:925:0x1fa2, B:928:0x1fae, B:931:0x1fb7, B:932:0x1fbf, B:935:0x1fd7, B:938:0x1fdd, B:941:0x1ff5, B:942:0x1ffb, B:945:0x2007, B:948:0x2010, B:949:0x2018, B:952:0x2030, B:955:0x2036, B:958:0x204e, B:959:0x2054, B:962:0x2076, B:963:0x207c, B:966:0x209a, B:967:0x20a0, B:970:0x20c0, B:971:0x20c5, B:974:0x20e5, B:975:0x20ea, B:978:0x210a, B:979:0x210f, B:982:0x2131, B:983:0x213f, B:986:0x2150, B:987:0x2156, B:990:0x216e, B:991:0x2174, B:994:0x2185, B:995:0x218b, B:998:0x2197, B:999:0x219d, B:1002:0x21a9, B:1003:0x21af, B:1006:0x21bb, B:1007:0x21c1, B:1010:0x21d2, B:1011:0x21d8, B:1014:0x21e9, B:1015:0x21ef, B:1018:0x2200, B:1019:0x2206, B:1022:0x2212, B:1023:0x2218, B:1025:0x221e, B:1027:0x2226, B:1030:0x2237, B:1031:0x2256, B:1034:0x2262, B:1035:0x2268, B:1038:0x2274, B:1039:0x227a, B:1042:0x2286, B:1043:0x228c, B:1044:0x229c, B:1047:0x22a8, B:1048:0x22b0, B:1051:0x22bc, B:1052:0x22c2, B:1055:0x22ce, B:1056:0x22d6, B:1059:0x22e2, B:1060:0x22e8, B:1061:0x22f1, B:1064:0x22fd, B:1065:0x2303, B:1068:0x230f, B:1069:0x2315, B:1071:0x2323, B:1073:0x232d, B:1075:0x2335, B:1077:0x2343, B:1079:0x234d, B:1080:0x2352, B:1082:0x2367, B:1084:0x2377, B:1086:0x2389, B:1087:0x2394, B:1089:0x23a6, B:1090:0x23b1, B:1093:0x23c5, B:1094:0x23da, B:1097:0x23ec, B:1098:0x23f4, B:1101:0x2405, B:1102:0x240b, B:1105:0x2417, B:1106:0x241e, B:1109:0x242a, B:1110:0x2442, B:1112:0x244d, B:1117:0x0723, B:1121:0x0739, B:1124:0x074f, B:1127:0x0765, B:1130:0x077b, B:1133:0x0791, B:1136:0x07a7, B:1139:0x07bd, B:1142:0x07d3, B:1145:0x07e9, B:1148:0x07ff, B:1151:0x0815, B:1154:0x082b, B:1157:0x0841, B:1160:0x0857, B:1163:0x086d, B:1166:0x0883, B:1169:0x0899, B:1172:0x08af, B:1175:0x08c5, B:1178:0x08db, B:1181:0x08f1, B:1184:0x0907, B:1187:0x091d, B:1190:0x0933, B:1193:0x0949, B:1196:0x095f, B:1199:0x0975, B:1202:0x098b, B:1205:0x09a1, B:1208:0x09b7, B:1211:0x09cb, B:1214:0x09e1, B:1217:0x09f7, B:1220:0x0a0d, B:1223:0x0a23, B:1226:0x0a38, B:1229:0x0a4e, B:1232:0x0a64, B:1235:0x0a7a, B:1238:0x0a90, B:1241:0x0aa6, B:1244:0x0abc, B:1247:0x0ad2, B:1250:0x0ae8, B:1253:0x0afe, B:1256:0x0b14, B:1259:0x0b2a, B:1262:0x0b40, B:1265:0x0b56, B:1268:0x0b6c, B:1271:0x0b81, B:1274:0x0b97, B:1277:0x0bad, B:1280:0x0bc3, B:1283:0x0bd9, B:1286:0x0bef, B:1289:0x0c05, B:1292:0x0c1b, B:1295:0x0c31, B:1298:0x0c47, B:1301:0x0c5d, B:1304:0x0c73, B:1307:0x0c89, B:1310:0x0c9f, B:1313:0x0cb5, B:1316:0x0ccb, B:1319:0x0ce1, B:1322:0x0cf7, B:1325:0x0d0d, B:1328:0x0d23, B:1331:0x0d39, B:1334:0x0d4f, B:1337:0x0d65, B:1340:0x0d7b, B:1343:0x0d91, B:1346:0x0da7, B:1349:0x0dbd, B:1352:0x0dd3, B:1355:0x0de9, B:1358:0x0dff, B:1361:0x0e15, B:1364:0x0e2b, B:1367:0x0e41, B:1370:0x0e57, B:1373:0x0e6d, B:1376:0x0e83, B:1379:0x0e97, B:1382:0x0ead, B:1385:0x0ec3, B:1388:0x0ed7, B:1391:0x0eed, B:1394:0x0f03, B:1397:0x0f19, B:1400:0x0f2f, B:1403:0x0f45, B:1406:0x0f5b, B:1409:0x0f71, B:1412:0x0f83, B:1416:0x0f9d, B:1419:0x0fb3, B:1422:0x0fc9, B:1425:0x0fdf, B:1428:0x0ff5, B:1431:0x100b, B:1434:0x1020, B:1437:0x1036, B:1440:0x104c, B:1443:0x1062, B:1446:0x1078, B:1449:0x108e, B:1452:0x10a4, B:1455:0x10ba, B:1458:0x10d0, B:1461:0x10e6, B:1464:0x10fc, B:1467:0x1112, B:1470:0x1128, B:1473:0x113c, B:1476:0x1152, B:1479:0x1164, B:1482:0x1178, B:1485:0x118e, B:1488:0x11a4, B:1491:0x11ba, B:1494:0x11d0, B:1497:0x11e6, B:1500:0x11fc, B:1503:0x1212, B:1506:0x1228, B:1509:0x123e, B:1512:0x1254, B:1515:0x126a, B:1518:0x127f, B:1521:0x1294, B:1524:0x12a9, B:1527:0x12be, B:1531:0x2458, B:1536:0x06ca, B:1539:0x06d5, B:1546:0x06ec), top: B:233:0x05ff }] */
    /* JADX WARN: Removed duplicated region for block: B:336:0x25a6  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x26d9  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x26f0  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x26e9  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01ec A[Catch: all -> 0x0197, TRY_ENTER, TryCatch #15 {all -> 0x0197, blocks: (B:1654:0x0192, B:56:0x01aa, B:58:0x01b3, B:63:0x01ec, B:69:0x0203, B:71:0x0207, B:72:0x021b, B:65:0x01fb, B:1642:0x01be, B:1645:0x01c9, B:1646:0x01d6, B:1650:0x01d0), top: B:1653:0x0192 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0203 A[Catch: all -> 0x0197, TryCatch #15 {all -> 0x0197, blocks: (B:1654:0x0192, B:56:0x01aa, B:58:0x01b3, B:63:0x01ec, B:69:0x0203, B:71:0x0207, B:72:0x021b, B:65:0x01fb, B:1642:0x01be, B:1645:0x01c9, B:1646:0x01d6, B:1650:0x01d0), top: B:1653:0x0192 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0221 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$processRemoteMessage$5(String str, String str2, long j) {
        String str3;
        String str4;
        String str5;
        Throwable th;
        String str6;
        String str7;
        int i;
        int i2;
        String str8;
        String str9;
        Object obj;
        String str10;
        String str11;
        JSONObject jSONObject;
        UserConfig userConfig;
        Integer parseInt;
        long intValue;
        int i3;
        int i4;
        int i5;
        boolean z;
        char c;
        int i6;
        long j2;
        long j3;
        long j4;
        int i7;
        long j5;
        int i8;
        long j6;
        JSONObject jSONObject2;
        boolean z2;
        int i9;
        String str12;
        String str13;
        long j7;
        int i10;
        String str14;
        String str15;
        boolean z3;
        boolean z4;
        Object obj2;
        int i11;
        boolean z5;
        JSONObject jSONObject3;
        long j8;
        String[] strArr;
        String str16;
        String str17;
        boolean z6;
        boolean z7;
        boolean z8;
        String str18;
        String str19;
        boolean z9;
        String str20;
        String str21;
        String reactedText;
        String str22;
        boolean z10;
        int i12;
        long j9;
        boolean z11;
        Object obj3;
        boolean z12;
        Object obj4;
        Object obj5;
        String string;
        String formatString;
        int i13;
        String str23;
        String str24;
        String formatPluralString;
        String formatPluralString2;
        String formatPluralString3;
        StringBuilder sb;
        String string2;
        String formatString2;
        int i14;
        int i15;
        int i16;
        String str25;
        StringBuilder sb2;
        TLRPC.TL_updateReadHistoryInbox tL_updateReadHistoryInbox;
        str3 = "";
        if (BuildVars.LOGS_ENABLED) {
            str4 = "Photos";
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            str5 = "Videos";
            sb3.append(" START PROCESSING");
            FileLog.d(sb3.toString());
        } else {
            str4 = "Photos";
            str5 = "Videos";
        }
        String str26 = null;
        try {
            byte[] decode = Base64.decode(str2, 8);
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(decode.length);
            nativeByteBuffer.writeBytes(decode);
            nativeByteBuffer.position(0);
            if (SharedConfig.pushAuthKeyId == null) {
                obj = "STORY_NOTEXT";
                SharedConfig.pushAuthKeyId = new byte[8];
                byte[] computeSHA1 = Utilities.computeSHA1(SharedConfig.pushAuthKey);
                str10 = "REACT_";
                str8 = "schedule";
                str9 = "encryption_id";
                System.arraycopy(computeSHA1, computeSHA1.length - 8, SharedConfig.pushAuthKeyId, 0, 8);
            } else {
                str8 = "schedule";
                str9 = "encryption_id";
                obj = "STORY_NOTEXT";
                str10 = "REACT_";
            }
            byte[] bArr = new byte[8];
            nativeByteBuffer.readBytes(bArr, true);
            if (!Arrays.equals(SharedConfig.pushAuthKeyId, bArr)) {
                onDecryptError();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d(String.format(Locale.US, str + " DECRYPT ERROR 2 k1=%s k2=%s, key=%s", Utilities.bytesToHex(SharedConfig.pushAuthKeyId), Utilities.bytesToHex(bArr), Utilities.bytesToHex(SharedConfig.pushAuthKey)));
                    return;
                }
                return;
            }
            byte[] bArr2 = new byte[16];
            nativeByteBuffer.readBytes(bArr2, true);
            MessageKeyData generateMessageKeyData = MessageKeyData.generateMessageKeyData(SharedConfig.pushAuthKey, bArr2, true, 2);
            char c2 = 24;
            Utilities.aesIgeEncryption(nativeByteBuffer.buffer, generateMessageKeyData.aesKey, generateMessageKeyData.aesIv, false, false, 24, decode.length - 24);
            byte[] bArr3 = SharedConfig.pushAuthKey;
            ByteBuffer byteBuffer = nativeByteBuffer.buffer;
            if (!Utilities.arraysEquals(bArr2, 0, Utilities.computeSHA256(bArr3, 96, 32, byteBuffer, 24, byteBuffer.limit()), 8)) {
                onDecryptError();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d(String.format(str + " DECRYPT ERROR 3, key = %s", Utilities.bytesToHex(SharedConfig.pushAuthKey)));
                    return;
                }
                return;
            }
            byte[] bArr4 = new byte[nativeByteBuffer.readInt32(true)];
            nativeByteBuffer.readBytes(bArr4, true);
            String str27 = new String(bArr4);
            try {
                JSONObject jSONObject4 = new JSONObject(str27);
                ApplicationLoader applicationLoader = ApplicationLoader.applicationLoaderInstance;
                if (applicationLoader != null) {
                    try {
                        if (applicationLoader.consumePush(-1, jSONObject4)) {
                            countDownLatch.countDown();
                            return;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        str11 = str27;
                        str6 = str11;
                        str7 = str26;
                        i2 = -1;
                        i = -1;
                        if (i == i2) {
                        }
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        FileLog.e(th);
                        return;
                    }
                }
                str7 = jSONObject4.has("loc_key") ? jSONObject4.getString("loc_key") : str3;
                try {
                    if (jSONObject4.get("custom") instanceof JSONObject) {
                        try {
                            jSONObject = jSONObject4.getJSONObject("custom");
                        } catch (Throwable th3) {
                            th = th3;
                            str11 = str27;
                            str26 = str7;
                            str6 = str11;
                            str7 = str26;
                            i2 = -1;
                            i = -1;
                            if (i == i2) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            FileLog.e(th);
                            return;
                        }
                    } else {
                        jSONObject = new JSONObject();
                    }
                    Object obj6 = jSONObject4.has("user_id") ? jSONObject4.get("user_id") : null;
                    if (obj6 == null) {
                        userConfig = UserConfig.getInstance(UserConfig.selectedAccount);
                    } else {
                        if (obj6 instanceof Long) {
                            intValue = ((Long) obj6).longValue();
                        } else {
                            if (obj6 instanceof Integer) {
                                parseInt = (Integer) obj6;
                            } else if (obj6 instanceof String) {
                                parseInt = Utilities.parseInt((CharSequence) obj6);
                            } else {
                                userConfig = UserConfig.getInstance(UserConfig.selectedAccount);
                            }
                            intValue = parseInt.intValue();
                        }
                        int i17 = UserConfig.selectedAccount;
                        i3 = 4;
                        i4 = 0;
                        while (true) {
                            if (i4 < i3) {
                                i5 = i17;
                                z = false;
                                break;
                            } else if (UserConfig.getInstance(i4).getClientUserId() == intValue) {
                                i5 = i4;
                                z = true;
                                break;
                            } else {
                                i4++;
                                i3 = 4;
                            }
                        }
                        if (z) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d(str + " ACCOUNT NOT FOUND");
                            }
                            countDownLatch.countDown();
                            return;
                        }
                        try {
                            try {
                                if (!UserConfig.getInstance(i5).isClientActivated()) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d(str + " ACCOUNT NOT ACTIVATED");
                                    }
                                    countDownLatch.countDown();
                                    return;
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d(str + " " + str7);
                                }
                                int hashCode = str7.hashCode();
                                if (hashCode == -1963663249) {
                                    if (str7.equals("SESSION_REVOKE")) {
                                        c = 2;
                                        if (c != 0) {
                                        }
                                    }
                                    c = 65535;
                                    if (c != 0) {
                                    }
                                } else if (hashCode == -920689527) {
                                    if (str7.equals("DC_UPDATE")) {
                                        c = 0;
                                        if (c != 0) {
                                        }
                                    }
                                    c = 65535;
                                    if (c != 0) {
                                    }
                                } else if (hashCode != 633004703) {
                                    try {
                                        if (hashCode == 1365673842 && str7.equals("GEO_LIVE_PENDING")) {
                                            c = 3;
                                            if (c != 0) {
                                                int i18 = i5;
                                                int i19 = jSONObject.getInt("dc");
                                                String[] split = jSONObject.getString("addr").split(":");
                                                if (split.length != 2) {
                                                    countDownLatch.countDown();
                                                    return;
                                                }
                                                ConnectionsManager.getInstance(i18).applyDatacenterAddress(i19, split[0], Integer.parseInt(split[1]));
                                                ConnectionsManager.getInstance(i18).resumeNetworkMaybe();
                                                countDownLatch.countDown();
                                                return;
                                            }
                                            if (c == 1) {
                                                final int i20 = i5;
                                                TLRPC.TL_updateServiceNotification tL_updateServiceNotification = new TLRPC.TL_updateServiceNotification();
                                                tL_updateServiceNotification.popup = false;
                                                tL_updateServiceNotification.flags = 2;
                                                tL_updateServiceNotification.inbox_date = (int) (j / 1000);
                                                tL_updateServiceNotification.message = jSONObject4.getString("message");
                                                tL_updateServiceNotification.type = "announcement";
                                                tL_updateServiceNotification.media = new TLRPC.TL_messageMediaEmpty();
                                                final TLRPC.TL_updates tL_updates = new TLRPC.TL_updates();
                                                tL_updates.updates.add(tL_updateServiceNotification);
                                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda4
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        PushListenerController.lambda$processRemoteMessage$2(i20, tL_updates);
                                                    }
                                                });
                                                ConnectionsManager.getInstance(i20).resumeNetworkMaybe();
                                                countDownLatch.countDown();
                                                return;
                                            }
                                            if (c == 2) {
                                                final int i21 = i5;
                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda3
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        PushListenerController.lambda$processRemoteMessage$3(i21);
                                                    }
                                                });
                                                countDownLatch.countDown();
                                                return;
                                            }
                                            if (c == 3) {
                                                str2 = str27;
                                                final int i22 = i5;
                                                try {
                                                    Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda2
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            PushListenerController.lambda$processRemoteMessage$4(i22);
                                                        }
                                                    });
                                                    countDownLatch.countDown();
                                                    return;
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    i4 = i22;
                                                    th = th;
                                                    str6 = str2;
                                                    i = i4;
                                                    i2 = -1;
                                                    if (i == i2) {
                                                    }
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    FileLog.e(th);
                                                    return;
                                                }
                                            }
                                            try {
                                                if (jSONObject.has("channel_id")) {
                                                    try {
                                                        j2 = jSONObject.getLong("channel_id");
                                                        str2 = str27;
                                                        j3 = -j2;
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                        str2 = str27;
                                                        th = th;
                                                        i4 = i5;
                                                        str6 = str2;
                                                        i = i4;
                                                        i2 = -1;
                                                        if (i == i2) {
                                                            ConnectionsManager.onInternalPushReceived(i);
                                                            ConnectionsManager.getInstance(i).resumeNetworkMaybe();
                                                            countDownLatch.countDown();
                                                        } else {
                                                            onDecryptError();
                                                        }
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.e("error in loc_key = " + str7 + " json " + str6);
                                                        }
                                                        FileLog.e(th);
                                                        return;
                                                    }
                                                } else {
                                                    str2 = str27;
                                                    j3 = 0;
                                                    j2 = 0;
                                                }
                                                try {
                                                    if (jSONObject.has("from_id")) {
                                                        try {
                                                            j3 = jSONObject.getLong("from_id");
                                                            j4 = j3;
                                                        } catch (Throwable th6) {
                                                            th = th6;
                                                            th = th;
                                                            i4 = i5;
                                                            str6 = str2;
                                                            i = i4;
                                                            i2 = -1;
                                                            if (i == i2) {
                                                            }
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            FileLog.e(th);
                                                            return;
                                                        }
                                                    } else {
                                                        j4 = 0;
                                                    }
                                                    if (jSONObject.has("chat_id")) {
                                                        try {
                                                            long j10 = jSONObject.getLong("chat_id");
                                                            i7 = i5;
                                                            j3 = -j10;
                                                            j5 = j10;
                                                        } catch (Throwable th7) {
                                                            th = th7;
                                                            i7 = i5;
                                                            th = th;
                                                            i4 = i7;
                                                            str6 = str2;
                                                            i = i4;
                                                            i2 = -1;
                                                            if (i == i2) {
                                                            }
                                                            if (BuildVars.LOGS_ENABLED) {
                                                            }
                                                            FileLog.e(th);
                                                            return;
                                                        }
                                                    } else {
                                                        i7 = i5;
                                                        j5 = 0;
                                                    }
                                                    try {
                                                        if (jSONObject.has("topic_id")) {
                                                            try {
                                                                i8 = jSONObject.getInt("topic_id");
                                                                j6 = j3;
                                                            } catch (Throwable th8) {
                                                                th = th8;
                                                                th = th;
                                                                i4 = i7;
                                                                str6 = str2;
                                                                i = i4;
                                                                i2 = -1;
                                                                if (i == i2) {
                                                                }
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                }
                                                                FileLog.e(th);
                                                                return;
                                                            }
                                                        } else {
                                                            j6 = j3;
                                                            i8 = 0;
                                                        }
                                                        FileLog.d("recived push notification {" + str7 + "} chatId " + j5 + " custom topicId " + i8);
                                                        long makeEncryptedDialogId = jSONObject.has(str9) ? DialogObject.makeEncryptedDialogId(jSONObject.getInt(r2)) : j6;
                                                        String str28 = str8;
                                                        if (jSONObject.has(str28)) {
                                                            jSONObject2 = jSONObject4;
                                                            if (jSONObject.getInt(str28) == 1) {
                                                                z2 = true;
                                                                if (makeEncryptedDialogId == 0 && "ENCRYPTED_MESSAGE".equals(str7)) {
                                                                    makeEncryptedDialogId = NotificationsController.globalSecretChatId;
                                                                }
                                                                if (makeEncryptedDialogId != 0) {
                                                                    boolean z13 = z2;
                                                                    if ("READ_HISTORY".equals(str7)) {
                                                                        int i23 = jSONObject.getInt("max_id");
                                                                        ArrayList<TLRPC.Update> arrayList = new ArrayList<>();
                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                            FileLog.d(str + " received read notification max_id = " + i23 + " for dialogId = " + makeEncryptedDialogId);
                                                                        }
                                                                        if (j2 != 0) {
                                                                            TLRPC.TL_updateReadChannelInbox tL_updateReadChannelInbox = new TLRPC.TL_updateReadChannelInbox();
                                                                            tL_updateReadChannelInbox.channel_id = j2;
                                                                            tL_updateReadChannelInbox.max_id = i23;
                                                                            tL_updateReadChannelInbox.still_unread_count = 0;
                                                                            tL_updateReadHistoryInbox = tL_updateReadChannelInbox;
                                                                        } else {
                                                                            TLRPC.TL_updateReadHistoryInbox tL_updateReadHistoryInbox2 = new TLRPC.TL_updateReadHistoryInbox();
                                                                            if (j4 != 0) {
                                                                                TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                                                                                tL_updateReadHistoryInbox2.peer = tL_peerUser;
                                                                                tL_peerUser.user_id = j4;
                                                                            } else {
                                                                                TLRPC.TL_peerChat tL_peerChat = new TLRPC.TL_peerChat();
                                                                                tL_updateReadHistoryInbox2.peer = tL_peerChat;
                                                                                tL_peerChat.chat_id = j5;
                                                                            }
                                                                            tL_updateReadHistoryInbox2.max_id = i23;
                                                                            tL_updateReadHistoryInbox = tL_updateReadHistoryInbox2;
                                                                        }
                                                                        arrayList.add(tL_updateReadHistoryInbox);
                                                                        MessagesController.getInstance(i7).processUpdateArray(arrayList, null, null, false, 0);
                                                                    } else if ("READ_STORIES".equals(str7)) {
                                                                        NotificationsController.getInstance(i7).processReadStories(makeEncryptedDialogId, jSONObject.getInt("max_id"));
                                                                    } else {
                                                                        long j11 = j4;
                                                                        if (!"STORY_DELETED".equals(str7)) {
                                                                            long j12 = j5;
                                                                            if ("MESSAGE_DELETED".equals(str7)) {
                                                                                String[] split2 = jSONObject.getString("messages").split(",");
                                                                                LongSparseArray longSparseArray = new LongSparseArray();
                                                                                ArrayList<Integer> arrayList2 = new ArrayList<>();
                                                                                for (String str29 : split2) {
                                                                                    arrayList2.add(Utilities.parseInt((CharSequence) str29));
                                                                                }
                                                                                longSparseArray.put(-j2, arrayList2);
                                                                                NotificationsController.getInstance(i7).removeDeletedMessagesFromNotifications(longSparseArray, false);
                                                                                MessagesController.getInstance(i7).deleteMessagesByPush(makeEncryptedDialogId, arrayList2, j2);
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                    sb2 = new StringBuilder();
                                                                                    sb2.append(str);
                                                                                    sb2.append(" received ");
                                                                                    sb2.append(str7);
                                                                                    sb2.append(" for dialogId = ");
                                                                                    sb2.append(makeEncryptedDialogId);
                                                                                    sb2.append(" mids = ");
                                                                                    sb2.append(TextUtils.join(",", arrayList2));
                                                                                    FileLog.d(sb2.toString());
                                                                                }
                                                                            } else if ("READ_REACTION".equals(str7)) {
                                                                                String[] split3 = jSONObject.getString("messages").split(",");
                                                                                LongSparseArray longSparseArray2 = new LongSparseArray();
                                                                                ArrayList arrayList3 = new ArrayList();
                                                                                SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                                                                                int i24 = 0;
                                                                                while (i24 < split3.length) {
                                                                                    Integer parseInt2 = Utilities.parseInt((CharSequence) split3[i24]);
                                                                                    String[] strArr2 = split3;
                                                                                    int intValue2 = parseInt2.intValue();
                                                                                    arrayList3.add(parseInt2);
                                                                                    sparseBooleanArray.put(intValue2, false);
                                                                                    i24++;
                                                                                    split3 = strArr2;
                                                                                }
                                                                                longSparseArray2.put(-j2, arrayList3);
                                                                                NotificationsController.getInstance(i7).removeDeletedMessagesFromNotifications(longSparseArray2, true);
                                                                                MessagesController.getInstance(i7).checkUnreadReactions(makeEncryptedDialogId, i8, sparseBooleanArray);
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                    sb2 = new StringBuilder();
                                                                                    sb2.append(str);
                                                                                    sb2.append(" received ");
                                                                                    sb2.append(str7);
                                                                                    sb2.append(" for dialogId = ");
                                                                                    sb2.append(makeEncryptedDialogId);
                                                                                    sb2.append(" mids = ");
                                                                                    sb2.append(TextUtils.join(",", arrayList3));
                                                                                    FileLog.d(sb2.toString());
                                                                                }
                                                                            } else if (!TextUtils.isEmpty(str7)) {
                                                                                if (jSONObject.has("msg_id")) {
                                                                                    str12 = "msg_id";
                                                                                } else if (jSONObject.has("story_id")) {
                                                                                    str12 = "story_id";
                                                                                } else {
                                                                                    i9 = 0;
                                                                                    if (jSONObject.has("random_id")) {
                                                                                        str13 = "messages";
                                                                                        j7 = 0;
                                                                                    } else {
                                                                                        str13 = "messages";
                                                                                        j7 = Utilities.parseLong(jSONObject.getString("random_id")).longValue();
                                                                                    }
                                                                                    if (i9 == 0) {
                                                                                        i10 = i8;
                                                                                        str14 = " for dialogId = ";
                                                                                        Integer num = MessagesController.getInstance(i7).dialogs_read_inbox_max.get(Long.valueOf(makeEncryptedDialogId));
                                                                                        if (num == null) {
                                                                                            num = Integer.valueOf(MessagesStorage.getInstance(i7).getDialogReadMax(false, makeEncryptedDialogId));
                                                                                            MessagesController.getInstance(i7).dialogs_read_inbox_max.put(Long.valueOf(makeEncryptedDialogId), num);
                                                                                        }
                                                                                        if (i9 > num.intValue()) {
                                                                                            str15 = str10;
                                                                                            z3 = true;
                                                                                        }
                                                                                        str15 = str10;
                                                                                        z3 = false;
                                                                                    } else {
                                                                                        i10 = i8;
                                                                                        str14 = " for dialogId = ";
                                                                                        if (j7 != 0) {
                                                                                        }
                                                                                        str15 = str10;
                                                                                        z3 = false;
                                                                                    }
                                                                                    z4 = z3;
                                                                                    if (!str7.startsWith(str15) || str7.startsWith("CHAT_REACT_")) {
                                                                                        obj2 = obj;
                                                                                        z4 = true;
                                                                                    } else {
                                                                                        obj2 = obj;
                                                                                    }
                                                                                    int i25 = i9;
                                                                                    if (!str7.equals(obj2) || str7.equals("STORY_HIDDEN_AUTHOR")) {
                                                                                        i11 = !jSONObject.has("story_id") ? jSONObject.getInt("story_id") : -1;
                                                                                        z4 = i11 < 0;
                                                                                    } else {
                                                                                        i11 = -1;
                                                                                    }
                                                                                    if (z4) {
                                                                                        int i26 = i11;
                                                                                        i6 = i7;
                                                                                        long j13 = j7;
                                                                                        try {
                                                                                            long optLong = jSONObject.optLong("chat_from_id", 0L);
                                                                                            String str30 = str13;
                                                                                            Object obj7 = "STORY_HIDDEN_AUTHOR";
                                                                                            Object obj8 = obj2;
                                                                                            long optLong2 = jSONObject.optLong("chat_from_broadcast_id", 0L);
                                                                                            long optLong3 = jSONObject.optLong("chat_from_group_id", 0L);
                                                                                            if (optLong == 0 && optLong3 == 0) {
                                                                                                z5 = false;
                                                                                                boolean z14 = (jSONObject.has("mention") || jSONObject.getInt("mention") == 0) ? false : true;
                                                                                                boolean z15 = (jSONObject.has("silent") || jSONObject.getInt("silent") == 0) ? false : true;
                                                                                                boolean z16 = z14;
                                                                                                jSONObject3 = jSONObject2;
                                                                                                if (jSONObject3.has("loc_args")) {
                                                                                                    j8 = optLong;
                                                                                                    strArr = null;
                                                                                                } else {
                                                                                                    JSONArray jSONArray = jSONObject3.getJSONArray("loc_args");
                                                                                                    int length = jSONArray.length();
                                                                                                    j8 = optLong;
                                                                                                    strArr = new String[length];
                                                                                                    for (int i27 = 0; i27 < length; i27++) {
                                                                                                        strArr[i27] = jSONArray.getString(i27);
                                                                                                    }
                                                                                                }
                                                                                                if (strArr != null && strArr.length > 0) {
                                                                                                    str16 = strArr[0];
                                                                                                    boolean has = jSONObject.has("edit_date");
                                                                                                    if (str7.startsWith("CHAT_") || strArr == null || strArr.length <= 0) {
                                                                                                        if (str7.startsWith("PINNED_")) {
                                                                                                            if (str7.startsWith("CHANNEL_")) {
                                                                                                                str17 = null;
                                                                                                                z6 = false;
                                                                                                                z7 = true;
                                                                                                                z8 = false;
                                                                                                                if (str7.startsWith(str15)) {
                                                                                                                }
                                                                                                                str18 = "CHAT_REACT_";
                                                                                                                str19 = str15;
                                                                                                                z9 = has;
                                                                                                                str20 = str17;
                                                                                                                str21 = str16;
                                                                                                                reactedText = getReactedText(str7, strArr);
                                                                                                                str22 = reactedText;
                                                                                                                i12 = i25;
                                                                                                                z10 = false;
                                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                                }
                                                                                                                if (str22 != null) {
                                                                                                                }
                                                                                                            }
                                                                                                            str17 = null;
                                                                                                            z6 = false;
                                                                                                            z7 = false;
                                                                                                            z8 = false;
                                                                                                            if (str7.startsWith(str15)) {
                                                                                                            }
                                                                                                            str18 = "CHAT_REACT_";
                                                                                                            str19 = str15;
                                                                                                            z9 = has;
                                                                                                            str20 = str17;
                                                                                                            str21 = str16;
                                                                                                            reactedText = getReactedText(str7, strArr);
                                                                                                            str22 = reactedText;
                                                                                                            i12 = i25;
                                                                                                            z10 = false;
                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                            }
                                                                                                            if (str22 != null) {
                                                                                                            }
                                                                                                        } else {
                                                                                                            z8 = j2 != 0;
                                                                                                            str17 = null;
                                                                                                            z6 = true;
                                                                                                            z7 = false;
                                                                                                            if (!str7.startsWith(str15) || str7.startsWith("CHAT_REACT_")) {
                                                                                                                str18 = "CHAT_REACT_";
                                                                                                                str19 = str15;
                                                                                                                z9 = has;
                                                                                                                str20 = str17;
                                                                                                                str21 = str16;
                                                                                                                reactedText = getReactedText(str7, strArr);
                                                                                                            } else {
                                                                                                                switch (str7.hashCode()) {
                                                                                                                    case -2104766184:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals(obj4)) {
                                                                                                                            c2 = 0;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -2100047043:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_GAME_SCORE")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = ' ';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -2091498420:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_CONTACT")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '6';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -2053872415:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_CREATED")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'W';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -2039746363:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_STICKER")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 23;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -2023218804:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_VIDEOS")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '?';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1979538588:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_DOC")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '3';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1979536003:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GEO")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '9';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1979535888:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GIF")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = ';';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1969004705:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_ADD_MEMBER")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '[';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1946699248:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_JOINED")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'd';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1833440864:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GIVEAWAY_STARS")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'D';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1717283471:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_REQ_JOINED")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'e';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1646640058:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_VOICECHAT_START")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '\\';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1633328296:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("PINNED_PAID_MEDIA")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 11;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1528047021:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_MESSAGES")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'k';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1507149394:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_RECURRING_PAY")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 4;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1493579426:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_AUDIO")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1482481933:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_MUTED")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 135;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1480102982:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_PHOTO")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 16;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1478041834:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_ROUND")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 21;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1476974979:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_STORY")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 14;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1474543101:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_VIDEO")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 18;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1465695932:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("ENCRYPTION_ACCEPT")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 133;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1374906292:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("ENCRYPTED_MESSAGE")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '~';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1372940586:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_RETURNED")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'c';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1264245338:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("PINNED_INVOICE")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '|';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1236154001:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_DOCS")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'A';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1236086700:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_FWDS")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '=';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1236077786:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GAME")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '<';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1235796237:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_POLL")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '8';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1235760759:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_QUIZ")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = '7';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1235686303:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_TEXT")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 6;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1198046100:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("MESSAGE_VIDEO_SECRET")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 19;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1124254527:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_CONTACT")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'N';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1085137927:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("PINNED_GAME")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'z';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1084856378:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("PINNED_POLL")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'w';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1084820900:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("PINNED_QUIZ")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'v';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -1084746444:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj5 = obj7;
                                                                                                                        if (str7.equals("PINNED_TEXT")) {
                                                                                                                            obj4 = obj8;
                                                                                                                            c2 = 'l';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj4 = obj8;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -891852842:
                                                                                                                        str20 = str17;
                                                                                                                        obj5 = obj7;
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        if (str7.equals(obj5)) {
                                                                                                                            c2 = 1;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -819729482:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_STICKER")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'r';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -772141857:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PHONE_CALL_REQUEST")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 134;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -638310039:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_STICKER")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '4';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -590403924:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_GAME_SCORE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '{';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -589196239:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_DOC")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'q';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -589193654:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_GEO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'x';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -589193539:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_GIF")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '}';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -455004278:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_WALLPAPER")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 3;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -440169325:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("AUTH_UNKNOWN")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 129;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -412748110:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_DELETE_YOU")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'a';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -242433887:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_SAME_WALLPAPER")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 2;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -228518075:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GEOLIVE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 29;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -213586509:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("ENCRYPTION_REQUEST")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 132;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -115582002:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_INVOICE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'V';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -112621464:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CONTACT_JOINED")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 128;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -108522133:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("AUTH_REGION")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 130;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -107572034:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_SCREENSHOT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 20;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -40534265:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_DELETE_MEMBER")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '`';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case -35560251:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_PAID_MEDIA")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\t';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 52369421:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("REACT_TEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 127;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 65254746:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_ADD_YOU")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'X';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 120441350:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_GIVEAWAY")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'u';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 141040782:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_LEFT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'b';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 191667248:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_PAID_MEDIA")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\n';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 202550149:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_VOICECHAT_INVITE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = ']';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 309993049:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_DOC")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'K';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 309995634:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GEO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'Q';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 309995749:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GIF")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'S';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 320532812:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGES")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '+';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 328933854:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_STICKER")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'L';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 331340546:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_AUDIO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '5';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 342406591:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_VOICECHAT_END")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '^';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 344816990:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_PHOTO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '0';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 346878138:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_ROUND")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '2';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 347944993:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_STORY")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '/';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 350376871:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_VIDEO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '1';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 510462069:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GIFTCODE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '(';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 608430149:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_VOICECHAT_INVITE_YOU")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '_';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 615714517:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_PHOTO_SECRET")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 17;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 715508879:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_AUDIO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 's';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 728985323:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_PHOTO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'n';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 731046471:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_ROUND")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'p';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 734545204:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_VIDEO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'o';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 802032552:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_CONTACT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 25;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 901537717:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GIVEAWAY_STARS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '-';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 954623703:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GIVEAWAY")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = ')';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 977076186:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_STARGIFT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 7;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 991498806:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_GEOLIVE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'y';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1007364121:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GAME_SCORE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '!';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1019850010:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_DOCS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'j';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1019917311:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_FWDS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'f';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1019926225:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GAME")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'T';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1020207774:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_POLL")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'P';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1020243252:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_QUIZ")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'O';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1020317708:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_TEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'E';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1054583304:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_STORY_MENTION")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 15;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1060282259:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_DOCS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\'';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1060349560:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_FWDS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '#';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1060358474:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GAME")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 31;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1060640023:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_POLL")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 27;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1060675501:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_QUIZ")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 26;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1060749957:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_TEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 5;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1073049781:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_NOTEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'm';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1078101399:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_TITLE_EDITED")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'Y';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1110103437:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_NOTEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'F';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1144183001:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GIVEAWAY_STARS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '*';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1151995881:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_PAID_MEDIA")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\b';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1160762272:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_PHOTOS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'g';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1172918249:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GEOLIVE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = ':';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1234591620:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GAME_SCORE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'U';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1281128640:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_DOC")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 22;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1281131225:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GEO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 28;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1281131340:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_GIF")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 30;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1310789062:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_NOTEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\r';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1333118583:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_VIDEOS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'h';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1361447897:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_PHOTOS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '$';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1369266398:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GIVEAWAY")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'C';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1449476787:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_GIVEAWAY")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = ',';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1498266155:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PHONE_CALL_MISSED")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 136;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1533804208:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_VIDEOS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '%';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1540131626:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_PLAYLIST")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '&';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1547988151:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_AUDIO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'M';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1561464595:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_PHOTO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'H';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1563525743:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_ROUND")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'J';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1564592598:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_STORY")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'G';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1567024476:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_VIDEO")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'I';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1810705077:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("MESSAGE_INVOICE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\"';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1815177512:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGES")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'B';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1837240696:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_REACT_PAID_MEDIA")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '\f';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1954774321:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_PLAYLIST")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'i';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 1963241394:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("LOCKED_MESSAGE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 131;
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 2014789757:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_PHOTO_EDITED")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'Z';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 2022049433:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("PINNED_CONTACT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 't';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 2034984710:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_PLAYLIST")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '@';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 2048733346:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_NOTEXT")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '.';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 2099392181:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHANNEL_MESSAGE_PHOTOS")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = '>';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    case 2140162142:
                                                                                                                        str20 = str17;
                                                                                                                        if (str7.equals("CHAT_MESSAGE_GEOLIVE")) {
                                                                                                                            str21 = str16;
                                                                                                                            obj4 = obj8;
                                                                                                                            obj5 = obj7;
                                                                                                                            c2 = 'R';
                                                                                                                            obj7 = obj5;
                                                                                                                            break;
                                                                                                                        }
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                    default:
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        obj4 = obj8;
                                                                                                                        obj5 = obj7;
                                                                                                                        obj7 = obj5;
                                                                                                                        c2 = 65535;
                                                                                                                        break;
                                                                                                                }
                                                                                                                obj8 = obj4;
                                                                                                                str18 = "CHAT_REACT_";
                                                                                                                str19 = str15;
                                                                                                                z9 = has;
                                                                                                                try {
                                                                                                                    switch (c2) {
                                                                                                                        case 0:
                                                                                                                            string = LocaleController.getString("StoryNotificationSingle");
                                                                                                                            str22 = string;
                                                                                                                            i12 = i26;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 1:
                                                                                                                            string = LocaleController.formatPluralString("StoryNotificationHidden", 1, new Object[0]);
                                                                                                                            str22 = string;
                                                                                                                            i12 = i26;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 2:
                                                                                                                            formatString = LocaleController.formatString("ActionSetSameWallpaperForThisChat", R.string.ActionSetSameWallpaperForThisChat, strArr[0]);
                                                                                                                            i13 = R.string.WallpaperSameNotification;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 3:
                                                                                                                            formatString = LocaleController.formatString("ActionSetWallpaperForThisChat", R.string.ActionSetWallpaperForThisChat, strArr[0]);
                                                                                                                            i13 = R.string.WallpaperNotification;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 4:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageRecurringPay", R.string.NotificationMessageRecurringPay, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.PaymentInvoice;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 5:
                                                                                                                        case 6:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, strArr[0], strArr[1]);
                                                                                                                            str23 = strArr[1];
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 7:
                                                                                                                            String str31 = strArr[0];
                                                                                                                            formatString = LocaleController.formatPluralStringComma("NotificationMessageStarGift", Integer.parseInt(strArr[1]), strArr[0]);
                                                                                                                            str26 = LocaleController.formatPluralStringComma("Gift2Notification", Integer.parseInt(strArr[1]));
                                                                                                                            str24 = str31;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '\b':
                                                                                                                            int parseInt3 = Integer.parseInt(strArr[1]);
                                                                                                                            reactedText = LocaleController.formatPluralString("NotificationMessagePaidMedia", parseInt3, strArr[0]);
                                                                                                                            formatPluralString = LocaleController.formatPluralString("NotificationPaidMedia", parseInt3, new Object[0]);
                                                                                                                            str26 = formatPluralString;
                                                                                                                            break;
                                                                                                                        case '\t':
                                                                                                                            int parseInt4 = Integer.parseInt(strArr[1]);
                                                                                                                            reactedText = LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", parseInt4, strArr[0]);
                                                                                                                            formatPluralString = LocaleController.formatPluralString("NotificationPaidMedia", parseInt4, new Object[0]);
                                                                                                                            str26 = formatPluralString;
                                                                                                                            break;
                                                                                                                        case '\n':
                                                                                                                            int parseInt5 = Integer.parseInt(strArr[2]);
                                                                                                                            reactedText = LocaleController.formatPluralString("NotificationChatMessagePaidMedia", parseInt5, strArr[0], strArr[1]);
                                                                                                                            formatPluralString = LocaleController.formatPluralString("NotificationPaidMedia", parseInt5, new Object[0]);
                                                                                                                            str26 = formatPluralString;
                                                                                                                            break;
                                                                                                                        case 11:
                                                                                                                            int parseInt6 = Integer.parseInt(strArr[1]);
                                                                                                                            formatPluralString2 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt6, strArr[0]);
                                                                                                                            formatPluralString3 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt6, strArr[0]);
                                                                                                                            str26 = formatPluralString3;
                                                                                                                            reactedText = formatPluralString2;
                                                                                                                            break;
                                                                                                                        case '\f':
                                                                                                                            int parseInt7 = Integer.parseInt(strArr[1]);
                                                                                                                            formatPluralString2 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt7, strArr[0]);
                                                                                                                            formatPluralString3 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt7, strArr[0]);
                                                                                                                            str26 = formatPluralString3;
                                                                                                                            reactedText = formatPluralString2;
                                                                                                                            break;
                                                                                                                        case '\r':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, strArr[0]);
                                                                                                                            i13 = R.string.Message;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 14:
                                                                                                                            formatString = LocaleController.formatString("NotificationStory", R.string.NotificationStory, strArr[0]);
                                                                                                                            i13 = R.string.Story;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 15:
                                                                                                                            reactedText = LocaleController.getString(R.string.StoryNotificationMention);
                                                                                                                            break;
                                                                                                                        case 16:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, strArr[0]);
                                                                                                                            i13 = R.string.AttachPhoto;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 17:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, strArr[0]);
                                                                                                                            i13 = R.string.AttachDestructingPhoto;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 18:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, strArr[0]);
                                                                                                                            i13 = R.string.AttachVideo;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 19:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, strArr[0]);
                                                                                                                            i13 = R.string.AttachDestructingVideo;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 20:
                                                                                                                            reactedText = LocaleController.getString(R.string.ActionTakeScreenshoot).replace("un1", strArr[0]);
                                                                                                                            break;
                                                                                                                        case 21:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, strArr[0]);
                                                                                                                            i13 = R.string.AttachRound;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 22:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, strArr[0]);
                                                                                                                            i13 = R.string.AttachDocument;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 23:
                                                                                                                            if (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) {
                                                                                                                                formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, strArr[0]);
                                                                                                                                i13 = R.string.AttachSticker;
                                                                                                                                str23 = LocaleController.getString(i13);
                                                                                                                                str26 = str23;
                                                                                                                                str24 = str20;
                                                                                                                                str22 = formatString;
                                                                                                                                str20 = str24;
                                                                                                                                i12 = i25;
                                                                                                                                z10 = false;
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, strArr[0], strArr[1]);
                                                                                                                                sb = new StringBuilder();
                                                                                                                                sb.append(strArr[1]);
                                                                                                                                sb.append(" ");
                                                                                                                                string2 = LocaleController.getString(R.string.AttachSticker);
                                                                                                                                sb.append(string2);
                                                                                                                                str23 = sb.toString();
                                                                                                                                str26 = str23;
                                                                                                                                str24 = str20;
                                                                                                                                str22 = formatString;
                                                                                                                                str20 = str24;
                                                                                                                                i12 = i25;
                                                                                                                                z10 = false;
                                                                                                                            }
                                                                                                                            break;
                                                                                                                        case 24:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, strArr[0]);
                                                                                                                            i13 = R.string.AttachAudio;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 25:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachContact;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 26:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.QuizPoll;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 27:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.Poll;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 28:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, strArr[0]);
                                                                                                                            i13 = R.string.AttachLocation;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 29:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, strArr[0]);
                                                                                                                            i13 = R.string.AttachLiveLocation;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 30:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, strArr[0]);
                                                                                                                            i13 = R.string.AttachGif;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 31:
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachGame;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case ' ':
                                                                                                                        case '!':
                                                                                                                            reactedText = LocaleController.formatString("NotificationMessageGameScored", R.string.NotificationMessageGameScored, strArr[0], strArr[1], strArr[2]);
                                                                                                                            break;
                                                                                                                        case '\"':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageInvoice", R.string.NotificationMessageInvoice, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.PaymentInvoice;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '#':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageForwardFew", R.string.NotificationMessageForwardFew, strArr[0], LocaleController.formatPluralString(str30, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '$':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString(str4, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '%':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString(str5, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '&':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '\'':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '(':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageGiftCode", R.string.NotificationMessageGiftCode, strArr[0], LocaleController.formatPluralString("Months", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case ')':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageGiveaway", R.string.NotificationMessageGiveaway, strArr[0], strArr[1], strArr[2]);
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '*':
                                                                                                                            try {
                                                                                                                                i14 = Integer.parseInt(strArr[1]);
                                                                                                                            } catch (Exception unused) {
                                                                                                                                i14 = 1;
                                                                                                                            }
                                                                                                                            formatString2 = LocaleController.formatString(R.string.NotificationMessageStarsGiveaway2, strArr[0], LocaleController.formatPluralString("AmongWinners", i14, new Object[0]), strArr[2]);
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '+':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationMessageAlbum", R.string.NotificationMessageAlbum, strArr[0]);
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case ',':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageChannelGiveaway", R.string.NotificationMessageChannelGiveaway, strArr[0], strArr[1], strArr[2]);
                                                                                                                            i13 = R.string.BoostingGiveaway;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '-':
                                                                                                                            try {
                                                                                                                                i15 = Integer.parseInt(strArr[1]);
                                                                                                                            } catch (Exception unused2) {
                                                                                                                                i15 = 1;
                                                                                                                            }
                                                                                                                            formatString = LocaleController.formatString(R.string.NotificationMessageChannelStarsGiveaway2, strArr[0], LocaleController.formatPluralString("AmongWinners", i15, new Object[0]), strArr[2]);
                                                                                                                            i13 = R.string.BoostingGiveaway;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '.':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, strArr[0]);
                                                                                                                            i13 = R.string.Message;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '/':
                                                                                                                            formatString = LocaleController.formatString("NotificationChannelStory", R.string.NotificationChannelStory, strArr[0]);
                                                                                                                            i13 = R.string.Story;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '0':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessagePhoto", R.string.ChannelMessagePhoto, strArr[0]);
                                                                                                                            i13 = R.string.AttachPhoto;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '1':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageVideo", R.string.ChannelMessageVideo, strArr[0]);
                                                                                                                            i13 = R.string.AttachVideo;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '2':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageRound", R.string.ChannelMessageRound, strArr[0]);
                                                                                                                            i13 = R.string.AttachRound;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '3':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageDocument", R.string.ChannelMessageDocument, strArr[0]);
                                                                                                                            i13 = R.string.AttachDocument;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '4':
                                                                                                                            if (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) {
                                                                                                                                formatString = LocaleController.formatString("ChannelMessageSticker", R.string.ChannelMessageSticker, strArr[0]);
                                                                                                                                i13 = R.string.AttachSticker;
                                                                                                                                str23 = LocaleController.getString(i13);
                                                                                                                                str26 = str23;
                                                                                                                                str24 = str20;
                                                                                                                                str22 = formatString;
                                                                                                                                str20 = str24;
                                                                                                                                i12 = i25;
                                                                                                                                z10 = false;
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                formatString = LocaleController.formatString("ChannelMessageStickerEmoji", R.string.ChannelMessageStickerEmoji, strArr[0], strArr[1]);
                                                                                                                                sb = new StringBuilder();
                                                                                                                                sb.append(strArr[1]);
                                                                                                                                sb.append(" ");
                                                                                                                                string2 = LocaleController.getString(R.string.AttachSticker);
                                                                                                                                sb.append(string2);
                                                                                                                                str23 = sb.toString();
                                                                                                                                str26 = str23;
                                                                                                                                str24 = str20;
                                                                                                                                str22 = formatString;
                                                                                                                                str20 = str24;
                                                                                                                                i12 = i25;
                                                                                                                                z10 = false;
                                                                                                                            }
                                                                                                                            break;
                                                                                                                        case '5':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageAudio", R.string.ChannelMessageAudio, strArr[0]);
                                                                                                                            i13 = R.string.AttachAudio;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '6':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageContact2", R.string.ChannelMessageContact2, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachContact;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '7':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageQuiz2", R.string.ChannelMessageQuiz2, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.QuizPoll;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '8':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessagePoll2", R.string.ChannelMessagePoll2, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.Poll;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '9':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageMap", R.string.ChannelMessageMap, strArr[0]);
                                                                                                                            i13 = R.string.AttachLocation;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case ':':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageLiveLocation", R.string.ChannelMessageLiveLocation, strArr[0]);
                                                                                                                            i13 = R.string.AttachLiveLocation;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case ';':
                                                                                                                            formatString = LocaleController.formatString("ChannelMessageGIF", R.string.ChannelMessageGIF, strArr[0]);
                                                                                                                            i13 = R.string.AttachGif;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '<':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0]);
                                                                                                                            i13 = R.string.AttachGame;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case '=':
                                                                                                                            formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("ForwardedMessageCount", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]).toLowerCase());
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '>':
                                                                                                                            formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString(str4, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '?':
                                                                                                                            formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString(str5, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case '@':
                                                                                                                            formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'A':
                                                                                                                            formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'B':
                                                                                                                            formatString2 = LocaleController.formatString("ChannelMessageAlbum", R.string.ChannelMessageAlbum, strArr[0]);
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'C':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageChatGiveaway", R.string.NotificationMessageChatGiveaway, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                                                                                            i13 = R.string.BoostingGiveaway;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'D':
                                                                                                                            try {
                                                                                                                                i16 = Integer.parseInt(strArr[2]);
                                                                                                                            } catch (Exception unused3) {
                                                                                                                                i16 = 1;
                                                                                                                            }
                                                                                                                            formatString = LocaleController.formatString(R.string.NotificationMessageChatStarsGiveaway2, strArr[0], strArr[1], LocaleController.formatPluralString("AmongWinners", i16, new Object[0]), strArr[3]);
                                                                                                                            i13 = R.string.BoostingGiveaway;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'E':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, strArr[0], strArr[1], strArr[2]);
                                                                                                                            str23 = strArr[2];
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'F':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.Message;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'G':
                                                                                                                            formatString = LocaleController.formatString("NotificationChatStory", R.string.NotificationChatStory, strArr[0]);
                                                                                                                            i13 = R.string.Story;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'H':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupPhoto", R.string.NotificationMessageGroupPhoto, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachPhoto;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'I':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupVideo", R.string.NotificationMessageGroupVideo, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachVideo;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'J':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupRound", R.string.NotificationMessageGroupRound, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachRound;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'K':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupDocument", R.string.NotificationMessageGroupDocument, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachDocument;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'L':
                                                                                                                            if (strArr.length <= 2 || TextUtils.isEmpty(strArr[2])) {
                                                                                                                                formatString = LocaleController.formatString("NotificationMessageGroupSticker", R.string.NotificationMessageGroupSticker, strArr[0], strArr[1]);
                                                                                                                                sb = new StringBuilder();
                                                                                                                                sb.append(strArr[1]);
                                                                                                                                sb.append(" ");
                                                                                                                                string2 = LocaleController.getString(R.string.AttachSticker);
                                                                                                                            } else {
                                                                                                                                formatString = LocaleController.formatString("NotificationMessageGroupStickerEmoji", R.string.NotificationMessageGroupStickerEmoji, strArr[0], strArr[1], strArr[2]);
                                                                                                                                sb = new StringBuilder();
                                                                                                                                sb.append(strArr[2]);
                                                                                                                                sb.append(" ");
                                                                                                                                string2 = LocaleController.getString(R.string.AttachSticker);
                                                                                                                            }
                                                                                                                            sb.append(string2);
                                                                                                                            str23 = sb.toString();
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'M':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupAudio", R.string.NotificationMessageGroupAudio, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachAudio;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'N':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupContact2", R.string.NotificationMessageGroupContact2, strArr[0], strArr[1], strArr[2]);
                                                                                                                            i13 = R.string.AttachContact;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'O':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupQuiz2", R.string.NotificationMessageGroupQuiz2, strArr[0], strArr[1], strArr[2]);
                                                                                                                            i13 = R.string.PollQuiz;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'P':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupPoll2", R.string.NotificationMessageGroupPoll2, strArr[0], strArr[1], strArr[2]);
                                                                                                                            i13 = R.string.Poll;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'Q':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachLocation;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'R':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupLiveLocation", R.string.NotificationMessageGroupLiveLocation, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachLiveLocation;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'S':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupGif", R.string.NotificationMessageGroupGif, strArr[0], strArr[1]);
                                                                                                                            i13 = R.string.AttachGif;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'T':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupGame", R.string.NotificationMessageGroupGame, strArr[0], strArr[1], strArr[2]);
                                                                                                                            i13 = R.string.AttachGame;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'U':
                                                                                                                            reactedText = LocaleController.formatString("NotificationMessageGroupGameScored", R.string.NotificationMessageGroupGameScored, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                                                                                            break;
                                                                                                                        case 'V':
                                                                                                                            formatString = LocaleController.formatString("NotificationMessageGroupInvoice", R.string.NotificationMessageGroupInvoice, strArr[0], strArr[1], strArr[2]);
                                                                                                                            i13 = R.string.PaymentInvoice;
                                                                                                                            str23 = LocaleController.getString(i13);
                                                                                                                            str26 = str23;
                                                                                                                            str24 = str20;
                                                                                                                            str22 = formatString;
                                                                                                                            str20 = str24;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                        case 'W':
                                                                                                                        case 'X':
                                                                                                                            reactedText = LocaleController.formatString("NotificationInvitedToGroup", R.string.NotificationInvitedToGroup, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'Y':
                                                                                                                            reactedText = LocaleController.formatString("NotificationEditedGroupName", R.string.NotificationEditedGroupName, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'Z':
                                                                                                                            reactedText = LocaleController.formatString("NotificationEditedGroupPhoto", R.string.NotificationEditedGroupPhoto, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case '[':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, strArr[0], strArr[1], strArr[2]);
                                                                                                                            break;
                                                                                                                        case '\\':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupCreatedCall", R.string.NotificationGroupCreatedCall, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case ']':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupInvitedToCall", R.string.NotificationGroupInvitedToCall, strArr[0], strArr[1], strArr[2]);
                                                                                                                            break;
                                                                                                                        case '^':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupEndedCall", R.string.NotificationGroupEndedCall, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case '_':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupInvitedYouToCall", R.string.NotificationGroupInvitedYouToCall, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case '`':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupKickMember", R.string.NotificationGroupKickMember, strArr[0], strArr[1], strArr.length > 2 ? strArr[2] : "");
                                                                                                                            break;
                                                                                                                        case 'a':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupKickYou", R.string.NotificationGroupKickYou, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'b':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupLeftMember", R.string.NotificationGroupLeftMember, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'c':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupAddSelf", R.string.NotificationGroupAddSelf, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'd':
                                                                                                                            reactedText = LocaleController.formatString("NotificationGroupAddSelfMega", R.string.NotificationGroupAddSelfMega, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'e':
                                                                                                                            reactedText = LocaleController.formatString("UserAcceptedToGroupPushWithGroup", R.string.UserAcceptedToGroupPushWithGroup, strArr[0], strArr[1]);
                                                                                                                            break;
                                                                                                                        case 'f':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationGroupForwardedFew", R.string.NotificationGroupForwardedFew, strArr[0], strArr[1], LocaleController.formatPluralString(str30, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'g':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString(str4, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'h':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString(str5, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'i':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'j':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'k':
                                                                                                                            formatString2 = LocaleController.formatString("NotificationGroupAlbum", R.string.NotificationGroupAlbum, strArr[0], strArr[1]);
                                                                                                                            str25 = str21;
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        case 'l':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedTextUser", R.string.NotificationActionPinnedTextUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, strArr[0], strArr[1], strArr[2]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'm':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedNoTextUser", R.string.NotificationActionPinnedNoTextUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'n':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedPhotoUser", R.string.NotificationActionPinnedPhotoUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedPhoto", R.string.NotificationActionPinnedPhoto, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedPhotoChannel", R.string.NotificationActionPinnedPhotoChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'o':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedVideoUser", R.string.NotificationActionPinnedVideoUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedVideo", R.string.NotificationActionPinnedVideo, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedVideoChannel", R.string.NotificationActionPinnedVideoChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'p':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedRoundUser", R.string.NotificationActionPinnedRoundUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedRound", R.string.NotificationActionPinnedRound, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedRoundChannel", R.string.NotificationActionPinnedRoundChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'q':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedFileUser", R.string.NotificationActionPinnedFileUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedFile", R.string.NotificationActionPinnedFile, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedFileChannel", R.string.NotificationActionPinnedFileChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'r':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                if (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) {
                                                                                                                                    reactedText = LocaleController.formatString("NotificationActionPinnedStickerUser", R.string.NotificationActionPinnedStickerUser, strArr[0]);
                                                                                                                                    break;
                                                                                                                                } else {
                                                                                                                                    reactedText = LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", R.string.NotificationActionPinnedStickerEmojiUser, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                }
                                                                                                                            } else if (z5) {
                                                                                                                                if (strArr.length <= 2 || TextUtils.isEmpty(strArr[2])) {
                                                                                                                                    reactedText = LocaleController.formatString("NotificationActionPinnedSticker", R.string.NotificationActionPinnedSticker, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                } else {
                                                                                                                                    reactedText = LocaleController.formatString("NotificationActionPinnedStickerEmoji", R.string.NotificationActionPinnedStickerEmoji, strArr[0], strArr[2], strArr[1]);
                                                                                                                                    break;
                                                                                                                                }
                                                                                                                            } else if (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedStickerChannel", R.string.NotificationActionPinnedStickerChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", R.string.NotificationActionPinnedStickerEmojiChannel, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                            break;
                                                                                                                        case 's':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedVoiceUser", R.string.NotificationActionPinnedVoiceUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedVoice", R.string.NotificationActionPinnedVoice, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedVoiceChannel", R.string.NotificationActionPinnedVoiceChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 't':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedContactUser", R.string.NotificationActionPinnedContactUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedContact2", R.string.NotificationActionPinnedContact2, strArr[0], strArr[2], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedContactChannel2", R.string.NotificationActionPinnedContactChannel2, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'u':
                                                                                                                            reactedText = LocaleController.formatString("NotificationPinnedGiveaway", R.string.NotificationPinnedGiveaway, strArr[0]);
                                                                                                                            break;
                                                                                                                        case 'v':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedQuizUser", R.string.NotificationActionPinnedQuizUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedQuiz2", R.string.NotificationActionPinnedQuiz2, strArr[0], strArr[2], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedQuizChannel2", R.string.NotificationActionPinnedQuizChannel2, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'w':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedPollUser", R.string.NotificationActionPinnedPollUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedPoll2", R.string.NotificationActionPinnedPoll2, strArr[0], strArr[2], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedPollChannel2", R.string.NotificationActionPinnedPollChannel2, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'x':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGeoUser", R.string.NotificationActionPinnedGeoUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGeo", R.string.NotificationActionPinnedGeo, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGeoChannel", R.string.NotificationActionPinnedGeoChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'y':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGeoLiveUser", R.string.NotificationActionPinnedGeoLiveUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGeoLive", R.string.NotificationActionPinnedGeoLive, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", R.string.NotificationActionPinnedGeoLiveChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case 'z':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGameUser", R.string.NotificationActionPinnedGameUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGame", R.string.NotificationActionPinnedGame, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGameChannel", R.string.NotificationActionPinnedGameChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case '{':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGameScoreUser", R.string.NotificationActionPinnedGameScoreUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGameScore", R.string.NotificationActionPinnedGameScore, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGameScoreChannel", R.string.NotificationActionPinnedGameScoreChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case '|':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedInvoiceUser", R.string.NotificationActionPinnedInvoiceUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedInvoice", R.string.NotificationActionPinnedInvoice, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedInvoiceChannel", R.string.NotificationActionPinnedInvoiceChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case '}':
                                                                                                                            if (makeEncryptedDialogId > 0) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGifUser", R.string.NotificationActionPinnedGifUser, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else if (z5) {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGif", R.string.NotificationActionPinnedGif, strArr[0], strArr[1]);
                                                                                                                                break;
                                                                                                                            } else {
                                                                                                                                reactedText = LocaleController.formatString("NotificationActionPinnedGifChannel", R.string.NotificationActionPinnedGifChannel, strArr[0]);
                                                                                                                                break;
                                                                                                                            }
                                                                                                                        case '~':
                                                                                                                            formatString2 = LocaleController.getString(R.string.YouHaveNewMessage);
                                                                                                                            str25 = LocaleController.getString(R.string.SecretChatName);
                                                                                                                            str22 = formatString2;
                                                                                                                            str21 = str25;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = true;
                                                                                                                            break;
                                                                                                                        default:
                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                FileLog.w("unhandled loc_key = " + str7);
                                                                                                                            }
                                                                                                                        case NotificationCenter.dialogTranslate /* 127 */:
                                                                                                                        case 128:
                                                                                                                        case NotificationCenter.walletPendingTransactionsChanged /* 129 */:
                                                                                                                        case NotificationCenter.walletSyncProgressChanged /* 130 */:
                                                                                                                        case NotificationCenter.httpFileDidLoad /* 131 */:
                                                                                                                        case NotificationCenter.httpFileDidFailedLoad /* 132 */:
                                                                                                                        case NotificationCenter.didUpdateConnectionState /* 133 */:
                                                                                                                        case NotificationCenter.fileUploaded /* 134 */:
                                                                                                                        case NotificationCenter.fileUploadFailed /* 135 */:
                                                                                                                        case NotificationCenter.fileUploadProgressChanged /* 136 */:
                                                                                                                            str22 = null;
                                                                                                                            i12 = i25;
                                                                                                                            z10 = false;
                                                                                                                            break;
                                                                                                                    }
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                        FileLog.d(str + " received message notification " + str7 + str14 + makeEncryptedDialogId + " mid = " + i12);
                                                                                                                    }
                                                                                                                    if (str22 != null) {
                                                                                                                        TLRPC.TL_message tL_message = new TLRPC.TL_message();
                                                                                                                        if (str7.startsWith("REACT_STORY") && i12 > 0) {
                                                                                                                            i12 = -i12;
                                                                                                                        }
                                                                                                                        tL_message.id = i12;
                                                                                                                        tL_message.random_id = j13;
                                                                                                                        tL_message.message = str26 != null ? str26 : str22;
                                                                                                                        tL_message.date = (int) (j / 1000);
                                                                                                                        if (z6) {
                                                                                                                            tL_message.action = new TLRPC.TL_messageActionPinMessage();
                                                                                                                        }
                                                                                                                        if (z8) {
                                                                                                                            tL_message.flags |= Integer.MIN_VALUE;
                                                                                                                        }
                                                                                                                        tL_message.dialog_id = makeEncryptedDialogId;
                                                                                                                        if (j2 != 0) {
                                                                                                                            TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
                                                                                                                            tL_message.peer_id = tL_peerChannel;
                                                                                                                            tL_peerChannel.channel_id = j2;
                                                                                                                            j9 = j12;
                                                                                                                        } else if (j12 != 0) {
                                                                                                                            TLRPC.TL_peerChat tL_peerChat2 = new TLRPC.TL_peerChat();
                                                                                                                            tL_message.peer_id = tL_peerChat2;
                                                                                                                            j9 = j12;
                                                                                                                            tL_peerChat2.chat_id = j9;
                                                                                                                        } else {
                                                                                                                            j9 = j12;
                                                                                                                            TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                                                                                                                            tL_message.peer_id = tL_peerUser2;
                                                                                                                            tL_peerUser2.user_id = j11;
                                                                                                                        }
                                                                                                                        tL_message.flags |= 256;
                                                                                                                        if (optLong3 != 0) {
                                                                                                                            TLRPC.TL_peerChat tL_peerChat3 = new TLRPC.TL_peerChat();
                                                                                                                            tL_message.from_id = tL_peerChat3;
                                                                                                                            tL_peerChat3.chat_id = j9;
                                                                                                                        } else if (optLong2 != 0) {
                                                                                                                            TLRPC.TL_peerChannel tL_peerChannel2 = new TLRPC.TL_peerChannel();
                                                                                                                            tL_message.from_id = tL_peerChannel2;
                                                                                                                            tL_peerChannel2.channel_id = optLong2;
                                                                                                                        } else if (j8 != 0) {
                                                                                                                            TLRPC.TL_peerUser tL_peerUser3 = new TLRPC.TL_peerUser();
                                                                                                                            tL_message.from_id = tL_peerUser3;
                                                                                                                            tL_peerUser3.user_id = j8;
                                                                                                                        } else {
                                                                                                                            tL_message.from_id = tL_message.peer_id;
                                                                                                                        }
                                                                                                                        if (!z16 && !z6) {
                                                                                                                            z11 = false;
                                                                                                                            tL_message.mentioned = z11;
                                                                                                                            tL_message.silent = z15;
                                                                                                                            tL_message.from_scheduled = z13;
                                                                                                                            MessageObject messageObject = new MessageObject(i6, tL_message, str22, str21, str20, z10, z7, z8, z9);
                                                                                                                            if (i10 != 0) {
                                                                                                                                messageObject.messageOwner.reply_to = new TLRPC.TL_messageReplyHeader();
                                                                                                                                TLRPC.MessageReplyHeader messageReplyHeader = messageObject.messageOwner.reply_to;
                                                                                                                                messageReplyHeader.forum_topic = true;
                                                                                                                                messageReplyHeader.reply_to_top_id = i10;
                                                                                                                            }
                                                                                                                            boolean startsWith = str7.startsWith("REACT_STORY");
                                                                                                                            messageObject.isStoryReactionPush = startsWith;
                                                                                                                            messageObject.isReactionPush = startsWith && (str7.startsWith(str19) || str7.startsWith(str18));
                                                                                                                            if (str7.equals(obj8)) {
                                                                                                                                obj3 = obj7;
                                                                                                                                if (!str7.equals(obj3)) {
                                                                                                                                    z12 = false;
                                                                                                                                    messageObject.isStoryPush = z12;
                                                                                                                                    messageObject.isStoryMentionPush = str7.equals("MESSAGE_STORY_MENTION");
                                                                                                                                    messageObject.isStoryPushHidden = str7.equals(obj3);
                                                                                                                                    ArrayList<MessageObject> arrayList4 = new ArrayList<>();
                                                                                                                                    arrayList4.add(messageObject);
                                                                                                                                    FileLog.d("PushListenerController push notification to NotificationsController of " + tL_message.dialog_id);
                                                                                                                                    NotificationsController.getInstance(i6).processNewMessages(arrayList4, true, true, countDownLatch);
                                                                                                                                    ConnectionsManager.onInternalPushReceived(i6);
                                                                                                                                    ConnectionsManager.getInstance(i6).resumeNetworkMaybe();
                                                                                                                                    return;
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                obj3 = obj7;
                                                                                                                            }
                                                                                                                            z12 = true;
                                                                                                                            messageObject.isStoryPush = z12;
                                                                                                                            messageObject.isStoryMentionPush = str7.equals("MESSAGE_STORY_MENTION");
                                                                                                                            messageObject.isStoryPushHidden = str7.equals(obj3);
                                                                                                                            ArrayList<MessageObject> arrayList42 = new ArrayList<>();
                                                                                                                            arrayList42.add(messageObject);
                                                                                                                            FileLog.d("PushListenerController push notification to NotificationsController of " + tL_message.dialog_id);
                                                                                                                            NotificationsController.getInstance(i6).processNewMessages(arrayList42, true, true, countDownLatch);
                                                                                                                            ConnectionsManager.onInternalPushReceived(i6);
                                                                                                                            ConnectionsManager.getInstance(i6).resumeNetworkMaybe();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        z11 = true;
                                                                                                                        tL_message.mentioned = z11;
                                                                                                                        tL_message.silent = z15;
                                                                                                                        tL_message.from_scheduled = z13;
                                                                                                                        MessageObject messageObject2 = new MessageObject(i6, tL_message, str22, str21, str20, z10, z7, z8, z9);
                                                                                                                        if (i10 != 0) {
                                                                                                                        }
                                                                                                                        boolean startsWith2 = str7.startsWith("REACT_STORY");
                                                                                                                        messageObject2.isStoryReactionPush = startsWith2;
                                                                                                                        messageObject2.isReactionPush = startsWith2 && (str7.startsWith(str19) || str7.startsWith(str18));
                                                                                                                        if (str7.equals(obj8)) {
                                                                                                                        }
                                                                                                                        z12 = true;
                                                                                                                        messageObject2.isStoryPush = z12;
                                                                                                                        messageObject2.isStoryMentionPush = str7.equals("MESSAGE_STORY_MENTION");
                                                                                                                        messageObject2.isStoryPushHidden = str7.equals(obj3);
                                                                                                                        ArrayList<MessageObject> arrayList422 = new ArrayList<>();
                                                                                                                        arrayList422.add(messageObject2);
                                                                                                                        FileLog.d("PushListenerController push notification to NotificationsController of " + tL_message.dialog_id);
                                                                                                                        NotificationsController.getInstance(i6).processNewMessages(arrayList422, true, true, countDownLatch);
                                                                                                                        ConnectionsManager.onInternalPushReceived(i6);
                                                                                                                        ConnectionsManager.getInstance(i6).resumeNetworkMaybe();
                                                                                                                        return;
                                                                                                                    }
                                                                                                                } catch (Throwable th9) {
                                                                                                                    th = th9;
                                                                                                                    str6 = str2;
                                                                                                                    i = i6;
                                                                                                                    i2 = -1;
                                                                                                                    if (i == i2) {
                                                                                                                    }
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                    }
                                                                                                                    FileLog.e(th);
                                                                                                                    return;
                                                                                                                }
                                                                                                            }
                                                                                                            str22 = reactedText;
                                                                                                            i12 = i25;
                                                                                                            z10 = false;
                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                            }
                                                                                                            if (str22 != null) {
                                                                                                            }
                                                                                                        }
                                                                                                    } else if (UserObject.isReplyUser(makeEncryptedDialogId)) {
                                                                                                        str16 = str16 + " @ " + strArr[1];
                                                                                                        str17 = null;
                                                                                                        z6 = false;
                                                                                                        z7 = false;
                                                                                                        z8 = false;
                                                                                                        if (str7.startsWith(str15)) {
                                                                                                        }
                                                                                                        str18 = "CHAT_REACT_";
                                                                                                        str19 = str15;
                                                                                                        z9 = has;
                                                                                                        str20 = str17;
                                                                                                        str21 = str16;
                                                                                                        reactedText = getReactedText(str7, strArr);
                                                                                                        str22 = reactedText;
                                                                                                        i12 = i25;
                                                                                                        z10 = false;
                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                        }
                                                                                                        if (str22 != null) {
                                                                                                        }
                                                                                                    } else {
                                                                                                        z8 = j2 != 0;
                                                                                                        str17 = str16;
                                                                                                        str16 = strArr[1];
                                                                                                        z6 = false;
                                                                                                        z7 = false;
                                                                                                        if (str7.startsWith(str15)) {
                                                                                                        }
                                                                                                        str18 = "CHAT_REACT_";
                                                                                                        str19 = str15;
                                                                                                        z9 = has;
                                                                                                        str20 = str17;
                                                                                                        str21 = str16;
                                                                                                        reactedText = getReactedText(str7, strArr);
                                                                                                        str22 = reactedText;
                                                                                                        i12 = i25;
                                                                                                        z10 = false;
                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                        }
                                                                                                        if (str22 != null) {
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                str16 = null;
                                                                                                boolean has2 = jSONObject.has("edit_date");
                                                                                                if (str7.startsWith("CHAT_")) {
                                                                                                }
                                                                                                if (str7.startsWith("PINNED_")) {
                                                                                                }
                                                                                            }
                                                                                            z5 = true;
                                                                                            if (jSONObject.has("mention")) {
                                                                                            }
                                                                                            if (jSONObject.has("silent")) {
                                                                                            }
                                                                                            boolean z162 = z14;
                                                                                            jSONObject3 = jSONObject2;
                                                                                            if (jSONObject3.has("loc_args")) {
                                                                                            }
                                                                                            if (strArr != null) {
                                                                                                str16 = strArr[0];
                                                                                                boolean has22 = jSONObject.has("edit_date");
                                                                                                if (str7.startsWith("CHAT_")) {
                                                                                                }
                                                                                                if (str7.startsWith("PINNED_")) {
                                                                                                }
                                                                                            }
                                                                                            str16 = null;
                                                                                            boolean has222 = jSONObject.has("edit_date");
                                                                                            if (str7.startsWith("CHAT_")) {
                                                                                            }
                                                                                            if (str7.startsWith("PINNED_")) {
                                                                                            }
                                                                                        } catch (Throwable th10) {
                                                                                            th = th10;
                                                                                            th = th;
                                                                                            i4 = i6;
                                                                                            str6 = str2;
                                                                                            i = i4;
                                                                                            i2 = -1;
                                                                                            if (i == i2) {
                                                                                            }
                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                            }
                                                                                            FileLog.e(th);
                                                                                            return;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                i9 = jSONObject.getInt(str12);
                                                                                if (jSONObject.has("random_id")) {
                                                                                }
                                                                                if (i9 == 0) {
                                                                                }
                                                                                z4 = z3;
                                                                                if (str7.startsWith(str15)) {
                                                                                }
                                                                                obj2 = obj;
                                                                                z4 = true;
                                                                                int i252 = i9;
                                                                                if (str7.equals(obj2)) {
                                                                                }
                                                                                if (!jSONObject.has("story_id")) {
                                                                                }
                                                                                if (i11 < 0) {
                                                                                }
                                                                                if (z4) {
                                                                                }
                                                                            }
                                                                            countDownLatch.countDown();
                                                                            ConnectionsManager.onInternalPushReceived(i6);
                                                                            ConnectionsManager.getInstance(i6).resumeNetworkMaybe();
                                                                            return;
                                                                        }
                                                                        NotificationsController.getInstance(i7).processDeleteStory(makeEncryptedDialogId, jSONObject.getInt("story_id"));
                                                                    }
                                                                }
                                                                i6 = i7;
                                                                countDownLatch.countDown();
                                                                ConnectionsManager.onInternalPushReceived(i6);
                                                                ConnectionsManager.getInstance(i6).resumeNetworkMaybe();
                                                                return;
                                                            }
                                                        } else {
                                                            jSONObject2 = jSONObject4;
                                                        }
                                                        z2 = false;
                                                        if (makeEncryptedDialogId == 0) {
                                                            makeEncryptedDialogId = NotificationsController.globalSecretChatId;
                                                        }
                                                        if (makeEncryptedDialogId != 0) {
                                                        }
                                                        i6 = i7;
                                                        countDownLatch.countDown();
                                                        ConnectionsManager.onInternalPushReceived(i6);
                                                        ConnectionsManager.getInstance(i6).resumeNetworkMaybe();
                                                        return;
                                                    } catch (Throwable th11) {
                                                        th = th11;
                                                        i6 = i7;
                                                    }
                                                } catch (Throwable th12) {
                                                    th = th12;
                                                    i6 = i5;
                                                    th = th;
                                                    i4 = i6;
                                                    str6 = str2;
                                                    i = i4;
                                                    i2 = -1;
                                                    if (i == i2) {
                                                    }
                                                    if (BuildVars.LOGS_ENABLED) {
                                                    }
                                                    FileLog.e(th);
                                                    return;
                                                }
                                            } catch (Throwable th13) {
                                                th = th13;
                                                str2 = str27;
                                            }
                                        }
                                        if (c != 0) {
                                        }
                                    } catch (Throwable th14) {
                                        th = th14;
                                    }
                                    c = 65535;
                                } else {
                                    if (str7.equals("MESSAGE_ANNOUNCEMENT")) {
                                        c = 1;
                                        if (c != 0) {
                                        }
                                    }
                                    c = 65535;
                                    if (c != 0) {
                                    }
                                }
                            } catch (Throwable th15) {
                                th = th15;
                                str2 = str27;
                            }
                        } catch (Throwable th16) {
                            th = th16;
                            str2 = str27;
                            i4 = i5;
                        }
                    }
                    intValue = userConfig.getClientUserId();
                    int i172 = UserConfig.selectedAccount;
                    i3 = 4;
                    i4 = 0;
                    while (true) {
                        if (i4 < i3) {
                        }
                        i4++;
                        i3 = 4;
                    }
                    if (z) {
                    }
                } catch (Throwable th17) {
                    str11 = str27;
                    th = th17;
                }
            } catch (Throwable th18) {
                str11 = str27;
                th = th18;
            }
        } catch (Throwable th19) {
            th = th19;
            str6 = null;
            str7 = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$6(final String str, final String str2, final long j) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " PRE INIT APP");
        }
        ApplicationLoader.postInitApplication();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " POST INIT APP");
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$processRemoteMessage$5(str, str2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRegistrationToServer$0(int i, int i2, String str) {
        MessagesController.getInstance(i).registerForPush(i2, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRegistrationToServer$1(final String str, final int i) {
        boolean z;
        ConnectionsManager.setRegId(str, i, SharedConfig.pushStringStatus);
        if (str == null) {
            return;
        }
        if (SharedConfig.pushStringGetTimeStart == 0 || SharedConfig.pushStringGetTimeEnd == 0 || (SharedConfig.pushStatSent && TextUtils.equals(SharedConfig.pushString, str))) {
            z = false;
        } else {
            SharedConfig.pushStatSent = false;
            z = true;
        }
        SharedConfig.pushString = str;
        SharedConfig.pushType = i;
        for (final int i2 = 0; i2 < 4; i2++) {
            UserConfig userConfig = UserConfig.getInstance(i2);
            userConfig.registeredForPush = false;
            userConfig.saveConfig(false);
            if (userConfig.getClientUserId() != 0) {
                if (z) {
                    String str2 = i == 2 ? "fcm" : "hcm";
                    TLRPC.TL_help_saveAppLog tL_help_saveAppLog = new TLRPC.TL_help_saveAppLog();
                    TLRPC.TL_inputAppEvent tL_inputAppEvent = new TLRPC.TL_inputAppEvent();
                    tL_inputAppEvent.time = SharedConfig.pushStringGetTimeStart;
                    tL_inputAppEvent.type = str2 + "_token_request";
                    tL_inputAppEvent.peer = 0L;
                    tL_inputAppEvent.data = new TLRPC.TL_jsonNull();
                    tL_help_saveAppLog.events.add(tL_inputAppEvent);
                    TLRPC.TL_inputAppEvent tL_inputAppEvent2 = new TLRPC.TL_inputAppEvent();
                    tL_inputAppEvent2.time = SharedConfig.pushStringGetTimeEnd;
                    tL_inputAppEvent2.type = str2 + "_token_response";
                    tL_inputAppEvent2.peer = SharedConfig.pushStringGetTimeEnd - SharedConfig.pushStringGetTimeStart;
                    tL_inputAppEvent2.data = new TLRPC.TL_jsonNull();
                    tL_help_saveAppLog.events.add(tL_inputAppEvent2);
                    SharedConfig.pushStatSent = true;
                    SharedConfig.saveConfig();
                    ConnectionsManager.getInstance(i2).sendRequest(tL_help_saveAppLog, null);
                    z = false;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        PushListenerController.lambda$sendRegistrationToServer$0(i2, i, str);
                    }
                });
            }
        }
    }

    private static void onDecryptError() {
        for (int i = 0; i < 4; i++) {
            if (UserConfig.getInstance(i).isClientActivated()) {
                ConnectionsManager.onInternalPushReceived(i);
                ConnectionsManager.getInstance(i).resumeNetworkMaybe();
            }
        }
        countDownLatch.countDown();
    }

    public static void processRemoteMessage(int i, final String str, final long j) {
        final String str2 = i == 2 ? "FCM" : "HCM";
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str2 + " PRE START PROCESSING");
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$processRemoteMessage$6(str2, str, j);
            }
        });
        try {
            countDownLatch.await();
        } catch (Throwable unused) {
        }
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("finished " + str2 + " service, time = " + (SystemClock.elapsedRealtime() - elapsedRealtime));
        }
    }

    public static void sendRegistrationToServer(final int i, final String str) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$sendRegistrationToServer$1(str, i);
            }
        });
    }
}
