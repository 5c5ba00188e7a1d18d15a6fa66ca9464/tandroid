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
    /* JADX WARN: Code restructure failed: missing block: B:196:0x04db, code lost:
        if (r7 > r12.intValue()) goto L188;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0563 A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0597 A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x05cb A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x05fb A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x062c  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x063d A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:859:0x1e59  */
    /* JADX WARN: Removed duplicated region for block: B:864:0x1e72 A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:897:0x1f3c A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:909:0x1f7b A[Catch: all -> 0x1f8c, TryCatch #5 {all -> 0x1f8c, blocks: (B:161:0x0370, B:164:0x037e, B:165:0x0391, B:167:0x0394, B:168:0x03a0, B:170:0x03be, B:909:0x1f7b, B:910:0x1f80, B:171:0x03e9, B:173:0x03f2, B:174:0x040a, B:176:0x040d, B:177:0x0429, B:179:0x0440, B:180:0x046b, B:182:0x0471, B:184:0x0479, B:186:0x0481, B:188:0x0489, B:191:0x04a4, B:193:0x04b8, B:195:0x04d7, B:204:0x04f6, B:207:0x04fe, B:211:0x0507, B:218:0x052d, B:220:0x0535, B:224:0x0540, B:226:0x0549, B:230:0x0559, B:232:0x0563, B:234:0x0576, B:237:0x0586, B:239:0x0597, B:241:0x059d, B:259:0x05f7, B:261:0x05fb, B:263:0x0635, B:265:0x063d, B:860:0x1e69, B:864:0x1e72, B:868:0x1e83, B:870:0x1e8e, B:872:0x1e97, B:873:0x1e9e, B:875:0x1ea6, B:880:0x1ed3, B:882:0x1edf, B:895:0x1f19, B:897:0x1f3c, B:898:0x1f4e, B:900:0x1f56, B:905:0x1f60, B:885:0x1eef, B:888:0x1f01, B:889:0x1f0d, B:878:0x1eba, B:879:0x1ec6, B:268:0x0656, B:269:0x065a, B:621:0x0be5, B:857:0x1e42, B:623:0x0bf1, B:626:0x0c11, B:629:0x0c36, B:630:0x0c4d, B:633:0x0c6b, B:635:0x0c84, B:636:0x0c9b, B:639:0x0cb9, B:641:0x0cd2, B:642:0x0ce9, B:645:0x0d07, B:647:0x0d20, B:648:0x0d37, B:651:0x0d55, B:653:0x0d6e, B:654:0x0d85, B:657:0x0da3, B:659:0x0dbc, B:660:0x0dd3, B:663:0x0df1, B:665:0x0e0a, B:666:0x0e26, B:669:0x0e49, B:671:0x0e62, B:672:0x0e7e, B:675:0x0ea1, B:677:0x0eba, B:678:0x0ed6, B:681:0x0ef9, B:683:0x0f12, B:684:0x0f29, B:687:0x0f47, B:689:0x0f4b, B:691:0x0f53, B:692:0x0f6a, B:694:0x0f7e, B:696:0x0f82, B:698:0x0f8a, B:699:0x0fa6, B:700:0x0fbd, B:702:0x0fc1, B:704:0x0fc9, B:705:0x0fe0, B:708:0x0ffe, B:710:0x1017, B:711:0x102e, B:714:0x104c, B:716:0x1065, B:717:0x107c, B:720:0x109a, B:722:0x10b3, B:723:0x10ca, B:726:0x10e8, B:728:0x1101, B:729:0x1118, B:732:0x1136, B:734:0x114f, B:735:0x1166, B:738:0x1184, B:740:0x119d, B:741:0x11b9, B:742:0x11d0, B:744:0x11ef, B:745:0x121f, B:746:0x124d, B:747:0x127c, B:748:0x12ab, B:749:0x12de, B:750:0x12fb, B:751:0x1318, B:752:0x1335, B:753:0x1352, B:754:0x136f, B:755:0x138c, B:756:0x13a9, B:757:0x13c6, B:758:0x13e8, B:759:0x1405, B:760:0x1426, B:761:0x1442, B:762:0x145e, B:764:0x147e, B:765:0x14a8, B:766:0x14ce, B:767:0x14f8, B:768:0x151d, B:769:0x1542, B:770:0x1567, B:771:0x1591, B:772:0x15ba, B:773:0x15e3, B:775:0x160c, B:777:0x1616, B:779:0x161e, B:780:0x1654, B:781:0x1685, B:782:0x16aa, B:783:0x16ce, B:784:0x16f2, B:785:0x1716, B:787:0x173c, B:790:0x1764, B:791:0x177d, B:792:0x17a8, B:793:0x17d1, B:794:0x17fa, B:795:0x1823, B:796:0x1853, B:797:0x1873, B:798:0x1893, B:799:0x18b3, B:800:0x18d3, B:801:0x18f8, B:802:0x191d, B:803:0x1942, B:804:0x1962, B:806:0x196c, B:808:0x1974, B:809:0x19a5, B:810:0x19bd, B:811:0x19dd, B:812:0x19fc, B:813:0x1a1b, B:814:0x1a3a, B:817:0x1a5f, B:821:0x1a7a, B:822:0x1aa5, B:823:0x1acd, B:824:0x1af5, B:826:0x1b1f, B:827:0x1b4c, B:828:0x1b71, B:829:0x1b93, B:830:0x1bb8, B:831:0x1bd8, B:832:0x1bf8, B:833:0x1c18, B:834:0x1c3d, B:835:0x1c62, B:836:0x1c87, B:837:0x1ca7, B:839:0x1cb1, B:841:0x1cb9, B:842:0x1cea, B:843:0x1d02, B:844:0x1d22, B:845:0x1d42, B:846:0x1d5c, B:847:0x1d7c, B:848:0x1d9b, B:849:0x1dba, B:850:0x1dd9, B:852:0x1dfa, B:855:0x1e1c, B:271:0x065f, B:274:0x066b, B:277:0x0677, B:280:0x0683, B:283:0x068f, B:286:0x069b, B:289:0x06a7, B:292:0x06b3, B:295:0x06bf, B:298:0x06cb, B:301:0x06d7, B:304:0x06e3, B:307:0x06ef, B:310:0x06fb, B:313:0x0707, B:316:0x0713, B:319:0x071f, B:322:0x072b, B:325:0x0737, B:328:0x0743, B:331:0x0750, B:334:0x075c, B:337:0x0768, B:340:0x0774, B:343:0x0780, B:346:0x078c, B:349:0x0798, B:352:0x07a4, B:355:0x07b0, B:358:0x07bc, B:361:0x07c9, B:364:0x07d5, B:367:0x07e1, B:370:0x07ed, B:373:0x07f9, B:376:0x0805, B:379:0x0811, B:382:0x081d, B:385:0x0829, B:388:0x0835, B:391:0x0841, B:394:0x084d, B:397:0x0859, B:400:0x0865, B:403:0x0871, B:406:0x087d, B:409:0x0889, B:412:0x0895, B:415:0x08a1, B:418:0x08ac, B:421:0x08b8, B:424:0x08c4, B:427:0x08d0, B:430:0x08dc, B:433:0x08e8, B:436:0x08f4, B:439:0x0900, B:442:0x090c, B:445:0x0918, B:448:0x0924, B:451:0x0930, B:454:0x093c, B:457:0x0948, B:460:0x0954, B:463:0x0960, B:466:0x096c, B:469:0x097a, B:472:0x0986, B:475:0x0992, B:478:0x099e, B:481:0x09aa, B:484:0x09b6, B:487:0x09c2, B:490:0x09ce, B:493:0x09da, B:496:0x09e6, B:499:0x09f2, B:502:0x09fe, B:505:0x0a0a, B:508:0x0a16, B:511:0x0a22, B:514:0x0a2e, B:517:0x0a3a, B:520:0x0a46, B:523:0x0a52, B:526:0x0a5e, B:529:0x0a69, B:532:0x0a76, B:535:0x0a82, B:538:0x0a8e, B:541:0x0a9a, B:544:0x0aa6, B:547:0x0ab2, B:550:0x0abe, B:553:0x0aca, B:556:0x0ad6, B:559:0x0ae2, B:562:0x0aed, B:565:0x0af9, B:568:0x0b06, B:571:0x0b12, B:574:0x0b1e, B:577:0x0b2b, B:580:0x0b37, B:583:0x0b43, B:586:0x0b4f, B:589:0x0b5a, B:592:0x0b65, B:595:0x0b70, B:598:0x0b7b, B:601:0x0b86, B:604:0x0b91, B:607:0x0b9c, B:610:0x0ba7, B:613:0x0bb2, B:246:0x05be, B:247:0x05cb, B:254:0x05e4, B:201:0x04eb), top: B:971:0x0370 }] */
    /* JADX WARN: Removed duplicated region for block: B:955:0x2081  */
    /* JADX WARN: Removed duplicated region for block: B:956:0x2091  */
    /* JADX WARN: Removed duplicated region for block: B:959:0x2098  */
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
        long j6;
        int i4;
        boolean z2;
        boolean z3;
        long j7;
        long j8;
        String str8;
        int i5;
        boolean z4;
        long j9;
        boolean z5;
        JSONObject jSONObject4;
        boolean z6;
        boolean z7;
        String[] strArr;
        String str9;
        boolean z8;
        boolean z9;
        boolean z10;
        String str10;
        String str11;
        long j10;
        long j11;
        boolean z11;
        String str12;
        long j12;
        long j13;
        String str13;
        boolean z12;
        int i6;
        String str14;
        String str15;
        boolean z13;
        String str16;
        long j14;
        boolean z14;
        boolean z15;
        char c;
        String str17;
        String string2;
        String formatString;
        String str18;
        boolean z16;
        boolean z17;
        int i7;
        boolean z18;
        int i8;
        String formatString2;
        String string3;
        boolean z19;
        String formatString3;
        String str19;
        String string4;
        boolean z20;
        String str20;
        String string5;
        boolean z21;
        int i9;
        String str21;
        boolean z22;
        String str22;
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
                            j5 = jSONObject2.getLong("chat_id");
                            str7 = string;
                            j6 = -j5;
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
                        j6 = j3;
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
                        FileLog.d("recived push notification chatId " + j5 + " custom topicId " + i4);
                        if (jSONObject2.has("encryption_id")) {
                            j6 = DialogObject.makeEncryptedDialogId(jSONObject2.getInt("encryption_id"));
                        }
                        z2 = jSONObject2.has("schedule") && jSONObject2.getInt("schedule") == 1;
                        if (j6 == 0 && "ENCRYPTED_MESSAGE".equals(str7)) {
                            j6 = NotificationsController.globalSecretChatId;
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
            if (j6 != 0) {
                if ("READ_HISTORY".equals(str7)) {
                    int i13 = jSONObject2.getInt("max_id");
                    ArrayList<TLRPC$Update> arrayList = new ArrayList<>();
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d(str + " received read notification max_id = " + i13 + " for dialogId = " + j6);
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
                    boolean z23 = z2;
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
                        for (String str23 : split2) {
                            arrayList2.add(Utilities.parseInt((CharSequence) str23));
                        }
                        longSparseArray.put(-j2, arrayList2);
                        NotificationsController.getInstance(i3).removeDeletedMessagesFromNotifications(longSparseArray, false);
                        MessagesController.getInstance(i3).deleteMessagesByPush(j6, arrayList2, j2);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d(str + " received " + str7 + " for dialogId = " + j6 + " mids = " + TextUtils.join(",", arrayList2));
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
                        MessagesController.getInstance(i3).checkUnreadReactions(j6, i4, sparseBooleanArray);
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d(str + " received " + str7 + " for dialogId = " + j6 + " mids = " + TextUtils.join(",", arrayList3));
                        }
                    } else if (!TextUtils.isEmpty(str7)) {
                        int i15 = jSONObject2.has("msg_id") ? jSONObject2.getInt("msg_id") : 0;
                        if (jSONObject2.has("random_id")) {
                            long j17 = j5;
                            j8 = Utilities.parseLong(jSONObject2.getString("random_id")).longValue();
                            j7 = j17;
                        } else {
                            j7 = j5;
                            j8 = 0;
                        }
                        if (i15 != 0) {
                            i5 = i4;
                            Integer num = MessagesController.getInstance(i3).dialogs_read_inbox_max.get(Long.valueOf(j6));
                            if (num == null) {
                                num = Integer.valueOf(MessagesStorage.getInstance(i3).getDialogReadMax(false, j6));
                                str8 = "messages";
                                MessagesController.getInstance(i3).dialogs_read_inbox_max.put(Long.valueOf(j6), num);
                            } else {
                                str8 = "messages";
                            }
                        } else {
                            str8 = "messages";
                            i5 = i4;
                            boolean z24 = (j8 == 0 || MessagesStorage.getInstance(i3).checkMessageByRandomId(j8)) ? false : true;
                        }
                        if ((str7.startsWith("REACT_") || str7.startsWith("CHAT_REACT_")) ? true : true) {
                            long j18 = j8;
                            long optLong = jSONObject2.optLong("chat_from_id", 0L);
                            long optLong2 = jSONObject2.optLong("chat_from_broadcast_id", 0L);
                            long optLong3 = jSONObject2.optLong("chat_from_group_id", 0L);
                            if (optLong == 0 && optLong3 == 0) {
                                z4 = false;
                                boolean z25 = (jSONObject2.has("mention") || jSONObject2.getInt("mention") == 0) ? false : true;
                                if (jSONObject2.has("silent") || jSONObject2.getInt("silent") == 0) {
                                    j9 = optLong2;
                                    z5 = false;
                                } else {
                                    j9 = optLong2;
                                    z5 = true;
                                }
                                jSONObject4 = jSONObject3;
                                if (jSONObject4.has("loc_args")) {
                                    z6 = z25;
                                    z7 = z5;
                                    strArr = null;
                                } else {
                                    JSONArray jSONArray = jSONObject4.getJSONArray("loc_args");
                                    int length = jSONArray.length();
                                    z7 = z5;
                                    String[] strArr2 = new String[length];
                                    z6 = z25;
                                    for (int i16 = 0; i16 < length; i16++) {
                                        strArr2[i16] = jSONArray.getString(i16);
                                    }
                                    strArr = strArr2;
                                }
                                String str24 = strArr[0];
                                boolean has = jSONObject2.has("edit_date");
                                if (!str7.startsWith("CHAT_")) {
                                    if (!UserObject.isReplyUser(j6)) {
                                        z9 = j2 != 0;
                                        z10 = false;
                                        str9 = str24;
                                        str24 = strArr[1];
                                        z8 = false;
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (str7.startsWith("REACT_")) {
                                        }
                                        str16 = getReactedText(str7, strArr);
                                        z13 = false;
                                        str15 = null;
                                        if (str16 != null) {
                                        }
                                    } else {
                                        str24 = str24 + " @ " + strArr[1];
                                        str9 = null;
                                        z8 = false;
                                        z9 = false;
                                        z10 = false;
                                        if (BuildVars.LOGS_ENABLED) {
                                            StringBuilder sb = new StringBuilder();
                                            str10 = str24;
                                            str11 = str9;
                                            j10 = j9;
                                            j11 = optLong;
                                            sb.append(str);
                                            sb.append(" received message notification ");
                                            sb.append(str7);
                                            sb.append(" for dialogId = ");
                                            sb.append(j6);
                                            sb.append(" mid = ");
                                            sb.append(i15);
                                            FileLog.d(sb.toString());
                                        } else {
                                            str10 = str24;
                                            str11 = str9;
                                            j10 = j9;
                                            j11 = optLong;
                                        }
                                        if (str7.startsWith("REACT_")) {
                                            z11 = has;
                                            str12 = "REACT_";
                                            j12 = j2;
                                            j13 = j10;
                                            str13 = str11;
                                            z12 = z7;
                                            i6 = i5;
                                            str14 = "CHAT_REACT_";
                                        } else if (str7.startsWith("CHAT_REACT_")) {
                                            z11 = has;
                                            str12 = "REACT_";
                                            j12 = j2;
                                            str14 = "CHAT_REACT_";
                                            j13 = j10;
                                            str13 = str11;
                                            z12 = z7;
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
                                            z11 = has;
                                            str13 = str11;
                                            j13 = j10;
                                            j12 = j2;
                                            switch (c) {
                                                case 0:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageRecurringPay", R.string.NotificationMessageRecurringPay, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("PaymentInvoice", R.string.PaymentInvoice);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 1:
                                                case 2:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    formatString = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, strArr[0], strArr[1]);
                                                    str18 = strArr[1];
                                                    str15 = str18;
                                                    str16 = formatString;
                                                    z13 = false;
                                                    break;
                                                case 3:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, strArr[0]);
                                                    string2 = LocaleController.getString("Message", R.string.Message);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 4:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, strArr[0]);
                                                    string2 = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 5:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, strArr[0]);
                                                    string2 = LocaleController.getString("AttachDestructingPhoto", R.string.AttachDestructingPhoto);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 6:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, strArr[0]);
                                                    string2 = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 7:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, strArr[0]);
                                                    string2 = LocaleController.getString("AttachDestructingVideo", R.string.AttachDestructingVideo);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case '\b':
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.getString("ActionTakeScreenshoot", R.string.ActionTakeScreenshoot).replace("un1", strArr[0]);
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case '\t':
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, strArr[0]);
                                                    string2 = LocaleController.getString("AttachRound", R.string.AttachRound);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case '\n':
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, strArr[0]);
                                                    string2 = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 11:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                        formatString = LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, strArr[0], strArr[1]);
                                                        str18 = strArr[1] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                        str15 = str18;
                                                        str16 = formatString;
                                                        z13 = false;
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, strArr[0]);
                                                        string2 = LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                        str15 = string2;
                                                        z13 = false;
                                                        break;
                                                    }
                                                    break;
                                                case '\f':
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, strArr[0]);
                                                    string2 = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case '\r':
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageContact2", R.string.NotificationMessageContact2, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("AttachContact", R.string.AttachContact);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 14:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageQuiz2", R.string.NotificationMessageQuiz2, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("QuizPoll", R.string.QuizPoll);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 15:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessagePoll2", R.string.NotificationMessagePoll2, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("Poll", R.string.Poll);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 16:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, strArr[0]);
                                                    string2 = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 17:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, strArr[0]);
                                                    string2 = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 18:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, strArr[0]);
                                                    string2 = LocaleController.getString("AttachGif", R.string.AttachGif);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 19:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("AttachGame", R.string.AttachGame);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 20:
                                                case 21:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGameScored", R.string.NotificationMessageGameScored, strArr[0], strArr[1], strArr[2]);
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 22:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageInvoice", R.string.NotificationMessageInvoice, strArr[0], strArr[1]);
                                                    string2 = LocaleController.getString("PaymentInvoice", R.string.PaymentInvoice);
                                                    str15 = string2;
                                                    z13 = false;
                                                    break;
                                                case 23:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageForwardFew", R.string.NotificationMessageForwardFew, strArr[0], LocaleController.formatPluralString(str8, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 24:
                                                    z16 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z16;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 25:
                                                    z16 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z16;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 26:
                                                    z16 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z16;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 27:
                                                    z16 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageFew", R.string.NotificationMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z16;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 28:
                                                    z17 = z7;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageAlbum", R.string.NotificationMessageAlbum, strArr[0]);
                                                    z12 = z17;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 29:
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, strArr[0]);
                                                    string3 = LocaleController.getString("Message", R.string.Message);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case 30:
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessagePhoto", R.string.ChannelMessagePhoto, strArr[0]);
                                                    string3 = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case 31:
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageVideo", R.string.ChannelMessageVideo, strArr[0]);
                                                    string3 = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case ' ':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageRound", R.string.ChannelMessageRound, strArr[0]);
                                                    string3 = LocaleController.getString("AttachRound", R.string.AttachRound);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '!':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageDocument", R.string.ChannelMessageDocument, strArr[0]);
                                                    string3 = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '\"':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                        formatString2 = LocaleController.formatString("ChannelMessageStickerEmoji", R.string.ChannelMessageStickerEmoji, strArr[0], strArr[1]);
                                                        string3 = strArr[1] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    } else {
                                                        formatString2 = LocaleController.formatString("ChannelMessageSticker", R.string.ChannelMessageSticker, strArr[0]);
                                                        string3 = LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    }
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '#':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageAudio", R.string.ChannelMessageAudio, strArr[0]);
                                                    string3 = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '$':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageContact2", R.string.ChannelMessageContact2, strArr[0], strArr[1]);
                                                    string3 = LocaleController.getString("AttachContact", R.string.AttachContact);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '%':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageQuiz2", R.string.ChannelMessageQuiz2, strArr[0], strArr[1]);
                                                    string3 = LocaleController.getString("QuizPoll", R.string.QuizPoll);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '&':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessagePoll2", R.string.ChannelMessagePoll2, strArr[0], strArr[1]);
                                                    string3 = LocaleController.getString("Poll", R.string.Poll);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '\'':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageMap", R.string.ChannelMessageMap, strArr[0]);
                                                    string3 = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '(':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageLiveLocation", R.string.ChannelMessageLiveLocation, strArr[0]);
                                                    string3 = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case ')':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("ChannelMessageGIF", R.string.ChannelMessageGIF, strArr[0]);
                                                    string3 = LocaleController.getString("AttachGif", R.string.AttachGif);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '*':
                                                    z18 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString2 = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, strArr[0]);
                                                    string3 = LocaleController.getString("AttachGame", R.string.AttachGame);
                                                    str15 = string3;
                                                    str16 = formatString2;
                                                    z12 = z18;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '+':
                                                    z17 = z7;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("ForwardedMessageCount", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]).toLowerCase());
                                                    z12 = z17;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case ',':
                                                    z17 = z7;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z17;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case '-':
                                                    z17 = z7;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z17;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case '.':
                                                    z17 = z7;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z17;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case '/':
                                                    z17 = z7;
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageFew", R.string.ChannelMessageFew, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                    z12 = z17;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case '0':
                                                    i7 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("ChannelMessageAlbum", R.string.ChannelMessageAlbum, strArr[0]);
                                                    z12 = z7;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case '1':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    formatString3 = LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, strArr[0], strArr[1], strArr[2]);
                                                    str19 = strArr[2];
                                                    str15 = str19;
                                                    str16 = formatString3;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '2':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("Message", R.string.Message);
                                                    str15 = string4;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '3':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupPhoto", R.string.NotificationMessageGroupPhoto, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachPhoto", R.string.AttachPhoto);
                                                    str15 = string4;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '4':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupVideo", R.string.NotificationMessageGroupVideo, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachVideo", R.string.AttachVideo);
                                                    str15 = string4;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '5':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupRound", R.string.NotificationMessageGroupRound, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachRound", R.string.AttachRound);
                                                    str15 = string4;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '6':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupDocument", R.string.NotificationMessageGroupDocument, strArr[0], strArr[1]);
                                                    string4 = LocaleController.getString("AttachDocument", R.string.AttachDocument);
                                                    str15 = string4;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '7':
                                                    z19 = z7;
                                                    i8 = i5;
                                                    str14 = str17;
                                                    if (strArr.length > 2 && !TextUtils.isEmpty(strArr[2])) {
                                                        formatString3 = LocaleController.formatString("NotificationMessageGroupStickerEmoji", R.string.NotificationMessageGroupStickerEmoji, strArr[0], strArr[1], strArr[2]);
                                                        str19 = strArr[2] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    } else {
                                                        formatString3 = LocaleController.formatString("NotificationMessageGroupSticker", R.string.NotificationMessageGroupSticker, strArr[0], strArr[1]);
                                                        str19 = strArr[1] + " " + LocaleController.getString("AttachSticker", R.string.AttachSticker);
                                                    }
                                                    str15 = str19;
                                                    str16 = formatString3;
                                                    z12 = z19;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '8':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupAudio", R.string.NotificationMessageGroupAudio, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachAudio", R.string.AttachAudio);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '9':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupContact2", R.string.NotificationMessageGroupContact2, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("AttachContact", R.string.AttachContact);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case ':':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupQuiz2", R.string.NotificationMessageGroupQuiz2, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("PollQuiz", R.string.PollQuiz);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case ';':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupPoll2", R.string.NotificationMessageGroupPoll2, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("Poll", R.string.Poll);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '<':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachLocation", R.string.AttachLocation);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '=':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupLiveLocation", R.string.NotificationMessageGroupLiveLocation, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '>':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupGif", R.string.NotificationMessageGroupGif, strArr[0], strArr[1]);
                                                    string5 = LocaleController.getString("AttachGif", R.string.AttachGif);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '?':
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupGame", R.string.NotificationMessageGroupGame, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("AttachGame", R.string.AttachGame);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case '@':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupGameScored", R.string.NotificationMessageGroupGameScored, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case VoIPService.CALL_MIN_LAYER /* 65 */:
                                                    z20 = z7;
                                                    i8 = i5;
                                                    str20 = str17;
                                                    str16 = LocaleController.formatString("NotificationMessageGroupInvoice", R.string.NotificationMessageGroupInvoice, strArr[0], strArr[1], strArr[2]);
                                                    string5 = LocaleController.getString("PaymentInvoice", R.string.PaymentInvoice);
                                                    str15 = string5;
                                                    z12 = z20;
                                                    str14 = str20;
                                                    i6 = i8;
                                                    z13 = false;
                                                    break;
                                                case 'B':
                                                case 'C':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationInvitedToGroup", R.string.NotificationInvitedToGroup, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'D':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationEditedGroupName", R.string.NotificationEditedGroupName, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'E':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationEditedGroupPhoto", R.string.NotificationEditedGroupPhoto, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'F':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, strArr[0], strArr[1], strArr[2]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'G':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupCreatedCall", R.string.NotificationGroupCreatedCall, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'H':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupInvitedToCall", R.string.NotificationGroupInvitedToCall, strArr[0], strArr[1], strArr[2]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'I':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupEndedCall", R.string.NotificationGroupEndedCall, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'J':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupInvitedYouToCall", R.string.NotificationGroupInvitedYouToCall, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'K':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupKickMember", R.string.NotificationGroupKickMember, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'L':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupKickYou", R.string.NotificationGroupKickYou, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'M':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupLeftMember", R.string.NotificationGroupLeftMember, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'N':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAddSelf", R.string.NotificationGroupAddSelf, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'O':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAddSelfMega", R.string.NotificationGroupAddSelfMega, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'P':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    str16 = LocaleController.formatString("UserAcceptedToGroupPushWithGroup", R.string.UserAcceptedToGroupPushWithGroup, strArr[0], strArr[1]);
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'Q':
                                                    z22 = z7;
                                                    i7 = i5;
                                                    str22 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupForwardedFew", R.string.NotificationGroupForwardedFew, strArr[0], strArr[1], LocaleController.formatPluralString(str8, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z12 = z22;
                                                    str14 = str22;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 'R':
                                                    z22 = z7;
                                                    i7 = i5;
                                                    str22 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z12 = z22;
                                                    str14 = str22;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 'S':
                                                    z22 = z7;
                                                    i7 = i5;
                                                    str22 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z12 = z22;
                                                    str14 = str22;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 'T':
                                                    z22 = z7;
                                                    i7 = i5;
                                                    str22 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z12 = z22;
                                                    str14 = str22;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 'U':
                                                    z22 = z7;
                                                    i7 = i5;
                                                    str22 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupFew", R.string.NotificationGroupFew, strArr[0], strArr[1], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                    z12 = z22;
                                                    str14 = str22;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 'V':
                                                    z22 = z7;
                                                    i7 = i5;
                                                    str22 = str17;
                                                    str16 = LocaleController.formatString("NotificationGroupAlbum", R.string.NotificationGroupAlbum, strArr[0], strArr[1]);
                                                    z12 = z22;
                                                    str14 = str22;
                                                    i6 = i7;
                                                    z13 = true;
                                                    str15 = null;
                                                    break;
                                                case 'W':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedTextUser", R.string.NotificationActionPinnedTextUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, strArr[0], strArr[1], strArr[2]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, strArr[0], strArr[1]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'X':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedNoTextUser", R.string.NotificationActionPinnedNoTextUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'Y':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPhotoUser", R.string.NotificationActionPinnedPhotoUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPhoto", R.string.NotificationActionPinnedPhoto, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPhotoChannel", R.string.NotificationActionPinnedPhotoChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'Z':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVideoUser", R.string.NotificationActionPinnedVideoUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVideo", R.string.NotificationActionPinnedVideo, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVideoChannel", R.string.NotificationActionPinnedVideoChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case '[':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedRoundUser", R.string.NotificationActionPinnedRoundUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedRound", R.string.NotificationActionPinnedRound, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedRoundChannel", R.string.NotificationActionPinnedRoundChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case '\\':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedFileUser", R.string.NotificationActionPinnedFileUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedFile", R.string.NotificationActionPinnedFile, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedFileChannel", R.string.NotificationActionPinnedFileChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case ']':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", R.string.NotificationActionPinnedStickerEmojiUser, strArr[0], strArr[1]);
                                                        } else {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedStickerUser", R.string.NotificationActionPinnedStickerUser, strArr[0]);
                                                        }
                                                    } else if (z4) {
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
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case '^':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVoiceUser", R.string.NotificationActionPinnedVoiceUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVoice", R.string.NotificationActionPinnedVoice, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedVoiceChannel", R.string.NotificationActionPinnedVoiceChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case '_':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedContactUser", R.string.NotificationActionPinnedContactUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedContact2", R.string.NotificationActionPinnedContact2, strArr[0], strArr[2], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedContactChannel2", R.string.NotificationActionPinnedContactChannel2, strArr[0], strArr[1]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case '`':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedQuizUser", R.string.NotificationActionPinnedQuizUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedQuiz2", R.string.NotificationActionPinnedQuiz2, strArr[0], strArr[2], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedQuizChannel2", R.string.NotificationActionPinnedQuizChannel2, strArr[0], strArr[1]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'a':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPollUser", R.string.NotificationActionPinnedPollUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPoll2", R.string.NotificationActionPinnedPoll2, strArr[0], strArr[2], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedPollChannel2", R.string.NotificationActionPinnedPollChannel2, strArr[0], strArr[1]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'b':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoUser", R.string.NotificationActionPinnedGeoUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeo", R.string.NotificationActionPinnedGeo, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoChannel", R.string.NotificationActionPinnedGeoChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'c':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoLiveUser", R.string.NotificationActionPinnedGeoLiveUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoLive", R.string.NotificationActionPinnedGeoLive, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", R.string.NotificationActionPinnedGeoLiveChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case FileLoader.MEDIA_DIR_IMAGE_PUBLIC /* 100 */:
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameUser", R.string.NotificationActionPinnedGameUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGame", R.string.NotificationActionPinnedGame, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameChannel", R.string.NotificationActionPinnedGameChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case FileLoader.MEDIA_DIR_VIDEO_PUBLIC /* 101 */:
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameScoreUser", R.string.NotificationActionPinnedGameScoreUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameScore", R.string.NotificationActionPinnedGameScore, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGameScoreChannel", R.string.NotificationActionPinnedGameScoreChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'f':
                                                    z21 = z7;
                                                    i9 = i5;
                                                    str21 = str17;
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedInvoiceUser", R.string.NotificationActionPinnedInvoiceUser, strArr[0], strArr[1]);
                                                    } else if (z4) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedInvoice", R.string.NotificationActionPinnedInvoice, strArr[0], strArr[1]);
                                                    } else {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedInvoiceChannel", R.string.NotificationActionPinnedInvoiceChannel, strArr[0]);
                                                    }
                                                    z12 = z21;
                                                    str14 = str21;
                                                    i6 = i9;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                case 'g':
                                                    if (j6 > 0) {
                                                        str16 = LocaleController.formatString("NotificationActionPinnedGifUser", R.string.NotificationActionPinnedGifUser, strArr[0], strArr[1]);
                                                        z12 = z7;
                                                        i6 = i5;
                                                        str14 = str17;
                                                        z13 = false;
                                                        str15 = null;
                                                        break;
                                                    } else {
                                                        z21 = z7;
                                                        i9 = i5;
                                                        str21 = str17;
                                                        if (z4) {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedGif", R.string.NotificationActionPinnedGif, strArr[0], strArr[1]);
                                                        } else {
                                                            str16 = LocaleController.formatString("NotificationActionPinnedGifChannel", R.string.NotificationActionPinnedGifChannel, strArr[0]);
                                                        }
                                                        z12 = z21;
                                                        str14 = str21;
                                                        i6 = i9;
                                                        z13 = false;
                                                        str15 = null;
                                                    }
                                                case 'h':
                                                    str16 = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
                                                    str10 = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    z13 = true;
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
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    str16 = null;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                                default:
                                                    z12 = z7;
                                                    i6 = i5;
                                                    str14 = str17;
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.w("unhandled loc_key = " + str7);
                                                    }
                                                    str16 = null;
                                                    z13 = false;
                                                    str15 = null;
                                                    break;
                                            }
                                            if (str16 != null) {
                                                TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                                                tLRPC$TL_message.id = i15;
                                                tLRPC$TL_message.random_id = j18;
                                                tLRPC$TL_message.message = str15 != null ? str15 : str16;
                                                tLRPC$TL_message.date = (int) (j / 1000);
                                                if (z10) {
                                                    tLRPC$TL_message.action = new TLRPC$TL_messageActionPinMessage();
                                                }
                                                if (z9) {
                                                    tLRPC$TL_message.flags |= Integer.MIN_VALUE;
                                                }
                                                tLRPC$TL_message.dialog_id = j6;
                                                if (j12 != 0) {
                                                    TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                                    tLRPC$TL_peerChannel.channel_id = j12;
                                                    j14 = j7;
                                                } else if (j7 != 0) {
                                                    TLRPC$TL_peerChat tLRPC$TL_peerChat2 = new TLRPC$TL_peerChat();
                                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerChat2;
                                                    j14 = j7;
                                                    tLRPC$TL_peerChat2.chat_id = j14;
                                                } else {
                                                    j14 = j7;
                                                    TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                                    tLRPC$TL_message.peer_id = tLRPC$TL_peerUser2;
                                                    tLRPC$TL_peerUser2.user_id = j16;
                                                }
                                                tLRPC$TL_message.flags |= LiteMode.FLAG_CHAT_BLUR;
                                                if (optLong3 != 0) {
                                                    TLRPC$TL_peerChat tLRPC$TL_peerChat3 = new TLRPC$TL_peerChat();
                                                    tLRPC$TL_message.from_id = tLRPC$TL_peerChat3;
                                                    tLRPC$TL_peerChat3.chat_id = j14;
                                                } else if (j13 != 0) {
                                                    TLRPC$TL_peerChannel tLRPC$TL_peerChannel2 = new TLRPC$TL_peerChannel();
                                                    tLRPC$TL_message.from_id = tLRPC$TL_peerChannel2;
                                                    tLRPC$TL_peerChannel2.channel_id = j13;
                                                } else if (j11 != 0) {
                                                    TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                                    tLRPC$TL_message.from_id = tLRPC$TL_peerUser3;
                                                    tLRPC$TL_peerUser3.user_id = j11;
                                                } else {
                                                    tLRPC$TL_message.from_id = tLRPC$TL_message.peer_id;
                                                }
                                                if (!z6 && !z10) {
                                                    z14 = false;
                                                    tLRPC$TL_message.mentioned = z14;
                                                    tLRPC$TL_message.silent = z12;
                                                    tLRPC$TL_message.from_scheduled = z23;
                                                    MessageObject messageObject = new MessageObject(i3, tLRPC$TL_message, str16, str10, str13, z13, z8, z9, z11);
                                                    if (i6 != 0) {
                                                        messageObject.messageOwner.reply_to = new TLRPC$TL_messageReplyHeader();
                                                        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = messageObject.messageOwner.reply_to;
                                                        tLRPC$TL_messageReplyHeader.forum_topic = true;
                                                        tLRPC$TL_messageReplyHeader.reply_to_top_id = i6;
                                                    }
                                                    if (!str7.startsWith(str12) && !str7.startsWith(str14)) {
                                                        z15 = false;
                                                        messageObject.isReactionPush = z15;
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
                                                    z15 = true;
                                                    messageObject.isReactionPush = z15;
                                                    ArrayList<MessageObject> arrayList42 = new ArrayList<>();
                                                    arrayList42.add(messageObject);
                                                    NotificationsController.getInstance(i3).processNewMessages(arrayList42, true, true, countDownLatch);
                                                    z3 = false;
                                                    if (z3) {
                                                    }
                                                    ConnectionsManager.onInternalPushReceived(i3);
                                                    ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                                                }
                                                z14 = true;
                                                tLRPC$TL_message.mentioned = z14;
                                                tLRPC$TL_message.silent = z12;
                                                tLRPC$TL_message.from_scheduled = z23;
                                                MessageObject messageObject2 = new MessageObject(i3, tLRPC$TL_message, str16, str10, str13, z13, z8, z9, z11);
                                                if (i6 != 0) {
                                                }
                                                if (!str7.startsWith(str12)) {
                                                    z15 = false;
                                                    messageObject2.isReactionPush = z15;
                                                    ArrayList<MessageObject> arrayList422 = new ArrayList<>();
                                                    arrayList422.add(messageObject2);
                                                    NotificationsController.getInstance(i3).processNewMessages(arrayList422, true, true, countDownLatch);
                                                    z3 = false;
                                                    if (z3) {
                                                    }
                                                    ConnectionsManager.onInternalPushReceived(i3);
                                                    ConnectionsManager.getInstance(i3).resumeNetworkMaybe();
                                                }
                                                z15 = true;
                                                messageObject2.isReactionPush = z15;
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
                                        z13 = false;
                                        str15 = null;
                                        if (str16 != null) {
                                        }
                                    }
                                } else if (!str7.startsWith("PINNED_")) {
                                    if (str7.startsWith("CHANNEL_")) {
                                        str9 = null;
                                        z8 = true;
                                        z9 = false;
                                        z10 = false;
                                        if (BuildVars.LOGS_ENABLED) {
                                        }
                                        if (str7.startsWith("REACT_")) {
                                        }
                                        str16 = getReactedText(str7, strArr);
                                        z13 = false;
                                        str15 = null;
                                        if (str16 != null) {
                                        }
                                    }
                                    str9 = null;
                                    z8 = false;
                                    z9 = false;
                                    z10 = false;
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (str7.startsWith("REACT_")) {
                                    }
                                    str16 = getReactedText(str7, strArr);
                                    z13 = false;
                                    str15 = null;
                                    if (str16 != null) {
                                    }
                                } else {
                                    z9 = j2 != 0;
                                    str9 = null;
                                    z8 = false;
                                    z10 = true;
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    if (str7.startsWith("REACT_")) {
                                    }
                                    str16 = getReactedText(str7, strArr);
                                    z13 = false;
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
                            z4 = true;
                            if (jSONObject2.has("mention")) {
                            }
                            if (jSONObject2.has("silent")) {
                            }
                            j9 = optLong2;
                            z5 = false;
                            jSONObject4 = jSONObject3;
                            if (jSONObject4.has("loc_args")) {
                            }
                            String str242 = strArr[0];
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
