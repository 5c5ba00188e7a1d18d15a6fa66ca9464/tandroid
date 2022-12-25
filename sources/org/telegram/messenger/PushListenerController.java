package org.telegram.messenger;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseBooleanArray;
import androidx.collection.LongSparseArray;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_saveAppLog;
import org.telegram.tgnet.TLRPC$TL_inputAppEvent;
import org.telegram.tgnet.TLRPC$TL_jsonNull;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_updateReadChannelInbox;
import org.telegram.tgnet.TLRPC$TL_updateReadHistoryInbox;
import org.telegram.tgnet.TLRPC$TL_updateServiceNotification;
import org.telegram.tgnet.TLRPC$TL_updates;
import org.telegram.tgnet.TLRPC$Update;
/* loaded from: classes.dex */
public class PushListenerController {
    public static final int NOTIFICATION_ID = 1;
    public static final int PUSH_TYPE_FIREBASE = 2;
    public static final int PUSH_TYPE_HUAWEI = 13;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    /* loaded from: classes.dex */
    public interface IPushListenerServiceProvider {
        String getLogTitle();

        int getPushType();

        boolean hasServices();

        void onRequestPushToken();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PushType {
    }

    public static void sendRegistrationToServer(final int i, final String str) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$sendRegistrationToServer$3(str, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRegistrationToServer$3(final String str, final int i) {
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
                    TLRPC$TL_help_saveAppLog tLRPC$TL_help_saveAppLog = new TLRPC$TL_help_saveAppLog();
                    TLRPC$TL_inputAppEvent tLRPC$TL_inputAppEvent = new TLRPC$TL_inputAppEvent();
                    tLRPC$TL_inputAppEvent.time = SharedConfig.pushStringGetTimeStart;
                    tLRPC$TL_inputAppEvent.type = str2 + "_token_request";
                    tLRPC$TL_inputAppEvent.peer = 0L;
                    tLRPC$TL_inputAppEvent.data = new TLRPC$TL_jsonNull();
                    tLRPC$TL_help_saveAppLog.events.add(tLRPC$TL_inputAppEvent);
                    TLRPC$TL_inputAppEvent tLRPC$TL_inputAppEvent2 = new TLRPC$TL_inputAppEvent();
                    tLRPC$TL_inputAppEvent2.time = SharedConfig.pushStringGetTimeEnd;
                    tLRPC$TL_inputAppEvent2.type = str2 + "_token_response";
                    tLRPC$TL_inputAppEvent2.peer = SharedConfig.pushStringGetTimeEnd - SharedConfig.pushStringGetTimeStart;
                    tLRPC$TL_inputAppEvent2.data = new TLRPC$TL_jsonNull();
                    tLRPC$TL_help_saveAppLog.events.add(tLRPC$TL_inputAppEvent2);
                    ConnectionsManager.getInstance(i2).sendRequest(tLRPC$TL_help_saveAppLog, PushListenerController$$ExternalSyntheticLambda8.INSTANCE);
                    z = false;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PushListenerController.lambda$sendRegistrationToServer$2(i2, i, str);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRegistrationToServer$1(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$sendRegistrationToServer$0(TLRPC$TL_error.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRegistrationToServer$0(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            SharedConfig.pushStatSent = true;
            SharedConfig.saveConfig();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendRegistrationToServer$2(int i, int i2, String str) {
        MessagesController.getInstance(i).registerForPush(i2, str);
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
                PushListenerController.lambda$processRemoteMessage$8(str2, str, j);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$8(final String str, final String str2, final long j) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " PRE INIT APP");
        }
        ApplicationLoader.postInitApplication();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " POST INIT APP");
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PushListenerController.lambda$processRemoteMessage$7(str, str2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x04c3, code lost:
        if (r7 > r12.intValue()) goto L188;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:160:0x1f64 A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0549 A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x057d A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x05e1 A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0623 A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:237:0x1e5b A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x1f25 A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x206a  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x2081  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x207a  */
    /* JADX WARN: Removed duplicated region for block: B:869:0x1e42  */
    /* JADX WARN: Removed duplicated region for block: B:870:0x0612  */
    /* JADX WARN: Removed duplicated region for block: B:876:0x05b1 A[Catch: all -> 0x1f75, TryCatch #11 {all -> 0x1f75, blocks: (B:147:0x0355, B:150:0x0363, B:151:0x0376, B:153:0x0379, B:155:0x0385, B:157:0x03a4, B:160:0x1f64, B:161:0x1f69, B:164:0x03d1, B:166:0x03da, B:167:0x03f2, B:169:0x03f5, B:171:0x0411, B:173:0x0428, B:174:0x0453, B:176:0x0459, B:178:0x0461, B:179:0x0469, B:181:0x0471, B:183:0x048c, B:185:0x04a0, B:186:0x04bf, B:189:0x04de, B:192:0x04e6, B:195:0x04ef, B:201:0x0515, B:203:0x051d, B:206:0x0528, B:208:0x0530, B:211:0x053f, B:213:0x0549, B:215:0x055c, B:218:0x056c, B:220:0x057d, B:222:0x0583, B:225:0x05dd, B:227:0x05e1, B:228:0x061b, B:230:0x0623, B:233:0x1e52, B:237:0x1e5b, B:240:0x1e6c, B:242:0x1e77, B:244:0x1e80, B:245:0x1e87, B:247:0x1e8f, B:248:0x1ebc, B:250:0x1ec8, B:255:0x1f02, B:257:0x1f25, B:258:0x1f37, B:260:0x1f3f, B:264:0x1f49, B:269:0x1ed8, B:272:0x1eea, B:273:0x1ef6, B:276:0x1ea3, B:277:0x1eaf, B:279:0x063c, B:280:0x0640, B:286:0x0bcb, B:288:0x1e2b, B:291:0x0bd7, B:295:0x0bf7, B:298:0x0c1c, B:300:0x0c33, B:303:0x0c51, B:305:0x0c6a, B:306:0x0c81, B:309:0x0c9f, B:311:0x0cb8, B:312:0x0ccf, B:315:0x0ced, B:317:0x0d06, B:318:0x0d1d, B:321:0x0d3b, B:323:0x0d54, B:324:0x0d6b, B:327:0x0d89, B:329:0x0da2, B:330:0x0db9, B:333:0x0dd7, B:335:0x0df0, B:336:0x0e0c, B:339:0x0e2f, B:341:0x0e48, B:342:0x0e64, B:345:0x0e87, B:347:0x0ea0, B:348:0x0ebc, B:351:0x0edf, B:353:0x0ef8, B:354:0x0f0f, B:357:0x0f2d, B:359:0x0f31, B:361:0x0f39, B:362:0x0f50, B:364:0x0f64, B:366:0x0f68, B:368:0x0f70, B:369:0x0f8c, B:370:0x0fa3, B:372:0x0fa7, B:374:0x0faf, B:375:0x0fc6, B:378:0x0fe4, B:380:0x0ffd, B:381:0x1014, B:384:0x1032, B:386:0x104b, B:387:0x1062, B:390:0x1080, B:392:0x1099, B:393:0x10b0, B:396:0x10ce, B:398:0x10e7, B:399:0x10fe, B:402:0x111c, B:404:0x1135, B:405:0x114c, B:408:0x116a, B:410:0x1183, B:411:0x119f, B:412:0x11b6, B:415:0x11d5, B:416:0x1205, B:417:0x1233, B:418:0x1262, B:419:0x1291, B:420:0x12c4, B:421:0x12e1, B:422:0x12fe, B:423:0x131b, B:424:0x1338, B:425:0x1355, B:426:0x1372, B:427:0x138f, B:428:0x13ac, B:429:0x13ce, B:430:0x13eb, B:431:0x140c, B:432:0x1428, B:433:0x1444, B:434:0x1464, B:438:0x148e, B:439:0x14b4, B:440:0x14de, B:441:0x1503, B:442:0x1528, B:443:0x154d, B:444:0x1577, B:445:0x15a0, B:446:0x15c9, B:447:0x15f2, B:449:0x15fc, B:451:0x1604, B:454:0x163a, B:455:0x166b, B:457:0x1690, B:458:0x16b4, B:459:0x16d8, B:460:0x16fc, B:461:0x1722, B:462:0x174a, B:463:0x1763, B:465:0x178e, B:466:0x17b7, B:467:0x17e0, B:468:0x1809, B:469:0x1839, B:471:0x1859, B:472:0x1879, B:473:0x1899, B:474:0x18b9, B:475:0x18de, B:476:0x1903, B:477:0x1928, B:478:0x1948, B:480:0x1952, B:482:0x195a, B:483:0x198b, B:484:0x19a3, B:485:0x19c3, B:486:0x19e2, B:487:0x1a01, B:488:0x1a20, B:489:0x1a45, B:490:0x1a60, B:492:0x1a8b, B:493:0x1ab3, B:494:0x1adb, B:495:0x1b05, B:496:0x1b32, B:498:0x1b57, B:499:0x1b79, B:500:0x1b9e, B:501:0x1bbe, B:502:0x1bde, B:503:0x1bfe, B:504:0x1c23, B:505:0x1c48, B:506:0x1c6d, B:507:0x1c8d, B:509:0x1c97, B:511:0x1c9f, B:512:0x1cd3, B:513:0x1ceb, B:514:0x1d0b, B:515:0x1d2b, B:516:0x1d45, B:517:0x1d65, B:518:0x1d84, B:519:0x1da3, B:520:0x1dc2, B:521:0x1de3, B:522:0x1e05, B:523:0x0645, B:527:0x0651, B:530:0x065d, B:533:0x0669, B:536:0x0675, B:539:0x0681, B:542:0x068d, B:545:0x0699, B:548:0x06a5, B:551:0x06b1, B:554:0x06bd, B:557:0x06c9, B:560:0x06d5, B:563:0x06e1, B:566:0x06ed, B:569:0x06f9, B:572:0x0705, B:575:0x0711, B:578:0x071d, B:581:0x0729, B:584:0x0736, B:587:0x0742, B:590:0x074e, B:593:0x075a, B:596:0x0766, B:599:0x0772, B:602:0x077e, B:605:0x078a, B:608:0x0796, B:611:0x07a2, B:614:0x07af, B:617:0x07bb, B:620:0x07c7, B:623:0x07d3, B:626:0x07df, B:629:0x07eb, B:632:0x07f7, B:635:0x0803, B:638:0x080f, B:641:0x081b, B:644:0x0827, B:647:0x0833, B:650:0x083f, B:653:0x084b, B:656:0x0857, B:659:0x0863, B:662:0x086f, B:665:0x087b, B:668:0x0887, B:671:0x0892, B:674:0x089e, B:677:0x08aa, B:680:0x08b6, B:683:0x08c2, B:686:0x08ce, B:689:0x08da, B:692:0x08e6, B:695:0x08f2, B:698:0x08fe, B:701:0x090a, B:704:0x0916, B:707:0x0922, B:710:0x092e, B:713:0x093a, B:716:0x0946, B:719:0x0952, B:722:0x0960, B:725:0x096c, B:728:0x0978, B:731:0x0984, B:734:0x0990, B:737:0x099c, B:740:0x09a8, B:743:0x09b4, B:746:0x09c0, B:749:0x09cc, B:752:0x09d8, B:755:0x09e4, B:758:0x09f0, B:761:0x09fc, B:764:0x0a08, B:767:0x0a14, B:770:0x0a20, B:773:0x0a2c, B:776:0x0a38, B:779:0x0a44, B:782:0x0a4f, B:785:0x0a5c, B:788:0x0a68, B:791:0x0a74, B:794:0x0a80, B:797:0x0a8c, B:800:0x0a98, B:803:0x0aa4, B:806:0x0ab0, B:809:0x0abc, B:812:0x0ac8, B:815:0x0ad3, B:818:0x0adf, B:821:0x0aec, B:824:0x0af8, B:827:0x0b04, B:830:0x0b11, B:833:0x0b1d, B:836:0x0b29, B:839:0x0b35, B:842:0x0b40, B:845:0x0b4b, B:848:0x0b56, B:851:0x0b61, B:854:0x0b6c, B:857:0x0b77, B:860:0x0b82, B:863:0x0b8d, B:866:0x0b98, B:874:0x05a4, B:876:0x05b1, B:883:0x05ca, B:895:0x04d3), top: B:146:0x0355 }] */
    /* JADX WARN: Removed duplicated region for block: B:886:0x0567  */
    /* JADX WARN: Type inference failed for: r14v25 */
    /* JADX WARN: Type inference failed for: r14v28 */
    /* JADX WARN: Type inference failed for: r14v31 */
    /* JADX WARN: Type inference failed for: r14v34 */
    /* JADX WARN: Type inference failed for: r14v35 */
    /* JADX WARN: Type inference failed for: r14v36 */
    /* JADX WARN: Type inference failed for: r14v55 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$processRemoteMessage$7(String str, String str2, long j) {
        Throwable th;
        int i;
        final int i2;
        String str3;
        String str4;
        byte[] decode;
        NativeByteBuffer nativeByteBuffer;
        byte[] bArr;
        JSONObject jSONObject;
        String string;
        JSONObject jSONObject2;
        long clientUserId;
        int intValue;
        boolean z;
        String str5;
        String str6;
        JSONObject jSONObject3;
        long j2;
        int i3;
        long j3;
        String str7;
        long j4;
        long j5;
        int i4;
        boolean z2;
        boolean z3;
        long j6;
        long j7;
        String str8;
        int i5;
        boolean z4;
        boolean z5;
        long j8;
        boolean z6;
        JSONObject jSONObject4;
        boolean z7;
        boolean z8;
        String[] strArr;
        String str9;
        boolean z9;
        boolean z10;
        boolean z11;
        String str10;
        String str11;
        long j9;
        long j10;
        boolean z12;
        long j11;
        String str12;
        long j12;
        String str13;
        boolean z13;
        int i6;
        String str14;
        String str15;
        boolean z14;
        String str16;
        long j13;
        boolean z15;
        boolean z16;
        char c;
        String str17;
        String string2;
        boolean z17;
        boolean z18;
        int i7;
        boolean z19;
        int i8;
        String formatString;
        String string3;
        boolean z20;
        String formatString2;
        String str18;
        String string4;
        boolean z21;
        String str19;
        String string5;
        boolean z22;
        int i9;
        String str20;
        boolean z23;
        String str21;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d(str + " START PROCESSING");
        }
        try {
            decode = Base64.decode(str2, 8);
            nativeByteBuffer = new NativeByteBuffer(decode.length);
            nativeByteBuffer.writeBytes(decode);
            nativeByteBuffer.position(0);
            if (SharedConfig.pushAuthKeyId == null) {
                SharedConfig.pushAuthKeyId = new byte[8];
                byte[] computeSHA1 = Utilities.computeSHA1(SharedConfig.pushAuthKey);
                System.arraycopy(computeSHA1, computeSHA1.length - 8, SharedConfig.pushAuthKeyId, 0, 8);
            }
            bArr = new byte[8];
            nativeByteBuffer.readBytes(bArr, true);
        } catch (Throwable th2) {
            th = th2;
            i = -1;
            i2 = -1;
            str3 = null;
            str4 = null;
        }
        if (!Arrays.equals(SharedConfig.pushAuthKeyId, bArr)) {
            onDecryptError();
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d(String.format(Locale.US, str + " DECRYPT ERROR 2 k1=%s k2=%s, key=%s", Utilities.bytesToHex(SharedConfig.pushAuthKeyId), Utilities.bytesToHex(bArr), Utilities.bytesToHex(SharedConfig.pushAuthKey)));
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
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d(String.format(str + " DECRYPT ERROR 3, key = %s", Utilities.bytesToHex(SharedConfig.pushAuthKey)));
            return;
        }
        byte[] bArr4 = new byte[nativeByteBuffer.readInt32(true)];
        nativeByteBuffer.readBytes(bArr4, true);
        str4 = new String(bArr4);
        try {
            jSONObject = new JSONObject(str4);
            if (jSONObject.has("loc_key")) {
                try {
                    string = jSONObject.getString("loc_key");
                } catch (Throwable th3) {
                    th = th3;
                    th = th;
                    i = -1;
                    i2 = -1;
                    str3 = null;
                    if (i2 == i) {
                    }
                    if (BuildVars.LOGS_ENABLED) {
                    }
                    FileLog.e(th);
                }
            } else {
                string = "";
            }
            try {
                if (jSONObject.get("custom") instanceof JSONObject) {
                    try {
                        jSONObject2 = jSONObject.getJSONObject("custom");
                    } catch (Throwable th4) {
                        th = th4;
                        str3 = string;
                        i = -1;
                        i2 = -1;
                        if (i2 == i) {
                        }
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        FileLog.e(th);
                    }
                } else {
                    jSONObject2 = new JSONObject();
                }
                Object obj = jSONObject.has("user_id") ? jSONObject.get("user_id") : null;
                if (obj == null) {
                    clientUserId = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                } else if (obj instanceof Long) {
                    clientUserId = ((Long) obj).longValue();
                } else {
                    if (obj instanceof Integer) {
                        intValue = ((Integer) obj).intValue();
                    } else if (obj instanceof String) {
                        intValue = Utilities.parseInt((CharSequence) ((String) obj)).intValue();
                    } else {
                        clientUserId = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    }
                    clientUserId = intValue;
                }
                int i10 = UserConfig.selectedAccount;
                i2 = 0;
                while (true) {
                    if (i2 >= 4) {
                        i2 = i10;
                        z = false;
                        break;
                    } else if (UserConfig.getInstance(i2).getClientUserId() == clientUserId) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
            } catch (Throwable th5) {
                th = th5;
                str3 = string;
            }
        } catch (Throwable th6) {
            th = th6;
        }
        if (!z) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d(str + " ACCOUNT NOT FOUND");
            }
            countDownLatch.countDown();
            return;
        }
        try {
            try {
            } catch (Throwable th7) {
                th = th7;
            }
        } catch (Throwable th8) {
            th = th8;
        }
        if (!UserConfig.getInstance(i2).isClientActivated()) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d(str + " ACCOUNT NOT ACTIVATED");
            }
            countDownLatch.countDown();
            return;
        }
        switch (string.hashCode()) {
            case -1963663249:
                if (string.equals("SESSION_REVOKE")) {
                    str5 = 2;
                    break;
                }
                str5 = -1;
                break;
            case -920689527:
                if (string.equals("DC_UPDATE")) {
                    str5 = 0;
                    break;
                }
                str5 = -1;
                break;
            case 633004703:
                if (string.equals("MESSAGE_ANNOUNCEMENT")) {
                    str5 = 1;
                    break;
                }
                str5 = -1;
                break;
            case 1365673842:
                if (string.equals("GEO_LIVE_PENDING")) {
                    str5 = 3;
                    break;
                }
                str5 = -1;
                break;
            default:
                str5 = -1;
                break;
        }
        try {
        } catch (Throwable th9) {
            th = th9;
        }
        if (str5 == 0) {
            int i11 = jSONObject2.getInt("dc");
            String[] split = jSONObject2.getString("addr").split(":");
            if (split.length != 2) {
                countDownLatch.countDown();
                return;
            }
            ConnectionsManager.getInstance(i2).applyDatacenterAddress(i11, split[0], Integer.parseInt(split[1]));
            ConnectionsManager.getInstance(i2).resumeNetworkMaybe();
            countDownLatch.countDown();
        } else if (str5 == 1) {
            TLRPC$TL_updateServiceNotification tLRPC$TL_updateServiceNotification = new TLRPC$TL_updateServiceNotification();
            tLRPC$TL_updateServiceNotification.popup = false;
            tLRPC$TL_updateServiceNotification.flags = 2;
            tLRPC$TL_updateServiceNotification.inbox_date = (int) (j / 1000);
            tLRPC$TL_updateServiceNotification.message = jSONObject.getString("message");
            tLRPC$TL_updateServiceNotification.type = "announcement";
            tLRPC$TL_updateServiceNotification.media = new TLRPC$TL_messageMediaEmpty();
            final TLRPC$TL_updates tLRPC$TL_updates = new TLRPC$TL_updates();
            tLRPC$TL_updates.updates.add(tLRPC$TL_updateServiceNotification);
            Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PushListenerController.lambda$processRemoteMessage$4(i2, tLRPC$TL_updates);
                }
            });
            ConnectionsManager.getInstance(i2).resumeNetworkMaybe();
            countDownLatch.countDown();
        } else if (str5 == 2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PushListenerController.lambda$processRemoteMessage$5(i2);
                }
            });
            countDownLatch.countDown();
        } else if (str5 == 3) {
            final int i12 = i2;
            str6 = str4;
            str5 = string;
            try {
                Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PushListenerController.lambda$processRemoteMessage$6(i12);
                    }
                });
                countDownLatch.countDown();
            } catch (Throwable th10) {
                th = th10;
                i2 = i12;
                th = th;
                str3 = str5;
                str4 = str6;
                i = -1;
                if (i2 == i) {
                }
                if (BuildVars.LOGS_ENABLED) {
                }
                FileLog.e(th);
            }
        } else {
            try {
                if (jSONObject2.has("channel_id")) {
                    try {
                        jSONObject3 = jSONObject;
                        j2 = jSONObject2.getLong("channel_id");
                        i3 = i2;
                        j3 = -j2;
                    } catch (Throwable th11) {
                        th = th11;
                        th = th;
                        str3 = string;
                        i = -1;
                        if (i2 == i) {
                        }
                        if (BuildVars.LOGS_ENABLED) {
                        }
                        FileLog.e(th);
                    }
                } else {
                    i3 = i2;
                    jSONObject3 = jSONObject;
                    j3 = 0;
                    j2 = 0;
                }
                try {
                    if (jSONObject2.has("from_id")) {
                        try {
                            j3 = jSONObject2.getLong("from_id");
                            j4 = j3;
                        } catch (Throwable th12) {
                            th = th12;
                            str3 = string;
                            i2 = i3;
                            i = -1;
                            if (i2 == i) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            FileLog.e(th);
                        }
                    } else {
                        j4 = 0;
                    }
                    if (jSONObject2.has("chat_id")) {
                        try {
                            long j14 = jSONObject2.getLong("chat_id");
                            str7 = string;
                            j3 = -j14;
                            j5 = j14;
                        } catch (Throwable th13) {
                            th = th13;
                            str7 = string;
                            th = th;
                            str3 = str7;
                            i2 = i3;
                            i = -1;
                            if (i2 == i) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            FileLog.e(th);
                        }
                    } else {
                        str7 = string;
                        j5 = 0;
                    }
                    try {
                        if (jSONObject2.has("topic_id")) {
                            try {
                                i4 = jSONObject2.getInt("topic_id");
                            } catch (Throwable th14) {
                                th = th14;
                                th = th;
                                str3 = str7;
                                i2 = i3;
                                i = -1;
                                if (i2 == i) {
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                }
                                FileLog.e(th);
                            }
                        } else {
                            i4 = 0;
                        }
                        if (jSONObject2.has("encryption_id")) {
                            j3 = DialogObject.makeEncryptedDialogId(jSONObject2.getInt("encryption_id"));
                        }
                        z2 = jSONObject2.has("schedule") && jSONObject2.getInt("schedule") == 1;
                        if (j3 == 0 && "ENCRYPTED_MESSAGE".equals(str7)) {
                            j3 = NotificationsController.globalSecretChatId;
                        }
                    } catch (Throwable th15) {
                        th = th15;
                    }
                } catch (Throwable th16) {
                    th = th16;
                }
            } catch (Throwable th17) {
                th = th17;
                th = th;
                str3 = string;
                i = -1;
                if (i2 == i) {
                }
                if (BuildVars.LOGS_ENABLED) {
                }
                FileLog.e(th);
            }
            if (j3 != 0) {
                if ("READ_HISTORY".equals(str7)) {
                    int i13 = jSONObject2.getInt("max_id");
                    ArrayList<TLRPC$Update> arrayList = new ArrayList<>();
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d(str + " received read notification max_id = " + i13 + " for dialogId = " + j3);
                    }
                    if (j2 != 0) {
                        TLRPC$TL_updateReadChannelInbox tLRPC$TL_updateReadChannelInbox = new TLRPC$TL_updateReadChannelInbox();
                        tLRPC$TL_updateReadChannelInbox.channel_id = j2;
                        tLRPC$TL_updateReadChannelInbox.max_id = i13;
                        tLRPC$TL_updateReadChannelInbox.still_unread_count = 0;
                        arrayList.add(tLRPC$TL_updateReadChannelInbox);
                    } else {
                        TLRPC$TL_updateReadHistoryInbox tLRPC$TL_updateReadHistoryInbox = new TLRPC$TL_updateReadHistoryInbox();
                        long j15 = j4;
                        if (j15 != 0) {
                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                            tLRPC$TL_updateReadHistoryInbox.peer = tLRPC$TL_peerUser;
                            tLRPC$TL_peerUser.user_id = j15;
                        } else {
                            TLRPC$TL_peerChat tLRPC$TL_peerChat = new TLRPC$TL_peerChat();
                            tLRPC$TL_updateReadHistoryInbox.peer = tLRPC$TL_peerChat;
                            tLRPC$TL_peerChat.chat_id = j5;
                        }
                        tLRPC$TL_updateReadHistoryInbox.max_id = i13;
                        arrayList.add(tLRPC$TL_updateReadHistoryInbox);
                    }
                    MessagesController.getInstance(i3).processUpdateArray(arrayList, null, null, false, 0);
                } else {
                    boolean z24 = z2;
                    long j16 = j4;
                    str6 = str4;
                    try {
                    } catch (Throwable th18) {
                        th = th18;
                        str3 = str7;
                        i2 = i3;
                    }
                    if ("MESSAGE_DELETED".equals(str7)) {
                        String[] split2 = jSONObject2.getString("messages").split(",");
                        LongSparseArray<ArrayList<Integer>> longSparseArray = new LongSparseArray<>();
                        ArrayList<Integer> arrayList2 = new ArrayList<>();
                        for (String str22 : split2) {
                            arrayList2.add(Utilities.parseInt((CharSequence) str22));
                        }
                        longSparseArray.put(-j2, arrayList2);
                        NotificationsController.getInstance(i3).removeDeletedMessagesFromNotifications(longSparseArray, false);
                        MessagesController.getInstance(i3).deleteMessagesByPush(j3, arrayList2, j2);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d(str + " received " + str7 + " for dialogId = " + j3 + " mids = " + TextUtils.join(",", arrayList2));
                        }
                    } else if ("READ_REACTION".equals(str7)) {
                        LongSparseArray<ArrayList<Integer>> longSparseArray2 = new LongSparseArray<>();
                        ArrayList<Integer> arrayList3 = new ArrayList<>();
                        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
                        int i14 = 0;
                        for (String[] split3 = jSONObject2.getString("messages").split(","); i14 < split3.length; split3 = split3) {
                            int intValue2 = Utilities.parseInt((CharSequence) split3[i14]).intValue();
                            arrayList3.add(Integer.valueOf(intValue2));
                            sparseBooleanArray.put(intValue2, false);
                            i14++;
                        }
                        longSparseArray2.put(-j2, arrayList3);
                        NotificationsController.getInstance(i3).removeDeletedMessagesFromNotifications(longSparseArray2, true);
                        MessagesController.getInstance(i3).checkUnreadReactions(j3, i4, sparseBooleanArray);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d(str + " received " + str7 + " for dialogId = " + j3 + " mids = " + TextUtils.join(",", arrayList3));
                        }
                    } else if (!TextUtils.isEmpty(str7)) {
                        int i15 = jSONObject2.has("msg_id") ? jSONObject2.getInt("msg_id") : 0;
                        if (jSONObject2.has("random_id")) {
                            long j17 = j5;
                            j7 = Utilities.parseLong(jSONObject2.getString("random_id")).longValue();
                            j6 = j17;
                        } else {
                            j6 = j5;
                            j7 = 0;
                        }
                        if (i15 != 0) {
                            i5 = i4;
                            Integer num = MessagesController.getInstance(i3).dialogs_read_inbox_max.get(Long.valueOf(j3));
                            if (num == null) {
                                num = Integer.valueOf(MessagesStorage.getInstance(i3).getDialogReadMax(false, j3));
                                str8 = "messages";
                                MessagesController.getInstance(i3).dialogs_read_inbox_max.put(Long.valueOf(j3), num);
                            } else {
                                str8 = "messages";
                            }
                        } else {
                            str8 = "messages";
                            i5 = i4;
                            if (j7 != 0 && !MessagesStorage.getInstance(i3).checkMessageByRandomId(j7)) {
                                z4 = true;
                            }
                            z4 = false;
                        }
                        if (str7.startsWith("REACT_") || str7.startsWith("CHAT_REACT_")) {
                            z4 = true;
                        }
                        if (z4) {
                            long j18 = j7;
                            long optLong = jSONObject2.optLong("chat_from_id", 0L);
                            long optLong2 = jSONObject2.optLong("chat_from_broadcast_id", 0L);
                            long optLong3 = jSONObject2.optLong("chat_from_group_id", 0L);
                            if (optLong == 0 && optLong3 == 0) {
                                z5 = false;
                                boolean z25 = !jSONObject2.has("mention") && jSONObject2.getInt("mention") != 0;
                                if (jSONObject2.has("silent") || jSONObject2.getInt("silent") == 0) {
                                    j8 = optLong2;
                                    z6 = false;
                                } else {
                                    j8 = optLong2;
                                    z6 = true;
                                }
                                jSONObject4 = jSONObject3;
                                if (!jSONObject4.has("loc_args")) {
                                    JSONArray jSONArray = jSONObject4.getJSONArray("loc_args");
                                    int length = jSONArray.length();
                                    z8 = z6;
                                    String[] strArr2 = new String[length];
                                    z7 = z25;
                                    for (int i16 = 0; i16 < length; i16++) {
                                        strArr2[i16] = jSONArray.getString(i16);
                                    }
                                    strArr = strArr2;
                                } else {
                                    z7 = z25;
                                    z8 = z6;
                                    strArr = null;
                                }
                                String str23 = strArr[0];
                                boolean has = jSONObject2.has("edit_date");
                                if (!str7.startsWith("CHAT_")) {
                                    if (!UserObject.isReplyUser(j3)) {
                                        z10 = j2 != 0;
                                        z11 = false;
                                        str9 = str23;
                                        str23 = strArr[1];
                                        z9 = false;
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (str7.startsWith("REACT_")) {
                                        }
                                        str16 = getReactedText(str7, strArr);
                                        z14 = false;
                                        str15 = null;
                                        if (str16 != null) {
                                        }
                                    } else {
                                        str23 = str23 + " @ " + strArr[1];
                                        str9 = null;
                                        z9 = false;
                                        z10 = false;
                                        z11 = false;
                                        if (BuildVars.LOGS_ENABLED) {
                                            StringBuilder sb = new StringBuilder();
                                            str10 = str23;
                                            str11 = str9;
                                            j9 = j8;
                                            j10 = optLong;
                                            sb.append(str);
                                            sb.append(" received message notification ");
                                            sb.append(str7);
                                            sb.append(" for dialogId = ");
                                            sb.append(j3);
                                            sb.append(" mid = ");
                                            sb.append(i15);
                                            FileLog.d(sb.toString());
                                        } else {
                                            str10 = str23;
                                            str11 = str9;
                                            j9 = j8;
                                            j10 = optLong;
                                        }
                                        if (str7.startsWith("REACT_")) {
                                            z12 = has;
                                            j11 = j2;
                                            str12 = "REACT_";
                                            j12 = j9;
                                            str13 = str11;
                                            z13 = z8;
                                            i6 = i5;
                                            str14 = "CHAT_REACT_";
                                        } else if (str7.startsWith("CHAT_REACT_")) {
                                            z12 = has;
                                            j11 = j2;
                                            str12 = "REACT_";
                                            str14 = "CHAT_REACT_";
                                            j12 = j9;
                                            str13 = str11;
                                            z13 = z8;
                                            i6 = i5;
                                        } else {
                                            switch (str7.hashCode()) {
                                                case -2100047043:
                                                    if (str7.equals("MESSAGE_GAME_SCORE")) {
                                                        c = 20;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -2091498420:
                                                    if (str7.equals("CHANNEL_MESSAGE_CONTACT")) {
                                                        c = '$';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -2053872415:
                                                    if (str7.equals("CHAT_CREATED")) {
                                                        c = 'B';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -2039746363:
                                                    if (str7.equals("MESSAGE_STICKER")) {
                                                        c = 11;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -2023218804:
                                                    if (str7.equals("CHANNEL_MESSAGE_VIDEOS")) {
                                                        c = '-';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1979538588:
                                                    if (str7.equals("CHANNEL_MESSAGE_DOC")) {
                                                        c = '!';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1979536003:
                                                    if (str7.equals("CHANNEL_MESSAGE_GEO")) {
                                                        c = '\'';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1979535888:
                                                    if (str7.equals("CHANNEL_MESSAGE_GIF")) {
                                                        c = ')';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1969004705:
                                                    if (str7.equals("CHAT_ADD_MEMBER")) {
                                                        c = 'F';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1946699248:
                                                    if (str7.equals("CHAT_JOINED")) {
                                                        c = 'O';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1717283471:
                                                    if (str7.equals("CHAT_REQ_JOINED")) {
                                                        c = 'P';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1646640058:
                                                    if (str7.equals("CHAT_VOICECHAT_START")) {
                                                        c = 'G';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1528047021:
                                                    if (str7.equals("CHAT_MESSAGES")) {
                                                        c = 'V';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1507149394:
                                                    if (str7.equals("MESSAGE_RECURRING_PAY")) {
                                                        str17 = "CHAT_REACT_";
                                                        c = 0;
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1493579426:
                                                    if (str7.equals("MESSAGE_AUDIO")) {
                                                        c = '\f';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1482481933:
                                                    if (str7.equals("MESSAGE_MUTED")) {
                                                        c = 'q';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1480102982:
                                                    if (str7.equals("MESSAGE_PHOTO")) {
                                                        str17 = "CHAT_REACT_";
                                                        c = 4;
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1478041834:
                                                    if (str7.equals("MESSAGE_ROUND")) {
                                                        c = '\t';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1474543101:
                                                    if (str7.equals("MESSAGE_VIDEO")) {
                                                        c = 6;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1465695932:
                                                    if (str7.equals("ENCRYPTION_ACCEPT")) {
                                                        c = 'o';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1374906292:
                                                    if (str7.equals("ENCRYPTED_MESSAGE")) {
                                                        c = 'h';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1372940586:
                                                    if (str7.equals("CHAT_RETURNED")) {
                                                        c = 'N';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1264245338:
                                                    if (str7.equals("PINNED_INVOICE")) {
                                                        c = 'f';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1236154001:
                                                    if (str7.equals("CHANNEL_MESSAGE_DOCS")) {
                                                        c = '/';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1236086700:
                                                    if (str7.equals("CHANNEL_MESSAGE_FWDS")) {
                                                        c = '+';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1236077786:
                                                    if (str7.equals("CHANNEL_MESSAGE_GAME")) {
                                                        c = '*';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1235796237:
                                                    if (str7.equals("CHANNEL_MESSAGE_POLL")) {
                                                        c = '&';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1235760759:
                                                    if (str7.equals("CHANNEL_MESSAGE_QUIZ")) {
                                                        c = '%';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1235686303:
                                                    if (str7.equals("CHANNEL_MESSAGE_TEXT")) {
                                                        str17 = "CHAT_REACT_";
                                                        c = 2;
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1198046100:
                                                    if (str7.equals("MESSAGE_VIDEO_SECRET")) {
                                                        c = 7;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1124254527:
                                                    if (str7.equals("CHAT_MESSAGE_CONTACT")) {
                                                        c = '9';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1085137927:
                                                    if (str7.equals("PINNED_GAME")) {
                                                        c = 'd';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1084856378:
                                                    if (str7.equals("PINNED_POLL")) {
                                                        c = 'a';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1084820900:
                                                    if (str7.equals("PINNED_QUIZ")) {
                                                        c = '`';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -1084746444:
                                                    if (str7.equals("PINNED_TEXT")) {
                                                        c = 'W';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -819729482:
                                                    if (str7.equals("PINNED_STICKER")) {
                                                        c = ']';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -772141857:
                                                    if (str7.equals("PHONE_CALL_REQUEST")) {
                                                        c = 'p';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -638310039:
                                                    if (str7.equals("CHANNEL_MESSAGE_STICKER")) {
                                                        c = '\"';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -590403924:
                                                    if (str7.equals("PINNED_GAME_SCORE")) {
                                                        c = 'e';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -589196239:
                                                    if (str7.equals("PINNED_DOC")) {
                                                        c = '\\';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -589193654:
                                                    if (str7.equals("PINNED_GEO")) {
                                                        c = 'b';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -589193539:
                                                    if (str7.equals("PINNED_GIF")) {
                                                        c = 'g';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -440169325:
                                                    if (str7.equals("AUTH_UNKNOWN")) {
                                                        c = 'k';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -412748110:
                                                    if (str7.equals("CHAT_DELETE_YOU")) {
                                                        c = 'L';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -228518075:
                                                    if (str7.equals("MESSAGE_GEOLIVE")) {
                                                        c = 17;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -213586509:
                                                    if (str7.equals("ENCRYPTION_REQUEST")) {
                                                        c = 'n';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -115582002:
                                                    if (str7.equals("CHAT_MESSAGE_INVOICE")) {
                                                        c = 'A';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -112621464:
                                                    if (str7.equals("CONTACT_JOINED")) {
                                                        c = 'j';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -108522133:
                                                    if (str7.equals("AUTH_REGION")) {
                                                        c = 'l';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -107572034:
                                                    if (str7.equals("MESSAGE_SCREENSHOT")) {
                                                        str17 = "CHAT_REACT_";
                                                        c = '\b';
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case -40534265:
                                                    if (str7.equals("CHAT_DELETE_MEMBER")) {
                                                        c = 'K';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 52369421:
                                                    if (str7.equals("REACT_TEXT")) {
                                                        c = 'i';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 65254746:
                                                    if (str7.equals("CHAT_ADD_YOU")) {
                                                        c = 'C';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 141040782:
                                                    if (str7.equals("CHAT_LEFT")) {
                                                        c = 'M';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 202550149:
                                                    if (str7.equals("CHAT_VOICECHAT_INVITE")) {
                                                        c = 'H';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 309993049:
                                                    if (str7.equals("CHAT_MESSAGE_DOC")) {
                                                        c = '6';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 309995634:
                                                    if (str7.equals("CHAT_MESSAGE_GEO")) {
                                                        c = '<';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 309995749:
                                                    if (str7.equals("CHAT_MESSAGE_GIF")) {
                                                        c = '>';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 320532812:
                                                    if (str7.equals("MESSAGES")) {
                                                        c = 28;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 328933854:
                                                    if (str7.equals("CHAT_MESSAGE_STICKER")) {
                                                        c = '7';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 331340546:
                                                    if (str7.equals("CHANNEL_MESSAGE_AUDIO")) {
                                                        c = '#';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 342406591:
                                                    if (str7.equals("CHAT_VOICECHAT_END")) {
                                                        c = 'I';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 344816990:
                                                    if (str7.equals("CHANNEL_MESSAGE_PHOTO")) {
                                                        c = 30;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 346878138:
                                                    if (str7.equals("CHANNEL_MESSAGE_ROUND")) {
                                                        c = ' ';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 350376871:
                                                    if (str7.equals("CHANNEL_MESSAGE_VIDEO")) {
                                                        c = 31;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 608430149:
                                                    if (str7.equals("CHAT_VOICECHAT_INVITE_YOU")) {
                                                        c = 'J';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 615714517:
                                                    if (str7.equals("MESSAGE_PHOTO_SECRET")) {
                                                        c = 5;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 715508879:
                                                    if (str7.equals("PINNED_AUDIO")) {
                                                        c = '^';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 728985323:
                                                    if (str7.equals("PINNED_PHOTO")) {
                                                        c = 'Y';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 731046471:
                                                    if (str7.equals("PINNED_ROUND")) {
                                                        c = '[';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 734545204:
                                                    if (str7.equals("PINNED_VIDEO")) {
                                                        c = 'Z';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 802032552:
                                                    if (str7.equals("MESSAGE_CONTACT")) {
                                                        c = '\r';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 991498806:
                                                    if (str7.equals("PINNED_GEOLIVE")) {
                                                        c = 'c';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1007364121:
                                                    if (str7.equals("CHANNEL_MESSAGE_GAME_SCORE")) {
                                                        c = 21;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1019850010:
                                                    if (str7.equals("CHAT_MESSAGE_DOCS")) {
                                                        c = 'U';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1019917311:
                                                    if (str7.equals("CHAT_MESSAGE_FWDS")) {
                                                        c = 'Q';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1019926225:
                                                    if (str7.equals("CHAT_MESSAGE_GAME")) {
                                                        c = '?';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1020207774:
                                                    if (str7.equals("CHAT_MESSAGE_POLL")) {
                                                        c = ';';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1020243252:
                                                    if (str7.equals("CHAT_MESSAGE_QUIZ")) {
                                                        c = ':';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1020317708:
                                                    if (str7.equals("CHAT_MESSAGE_TEXT")) {
                                                        c = '1';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1060282259:
                                                    if (str7.equals("MESSAGE_DOCS")) {
                                                        c = 27;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1060349560:
                                                    if (str7.equals("MESSAGE_FWDS")) {
                                                        c = 23;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1060358474:
                                                    if (str7.equals("MESSAGE_GAME")) {
                                                        c = 19;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1060640023:
                                                    if (str7.equals("MESSAGE_POLL")) {
                                                        c = 15;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1060675501:
                                                    if (str7.equals("MESSAGE_QUIZ")) {
                                                        c = 14;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1060749957:
                                                    if (str7.equals("MESSAGE_TEXT")) {
                                                        str17 = "CHAT_REACT_";
                                                        c = 1;
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1073049781:
                                                    if (str7.equals("PINNED_NOTEXT")) {
                                                        c = 'X';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1078101399:
                                                    if (str7.equals("CHAT_TITLE_EDITED")) {
                                                        c = 'D';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1110103437:
                                                    if (str7.equals("CHAT_MESSAGE_NOTEXT")) {
                                                        c = '2';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1160762272:
                                                    if (str7.equals("CHAT_MESSAGE_PHOTOS")) {
                                                        c = 'R';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1172918249:
                                                    if (str7.equals("CHANNEL_MESSAGE_GEOLIVE")) {
                                                        c = '(';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1234591620:
                                                    if (str7.equals("CHAT_MESSAGE_GAME_SCORE")) {
                                                        c = '@';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1281128640:
                                                    if (str7.equals("MESSAGE_DOC")) {
                                                        c = '\n';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1281131225:
                                                    if (str7.equals("MESSAGE_GEO")) {
                                                        c = 16;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1281131340:
                                                    if (str7.equals("MESSAGE_GIF")) {
                                                        c = 18;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1310789062:
                                                    if (str7.equals("MESSAGE_NOTEXT")) {
                                                        str17 = "CHAT_REACT_";
                                                        c = 3;
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1333118583:
                                                    if (str7.equals("CHAT_MESSAGE_VIDEOS")) {
                                                        c = 'S';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1361447897:
                                                    if (str7.equals("MESSAGE_PHOTOS")) {
                                                        c = 24;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1498266155:
                                                    if (str7.equals("PHONE_CALL_MISSED")) {
                                                        c = 'r';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1533804208:
                                                    if (str7.equals("MESSAGE_VIDEOS")) {
                                                        c = 25;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1540131626:
                                                    if (str7.equals("MESSAGE_PLAYLIST")) {
                                                        c = 26;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1547988151:
                                                    if (str7.equals("CHAT_MESSAGE_AUDIO")) {
                                                        c = '8';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1561464595:
                                                    if (str7.equals("CHAT_MESSAGE_PHOTO")) {
                                                        c = '3';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1563525743:
                                                    if (str7.equals("CHAT_MESSAGE_ROUND")) {
                                                        c = '5';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1567024476:
                                                    if (str7.equals("CHAT_MESSAGE_VIDEO")) {
                                                        c = '4';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1810705077:
                                                    if (str7.equals("MESSAGE_INVOICE")) {
                                                        c = 22;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1815177512:
                                                    if (str7.equals("CHANNEL_MESSAGES")) {
                                                        c = '0';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1954774321:
                                                    if (str7.equals("CHAT_MESSAGE_PLAYLIST")) {
                                                        c = 'T';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 1963241394:
                                                    if (str7.equals("LOCKED_MESSAGE")) {
                                                        c = 'm';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 2014789757:
                                                    if (str7.equals("CHAT_PHOTO_EDITED")) {
                                                        c = 'E';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 2022049433:
                                                    if (str7.equals("PINNED_CONTACT")) {
                                                        c = '_';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 2034984710:
                                                    if (str7.equals("CHANNEL_MESSAGE_PLAYLIST")) {
                                                        c = '.';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 2048733346:
                                                    if (str7.equals("CHANNEL_MESSAGE_NOTEXT")) {
                                                        c = 29;
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 2099392181:
                                                    if (str7.equals("CHANNEL_MESSAGE_PHOTOS")) {
                                                        c = ',';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                case 2140162142:
                                                    if (str7.equals("CHAT_MESSAGE_GEOLIVE")) {
                                                        c = '=';
                                                        str17 = "CHAT_REACT_";
                                                        break;
                                                    }
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                                default:
                                                    str17 = "CHAT_REACT_";
                                                    c = 65535;
                                                    break;
                                            }
                                            str12 = "REACT_";
                                            z12 = has;
                                            str13 = str11;
                                            j12 = j9;
                                            j11 = j2;
                                            switch (c) {
                                                case 0:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageRecurringPay", R.string.NotificationMessageRecurringPay, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("PaymentInvoice", R.string.PaymentInvoice);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 1:
                                                case 2:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    String formatString3 = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, strArr[0], strArr[1]);
                                                    str15 = strArr[1];
                                                    str16 = formatString3;
                                                    z14 = false;
                                                    break;
                                                case 3:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, strArr[0]);
                                                    string2 = LocaleController.getString("Message", R.string.Message);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 4:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, strArr[0]);
                                                    string2 = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 5:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, strArr[0]);
                                                    string2 = LocaleController.getString("AttachDestructingPhoto", R.string.AttachDestructingPhoto);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 6:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, strArr[0]);
                                                    string2 = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 7:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, strArr[0]);
                                                    string2 = LocaleController.getString("AttachDestructingVideo", R.string.AttachDestructingVideo);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case '\b':
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.getString("ActionTakeScreenshoot", R.string.ActionTakeScreenshoot).replace("un1", strArr[0]);
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case '\t':
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, strArr[0]);
                                                    string2 = LocaleController.getString("AttachRound", R.string.AttachRound);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case '\n':
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, strArr[0]);
                                                    string2 = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 11:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                        str15 = strArr[1] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                        str16 = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, strArr[0], strArr[1]);
                                                        z14 = false;
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, strArr[0]);
                                                        string2 = LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                        str15 = string2;
                                                        z14 = false;
                                                        break;
                                                    }
                                                    break;
                                                case '\f':
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, strArr[0]);
                                                    string2 = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case '\r':
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("AttachContact", R.string.AttachContact);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 14:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("QuizPoll", R.string.QuizPoll);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 15:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("Poll", R.string.Poll);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 16:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, strArr[0]);
                                                    string2 = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 17:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, strArr[0]);
                                                    string2 = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 18:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, strArr[0]);
                                                    string2 = LocaleController.getString("AttachGif", R.string.AttachGif);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 19:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("AttachGame", R.string.AttachGame);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 20:
                                                case 21:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGameScored", R.string.NotificationMessageGameScored, strArr[0], strArr[1], strArr[2]);
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 22:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageInvoice", R.string.NotificationMessageInvoice, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("PaymentInvoice", R.string.PaymentInvoice);
                                                    str15 = string2;
                                                    z14 = false;
                                                    break;
                                                case 23:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageForwardFew", R.string.NotificationMessageForwardFew, strArr[0], LocaleController.formatPluralString(str8, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 24:
                                                    z17 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z17;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 25:
                                                    z17 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z17;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 26:
                                                    z17 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z17;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 27:
                                                    z17 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z17;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 28:
                                                    z18 = z8;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageAlbum", R.string.NotificationMessageAlbum, strArr[0]);
                                                    z13 = z18;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 29:
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, strArr[0]);
                                                    string3 = LocaleController.getString("Message", R.string.Message);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case 30:
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessagePhoto", R.string.ChannelMessagePhoto, strArr[0]);
                                                    string3 = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case 31:
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageVideo", R.string.ChannelMessageVideo, strArr[0]);
                                                    string3 = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case ' ':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageRound", R.string.ChannelMessageRound, strArr[0]);
                                                    string3 = LocaleController.getString("AttachRound", R.string.AttachRound);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '!':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageDocument", R.string.ChannelMessageDocument, strArr[0]);
                                                    string3 = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '\"':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                        formatString = LocaleController.formatString("ChannelMessageStickerEmoji", R.string.ChannelMessageStickerEmoji, strArr[0], strArr[1]);
                                                        string3 = strArr[1] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    } else {
                                                        formatString = LocaleController.formatString("ChannelMessageSticker", R.string.ChannelMessageSticker, strArr[0]);
                                                        string3 = LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    }
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '#':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageAudio", R.string.ChannelMessageAudio, strArr[0]);
                                                    string3 = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '$':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageContact2", R.string.ChannelMessageContact2, strArr[0], strArr[1]);
                                                    string3 = LocaleController.getString("AttachContact", R.string.AttachContact);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '%':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageQuiz2", R.string.ChannelMessageQuiz2, strArr[0], strArr[1]);
                                                    string3 = LocaleController.getString("QuizPoll", R.string.QuizPoll);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '&':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessagePoll2", R.string.ChannelMessagePoll2, strArr[0], strArr[1]);
                                                    string3 = LocaleController.getString("Poll", R.string.Poll);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '\'':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageMap", R.string.ChannelMessageMap, strArr[0]);
                                                    string3 = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '(':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageLiveLocation", R.string.ChannelMessageLiveLocation, strArr[0]);
                                                    string3 = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case ')':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("ChannelMessageGIF", R.string.ChannelMessageGIF, strArr[0]);
                                                    string3 = LocaleController.getString("AttachGif", R.string.AttachGif);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '*':
                                                    z19 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0]);
                                                    string3 = LocaleController.getString("AttachGame", R.string.AttachGame);
                                                    str15 = string3;
                                                    str16 = formatString;
                                                    z13 = z19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '+':
                                                    z18 = z8;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("ForwardedMessageCount", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]).toLowerCase());
                                                    z13 = z18;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case ',':
                                                    z18 = z8;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z18;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case '-':
                                                    z18 = z8;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z18;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case '.':
                                                    z18 = z8;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z18;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case '/':
                                                    z18 = z8;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = z18;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case '0':
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageAlbum", R.string.ChannelMessageAlbum, strArr[0]);
                                                    z13 = z8;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case '1':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, strArr[0], strArr[1], strArr[2]);
                                                    str18 = strArr[2];
                                                    str15 = str18;
                                                    str16 = formatString2;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '2':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("Message", R.string.Message);
                                                    str15 = string4;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '3':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupPhoto", R.string.NotificationMessageGroupPhoto, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                                                    str15 = string4;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '4':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupVideo", R.string.NotificationMessageGroupVideo, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                                                    str15 = string4;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '5':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupRound", R.string.NotificationMessageGroupRound, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachRound", R.string.AttachRound);
                                                    str15 = string4;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '6':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupDocument", R.string.NotificationMessageGroupDocument, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                                                    str15 = string4;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '7':
                                                    z20 = z8;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    if (strArr.length > 2 && !TextUtils.isEmpty(strArr[2])) {
                                                        formatString2 = LocaleController.formatString("NotificationMessageGroupStickerEmoji", R.string.NotificationMessageGroupStickerEmoji, strArr[0], strArr[1], strArr[2]);
                                                        str18 = strArr[2] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    } else {
                                                        formatString2 = LocaleController.formatString("NotificationMessageGroupSticker", R.string.NotificationMessageGroupSticker, strArr[0], strArr[1]);
                                                        str18 = strArr[1] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    }
                                                    str15 = str18;
                                                    str16 = formatString2;
                                                    z13 = z20;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '8':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupAudio", R.string.NotificationMessageGroupAudio, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '9':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupContact2", R.string.NotificationMessageGroupContact2, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("AttachContact", R.string.AttachContact);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case ':':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupQuiz2", R.string.NotificationMessageGroupQuiz2, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("PollQuiz", R.string.PollQuiz);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case ';':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupPoll2", R.string.NotificationMessageGroupPoll2, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("Poll", R.string.Poll);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '<':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '=':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupLiveLocation", R.string.NotificationMessageGroupLiveLocation, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '>':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupGif", R.string.NotificationMessageGroupGif, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachGif", R.string.AttachGif);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '?':
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupGame", R.string.NotificationMessageGroupGame, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("AttachGame", R.string.AttachGame);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case '@':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupGameScored", R.string.NotificationMessageGroupGameScored, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case VoIPService.CALL_MIN_LAYER /* 65 */:
                                                    z21 = z8;
                                                    i8 = i5;
                                                    str19 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupInvoice", R.string.NotificationMessageGroupInvoice, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("PaymentInvoice", R.string.PaymentInvoice);
                                                    str15 = string5;
                                                    z13 = z21;
                                                    str14 = str19;
                                                    i6 = i8;
                                                    z14 = false;
                                                    break;
                                                case 'B':
                                                case 'C':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationInvitedToGroup", R.string.NotificationInvitedToGroup, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'D':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationEditedGroupName", R.string.NotificationEditedGroupName, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'E':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationEditedGroupPhoto", R.string.NotificationEditedGroupPhoto, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'F':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, strArr[0], strArr[1], strArr[2]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'G':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupCreatedCall", R.string.NotificationGroupCreatedCall, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'H':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupInvitedToCall", R.string.NotificationGroupInvitedToCall, strArr[0], strArr[1], strArr[2]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'I':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupEndedCall", R.string.NotificationGroupEndedCall, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'J':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupInvitedYouToCall", R.string.NotificationGroupInvitedYouToCall, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'K':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupKickMember", R.string.NotificationGroupKickMember, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'L':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupKickYou", R.string.NotificationGroupKickYou, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'M':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupLeftMember", R.string.NotificationGroupLeftMember, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'N':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAddSelf", R.string.NotificationGroupAddSelf, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'O':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAddSelfMega", R.string.NotificationGroupAddSelfMega, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'P':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("UserAcceptedToGroupPushWithGroup", R.string.UserAcceptedToGroupPushWithGroup, strArr[0], strArr[1]);
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'Q':
                                                    z23 = z8;
                                                    i7 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupForwardedFew", R.string.NotificationGroupForwardedFew, strArr[0], strArr[1], LocaleController.formatPluralString(str8, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z13 = z23;
                                                    str14 = str21;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'R':
                                                    z23 = z8;
                                                    i7 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z13 = z23;
                                                    str14 = str21;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'S':
                                                    z23 = z8;
                                                    i7 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z13 = z23;
                                                    str14 = str21;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'T':
                                                    z23 = z8;
                                                    i7 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z13 = z23;
                                                    str14 = str21;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'U':
                                                    z23 = z8;
                                                    i7 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z13 = z23;
                                                    str14 = str21;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'V':
                                                    z23 = z8;
                                                    i7 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAlbum", R.string.NotificationGroupAlbum, strArr[0], strArr[1]);
                                                    z13 = z23;
                                                    str14 = str21;
                                                    i6 = i7;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'W':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedTextUser", R.string.NotificationActionPinnedTextUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, strArr[0], strArr[1], strArr[2]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, strArr[0], strArr[1]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'X':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedNoTextUser", R.string.NotificationActionPinnedNoTextUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'Y':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPhotoUser", R.string.NotificationActionPinnedPhotoUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPhoto", R.string.NotificationActionPinnedPhoto, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPhotoChannel", R.string.NotificationActionPinnedPhotoChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'Z':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVideoUser", R.string.NotificationActionPinnedVideoUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVideo", R.string.NotificationActionPinnedVideo, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVideoChannel", R.string.NotificationActionPinnedVideoChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case '[':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedRoundUser", R.string.NotificationActionPinnedRoundUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedRound", R.string.NotificationActionPinnedRound, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedRoundChannel", R.string.NotificationActionPinnedRoundChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case '\\':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedFileUser", R.string.NotificationActionPinnedFileUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedFile", R.string.NotificationActionPinnedFile, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedFileChannel", R.string.NotificationActionPinnedFileChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case ']':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", R.string.NotificationActionPinnedStickerEmojiUser, strArr[0], strArr[1]);
                                                        } else {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedStickerUser", R.string.NotificationActionPinnedStickerUser, strArr[0]);
                                                        }
                                                    } else if (z5) {
                                                        if (strArr.length > 2 && !TextUtils.isEmpty(strArr[2])) {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedStickerEmoji", R.string.NotificationActionPinnedStickerEmoji, strArr[0], strArr[2], strArr[1]);
                                                        } else {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedSticker", R.string.NotificationActionPinnedSticker, strArr[0], strArr[1]);
                                                        }
                                                    } else if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", R.string.NotificationActionPinnedStickerEmojiChannel, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedStickerChannel", R.string.NotificationActionPinnedStickerChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case '^':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVoiceUser", R.string.NotificationActionPinnedVoiceUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVoice", R.string.NotificationActionPinnedVoice, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVoiceChannel", R.string.NotificationActionPinnedVoiceChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case '_':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedContactUser", R.string.NotificationActionPinnedContactUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedContact2", R.string.NotificationActionPinnedContact2, strArr[0], strArr[2], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedContactChannel2", R.string.NotificationActionPinnedContactChannel2, strArr[0], strArr[1]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case '`':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedQuizUser", R.string.NotificationActionPinnedQuizUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedQuiz2", R.string.NotificationActionPinnedQuiz2, strArr[0], strArr[2], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedQuizChannel2", R.string.NotificationActionPinnedQuizChannel2, strArr[0], strArr[1]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'a':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPollUser", R.string.NotificationActionPinnedPollUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPoll2", R.string.NotificationActionPinnedPoll2, strArr[0], strArr[2], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPollChannel2", R.string.NotificationActionPinnedPollChannel2, strArr[0], strArr[1]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'b':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoUser", R.string.NotificationActionPinnedGeoUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeo", R.string.NotificationActionPinnedGeo, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoChannel", R.string.NotificationActionPinnedGeoChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'c':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoLiveUser", R.string.NotificationActionPinnedGeoLiveUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoLive", R.string.NotificationActionPinnedGeoLive, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", R.string.NotificationActionPinnedGeoLiveChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case FileLoader.MEDIA_DIR_IMAGE_PUBLIC /* 100 */:
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameUser", R.string.NotificationActionPinnedGameUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGame", R.string.NotificationActionPinnedGame, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameChannel", R.string.NotificationActionPinnedGameChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case FileLoader.MEDIA_DIR_VIDEO_PUBLIC /* 101 */:
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameScoreUser", R.string.NotificationActionPinnedGameScoreUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameScore", R.string.NotificationActionPinnedGameScore, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameScoreChannel", R.string.NotificationActionPinnedGameScoreChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'f':
                                                    z22 = z8;
                                                    i9 = i5;
                                                    str20 = str17;
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedInvoiceUser", R.string.NotificationActionPinnedInvoiceUser, strArr[0], strArr[1]);
                                                    } else if (z5) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedInvoice", R.string.NotificationActionPinnedInvoice, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedInvoiceChannel", R.string.NotificationActionPinnedInvoiceChannel, strArr[0]);
                                                    }
                                                    z13 = z22;
                                                    str14 = str20;
                                                    i6 = i9;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                case 'g':
                                                    if (j3 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGifUser", R.string.NotificationActionPinnedGifUser, strArr[0], strArr[1]);
                                                        z13 = z8;
                                                        i6 = i5;
                                                        str14 = str17;
                                                        z14 = false;
                                                        str15 = null;
                                                        break;
                                                    } else {
                                                        z22 = z8;
                                                        i9 = i5;
                                                        str20 = str17;
                                                        if (z5) {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedGif", R.string.NotificationActionPinnedGif, strArr[0], strArr[1]);
                                                        } else {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedGifChannel", R.string.NotificationActionPinnedGifChannel, strArr[0]);
                                                        }
                                                        z13 = z22;
                                                        str14 = str20;
                                                        i6 = i9;
                                                        z14 = false;
                                                        str15 = null;
                                                    }
                                                case 'h':
                                                    str16 = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
                                                    str10 = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    z14 = true;
                                                    str15 = null;
                                                    break;
                                                case 'i':
                                                case 'j':
                                                case 'k':
                                                case 'l':
                                                case 'm':
                                                case 'n':
                                                case 'o':
                                                case 'p':
                                                case 'q':
                                                case 'r':
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = null;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                                default:
                                                    z13 = z8;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.w("unhandled loc_key = " + str7);
                                                    }
                                                    str16 = null;
                                                    z14 = false;
                                                    str15 = null;
                                                    break;
                                            }
                                            if (str16 != null) {
                                                TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                                                tLRPC$TL_message.id = i15;
                                                tLRPC$TL_message.random_id = j18;
                                                tLRPC$TL_message.message = str15 != null ? str15 : str16;
                                                tLRPC$TL_message.date = (int) (j / 1000);
                                                if (z11) {
                                                    tLRPC$TL_message.action = new TLRPC$TL_messageActionPinMessage();
                                                }
                                                if (z10) {
                                                    tLRPC$TL_message.flags |= Integer.MIN_VALUE;
                                                }
                                                tLRPC$TL_message.dialog_id = j3;
                                                if (j11 != 0) {
                                                    TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                                    tLRPC$TL_peerChannel.channel_id = j11;
                                                    j13 = j6;
                                                } else if (j6 != 0) {
                                                    TLRPC$TL_peerChat tLRPC$TL_peerChat2 = new TLRPC$TL_peerChat();
                                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerChat2;
                                                    j13 = j6;
                                                    tLRPC$TL_peerChat2.chat_id = j13;
                                                } else {
                                                    j13 = j6;
                                                    TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerUser2;
                                                    tLRPC$TL_peerUser2.user_id = j16;
                                                }
                                                tLRPC$TL_message.flags |= 256;
                                                if (optLong3 != 0) {
                                                    TLRPC$TL_peerChat tLRPC$TL_peerChat3 = new TLRPC$TL_peerChat();
                                                    tLRPC$TL_message.from_id = tLRPC$TL_peerChat3;
                                                    tLRPC$TL_peerChat3.chat_id = j13;
                                                } else if (j12 != 0) {
                                                    TLRPC$TL_peerChannel tLRPC$TL_peerChannel2 = new TLRPC$TL_peerChannel();
                                                    tLRPC$TL_message.from_id = tLRPC$TL_peerChannel2;
                                                    tLRPC$TL_peerChannel2.channel_id = j12;
                                                } else if (j10 != 0) {
                                                    TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                                    tLRPC$TL_message.from_id = tLRPC$TL_peerUser3;
                                                    tLRPC$TL_peerUser3.user_id = j10;
                                                } else {
                                                    tLRPC$TL_message.from_id = tLRPC$TL_message.peer_id;
                                                }
                                                if (!z7 && !z11) {
                                                    z15 = false;
                                                    tLRPC$TL_message.mentioned = z15;
                                                    tLRPC$TL_message.silent = z13;
                                                    tLRPC$TL_message.from_scheduled = z24;
                                                    MessageObject messageObject = new MessageObject(i3, tLRPC$TL_message, str16, str10, str13, z14, z9, z10, z12);
                                                    if (i6 != 0) {
                                                        messageObject.messageOwner.reply_to = new TLRPC$TL_messageReplyHeader();
                                                        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = messageObject.messageOwner.reply_to;
                                                        tLRPC$TL_messageReplyHeader.forum_topic = true;
                                                        tLRPC$TL_messageReplyHeader.reply_to_top_id = i6;
                                                    }
                                                    if (!str7.startsWith(str12) && !str7.startsWith(str14)) {
                                                        z16 = false;
                                                        messageObject.isReactionPush = z16;
                                                        ArrayList<MessageObject> arrayList4 = new ArrayList<>();
                                                        arrayList4.add(messageObject);
                                                        NotificationsController.getInstance(i3).processNewMessages(arrayList4, true, true, countDownLatch);
                                                        z3 = false;
                                                        if (z3) {
                                                            countDownLatch.countDown();
                                                        }
                                                        ConnectionsManager.onInternalPushReceived(i3);
                                                        ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                                                    }
                                                    z16 = true;
                                                    messageObject.isReactionPush = z16;
                                                    ArrayList<MessageObject> arrayList42 = new ArrayList<>();
                                                    arrayList42.add(messageObject);
                                                    NotificationsController.getInstance(i3).processNewMessages(arrayList42, true, true, countDownLatch);
                                                    z3 = false;
                                                    if (z3) {
                                                    }
                                                    ConnectionsManager.onInternalPushReceived(i3);
                                                    ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                                                }
                                                z15 = true;
                                                tLRPC$TL_message.mentioned = z15;
                                                tLRPC$TL_message.silent = z13;
                                                tLRPC$TL_message.from_scheduled = z24;
                                                MessageObject messageObject2 = new MessageObject(i3, tLRPC$TL_message, str16, str10, str13, z14, z9, z10, z12);
                                                if (i6 != 0) {
                                                }
                                                if (!str7.startsWith(str12)) {
                                                    z16 = false;
                                                    messageObject2.isReactionPush = z16;
                                                    ArrayList<MessageObject> arrayList422 = new ArrayList<>();
                                                    arrayList422.add(messageObject2);
                                                    NotificationsController.getInstance(i3).processNewMessages(arrayList422, true, true, countDownLatch);
                                                    z3 = false;
                                                    if (z3) {
                                                    }
                                                    ConnectionsManager.onInternalPushReceived(i3);
                                                    ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                                                }
                                                z16 = true;
                                                messageObject2.isReactionPush = z16;
                                                ArrayList<MessageObject> arrayList4222 = new ArrayList<>();
                                                arrayList4222.add(messageObject2);
                                                NotificationsController.getInstance(i3).processNewMessages(arrayList4222, true, true, countDownLatch);
                                                z3 = false;
                                                if (z3) {
                                                }
                                                ConnectionsManager.onInternalPushReceived(i3);
                                                ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                                            }
                                        }
                                        str16 = getReactedText(str7, strArr);
                                        z14 = false;
                                        str15 = null;
                                        if (str16 != null) {
                                        }
                                    }
                                } else if (!str7.startsWith("PINNED_")) {
                                    if (str7.startsWith("CHANNEL_")) {
                                        str9 = null;
                                        z9 = true;
                                        z10 = false;
                                        z11 = false;
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (str7.startsWith("REACT_")) {
                                        }
                                        str16 = getReactedText(str7, strArr);
                                        z14 = false;
                                        str15 = null;
                                        if (str16 != null) {
                                        }
                                    }
                                    str9 = null;
                                    z9 = false;
                                    z10 = false;
                                    z11 = false;
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (str7.startsWith("REACT_")) {
                                    }
                                    str16 = getReactedText(str7, strArr);
                                    z14 = false;
                                    str15 = null;
                                    if (str16 != null) {
                                    }
                                } else {
                                    z10 = j2 != 0;
                                    str9 = null;
                                    z9 = false;
                                    z11 = true;
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (str7.startsWith("REACT_")) {
                                    }
                                    str16 = getReactedText(str7, strArr);
                                    z14 = false;
                                    str15 = null;
                                    if (str16 != null) {
                                    }
                                }
                                th = th18;
                                str3 = str7;
                                i2 = i3;
                                str4 = str6;
                                i = -1;
                                if (i2 == i) {
                                    ConnectionsManager.onInternalPushReceived(i2);
                                    ConnectionsManager.getInstance(i2).resumeNetworkMaybe();
                                    countDownLatch.countDown();
                                } else {
                                    onDecryptError();
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("error in loc_key = " + str3 + " json " + str4);
                                }
                                FileLog.e(th);
                            }
                            z5 = true;
                            if (!jSONObject2.has("mention")) {
                            }
                            if (jSONObject2.has("silent")) {
                            }
                            j8 = optLong2;
                            z6 = false;
                            jSONObject4 = jSONObject3;
                            if (!jSONObject4.has("loc_args")) {
                            }
                            String str232 = strArr[0];
                            boolean has2 = jSONObject2.has("edit_date");
                            if (!str7.startsWith("CHAT_")) {
                            }
                            th = th18;
                            str3 = str7;
                            i2 = i3;
                            str4 = str6;
                            i = -1;
                            if (i2 == i) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            FileLog.e(th);
                        }
                    }
                    z3 = true;
                    if (z3) {
                    }
                    ConnectionsManager.onInternalPushReceived(i3);
                    ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                }
            }
            str6 = str4;
            z3 = true;
            if (z3) {
            }
            ConnectionsManager.onInternalPushReceived(i3);
            ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$4(int i, TLRPC$TL_updates tLRPC$TL_updates) {
        MessagesController.getInstance(i).processUpdates(tLRPC$TL_updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$5(int i) {
        if (UserConfig.getInstance(i).getClientUserId() != 0) {
            UserConfig.getInstance(i).clearConfig();
            MessagesController.getInstance(i).performLogout(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$processRemoteMessage$6(int i) {
        LocationController.getInstance(i).setNewLocationEndWatchTime();
    }

    private static String getReactedText(String str, Object[] objArr) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2114646919:
                if (str.equals("CHAT_REACT_CONTACT")) {
                    c = 0;
                    break;
                }
                break;
            case -1891797827:
                if (str.equals("REACT_GEOLIVE")) {
                    c = 1;
                    break;
                }
                break;
            case -1415696683:
                if (str.equals("CHAT_REACT_NOTEXT")) {
                    c = 2;
                    break;
                }
                break;
            case -1375264434:
                if (str.equals("REACT_NOTEXT")) {
                    c = 3;
                    break;
                }
                break;
            case -1105974394:
                if (str.equals("CHAT_REACT_INVOICE")) {
                    c = 4;
                    break;
                }
                break;
            case -861247200:
                if (str.equals("REACT_CONTACT")) {
                    c = 5;
                    break;
                }
                break;
            case -661458538:
                if (str.equals("CHAT_REACT_STICKER")) {
                    c = 6;
                    break;
                }
                break;
            case 51977938:
                if (str.equals("REACT_GAME")) {
                    c = 7;
                    break;
                }
                break;
            case 52259487:
                if (str.equals("REACT_POLL")) {
                    c = '\b';
                    break;
                }
                break;
            case 52294965:
                if (str.equals("REACT_QUIZ")) {
                    c = '\t';
                    break;
                }
                break;
            case 52369421:
                if (str.equals("REACT_TEXT")) {
                    c = '\n';
                    break;
                }
                break;
            case 147425325:
                if (str.equals("REACT_INVOICE")) {
                    c = 11;
                    break;
                }
                break;
            case 192842257:
                if (str.equals("CHAT_REACT_DOC")) {
                    c = '\f';
                    break;
                }
                break;
            case 192844842:
                if (str.equals("CHAT_REACT_GEO")) {
                    c = '\r';
                    break;
                }
                break;
            case 192844957:
                if (str.equals("CHAT_REACT_GIF")) {
                    c = 14;
                    break;
                }
                break;
            case 591941181:
                if (str.equals("REACT_STICKER")) {
                    c = 15;
                    break;
                }
                break;
            case 635226735:
                if (str.equals("CHAT_REACT_AUDIO")) {
                    c = 16;
                    break;
                }
                break;
            case 648703179:
                if (str.equals("CHAT_REACT_PHOTO")) {
                    c = 17;
                    break;
                }
                break;
            case 650764327:
                if (str.equals("CHAT_REACT_ROUND")) {
                    c = 18;
                    break;
                }
                break;
            case 654263060:
                if (str.equals("CHAT_REACT_VIDEO")) {
                    c = 19;
                    break;
                }
                break;
            case 1149769750:
                if (str.equals("CHAT_REACT_GEOLIVE")) {
                    c = 20;
                    break;
                }
                break;
            case 1606362326:
                if (str.equals("REACT_AUDIO")) {
                    c = 21;
                    break;
                }
                break;
            case 1619838770:
                if (str.equals("REACT_PHOTO")) {
                    c = 22;
                    break;
                }
                break;
            case 1621899918:
                if (str.equals("REACT_ROUND")) {
                    c = 23;
                    break;
                }
                break;
            case 1625398651:
                if (str.equals("REACT_VIDEO")) {
                    c = 24;
                    break;
                }
                break;
            case 1664242232:
                if (str.equals("REACT_DOC")) {
                    c = 25;
                    break;
                }
                break;
            case 1664244817:
                if (str.equals("REACT_GEO")) {
                    c = 26;
                    break;
                }
                break;
            case 1664244932:
                if (str.equals("REACT_GIF")) {
                    c = 27;
                    break;
                }
                break;
            case 1683218969:
                if (str.equals("CHAT_REACT_GAME")) {
                    c = 28;
                    break;
                }
                break;
            case 1683500518:
                if (str.equals("CHAT_REACT_POLL")) {
                    c = 29;
                    break;
                }
                break;
            case 1683535996:
                if (str.equals("CHAT_REACT_QUIZ")) {
                    c = 30;
                    break;
                }
                break;
            case 1683610452:
                if (str.equals("CHAT_REACT_TEXT")) {
                    c = 31;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return LocaleController.formatString("PushChatReactContact", R.string.PushChatReactContact, objArr);
            case 1:
                return LocaleController.formatString("PushReactGeoLocation", R.string.PushReactGeoLocation, objArr);
            case 2:
                return LocaleController.formatString("PushChatReactNotext", R.string.PushChatReactNotext, objArr);
            case 3:
                return LocaleController.formatString("PushReactNoText", R.string.PushReactNoText, objArr);
            case 4:
                return LocaleController.formatString("PushChatReactInvoice", R.string.PushChatReactInvoice, objArr);
            case 5:
                return LocaleController.formatString("PushReactContect", R.string.PushReactContect, objArr);
            case 6:
                return LocaleController.formatString("PushChatReactSticker", R.string.PushChatReactSticker, objArr);
            case 7:
                return LocaleController.formatString("PushReactGame", R.string.PushReactGame, objArr);
            case '\b':
                return LocaleController.formatString("PushReactPoll", R.string.PushReactPoll, objArr);
            case '\t':
                return LocaleController.formatString("PushReactQuiz", R.string.PushReactQuiz, objArr);
            case '\n':
                return LocaleController.formatString("PushReactText", R.string.PushReactText, objArr);
            case 11:
                return LocaleController.formatString("PushReactInvoice", R.string.PushReactInvoice, objArr);
            case '\f':
                return LocaleController.formatString("PushChatReactDoc", R.string.PushChatReactDoc, objArr);
            case '\r':
                return LocaleController.formatString("PushChatReactGeo", R.string.PushChatReactGeo, objArr);
            case 14:
                return LocaleController.formatString("PushChatReactGif", R.string.PushChatReactGif, objArr);
            case 15:
                return LocaleController.formatString("PushReactSticker", R.string.PushReactSticker, objArr);
            case 16:
                return LocaleController.formatString("PushChatReactAudio", R.string.PushChatReactAudio, objArr);
            case 17:
                return LocaleController.formatString("PushChatReactPhoto", R.string.PushChatReactPhoto, objArr);
            case 18:
                return LocaleController.formatString("PushChatReactRound", R.string.PushChatReactRound, objArr);
            case 19:
                return LocaleController.formatString("PushChatReactVideo", R.string.PushChatReactVideo, objArr);
            case 20:
                return LocaleController.formatString("PushChatReactGeoLive", R.string.PushChatReactGeoLive, objArr);
            case 21:
                return LocaleController.formatString("PushReactAudio", R.string.PushReactAudio, objArr);
            case 22:
                return LocaleController.formatString("PushReactPhoto", R.string.PushReactPhoto, objArr);
            case 23:
                return LocaleController.formatString("PushReactRound", R.string.PushReactRound, objArr);
            case 24:
                return LocaleController.formatString("PushReactVideo", R.string.PushReactVideo, objArr);
            case 25:
                return LocaleController.formatString("PushReactDoc", R.string.PushReactDoc, objArr);
            case 26:
                return LocaleController.formatString("PushReactGeo", R.string.PushReactGeo, objArr);
            case 27:
                return LocaleController.formatString("PushReactGif", R.string.PushReactGif, objArr);
            case 28:
                return LocaleController.formatString("PushChatReactGame", R.string.PushChatReactGame, objArr);
            case 29:
                return LocaleController.formatString("PushChatReactPoll", R.string.PushChatReactPoll, objArr);
            case 30:
                return LocaleController.formatString("PushChatReactQuiz", R.string.PushChatReactQuiz, objArr);
            case 31:
                return LocaleController.formatString("PushChatReactText", R.string.PushChatReactText, objArr);
            default:
                return null;
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

    /* loaded from: classes.dex */
    public static final class GooglePushListenerServiceProvider implements IPushListenerServiceProvider {
        public static final GooglePushListenerServiceProvider INSTANCE = new GooglePushListenerServiceProvider();
        private Boolean hasServices;

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public String getLogTitle() {
            return "Google Play Services";
        }

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public int getPushType() {
            return 2;
        }

        private GooglePushListenerServiceProvider() {
        }

        @Override // org.telegram.messenger.PushListenerController.IPushListenerServiceProvider
        public void onRequestPushToken() {
            String str = SharedConfig.pushString;
            if (!TextUtils.isEmpty(str)) {
                if (BuildVars.DEBUG_PRIVATE_VERSION && BuildVars.LOGS_ENABLED) {
                    FileLog.d("FCM regId = " + str);
                }
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.d("FCM Registration not found.");
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.PushListenerController$GooglePushListenerServiceProvider$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PushListenerController.GooglePushListenerServiceProvider.this.lambda$onRequestPushToken$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRequestPushToken$1() {
            try {
                SharedConfig.pushStringGetTimeStart = SystemClock.elapsedRealtime();
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRequestPushToken$0(Task task) {
            SharedConfig.pushStringGetTimeEnd = SystemClock.elapsedRealtime();
            if (!task.isSuccessful()) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("Failed to get regid");
                }
                SharedConfig.pushStringStatus = "__FIREBASE_FAILED__";
                PushListenerController.sendRegistrationToServer(getPushType(), null);
                return;
            }
            String str = (String) task.getResult();
            if (TextUtils.isEmpty(str)) {
                return;
            }
            PushListenerController.sendRegistrationToServer(getPushType(), str);
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
    }
}
