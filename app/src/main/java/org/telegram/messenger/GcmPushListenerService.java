package org.telegram.messenger;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import androidx.collection.LongSparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.text.HtmlCompat;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatReactionsEditActivity;
import org.telegram.ui.Components.UndoView;
/* loaded from: classes.dex */
public class GcmPushListenerService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 1;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        final Map data = message.getData();
        final long time = message.getSentTime();
        long receiveTime = SystemClock.elapsedRealtime();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("GCM received data: " + data + " from: " + from);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                GcmPushListenerService.this.m281x1d2d684b(data, time);
            }
        });
        try {
            this.countDownLatch.await();
        } catch (Throwable th) {
        }
        if (BuildVars.DEBUG_VERSION) {
            FileLog.d("finished GCM service, time = " + (SystemClock.elapsedRealtime() - receiveTime));
        }
    }

    /* renamed from: lambda$onMessageReceived$4$org-telegram-messenger-GcmPushListenerService */
    public /* synthetic */ void m281x1d2d684b(final Map data, final long time) {
        ApplicationLoader.postInitApplication();
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                GcmPushListenerService.this.m280xa7b3420a(data, time);
            }
        });
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:257:0x05f8 A[Catch: all -> 0x0354, TRY_ENTER, TryCatch #9 {all -> 0x0354, blocks: (B:142:0x0344, B:150:0x0374, B:155:0x038c, B:163:0x03a7, B:165:0x03af, B:172:0x03c5, B:174:0x03d4, B:178:0x03fd, B:179:0x040b, B:181:0x0416, B:182:0x0422, B:183:0x042d, B:184:0x0432, B:191:0x0464, B:192:0x0480, B:194:0x0485, B:195:0x0497, B:197:0x04b8, B:204:0x0506, B:208:0x0516, B:212:0x052a, B:214:0x053e, B:216:0x0561, B:223:0x0577, B:230:0x058d, B:243:0x05ca, B:250:0x05df, B:257:0x05f8, B:258:0x060c, B:260:0x060f, B:266:0x0640, B:268:0x0646, B:273:0x0679, B:290:0x06cd, B:295:0x06fe, B:298:0x070f, B:299:0x0713, B:301:0x0718, B:304:0x0724, B:307:0x0730, B:310:0x073c, B:313:0x0748, B:316:0x0754, B:319:0x0760, B:322:0x076c, B:325:0x0778, B:328:0x0784, B:331:0x0790, B:334:0x079c, B:337:0x07a8, B:340:0x07b4, B:343:0x07c0, B:346:0x07cc, B:349:0x07d8, B:352:0x07e4, B:355:0x07f0, B:358:0x07fc, B:361:0x0807, B:364:0x0813, B:367:0x081f, B:370:0x082b, B:373:0x0837, B:376:0x0843, B:379:0x084f, B:382:0x085b, B:385:0x0867, B:388:0x0873, B:391:0x087e, B:394:0x088a, B:397:0x0896, B:400:0x08a2, B:403:0x08ae, B:406:0x08ba, B:409:0x08c6, B:412:0x08d2, B:415:0x08de, B:418:0x08ea, B:421:0x08f6, B:424:0x0902, B:427:0x090e, B:430:0x091a, B:433:0x0926, B:436:0x0932, B:439:0x093e, B:442:0x094a, B:445:0x0956, B:448:0x0961, B:451:0x096d, B:454:0x0979, B:457:0x0985, B:460:0x0991, B:463:0x099d, B:466:0x09a9, B:469:0x09b5, B:472:0x09c1, B:475:0x09cd, B:478:0x09d9, B:481:0x09e5, B:484:0x09f1, B:487:0x09fd, B:490:0x0a09, B:493:0x0a15, B:496:0x0a21, B:499:0x0a2d, B:502:0x0a39, B:505:0x0a45, B:508:0x0a51, B:511:0x0a5d, B:514:0x0a69, B:517:0x0a75, B:520:0x0a81, B:523:0x0a8d, B:526:0x0a99, B:529:0x0aa5, B:532:0x0ab1, B:535:0x0abd, B:538:0x0ac9, B:541:0x0ad5, B:544:0x0ae1, B:547:0x0aed, B:550:0x0af9, B:553:0x0b05, B:556:0x0b11, B:559:0x0b1c, B:562:0x0b27, B:565:0x0b33, B:568:0x0b3f, B:571:0x0b4b, B:574:0x0b57, B:577:0x0b63, B:580:0x0b6f, B:583:0x0b7b, B:586:0x0b87, B:589:0x0b93, B:592:0x0b9e, B:595:0x0baa, B:598:0x0bb5, B:601:0x0bc1, B:604:0x0bcd, B:607:0x0bd8, B:610:0x0be4, B:613:0x0bf0, B:616:0x0bfc, B:619:0x0c08, B:622:0x0c13, B:625:0x0c1e, B:628:0x0c29, B:631:0x0c34, B:634:0x0c3f, B:637:0x0c4a, B:640:0x0c55, B:643:0x0c60, B:650:0x0c84, B:653:0x0c8e, B:656:0x0cb1, B:658:0x0cd3, B:659:0x0cf3, B:662:0x0d14, B:664:0x0d36, B:665:0x0d56, B:668:0x0d77, B:670:0x0d99, B:671:0x0db9, B:674:0x0dda, B:676:0x0dfc, B:677:0x0e1c, B:680:0x0e3d, B:682:0x0e5f, B:683:0x0e7f, B:686:0x0ea0, B:688:0x0ec2, B:689:0x0ee2, B:692:0x0f03, B:694:0x0f25, B:695:0x0f4a, B:698:0x0f70, B:700:0x0f92, B:701:0x0fb7, B:704:0x0fdd, B:706:0x0fff, B:707:0x1024, B:710:0x104a, B:712:0x106c, B:713:0x108c, B:716:0x10ad, B:718:0x10b1, B:720:0x10b9, B:721:0x10d9, B:723:0x10f6, B:725:0x10fa, B:727:0x1102, B:728:0x1127, B:729:0x1147, B:731:0x114b, B:733:0x1153, B:734:0x1173, B:737:0x1194, B:739:0x11b6, B:740:0x11d6, B:743:0x11f7, B:745:0x1219, B:746:0x1239, B:749:0x125a, B:751:0x127c, B:752:0x129c, B:755:0x12bd, B:757:0x12df, B:758:0x12ff, B:761:0x1320, B:763:0x1342, B:764:0x1362, B:767:0x1383, B:769:0x13a5, B:770:0x13ca, B:771:0x13ea, B:772:0x140c, B:773:0x1443, B:774:0x147a, B:775:0x14b1, B:776:0x14e6, B:777:0x151d, B:778:0x153d, B:779:0x155d, B:780:0x157d, B:781:0x159d, B:782:0x15bd, B:783:0x15dd, B:784:0x15fd, B:785:0x161d, B:786:0x1642, B:787:0x1662, B:788:0x1687, B:789:0x16a7, B:790:0x16c7, B:791:0x16e7, B:792:0x1717, B:793:0x1741, B:794:0x1771, B:795:0x179c, B:796:0x17c7, B:797:0x17f2, B:798:0x1822, B:799:0x1852, B:800:0x1882, B:801:0x18ad, B:803:0x18b1, B:805:0x18b9, B:806:0x18fc, B:807:0x193a, B:808:0x1965, B:809:0x1990, B:810:0x19bb, B:811:0x19e6, B:812:0x1a11, B:813:0x1a3a, B:814:0x1a57, B:815:0x1a89, B:816:0x1abb, B:817:0x1aed, B:818:0x1b1d, B:819:0x1b53, B:820:0x1b79, B:821:0x1b9f, B:822:0x1bc5, B:823:0x1beb, B:824:0x1c16, B:825:0x1c41, B:826:0x1c6c, B:827:0x1c92, B:829:0x1c96, B:831:0x1c9e, B:832:0x1cdc, B:833:0x1d00, B:834:0x1d26, B:835:0x1d4c, B:836:0x1d72, B:837:0x1d98, B:838:0x1dbe, B:839:0x1ddb, B:840:0x1e0d, B:841:0x1e3f, B:842:0x1e71, B:843:0x1ea1, B:844:0x1ed3, B:845:0x1efe, B:846:0x1f23, B:847:0x1f4e, B:848:0x1f74, B:849:0x1f9a, B:850:0x1fc0, B:851:0x1feb, B:852:0x2016, B:853:0x2041, B:854:0x2067, B:856:0x206b, B:858:0x2073, B:859:0x20b1, B:860:0x20d5, B:861:0x20fb, B:862:0x2121, B:863:0x213d, B:864:0x2163, B:865:0x2189, B:866:0x21af, B:867:0x21d5, B:868:0x21fb, B:869:0x221e, B:871:0x2249), top: B:977:0x0344 }] */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0619  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0640 A[Catch: all -> 0x0354, TRY_ENTER, TryCatch #9 {all -> 0x0354, blocks: (B:142:0x0344, B:150:0x0374, B:155:0x038c, B:163:0x03a7, B:165:0x03af, B:172:0x03c5, B:174:0x03d4, B:178:0x03fd, B:179:0x040b, B:181:0x0416, B:182:0x0422, B:183:0x042d, B:184:0x0432, B:191:0x0464, B:192:0x0480, B:194:0x0485, B:195:0x0497, B:197:0x04b8, B:204:0x0506, B:208:0x0516, B:212:0x052a, B:214:0x053e, B:216:0x0561, B:223:0x0577, B:230:0x058d, B:243:0x05ca, B:250:0x05df, B:257:0x05f8, B:258:0x060c, B:260:0x060f, B:266:0x0640, B:268:0x0646, B:273:0x0679, B:290:0x06cd, B:295:0x06fe, B:298:0x070f, B:299:0x0713, B:301:0x0718, B:304:0x0724, B:307:0x0730, B:310:0x073c, B:313:0x0748, B:316:0x0754, B:319:0x0760, B:322:0x076c, B:325:0x0778, B:328:0x0784, B:331:0x0790, B:334:0x079c, B:337:0x07a8, B:340:0x07b4, B:343:0x07c0, B:346:0x07cc, B:349:0x07d8, B:352:0x07e4, B:355:0x07f0, B:358:0x07fc, B:361:0x0807, B:364:0x0813, B:367:0x081f, B:370:0x082b, B:373:0x0837, B:376:0x0843, B:379:0x084f, B:382:0x085b, B:385:0x0867, B:388:0x0873, B:391:0x087e, B:394:0x088a, B:397:0x0896, B:400:0x08a2, B:403:0x08ae, B:406:0x08ba, B:409:0x08c6, B:412:0x08d2, B:415:0x08de, B:418:0x08ea, B:421:0x08f6, B:424:0x0902, B:427:0x090e, B:430:0x091a, B:433:0x0926, B:436:0x0932, B:439:0x093e, B:442:0x094a, B:445:0x0956, B:448:0x0961, B:451:0x096d, B:454:0x0979, B:457:0x0985, B:460:0x0991, B:463:0x099d, B:466:0x09a9, B:469:0x09b5, B:472:0x09c1, B:475:0x09cd, B:478:0x09d9, B:481:0x09e5, B:484:0x09f1, B:487:0x09fd, B:490:0x0a09, B:493:0x0a15, B:496:0x0a21, B:499:0x0a2d, B:502:0x0a39, B:505:0x0a45, B:508:0x0a51, B:511:0x0a5d, B:514:0x0a69, B:517:0x0a75, B:520:0x0a81, B:523:0x0a8d, B:526:0x0a99, B:529:0x0aa5, B:532:0x0ab1, B:535:0x0abd, B:538:0x0ac9, B:541:0x0ad5, B:544:0x0ae1, B:547:0x0aed, B:550:0x0af9, B:553:0x0b05, B:556:0x0b11, B:559:0x0b1c, B:562:0x0b27, B:565:0x0b33, B:568:0x0b3f, B:571:0x0b4b, B:574:0x0b57, B:577:0x0b63, B:580:0x0b6f, B:583:0x0b7b, B:586:0x0b87, B:589:0x0b93, B:592:0x0b9e, B:595:0x0baa, B:598:0x0bb5, B:601:0x0bc1, B:604:0x0bcd, B:607:0x0bd8, B:610:0x0be4, B:613:0x0bf0, B:616:0x0bfc, B:619:0x0c08, B:622:0x0c13, B:625:0x0c1e, B:628:0x0c29, B:631:0x0c34, B:634:0x0c3f, B:637:0x0c4a, B:640:0x0c55, B:643:0x0c60, B:650:0x0c84, B:653:0x0c8e, B:656:0x0cb1, B:658:0x0cd3, B:659:0x0cf3, B:662:0x0d14, B:664:0x0d36, B:665:0x0d56, B:668:0x0d77, B:670:0x0d99, B:671:0x0db9, B:674:0x0dda, B:676:0x0dfc, B:677:0x0e1c, B:680:0x0e3d, B:682:0x0e5f, B:683:0x0e7f, B:686:0x0ea0, B:688:0x0ec2, B:689:0x0ee2, B:692:0x0f03, B:694:0x0f25, B:695:0x0f4a, B:698:0x0f70, B:700:0x0f92, B:701:0x0fb7, B:704:0x0fdd, B:706:0x0fff, B:707:0x1024, B:710:0x104a, B:712:0x106c, B:713:0x108c, B:716:0x10ad, B:718:0x10b1, B:720:0x10b9, B:721:0x10d9, B:723:0x10f6, B:725:0x10fa, B:727:0x1102, B:728:0x1127, B:729:0x1147, B:731:0x114b, B:733:0x1153, B:734:0x1173, B:737:0x1194, B:739:0x11b6, B:740:0x11d6, B:743:0x11f7, B:745:0x1219, B:746:0x1239, B:749:0x125a, B:751:0x127c, B:752:0x129c, B:755:0x12bd, B:757:0x12df, B:758:0x12ff, B:761:0x1320, B:763:0x1342, B:764:0x1362, B:767:0x1383, B:769:0x13a5, B:770:0x13ca, B:771:0x13ea, B:772:0x140c, B:773:0x1443, B:774:0x147a, B:775:0x14b1, B:776:0x14e6, B:777:0x151d, B:778:0x153d, B:779:0x155d, B:780:0x157d, B:781:0x159d, B:782:0x15bd, B:783:0x15dd, B:784:0x15fd, B:785:0x161d, B:786:0x1642, B:787:0x1662, B:788:0x1687, B:789:0x16a7, B:790:0x16c7, B:791:0x16e7, B:792:0x1717, B:793:0x1741, B:794:0x1771, B:795:0x179c, B:796:0x17c7, B:797:0x17f2, B:798:0x1822, B:799:0x1852, B:800:0x1882, B:801:0x18ad, B:803:0x18b1, B:805:0x18b9, B:806:0x18fc, B:807:0x193a, B:808:0x1965, B:809:0x1990, B:810:0x19bb, B:811:0x19e6, B:812:0x1a11, B:813:0x1a3a, B:814:0x1a57, B:815:0x1a89, B:816:0x1abb, B:817:0x1aed, B:818:0x1b1d, B:819:0x1b53, B:820:0x1b79, B:821:0x1b9f, B:822:0x1bc5, B:823:0x1beb, B:824:0x1c16, B:825:0x1c41, B:826:0x1c6c, B:827:0x1c92, B:829:0x1c96, B:831:0x1c9e, B:832:0x1cdc, B:833:0x1d00, B:834:0x1d26, B:835:0x1d4c, B:836:0x1d72, B:837:0x1d98, B:838:0x1dbe, B:839:0x1ddb, B:840:0x1e0d, B:841:0x1e3f, B:842:0x1e71, B:843:0x1ea1, B:844:0x1ed3, B:845:0x1efe, B:846:0x1f23, B:847:0x1f4e, B:848:0x1f74, B:849:0x1f9a, B:850:0x1fc0, B:851:0x1feb, B:852:0x2016, B:853:0x2041, B:854:0x2067, B:856:0x206b, B:858:0x2073, B:859:0x20b1, B:860:0x20d5, B:861:0x20fb, B:862:0x2121, B:863:0x213d, B:864:0x2163, B:865:0x2189, B:866:0x21af, B:867:0x21d5, B:868:0x21fb, B:869:0x221e, B:871:0x2249), top: B:977:0x0344 }] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x068a  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x06cb  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x06f2  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x06fe A[Catch: all -> 0x0354, TRY_ENTER, TryCatch #9 {all -> 0x0354, blocks: (B:142:0x0344, B:150:0x0374, B:155:0x038c, B:163:0x03a7, B:165:0x03af, B:172:0x03c5, B:174:0x03d4, B:178:0x03fd, B:179:0x040b, B:181:0x0416, B:182:0x0422, B:183:0x042d, B:184:0x0432, B:191:0x0464, B:192:0x0480, B:194:0x0485, B:195:0x0497, B:197:0x04b8, B:204:0x0506, B:208:0x0516, B:212:0x052a, B:214:0x053e, B:216:0x0561, B:223:0x0577, B:230:0x058d, B:243:0x05ca, B:250:0x05df, B:257:0x05f8, B:258:0x060c, B:260:0x060f, B:266:0x0640, B:268:0x0646, B:273:0x0679, B:290:0x06cd, B:295:0x06fe, B:298:0x070f, B:299:0x0713, B:301:0x0718, B:304:0x0724, B:307:0x0730, B:310:0x073c, B:313:0x0748, B:316:0x0754, B:319:0x0760, B:322:0x076c, B:325:0x0778, B:328:0x0784, B:331:0x0790, B:334:0x079c, B:337:0x07a8, B:340:0x07b4, B:343:0x07c0, B:346:0x07cc, B:349:0x07d8, B:352:0x07e4, B:355:0x07f0, B:358:0x07fc, B:361:0x0807, B:364:0x0813, B:367:0x081f, B:370:0x082b, B:373:0x0837, B:376:0x0843, B:379:0x084f, B:382:0x085b, B:385:0x0867, B:388:0x0873, B:391:0x087e, B:394:0x088a, B:397:0x0896, B:400:0x08a2, B:403:0x08ae, B:406:0x08ba, B:409:0x08c6, B:412:0x08d2, B:415:0x08de, B:418:0x08ea, B:421:0x08f6, B:424:0x0902, B:427:0x090e, B:430:0x091a, B:433:0x0926, B:436:0x0932, B:439:0x093e, B:442:0x094a, B:445:0x0956, B:448:0x0961, B:451:0x096d, B:454:0x0979, B:457:0x0985, B:460:0x0991, B:463:0x099d, B:466:0x09a9, B:469:0x09b5, B:472:0x09c1, B:475:0x09cd, B:478:0x09d9, B:481:0x09e5, B:484:0x09f1, B:487:0x09fd, B:490:0x0a09, B:493:0x0a15, B:496:0x0a21, B:499:0x0a2d, B:502:0x0a39, B:505:0x0a45, B:508:0x0a51, B:511:0x0a5d, B:514:0x0a69, B:517:0x0a75, B:520:0x0a81, B:523:0x0a8d, B:526:0x0a99, B:529:0x0aa5, B:532:0x0ab1, B:535:0x0abd, B:538:0x0ac9, B:541:0x0ad5, B:544:0x0ae1, B:547:0x0aed, B:550:0x0af9, B:553:0x0b05, B:556:0x0b11, B:559:0x0b1c, B:562:0x0b27, B:565:0x0b33, B:568:0x0b3f, B:571:0x0b4b, B:574:0x0b57, B:577:0x0b63, B:580:0x0b6f, B:583:0x0b7b, B:586:0x0b87, B:589:0x0b93, B:592:0x0b9e, B:595:0x0baa, B:598:0x0bb5, B:601:0x0bc1, B:604:0x0bcd, B:607:0x0bd8, B:610:0x0be4, B:613:0x0bf0, B:616:0x0bfc, B:619:0x0c08, B:622:0x0c13, B:625:0x0c1e, B:628:0x0c29, B:631:0x0c34, B:634:0x0c3f, B:637:0x0c4a, B:640:0x0c55, B:643:0x0c60, B:650:0x0c84, B:653:0x0c8e, B:656:0x0cb1, B:658:0x0cd3, B:659:0x0cf3, B:662:0x0d14, B:664:0x0d36, B:665:0x0d56, B:668:0x0d77, B:670:0x0d99, B:671:0x0db9, B:674:0x0dda, B:676:0x0dfc, B:677:0x0e1c, B:680:0x0e3d, B:682:0x0e5f, B:683:0x0e7f, B:686:0x0ea0, B:688:0x0ec2, B:689:0x0ee2, B:692:0x0f03, B:694:0x0f25, B:695:0x0f4a, B:698:0x0f70, B:700:0x0f92, B:701:0x0fb7, B:704:0x0fdd, B:706:0x0fff, B:707:0x1024, B:710:0x104a, B:712:0x106c, B:713:0x108c, B:716:0x10ad, B:718:0x10b1, B:720:0x10b9, B:721:0x10d9, B:723:0x10f6, B:725:0x10fa, B:727:0x1102, B:728:0x1127, B:729:0x1147, B:731:0x114b, B:733:0x1153, B:734:0x1173, B:737:0x1194, B:739:0x11b6, B:740:0x11d6, B:743:0x11f7, B:745:0x1219, B:746:0x1239, B:749:0x125a, B:751:0x127c, B:752:0x129c, B:755:0x12bd, B:757:0x12df, B:758:0x12ff, B:761:0x1320, B:763:0x1342, B:764:0x1362, B:767:0x1383, B:769:0x13a5, B:770:0x13ca, B:771:0x13ea, B:772:0x140c, B:773:0x1443, B:774:0x147a, B:775:0x14b1, B:776:0x14e6, B:777:0x151d, B:778:0x153d, B:779:0x155d, B:780:0x157d, B:781:0x159d, B:782:0x15bd, B:783:0x15dd, B:784:0x15fd, B:785:0x161d, B:786:0x1642, B:787:0x1662, B:788:0x1687, B:789:0x16a7, B:790:0x16c7, B:791:0x16e7, B:792:0x1717, B:793:0x1741, B:794:0x1771, B:795:0x179c, B:796:0x17c7, B:797:0x17f2, B:798:0x1822, B:799:0x1852, B:800:0x1882, B:801:0x18ad, B:803:0x18b1, B:805:0x18b9, B:806:0x18fc, B:807:0x193a, B:808:0x1965, B:809:0x1990, B:810:0x19bb, B:811:0x19e6, B:812:0x1a11, B:813:0x1a3a, B:814:0x1a57, B:815:0x1a89, B:816:0x1abb, B:817:0x1aed, B:818:0x1b1d, B:819:0x1b53, B:820:0x1b79, B:821:0x1b9f, B:822:0x1bc5, B:823:0x1beb, B:824:0x1c16, B:825:0x1c41, B:826:0x1c6c, B:827:0x1c92, B:829:0x1c96, B:831:0x1c9e, B:832:0x1cdc, B:833:0x1d00, B:834:0x1d26, B:835:0x1d4c, B:836:0x1d72, B:837:0x1d98, B:838:0x1dbe, B:839:0x1ddb, B:840:0x1e0d, B:841:0x1e3f, B:842:0x1e71, B:843:0x1ea1, B:844:0x1ed3, B:845:0x1efe, B:846:0x1f23, B:847:0x1f4e, B:848:0x1f74, B:849:0x1f9a, B:850:0x1fc0, B:851:0x1feb, B:852:0x2016, B:853:0x2041, B:854:0x2067, B:856:0x206b, B:858:0x2073, B:859:0x20b1, B:860:0x20d5, B:861:0x20fb, B:862:0x2121, B:863:0x213d, B:864:0x2163, B:865:0x2189, B:866:0x21af, B:867:0x21d5, B:868:0x21fb, B:869:0x221e, B:871:0x2249), top: B:977:0x0344 }] */
    /* JADX WARN: Removed duplicated region for block: B:873:0x2266  */
    /* JADX WARN: Removed duplicated region for block: B:877:0x227b A[Catch: all -> 0x23c4, TryCatch #4 {all -> 0x23c4, blocks: (B:875:0x2271, B:877:0x227b, B:881:0x228d, B:888:0x22b4, B:897:0x22f2, B:898:0x2301, B:909:0x2351, B:915:0x235d, B:922:0x2394), top: B:968:0x2271 }] */
    /* JADX WARN: Removed duplicated region for block: B:917:0x2388  */
    /* JADX WARN: Removed duplicated region for block: B:925:0x23b4  */
    /* JADX WARN: Removed duplicated region for block: B:933:0x2409 A[Catch: all -> 0x2420, TryCatch #0 {all -> 0x2420, blocks: (B:924:0x23ad, B:933:0x2409, B:934:0x240e), top: B:961:0x23ad }] */
    /* JADX WARN: Removed duplicated region for block: B:954:0x2468  */
    /* JADX WARN: Removed duplicated region for block: B:955:0x2478  */
    /* JADX WARN: Removed duplicated region for block: B:958:0x247f  */
    /* renamed from: lambda$onMessageReceived$3$org-telegram-messenger-GcmPushListenerService */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void m280xa7b3420a(Map data, long time) {
        GcmPushListenerService gcmPushListenerService;
        Throwable e;
        JSONObject custom;
        char c;
        long channel_id;
        long channel_id2;
        byte[] strBytes;
        long user_id;
        long dialogId;
        long chat_id;
        long dialogId2;
        String loc_key;
        boolean silent;
        boolean canRelease;
        boolean processNotification;
        boolean isGroup;
        long chat_from_id;
        String[] args;
        boolean channel;
        String message1;
        boolean pinned;
        boolean supergroup;
        String userName;
        String userName2;
        String name;
        boolean supergroup2;
        long channel_id3;
        String str;
        String str2;
        boolean localMessage;
        String name2;
        String messageText;
        long dialogId3;
        long chat_id2;
        boolean z;
        boolean scheduled;
        boolean z2;
        char c2;
        JSONObject custom2;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("GCM START PROCESSING");
        }
        int currentAccount = -1;
        String loc_key2 = null;
        String jsonString = null;
        try {
            Object value = data.get(TtmlNode.TAG_P);
            try {
                if (!(value instanceof String)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("GCM DECRYPT ERROR 1");
                    }
                    onDecryptError();
                    return;
                }
                byte[] bytes = Base64.decode((String) value, 8);
                NativeByteBuffer buffer = new NativeByteBuffer(bytes.length);
                buffer.writeBytes(bytes);
                buffer.position(0);
                if (SharedConfig.pushAuthKeyId == null) {
                    SharedConfig.pushAuthKeyId = new byte[8];
                    byte[] authKeyHash = Utilities.computeSHA1(SharedConfig.pushAuthKey);
                    System.arraycopy(authKeyHash, authKeyHash.length - 8, SharedConfig.pushAuthKeyId, 0, 8);
                }
                byte[] authKeyHash2 = new byte[8];
                buffer.readBytes(authKeyHash2, true);
                if (!Arrays.equals(SharedConfig.pushAuthKeyId, authKeyHash2)) {
                    onDecryptError();
                    if (!BuildVars.LOGS_ENABLED) {
                        return;
                    }
                    FileLog.d(String.format(Locale.US, "GCM DECRYPT ERROR 2 k1=%s k2=%s, key=%s", Utilities.bytesToHex(SharedConfig.pushAuthKeyId), Utilities.bytesToHex(authKeyHash2), Utilities.bytesToHex(SharedConfig.pushAuthKey)));
                    return;
                }
                byte[] messageKey = new byte[16];
                buffer.readBytes(messageKey, true);
                MessageKeyData messageKeyData = MessageKeyData.generateMessageKeyData(SharedConfig.pushAuthKey, messageKey, true, 2);
                try {
                    Utilities.aesIgeEncryption(buffer.buffer, messageKeyData.aesKey, messageKeyData.aesIv, false, false, 24, bytes.length - 24);
                    byte[] messageKeyFull = Utilities.computeSHA256(SharedConfig.pushAuthKey, 96, 32, buffer.buffer, 24, buffer.buffer.limit());
                    try {
                        if (!Utilities.arraysEquals(messageKey, 0, messageKeyFull, 8)) {
                            onDecryptError();
                            if (!BuildVars.LOGS_ENABLED) {
                                return;
                            }
                            FileLog.d(String.format("GCM DECRYPT ERROR 3, key = %s", Utilities.bytesToHex(SharedConfig.pushAuthKey)));
                            return;
                        }
                        int len = buffer.readInt32(true);
                        byte[] strBytes2 = new byte[len];
                        buffer.readBytes(strBytes2, true);
                        jsonString = new String(strBytes2);
                        try {
                            JSONObject json = new JSONObject(jsonString);
                            loc_key2 = json.has("loc_key") ? json.getString("loc_key") : "";
                            try {
                                Object object = json.get("custom");
                                try {
                                    if (object instanceof JSONObject) {
                                        try {
                                            custom = json.getJSONObject("custom");
                                        } catch (Throwable th) {
                                            gcmPushListenerService = this;
                                            jsonString = jsonString;
                                            currentAccount = -1;
                                            e = th;
                                            if (currentAccount != -1) {
                                            }
                                            if (BuildVars.LOGS_ENABLED) {
                                            }
                                            FileLog.e(e);
                                            return;
                                        }
                                    } else {
                                        custom = new JSONObject();
                                    }
                                    Object userIdObject = json.has("user_id") ? json.get("user_id") : null;
                                    long accountUserId = userIdObject == null ? UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId() : userIdObject instanceof Long ? ((Long) userIdObject).longValue() : userIdObject instanceof Integer ? ((Integer) userIdObject).intValue() : userIdObject instanceof String ? Utilities.parseInt((CharSequence) ((String) userIdObject)).intValue() : UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                                    int account = UserConfig.selectedAccount;
                                    boolean foundAccount = false;
                                    int a = 0;
                                    while (true) {
                                        byte[] bytes2 = bytes;
                                        if (a >= 4) {
                                            break;
                                        } else if (UserConfig.getInstance(a).getClientUserId() == accountUserId) {
                                            account = a;
                                            foundAccount = true;
                                            break;
                                        } else {
                                            a++;
                                            bytes = bytes2;
                                        }
                                    }
                                    if (!foundAccount) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("GCM ACCOUNT NOT FOUND");
                                        }
                                        this.countDownLatch.countDown();
                                        return;
                                    }
                                    int currentAccount2 = account;
                                    final int accountFinal = account;
                                    try {
                                        try {
                                            if (UserConfig.getInstance(currentAccount2).isClientActivated()) {
                                                data.get(Constants.MessagePayloadKeys.SENT_TIME);
                                                switch (loc_key2.hashCode()) {
                                                    case -1963663249:
                                                        if (loc_key2.equals("SESSION_REVOKE")) {
                                                            c = 2;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case -920689527:
                                                        if (loc_key2.equals("DC_UPDATE")) {
                                                            c = 0;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case 633004703:
                                                        if (loc_key2.equals("MESSAGE_ANNOUNCEMENT")) {
                                                            c = 1;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    case 1365673842:
                                                        if (loc_key2.equals("GEO_LIVE_PENDING")) {
                                                            c = 3;
                                                            break;
                                                        }
                                                        c = 65535;
                                                        break;
                                                    default:
                                                        c = 65535;
                                                        break;
                                                }
                                                switch (c) {
                                                    case 0:
                                                        int dc = custom.getInt("dc");
                                                        String addr = custom.getString("addr");
                                                        String[] parts = addr.split(com.microsoft.appcenter.Constants.COMMON_SCHEMA_PREFIX_SEPARATOR);
                                                        if (parts.length != 2) {
                                                            this.countDownLatch.countDown();
                                                            return;
                                                        }
                                                        String ip = parts[0];
                                                        int port = Integer.parseInt(parts[1]);
                                                        ConnectionsManager.getInstance(currentAccount2).applyDatacenterAddress(dc, ip, port);
                                                        ConnectionsManager.getInstance(currentAccount2).resumeNetworkMaybe();
                                                        this.countDownLatch.countDown();
                                                        return;
                                                    case 1:
                                                        TLRPC.TL_updateServiceNotification update = new TLRPC.TL_updateServiceNotification();
                                                        update.popup = false;
                                                        update.flags = 2;
                                                        long accountUserId2 = time / 1000;
                                                        update.inbox_date = (int) accountUserId2;
                                                        update.message = json.getString("message");
                                                        update.type = "announcement";
                                                        update.media = new TLRPC.TL_messageMediaEmpty();
                                                        final TLRPC.TL_updates updates = new TLRPC.TL_updates();
                                                        updates.updates.add(update);
                                                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda3
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                MessagesController.getInstance(accountFinal).processUpdates(updates, false);
                                                            }
                                                        });
                                                        ConnectionsManager.getInstance(currentAccount2).resumeNetworkMaybe();
                                                        this.countDownLatch.countDown();
                                                        return;
                                                    case 2:
                                                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda0
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                GcmPushListenerService.lambda$onMessageReceived$1(accountFinal);
                                                            }
                                                        });
                                                        this.countDownLatch.countDown();
                                                        return;
                                                    case 3:
                                                        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda1
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                LocationController.getInstance(accountFinal).setNewLocationEndWatchTime();
                                                            }
                                                        });
                                                        this.countDownLatch.countDown();
                                                        return;
                                                    default:
                                                        if (custom.has("channel_id")) {
                                                            long channel_id4 = custom.getLong("channel_id");
                                                            strBytes = strBytes2;
                                                            channel_id2 = -channel_id4;
                                                            channel_id = channel_id4;
                                                        } else {
                                                            strBytes = strBytes2;
                                                            channel_id2 = 0;
                                                            channel_id = 0;
                                                        }
                                                        if (custom.has("from_id")) {
                                                            long user_id2 = custom.getLong("from_id");
                                                            dialogId = user_id2;
                                                            user_id = dialogId;
                                                        } else {
                                                            long j = channel_id2;
                                                            dialogId = 0;
                                                            user_id = j;
                                                        }
                                                        if (custom.has(ChatReactionsEditActivity.KEY_CHAT_ID)) {
                                                            try {
                                                                chat_id = custom.getLong(ChatReactionsEditActivity.KEY_CHAT_ID);
                                                                dialogId2 = -chat_id;
                                                            } catch (Throwable th2) {
                                                                gcmPushListenerService = this;
                                                                e = th2;
                                                                jsonString = jsonString;
                                                                currentAccount = currentAccount2;
                                                                if (currentAccount != -1) {
                                                                }
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                }
                                                                FileLog.e(e);
                                                                return;
                                                            }
                                                        } else {
                                                            dialogId2 = user_id;
                                                            chat_id = 0;
                                                        }
                                                        try {
                                                            long dialogId4 = custom.has("encryption_id") ? DialogObject.makeEncryptedDialogId(custom.getInt("encryption_id")) : dialogId2;
                                                            boolean scheduled2 = custom.has("schedule") ? custom.getInt("schedule") == 1 : false;
                                                            boolean scheduled3 = scheduled2;
                                                            if (dialogId4 == 0 && "ENCRYPTED_MESSAGE".equals(loc_key2)) {
                                                                dialogId4 = NotificationsController.globalSecretChatId;
                                                            }
                                                            if (dialogId4 != 0) {
                                                                canRelease = true;
                                                                if ("READ_HISTORY".equals(loc_key2)) {
                                                                    int max_id = custom.getInt("max_id");
                                                                    ArrayList<TLRPC.Update> updates2 = new ArrayList<>();
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                        StringBuilder sb = new StringBuilder();
                                                                        custom2 = custom;
                                                                        sb.append("GCM received read notification max_id = ");
                                                                        sb.append(max_id);
                                                                        sb.append(" for dialogId = ");
                                                                        sb.append(dialogId4);
                                                                        FileLog.d(sb.toString());
                                                                    } else {
                                                                        custom2 = custom;
                                                                    }
                                                                    if (channel_id != 0) {
                                                                        TLRPC.TL_updateReadChannelInbox update2 = new TLRPC.TL_updateReadChannelInbox();
                                                                        update2.channel_id = channel_id;
                                                                        update2.max_id = max_id;
                                                                        updates2.add(update2);
                                                                    } else {
                                                                        TLRPC.TL_updateReadHistoryInbox update3 = new TLRPC.TL_updateReadHistoryInbox();
                                                                        if (dialogId != 0) {
                                                                            update3.peer = new TLRPC.TL_peerUser();
                                                                            update3.peer.user_id = dialogId;
                                                                        } else {
                                                                            update3.peer = new TLRPC.TL_peerChat();
                                                                            update3.peer.chat_id = chat_id;
                                                                        }
                                                                        update3.max_id = max_id;
                                                                        updates2.add(update3);
                                                                    }
                                                                    MessagesController.getInstance(accountFinal).processUpdateArray(updates2, null, null, false, 0);
                                                                    gcmPushListenerService = this;
                                                                    loc_key = loc_key2;
                                                                } else {
                                                                    JSONObject custom3 = custom;
                                                                    if ("MESSAGE_DELETED".equals(loc_key2)) {
                                                                        String messages = custom3.getString("messages");
                                                                        String[] messagesArgs = messages.split(",");
                                                                        LongSparseArray<ArrayList<Integer>> deletedMessages = new LongSparseArray<>();
                                                                        ArrayList<Integer> ids = new ArrayList<>();
                                                                        int a2 = 0;
                                                                        while (true) {
                                                                            long user_id3 = dialogId;
                                                                            if (a2 < messagesArgs.length) {
                                                                                ArrayList<Integer> ids2 = ids;
                                                                                ids2.add(Utilities.parseInt((CharSequence) messagesArgs[a2]));
                                                                                a2++;
                                                                                ids = ids2;
                                                                                dialogId = user_id3;
                                                                            } else {
                                                                                ArrayList<Integer> ids3 = ids;
                                                                                deletedMessages.put(-channel_id, ids3);
                                                                                NotificationsController.getInstance(currentAccount2).removeDeletedMessagesFromNotifications(deletedMessages);
                                                                                MessagesController.getInstance(currentAccount2).deleteMessagesByPush(dialogId4, ids3, channel_id);
                                                                                if (BuildVars.LOGS_ENABLED) {
                                                                                    FileLog.d("GCM received " + loc_key2 + " for dialogId = " + dialogId4 + " mids = " + TextUtils.join(",", ids3));
                                                                                }
                                                                                gcmPushListenerService = this;
                                                                                loc_key = loc_key2;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        long chat_id3 = chat_id;
                                                                        long user_id4 = dialogId;
                                                                        if (!TextUtils.isEmpty(loc_key2)) {
                                                                            int msg_id = custom3.has("msg_id") ? custom3.getInt("msg_id") : 0;
                                                                            long random_id = custom3.has("random_id") ? Utilities.parseLong(custom3.getString("random_id")).longValue() : 0L;
                                                                            if (msg_id != 0) {
                                                                                Integer currentReadValue = MessagesController.getInstance(currentAccount2).dialogs_read_inbox_max.get(Long.valueOf(dialogId4));
                                                                                if (currentReadValue == null) {
                                                                                    currentReadValue = Integer.valueOf(MessagesStorage.getInstance(currentAccount2).getDialogReadMax(false, dialogId4));
                                                                                    MessagesController.getInstance(accountFinal).dialogs_read_inbox_max.put(Long.valueOf(dialogId4), currentReadValue);
                                                                                }
                                                                                processNotification = msg_id > currentReadValue.intValue();
                                                                            } else {
                                                                                processNotification = random_id != 0 && !MessagesStorage.getInstance(account).checkMessageByRandomId(random_id);
                                                                            }
                                                                            if (loc_key2.startsWith("REACT_") || loc_key2.startsWith("CHAT_REACT_")) {
                                                                                processNotification = true;
                                                                            }
                                                                            if (processNotification) {
                                                                                long random_id2 = random_id;
                                                                                long chat_from_id2 = custom3.optLong("chat_from_id", 0L);
                                                                                long chat_from_broadcast_id = custom3.optLong("chat_from_broadcast_id", 0L);
                                                                                long chat_from_group_id = custom3.optLong("chat_from_group_id", 0L);
                                                                                try {
                                                                                    if (chat_from_id2 == 0 && chat_from_group_id == 0) {
                                                                                        isGroup = false;
                                                                                        boolean mention = !custom3.has("mention") && custom3.getInt("mention") != 0;
                                                                                        boolean silent2 = !custom3.has(NotificationCompat.GROUP_KEY_SILENT) && custom3.getInt(NotificationCompat.GROUP_KEY_SILENT) != 0;
                                                                                        boolean silent3 = silent2;
                                                                                        boolean mention2 = mention;
                                                                                        if (!json.has("loc_args")) {
                                                                                            JSONArray loc_args = json.getJSONArray("loc_args");
                                                                                            args = new String[loc_args.length()];
                                                                                            chat_from_id = chat_from_id2;
                                                                                            for (int a3 = 0; a3 < args.length; a3++) {
                                                                                                args[a3] = loc_args.getString(a3);
                                                                                            }
                                                                                        } else {
                                                                                            chat_from_id = chat_from_id2;
                                                                                            args = null;
                                                                                        }
                                                                                        String name3 = args[0];
                                                                                        boolean edited = custom3.has("edit_date");
                                                                                        if (loc_key2.startsWith("CHAT_")) {
                                                                                            message1 = null;
                                                                                            if (loc_key2.startsWith("PINNED_")) {
                                                                                                userName = null;
                                                                                                supergroup = channel_id != 0;
                                                                                                pinned = true;
                                                                                                channel = false;
                                                                                            } else if (loc_key2.startsWith("CHANNEL_")) {
                                                                                                userName = null;
                                                                                                supergroup = false;
                                                                                                pinned = false;
                                                                                                channel = true;
                                                                                            } else {
                                                                                                userName = null;
                                                                                                supergroup = false;
                                                                                                pinned = false;
                                                                                                channel = false;
                                                                                            }
                                                                                        } else if (UserObject.isReplyUser(dialogId4)) {
                                                                                            StringBuilder sb2 = new StringBuilder();
                                                                                            sb2.append(name3);
                                                                                            message1 = null;
                                                                                            sb2.append(" @ ");
                                                                                            sb2.append(args[1]);
                                                                                            name3 = sb2.toString();
                                                                                            userName = null;
                                                                                            supergroup = false;
                                                                                            pinned = false;
                                                                                            channel = false;
                                                                                        } else {
                                                                                            message1 = null;
                                                                                            boolean supergroup3 = channel_id != 0;
                                                                                            name3 = args[1];
                                                                                            userName = name3;
                                                                                            supergroup = supergroup3;
                                                                                            pinned = false;
                                                                                            channel = false;
                                                                                        }
                                                                                        if (!BuildVars.LOGS_ENABLED) {
                                                                                            name = name3;
                                                                                            StringBuilder sb3 = new StringBuilder();
                                                                                            userName2 = userName;
                                                                                            sb3.append("GCM received message notification ");
                                                                                            sb3.append(loc_key2);
                                                                                            sb3.append(" for dialogId = ");
                                                                                            sb3.append(dialogId4);
                                                                                            sb3.append(" mid = ");
                                                                                            sb3.append(msg_id);
                                                                                            FileLog.d(sb3.toString());
                                                                                        } else {
                                                                                            userName2 = userName;
                                                                                            name = name3;
                                                                                        }
                                                                                        if (!loc_key2.startsWith("REACT_")) {
                                                                                            str = "REACT_";
                                                                                            supergroup2 = supergroup;
                                                                                            channel_id3 = channel_id;
                                                                                            str2 = "CHAT_REACT_";
                                                                                        } else if (loc_key2.startsWith("CHAT_REACT_")) {
                                                                                            str = "REACT_";
                                                                                            supergroup2 = supergroup;
                                                                                            channel_id3 = channel_id;
                                                                                            str2 = "CHAT_REACT_";
                                                                                        } else {
                                                                                            switch (loc_key2.hashCode()) {
                                                                                                case -2100047043:
                                                                                                    if (loc_key2.equals("MESSAGE_GAME_SCORE")) {
                                                                                                        c2 = 20;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -2091498420:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_CONTACT")) {
                                                                                                        c2 = '$';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -2053872415:
                                                                                                    if (loc_key2.equals("CHAT_CREATED")) {
                                                                                                        c2 = 'B';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -2039746363:
                                                                                                    if (loc_key2.equals("MESSAGE_STICKER")) {
                                                                                                        c2 = 11;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -2023218804:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_VIDEOS")) {
                                                                                                        c2 = '-';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1979538588:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_DOC")) {
                                                                                                        c2 = '!';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1979536003:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_GEO")) {
                                                                                                        c2 = '\'';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1979535888:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_GIF")) {
                                                                                                        c2 = ')';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1969004705:
                                                                                                    if (loc_key2.equals("CHAT_ADD_MEMBER")) {
                                                                                                        c2 = 'F';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1946699248:
                                                                                                    if (loc_key2.equals("CHAT_JOINED")) {
                                                                                                        c2 = 'O';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1717283471:
                                                                                                    if (loc_key2.equals("CHAT_REQ_JOINED")) {
                                                                                                        c2 = 'P';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1646640058:
                                                                                                    if (loc_key2.equals("CHAT_VOICECHAT_START")) {
                                                                                                        c2 = 'G';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1528047021:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGES")) {
                                                                                                        c2 = 'V';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1507149394:
                                                                                                    if (loc_key2.equals("MESSAGE_RECURRING_PAY")) {
                                                                                                        c2 = 0;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1493579426:
                                                                                                    if (loc_key2.equals("MESSAGE_AUDIO")) {
                                                                                                        c2 = '\f';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1482481933:
                                                                                                    if (loc_key2.equals("MESSAGE_MUTED")) {
                                                                                                        c2 = 'q';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1480102982:
                                                                                                    if (loc_key2.equals("MESSAGE_PHOTO")) {
                                                                                                        c2 = 4;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1478041834:
                                                                                                    if (loc_key2.equals("MESSAGE_ROUND")) {
                                                                                                        c2 = '\t';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1474543101:
                                                                                                    if (loc_key2.equals("MESSAGE_VIDEO")) {
                                                                                                        c2 = 6;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1465695932:
                                                                                                    if (loc_key2.equals("ENCRYPTION_ACCEPT")) {
                                                                                                        c2 = 'o';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1374906292:
                                                                                                    if (loc_key2.equals("ENCRYPTED_MESSAGE")) {
                                                                                                        c2 = 'h';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1372940586:
                                                                                                    if (loc_key2.equals("CHAT_RETURNED")) {
                                                                                                        c2 = 'N';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1264245338:
                                                                                                    if (loc_key2.equals("PINNED_INVOICE")) {
                                                                                                        c2 = 'f';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1236154001:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_DOCS")) {
                                                                                                        c2 = '/';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1236086700:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_FWDS")) {
                                                                                                        c2 = '+';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1236077786:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_GAME")) {
                                                                                                        c2 = '*';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1235796237:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_POLL")) {
                                                                                                        c2 = '&';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1235760759:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_QUIZ")) {
                                                                                                        c2 = '%';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1235686303:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_TEXT")) {
                                                                                                        c2 = 2;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1198046100:
                                                                                                    if (loc_key2.equals("MESSAGE_VIDEO_SECRET")) {
                                                                                                        c2 = 7;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1124254527:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_CONTACT")) {
                                                                                                        c2 = '9';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1085137927:
                                                                                                    if (loc_key2.equals("PINNED_GAME")) {
                                                                                                        c2 = 'd';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1084856378:
                                                                                                    if (loc_key2.equals("PINNED_POLL")) {
                                                                                                        c2 = 'a';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1084820900:
                                                                                                    if (loc_key2.equals("PINNED_QUIZ")) {
                                                                                                        c2 = '`';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -1084746444:
                                                                                                    if (loc_key2.equals("PINNED_TEXT")) {
                                                                                                        c2 = 'W';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -819729482:
                                                                                                    if (loc_key2.equals("PINNED_STICKER")) {
                                                                                                        c2 = ']';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -772141857:
                                                                                                    if (loc_key2.equals("PHONE_CALL_REQUEST")) {
                                                                                                        c2 = 'p';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -638310039:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_STICKER")) {
                                                                                                        c2 = '\"';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -590403924:
                                                                                                    if (loc_key2.equals("PINNED_GAME_SCORE")) {
                                                                                                        c2 = 'e';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -589196239:
                                                                                                    if (loc_key2.equals("PINNED_DOC")) {
                                                                                                        c2 = '\\';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -589193654:
                                                                                                    if (loc_key2.equals("PINNED_GEO")) {
                                                                                                        c2 = 'b';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -589193539:
                                                                                                    if (loc_key2.equals("PINNED_GIF")) {
                                                                                                        c2 = 'g';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -440169325:
                                                                                                    if (loc_key2.equals("AUTH_UNKNOWN")) {
                                                                                                        c2 = 'k';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -412748110:
                                                                                                    if (loc_key2.equals("CHAT_DELETE_YOU")) {
                                                                                                        c2 = 'L';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -228518075:
                                                                                                    if (loc_key2.equals("MESSAGE_GEOLIVE")) {
                                                                                                        c2 = 17;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -213586509:
                                                                                                    if (loc_key2.equals("ENCRYPTION_REQUEST")) {
                                                                                                        c2 = 'n';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -115582002:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_INVOICE")) {
                                                                                                        c2 = 'A';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -112621464:
                                                                                                    if (loc_key2.equals("CONTACT_JOINED")) {
                                                                                                        c2 = 'j';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -108522133:
                                                                                                    if (loc_key2.equals("AUTH_REGION")) {
                                                                                                        c2 = 'l';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -107572034:
                                                                                                    if (loc_key2.equals("MESSAGE_SCREENSHOT")) {
                                                                                                        c2 = '\b';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case -40534265:
                                                                                                    if (loc_key2.equals("CHAT_DELETE_MEMBER")) {
                                                                                                        c2 = 'K';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 52369421:
                                                                                                    if (loc_key2.equals("REACT_TEXT")) {
                                                                                                        c2 = 'i';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 65254746:
                                                                                                    if (loc_key2.equals("CHAT_ADD_YOU")) {
                                                                                                        c2 = 'C';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 141040782:
                                                                                                    if (loc_key2.equals("CHAT_LEFT")) {
                                                                                                        c2 = 'M';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 202550149:
                                                                                                    if (loc_key2.equals("CHAT_VOICECHAT_INVITE")) {
                                                                                                        c2 = 'H';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 309993049:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_DOC")) {
                                                                                                        c2 = '6';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 309995634:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_GEO")) {
                                                                                                        c2 = '<';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 309995749:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_GIF")) {
                                                                                                        c2 = '>';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 320532812:
                                                                                                    if (loc_key2.equals("MESSAGES")) {
                                                                                                        c2 = 28;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 328933854:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_STICKER")) {
                                                                                                        c2 = '7';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 331340546:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_AUDIO")) {
                                                                                                        c2 = '#';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 342406591:
                                                                                                    if (loc_key2.equals("CHAT_VOICECHAT_END")) {
                                                                                                        c2 = 'I';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 344816990:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_PHOTO")) {
                                                                                                        c2 = 30;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 346878138:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_ROUND")) {
                                                                                                        c2 = ' ';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 350376871:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_VIDEO")) {
                                                                                                        c2 = 31;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 608430149:
                                                                                                    if (loc_key2.equals("CHAT_VOICECHAT_INVITE_YOU")) {
                                                                                                        c2 = 'J';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 615714517:
                                                                                                    if (loc_key2.equals("MESSAGE_PHOTO_SECRET")) {
                                                                                                        c2 = 5;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 715508879:
                                                                                                    if (loc_key2.equals("PINNED_AUDIO")) {
                                                                                                        c2 = '^';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 728985323:
                                                                                                    if (loc_key2.equals("PINNED_PHOTO")) {
                                                                                                        c2 = 'Y';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 731046471:
                                                                                                    if (loc_key2.equals("PINNED_ROUND")) {
                                                                                                        c2 = '[';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 734545204:
                                                                                                    if (loc_key2.equals("PINNED_VIDEO")) {
                                                                                                        c2 = 'Z';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 802032552:
                                                                                                    if (loc_key2.equals("MESSAGE_CONTACT")) {
                                                                                                        c2 = '\r';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 991498806:
                                                                                                    if (loc_key2.equals("PINNED_GEOLIVE")) {
                                                                                                        c2 = 'c';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1007364121:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_GAME_SCORE")) {
                                                                                                        c2 = 21;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1019850010:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_DOCS")) {
                                                                                                        c2 = 'U';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1019917311:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_FWDS")) {
                                                                                                        c2 = 'Q';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1019926225:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_GAME")) {
                                                                                                        c2 = '?';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1020207774:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_POLL")) {
                                                                                                        c2 = ';';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1020243252:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_QUIZ")) {
                                                                                                        c2 = ':';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1020317708:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_TEXT")) {
                                                                                                        c2 = '1';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1060282259:
                                                                                                    if (loc_key2.equals("MESSAGE_DOCS")) {
                                                                                                        c2 = 27;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1060349560:
                                                                                                    if (loc_key2.equals("MESSAGE_FWDS")) {
                                                                                                        c2 = 23;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1060358474:
                                                                                                    if (loc_key2.equals("MESSAGE_GAME")) {
                                                                                                        c2 = 19;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1060640023:
                                                                                                    if (loc_key2.equals("MESSAGE_POLL")) {
                                                                                                        c2 = 15;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1060675501:
                                                                                                    if (loc_key2.equals("MESSAGE_QUIZ")) {
                                                                                                        c2 = 14;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1060749957:
                                                                                                    if (loc_key2.equals("MESSAGE_TEXT")) {
                                                                                                        c2 = 1;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1073049781:
                                                                                                    if (loc_key2.equals("PINNED_NOTEXT")) {
                                                                                                        c2 = 'X';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1078101399:
                                                                                                    if (loc_key2.equals("CHAT_TITLE_EDITED")) {
                                                                                                        c2 = 'D';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1110103437:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_NOTEXT")) {
                                                                                                        c2 = '2';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1160762272:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_PHOTOS")) {
                                                                                                        c2 = 'R';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1172918249:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_GEOLIVE")) {
                                                                                                        c2 = '(';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1234591620:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_GAME_SCORE")) {
                                                                                                        c2 = '@';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1281128640:
                                                                                                    if (loc_key2.equals("MESSAGE_DOC")) {
                                                                                                        c2 = '\n';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1281131225:
                                                                                                    if (loc_key2.equals("MESSAGE_GEO")) {
                                                                                                        c2 = 16;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1281131340:
                                                                                                    if (loc_key2.equals("MESSAGE_GIF")) {
                                                                                                        c2 = 18;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1310789062:
                                                                                                    if (loc_key2.equals("MESSAGE_NOTEXT")) {
                                                                                                        c2 = 3;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1333118583:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_VIDEOS")) {
                                                                                                        c2 = 'S';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1361447897:
                                                                                                    if (loc_key2.equals("MESSAGE_PHOTOS")) {
                                                                                                        c2 = 24;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1498266155:
                                                                                                    if (loc_key2.equals("PHONE_CALL_MISSED")) {
                                                                                                        c2 = 'r';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1533804208:
                                                                                                    if (loc_key2.equals("MESSAGE_VIDEOS")) {
                                                                                                        c2 = 25;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1540131626:
                                                                                                    if (loc_key2.equals("MESSAGE_PLAYLIST")) {
                                                                                                        c2 = 26;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1547988151:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_AUDIO")) {
                                                                                                        c2 = '8';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1561464595:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_PHOTO")) {
                                                                                                        c2 = '3';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1563525743:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_ROUND")) {
                                                                                                        c2 = '5';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1567024476:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_VIDEO")) {
                                                                                                        c2 = '4';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1810705077:
                                                                                                    if (loc_key2.equals("MESSAGE_INVOICE")) {
                                                                                                        c2 = 22;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1815177512:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGES")) {
                                                                                                        c2 = '0';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1954774321:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_PLAYLIST")) {
                                                                                                        c2 = 'T';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 1963241394:
                                                                                                    if (loc_key2.equals("LOCKED_MESSAGE")) {
                                                                                                        c2 = 'm';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 2014789757:
                                                                                                    if (loc_key2.equals("CHAT_PHOTO_EDITED")) {
                                                                                                        c2 = 'E';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 2022049433:
                                                                                                    if (loc_key2.equals("PINNED_CONTACT")) {
                                                                                                        c2 = '_';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 2034984710:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_PLAYLIST")) {
                                                                                                        c2 = '.';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 2048733346:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_NOTEXT")) {
                                                                                                        c2 = 29;
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 2099392181:
                                                                                                    if (loc_key2.equals("CHANNEL_MESSAGE_PHOTOS")) {
                                                                                                        c2 = ',';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                case 2140162142:
                                                                                                    if (loc_key2.equals("CHAT_MESSAGE_GEOLIVE")) {
                                                                                                        c2 = '=';
                                                                                                        break;
                                                                                                    }
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                                default:
                                                                                                    c2 = 65535;
                                                                                                    break;
                                                                                            }
                                                                                            str2 = "CHAT_REACT_";
                                                                                            str = "REACT_";
                                                                                            channel_id3 = channel_id;
                                                                                            supergroup2 = supergroup;
                                                                                            switch (c2) {
                                                                                                case 0:
                                                                                                    String messageText2 = LocaleController.formatString("NotificationMessageRecurringPay", org.telegram.messenger.beta.R.string.NotificationMessageRecurringPay, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("PaymentInvoice", org.telegram.messenger.beta.R.string.PaymentInvoice);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText2;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 1:
                                                                                                case 2:
                                                                                                    String messageText3 = LocaleController.formatString("NotificationMessageText", org.telegram.messenger.beta.R.string.NotificationMessageText, args[0], args[1]);
                                                                                                    message1 = args[1];
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText3;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 3:
                                                                                                    String messageText4 = LocaleController.formatString("NotificationMessageNoText", org.telegram.messenger.beta.R.string.NotificationMessageNoText, args[0]);
                                                                                                    message1 = LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText4;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 4:
                                                                                                    String messageText5 = LocaleController.formatString("NotificationMessagePhoto", org.telegram.messenger.beta.R.string.NotificationMessagePhoto, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachPhoto", org.telegram.messenger.beta.R.string.AttachPhoto);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText5;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 5:
                                                                                                    String messageText6 = LocaleController.formatString("NotificationMessageSDPhoto", org.telegram.messenger.beta.R.string.NotificationMessageSDPhoto, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachDestructingPhoto", org.telegram.messenger.beta.R.string.AttachDestructingPhoto);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText6;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 6:
                                                                                                    String messageText7 = LocaleController.formatString("NotificationMessageVideo", org.telegram.messenger.beta.R.string.NotificationMessageVideo, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachVideo", org.telegram.messenger.beta.R.string.AttachVideo);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText7;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 7:
                                                                                                    String messageText8 = LocaleController.formatString("NotificationMessageSDVideo", org.telegram.messenger.beta.R.string.NotificationMessageSDVideo, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachDestructingVideo", org.telegram.messenger.beta.R.string.AttachDestructingVideo);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText8;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '\b':
                                                                                                    messageText = LocaleController.getString("ActionTakeScreenshoot", org.telegram.messenger.beta.R.string.ActionTakeScreenshoot).replace("un1", args[0]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '\t':
                                                                                                    String messageText9 = LocaleController.formatString("NotificationMessageRound", org.telegram.messenger.beta.R.string.NotificationMessageRound, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachRound", org.telegram.messenger.beta.R.string.AttachRound);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText9;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '\n':
                                                                                                    String messageText10 = LocaleController.formatString("NotificationMessageDocument", org.telegram.messenger.beta.R.string.NotificationMessageDocument, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachDocument", org.telegram.messenger.beta.R.string.AttachDocument);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText10;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 11:
                                                                                                    if (args.length <= 1 || TextUtils.isEmpty(args[1])) {
                                                                                                        String messageText11 = LocaleController.formatString("NotificationMessageSticker", org.telegram.messenger.beta.R.string.NotificationMessageSticker, args[0]);
                                                                                                        message1 = LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        messageText = messageText11;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        message1 = args[1] + " " + LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        messageText = LocaleController.formatString("NotificationMessageStickerEmoji", org.telegram.messenger.beta.R.string.NotificationMessageStickerEmoji, args[0], args[1]);
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case '\f':
                                                                                                    String messageText12 = LocaleController.formatString("NotificationMessageAudio", org.telegram.messenger.beta.R.string.NotificationMessageAudio, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachAudio", org.telegram.messenger.beta.R.string.AttachAudio);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText12;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '\r':
                                                                                                    String messageText13 = LocaleController.formatString("NotificationMessageContact2", org.telegram.messenger.beta.R.string.NotificationMessageContact2, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachContact", org.telegram.messenger.beta.R.string.AttachContact);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText13;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 14:
                                                                                                    String messageText14 = LocaleController.formatString("NotificationMessageQuiz2", org.telegram.messenger.beta.R.string.NotificationMessageQuiz2, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("QuizPoll", org.telegram.messenger.beta.R.string.QuizPoll);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText14;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 15:
                                                                                                    String messageText15 = LocaleController.formatString("NotificationMessagePoll2", org.telegram.messenger.beta.R.string.NotificationMessagePoll2, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("Poll", org.telegram.messenger.beta.R.string.Poll);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText15;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 16:
                                                                                                    String messageText16 = LocaleController.formatString("NotificationMessageMap", org.telegram.messenger.beta.R.string.NotificationMessageMap, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachLocation", org.telegram.messenger.beta.R.string.AttachLocation);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText16;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 17:
                                                                                                    String messageText17 = LocaleController.formatString("NotificationMessageLiveLocation", org.telegram.messenger.beta.R.string.NotificationMessageLiveLocation, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachLiveLocation", org.telegram.messenger.beta.R.string.AttachLiveLocation);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText17;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 18:
                                                                                                    String messageText18 = LocaleController.formatString("NotificationMessageGif", org.telegram.messenger.beta.R.string.NotificationMessageGif, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachGif", org.telegram.messenger.beta.R.string.AttachGif);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText18;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 19:
                                                                                                    String messageText19 = LocaleController.formatString("NotificationMessageGame", org.telegram.messenger.beta.R.string.NotificationMessageGame, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachGame", org.telegram.messenger.beta.R.string.AttachGame);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText19;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 20:
                                                                                                case 21:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageGameScored", org.telegram.messenger.beta.R.string.NotificationMessageGameScored, args[0], args[1], args[2]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 22:
                                                                                                    String messageText20 = LocaleController.formatString("NotificationMessageInvoice", org.telegram.messenger.beta.R.string.NotificationMessageInvoice, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("PaymentInvoice", org.telegram.messenger.beta.R.string.PaymentInvoice);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText20;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 23:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageForwardFew", org.telegram.messenger.beta.R.string.NotificationMessageForwardFew, args[0], LocaleController.formatPluralString("messages", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 24:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageFew", org.telegram.messenger.beta.R.string.NotificationMessageFew, args[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 25:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageFew", org.telegram.messenger.beta.R.string.NotificationMessageFew, args[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 26:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageFew", org.telegram.messenger.beta.R.string.NotificationMessageFew, args[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 27:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageFew", org.telegram.messenger.beta.R.string.NotificationMessageFew, args[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 28:
                                                                                                    messageText = LocaleController.formatString("NotificationMessageAlbum", org.telegram.messenger.beta.R.string.NotificationMessageAlbum, args[0]);
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                                                                                                    String messageText21 = LocaleController.formatString("ChannelMessageNoText", org.telegram.messenger.beta.R.string.ChannelMessageNoText, args[0]);
                                                                                                    message1 = LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText21;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 30:
                                                                                                    String messageText22 = LocaleController.formatString("ChannelMessagePhoto", org.telegram.messenger.beta.R.string.ChannelMessagePhoto, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachPhoto", org.telegram.messenger.beta.R.string.AttachPhoto);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText22;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 31:
                                                                                                    String messageText23 = LocaleController.formatString("ChannelMessageVideo", org.telegram.messenger.beta.R.string.ChannelMessageVideo, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachVideo", org.telegram.messenger.beta.R.string.AttachVideo);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText23;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case ' ':
                                                                                                    String messageText24 = LocaleController.formatString("ChannelMessageRound", org.telegram.messenger.beta.R.string.ChannelMessageRound, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachRound", org.telegram.messenger.beta.R.string.AttachRound);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText24;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '!':
                                                                                                    String messageText25 = LocaleController.formatString("ChannelMessageDocument", org.telegram.messenger.beta.R.string.ChannelMessageDocument, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachDocument", org.telegram.messenger.beta.R.string.AttachDocument);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText25;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '\"':
                                                                                                    if (args.length <= 1 || TextUtils.isEmpty(args[1])) {
                                                                                                        String messageText26 = LocaleController.formatString("ChannelMessageSticker", org.telegram.messenger.beta.R.string.ChannelMessageSticker, args[0]);
                                                                                                        message1 = LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        messageText = messageText26;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        message1 = args[1] + " " + LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        messageText = LocaleController.formatString("ChannelMessageStickerEmoji", org.telegram.messenger.beta.R.string.ChannelMessageStickerEmoji, args[0], args[1]);
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                    break;
                                                                                                case '#':
                                                                                                    String messageText27 = LocaleController.formatString("ChannelMessageAudio", org.telegram.messenger.beta.R.string.ChannelMessageAudio, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachAudio", org.telegram.messenger.beta.R.string.AttachAudio);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText27;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '$':
                                                                                                    String messageText28 = LocaleController.formatString("ChannelMessageContact2", org.telegram.messenger.beta.R.string.ChannelMessageContact2, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachContact", org.telegram.messenger.beta.R.string.AttachContact);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText28;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '%':
                                                                                                    String messageText29 = LocaleController.formatString("ChannelMessageQuiz2", org.telegram.messenger.beta.R.string.ChannelMessageQuiz2, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("QuizPoll", org.telegram.messenger.beta.R.string.QuizPoll);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText29;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '&':
                                                                                                    String messageText30 = LocaleController.formatString("ChannelMessagePoll2", org.telegram.messenger.beta.R.string.ChannelMessagePoll2, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("Poll", org.telegram.messenger.beta.R.string.Poll);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText30;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '\'':
                                                                                                    String messageText31 = LocaleController.formatString("ChannelMessageMap", org.telegram.messenger.beta.R.string.ChannelMessageMap, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachLocation", org.telegram.messenger.beta.R.string.AttachLocation);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText31;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '(':
                                                                                                    String messageText32 = LocaleController.formatString("ChannelMessageLiveLocation", org.telegram.messenger.beta.R.string.ChannelMessageLiveLocation, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachLiveLocation", org.telegram.messenger.beta.R.string.AttachLiveLocation);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText32;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case ')':
                                                                                                    String messageText33 = LocaleController.formatString("ChannelMessageGIF", org.telegram.messenger.beta.R.string.ChannelMessageGIF, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachGif", org.telegram.messenger.beta.R.string.AttachGif);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText33;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '*':
                                                                                                    String messageText34 = LocaleController.formatString("NotificationMessageGame", org.telegram.messenger.beta.R.string.NotificationMessageGame, args[0]);
                                                                                                    message1 = LocaleController.getString("AttachGame", org.telegram.messenger.beta.R.string.AttachGame);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText34;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '+':
                                                                                                    messageText = LocaleController.formatString("ChannelMessageFew", org.telegram.messenger.beta.R.string.ChannelMessageFew, args[0], LocaleController.formatPluralString("ForwardedMessageCount", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]).toLowerCase());
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case ',':
                                                                                                    messageText = LocaleController.formatString("ChannelMessageFew", org.telegram.messenger.beta.R.string.ChannelMessageFew, args[0], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '-':
                                                                                                    messageText = LocaleController.formatString("ChannelMessageFew", org.telegram.messenger.beta.R.string.ChannelMessageFew, args[0], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '.':
                                                                                                    messageText = LocaleController.formatString("ChannelMessageFew", org.telegram.messenger.beta.R.string.ChannelMessageFew, args[0], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '/':
                                                                                                    messageText = LocaleController.formatString("ChannelMessageFew", org.telegram.messenger.beta.R.string.ChannelMessageFew, args[0], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) args[1]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '0':
                                                                                                    messageText = LocaleController.formatString("ChannelMessageAlbum", org.telegram.messenger.beta.R.string.ChannelMessageAlbum, args[0]);
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '1':
                                                                                                    String messageText35 = LocaleController.formatString("NotificationMessageGroupText", org.telegram.messenger.beta.R.string.NotificationMessageGroupText, args[0], args[1], args[2]);
                                                                                                    message1 = args[2];
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText35;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '2':
                                                                                                    String messageText36 = LocaleController.formatString("NotificationMessageGroupNoText", org.telegram.messenger.beta.R.string.NotificationMessageGroupNoText, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("Message", org.telegram.messenger.beta.R.string.Message);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText36;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '3':
                                                                                                    String messageText37 = LocaleController.formatString("NotificationMessageGroupPhoto", org.telegram.messenger.beta.R.string.NotificationMessageGroupPhoto, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachPhoto", org.telegram.messenger.beta.R.string.AttachPhoto);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText37;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '4':
                                                                                                    String messageText38 = LocaleController.formatString("NotificationMessageGroupVideo", org.telegram.messenger.beta.R.string.NotificationMessageGroupVideo, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachVideo", org.telegram.messenger.beta.R.string.AttachVideo);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText38;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '5':
                                                                                                    String messageText39 = LocaleController.formatString("NotificationMessageGroupRound", org.telegram.messenger.beta.R.string.NotificationMessageGroupRound, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachRound", org.telegram.messenger.beta.R.string.AttachRound);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText39;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '6':
                                                                                                    String messageText40 = LocaleController.formatString("NotificationMessageGroupDocument", org.telegram.messenger.beta.R.string.NotificationMessageGroupDocument, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachDocument", org.telegram.messenger.beta.R.string.AttachDocument);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText40;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '7':
                                                                                                    if (args.length <= 2 || TextUtils.isEmpty(args[2])) {
                                                                                                        message1 = args[1] + " " + LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        messageText = LocaleController.formatString("NotificationMessageGroupSticker", org.telegram.messenger.beta.R.string.NotificationMessageGroupSticker, args[0], args[1]);
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        message1 = args[2] + " " + LocaleController.getString("AttachSticker", org.telegram.messenger.beta.R.string.AttachSticker);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        messageText = LocaleController.formatString("NotificationMessageGroupStickerEmoji", org.telegram.messenger.beta.R.string.NotificationMessageGroupStickerEmoji, args[0], args[1], args[2]);
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case UndoView.ACTION_USERNAME_COPIED /* 56 */:
                                                                                                    String messageText41 = LocaleController.formatString("NotificationMessageGroupAudio", org.telegram.messenger.beta.R.string.NotificationMessageGroupAudio, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachAudio", org.telegram.messenger.beta.R.string.AttachAudio);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText41;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_HASHTAG_COPIED /* 57 */:
                                                                                                    String messageText42 = LocaleController.formatString("NotificationMessageGroupContact2", org.telegram.messenger.beta.R.string.NotificationMessageGroupContact2, args[0], args[1], args[2]);
                                                                                                    message1 = LocaleController.getString("AttachContact", org.telegram.messenger.beta.R.string.AttachContact);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText42;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_TEXT_COPIED /* 58 */:
                                                                                                    String messageText43 = LocaleController.formatString("NotificationMessageGroupQuiz2", org.telegram.messenger.beta.R.string.NotificationMessageGroupQuiz2, args[0], args[1], args[2]);
                                                                                                    message1 = LocaleController.getString("PollQuiz", org.telegram.messenger.beta.R.string.PollQuiz);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText43;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case ';':
                                                                                                    String messageText44 = LocaleController.formatString("NotificationMessageGroupPoll2", org.telegram.messenger.beta.R.string.NotificationMessageGroupPoll2, args[0], args[1], args[2]);
                                                                                                    message1 = LocaleController.getString("Poll", org.telegram.messenger.beta.R.string.Poll);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText44;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_PHONE_COPIED /* 60 */:
                                                                                                    String messageText45 = LocaleController.formatString("NotificationMessageGroupMap", org.telegram.messenger.beta.R.string.NotificationMessageGroupMap, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachLocation", org.telegram.messenger.beta.R.string.AttachLocation);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText45;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_SHARE_BACKGROUND /* 61 */:
                                                                                                    String messageText46 = LocaleController.formatString("NotificationMessageGroupLiveLocation", org.telegram.messenger.beta.R.string.NotificationMessageGroupLiveLocation, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachLiveLocation", org.telegram.messenger.beta.R.string.AttachLiveLocation);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText46;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '>':
                                                                                                    String messageText47 = LocaleController.formatString("NotificationMessageGroupGif", org.telegram.messenger.beta.R.string.NotificationMessageGroupGif, args[0], args[1]);
                                                                                                    message1 = LocaleController.getString("AttachGif", org.telegram.messenger.beta.R.string.AttachGif);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText47;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case HtmlCompat.FROM_HTML_MODE_COMPACT /* 63 */:
                                                                                                    String messageText48 = LocaleController.formatString("NotificationMessageGroupGame", org.telegram.messenger.beta.R.string.NotificationMessageGroupGame, args[0], args[1], args[2]);
                                                                                                    message1 = LocaleController.getString("AttachGame", org.telegram.messenger.beta.R.string.AttachGame);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText48;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case '@':
                                                                                                    messageText = LocaleController.formatString("NotificationMessageGroupGameScored", org.telegram.messenger.beta.R.string.NotificationMessageGroupGameScored, args[0], args[1], args[2], args[3]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case VoIPService.CALL_MIN_LAYER /* 65 */:
                                                                                                    String messageText49 = LocaleController.formatString("NotificationMessageGroupInvoice", org.telegram.messenger.beta.R.string.NotificationMessageGroupInvoice, args[0], args[1], args[2]);
                                                                                                    message1 = LocaleController.getString("PaymentInvoice", org.telegram.messenger.beta.R.string.PaymentInvoice);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = messageText49;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'B':
                                                                                                case 'C':
                                                                                                    messageText = LocaleController.formatString("NotificationInvitedToGroup", org.telegram.messenger.beta.R.string.NotificationInvitedToGroup, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'D':
                                                                                                    messageText = LocaleController.formatString("NotificationEditedGroupName", org.telegram.messenger.beta.R.string.NotificationEditedGroupName, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'E':
                                                                                                    messageText = LocaleController.formatString("NotificationEditedGroupPhoto", org.telegram.messenger.beta.R.string.NotificationEditedGroupPhoto, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_AUTO_DELETE_ON /* 70 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupAddMember", org.telegram.messenger.beta.R.string.NotificationGroupAddMember, args[0], args[1], args[2]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'G':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupCreatedCall", org.telegram.messenger.beta.R.string.NotificationGroupCreatedCall, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'H':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupInvitedToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedToCall, args[0], args[1], args[2]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'I':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupEndedCall", org.telegram.messenger.beta.R.string.NotificationGroupEndedCall, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_REPORT_SENT /* 74 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupInvitedYouToCall", org.telegram.messenger.beta.R.string.NotificationGroupInvitedYouToCall, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_GIGAGROUP_CANCEL /* 75 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupKickMember", org.telegram.messenger.beta.R.string.NotificationGroupKickMember, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_GIGAGROUP_SUCCESS /* 76 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupKickYou", org.telegram.messenger.beta.R.string.NotificationGroupKickYou, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_PAYMENT_SUCCESS /* 77 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupLeftMember", org.telegram.messenger.beta.R.string.NotificationGroupLeftMember, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_PIN_DIALOGS /* 78 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupAddSelf", org.telegram.messenger.beta.R.string.NotificationGroupAddSelf, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_UNPIN_DIALOGS /* 79 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupAddSelfMega", org.telegram.messenger.beta.R.string.NotificationGroupAddSelfMega, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_EMAIL_COPIED /* 80 */:
                                                                                                    messageText = LocaleController.formatString("UserAcceptedToGroupPushWithGroup", org.telegram.messenger.beta.R.string.UserAcceptedToGroupPushWithGroup, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_CLEAR_DATES /* 81 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupForwardedFew", org.telegram.messenger.beta.R.string.NotificationGroupForwardedFew, args[0], args[1], LocaleController.formatPluralString("messages", Utilities.parseInt((CharSequence) args[2]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case UndoView.ACTION_PREVIEW_MEDIA_DESELECTED /* 82 */:
                                                                                                    messageText = LocaleController.formatString("NotificationGroupFew", org.telegram.messenger.beta.R.string.NotificationGroupFew, args[0], args[1], LocaleController.formatPluralString("Photos", Utilities.parseInt((CharSequence) args[2]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'S':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupFew", org.telegram.messenger.beta.R.string.NotificationGroupFew, args[0], args[1], LocaleController.formatPluralString("Videos", Utilities.parseInt((CharSequence) args[2]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'T':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupFew", org.telegram.messenger.beta.R.string.NotificationGroupFew, args[0], args[1], LocaleController.formatPluralString("MusicFiles", Utilities.parseInt((CharSequence) args[2]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'U':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupFew", org.telegram.messenger.beta.R.string.NotificationGroupFew, args[0], args[1], LocaleController.formatPluralString("Files", Utilities.parseInt((CharSequence) args[2]).intValue(), new Object[0]));
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'V':
                                                                                                    messageText = LocaleController.formatString("NotificationGroupAlbum", org.telegram.messenger.beta.R.string.NotificationGroupAlbum, args[0], args[1]);
                                                                                                    name2 = name;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case 'W':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedText", org.telegram.messenger.beta.R.string.NotificationActionPinnedText, args[0], args[1], args[2]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedTextChannel, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'X':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedNoTextUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedNoText", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoText, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedNoTextChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedNoTextChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case TsExtractor.TS_STREAM_TYPE_DVBSUBS /* 89 */:
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedPhotoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedPhoto", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhoto, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedPhotoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedPhotoChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'Z':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedVideoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedVideo", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideo, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedVideoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVideoChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case '[':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedRoundUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedRound", org.telegram.messenger.beta.R.string.NotificationActionPinnedRound, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedRoundChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedRoundChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case '\\':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedFileUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedFile", org.telegram.messenger.beta.R.string.NotificationActionPinnedFile, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedFileChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedFileChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case ']':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        if (args.length <= 1 || TextUtils.isEmpty(args[1])) {
                                                                                                            messageText = LocaleController.formatString("NotificationActionPinnedStickerUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerUser, args[0]);
                                                                                                            name2 = name;
                                                                                                            localMessage = false;
                                                                                                            gcmPushListenerService = this;
                                                                                                            break;
                                                                                                        } else {
                                                                                                            messageText = LocaleController.formatString("NotificationActionPinnedStickerEmojiUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiUser, args[0], args[1]);
                                                                                                            name2 = name;
                                                                                                            localMessage = false;
                                                                                                            gcmPushListenerService = this;
                                                                                                            break;
                                                                                                        }
                                                                                                    } else if (isGroup) {
                                                                                                        if (args.length <= 2 || TextUtils.isEmpty(args[2])) {
                                                                                                            messageText = LocaleController.formatString("NotificationActionPinnedSticker", org.telegram.messenger.beta.R.string.NotificationActionPinnedSticker, args[0], args[1]);
                                                                                                            name2 = name;
                                                                                                            localMessage = false;
                                                                                                            gcmPushListenerService = this;
                                                                                                            break;
                                                                                                        } else {
                                                                                                            messageText = LocaleController.formatString("NotificationActionPinnedStickerEmoji", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmoji, args[0], args[2], args[1]);
                                                                                                            name2 = name;
                                                                                                            localMessage = false;
                                                                                                            gcmPushListenerService = this;
                                                                                                            break;
                                                                                                        }
                                                                                                    } else if (args.length <= 1 || TextUtils.isEmpty(args[1])) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedStickerChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedStickerEmojiChannel, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                    break;
                                                                                                case '^':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedVoiceUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedVoice", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoice, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedVoiceChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedVoiceChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case '_':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedContactUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedContact2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContact2, args[0], args[2], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedContactChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedContactChannel2, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case '`':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedQuizUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedQuiz2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuiz2, args[0], args[2], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedQuizChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedQuizChannel2, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'a':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedPollUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedPoll2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPoll2, args[0], args[2], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedPollChannel2", org.telegram.messenger.beta.R.string.NotificationActionPinnedPollChannel2, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'b':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGeoUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGeo", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeo, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGeoChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'c':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGeoLiveUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGeoLive", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLive, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGeoLiveChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'd':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGameUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGame", org.telegram.messenger.beta.R.string.NotificationActionPinnedGame, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGameChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'e':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGameScoreUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameScoreUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGameScore", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameScore, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGameScoreChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGameScoreChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'f':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedInvoiceUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedInvoiceUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedInvoice", org.telegram.messenger.beta.R.string.NotificationActionPinnedInvoice, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedInvoiceChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedInvoiceChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case 'g':
                                                                                                    if (dialogId4 > 0) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGifUser", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifUser, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else if (isGroup) {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGif", org.telegram.messenger.beta.R.string.NotificationActionPinnedGif, args[0], args[1]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    } else {
                                                                                                        messageText = LocaleController.formatString("NotificationActionPinnedGifChannel", org.telegram.messenger.beta.R.string.NotificationActionPinnedGifChannel, args[0]);
                                                                                                        name2 = name;
                                                                                                        localMessage = false;
                                                                                                        gcmPushListenerService = this;
                                                                                                        break;
                                                                                                    }
                                                                                                case LocationRequest.PRIORITY_LOW_POWER /* 104 */:
                                                                                                    String messageText50 = LocaleController.getString("YouHaveNewMessage", org.telegram.messenger.beta.R.string.YouHaveNewMessage);
                                                                                                    name2 = LocaleController.getString("SecretChatName", org.telegram.messenger.beta.R.string.SecretChatName);
                                                                                                    messageText = messageText50;
                                                                                                    localMessage = true;
                                                                                                    gcmPushListenerService = this;
                                                                                                    break;
                                                                                                case LocationRequest.PRIORITY_NO_POWER /* 105 */:
                                                                                                    gcmPushListenerService = this;
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = null;
                                                                                                    break;
                                                                                                case 'j':
                                                                                                case 'k':
                                                                                                case 'l':
                                                                                                case 'm':
                                                                                                case 'n':
                                                                                                case 'o':
                                                                                                case 'p':
                                                                                                case 'q':
                                                                                                case 'r':
                                                                                                    gcmPushListenerService = this;
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = null;
                                                                                                    break;
                                                                                                default:
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                        FileLog.w("unhandled loc_key = " + loc_key2);
                                                                                                    }
                                                                                                    gcmPushListenerService = this;
                                                                                                    name2 = name;
                                                                                                    localMessage = false;
                                                                                                    messageText = null;
                                                                                                    break;
                                                                                            }
                                                                                            if (messageText != null) {
                                                                                                TLRPC.TL_message messageOwner = new TLRPC.TL_message();
                                                                                                messageOwner.id = msg_id;
                                                                                                messageOwner.random_id = random_id2;
                                                                                                messageOwner.message = message1 != null ? message1 : messageText;
                                                                                                messageOwner.date = (int) (time / 1000);
                                                                                                if (pinned) {
                                                                                                    try {
                                                                                                        messageOwner.action = new TLRPC.TL_messageActionPinMessage();
                                                                                                    } catch (Throwable th3) {
                                                                                                        e = th3;
                                                                                                        jsonString = jsonString;
                                                                                                        currentAccount = currentAccount2;
                                                                                                        if (currentAccount != -1) {
                                                                                                        }
                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                        }
                                                                                                        FileLog.e(e);
                                                                                                        return;
                                                                                                    }
                                                                                                }
                                                                                                if (supergroup2) {
                                                                                                    messageOwner.flags |= Integer.MIN_VALUE;
                                                                                                }
                                                                                                messageOwner.dialog_id = dialogId4;
                                                                                                if (channel_id3 != 0) {
                                                                                                    messageOwner.peer_id = new TLRPC.TL_peerChannel();
                                                                                                    messageOwner.peer_id.channel_id = channel_id3;
                                                                                                    dialogId3 = chat_id3;
                                                                                                } else if (chat_id3 != 0) {
                                                                                                    messageOwner.peer_id = new TLRPC.TL_peerChat();
                                                                                                    dialogId3 = chat_id3;
                                                                                                    messageOwner.peer_id.chat_id = dialogId3;
                                                                                                } else {
                                                                                                    dialogId3 = chat_id3;
                                                                                                    messageOwner.peer_id = new TLRPC.TL_peerUser();
                                                                                                    messageOwner.peer_id.user_id = user_id4;
                                                                                                }
                                                                                                messageOwner.flags |= 256;
                                                                                                if (chat_from_group_id != 0) {
                                                                                                    messageOwner.from_id = new TLRPC.TL_peerChat();
                                                                                                    messageOwner.from_id.chat_id = dialogId3;
                                                                                                    chat_id2 = chat_from_id;
                                                                                                } else if (chat_from_broadcast_id != 0) {
                                                                                                    messageOwner.from_id = new TLRPC.TL_peerChannel();
                                                                                                    messageOwner.from_id.channel_id = chat_from_broadcast_id;
                                                                                                    chat_id2 = chat_from_id;
                                                                                                } else if (chat_from_id != 0) {
                                                                                                    messageOwner.from_id = new TLRPC.TL_peerUser();
                                                                                                    chat_id2 = chat_from_id;
                                                                                                    messageOwner.from_id.user_id = chat_id2;
                                                                                                } else {
                                                                                                    chat_id2 = chat_from_id;
                                                                                                    messageOwner.from_id = messageOwner.peer_id;
                                                                                                }
                                                                                                try {
                                                                                                    if (!mention2 && !pinned) {
                                                                                                        z = false;
                                                                                                        messageOwner.mentioned = z;
                                                                                                        messageOwner.silent = silent3;
                                                                                                        messageOwner.from_scheduled = scheduled3;
                                                                                                        MessageObject messageObject = new MessageObject(currentAccount2, messageOwner, messageText, name2, userName2, localMessage, channel, supergroup2, edited);
                                                                                                        scheduled = loc_key2.startsWith(str);
                                                                                                        if (!scheduled) {
                                                                                                            if (!loc_key2.startsWith(str2)) {
                                                                                                                z2 = false;
                                                                                                                messageObject.isReactionPush = z2;
                                                                                                                ArrayList<MessageObject> arrayList = new ArrayList<>();
                                                                                                                arrayList.add(messageObject);
                                                                                                                loc_key = loc_key2;
                                                                                                                NotificationsController.getInstance(currentAccount2).processNewMessages(arrayList, true, true, gcmPushListenerService.countDownLatch);
                                                                                                                silent = false;
                                                                                                                if (silent) {
                                                                                                                    gcmPushListenerService.countDownLatch.countDown();
                                                                                                                }
                                                                                                                ConnectionsManager.onInternalPushReceived(currentAccount2);
                                                                                                                ConnectionsManager.getInstance(currentAccount2).resumeNetworkMaybe();
                                                                                                                return;
                                                                                                            }
                                                                                                        }
                                                                                                        z2 = true;
                                                                                                        messageObject.isReactionPush = z2;
                                                                                                        ArrayList<MessageObject> arrayList2 = new ArrayList<>();
                                                                                                        arrayList2.add(messageObject);
                                                                                                        loc_key = loc_key2;
                                                                                                        NotificationsController.getInstance(currentAccount2).processNewMessages(arrayList2, true, true, gcmPushListenerService.countDownLatch);
                                                                                                        silent = false;
                                                                                                        if (silent) {
                                                                                                        }
                                                                                                        ConnectionsManager.onInternalPushReceived(currentAccount2);
                                                                                                        ConnectionsManager.getInstance(currentAccount2).resumeNetworkMaybe();
                                                                                                        return;
                                                                                                    }
                                                                                                    NotificationsController.getInstance(currentAccount2).processNewMessages(arrayList2, true, true, gcmPushListenerService.countDownLatch);
                                                                                                    silent = false;
                                                                                                    if (silent) {
                                                                                                    }
                                                                                                    ConnectionsManager.onInternalPushReceived(currentAccount2);
                                                                                                    ConnectionsManager.getInstance(currentAccount2).resumeNetworkMaybe();
                                                                                                    return;
                                                                                                } catch (Throwable th4) {
                                                                                                    e = th4;
                                                                                                    jsonString = jsonString;
                                                                                                    loc_key2 = loc_key;
                                                                                                    currentAccount = currentAccount2;
                                                                                                    if (currentAccount != -1) {
                                                                                                        ConnectionsManager.onInternalPushReceived(currentAccount);
                                                                                                        ConnectionsManager.getInstance(currentAccount).resumeNetworkMaybe();
                                                                                                        gcmPushListenerService.countDownLatch.countDown();
                                                                                                    } else {
                                                                                                        onDecryptError();
                                                                                                    }
                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                        FileLog.e("error in loc_key = " + loc_key2 + " json " + jsonString);
                                                                                                    }
                                                                                                    FileLog.e(e);
                                                                                                    return;
                                                                                                }
                                                                                                z = true;
                                                                                                messageOwner.mentioned = z;
                                                                                                messageOwner.silent = silent3;
                                                                                                messageOwner.from_scheduled = scheduled3;
                                                                                                MessageObject messageObject2 = new MessageObject(currentAccount2, messageOwner, messageText, name2, userName2, localMessage, channel, supergroup2, edited);
                                                                                                scheduled = loc_key2.startsWith(str);
                                                                                                if (!scheduled) {
                                                                                                }
                                                                                                z2 = true;
                                                                                                messageObject2.isReactionPush = z2;
                                                                                                ArrayList<MessageObject> arrayList22 = new ArrayList<>();
                                                                                                arrayList22.add(messageObject2);
                                                                                                loc_key = loc_key2;
                                                                                            } else {
                                                                                                loc_key = loc_key2;
                                                                                            }
                                                                                        }
                                                                                        gcmPushListenerService = this;
                                                                                        messageText = gcmPushListenerService.getReactedText(loc_key2, args);
                                                                                        name2 = name;
                                                                                        localMessage = false;
                                                                                        if (messageText != null) {
                                                                                        }
                                                                                    }
                                                                                    messageText = gcmPushListenerService.getReactedText(loc_key2, args);
                                                                                    name2 = name;
                                                                                    localMessage = false;
                                                                                    if (messageText != null) {
                                                                                    }
                                                                                } catch (Throwable th5) {
                                                                                    e = th5;
                                                                                    jsonString = jsonString;
                                                                                    currentAccount = currentAccount2;
                                                                                    if (currentAccount != -1) {
                                                                                    }
                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                    }
                                                                                    FileLog.e(e);
                                                                                    return;
                                                                                }
                                                                                isGroup = true;
                                                                                if (!custom3.has("mention")) {
                                                                                }
                                                                                if (!custom3.has(NotificationCompat.GROUP_KEY_SILENT)) {
                                                                                }
                                                                                boolean silent32 = silent2;
                                                                                boolean mention22 = mention;
                                                                                if (!json.has("loc_args")) {
                                                                                }
                                                                                String name32 = args[0];
                                                                                boolean edited2 = custom3.has("edit_date");
                                                                                if (loc_key2.startsWith("CHAT_")) {
                                                                                }
                                                                                if (!BuildVars.LOGS_ENABLED) {
                                                                                }
                                                                                if (!loc_key2.startsWith("REACT_")) {
                                                                                }
                                                                                gcmPushListenerService = this;
                                                                            } else {
                                                                                gcmPushListenerService = this;
                                                                                loc_key = loc_key2;
                                                                            }
                                                                        } else {
                                                                            gcmPushListenerService = this;
                                                                            loc_key = loc_key2;
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                gcmPushListenerService = this;
                                                                loc_key = loc_key2;
                                                                canRelease = true;
                                                            }
                                                            silent = canRelease;
                                                            if (silent) {
                                                            }
                                                            ConnectionsManager.onInternalPushReceived(currentAccount2);
                                                            ConnectionsManager.getInstance(currentAccount2).resumeNetworkMaybe();
                                                            return;
                                                        } catch (Throwable th6) {
                                                            gcmPushListenerService = this;
                                                            e = th6;
                                                            jsonString = jsonString;
                                                            currentAccount = currentAccount2;
                                                        }
                                                        break;
                                                }
                                            } else {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    FileLog.d("GCM ACCOUNT NOT ACTIVATED");
                                                }
                                                this.countDownLatch.countDown();
                                            }
                                        } catch (Throwable th7) {
                                            gcmPushListenerService = this;
                                            jsonString = jsonString;
                                            currentAccount = currentAccount2;
                                            e = th7;
                                        }
                                    } catch (Throwable th8) {
                                        gcmPushListenerService = this;
                                        e = th8;
                                        jsonString = jsonString;
                                        currentAccount = currentAccount2;
                                    }
                                } catch (Throwable th9) {
                                    gcmPushListenerService = this;
                                    e = th9;
                                    jsonString = jsonString;
                                    currentAccount = -1;
                                }
                            } catch (Throwable th10) {
                                gcmPushListenerService = this;
                                e = th10;
                                currentAccount = -1;
                            }
                        } catch (Throwable th11) {
                            gcmPushListenerService = this;
                            e = th11;
                            currentAccount = -1;
                        }
                    } catch (Throwable th12) {
                        gcmPushListenerService = this;
                        currentAccount = -1;
                        e = th12;
                    }
                } catch (Throwable th13) {
                    gcmPushListenerService = this;
                    e = th13;
                    currentAccount = -1;
                }
            } catch (Throwable th14) {
                gcmPushListenerService = this;
                e = th14;
            }
        } catch (Throwable th15) {
            gcmPushListenerService = this;
            e = th15;
        }
    }

    public static /* synthetic */ void lambda$onMessageReceived$1(int accountFinal) {
        if (UserConfig.getInstance(accountFinal).getClientUserId() != 0) {
            UserConfig.getInstance(accountFinal).clearConfig();
            MessagesController.getInstance(accountFinal).performLogout(0);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private String getReactedText(String loc_key, Object[] args) {
        char c;
        switch (loc_key.hashCode()) {
            case -2114646919:
                if (loc_key.equals("CHAT_REACT_CONTACT")) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case -1891797827:
                if (loc_key.equals("REACT_GEOLIVE")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1415696683:
                if (loc_key.equals("CHAT_REACT_NOTEXT")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -1375264434:
                if (loc_key.equals("REACT_NOTEXT")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1105974394:
                if (loc_key.equals("CHAT_REACT_INVOICE")) {
                    c = 30;
                    break;
                }
                c = 65535;
                break;
            case -861247200:
                if (loc_key.equals("REACT_CONTACT")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -661458538:
                if (loc_key.equals("CHAT_REACT_STICKER")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 51977938:
                if (loc_key.equals("REACT_GAME")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 52259487:
                if (loc_key.equals("REACT_POLL")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 52294965:
                if (loc_key.equals("REACT_QUIZ")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 52369421:
                if (loc_key.equals("REACT_TEXT")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 147425325:
                if (loc_key.equals("REACT_INVOICE")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 192842257:
                if (loc_key.equals("CHAT_REACT_DOC")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 192844842:
                if (loc_key.equals("CHAT_REACT_GEO")) {
                    c = 25;
                    break;
                }
                c = 65535;
                break;
            case 192844957:
                if (loc_key.equals("CHAT_REACT_GIF")) {
                    c = 31;
                    break;
                }
                c = 65535;
                break;
            case 591941181:
                if (loc_key.equals("REACT_STICKER")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 635226735:
                if (loc_key.equals("CHAT_REACT_AUDIO")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case 648703179:
                if (loc_key.equals("CHAT_REACT_PHOTO")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 650764327:
                if (loc_key.equals("CHAT_REACT_ROUND")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 654263060:
                if (loc_key.equals("CHAT_REACT_VIDEO")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 1149769750:
                if (loc_key.equals("CHAT_REACT_GEOLIVE")) {
                    c = 26;
                    break;
                }
                c = 65535;
                break;
            case 1606362326:
                if (loc_key.equals("REACT_AUDIO")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1619838770:
                if (loc_key.equals("REACT_PHOTO")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1621899918:
                if (loc_key.equals("REACT_ROUND")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1625398651:
                if (loc_key.equals("REACT_VIDEO")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1664242232:
                if (loc_key.equals("REACT_DOC")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1664244817:
                if (loc_key.equals("REACT_GEO")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1664244932:
                if (loc_key.equals("REACT_GIF")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 1683218969:
                if (loc_key.equals("CHAT_REACT_GAME")) {
                    c = 29;
                    break;
                }
                c = 65535;
                break;
            case 1683500518:
                if (loc_key.equals("CHAT_REACT_POLL")) {
                    c = 27;
                    break;
                }
                c = 65535;
                break;
            case 1683535996:
                if (loc_key.equals("CHAT_REACT_QUIZ")) {
                    c = 28;
                    break;
                }
                c = 65535;
                break;
            case 1683610452:
                if (loc_key.equals("CHAT_REACT_TEXT")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return LocaleController.formatString("PushReactText", org.telegram.messenger.beta.R.string.PushReactText, args);
            case 1:
                return LocaleController.formatString("PushReactNoText", org.telegram.messenger.beta.R.string.PushReactNoText, args);
            case 2:
                return LocaleController.formatString("PushReactPhoto", org.telegram.messenger.beta.R.string.PushReactPhoto, args);
            case 3:
                return LocaleController.formatString("PushReactVideo", org.telegram.messenger.beta.R.string.PushReactVideo, args);
            case 4:
                return LocaleController.formatString("PushReactRound", org.telegram.messenger.beta.R.string.PushReactRound, args);
            case 5:
                return LocaleController.formatString("PushReactDoc", org.telegram.messenger.beta.R.string.PushReactDoc, args);
            case 6:
                return LocaleController.formatString("PushReactSticker", org.telegram.messenger.beta.R.string.PushReactSticker, args);
            case 7:
                return LocaleController.formatString("PushReactAudio", org.telegram.messenger.beta.R.string.PushReactAudio, args);
            case '\b':
                return LocaleController.formatString("PushReactContect", org.telegram.messenger.beta.R.string.PushReactContect, args);
            case '\t':
                return LocaleController.formatString("PushReactGeo", org.telegram.messenger.beta.R.string.PushReactGeo, args);
            case '\n':
                return LocaleController.formatString("PushReactGeoLocation", org.telegram.messenger.beta.R.string.PushReactGeoLocation, args);
            case 11:
                return LocaleController.formatString("PushReactPoll", org.telegram.messenger.beta.R.string.PushReactPoll, args);
            case '\f':
                return LocaleController.formatString("PushReactQuiz", org.telegram.messenger.beta.R.string.PushReactQuiz, args);
            case '\r':
                return LocaleController.formatString("PushReactGame", org.telegram.messenger.beta.R.string.PushReactGame, args);
            case 14:
                return LocaleController.formatString("PushReactInvoice", org.telegram.messenger.beta.R.string.PushReactInvoice, args);
            case 15:
                return LocaleController.formatString("PushReactGif", org.telegram.messenger.beta.R.string.PushReactGif, args);
            case 16:
                return LocaleController.formatString("PushChatReactText", org.telegram.messenger.beta.R.string.PushChatReactText, args);
            case 17:
                return LocaleController.formatString("PushChatReactNotext", org.telegram.messenger.beta.R.string.PushChatReactNotext, args);
            case 18:
                return LocaleController.formatString("PushChatReactPhoto", org.telegram.messenger.beta.R.string.PushChatReactPhoto, args);
            case 19:
                return LocaleController.formatString("PushChatReactVideo", org.telegram.messenger.beta.R.string.PushChatReactVideo, args);
            case 20:
                return LocaleController.formatString("PushChatReactRound", org.telegram.messenger.beta.R.string.PushChatReactRound, args);
            case 21:
                return LocaleController.formatString("PushChatReactDoc", org.telegram.messenger.beta.R.string.PushChatReactDoc, args);
            case 22:
                return LocaleController.formatString("PushChatReactSticker", org.telegram.messenger.beta.R.string.PushChatReactSticker, args);
            case 23:
                return LocaleController.formatString("PushChatReactAudio", org.telegram.messenger.beta.R.string.PushChatReactAudio, args);
            case 24:
                return LocaleController.formatString("PushChatReactContact", org.telegram.messenger.beta.R.string.PushChatReactContact, args);
            case 25:
                return LocaleController.formatString("PushChatReactGeo", org.telegram.messenger.beta.R.string.PushChatReactGeo, args);
            case 26:
                return LocaleController.formatString("PushChatReactGeoLive", org.telegram.messenger.beta.R.string.PushChatReactGeoLive, args);
            case 27:
                return LocaleController.formatString("PushChatReactPoll", org.telegram.messenger.beta.R.string.PushChatReactPoll, args);
            case 28:
                return LocaleController.formatString("PushChatReactQuiz", org.telegram.messenger.beta.R.string.PushChatReactQuiz, args);
            case NalUnitTypes.NAL_TYPE_RSV_VCL29 /* 29 */:
                return LocaleController.formatString("PushChatReactGame", org.telegram.messenger.beta.R.string.PushChatReactGame, args);
            case 30:
                return LocaleController.formatString("PushChatReactInvoice", org.telegram.messenger.beta.R.string.PushChatReactInvoice, args);
            case 31:
                return LocaleController.formatString("PushChatReactGif", org.telegram.messenger.beta.R.string.PushChatReactGif, args);
            default:
                return null;
        }
    }

    private void onDecryptError() {
        for (int a = 0; a < 4; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                ConnectionsManager.onInternalPushReceived(a);
                ConnectionsManager.getInstance(a).resumeNetworkMaybe();
            }
        }
        this.countDownLatch.countDown();
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(final String token) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                GcmPushListenerService.lambda$onNewToken$5(token);
            }
        });
    }

    public static /* synthetic */ void lambda$onNewToken$5(String token) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("Refreshed token: " + token);
        }
        ApplicationLoader.postInitApplication();
        sendRegistrationToServer(token);
    }

    public static void sendRegistrationToServer(final String token) {
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                GcmPushListenerService.lambda$sendRegistrationToServer$9(token);
            }
        });
    }

    public static /* synthetic */ void lambda$sendRegistrationToServer$9(final String token) {
        ConnectionsManager.setRegId(token, SharedConfig.pushStringStatus);
        if (token == null) {
            return;
        }
        boolean sendStat = false;
        if (SharedConfig.pushStringGetTimeStart != 0 && SharedConfig.pushStringGetTimeEnd != 0 && (!SharedConfig.pushStatSent || !TextUtils.equals(SharedConfig.pushString, token))) {
            sendStat = true;
            SharedConfig.pushStatSent = false;
        }
        SharedConfig.pushString = token;
        for (int a = 0; a < 4; a++) {
            UserConfig userConfig = UserConfig.getInstance(a);
            userConfig.registeredForPush = false;
            userConfig.saveConfig(false);
            if (userConfig.getClientUserId() != 0) {
                final int currentAccount = a;
                if (sendStat) {
                    TLRPC.TL_help_saveAppLog req = new TLRPC.TL_help_saveAppLog();
                    TLRPC.TL_inputAppEvent event = new TLRPC.TL_inputAppEvent();
                    event.time = SharedConfig.pushStringGetTimeStart;
                    event.type = "fcm_token_request";
                    event.peer = 0L;
                    event.data = new TLRPC.TL_jsonNull();
                    req.events.add(event);
                    TLRPC.TL_inputAppEvent event2 = new TLRPC.TL_inputAppEvent();
                    event2.time = SharedConfig.pushStringGetTimeEnd;
                    event2.type = "fcm_token_response";
                    event2.peer = SharedConfig.pushStringGetTimeEnd - SharedConfig.pushStringGetTimeStart;
                    event2.data = new TLRPC.TL_jsonNull();
                    req.events.add(event2);
                    sendStat = false;
                    ConnectionsManager.getInstance(currentAccount).sendRequest(req, GcmPushListenerService$$ExternalSyntheticLambda9.INSTANCE);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.GcmPushListenerService$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MessagesController.getInstance(currentAccount).registerForPush(token);
                    }
                });
            }
        }
    }

    public static /* synthetic */ void lambda$sendRegistrationToServer$6(TLRPC.TL_error error) {
        if (error != null) {
            SharedConfig.pushStatSent = true;
            SharedConfig.saveConfig();
        }
    }
}
