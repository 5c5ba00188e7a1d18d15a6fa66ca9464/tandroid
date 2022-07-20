package org.telegram.messenger;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import androidx.collection.LongSparseArray;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.json.JSONArray;
import org.json.JSONObject;
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
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_updateReadChannelInbox;
import org.telegram.tgnet.TLRPC$TL_updateReadHistoryInbox;
import org.telegram.tgnet.TLRPC$TL_updateServiceNotification;
import org.telegram.tgnet.TLRPC$TL_updates;
import org.telegram.tgnet.TLRPC$Update;
/* loaded from: classes.dex */
public class GcmPushListenerService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 1;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        long sentTime = remoteMessage.getSentTime();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("GCM received data: " + data + " from: " + from);
        }
        AndroidUtilities.runOnUIThread(new GcmPushListenerService$$ExternalSyntheticLambda6(this, data, sentTime));
        try {
            this.countDownLatch.await();
        } catch (Throwable unused) {
        }
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("finished GCM service, time = " + (SystemClock.elapsedRealtime() - elapsedRealtime));
        }
    }

    public /* synthetic */ void lambda$onMessageReceived$4(Map map, long j) {
        ApplicationLoader.postInitApplication();
        Utilities.stageQueue.postRunnable(new GcmPushListenerService$$ExternalSyntheticLambda7(this, map, j));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x03ef, code lost:
        if (r11 > r1.intValue()) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x03f1, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x040e, code lost:
        if (org.telegram.messenger.MessagesStorage.getInstance(r12).checkMessageByRandomId(r4) == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x041d, code lost:
        if (r2.startsWith("CHAT_REACT_") != false) goto L194;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:215:0x046b A[Catch: all -> 0x045b, TRY_ENTER, TRY_LEAVE, TryCatch #19 {all -> 0x045b, blocks: (B:207:0x0451, B:215:0x046b), top: B:986:0x0451 }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x04ad  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x04c3 A[Catch: all -> 0x04a2, TRY_ENTER, TryCatch #2 {all -> 0x04a2, blocks: (B:221:0x0486, B:223:0x0499, B:231:0x04c3, B:233:0x04c9, B:238:0x04ea, B:256:0x0521, B:260:0x0554, B:263:0x0566, B:264:0x056a, B:266:0x056f, B:269:0x057b, B:272:0x0587, B:275:0x0593, B:278:0x059f, B:281:0x05ab, B:284:0x05b7, B:287:0x05c3, B:290:0x05cf, B:293:0x05db, B:296:0x05e7, B:299:0x05f3, B:302:0x05ff, B:305:0x060b, B:308:0x0617, B:311:0x0623, B:314:0x062f, B:317:0x063b, B:320:0x0647, B:323:0x0653, B:326:0x065e, B:329:0x066a, B:332:0x0676, B:335:0x0682, B:338:0x068e, B:341:0x069a, B:344:0x06a6, B:347:0x06b2, B:350:0x06be, B:353:0x06ca, B:356:0x06d5, B:359:0x06e1, B:362:0x06ed, B:365:0x06f9, B:368:0x0705, B:371:0x0711, B:374:0x071d, B:377:0x0729, B:380:0x0735, B:383:0x0741, B:386:0x074d, B:389:0x0759, B:392:0x0765, B:395:0x0771, B:398:0x077d, B:401:0x0789, B:404:0x0795, B:407:0x07a1, B:410:0x07ad, B:413:0x07b8, B:416:0x07c4, B:419:0x07d0, B:422:0x07dc, B:425:0x07e8, B:428:0x07f4, B:431:0x0800, B:434:0x080c, B:437:0x0818, B:440:0x0824, B:443:0x0830, B:446:0x083c, B:449:0x0848, B:452:0x0854, B:455:0x0860, B:458:0x086c, B:461:0x0878, B:464:0x0884, B:467:0x0890, B:470:0x089c, B:473:0x08a8, B:476:0x08b4, B:479:0x08c0, B:482:0x08cc, B:485:0x08d8, B:488:0x08e4, B:491:0x08f0, B:494:0x08fc, B:497:0x0908, B:500:0x0914, B:503:0x0920, B:506:0x092c, B:509:0x0938, B:512:0x0944, B:515:0x0950, B:518:0x095c, B:521:0x0968, B:524:0x0973, B:527:0x097e, B:530:0x098a, B:533:0x0996, B:536:0x09a2, B:539:0x09ae, B:542:0x09ba, B:545:0x09c6, B:548:0x09d2, B:551:0x09de, B:554:0x09ea, B:557:0x09f5, B:560:0x0a01, B:563:0x0a0c, B:566:0x0a18, B:569:0x0a24, B:572:0x0a2f, B:575:0x0a3b, B:578:0x0a47, B:581:0x0a53, B:584:0x0a5f, B:587:0x0a6a, B:590:0x0a75, B:593:0x0a80, B:596:0x0a8b, B:599:0x0a96, B:602:0x0aa1, B:605:0x0aac, B:608:0x0ab7, B:614:0x0ad8, B:615:0x0adc, B:619:0x0afc, B:621:0x0b16, B:622:0x0b2e, B:625:0x0b47, B:627:0x0b61, B:628:0x0b79, B:631:0x0b92, B:633:0x0bac, B:634:0x0bc4, B:637:0x0bdd, B:639:0x0bf7, B:640:0x0c0f, B:643:0x0c28, B:645:0x0c42, B:646:0x0c5a, B:649:0x0c73, B:651:0x0c8d, B:652:0x0ca5, B:655:0x0cbe, B:657:0x0cd8, B:658:0x0cf5, B:661:0x0d14, B:663:0x0d2e, B:664:0x0d4b, B:667:0x0d6a, B:669:0x0d84, B:670:0x0da1, B:673:0x0dc0, B:675:0x0dda, B:676:0x0df2, B:679:0x0e0c, B:681:0x0e10, B:683:0x0e18, B:684:0x0e30, B:686:0x0e45, B:688:0x0e49, B:690:0x0e51, B:691:0x0e6e, B:692:0x0e86, B:694:0x0e8a, B:696:0x0e92, B:697:0x0eaa, B:700:0x0ec4, B:702:0x0ede, B:703:0x0ef6, B:706:0x0f10, B:708:0x0f2a, B:709:0x0f42, B:712:0x0f5c, B:714:0x0f76, B:715:0x0f8e, B:718:0x0fa8, B:720:0x0fc2, B:721:0x0fda, B:724:0x0ff4, B:726:0x100e, B:727:0x1026, B:730:0x1040, B:732:0x105a, B:733:0x1077, B:734:0x108f, B:736:0x10ab, B:737:0x10d7, B:738:0x1103, B:739:0x1130, B:740:0x115d, B:741:0x118c, B:742:0x11a5, B:743:0x11be, B:744:0x11d7, B:745:0x11f0, B:746:0x1209, B:747:0x1222, B:748:0x123b, B:749:0x1254, B:750:0x1272, B:751:0x128a, B:752:0x12a7, B:753:0x12bf, B:754:0x12d7, B:756:0x12f2, B:757:0x1319, B:758:0x133b, B:759:0x1362, B:760:0x1384, B:761:0x13a6, B:762:0x13c8, B:763:0x13ee, B:764:0x1414, B:765:0x143a, B:767:0x145f, B:769:0x1463, B:771:0x146b, B:772:0x14a3, B:773:0x14d6, B:774:0x14f7, B:775:0x1518, B:776:0x1539, B:777:0x155a, B:778:0x157b, B:779:0x159a, B:780:0x15ad, B:781:0x15d3, B:782:0x15f9, B:783:0x161f, B:784:0x1645, B:785:0x1670, B:786:0x168c, B:787:0x16a8, B:788:0x16c4, B:789:0x16e0, B:790:0x1701, B:791:0x1722, B:792:0x1743, B:793:0x175f, B:795:0x1763, B:797:0x176b, B:798:0x179e, B:799:0x17b8, B:800:0x17d4, B:801:0x17f0, B:802:0x180c, B:803:0x1828, B:804:0x1844, B:806:0x1858, B:807:0x187d, B:808:0x18a2, B:809:0x18c7, B:810:0x18ed, B:811:0x1915, B:812:0x1936, B:813:0x1953, B:814:0x1974, B:815:0x1990, B:816:0x19ac, B:817:0x19c8, B:818:0x19e9, B:819:0x1a0a, B:820:0x1a2b, B:821:0x1a47, B:823:0x1a4b, B:825:0x1a53, B:826:0x1a86, B:827:0x1aa0, B:828:0x1abc, B:829:0x1ad8, B:832:0x1aef, B:833:0x1b0a, B:834:0x1b25, B:835:0x1b40, B:836:0x1b5b, B:839:0x1b7a, B:840:0x1b93, B:842:0x1bb5), top: B:955:0x0486 }] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x04f3 A[Catch: all -> 0x1cd5, TRY_ENTER, TryCatch #14 {all -> 0x1cd5, blocks: (B:219:0x047a, B:229:0x04b3, B:240:0x04f3, B:248:0x050a, B:253:0x051b, B:258:0x054e), top: B:976:0x047a }] */
    /* JADX WARN: Removed duplicated region for block: B:255:0x051f  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0548  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x0554 A[Catch: all -> 0x04a2, TRY_ENTER, TryCatch #2 {all -> 0x04a2, blocks: (B:221:0x0486, B:223:0x0499, B:231:0x04c3, B:233:0x04c9, B:238:0x04ea, B:256:0x0521, B:260:0x0554, B:263:0x0566, B:264:0x056a, B:266:0x056f, B:269:0x057b, B:272:0x0587, B:275:0x0593, B:278:0x059f, B:281:0x05ab, B:284:0x05b7, B:287:0x05c3, B:290:0x05cf, B:293:0x05db, B:296:0x05e7, B:299:0x05f3, B:302:0x05ff, B:305:0x060b, B:308:0x0617, B:311:0x0623, B:314:0x062f, B:317:0x063b, B:320:0x0647, B:323:0x0653, B:326:0x065e, B:329:0x066a, B:332:0x0676, B:335:0x0682, B:338:0x068e, B:341:0x069a, B:344:0x06a6, B:347:0x06b2, B:350:0x06be, B:353:0x06ca, B:356:0x06d5, B:359:0x06e1, B:362:0x06ed, B:365:0x06f9, B:368:0x0705, B:371:0x0711, B:374:0x071d, B:377:0x0729, B:380:0x0735, B:383:0x0741, B:386:0x074d, B:389:0x0759, B:392:0x0765, B:395:0x0771, B:398:0x077d, B:401:0x0789, B:404:0x0795, B:407:0x07a1, B:410:0x07ad, B:413:0x07b8, B:416:0x07c4, B:419:0x07d0, B:422:0x07dc, B:425:0x07e8, B:428:0x07f4, B:431:0x0800, B:434:0x080c, B:437:0x0818, B:440:0x0824, B:443:0x0830, B:446:0x083c, B:449:0x0848, B:452:0x0854, B:455:0x0860, B:458:0x086c, B:461:0x0878, B:464:0x0884, B:467:0x0890, B:470:0x089c, B:473:0x08a8, B:476:0x08b4, B:479:0x08c0, B:482:0x08cc, B:485:0x08d8, B:488:0x08e4, B:491:0x08f0, B:494:0x08fc, B:497:0x0908, B:500:0x0914, B:503:0x0920, B:506:0x092c, B:509:0x0938, B:512:0x0944, B:515:0x0950, B:518:0x095c, B:521:0x0968, B:524:0x0973, B:527:0x097e, B:530:0x098a, B:533:0x0996, B:536:0x09a2, B:539:0x09ae, B:542:0x09ba, B:545:0x09c6, B:548:0x09d2, B:551:0x09de, B:554:0x09ea, B:557:0x09f5, B:560:0x0a01, B:563:0x0a0c, B:566:0x0a18, B:569:0x0a24, B:572:0x0a2f, B:575:0x0a3b, B:578:0x0a47, B:581:0x0a53, B:584:0x0a5f, B:587:0x0a6a, B:590:0x0a75, B:593:0x0a80, B:596:0x0a8b, B:599:0x0a96, B:602:0x0aa1, B:605:0x0aac, B:608:0x0ab7, B:614:0x0ad8, B:615:0x0adc, B:619:0x0afc, B:621:0x0b16, B:622:0x0b2e, B:625:0x0b47, B:627:0x0b61, B:628:0x0b79, B:631:0x0b92, B:633:0x0bac, B:634:0x0bc4, B:637:0x0bdd, B:639:0x0bf7, B:640:0x0c0f, B:643:0x0c28, B:645:0x0c42, B:646:0x0c5a, B:649:0x0c73, B:651:0x0c8d, B:652:0x0ca5, B:655:0x0cbe, B:657:0x0cd8, B:658:0x0cf5, B:661:0x0d14, B:663:0x0d2e, B:664:0x0d4b, B:667:0x0d6a, B:669:0x0d84, B:670:0x0da1, B:673:0x0dc0, B:675:0x0dda, B:676:0x0df2, B:679:0x0e0c, B:681:0x0e10, B:683:0x0e18, B:684:0x0e30, B:686:0x0e45, B:688:0x0e49, B:690:0x0e51, B:691:0x0e6e, B:692:0x0e86, B:694:0x0e8a, B:696:0x0e92, B:697:0x0eaa, B:700:0x0ec4, B:702:0x0ede, B:703:0x0ef6, B:706:0x0f10, B:708:0x0f2a, B:709:0x0f42, B:712:0x0f5c, B:714:0x0f76, B:715:0x0f8e, B:718:0x0fa8, B:720:0x0fc2, B:721:0x0fda, B:724:0x0ff4, B:726:0x100e, B:727:0x1026, B:730:0x1040, B:732:0x105a, B:733:0x1077, B:734:0x108f, B:736:0x10ab, B:737:0x10d7, B:738:0x1103, B:739:0x1130, B:740:0x115d, B:741:0x118c, B:742:0x11a5, B:743:0x11be, B:744:0x11d7, B:745:0x11f0, B:746:0x1209, B:747:0x1222, B:748:0x123b, B:749:0x1254, B:750:0x1272, B:751:0x128a, B:752:0x12a7, B:753:0x12bf, B:754:0x12d7, B:756:0x12f2, B:757:0x1319, B:758:0x133b, B:759:0x1362, B:760:0x1384, B:761:0x13a6, B:762:0x13c8, B:763:0x13ee, B:764:0x1414, B:765:0x143a, B:767:0x145f, B:769:0x1463, B:771:0x146b, B:772:0x14a3, B:773:0x14d6, B:774:0x14f7, B:775:0x1518, B:776:0x1539, B:777:0x155a, B:778:0x157b, B:779:0x159a, B:780:0x15ad, B:781:0x15d3, B:782:0x15f9, B:783:0x161f, B:784:0x1645, B:785:0x1670, B:786:0x168c, B:787:0x16a8, B:788:0x16c4, B:789:0x16e0, B:790:0x1701, B:791:0x1722, B:792:0x1743, B:793:0x175f, B:795:0x1763, B:797:0x176b, B:798:0x179e, B:799:0x17b8, B:800:0x17d4, B:801:0x17f0, B:802:0x180c, B:803:0x1828, B:804:0x1844, B:806:0x1858, B:807:0x187d, B:808:0x18a2, B:809:0x18c7, B:810:0x18ed, B:811:0x1915, B:812:0x1936, B:813:0x1953, B:814:0x1974, B:815:0x1990, B:816:0x19ac, B:817:0x19c8, B:818:0x19e9, B:819:0x1a0a, B:820:0x1a2b, B:821:0x1a47, B:823:0x1a4b, B:825:0x1a53, B:826:0x1a86, B:827:0x1aa0, B:828:0x1abc, B:829:0x1ad8, B:832:0x1aef, B:833:0x1b0a, B:834:0x1b25, B:835:0x1b40, B:836:0x1b5b, B:839:0x1b7a, B:840:0x1b93, B:842:0x1bb5), top: B:955:0x0486 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0184 A[Catch: all -> 0x011b, TRY_ENTER, TryCatch #20 {all -> 0x011b, blocks: (B:35:0x0114, B:41:0x012e, B:44:0x0138, B:48:0x014d, B:51:0x0158, B:56:0x0168, B:63:0x0184, B:66:0x0193, B:69:0x0199, B:71:0x019d, B:72:0x01a2), top: B:988:0x0114 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0199 A[Catch: all -> 0x011b, TryCatch #20 {all -> 0x011b, blocks: (B:35:0x0114, B:41:0x012e, B:44:0x0138, B:48:0x014d, B:51:0x0158, B:56:0x0168, B:63:0x0184, B:66:0x0193, B:69:0x0199, B:71:0x019d, B:72:0x01a2), top: B:988:0x0114 }] */
    /* JADX WARN: Removed duplicated region for block: B:847:0x1bd0  */
    /* JADX WARN: Removed duplicated region for block: B:850:0x1be3 A[Catch: all -> 0x1d03, TryCatch #4 {all -> 0x1d03, blocks: (B:848:0x1bda, B:850:0x1be3, B:854:0x1bf4, B:856:0x1bff, B:858:0x1c08, B:859:0x1c0f, B:861:0x1c17, B:864:0x1c2b, B:865:0x1c37, B:866:0x1c44, B:868:0x1c50, B:871:0x1c60, B:874:0x1c70, B:875:0x1c7c, B:881:0x1c88, B:883:0x1cb3, B:888:0x1cbf, B:902:0x1cf2, B:903:0x1cf7), top: B:958:0x1bda }] */
    /* JADX WARN: Removed duplicated region for block: B:902:0x1cf2 A[Catch: all -> 0x1d03, TryCatch #4 {all -> 0x1d03, blocks: (B:848:0x1bda, B:850:0x1be3, B:854:0x1bf4, B:856:0x1bff, B:858:0x1c08, B:859:0x1c0f, B:861:0x1c17, B:864:0x1c2b, B:865:0x1c37, B:866:0x1c44, B:868:0x1c50, B:871:0x1c60, B:874:0x1c70, B:875:0x1c7c, B:881:0x1c88, B:883:0x1cb3, B:888:0x1cbf, B:902:0x1cf2, B:903:0x1cf7), top: B:958:0x1bda }] */
    /* JADX WARN: Removed duplicated region for block: B:945:0x1df0  */
    /* JADX WARN: Removed duplicated region for block: B:946:0x1e00  */
    /* JADX WARN: Removed duplicated region for block: B:949:0x1e07  */
    /* JADX WARN: Removed duplicated region for block: B:955:0x0486 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:974:0x01a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:986:0x0451 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:990:0x0196 A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v34 */
    /* JADX WARN: Type inference failed for: r2v37 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$onMessageReceived$3(Map map, long j) {
        int i;
        String str;
        String str2;
        int i2;
        Throwable th;
        Object obj;
        Throwable th2;
        JSONObject jSONObject;
        String str3;
        Throwable th3;
        JSONObject jSONObject2;
        Object obj2;
        JSONObject jSONObject3;
        long j2;
        int i3;
        boolean z;
        String str4;
        String str5;
        long j3;
        long j4;
        long j5;
        long j6;
        int i4;
        boolean z2;
        boolean z3;
        int i5;
        long j7;
        long j8;
        String str6;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        JSONObject jSONObject4;
        boolean z8;
        String[] strArr;
        long j9;
        boolean z9;
        boolean z10;
        String str7;
        boolean z11;
        boolean z12;
        String str8;
        int i6;
        String str9;
        boolean z13;
        String str10;
        String str11;
        String reactedText;
        boolean z14;
        String str12;
        long j10;
        boolean z15;
        boolean z16;
        char c;
        String formatString;
        String string;
        long j11;
        String str13;
        long j12;
        String formatString2;
        long j13;
        String formatString3;
        long longValue;
        GcmPushListenerService gcmPushListenerService = this;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("GCM START PROCESSING");
        }
        try {
            obj = map.get("p");
        } catch (Throwable th4) {
            th = th4;
            i2 = -1;
            str2 = null;
            str = null;
        }
        if (!(obj instanceof String)) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("GCM DECRYPT ERROR 1");
            }
            onDecryptError();
            return;
        }
        byte[] decode = Base64.decode((String) obj, 8);
        NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(decode.length);
        nativeByteBuffer.writeBytes(decode);
        nativeByteBuffer.position(0);
        if (SharedConfig.pushAuthKeyId == null) {
            SharedConfig.pushAuthKeyId = new byte[8];
            byte[] computeSHA1 = Utilities.computeSHA1(SharedConfig.pushAuthKey);
            System.arraycopy(computeSHA1, computeSHA1.length - 8, SharedConfig.pushAuthKeyId, 0, 8);
        }
        byte[] bArr = new byte[8];
        nativeByteBuffer.readBytes(bArr, true);
        if (!Arrays.equals(SharedConfig.pushAuthKeyId, bArr)) {
            onDecryptError();
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d(String.format(Locale.US, "GCM DECRYPT ERROR 2 k1=%s k2=%s, key=%s", Utilities.bytesToHex(SharedConfig.pushAuthKeyId), Utilities.bytesToHex(bArr), Utilities.bytesToHex(SharedConfig.pushAuthKey)));
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
            FileLog.d(String.format("GCM DECRYPT ERROR 3, key = %s", Utilities.bytesToHex(SharedConfig.pushAuthKey)));
            return;
        }
        byte[] bArr4 = new byte[nativeByteBuffer.readInt32(true)];
        nativeByteBuffer.readBytes(bArr4, true);
        str = new String(bArr4);
        try {
            jSONObject = new JSONObject(str);
            if (jSONObject.has("loc_key")) {
                try {
                    str3 = jSONObject.getString("loc_key");
                } catch (Throwable th5) {
                    th2 = th5;
                    th = th2;
                    i2 = -1;
                    str2 = null;
                    i = -1;
                    if (i == i2) {
                    }
                    if (BuildVars.LOGS_ENABLED) {
                    }
                    FileLog.e(th);
                    return;
                }
            } else {
                str3 = "";
            }
            try {
                if (jSONObject.get("custom") instanceof JSONObject) {
                    try {
                        jSONObject2 = jSONObject.getJSONObject("custom");
                    } catch (Throwable th6) {
                        th = th6;
                        str2 = str3;
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
                    jSONObject2 = new JSONObject();
                }
                obj2 = jSONObject.has("user_id") ? jSONObject.get("user_id") : null;
            } catch (Throwable th7) {
                th3 = th7;
                str2 = str3;
                i2 = -1;
                i = -1;
            }
        } catch (Throwable th8) {
            th2 = th8;
        }
        if (obj2 == null) {
            longValue = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
        } else if (obj2 instanceof Long) {
            longValue = ((Long) obj2).longValue();
        } else {
            if (obj2 instanceof Integer) {
                jSONObject3 = jSONObject;
                j2 = ((Integer) obj2).intValue();
            } else {
                jSONObject3 = jSONObject;
                if (obj2 instanceof String) {
                    j2 = Utilities.parseInt((CharSequence) ((String) obj2)).intValue();
                } else {
                    j2 = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                }
            }
            i = UserConfig.selectedAccount;
            i3 = 0;
            while (true) {
                if (i3 < 4) {
                    z = false;
                    break;
                } else if (UserConfig.getInstance(i3).getClientUserId() == j2) {
                    i = i3;
                    z = true;
                    break;
                } else {
                    i3++;
                }
            }
            if (z) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("GCM ACCOUNT NOT FOUND");
                }
                gcmPushListenerService.countDownLatch.countDown();
                return;
            }
            try {
                try {
                } catch (Throwable th9) {
                    th = th9;
                    str2 = str3;
                    i2 = -1;
                }
            } catch (Throwable th10) {
                th3 = th10;
            }
            if (!UserConfig.getInstance(i).isClientActivated()) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("GCM ACCOUNT NOT ACTIVATED");
                }
                gcmPushListenerService.countDownLatch.countDown();
                return;
            }
            map.get("google.sent_time");
            switch (str3.hashCode()) {
                case -1963663249:
                    if (str3.equals("SESSION_REVOKE")) {
                        str4 = 2;
                        break;
                    }
                    str4 = -1;
                    break;
                case -920689527:
                    if (str3.equals("DC_UPDATE")) {
                        str4 = 0;
                        break;
                    }
                    str4 = -1;
                    break;
                case 633004703:
                    if (str3.equals("MESSAGE_ANNOUNCEMENT")) {
                        str4 = 1;
                        break;
                    }
                    str4 = -1;
                    break;
                case 1365673842:
                    if (str3.equals("GEO_LIVE_PENDING")) {
                        str4 = 3;
                        break;
                    }
                    str4 = -1;
                    break;
                default:
                    str4 = -1;
                    break;
            }
            try {
            } catch (Throwable th11) {
                th3 = th11;
            }
            if (str4 == 0) {
                int i7 = jSONObject2.getInt("dc");
                String[] split = jSONObject2.getString("addr").split(":");
                if (split.length != 2) {
                    gcmPushListenerService.countDownLatch.countDown();
                    return;
                }
                ConnectionsManager.getInstance(i).applyDatacenterAddress(i7, split[0], Integer.parseInt(split[1]));
                ConnectionsManager.getInstance(i).resumeNetworkMaybe();
                gcmPushListenerService.countDownLatch.countDown();
                return;
            } else if (str4 == 1) {
                TLRPC$TL_updateServiceNotification tLRPC$TL_updateServiceNotification = new TLRPC$TL_updateServiceNotification();
                tLRPC$TL_updateServiceNotification.popup = false;
                tLRPC$TL_updateServiceNotification.flags = 2;
                tLRPC$TL_updateServiceNotification.inbox_date = (int) (j / 1000);
                tLRPC$TL_updateServiceNotification.message = jSONObject3.getString("message");
                tLRPC$TL_updateServiceNotification.type = "announcement";
                tLRPC$TL_updateServiceNotification.media = new TLRPC$TL_messageMediaEmpty();
                TLRPC$TL_updates tLRPC$TL_updates = new TLRPC$TL_updates();
                tLRPC$TL_updates.updates.add(tLRPC$TL_updateServiceNotification);
                Utilities.stageQueue.postRunnable(new GcmPushListenerService$$ExternalSyntheticLambda3(i, tLRPC$TL_updates));
                ConnectionsManager.getInstance(i).resumeNetworkMaybe();
                gcmPushListenerService.countDownLatch.countDown();
                return;
            } else if (str4 == 2) {
                AndroidUtilities.runOnUIThread(new GcmPushListenerService$$ExternalSyntheticLambda0(i));
                gcmPushListenerService.countDownLatch.countDown();
                return;
            } else {
                if (str4 == 3) {
                    str5 = str;
                    str4 = str3;
                    int i8 = i;
                    try {
                        Utilities.stageQueue.postRunnable(new GcmPushListenerService$$ExternalSyntheticLambda1(i8));
                        gcmPushListenerService.countDownLatch.countDown();
                        return;
                    } catch (Throwable th12) {
                        th3 = th12;
                        i = i8;
                    }
                } else {
                    try {
                        if (jSONObject2.has("channel_id")) {
                            try {
                                j3 = jSONObject2.getLong("channel_id");
                                str4 = str3;
                                j4 = -j3;
                            } catch (Throwable th13) {
                                th3 = th13;
                                str4 = str3;
                                str2 = str4;
                                i2 = -1;
                                th = th3;
                                if (i == i2) {
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                }
                                FileLog.e(th);
                                return;
                            }
                        } else {
                            str4 = str3;
                            j4 = 0;
                            j3 = 0;
                        }
                        try {
                            if (jSONObject2.has("from_id")) {
                                try {
                                    j4 = jSONObject2.getLong("from_id");
                                    j5 = j4;
                                } catch (Throwable th14) {
                                    th3 = th14;
                                    str2 = str4;
                                    i2 = -1;
                                    th = th3;
                                    if (i == i2) {
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    FileLog.e(th);
                                    return;
                                }
                            } else {
                                j5 = 0;
                            }
                            if (jSONObject2.has("chat_id")) {
                                long j14 = jSONObject2.getLong("chat_id");
                                j6 = j14;
                                j4 = -j14;
                            } else {
                                j6 = 0;
                            }
                            if (jSONObject2.has("encryption_id")) {
                                j4 = DialogObject.makeEncryptedDialogId(jSONObject2.getInt("encryption_id"));
                            }
                            boolean z17 = jSONObject2.has("schedule") && jSONObject2.getInt("schedule") == 1;
                            if (j4 == 0 && "ENCRYPTED_MESSAGE".equals(str4)) {
                                j4 = NotificationsController.globalSecretChatId;
                            }
                            if (j4 != 0) {
                                if ("READ_HISTORY".equals(str4)) {
                                    int i9 = jSONObject2.getInt("max_id");
                                    ArrayList<TLRPC$Update> arrayList = new ArrayList<>();
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("GCM received read notification max_id = " + i9 + " for dialogId = " + j4);
                                    }
                                    if (j3 != 0) {
                                        TLRPC$TL_updateReadChannelInbox tLRPC$TL_updateReadChannelInbox = new TLRPC$TL_updateReadChannelInbox();
                                        tLRPC$TL_updateReadChannelInbox.channel_id = j3;
                                        tLRPC$TL_updateReadChannelInbox.max_id = i9;
                                        arrayList.add(tLRPC$TL_updateReadChannelInbox);
                                    } else {
                                        TLRPC$TL_updateReadHistoryInbox tLRPC$TL_updateReadHistoryInbox = new TLRPC$TL_updateReadHistoryInbox();
                                        long j15 = j5;
                                        if (j15 != 0) {
                                            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                            tLRPC$TL_updateReadHistoryInbox.peer = tLRPC$TL_peerUser;
                                            tLRPC$TL_peerUser.user_id = j15;
                                        } else {
                                            TLRPC$TL_peerChat tLRPC$TL_peerChat = new TLRPC$TL_peerChat();
                                            tLRPC$TL_updateReadHistoryInbox.peer = tLRPC$TL_peerChat;
                                            tLRPC$TL_peerChat.chat_id = j6;
                                        }
                                        tLRPC$TL_updateReadHistoryInbox.max_id = i9;
                                        arrayList.add(tLRPC$TL_updateReadHistoryInbox);
                                    }
                                    MessagesController.getInstance(i).processUpdateArray(arrayList, null, null, false, 0);
                                } else {
                                    long j16 = j5;
                                    str5 = str;
                                    if ("MESSAGE_DELETED".equals(str4)) {
                                        String[] split2 = jSONObject2.getString("messages").split(",");
                                        LongSparseArray<ArrayList<Integer>> longSparseArray = new LongSparseArray<>();
                                        ArrayList<Integer> arrayList2 = new ArrayList<>();
                                        for (String str14 : split2) {
                                            arrayList2.add(Utilities.parseInt((CharSequence) str14));
                                        }
                                        longSparseArray.put(-j3, arrayList2);
                                        NotificationsController.getInstance(i).removeDeletedMessagesFromNotifications(longSparseArray);
                                        MessagesController.getInstance(i).deleteMessagesByPush(j4, arrayList2, j3);
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("GCM received " + str4 + " for dialogId = " + j4 + " mids = " + TextUtils.join(",", arrayList2));
                                        }
                                    } else {
                                        try {
                                            if (!TextUtils.isEmpty(str4)) {
                                                if (jSONObject2.has("msg_id")) {
                                                    i5 = jSONObject2.getInt("msg_id");
                                                    z3 = z17;
                                                } else {
                                                    z3 = z17;
                                                    i5 = 0;
                                                }
                                                if (jSONObject2.has("random_id")) {
                                                    long j17 = j6;
                                                    j8 = Utilities.parseLong(jSONObject2.getString("random_id")).longValue();
                                                    j7 = j17;
                                                } else {
                                                    j7 = j6;
                                                    j8 = 0;
                                                }
                                                try {
                                                    if (i5 != 0) {
                                                        Integer num = MessagesController.getInstance(i).dialogs_read_inbox_max.get(Long.valueOf(j4));
                                                        if (num == null) {
                                                            num = Integer.valueOf(MessagesStorage.getInstance(i).getDialogReadMax(false, j4));
                                                            str6 = "messages";
                                                            MessagesController.getInstance(i).dialogs_read_inbox_max.put(Long.valueOf(j4), num);
                                                        } else {
                                                            str6 = "messages";
                                                        }
                                                    } else {
                                                        str6 = "messages";
                                                        if (j8 != 0) {
                                                        }
                                                        z4 = false;
                                                    }
                                                    try {
                                                        if (!str4.startsWith("REACT_")) {
                                                        }
                                                        z4 = true;
                                                        if (z4) {
                                                            long j18 = j8;
                                                            int i10 = i5;
                                                            int i11 = i;
                                                            try {
                                                                long optLong = jSONObject2.optLong("chat_from_id", 0L);
                                                                long j19 = j3;
                                                                long optLong2 = jSONObject2.optLong("chat_from_broadcast_id", 0L);
                                                                long optLong3 = jSONObject2.optLong("chat_from_group_id", 0L);
                                                                try {
                                                                    if (optLong == 0 && optLong3 == 0) {
                                                                        z5 = false;
                                                                        if (jSONObject2.has("mention")) {
                                                                            try {
                                                                                if (jSONObject2.getInt("mention") != 0) {
                                                                                    z6 = true;
                                                                                    if (jSONObject2.has("silent")) {
                                                                                        if (jSONObject2.getInt("silent") != 0) {
                                                                                            i4 = i11;
                                                                                            z7 = true;
                                                                                            boolean z18 = z7;
                                                                                            jSONObject4 = jSONObject3;
                                                                                            if (!jSONObject4.has("loc_args")) {
                                                                                                try {
                                                                                                    JSONArray jSONArray = jSONObject4.getJSONArray("loc_args");
                                                                                                    int length = jSONArray.length();
                                                                                                    z8 = z6;
                                                                                                    strArr = new String[length];
                                                                                                    j9 = optLong;
                                                                                                    for (int i12 = 0; i12 < length; i12++) {
                                                                                                        strArr[i12] = jSONArray.getString(i12);
                                                                                                    }
                                                                                                } catch (Throwable th15) {
                                                                                                    th3 = th15;
                                                                                                    i2 = -1;
                                                                                                    gcmPushListenerService = this;
                                                                                                    str2 = str4;
                                                                                                    str = str5;
                                                                                                    i = i4;
                                                                                                }
                                                                                            } else {
                                                                                                z8 = z6;
                                                                                                j9 = optLong;
                                                                                                strArr = null;
                                                                                            }
                                                                                            String str15 = strArr[0];
                                                                                            boolean has = jSONObject2.has("edit_date");
                                                                                            if (!str4.startsWith("CHAT_")) {
                                                                                                if (UserObject.isReplyUser(j4)) {
                                                                                                    str15 = str15 + " @ " + strArr[1];
                                                                                                    z11 = false;
                                                                                                    str7 = null;
                                                                                                    z10 = false;
                                                                                                    z9 = false;
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                        str8 = str15;
                                                                                                        StringBuilder sb = new StringBuilder();
                                                                                                        z12 = has;
                                                                                                        sb.append("GCM received message notification ");
                                                                                                        sb.append(str4);
                                                                                                        sb.append(" for dialogId = ");
                                                                                                        sb.append(j4);
                                                                                                        sb.append(" mid = ");
                                                                                                        i6 = i10;
                                                                                                        sb.append(i6);
                                                                                                        FileLog.d(sb.toString());
                                                                                                    } else {
                                                                                                        z12 = has;
                                                                                                        str8 = str15;
                                                                                                        i6 = i10;
                                                                                                    }
                                                                                                    try {
                                                                                                        if (str4.startsWith("REACT_")) {
                                                                                                            str10 = "REACT_";
                                                                                                            str9 = str7;
                                                                                                            z13 = z10;
                                                                                                            str11 = "CHAT_REACT_";
                                                                                                            gcmPushListenerService = this;
                                                                                                        } else if (!str4.startsWith("CHAT_REACT_")) {
                                                                                                            switch (str4.hashCode()) {
                                                                                                                case -2100047043:
                                                                                                                    if (str4.equals("MESSAGE_GAME_SCORE")) {
                                                                                                                        c = 20;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -2091498420:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_CONTACT")) {
                                                                                                                        c = '$';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -2053872415:
                                                                                                                    if (str4.equals("CHAT_CREATED")) {
                                                                                                                        c = 'B';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -2039746363:
                                                                                                                    if (str4.equals("MESSAGE_STICKER")) {
                                                                                                                        c = 11;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -2023218804:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_VIDEOS")) {
                                                                                                                        c = '-';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1979538588:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_DOC")) {
                                                                                                                        c = '!';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1979536003:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_GEO")) {
                                                                                                                        c = '\'';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1979535888:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_GIF")) {
                                                                                                                        c = ')';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1969004705:
                                                                                                                    if (str4.equals("CHAT_ADD_MEMBER")) {
                                                                                                                        c = 'F';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1946699248:
                                                                                                                    if (str4.equals("CHAT_JOINED")) {
                                                                                                                        c = 'O';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1717283471:
                                                                                                                    if (str4.equals("CHAT_REQ_JOINED")) {
                                                                                                                        c = 'P';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1646640058:
                                                                                                                    if (str4.equals("CHAT_VOICECHAT_START")) {
                                                                                                                        c = 'G';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1528047021:
                                                                                                                    if (str4.equals("CHAT_MESSAGES")) {
                                                                                                                        c = 'V';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1507149394:
                                                                                                                    if (str4.equals("MESSAGE_RECURRING_PAY")) {
                                                                                                                        c = 0;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1493579426:
                                                                                                                    if (str4.equals("MESSAGE_AUDIO")) {
                                                                                                                        c = '\f';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1482481933:
                                                                                                                    if (str4.equals("MESSAGE_MUTED")) {
                                                                                                                        c = 'q';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1480102982:
                                                                                                                    if (str4.equals("MESSAGE_PHOTO")) {
                                                                                                                        c = 4;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1478041834:
                                                                                                                    if (str4.equals("MESSAGE_ROUND")) {
                                                                                                                        c = '\t';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1474543101:
                                                                                                                    if (str4.equals("MESSAGE_VIDEO")) {
                                                                                                                        c = 6;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1465695932:
                                                                                                                    if (str4.equals("ENCRYPTION_ACCEPT")) {
                                                                                                                        c = 'o';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1374906292:
                                                                                                                    if (str4.equals("ENCRYPTED_MESSAGE")) {
                                                                                                                        c = 'h';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1372940586:
                                                                                                                    if (str4.equals("CHAT_RETURNED")) {
                                                                                                                        c = 'N';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1264245338:
                                                                                                                    if (str4.equals("PINNED_INVOICE")) {
                                                                                                                        c = 'f';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1236154001:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_DOCS")) {
                                                                                                                        c = '/';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1236086700:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_FWDS")) {
                                                                                                                        c = '+';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1236077786:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_GAME")) {
                                                                                                                        c = '*';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1235796237:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_POLL")) {
                                                                                                                        c = '&';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1235760759:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_QUIZ")) {
                                                                                                                        c = '%';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1235686303:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_TEXT")) {
                                                                                                                        c = 2;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1198046100:
                                                                                                                    if (str4.equals("MESSAGE_VIDEO_SECRET")) {
                                                                                                                        c = 7;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1124254527:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_CONTACT")) {
                                                                                                                        c = '9';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1085137927:
                                                                                                                    if (str4.equals("PINNED_GAME")) {
                                                                                                                        c = 'd';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1084856378:
                                                                                                                    if (str4.equals("PINNED_POLL")) {
                                                                                                                        c = 'a';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1084820900:
                                                                                                                    if (str4.equals("PINNED_QUIZ")) {
                                                                                                                        c = '`';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -1084746444:
                                                                                                                    if (str4.equals("PINNED_TEXT")) {
                                                                                                                        c = 'W';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -819729482:
                                                                                                                    if (str4.equals("PINNED_STICKER")) {
                                                                                                                        c = ']';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -772141857:
                                                                                                                    if (str4.equals("PHONE_CALL_REQUEST")) {
                                                                                                                        c = 'p';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -638310039:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_STICKER")) {
                                                                                                                        c = '\"';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -590403924:
                                                                                                                    if (str4.equals("PINNED_GAME_SCORE")) {
                                                                                                                        c = 'e';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -589196239:
                                                                                                                    if (str4.equals("PINNED_DOC")) {
                                                                                                                        c = '\\';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -589193654:
                                                                                                                    if (str4.equals("PINNED_GEO")) {
                                                                                                                        c = 'b';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -589193539:
                                                                                                                    if (str4.equals("PINNED_GIF")) {
                                                                                                                        c = 'g';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -440169325:
                                                                                                                    if (str4.equals("AUTH_UNKNOWN")) {
                                                                                                                        c = 'k';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -412748110:
                                                                                                                    if (str4.equals("CHAT_DELETE_YOU")) {
                                                                                                                        c = 'L';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -228518075:
                                                                                                                    if (str4.equals("MESSAGE_GEOLIVE")) {
                                                                                                                        c = 17;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -213586509:
                                                                                                                    if (str4.equals("ENCRYPTION_REQUEST")) {
                                                                                                                        c = 'n';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -115582002:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_INVOICE")) {
                                                                                                                        c = 'A';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -112621464:
                                                                                                                    if (str4.equals("CONTACT_JOINED")) {
                                                                                                                        c = 'j';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -108522133:
                                                                                                                    if (str4.equals("AUTH_REGION")) {
                                                                                                                        c = 'l';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -107572034:
                                                                                                                    if (str4.equals("MESSAGE_SCREENSHOT")) {
                                                                                                                        c = '\b';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case -40534265:
                                                                                                                    if (str4.equals("CHAT_DELETE_MEMBER")) {
                                                                                                                        c = 'K';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 52369421:
                                                                                                                    if (str4.equals("REACT_TEXT")) {
                                                                                                                        c = 'i';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 65254746:
                                                                                                                    if (str4.equals("CHAT_ADD_YOU")) {
                                                                                                                        c = 'C';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 141040782:
                                                                                                                    if (str4.equals("CHAT_LEFT")) {
                                                                                                                        c = 'M';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 202550149:
                                                                                                                    if (str4.equals("CHAT_VOICECHAT_INVITE")) {
                                                                                                                        c = 'H';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 309993049:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_DOC")) {
                                                                                                                        c = '6';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 309995634:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_GEO")) {
                                                                                                                        c = '<';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 309995749:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_GIF")) {
                                                                                                                        c = '>';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 320532812:
                                                                                                                    if (str4.equals("MESSAGES")) {
                                                                                                                        c = 28;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 328933854:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_STICKER")) {
                                                                                                                        c = '7';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 331340546:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_AUDIO")) {
                                                                                                                        c = '#';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 342406591:
                                                                                                                    if (str4.equals("CHAT_VOICECHAT_END")) {
                                                                                                                        c = 'I';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 344816990:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_PHOTO")) {
                                                                                                                        c = 30;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 346878138:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_ROUND")) {
                                                                                                                        c = ' ';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 350376871:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_VIDEO")) {
                                                                                                                        c = 31;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 608430149:
                                                                                                                    if (str4.equals("CHAT_VOICECHAT_INVITE_YOU")) {
                                                                                                                        c = 'J';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 615714517:
                                                                                                                    if (str4.equals("MESSAGE_PHOTO_SECRET")) {
                                                                                                                        c = 5;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 715508879:
                                                                                                                    if (str4.equals("PINNED_AUDIO")) {
                                                                                                                        c = '^';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 728985323:
                                                                                                                    if (str4.equals("PINNED_PHOTO")) {
                                                                                                                        c = 'Y';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 731046471:
                                                                                                                    if (str4.equals("PINNED_ROUND")) {
                                                                                                                        c = '[';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 734545204:
                                                                                                                    if (str4.equals("PINNED_VIDEO")) {
                                                                                                                        c = 'Z';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 802032552:
                                                                                                                    if (str4.equals("MESSAGE_CONTACT")) {
                                                                                                                        c = '\r';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 991498806:
                                                                                                                    if (str4.equals("PINNED_GEOLIVE")) {
                                                                                                                        c = 'c';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1007364121:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_GAME_SCORE")) {
                                                                                                                        c = 21;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1019850010:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_DOCS")) {
                                                                                                                        c = 'U';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1019917311:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_FWDS")) {
                                                                                                                        c = 'Q';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1019926225:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_GAME")) {
                                                                                                                        c = '?';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1020207774:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_POLL")) {
                                                                                                                        c = ';';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1020243252:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_QUIZ")) {
                                                                                                                        c = ':';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1020317708:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_TEXT")) {
                                                                                                                        c = '1';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1060282259:
                                                                                                                    if (str4.equals("MESSAGE_DOCS")) {
                                                                                                                        c = 27;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1060349560:
                                                                                                                    if (str4.equals("MESSAGE_FWDS")) {
                                                                                                                        c = 23;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1060358474:
                                                                                                                    if (str4.equals("MESSAGE_GAME")) {
                                                                                                                        c = 19;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1060640023:
                                                                                                                    if (str4.equals("MESSAGE_POLL")) {
                                                                                                                        c = 15;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1060675501:
                                                                                                                    if (str4.equals("MESSAGE_QUIZ")) {
                                                                                                                        c = 14;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1060749957:
                                                                                                                    if (str4.equals("MESSAGE_TEXT")) {
                                                                                                                        c = 1;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1073049781:
                                                                                                                    if (str4.equals("PINNED_NOTEXT")) {
                                                                                                                        c = 'X';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1078101399:
                                                                                                                    if (str4.equals("CHAT_TITLE_EDITED")) {
                                                                                                                        c = 'D';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1110103437:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_NOTEXT")) {
                                                                                                                        c = '2';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1160762272:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_PHOTOS")) {
                                                                                                                        c = 'R';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1172918249:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_GEOLIVE")) {
                                                                                                                        c = '(';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1234591620:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_GAME_SCORE")) {
                                                                                                                        c = '@';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1281128640:
                                                                                                                    if (str4.equals("MESSAGE_DOC")) {
                                                                                                                        c = '\n';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1281131225:
                                                                                                                    if (str4.equals("MESSAGE_GEO")) {
                                                                                                                        c = 16;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1281131340:
                                                                                                                    if (str4.equals("MESSAGE_GIF")) {
                                                                                                                        c = 18;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1310789062:
                                                                                                                    if (str4.equals("MESSAGE_NOTEXT")) {
                                                                                                                        c = 3;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1333118583:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_VIDEOS")) {
                                                                                                                        c = 'S';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1361447897:
                                                                                                                    if (str4.equals("MESSAGE_PHOTOS")) {
                                                                                                                        c = 24;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1498266155:
                                                                                                                    if (str4.equals("PHONE_CALL_MISSED")) {
                                                                                                                        c = 'r';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1533804208:
                                                                                                                    if (str4.equals("MESSAGE_VIDEOS")) {
                                                                                                                        c = 25;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1540131626:
                                                                                                                    if (str4.equals("MESSAGE_PLAYLIST")) {
                                                                                                                        c = 26;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1547988151:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_AUDIO")) {
                                                                                                                        c = '8';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1561464595:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_PHOTO")) {
                                                                                                                        c = '3';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1563525743:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_ROUND")) {
                                                                                                                        c = '5';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1567024476:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_VIDEO")) {
                                                                                                                        c = '4';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1810705077:
                                                                                                                    if (str4.equals("MESSAGE_INVOICE")) {
                                                                                                                        c = 22;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1815177512:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGES")) {
                                                                                                                        c = '0';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1954774321:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_PLAYLIST")) {
                                                                                                                        c = 'T';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 1963241394:
                                                                                                                    if (str4.equals("LOCKED_MESSAGE")) {
                                                                                                                        c = 'm';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 2014789757:
                                                                                                                    if (str4.equals("CHAT_PHOTO_EDITED")) {
                                                                                                                        c = 'E';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 2022049433:
                                                                                                                    if (str4.equals("PINNED_CONTACT")) {
                                                                                                                        c = '_';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 2034984710:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_PLAYLIST")) {
                                                                                                                        c = '.';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 2048733346:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_NOTEXT")) {
                                                                                                                        c = 29;
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 2099392181:
                                                                                                                    if (str4.equals("CHANNEL_MESSAGE_PHOTOS")) {
                                                                                                                        c = ',';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                case 2140162142:
                                                                                                                    if (str4.equals("CHAT_MESSAGE_GEOLIVE")) {
                                                                                                                        c = '=';
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                                default:
                                                                                                                    c = 65535;
                                                                                                                    break;
                                                                                                            }
                                                                                                            str11 = "CHAT_REACT_";
                                                                                                            str10 = "REACT_";
                                                                                                            z13 = z10;
                                                                                                            str9 = str7;
                                                                                                            switch (c) {
                                                                                                                case 0:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageRecurringPay", 2131627004, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("PaymentInvoice", 2131627393);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                        TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                                                                                                                        tLRPC$TL_message.id = i6;
                                                                                                                        tLRPC$TL_message.random_id = j18;
                                                                                                                        tLRPC$TL_message.message = str12 != null ? str12 : reactedText;
                                                                                                                        tLRPC$TL_message.date = (int) (j / 1000);
                                                                                                                        if (z11) {
                                                                                                                            tLRPC$TL_message.action = new TLRPC$TL_messageActionPinMessage();
                                                                                                                        }
                                                                                                                        if (z9) {
                                                                                                                            tLRPC$TL_message.flags |= Integer.MIN_VALUE;
                                                                                                                        }
                                                                                                                        tLRPC$TL_message.dialog_id = j4;
                                                                                                                        if (j19 != 0) {
                                                                                                                            TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                                                                                                            tLRPC$TL_message.peer_id = tLRPC$TL_peerChannel;
                                                                                                                            tLRPC$TL_peerChannel.channel_id = j19;
                                                                                                                            j10 = j7;
                                                                                                                        } else if (j7 != 0) {
                                                                                                                            TLRPC$TL_peerChat tLRPC$TL_peerChat2 = new TLRPC$TL_peerChat();
                                                                                                                            tLRPC$TL_message.peer_id = tLRPC$TL_peerChat2;
                                                                                                                            j10 = j7;
                                                                                                                            tLRPC$TL_peerChat2.chat_id = j10;
                                                                                                                        } else {
                                                                                                                            j10 = j7;
                                                                                                                            TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                                                                                                                            tLRPC$TL_message.peer_id = tLRPC$TL_peerUser2;
                                                                                                                            tLRPC$TL_peerUser2.user_id = j16;
                                                                                                                        }
                                                                                                                        tLRPC$TL_message.flags |= 256;
                                                                                                                        if (optLong3 != 0) {
                                                                                                                            TLRPC$TL_peerChat tLRPC$TL_peerChat3 = new TLRPC$TL_peerChat();
                                                                                                                            tLRPC$TL_message.from_id = tLRPC$TL_peerChat3;
                                                                                                                            tLRPC$TL_peerChat3.chat_id = j10;
                                                                                                                        } else if (optLong2 != 0) {
                                                                                                                            TLRPC$TL_peerChannel tLRPC$TL_peerChannel2 = new TLRPC$TL_peerChannel();
                                                                                                                            tLRPC$TL_message.from_id = tLRPC$TL_peerChannel2;
                                                                                                                            tLRPC$TL_peerChannel2.channel_id = optLong2;
                                                                                                                        } else if (j9 != 0) {
                                                                                                                            TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                                                                                                                            tLRPC$TL_message.from_id = tLRPC$TL_peerUser3;
                                                                                                                            tLRPC$TL_peerUser3.user_id = j9;
                                                                                                                        } else {
                                                                                                                            tLRPC$TL_message.from_id = tLRPC$TL_message.peer_id;
                                                                                                                        }
                                                                                                                        if (!z8 && !z11) {
                                                                                                                            z15 = false;
                                                                                                                            tLRPC$TL_message.mentioned = z15;
                                                                                                                            tLRPC$TL_message.silent = z18;
                                                                                                                            tLRPC$TL_message.from_scheduled = z3;
                                                                                                                            MessageObject messageObject = new MessageObject(i4, tLRPC$TL_message, reactedText, str8, str9, z14, z13, z9, z12);
                                                                                                                            if (!str4.startsWith(str10) && !str4.startsWith(str11)) {
                                                                                                                                z16 = false;
                                                                                                                                messageObject.isReactionPush = z16;
                                                                                                                                ArrayList<MessageObject> arrayList3 = new ArrayList<>();
                                                                                                                                arrayList3.add(messageObject);
                                                                                                                                NotificationsController.getInstance(i4).processNewMessages(arrayList3, true, true, gcmPushListenerService.countDownLatch);
                                                                                                                                z2 = false;
                                                                                                                                if (z2) {
                                                                                                                                    gcmPushListenerService.countDownLatch.countDown();
                                                                                                                                }
                                                                                                                                ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                                ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                                return;
                                                                                                                            }
                                                                                                                            z16 = true;
                                                                                                                            messageObject.isReactionPush = z16;
                                                                                                                            ArrayList<MessageObject> arrayList32 = new ArrayList<>();
                                                                                                                            arrayList32.add(messageObject);
                                                                                                                            NotificationsController.getInstance(i4).processNewMessages(arrayList32, true, true, gcmPushListenerService.countDownLatch);
                                                                                                                            z2 = false;
                                                                                                                            if (z2) {
                                                                                                                            }
                                                                                                                            ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                            ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        z15 = true;
                                                                                                                        tLRPC$TL_message.mentioned = z15;
                                                                                                                        tLRPC$TL_message.silent = z18;
                                                                                                                        tLRPC$TL_message.from_scheduled = z3;
                                                                                                                        MessageObject messageObject2 = new MessageObject(i4, tLRPC$TL_message, reactedText, str8, str9, z14, z13, z9, z12);
                                                                                                                        if (!str4.startsWith(str10)) {
                                                                                                                            z16 = false;
                                                                                                                            messageObject2.isReactionPush = z16;
                                                                                                                            ArrayList<MessageObject> arrayList322 = new ArrayList<>();
                                                                                                                            arrayList322.add(messageObject2);
                                                                                                                            NotificationsController.getInstance(i4).processNewMessages(arrayList322, true, true, gcmPushListenerService.countDownLatch);
                                                                                                                            z2 = false;
                                                                                                                            if (z2) {
                                                                                                                            }
                                                                                                                            ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                            ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        z16 = true;
                                                                                                                        messageObject2.isReactionPush = z16;
                                                                                                                        ArrayList<MessageObject> arrayList3222 = new ArrayList<>();
                                                                                                                        arrayList3222.add(messageObject2);
                                                                                                                        NotificationsController.getInstance(i4).processNewMessages(arrayList3222, true, true, gcmPushListenerService.countDownLatch);
                                                                                                                        z2 = false;
                                                                                                                        if (z2) {
                                                                                                                        }
                                                                                                                        ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                        ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                        return;
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 1:
                                                                                                                case 2:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageText", 2131627012, strArr[0], strArr[1]);
                                                                                                                    string = strArr[1];
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 3:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageNoText", 2131627000, strArr[0]);
                                                                                                                    string = LocaleController.getString("Message", 2131626622);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 4:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessagePhoto", 2131627001, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachPhoto", 2131624502);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 5:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageSDPhoto", 2131627006, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachDestructingPhoto", 2131624479);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 6:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageVideo", 2131627013, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachVideo", 2131624508);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 7:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageSDVideo", 2131627007, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachDestructingVideo", 2131624480);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\b':
                                                                                                                    formatString = LocaleController.getString("ActionTakeScreenshoot", 2131624216).replace("un1", strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\t':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageRound", 2131627005, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachRound", 2131624504);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\n':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageDocument", 2131626971, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachDocument", 2131624481);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 11:
                                                                                                                    if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                                                                                        formatString = LocaleController.formatString("NotificationMessageStickerEmoji", 2131627011, strArr[0], strArr[1]);
                                                                                                                        string = strArr[1] + " " + LocaleController.getString("AttachSticker", 2131624505);
                                                                                                                    } else {
                                                                                                                        formatString = LocaleController.formatString("NotificationMessageSticker", 2131627010, strArr[0]);
                                                                                                                        string = LocaleController.getString("AttachSticker", 2131624505);
                                                                                                                    }
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\f':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageAudio", 2131626969, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachAudio", 2131624476);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\r':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageContact2", 2131626970, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachContact", 2131624478);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 14:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageQuiz2", 2131627003, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("QuizPoll", 2131627823);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 15:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessagePoll2", 2131627002, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("Poll", 2131627577);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 16:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageMap", 2131626998, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachLocation", 2131624492);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 17:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageLiveLocation", 2131626997, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachLiveLocation", 2131624488);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 18:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGif", 2131626976, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachGif", 2131624483);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 19:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGame", 2131626974, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachGame", 2131624482);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 20:
                                                                                                                case 21:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGameScored", 2131626975, strArr[0], strArr[1], strArr[2]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 22:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageInvoice", 2131626996, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("PaymentInvoice", 2131627393);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 23:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageForwardFew", 2131626973, strArr[0], LocaleController.formatPluralString(str6, Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 24:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageFew", 2131626972, strArr[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 25:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageFew", 2131626972, strArr[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 26:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageFew", 2131626972, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 27:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageFew", 2131626972, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 28:
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageAlbum", 2131626968, strArr[0]);
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 29:
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageNoText", 2131624928, strArr[0]);
                                                                                                                    string = LocaleController.getString("Message", 2131626622);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 30:
                                                                                                                    formatString = LocaleController.formatString("ChannelMessagePhoto", 2131624929, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachPhoto", 2131624502);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 31:
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageVideo", 2131624935, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachVideo", 2131624508);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case ' ':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageRound", 2131624932, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachRound", 2131624504);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '!':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageDocument", 2131624922, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachDocument", 2131624481);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\"':
                                                                                                                    if (strArr.length > 1 && !TextUtils.isEmpty(strArr[1])) {
                                                                                                                        formatString = LocaleController.formatString("ChannelMessageStickerEmoji", 2131624934, strArr[0], strArr[1]);
                                                                                                                        string = strArr[1] + " " + LocaleController.getString("AttachSticker", 2131624505);
                                                                                                                    } else {
                                                                                                                        formatString = LocaleController.formatString("ChannelMessageSticker", 2131624933, strArr[0]);
                                                                                                                        string = LocaleController.getString("AttachSticker", 2131624505);
                                                                                                                    }
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '#':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageAudio", 2131624920, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachAudio", 2131624476);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '$':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageContact2", 2131624921, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachContact", 2131624478);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '%':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageQuiz2", 2131624931, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("QuizPoll", 2131627823);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '&':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessagePoll2", 2131624930, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("Poll", 2131627577);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\'':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageMap", 2131624926, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachLocation", 2131624492);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '(':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageLiveLocation", 2131624925, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachLiveLocation", 2131624488);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case ')':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageGIF", 2131624924, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachGif", 2131624483);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '*':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGame", 2131626974, strArr[0]);
                                                                                                                    string = LocaleController.getString("AttachGame", 2131624482);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '+':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageFew", 2131624923, strArr[0], LocaleController.formatPluralString("ForwardedMessageCount", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]).toLowerCase());
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case ',':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageFew", 2131624923, strArr[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '-':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageFew", 2131624923, strArr[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '.':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageFew", 2131624923, strArr[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '/':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageFew", 2131624923, strArr[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[1]).intValue(), new Object[0]));
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '0':
                                                                                                                    formatString = LocaleController.formatString("ChannelMessageAlbum", 2131624919, strArr[0]);
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '1':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupText", 2131626994, strArr[0], strArr[1], strArr[2]);
                                                                                                                    string = strArr[2];
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '2':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupNoText", 2131626987, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("Message", 2131626622);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '3':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupPhoto", 2131626988, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachPhoto", 2131624502);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '4':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupVideo", 2131626995, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachVideo", 2131624508);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '5':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupRound", 2131626991, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachRound", 2131624504);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '6':
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupDocument", 2131626979, strArr[0], strArr[1]);
                                                                                                                    string = LocaleController.getString("AttachDocument", 2131624481);
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '7':
                                                                                                                    if (strArr.length > 2 && !TextUtils.isEmpty(strArr[2])) {
                                                                                                                        formatString = LocaleController.formatString("NotificationMessageGroupStickerEmoji", 2131626993, strArr[0], strArr[1], strArr[2]);
                                                                                                                        string = strArr[2] + " " + LocaleController.getString("AttachSticker", 2131624505);
                                                                                                                    } else {
                                                                                                                        formatString = LocaleController.formatString("NotificationMessageGroupSticker", 2131626992, strArr[0], strArr[1]);
                                                                                                                        string = strArr[1] + " " + LocaleController.getString("AttachSticker", 2131624505);
                                                                                                                    }
                                                                                                                    str12 = string;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '8':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupAudio", 2131626977, strArr[0], strArr[1]);
                                                                                                                    str13 = LocaleController.getString("AttachAudio", 2131624476);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '9':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupContact2", 2131626978, strArr[0], strArr[1], strArr[2]);
                                                                                                                    str13 = LocaleController.getString("AttachContact", 2131624478);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case ':':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupQuiz2", 2131626990, strArr[0], strArr[1], strArr[2]);
                                                                                                                    str13 = LocaleController.getString("PollQuiz", 2131627584);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case ';':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupPoll2", 2131626989, strArr[0], strArr[1], strArr[2]);
                                                                                                                    str13 = LocaleController.getString("Poll", 2131627577);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '<':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupMap", 2131626985, strArr[0], strArr[1]);
                                                                                                                    str13 = LocaleController.getString("AttachLocation", 2131624492);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '=':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupLiveLocation", 2131626984, strArr[0], strArr[1]);
                                                                                                                    str13 = LocaleController.getString("AttachLiveLocation", 2131624488);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '>':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupGif", 2131626982, strArr[0], strArr[1]);
                                                                                                                    str13 = LocaleController.getString("AttachGif", 2131624483);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '?':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupGame", 2131626980, strArr[0], strArr[1], strArr[2]);
                                                                                                                    str13 = LocaleController.getString("AttachGame", 2131624482);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '@':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationMessageGroupGameScored", 2131626981, strArr[0], strArr[1], strArr[2], strArr[3]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'A':
                                                                                                                    j11 = j4;
                                                                                                                    formatString = LocaleController.formatString("NotificationMessageGroupInvoice", 2131626983, strArr[0], strArr[1], strArr[2]);
                                                                                                                    str13 = LocaleController.getString("PaymentInvoice", 2131627393);
                                                                                                                    str12 = str13;
                                                                                                                    j4 = j11;
                                                                                                                    z14 = false;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'B':
                                                                                                                case 'C':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationInvitedToGroup", 2131626966, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'D':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationEditedGroupName", 2131626946, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'E':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationEditedGroupPhoto", 2131626947, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'F':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupAddMember", 2131626949, strArr[0], strArr[1], strArr[2]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'G':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupCreatedCall", 2131626953, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'H':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupInvitedToCall", 2131626957, strArr[0], strArr[1], strArr[2]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'I':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupEndedCall", 2131626954, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'J':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupInvitedYouToCall", 2131626958, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'K':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupKickMember", 2131626959, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'L':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupKickYou", 2131626960, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'M':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupLeftMember", 2131626961, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'N':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupAddSelf", 2131626950, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'O':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("NotificationGroupAddSelfMega", 2131626951, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'P':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = LocaleController.formatString("UserAcceptedToGroupPushWithGroup", 2131628800, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'Q':
                                                                                                                    j13 = j4;
                                                                                                                    formatString3 = LocaleController.formatString("NotificationGroupForwardedFew", 2131626956, strArr[0], strArr[1], LocaleController.formatPluralString(str6, Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                    reactedText = formatString3;
                                                                                                                    j4 = j13;
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'R':
                                                                                                                    j13 = j4;
                                                                                                                    formatString3 = LocaleController.formatString("NotificationGroupFew", 2131626955, strArr[0], strArr[1], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                    reactedText = formatString3;
                                                                                                                    j4 = j13;
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'S':
                                                                                                                    j13 = j4;
                                                                                                                    formatString3 = LocaleController.formatString("NotificationGroupFew", 2131626955, strArr[0], strArr[1], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                    reactedText = formatString3;
                                                                                                                    j4 = j13;
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'T':
                                                                                                                    j13 = j4;
                                                                                                                    formatString3 = LocaleController.formatString("NotificationGroupFew", 2131626955, strArr[0], strArr[1], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                    reactedText = formatString3;
                                                                                                                    j4 = j13;
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'U':
                                                                                                                    j13 = j4;
                                                                                                                    formatString3 = LocaleController.formatString("NotificationGroupFew", 2131626955, strArr[0], strArr[1], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) strArr[2]).intValue(), new Object[0]));
                                                                                                                    reactedText = formatString3;
                                                                                                                    j4 = j13;
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'V':
                                                                                                                    j13 = j4;
                                                                                                                    formatString3 = LocaleController.formatString("NotificationGroupAlbum", 2131626952, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString3;
                                                                                                                    j4 = j13;
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'W':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedTextUser", 2131626937, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedText", 2131626935, strArr[0], strArr[1], strArr[2]) : LocaleController.formatString("NotificationActionPinnedTextChannel", 2131626936, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'X':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedNoTextUser", 2131626916, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedNoText", 2131626914, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131626915, strArr[0]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'Y':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedPhotoUser", 2131626919, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedPhoto", 2131626917, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedPhotoChannel", 2131626918, strArr[0]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'Z':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedVideoUser", 2131626940, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedVideo", 2131626938, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedVideoChannel", 2131626939, strArr[0]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '[':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedRoundUser", 2131626928, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedRound", 2131626926, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedRoundChannel", 2131626927, strArr[0]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '\\':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedFileUser", 2131626892, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedFile", 2131626890, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedFileChannel", 2131626891, strArr[0]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case ']':
                                                                                                                    j12 = j4;
                                                                                                                    if (j12 > 0) {
                                                                                                                        formatString2 = (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) ? LocaleController.formatString("NotificationActionPinnedStickerUser", 2131626934, strArr[0]) : LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", 2131626933, strArr[0], strArr[1]);
                                                                                                                    } else if (z5) {
                                                                                                                        formatString2 = (strArr.length <= 2 || TextUtils.isEmpty(strArr[2])) ? LocaleController.formatString("NotificationActionPinnedSticker", 2131626929, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedStickerEmoji", 2131626931, strArr[0], strArr[2], strArr[1]);
                                                                                                                    } else {
                                                                                                                        formatString2 = (strArr.length <= 1 || TextUtils.isEmpty(strArr[1])) ? LocaleController.formatString("NotificationActionPinnedStickerChannel", 2131626930, strArr[0]) : LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", 2131626932, strArr[0], strArr[1]);
                                                                                                                    }
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '^':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedVoiceUser", 2131626943, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedVoice", 2131626941, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedVoiceChannel", 2131626942, strArr[0]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '_':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedContactUser", 2131626889, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedContact2", 2131626887, strArr[0], strArr[2], strArr[1]) : LocaleController.formatString("NotificationActionPinnedContactChannel2", 2131626888, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case '`':
                                                                                                                    j12 = j4;
                                                                                                                    formatString2 = j12 > 0 ? LocaleController.formatString("NotificationActionPinnedQuizUser", 2131626925, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedQuiz2", 2131626923, strArr[0], strArr[2], strArr[1]) : LocaleController.formatString("NotificationActionPinnedQuizChannel2", 2131626924, strArr[0], strArr[1]);
                                                                                                                    reactedText = formatString2;
                                                                                                                    j4 = j12;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'a':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedPollUser", 2131626922, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedPoll2", 2131626920, strArr[0], strArr[2], strArr[1]) : LocaleController.formatString("NotificationActionPinnedPollChannel2", 2131626921, strArr[0], strArr[1]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'b':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedGeoUser", 2131626904, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedGeo", 2131626899, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedGeoChannel", 2131626900, strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'c':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedGeoLiveUser", 2131626903, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedGeoLive", 2131626901, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", 2131626902, strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'd':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedGameUser", 2131626898, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedGame", 2131626893, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedGameChannel", 2131626894, strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'e':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedGameScoreUser", 2131626897, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedGameScore", 2131626895, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedGameScoreChannel", 2131626896, strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'f':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedInvoiceUser", 2131626910, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedInvoice", 2131626908, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedInvoiceChannel", 2131626909, strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'g':
                                                                                                                    formatString = j4 > 0 ? LocaleController.formatString("NotificationActionPinnedGifUser", 2131626907, strArr[0], strArr[1]) : z5 ? LocaleController.formatString("NotificationActionPinnedGif", 2131626905, strArr[0], strArr[1]) : LocaleController.formatString("NotificationActionPinnedGifChannel", 2131626906, strArr[0]);
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                case 'h':
                                                                                                                    formatString = LocaleController.getString("YouHaveNewMessage", 2131629269);
                                                                                                                    z14 = true;
                                                                                                                    str12 = null;
                                                                                                                    str8 = LocaleController.getString("SecretChatName", 2131628146);
                                                                                                                    reactedText = formatString;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
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
                                                                                                                    reactedText = null;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                                default:
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                        FileLog.w("unhandled loc_key = " + str4);
                                                                                                                    }
                                                                                                                    reactedText = null;
                                                                                                                    z14 = false;
                                                                                                                    str12 = null;
                                                                                                                    gcmPushListenerService = this;
                                                                                                                    if (reactedText != null) {
                                                                                                                    }
                                                                                                                    z2 = true;
                                                                                                                    if (z2) {
                                                                                                                    }
                                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                                    return;
                                                                                                            }
                                                                                                        } else {
                                                                                                            gcmPushListenerService = this;
                                                                                                            str10 = "REACT_";
                                                                                                            str9 = str7;
                                                                                                            z13 = z10;
                                                                                                            str11 = "CHAT_REACT_";
                                                                                                        }
                                                                                                        reactedText = gcmPushListenerService.getReactedText(str4, strArr);
                                                                                                        z14 = false;
                                                                                                        str12 = null;
                                                                                                        if (reactedText != null) {
                                                                                                        }
                                                                                                        z2 = true;
                                                                                                        if (z2) {
                                                                                                        }
                                                                                                        ConnectionsManager.onInternalPushReceived(i4);
                                                                                                        ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                        return;
                                                                                                    } catch (Throwable th16) {
                                                                                                        th3 = th16;
                                                                                                        str2 = str4;
                                                                                                        str = str5;
                                                                                                        i = i4;
                                                                                                        i2 = -1;
                                                                                                        th = th3;
                                                                                                        if (i == i2) {
                                                                                                        }
                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                        }
                                                                                                        FileLog.e(th);
                                                                                                        return;
                                                                                                    }
                                                                                                }
                                                                                                str7 = str15;
                                                                                                z9 = j19 != 0;
                                                                                                str15 = strArr[1];
                                                                                                z11 = false;
                                                                                                z10 = false;
                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                }
                                                                                                if (str4.startsWith("REACT_")) {
                                                                                                }
                                                                                                reactedText = gcmPushListenerService.getReactedText(str4, strArr);
                                                                                                z14 = false;
                                                                                                str12 = null;
                                                                                                if (reactedText != null) {
                                                                                                }
                                                                                                z2 = true;
                                                                                                if (z2) {
                                                                                                }
                                                                                                ConnectionsManager.onInternalPushReceived(i4);
                                                                                                ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                return;
                                                                                            } else if (!str4.startsWith("PINNED_")) {
                                                                                                if (str4.startsWith("CHANNEL_")) {
                                                                                                    z11 = false;
                                                                                                    str7 = null;
                                                                                                    z10 = true;
                                                                                                    z9 = false;
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                    }
                                                                                                    if (str4.startsWith("REACT_")) {
                                                                                                    }
                                                                                                    reactedText = gcmPushListenerService.getReactedText(str4, strArr);
                                                                                                    z14 = false;
                                                                                                    str12 = null;
                                                                                                    if (reactedText != null) {
                                                                                                    }
                                                                                                    z2 = true;
                                                                                                    if (z2) {
                                                                                                    }
                                                                                                    ConnectionsManager.onInternalPushReceived(i4);
                                                                                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                    return;
                                                                                                }
                                                                                                z11 = false;
                                                                                                str7 = null;
                                                                                                z10 = false;
                                                                                                z9 = false;
                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                }
                                                                                                if (str4.startsWith("REACT_")) {
                                                                                                }
                                                                                                reactedText = gcmPushListenerService.getReactedText(str4, strArr);
                                                                                                z14 = false;
                                                                                                str12 = null;
                                                                                                if (reactedText != null) {
                                                                                                }
                                                                                                z2 = true;
                                                                                                if (z2) {
                                                                                                }
                                                                                                ConnectionsManager.onInternalPushReceived(i4);
                                                                                                ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                return;
                                                                                            } else {
                                                                                                z9 = j19 != 0;
                                                                                                z11 = true;
                                                                                                str7 = null;
                                                                                                z10 = false;
                                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                                }
                                                                                                if (str4.startsWith("REACT_")) {
                                                                                                }
                                                                                                reactedText = gcmPushListenerService.getReactedText(str4, strArr);
                                                                                                z14 = false;
                                                                                                str12 = null;
                                                                                                if (reactedText != null) {
                                                                                                }
                                                                                                z2 = true;
                                                                                                if (z2) {
                                                                                                }
                                                                                                ConnectionsManager.onInternalPushReceived(i4);
                                                                                                ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                                                                                return;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    i4 = i11;
                                                                                    z7 = false;
                                                                                    boolean z182 = z7;
                                                                                    jSONObject4 = jSONObject3;
                                                                                    if (!jSONObject4.has("loc_args")) {
                                                                                    }
                                                                                    String str152 = strArr[0];
                                                                                    boolean has2 = jSONObject2.has("edit_date");
                                                                                    if (!str4.startsWith("CHAT_")) {
                                                                                    }
                                                                                }
                                                                            } catch (Throwable th17) {
                                                                                th3 = th17;
                                                                                i2 = -1;
                                                                                gcmPushListenerService = this;
                                                                                str2 = str4;
                                                                                i = i11;
                                                                                str = str5;
                                                                                th = th3;
                                                                                if (i == i2) {
                                                                                }
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                FileLog.e(th);
                                                                                return;
                                                                            }
                                                                        }
                                                                        z6 = false;
                                                                        if (jSONObject2.has("silent")) {
                                                                        }
                                                                        i4 = i11;
                                                                        z7 = false;
                                                                        boolean z1822 = z7;
                                                                        jSONObject4 = jSONObject3;
                                                                        if (!jSONObject4.has("loc_args")) {
                                                                        }
                                                                        String str1522 = strArr[0];
                                                                        boolean has22 = jSONObject2.has("edit_date");
                                                                        if (!str4.startsWith("CHAT_")) {
                                                                        }
                                                                    }
                                                                    boolean z18222 = z7;
                                                                    jSONObject4 = jSONObject3;
                                                                    if (!jSONObject4.has("loc_args")) {
                                                                    }
                                                                    String str15222 = strArr[0];
                                                                    boolean has222 = jSONObject2.has("edit_date");
                                                                    if (!str4.startsWith("CHAT_")) {
                                                                    }
                                                                } catch (Throwable th18) {
                                                                    th3 = th18;
                                                                    gcmPushListenerService = this;
                                                                    str2 = str4;
                                                                    str = str5;
                                                                    i = i4;
                                                                    i2 = -1;
                                                                    th = th3;
                                                                    if (i == i2) {
                                                                    }
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                    }
                                                                    FileLog.e(th);
                                                                    return;
                                                                }
                                                                z5 = true;
                                                                if (jSONObject2.has("mention")) {
                                                                }
                                                                z6 = false;
                                                                if (jSONObject2.has("silent")) {
                                                                }
                                                                i4 = i11;
                                                                z7 = false;
                                                            } catch (Throwable th19) {
                                                                th3 = th19;
                                                                gcmPushListenerService = this;
                                                                i4 = i11;
                                                            }
                                                        } else {
                                                            gcmPushListenerService = this;
                                                        }
                                                    } catch (Throwable th20) {
                                                        th3 = th20;
                                                        gcmPushListenerService = this;
                                                        str2 = str4;
                                                        str = str5;
                                                        i2 = -1;
                                                        th = th3;
                                                        if (i == i2) {
                                                        }
                                                        if (BuildVars.LOGS_ENABLED) {
                                                        }
                                                        FileLog.e(th);
                                                        return;
                                                    }
                                                } catch (Throwable th21) {
                                                    th3 = th21;
                                                    i2 = -1;
                                                    gcmPushListenerService = this;
                                                    str2 = str4;
                                                }
                                            }
                                        } catch (Throwable th22) {
                                            th3 = th22;
                                        }
                                    }
                                    i4 = i;
                                    z2 = true;
                                    if (z2) {
                                    }
                                    ConnectionsManager.onInternalPushReceived(i4);
                                    ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                                    return;
                                }
                            }
                            str5 = str;
                            i4 = i;
                            z2 = true;
                            if (z2) {
                            }
                            ConnectionsManager.onInternalPushReceived(i4);
                            ConnectionsManager.getInstance(i4).resumeNetworkMaybe();
                            return;
                        } catch (Throwable th23) {
                            th3 = th23;
                            str2 = str4;
                            i2 = -1;
                            th = th3;
                            if (i == i2) {
                            }
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            FileLog.e(th);
                            return;
                        }
                    } catch (Throwable th24) {
                        th3 = th24;
                        str4 = str3;
                    }
                }
                str2 = str4;
                str = str5;
                i2 = -1;
                th = th3;
                if (i == i2) {
                    ConnectionsManager.onInternalPushReceived(i);
                    ConnectionsManager.getInstance(i).resumeNetworkMaybe();
                    gcmPushListenerService.countDownLatch.countDown();
                } else {
                    onDecryptError();
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("error in loc_key = " + str2 + " json " + str);
                }
                FileLog.e(th);
                return;
            }
        }
        jSONObject3 = jSONObject;
        j2 = longValue;
        i = UserConfig.selectedAccount;
        i3 = 0;
        while (true) {
            if (i3 < 4) {
            }
            i3++;
        }
        if (z) {
        }
    }

    public static /* synthetic */ void lambda$onMessageReceived$0(int i, TLRPC$TL_updates tLRPC$TL_updates) {
        MessagesController.getInstance(i).processUpdates(tLRPC$TL_updates, false);
    }

    public static /* synthetic */ void lambda$onMessageReceived$1(int i) {
        if (UserConfig.getInstance(i).getClientUserId() != 0) {
            UserConfig.getInstance(i).clearConfig();
            MessagesController.getInstance(i).performLogout(0);
        }
    }

    public static /* synthetic */ void lambda$onMessageReceived$2(int i) {
        LocationController.getInstance(i).setNewLocationEndWatchTime();
    }

    private String getReactedText(String str, Object[] objArr) {
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
                return LocaleController.formatString("PushChatReactContact", 2131627779, objArr);
            case 1:
                return LocaleController.formatString("PushReactGeoLocation", 2131627799, objArr);
            case 2:
                return LocaleController.formatString("PushChatReactNotext", 2131627786, objArr);
            case 3:
                return LocaleController.formatString("PushReactNoText", 2131627802, objArr);
            case 4:
                return LocaleController.formatString("PushChatReactInvoice", 2131627785, objArr);
            case 5:
                return LocaleController.formatString("PushReactContect", 2131627795, objArr);
            case 6:
                return LocaleController.formatString("PushChatReactSticker", 2131627791, objArr);
            case 7:
                return LocaleController.formatString("PushReactGame", 2131627797, objArr);
            case '\b':
                return LocaleController.formatString("PushReactPoll", 2131627804, objArr);
            case '\t':
                return LocaleController.formatString("PushReactQuiz", 2131627805, objArr);
            case '\n':
                return LocaleController.formatString("PushReactText", 2131627808, objArr);
            case 11:
                return LocaleController.formatString("PushReactInvoice", 2131627801, objArr);
            case '\f':
                return LocaleController.formatString("PushChatReactDoc", 2131627780, objArr);
            case '\r':
                return LocaleController.formatString("PushChatReactGeo", 2131627782, objArr);
            case 14:
                return LocaleController.formatString("PushChatReactGif", 2131627784, objArr);
            case 15:
                return LocaleController.formatString("PushReactSticker", 2131627807, objArr);
            case 16:
                return LocaleController.formatString("PushChatReactAudio", 2131627778, objArr);
            case 17:
                return LocaleController.formatString("PushChatReactPhoto", 2131627787, objArr);
            case 18:
                return LocaleController.formatString("PushChatReactRound", 2131627790, objArr);
            case 19:
                return LocaleController.formatString("PushChatReactVideo", 2131627793, objArr);
            case 20:
                return LocaleController.formatString("PushChatReactGeoLive", 2131627783, objArr);
            case 21:
                return LocaleController.formatString("PushReactAudio", 2131627794, objArr);
            case 22:
                return LocaleController.formatString("PushReactPhoto", 2131627803, objArr);
            case 23:
                return LocaleController.formatString("PushReactRound", 2131627806, objArr);
            case 24:
                return LocaleController.formatString("PushReactVideo", 2131627809, objArr);
            case 25:
                return LocaleController.formatString("PushReactDoc", 2131627796, objArr);
            case 26:
                return LocaleController.formatString("PushReactGeo", 2131627798, objArr);
            case 27:
                return LocaleController.formatString("PushReactGif", 2131627800, objArr);
            case 28:
                return LocaleController.formatString("PushChatReactGame", 2131627781, objArr);
            case 29:
                return LocaleController.formatString("PushChatReactPoll", 2131627788, objArr);
            case 30:
                return LocaleController.formatString("PushChatReactQuiz", 2131627789, objArr);
            case 31:
                return LocaleController.formatString("PushChatReactText", 2131627792, objArr);
            default:
                return null;
        }
    }

    private void onDecryptError() {
        for (int i = 0; i < 4; i++) {
            if (UserConfig.getInstance(i).isClientActivated()) {
                ConnectionsManager.onInternalPushReceived(i);
                ConnectionsManager.getInstance(i).resumeNetworkMaybe();
            }
        }
        this.countDownLatch.countDown();
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String str) {
        AndroidUtilities.runOnUIThread(new GcmPushListenerService$$ExternalSyntheticLambda5(str));
    }

    public static /* synthetic */ void lambda$onNewToken$5(String str) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Refreshed token: " + str);
        }
        ApplicationLoader.postInitApplication();
        sendRegistrationToServer(str);
    }

    public static void sendRegistrationToServer(String str) {
        Utilities.stageQueue.postRunnable(new GcmPushListenerService$$ExternalSyntheticLambda4(str));
    }

    public static /* synthetic */ void lambda$sendRegistrationToServer$9(String str) {
        boolean z;
        ConnectionsManager.setRegId(str, SharedConfig.pushStringStatus);
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
        for (int i = 0; i < 4; i++) {
            UserConfig userConfig = UserConfig.getInstance(i);
            userConfig.registeredForPush = false;
            userConfig.saveConfig(false);
            if (userConfig.getClientUserId() != 0) {
                if (z) {
                    TLRPC$TL_help_saveAppLog tLRPC$TL_help_saveAppLog = new TLRPC$TL_help_saveAppLog();
                    TLRPC$TL_inputAppEvent tLRPC$TL_inputAppEvent = new TLRPC$TL_inputAppEvent();
                    tLRPC$TL_inputAppEvent.time = SharedConfig.pushStringGetTimeStart;
                    tLRPC$TL_inputAppEvent.type = "fcm_token_request";
                    tLRPC$TL_inputAppEvent.peer = 0L;
                    tLRPC$TL_inputAppEvent.data = new TLRPC$TL_jsonNull();
                    tLRPC$TL_help_saveAppLog.events.add(tLRPC$TL_inputAppEvent);
                    TLRPC$TL_inputAppEvent tLRPC$TL_inputAppEvent2 = new TLRPC$TL_inputAppEvent();
                    long j = SharedConfig.pushStringGetTimeEnd;
                    tLRPC$TL_inputAppEvent2.time = j;
                    tLRPC$TL_inputAppEvent2.type = "fcm_token_response";
                    tLRPC$TL_inputAppEvent2.peer = j - SharedConfig.pushStringGetTimeStart;
                    tLRPC$TL_inputAppEvent2.data = new TLRPC$TL_jsonNull();
                    tLRPC$TL_help_saveAppLog.events.add(tLRPC$TL_inputAppEvent2);
                    ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_help_saveAppLog, GcmPushListenerService$$ExternalSyntheticLambda9.INSTANCE);
                    z = false;
                }
                AndroidUtilities.runOnUIThread(new GcmPushListenerService$$ExternalSyntheticLambda2(i, str));
            }
        }
    }

    public static /* synthetic */ void lambda$sendRegistrationToServer$7(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new GcmPushListenerService$$ExternalSyntheticLambda8(tLRPC$TL_error));
    }

    public static /* synthetic */ void lambda$sendRegistrationToServer$6(TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            SharedConfig.pushStatSent = true;
            SharedConfig.saveConfig();
        }
    }

    public static /* synthetic */ void lambda$sendRegistrationToServer$8(int i, String str) {
        MessagesController.getInstance(i).registerForPush(str);
    }
}
