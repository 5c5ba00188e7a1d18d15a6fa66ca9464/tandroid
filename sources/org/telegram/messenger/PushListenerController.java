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
    public static /* synthetic */ void lambda$processRemoteMessage$5(int i, long j, int i2) {
        MessagesController.getInstance(i).reportMessageDelivery(j, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:1603:0x05ab, code lost:
    
        if (org.telegram.messenger.MessagesStorage.getInstance(r5).checkMessageByRandomId(r14) == false) goto L254;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:261:0x0715. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:267:0x1316. Please report as an issue. */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x037a A[Catch: all -> 0x2669, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x2669, blocks: (B:122:0x02fe, B:125:0x0310, B:129:0x034f, B:142:0x037a, B:157:0x03fc, B:160:0x0412, B:164:0x042a, B:177:0x049f, B:187:0x0525, B:189:0x052b, B:193:0x0543, B:204:0x05b4, B:210:0x05cc, B:1590:0x05df, B:1605:0x0536), top: B:121:0x02fe }] */
    /* JADX WARN: Removed duplicated region for block: B:1571:0x06dc  */
    /* JADX WARN: Removed duplicated region for block: B:1576:0x06eb A[Catch: all -> 0x2655, TryCatch #11 {all -> 0x2655, blocks: (B:221:0x05fe, B:227:0x0620, B:230:0x0639, B:235:0x064e, B:246:0x0686, B:256:0x06f9, B:274:0x24ab, B:278:0x24da, B:282:0x24ea, B:285:0x24f6, B:290:0x2511, B:294:0x254a, B:302:0x258e, B:305:0x25c7, B:313:0x25e4, B:319:0x25fc, B:321:0x262e, B:323:0x2632, B:325:0x2636, B:327:0x263a, B:332:0x2644, B:355:0x2582, B:361:0x253d, B:1560:0x24a5, B:1569:0x06d4, B:1576:0x06eb), top: B:220:0x05fe }] */
    /* JADX WARN: Removed duplicated region for block: B:1580:0x0676  */
    /* JADX WARN: Removed duplicated region for block: B:1582:0x0628 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1592:0x05e5 A[Catch: all -> 0x030b, TRY_ENTER, TRY_LEAVE, TryCatch #7 {all -> 0x030b, blocks: (B:1614:0x0304, B:127:0x033c, B:131:0x0355, B:137:0x036a, B:139:0x0372, B:145:0x0386, B:147:0x0395, B:150:0x03b8, B:151:0x03e5, B:152:0x03c5, B:154:0x03d0, B:155:0x03e3, B:156:0x03da, B:159:0x0404, B:163:0x041e, B:167:0x0438, B:168:0x044b, B:170:0x044e, B:172:0x045a, B:174:0x0477, B:175:0x0499, B:176:0x0520, B:179:0x04a7, B:180:0x04c1, B:182:0x04c4, B:184:0x04dc, B:186:0x04fa, B:192:0x053d, B:195:0x054b, B:197:0x0562, B:199:0x0578, B:200:0x0592, B:207:0x05be, B:213:0x05d6, B:1592:0x05e5, B:1602:0x05a3), top: B:1613:0x0304 }] */
    /* JADX WARN: Removed duplicated region for block: B:1594:0x05ed  */
    /* JADX WARN: Removed duplicated region for block: B:1595:0x05f0  */
    /* JADX WARN: Removed duplicated region for block: B:1596:0x05ea  */
    /* JADX WARN: Removed duplicated region for block: B:1600:0x0599  */
    /* JADX WARN: Removed duplicated region for block: B:1604:0x055d  */
    /* JADX WARN: Removed duplicated region for block: B:1649:0x26f2 A[Catch: all -> 0x2651, TryCatch #8 {all -> 0x2651, blocks: (B:334:0x264a, B:335:0x265b, B:336:0x2671, B:364:0x266c, B:1643:0x2684, B:1645:0x2697, B:1647:0x26a8, B:1649:0x26f2, B:1651:0x270b, B:1653:0x2711), top: B:103:0x02ae }] */
    /* JADX WARN: Removed duplicated region for block: B:1670:0x01ff A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x054b A[Catch: all -> 0x030b, TRY_ENTER, TryCatch #7 {all -> 0x030b, blocks: (B:1614:0x0304, B:127:0x033c, B:131:0x0355, B:137:0x036a, B:139:0x0372, B:145:0x0386, B:147:0x0395, B:150:0x03b8, B:151:0x03e5, B:152:0x03c5, B:154:0x03d0, B:155:0x03e3, B:156:0x03da, B:159:0x0404, B:163:0x041e, B:167:0x0438, B:168:0x044b, B:170:0x044e, B:172:0x045a, B:174:0x0477, B:175:0x0499, B:176:0x0520, B:179:0x04a7, B:180:0x04c1, B:182:0x04c4, B:184:0x04dc, B:186:0x04fa, B:192:0x053d, B:195:0x054b, B:197:0x0562, B:199:0x0578, B:200:0x0592, B:207:0x05be, B:213:0x05d6, B:1592:0x05e5, B:1602:0x05a3), top: B:1613:0x0304 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0562 A[Catch: all -> 0x030b, TryCatch #7 {all -> 0x030b, blocks: (B:1614:0x0304, B:127:0x033c, B:131:0x0355, B:137:0x036a, B:139:0x0372, B:145:0x0386, B:147:0x0395, B:150:0x03b8, B:151:0x03e5, B:152:0x03c5, B:154:0x03d0, B:155:0x03e3, B:156:0x03da, B:159:0x0404, B:163:0x041e, B:167:0x0438, B:168:0x044b, B:170:0x044e, B:172:0x045a, B:174:0x0477, B:175:0x0499, B:176:0x0520, B:179:0x04a7, B:180:0x04c1, B:182:0x04c4, B:184:0x04dc, B:186:0x04fa, B:192:0x053d, B:195:0x054b, B:197:0x0562, B:199:0x0578, B:200:0x0592, B:207:0x05be, B:213:0x05d6, B:1592:0x05e5, B:1602:0x05a3), top: B:1613:0x0304 }] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x05f4  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0641 A[Catch: all -> 0x0632, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x0632, blocks: (B:1583:0x0628, B:232:0x0641, B:237:0x065a, B:239:0x066b, B:242:0x067c, B:245:0x0680, B:249:0x0696, B:251:0x0699, B:253:0x069f, B:258:0x06ff, B:260:0x0711, B:261:0x0715, B:268:0x1319, B:270:0x131d, B:276:0x24af, B:287:0x2501, B:289:0x250a, B:293:0x2519, B:297:0x2556, B:304:0x25b3, B:308:0x25d3, B:310:0x25d9, B:316:0x25f0, B:351:0x2566, B:354:0x2576, B:358:0x252d, B:365:0x1338, B:369:0x134c, B:372:0x135d, B:375:0x136a, B:378:0x137b, B:379:0x1381, B:382:0x138d, B:385:0x1399, B:388:0x13aa, B:390:0x13b2, B:393:0x13c3, B:394:0x13c9, B:397:0x13d5, B:400:0x13e1, B:403:0x13f2, B:405:0x13fa, B:408:0x140b, B:409:0x1411, B:412:0x141d, B:415:0x1429, B:418:0x143a, B:420:0x1442, B:423:0x1453, B:424:0x1459, B:427:0x1465, B:430:0x1471, B:433:0x1482, B:435:0x148a, B:438:0x149b, B:439:0x14a1, B:442:0x14ad, B:445:0x14b9, B:448:0x14ca, B:450:0x14d2, B:453:0x14e3, B:454:0x14e9, B:457:0x14f5, B:460:0x1501, B:463:0x1512, B:465:0x151a, B:468:0x1532, B:469:0x1538, B:472:0x1549, B:475:0x1555, B:478:0x1566, B:480:0x156e, B:483:0x1586, B:484:0x158c, B:487:0x159d, B:488:0x15a3, B:491:0x15af, B:494:0x15bb, B:497:0x15cc, B:499:0x15d4, B:502:0x15ec, B:503:0x15f2, B:506:0x1603, B:509:0x160f, B:512:0x1620, B:514:0x1628, B:517:0x1639, B:518:0x163f, B:521:0x164b, B:524:0x1657, B:526:0x165b, B:528:0x1663, B:531:0x1673, B:532:0x1679, B:535:0x1685, B:537:0x168d, B:539:0x1691, B:541:0x1699, B:544:0x16b0, B:545:0x16b6, B:548:0x16c7, B:549:0x16cd, B:551:0x16d1, B:553:0x16d9, B:556:0x16e9, B:557:0x16ef, B:560:0x16fb, B:563:0x1707, B:566:0x1718, B:568:0x1720, B:571:0x1731, B:572:0x1737, B:575:0x1743, B:578:0x174f, B:581:0x1760, B:583:0x1768, B:586:0x1779, B:587:0x177f, B:590:0x178b, B:593:0x1797, B:596:0x17a8, B:598:0x17b0, B:601:0x17c1, B:602:0x17c7, B:605:0x17d3, B:608:0x17df, B:611:0x17f0, B:613:0x17f8, B:616:0x1809, B:617:0x180f, B:620:0x181b, B:623:0x1827, B:626:0x1838, B:628:0x1840, B:631:0x1851, B:632:0x1857, B:635:0x1863, B:638:0x186f, B:641:0x1880, B:643:0x1888, B:646:0x18a0, B:647:0x18a6, B:650:0x18b7, B:651:0x18bd, B:654:0x18ce, B:656:0x18d4, B:659:0x18f8, B:660:0x18fe, B:663:0x1925, B:664:0x192b, B:667:0x1952, B:668:0x1958, B:671:0x197f, B:672:0x1985, B:675:0x19ae, B:676:0x19b4, B:679:0x19c5, B:680:0x19cb, B:683:0x19dc, B:684:0x19e2, B:687:0x19f3, B:688:0x19f9, B:691:0x1a0a, B:692:0x1a10, B:695:0x1a21, B:696:0x1a27, B:700:0x1a46, B:701:0x1a37, B:703:0x1a4c, B:706:0x1a5d, B:707:0x1a63, B:710:0x1a74, B:711:0x1a7a, B:714:0x1a92, B:715:0x1a98, B:718:0x1aa9, B:719:0x1aaf, B:722:0x1ac7, B:723:0x1acd, B:726:0x1ade, B:727:0x1ae4, B:730:0x1af5, B:731:0x1afb, B:734:0x1b0c, B:735:0x1b12, B:738:0x1b2a, B:739:0x1b2e, B:741:0x247d, B:743:0x1b34, B:746:0x1b52, B:747:0x1b58, B:750:0x1b70, B:751:0x1b74, B:752:0x1b77, B:755:0x1b88, B:756:0x1b8c, B:757:0x1b8f, B:760:0x1ba0, B:761:0x1ba4, B:762:0x1ba7, B:765:0x1bb8, B:766:0x1bbc, B:767:0x1bc0, B:770:0x1bd8, B:771:0x1bdc, B:772:0x1be0, B:775:0x1bf8, B:776:0x1c00, B:779:0x1c18, B:780:0x1c1c, B:781:0x1c20, B:784:0x1c31, B:785:0x1c35, B:786:0x1c39, B:788:0x1c3d, B:790:0x1c45, B:793:0x1c5d, B:794:0x1c76, B:795:0x2276, B:797:0x1c7b, B:800:0x1c8f, B:801:0x1ca7, B:804:0x1cb8, B:805:0x1cbc, B:806:0x1cc0, B:809:0x1cd1, B:810:0x1cd5, B:811:0x1cd9, B:814:0x1cea, B:815:0x1cee, B:816:0x1cf2, B:819:0x1d03, B:820:0x1d07, B:821:0x1d0b, B:824:0x1d17, B:825:0x1d1b, B:826:0x1d1f, B:829:0x1d30, B:830:0x1d34, B:831:0x1d38, B:834:0x1d50, B:837:0x1d5d, B:838:0x1d65, B:841:0x1d87, B:842:0x1d8b, B:845:0x1d8f, B:848:0x1dad, B:849:0x1db2, B:852:0x1dbe, B:853:0x1dc4, B:856:0x1de3, B:857:0x1de9, B:860:0x1e0a, B:861:0x1e10, B:864:0x1e31, B:865:0x1e37, B:868:0x1e58, B:869:0x1e5e, B:872:0x1e83, B:873:0x1e89, B:876:0x1e95, B:877:0x1e9b, B:880:0x1ea7, B:881:0x1ead, B:884:0x1eb9, B:885:0x1ebf, B:888:0x1ecb, B:889:0x1ed1, B:892:0x1ee2, B:893:0x1ee8, B:896:0x1ef9, B:897:0x1efd, B:898:0x1f01, B:901:0x1f12, B:902:0x1f18, B:905:0x1f24, B:906:0x1f2a, B:908:0x1f30, B:910:0x1f38, B:913:0x1f49, B:914:0x1f62, B:917:0x1f6e, B:918:0x1f72, B:919:0x1f76, B:922:0x1f82, B:923:0x1f88, B:926:0x1f94, B:927:0x1f9a, B:930:0x1fa6, B:931:0x1fac, B:934:0x1fb8, B:935:0x1fbe, B:938:0x1fca, B:939:0x1fd0, B:942:0x1fdc, B:945:0x1fe5, B:946:0x1fed, B:949:0x2005, B:952:0x200b, B:955:0x2023, B:956:0x2029, B:959:0x2035, B:962:0x203e, B:963:0x2046, B:966:0x205e, B:969:0x2064, B:972:0x207c, B:973:0x2082, B:976:0x20a4, B:977:0x20aa, B:980:0x20c8, B:981:0x20ce, B:984:0x20ee, B:985:0x20f3, B:988:0x2113, B:989:0x2118, B:992:0x2138, B:993:0x213d, B:996:0x215f, B:997:0x216d, B:1000:0x217e, B:1001:0x2184, B:1004:0x219c, B:1005:0x21a2, B:1008:0x21b3, B:1009:0x21b9, B:1012:0x21c5, B:1013:0x21cb, B:1016:0x21d7, B:1017:0x21dd, B:1020:0x21e9, B:1021:0x21ef, B:1024:0x21fe, B:1025:0x2204, B:1028:0x2213, B:1029:0x2219, B:1032:0x2228, B:1033:0x222e, B:1036:0x2238, B:1037:0x223e, B:1039:0x2244, B:1041:0x224c, B:1044:0x225d, B:1045:0x227c, B:1048:0x2288, B:1049:0x228e, B:1052:0x229a, B:1053:0x22a0, B:1056:0x22ac, B:1057:0x22b2, B:1058:0x22c2, B:1061:0x22ce, B:1062:0x22d6, B:1065:0x22e2, B:1066:0x22e8, B:1069:0x22f4, B:1070:0x22fc, B:1073:0x2308, B:1074:0x230e, B:1075:0x2317, B:1078:0x2323, B:1079:0x2329, B:1082:0x2335, B:1083:0x233b, B:1085:0x2349, B:1087:0x2353, B:1089:0x235b, B:1091:0x2369, B:1093:0x2373, B:1094:0x2378, B:1096:0x238d, B:1098:0x239d, B:1100:0x23af, B:1101:0x23ba, B:1103:0x23cc, B:1104:0x23d7, B:1107:0x23e1, B:1108:0x23e9, B:1111:0x23f3, B:1112:0x23fb, B:1115:0x240f, B:1116:0x2424, B:1119:0x2436, B:1120:0x243e, B:1123:0x244f, B:1124:0x2455, B:1127:0x2461, B:1128:0x2469, B:1131:0x2475, B:1132:0x248f, B:1134:0x249a, B:1139:0x0722, B:1143:0x0738, B:1146:0x074e, B:1149:0x0764, B:1152:0x077a, B:1155:0x0790, B:1158:0x07a6, B:1161:0x07bc, B:1164:0x07d2, B:1167:0x07e8, B:1170:0x07fe, B:1173:0x0814, B:1176:0x082a, B:1179:0x0840, B:1182:0x0856, B:1185:0x086c, B:1188:0x0882, B:1191:0x0898, B:1194:0x08ae, B:1197:0x08c4, B:1200:0x08da, B:1203:0x08f0, B:1206:0x0906, B:1209:0x091c, B:1212:0x0932, B:1215:0x0948, B:1218:0x095e, B:1221:0x0974, B:1224:0x0988, B:1227:0x099e, B:1230:0x09b4, B:1233:0x09ca, B:1236:0x09e0, B:1239:0x09f6, B:1242:0x0a0c, B:1245:0x0a22, B:1248:0x0a38, B:1251:0x0a4d, B:1254:0x0a63, B:1257:0x0a79, B:1260:0x0a8f, B:1263:0x0aa5, B:1266:0x0abb, B:1269:0x0ad1, B:1272:0x0ae7, B:1275:0x0afd, B:1278:0x0b13, B:1281:0x0b29, B:1284:0x0b3f, B:1287:0x0b55, B:1290:0x0b6b, B:1293:0x0b81, B:1296:0x0b96, B:1299:0x0bac, B:1302:0x0bc2, B:1305:0x0bd8, B:1308:0x0bee, B:1311:0x0c04, B:1314:0x0c1a, B:1317:0x0c30, B:1320:0x0c46, B:1323:0x0c5c, B:1326:0x0c72, B:1329:0x0c88, B:1332:0x0c9e, B:1335:0x0cb4, B:1338:0x0cca, B:1341:0x0ce0, B:1344:0x0cf6, B:1347:0x0d0c, B:1350:0x0d22, B:1353:0x0d38, B:1356:0x0d4e, B:1359:0x0d64, B:1362:0x0d7a, B:1365:0x0d90, B:1368:0x0da6, B:1371:0x0dbc, B:1374:0x0dd2, B:1377:0x0de8, B:1380:0x0dfe, B:1383:0x0e14, B:1386:0x0e2a, B:1389:0x0e40, B:1392:0x0e56, B:1395:0x0e6c, B:1398:0x0e82, B:1401:0x0e98, B:1404:0x0eac, B:1407:0x0ec2, B:1410:0x0ed8, B:1413:0x0eec, B:1416:0x0f02, B:1419:0x0f18, B:1422:0x0f2e, B:1425:0x0f44, B:1428:0x0f5a, B:1431:0x0f6e, B:1434:0x0f84, B:1437:0x0f9a, B:1442:0x0fb2, B:1445:0x0fc8, B:1448:0x0fde, B:1451:0x0ff4, B:1454:0x100a, B:1457:0x1020, B:1460:0x1036, B:1463:0x104b, B:1466:0x1061, B:1469:0x1077, B:1472:0x108d, B:1475:0x10a3, B:1478:0x10b9, B:1481:0x10cf, B:1484:0x10e5, B:1487:0x10fb, B:1490:0x1111, B:1493:0x1127, B:1496:0x113b, B:1499:0x1151, B:1502:0x1167, B:1505:0x117d, B:1508:0x1193, B:1511:0x11a7, B:1514:0x11bd, B:1517:0x11d3, B:1520:0x11e9, B:1523:0x11ff, B:1526:0x1215, B:1529:0x122b, B:1532:0x1241, B:1535:0x1257, B:1538:0x126d, B:1541:0x1283, B:1544:0x1299, B:1547:0x12ae, B:1550:0x12c3, B:1553:0x12d8, B:1556:0x12ed, B:1565:0x06c9), top: B:1582:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x065a A[Catch: all -> 0x0632, TRY_ENTER, TryCatch #9 {all -> 0x0632, blocks: (B:1583:0x0628, B:232:0x0641, B:237:0x065a, B:239:0x066b, B:242:0x067c, B:245:0x0680, B:249:0x0696, B:251:0x0699, B:253:0x069f, B:258:0x06ff, B:260:0x0711, B:261:0x0715, B:268:0x1319, B:270:0x131d, B:276:0x24af, B:287:0x2501, B:289:0x250a, B:293:0x2519, B:297:0x2556, B:304:0x25b3, B:308:0x25d3, B:310:0x25d9, B:316:0x25f0, B:351:0x2566, B:354:0x2576, B:358:0x252d, B:365:0x1338, B:369:0x134c, B:372:0x135d, B:375:0x136a, B:378:0x137b, B:379:0x1381, B:382:0x138d, B:385:0x1399, B:388:0x13aa, B:390:0x13b2, B:393:0x13c3, B:394:0x13c9, B:397:0x13d5, B:400:0x13e1, B:403:0x13f2, B:405:0x13fa, B:408:0x140b, B:409:0x1411, B:412:0x141d, B:415:0x1429, B:418:0x143a, B:420:0x1442, B:423:0x1453, B:424:0x1459, B:427:0x1465, B:430:0x1471, B:433:0x1482, B:435:0x148a, B:438:0x149b, B:439:0x14a1, B:442:0x14ad, B:445:0x14b9, B:448:0x14ca, B:450:0x14d2, B:453:0x14e3, B:454:0x14e9, B:457:0x14f5, B:460:0x1501, B:463:0x1512, B:465:0x151a, B:468:0x1532, B:469:0x1538, B:472:0x1549, B:475:0x1555, B:478:0x1566, B:480:0x156e, B:483:0x1586, B:484:0x158c, B:487:0x159d, B:488:0x15a3, B:491:0x15af, B:494:0x15bb, B:497:0x15cc, B:499:0x15d4, B:502:0x15ec, B:503:0x15f2, B:506:0x1603, B:509:0x160f, B:512:0x1620, B:514:0x1628, B:517:0x1639, B:518:0x163f, B:521:0x164b, B:524:0x1657, B:526:0x165b, B:528:0x1663, B:531:0x1673, B:532:0x1679, B:535:0x1685, B:537:0x168d, B:539:0x1691, B:541:0x1699, B:544:0x16b0, B:545:0x16b6, B:548:0x16c7, B:549:0x16cd, B:551:0x16d1, B:553:0x16d9, B:556:0x16e9, B:557:0x16ef, B:560:0x16fb, B:563:0x1707, B:566:0x1718, B:568:0x1720, B:571:0x1731, B:572:0x1737, B:575:0x1743, B:578:0x174f, B:581:0x1760, B:583:0x1768, B:586:0x1779, B:587:0x177f, B:590:0x178b, B:593:0x1797, B:596:0x17a8, B:598:0x17b0, B:601:0x17c1, B:602:0x17c7, B:605:0x17d3, B:608:0x17df, B:611:0x17f0, B:613:0x17f8, B:616:0x1809, B:617:0x180f, B:620:0x181b, B:623:0x1827, B:626:0x1838, B:628:0x1840, B:631:0x1851, B:632:0x1857, B:635:0x1863, B:638:0x186f, B:641:0x1880, B:643:0x1888, B:646:0x18a0, B:647:0x18a6, B:650:0x18b7, B:651:0x18bd, B:654:0x18ce, B:656:0x18d4, B:659:0x18f8, B:660:0x18fe, B:663:0x1925, B:664:0x192b, B:667:0x1952, B:668:0x1958, B:671:0x197f, B:672:0x1985, B:675:0x19ae, B:676:0x19b4, B:679:0x19c5, B:680:0x19cb, B:683:0x19dc, B:684:0x19e2, B:687:0x19f3, B:688:0x19f9, B:691:0x1a0a, B:692:0x1a10, B:695:0x1a21, B:696:0x1a27, B:700:0x1a46, B:701:0x1a37, B:703:0x1a4c, B:706:0x1a5d, B:707:0x1a63, B:710:0x1a74, B:711:0x1a7a, B:714:0x1a92, B:715:0x1a98, B:718:0x1aa9, B:719:0x1aaf, B:722:0x1ac7, B:723:0x1acd, B:726:0x1ade, B:727:0x1ae4, B:730:0x1af5, B:731:0x1afb, B:734:0x1b0c, B:735:0x1b12, B:738:0x1b2a, B:739:0x1b2e, B:741:0x247d, B:743:0x1b34, B:746:0x1b52, B:747:0x1b58, B:750:0x1b70, B:751:0x1b74, B:752:0x1b77, B:755:0x1b88, B:756:0x1b8c, B:757:0x1b8f, B:760:0x1ba0, B:761:0x1ba4, B:762:0x1ba7, B:765:0x1bb8, B:766:0x1bbc, B:767:0x1bc0, B:770:0x1bd8, B:771:0x1bdc, B:772:0x1be0, B:775:0x1bf8, B:776:0x1c00, B:779:0x1c18, B:780:0x1c1c, B:781:0x1c20, B:784:0x1c31, B:785:0x1c35, B:786:0x1c39, B:788:0x1c3d, B:790:0x1c45, B:793:0x1c5d, B:794:0x1c76, B:795:0x2276, B:797:0x1c7b, B:800:0x1c8f, B:801:0x1ca7, B:804:0x1cb8, B:805:0x1cbc, B:806:0x1cc0, B:809:0x1cd1, B:810:0x1cd5, B:811:0x1cd9, B:814:0x1cea, B:815:0x1cee, B:816:0x1cf2, B:819:0x1d03, B:820:0x1d07, B:821:0x1d0b, B:824:0x1d17, B:825:0x1d1b, B:826:0x1d1f, B:829:0x1d30, B:830:0x1d34, B:831:0x1d38, B:834:0x1d50, B:837:0x1d5d, B:838:0x1d65, B:841:0x1d87, B:842:0x1d8b, B:845:0x1d8f, B:848:0x1dad, B:849:0x1db2, B:852:0x1dbe, B:853:0x1dc4, B:856:0x1de3, B:857:0x1de9, B:860:0x1e0a, B:861:0x1e10, B:864:0x1e31, B:865:0x1e37, B:868:0x1e58, B:869:0x1e5e, B:872:0x1e83, B:873:0x1e89, B:876:0x1e95, B:877:0x1e9b, B:880:0x1ea7, B:881:0x1ead, B:884:0x1eb9, B:885:0x1ebf, B:888:0x1ecb, B:889:0x1ed1, B:892:0x1ee2, B:893:0x1ee8, B:896:0x1ef9, B:897:0x1efd, B:898:0x1f01, B:901:0x1f12, B:902:0x1f18, B:905:0x1f24, B:906:0x1f2a, B:908:0x1f30, B:910:0x1f38, B:913:0x1f49, B:914:0x1f62, B:917:0x1f6e, B:918:0x1f72, B:919:0x1f76, B:922:0x1f82, B:923:0x1f88, B:926:0x1f94, B:927:0x1f9a, B:930:0x1fa6, B:931:0x1fac, B:934:0x1fb8, B:935:0x1fbe, B:938:0x1fca, B:939:0x1fd0, B:942:0x1fdc, B:945:0x1fe5, B:946:0x1fed, B:949:0x2005, B:952:0x200b, B:955:0x2023, B:956:0x2029, B:959:0x2035, B:962:0x203e, B:963:0x2046, B:966:0x205e, B:969:0x2064, B:972:0x207c, B:973:0x2082, B:976:0x20a4, B:977:0x20aa, B:980:0x20c8, B:981:0x20ce, B:984:0x20ee, B:985:0x20f3, B:988:0x2113, B:989:0x2118, B:992:0x2138, B:993:0x213d, B:996:0x215f, B:997:0x216d, B:1000:0x217e, B:1001:0x2184, B:1004:0x219c, B:1005:0x21a2, B:1008:0x21b3, B:1009:0x21b9, B:1012:0x21c5, B:1013:0x21cb, B:1016:0x21d7, B:1017:0x21dd, B:1020:0x21e9, B:1021:0x21ef, B:1024:0x21fe, B:1025:0x2204, B:1028:0x2213, B:1029:0x2219, B:1032:0x2228, B:1033:0x222e, B:1036:0x2238, B:1037:0x223e, B:1039:0x2244, B:1041:0x224c, B:1044:0x225d, B:1045:0x227c, B:1048:0x2288, B:1049:0x228e, B:1052:0x229a, B:1053:0x22a0, B:1056:0x22ac, B:1057:0x22b2, B:1058:0x22c2, B:1061:0x22ce, B:1062:0x22d6, B:1065:0x22e2, B:1066:0x22e8, B:1069:0x22f4, B:1070:0x22fc, B:1073:0x2308, B:1074:0x230e, B:1075:0x2317, B:1078:0x2323, B:1079:0x2329, B:1082:0x2335, B:1083:0x233b, B:1085:0x2349, B:1087:0x2353, B:1089:0x235b, B:1091:0x2369, B:1093:0x2373, B:1094:0x2378, B:1096:0x238d, B:1098:0x239d, B:1100:0x23af, B:1101:0x23ba, B:1103:0x23cc, B:1104:0x23d7, B:1107:0x23e1, B:1108:0x23e9, B:1111:0x23f3, B:1112:0x23fb, B:1115:0x240f, B:1116:0x2424, B:1119:0x2436, B:1120:0x243e, B:1123:0x244f, B:1124:0x2455, B:1127:0x2461, B:1128:0x2469, B:1131:0x2475, B:1132:0x248f, B:1134:0x249a, B:1139:0x0722, B:1143:0x0738, B:1146:0x074e, B:1149:0x0764, B:1152:0x077a, B:1155:0x0790, B:1158:0x07a6, B:1161:0x07bc, B:1164:0x07d2, B:1167:0x07e8, B:1170:0x07fe, B:1173:0x0814, B:1176:0x082a, B:1179:0x0840, B:1182:0x0856, B:1185:0x086c, B:1188:0x0882, B:1191:0x0898, B:1194:0x08ae, B:1197:0x08c4, B:1200:0x08da, B:1203:0x08f0, B:1206:0x0906, B:1209:0x091c, B:1212:0x0932, B:1215:0x0948, B:1218:0x095e, B:1221:0x0974, B:1224:0x0988, B:1227:0x099e, B:1230:0x09b4, B:1233:0x09ca, B:1236:0x09e0, B:1239:0x09f6, B:1242:0x0a0c, B:1245:0x0a22, B:1248:0x0a38, B:1251:0x0a4d, B:1254:0x0a63, B:1257:0x0a79, B:1260:0x0a8f, B:1263:0x0aa5, B:1266:0x0abb, B:1269:0x0ad1, B:1272:0x0ae7, B:1275:0x0afd, B:1278:0x0b13, B:1281:0x0b29, B:1284:0x0b3f, B:1287:0x0b55, B:1290:0x0b6b, B:1293:0x0b81, B:1296:0x0b96, B:1299:0x0bac, B:1302:0x0bc2, B:1305:0x0bd8, B:1308:0x0bee, B:1311:0x0c04, B:1314:0x0c1a, B:1317:0x0c30, B:1320:0x0c46, B:1323:0x0c5c, B:1326:0x0c72, B:1329:0x0c88, B:1332:0x0c9e, B:1335:0x0cb4, B:1338:0x0cca, B:1341:0x0ce0, B:1344:0x0cf6, B:1347:0x0d0c, B:1350:0x0d22, B:1353:0x0d38, B:1356:0x0d4e, B:1359:0x0d64, B:1362:0x0d7a, B:1365:0x0d90, B:1368:0x0da6, B:1371:0x0dbc, B:1374:0x0dd2, B:1377:0x0de8, B:1380:0x0dfe, B:1383:0x0e14, B:1386:0x0e2a, B:1389:0x0e40, B:1392:0x0e56, B:1395:0x0e6c, B:1398:0x0e82, B:1401:0x0e98, B:1404:0x0eac, B:1407:0x0ec2, B:1410:0x0ed8, B:1413:0x0eec, B:1416:0x0f02, B:1419:0x0f18, B:1422:0x0f2e, B:1425:0x0f44, B:1428:0x0f5a, B:1431:0x0f6e, B:1434:0x0f84, B:1437:0x0f9a, B:1442:0x0fb2, B:1445:0x0fc8, B:1448:0x0fde, B:1451:0x0ff4, B:1454:0x100a, B:1457:0x1020, B:1460:0x1036, B:1463:0x104b, B:1466:0x1061, B:1469:0x1077, B:1472:0x108d, B:1475:0x10a3, B:1478:0x10b9, B:1481:0x10cf, B:1484:0x10e5, B:1487:0x10fb, B:1490:0x1111, B:1493:0x1127, B:1496:0x113b, B:1499:0x1151, B:1502:0x1167, B:1505:0x117d, B:1508:0x1193, B:1511:0x11a7, B:1514:0x11bd, B:1517:0x11d3, B:1520:0x11e9, B:1523:0x11ff, B:1526:0x1215, B:1529:0x122b, B:1532:0x1241, B:1535:0x1257, B:1538:0x126d, B:1541:0x1283, B:1544:0x1299, B:1547:0x12ae, B:1550:0x12c3, B:1553:0x12d8, B:1556:0x12ed, B:1565:0x06c9), top: B:1582:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:242:0x067c A[Catch: all -> 0x0632, TryCatch #9 {all -> 0x0632, blocks: (B:1583:0x0628, B:232:0x0641, B:237:0x065a, B:239:0x066b, B:242:0x067c, B:245:0x0680, B:249:0x0696, B:251:0x0699, B:253:0x069f, B:258:0x06ff, B:260:0x0711, B:261:0x0715, B:268:0x1319, B:270:0x131d, B:276:0x24af, B:287:0x2501, B:289:0x250a, B:293:0x2519, B:297:0x2556, B:304:0x25b3, B:308:0x25d3, B:310:0x25d9, B:316:0x25f0, B:351:0x2566, B:354:0x2576, B:358:0x252d, B:365:0x1338, B:369:0x134c, B:372:0x135d, B:375:0x136a, B:378:0x137b, B:379:0x1381, B:382:0x138d, B:385:0x1399, B:388:0x13aa, B:390:0x13b2, B:393:0x13c3, B:394:0x13c9, B:397:0x13d5, B:400:0x13e1, B:403:0x13f2, B:405:0x13fa, B:408:0x140b, B:409:0x1411, B:412:0x141d, B:415:0x1429, B:418:0x143a, B:420:0x1442, B:423:0x1453, B:424:0x1459, B:427:0x1465, B:430:0x1471, B:433:0x1482, B:435:0x148a, B:438:0x149b, B:439:0x14a1, B:442:0x14ad, B:445:0x14b9, B:448:0x14ca, B:450:0x14d2, B:453:0x14e3, B:454:0x14e9, B:457:0x14f5, B:460:0x1501, B:463:0x1512, B:465:0x151a, B:468:0x1532, B:469:0x1538, B:472:0x1549, B:475:0x1555, B:478:0x1566, B:480:0x156e, B:483:0x1586, B:484:0x158c, B:487:0x159d, B:488:0x15a3, B:491:0x15af, B:494:0x15bb, B:497:0x15cc, B:499:0x15d4, B:502:0x15ec, B:503:0x15f2, B:506:0x1603, B:509:0x160f, B:512:0x1620, B:514:0x1628, B:517:0x1639, B:518:0x163f, B:521:0x164b, B:524:0x1657, B:526:0x165b, B:528:0x1663, B:531:0x1673, B:532:0x1679, B:535:0x1685, B:537:0x168d, B:539:0x1691, B:541:0x1699, B:544:0x16b0, B:545:0x16b6, B:548:0x16c7, B:549:0x16cd, B:551:0x16d1, B:553:0x16d9, B:556:0x16e9, B:557:0x16ef, B:560:0x16fb, B:563:0x1707, B:566:0x1718, B:568:0x1720, B:571:0x1731, B:572:0x1737, B:575:0x1743, B:578:0x174f, B:581:0x1760, B:583:0x1768, B:586:0x1779, B:587:0x177f, B:590:0x178b, B:593:0x1797, B:596:0x17a8, B:598:0x17b0, B:601:0x17c1, B:602:0x17c7, B:605:0x17d3, B:608:0x17df, B:611:0x17f0, B:613:0x17f8, B:616:0x1809, B:617:0x180f, B:620:0x181b, B:623:0x1827, B:626:0x1838, B:628:0x1840, B:631:0x1851, B:632:0x1857, B:635:0x1863, B:638:0x186f, B:641:0x1880, B:643:0x1888, B:646:0x18a0, B:647:0x18a6, B:650:0x18b7, B:651:0x18bd, B:654:0x18ce, B:656:0x18d4, B:659:0x18f8, B:660:0x18fe, B:663:0x1925, B:664:0x192b, B:667:0x1952, B:668:0x1958, B:671:0x197f, B:672:0x1985, B:675:0x19ae, B:676:0x19b4, B:679:0x19c5, B:680:0x19cb, B:683:0x19dc, B:684:0x19e2, B:687:0x19f3, B:688:0x19f9, B:691:0x1a0a, B:692:0x1a10, B:695:0x1a21, B:696:0x1a27, B:700:0x1a46, B:701:0x1a37, B:703:0x1a4c, B:706:0x1a5d, B:707:0x1a63, B:710:0x1a74, B:711:0x1a7a, B:714:0x1a92, B:715:0x1a98, B:718:0x1aa9, B:719:0x1aaf, B:722:0x1ac7, B:723:0x1acd, B:726:0x1ade, B:727:0x1ae4, B:730:0x1af5, B:731:0x1afb, B:734:0x1b0c, B:735:0x1b12, B:738:0x1b2a, B:739:0x1b2e, B:741:0x247d, B:743:0x1b34, B:746:0x1b52, B:747:0x1b58, B:750:0x1b70, B:751:0x1b74, B:752:0x1b77, B:755:0x1b88, B:756:0x1b8c, B:757:0x1b8f, B:760:0x1ba0, B:761:0x1ba4, B:762:0x1ba7, B:765:0x1bb8, B:766:0x1bbc, B:767:0x1bc0, B:770:0x1bd8, B:771:0x1bdc, B:772:0x1be0, B:775:0x1bf8, B:776:0x1c00, B:779:0x1c18, B:780:0x1c1c, B:781:0x1c20, B:784:0x1c31, B:785:0x1c35, B:786:0x1c39, B:788:0x1c3d, B:790:0x1c45, B:793:0x1c5d, B:794:0x1c76, B:795:0x2276, B:797:0x1c7b, B:800:0x1c8f, B:801:0x1ca7, B:804:0x1cb8, B:805:0x1cbc, B:806:0x1cc0, B:809:0x1cd1, B:810:0x1cd5, B:811:0x1cd9, B:814:0x1cea, B:815:0x1cee, B:816:0x1cf2, B:819:0x1d03, B:820:0x1d07, B:821:0x1d0b, B:824:0x1d17, B:825:0x1d1b, B:826:0x1d1f, B:829:0x1d30, B:830:0x1d34, B:831:0x1d38, B:834:0x1d50, B:837:0x1d5d, B:838:0x1d65, B:841:0x1d87, B:842:0x1d8b, B:845:0x1d8f, B:848:0x1dad, B:849:0x1db2, B:852:0x1dbe, B:853:0x1dc4, B:856:0x1de3, B:857:0x1de9, B:860:0x1e0a, B:861:0x1e10, B:864:0x1e31, B:865:0x1e37, B:868:0x1e58, B:869:0x1e5e, B:872:0x1e83, B:873:0x1e89, B:876:0x1e95, B:877:0x1e9b, B:880:0x1ea7, B:881:0x1ead, B:884:0x1eb9, B:885:0x1ebf, B:888:0x1ecb, B:889:0x1ed1, B:892:0x1ee2, B:893:0x1ee8, B:896:0x1ef9, B:897:0x1efd, B:898:0x1f01, B:901:0x1f12, B:902:0x1f18, B:905:0x1f24, B:906:0x1f2a, B:908:0x1f30, B:910:0x1f38, B:913:0x1f49, B:914:0x1f62, B:917:0x1f6e, B:918:0x1f72, B:919:0x1f76, B:922:0x1f82, B:923:0x1f88, B:926:0x1f94, B:927:0x1f9a, B:930:0x1fa6, B:931:0x1fac, B:934:0x1fb8, B:935:0x1fbe, B:938:0x1fca, B:939:0x1fd0, B:942:0x1fdc, B:945:0x1fe5, B:946:0x1fed, B:949:0x2005, B:952:0x200b, B:955:0x2023, B:956:0x2029, B:959:0x2035, B:962:0x203e, B:963:0x2046, B:966:0x205e, B:969:0x2064, B:972:0x207c, B:973:0x2082, B:976:0x20a4, B:977:0x20aa, B:980:0x20c8, B:981:0x20ce, B:984:0x20ee, B:985:0x20f3, B:988:0x2113, B:989:0x2118, B:992:0x2138, B:993:0x213d, B:996:0x215f, B:997:0x216d, B:1000:0x217e, B:1001:0x2184, B:1004:0x219c, B:1005:0x21a2, B:1008:0x21b3, B:1009:0x21b9, B:1012:0x21c5, B:1013:0x21cb, B:1016:0x21d7, B:1017:0x21dd, B:1020:0x21e9, B:1021:0x21ef, B:1024:0x21fe, B:1025:0x2204, B:1028:0x2213, B:1029:0x2219, B:1032:0x2228, B:1033:0x222e, B:1036:0x2238, B:1037:0x223e, B:1039:0x2244, B:1041:0x224c, B:1044:0x225d, B:1045:0x227c, B:1048:0x2288, B:1049:0x228e, B:1052:0x229a, B:1053:0x22a0, B:1056:0x22ac, B:1057:0x22b2, B:1058:0x22c2, B:1061:0x22ce, B:1062:0x22d6, B:1065:0x22e2, B:1066:0x22e8, B:1069:0x22f4, B:1070:0x22fc, B:1073:0x2308, B:1074:0x230e, B:1075:0x2317, B:1078:0x2323, B:1079:0x2329, B:1082:0x2335, B:1083:0x233b, B:1085:0x2349, B:1087:0x2353, B:1089:0x235b, B:1091:0x2369, B:1093:0x2373, B:1094:0x2378, B:1096:0x238d, B:1098:0x239d, B:1100:0x23af, B:1101:0x23ba, B:1103:0x23cc, B:1104:0x23d7, B:1107:0x23e1, B:1108:0x23e9, B:1111:0x23f3, B:1112:0x23fb, B:1115:0x240f, B:1116:0x2424, B:1119:0x2436, B:1120:0x243e, B:1123:0x244f, B:1124:0x2455, B:1127:0x2461, B:1128:0x2469, B:1131:0x2475, B:1132:0x248f, B:1134:0x249a, B:1139:0x0722, B:1143:0x0738, B:1146:0x074e, B:1149:0x0764, B:1152:0x077a, B:1155:0x0790, B:1158:0x07a6, B:1161:0x07bc, B:1164:0x07d2, B:1167:0x07e8, B:1170:0x07fe, B:1173:0x0814, B:1176:0x082a, B:1179:0x0840, B:1182:0x0856, B:1185:0x086c, B:1188:0x0882, B:1191:0x0898, B:1194:0x08ae, B:1197:0x08c4, B:1200:0x08da, B:1203:0x08f0, B:1206:0x0906, B:1209:0x091c, B:1212:0x0932, B:1215:0x0948, B:1218:0x095e, B:1221:0x0974, B:1224:0x0988, B:1227:0x099e, B:1230:0x09b4, B:1233:0x09ca, B:1236:0x09e0, B:1239:0x09f6, B:1242:0x0a0c, B:1245:0x0a22, B:1248:0x0a38, B:1251:0x0a4d, B:1254:0x0a63, B:1257:0x0a79, B:1260:0x0a8f, B:1263:0x0aa5, B:1266:0x0abb, B:1269:0x0ad1, B:1272:0x0ae7, B:1275:0x0afd, B:1278:0x0b13, B:1281:0x0b29, B:1284:0x0b3f, B:1287:0x0b55, B:1290:0x0b6b, B:1293:0x0b81, B:1296:0x0b96, B:1299:0x0bac, B:1302:0x0bc2, B:1305:0x0bd8, B:1308:0x0bee, B:1311:0x0c04, B:1314:0x0c1a, B:1317:0x0c30, B:1320:0x0c46, B:1323:0x0c5c, B:1326:0x0c72, B:1329:0x0c88, B:1332:0x0c9e, B:1335:0x0cb4, B:1338:0x0cca, B:1341:0x0ce0, B:1344:0x0cf6, B:1347:0x0d0c, B:1350:0x0d22, B:1353:0x0d38, B:1356:0x0d4e, B:1359:0x0d64, B:1362:0x0d7a, B:1365:0x0d90, B:1368:0x0da6, B:1371:0x0dbc, B:1374:0x0dd2, B:1377:0x0de8, B:1380:0x0dfe, B:1383:0x0e14, B:1386:0x0e2a, B:1389:0x0e40, B:1392:0x0e56, B:1395:0x0e6c, B:1398:0x0e82, B:1401:0x0e98, B:1404:0x0eac, B:1407:0x0ec2, B:1410:0x0ed8, B:1413:0x0eec, B:1416:0x0f02, B:1419:0x0f18, B:1422:0x0f2e, B:1425:0x0f44, B:1428:0x0f5a, B:1431:0x0f6e, B:1434:0x0f84, B:1437:0x0f9a, B:1442:0x0fb2, B:1445:0x0fc8, B:1448:0x0fde, B:1451:0x0ff4, B:1454:0x100a, B:1457:0x1020, B:1460:0x1036, B:1463:0x104b, B:1466:0x1061, B:1469:0x1077, B:1472:0x108d, B:1475:0x10a3, B:1478:0x10b9, B:1481:0x10cf, B:1484:0x10e5, B:1487:0x10fb, B:1490:0x1111, B:1493:0x1127, B:1496:0x113b, B:1499:0x1151, B:1502:0x1167, B:1505:0x117d, B:1508:0x1193, B:1511:0x11a7, B:1514:0x11bd, B:1517:0x11d3, B:1520:0x11e9, B:1523:0x11ff, B:1526:0x1215, B:1529:0x122b, B:1532:0x1241, B:1535:0x1257, B:1538:0x126d, B:1541:0x1283, B:1544:0x1299, B:1547:0x12ae, B:1550:0x12c3, B:1553:0x12d8, B:1556:0x12ed, B:1565:0x06c9), top: B:1582:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0694 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x06ff A[Catch: all -> 0x0632, TRY_ENTER, TryCatch #9 {all -> 0x0632, blocks: (B:1583:0x0628, B:232:0x0641, B:237:0x065a, B:239:0x066b, B:242:0x067c, B:245:0x0680, B:249:0x0696, B:251:0x0699, B:253:0x069f, B:258:0x06ff, B:260:0x0711, B:261:0x0715, B:268:0x1319, B:270:0x131d, B:276:0x24af, B:287:0x2501, B:289:0x250a, B:293:0x2519, B:297:0x2556, B:304:0x25b3, B:308:0x25d3, B:310:0x25d9, B:316:0x25f0, B:351:0x2566, B:354:0x2576, B:358:0x252d, B:365:0x1338, B:369:0x134c, B:372:0x135d, B:375:0x136a, B:378:0x137b, B:379:0x1381, B:382:0x138d, B:385:0x1399, B:388:0x13aa, B:390:0x13b2, B:393:0x13c3, B:394:0x13c9, B:397:0x13d5, B:400:0x13e1, B:403:0x13f2, B:405:0x13fa, B:408:0x140b, B:409:0x1411, B:412:0x141d, B:415:0x1429, B:418:0x143a, B:420:0x1442, B:423:0x1453, B:424:0x1459, B:427:0x1465, B:430:0x1471, B:433:0x1482, B:435:0x148a, B:438:0x149b, B:439:0x14a1, B:442:0x14ad, B:445:0x14b9, B:448:0x14ca, B:450:0x14d2, B:453:0x14e3, B:454:0x14e9, B:457:0x14f5, B:460:0x1501, B:463:0x1512, B:465:0x151a, B:468:0x1532, B:469:0x1538, B:472:0x1549, B:475:0x1555, B:478:0x1566, B:480:0x156e, B:483:0x1586, B:484:0x158c, B:487:0x159d, B:488:0x15a3, B:491:0x15af, B:494:0x15bb, B:497:0x15cc, B:499:0x15d4, B:502:0x15ec, B:503:0x15f2, B:506:0x1603, B:509:0x160f, B:512:0x1620, B:514:0x1628, B:517:0x1639, B:518:0x163f, B:521:0x164b, B:524:0x1657, B:526:0x165b, B:528:0x1663, B:531:0x1673, B:532:0x1679, B:535:0x1685, B:537:0x168d, B:539:0x1691, B:541:0x1699, B:544:0x16b0, B:545:0x16b6, B:548:0x16c7, B:549:0x16cd, B:551:0x16d1, B:553:0x16d9, B:556:0x16e9, B:557:0x16ef, B:560:0x16fb, B:563:0x1707, B:566:0x1718, B:568:0x1720, B:571:0x1731, B:572:0x1737, B:575:0x1743, B:578:0x174f, B:581:0x1760, B:583:0x1768, B:586:0x1779, B:587:0x177f, B:590:0x178b, B:593:0x1797, B:596:0x17a8, B:598:0x17b0, B:601:0x17c1, B:602:0x17c7, B:605:0x17d3, B:608:0x17df, B:611:0x17f0, B:613:0x17f8, B:616:0x1809, B:617:0x180f, B:620:0x181b, B:623:0x1827, B:626:0x1838, B:628:0x1840, B:631:0x1851, B:632:0x1857, B:635:0x1863, B:638:0x186f, B:641:0x1880, B:643:0x1888, B:646:0x18a0, B:647:0x18a6, B:650:0x18b7, B:651:0x18bd, B:654:0x18ce, B:656:0x18d4, B:659:0x18f8, B:660:0x18fe, B:663:0x1925, B:664:0x192b, B:667:0x1952, B:668:0x1958, B:671:0x197f, B:672:0x1985, B:675:0x19ae, B:676:0x19b4, B:679:0x19c5, B:680:0x19cb, B:683:0x19dc, B:684:0x19e2, B:687:0x19f3, B:688:0x19f9, B:691:0x1a0a, B:692:0x1a10, B:695:0x1a21, B:696:0x1a27, B:700:0x1a46, B:701:0x1a37, B:703:0x1a4c, B:706:0x1a5d, B:707:0x1a63, B:710:0x1a74, B:711:0x1a7a, B:714:0x1a92, B:715:0x1a98, B:718:0x1aa9, B:719:0x1aaf, B:722:0x1ac7, B:723:0x1acd, B:726:0x1ade, B:727:0x1ae4, B:730:0x1af5, B:731:0x1afb, B:734:0x1b0c, B:735:0x1b12, B:738:0x1b2a, B:739:0x1b2e, B:741:0x247d, B:743:0x1b34, B:746:0x1b52, B:747:0x1b58, B:750:0x1b70, B:751:0x1b74, B:752:0x1b77, B:755:0x1b88, B:756:0x1b8c, B:757:0x1b8f, B:760:0x1ba0, B:761:0x1ba4, B:762:0x1ba7, B:765:0x1bb8, B:766:0x1bbc, B:767:0x1bc0, B:770:0x1bd8, B:771:0x1bdc, B:772:0x1be0, B:775:0x1bf8, B:776:0x1c00, B:779:0x1c18, B:780:0x1c1c, B:781:0x1c20, B:784:0x1c31, B:785:0x1c35, B:786:0x1c39, B:788:0x1c3d, B:790:0x1c45, B:793:0x1c5d, B:794:0x1c76, B:795:0x2276, B:797:0x1c7b, B:800:0x1c8f, B:801:0x1ca7, B:804:0x1cb8, B:805:0x1cbc, B:806:0x1cc0, B:809:0x1cd1, B:810:0x1cd5, B:811:0x1cd9, B:814:0x1cea, B:815:0x1cee, B:816:0x1cf2, B:819:0x1d03, B:820:0x1d07, B:821:0x1d0b, B:824:0x1d17, B:825:0x1d1b, B:826:0x1d1f, B:829:0x1d30, B:830:0x1d34, B:831:0x1d38, B:834:0x1d50, B:837:0x1d5d, B:838:0x1d65, B:841:0x1d87, B:842:0x1d8b, B:845:0x1d8f, B:848:0x1dad, B:849:0x1db2, B:852:0x1dbe, B:853:0x1dc4, B:856:0x1de3, B:857:0x1de9, B:860:0x1e0a, B:861:0x1e10, B:864:0x1e31, B:865:0x1e37, B:868:0x1e58, B:869:0x1e5e, B:872:0x1e83, B:873:0x1e89, B:876:0x1e95, B:877:0x1e9b, B:880:0x1ea7, B:881:0x1ead, B:884:0x1eb9, B:885:0x1ebf, B:888:0x1ecb, B:889:0x1ed1, B:892:0x1ee2, B:893:0x1ee8, B:896:0x1ef9, B:897:0x1efd, B:898:0x1f01, B:901:0x1f12, B:902:0x1f18, B:905:0x1f24, B:906:0x1f2a, B:908:0x1f30, B:910:0x1f38, B:913:0x1f49, B:914:0x1f62, B:917:0x1f6e, B:918:0x1f72, B:919:0x1f76, B:922:0x1f82, B:923:0x1f88, B:926:0x1f94, B:927:0x1f9a, B:930:0x1fa6, B:931:0x1fac, B:934:0x1fb8, B:935:0x1fbe, B:938:0x1fca, B:939:0x1fd0, B:942:0x1fdc, B:945:0x1fe5, B:946:0x1fed, B:949:0x2005, B:952:0x200b, B:955:0x2023, B:956:0x2029, B:959:0x2035, B:962:0x203e, B:963:0x2046, B:966:0x205e, B:969:0x2064, B:972:0x207c, B:973:0x2082, B:976:0x20a4, B:977:0x20aa, B:980:0x20c8, B:981:0x20ce, B:984:0x20ee, B:985:0x20f3, B:988:0x2113, B:989:0x2118, B:992:0x2138, B:993:0x213d, B:996:0x215f, B:997:0x216d, B:1000:0x217e, B:1001:0x2184, B:1004:0x219c, B:1005:0x21a2, B:1008:0x21b3, B:1009:0x21b9, B:1012:0x21c5, B:1013:0x21cb, B:1016:0x21d7, B:1017:0x21dd, B:1020:0x21e9, B:1021:0x21ef, B:1024:0x21fe, B:1025:0x2204, B:1028:0x2213, B:1029:0x2219, B:1032:0x2228, B:1033:0x222e, B:1036:0x2238, B:1037:0x223e, B:1039:0x2244, B:1041:0x224c, B:1044:0x225d, B:1045:0x227c, B:1048:0x2288, B:1049:0x228e, B:1052:0x229a, B:1053:0x22a0, B:1056:0x22ac, B:1057:0x22b2, B:1058:0x22c2, B:1061:0x22ce, B:1062:0x22d6, B:1065:0x22e2, B:1066:0x22e8, B:1069:0x22f4, B:1070:0x22fc, B:1073:0x2308, B:1074:0x230e, B:1075:0x2317, B:1078:0x2323, B:1079:0x2329, B:1082:0x2335, B:1083:0x233b, B:1085:0x2349, B:1087:0x2353, B:1089:0x235b, B:1091:0x2369, B:1093:0x2373, B:1094:0x2378, B:1096:0x238d, B:1098:0x239d, B:1100:0x23af, B:1101:0x23ba, B:1103:0x23cc, B:1104:0x23d7, B:1107:0x23e1, B:1108:0x23e9, B:1111:0x23f3, B:1112:0x23fb, B:1115:0x240f, B:1116:0x2424, B:1119:0x2436, B:1120:0x243e, B:1123:0x244f, B:1124:0x2455, B:1127:0x2461, B:1128:0x2469, B:1131:0x2475, B:1132:0x248f, B:1134:0x249a, B:1139:0x0722, B:1143:0x0738, B:1146:0x074e, B:1149:0x0764, B:1152:0x077a, B:1155:0x0790, B:1158:0x07a6, B:1161:0x07bc, B:1164:0x07d2, B:1167:0x07e8, B:1170:0x07fe, B:1173:0x0814, B:1176:0x082a, B:1179:0x0840, B:1182:0x0856, B:1185:0x086c, B:1188:0x0882, B:1191:0x0898, B:1194:0x08ae, B:1197:0x08c4, B:1200:0x08da, B:1203:0x08f0, B:1206:0x0906, B:1209:0x091c, B:1212:0x0932, B:1215:0x0948, B:1218:0x095e, B:1221:0x0974, B:1224:0x0988, B:1227:0x099e, B:1230:0x09b4, B:1233:0x09ca, B:1236:0x09e0, B:1239:0x09f6, B:1242:0x0a0c, B:1245:0x0a22, B:1248:0x0a38, B:1251:0x0a4d, B:1254:0x0a63, B:1257:0x0a79, B:1260:0x0a8f, B:1263:0x0aa5, B:1266:0x0abb, B:1269:0x0ad1, B:1272:0x0ae7, B:1275:0x0afd, B:1278:0x0b13, B:1281:0x0b29, B:1284:0x0b3f, B:1287:0x0b55, B:1290:0x0b6b, B:1293:0x0b81, B:1296:0x0b96, B:1299:0x0bac, B:1302:0x0bc2, B:1305:0x0bd8, B:1308:0x0bee, B:1311:0x0c04, B:1314:0x0c1a, B:1317:0x0c30, B:1320:0x0c46, B:1323:0x0c5c, B:1326:0x0c72, B:1329:0x0c88, B:1332:0x0c9e, B:1335:0x0cb4, B:1338:0x0cca, B:1341:0x0ce0, B:1344:0x0cf6, B:1347:0x0d0c, B:1350:0x0d22, B:1353:0x0d38, B:1356:0x0d4e, B:1359:0x0d64, B:1362:0x0d7a, B:1365:0x0d90, B:1368:0x0da6, B:1371:0x0dbc, B:1374:0x0dd2, B:1377:0x0de8, B:1380:0x0dfe, B:1383:0x0e14, B:1386:0x0e2a, B:1389:0x0e40, B:1392:0x0e56, B:1395:0x0e6c, B:1398:0x0e82, B:1401:0x0e98, B:1404:0x0eac, B:1407:0x0ec2, B:1410:0x0ed8, B:1413:0x0eec, B:1416:0x0f02, B:1419:0x0f18, B:1422:0x0f2e, B:1425:0x0f44, B:1428:0x0f5a, B:1431:0x0f6e, B:1434:0x0f84, B:1437:0x0f9a, B:1442:0x0fb2, B:1445:0x0fc8, B:1448:0x0fde, B:1451:0x0ff4, B:1454:0x100a, B:1457:0x1020, B:1460:0x1036, B:1463:0x104b, B:1466:0x1061, B:1469:0x1077, B:1472:0x108d, B:1475:0x10a3, B:1478:0x10b9, B:1481:0x10cf, B:1484:0x10e5, B:1487:0x10fb, B:1490:0x1111, B:1493:0x1127, B:1496:0x113b, B:1499:0x1151, B:1502:0x1167, B:1505:0x117d, B:1508:0x1193, B:1511:0x11a7, B:1514:0x11bd, B:1517:0x11d3, B:1520:0x11e9, B:1523:0x11ff, B:1526:0x1215, B:1529:0x122b, B:1532:0x1241, B:1535:0x1257, B:1538:0x126d, B:1541:0x1283, B:1544:0x1299, B:1547:0x12ae, B:1550:0x12c3, B:1553:0x12d8, B:1556:0x12ed, B:1565:0x06c9), top: B:1582:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:276:0x24af A[Catch: all -> 0x0632, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x0632, blocks: (B:1583:0x0628, B:232:0x0641, B:237:0x065a, B:239:0x066b, B:242:0x067c, B:245:0x0680, B:249:0x0696, B:251:0x0699, B:253:0x069f, B:258:0x06ff, B:260:0x0711, B:261:0x0715, B:268:0x1319, B:270:0x131d, B:276:0x24af, B:287:0x2501, B:289:0x250a, B:293:0x2519, B:297:0x2556, B:304:0x25b3, B:308:0x25d3, B:310:0x25d9, B:316:0x25f0, B:351:0x2566, B:354:0x2576, B:358:0x252d, B:365:0x1338, B:369:0x134c, B:372:0x135d, B:375:0x136a, B:378:0x137b, B:379:0x1381, B:382:0x138d, B:385:0x1399, B:388:0x13aa, B:390:0x13b2, B:393:0x13c3, B:394:0x13c9, B:397:0x13d5, B:400:0x13e1, B:403:0x13f2, B:405:0x13fa, B:408:0x140b, B:409:0x1411, B:412:0x141d, B:415:0x1429, B:418:0x143a, B:420:0x1442, B:423:0x1453, B:424:0x1459, B:427:0x1465, B:430:0x1471, B:433:0x1482, B:435:0x148a, B:438:0x149b, B:439:0x14a1, B:442:0x14ad, B:445:0x14b9, B:448:0x14ca, B:450:0x14d2, B:453:0x14e3, B:454:0x14e9, B:457:0x14f5, B:460:0x1501, B:463:0x1512, B:465:0x151a, B:468:0x1532, B:469:0x1538, B:472:0x1549, B:475:0x1555, B:478:0x1566, B:480:0x156e, B:483:0x1586, B:484:0x158c, B:487:0x159d, B:488:0x15a3, B:491:0x15af, B:494:0x15bb, B:497:0x15cc, B:499:0x15d4, B:502:0x15ec, B:503:0x15f2, B:506:0x1603, B:509:0x160f, B:512:0x1620, B:514:0x1628, B:517:0x1639, B:518:0x163f, B:521:0x164b, B:524:0x1657, B:526:0x165b, B:528:0x1663, B:531:0x1673, B:532:0x1679, B:535:0x1685, B:537:0x168d, B:539:0x1691, B:541:0x1699, B:544:0x16b0, B:545:0x16b6, B:548:0x16c7, B:549:0x16cd, B:551:0x16d1, B:553:0x16d9, B:556:0x16e9, B:557:0x16ef, B:560:0x16fb, B:563:0x1707, B:566:0x1718, B:568:0x1720, B:571:0x1731, B:572:0x1737, B:575:0x1743, B:578:0x174f, B:581:0x1760, B:583:0x1768, B:586:0x1779, B:587:0x177f, B:590:0x178b, B:593:0x1797, B:596:0x17a8, B:598:0x17b0, B:601:0x17c1, B:602:0x17c7, B:605:0x17d3, B:608:0x17df, B:611:0x17f0, B:613:0x17f8, B:616:0x1809, B:617:0x180f, B:620:0x181b, B:623:0x1827, B:626:0x1838, B:628:0x1840, B:631:0x1851, B:632:0x1857, B:635:0x1863, B:638:0x186f, B:641:0x1880, B:643:0x1888, B:646:0x18a0, B:647:0x18a6, B:650:0x18b7, B:651:0x18bd, B:654:0x18ce, B:656:0x18d4, B:659:0x18f8, B:660:0x18fe, B:663:0x1925, B:664:0x192b, B:667:0x1952, B:668:0x1958, B:671:0x197f, B:672:0x1985, B:675:0x19ae, B:676:0x19b4, B:679:0x19c5, B:680:0x19cb, B:683:0x19dc, B:684:0x19e2, B:687:0x19f3, B:688:0x19f9, B:691:0x1a0a, B:692:0x1a10, B:695:0x1a21, B:696:0x1a27, B:700:0x1a46, B:701:0x1a37, B:703:0x1a4c, B:706:0x1a5d, B:707:0x1a63, B:710:0x1a74, B:711:0x1a7a, B:714:0x1a92, B:715:0x1a98, B:718:0x1aa9, B:719:0x1aaf, B:722:0x1ac7, B:723:0x1acd, B:726:0x1ade, B:727:0x1ae4, B:730:0x1af5, B:731:0x1afb, B:734:0x1b0c, B:735:0x1b12, B:738:0x1b2a, B:739:0x1b2e, B:741:0x247d, B:743:0x1b34, B:746:0x1b52, B:747:0x1b58, B:750:0x1b70, B:751:0x1b74, B:752:0x1b77, B:755:0x1b88, B:756:0x1b8c, B:757:0x1b8f, B:760:0x1ba0, B:761:0x1ba4, B:762:0x1ba7, B:765:0x1bb8, B:766:0x1bbc, B:767:0x1bc0, B:770:0x1bd8, B:771:0x1bdc, B:772:0x1be0, B:775:0x1bf8, B:776:0x1c00, B:779:0x1c18, B:780:0x1c1c, B:781:0x1c20, B:784:0x1c31, B:785:0x1c35, B:786:0x1c39, B:788:0x1c3d, B:790:0x1c45, B:793:0x1c5d, B:794:0x1c76, B:795:0x2276, B:797:0x1c7b, B:800:0x1c8f, B:801:0x1ca7, B:804:0x1cb8, B:805:0x1cbc, B:806:0x1cc0, B:809:0x1cd1, B:810:0x1cd5, B:811:0x1cd9, B:814:0x1cea, B:815:0x1cee, B:816:0x1cf2, B:819:0x1d03, B:820:0x1d07, B:821:0x1d0b, B:824:0x1d17, B:825:0x1d1b, B:826:0x1d1f, B:829:0x1d30, B:830:0x1d34, B:831:0x1d38, B:834:0x1d50, B:837:0x1d5d, B:838:0x1d65, B:841:0x1d87, B:842:0x1d8b, B:845:0x1d8f, B:848:0x1dad, B:849:0x1db2, B:852:0x1dbe, B:853:0x1dc4, B:856:0x1de3, B:857:0x1de9, B:860:0x1e0a, B:861:0x1e10, B:864:0x1e31, B:865:0x1e37, B:868:0x1e58, B:869:0x1e5e, B:872:0x1e83, B:873:0x1e89, B:876:0x1e95, B:877:0x1e9b, B:880:0x1ea7, B:881:0x1ead, B:884:0x1eb9, B:885:0x1ebf, B:888:0x1ecb, B:889:0x1ed1, B:892:0x1ee2, B:893:0x1ee8, B:896:0x1ef9, B:897:0x1efd, B:898:0x1f01, B:901:0x1f12, B:902:0x1f18, B:905:0x1f24, B:906:0x1f2a, B:908:0x1f30, B:910:0x1f38, B:913:0x1f49, B:914:0x1f62, B:917:0x1f6e, B:918:0x1f72, B:919:0x1f76, B:922:0x1f82, B:923:0x1f88, B:926:0x1f94, B:927:0x1f9a, B:930:0x1fa6, B:931:0x1fac, B:934:0x1fb8, B:935:0x1fbe, B:938:0x1fca, B:939:0x1fd0, B:942:0x1fdc, B:945:0x1fe5, B:946:0x1fed, B:949:0x2005, B:952:0x200b, B:955:0x2023, B:956:0x2029, B:959:0x2035, B:962:0x203e, B:963:0x2046, B:966:0x205e, B:969:0x2064, B:972:0x207c, B:973:0x2082, B:976:0x20a4, B:977:0x20aa, B:980:0x20c8, B:981:0x20ce, B:984:0x20ee, B:985:0x20f3, B:988:0x2113, B:989:0x2118, B:992:0x2138, B:993:0x213d, B:996:0x215f, B:997:0x216d, B:1000:0x217e, B:1001:0x2184, B:1004:0x219c, B:1005:0x21a2, B:1008:0x21b3, B:1009:0x21b9, B:1012:0x21c5, B:1013:0x21cb, B:1016:0x21d7, B:1017:0x21dd, B:1020:0x21e9, B:1021:0x21ef, B:1024:0x21fe, B:1025:0x2204, B:1028:0x2213, B:1029:0x2219, B:1032:0x2228, B:1033:0x222e, B:1036:0x2238, B:1037:0x223e, B:1039:0x2244, B:1041:0x224c, B:1044:0x225d, B:1045:0x227c, B:1048:0x2288, B:1049:0x228e, B:1052:0x229a, B:1053:0x22a0, B:1056:0x22ac, B:1057:0x22b2, B:1058:0x22c2, B:1061:0x22ce, B:1062:0x22d6, B:1065:0x22e2, B:1066:0x22e8, B:1069:0x22f4, B:1070:0x22fc, B:1073:0x2308, B:1074:0x230e, B:1075:0x2317, B:1078:0x2323, B:1079:0x2329, B:1082:0x2335, B:1083:0x233b, B:1085:0x2349, B:1087:0x2353, B:1089:0x235b, B:1091:0x2369, B:1093:0x2373, B:1094:0x2378, B:1096:0x238d, B:1098:0x239d, B:1100:0x23af, B:1101:0x23ba, B:1103:0x23cc, B:1104:0x23d7, B:1107:0x23e1, B:1108:0x23e9, B:1111:0x23f3, B:1112:0x23fb, B:1115:0x240f, B:1116:0x2424, B:1119:0x2436, B:1120:0x243e, B:1123:0x244f, B:1124:0x2455, B:1127:0x2461, B:1128:0x2469, B:1131:0x2475, B:1132:0x248f, B:1134:0x249a, B:1139:0x0722, B:1143:0x0738, B:1146:0x074e, B:1149:0x0764, B:1152:0x077a, B:1155:0x0790, B:1158:0x07a6, B:1161:0x07bc, B:1164:0x07d2, B:1167:0x07e8, B:1170:0x07fe, B:1173:0x0814, B:1176:0x082a, B:1179:0x0840, B:1182:0x0856, B:1185:0x086c, B:1188:0x0882, B:1191:0x0898, B:1194:0x08ae, B:1197:0x08c4, B:1200:0x08da, B:1203:0x08f0, B:1206:0x0906, B:1209:0x091c, B:1212:0x0932, B:1215:0x0948, B:1218:0x095e, B:1221:0x0974, B:1224:0x0988, B:1227:0x099e, B:1230:0x09b4, B:1233:0x09ca, B:1236:0x09e0, B:1239:0x09f6, B:1242:0x0a0c, B:1245:0x0a22, B:1248:0x0a38, B:1251:0x0a4d, B:1254:0x0a63, B:1257:0x0a79, B:1260:0x0a8f, B:1263:0x0aa5, B:1266:0x0abb, B:1269:0x0ad1, B:1272:0x0ae7, B:1275:0x0afd, B:1278:0x0b13, B:1281:0x0b29, B:1284:0x0b3f, B:1287:0x0b55, B:1290:0x0b6b, B:1293:0x0b81, B:1296:0x0b96, B:1299:0x0bac, B:1302:0x0bc2, B:1305:0x0bd8, B:1308:0x0bee, B:1311:0x0c04, B:1314:0x0c1a, B:1317:0x0c30, B:1320:0x0c46, B:1323:0x0c5c, B:1326:0x0c72, B:1329:0x0c88, B:1332:0x0c9e, B:1335:0x0cb4, B:1338:0x0cca, B:1341:0x0ce0, B:1344:0x0cf6, B:1347:0x0d0c, B:1350:0x0d22, B:1353:0x0d38, B:1356:0x0d4e, B:1359:0x0d64, B:1362:0x0d7a, B:1365:0x0d90, B:1368:0x0da6, B:1371:0x0dbc, B:1374:0x0dd2, B:1377:0x0de8, B:1380:0x0dfe, B:1383:0x0e14, B:1386:0x0e2a, B:1389:0x0e40, B:1392:0x0e56, B:1395:0x0e6c, B:1398:0x0e82, B:1401:0x0e98, B:1404:0x0eac, B:1407:0x0ec2, B:1410:0x0ed8, B:1413:0x0eec, B:1416:0x0f02, B:1419:0x0f18, B:1422:0x0f2e, B:1425:0x0f44, B:1428:0x0f5a, B:1431:0x0f6e, B:1434:0x0f84, B:1437:0x0f9a, B:1442:0x0fb2, B:1445:0x0fc8, B:1448:0x0fde, B:1451:0x0ff4, B:1454:0x100a, B:1457:0x1020, B:1460:0x1036, B:1463:0x104b, B:1466:0x1061, B:1469:0x1077, B:1472:0x108d, B:1475:0x10a3, B:1478:0x10b9, B:1481:0x10cf, B:1484:0x10e5, B:1487:0x10fb, B:1490:0x1111, B:1493:0x1127, B:1496:0x113b, B:1499:0x1151, B:1502:0x1167, B:1505:0x117d, B:1508:0x1193, B:1511:0x11a7, B:1514:0x11bd, B:1517:0x11d3, B:1520:0x11e9, B:1523:0x11ff, B:1526:0x1215, B:1529:0x122b, B:1532:0x1241, B:1535:0x1257, B:1538:0x126d, B:1541:0x1283, B:1544:0x1299, B:1547:0x12ae, B:1550:0x12c3, B:1553:0x12d8, B:1556:0x12ed, B:1565:0x06c9), top: B:1582:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:278:0x24da A[Catch: all -> 0x2655, TRY_ENTER, TryCatch #11 {all -> 0x2655, blocks: (B:221:0x05fe, B:227:0x0620, B:230:0x0639, B:235:0x064e, B:246:0x0686, B:256:0x06f9, B:274:0x24ab, B:278:0x24da, B:282:0x24ea, B:285:0x24f6, B:290:0x2511, B:294:0x254a, B:302:0x258e, B:305:0x25c7, B:313:0x25e4, B:319:0x25fc, B:321:0x262e, B:323:0x2632, B:325:0x2636, B:327:0x263a, B:332:0x2644, B:355:0x2582, B:361:0x253d, B:1560:0x24a5, B:1569:0x06d4, B:1576:0x06eb), top: B:220:0x05fe }] */
    /* JADX WARN: Removed duplicated region for block: B:304:0x25b3 A[Catch: all -> 0x0632, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x0632, blocks: (B:1583:0x0628, B:232:0x0641, B:237:0x065a, B:239:0x066b, B:242:0x067c, B:245:0x0680, B:249:0x0696, B:251:0x0699, B:253:0x069f, B:258:0x06ff, B:260:0x0711, B:261:0x0715, B:268:0x1319, B:270:0x131d, B:276:0x24af, B:287:0x2501, B:289:0x250a, B:293:0x2519, B:297:0x2556, B:304:0x25b3, B:308:0x25d3, B:310:0x25d9, B:316:0x25f0, B:351:0x2566, B:354:0x2576, B:358:0x252d, B:365:0x1338, B:369:0x134c, B:372:0x135d, B:375:0x136a, B:378:0x137b, B:379:0x1381, B:382:0x138d, B:385:0x1399, B:388:0x13aa, B:390:0x13b2, B:393:0x13c3, B:394:0x13c9, B:397:0x13d5, B:400:0x13e1, B:403:0x13f2, B:405:0x13fa, B:408:0x140b, B:409:0x1411, B:412:0x141d, B:415:0x1429, B:418:0x143a, B:420:0x1442, B:423:0x1453, B:424:0x1459, B:427:0x1465, B:430:0x1471, B:433:0x1482, B:435:0x148a, B:438:0x149b, B:439:0x14a1, B:442:0x14ad, B:445:0x14b9, B:448:0x14ca, B:450:0x14d2, B:453:0x14e3, B:454:0x14e9, B:457:0x14f5, B:460:0x1501, B:463:0x1512, B:465:0x151a, B:468:0x1532, B:469:0x1538, B:472:0x1549, B:475:0x1555, B:478:0x1566, B:480:0x156e, B:483:0x1586, B:484:0x158c, B:487:0x159d, B:488:0x15a3, B:491:0x15af, B:494:0x15bb, B:497:0x15cc, B:499:0x15d4, B:502:0x15ec, B:503:0x15f2, B:506:0x1603, B:509:0x160f, B:512:0x1620, B:514:0x1628, B:517:0x1639, B:518:0x163f, B:521:0x164b, B:524:0x1657, B:526:0x165b, B:528:0x1663, B:531:0x1673, B:532:0x1679, B:535:0x1685, B:537:0x168d, B:539:0x1691, B:541:0x1699, B:544:0x16b0, B:545:0x16b6, B:548:0x16c7, B:549:0x16cd, B:551:0x16d1, B:553:0x16d9, B:556:0x16e9, B:557:0x16ef, B:560:0x16fb, B:563:0x1707, B:566:0x1718, B:568:0x1720, B:571:0x1731, B:572:0x1737, B:575:0x1743, B:578:0x174f, B:581:0x1760, B:583:0x1768, B:586:0x1779, B:587:0x177f, B:590:0x178b, B:593:0x1797, B:596:0x17a8, B:598:0x17b0, B:601:0x17c1, B:602:0x17c7, B:605:0x17d3, B:608:0x17df, B:611:0x17f0, B:613:0x17f8, B:616:0x1809, B:617:0x180f, B:620:0x181b, B:623:0x1827, B:626:0x1838, B:628:0x1840, B:631:0x1851, B:632:0x1857, B:635:0x1863, B:638:0x186f, B:641:0x1880, B:643:0x1888, B:646:0x18a0, B:647:0x18a6, B:650:0x18b7, B:651:0x18bd, B:654:0x18ce, B:656:0x18d4, B:659:0x18f8, B:660:0x18fe, B:663:0x1925, B:664:0x192b, B:667:0x1952, B:668:0x1958, B:671:0x197f, B:672:0x1985, B:675:0x19ae, B:676:0x19b4, B:679:0x19c5, B:680:0x19cb, B:683:0x19dc, B:684:0x19e2, B:687:0x19f3, B:688:0x19f9, B:691:0x1a0a, B:692:0x1a10, B:695:0x1a21, B:696:0x1a27, B:700:0x1a46, B:701:0x1a37, B:703:0x1a4c, B:706:0x1a5d, B:707:0x1a63, B:710:0x1a74, B:711:0x1a7a, B:714:0x1a92, B:715:0x1a98, B:718:0x1aa9, B:719:0x1aaf, B:722:0x1ac7, B:723:0x1acd, B:726:0x1ade, B:727:0x1ae4, B:730:0x1af5, B:731:0x1afb, B:734:0x1b0c, B:735:0x1b12, B:738:0x1b2a, B:739:0x1b2e, B:741:0x247d, B:743:0x1b34, B:746:0x1b52, B:747:0x1b58, B:750:0x1b70, B:751:0x1b74, B:752:0x1b77, B:755:0x1b88, B:756:0x1b8c, B:757:0x1b8f, B:760:0x1ba0, B:761:0x1ba4, B:762:0x1ba7, B:765:0x1bb8, B:766:0x1bbc, B:767:0x1bc0, B:770:0x1bd8, B:771:0x1bdc, B:772:0x1be0, B:775:0x1bf8, B:776:0x1c00, B:779:0x1c18, B:780:0x1c1c, B:781:0x1c20, B:784:0x1c31, B:785:0x1c35, B:786:0x1c39, B:788:0x1c3d, B:790:0x1c45, B:793:0x1c5d, B:794:0x1c76, B:795:0x2276, B:797:0x1c7b, B:800:0x1c8f, B:801:0x1ca7, B:804:0x1cb8, B:805:0x1cbc, B:806:0x1cc0, B:809:0x1cd1, B:810:0x1cd5, B:811:0x1cd9, B:814:0x1cea, B:815:0x1cee, B:816:0x1cf2, B:819:0x1d03, B:820:0x1d07, B:821:0x1d0b, B:824:0x1d17, B:825:0x1d1b, B:826:0x1d1f, B:829:0x1d30, B:830:0x1d34, B:831:0x1d38, B:834:0x1d50, B:837:0x1d5d, B:838:0x1d65, B:841:0x1d87, B:842:0x1d8b, B:845:0x1d8f, B:848:0x1dad, B:849:0x1db2, B:852:0x1dbe, B:853:0x1dc4, B:856:0x1de3, B:857:0x1de9, B:860:0x1e0a, B:861:0x1e10, B:864:0x1e31, B:865:0x1e37, B:868:0x1e58, B:869:0x1e5e, B:872:0x1e83, B:873:0x1e89, B:876:0x1e95, B:877:0x1e9b, B:880:0x1ea7, B:881:0x1ead, B:884:0x1eb9, B:885:0x1ebf, B:888:0x1ecb, B:889:0x1ed1, B:892:0x1ee2, B:893:0x1ee8, B:896:0x1ef9, B:897:0x1efd, B:898:0x1f01, B:901:0x1f12, B:902:0x1f18, B:905:0x1f24, B:906:0x1f2a, B:908:0x1f30, B:910:0x1f38, B:913:0x1f49, B:914:0x1f62, B:917:0x1f6e, B:918:0x1f72, B:919:0x1f76, B:922:0x1f82, B:923:0x1f88, B:926:0x1f94, B:927:0x1f9a, B:930:0x1fa6, B:931:0x1fac, B:934:0x1fb8, B:935:0x1fbe, B:938:0x1fca, B:939:0x1fd0, B:942:0x1fdc, B:945:0x1fe5, B:946:0x1fed, B:949:0x2005, B:952:0x200b, B:955:0x2023, B:956:0x2029, B:959:0x2035, B:962:0x203e, B:963:0x2046, B:966:0x205e, B:969:0x2064, B:972:0x207c, B:973:0x2082, B:976:0x20a4, B:977:0x20aa, B:980:0x20c8, B:981:0x20ce, B:984:0x20ee, B:985:0x20f3, B:988:0x2113, B:989:0x2118, B:992:0x2138, B:993:0x213d, B:996:0x215f, B:997:0x216d, B:1000:0x217e, B:1001:0x2184, B:1004:0x219c, B:1005:0x21a2, B:1008:0x21b3, B:1009:0x21b9, B:1012:0x21c5, B:1013:0x21cb, B:1016:0x21d7, B:1017:0x21dd, B:1020:0x21e9, B:1021:0x21ef, B:1024:0x21fe, B:1025:0x2204, B:1028:0x2213, B:1029:0x2219, B:1032:0x2228, B:1033:0x222e, B:1036:0x2238, B:1037:0x223e, B:1039:0x2244, B:1041:0x224c, B:1044:0x225d, B:1045:0x227c, B:1048:0x2288, B:1049:0x228e, B:1052:0x229a, B:1053:0x22a0, B:1056:0x22ac, B:1057:0x22b2, B:1058:0x22c2, B:1061:0x22ce, B:1062:0x22d6, B:1065:0x22e2, B:1066:0x22e8, B:1069:0x22f4, B:1070:0x22fc, B:1073:0x2308, B:1074:0x230e, B:1075:0x2317, B:1078:0x2323, B:1079:0x2329, B:1082:0x2335, B:1083:0x233b, B:1085:0x2349, B:1087:0x2353, B:1089:0x235b, B:1091:0x2369, B:1093:0x2373, B:1094:0x2378, B:1096:0x238d, B:1098:0x239d, B:1100:0x23af, B:1101:0x23ba, B:1103:0x23cc, B:1104:0x23d7, B:1107:0x23e1, B:1108:0x23e9, B:1111:0x23f3, B:1112:0x23fb, B:1115:0x240f, B:1116:0x2424, B:1119:0x2436, B:1120:0x243e, B:1123:0x244f, B:1124:0x2455, B:1127:0x2461, B:1128:0x2469, B:1131:0x2475, B:1132:0x248f, B:1134:0x249a, B:1139:0x0722, B:1143:0x0738, B:1146:0x074e, B:1149:0x0764, B:1152:0x077a, B:1155:0x0790, B:1158:0x07a6, B:1161:0x07bc, B:1164:0x07d2, B:1167:0x07e8, B:1170:0x07fe, B:1173:0x0814, B:1176:0x082a, B:1179:0x0840, B:1182:0x0856, B:1185:0x086c, B:1188:0x0882, B:1191:0x0898, B:1194:0x08ae, B:1197:0x08c4, B:1200:0x08da, B:1203:0x08f0, B:1206:0x0906, B:1209:0x091c, B:1212:0x0932, B:1215:0x0948, B:1218:0x095e, B:1221:0x0974, B:1224:0x0988, B:1227:0x099e, B:1230:0x09b4, B:1233:0x09ca, B:1236:0x09e0, B:1239:0x09f6, B:1242:0x0a0c, B:1245:0x0a22, B:1248:0x0a38, B:1251:0x0a4d, B:1254:0x0a63, B:1257:0x0a79, B:1260:0x0a8f, B:1263:0x0aa5, B:1266:0x0abb, B:1269:0x0ad1, B:1272:0x0ae7, B:1275:0x0afd, B:1278:0x0b13, B:1281:0x0b29, B:1284:0x0b3f, B:1287:0x0b55, B:1290:0x0b6b, B:1293:0x0b81, B:1296:0x0b96, B:1299:0x0bac, B:1302:0x0bc2, B:1305:0x0bd8, B:1308:0x0bee, B:1311:0x0c04, B:1314:0x0c1a, B:1317:0x0c30, B:1320:0x0c46, B:1323:0x0c5c, B:1326:0x0c72, B:1329:0x0c88, B:1332:0x0c9e, B:1335:0x0cb4, B:1338:0x0cca, B:1341:0x0ce0, B:1344:0x0cf6, B:1347:0x0d0c, B:1350:0x0d22, B:1353:0x0d38, B:1356:0x0d4e, B:1359:0x0d64, B:1362:0x0d7a, B:1365:0x0d90, B:1368:0x0da6, B:1371:0x0dbc, B:1374:0x0dd2, B:1377:0x0de8, B:1380:0x0dfe, B:1383:0x0e14, B:1386:0x0e2a, B:1389:0x0e40, B:1392:0x0e56, B:1395:0x0e6c, B:1398:0x0e82, B:1401:0x0e98, B:1404:0x0eac, B:1407:0x0ec2, B:1410:0x0ed8, B:1413:0x0eec, B:1416:0x0f02, B:1419:0x0f18, B:1422:0x0f2e, B:1425:0x0f44, B:1428:0x0f5a, B:1431:0x0f6e, B:1434:0x0f84, B:1437:0x0f9a, B:1442:0x0fb2, B:1445:0x0fc8, B:1448:0x0fde, B:1451:0x0ff4, B:1454:0x100a, B:1457:0x1020, B:1460:0x1036, B:1463:0x104b, B:1466:0x1061, B:1469:0x1077, B:1472:0x108d, B:1475:0x10a3, B:1478:0x10b9, B:1481:0x10cf, B:1484:0x10e5, B:1487:0x10fb, B:1490:0x1111, B:1493:0x1127, B:1496:0x113b, B:1499:0x1151, B:1502:0x1167, B:1505:0x117d, B:1508:0x1193, B:1511:0x11a7, B:1514:0x11bd, B:1517:0x11d3, B:1520:0x11e9, B:1523:0x11ff, B:1526:0x1215, B:1529:0x122b, B:1532:0x1241, B:1535:0x1257, B:1538:0x126d, B:1541:0x1283, B:1544:0x1299, B:1547:0x12ae, B:1550:0x12c3, B:1553:0x12d8, B:1556:0x12ed, B:1565:0x06c9), top: B:1582:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x25ee  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x262e A[Catch: all -> 0x2655, TryCatch #11 {all -> 0x2655, blocks: (B:221:0x05fe, B:227:0x0620, B:230:0x0639, B:235:0x064e, B:246:0x0686, B:256:0x06f9, B:274:0x24ab, B:278:0x24da, B:282:0x24ea, B:285:0x24f6, B:290:0x2511, B:294:0x254a, B:302:0x258e, B:305:0x25c7, B:313:0x25e4, B:319:0x25fc, B:321:0x262e, B:323:0x2632, B:325:0x2636, B:327:0x263a, B:332:0x2644, B:355:0x2582, B:361:0x253d, B:1560:0x24a5, B:1569:0x06d4, B:1576:0x06eb), top: B:220:0x05fe }] */
    /* JADX WARN: Removed duplicated region for block: B:346:0x25f9  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x2666  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x274b  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x2762  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x275b  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01ec A[Catch: all -> 0x0197, TRY_ENTER, TryCatch #13 {all -> 0x0197, blocks: (B:1687:0x0192, B:56:0x01aa, B:58:0x01b3, B:63:0x01ec, B:69:0x0203, B:71:0x0207, B:72:0x021b, B:65:0x01fb, B:1675:0x01be, B:1678:0x01c9, B:1679:0x01d6, B:1683:0x01d0), top: B:1686:0x0192 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0203 A[Catch: all -> 0x0197, TryCatch #13 {all -> 0x0197, blocks: (B:1687:0x0192, B:56:0x01aa, B:58:0x01b3, B:63:0x01ec, B:69:0x0203, B:71:0x0207, B:72:0x021b, B:65:0x01fb, B:1675:0x01be, B:1678:0x01c9, B:1679:0x01d6, B:1683:0x01d0), top: B:1686:0x0192 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0221 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$processRemoteMessage$6(String str, String str2, long j) {
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
        long j2;
        long j3;
        long j4;
        int i6;
        long j5;
        int i7;
        long j6;
        JSONObject jSONObject2;
        boolean z2;
        final int i8;
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
        boolean z6;
        JSONObject jSONObject3;
        long j8;
        String[] strArr;
        String str16;
        String str17;
        boolean z7;
        boolean z8;
        boolean z9;
        String str18;
        String str19;
        boolean z10;
        String str20;
        String str21;
        String reactedText;
        String str22;
        boolean z11;
        final int i12;
        long j9;
        long j10;
        boolean z12;
        MessageObject messageObject;
        Object obj3;
        boolean z13;
        Object obj4;
        Object obj5;
        char c2;
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
                                                final int i22 = i5;
                                                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda2
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        PushListenerController.lambda$processRemoteMessage$4(i22);
                                                    }
                                                });
                                                countDownLatch.countDown();
                                                return;
                                            }
                                            if (jSONObject.has("channel_id")) {
                                                try {
                                                    j2 = jSONObject.getLong("channel_id");
                                                    str2 = str27;
                                                    j3 = -j2;
                                                } catch (Throwable th4) {
                                                    th = th4;
                                                    str2 = str27;
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
                                                str2 = str27;
                                                j3 = 0;
                                                j2 = 0;
                                            }
                                            try {
                                                if (jSONObject.has("from_id")) {
                                                    try {
                                                        j3 = jSONObject.getLong("from_id");
                                                        j4 = j3;
                                                    } catch (Throwable th5) {
                                                        th = th5;
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
                                                        long j11 = jSONObject.getLong("chat_id");
                                                        i6 = i5;
                                                        j3 = -j11;
                                                        j5 = j11;
                                                    } catch (Throwable th6) {
                                                        th = th6;
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
                                                } else {
                                                    i6 = i5;
                                                    j5 = 0;
                                                }
                                                try {
                                                    if (jSONObject.has("topic_id")) {
                                                        try {
                                                            i7 = jSONObject.getInt("topic_id");
                                                            j6 = j3;
                                                        } catch (Throwable th7) {
                                                            th = th7;
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
                                                    } else {
                                                        j6 = j3;
                                                        i7 = 0;
                                                    }
                                                    FileLog.d("recived push notification {" + str7 + "} chatId " + j5 + " custom topicId " + i7);
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
                                                                boolean z14 = z2;
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
                                                                    MessagesController.getInstance(i6).processUpdateArray(arrayList, null, null, false, 0);
                                                                } else if ("READ_STORIES".equals(str7)) {
                                                                    NotificationsController.getInstance(i6).processReadStories(makeEncryptedDialogId, jSONObject.getInt("max_id"));
                                                                } else {
                                                                    long j12 = j4;
                                                                    if (!"STORY_DELETED".equals(str7)) {
                                                                        long j13 = j5;
                                                                        if ("MESSAGE_DELETED".equals(str7)) {
                                                                            String[] split2 = jSONObject.getString("messages").split(",");
                                                                            LongSparseArray longSparseArray = new LongSparseArray();
                                                                            ArrayList<Integer> arrayList2 = new ArrayList<>();
                                                                            for (String str29 : split2) {
                                                                                arrayList2.add(Utilities.parseInt((CharSequence) str29));
                                                                            }
                                                                            longSparseArray.put(-j2, arrayList2);
                                                                            NotificationsController.getInstance(i6).removeDeletedMessagesFromNotifications(longSparseArray, false);
                                                                            MessagesController.getInstance(i6).deleteMessagesByPush(makeEncryptedDialogId, arrayList2, j2);
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
                                                                            NotificationsController.getInstance(i6).removeDeletedMessagesFromNotifications(longSparseArray2, true);
                                                                            MessagesController.getInstance(i6).checkUnreadReactions(makeEncryptedDialogId, i7, sparseBooleanArray);
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
                                                                                    i10 = i7;
                                                                                    str14 = " for dialogId = ";
                                                                                    Integer num = MessagesController.getInstance(i6).dialogs_read_inbox_max.get(Long.valueOf(makeEncryptedDialogId));
                                                                                    if (num == null) {
                                                                                        num = Integer.valueOf(MessagesStorage.getInstance(i6).getDialogReadMax(false, makeEncryptedDialogId));
                                                                                        MessagesController.getInstance(i6).dialogs_read_inbox_max.put(Long.valueOf(makeEncryptedDialogId), num);
                                                                                    }
                                                                                    if (i9 > num.intValue()) {
                                                                                        str15 = str10;
                                                                                        z3 = true;
                                                                                    }
                                                                                    str15 = str10;
                                                                                    z3 = false;
                                                                                } else {
                                                                                    i10 = i7;
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
                                                                                    int i27 = i6;
                                                                                    long j14 = j7;
                                                                                    try {
                                                                                        long optLong = jSONObject.optLong("chat_from_id", 0L);
                                                                                        String str30 = str13;
                                                                                        Object obj7 = "STORY_HIDDEN_AUTHOR";
                                                                                        Object obj8 = obj2;
                                                                                        long optLong2 = jSONObject.optLong("chat_from_broadcast_id", 0L);
                                                                                        long optLong3 = jSONObject.optLong("chat_from_group_id", 0L);
                                                                                        if (optLong == 0 && optLong3 == 0) {
                                                                                            z5 = false;
                                                                                            if (jSONObject.has("mention")) {
                                                                                                try {
                                                                                                    if (jSONObject.getInt("mention") != 0) {
                                                                                                        z6 = true;
                                                                                                        boolean z15 = (jSONObject.has("silent") || jSONObject.getInt("silent") == 0) ? false : true;
                                                                                                        boolean z16 = z6;
                                                                                                        jSONObject3 = jSONObject2;
                                                                                                        if (jSONObject3.has("loc_args")) {
                                                                                                            JSONArray jSONArray = jSONObject3.getJSONArray("loc_args");
                                                                                                            int length = jSONArray.length();
                                                                                                            j8 = optLong;
                                                                                                            strArr = new String[length];
                                                                                                            for (int i28 = 0; i28 < length; i28++) {
                                                                                                                strArr[i28] = jSONArray.getString(i28);
                                                                                                            }
                                                                                                        } else {
                                                                                                            j8 = optLong;
                                                                                                            strArr = null;
                                                                                                        }
                                                                                                        if (strArr != null && strArr.length > 0) {
                                                                                                            str16 = strArr[0];
                                                                                                            boolean has = jSONObject.has("edit_date");
                                                                                                            if (str7.startsWith("CHAT_") || strArr == null || strArr.length <= 0) {
                                                                                                                if (str7.startsWith("PINNED_")) {
                                                                                                                    z9 = j2 != 0;
                                                                                                                    str17 = null;
                                                                                                                    z7 = true;
                                                                                                                    z8 = false;
                                                                                                                    if (!str7.startsWith(str15) || str7.startsWith("CHAT_REACT_")) {
                                                                                                                        str18 = "CHAT_REACT_";
                                                                                                                        str19 = str15;
                                                                                                                        z10 = has;
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
                                                                                                                                    c2 = '\"';
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
                                                                                                                                    c2 = '8';
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
                                                                                                                                    c2 = 'Y';
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
                                                                                                                                    c2 = 25;
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
                                                                                                                                    c2 = 'A';
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
                                                                                                                                    c2 = '5';
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
                                                                                                                                    c2 = ';';
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
                                                                                                                                    c2 = '=';
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
                                                                                                                                    c2 = ']';
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
                                                                                                                                    c2 = 'f';
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
                                                                                                                                    c2 = 'F';
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
                                                                                                                                    c2 = 'g';
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
                                                                                                                                    c2 = '^';
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
                                                                                                                                    c2 = '\r';
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
                                                                                                                                    c2 = 'm';
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
                                                                                                                                    c2 = 26;
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
                                                                                                                                    c2 = 137;
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
                                                                                                                                    c2 = 18;
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
                                                                                                                                    c2 = 23;
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
                                                                                                                                    c2 = 16;
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
                                                                                                                                    c2 = 20;
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
                                                                                                                                    c2 = 135;
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
                                                                                                                                    c2 = 128;
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
                                                                                                                                    c2 = 'e';
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
                                                                                                                                    c2 = '~';
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
                                                                                                                                    c2 = 'C';
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
                                                                                                                                    c2 = '?';
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
                                                                                                                                    c2 = '>';
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
                                                                                                                                    c2 = ':';
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
                                                                                                                                    c2 = '9';
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
                                                                                                                                    c2 = 21;
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
                                                                                                                                    c2 = 'P';
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
                                                                                                                                    c2 = '|';
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
                                                                                                                                    c2 = 'y';
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
                                                                                                                                    c2 = 'x';
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
                                                                                                                                    c2 = 'n';
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
                                                                                                                                if (str7.equals(obj5)) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    c2 = 1;
                                                                                                                                    obj7 = obj5;
                                                                                                                                    break;
                                                                                                                                } else {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj7 = obj5;
                                                                                                                                    c2 = 65535;
                                                                                                                                    break;
                                                                                                                                }
                                                                                                                            case -819729482:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_STICKER")) {
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
                                                                                                                            case -772141857:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PHONE_CALL_REQUEST")) {
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
                                                                                                                            case -706345256:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_UNIQUE_STARGIFT")) {
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
                                                                                                                            case -638310039:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_STICKER")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = '6';
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
                                                                                                                            case -589196239:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_DOC")) {
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
                                                                                                                            case -589193654:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_GEO")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 'z';
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
                                                                                                                            case -412748110:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_DELETE_YOU")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 'c';
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
                                                                                                                            case -213586509:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("ENCRYPTION_REQUEST")) {
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
                                                                                                                            case -115582002:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_INVOICE")) {
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
                                                                                                                            case -112621464:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CONTACT_JOINED")) {
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
                                                                                                                            case -108522133:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("AUTH_REGION")) {
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
                                                                                                                            case -107572034:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_SCREENSHOT")) {
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
                                                                                                                            case -40534265:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_DELETE_MEMBER")) {
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
                                                                                                                            case -35560251:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_PAID_MEDIA")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 11;
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
                                                                                                                            case 65254746:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_ADD_YOU")) {
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
                                                                                                                            case 120441350:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_GIVEAWAY")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 'w';
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
                                                                                                                                    c2 = 'd';
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
                                                                                                                            case 202550149:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_VOICECHAT_INVITE")) {
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
                                                                                                                            case 309993049:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_DOC")) {
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
                                                                                                                            case 309995634:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_GEO")) {
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
                                                                                                                            case 309995749:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_GIF")) {
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
                                                                                                                            case 320532812:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGES")) {
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
                                                                                                                            case 328933854:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_STICKER")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 'N';
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
                                                                                                                                    c2 = '7';
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
                                                                                                                            case 344816990:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_PHOTO")) {
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
                                                                                                                            case 346878138:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_ROUND")) {
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
                                                                                                                            case 347944993:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_STORY")) {
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
                                                                                                                            case 350376871:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_VIDEO")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = '3';
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
                                                                                                                            case 608430149:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_VOICECHAT_INVITE_YOU")) {
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
                                                                                                                            case 615714517:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_PHOTO_SECRET")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 19;
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
                                                                                                                            case 728985323:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_PHOTO")) {
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
                                                                                                                            case 731046471:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_ROUND")) {
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
                                                                                                                            case 734545204:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PINNED_VIDEO")) {
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
                                                                                                                            case 802032552:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_CONTACT")) {
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
                                                                                                                            case 901537717:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_GIVEAWAY_STARS")) {
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
                                                                                                                            case 954623703:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_GIVEAWAY")) {
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
                                                                                                                            case 1007364121:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_GAME_SCORE")) {
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
                                                                                                                            case 1019850010:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_DOCS")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 'l';
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
                                                                                                                            case 1019926225:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_GAME")) {
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
                                                                                                                            case 1020207774:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_POLL")) {
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
                                                                                                                            case 1020243252:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_QUIZ")) {
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
                                                                                                                            case 1020317708:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_TEXT")) {
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
                                                                                                                            case 1054583304:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_STORY_MENTION")) {
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
                                                                                                                            case 1060282259:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_DOCS")) {
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
                                                                                                                            case 1060349560:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_FWDS")) {
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
                                                                                                                            case 1060358474:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_GAME")) {
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
                                                                                                                            case 1060640023:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_POLL")) {
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
                                                                                                                            case 1060675501:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_QUIZ")) {
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
                                                                                                                            case 1078101399:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_TITLE_EDITED")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = '[';
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
                                                                                                                            case 1144183001:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_GIVEAWAY_STARS")) {
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
                                                                                                                            case 1151995881:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_PAID_MEDIA")) {
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
                                                                                                                            case 1160762272:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_PHOTOS")) {
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
                                                                                                                            case 1172918249:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_GEOLIVE")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = '<';
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
                                                                                                                                    c2 = 'W';
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
                                                                                                                                    c2 = 24;
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
                                                                                                                            case 1281131340:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_GIF")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = ' ';
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
                                                                                                                            case 1333118583:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_VIDEOS")) {
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
                                                                                                                            case 1361447897:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_PHOTOS")) {
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
                                                                                                                            case 1369266398:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_GIVEAWAY")) {
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
                                                                                                                            case 1420317335:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_STARGIFT_UPGRADE")) {
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
                                                                                                                            case 1449476787:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_GIVEAWAY")) {
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
                                                                                                                            case 1498266155:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("PHONE_CALL_MISSED")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 138;
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
                                                                                                                            case 1540131626:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_PLAYLIST")) {
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
                                                                                                                            case 1547988151:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_AUDIO")) {
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
                                                                                                                            case 1561464595:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_PHOTO")) {
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
                                                                                                                            case 1563525743:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_ROUND")) {
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
                                                                                                                            case 1564592598:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_STORY")) {
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
                                                                                                                            case 1567024476:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_VIDEO")) {
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
                                                                                                                            case 1810705077:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("MESSAGE_INVOICE")) {
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
                                                                                                                            case 1815177512:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGES")) {
                                                                                                                                    str21 = str16;
                                                                                                                                    obj4 = obj8;
                                                                                                                                    obj5 = obj7;
                                                                                                                                    c2 = 'D';
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
                                                                                                                                    c2 = 14;
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
                                                                                                                                    c2 = 'k';
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
                                                                                                                                    c2 = 133;
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
                                                                                                                                    c2 = '\\';
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
                                                                                                                                    c2 = 'v';
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
                                                                                                                            case 2048733346:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_NOTEXT")) {
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
                                                                                                                            case 2099392181:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHANNEL_MESSAGE_PHOTOS")) {
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
                                                                                                                            case 2140162142:
                                                                                                                                str20 = str17;
                                                                                                                                if (str7.equals("CHAT_MESSAGE_GEOLIVE")) {
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
                                                                                                                        z10 = has;
                                                                                                                        try {
                                                                                                                            switch (c2) {
                                                                                                                                case 0:
                                                                                                                                    string = LocaleController.getString(R.string.StoryNotificationSingle);
                                                                                                                                    str22 = string;
                                                                                                                                    i12 = i26;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 1:
                                                                                                                                    string = LocaleController.formatPluralString("StoryNotificationHidden", 1, new Object[0]);
                                                                                                                                    str22 = string;
                                                                                                                                    i12 = i26;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 2:
                                                                                                                                    formatString = LocaleController.formatString("ActionSetSameWallpaperForThisChat", R.string.ActionSetSameWallpaperForThisChat, strArr[0]);
                                                                                                                                    i13 = R.string.WallpaperSameNotification;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 3:
                                                                                                                                    formatString = LocaleController.formatString("ActionSetWallpaperForThisChat", R.string.ActionSetWallpaperForThisChat, strArr[0]);
                                                                                                                                    i13 = R.string.WallpaperNotification;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 4:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageRecurringPay", R.string.NotificationMessageRecurringPay, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.PaymentInvoice;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
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
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 7:
                                                                                                                                    String str31 = strArr[0];
                                                                                                                                    formatString = LocaleController.formatPluralStringComma("NotificationMessageStarGift", Integer.parseInt(strArr[1]), strArr[0]);
                                                                                                                                    str26 = LocaleController.formatPluralStringComma("Gift2Notification", Integer.parseInt(strArr[1]));
                                                                                                                                    str24 = str31;
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '\b':
                                                                                                                                    str24 = strArr[0];
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageUniqueStarGift, str24);
                                                                                                                                    i13 = R.string.Gift2UniqueNotification;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '\t':
                                                                                                                                    str24 = strArr[0];
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageUniqueStarGiftUpgrade, str24);
                                                                                                                                    i13 = R.string.Gift2UniqueUpgradeNotification;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '\n':
                                                                                                                                    int parseInt3 = Integer.parseInt(strArr[1]);
                                                                                                                                    reactedText = LocaleController.formatPluralString("NotificationMessagePaidMedia", parseInt3, strArr[0]);
                                                                                                                                    formatPluralString = LocaleController.formatPluralString("NotificationPaidMedia", parseInt3, new Object[0]);
                                                                                                                                    str26 = formatPluralString;
                                                                                                                                    break;
                                                                                                                                case 11:
                                                                                                                                    int parseInt4 = Integer.parseInt(strArr[1]);
                                                                                                                                    reactedText = LocaleController.formatPluralString("NotificationChannelMessagePaidMedia", parseInt4, strArr[0]);
                                                                                                                                    formatPluralString = LocaleController.formatPluralString("NotificationPaidMedia", parseInt4, new Object[0]);
                                                                                                                                    str26 = formatPluralString;
                                                                                                                                    break;
                                                                                                                                case '\f':
                                                                                                                                    int parseInt5 = Integer.parseInt(strArr[2]);
                                                                                                                                    reactedText = LocaleController.formatPluralString("NotificationChatMessagePaidMedia", parseInt5, strArr[0], strArr[1]);
                                                                                                                                    formatPluralString = LocaleController.formatPluralString("NotificationPaidMedia", parseInt5, new Object[0]);
                                                                                                                                    str26 = formatPluralString;
                                                                                                                                    break;
                                                                                                                                case '\r':
                                                                                                                                    int parseInt6 = Integer.parseInt(strArr[1]);
                                                                                                                                    formatPluralString2 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt6, strArr[0]);
                                                                                                                                    formatPluralString3 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt6, strArr[0]);
                                                                                                                                    str26 = formatPluralString3;
                                                                                                                                    reactedText = formatPluralString2;
                                                                                                                                    break;
                                                                                                                                case 14:
                                                                                                                                    int parseInt7 = Integer.parseInt(strArr[1]);
                                                                                                                                    formatPluralString2 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt7, strArr[0]);
                                                                                                                                    formatPluralString3 = LocaleController.formatPluralString("NotificationPinnedPaidMedia", parseInt7, strArr[0]);
                                                                                                                                    str26 = formatPluralString3;
                                                                                                                                    reactedText = formatPluralString2;
                                                                                                                                    break;
                                                                                                                                case 15:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, strArr[0]);
                                                                                                                                    i13 = R.string.Message;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 16:
                                                                                                                                    formatString = LocaleController.formatString("NotificationStory", R.string.NotificationStory, strArr[0]);
                                                                                                                                    i13 = R.string.Story;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 17:
                                                                                                                                    reactedText = LocaleController.getString(R.string.StoryNotificationMention);
                                                                                                                                    break;
                                                                                                                                case 18:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, strArr[0]);
                                                                                                                                    i13 = R.string.AttachPhoto;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 19:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, strArr[0]);
                                                                                                                                    i13 = R.string.AttachDestructingPhoto;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 20:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, strArr[0]);
                                                                                                                                    i13 = R.string.AttachVideo;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 21:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, strArr[0]);
                                                                                                                                    i13 = R.string.AttachDestructingVideo;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 22:
                                                                                                                                    reactedText = LocaleController.getString(R.string.ActionTakeScreenshoot).replace("un1", strArr[0]);
                                                                                                                                    break;
                                                                                                                                case 23:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, strArr[0]);
                                                                                                                                    i13 = R.string.AttachRound;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 24:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, strArr[0]);
                                                                                                                                    i13 = R.string.AttachDocument;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 25:
                                                                                                                                    if (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) {
                                                                                                                                        formatString = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, strArr[0]);
                                                                                                                                        i13 = R.string.AttachSticker;
                                                                                                                                        str24 = str20;
                                                                                                                                        str26 = LocaleController.getString(i13);
                                                                                                                                        str22 = formatString;
                                                                                                                                        str20 = str24;
                                                                                                                                        i12 = i25;
                                                                                                                                        z11 = false;
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
                                                                                                                                        z11 = false;
                                                                                                                                    }
                                                                                                                                    break;
                                                                                                                                case 26:
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageAudio, strArr[0]);
                                                                                                                                    i13 = R.string.AttachAudio;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 27:
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageContact2, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachContact;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 28:
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageQuiz2, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.QuizPoll;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 29:
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessagePoll2, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.Poll;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 30:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, strArr[0]);
                                                                                                                                    i13 = R.string.AttachLocation;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 31:
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, strArr[0]);
                                                                                                                                    i13 = R.string.AttachLiveLocation;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case ' ':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, strArr[0]);
                                                                                                                                    i13 = R.string.AttachGif;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '!':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachGame;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '\"':
                                                                                                                                case '#':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationMessageGameScored", R.string.NotificationMessageGameScored, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    break;
                                                                                                                                case '$':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageInvoice", R.string.NotificationMessageInvoice, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.PaymentInvoice;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '%':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageForwardFew", R.string.NotificationMessageForwardFew, strArr[0], LocaleController.formatPluralString(str30, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '&':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString(str4, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '\'':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString(str5, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '(':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case ')':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '*':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageGiftCode", R.string.NotificationMessageGiftCode, strArr[0], LocaleController.formatPluralString("Months", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '+':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageGiveaway", R.string.NotificationMessageGiveaway, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case ',':
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
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '-':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageAlbum", R.string.NotificationMessageAlbum, strArr[0]);
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '.':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageChannelGiveaway", R.string.NotificationMessageChannelGiveaway, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    i13 = R.string.BoostingGiveaway;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '/':
                                                                                                                                    try {
                                                                                                                                        i15 = Integer.parseInt(strArr[1]);
                                                                                                                                    } catch (Exception unused2) {
                                                                                                                                        i15 = 1;
                                                                                                                                    }
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageChannelStarsGiveaway2, strArr[0], LocaleController.formatPluralString("AmongWinners", i15, new Object[0]), strArr[2]);
                                                                                                                                    i13 = R.string.BoostingGiveaway;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '0':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, strArr[0]);
                                                                                                                                    i13 = R.string.Message;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '1':
                                                                                                                                    formatString = LocaleController.formatString("NotificationChannelStory", R.string.NotificationChannelStory, strArr[0]);
                                                                                                                                    i13 = R.string.Story;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '2':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessagePhoto", R.string.ChannelMessagePhoto, strArr[0]);
                                                                                                                                    i13 = R.string.AttachPhoto;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '3':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageVideo", R.string.ChannelMessageVideo, strArr[0]);
                                                                                                                                    i13 = R.string.AttachVideo;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '4':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageRound", R.string.ChannelMessageRound, strArr[0]);
                                                                                                                                    i13 = R.string.AttachRound;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '5':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageDocument", R.string.ChannelMessageDocument, strArr[0]);
                                                                                                                                    i13 = R.string.AttachDocument;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '6':
                                                                                                                                    if (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) {
                                                                                                                                        formatString = LocaleController.formatString("ChannelMessageSticker", R.string.ChannelMessageSticker, strArr[0]);
                                                                                                                                        i13 = R.string.AttachSticker;
                                                                                                                                        str24 = str20;
                                                                                                                                        str26 = LocaleController.getString(i13);
                                                                                                                                        str22 = formatString;
                                                                                                                                        str20 = str24;
                                                                                                                                        i12 = i25;
                                                                                                                                        z11 = false;
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
                                                                                                                                        z11 = false;
                                                                                                                                    }
                                                                                                                                    break;
                                                                                                                                case '7':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageAudio", R.string.ChannelMessageAudio, strArr[0]);
                                                                                                                                    i13 = R.string.AttachAudio;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '8':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageContact2", R.string.ChannelMessageContact2, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachContact;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '9':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageQuiz2", R.string.ChannelMessageQuiz2, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.QuizPoll;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case ':':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessagePoll2", R.string.ChannelMessagePoll2, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.Poll;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case ';':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageMap", R.string.ChannelMessageMap, strArr[0]);
                                                                                                                                    i13 = R.string.AttachLocation;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '<':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageLiveLocation", R.string.ChannelMessageLiveLocation, strArr[0]);
                                                                                                                                    i13 = R.string.AttachLiveLocation;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '=':
                                                                                                                                    formatString = LocaleController.formatString("ChannelMessageGIF", R.string.ChannelMessageGIF, strArr[0]);
                                                                                                                                    i13 = R.string.AttachGif;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '>':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0]);
                                                                                                                                    i13 = R.string.AttachGame;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case '?':
                                                                                                                                    formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("ForwardedMessageCount", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]).toLowerCase());
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case '@':
                                                                                                                                    formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString(str4, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'A':
                                                                                                                                    formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString(str5, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'B':
                                                                                                                                    formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'C':
                                                                                                                                    formatString2 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'D':
                                                                                                                                    formatString2 = LocaleController.formatString("ChannelMessageAlbum", R.string.ChannelMessageAlbum, strArr[0]);
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'E':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageChatGiveaway", R.string.NotificationMessageChatGiveaway, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                                                                                                    i13 = R.string.BoostingGiveaway;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'F':
                                                                                                                                    try {
                                                                                                                                        i16 = Integer.parseInt(strArr[2]);
                                                                                                                                    } catch (Exception unused3) {
                                                                                                                                        i16 = 1;
                                                                                                                                    }
                                                                                                                                    formatString = LocaleController.formatString(R.string.NotificationMessageChatStarsGiveaway2, strArr[0], strArr[1], LocaleController.formatPluralString("AmongWinners", i16, new Object[0]), strArr[3]);
                                                                                                                                    i13 = R.string.BoostingGiveaway;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'G':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    str23 = strArr[2];
                                                                                                                                    str26 = str23;
                                                                                                                                    str24 = str20;
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'H':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.Message;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'I':
                                                                                                                                    formatString = LocaleController.formatString("NotificationChatStory", R.string.NotificationChatStory, strArr[0]);
                                                                                                                                    i13 = R.string.Story;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'J':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupPhoto", R.string.NotificationMessageGroupPhoto, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachPhoto;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'K':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupVideo", R.string.NotificationMessageGroupVideo, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachVideo;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'L':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupRound", R.string.NotificationMessageGroupRound, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachRound;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'M':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupDocument", R.string.NotificationMessageGroupDocument, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachDocument;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'N':
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
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'O':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupAudio", R.string.NotificationMessageGroupAudio, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachAudio;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'P':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupContact2", R.string.NotificationMessageGroupContact2, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    i13 = R.string.AttachContact;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'Q':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupQuiz2", R.string.NotificationMessageGroupQuiz2, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    i13 = R.string.PollQuiz;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'R':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupPoll2", R.string.NotificationMessageGroupPoll2, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    i13 = R.string.Poll;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'S':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachLocation;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'T':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupLiveLocation", R.string.NotificationMessageGroupLiveLocation, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachLiveLocation;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'U':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupGif", R.string.NotificationMessageGroupGif, strArr[0], strArr[1]);
                                                                                                                                    i13 = R.string.AttachGif;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'V':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupGame", R.string.NotificationMessageGroupGame, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    i13 = R.string.AttachGame;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'W':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationMessageGroupGameScored", R.string.NotificationMessageGroupGameScored, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                                                                                                    break;
                                                                                                                                case 'X':
                                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupInvoice", R.string.NotificationMessageGroupInvoice, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    i13 = R.string.PaymentInvoice;
                                                                                                                                    str24 = str20;
                                                                                                                                    str26 = LocaleController.getString(i13);
                                                                                                                                    str22 = formatString;
                                                                                                                                    str20 = str24;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                                case 'Y':
                                                                                                                                case 'Z':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationInvitedToGroup", R.string.NotificationInvitedToGroup, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case '[':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationEditedGroupName", R.string.NotificationEditedGroupName, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case '\\':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationEditedGroupPhoto", R.string.NotificationEditedGroupPhoto, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case ']':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    break;
                                                                                                                                case '^':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupCreatedCall", R.string.NotificationGroupCreatedCall, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case '_':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupInvitedToCall", R.string.NotificationGroupInvitedToCall, strArr[0], strArr[1], strArr[2]);
                                                                                                                                    break;
                                                                                                                                case '`':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupEndedCall", R.string.NotificationGroupEndedCall, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'a':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupInvitedYouToCall", R.string.NotificationGroupInvitedYouToCall, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'b':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupKickMember", R.string.NotificationGroupKickMember, strArr[0], strArr[1], strArr.length > 2 ? strArr[2] : "");
                                                                                                                                    break;
                                                                                                                                case 'c':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupKickYou", R.string.NotificationGroupKickYou, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'd':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupLeftMember", R.string.NotificationGroupLeftMember, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'e':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupAddSelf", R.string.NotificationGroupAddSelf, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'f':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationGroupAddSelfMega", R.string.NotificationGroupAddSelfMega, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'g':
                                                                                                                                    reactedText = LocaleController.formatString("UserAcceptedToGroupPushWithGroup", R.string.UserAcceptedToGroupPushWithGroup, strArr[0], strArr[1]);
                                                                                                                                    break;
                                                                                                                                case 'h':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupForwardedFew", R.string.NotificationGroupForwardedFew, strArr[0], strArr[1], LocaleController.formatPluralString(str30, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'i':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString(str4, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'j':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString(str5, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'k':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'l':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'm':
                                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupAlbum", R.string.NotificationGroupAlbum, strArr[0], strArr[1]);
                                                                                                                                    str25 = str21;
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                case 'n':
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
                                                                                                                                case 'o':
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
                                                                                                                                case 'p':
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
                                                                                                                                case 'q':
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
                                                                                                                                case 'r':
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
                                                                                                                                case 's':
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
                                                                                                                                case 't':
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
                                                                                                                                case 'u':
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
                                                                                                                                case 'v':
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
                                                                                                                                case 'w':
                                                                                                                                    reactedText = LocaleController.formatString("NotificationPinnedGiveaway", R.string.NotificationPinnedGiveaway, strArr[0]);
                                                                                                                                    break;
                                                                                                                                case 'x':
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
                                                                                                                                case 'y':
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
                                                                                                                                case 'z':
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
                                                                                                                                case '{':
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
                                                                                                                                case '|':
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
                                                                                                                                case '}':
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
                                                                                                                                case '~':
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
                                                                                                                                case NotificationCenter.dialogTranslate /* 127 */:
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
                                                                                                                                case 128:
                                                                                                                                    formatString2 = LocaleController.getString(R.string.YouHaveNewMessage);
                                                                                                                                    str25 = LocaleController.getString(R.string.SecretChatName);
                                                                                                                                    str22 = formatString2;
                                                                                                                                    str21 = str25;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = true;
                                                                                                                                    break;
                                                                                                                                default:
                                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                                        FileLog.w("unhandled loc_key = " + str7);
                                                                                                                                    }
                                                                                                                                case NotificationCenter.walletPendingTransactionsChanged /* 129 */:
                                                                                                                                case NotificationCenter.walletSyncProgressChanged /* 130 */:
                                                                                                                                case NotificationCenter.httpFileDidLoad /* 131 */:
                                                                                                                                case NotificationCenter.httpFileDidFailedLoad /* 132 */:
                                                                                                                                case NotificationCenter.didUpdateConnectionState /* 133 */:
                                                                                                                                case NotificationCenter.fileUploaded /* 134 */:
                                                                                                                                case NotificationCenter.fileUploadFailed /* 135 */:
                                                                                                                                case NotificationCenter.fileUploadProgressChanged /* 136 */:
                                                                                                                                case NotificationCenter.fileLoadProgressChanged /* 137 */:
                                                                                                                                case NotificationCenter.fileLoaded /* 138 */:
                                                                                                                                    str22 = null;
                                                                                                                                    i12 = i25;
                                                                                                                                    z11 = false;
                                                                                                                                    break;
                                                                                                                            }
                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                FileLog.d(str + " received message notification " + str7 + str14 + makeEncryptedDialogId + " mid = " + i12);
                                                                                                                            }
                                                                                                                            if (str22 == null) {
                                                                                                                                TLRPC.TL_message tL_message = new TLRPC.TL_message();
                                                                                                                                if (str7.startsWith("REACT_STORY") && i12 > 0) {
                                                                                                                                    i12 = -i12;
                                                                                                                                }
                                                                                                                                tL_message.id = i12;
                                                                                                                                tL_message.random_id = j14;
                                                                                                                                tL_message.message = str26 != null ? str26 : str22;
                                                                                                                                tL_message.date = (int) (j / 1000);
                                                                                                                                if (z7) {
                                                                                                                                    tL_message.action = new TLRPC.TL_messageActionPinMessage();
                                                                                                                                }
                                                                                                                                if (z9) {
                                                                                                                                    tL_message.flags |= Integer.MIN_VALUE;
                                                                                                                                }
                                                                                                                                tL_message.dialog_id = makeEncryptedDialogId;
                                                                                                                                if (j2 != 0) {
                                                                                                                                    TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
                                                                                                                                    tL_message.peer_id = tL_peerChannel;
                                                                                                                                    tL_peerChannel.channel_id = j2;
                                                                                                                                    j10 = makeEncryptedDialogId;
                                                                                                                                    j9 = j13;
                                                                                                                                } else if (j13 != 0) {
                                                                                                                                    TLRPC.TL_peerChat tL_peerChat2 = new TLRPC.TL_peerChat();
                                                                                                                                    tL_message.peer_id = tL_peerChat2;
                                                                                                                                    j9 = j13;
                                                                                                                                    tL_peerChat2.chat_id = j9;
                                                                                                                                    j10 = makeEncryptedDialogId;
                                                                                                                                } else {
                                                                                                                                    j9 = j13;
                                                                                                                                    TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                                                                                                                                    tL_message.peer_id = tL_peerUser2;
                                                                                                                                    j10 = makeEncryptedDialogId;
                                                                                                                                    tL_peerUser2.user_id = j12;
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
                                                                                                                                if (!z16 && !z7) {
                                                                                                                                    z12 = false;
                                                                                                                                    tL_message.mentioned = z12;
                                                                                                                                    tL_message.silent = z15;
                                                                                                                                    tL_message.from_scheduled = z14;
                                                                                                                                    messageObject = new MessageObject(i27, tL_message, str22, str21, str20, z11, z8, z9, z10);
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
                                                                                                                                            z13 = false;
                                                                                                                                            messageObject.isStoryPush = z13;
                                                                                                                                            messageObject.isStoryMentionPush = str7.equals("MESSAGE_STORY_MENTION");
                                                                                                                                            messageObject.isStoryPushHidden = str7.equals(obj3);
                                                                                                                                            ArrayList<MessageObject> arrayList4 = new ArrayList<>();
                                                                                                                                            arrayList4.add(messageObject);
                                                                                                                                            FileLog.d("PushListenerController push notification to NotificationsController of " + tL_message.dialog_id);
                                                                                                                                            if (!messageObject.isStoryReactionPush || messageObject.isReactionPush || messageObject.isStoryMentionPush || messageObject.isStoryPush || messageObject.isStoryPushHidden || z16 || z7 || i12 <= 0) {
                                                                                                                                                i8 = i27;
                                                                                                                                            } else {
                                                                                                                                                final long j15 = j10;
                                                                                                                                                i8 = i27;
                                                                                                                                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda1
                                                                                                                                                    @Override // java.lang.Runnable
                                                                                                                                                    public final void run() {
                                                                                                                                                        PushListenerController.lambda$processRemoteMessage$5(i8, j15, i12);
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                            }
                                                                                                                                            NotificationsController.getInstance(i8).processNewMessages(arrayList4, true, true, countDownLatch);
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        obj3 = obj7;
                                                                                                                                    }
                                                                                                                                    z13 = true;
                                                                                                                                    messageObject.isStoryPush = z13;
                                                                                                                                    messageObject.isStoryMentionPush = str7.equals("MESSAGE_STORY_MENTION");
                                                                                                                                    messageObject.isStoryPushHidden = str7.equals(obj3);
                                                                                                                                    ArrayList<MessageObject> arrayList42 = new ArrayList<>();
                                                                                                                                    arrayList42.add(messageObject);
                                                                                                                                    FileLog.d("PushListenerController push notification to NotificationsController of " + tL_message.dialog_id);
                                                                                                                                    if (messageObject.isStoryReactionPush) {
                                                                                                                                    }
                                                                                                                                    i8 = i27;
                                                                                                                                    NotificationsController.getInstance(i8).processNewMessages(arrayList42, true, true, countDownLatch);
                                                                                                                                }
                                                                                                                                z12 = true;
                                                                                                                                tL_message.mentioned = z12;
                                                                                                                                tL_message.silent = z15;
                                                                                                                                tL_message.from_scheduled = z14;
                                                                                                                                messageObject = new MessageObject(i27, tL_message, str22, str21, str20, z11, z8, z9, z10);
                                                                                                                                if (i10 != 0) {
                                                                                                                                }
                                                                                                                                boolean startsWith2 = str7.startsWith("REACT_STORY");
                                                                                                                                messageObject.isStoryReactionPush = startsWith2;
                                                                                                                                messageObject.isReactionPush = startsWith2 && (str7.startsWith(str19) || str7.startsWith(str18));
                                                                                                                                if (str7.equals(obj8)) {
                                                                                                                                }
                                                                                                                                z13 = true;
                                                                                                                                messageObject.isStoryPush = z13;
                                                                                                                                messageObject.isStoryMentionPush = str7.equals("MESSAGE_STORY_MENTION");
                                                                                                                                messageObject.isStoryPushHidden = str7.equals(obj3);
                                                                                                                                ArrayList<MessageObject> arrayList422 = new ArrayList<>();
                                                                                                                                arrayList422.add(messageObject);
                                                                                                                                FileLog.d("PushListenerController push notification to NotificationsController of " + tL_message.dialog_id);
                                                                                                                                if (messageObject.isStoryReactionPush) {
                                                                                                                                }
                                                                                                                                i8 = i27;
                                                                                                                                NotificationsController.getInstance(i8).processNewMessages(arrayList422, true, true, countDownLatch);
                                                                                                                            } else {
                                                                                                                                i8 = i27;
                                                                                                                            }
                                                                                                                        } catch (Throwable th8) {
                                                                                                                            th = th8;
                                                                                                                            str6 = str2;
                                                                                                                            i = i27;
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
                                                                                                                    }
                                                                                                                    str22 = reactedText;
                                                                                                                    i12 = i25;
                                                                                                                    z11 = false;
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                    }
                                                                                                                    if (str22 == null) {
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    if (str7.startsWith("CHANNEL_")) {
                                                                                                                        str17 = null;
                                                                                                                        z7 = false;
                                                                                                                        z8 = true;
                                                                                                                        z9 = false;
                                                                                                                        if (str7.startsWith(str15)) {
                                                                                                                        }
                                                                                                                        str18 = "CHAT_REACT_";
                                                                                                                        str19 = str15;
                                                                                                                        z10 = has;
                                                                                                                        str20 = str17;
                                                                                                                        str21 = str16;
                                                                                                                        reactedText = getReactedText(str7, strArr);
                                                                                                                        str22 = reactedText;
                                                                                                                        i12 = i25;
                                                                                                                        z11 = false;
                                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                                        }
                                                                                                                        if (str22 == null) {
                                                                                                                        }
                                                                                                                    }
                                                                                                                    str17 = null;
                                                                                                                    z7 = false;
                                                                                                                    z8 = false;
                                                                                                                    z9 = false;
                                                                                                                    if (str7.startsWith(str15)) {
                                                                                                                    }
                                                                                                                    str18 = "CHAT_REACT_";
                                                                                                                    str19 = str15;
                                                                                                                    z10 = has;
                                                                                                                    str20 = str17;
                                                                                                                    str21 = str16;
                                                                                                                    reactedText = getReactedText(str7, strArr);
                                                                                                                    str22 = reactedText;
                                                                                                                    i12 = i25;
                                                                                                                    z11 = false;
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                    }
                                                                                                                    if (str22 == null) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } else if (UserObject.isReplyUser(makeEncryptedDialogId)) {
                                                                                                                str16 = str16 + " @ " + strArr[1];
                                                                                                                str17 = null;
                                                                                                                z7 = false;
                                                                                                                z8 = false;
                                                                                                                z9 = false;
                                                                                                                if (str7.startsWith(str15)) {
                                                                                                                }
                                                                                                                str18 = "CHAT_REACT_";
                                                                                                                str19 = str15;
                                                                                                                z10 = has;
                                                                                                                str20 = str17;
                                                                                                                str21 = str16;
                                                                                                                reactedText = getReactedText(str7, strArr);
                                                                                                                str22 = reactedText;
                                                                                                                i12 = i25;
                                                                                                                z11 = false;
                                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                                }
                                                                                                                if (str22 == null) {
                                                                                                                }
                                                                                                            } else {
                                                                                                                z9 = j2 != 0;
                                                                                                                str17 = str16;
                                                                                                                str16 = strArr[1];
                                                                                                                z7 = false;
                                                                                                                z8 = false;
                                                                                                                if (str7.startsWith(str15)) {
                                                                                                                }
                                                                                                                str18 = "CHAT_REACT_";
                                                                                                                str19 = str15;
                                                                                                                z10 = has;
                                                                                                                str20 = str17;
                                                                                                                str21 = str16;
                                                                                                                reactedText = getReactedText(str7, strArr);
                                                                                                                str22 = reactedText;
                                                                                                                i12 = i25;
                                                                                                                z11 = false;
                                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                                }
                                                                                                                if (str22 == null) {
                                                                                                                }
                                                                                                            }
                                                                                                            ConnectionsManager.onInternalPushReceived(i8);
                                                                                                            ConnectionsManager.getInstance(i8).resumeNetworkMaybe();
                                                                                                            return;
                                                                                                        }
                                                                                                        str16 = null;
                                                                                                        boolean has2 = jSONObject.has("edit_date");
                                                                                                        if (str7.startsWith("CHAT_")) {
                                                                                                        }
                                                                                                        if (str7.startsWith("PINNED_")) {
                                                                                                        }
                                                                                                    }
                                                                                                } catch (Throwable th9) {
                                                                                                    th = th9;
                                                                                                    i4 = i27;
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
                                                                                            z6 = false;
                                                                                            if (jSONObject.has("silent")) {
                                                                                            }
                                                                                            boolean z162 = z6;
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
                                                                                        }
                                                                                        z5 = true;
                                                                                        if (jSONObject.has("mention")) {
                                                                                        }
                                                                                        z6 = false;
                                                                                        if (jSONObject.has("silent")) {
                                                                                        }
                                                                                        boolean z1622 = z6;
                                                                                        jSONObject3 = jSONObject2;
                                                                                        if (jSONObject3.has("loc_args")) {
                                                                                        }
                                                                                        if (strArr != null) {
                                                                                        }
                                                                                        str16 = null;
                                                                                        boolean has2222 = jSONObject.has("edit_date");
                                                                                        if (str7.startsWith("CHAT_")) {
                                                                                        }
                                                                                        if (str7.startsWith("PINNED_")) {
                                                                                        }
                                                                                    } catch (Throwable th10) {
                                                                                        th = th10;
                                                                                        i4 = i27;
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
                                                                        ConnectionsManager.onInternalPushReceived(i8);
                                                                        ConnectionsManager.getInstance(i8).resumeNetworkMaybe();
                                                                        return;
                                                                    }
                                                                    NotificationsController.getInstance(i6).processDeleteStory(makeEncryptedDialogId, jSONObject.getInt("story_id"));
                                                                }
                                                            }
                                                            i8 = i6;
                                                            countDownLatch.countDown();
                                                            ConnectionsManager.onInternalPushReceived(i8);
                                                            ConnectionsManager.getInstance(i8).resumeNetworkMaybe();
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
                                                    i8 = i6;
                                                    countDownLatch.countDown();
                                                    ConnectionsManager.onInternalPushReceived(i8);
                                                    ConnectionsManager.getInstance(i8).resumeNetworkMaybe();
                                                    return;
                                                } catch (Throwable th11) {
                                                    th = th11;
                                                    i4 = i6;
                                                }
                                            } catch (Throwable th12) {
                                                th = th12;
                                                i4 = i5;
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
                                        if (c != 0) {
                                        }
                                    } catch (Throwable th13) {
                                        th = th13;
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
                            } catch (Throwable th14) {
                                th = th14;
                                str2 = str27;
                            }
                        } catch (Throwable th15) {
                            th = th15;
                            str2 = str27;
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
                } catch (Throwable th16) {
                    str11 = str27;
                    th = th16;
                }
            } catch (Throwable th17) {
                str11 = str27;
                th = th17;
            }
        } catch (Throwable th18) {
            th = th18;
            str6 = null;
            str7 = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$7(final String str, final String str2, final long j) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " PRE INIT APP");
        }
        ApplicationLoader.postInitApplication();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " POST INIT APP");
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$processRemoteMessage$6(str, str2, j);
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$processRemoteMessage$7(str2, str, j);
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
