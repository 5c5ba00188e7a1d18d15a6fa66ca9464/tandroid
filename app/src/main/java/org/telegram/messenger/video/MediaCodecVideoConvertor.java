package org.telegram.messenger.video;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
/* loaded from: classes.dex */
public class MediaCodecVideoConvertor {
    private static final int MEDIACODEC_TIMEOUT_DEFAULT = 2500;
    private static final int MEDIACODEC_TIMEOUT_INCREASED = 22000;
    private static final int PROCESSOR_TYPE_INTEL = 2;
    private static final int PROCESSOR_TYPE_MTK = 3;
    private static final int PROCESSOR_TYPE_OTHER = 0;
    private static final int PROCESSOR_TYPE_QCOM = 1;
    private static final int PROCESSOR_TYPE_SEC = 4;
    private static final int PROCESSOR_TYPE_TI = 5;
    private MediaController.VideoConvertorListener callback;
    private long endPresentationTime;
    private MediaExtractor extractor;
    private MP4Builder mediaMuxer;

    public boolean convertVideo(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, boolean z2, long j4, MediaController.SavedFilterState savedFilterState, String str2, ArrayList<VideoEditedInfo.MediaEntity> arrayList, boolean z3, MediaController.CropState cropState, boolean z4, MediaController.VideoConvertorListener videoConvertorListener) {
        this.callback = videoConvertorListener;
        return convertVideoInternal(str, file, i, z, i2, i3, i4, i5, i6, i7, i8, j, j2, j3, j4, z2, false, savedFilterState, str2, arrayList, z3, cropState, z4);
    }

    public long getLastFrameTimestamp() {
        return this.endPresentationTime;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:(25:(2:392|393)(2:399|(47:401|402|1260|403|404|1308|(3:413|(1:418)(1:417)|419)(1:425)|426|(1:428)|429|430|(3:432|433|(39:435|(1:437)|1284|438|439|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(8:454|1314|455|456|1320|457|458|(23:460|1187|461|1235|479|480|1217|481|482|(2:1163|484)(1:488)|489|(7:1270|491|(3:1257|493|(4:495|499|(1:501)|(11:(9:1318|504|1231|505|(1:510)|511|512|(2:514|515)(2:1213|516)|517)(12:524|1250|525|526|(3:528|1240|529)(2:532|533)|534|1151|535|536|1282|537|538)|(1:558)(1:559)|1255|560|561|(2:(6:1262|566|(1:576)(4:569|1177|570|571)|(5:1298|578|579|(5:581|1258|582|(4:584|(1:586)(1:587)|588|(1:590)(1:591))(1:592)|593)(5:597|(2:599|(1:(16:1160|602|603|(4:1206|605|606|(3:608|1209|609))(1:614)|615|1149|616|617|(1:619)|620|(1:622)(2:623|624)|625|(3:632|633|(10:637|1288|638|639|(1:641)|642|1296|643|649|663))|648|649|663))(3:659|(1:662)|663))|660|(0)|663)|(2:667|668))(1:678)|679|(1:(11:1165|683|(1:685)|686|1169|687|688|(1:690)(2:692|(4:1328|694|(1:696)|697)(2:704|(3:706|(1:730)(7:709|710|1306|711|(3:713|714|(5:716|1300|717|718|723))(1:721)|722|723)|731)(3:732|1138|(4:734|735|(1:737)(1:738)|(12:740|741|(12:1276|743|744|(5:(1:753)(3:749|1191|750)|(3:757|(2:759|(2:760|(1:1365)(3:762|(2:773|1366)(2:768|(2:1364|772))|774)))(0)|775)|1219|776|(2:778|(5:780|1233|781|(1:783)|784)))(2:788|(14:790|(3:794|(2:800|(2:1371|802)(1:1375))|803)|804|805|(1:808)|809|810|1302|818|(1:820)(1:821)|822|823|(3:1360|825|1362)(5:1358|(7:827|1286|828|829|(1:831)(2:832|(2:834|(2:836|(1:838))(1:(20:845|(1:847)(1:848)|849|850|(1:857)(3:854|855|856)|858|(4:860|1145|861|(6:863|864|1143|865|866|(16:868|(3:1252|870|871)(4:872|873|874|875)|876|1330|877|878|892|(4:894|1158|895|(1:899))(1:902)|903|(1:905)(1:906)|907|(1:922)(2:911|(3:913|(1:915)(1:916)|917)(3:918|(1:920)|921))|(1:924)(4:925|(1:929)|1244|930)|(8:932|933|(1:935)(1:936)|1136|937|938|(4:943|944|1256|945)(1:946)|947)(1:948)|949|(3:951|(1:953)|954)(1:955))(13:883|891|892|(0)(0)|903|(0)(0)|907|(1:909)|922|(0)(0)|(0)(0)|949|(0)(0))))(1:889)|890|891|892|(0)(0)|903|(0)(0)|907|(0)|922|(0)(0)|(0)(0)|949|(0)(0))(1:1353))))|972|1363)(1:970)|971|972|1363)|1361))|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(1:816)|817|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(4:1355|977|978|979))(3:1354|980|981))))|691|(0)(0)|1361)))|682)|565|1237|1060|(1:1062)|1063)(1:546)))|498|499|(0)|(0)(0))(1:555)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(1:464))(1:477)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063))(1:444)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(6:407|1274|408|409|1264|410))|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)|1167|446|447|1147|448|1199|449|1175|450|451) */
    /* JADX WARN: Can't wrap try/catch for region: R(25:(2:392|393)(2:399|(47:401|402|1260|403|404|1308|(3:413|(1:418)(1:417)|419)(1:425)|426|(1:428)|429|430|(3:432|433|(39:435|(1:437)|1284|438|439|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(8:454|1314|455|456|1320|457|458|(23:460|1187|461|1235|479|480|1217|481|482|(2:1163|484)(1:488)|489|(7:1270|491|(3:1257|493|(4:495|499|(1:501)|(11:(9:1318|504|1231|505|(1:510)|511|512|(2:514|515)(2:1213|516)|517)(12:524|1250|525|526|(3:528|1240|529)(2:532|533)|534|1151|535|536|1282|537|538)|(1:558)(1:559)|1255|560|561|(2:(6:1262|566|(1:576)(4:569|1177|570|571)|(5:1298|578|579|(5:581|1258|582|(4:584|(1:586)(1:587)|588|(1:590)(1:591))(1:592)|593)(5:597|(2:599|(1:(16:1160|602|603|(4:1206|605|606|(3:608|1209|609))(1:614)|615|1149|616|617|(1:619)|620|(1:622)(2:623|624)|625|(3:632|633|(10:637|1288|638|639|(1:641)|642|1296|643|649|663))|648|649|663))(3:659|(1:662)|663))|660|(0)|663)|(2:667|668))(1:678)|679|(1:(11:1165|683|(1:685)|686|1169|687|688|(1:690)(2:692|(4:1328|694|(1:696)|697)(2:704|(3:706|(1:730)(7:709|710|1306|711|(3:713|714|(5:716|1300|717|718|723))(1:721)|722|723)|731)(3:732|1138|(4:734|735|(1:737)(1:738)|(12:740|741|(12:1276|743|744|(5:(1:753)(3:749|1191|750)|(3:757|(2:759|(2:760|(1:1365)(3:762|(2:773|1366)(2:768|(2:1364|772))|774)))(0)|775)|1219|776|(2:778|(5:780|1233|781|(1:783)|784)))(2:788|(14:790|(3:794|(2:800|(2:1371|802)(1:1375))|803)|804|805|(1:808)|809|810|1302|818|(1:820)(1:821)|822|823|(3:1360|825|1362)(5:1358|(7:827|1286|828|829|(1:831)(2:832|(2:834|(2:836|(1:838))(1:(20:845|(1:847)(1:848)|849|850|(1:857)(3:854|855|856)|858|(4:860|1145|861|(6:863|864|1143|865|866|(16:868|(3:1252|870|871)(4:872|873|874|875)|876|1330|877|878|892|(4:894|1158|895|(1:899))(1:902)|903|(1:905)(1:906)|907|(1:922)(2:911|(3:913|(1:915)(1:916)|917)(3:918|(1:920)|921))|(1:924)(4:925|(1:929)|1244|930)|(8:932|933|(1:935)(1:936)|1136|937|938|(4:943|944|1256|945)(1:946)|947)(1:948)|949|(3:951|(1:953)|954)(1:955))(13:883|891|892|(0)(0)|903|(0)(0)|907|(1:909)|922|(0)(0)|(0)(0)|949|(0)(0))))(1:889)|890|891|892|(0)(0)|903|(0)(0)|907|(0)|922|(0)(0)|(0)(0)|949|(0)(0))(1:1353))))|972|1363)(1:970)|971|972|1363)|1361))|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(1:816)|817|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(4:1355|977|978|979))(3:1354|980|981))))|691|(0)(0)|1361)))|682)|565|1237|1060|(1:1062)|1063)(1:546)))|498|499|(0)|(0)(0))(1:555)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(1:464))(1:477)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063))(1:444)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(6:407|1274|408|409|1264|410))|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063) */
    /* JADX WARN: Can't wrap try/catch for region: R(35:(2:392|393)(2:399|(47:401|402|1260|403|404|1308|(3:413|(1:418)(1:417)|419)(1:425)|426|(1:428)|429|430|(3:432|433|(39:435|(1:437)|1284|438|439|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(8:454|1314|455|456|1320|457|458|(23:460|1187|461|1235|479|480|1217|481|482|(2:1163|484)(1:488)|489|(7:1270|491|(3:1257|493|(4:495|499|(1:501)|(11:(9:1318|504|1231|505|(1:510)|511|512|(2:514|515)(2:1213|516)|517)(12:524|1250|525|526|(3:528|1240|529)(2:532|533)|534|1151|535|536|1282|537|538)|(1:558)(1:559)|1255|560|561|(2:(6:1262|566|(1:576)(4:569|1177|570|571)|(5:1298|578|579|(5:581|1258|582|(4:584|(1:586)(1:587)|588|(1:590)(1:591))(1:592)|593)(5:597|(2:599|(1:(16:1160|602|603|(4:1206|605|606|(3:608|1209|609))(1:614)|615|1149|616|617|(1:619)|620|(1:622)(2:623|624)|625|(3:632|633|(10:637|1288|638|639|(1:641)|642|1296|643|649|663))|648|649|663))(3:659|(1:662)|663))|660|(0)|663)|(2:667|668))(1:678)|679|(1:(11:1165|683|(1:685)|686|1169|687|688|(1:690)(2:692|(4:1328|694|(1:696)|697)(2:704|(3:706|(1:730)(7:709|710|1306|711|(3:713|714|(5:716|1300|717|718|723))(1:721)|722|723)|731)(3:732|1138|(4:734|735|(1:737)(1:738)|(12:740|741|(12:1276|743|744|(5:(1:753)(3:749|1191|750)|(3:757|(2:759|(2:760|(1:1365)(3:762|(2:773|1366)(2:768|(2:1364|772))|774)))(0)|775)|1219|776|(2:778|(5:780|1233|781|(1:783)|784)))(2:788|(14:790|(3:794|(2:800|(2:1371|802)(1:1375))|803)|804|805|(1:808)|809|810|1302|818|(1:820)(1:821)|822|823|(3:1360|825|1362)(5:1358|(7:827|1286|828|829|(1:831)(2:832|(2:834|(2:836|(1:838))(1:(20:845|(1:847)(1:848)|849|850|(1:857)(3:854|855|856)|858|(4:860|1145|861|(6:863|864|1143|865|866|(16:868|(3:1252|870|871)(4:872|873|874|875)|876|1330|877|878|892|(4:894|1158|895|(1:899))(1:902)|903|(1:905)(1:906)|907|(1:922)(2:911|(3:913|(1:915)(1:916)|917)(3:918|(1:920)|921))|(1:924)(4:925|(1:929)|1244|930)|(8:932|933|(1:935)(1:936)|1136|937|938|(4:943|944|1256|945)(1:946)|947)(1:948)|949|(3:951|(1:953)|954)(1:955))(13:883|891|892|(0)(0)|903|(0)(0)|907|(1:909)|922|(0)(0)|(0)(0)|949|(0)(0))))(1:889)|890|891|892|(0)(0)|903|(0)(0)|907|(0)|922|(0)(0)|(0)(0)|949|(0)(0))(1:1353))))|972|1363)(1:970)|971|972|1363)|1361))|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(1:816)|817|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(4:1355|977|978|979))(3:1354|980|981))))|691|(0)(0)|1361)))|682)|565|1237|1060|(1:1062)|1063)(1:546)))|498|499|(0)|(0)(0))(1:555)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(1:464))(1:477)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063))(1:444)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(6:407|1274|408|409|1264|410))|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063) */
    /* JADX WARN: Can't wrap try/catch for region: R(46:350|(14:1290|351|352|(3:354|1294|355)(2:361|362)|356|363|364|(3:366|(1:368)(2:369|(1:371)(1:372))|373)(1:(1:375)(1:376))|377|(2:1272|379)|386|(1:388)(1:389)|390|1162)|(2:392|393)(2:399|(47:401|402|1260|403|404|1308|(3:413|(1:418)(1:417)|419)(1:425)|426|(1:428)|429|430|(3:432|433|(39:435|(1:437)|1284|438|439|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(8:454|1314|455|456|1320|457|458|(23:460|1187|461|1235|479|480|1217|481|482|(2:1163|484)(1:488)|489|(7:1270|491|(3:1257|493|(4:495|499|(1:501)|(11:(9:1318|504|1231|505|(1:510)|511|512|(2:514|515)(2:1213|516)|517)(12:524|1250|525|526|(3:528|1240|529)(2:532|533)|534|1151|535|536|1282|537|538)|(1:558)(1:559)|1255|560|561|(2:(6:1262|566|(1:576)(4:569|1177|570|571)|(5:1298|578|579|(5:581|1258|582|(4:584|(1:586)(1:587)|588|(1:590)(1:591))(1:592)|593)(5:597|(2:599|(1:(16:1160|602|603|(4:1206|605|606|(3:608|1209|609))(1:614)|615|1149|616|617|(1:619)|620|(1:622)(2:623|624)|625|(3:632|633|(10:637|1288|638|639|(1:641)|642|1296|643|649|663))|648|649|663))(3:659|(1:662)|663))|660|(0)|663)|(2:667|668))(1:678)|679|(1:(11:1165|683|(1:685)|686|1169|687|688|(1:690)(2:692|(4:1328|694|(1:696)|697)(2:704|(3:706|(1:730)(7:709|710|1306|711|(3:713|714|(5:716|1300|717|718|723))(1:721)|722|723)|731)(3:732|1138|(4:734|735|(1:737)(1:738)|(12:740|741|(12:1276|743|744|(5:(1:753)(3:749|1191|750)|(3:757|(2:759|(2:760|(1:1365)(3:762|(2:773|1366)(2:768|(2:1364|772))|774)))(0)|775)|1219|776|(2:778|(5:780|1233|781|(1:783)|784)))(2:788|(14:790|(3:794|(2:800|(2:1371|802)(1:1375))|803)|804|805|(1:808)|809|810|1302|818|(1:820)(1:821)|822|823|(3:1360|825|1362)(5:1358|(7:827|1286|828|829|(1:831)(2:832|(2:834|(2:836|(1:838))(1:(20:845|(1:847)(1:848)|849|850|(1:857)(3:854|855|856)|858|(4:860|1145|861|(6:863|864|1143|865|866|(16:868|(3:1252|870|871)(4:872|873|874|875)|876|1330|877|878|892|(4:894|1158|895|(1:899))(1:902)|903|(1:905)(1:906)|907|(1:922)(2:911|(3:913|(1:915)(1:916)|917)(3:918|(1:920)|921))|(1:924)(4:925|(1:929)|1244|930)|(8:932|933|(1:935)(1:936)|1136|937|938|(4:943|944|1256|945)(1:946)|947)(1:948)|949|(3:951|(1:953)|954)(1:955))(13:883|891|892|(0)(0)|903|(0)(0)|907|(1:909)|922|(0)(0)|(0)(0)|949|(0)(0))))(1:889)|890|891|892|(0)(0)|903|(0)(0)|907|(0)|922|(0)(0)|(0)(0)|949|(0)(0))(1:1353))))|972|1363)(1:970)|971|972|1363)|1361))|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(1:816)|817|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(4:1355|977|978|979))(3:1354|980|981))))|691|(0)(0)|1361)))|682)|565|1237|1060|(1:1062)|1063)(1:546)))|498|499|(0)|(0)(0))(1:555)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(1:464))(1:477)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063))(1:444)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(6:407|1274|408|409|1264|410))|411|1308|(0)(0)|426|(0)|429|430|(0)(0)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063) */
    /* JADX WARN: Can't wrap try/catch for region: R(59:350|1290|351|352|(3:354|1294|355)(2:361|362)|356|363|364|(3:366|(1:368)(2:369|(1:371)(1:372))|373)(1:(1:375)(1:376))|377|(2:1272|379)|386|(1:388)(1:389)|390|1162|(2:392|393)(2:399|(47:401|402|1260|403|404|1308|(3:413|(1:418)(1:417)|419)(1:425)|426|(1:428)|429|430|(3:432|433|(39:435|(1:437)|1284|438|439|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(8:454|1314|455|456|1320|457|458|(23:460|1187|461|1235|479|480|1217|481|482|(2:1163|484)(1:488)|489|(7:1270|491|(3:1257|493|(4:495|499|(1:501)|(11:(9:1318|504|1231|505|(1:510)|511|512|(2:514|515)(2:1213|516)|517)(12:524|1250|525|526|(3:528|1240|529)(2:532|533)|534|1151|535|536|1282|537|538)|(1:558)(1:559)|1255|560|561|(2:(6:1262|566|(1:576)(4:569|1177|570|571)|(5:1298|578|579|(5:581|1258|582|(4:584|(1:586)(1:587)|588|(1:590)(1:591))(1:592)|593)(5:597|(2:599|(1:(16:1160|602|603|(4:1206|605|606|(3:608|1209|609))(1:614)|615|1149|616|617|(1:619)|620|(1:622)(2:623|624)|625|(3:632|633|(10:637|1288|638|639|(1:641)|642|1296|643|649|663))|648|649|663))(3:659|(1:662)|663))|660|(0)|663)|(2:667|668))(1:678)|679|(1:(11:1165|683|(1:685)|686|1169|687|688|(1:690)(2:692|(4:1328|694|(1:696)|697)(2:704|(3:706|(1:730)(7:709|710|1306|711|(3:713|714|(5:716|1300|717|718|723))(1:721)|722|723)|731)(3:732|1138|(4:734|735|(1:737)(1:738)|(12:740|741|(12:1276|743|744|(5:(1:753)(3:749|1191|750)|(3:757|(2:759|(2:760|(1:1365)(3:762|(2:773|1366)(2:768|(2:1364|772))|774)))(0)|775)|1219|776|(2:778|(5:780|1233|781|(1:783)|784)))(2:788|(14:790|(3:794|(2:800|(2:1371|802)(1:1375))|803)|804|805|(1:808)|809|810|1302|818|(1:820)(1:821)|822|823|(3:1360|825|1362)(5:1358|(7:827|1286|828|829|(1:831)(2:832|(2:834|(2:836|(1:838))(1:(20:845|(1:847)(1:848)|849|850|(1:857)(3:854|855|856)|858|(4:860|1145|861|(6:863|864|1143|865|866|(16:868|(3:1252|870|871)(4:872|873|874|875)|876|1330|877|878|892|(4:894|1158|895|(1:899))(1:902)|903|(1:905)(1:906)|907|(1:922)(2:911|(3:913|(1:915)(1:916)|917)(3:918|(1:920)|921))|(1:924)(4:925|(1:929)|1244|930)|(8:932|933|(1:935)(1:936)|1136|937|938|(4:943|944|1256|945)(1:946)|947)(1:948)|949|(3:951|(1:953)|954)(1:955))(13:883|891|892|(0)(0)|903|(0)(0)|907|(1:909)|922|(0)(0)|(0)(0)|949|(0)(0))))(1:889)|890|891|892|(0)(0)|903|(0)(0)|907|(0)|922|(0)(0)|(0)(0)|949|(0)(0))(1:1353))))|972|1363)(1:970)|971|972|1363)|1361))|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(1:816)|817|785|1302|818|(0)(0)|822|823|(0)(0)|1361)(4:1355|977|978|979))(3:1354|980|981))))|691|(0)(0)|1361)))|682)|565|1237|1060|(1:1062)|1063)(1:546)))|498|499|(0)|(0)(0))(1:555)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(1:464))(1:477)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063))(1:444)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063)(6:407|1274|408|409|1264|410))|411|1308|(0)(0)|426|(0)|429|430|(0)(0)|445|1167|446|447|1147|448|1199|449|1175|450|451|1193|452|(0)(0)|478|1235|479|480|1217|481|482|(0)(0)|489|(0)(0)|556|(0)(0)|1255|560|561|(9:(0)|1262|566|(0)|576|(0)(0)|679|(12:(0)|1165|683|(0)|686|1169|687|688|(0)(0)|691|(0)(0)|1361)|682)|565|1237|1060|(0)|1063) */
    /* JADX WARN: Code restructure failed: missing block: B:1005:0x134e, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1006:0x134f, code lost:
        r10 = r87;
        r5 = r88;
        r69 = r9;
        r23 = r14;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
        r3 = r21;
        r13 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1007:0x1368, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1008:0x1369, code lost:
        r10 = r87;
        r5 = r88;
        r23 = r14;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1009:0x137c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1010:0x137d, code lost:
        r10 = r87;
        r5 = r88;
        r23 = r14;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
        r8 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1011:0x1391, code lost:
        r3 = r21;
        r13 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1014:0x139c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1015:0x139d, code lost:
        r10 = r87;
        r5 = r88;
        r14 = r2;
        r71 = r55;
        r72 = r92;
        r44 = r94;
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1016:0x13af, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1017:0x13b0, code lost:
        r10 = r87;
        r94 = r14;
        r71 = r30;
        r15 = r78;
        r14 = r2;
        r72 = r92;
        r44 = r94;
        r1 = r0;
        r54 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1018:0x13c1, code lost:
        r8 = r14;
        r3 = r21;
        r13 = -5;
        r23 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1019:0x13c7, code lost:
        r69 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1021:0x13cc, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1022:0x13cd, code lost:
        r10 = r87;
        r71 = r30;
        r15 = r78;
        r72 = r92;
        r44 = r14;
        r1 = r0;
        r54 = r4;
        r14 = r5;
        r3 = r21;
        r8 = null;
        r13 = -5;
        r23 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1023:0x13e6, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1024:0x13e7, code lost:
        r10 = r87;
        r71 = r30;
        r15 = r78;
        r72 = r92;
        r44 = r14;
        r1 = r0;
        r14 = r5;
        r3 = r21;
        r8 = null;
        r13 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1030:0x1411, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1031:0x1412, code lost:
        r10 = r87;
        r71 = r30;
        r15 = r78;
        r72 = r92;
        r44 = r14;
        r1 = r0;
        r3 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x087b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x087c, code lost:
        r2 = r85;
        r72 = r92;
        r1 = r0;
        r7 = r3;
        r10 = r87;
        r44 = r14;
        r6 = false;
        r13 = -5;
        r15 = r78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x088b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x088c, code lost:
        r72 = r92;
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01fb, code lost:
        r6 = r7;
        r13 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:958:0x122b, code lost:
        r10 = r87;
        r93 = r11;
        r4 = r54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:959:0x1247, code lost:
        throw new java.lang.RuntimeException("unexpected result from decoder.dequeueOutputBuffer: " + r1);
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1051:0x1480 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1062:0x14cc A[Catch: all -> 0x14dc, TRY_LEAVE, TryCatch #108 {all -> 0x14dc, blocks: (B:1060:0x14c3, B:1062:0x14cc), top: B:1237:0x14c3 }] */
    /* JADX WARN: Removed duplicated region for block: B:1075:0x1502  */
    /* JADX WARN: Removed duplicated region for block: B:1082:0x152d A[Catch: all -> 0x1521, TryCatch #0 {all -> 0x1521, blocks: (B:1077:0x151d, B:1082:0x152d, B:1084:0x1532, B:1086:0x153a, B:1087:0x153d), top: B:1132:0x151d }] */
    /* JADX WARN: Removed duplicated region for block: B:1084:0x1532 A[Catch: all -> 0x1521, TryCatch #0 {all -> 0x1521, blocks: (B:1077:0x151d, B:1082:0x152d, B:1084:0x1532, B:1086:0x153a, B:1087:0x153d), top: B:1132:0x151d }] */
    /* JADX WARN: Removed duplicated region for block: B:1086:0x153a A[Catch: all -> 0x1521, TryCatch #0 {all -> 0x1521, blocks: (B:1077:0x151d, B:1082:0x152d, B:1084:0x1532, B:1086:0x153a, B:1087:0x153d), top: B:1132:0x151d }] */
    /* JADX WARN: Removed duplicated region for block: B:1091:0x1548  */
    /* JADX WARN: Removed duplicated region for block: B:1108:0x15b5  */
    /* JADX WARN: Removed duplicated region for block: B:1116:0x15d3  */
    /* JADX WARN: Removed duplicated region for block: B:1118:0x1602  */
    /* JADX WARN: Removed duplicated region for block: B:1132:0x151d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1156:0x0643 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1163:0x0a0d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1171:0x154f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1225:0x15bc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1270:0x0a2e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1298:0x0bd0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1335:0x045b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1337:0x044c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1358:0x1028 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1360:0x1009 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x043c  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x05fc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0658 A[Catch: all -> 0x0647, TryCatch #24 {all -> 0x0647, blocks: (B:298:0x0643, B:302:0x0658, B:304:0x065d, B:305:0x0663), top: B:1156:0x0643 }] */
    /* JADX WARN: Removed duplicated region for block: B:304:0x065d A[Catch: all -> 0x0647, TryCatch #24 {all -> 0x0647, blocks: (B:298:0x0643, B:302:0x0658, B:304:0x065d, B:305:0x0663), top: B:1156:0x0643 }] */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0744  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x0861  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x08a1  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x08ab A[Catch: all -> 0x087b, Exception -> 0x088b, TRY_ENTER, TRY_LEAVE, TryCatch #129 {Exception -> 0x088b, all -> 0x087b, blocks: (B:417:0x086c, B:418:0x0871, B:428:0x08ab, B:432:0x08e9), top: B:1308:0x085f }] */
    /* JADX WARN: Removed duplicated region for block: B:432:0x08e9 A[Catch: all -> 0x087b, Exception -> 0x088b, TRY_ENTER, TRY_LEAVE, TryCatch #129 {Exception -> 0x088b, all -> 0x087b, blocks: (B:417:0x086c, B:418:0x0871, B:428:0x08ab, B:432:0x08e9), top: B:1308:0x085f }] */
    /* JADX WARN: Removed duplicated region for block: B:444:0x091b  */
    /* JADX WARN: Removed duplicated region for block: B:454:0x0989  */
    /* JADX WARN: Removed duplicated region for block: B:477:0x09ee  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x0a28  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0a68  */
    /* JADX WARN: Removed duplicated region for block: B:503:0x0a6b  */
    /* JADX WARN: Removed duplicated region for block: B:546:0x0b21  */
    /* JADX WARN: Removed duplicated region for block: B:555:0x0b4e  */
    /* JADX WARN: Removed duplicated region for block: B:558:0x0b5c  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0b5e  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x0b7f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:568:0x0b9f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:662:0x0d44  */
    /* JADX WARN: Removed duplicated region for block: B:678:0x0d84  */
    /* JADX WARN: Removed duplicated region for block: B:681:0x0da5 A[ADDED_TO_REGION, EDGE_INSN: B:681:0x0da5->B:1359:0x0da8 ?: BREAK  ] */
    /* JADX WARN: Removed duplicated region for block: B:685:0x0dc8  */
    /* JADX WARN: Removed duplicated region for block: B:690:0x0dd9  */
    /* JADX WARN: Removed duplicated region for block: B:692:0x0df1  */
    /* JADX WARN: Removed duplicated region for block: B:820:0x0ffa  */
    /* JADX WARN: Removed duplicated region for block: B:821:0x0ffc  */
    /* JADX WARN: Removed duplicated region for block: B:894:0x1156  */
    /* JADX WARN: Removed duplicated region for block: B:902:0x116b  */
    /* JADX WARN: Removed duplicated region for block: B:905:0x1173  */
    /* JADX WARN: Removed duplicated region for block: B:906:0x1177  */
    /* JADX WARN: Removed duplicated region for block: B:909:0x117e  */
    /* JADX WARN: Removed duplicated region for block: B:924:0x11bf  */
    /* JADX WARN: Removed duplicated region for block: B:925:0x11c2  */
    /* JADX WARN: Removed duplicated region for block: B:932:0x11d7 A[Catch: Exception -> 0x1229, all -> 0x1248, TRY_LEAVE, TryCatch #25 {all -> 0x1248, blocks: (B:895:0x1158, B:897:0x1160, B:913:0x1186, B:915:0x118a, B:918:0x11ad, B:920:0x11b7, B:929:0x11cc, B:930:0x11d2, B:932:0x11d7, B:935:0x11df, B:937:0x11e6, B:941:0x11ed, B:943:0x11f3, B:945:0x11fe, B:949:0x1210, B:951:0x1216, B:953:0x121a, B:954:0x121f, B:958:0x122b, B:959:0x1247), top: B:1158:0x1158 }] */
    /* JADX WARN: Removed duplicated region for block: B:948:0x120a  */
    /* JADX WARN: Removed duplicated region for block: B:951:0x1216 A[Catch: all -> 0x1248, Exception -> 0x124a, TryCatch #25 {all -> 0x1248, blocks: (B:895:0x1158, B:897:0x1160, B:913:0x1186, B:915:0x118a, B:918:0x11ad, B:920:0x11b7, B:929:0x11cc, B:930:0x11d2, B:932:0x11d7, B:935:0x11df, B:937:0x11e6, B:941:0x11ed, B:943:0x11f3, B:945:0x11fe, B:949:0x1210, B:951:0x1216, B:953:0x121a, B:954:0x121f, B:958:0x122b, B:959:0x1247), top: B:1158:0x1158 }] */
    /* JADX WARN: Removed duplicated region for block: B:955:0x1225  */
    /* JADX WARN: Type inference failed for: r44v185 */
    /* JADX WARN: Type inference failed for: r44v186 */
    /* JADX WARN: Type inference failed for: r44v187 */
    /* JADX WARN: Type inference failed for: r44v188 */
    /* JADX WARN: Type inference failed for: r44v72 */
    /* JADX WARN: Type inference failed for: r44v73 */
    /* JADX WARN: Type inference failed for: r4v123 */
    /* JADX WARN: Type inference failed for: r4v185 */
    /* JADX WARN: Type inference failed for: r4v188 */
    /* JADX WARN: Type inference failed for: r4v189 */
    /* JADX WARN: Type inference failed for: r4v190 */
    /* JADX WARN: Type inference failed for: r4v191 */
    /* JADX WARN: Type inference failed for: r4v192 */
    /* JADX WARN: Type inference failed for: r4v193 */
    /* JADX WARN: Type inference failed for: r4v194 */
    /* JADX WARN: Type inference failed for: r4v38 */
    /* JADX WARN: Type inference failed for: r4v40 */
    /* JADX WARN: Type inference failed for: r4v41 */
    /* JADX WARN: Type inference failed for: r4v46, types: [java.nio.ByteBuffer] */
    /* JADX WARN: Type inference failed for: r4v47 */
    /* JADX WARN: Type inference failed for: r4v48 */
    /* JADX WARN: Type inference failed for: r4v53 */
    /* JADX WARN: Type inference failed for: r4v54 */
    /* JADX WARN: Type inference failed for: r4v56 */
    /* JADX WARN: Type inference failed for: r4v57 */
    /* JADX WARN: Type inference failed for: r5v42, types: [android.media.MediaExtractor] */
    /* JADX WARN: Type inference failed for: r5v48, types: [org.telegram.messenger.video.MP4Builder] */
    /* JADX WARN: Type inference failed for: r9v40 */
    /* JADX WARN: Type inference failed for: r9v41, types: [org.telegram.messenger.video.InputSurface] */
    /* JADX WARN: Type inference failed for: r9v53, types: [org.telegram.messenger.video.InputSurface] */
    /* JADX WARN: Type inference failed for: r9v55 */
    /* JADX WARN: Type inference failed for: r9v60, types: [org.telegram.messenger.video.InputSurface] */
    @TargetApi(18)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean convertVideoInternal(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, long j4, boolean z2, boolean z3, MediaController.SavedFilterState savedFilterState, String str2, ArrayList<VideoEditedInfo.MediaEntity> arrayList, boolean z4, MediaController.CropState cropState, boolean z5) {
        long j5;
        long j6;
        int i9;
        int i10;
        boolean z6;
        int i11;
        int i12;
        Throwable th;
        MediaExtractor mediaExtractor;
        MP4Builder mP4Builder;
        int i13;
        int i14;
        int i15;
        boolean z7;
        long j7;
        int i16;
        int i17;
        int i18;
        Throwable th2;
        boolean z8;
        int i19;
        int i20;
        int i21;
        MediaExtractor mediaExtractor2;
        MP4Builder mP4Builder2;
        long j8;
        int i22;
        int findTrack;
        boolean z9;
        boolean z10;
        boolean z11;
        int i23;
        AudioRecoder audioRecoder;
        InputSurface inputSurface;
        MediaCodec mediaCodec;
        OutputSurface outputSurface;
        boolean z12;
        int i24;
        OutputSurface outputSurface2;
        MediaCodec mediaCodec2;
        MediaCodec mediaCodec3;
        int i25;
        Exception exc;
        Throwable th3;
        MediaCodec mediaCodec4;
        int i26;
        boolean z13;
        long j9;
        int i27;
        Exception e;
        long j10;
        int i28;
        MediaFormat trackFormat;
        long j11;
        long j12;
        int i29;
        long j13;
        long j14;
        long j15;
        int i30;
        long j16;
        String str3;
        int i31;
        int i32;
        MediaController.CropState cropState2;
        int i33;
        int i34;
        int i35;
        MediaFormat createVideoFormat;
        int i36;
        int i37;
        long j17;
        Throwable th4;
        MediaCodec createEncoderByType;
        MediaCodec createDecoderByType;
        long j18;
        int i38;
        OutputSurface outputSurface3;
        int i39;
        MediaCodec mediaCodec5;
        int i40;
        MediaCodec.BufferInfo bufferInfo;
        String str4;
        String str5;
        int i41;
        String str6;
        OutputSurface outputSurface4;
        boolean z14;
        int i42;
        int i43;
        long j19;
        ByteBuffer[] inputBuffers;
        ByteBuffer[] outputBuffers;
        int i44;
        long j20;
        int i45;
        Exception e2;
        long j21;
        Throwable th5;
        MediaFormat trackFormat2;
        boolean z15;
        ByteBuffer[] byteBufferArr;
        long j22;
        int i46;
        int i47;
        AudioRecoder audioRecoder2;
        int i48;
        int i49;
        ByteBuffer byteBuffer;
        boolean z16;
        AudioRecoder audioRecoder3;
        Exception e3;
        long j23;
        long j24;
        long j25;
        boolean z17;
        boolean z18;
        int i50;
        InputSurface inputSurface2;
        ByteBuffer[] byteBufferArr2;
        long j26;
        int i51;
        Throwable th6;
        int i52;
        long j27;
        int i53;
        Exception e4;
        int i54;
        boolean z19;
        ByteBuffer[] byteBufferArr3;
        long j28;
        int i55;
        long j29;
        MediaCodec.BufferInfo bufferInfo2;
        boolean z20;
        long j30;
        int i56;
        Exception e5;
        int i57;
        long j31;
        int i58;
        MediaController.VideoConvertorListener videoConvertorListener;
        long j32;
        int i59;
        ByteBuffer byteBuffer2;
        boolean z21;
        int i60;
        long j33;
        long j34;
        long j35;
        int i61;
        Exception e6;
        long j36;
        int i62;
        long j37;
        int i63;
        Throwable th7;
        int i64;
        boolean z22;
        int dequeueOutputBuffer;
        int i65;
        long j38;
        MediaCodec mediaCodec6;
        int i66;
        int i67;
        MediaCodec mediaCodec7;
        String str7;
        MediaCodec mediaCodec8;
        Exception e7;
        int i68;
        int i69;
        OutputSurface outputSurface5;
        String str8;
        String str9;
        int i70;
        boolean z23;
        int i71;
        OutputSurface outputSurface6;
        MediaCodec.BufferInfo bufferInfo3;
        boolean z24;
        long j39;
        int i72;
        Exception e8;
        long j40;
        int i73;
        int i74;
        Throwable th8;
        InputSurface inputSurface3;
        long j41;
        boolean z25;
        boolean z26;
        long j42;
        long j43;
        boolean z27;
        MediaCodec.BufferInfo bufferInfo4;
        long j44;
        InputSurface inputSurface4;
        boolean z28;
        boolean z29;
        int i75;
        int i76;
        long j45;
        int i77;
        long j46;
        int i78;
        ByteBuffer byteBuffer3;
        int i79;
        int i80;
        long j47;
        ByteBuffer byteBuffer4;
        ByteBuffer byteBuffer5;
        byte[] bArr;
        long j48;
        MediaCodec mediaCodec9;
        long j49;
        boolean z30;
        String str10;
        int i81;
        Exception e9;
        Throwable th9;
        int i82;
        long j50;
        long j51;
        int i83;
        Throwable th10;
        Exception e10;
        int i84;
        int i85;
        long j52;
        int i86;
        Throwable th11;
        int i87;
        Throwable th12;
        ByteBuffer[] byteBufferArr4;
        int i88;
        OutputSurface outputSurface7;
        MediaCodec mediaCodec10;
        MediaCodecVideoConvertor mediaCodecVideoConvertor;
        int i89;
        int i90;
        Exception exc2;
        int i91;
        long j53;
        boolean z31;
        int i92;
        Throwable th13;
        int i93;
        int i94;
        int i95;
        int i96;
        ?? r9;
        long j54;
        long j55;
        MediaFormat createVideoFormat2;
        MediaCodec createEncoderByType2;
        Exception e11;
        String str11;
        int i97;
        String str12;
        String str13;
        ByteBuffer[] outputBuffers2;
        boolean z32;
        int i98;
        boolean z33;
        boolean z34;
        long j56;
        int i99;
        long j57;
        boolean z35;
        boolean z36;
        boolean z37;
        int dequeueOutputBuffer2;
        int i100;
        Throwable th14;
        Exception e12;
        boolean z38;
        int i101;
        String str14;
        String str15;
        boolean z39;
        boolean z40;
        ByteBuffer[] byteBufferArr5;
        int i102;
        boolean z41;
        ?? r92;
        boolean z42;
        ByteBuffer[] byteBufferArr6;
        ByteBuffer[] byteBufferArr7;
        ByteBuffer byteBuffer6;
        int i103;
        int i104;
        Throwable th15;
        ByteBuffer[] byteBufferArr8;
        ByteBuffer byteBuffer7;
        ByteBuffer byteBuffer8;
        boolean z43;
        MediaController.VideoConvertorListener videoConvertorListener2;
        byte[] bArr2;
        boolean z44;
        MediaCodecVideoConvertor mediaCodecVideoConvertor2 = this;
        int i105 = i5;
        int i106 = i7;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            MediaCodec.BufferInfo bufferInfo5 = new MediaCodec.BufferInfo();
            Mp4Movie mp4Movie = new Mp4Movie();
            mp4Movie.setCacheFile(file);
            mp4Movie.setRotation(0);
            mp4Movie.setSize(i4, i105);
            mediaCodecVideoConvertor2.mediaMuxer = new MP4Builder().createMovie(mp4Movie, z);
            float f = ((float) j4) / 1000.0f;
            mediaCodecVideoConvertor2.endPresentationTime = j4 * 1000;
            checkConversionCanceled();
            if (z4) {
                if (j3 >= 0) {
                    i106 = f <= 2000.0f ? 2600000 : f <= 5000.0f ? 2200000 : 1560000;
                } else if (i106 <= 0) {
                    i106 = 921600;
                }
                try {
                    if (i4 % 16 != 0) {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("changing width from " + i4 + " to " + (Math.round(i4 / 16.0f) * 16));
                            }
                            i89 = Math.round(i4 / 16.0f) * 16;
                        } catch (Exception e13) {
                            mediaCodecVideoConvertor = this;
                            exc2 = e13;
                            i88 = i106;
                            i89 = i4;
                            i11 = i105;
                            i90 = -5;
                            mediaCodec10 = null;
                            outputSurface7 = null;
                            byteBufferArr4 = null;
                            try {
                                if (!(exc2 instanceof IllegalStateException)) {
                                }
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("bitrate: ");
                                    i92 = i88;
                                    try {
                                        sb.append(i92);
                                        sb.append(" framerate: ");
                                        i94 = i6;
                                        try {
                                            sb.append(i94);
                                            sb.append(" size: ");
                                            sb.append(i11);
                                            sb.append("x");
                                            sb.append(i89);
                                            FileLog.e(sb.toString());
                                            FileLog.e(exc2);
                                            i95 = i90;
                                            i96 = i89;
                                            z6 = z31;
                                            r9 = byteBufferArr4;
                                            z8 = true;
                                            if (outputSurface7 != null) {
                                            }
                                            if (r9 != 0) {
                                            }
                                            if (mediaCodec10 != null) {
                                            }
                                            checkConversionCanceled();
                                            j5 = j2;
                                            j6 = j3;
                                            i19 = i92;
                                            i20 = i96;
                                            i21 = i11;
                                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                            i9 = i95;
                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor2 != null) {
                                            }
                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder2 != null) {
                                            }
                                            i14 = i21;
                                            i13 = i20;
                                            i15 = i19;
                                            z7 = z8;
                                        } catch (Throwable th16) {
                                            j5 = j2;
                                            j53 = j3;
                                            th = th16;
                                            i106 = i92;
                                            i12 = i89;
                                            i93 = i94;
                                            z6 = z31;
                                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                            i9 = i90;
                                            i10 = i93;
                                            j6 = j53;
                                            try {
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("bitrate: ");
                                                sb2.append(i106);
                                                sb2.append(" framerate: ");
                                                int i107 = i10 == 1 ? 1 : 0;
                                                int i108 = i10 == 1 ? 1 : 0;
                                                int i109 = i10 == 1 ? 1 : 0;
                                                int i110 = i10 == 1 ? 1 : 0;
                                                int i111 = i10 == 1 ? 1 : 0;
                                                int i112 = i10 == 1 ? 1 : 0;
                                                sb2.append(i107);
                                                sb2.append(" size: ");
                                                sb2.append(i11);
                                                sb2.append("x");
                                                sb2.append(i12);
                                                FileLog.e(sb2.toString());
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i13 = i12;
                                                i14 = i11;
                                                i15 = i106;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            } catch (Throwable th17) {
                                                MediaExtractor mediaExtractor3 = this.extractor;
                                                if (mediaExtractor3 != null) {
                                                    mediaExtractor3.release();
                                                }
                                                MP4Builder mP4Builder3 = this.mediaMuxer;
                                                if (mP4Builder3 != null) {
                                                    try {
                                                        mP4Builder3.finishMovie();
                                                        this.endPresentationTime = this.mediaMuxer.getLastFrameTimestamp(i9);
                                                    } catch (Throwable th18) {
                                                        FileLog.e(th18);
                                                    }
                                                }
                                                throw th17;
                                            }
                                        }
                                    } catch (Throwable th19) {
                                        th13 = th19;
                                        i93 = i6;
                                        j5 = j2;
                                        j53 = j3;
                                        th = th13;
                                        i106 = i92;
                                        i12 = i89;
                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                        z6 = z31;
                                        i9 = i90;
                                        i10 = i93;
                                        j6 = j53;
                                        StringBuilder sb22 = new StringBuilder();
                                        sb22.append("bitrate: ");
                                        sb22.append(i106);
                                        sb22.append(" framerate: ");
                                        int i1072 = i10 == 1 ? 1 : 0;
                                        int i1082 = i10 == 1 ? 1 : 0;
                                        int i1092 = i10 == 1 ? 1 : 0;
                                        int i1102 = i10 == 1 ? 1 : 0;
                                        int i1112 = i10 == 1 ? 1 : 0;
                                        int i1122 = i10 == 1 ? 1 : 0;
                                        sb22.append(i1072);
                                        sb22.append(" size: ");
                                        sb22.append(i11);
                                        sb22.append("x");
                                        sb22.append(i12);
                                        FileLog.e(sb22.toString());
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i13 = i12;
                                        i14 = i11;
                                        i15 = i106;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                } catch (Throwable th20) {
                                    th13 = th20;
                                    i92 = i88;
                                }
                            } catch (Throwable th21) {
                                i91 = i6;
                                j5 = j2;
                                j53 = j3;
                                th = th21;
                                i106 = i88;
                                i12 = i89;
                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                z6 = false;
                                i93 = i91;
                                i9 = i90;
                                i10 = i93;
                                j6 = j53;
                                StringBuilder sb222 = new StringBuilder();
                                sb222.append("bitrate: ");
                                sb222.append(i106);
                                sb222.append(" framerate: ");
                                int i10722 = i10 == 1 ? 1 : 0;
                                int i10822 = i10 == 1 ? 1 : 0;
                                int i10922 = i10 == 1 ? 1 : 0;
                                int i11022 = i10 == 1 ? 1 : 0;
                                int i11122 = i10 == 1 ? 1 : 0;
                                int i11222 = i10 == 1 ? 1 : 0;
                                sb222.append(i10722);
                                sb222.append(" size: ");
                                sb222.append(i11);
                                sb222.append("x");
                                sb222.append(i12);
                                FileLog.e(sb222.toString());
                                FileLog.e(th);
                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                i13 = i12;
                                i14 = i11;
                                i15 = i106;
                                z7 = true;
                                if (z6) {
                                }
                            }
                            if (z6) {
                            }
                        } catch (Throwable th22) {
                            mediaCodecVideoConvertor2 = this;
                            j5 = j2;
                            j55 = j3;
                            th = th22;
                            i12 = i4;
                            i11 = i105;
                            z6 = false;
                            i9 = -5;
                            i10 = i6;
                            j6 = j55;
                            StringBuilder sb2222 = new StringBuilder();
                            sb2222.append("bitrate: ");
                            sb2222.append(i106);
                            sb2222.append(" framerate: ");
                            int i107222 = i10 == 1 ? 1 : 0;
                            int i108222 = i10 == 1 ? 1 : 0;
                            int i109222 = i10 == 1 ? 1 : 0;
                            int i110222 = i10 == 1 ? 1 : 0;
                            int i111222 = i10 == 1 ? 1 : 0;
                            int i112222 = i10 == 1 ? 1 : 0;
                            sb2222.append(i107222);
                            sb2222.append(" size: ");
                            sb2222.append(i11);
                            sb2222.append("x");
                            sb2222.append(i12);
                            FileLog.e(sb2222.toString());
                            FileLog.e(th);
                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            i13 = i12;
                            i14 = i11;
                            i15 = i106;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    } else {
                        i89 = i4;
                    }
                } catch (Exception e14) {
                    mediaCodecVideoConvertor = this;
                    i88 = i106;
                    exc2 = e14;
                } catch (Throwable th23) {
                    mediaCodecVideoConvertor2 = this;
                    j5 = j2;
                    j54 = j3;
                    th = th23;
                    i12 = i4;
                    i11 = i105;
                }
                try {
                    if (i105 % 16 != 0) {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("changing height from " + i105 + " to " + (Math.round(i105 / 16.0f) * 16));
                            }
                            i105 = Math.round(i105 / 16.0f) * 16;
                        } catch (Exception e15) {
                            mediaCodecVideoConvertor = this;
                            exc2 = e15;
                            i88 = i106;
                            i11 = i105;
                            i90 = -5;
                            mediaCodec10 = null;
                            outputSurface7 = null;
                            byteBufferArr4 = null;
                            if (!(exc2 instanceof IllegalStateException)) {
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("bitrate: ");
                            i92 = i88;
                            sb3.append(i92);
                            sb3.append(" framerate: ");
                            i94 = i6;
                            sb3.append(i94);
                            sb3.append(" size: ");
                            sb3.append(i11);
                            sb3.append("x");
                            sb3.append(i89);
                            FileLog.e(sb3.toString());
                            FileLog.e(exc2);
                            i95 = i90;
                            i96 = i89;
                            z6 = z31;
                            r9 = byteBufferArr4;
                            z8 = true;
                            if (outputSurface7 != null) {
                            }
                            if (r9 != 0) {
                            }
                            if (mediaCodec10 != null) {
                            }
                            checkConversionCanceled();
                            j5 = j2;
                            j6 = j3;
                            i19 = i92;
                            i20 = i96;
                            i21 = i11;
                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                            i9 = i95;
                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor2 != null) {
                            }
                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder2 != null) {
                            }
                            i14 = i21;
                            i13 = i20;
                            i15 = i19;
                            z7 = z8;
                            if (z6) {
                            }
                        } catch (Throwable th24) {
                            mediaCodecVideoConvertor2 = this;
                            j5 = j2;
                            j55 = j3;
                            th = th24;
                            i11 = i105;
                            i12 = i89;
                            z6 = false;
                            i9 = -5;
                            i10 = i6;
                            j6 = j55;
                            StringBuilder sb22222 = new StringBuilder();
                            sb22222.append("bitrate: ");
                            sb22222.append(i106);
                            sb22222.append(" framerate: ");
                            int i1072222 = i10 == 1 ? 1 : 0;
                            int i1082222 = i10 == 1 ? 1 : 0;
                            int i1092222 = i10 == 1 ? 1 : 0;
                            int i1102222 = i10 == 1 ? 1 : 0;
                            int i1112222 = i10 == 1 ? 1 : 0;
                            int i1122222 = i10 == 1 ? 1 : 0;
                            sb22222.append(i1072222);
                            sb22222.append(" size: ");
                            sb22222.append(i11);
                            sb22222.append("x");
                            sb22222.append(i12);
                            FileLog.e(sb22222.toString());
                            FileLog.e(th);
                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            i13 = i12;
                            i14 = i11;
                            i15 = i106;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    }
                    try {
                        try {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("create photo encoder " + i89 + " " + i105 + " duration = " + j4);
                            }
                            createVideoFormat2 = MediaFormat.createVideoFormat("video/avc", i89, i105);
                            createVideoFormat2.setInteger("color-format", 2130708361);
                            createVideoFormat2.setInteger("bitrate", i106);
                            createVideoFormat2.setInteger("frame-rate", 30);
                            createVideoFormat2.setInteger("i-frame-interval", 1);
                            createEncoderByType2 = MediaCodec.createEncoderByType("video/avc");
                        } catch (Exception e16) {
                            mediaCodecVideoConvertor = this;
                            i88 = i106;
                            i11 = i105;
                            exc2 = e16;
                            i90 = -5;
                            mediaCodec10 = null;
                            outputSurface7 = null;
                            byteBufferArr4 = null;
                            if (!(exc2 instanceof IllegalStateException)) {
                            }
                            StringBuilder sb32 = new StringBuilder();
                            sb32.append("bitrate: ");
                            i92 = i88;
                            sb32.append(i92);
                            sb32.append(" framerate: ");
                            i94 = i6;
                            sb32.append(i94);
                            sb32.append(" size: ");
                            sb32.append(i11);
                            sb32.append("x");
                            sb32.append(i89);
                            FileLog.e(sb32.toString());
                            FileLog.e(exc2);
                            i95 = i90;
                            i96 = i89;
                            z6 = z31;
                            r9 = byteBufferArr4;
                            z8 = true;
                            if (outputSurface7 != null) {
                            }
                            if (r9 != 0) {
                            }
                            if (mediaCodec10 != null) {
                            }
                            checkConversionCanceled();
                            j5 = j2;
                            j6 = j3;
                            i19 = i92;
                            i20 = i96;
                            i21 = i11;
                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                            i9 = i95;
                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor2 != null) {
                            }
                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder2 != null) {
                            }
                            i14 = i21;
                            i13 = i20;
                            i15 = i19;
                            z7 = z8;
                            if (z6) {
                            }
                        }
                    } catch (Throwable th25) {
                        i11 = i105;
                        i96 = i89;
                        mediaCodecVideoConvertor2 = this;
                        i16 = i6;
                        j5 = j2;
                        j7 = j3;
                        th = th25;
                    }
                    try {
                        createEncoderByType2.configure(createVideoFormat2, (Surface) null, (MediaCrypto) null, 1);
                        ?? inputSurface5 = new InputSurface(createEncoderByType2.createInputSurface());
                        try {
                            inputSurface5.makeCurrent();
                            createEncoderByType2.start();
                            str11 = "csd-0";
                            i88 = i106;
                            byteBufferArr4 = inputSurface5;
                            i97 = i105;
                            i96 = i89;
                            str12 = "video/avc";
                            str13 = "prepend-sps-pps-to-idr-frames";
                            try {
                                try {
                                    outputSurface7 = new OutputSurface(savedFilterState, str, str2, arrayList, null, i89, i105, i2, i3, i, i6, true);
                                    try {
                                        if (Build.VERSION.SDK_INT < 21) {
                                            try {
                                                outputBuffers2 = createEncoderByType2.getOutputBuffers();
                                            } catch (Exception e17) {
                                                mediaCodecVideoConvertor = this;
                                                exc2 = e17;
                                                mediaCodec10 = createEncoderByType2;
                                                i11 = i97;
                                                i89 = i96;
                                                i90 = -5;
                                            } catch (Throwable th26) {
                                                mediaCodecVideoConvertor2 = this;
                                                i16 = i6;
                                                j5 = j2;
                                                j7 = j3;
                                                th = th26;
                                                i106 = i88;
                                                i11 = i97;
                                                i12 = i96;
                                                z6 = false;
                                                i9 = -5;
                                                i10 = i16;
                                                j6 = j7;
                                                StringBuilder sb222222 = new StringBuilder();
                                                sb222222.append("bitrate: ");
                                                sb222222.append(i106);
                                                sb222222.append(" framerate: ");
                                                int i10722222 = i10 == 1 ? 1 : 0;
                                                int i10822222 = i10 == 1 ? 1 : 0;
                                                int i10922222 = i10 == 1 ? 1 : 0;
                                                int i11022222 = i10 == 1 ? 1 : 0;
                                                int i11122222 = i10 == 1 ? 1 : 0;
                                                int i11222222 = i10 == 1 ? 1 : 0;
                                                sb222222.append(i10722222);
                                                sb222222.append(" size: ");
                                                sb222222.append(i11);
                                                sb222222.append("x");
                                                sb222222.append(i12);
                                                FileLog.e(sb222222.toString());
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i13 = i12;
                                                i14 = i11;
                                                i15 = i106;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        } else {
                                            outputBuffers2 = null;
                                        }
                                        checkConversionCanceled();
                                        z32 = false;
                                        i11 = 0;
                                        i98 = 0;
                                        z33 = true;
                                        z34 = false;
                                        i9 = -5;
                                    } catch (Exception e18) {
                                        e11 = e18;
                                        mediaCodecVideoConvertor = this;
                                        i11 = i97;
                                        mediaCodec10 = createEncoderByType2;
                                        i89 = i96;
                                        i90 = -5;
                                    }
                                } catch (Throwable th27) {
                                    i11 = i97;
                                    mediaCodecVideoConvertor2 = this;
                                    i16 = i6;
                                    j5 = j2;
                                    j7 = j3;
                                    th = th27;
                                    i106 = i88;
                                }
                            } catch (Exception e19) {
                                e11 = e19;
                                mediaCodecVideoConvertor = this;
                                i11 = i97;
                                mediaCodec10 = createEncoderByType2;
                                i89 = i96;
                                i90 = -5;
                                outputSurface7 = null;
                                exc2 = e11;
                                if (!(exc2 instanceof IllegalStateException)) {
                                }
                                StringBuilder sb322 = new StringBuilder();
                                sb322.append("bitrate: ");
                                i92 = i88;
                                sb322.append(i92);
                                sb322.append(" framerate: ");
                                i94 = i6;
                                sb322.append(i94);
                                sb322.append(" size: ");
                                sb322.append(i11);
                                sb322.append("x");
                                sb322.append(i89);
                                FileLog.e(sb322.toString());
                                FileLog.e(exc2);
                                i95 = i90;
                                i96 = i89;
                                z6 = z31;
                                r9 = byteBufferArr4;
                                z8 = true;
                                if (outputSurface7 != null) {
                                }
                                if (r9 != 0) {
                                }
                                if (mediaCodec10 != null) {
                                }
                                checkConversionCanceled();
                                j5 = j2;
                                j6 = j3;
                                i19 = i92;
                                i20 = i96;
                                i21 = i11;
                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                i9 = i95;
                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                if (mediaExtractor2 != null) {
                                }
                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                if (mP4Builder2 != null) {
                                }
                                i14 = i21;
                                i13 = i20;
                                i15 = i19;
                                z7 = z8;
                                if (z6) {
                                }
                            }
                        } catch (Exception e20) {
                            e11 = e20;
                            mediaCodecVideoConvertor = this;
                            i88 = i106;
                            i11 = i105;
                            byteBufferArr4 = inputSurface5;
                            mediaCodec10 = createEncoderByType2;
                        }
                    } catch (Exception e21) {
                        mediaCodecVideoConvertor = this;
                        i88 = i106;
                        i11 = i105;
                        exc2 = e21;
                        mediaCodec10 = createEncoderByType2;
                        i90 = -5;
                        outputSurface7 = null;
                        byteBufferArr4 = null;
                        if (!(exc2 instanceof IllegalStateException)) {
                        }
                        StringBuilder sb3222 = new StringBuilder();
                        sb3222.append("bitrate: ");
                        i92 = i88;
                        sb3222.append(i92);
                        sb3222.append(" framerate: ");
                        i94 = i6;
                        sb3222.append(i94);
                        sb3222.append(" size: ");
                        sb3222.append(i11);
                        sb3222.append("x");
                        sb3222.append(i89);
                        FileLog.e(sb3222.toString());
                        FileLog.e(exc2);
                        i95 = i90;
                        i96 = i89;
                        z6 = z31;
                        r9 = byteBufferArr4;
                        z8 = true;
                        if (outputSurface7 != null) {
                        }
                        if (r9 != 0) {
                        }
                        if (mediaCodec10 != null) {
                        }
                        checkConversionCanceled();
                        j5 = j2;
                        j6 = j3;
                        i19 = i92;
                        i20 = i96;
                        i21 = i11;
                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                        i9 = i95;
                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                        if (mediaExtractor2 != null) {
                        }
                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                        if (mP4Builder2 != null) {
                        }
                        i14 = i21;
                        i13 = i20;
                        i15 = i19;
                        z7 = z8;
                        if (z6) {
                        }
                    }
                } catch (Exception e22) {
                    mediaCodecVideoConvertor = this;
                    i88 = i106;
                    exc2 = e22;
                } catch (Throwable th28) {
                    mediaCodecVideoConvertor2 = this;
                    j5 = j2;
                    j54 = j3;
                    th = th28;
                    i11 = i105;
                    i12 = i89;
                    z6 = false;
                    i9 = -5;
                    i10 = i6;
                    j6 = j54;
                    StringBuilder sb2222222 = new StringBuilder();
                    sb2222222.append("bitrate: ");
                    sb2222222.append(i106);
                    sb2222222.append(" framerate: ");
                    int i107222222 = i10 == 1 ? 1 : 0;
                    int i108222222 = i10 == 1 ? 1 : 0;
                    int i109222222 = i10 == 1 ? 1 : 0;
                    int i110222222 = i10 == 1 ? 1 : 0;
                    int i111222222 = i10 == 1 ? 1 : 0;
                    int i112222222 = i10 == 1 ? 1 : 0;
                    sb2222222.append(i107222222);
                    sb2222222.append(" size: ");
                    sb2222222.append(i11);
                    sb2222222.append("x");
                    sb2222222.append(i12);
                    FileLog.e(sb2222222.toString());
                    FileLog.e(th);
                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                    if (mediaExtractor != null) {
                    }
                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                    if (mP4Builder != null) {
                    }
                    i13 = i12;
                    i14 = i11;
                    i15 = i106;
                    z7 = true;
                    if (z6) {
                    }
                }
                while (!z34) {
                    try {
                        checkConversionCanceled();
                        i90 = i9;
                        z35 = true;
                        z36 = z34;
                        z37 = !z32;
                    } catch (Exception e23) {
                        e11 = e23;
                        int i113 = i9;
                        i11 = i97;
                        mediaCodecVideoConvertor = this;
                        mediaCodec10 = createEncoderByType2;
                        i90 = i113;
                    } catch (Throwable th29) {
                        i11 = i97;
                        mediaCodecVideoConvertor2 = this;
                        i99 = i6;
                        j5 = j2;
                        j57 = j3;
                        th = th29;
                    }
                    while (true) {
                        if (z37 || z35) {
                            try {
                                checkConversionCanceled();
                                dequeueOutputBuffer2 = createEncoderByType2.dequeueOutputBuffer(bufferInfo5, z3 ? 22000L : 2500L);
                            } catch (Exception e24) {
                                e11 = e24;
                                mediaCodecVideoConvertor = this;
                            } catch (Throwable th30) {
                                i11 = i97;
                                mediaCodecVideoConvertor2 = this;
                                i99 = i6;
                                j5 = j2;
                                j57 = j3;
                                th = th30;
                                i9 = i90;
                                i106 = i88;
                                j56 = j57;
                                i12 = i96;
                                i22 = i99;
                                j8 = j56;
                                z6 = false;
                                i10 = i22;
                                j6 = j8;
                                StringBuilder sb22222222 = new StringBuilder();
                                sb22222222.append("bitrate: ");
                                sb22222222.append(i106);
                                sb22222222.append(" framerate: ");
                                int i1072222222 = i10 == 1 ? 1 : 0;
                                int i1082222222 = i10 == 1 ? 1 : 0;
                                int i1092222222 = i10 == 1 ? 1 : 0;
                                int i1102222222 = i10 == 1 ? 1 : 0;
                                int i1112222222 = i10 == 1 ? 1 : 0;
                                int i1122222222 = i10 == 1 ? 1 : 0;
                                sb22222222.append(i1072222222);
                                sb22222222.append(" size: ");
                                sb22222222.append(i11);
                                sb22222222.append("x");
                                sb22222222.append(i12);
                                FileLog.e(sb22222222.toString());
                                FileLog.e(th);
                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                i13 = i12;
                                i14 = i11;
                                i15 = i106;
                                z7 = true;
                                if (z6) {
                                }
                            }
                            if (dequeueOutputBuffer2 == -1) {
                                z39 = false;
                                mediaCodecVideoConvertor = this;
                                i101 = i11;
                                z38 = z37;
                                str14 = str11;
                            } else {
                                if (dequeueOutputBuffer2 == -3) {
                                    try {
                                        if (Build.VERSION.SDK_INT < 21) {
                                            outputBuffers2 = createEncoderByType2.getOutputBuffers();
                                        }
                                        i101 = i11;
                                        z38 = z37;
                                        z39 = z35;
                                        str14 = str11;
                                        i11 = i97;
                                        str15 = str12;
                                        mediaCodecVideoConvertor = this;
                                        z40 = z33;
                                        byteBufferArr5 = outputBuffers2;
                                        i102 = -1;
                                        if (dequeueOutputBuffer2 == i102) {
                                            i97 = i11;
                                            outputBuffers2 = byteBufferArr5;
                                            z33 = z40;
                                            z35 = z39;
                                            str12 = str15;
                                            str11 = str14;
                                            i11 = i101;
                                            z37 = z38;
                                        } else {
                                            if (!z32) {
                                                try {
                                                    try {
                                                        outputSurface7.drawImage();
                                                        z41 = z32;
                                                        r92 = byteBufferArr4;
                                                    } catch (Exception e25) {
                                                        e12 = e25;
                                                        exc2 = e12;
                                                        mediaCodec10 = createEncoderByType2;
                                                        i89 = i96;
                                                        if (!(exc2 instanceof IllegalStateException)) {
                                                        }
                                                        StringBuilder sb32222 = new StringBuilder();
                                                        sb32222.append("bitrate: ");
                                                        i92 = i88;
                                                        sb32222.append(i92);
                                                        sb32222.append(" framerate: ");
                                                        i94 = i6;
                                                        sb32222.append(i94);
                                                        sb32222.append(" size: ");
                                                        sb32222.append(i11);
                                                        sb32222.append("x");
                                                        sb32222.append(i89);
                                                        FileLog.e(sb32222.toString());
                                                        FileLog.e(exc2);
                                                        i95 = i90;
                                                        i96 = i89;
                                                        z6 = z31;
                                                        r9 = byteBufferArr4;
                                                        z8 = true;
                                                        if (outputSurface7 != null) {
                                                        }
                                                        if (r9 != 0) {
                                                        }
                                                        if (mediaCodec10 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        j5 = j2;
                                                        j6 = j3;
                                                        i19 = i92;
                                                        i20 = i96;
                                                        i21 = i11;
                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                        i9 = i95;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i14 = i21;
                                                        i13 = i20;
                                                        i15 = i19;
                                                        z7 = z8;
                                                        if (z6) {
                                                        }
                                                    }
                                                    try {
                                                        r92.setPresentationTime((i98 / 30.0f) * 1000.0f * 1000.0f * 1000.0f);
                                                        r92.swapBuffers();
                                                        i98++;
                                                        if (i98 >= 30.0f * f) {
                                                            createEncoderByType2.signalEndOfInputStream();
                                                            z42 = false;
                                                            z32 = true;
                                                            byteBufferArr6 = r92;
                                                            i97 = i11;
                                                            byteBufferArr4 = byteBufferArr6;
                                                            z35 = z39;
                                                            str12 = str15;
                                                            str11 = str14;
                                                            i11 = i101;
                                                            boolean z45 = z40;
                                                            z37 = z42;
                                                            outputBuffers2 = byteBufferArr5;
                                                            z33 = z45;
                                                        } else {
                                                            z32 = z41;
                                                            byteBufferArr7 = r92;
                                                        }
                                                    } catch (Exception e26) {
                                                        exc2 = e26;
                                                        byteBufferArr4 = r92;
                                                        mediaCodec10 = createEncoderByType2;
                                                        i89 = i96;
                                                        if (!(exc2 instanceof IllegalStateException)) {
                                                        }
                                                        StringBuilder sb322222 = new StringBuilder();
                                                        sb322222.append("bitrate: ");
                                                        i92 = i88;
                                                        sb322222.append(i92);
                                                        sb322222.append(" framerate: ");
                                                        i94 = i6;
                                                        sb322222.append(i94);
                                                        sb322222.append(" size: ");
                                                        sb322222.append(i11);
                                                        sb322222.append("x");
                                                        sb322222.append(i89);
                                                        FileLog.e(sb322222.toString());
                                                        FileLog.e(exc2);
                                                        i95 = i90;
                                                        i96 = i89;
                                                        z6 = z31;
                                                        r9 = byteBufferArr4;
                                                        z8 = true;
                                                        if (outputSurface7 != null) {
                                                        }
                                                        if (r9 != 0) {
                                                        }
                                                        if (mediaCodec10 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        j5 = j2;
                                                        j6 = j3;
                                                        i19 = i92;
                                                        i20 = i96;
                                                        i21 = i11;
                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                        i9 = i95;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i14 = i21;
                                                        i13 = i20;
                                                        i15 = i19;
                                                        z7 = z8;
                                                        if (z6) {
                                                        }
                                                    }
                                                } catch (Throwable th31) {
                                                    th14 = th31;
                                                    i100 = i6;
                                                    j5 = j2;
                                                    j53 = j3;
                                                    th = th14;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i106 = i88;
                                                    i12 = i96;
                                                    i91 = i100;
                                                    z6 = false;
                                                    i93 = i91;
                                                    i9 = i90;
                                                    i10 = i93;
                                                    j6 = j53;
                                                    StringBuilder sb222222222 = new StringBuilder();
                                                    sb222222222.append("bitrate: ");
                                                    sb222222222.append(i106);
                                                    sb222222222.append(" framerate: ");
                                                    int i10722222222 = i10 == 1 ? 1 : 0;
                                                    int i10822222222 = i10 == 1 ? 1 : 0;
                                                    int i10922222222 = i10 == 1 ? 1 : 0;
                                                    int i11022222222 = i10 == 1 ? 1 : 0;
                                                    int i11122222222 = i10 == 1 ? 1 : 0;
                                                    int i11222222222 = i10 == 1 ? 1 : 0;
                                                    sb222222222.append(i10722222222);
                                                    sb222222222.append(" size: ");
                                                    sb222222222.append(i11);
                                                    sb222222222.append("x");
                                                    sb222222222.append(i12);
                                                    FileLog.e(sb222222222.toString());
                                                    FileLog.e(th);
                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    i13 = i12;
                                                    i14 = i11;
                                                    i15 = i106;
                                                    z7 = true;
                                                    if (z6) {
                                                    }
                                                }
                                            } else {
                                                byteBufferArr7 = byteBufferArr4;
                                            }
                                            z42 = z38;
                                            byteBufferArr6 = byteBufferArr7;
                                            i97 = i11;
                                            byteBufferArr4 = byteBufferArr6;
                                            z35 = z39;
                                            str12 = str15;
                                            str11 = str14;
                                            i11 = i101;
                                            boolean z452 = z40;
                                            z37 = z42;
                                            outputBuffers2 = byteBufferArr5;
                                            z33 = z452;
                                        }
                                    } catch (Exception e27) {
                                        e = e27;
                                        mediaCodecVideoConvertor = this;
                                    } catch (Throwable th32) {
                                        mediaCodecVideoConvertor2 = this;
                                        i99 = i6;
                                        j5 = j2;
                                        j56 = j3;
                                        th = th32;
                                        i9 = i90;
                                        i106 = i88;
                                        i11 = i97;
                                        i12 = i96;
                                        i22 = i99;
                                        j8 = j56;
                                        z6 = false;
                                        i10 = i22;
                                        j6 = j8;
                                        StringBuilder sb2222222222 = new StringBuilder();
                                        sb2222222222.append("bitrate: ");
                                        sb2222222222.append(i106);
                                        sb2222222222.append(" framerate: ");
                                        int i107222222222 = i10 == 1 ? 1 : 0;
                                        int i108222222222 = i10 == 1 ? 1 : 0;
                                        int i109222222222 = i10 == 1 ? 1 : 0;
                                        int i110222222222 = i10 == 1 ? 1 : 0;
                                        int i111222222222 = i10 == 1 ? 1 : 0;
                                        int i112222222222 = i10 == 1 ? 1 : 0;
                                        sb2222222222.append(i107222222222);
                                        sb2222222222.append(" size: ");
                                        sb2222222222.append(i11);
                                        sb2222222222.append("x");
                                        sb2222222222.append(i12);
                                        FileLog.e(sb2222222222.toString());
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i13 = i12;
                                        i14 = i11;
                                        i15 = i106;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                } else if (dequeueOutputBuffer2 == -2) {
                                    MediaFormat outputFormat = createEncoderByType2.getOutputFormat();
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("photo encoder new format " + outputFormat);
                                    }
                                    if (i90 != -5 || outputFormat == null) {
                                        z44 = z35;
                                        str14 = str11;
                                        mediaCodecVideoConvertor = this;
                                    } else {
                                        z44 = z35;
                                        mediaCodecVideoConvertor = this;
                                        try {
                                            i90 = mediaCodecVideoConvertor.mediaMuxer.addTrack(outputFormat, false);
                                            String str16 = str13;
                                            if (outputFormat.containsKey(str16)) {
                                                str13 = str16;
                                                if (outputFormat.getInteger(str16) == 1) {
                                                    str14 = str11;
                                                    i11 = outputFormat.getByteBuffer(str14).limit() + outputFormat.getByteBuffer("csd-1").limit();
                                                }
                                            } else {
                                                str13 = str16;
                                            }
                                            str14 = str11;
                                        } catch (Exception e28) {
                                            e = e28;
                                        } catch (Throwable th33) {
                                            i100 = i6;
                                            j5 = j2;
                                            j53 = j3;
                                            th = th33;
                                            mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                            i106 = i88;
                                            i11 = i97;
                                            i12 = i96;
                                            i91 = i100;
                                            z6 = false;
                                            i93 = i91;
                                            i9 = i90;
                                            i10 = i93;
                                            j6 = j53;
                                            StringBuilder sb22222222222 = new StringBuilder();
                                            sb22222222222.append("bitrate: ");
                                            sb22222222222.append(i106);
                                            sb22222222222.append(" framerate: ");
                                            int i1072222222222 = i10 == 1 ? 1 : 0;
                                            int i1082222222222 = i10 == 1 ? 1 : 0;
                                            int i1092222222222 = i10 == 1 ? 1 : 0;
                                            int i1102222222222 = i10 == 1 ? 1 : 0;
                                            int i1112222222222 = i10 == 1 ? 1 : 0;
                                            int i1122222222222 = i10 == 1 ? 1 : 0;
                                            sb22222222222.append(i1072222222222);
                                            sb22222222222.append(" size: ");
                                            sb22222222222.append(i11);
                                            sb22222222222.append("x");
                                            sb22222222222.append(i12);
                                            FileLog.e(sb22222222222.toString());
                                            FileLog.e(th);
                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            i13 = i12;
                                            i14 = i11;
                                            i15 = i106;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                    }
                                    z39 = z44;
                                    i101 = i11;
                                    z38 = z37;
                                } else {
                                    boolean z46 = z35;
                                    str14 = str11;
                                    mediaCodecVideoConvertor = this;
                                    try {
                                        if (dequeueOutputBuffer2 < 0) {
                                            throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer2);
                                        }
                                        try {
                                            try {
                                                if (Build.VERSION.SDK_INT < 21) {
                                                    byteBuffer6 = outputBuffers2[dequeueOutputBuffer2];
                                                } else {
                                                    byteBuffer6 = createEncoderByType2.getOutputBuffer(dequeueOutputBuffer2);
                                                }
                                                if (byteBuffer6 == null) {
                                                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer2 + " was null");
                                                }
                                                try {
                                                    i103 = bufferInfo5.size;
                                                } catch (Exception e29) {
                                                    e12 = e29;
                                                    i11 = i97;
                                                    exc2 = e12;
                                                    mediaCodec10 = createEncoderByType2;
                                                    i89 = i96;
                                                    if (!(exc2 instanceof IllegalStateException)) {
                                                    }
                                                    StringBuilder sb3222222 = new StringBuilder();
                                                    sb3222222.append("bitrate: ");
                                                    i92 = i88;
                                                    sb3222222.append(i92);
                                                    sb3222222.append(" framerate: ");
                                                    i94 = i6;
                                                    sb3222222.append(i94);
                                                    sb3222222.append(" size: ");
                                                    sb3222222.append(i11);
                                                    sb3222222.append("x");
                                                    sb3222222.append(i89);
                                                    FileLog.e(sb3222222.toString());
                                                    FileLog.e(exc2);
                                                    i95 = i90;
                                                    i96 = i89;
                                                    z6 = z31;
                                                    r9 = byteBufferArr4;
                                                    z8 = true;
                                                    if (outputSurface7 != null) {
                                                    }
                                                    if (r9 != 0) {
                                                    }
                                                    if (mediaCodec10 != null) {
                                                    }
                                                    checkConversionCanceled();
                                                    j5 = j2;
                                                    j6 = j3;
                                                    i19 = i92;
                                                    i20 = i96;
                                                    i21 = i11;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i9 = i95;
                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor2 != null) {
                                                    }
                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder2 != null) {
                                                    }
                                                    i14 = i21;
                                                    i13 = i20;
                                                    i15 = i19;
                                                    z7 = z8;
                                                    if (z6) {
                                                    }
                                                }
                                                try {
                                                    if (i103 > 1) {
                                                        try {
                                                            int i114 = bufferInfo5.flags;
                                                            if ((i114 & 2) == 0) {
                                                                if (i11 == 0 || (i114 & 1) == 0) {
                                                                    byteBufferArr8 = outputBuffers2;
                                                                } else {
                                                                    byteBufferArr8 = outputBuffers2;
                                                                    bufferInfo5.offset += i11;
                                                                    bufferInfo5.size = i103 - i11;
                                                                }
                                                                if (z33 && (i114 & 1) != 0) {
                                                                    if (bufferInfo5.size > 100) {
                                                                        byteBuffer6.position(bufferInfo5.offset);
                                                                        byte[] bArr3 = new byte[100];
                                                                        byteBuffer6.get(bArr3);
                                                                        int i115 = 0;
                                                                        int i116 = 0;
                                                                        while (true) {
                                                                            if (i115 >= 96) {
                                                                                break;
                                                                            }
                                                                            if (bArr3[i115] == 0 && bArr3[i115 + 1] == 0 && bArr3[i115 + 2] == 0) {
                                                                                bArr2 = bArr3;
                                                                                if (bArr3[i115 + 3] == 1 && (i116 = i116 + 1) > 1) {
                                                                                    bufferInfo5.offset += i115;
                                                                                    bufferInfo5.size -= i115;
                                                                                    break;
                                                                                }
                                                                            } else {
                                                                                bArr2 = bArr3;
                                                                            }
                                                                            i115++;
                                                                            bArr3 = bArr2;
                                                                        }
                                                                    }
                                                                    z33 = false;
                                                                }
                                                                boolean z47 = z33;
                                                                z38 = z37;
                                                                long writeSampleData = mediaCodecVideoConvertor.mediaMuxer.writeSampleData(i90, byteBuffer6, bufferInfo5, true);
                                                                if (writeSampleData == 0 || (videoConvertorListener2 = mediaCodecVideoConvertor.callback) == null) {
                                                                    z43 = z47;
                                                                } else {
                                                                    z43 = z47;
                                                                    videoConvertorListener2.didWriteData(writeSampleData, (((float) 0) / 1000.0f) / f);
                                                                }
                                                                z33 = z43;
                                                                i104 = i96;
                                                                str15 = str12;
                                                                i101 = i11;
                                                                i11 = i97;
                                                            } else {
                                                                byteBufferArr8 = outputBuffers2;
                                                                z38 = z37;
                                                                if (i90 == -5) {
                                                                    byte[] bArr4 = new byte[i103];
                                                                    byteBuffer6.limit(bufferInfo5.offset + i103);
                                                                    byteBuffer6.position(bufferInfo5.offset);
                                                                    byteBuffer6.get(bArr4);
                                                                    byte b = 1;
                                                                    int i117 = bufferInfo5.size - 1;
                                                                    while (i117 >= 0 && i117 > 3) {
                                                                        if (bArr4[i117] == b && bArr4[i117 - 1] == 0 && bArr4[i117 - 2] == 0) {
                                                                            int i118 = i117 - 3;
                                                                            if (bArr4[i118] == 0) {
                                                                                byteBuffer8 = ByteBuffer.allocate(i118);
                                                                                byteBuffer7 = ByteBuffer.allocate(bufferInfo5.size - i118);
                                                                                i101 = i11;
                                                                                byteBuffer8.put(bArr4, 0, i118).position(0);
                                                                                byteBuffer7.put(bArr4, i118, bufferInfo5.size - i118).position(0);
                                                                                break;
                                                                            }
                                                                        }
                                                                        i117--;
                                                                        i11 = i11;
                                                                        b = 1;
                                                                    }
                                                                    i101 = i11;
                                                                    byteBuffer8 = null;
                                                                    byteBuffer7 = null;
                                                                    i11 = i97;
                                                                    i104 = i96;
                                                                    str15 = str12;
                                                                    try {
                                                                        MediaFormat createVideoFormat3 = MediaFormat.createVideoFormat(str15, i104, i11);
                                                                        if (byteBuffer8 != null && byteBuffer7 != null) {
                                                                            createVideoFormat3.setByteBuffer(str14, byteBuffer8);
                                                                            createVideoFormat3.setByteBuffer("csd-1", byteBuffer7);
                                                                        }
                                                                        i90 = mediaCodecVideoConvertor.mediaMuxer.addTrack(createVideoFormat3, false);
                                                                    } catch (Exception e30) {
                                                                        e11 = e30;
                                                                        i89 = i104;
                                                                        mediaCodec10 = createEncoderByType2;
                                                                        exc2 = e11;
                                                                        if (!(exc2 instanceof IllegalStateException)) {
                                                                        }
                                                                        StringBuilder sb32222222 = new StringBuilder();
                                                                        sb32222222.append("bitrate: ");
                                                                        i92 = i88;
                                                                        sb32222222.append(i92);
                                                                        sb32222222.append(" framerate: ");
                                                                        i94 = i6;
                                                                        sb32222222.append(i94);
                                                                        sb32222222.append(" size: ");
                                                                        sb32222222.append(i11);
                                                                        sb32222222.append("x");
                                                                        sb32222222.append(i89);
                                                                        FileLog.e(sb32222222.toString());
                                                                        FileLog.e(exc2);
                                                                        i95 = i90;
                                                                        i96 = i89;
                                                                        z6 = z31;
                                                                        r9 = byteBufferArr4;
                                                                        z8 = true;
                                                                        if (outputSurface7 != null) {
                                                                        }
                                                                        if (r9 != 0) {
                                                                        }
                                                                        if (mediaCodec10 != null) {
                                                                        }
                                                                        checkConversionCanceled();
                                                                        j5 = j2;
                                                                        j6 = j3;
                                                                        i19 = i92;
                                                                        i20 = i96;
                                                                        i21 = i11;
                                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                                        i9 = i95;
                                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                        if (mediaExtractor2 != null) {
                                                                        }
                                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                        if (mP4Builder2 != null) {
                                                                        }
                                                                        i14 = i21;
                                                                        i13 = i20;
                                                                        i15 = i19;
                                                                        z7 = z8;
                                                                        if (z6) {
                                                                        }
                                                                    } catch (Throwable th34) {
                                                                        th15 = th34;
                                                                        i93 = i6;
                                                                        j5 = j2;
                                                                        j53 = j3;
                                                                        i12 = i104;
                                                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                                        i106 = i88;
                                                                        z6 = false;
                                                                        th = th15;
                                                                        i9 = i90;
                                                                        i10 = i93;
                                                                        j6 = j53;
                                                                        StringBuilder sb222222222222 = new StringBuilder();
                                                                        sb222222222222.append("bitrate: ");
                                                                        sb222222222222.append(i106);
                                                                        sb222222222222.append(" framerate: ");
                                                                        int i10722222222222 = i10 == 1 ? 1 : 0;
                                                                        int i10822222222222 = i10 == 1 ? 1 : 0;
                                                                        int i10922222222222 = i10 == 1 ? 1 : 0;
                                                                        int i11022222222222 = i10 == 1 ? 1 : 0;
                                                                        int i11122222222222 = i10 == 1 ? 1 : 0;
                                                                        int i11222222222222 = i10 == 1 ? 1 : 0;
                                                                        sb222222222222.append(i10722222222222);
                                                                        sb222222222222.append(" size: ");
                                                                        sb222222222222.append(i11);
                                                                        sb222222222222.append("x");
                                                                        sb222222222222.append(i12);
                                                                        FileLog.e(sb222222222222.toString());
                                                                        FileLog.e(th);
                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        i13 = i12;
                                                                        i14 = i11;
                                                                        i15 = i106;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    }
                                                                } else {
                                                                    i101 = i11;
                                                                }
                                                            }
                                                            boolean z48 = (bufferInfo5.flags & 4) == 0;
                                                            createEncoderByType2.releaseOutputBuffer(dequeueOutputBuffer2, false);
                                                            z39 = z46;
                                                            i96 = i104;
                                                            z36 = z48;
                                                            i102 = -1;
                                                            z40 = z33;
                                                            byteBufferArr5 = byteBufferArr8;
                                                            if (dequeueOutputBuffer2 == i102) {
                                                            }
                                                        } catch (Exception e31) {
                                                            e11 = e31;
                                                            i11 = i97;
                                                            i104 = i96;
                                                        } catch (Throwable th35) {
                                                            th15 = th35;
                                                            i11 = i97;
                                                            i104 = i96;
                                                        }
                                                    } else {
                                                        byteBufferArr8 = outputBuffers2;
                                                        i101 = i11;
                                                        z38 = z37;
                                                    }
                                                    if ((bufferInfo5.flags & 4) == 0) {
                                                    }
                                                    createEncoderByType2.releaseOutputBuffer(dequeueOutputBuffer2, false);
                                                    z39 = z46;
                                                    i96 = i104;
                                                    z36 = z48;
                                                    i102 = -1;
                                                    z40 = z33;
                                                    byteBufferArr5 = byteBufferArr8;
                                                    if (dequeueOutputBuffer2 == i102) {
                                                    }
                                                } catch (Exception e32) {
                                                    e12 = e32;
                                                    i96 = i104;
                                                    exc2 = e12;
                                                    mediaCodec10 = createEncoderByType2;
                                                    i89 = i96;
                                                    if (!(exc2 instanceof IllegalStateException)) {
                                                    }
                                                    StringBuilder sb322222222 = new StringBuilder();
                                                    sb322222222.append("bitrate: ");
                                                    i92 = i88;
                                                    sb322222222.append(i92);
                                                    sb322222222.append(" framerate: ");
                                                    i94 = i6;
                                                    sb322222222.append(i94);
                                                    sb322222222.append(" size: ");
                                                    sb322222222.append(i11);
                                                    sb322222222.append("x");
                                                    sb322222222.append(i89);
                                                    FileLog.e(sb322222222.toString());
                                                    FileLog.e(exc2);
                                                    i95 = i90;
                                                    i96 = i89;
                                                    z6 = z31;
                                                    r9 = byteBufferArr4;
                                                    z8 = true;
                                                    if (outputSurface7 != null) {
                                                    }
                                                    if (r9 != 0) {
                                                    }
                                                    if (mediaCodec10 != null) {
                                                    }
                                                    checkConversionCanceled();
                                                    j5 = j2;
                                                    j6 = j3;
                                                    i19 = i92;
                                                    i20 = i96;
                                                    i21 = i11;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i9 = i95;
                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor2 != null) {
                                                    }
                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder2 != null) {
                                                    }
                                                    i14 = i21;
                                                    i13 = i20;
                                                    i15 = i19;
                                                    z7 = z8;
                                                    if (z6) {
                                                    }
                                                } catch (Throwable th36) {
                                                    th14 = th36;
                                                    i96 = i104;
                                                    i100 = i6;
                                                    j5 = j2;
                                                    j53 = j3;
                                                    th = th14;
                                                    mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                    i106 = i88;
                                                    i12 = i96;
                                                    i91 = i100;
                                                    z6 = false;
                                                    i93 = i91;
                                                    i9 = i90;
                                                    i10 = i93;
                                                    j6 = j53;
                                                    StringBuilder sb2222222222222 = new StringBuilder();
                                                    sb2222222222222.append("bitrate: ");
                                                    sb2222222222222.append(i106);
                                                    sb2222222222222.append(" framerate: ");
                                                    int i107222222222222 = i10 == 1 ? 1 : 0;
                                                    int i108222222222222 = i10 == 1 ? 1 : 0;
                                                    int i109222222222222 = i10 == 1 ? 1 : 0;
                                                    int i110222222222222 = i10 == 1 ? 1 : 0;
                                                    int i111222222222222 = i10 == 1 ? 1 : 0;
                                                    int i112222222222222 = i10 == 1 ? 1 : 0;
                                                    sb2222222222222.append(i107222222222222);
                                                    sb2222222222222.append(" size: ");
                                                    sb2222222222222.append(i11);
                                                    sb2222222222222.append("x");
                                                    sb2222222222222.append(i12);
                                                    FileLog.e(sb2222222222222.toString());
                                                    FileLog.e(th);
                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    i13 = i12;
                                                    i14 = i11;
                                                    i15 = i106;
                                                    z7 = true;
                                                    if (z6) {
                                                    }
                                                }
                                                i11 = i97;
                                                i104 = i96;
                                                str15 = str12;
                                            } catch (Exception e33) {
                                                e11 = e33;
                                                i11 = i97;
                                                mediaCodec10 = createEncoderByType2;
                                                i89 = i96;
                                                exc2 = e11;
                                                if (!(exc2 instanceof IllegalStateException)) {
                                                }
                                                StringBuilder sb3222222222 = new StringBuilder();
                                                sb3222222222.append("bitrate: ");
                                                i92 = i88;
                                                sb3222222222.append(i92);
                                                sb3222222222.append(" framerate: ");
                                                i94 = i6;
                                                sb3222222222.append(i94);
                                                sb3222222222.append(" size: ");
                                                sb3222222222.append(i11);
                                                sb3222222222.append("x");
                                                sb3222222222.append(i89);
                                                FileLog.e(sb3222222222.toString());
                                                FileLog.e(exc2);
                                                i95 = i90;
                                                i96 = i89;
                                                z6 = z31;
                                                r9 = byteBufferArr4;
                                                z8 = true;
                                                if (outputSurface7 != null) {
                                                }
                                                if (r9 != 0) {
                                                }
                                                if (mediaCodec10 != null) {
                                                }
                                                checkConversionCanceled();
                                                j5 = j2;
                                                j6 = j3;
                                                i19 = i92;
                                                i20 = i96;
                                                i21 = i11;
                                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                                i9 = i95;
                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                i14 = i21;
                                                i13 = i20;
                                                i15 = i19;
                                                z7 = z8;
                                                if (z6) {
                                                }
                                            }
                                        } catch (Throwable th37) {
                                            th14 = th37;
                                            i11 = i97;
                                        }
                                    } catch (Exception e34) {
                                        e11 = e34;
                                        byteBufferArr4 = outputBuffers2;
                                        mediaCodec10 = createEncoderByType2;
                                        i89 = i96;
                                        exc2 = e11;
                                        if (!(exc2 instanceof IllegalStateException)) {
                                        }
                                        StringBuilder sb32222222222 = new StringBuilder();
                                        sb32222222222.append("bitrate: ");
                                        i92 = i88;
                                        sb32222222222.append(i92);
                                        sb32222222222.append(" framerate: ");
                                        i94 = i6;
                                        sb32222222222.append(i94);
                                        sb32222222222.append(" size: ");
                                        sb32222222222.append(i11);
                                        sb32222222222.append("x");
                                        sb32222222222.append(i89);
                                        FileLog.e(sb32222222222.toString());
                                        FileLog.e(exc2);
                                        i95 = i90;
                                        i96 = i89;
                                        z6 = z31;
                                        r9 = byteBufferArr4;
                                        z8 = true;
                                        if (outputSurface7 != null) {
                                        }
                                        if (r9 != 0) {
                                        }
                                        if (mediaCodec10 != null) {
                                        }
                                        checkConversionCanceled();
                                        j5 = j2;
                                        j6 = j3;
                                        i19 = i92;
                                        i20 = i96;
                                        i21 = i11;
                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                        i9 = i95;
                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor2 != null) {
                                        }
                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder2 != null) {
                                        }
                                        i14 = i21;
                                        i13 = i20;
                                        i15 = i19;
                                        z7 = z8;
                                        if (z6) {
                                        }
                                    }
                                }
                                exc2 = e;
                                mediaCodec10 = createEncoderByType2;
                                i11 = i97;
                                i89 = i96;
                                z31 = !(exc2 instanceof IllegalStateException) && !z3;
                                StringBuilder sb322222222222 = new StringBuilder();
                                sb322222222222.append("bitrate: ");
                                i92 = i88;
                                sb322222222222.append(i92);
                                sb322222222222.append(" framerate: ");
                                i94 = i6;
                                sb322222222222.append(i94);
                                sb322222222222.append(" size: ");
                                sb322222222222.append(i11);
                                sb322222222222.append("x");
                                sb322222222222.append(i89);
                                FileLog.e(sb322222222222.toString());
                                FileLog.e(exc2);
                                i95 = i90;
                                i96 = i89;
                                z6 = z31;
                                r9 = byteBufferArr4;
                                z8 = true;
                                if (outputSurface7 != null) {
                                    try {
                                        outputSurface7.release();
                                    } catch (Throwable th38) {
                                        j5 = j2;
                                        j6 = j3;
                                        th = th38;
                                        i106 = i92;
                                        i10 = i94;
                                        i12 = i96;
                                        mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                        i9 = i95;
                                        StringBuilder sb22222222222222 = new StringBuilder();
                                        sb22222222222222.append("bitrate: ");
                                        sb22222222222222.append(i106);
                                        sb22222222222222.append(" framerate: ");
                                        int i1072222222222222 = i10 == 1 ? 1 : 0;
                                        int i1082222222222222 = i10 == 1 ? 1 : 0;
                                        int i1092222222222222 = i10 == 1 ? 1 : 0;
                                        int i1102222222222222 = i10 == 1 ? 1 : 0;
                                        int i1112222222222222 = i10 == 1 ? 1 : 0;
                                        int i1122222222222222 = i10 == 1 ? 1 : 0;
                                        sb22222222222222.append(i1072222222222222);
                                        sb22222222222222.append(" size: ");
                                        sb22222222222222.append(i11);
                                        sb22222222222222.append("x");
                                        sb22222222222222.append(i12);
                                        FileLog.e(sb22222222222222.toString());
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i13 = i12;
                                        i14 = i11;
                                        i15 = i106;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                }
                                if (r9 != 0) {
                                    r9.release();
                                }
                                if (mediaCodec10 != null) {
                                    mediaCodec10.stop();
                                    mediaCodec10.release();
                                }
                                checkConversionCanceled();
                                j5 = j2;
                                j6 = j3;
                                i19 = i92;
                                i20 = i96;
                                i21 = i11;
                                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                                i9 = i95;
                            }
                            i11 = i97;
                            str15 = str12;
                            z40 = z33;
                            byteBufferArr5 = outputBuffers2;
                            i102 = -1;
                            if (dequeueOutputBuffer2 == i102) {
                            }
                        }
                    }
                }
                i95 = i9;
                i11 = i97;
                mediaCodecVideoConvertor = this;
                r9 = byteBufferArr4;
                mediaCodec10 = createEncoderByType2;
                i92 = i88;
                z6 = false;
                z8 = false;
                i94 = i6;
                if (outputSurface7 != null) {
                }
                if (r9 != 0) {
                }
                if (mediaCodec10 != null) {
                }
                checkConversionCanceled();
                j5 = j2;
                j6 = j3;
                i19 = i92;
                i20 = i96;
                i21 = i11;
                mediaCodecVideoConvertor2 = mediaCodecVideoConvertor;
                i9 = i95;
            } else {
                try {
                    MediaExtractor mediaExtractor4 = new MediaExtractor();
                    this.extractor = mediaExtractor4;
                    mediaExtractor4.setDataSource(str);
                    int findTrack2 = MediaController.findTrack(this.extractor, false);
                    if (i106 != -1) {
                        try {
                            findTrack = MediaController.findTrack(this.extractor, true);
                        } catch (Throwable th39) {
                            j5 = j2;
                            j7 = j3;
                            th = th39;
                            i12 = i4;
                            i11 = i105;
                            i16 = i6;
                            z6 = false;
                            mediaCodecVideoConvertor2 = this;
                            i9 = -5;
                            i10 = i16;
                            j6 = j7;
                            StringBuilder sb222222222222222 = new StringBuilder();
                            sb222222222222222.append("bitrate: ");
                            sb222222222222222.append(i106);
                            sb222222222222222.append(" framerate: ");
                            int i10722222222222222 = i10 == 1 ? 1 : 0;
                            int i10822222222222222 = i10 == 1 ? 1 : 0;
                            int i10922222222222222 = i10 == 1 ? 1 : 0;
                            int i11022222222222222 = i10 == 1 ? 1 : 0;
                            int i11122222222222222 = i10 == 1 ? 1 : 0;
                            int i11222222222222222 = i10 == 1 ? 1 : 0;
                            sb222222222222222.append(i10722222222222222);
                            sb222222222222222.append(" size: ");
                            sb222222222222222.append(i11);
                            sb222222222222222.append("x");
                            sb222222222222222.append(i12);
                            FileLog.e(sb222222222222222.toString());
                            FileLog.e(th);
                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            i13 = i12;
                            i14 = i11;
                            i15 = i106;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    } else {
                        findTrack = -1;
                    }
                    if (findTrack2 >= 0) {
                        if (!this.extractor.getTrackFormat(findTrack2).getString("mime").equals("video/avc")) {
                            z9 = z2;
                            z10 = true;
                            if (!z9 || z10) {
                                if (findTrack2 < 0) {
                                    try {
                                        j10 = (1000 / i6) * 1000;
                                        if (i6 < 30) {
                                            try {
                                                i28 = 1000 / (i6 + 5);
                                            } catch (Exception e35) {
                                                i25 = i7;
                                                j5 = j2;
                                                j9 = j3;
                                                exc = e35;
                                                i24 = findTrack2;
                                                i27 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                mediaCodec3 = null;
                                                i9 = -5;
                                                mediaCodec2 = null;
                                                outputSurface2 = null;
                                                inputSurface = null;
                                                audioRecoder = null;
                                                i23 = i27;
                                                j6 = j9;
                                                try {
                                                    if (!(exc instanceof IllegalStateException)) {
                                                    }
                                                    try {
                                                        StringBuilder sb4 = new StringBuilder();
                                                        sb4.append("bitrate: ");
                                                        sb4.append(i25);
                                                        sb4.append(" framerate: ");
                                                        int i119 = i23 == 1 ? 1 : 0;
                                                        int i120 = i23 == 1 ? 1 : 0;
                                                        int i121 = i23 == 1 ? 1 : 0;
                                                        int i122 = i23 == 1 ? 1 : 0;
                                                        int i123 = i23 == 1 ? 1 : 0;
                                                        int i124 = i23 == 1 ? 1 : 0;
                                                        int i125 = i23 == 1 ? 1 : 0;
                                                        sb4.append(i119);
                                                        sb4.append(" size: ");
                                                        i21 = i5;
                                                        try {
                                                            sb4.append(i21);
                                                            sb4.append("x");
                                                            i20 = i4;
                                                            try {
                                                                sb4.append(i20);
                                                                FileLog.e(sb4.toString());
                                                                FileLog.e(exc);
                                                                i19 = i25;
                                                                mediaCodec4 = mediaCodec3;
                                                                i26 = i9;
                                                                z6 = z11;
                                                                z13 = true;
                                                                try {
                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                    if (mediaCodec4 != null) {
                                                                    }
                                                                    z11 = z6;
                                                                    z12 = z13;
                                                                    mediaCodec = mediaCodec2;
                                                                    outputSurface = outputSurface2;
                                                                    i9 = i26;
                                                                    if (outputSurface != null) {
                                                                    }
                                                                    if (inputSurface != null) {
                                                                    }
                                                                    if (mediaCodec != null) {
                                                                    }
                                                                    if (audioRecoder != null) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    z8 = z12;
                                                                    z6 = z11;
                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor2 != null) {
                                                                    }
                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder2 != null) {
                                                                    }
                                                                    i14 = i21;
                                                                    i13 = i20;
                                                                    i15 = i19;
                                                                    z7 = z8;
                                                                } catch (Throwable th40) {
                                                                    th = th40;
                                                                    i11 = i21;
                                                                    i12 = i20;
                                                                    i106 = i19;
                                                                    i9 = i26;
                                                                    i10 = i23;
                                                                    j6 = j6;
                                                                    StringBuilder sb2222222222222222 = new StringBuilder();
                                                                    sb2222222222222222.append("bitrate: ");
                                                                    sb2222222222222222.append(i106);
                                                                    sb2222222222222222.append(" framerate: ");
                                                                    int i107222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i108222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i109222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i110222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i111222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i112222222222222222 = i10 == 1 ? 1 : 0;
                                                                    sb2222222222222222.append(i107222222222222222);
                                                                    sb2222222222222222.append(" size: ");
                                                                    sb2222222222222222.append(i11);
                                                                    sb2222222222222222.append("x");
                                                                    sb2222222222222222.append(i12);
                                                                    FileLog.e(sb2222222222222222.toString());
                                                                    FileLog.e(th);
                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor != null) {
                                                                    }
                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder != null) {
                                                                    }
                                                                    i13 = i12;
                                                                    i14 = i11;
                                                                    i15 = i106;
                                                                    z7 = true;
                                                                    if (z6) {
                                                                    }
                                                                }
                                                            } catch (Throwable th41) {
                                                                th3 = th41;
                                                                th = th3;
                                                                i106 = i25;
                                                                i11 = i21;
                                                                i12 = i20;
                                                                z6 = z11;
                                                                i10 = i23;
                                                                j6 = j6;
                                                                StringBuilder sb22222222222222222 = new StringBuilder();
                                                                sb22222222222222222.append("bitrate: ");
                                                                sb22222222222222222.append(i106);
                                                                sb22222222222222222.append(" framerate: ");
                                                                int i1072222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i1082222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i1092222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i1102222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i1112222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i1122222222222222222 = i10 == 1 ? 1 : 0;
                                                                sb22222222222222222.append(i1072222222222222222);
                                                                sb22222222222222222.append(" size: ");
                                                                sb22222222222222222.append(i11);
                                                                sb22222222222222222.append("x");
                                                                sb22222222222222222.append(i12);
                                                                FileLog.e(sb22222222222222222.toString());
                                                                FileLog.e(th);
                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                if (mediaExtractor != null) {
                                                                }
                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                if (mP4Builder != null) {
                                                                }
                                                                i13 = i12;
                                                                i14 = i11;
                                                                i15 = i106;
                                                                z7 = true;
                                                                if (z6) {
                                                                }
                                                            }
                                                        } catch (Throwable th42) {
                                                            th3 = th42;
                                                            i20 = i4;
                                                        }
                                                    } catch (Throwable th43) {
                                                        th3 = th43;
                                                        i20 = i4;
                                                        i21 = i5;
                                                    }
                                                } catch (Throwable th44) {
                                                    th = th44;
                                                    i106 = i25;
                                                    i11 = i5;
                                                    i12 = i4;
                                                    i22 = i23;
                                                    j8 = j6;
                                                    z6 = false;
                                                    i10 = i22;
                                                    j6 = j8;
                                                    StringBuilder sb222222222222222222 = new StringBuilder();
                                                    sb222222222222222222.append("bitrate: ");
                                                    sb222222222222222222.append(i106);
                                                    sb222222222222222222.append(" framerate: ");
                                                    int i10722222222222222222 = i10 == 1 ? 1 : 0;
                                                    int i10822222222222222222 = i10 == 1 ? 1 : 0;
                                                    int i10922222222222222222 = i10 == 1 ? 1 : 0;
                                                    int i11022222222222222222 = i10 == 1 ? 1 : 0;
                                                    int i11122222222222222222 = i10 == 1 ? 1 : 0;
                                                    int i11222222222222222222 = i10 == 1 ? 1 : 0;
                                                    sb222222222222222222.append(i10722222222222222222);
                                                    sb222222222222222222.append(" size: ");
                                                    sb222222222222222222.append(i11);
                                                    sb222222222222222222.append("x");
                                                    sb222222222222222222.append(i12);
                                                    FileLog.e(sb222222222222222222.toString());
                                                    FileLog.e(th);
                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                    if (mediaExtractor != null) {
                                                    }
                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                    if (mP4Builder != null) {
                                                    }
                                                    i13 = i12;
                                                    i14 = i11;
                                                    i15 = i106;
                                                    z7 = true;
                                                    if (z6) {
                                                    }
                                                }
                                                if (z6) {
                                                }
                                            } catch (Throwable th45) {
                                                i12 = i4;
                                                i11 = i5;
                                                i106 = i7;
                                                j5 = j2;
                                                j7 = j3;
                                                th = th45;
                                                i16 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                z6 = false;
                                                i9 = -5;
                                                i10 = i16;
                                                j6 = j7;
                                                StringBuilder sb2222222222222222222 = new StringBuilder();
                                                sb2222222222222222222.append("bitrate: ");
                                                sb2222222222222222222.append(i106);
                                                sb2222222222222222222.append(" framerate: ");
                                                int i107222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i108222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i109222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i110222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i111222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i112222222222222222222 = i10 == 1 ? 1 : 0;
                                                sb2222222222222222222.append(i107222222222222222222);
                                                sb2222222222222222222.append(" size: ");
                                                sb2222222222222222222.append(i11);
                                                sb2222222222222222222.append("x");
                                                sb2222222222222222222.append(i12);
                                                FileLog.e(sb2222222222222222222.toString());
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i13 = i12;
                                                i14 = i11;
                                                i15 = i106;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        } else {
                                            i28 = 1000 / (i6 + 1);
                                        }
                                        long j58 = i28 * 1000;
                                        this.extractor.selectTrack(findTrack2);
                                        trackFormat = this.extractor.getTrackFormat(findTrack2);
                                        if (j3 >= 0) {
                                            i25 = f <= 2000.0f ? 2600000 : f <= 5000.0f ? 2200000 : 1560000;
                                            j11 = j58;
                                            j12 = 0;
                                        } else if (i7 <= 0) {
                                            j12 = j3;
                                            j11 = j58;
                                            i25 = 921600;
                                        } else {
                                            i25 = i7;
                                            j12 = j3;
                                            j11 = j58;
                                        }
                                        if (i8 > 0) {
                                            try {
                                                i25 = Math.min(i8, i25);
                                            } catch (Exception e36) {
                                                j5 = j2;
                                                exc = e36;
                                                i24 = findTrack2;
                                                i27 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                j9 = j12;
                                                mediaCodec3 = null;
                                                i9 = -5;
                                                mediaCodec2 = null;
                                                outputSurface2 = null;
                                                inputSurface = null;
                                                audioRecoder = null;
                                                i23 = i27;
                                                j6 = j9;
                                                if (!(exc instanceof IllegalStateException)) {
                                                }
                                                StringBuilder sb42 = new StringBuilder();
                                                sb42.append("bitrate: ");
                                                sb42.append(i25);
                                                sb42.append(" framerate: ");
                                                int i1192 = i23 == 1 ? 1 : 0;
                                                int i1202 = i23 == 1 ? 1 : 0;
                                                int i1212 = i23 == 1 ? 1 : 0;
                                                int i1222 = i23 == 1 ? 1 : 0;
                                                int i1232 = i23 == 1 ? 1 : 0;
                                                int i1242 = i23 == 1 ? 1 : 0;
                                                int i1252 = i23 == 1 ? 1 : 0;
                                                sb42.append(i1192);
                                                sb42.append(" size: ");
                                                i21 = i5;
                                                sb42.append(i21);
                                                sb42.append("x");
                                                i20 = i4;
                                                sb42.append(i20);
                                                FileLog.e(sb42.toString());
                                                FileLog.e(exc);
                                                i19 = i25;
                                                mediaCodec4 = mediaCodec3;
                                                i26 = i9;
                                                z6 = z11;
                                                z13 = true;
                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                if (mediaCodec4 != null) {
                                                }
                                                z11 = z6;
                                                z12 = z13;
                                                mediaCodec = mediaCodec2;
                                                outputSurface = outputSurface2;
                                                i9 = i26;
                                                if (outputSurface != null) {
                                                }
                                                if (inputSurface != null) {
                                                }
                                                if (mediaCodec != null) {
                                                }
                                                if (audioRecoder != null) {
                                                }
                                                checkConversionCanceled();
                                                z8 = z12;
                                                z6 = z11;
                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                i14 = i21;
                                                i13 = i20;
                                                i15 = i19;
                                                z7 = z8;
                                                if (z6) {
                                                }
                                            } catch (Throwable th46) {
                                                i12 = i4;
                                                j5 = j2;
                                                th = th46;
                                                i106 = i25;
                                                i29 = i6;
                                                mediaCodecVideoConvertor2 = this;
                                                j13 = j12;
                                                z6 = false;
                                                i9 = -5;
                                                i33 = i29;
                                                i11 = i5;
                                                i10 = i33;
                                                j6 = j13;
                                                StringBuilder sb22222222222222222222 = new StringBuilder();
                                                sb22222222222222222222.append("bitrate: ");
                                                sb22222222222222222222.append(i106);
                                                sb22222222222222222222.append(" framerate: ");
                                                int i1072222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i1082222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i1092222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i1102222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i1112222222222222222222 = i10 == 1 ? 1 : 0;
                                                int i1122222222222222222222 = i10 == 1 ? 1 : 0;
                                                sb22222222222222222222.append(i1072222222222222222222);
                                                sb22222222222222222222.append(" size: ");
                                                sb22222222222222222222.append(i11);
                                                sb22222222222222222222.append("x");
                                                sb22222222222222222222.append(i12);
                                                FileLog.e(sb22222222222222222222.toString());
                                                FileLog.e(th);
                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                i13 = i12;
                                                i14 = i11;
                                                i15 = i106;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        }
                                        j14 = j12 >= 0 ? -1L : j12;
                                        try {
                                            try {
                                            } catch (Exception e37) {
                                                j5 = j2;
                                                exc = e37;
                                                i24 = findTrack2;
                                                i30 = i6;
                                                j15 = j14;
                                            }
                                        } catch (Throwable th47) {
                                            i12 = i4;
                                            j5 = j2;
                                            th = th47;
                                            i106 = i25;
                                            i16 = i6;
                                            j7 = j14;
                                            z6 = false;
                                            i11 = i5;
                                            mediaCodecVideoConvertor2 = this;
                                            i9 = -5;
                                            i10 = i16;
                                            j6 = j7;
                                            StringBuilder sb222222222222222222222 = new StringBuilder();
                                            sb222222222222222222222.append("bitrate: ");
                                            sb222222222222222222222.append(i106);
                                            sb222222222222222222222.append(" framerate: ");
                                            int i10722222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i10822222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i10922222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i11022222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i11122222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i11222222222222222222222 = i10 == 1 ? 1 : 0;
                                            sb222222222222222222222.append(i10722222222222222222222);
                                            sb222222222222222222222.append(" size: ");
                                            sb222222222222222222222.append(i11);
                                            sb222222222222222222222.append("x");
                                            sb222222222222222222222.append(i12);
                                            FileLog.e(sb222222222222222222222.toString());
                                            FileLog.e(th);
                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            i13 = i12;
                                            i14 = i11;
                                            i15 = i106;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                    } catch (Exception e38) {
                                        e = e38;
                                        i24 = findTrack2;
                                        i27 = i6;
                                        mediaCodecVideoConvertor2 = this;
                                        i25 = i7;
                                    } catch (Throwable th48) {
                                        i16 = i6;
                                        mediaCodecVideoConvertor2 = this;
                                        i12 = i4;
                                        i11 = i5;
                                        i106 = i7;
                                        j5 = j2;
                                        j7 = j3;
                                        th = th48;
                                    }
                                    try {
                                        try {
                                            if (j14 >= 0) {
                                                i32 = findTrack;
                                                j16 = j10;
                                                this.extractor.seekTo(j14, 0);
                                                i31 = findTrack2;
                                                str3 = "csd-0";
                                            } else {
                                                i32 = findTrack;
                                                j16 = j10;
                                                if (j > 0) {
                                                    i31 = findTrack2;
                                                    try {
                                                        this.extractor.seekTo(j, 0);
                                                        cropState2 = cropState;
                                                        str3 = "csd-0";
                                                        if (cropState2 == null) {
                                                            if (i == 90 || i == 270) {
                                                                i85 = cropState2.transformHeight;
                                                                i84 = cropState2.transformWidth;
                                                            } else {
                                                                i85 = cropState2.transformWidth;
                                                                i84 = cropState2.transformHeight;
                                                            }
                                                            int i126 = i84;
                                                            i34 = i85;
                                                            i35 = i126;
                                                        } else {
                                                            i34 = i4;
                                                            i35 = i5;
                                                        }
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            FileLog.d("create encoder with w = " + i34 + " h = " + i35);
                                                        }
                                                        createVideoFormat = MediaFormat.createVideoFormat("video/avc", i34, i35);
                                                        createVideoFormat.setInteger("color-format", 2130708361);
                                                        createVideoFormat.setInteger("bitrate", i25);
                                                        createVideoFormat.setInteger("frame-rate", i6);
                                                        createVideoFormat.setInteger("i-frame-interval", 2);
                                                        i36 = Build.VERSION.SDK_INT;
                                                    } catch (Exception e39) {
                                                        j5 = j2;
                                                        exc = e39;
                                                        i30 = i6;
                                                        j15 = j14;
                                                        i24 = i31;
                                                        mediaCodec3 = null;
                                                        mediaCodec2 = null;
                                                        outputSurface2 = null;
                                                        inputSurface = null;
                                                        audioRecoder = null;
                                                        mediaCodecVideoConvertor2 = this;
                                                        i9 = -5;
                                                        i23 = i30;
                                                        j6 = j15;
                                                        if (!(exc instanceof IllegalStateException)) {
                                                        }
                                                        StringBuilder sb422 = new StringBuilder();
                                                        sb422.append("bitrate: ");
                                                        sb422.append(i25);
                                                        sb422.append(" framerate: ");
                                                        int i11922 = i23 == 1 ? 1 : 0;
                                                        int i12022 = i23 == 1 ? 1 : 0;
                                                        int i12122 = i23 == 1 ? 1 : 0;
                                                        int i12222 = i23 == 1 ? 1 : 0;
                                                        int i12322 = i23 == 1 ? 1 : 0;
                                                        int i12422 = i23 == 1 ? 1 : 0;
                                                        int i12522 = i23 == 1 ? 1 : 0;
                                                        sb422.append(i11922);
                                                        sb422.append(" size: ");
                                                        i21 = i5;
                                                        sb422.append(i21);
                                                        sb422.append("x");
                                                        i20 = i4;
                                                        sb422.append(i20);
                                                        FileLog.e(sb422.toString());
                                                        FileLog.e(exc);
                                                        i19 = i25;
                                                        mediaCodec4 = mediaCodec3;
                                                        i26 = i9;
                                                        z6 = z11;
                                                        z13 = true;
                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                        if (mediaCodec4 != null) {
                                                        }
                                                        z11 = z6;
                                                        z12 = z13;
                                                        mediaCodec = mediaCodec2;
                                                        outputSurface = outputSurface2;
                                                        i9 = i26;
                                                        if (outputSurface != null) {
                                                        }
                                                        if (inputSurface != null) {
                                                        }
                                                        if (mediaCodec != null) {
                                                        }
                                                        if (audioRecoder != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        z8 = z12;
                                                        z6 = z11;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i14 = i21;
                                                        i13 = i20;
                                                        i15 = i19;
                                                        z7 = z8;
                                                        if (z6) {
                                                        }
                                                    }
                                                    if (i36 >= 23) {
                                                        i37 = i35;
                                                        if (Math.min(i35, i34) <= 480) {
                                                            int i127 = 921600;
                                                            if (i25 <= 921600) {
                                                                i127 = i25;
                                                            }
                                                            try {
                                                                createVideoFormat.setInteger("bitrate", i127);
                                                                i19 = i127;
                                                                createEncoderByType = MediaCodec.createEncoderByType("video/avc");
                                                                createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                                                InputSurface inputSurface6 = new InputSurface(createEncoderByType.createInputSurface());
                                                                inputSurface6.makeCurrent();
                                                                createEncoderByType.start();
                                                                createDecoderByType = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                                                                int i128 = i32;
                                                                i39 = i37;
                                                                inputSurface = inputSurface6;
                                                                mediaCodec5 = createEncoderByType;
                                                                i40 = i31;
                                                                bufferInfo = bufferInfo5;
                                                                str4 = "video/avc";
                                                                str5 = "prepend-sps-pps-to-idr-frames";
                                                                i41 = i34;
                                                                str6 = str3;
                                                                j17 = j14;
                                                                mediaCodecVideoConvertor2 = this;
                                                                outputSurface4 = outputSurface3;
                                                                outputSurface3 = new OutputSurface(savedFilterState, null, str2, arrayList, cropState, i4, i5, i2, i3, i, i6, false);
                                                            } catch (Exception e40) {
                                                                j5 = j2;
                                                                exc = e40;
                                                                i25 = i127;
                                                                i23 = i6;
                                                                j6 = j14;
                                                                i24 = i31;
                                                                mediaCodec3 = null;
                                                                i9 = -5;
                                                                mediaCodec2 = null;
                                                                outputSurface2 = null;
                                                                inputSurface = null;
                                                                audioRecoder = null;
                                                                mediaCodecVideoConvertor2 = this;
                                                                if (!(exc instanceof IllegalStateException)) {
                                                                }
                                                                StringBuilder sb4222 = new StringBuilder();
                                                                sb4222.append("bitrate: ");
                                                                sb4222.append(i25);
                                                                sb4222.append(" framerate: ");
                                                                int i119222 = i23 == 1 ? 1 : 0;
                                                                int i120222 = i23 == 1 ? 1 : 0;
                                                                int i121222 = i23 == 1 ? 1 : 0;
                                                                int i122222 = i23 == 1 ? 1 : 0;
                                                                int i123222 = i23 == 1 ? 1 : 0;
                                                                int i124222 = i23 == 1 ? 1 : 0;
                                                                int i125222 = i23 == 1 ? 1 : 0;
                                                                sb4222.append(i119222);
                                                                sb4222.append(" size: ");
                                                                i21 = i5;
                                                                sb4222.append(i21);
                                                                sb4222.append("x");
                                                                i20 = i4;
                                                                sb4222.append(i20);
                                                                FileLog.e(sb4222.toString());
                                                                FileLog.e(exc);
                                                                i19 = i25;
                                                                mediaCodec4 = mediaCodec3;
                                                                i26 = i9;
                                                                z6 = z11;
                                                                z13 = true;
                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                if (mediaCodec4 != null) {
                                                                }
                                                                z11 = z6;
                                                                z12 = z13;
                                                                mediaCodec = mediaCodec2;
                                                                outputSurface = outputSurface2;
                                                                i9 = i26;
                                                                if (outputSurface != null) {
                                                                }
                                                                if (inputSurface != null) {
                                                                }
                                                                if (mediaCodec != null) {
                                                                }
                                                                if (audioRecoder != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                z8 = z12;
                                                                z6 = z11;
                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                if (mediaExtractor2 != null) {
                                                                }
                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                if (mP4Builder2 != null) {
                                                                }
                                                                i14 = i21;
                                                                i13 = i20;
                                                                i15 = i19;
                                                                z7 = z8;
                                                                if (z6) {
                                                                }
                                                            } catch (Throwable th49) {
                                                                i11 = i5;
                                                                j5 = j2;
                                                                th = th49;
                                                                i106 = i127;
                                                                i10 = i6;
                                                                j6 = j14;
                                                                z6 = false;
                                                                i9 = -5;
                                                                mediaCodecVideoConvertor2 = this;
                                                                i12 = i4;
                                                                StringBuilder sb2222222222222222222222 = new StringBuilder();
                                                                sb2222222222222222222222.append("bitrate: ");
                                                                sb2222222222222222222222.append(i106);
                                                                sb2222222222222222222222.append(" framerate: ");
                                                                int i107222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i108222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i109222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i110222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i111222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                int i112222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                sb2222222222222222222222.append(i107222222222222222222222);
                                                                sb2222222222222222222222.append(" size: ");
                                                                sb2222222222222222222222.append(i11);
                                                                sb2222222222222222222222.append("x");
                                                                sb2222222222222222222222.append(i12);
                                                                FileLog.e(sb2222222222222222222222.toString());
                                                                FileLog.e(th);
                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                if (mediaExtractor != null) {
                                                                }
                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                if (mP4Builder != null) {
                                                                }
                                                                i13 = i12;
                                                                i14 = i11;
                                                                i15 = i106;
                                                                z7 = true;
                                                                if (z6) {
                                                                }
                                                            }
                                                            if (!z5) {
                                                                i42 = i5;
                                                                try {
                                                                } catch (Exception e41) {
                                                                    e10 = e41;
                                                                } catch (Throwable th50) {
                                                                    th10 = th50;
                                                                }
                                                                try {
                                                                } catch (Exception e42) {
                                                                    e10 = e42;
                                                                    i83 = i6;
                                                                    j5 = j2;
                                                                    j51 = j17;
                                                                    exc = e10;
                                                                    outputSurface2 = outputSurface4;
                                                                    i25 = i19;
                                                                    mediaCodec3 = createDecoderByType;
                                                                    i24 = i40;
                                                                    i9 = -5;
                                                                    audioRecoder = null;
                                                                    i82 = i83;
                                                                    j50 = j51;
                                                                    mediaCodec2 = mediaCodec5;
                                                                    i23 = i82;
                                                                    j6 = j50;
                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                    }
                                                                    StringBuilder sb42222 = new StringBuilder();
                                                                    sb42222.append("bitrate: ");
                                                                    sb42222.append(i25);
                                                                    sb42222.append(" framerate: ");
                                                                    int i1192222 = i23 == 1 ? 1 : 0;
                                                                    int i1202222 = i23 == 1 ? 1 : 0;
                                                                    int i1212222 = i23 == 1 ? 1 : 0;
                                                                    int i1222222 = i23 == 1 ? 1 : 0;
                                                                    int i1232222 = i23 == 1 ? 1 : 0;
                                                                    int i1242222 = i23 == 1 ? 1 : 0;
                                                                    int i1252222 = i23 == 1 ? 1 : 0;
                                                                    sb42222.append(i1192222);
                                                                    sb42222.append(" size: ");
                                                                    i21 = i5;
                                                                    sb42222.append(i21);
                                                                    sb42222.append("x");
                                                                    i20 = i4;
                                                                    sb42222.append(i20);
                                                                    FileLog.e(sb42222.toString());
                                                                    FileLog.e(exc);
                                                                    i19 = i25;
                                                                    mediaCodec4 = mediaCodec3;
                                                                    i26 = i9;
                                                                    z6 = z11;
                                                                    z13 = true;
                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                    if (mediaCodec4 != null) {
                                                                    }
                                                                    z11 = z6;
                                                                    z12 = z13;
                                                                    mediaCodec = mediaCodec2;
                                                                    outputSurface = outputSurface2;
                                                                    i9 = i26;
                                                                    if (outputSurface != null) {
                                                                    }
                                                                    if (inputSurface != null) {
                                                                    }
                                                                    if (mediaCodec != null) {
                                                                    }
                                                                    if (audioRecoder != null) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    z8 = z12;
                                                                    z6 = z11;
                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor2 != null) {
                                                                    }
                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder2 != null) {
                                                                    }
                                                                    i14 = i21;
                                                                    i13 = i20;
                                                                    i15 = i19;
                                                                    z7 = z8;
                                                                    if (z6) {
                                                                    }
                                                                } catch (Throwable th51) {
                                                                    th10 = th51;
                                                                    i12 = i4;
                                                                    i10 = i6;
                                                                    j5 = j2;
                                                                    j6 = j17;
                                                                    i11 = i42;
                                                                    i106 = i19;
                                                                    z6 = false;
                                                                    i9 = -5;
                                                                    th = th10;
                                                                    StringBuilder sb22222222222222222222222 = new StringBuilder();
                                                                    sb22222222222222222222222.append("bitrate: ");
                                                                    sb22222222222222222222222.append(i106);
                                                                    sb22222222222222222222222.append(" framerate: ");
                                                                    int i1072222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i1082222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i1092222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i1102222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i1112222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                    int i1122222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                    sb22222222222222222222222.append(i1072222222222222222222222);
                                                                    sb22222222222222222222222.append(" size: ");
                                                                    sb22222222222222222222222.append(i11);
                                                                    sb22222222222222222222222.append("x");
                                                                    sb22222222222222222222222.append(i12);
                                                                    FileLog.e(sb22222222222222222222222.toString());
                                                                    FileLog.e(th);
                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                    if (mediaExtractor != null) {
                                                                    }
                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                    if (mP4Builder != null) {
                                                                    }
                                                                    i13 = i12;
                                                                    i14 = i11;
                                                                    i15 = i106;
                                                                    z7 = true;
                                                                    if (z6) {
                                                                    }
                                                                }
                                                                if (Math.max(i42, i42) / Math.max(i3, i2) < 0.9f) {
                                                                    i12 = i4;
                                                                    z14 = true;
                                                                    try {
                                                                        try {
                                                                            outputSurface4.changeFragmentShader(createFragmentShader(i2, i3, i12, i42, true), createFragmentShader(i2, i3, i12, i42, false));
                                                                            mediaCodec3 = createDecoderByType;
                                                                            mediaCodec3.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                                                            mediaCodec3.start();
                                                                            if (i36 >= 21) {
                                                                                try {
                                                                                    inputBuffers = mediaCodec3.getInputBuffers();
                                                                                    outputBuffers = mediaCodec5.getOutputBuffers();
                                                                                } catch (Exception e43) {
                                                                                    i43 = i6;
                                                                                    j5 = j2;
                                                                                    j19 = j17;
                                                                                    exc = e43;
                                                                                    audioRecoder = null;
                                                                                    outputSurface2 = outputSurface4;
                                                                                    i25 = i19;
                                                                                    i24 = i40;
                                                                                    i9 = -5;
                                                                                    i82 = i43;
                                                                                    j50 = j19;
                                                                                    mediaCodec2 = mediaCodec5;
                                                                                    i23 = i82;
                                                                                    j6 = j50;
                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                    }
                                                                                    StringBuilder sb422222 = new StringBuilder();
                                                                                    sb422222.append("bitrate: ");
                                                                                    sb422222.append(i25);
                                                                                    sb422222.append(" framerate: ");
                                                                                    int i11922222 = i23 == 1 ? 1 : 0;
                                                                                    int i12022222 = i23 == 1 ? 1 : 0;
                                                                                    int i12122222 = i23 == 1 ? 1 : 0;
                                                                                    int i12222222 = i23 == 1 ? 1 : 0;
                                                                                    int i12322222 = i23 == 1 ? 1 : 0;
                                                                                    int i12422222 = i23 == 1 ? 1 : 0;
                                                                                    int i12522222 = i23 == 1 ? 1 : 0;
                                                                                    sb422222.append(i11922222);
                                                                                    sb422222.append(" size: ");
                                                                                    i21 = i5;
                                                                                    sb422222.append(i21);
                                                                                    sb422222.append("x");
                                                                                    i20 = i4;
                                                                                    sb422222.append(i20);
                                                                                    FileLog.e(sb422222.toString());
                                                                                    FileLog.e(exc);
                                                                                    i19 = i25;
                                                                                    mediaCodec4 = mediaCodec3;
                                                                                    i26 = i9;
                                                                                    z6 = z11;
                                                                                    z13 = true;
                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                    if (mediaCodec4 != null) {
                                                                                    }
                                                                                    z11 = z6;
                                                                                    z12 = z13;
                                                                                    mediaCodec = mediaCodec2;
                                                                                    outputSurface = outputSurface2;
                                                                                    i9 = i26;
                                                                                    if (outputSurface != null) {
                                                                                    }
                                                                                    if (inputSurface != null) {
                                                                                    }
                                                                                    if (mediaCodec != null) {
                                                                                    }
                                                                                    if (audioRecoder != null) {
                                                                                    }
                                                                                    checkConversionCanceled();
                                                                                    z8 = z12;
                                                                                    z6 = z11;
                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                    if (mediaExtractor2 != null) {
                                                                                    }
                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                    if (mP4Builder2 != null) {
                                                                                    }
                                                                                    i14 = i21;
                                                                                    i13 = i20;
                                                                                    i15 = i19;
                                                                                    z7 = z8;
                                                                                    if (z6) {
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                inputBuffers = null;
                                                                                outputBuffers = null;
                                                                            }
                                                                            i44 = i128;
                                                                        } catch (Exception e44) {
                                                                            e10 = e44;
                                                                            i83 = i6;
                                                                            j5 = j2;
                                                                            j51 = j17;
                                                                            exc = e10;
                                                                            outputSurface2 = outputSurface4;
                                                                            i25 = i19;
                                                                            mediaCodec3 = createDecoderByType;
                                                                            i24 = i40;
                                                                            i9 = -5;
                                                                            audioRecoder = null;
                                                                            i82 = i83;
                                                                            j50 = j51;
                                                                            mediaCodec2 = mediaCodec5;
                                                                            i23 = i82;
                                                                            j6 = j50;
                                                                            if (!(exc instanceof IllegalStateException)) {
                                                                            }
                                                                            StringBuilder sb4222222 = new StringBuilder();
                                                                            sb4222222.append("bitrate: ");
                                                                            sb4222222.append(i25);
                                                                            sb4222222.append(" framerate: ");
                                                                            int i119222222 = i23 == 1 ? 1 : 0;
                                                                            int i120222222 = i23 == 1 ? 1 : 0;
                                                                            int i121222222 = i23 == 1 ? 1 : 0;
                                                                            int i122222222 = i23 == 1 ? 1 : 0;
                                                                            int i123222222 = i23 == 1 ? 1 : 0;
                                                                            int i124222222 = i23 == 1 ? 1 : 0;
                                                                            int i125222222 = i23 == 1 ? 1 : 0;
                                                                            sb4222222.append(i119222222);
                                                                            sb4222222.append(" size: ");
                                                                            i21 = i5;
                                                                            sb4222222.append(i21);
                                                                            sb4222222.append("x");
                                                                            i20 = i4;
                                                                            sb4222222.append(i20);
                                                                            FileLog.e(sb4222222.toString());
                                                                            FileLog.e(exc);
                                                                            i19 = i25;
                                                                            mediaCodec4 = mediaCodec3;
                                                                            i26 = i9;
                                                                            z6 = z11;
                                                                            z13 = true;
                                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                            if (mediaCodec4 != null) {
                                                                            }
                                                                            z11 = z6;
                                                                            z12 = z13;
                                                                            mediaCodec = mediaCodec2;
                                                                            outputSurface = outputSurface2;
                                                                            i9 = i26;
                                                                            if (outputSurface != null) {
                                                                            }
                                                                            if (inputSurface != null) {
                                                                            }
                                                                            if (mediaCodec != null) {
                                                                            }
                                                                            if (audioRecoder != null) {
                                                                            }
                                                                            checkConversionCanceled();
                                                                            z8 = z12;
                                                                            z6 = z11;
                                                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                            if (mediaExtractor2 != null) {
                                                                            }
                                                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                            if (mP4Builder2 != null) {
                                                                            }
                                                                            i14 = i21;
                                                                            i13 = i20;
                                                                            i15 = i19;
                                                                            z7 = z8;
                                                                            if (z6) {
                                                                            }
                                                                        }
                                                                        if (i44 < 0) {
                                                                            try {
                                                                                trackFormat2 = mediaCodecVideoConvertor2.extractor.getTrackFormat(i44);
                                                                            } catch (Exception e45) {
                                                                                e2 = e45;
                                                                            } catch (Throwable th52) {
                                                                                th5 = th52;
                                                                            }
                                                                            if (!trackFormat2.getString("mime").equals("audio/mp4a-latm")) {
                                                                                try {
                                                                                } catch (Exception e46) {
                                                                                    e = e46;
                                                                                    i45 = i6;
                                                                                    j5 = j2;
                                                                                    j20 = j17;
                                                                                    exc = e;
                                                                                    outputSurface2 = outputSurface4;
                                                                                    i25 = i19;
                                                                                    i83 = i45;
                                                                                    j51 = j20;
                                                                                    i24 = i40;
                                                                                    i9 = -5;
                                                                                    audioRecoder = null;
                                                                                    i82 = i83;
                                                                                    j50 = j51;
                                                                                    mediaCodec2 = mediaCodec5;
                                                                                    i23 = i82;
                                                                                    j6 = j50;
                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                    }
                                                                                    StringBuilder sb42222222 = new StringBuilder();
                                                                                    sb42222222.append("bitrate: ");
                                                                                    sb42222222.append(i25);
                                                                                    sb42222222.append(" framerate: ");
                                                                                    int i1192222222 = i23 == 1 ? 1 : 0;
                                                                                    int i1202222222 = i23 == 1 ? 1 : 0;
                                                                                    int i1212222222 = i23 == 1 ? 1 : 0;
                                                                                    int i1222222222 = i23 == 1 ? 1 : 0;
                                                                                    int i1232222222 = i23 == 1 ? 1 : 0;
                                                                                    int i1242222222 = i23 == 1 ? 1 : 0;
                                                                                    int i1252222222 = i23 == 1 ? 1 : 0;
                                                                                    sb42222222.append(i1192222222);
                                                                                    sb42222222.append(" size: ");
                                                                                    i21 = i5;
                                                                                    sb42222222.append(i21);
                                                                                    sb42222222.append("x");
                                                                                    i20 = i4;
                                                                                    sb42222222.append(i20);
                                                                                    FileLog.e(sb42222222.toString());
                                                                                    FileLog.e(exc);
                                                                                    i19 = i25;
                                                                                    mediaCodec4 = mediaCodec3;
                                                                                    i26 = i9;
                                                                                    z6 = z11;
                                                                                    z13 = true;
                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                    if (mediaCodec4 != null) {
                                                                                    }
                                                                                    z11 = z6;
                                                                                    z12 = z13;
                                                                                    mediaCodec = mediaCodec2;
                                                                                    outputSurface = outputSurface2;
                                                                                    i9 = i26;
                                                                                    if (outputSurface != null) {
                                                                                    }
                                                                                    if (inputSurface != null) {
                                                                                    }
                                                                                    if (mediaCodec != null) {
                                                                                    }
                                                                                    if (audioRecoder != null) {
                                                                                    }
                                                                                    checkConversionCanceled();
                                                                                    z8 = z12;
                                                                                    z6 = z11;
                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                    if (mediaExtractor2 != null) {
                                                                                    }
                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                    if (mP4Builder2 != null) {
                                                                                    }
                                                                                    i14 = i21;
                                                                                    i13 = i20;
                                                                                    i15 = i19;
                                                                                    z7 = z8;
                                                                                    if (z6) {
                                                                                    }
                                                                                }
                                                                                if (!trackFormat2.getString("mime").equals("audio/mpeg")) {
                                                                                    z15 = false;
                                                                                    if (trackFormat2.getString("mime").equals("audio/unknown")) {
                                                                                        i44 = -1;
                                                                                    }
                                                                                    if (i44 < 0) {
                                                                                        if (z15) {
                                                                                            try {
                                                                                                int addTrack = mediaCodecVideoConvertor2.mediaMuxer.addTrack(trackFormat2, z14);
                                                                                                mediaCodecVideoConvertor2.extractor.selectTrack(i44);
                                                                                                try {
                                                                                                    i48 = trackFormat2.getInteger("max-input-size");
                                                                                                } catch (Exception e47) {
                                                                                                    FileLog.e(e47);
                                                                                                    i48 = 0;
                                                                                                }
                                                                                                if (i48 <= 0) {
                                                                                                    i48 = 65536;
                                                                                                }
                                                                                                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i48);
                                                                                                byteBufferArr = outputBuffers;
                                                                                                long j59 = j;
                                                                                                if (j59 > 0) {
                                                                                                    mediaCodecVideoConvertor2.extractor.seekTo(j59, 0);
                                                                                                    i49 = i48;
                                                                                                    byteBuffer = allocateDirect;
                                                                                                } else {
                                                                                                    try {
                                                                                                        i49 = i48;
                                                                                                        byteBuffer = allocateDirect;
                                                                                                        mediaCodecVideoConvertor2.extractor.seekTo(0L, 0);
                                                                                                    } catch (Throwable th53) {
                                                                                                        th4 = th53;
                                                                                                        i11 = i5;
                                                                                                        i16 = i6;
                                                                                                        j5 = j2;
                                                                                                        j7 = j17;
                                                                                                        th = th4;
                                                                                                        i106 = i19;
                                                                                                        z6 = false;
                                                                                                        i9 = -5;
                                                                                                        i10 = i16;
                                                                                                        j6 = j7;
                                                                                                        StringBuilder sb222222222222222222222222 = new StringBuilder();
                                                                                                        sb222222222222222222222222.append("bitrate: ");
                                                                                                        sb222222222222222222222222.append(i106);
                                                                                                        sb222222222222222222222222.append(" framerate: ");
                                                                                                        int i10722222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                        int i10822222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                        int i10922222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                        int i11022222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                        int i11122222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                        int i11222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                        sb222222222222222222222222.append(i10722222222222222222222222);
                                                                                                        sb222222222222222222222222.append(" size: ");
                                                                                                        sb222222222222222222222222.append(i11);
                                                                                                        sb222222222222222222222222.append("x");
                                                                                                        sb222222222222222222222222.append(i12);
                                                                                                        FileLog.e(sb222222222222222222222222.toString());
                                                                                                        FileLog.e(th);
                                                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                        if (mediaExtractor != null) {
                                                                                                        }
                                                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                        if (mP4Builder != null) {
                                                                                                        }
                                                                                                        i13 = i12;
                                                                                                        i14 = i11;
                                                                                                        i15 = i106;
                                                                                                        z7 = true;
                                                                                                        if (z6) {
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                j21 = j2;
                                                                                                i47 = i49;
                                                                                                i46 = addTrack;
                                                                                                audioRecoder2 = null;
                                                                                                j23 = j59;
                                                                                            } catch (Exception e48) {
                                                                                                e = e48;
                                                                                                i45 = i6;
                                                                                                j5 = j2;
                                                                                                j20 = j17;
                                                                                                exc = e;
                                                                                                outputSurface2 = outputSurface4;
                                                                                                i25 = i19;
                                                                                                i83 = i45;
                                                                                                j51 = j20;
                                                                                                i24 = i40;
                                                                                                i9 = -5;
                                                                                                audioRecoder = null;
                                                                                                i82 = i83;
                                                                                                j50 = j51;
                                                                                                mediaCodec2 = mediaCodec5;
                                                                                                i23 = i82;
                                                                                                j6 = j50;
                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                }
                                                                                                StringBuilder sb422222222 = new StringBuilder();
                                                                                                sb422222222.append("bitrate: ");
                                                                                                sb422222222.append(i25);
                                                                                                sb422222222.append(" framerate: ");
                                                                                                int i11922222222 = i23 == 1 ? 1 : 0;
                                                                                                int i12022222222 = i23 == 1 ? 1 : 0;
                                                                                                int i12122222222 = i23 == 1 ? 1 : 0;
                                                                                                int i12222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i12322222222 = i23 == 1 ? 1 : 0;
                                                                                                int i12422222222 = i23 == 1 ? 1 : 0;
                                                                                                int i12522222222 = i23 == 1 ? 1 : 0;
                                                                                                sb422222222.append(i11922222222);
                                                                                                sb422222222.append(" size: ");
                                                                                                i21 = i5;
                                                                                                sb422222222.append(i21);
                                                                                                sb422222222.append("x");
                                                                                                i20 = i4;
                                                                                                sb422222222.append(i20);
                                                                                                FileLog.e(sb422222222.toString());
                                                                                                FileLog.e(exc);
                                                                                                i19 = i25;
                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                i26 = i9;
                                                                                                z6 = z11;
                                                                                                z13 = true;
                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                z11 = z6;
                                                                                                z12 = z13;
                                                                                                mediaCodec = mediaCodec2;
                                                                                                outputSurface = outputSurface2;
                                                                                                i9 = i26;
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (audioRecoder != null) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                z8 = z12;
                                                                                                z6 = z11;
                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor2 != null) {
                                                                                                }
                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder2 != null) {
                                                                                                }
                                                                                                i14 = i21;
                                                                                                i13 = i20;
                                                                                                i15 = i19;
                                                                                                z7 = z8;
                                                                                                if (z6) {
                                                                                                }
                                                                                            } catch (Throwable th54) {
                                                                                                th4 = th54;
                                                                                            }
                                                                                        } else {
                                                                                            byteBufferArr = outputBuffers;
                                                                                            long j60 = j;
                                                                                            try {
                                                                                                try {
                                                                                                    MediaExtractor mediaExtractor5 = new MediaExtractor();
                                                                                                    mediaExtractor5.setDataSource(str);
                                                                                                    mediaExtractor5.selectTrack(i44);
                                                                                                    z16 = z15;
                                                                                                    if (j60 > 0) {
                                                                                                        try {
                                                                                                            mediaExtractor5.seekTo(j60, 0);
                                                                                                        } catch (Throwable th55) {
                                                                                                            i16 = i6;
                                                                                                            j5 = j2;
                                                                                                            j7 = j17;
                                                                                                            th = th55;
                                                                                                            i11 = i5;
                                                                                                            i106 = i19;
                                                                                                            z6 = false;
                                                                                                            i9 = -5;
                                                                                                            i10 = i16;
                                                                                                            j6 = j7;
                                                                                                            StringBuilder sb2222222222222222222222222 = new StringBuilder();
                                                                                                            sb2222222222222222222222222.append("bitrate: ");
                                                                                                            sb2222222222222222222222222.append(i106);
                                                                                                            sb2222222222222222222222222.append(" framerate: ");
                                                                                                            int i107222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i108222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i109222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i110222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i111222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i112222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            sb2222222222222222222222222.append(i107222222222222222222222222);
                                                                                                            sb2222222222222222222222222.append(" size: ");
                                                                                                            sb2222222222222222222222222.append(i11);
                                                                                                            sb2222222222222222222222222.append("x");
                                                                                                            sb2222222222222222222222222.append(i12);
                                                                                                            FileLog.e(sb2222222222222222222222222.toString());
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            i13 = i12;
                                                                                                            i14 = i11;
                                                                                                            i15 = i106;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    } else {
                                                                                                        mediaExtractor5.seekTo(0L, 0);
                                                                                                    }
                                                                                                    audioRecoder3 = new AudioRecoder(trackFormat2, mediaExtractor5, i44);
                                                                                                    try {
                                                                                                        audioRecoder3.startTime = j60;
                                                                                                        j21 = j2;
                                                                                                    } catch (Exception e49) {
                                                                                                        e3 = e49;
                                                                                                        j21 = j2;
                                                                                                    }
                                                                                                } catch (Exception e50) {
                                                                                                    e2 = e50;
                                                                                                    i45 = i6;
                                                                                                    j20 = j17;
                                                                                                    exc = e2;
                                                                                                    j5 = j2;
                                                                                                    outputSurface2 = outputSurface4;
                                                                                                    i25 = i19;
                                                                                                    i83 = i45;
                                                                                                    j51 = j20;
                                                                                                    i24 = i40;
                                                                                                    i9 = -5;
                                                                                                    audioRecoder = null;
                                                                                                    i82 = i83;
                                                                                                    j50 = j51;
                                                                                                    mediaCodec2 = mediaCodec5;
                                                                                                    i23 = i82;
                                                                                                    j6 = j50;
                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                    }
                                                                                                    StringBuilder sb4222222222 = new StringBuilder();
                                                                                                    sb4222222222.append("bitrate: ");
                                                                                                    sb4222222222.append(i25);
                                                                                                    sb4222222222.append(" framerate: ");
                                                                                                    int i119222222222 = i23 == 1 ? 1 : 0;
                                                                                                    int i120222222222 = i23 == 1 ? 1 : 0;
                                                                                                    int i121222222222 = i23 == 1 ? 1 : 0;
                                                                                                    int i122222222222 = i23 == 1 ? 1 : 0;
                                                                                                    int i123222222222 = i23 == 1 ? 1 : 0;
                                                                                                    int i124222222222 = i23 == 1 ? 1 : 0;
                                                                                                    int i125222222222 = i23 == 1 ? 1 : 0;
                                                                                                    sb4222222222.append(i119222222222);
                                                                                                    sb4222222222.append(" size: ");
                                                                                                    i21 = i5;
                                                                                                    sb4222222222.append(i21);
                                                                                                    sb4222222222.append("x");
                                                                                                    i20 = i4;
                                                                                                    sb4222222222.append(i20);
                                                                                                    FileLog.e(sb4222222222.toString());
                                                                                                    FileLog.e(exc);
                                                                                                    i19 = i25;
                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                    i26 = i9;
                                                                                                    z6 = z11;
                                                                                                    z13 = true;
                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                    if (mediaCodec4 != null) {
                                                                                                    }
                                                                                                    z11 = z6;
                                                                                                    z12 = z13;
                                                                                                    mediaCodec = mediaCodec2;
                                                                                                    outputSurface = outputSurface2;
                                                                                                    i9 = i26;
                                                                                                    if (outputSurface != null) {
                                                                                                    }
                                                                                                    if (inputSurface != null) {
                                                                                                    }
                                                                                                    if (mediaCodec != null) {
                                                                                                    }
                                                                                                    if (audioRecoder != null) {
                                                                                                    }
                                                                                                    checkConversionCanceled();
                                                                                                    z8 = z12;
                                                                                                    z6 = z11;
                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                    if (mediaExtractor2 != null) {
                                                                                                    }
                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                    if (mP4Builder2 != null) {
                                                                                                    }
                                                                                                    i14 = i21;
                                                                                                    i13 = i20;
                                                                                                    i15 = i19;
                                                                                                    z7 = z8;
                                                                                                    if (z6) {
                                                                                                    }
                                                                                                }
                                                                                            } catch (Throwable th56) {
                                                                                                th5 = th56;
                                                                                                j21 = j2;
                                                                                                i11 = i5;
                                                                                                i16 = i6;
                                                                                                j7 = j17;
                                                                                                th = th5;
                                                                                                j5 = j21;
                                                                                                i106 = i19;
                                                                                                z6 = false;
                                                                                                i9 = -5;
                                                                                                i10 = i16;
                                                                                                j6 = j7;
                                                                                                StringBuilder sb22222222222222222222222222 = new StringBuilder();
                                                                                                sb22222222222222222222222222.append("bitrate: ");
                                                                                                sb22222222222222222222222222.append(i106);
                                                                                                sb22222222222222222222222222.append(" framerate: ");
                                                                                                int i1072222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i1082222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i1092222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i1102222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i1112222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i1122222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                sb22222222222222222222222222.append(i1072222222222222222222222222);
                                                                                                sb22222222222222222222222222.append(" size: ");
                                                                                                sb22222222222222222222222222.append(i11);
                                                                                                sb22222222222222222222222222.append("x");
                                                                                                sb22222222222222222222222222.append(i12);
                                                                                                FileLog.e(sb22222222222222222222222222.toString());
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                i13 = i12;
                                                                                                i14 = i11;
                                                                                                i15 = i106;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                            try {
                                                                                                audioRecoder3.endTime = j21;
                                                                                                i46 = mediaCodecVideoConvertor2.mediaMuxer.addTrack(audioRecoder3.format, true);
                                                                                                z15 = z16;
                                                                                                byteBuffer = null;
                                                                                                audioRecoder2 = audioRecoder3;
                                                                                                i47 = 0;
                                                                                                j23 = j60;
                                                                                            } catch (Exception e51) {
                                                                                                e3 = e51;
                                                                                                i43 = i6;
                                                                                                j19 = j17;
                                                                                                exc = e3;
                                                                                                audioRecoder = audioRecoder3;
                                                                                                j5 = j21;
                                                                                                outputSurface2 = outputSurface4;
                                                                                                i25 = i19;
                                                                                                i24 = i40;
                                                                                                i9 = -5;
                                                                                                i82 = i43;
                                                                                                j50 = j19;
                                                                                                mediaCodec2 = mediaCodec5;
                                                                                                i23 = i82;
                                                                                                j6 = j50;
                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                }
                                                                                                StringBuilder sb42222222222 = new StringBuilder();
                                                                                                sb42222222222.append("bitrate: ");
                                                                                                sb42222222222.append(i25);
                                                                                                sb42222222222.append(" framerate: ");
                                                                                                int i1192222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i1202222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i1212222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i1222222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i1232222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i1242222222222 = i23 == 1 ? 1 : 0;
                                                                                                int i1252222222222 = i23 == 1 ? 1 : 0;
                                                                                                sb42222222222.append(i1192222222222);
                                                                                                sb42222222222.append(" size: ");
                                                                                                i21 = i5;
                                                                                                sb42222222222.append(i21);
                                                                                                sb42222222222.append("x");
                                                                                                i20 = i4;
                                                                                                sb42222222222.append(i20);
                                                                                                FileLog.e(sb42222222222.toString());
                                                                                                FileLog.e(exc);
                                                                                                i19 = i25;
                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                i26 = i9;
                                                                                                z6 = z11;
                                                                                                z13 = true;
                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                z11 = z6;
                                                                                                z12 = z13;
                                                                                                mediaCodec = mediaCodec2;
                                                                                                outputSurface = outputSurface2;
                                                                                                i9 = i26;
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (audioRecoder != null) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                z8 = z12;
                                                                                                z6 = z11;
                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor2 != null) {
                                                                                                }
                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder2 != null) {
                                                                                                }
                                                                                                i14 = i21;
                                                                                                i13 = i20;
                                                                                                i15 = i19;
                                                                                                z7 = z8;
                                                                                                if (z6) {
                                                                                                }
                                                                                            } catch (Throwable th57) {
                                                                                                th5 = th57;
                                                                                                i11 = i5;
                                                                                                i16 = i6;
                                                                                                j7 = j17;
                                                                                                th = th5;
                                                                                                j5 = j21;
                                                                                                i106 = i19;
                                                                                                z6 = false;
                                                                                                i9 = -5;
                                                                                                i10 = i16;
                                                                                                j6 = j7;
                                                                                                StringBuilder sb222222222222222222222222222 = new StringBuilder();
                                                                                                sb222222222222222222222222222.append("bitrate: ");
                                                                                                sb222222222222222222222222222.append(i106);
                                                                                                sb222222222222222222222222222.append(" framerate: ");
                                                                                                int i10722222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i10822222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i10922222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i11022222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i11122222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                int i11222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                sb222222222222222222222222222.append(i10722222222222222222222222222);
                                                                                                sb222222222222222222222222222.append(" size: ");
                                                                                                sb222222222222222222222222222.append(i11);
                                                                                                sb222222222222222222222222222.append("x");
                                                                                                sb222222222222222222222222222.append(i12);
                                                                                                FileLog.e(sb222222222222222222222222222.toString());
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                i13 = i12;
                                                                                                i14 = i11;
                                                                                                i15 = i106;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        boolean z49 = i44 < 0;
                                                                                        checkConversionCanceled();
                                                                                        j24 = j17;
                                                                                        long j61 = -1;
                                                                                        long j62 = -1;
                                                                                        ByteBuffer byteBuffer9 = byteBuffer;
                                                                                        j25 = -2147483648L;
                                                                                        outputSurface2 = null;
                                                                                        z17 = false;
                                                                                        i26 = -5;
                                                                                        z18 = false;
                                                                                        boolean z50 = true;
                                                                                        long j63 = 0;
                                                                                        long j64 = 0;
                                                                                        boolean z51 = z49;
                                                                                        i50 = 0;
                                                                                        byteBufferArr2 = inputBuffers;
                                                                                        inputSurface2 = j23;
                                                                                        loop4: while (true) {
                                                                                            if (outputSurface2 != null || (!z15 && !z51)) {
                                                                                                try {
                                                                                                    checkConversionCanceled();
                                                                                                    if (!z15 || audioRecoder2 == null) {
                                                                                                        i52 = i50;
                                                                                                    } else {
                                                                                                        i52 = i50;
                                                                                                        try {
                                                                                                            try {
                                                                                                                z51 = audioRecoder2.step(mediaCodecVideoConvertor2.mediaMuxer, i46);
                                                                                                            } catch (Exception e52) {
                                                                                                                i82 = i6;
                                                                                                                j50 = j24;
                                                                                                                exc = e52;
                                                                                                                audioRecoder = audioRecoder2;
                                                                                                                j5 = j21;
                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                i25 = i19;
                                                                                                                i9 = i26;
                                                                                                                i24 = i40;
                                                                                                                mediaCodec2 = mediaCodec5;
                                                                                                                i23 = i82;
                                                                                                                j6 = j50;
                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                }
                                                                                                                StringBuilder sb422222222222 = new StringBuilder();
                                                                                                                sb422222222222.append("bitrate: ");
                                                                                                                sb422222222222.append(i25);
                                                                                                                sb422222222222.append(" framerate: ");
                                                                                                                int i11922222222222 = i23 == 1 ? 1 : 0;
                                                                                                                int i12022222222222 = i23 == 1 ? 1 : 0;
                                                                                                                int i12122222222222 = i23 == 1 ? 1 : 0;
                                                                                                                int i12222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                int i12322222222222 = i23 == 1 ? 1 : 0;
                                                                                                                int i12422222222222 = i23 == 1 ? 1 : 0;
                                                                                                                int i12522222222222 = i23 == 1 ? 1 : 0;
                                                                                                                sb422222222222.append(i11922222222222);
                                                                                                                sb422222222222.append(" size: ");
                                                                                                                i21 = i5;
                                                                                                                sb422222222222.append(i21);
                                                                                                                sb422222222222.append("x");
                                                                                                                i20 = i4;
                                                                                                                sb422222222222.append(i20);
                                                                                                                FileLog.e(sb422222222222.toString());
                                                                                                                FileLog.e(exc);
                                                                                                                i19 = i25;
                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                i26 = i9;
                                                                                                                z6 = z11;
                                                                                                                z13 = true;
                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                if (mediaCodec4 != null) {
                                                                                                                }
                                                                                                                z11 = z6;
                                                                                                                z12 = z13;
                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                outputSurface = outputSurface2;
                                                                                                                i9 = i26;
                                                                                                                if (outputSurface != null) {
                                                                                                                }
                                                                                                                if (inputSurface != null) {
                                                                                                                }
                                                                                                                if (mediaCodec != null) {
                                                                                                                }
                                                                                                                if (audioRecoder != null) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                z8 = z12;
                                                                                                                z6 = z11;
                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                }
                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                if (mP4Builder2 != null) {
                                                                                                                }
                                                                                                                i14 = i21;
                                                                                                                i13 = i20;
                                                                                                                i15 = i19;
                                                                                                                z7 = z8;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            }
                                                                                                        } catch (Throwable th58) {
                                                                                                            i11 = i5;
                                                                                                            i51 = i6;
                                                                                                            j26 = j24;
                                                                                                            th = th58;
                                                                                                            j5 = j21;
                                                                                                            i106 = i19;
                                                                                                            i9 = i26;
                                                                                                            i22 = i51;
                                                                                                            j8 = j26;
                                                                                                            z6 = false;
                                                                                                            i10 = i22;
                                                                                                            j6 = j8;
                                                                                                            StringBuilder sb2222222222222222222222222222 = new StringBuilder();
                                                                                                            sb2222222222222222222222222222.append("bitrate: ");
                                                                                                            sb2222222222222222222222222222.append(i106);
                                                                                                            sb2222222222222222222222222222.append(" framerate: ");
                                                                                                            int i107222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i108222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i109222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i110222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i111222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            int i112222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                            sb2222222222222222222222222222.append(i107222222222222222222222222222);
                                                                                                            sb2222222222222222222222222222.append(" size: ");
                                                                                                            sb2222222222222222222222222222.append(i11);
                                                                                                            sb2222222222222222222222222222.append("x");
                                                                                                            sb2222222222222222222222222222.append(i12);
                                                                                                            FileLog.e(sb2222222222222222222222222222.toString());
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            i13 = i12;
                                                                                                            i14 = i11;
                                                                                                            i15 = i106;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    if (!z18) {
                                                                                                        try {
                                                                                                            int sampleTrackIndex = mediaCodecVideoConvertor2.extractor.getSampleTrackIndex();
                                                                                                            audioRecoder = audioRecoder2;
                                                                                                            int i129 = i40;
                                                                                                            if (sampleTrackIndex == i129) {
                                                                                                                ?? r4 = 2500;
                                                                                                                try {
                                                                                                                    int dequeueInputBuffer = mediaCodec3.dequeueInputBuffer(2500L);
                                                                                                                    if (dequeueInputBuffer >= 0) {
                                                                                                                        if (Build.VERSION.SDK_INT < 21) {
                                                                                                                            byteBuffer2 = byteBufferArr2[dequeueInputBuffer];
                                                                                                                        } else {
                                                                                                                            byteBuffer2 = mediaCodec3.getInputBuffer(dequeueInputBuffer);
                                                                                                                        }
                                                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                                                        int readSampleData = mediaCodecVideoConvertor2.extractor.readSampleData(byteBuffer2, 0);
                                                                                                                        if (readSampleData < 0) {
                                                                                                                            int i130 = dequeueInputBuffer;
                                                                                                                            mediaCodec3.queueInputBuffer(i130, 0, 0, 0L, 4);
                                                                                                                            z18 = true;
                                                                                                                            r4 = byteBuffer2;
                                                                                                                            i59 = i130;
                                                                                                                        } else {
                                                                                                                            int i131 = dequeueInputBuffer;
                                                                                                                            mediaCodec3.queueInputBuffer(i131, 0, readSampleData, mediaCodecVideoConvertor2.extractor.getSampleTime(), 0);
                                                                                                                            mediaCodecVideoConvertor2.extractor.advance();
                                                                                                                            r4 = byteBuffer2;
                                                                                                                            i59 = i131;
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                                                        i59 = j28;
                                                                                                                    }
                                                                                                                    i55 = i46;
                                                                                                                    i24 = i129;
                                                                                                                    i54 = i44;
                                                                                                                    j5 = j21;
                                                                                                                    bufferInfo2 = bufferInfo;
                                                                                                                    j30 = 2500;
                                                                                                                    z20 = false;
                                                                                                                    z19 = z15;
                                                                                                                    j29 = j;
                                                                                                                    inputSurface2 = r4;
                                                                                                                    j28 = i59;
                                                                                                                } catch (Exception e53) {
                                                                                                                    i57 = i6;
                                                                                                                    j31 = j24;
                                                                                                                    exc = e53;
                                                                                                                    i24 = i129;
                                                                                                                    j5 = j21;
                                                                                                                    i53 = i57;
                                                                                                                    j27 = j31;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    i25 = i19;
                                                                                                                    i9 = i26;
                                                                                                                    i82 = i53;
                                                                                                                    j50 = j27;
                                                                                                                    mediaCodec2 = mediaCodec5;
                                                                                                                    i23 = i82;
                                                                                                                    j6 = j50;
                                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                                    }
                                                                                                                    StringBuilder sb4222222222222 = new StringBuilder();
                                                                                                                    sb4222222222222.append("bitrate: ");
                                                                                                                    sb4222222222222.append(i25);
                                                                                                                    sb4222222222222.append(" framerate: ");
                                                                                                                    int i119222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i120222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i121222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i122222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i123222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i124222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i125222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    sb4222222222222.append(i119222222222222);
                                                                                                                    sb4222222222222.append(" size: ");
                                                                                                                    i21 = i5;
                                                                                                                    sb4222222222222.append(i21);
                                                                                                                    sb4222222222222.append("x");
                                                                                                                    i20 = i4;
                                                                                                                    sb4222222222222.append(i20);
                                                                                                                    FileLog.e(sb4222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i19 = i25;
                                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                                    i26 = i9;
                                                                                                                    z6 = z11;
                                                                                                                    z13 = true;
                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    z11 = z6;
                                                                                                                    z12 = z13;
                                                                                                                    mediaCodec = mediaCodec2;
                                                                                                                    outputSurface = outputSurface2;
                                                                                                                    i9 = i26;
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    z8 = z12;
                                                                                                                    z6 = z11;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    i14 = i21;
                                                                                                                    i13 = i20;
                                                                                                                    i15 = i19;
                                                                                                                    z7 = z8;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                byteBufferArr3 = byteBufferArr2;
                                                                                                                if (z15) {
                                                                                                                    i56 = -1;
                                                                                                                    if (i44 == -1) {
                                                                                                                        i55 = i46;
                                                                                                                        i24 = i129;
                                                                                                                        i54 = i44;
                                                                                                                        j5 = j21;
                                                                                                                        bufferInfo2 = bufferInfo;
                                                                                                                        j30 = 2500;
                                                                                                                        z19 = z15;
                                                                                                                        j29 = j;
                                                                                                                        if (sampleTrackIndex == i56) {
                                                                                                                            z20 = true;
                                                                                                                            inputSurface2 = inputSurface2;
                                                                                                                            j28 = j28;
                                                                                                                        }
                                                                                                                        z20 = false;
                                                                                                                        inputSurface2 = inputSurface2;
                                                                                                                        j28 = j28;
                                                                                                                    } else if (sampleTrackIndex == i44) {
                                                                                                                        try {
                                                                                                                            try {
                                                                                                                                int i132 = Build.VERSION.SDK_INT;
                                                                                                                                if (i132 >= 28) {
                                                                                                                                    try {
                                                                                                                                        long sampleSize = mediaCodecVideoConvertor2.extractor.getSampleSize();
                                                                                                                                        i24 = i129;
                                                                                                                                        i54 = i44;
                                                                                                                                        if (sampleSize > i47) {
                                                                                                                                            i47 = (int) (sampleSize + 1024);
                                                                                                                                            try {
                                                                                                                                                byteBuffer9 = ByteBuffer.allocateDirect(i47);
                                                                                                                                            } catch (Exception e54) {
                                                                                                                                                e5 = e54;
                                                                                                                                                i57 = i6;
                                                                                                                                                j31 = j24;
                                                                                                                                                exc = e5;
                                                                                                                                                j5 = j21;
                                                                                                                                                i53 = i57;
                                                                                                                                                j27 = j31;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                i25 = i19;
                                                                                                                                                i9 = i26;
                                                                                                                                                i82 = i53;
                                                                                                                                                j50 = j27;
                                                                                                                                                mediaCodec2 = mediaCodec5;
                                                                                                                                                i23 = i82;
                                                                                                                                                j6 = j50;
                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                }
                                                                                                                                                StringBuilder sb42222222222222 = new StringBuilder();
                                                                                                                                                sb42222222222222.append("bitrate: ");
                                                                                                                                                sb42222222222222.append(i25);
                                                                                                                                                sb42222222222222.append(" framerate: ");
                                                                                                                                                int i1192222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i1202222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i1212222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i1222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i1232222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i1242222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i1252222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                sb42222222222222.append(i1192222222222222);
                                                                                                                                                sb42222222222222.append(" size: ");
                                                                                                                                                i21 = i5;
                                                                                                                                                sb42222222222222.append(i21);
                                                                                                                                                sb42222222222222.append("x");
                                                                                                                                                i20 = i4;
                                                                                                                                                sb42222222222222.append(i20);
                                                                                                                                                FileLog.e(sb42222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i19 = i25;
                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                i26 = i9;
                                                                                                                                                z6 = z11;
                                                                                                                                                z13 = true;
                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                z11 = z6;
                                                                                                                                                z12 = z13;
                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                i9 = i26;
                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                z8 = z12;
                                                                                                                                                z6 = z11;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                i14 = i21;
                                                                                                                                                i13 = i20;
                                                                                                                                                i15 = i19;
                                                                                                                                                z7 = z8;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } catch (Exception e55) {
                                                                                                                                        e5 = e55;
                                                                                                                                        i24 = i129;
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    i24 = i129;
                                                                                                                                    i54 = i44;
                                                                                                                                }
                                                                                                                                inputSurface2 = byteBuffer9;
                                                                                                                                try {
                                                                                                                                    bufferInfo2 = bufferInfo;
                                                                                                                                    bufferInfo2.size = mediaCodecVideoConvertor2.extractor.readSampleData(inputSurface2, 0);
                                                                                                                                    if (i132 < 21) {
                                                                                                                                        inputSurface2.position(0);
                                                                                                                                        inputSurface2.limit(bufferInfo2.size);
                                                                                                                                    }
                                                                                                                                    if (bufferInfo2.size >= 0) {
                                                                                                                                        i58 = i47;
                                                                                                                                        bufferInfo2.presentationTimeUs = mediaCodecVideoConvertor2.extractor.getSampleTime();
                                                                                                                                        mediaCodecVideoConvertor2.extractor.advance();
                                                                                                                                    } else {
                                                                                                                                        i58 = i47;
                                                                                                                                        bufferInfo2.size = 0;
                                                                                                                                        z18 = true;
                                                                                                                                    }
                                                                                                                                    if (bufferInfo2.size > 0 && (j21 < 0 || bufferInfo2.presentationTimeUs < j21)) {
                                                                                                                                        bufferInfo2.offset = 0;
                                                                                                                                        bufferInfo2.flags = mediaCodecVideoConvertor2.extractor.getSampleFlags();
                                                                                                                                        long writeSampleData2 = mediaCodecVideoConvertor2.mediaMuxer.writeSampleData(i46, inputSurface2, bufferInfo2, false);
                                                                                                                                        if (writeSampleData2 != 0 && (videoConvertorListener = mediaCodecVideoConvertor2.callback) != null) {
                                                                                                                                            j5 = j21;
                                                                                                                                            try {
                                                                                                                                                long j65 = bufferInfo2.presentationTimeUs;
                                                                                                                                                i55 = i46;
                                                                                                                                                z19 = z15;
                                                                                                                                                j30 = 2500;
                                                                                                                                                j29 = j;
                                                                                                                                                if (j65 - j29 > j63) {
                                                                                                                                                    j63 = j65 - j29;
                                                                                                                                                }
                                                                                                                                                j32 = j63;
                                                                                                                                            } catch (Exception e56) {
                                                                                                                                                e4 = e56;
                                                                                                                                                i53 = i6;
                                                                                                                                                j27 = j24;
                                                                                                                                                exc = e4;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                i25 = i19;
                                                                                                                                                i9 = i26;
                                                                                                                                                i82 = i53;
                                                                                                                                                j50 = j27;
                                                                                                                                                mediaCodec2 = mediaCodec5;
                                                                                                                                                i23 = i82;
                                                                                                                                                j6 = j50;
                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                }
                                                                                                                                                StringBuilder sb422222222222222 = new StringBuilder();
                                                                                                                                                sb422222222222222.append("bitrate: ");
                                                                                                                                                sb422222222222222.append(i25);
                                                                                                                                                sb422222222222222.append(" framerate: ");
                                                                                                                                                int i11922222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i12022222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i12122222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i12222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i12322222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i12422222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i12522222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                sb422222222222222.append(i11922222222222222);
                                                                                                                                                sb422222222222222.append(" size: ");
                                                                                                                                                i21 = i5;
                                                                                                                                                sb422222222222222.append(i21);
                                                                                                                                                sb422222222222222.append("x");
                                                                                                                                                i20 = i4;
                                                                                                                                                sb422222222222222.append(i20);
                                                                                                                                                FileLog.e(sb422222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i19 = i25;
                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                i26 = i9;
                                                                                                                                                z6 = z11;
                                                                                                                                                z13 = true;
                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                z11 = z6;
                                                                                                                                                z12 = z13;
                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                i9 = i26;
                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                z8 = z12;
                                                                                                                                                z6 = z11;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                i14 = i21;
                                                                                                                                                i13 = i20;
                                                                                                                                                i15 = i19;
                                                                                                                                                z7 = z8;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            } catch (Throwable th59) {
                                                                                                                                                th6 = th59;
                                                                                                                                                i12 = i4;
                                                                                                                                                i11 = i5;
                                                                                                                                                i51 = i6;
                                                                                                                                                j26 = j24;
                                                                                                                                                th = th6;
                                                                                                                                                i106 = i19;
                                                                                                                                                i9 = i26;
                                                                                                                                                i22 = i51;
                                                                                                                                                j8 = j26;
                                                                                                                                                z6 = false;
                                                                                                                                                i10 = i22;
                                                                                                                                                j6 = j8;
                                                                                                                                                StringBuilder sb22222222222222222222222222222 = new StringBuilder();
                                                                                                                                                sb22222222222222222222222222222.append("bitrate: ");
                                                                                                                                                sb22222222222222222222222222222.append(i106);
                                                                                                                                                sb22222222222222222222222222222.append(" framerate: ");
                                                                                                                                                int i1072222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1082222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1092222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1102222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1112222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1122222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                sb22222222222222222222222222222.append(i1072222222222222222222222222222);
                                                                                                                                                sb22222222222222222222222222222.append(" size: ");
                                                                                                                                                sb22222222222222222222222222222.append(i11);
                                                                                                                                                sb22222222222222222222222222222.append("x");
                                                                                                                                                sb22222222222222222222222222222.append(i12);
                                                                                                                                                FileLog.e(sb22222222222222222222222222222.toString());
                                                                                                                                                FileLog.e(th);
                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                }
                                                                                                                                                i13 = i12;
                                                                                                                                                i14 = i11;
                                                                                                                                                i15 = i106;
                                                                                                                                                z7 = true;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            try {
                                                                                                                                                videoConvertorListener.didWriteData(writeSampleData2, (((float) j32) / 1000.0f) / f);
                                                                                                                                                j63 = j32;
                                                                                                                                                i47 = i58;
                                                                                                                                                byteBuffer9 = inputSurface2;
                                                                                                                                                z20 = false;
                                                                                                                                                inputSurface2 = inputSurface2;
                                                                                                                                                j28 = j28;
                                                                                                                                            } catch (Exception e57) {
                                                                                                                                                e4 = e57;
                                                                                                                                                i53 = i6;
                                                                                                                                                j27 = j24;
                                                                                                                                                exc = e4;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                i25 = i19;
                                                                                                                                                i9 = i26;
                                                                                                                                                i82 = i53;
                                                                                                                                                j50 = j27;
                                                                                                                                                mediaCodec2 = mediaCodec5;
                                                                                                                                                i23 = i82;
                                                                                                                                                j6 = j50;
                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                }
                                                                                                                                                StringBuilder sb4222222222222222 = new StringBuilder();
                                                                                                                                                sb4222222222222222.append("bitrate: ");
                                                                                                                                                sb4222222222222222.append(i25);
                                                                                                                                                sb4222222222222222.append(" framerate: ");
                                                                                                                                                int i119222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i120222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i121222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i122222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i123222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i124222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i125222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                sb4222222222222222.append(i119222222222222222);
                                                                                                                                                sb4222222222222222.append(" size: ");
                                                                                                                                                i21 = i5;
                                                                                                                                                sb4222222222222222.append(i21);
                                                                                                                                                sb4222222222222222.append("x");
                                                                                                                                                i20 = i4;
                                                                                                                                                sb4222222222222222.append(i20);
                                                                                                                                                FileLog.e(sb4222222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i19 = i25;
                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                i26 = i9;
                                                                                                                                                z6 = z11;
                                                                                                                                                z13 = true;
                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                z11 = z6;
                                                                                                                                                z12 = z13;
                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                i9 = i26;
                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                z8 = z12;
                                                                                                                                                z6 = z11;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                i14 = i21;
                                                                                                                                                i13 = i20;
                                                                                                                                                i15 = i19;
                                                                                                                                                z7 = z8;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            } catch (Throwable th60) {
                                                                                                                                                th6 = th60;
                                                                                                                                                i12 = i4;
                                                                                                                                                i11 = i5;
                                                                                                                                                i51 = i6;
                                                                                                                                                j26 = j24;
                                                                                                                                                th = th6;
                                                                                                                                                i106 = i19;
                                                                                                                                                i9 = i26;
                                                                                                                                                i22 = i51;
                                                                                                                                                j8 = j26;
                                                                                                                                                z6 = false;
                                                                                                                                                i10 = i22;
                                                                                                                                                j6 = j8;
                                                                                                                                                StringBuilder sb222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                sb222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                sb222222222222222222222222222222.append(i106);
                                                                                                                                                sb222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                int i10722222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i10822222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i10922222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i11022222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i11122222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i11222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                sb222222222222222222222222222222.append(i10722222222222222222222222222222);
                                                                                                                                                sb222222222222222222222222222222.append(" size: ");
                                                                                                                                                sb222222222222222222222222222222.append(i11);
                                                                                                                                                sb222222222222222222222222222222.append("x");
                                                                                                                                                sb222222222222222222222222222222.append(i12);
                                                                                                                                                FileLog.e(sb222222222222222222222222222222.toString());
                                                                                                                                                FileLog.e(th);
                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                }
                                                                                                                                                i13 = i12;
                                                                                                                                                i14 = i11;
                                                                                                                                                i15 = i106;
                                                                                                                                                z7 = true;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    i55 = i46;
                                                                                                                                    j5 = j21;
                                                                                                                                    z19 = z15;
                                                                                                                                    j30 = 2500;
                                                                                                                                    j29 = j;
                                                                                                                                    i47 = i58;
                                                                                                                                    byteBuffer9 = inputSurface2;
                                                                                                                                    z20 = false;
                                                                                                                                    inputSurface2 = inputSurface2;
                                                                                                                                    j28 = j28;
                                                                                                                                } catch (Exception e58) {
                                                                                                                                    e4 = e58;
                                                                                                                                    j5 = j21;
                                                                                                                                    i53 = i6;
                                                                                                                                    j27 = j24;
                                                                                                                                    exc = e4;
                                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                                    i25 = i19;
                                                                                                                                    i9 = i26;
                                                                                                                                    i82 = i53;
                                                                                                                                    j50 = j27;
                                                                                                                                    mediaCodec2 = mediaCodec5;
                                                                                                                                    i23 = i82;
                                                                                                                                    j6 = j50;
                                                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                                                    }
                                                                                                                                    StringBuilder sb42222222222222222 = new StringBuilder();
                                                                                                                                    sb42222222222222222.append("bitrate: ");
                                                                                                                                    sb42222222222222222.append(i25);
                                                                                                                                    sb42222222222222222.append(" framerate: ");
                                                                                                                                    int i1192222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i1202222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i1212222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i1222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i1232222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i1242222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i1252222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    sb42222222222222222.append(i1192222222222222222);
                                                                                                                                    sb42222222222222222.append(" size: ");
                                                                                                                                    i21 = i5;
                                                                                                                                    sb42222222222222222.append(i21);
                                                                                                                                    sb42222222222222222.append("x");
                                                                                                                                    i20 = i4;
                                                                                                                                    sb42222222222222222.append(i20);
                                                                                                                                    FileLog.e(sb42222222222222222.toString());
                                                                                                                                    FileLog.e(exc);
                                                                                                                                    i19 = i25;
                                                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                                                    i26 = i9;
                                                                                                                                    z6 = z11;
                                                                                                                                    z13 = true;
                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                    }
                                                                                                                                    z11 = z6;
                                                                                                                                    z12 = z13;
                                                                                                                                    mediaCodec = mediaCodec2;
                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                    i9 = i26;
                                                                                                                                    if (outputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (inputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                    }
                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                    }
                                                                                                                                    checkConversionCanceled();
                                                                                                                                    z8 = z12;
                                                                                                                                    z6 = z11;
                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                    }
                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                    }
                                                                                                                                    i14 = i21;
                                                                                                                                    i13 = i20;
                                                                                                                                    i15 = i19;
                                                                                                                                    z7 = z8;
                                                                                                                                    if (z6) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            } catch (Exception e59) {
                                                                                                                                e4 = e59;
                                                                                                                                i24 = i129;
                                                                                                                            }
                                                                                                                        } catch (Throwable th61) {
                                                                                                                            th6 = th61;
                                                                                                                            j5 = j21;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                i55 = i46;
                                                                                                                i24 = i129;
                                                                                                                i54 = i44;
                                                                                                                j5 = j21;
                                                                                                                bufferInfo2 = bufferInfo;
                                                                                                                j30 = 2500;
                                                                                                                z19 = z15;
                                                                                                                j29 = j;
                                                                                                                i56 = -1;
                                                                                                                if (sampleTrackIndex == i56) {
                                                                                                                }
                                                                                                                z20 = false;
                                                                                                                inputSurface2 = inputSurface2;
                                                                                                                j28 = j28;
                                                                                                            }
                                                                                                            if (z20 && (j28 = mediaCodec3.dequeueInputBuffer(j30)) >= 0) {
                                                                                                                mediaCodec3.queueInputBuffer(j28, 0, 0, 0L, 4);
                                                                                                                z18 = true;
                                                                                                            }
                                                                                                        } catch (Exception e60) {
                                                                                                            e4 = e60;
                                                                                                            audioRecoder = audioRecoder2;
                                                                                                            j5 = j21;
                                                                                                            i24 = i40;
                                                                                                        } catch (Throwable th62) {
                                                                                                            th6 = th62;
                                                                                                            j5 = j21;
                                                                                                        }
                                                                                                    } else {
                                                                                                        i55 = i46;
                                                                                                        audioRecoder = audioRecoder2;
                                                                                                        i54 = i44;
                                                                                                        j5 = j21;
                                                                                                        i24 = i40;
                                                                                                        bufferInfo2 = bufferInfo;
                                                                                                        j30 = 2500;
                                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                                        z19 = z15;
                                                                                                        j29 = inputSurface2;
                                                                                                    }
                                                                                                    i50 = i52;
                                                                                                    z21 = !z17;
                                                                                                    i60 = i26;
                                                                                                    j33 = j25;
                                                                                                    boolean z52 = true;
                                                                                                    j34 = j24;
                                                                                                    while (true) {
                                                                                                        if (!z21 || z52) {
                                                                                                            try {
                                                                                                                try {
                                                                                                                    checkConversionCanceled();
                                                                                                                    if (z3) {
                                                                                                                        j30 = 22000;
                                                                                                                    }
                                                                                                                    z22 = z21;
                                                                                                                    createEncoderByType = mediaCodec5;
                                                                                                                } catch (Exception e61) {
                                                                                                                    e6 = e61;
                                                                                                                    i61 = i6;
                                                                                                                    createEncoderByType = mediaCodec5;
                                                                                                                }
                                                                                                                try {
                                                                                                                    dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j30);
                                                                                                                    i65 = -1;
                                                                                                                    if (dequeueOutputBuffer == -1) {
                                                                                                                        j46 = j34;
                                                                                                                        i68 = i47;
                                                                                                                        i69 = i50;
                                                                                                                        i78 = i39;
                                                                                                                        str8 = str4;
                                                                                                                        i70 = i41;
                                                                                                                        str9 = str6;
                                                                                                                        z52 = false;
                                                                                                                    } else if (dequeueOutputBuffer == -3) {
                                                                                                                        try {
                                                                                                                            i68 = i47;
                                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                                                byteBufferArr = createEncoderByType.getOutputBuffers();
                                                                                                                            }
                                                                                                                            j46 = j34;
                                                                                                                            i69 = i50;
                                                                                                                            i78 = i39;
                                                                                                                            str8 = str4;
                                                                                                                            i70 = i41;
                                                                                                                            str9 = str6;
                                                                                                                            i65 = -1;
                                                                                                                        } catch (Exception e62) {
                                                                                                                            i76 = i6;
                                                                                                                            exc = e62;
                                                                                                                            j45 = j34;
                                                                                                                            i9 = i60;
                                                                                                                            outputSurface2 = outputSurface4;
                                                                                                                            i38 = i76;
                                                                                                                            j18 = j45;
                                                                                                                            i25 = i19;
                                                                                                                            mediaCodec2 = createEncoderByType;
                                                                                                                            i23 = i38;
                                                                                                                            j6 = j18;
                                                                                                                            if (!(exc instanceof IllegalStateException)) {
                                                                                                                            }
                                                                                                                            StringBuilder sb422222222222222222 = new StringBuilder();
                                                                                                                            sb422222222222222222.append("bitrate: ");
                                                                                                                            sb422222222222222222.append(i25);
                                                                                                                            sb422222222222222222.append(" framerate: ");
                                                                                                                            int i11922222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            int i12022222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            int i12122222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            int i12222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            int i12322222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            int i12422222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            int i12522222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                            sb422222222222222222.append(i11922222222222222222);
                                                                                                                            sb422222222222222222.append(" size: ");
                                                                                                                            i21 = i5;
                                                                                                                            sb422222222222222222.append(i21);
                                                                                                                            sb422222222222222222.append("x");
                                                                                                                            i20 = i4;
                                                                                                                            sb422222222222222222.append(i20);
                                                                                                                            FileLog.e(sb422222222222222222.toString());
                                                                                                                            FileLog.e(exc);
                                                                                                                            i19 = i25;
                                                                                                                            mediaCodec4 = mediaCodec3;
                                                                                                                            i26 = i9;
                                                                                                                            z6 = z11;
                                                                                                                            z13 = true;
                                                                                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                            if (mediaCodec4 != null) {
                                                                                                                            }
                                                                                                                            z11 = z6;
                                                                                                                            z12 = z13;
                                                                                                                            mediaCodec = mediaCodec2;
                                                                                                                            outputSurface = outputSurface2;
                                                                                                                            i9 = i26;
                                                                                                                            if (outputSurface != null) {
                                                                                                                            }
                                                                                                                            if (inputSurface != null) {
                                                                                                                            }
                                                                                                                            if (mediaCodec != null) {
                                                                                                                            }
                                                                                                                            if (audioRecoder != null) {
                                                                                                                            }
                                                                                                                            checkConversionCanceled();
                                                                                                                            z8 = z12;
                                                                                                                            z6 = z11;
                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                            }
                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                            }
                                                                                                                            i14 = i21;
                                                                                                                            i13 = i20;
                                                                                                                            i15 = i19;
                                                                                                                            z7 = z8;
                                                                                                                            if (z6) {
                                                                                                                            }
                                                                                                                        } catch (Throwable th63) {
                                                                                                                            i12 = i4;
                                                                                                                            i77 = i6;
                                                                                                                            th = th63;
                                                                                                                            j13 = j34;
                                                                                                                            i9 = i60;
                                                                                                                            i106 = i19;
                                                                                                                            z6 = false;
                                                                                                                            i33 = i77;
                                                                                                                            i11 = i5;
                                                                                                                            i10 = i33;
                                                                                                                            j6 = j13;
                                                                                                                            StringBuilder sb2222222222222222222222222222222 = new StringBuilder();
                                                                                                                            sb2222222222222222222222222222222.append("bitrate: ");
                                                                                                                            sb2222222222222222222222222222222.append(i106);
                                                                                                                            sb2222222222222222222222222222222.append(" framerate: ");
                                                                                                                            int i107222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                            int i108222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                            int i109222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                            int i110222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                            int i111222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                            int i112222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                            sb2222222222222222222222222222222.append(i107222222222222222222222222222222);
                                                                                                                            sb2222222222222222222222222222222.append(" size: ");
                                                                                                                            sb2222222222222222222222222222222.append(i11);
                                                                                                                            sb2222222222222222222222222222222.append("x");
                                                                                                                            sb2222222222222222222222222222222.append(i12);
                                                                                                                            FileLog.e(sb2222222222222222222222222222222.toString());
                                                                                                                            FileLog.e(th);
                                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                            if (mediaExtractor != null) {
                                                                                                                            }
                                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                            if (mP4Builder != null) {
                                                                                                                            }
                                                                                                                            i13 = i12;
                                                                                                                            i14 = i11;
                                                                                                                            i15 = i106;
                                                                                                                            z7 = true;
                                                                                                                            if (z6) {
                                                                                                                            }
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        i68 = i47;
                                                                                                                        if (dequeueOutputBuffer == -2) {
                                                                                                                            MediaFormat outputFormat2 = createEncoderByType.getOutputFormat();
                                                                                                                            if (i60 != -5 || outputFormat2 == null) {
                                                                                                                                z30 = z52;
                                                                                                                                str10 = str5;
                                                                                                                                str9 = str6;
                                                                                                                            } else {
                                                                                                                                z30 = z52;
                                                                                                                                int addTrack2 = mediaCodecVideoConvertor2.mediaMuxer.addTrack(outputFormat2, false);
                                                                                                                                str10 = str5;
                                                                                                                                try {
                                                                                                                                    if (outputFormat2.containsKey(str10)) {
                                                                                                                                        i81 = addTrack2;
                                                                                                                                        if (outputFormat2.getInteger(str10) == 1) {
                                                                                                                                            str9 = str6;
                                                                                                                                            try {
                                                                                                                                                i50 = outputFormat2.getByteBuffer(str9).limit() + outputFormat2.getByteBuffer("csd-1").limit();
                                                                                                                                                i60 = i81;
                                                                                                                                            } catch (Exception e63) {
                                                                                                                                                e9 = e63;
                                                                                                                                                i76 = i6;
                                                                                                                                                i9 = i81;
                                                                                                                                                exc = e9;
                                                                                                                                                j45 = j34;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                i38 = i76;
                                                                                                                                                j18 = j45;
                                                                                                                                                i25 = i19;
                                                                                                                                                mediaCodec2 = createEncoderByType;
                                                                                                                                                i23 = i38;
                                                                                                                                                j6 = j18;
                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                }
                                                                                                                                                StringBuilder sb4222222222222222222 = new StringBuilder();
                                                                                                                                                sb4222222222222222222.append("bitrate: ");
                                                                                                                                                sb4222222222222222222.append(i25);
                                                                                                                                                sb4222222222222222222.append(" framerate: ");
                                                                                                                                                int i119222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i120222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i121222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i122222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i123222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i124222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                int i125222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                sb4222222222222222222.append(i119222222222222222222);
                                                                                                                                                sb4222222222222222222.append(" size: ");
                                                                                                                                                i21 = i5;
                                                                                                                                                sb4222222222222222222.append(i21);
                                                                                                                                                sb4222222222222222222.append("x");
                                                                                                                                                i20 = i4;
                                                                                                                                                sb4222222222222222222.append(i20);
                                                                                                                                                FileLog.e(sb4222222222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i19 = i25;
                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                i26 = i9;
                                                                                                                                                z6 = z11;
                                                                                                                                                z13 = true;
                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                z11 = z6;
                                                                                                                                                z12 = z13;
                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                i9 = i26;
                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                z8 = z12;
                                                                                                                                                z6 = z11;
                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                i14 = i21;
                                                                                                                                                i13 = i20;
                                                                                                                                                i15 = i19;
                                                                                                                                                z7 = z8;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            } catch (Throwable th64) {
                                                                                                                                                th9 = th64;
                                                                                                                                                i12 = i4;
                                                                                                                                                i77 = i6;
                                                                                                                                                i9 = i81;
                                                                                                                                                th = th9;
                                                                                                                                                j13 = j34;
                                                                                                                                                i106 = i19;
                                                                                                                                                z6 = false;
                                                                                                                                                i33 = i77;
                                                                                                                                                i11 = i5;
                                                                                                                                                i10 = i33;
                                                                                                                                                j6 = j13;
                                                                                                                                                StringBuilder sb22222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                sb22222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                sb22222222222222222222222222222222.append(i106);
                                                                                                                                                sb22222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                int i1072222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1082222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1092222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1102222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1112222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                int i1122222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                sb22222222222222222222222222222222.append(i1072222222222222222222222222222222);
                                                                                                                                                sb22222222222222222222222222222222.append(" size: ");
                                                                                                                                                sb22222222222222222222222222222222.append(i11);
                                                                                                                                                sb22222222222222222222222222222222.append("x");
                                                                                                                                                sb22222222222222222222222222222222.append(i12);
                                                                                                                                                FileLog.e(sb22222222222222222222222222222222.toString());
                                                                                                                                                FileLog.e(th);
                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                }
                                                                                                                                                i13 = i12;
                                                                                                                                                i14 = i11;
                                                                                                                                                i15 = i106;
                                                                                                                                                z7 = true;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        i81 = addTrack2;
                                                                                                                                    }
                                                                                                                                    str9 = str6;
                                                                                                                                    i60 = i81;
                                                                                                                                } catch (Exception e64) {
                                                                                                                                    e9 = e64;
                                                                                                                                    i81 = addTrack2;
                                                                                                                                } catch (Throwable th65) {
                                                                                                                                    th9 = th65;
                                                                                                                                    i81 = addTrack2;
                                                                                                                                }
                                                                                                                            }
                                                                                                                            j46 = j34;
                                                                                                                            str5 = str10;
                                                                                                                            i78 = i39;
                                                                                                                            i70 = i41;
                                                                                                                            i65 = -1;
                                                                                                                            z52 = z30;
                                                                                                                            i69 = i50;
                                                                                                                            str8 = str4;
                                                                                                                        } else {
                                                                                                                            boolean z53 = z52;
                                                                                                                            String str17 = str5;
                                                                                                                            str9 = str6;
                                                                                                                            try {
                                                                                                                                if (dequeueOutputBuffer < 0) {
                                                                                                                                    throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                                                                                                                                }
                                                                                                                                str5 = str17;
                                                                                                                                if (Build.VERSION.SDK_INT < 21) {
                                                                                                                                    byteBuffer3 = byteBufferArr[dequeueOutputBuffer];
                                                                                                                                } else {
                                                                                                                                    byteBuffer3 = createEncoderByType.getOutputBuffer(dequeueOutputBuffer);
                                                                                                                                }
                                                                                                                                if (byteBuffer3 == null) {
                                                                                                                                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                                                                                                                                }
                                                                                                                                int i133 = bufferInfo2.size;
                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                try {
                                                                                                                                    if (i133 > 1) {
                                                                                                                                        try {
                                                                                                                                            int i134 = bufferInfo2.flags;
                                                                                                                                            if ((i134 & 2) == 0) {
                                                                                                                                                if (i50 == 0 || (i134 & 1) == 0) {
                                                                                                                                                    j28 = j34;
                                                                                                                                                } else {
                                                                                                                                                    j47 = j34;
                                                                                                                                                    try {
                                                                                                                                                        try {
                                                                                                                                                            bufferInfo2.offset += i50;
                                                                                                                                                            bufferInfo2.size = i133 - i50;
                                                                                                                                                            j28 = j47;
                                                                                                                                                        } catch (Throwable th66) {
                                                                                                                                                            th7 = th66;
                                                                                                                                                            i12 = i4;
                                                                                                                                                            i11 = i5;
                                                                                                                                                            i63 = i6;
                                                                                                                                                            j37 = j47;
                                                                                                                                                            th = th7;
                                                                                                                                                            i9 = i60;
                                                                                                                                                            i62 = i63;
                                                                                                                                                            j36 = j37;
                                                                                                                                                            i106 = i19;
                                                                                                                                                            i22 = i62;
                                                                                                                                                            j8 = j36;
                                                                                                                                                            z6 = false;
                                                                                                                                                            i10 = i22;
                                                                                                                                                            j6 = j8;
                                                                                                                                                            StringBuilder sb222222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                            sb222222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                            sb222222222222222222222222222222222.append(i106);
                                                                                                                                                            sb222222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                            int i10722222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                            int i10822222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                            int i10922222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                            int i11022222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                            int i11122222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                            int i11222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                            sb222222222222222222222222222222222.append(i10722222222222222222222222222222222);
                                                                                                                                                            sb222222222222222222222222222222222.append(" size: ");
                                                                                                                                                            sb222222222222222222222222222222222.append(i11);
                                                                                                                                                            sb222222222222222222222222222222222.append("x");
                                                                                                                                                            sb222222222222222222222222222222222.append(i12);
                                                                                                                                                            FileLog.e(sb222222222222222222222222222222222.toString());
                                                                                                                                                            FileLog.e(th);
                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                            }
                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                            }
                                                                                                                                                            i13 = i12;
                                                                                                                                                            i14 = i11;
                                                                                                                                                            i15 = i106;
                                                                                                                                                            z7 = true;
                                                                                                                                                            if (z6) {
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    } catch (Exception e65) {
                                                                                                                                                        i23 = i6;
                                                                                                                                                        exc = e65;
                                                                                                                                                        mediaCodec2 = createEncoderByType;
                                                                                                                                                        i9 = i60;
                                                                                                                                                        i25 = i19;
                                                                                                                                                        j6 = j47;
                                                                                                                                                        if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                        }
                                                                                                                                                        StringBuilder sb42222222222222222222 = new StringBuilder();
                                                                                                                                                        sb42222222222222222222.append("bitrate: ");
                                                                                                                                                        sb42222222222222222222.append(i25);
                                                                                                                                                        sb42222222222222222222.append(" framerate: ");
                                                                                                                                                        int i1192222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        int i1202222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        int i1212222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        int i1222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        int i1232222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        int i1242222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        int i1252222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                        sb42222222222222222222.append(i1192222222222222222222);
                                                                                                                                                        sb42222222222222222222.append(" size: ");
                                                                                                                                                        i21 = i5;
                                                                                                                                                        sb42222222222222222222.append(i21);
                                                                                                                                                        sb42222222222222222222.append("x");
                                                                                                                                                        i20 = i4;
                                                                                                                                                        sb42222222222222222222.append(i20);
                                                                                                                                                        FileLog.e(sb42222222222222222222.toString());
                                                                                                                                                        FileLog.e(exc);
                                                                                                                                                        i19 = i25;
                                                                                                                                                        mediaCodec4 = mediaCodec3;
                                                                                                                                                        i26 = i9;
                                                                                                                                                        z6 = z11;
                                                                                                                                                        z13 = true;
                                                                                                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                                                        }
                                                                                                                                                        z11 = z6;
                                                                                                                                                        z12 = z13;
                                                                                                                                                        mediaCodec = mediaCodec2;
                                                                                                                                                        outputSurface = outputSurface2;
                                                                                                                                                        i9 = i26;
                                                                                                                                                        if (outputSurface != null) {
                                                                                                                                                        }
                                                                                                                                                        if (inputSurface != null) {
                                                                                                                                                        }
                                                                                                                                                        if (mediaCodec != null) {
                                                                                                                                                        }
                                                                                                                                                        if (audioRecoder != null) {
                                                                                                                                                        }
                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                        z8 = z12;
                                                                                                                                                        z6 = z11;
                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                        }
                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                        }
                                                                                                                                                        i14 = i21;
                                                                                                                                                        i13 = i20;
                                                                                                                                                        i15 = i19;
                                                                                                                                                        z7 = z8;
                                                                                                                                                        if (z6) {
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                                if (z50 && (i134 & 1) != 0) {
                                                                                                                                                    if (bufferInfo2.size > 100) {
                                                                                                                                                        byteBuffer3.position(bufferInfo2.offset);
                                                                                                                                                        byte[] bArr5 = new byte[100];
                                                                                                                                                        byteBuffer3.get(bArr5);
                                                                                                                                                        int i135 = 0;
                                                                                                                                                        int i136 = 0;
                                                                                                                                                        while (true) {
                                                                                                                                                            if (i135 >= 96) {
                                                                                                                                                                break;
                                                                                                                                                            }
                                                                                                                                                            if (bArr5[i135] == 0 && bArr5[i135 + 1] == 0 && bArr5[i135 + 2] == 0) {
                                                                                                                                                                bArr = bArr5;
                                                                                                                                                                if (bArr5[i135 + 3] == 1 && (i136 = i136 + 1) > 1) {
                                                                                                                                                                    bufferInfo2.offset += i135;
                                                                                                                                                                    bufferInfo2.size -= i135;
                                                                                                                                                                    break;
                                                                                                                                                                }
                                                                                                                                                            } else {
                                                                                                                                                                bArr = bArr5;
                                                                                                                                                            }
                                                                                                                                                            i135++;
                                                                                                                                                            bArr5 = bArr;
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    z50 = false;
                                                                                                                                                }
                                                                                                                                                try {
                                                                                                                                                    long writeSampleData3 = mediaCodecVideoConvertor2.mediaMuxer.writeSampleData(i60, byteBuffer3, bufferInfo2, true);
                                                                                                                                                    j48 = j28;
                                                                                                                                                    if (writeSampleData3 != 0) {
                                                                                                                                                        MediaController.VideoConvertorListener videoConvertorListener3 = mediaCodecVideoConvertor2.callback;
                                                                                                                                                        j48 = j28;
                                                                                                                                                        if (videoConvertorListener3 != null) {
                                                                                                                                                            i79 = i50;
                                                                                                                                                            mediaCodec8 = mediaCodec3;
                                                                                                                                                            try {
                                                                                                                                                                long j66 = bufferInfo2.presentationTimeUs;
                                                                                                                                                                if (j66 - j29 > j63) {
                                                                                                                                                                    j63 = j66 - j29;
                                                                                                                                                                }
                                                                                                                                                                long j67 = j63;
                                                                                                                                                                videoConvertorListener3.didWriteData(writeSampleData3, (((float) j67) / 1000.0f) / f);
                                                                                                                                                                j63 = j67;
                                                                                                                                                                mediaCodec9 = mediaCodec8;
                                                                                                                                                                j49 = j28;
                                                                                                                                                            } catch (Exception e66) {
                                                                                                                                                                e7 = e66;
                                                                                                                                                                i64 = i6;
                                                                                                                                                                exc = e7;
                                                                                                                                                                str7 = i64;
                                                                                                                                                                mediaCodec7 = mediaCodec8;
                                                                                                                                                                i67 = j28;
                                                                                                                                                                i9 = i60;
                                                                                                                                                                i66 = str7;
                                                                                                                                                                mediaCodec6 = mediaCodec7;
                                                                                                                                                                j38 = i67;
                                                                                                                                                                mediaCodec3 = mediaCodec6;
                                                                                                                                                                i38 = i66;
                                                                                                                                                                j18 = j38;
                                                                                                                                                                i25 = i19;
                                                                                                                                                                mediaCodec2 = createEncoderByType;
                                                                                                                                                                i23 = i38;
                                                                                                                                                                j6 = j18;
                                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                                }
                                                                                                                                                                StringBuilder sb422222222222222222222 = new StringBuilder();
                                                                                                                                                                sb422222222222222222222.append("bitrate: ");
                                                                                                                                                                sb422222222222222222222.append(i25);
                                                                                                                                                                sb422222222222222222222.append(" framerate: ");
                                                                                                                                                                int i11922222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                int i12022222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                int i12122222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                int i12222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                int i12322222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                int i12422222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                int i12522222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                sb422222222222222222222.append(i11922222222222222222222);
                                                                                                                                                                sb422222222222222222222.append(" size: ");
                                                                                                                                                                i21 = i5;
                                                                                                                                                                sb422222222222222222222.append(i21);
                                                                                                                                                                sb422222222222222222222.append("x");
                                                                                                                                                                i20 = i4;
                                                                                                                                                                sb422222222222222222222.append(i20);
                                                                                                                                                                FileLog.e(sb422222222222222222222.toString());
                                                                                                                                                                FileLog.e(exc);
                                                                                                                                                                i19 = i25;
                                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                                i26 = i9;
                                                                                                                                                                z6 = z11;
                                                                                                                                                                z13 = true;
                                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                                }
                                                                                                                                                                z11 = z6;
                                                                                                                                                                z12 = z13;
                                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                                i9 = i26;
                                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                                }
                                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                                }
                                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                                }
                                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                                }
                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                z8 = z12;
                                                                                                                                                                z6 = z11;
                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                }
                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                }
                                                                                                                                                                i14 = i21;
                                                                                                                                                                i13 = i20;
                                                                                                                                                                i15 = i19;
                                                                                                                                                                z7 = z8;
                                                                                                                                                                if (z6) {
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                } catch (Exception e67) {
                                                                                                                                                    e6 = e67;
                                                                                                                                                    j35 = j28;
                                                                                                                                                    i61 = i6;
                                                                                                                                                    exc = e6;
                                                                                                                                                    i9 = i60;
                                                                                                                                                    i38 = i61;
                                                                                                                                                    j18 = j35;
                                                                                                                                                    i25 = i19;
                                                                                                                                                    mediaCodec2 = createEncoderByType;
                                                                                                                                                    i23 = i38;
                                                                                                                                                    j6 = j18;
                                                                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                    }
                                                                                                                                                    StringBuilder sb4222222222222222222222 = new StringBuilder();
                                                                                                                                                    sb4222222222222222222222.append("bitrate: ");
                                                                                                                                                    sb4222222222222222222222.append(i25);
                                                                                                                                                    sb4222222222222222222222.append(" framerate: ");
                                                                                                                                                    int i119222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    int i120222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    int i121222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    int i122222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    int i123222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    int i124222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    int i125222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                    sb4222222222222222222222.append(i119222222222222222222222);
                                                                                                                                                    sb4222222222222222222222.append(" size: ");
                                                                                                                                                    i21 = i5;
                                                                                                                                                    sb4222222222222222222222.append(i21);
                                                                                                                                                    sb4222222222222222222222.append("x");
                                                                                                                                                    i20 = i4;
                                                                                                                                                    sb4222222222222222222222.append(i20);
                                                                                                                                                    FileLog.e(sb4222222222222222222222.toString());
                                                                                                                                                    FileLog.e(exc);
                                                                                                                                                    i19 = i25;
                                                                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                                                                    i26 = i9;
                                                                                                                                                    z6 = z11;
                                                                                                                                                    z13 = true;
                                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                                    }
                                                                                                                                                    z11 = z6;
                                                                                                                                                    z12 = z13;
                                                                                                                                                    mediaCodec = mediaCodec2;
                                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                                    i9 = i26;
                                                                                                                                                    if (outputSurface != null) {
                                                                                                                                                    }
                                                                                                                                                    if (inputSurface != null) {
                                                                                                                                                    }
                                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                                    }
                                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                                    }
                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                    z8 = z12;
                                                                                                                                                    z6 = z11;
                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                    }
                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                    }
                                                                                                                                                    i14 = i21;
                                                                                                                                                    i13 = i20;
                                                                                                                                                    i15 = i19;
                                                                                                                                                    z7 = z8;
                                                                                                                                                    if (z6) {
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                long j68 = j34;
                                                                                                                                                i79 = i50;
                                                                                                                                                MediaCodec mediaCodec11 = mediaCodec3;
                                                                                                                                                mediaCodec9 = mediaCodec11;
                                                                                                                                                j49 = j68;
                                                                                                                                                if (i60 == -5) {
                                                                                                                                                    byte[] bArr6 = new byte[i133];
                                                                                                                                                    byteBuffer3.limit(bufferInfo2.offset + i133);
                                                                                                                                                    byteBuffer3.position(bufferInfo2.offset);
                                                                                                                                                    byteBuffer3.get(bArr6);
                                                                                                                                                    byte b2 = 1;
                                                                                                                                                    int i137 = bufferInfo2.size - 1;
                                                                                                                                                    while (i137 >= 0 && i137 > 3) {
                                                                                                                                                        if (bArr6[i137] == b2 && bArr6[i137 - 1] == 0 && bArr6[i137 - 2] == 0) {
                                                                                                                                                            int i138 = i137 - 3;
                                                                                                                                                            if (bArr6[i138] == 0) {
                                                                                                                                                                byteBuffer5 = ByteBuffer.allocate(i138);
                                                                                                                                                                byteBuffer4 = ByteBuffer.allocate(bufferInfo2.size - i138);
                                                                                                                                                                byteBuffer5.put(bArr6, 0, i138).position(0);
                                                                                                                                                                byteBuffer4.put(bArr6, i138, bufferInfo2.size - i138).position(0);
                                                                                                                                                                break;
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                        i137--;
                                                                                                                                                        b2 = 1;
                                                                                                                                                    }
                                                                                                                                                    byteBuffer5 = null;
                                                                                                                                                    byteBuffer4 = null;
                                                                                                                                                    int i139 = i39;
                                                                                                                                                    str8 = str4;
                                                                                                                                                    i70 = i41;
                                                                                                                                                    MediaFormat createVideoFormat4 = MediaFormat.createVideoFormat(str8, i70, i139);
                                                                                                                                                    if (byteBuffer5 != null && byteBuffer4 != null) {
                                                                                                                                                        createVideoFormat4.setByteBuffer(str9, byteBuffer5);
                                                                                                                                                        createVideoFormat4.setByteBuffer("csd-1", byteBuffer4);
                                                                                                                                                    }
                                                                                                                                                    i60 = mediaCodecVideoConvertor2.mediaMuxer.addTrack(createVideoFormat4, false);
                                                                                                                                                    i80 = i139;
                                                                                                                                                    mediaCodec8 = mediaCodec11;
                                                                                                                                                    j28 = j68;
                                                                                                                                                    boolean z54 = (bufferInfo2.flags & 4) != 0;
                                                                                                                                                    createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                                                                                    z52 = z53;
                                                                                                                                                    i69 = i79;
                                                                                                                                                    OutputSurface outputSurface8 = z54 ? 1 : 0;
                                                                                                                                                    boolean z55 = z54 ? 1 : 0;
                                                                                                                                                    outputSurface5 = outputSurface8;
                                                                                                                                                    i65 = -1;
                                                                                                                                                    inputSurface2 = i80;
                                                                                                                                                    mediaCodec8 = mediaCodec8;
                                                                                                                                                    j28 = j28;
                                                                                                                                                    if (dequeueOutputBuffer == i65) {
                                                                                                                                                        i41 = i70;
                                                                                                                                                        i39 = inputSurface2;
                                                                                                                                                        str6 = str9;
                                                                                                                                                        str4 = str8;
                                                                                                                                                        j34 = j28;
                                                                                                                                                        j30 = 2500;
                                                                                                                                                        i50 = i69;
                                                                                                                                                        i47 = i68;
                                                                                                                                                        z21 = z22;
                                                                                                                                                        mediaCodec5 = createEncoderByType;
                                                                                                                                                        OutputSurface outputSurface9 = outputSurface2;
                                                                                                                                                        outputSurface2 = outputSurface5;
                                                                                                                                                        mediaCodec3 = mediaCodec8;
                                                                                                                                                        outputSurface4 = outputSurface9;
                                                                                                                                                    } else {
                                                                                                                                                        if (!z17) {
                                                                                                                                                            i41 = i70;
                                                                                                                                                            try {
                                                                                                                                                                int dequeueOutputBuffer3 = mediaCodec8.dequeueOutputBuffer(bufferInfo2, 2500L);
                                                                                                                                                                if (dequeueOutputBuffer3 != -1) {
                                                                                                                                                                    if (dequeueOutputBuffer3 != -3) {
                                                                                                                                                                        if (dequeueOutputBuffer3 != -2) {
                                                                                                                                                                            if (dequeueOutputBuffer3 < 0) {
                                                                                                                                                                                break loop4;
                                                                                                                                                                            }
                                                                                                                                                                            boolean z56 = bufferInfo2.size != 0;
                                                                                                                                                                            long j69 = bufferInfo2.presentationTimeUs;
                                                                                                                                                                            if (j5 <= 0 || j69 < j5) {
                                                                                                                                                                                i39 = inputSurface2;
                                                                                                                                                                                z25 = z56;
                                                                                                                                                                                j41 = 0;
                                                                                                                                                                            } else {
                                                                                                                                                                                i39 = inputSurface2;
                                                                                                                                                                                bufferInfo2.flags |= 4;
                                                                                                                                                                                z25 = false;
                                                                                                                                                                                z17 = true;
                                                                                                                                                                                j41 = 0;
                                                                                                                                                                                z18 = true;
                                                                                                                                                                            }
                                                                                                                                                                            if (j28 >= j41) {
                                                                                                                                                                                z29 = z25;
                                                                                                                                                                                try {
                                                                                                                                                                                    if ((bufferInfo2.flags & 4) != 0) {
                                                                                                                                                                                        z23 = z52;
                                                                                                                                                                                        i64 = i6;
                                                                                                                                                                                        try {
                                                                                                                                                                                            try {
                                                                                                                                                                                                str6 = str9;
                                                                                                                                                                                                str4 = str8;
                                                                                                                                                                                                if (Math.abs(j28 - j29) > 1000000 / i64) {
                                                                                                                                                                                                    if (j29 > 0) {
                                                                                                                                                                                                        try {
                                                                                                                                                                                                            mediaCodecVideoConvertor2.extractor.seekTo(j29, 0);
                                                                                                                                                                                                            outputSurface6 = outputSurface5;
                                                                                                                                                                                                            j43 = j33;
                                                                                                                                                                                                        } catch (Throwable th67) {
                                                                                                                                                                                                            th7 = th67;
                                                                                                                                                                                                            i12 = i4;
                                                                                                                                                                                                            i11 = i5;
                                                                                                                                                                                                            i63 = i64;
                                                                                                                                                                                                            j37 = j28;
                                                                                                                                                                                                            th = th7;
                                                                                                                                                                                                            i9 = i60;
                                                                                                                                                                                                            i62 = i63;
                                                                                                                                                                                                            j36 = j37;
                                                                                                                                                                                                            i106 = i19;
                                                                                                                                                                                                            i22 = i62;
                                                                                                                                                                                                            j8 = j36;
                                                                                                                                                                                                            z6 = false;
                                                                                                                                                                                                            i10 = i22;
                                                                                                                                                                                                            j6 = j8;
                                                                                                                                                                                                            StringBuilder sb2222222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append(i106);
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                            int i107222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                            int i108222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                            int i109222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                            int i110222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                            int i111222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                            int i112222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append(i107222222222222222222222222222222222);
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append(" size: ");
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append(i11);
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append("x");
                                                                                                                                                                                                            sb2222222222222222222222222222222222.append(i12);
                                                                                                                                                                                                            FileLog.e(sb2222222222222222222222222222222222.toString());
                                                                                                                                                                                                            FileLog.e(th);
                                                                                                                                                                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                            i13 = i12;
                                                                                                                                                                                                            i14 = i11;
                                                                                                                                                                                                            i15 = i106;
                                                                                                                                                                                                            z7 = true;
                                                                                                                                                                                                            if (z6) {
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } else {
                                                                                                                                                                                                        outputSurface6 = outputSurface5;
                                                                                                                                                                                                        mediaCodecVideoConvertor2.extractor.seekTo(0L, 0);
                                                                                                                                                                                                        j43 = j33;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    j64 = j43 + j16;
                                                                                                                                                                                                    try {
                                                                                                                                                                                                        bufferInfo2.flags &= -5;
                                                                                                                                                                                                        mediaCodec8.flush();
                                                                                                                                                                                                        j5 = j28;
                                                                                                                                                                                                        z27 = false;
                                                                                                                                                                                                        z17 = false;
                                                                                                                                                                                                        j42 = 0;
                                                                                                                                                                                                        z18 = false;
                                                                                                                                                                                                        z26 = true;
                                                                                                                                                                                                        j28 = -1;
                                                                                                                                                                                                        i74 = i64;
                                                                                                                                                                                                        if (j61 > j42) {
                                                                                                                                                                                                            i71 = i60;
                                                                                                                                                                                                            try {
                                                                                                                                                                                                                try {
                                                                                                                                                                                                                    if (bufferInfo2.presentationTimeUs - j61 < j11 && (bufferInfo2.flags & 4) == 0) {
                                                                                                                                                                                                                        z27 = false;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                } catch (Throwable th68) {
                                                                                                                                                                                                                    th8 = th68;
                                                                                                                                                                                                                    i12 = i4;
                                                                                                                                                                                                                    i11 = i5;
                                                                                                                                                                                                                    i9 = i71;
                                                                                                                                                                                                                    th = th8;
                                                                                                                                                                                                                    i62 = i74;
                                                                                                                                                                                                                    j36 = j28;
                                                                                                                                                                                                                    i106 = i19;
                                                                                                                                                                                                                    i22 = i62;
                                                                                                                                                                                                                    j8 = j36;
                                                                                                                                                                                                                    z6 = false;
                                                                                                                                                                                                                    i10 = i22;
                                                                                                                                                                                                                    j6 = j8;
                                                                                                                                                                                                                    StringBuilder sb22222222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append(i106);
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                    int i1072222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i1082222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i1092222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i1102222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i1112222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i1122222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append(i1072222222222222222222222222222222222);
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append(" size: ");
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append(i11);
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append("x");
                                                                                                                                                                                                                    sb22222222222222222222222222222222222.append(i12);
                                                                                                                                                                                                                    FileLog.e(sb22222222222222222222222222222222222.toString());
                                                                                                                                                                                                                    FileLog.e(th);
                                                                                                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    i13 = i12;
                                                                                                                                                                                                                    i14 = i11;
                                                                                                                                                                                                                    i15 = i106;
                                                                                                                                                                                                                    z7 = true;
                                                                                                                                                                                                                    if (z6) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                }
                                                                                                                                                                                                            } catch (Exception e68) {
                                                                                                                                                                                                                e8 = e68;
                                                                                                                                                                                                                i72 = i74;
                                                                                                                                                                                                                j39 = j28;
                                                                                                                                                                                                                i9 = i71;
                                                                                                                                                                                                                exc = e8;
                                                                                                                                                                                                                i66 = i72;
                                                                                                                                                                                                                mediaCodec6 = mediaCodec8;
                                                                                                                                                                                                                j38 = j39;
                                                                                                                                                                                                                mediaCodec3 = mediaCodec6;
                                                                                                                                                                                                                i38 = i66;
                                                                                                                                                                                                                j18 = j38;
                                                                                                                                                                                                                i25 = i19;
                                                                                                                                                                                                                mediaCodec2 = createEncoderByType;
                                                                                                                                                                                                                i23 = i38;
                                                                                                                                                                                                                j6 = j18;
                                                                                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                StringBuilder sb42222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                sb42222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                                sb42222222222222222222222.append(i25);
                                                                                                                                                                                                                sb42222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                int i1192222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i1202222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i1212222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i1222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i1232222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i1242222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i1252222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                sb42222222222222222222222.append(i1192222222222222222222222);
                                                                                                                                                                                                                sb42222222222222222222222.append(" size: ");
                                                                                                                                                                                                                i21 = i5;
                                                                                                                                                                                                                sb42222222222222222222222.append(i21);
                                                                                                                                                                                                                sb42222222222222222222222.append("x");
                                                                                                                                                                                                                i20 = i4;
                                                                                                                                                                                                                sb42222222222222222222222.append(i20);
                                                                                                                                                                                                                FileLog.e(sb42222222222222222222222.toString());
                                                                                                                                                                                                                FileLog.e(exc);
                                                                                                                                                                                                                i19 = i25;
                                                                                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                                                                                i26 = i9;
                                                                                                                                                                                                                z6 = z11;
                                                                                                                                                                                                                z13 = true;
                                                                                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                z11 = z6;
                                                                                                                                                                                                                z12 = z13;
                                                                                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                                                                                i9 = i26;
                                                                                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                                                                z8 = z12;
                                                                                                                                                                                                                z6 = z11;
                                                                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                i14 = i21;
                                                                                                                                                                                                                i13 = i20;
                                                                                                                                                                                                                i15 = i19;
                                                                                                                                                                                                                z7 = z8;
                                                                                                                                                                                                                if (z6) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            i71 = i60;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (j28 >= 0) {
                                                                                                                                                                                                            bufferInfo4 = bufferInfo2;
                                                                                                                                                                                                            j44 = j28;
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            bufferInfo4 = bufferInfo2;
                                                                                                                                                                                                            j44 = j;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (j44 > 0 || j62 != -1) {
                                                                                                                                                                                                            bufferInfo3 = bufferInfo4;
                                                                                                                                                                                                        } else if (j69 < j44) {
                                                                                                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                                                StringBuilder sb5 = new StringBuilder();
                                                                                                                                                                                                                sb5.append("drop frame startTime = ");
                                                                                                                                                                                                                sb5.append(j44);
                                                                                                                                                                                                                sb5.append(" present time = ");
                                                                                                                                                                                                                bufferInfo3 = bufferInfo4;
                                                                                                                                                                                                                sb5.append(bufferInfo3.presentationTimeUs);
                                                                                                                                                                                                                FileLog.d(sb5.toString());
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                bufferInfo3 = bufferInfo4;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            z27 = false;
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            bufferInfo3 = bufferInfo4;
                                                                                                                                                                                                            long j70 = bufferInfo3.presentationTimeUs;
                                                                                                                                                                                                            if (j43 != -2147483648L) {
                                                                                                                                                                                                                j64 -= j70;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            j62 = j70;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (z26) {
                                                                                                                                                                                                            j62 = -1;
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            if (j28 == -1 && j64 != 0) {
                                                                                                                                                                                                                bufferInfo3.presentationTimeUs += j64;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            try {
                                                                                                                                                                                                                mediaCodec8.releaseOutputBuffer(dequeueOutputBuffer3, z27);
                                                                                                                                                                                                            } catch (Exception e69) {
                                                                                                                                                                                                                e8 = e69;
                                                                                                                                                                                                                i73 = i74;
                                                                                                                                                                                                                j40 = j28;
                                                                                                                                                                                                                i72 = i73;
                                                                                                                                                                                                                j39 = j40;
                                                                                                                                                                                                                i9 = i71;
                                                                                                                                                                                                                exc = e8;
                                                                                                                                                                                                                i66 = i72;
                                                                                                                                                                                                                mediaCodec6 = mediaCodec8;
                                                                                                                                                                                                                j38 = j39;
                                                                                                                                                                                                                mediaCodec3 = mediaCodec6;
                                                                                                                                                                                                                i38 = i66;
                                                                                                                                                                                                                j18 = j38;
                                                                                                                                                                                                                i25 = i19;
                                                                                                                                                                                                                mediaCodec2 = createEncoderByType;
                                                                                                                                                                                                                i23 = i38;
                                                                                                                                                                                                                j6 = j18;
                                                                                                                                                                                                                if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                StringBuilder sb422222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                sb422222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                                sb422222222222222222222222.append(i25);
                                                                                                                                                                                                                sb422222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                int i11922222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i12022222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i12122222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i12222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i12322222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i12422222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                int i12522222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                sb422222222222222222222222.append(i11922222222222222222222222);
                                                                                                                                                                                                                sb422222222222222222222222.append(" size: ");
                                                                                                                                                                                                                i21 = i5;
                                                                                                                                                                                                                sb422222222222222222222222.append(i21);
                                                                                                                                                                                                                sb422222222222222222222222.append("x");
                                                                                                                                                                                                                i20 = i4;
                                                                                                                                                                                                                sb422222222222222222222222.append(i20);
                                                                                                                                                                                                                FileLog.e(sb422222222222222222222222.toString());
                                                                                                                                                                                                                FileLog.e(exc);
                                                                                                                                                                                                                i19 = i25;
                                                                                                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                                                                                                i26 = i9;
                                                                                                                                                                                                                z6 = z11;
                                                                                                                                                                                                                z13 = true;
                                                                                                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                z11 = z6;
                                                                                                                                                                                                                z12 = z13;
                                                                                                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                                                                                                i9 = i26;
                                                                                                                                                                                                                if (outputSurface != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (inputSurface != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (mediaCodec != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                checkConversionCanceled();
                                                                                                                                                                                                                z8 = z12;
                                                                                                                                                                                                                z6 = z11;
                                                                                                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                                i14 = i21;
                                                                                                                                                                                                                i13 = i20;
                                                                                                                                                                                                                i15 = i19;
                                                                                                                                                                                                                z7 = z8;
                                                                                                                                                                                                                if (z6) {
                                                                                                                                                                                                                }
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (z27) {
                                                                                                                                                                                                            long j71 = bufferInfo3.presentationTimeUs;
                                                                                                                                                                                                            j33 = j28 >= 0 ? Math.max(j43, j71) : j43;
                                                                                                                                                                                                            try {
                                                                                                                                                                                                                outputSurface2.awaitNewImage();
                                                                                                                                                                                                                z28 = false;
                                                                                                                                                                                                            } catch (Exception e70) {
                                                                                                                                                                                                                FileLog.e(e70);
                                                                                                                                                                                                                z28 = true;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            if (!z28) {
                                                                                                                                                                                                                outputSurface2.drawImage();
                                                                                                                                                                                                                inputSurface3 = inputSurface;
                                                                                                                                                                                                                try {
                                                                                                                                                                                                                    inputSurface3.setPresentationTime(bufferInfo3.presentationTimeUs * 1000);
                                                                                                                                                                                                                    inputSurface3.swapBuffers();
                                                                                                                                                                                                                } catch (Exception e71) {
                                                                                                                                                                                                                    i9 = i71;
                                                                                                                                                                                                                    exc = e71;
                                                                                                                                                                                                                    inputSurface = inputSurface3;
                                                                                                                                                                                                                    i66 = i74;
                                                                                                                                                                                                                    mediaCodec6 = mediaCodec8;
                                                                                                                                                                                                                    j38 = j28;
                                                                                                                                                                                                                    mediaCodec3 = mediaCodec6;
                                                                                                                                                                                                                    i38 = i66;
                                                                                                                                                                                                                    j18 = j38;
                                                                                                                                                                                                                    i25 = i19;
                                                                                                                                                                                                                    mediaCodec2 = createEncoderByType;
                                                                                                                                                                                                                    i23 = i38;
                                                                                                                                                                                                                    j6 = j18;
                                                                                                                                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    StringBuilder sb4222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                                    sb4222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                                    sb4222222222222222222222222.append(i25);
                                                                                                                                                                                                                    sb4222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                                    int i119222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i120222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i121222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i122222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i123222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i124222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    int i125222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                                    sb4222222222222222222222222.append(i119222222222222222222222222);
                                                                                                                                                                                                                    sb4222222222222222222222222.append(" size: ");
                                                                                                                                                                                                                    i21 = i5;
                                                                                                                                                                                                                    sb4222222222222222222222222.append(i21);
                                                                                                                                                                                                                    sb4222222222222222222222222.append("x");
                                                                                                                                                                                                                    i20 = i4;
                                                                                                                                                                                                                    sb4222222222222222222222222.append(i20);
                                                                                                                                                                                                                    FileLog.e(sb4222222222222222222222222.toString());
                                                                                                                                                                                                                    FileLog.e(exc);
                                                                                                                                                                                                                    i19 = i25;
                                                                                                                                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                                                                                                                                    i26 = i9;
                                                                                                                                                                                                                    z6 = z11;
                                                                                                                                                                                                                    z13 = true;
                                                                                                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    z11 = z6;
                                                                                                                                                                                                                    z12 = z13;
                                                                                                                                                                                                                    mediaCodec = mediaCodec2;
                                                                                                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                                                                                                    i9 = i26;
                                                                                                                                                                                                                    if (outputSurface != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    if (inputSurface != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                                                                    z8 = z12;
                                                                                                                                                                                                                    z6 = z11;
                                                                                                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                    i14 = i21;
                                                                                                                                                                                                                    i13 = i20;
                                                                                                                                                                                                                    i15 = i19;
                                                                                                                                                                                                                    z7 = z8;
                                                                                                                                                                                                                    if (z6) {
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                }
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                inputSurface3 = inputSurface;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            j61 = j71;
                                                                                                                                                                                                            inputSurface4 = inputSurface3;
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            inputSurface4 = inputSurface;
                                                                                                                                                                                                            j33 = j43;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if ((bufferInfo3.flags & 4) != 0) {
                                                                                                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                                                FileLog.d("decoder stream end");
                                                                                                                                                                                                            }
                                                                                                                                                                                                            createEncoderByType.signalEndOfInputStream();
                                                                                                                                                                                                            z24 = false;
                                                                                                                                                                                                            inputSurface2 = inputSurface4;
                                                                                                                                                                                                            j28 = j28;
                                                                                                                                                                                                        } else {
                                                                                                                                                                                                            z24 = z22;
                                                                                                                                                                                                            inputSurface2 = inputSurface4;
                                                                                                                                                                                                            j28 = j28;
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } catch (Exception e72) {
                                                                                                                                                                                                        exc = e72;
                                                                                                                                                                                                        i9 = i60;
                                                                                                                                                                                                        mediaCodec3 = mediaCodec8;
                                                                                                                                                                                                        i25 = i19;
                                                                                                                                                                                                        j5 = j28;
                                                                                                                                                                                                        mediaCodec2 = createEncoderByType;
                                                                                                                                                                                                        j6 = -1;
                                                                                                                                                                                                        i23 = i64;
                                                                                                                                                                                                        if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        StringBuilder sb42222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                        sb42222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                        sb42222222222222222222222222.append(i25);
                                                                                                                                                                                                        sb42222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                        int i1192222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i1202222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i1212222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i1222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i1232222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i1242222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i1252222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                                        sb42222222222222222222222222.append(i1192222222222222222222222222);
                                                                                                                                                                                                        sb42222222222222222222222222.append(" size: ");
                                                                                                                                                                                                        i21 = i5;
                                                                                                                                                                                                        sb42222222222222222222222222.append(i21);
                                                                                                                                                                                                        sb42222222222222222222222222.append("x");
                                                                                                                                                                                                        i20 = i4;
                                                                                                                                                                                                        sb42222222222222222222222222.append(i20);
                                                                                                                                                                                                        FileLog.e(sb42222222222222222222222222.toString());
                                                                                                                                                                                                        FileLog.e(exc);
                                                                                                                                                                                                        i19 = i25;
                                                                                                                                                                                                        mediaCodec4 = mediaCodec3;
                                                                                                                                                                                                        i26 = i9;
                                                                                                                                                                                                        z6 = z11;
                                                                                                                                                                                                        z13 = true;
                                                                                                                                                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        z11 = z6;
                                                                                                                                                                                                        z12 = z13;
                                                                                                                                                                                                        mediaCodec = mediaCodec2;
                                                                                                                                                                                                        outputSurface = outputSurface2;
                                                                                                                                                                                                        i9 = i26;
                                                                                                                                                                                                        if (outputSurface != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (inputSurface != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (mediaCodec != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        if (audioRecoder != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        checkConversionCanceled();
                                                                                                                                                                                                        z8 = z12;
                                                                                                                                                                                                        z6 = z11;
                                                                                                                                                                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        i14 = i21;
                                                                                                                                                                                                        i13 = i20;
                                                                                                                                                                                                        i15 = i19;
                                                                                                                                                                                                        z7 = z8;
                                                                                                                                                                                                        if (z6) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                    } catch (Throwable th69) {
                                                                                                                                                                                                        i12 = i4;
                                                                                                                                                                                                        i11 = i5;
                                                                                                                                                                                                        th = th69;
                                                                                                                                                                                                        i9 = i60;
                                                                                                                                                                                                        i106 = i19;
                                                                                                                                                                                                        j5 = j28;
                                                                                                                                                                                                        z6 = false;
                                                                                                                                                                                                        j6 = -1;
                                                                                                                                                                                                        i10 = i64;
                                                                                                                                                                                                        StringBuilder sb222222222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append(i106);
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                        int i10722222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i10822222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i10922222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i11022222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i11122222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                        int i11222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append(i10722222222222222222222222222222222222);
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append(" size: ");
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append(i11);
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append("x");
                                                                                                                                                                                                        sb222222222222222222222222222222222222.append(i12);
                                                                                                                                                                                                        FileLog.e(sb222222222222222222222222222222222222.toString());
                                                                                                                                                                                                        FileLog.e(th);
                                                                                                                                                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                        if (mediaExtractor != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                        if (mP4Builder != null) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                        i13 = i12;
                                                                                                                                                                                                        i14 = i11;
                                                                                                                                                                                                        i15 = i106;
                                                                                                                                                                                                        z7 = true;
                                                                                                                                                                                                        if (z6) {
                                                                                                                                                                                                        }
                                                                                                                                                                                                    }
                                                                                                                                                                                                } else {
                                                                                                                                                                                                    outputSurface6 = outputSurface5;
                                                                                                                                                                                                    j43 = j33;
                                                                                                                                                                                                    i75 = i64;
                                                                                                                                                                                                    z27 = z29;
                                                                                                                                                                                                    j42 = 0;
                                                                                                                                                                                                    z26 = false;
                                                                                                                                                                                                    i74 = i75;
                                                                                                                                                                                                    j28 = j28;
                                                                                                                                                                                                    if (j61 > j42) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (j28 >= 0) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (j44 > 0) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    bufferInfo3 = bufferInfo4;
                                                                                                                                                                                                    if (z26) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if (z27) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                    if ((bufferInfo3.flags & 4) != 0) {
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                            } catch (Throwable th70) {
                                                                                                                                                                                                th7 = th70;
                                                                                                                                                                                                i12 = i4;
                                                                                                                                                                                                i11 = i5;
                                                                                                                                                                                                i63 = i64;
                                                                                                                                                                                                j37 = j28;
                                                                                                                                                                                                th = th7;
                                                                                                                                                                                                i9 = i60;
                                                                                                                                                                                                i62 = i63;
                                                                                                                                                                                                j36 = j37;
                                                                                                                                                                                                i106 = i19;
                                                                                                                                                                                                i22 = i62;
                                                                                                                                                                                                j8 = j36;
                                                                                                                                                                                                z6 = false;
                                                                                                                                                                                                i10 = i22;
                                                                                                                                                                                                j6 = j8;
                                                                                                                                                                                                StringBuilder sb2222222222222222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append(i106);
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                                int i107222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                int i108222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                int i109222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                int i110222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                int i111222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                int i112222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append(i107222222222222222222222222222222222222);
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append(" size: ");
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append(i11);
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append("x");
                                                                                                                                                                                                sb2222222222222222222222222222222222222.append(i12);
                                                                                                                                                                                                FileLog.e(sb2222222222222222222222222222222222222.toString());
                                                                                                                                                                                                FileLog.e(th);
                                                                                                                                                                                                mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                                                                                }
                                                                                                                                                                                                mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                                if (mP4Builder != null) {
                                                                                                                                                                                                }
                                                                                                                                                                                                i13 = i12;
                                                                                                                                                                                                i14 = i11;
                                                                                                                                                                                                i15 = i106;
                                                                                                                                                                                                z7 = true;
                                                                                                                                                                                                if (z6) {
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                        } catch (Exception e73) {
                                                                                                                                                                                            e7 = e73;
                                                                                                                                                                                            exc = e7;
                                                                                                                                                                                            str7 = i64;
                                                                                                                                                                                            mediaCodec7 = mediaCodec8;
                                                                                                                                                                                            i67 = j28;
                                                                                                                                                                                            i9 = i60;
                                                                                                                                                                                            i66 = str7;
                                                                                                                                                                                            mediaCodec6 = mediaCodec7;
                                                                                                                                                                                            j38 = i67;
                                                                                                                                                                                            mediaCodec3 = mediaCodec6;
                                                                                                                                                                                            i38 = i66;
                                                                                                                                                                                            j18 = j38;
                                                                                                                                                                                            i25 = i19;
                                                                                                                                                                                            mediaCodec2 = createEncoderByType;
                                                                                                                                                                                            i23 = i38;
                                                                                                                                                                                            j6 = j18;
                                                                                                                                                                                            if (!(exc instanceof IllegalStateException)) {
                                                                                                                                                                                            }
                                                                                                                                                                                            StringBuilder sb422222222222222222222222222 = new StringBuilder();
                                                                                                                                                                                            sb422222222222222222222222222.append("bitrate: ");
                                                                                                                                                                                            sb422222222222222222222222222.append(i25);
                                                                                                                                                                                            sb422222222222222222222222222.append(" framerate: ");
                                                                                                                                                                                            int i11922222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            int i12022222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            int i12122222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            int i12222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            int i12322222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            int i12422222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            int i12522222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                                                                            sb422222222222222222222222222.append(i11922222222222222222222222222);
                                                                                                                                                                                            sb422222222222222222222222222.append(" size: ");
                                                                                                                                                                                            i21 = i5;
                                                                                                                                                                                            sb422222222222222222222222222.append(i21);
                                                                                                                                                                                            sb422222222222222222222222222.append("x");
                                                                                                                                                                                            i20 = i4;
                                                                                                                                                                                            sb422222222222222222222222222.append(i20);
                                                                                                                                                                                            FileLog.e(sb422222222222222222222222222.toString());
                                                                                                                                                                                            FileLog.e(exc);
                                                                                                                                                                                            i19 = i25;
                                                                                                                                                                                            mediaCodec4 = mediaCodec3;
                                                                                                                                                                                            i26 = i9;
                                                                                                                                                                                            z6 = z11;
                                                                                                                                                                                            z13 = true;
                                                                                                                                                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                                                                            if (mediaCodec4 != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            z11 = z6;
                                                                                                                                                                                            z12 = z13;
                                                                                                                                                                                            mediaCodec = mediaCodec2;
                                                                                                                                                                                            outputSurface = outputSurface2;
                                                                                                                                                                                            i9 = i26;
                                                                                                                                                                                            if (outputSurface != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            if (inputSurface != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            if (mediaCodec != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            if (audioRecoder != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                                                            z8 = z12;
                                                                                                                                                                                            z6 = z11;
                                                                                                                                                                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                                                            }
                                                                                                                                                                                            i14 = i21;
                                                                                                                                                                                            i13 = i20;
                                                                                                                                                                                            i15 = i19;
                                                                                                                                                                                            z7 = z8;
                                                                                                                                                                                            if (z6) {
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                } catch (Throwable th71) {
                                                                                                                                                                                    th7 = th71;
                                                                                                                                                                                    i64 = i6;
                                                                                                                                                                                }
                                                                                                                                                                            } else {
                                                                                                                                                                                z29 = z25;
                                                                                                                                                                            }
                                                                                                                                                                            str6 = str9;
                                                                                                                                                                            str4 = str8;
                                                                                                                                                                            outputSurface6 = outputSurface5;
                                                                                                                                                                            z23 = z52;
                                                                                                                                                                            j43 = j33;
                                                                                                                                                                            i75 = i6;
                                                                                                                                                                            z27 = z29;
                                                                                                                                                                            j42 = 0;
                                                                                                                                                                            z26 = false;
                                                                                                                                                                            i74 = i75;
                                                                                                                                                                            j28 = j28;
                                                                                                                                                                            if (j61 > j42) {
                                                                                                                                                                            }
                                                                                                                                                                            if (j28 >= 0) {
                                                                                                                                                                            }
                                                                                                                                                                            if (j44 > 0) {
                                                                                                                                                                            }
                                                                                                                                                                            bufferInfo3 = bufferInfo4;
                                                                                                                                                                            if (z26) {
                                                                                                                                                                            }
                                                                                                                                                                            if (z27) {
                                                                                                                                                                            }
                                                                                                                                                                            if ((bufferInfo3.flags & 4) != 0) {
                                                                                                                                                                            }
                                                                                                                                                                        } else {
                                                                                                                                                                            MediaFormat outputFormat3 = mediaCodec8.getOutputFormat();
                                                                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                                                FileLog.d("newFormat = " + outputFormat3);
                                                                                                                                                                            }
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                } else {
                                                                                                                                                                    i39 = inputSurface2;
                                                                                                                                                                    str6 = str9;
                                                                                                                                                                    str4 = str8;
                                                                                                                                                                    outputSurface6 = outputSurface5;
                                                                                                                                                                    bufferInfo3 = bufferInfo2;
                                                                                                                                                                    z23 = z52;
                                                                                                                                                                    i71 = i60;
                                                                                                                                                                    inputSurface2 = inputSurface;
                                                                                                                                                                    z24 = false;
                                                                                                                                                                    j28 = j28;
                                                                                                                                                                }
                                                                                                                                                                j29 = j;
                                                                                                                                                                i50 = i69;
                                                                                                                                                                i60 = i71;
                                                                                                                                                                i47 = i68;
                                                                                                                                                                z52 = z23;
                                                                                                                                                                bufferInfo2 = bufferInfo3;
                                                                                                                                                                inputSurface = inputSurface2;
                                                                                                                                                                mediaCodec5 = createEncoderByType;
                                                                                                                                                                mediaCodec3 = mediaCodec8;
                                                                                                                                                                outputSurface4 = outputSurface2;
                                                                                                                                                                outputSurface2 = outputSurface6;
                                                                                                                                                                j34 = j28;
                                                                                                                                                                z21 = z24;
                                                                                                                                                                j30 = 2500;
                                                                                                                                                            } catch (Exception e74) {
                                                                                                                                                                e8 = e74;
                                                                                                                                                                i73 = i6;
                                                                                                                                                                i71 = i60;
                                                                                                                                                                j40 = j28;
                                                                                                                                                            } catch (Throwable th72) {
                                                                                                                                                                th8 = th72;
                                                                                                                                                                i74 = i6;
                                                                                                                                                                i71 = i60;
                                                                                                                                                            }
                                                                                                                                                        } else {
                                                                                                                                                            i41 = i70;
                                                                                                                                                        }
                                                                                                                                                        i39 = inputSurface2;
                                                                                                                                                        str6 = str9;
                                                                                                                                                        str4 = str8;
                                                                                                                                                        outputSurface6 = outputSurface5;
                                                                                                                                                        bufferInfo3 = bufferInfo2;
                                                                                                                                                        z23 = z52;
                                                                                                                                                        i71 = i60;
                                                                                                                                                        inputSurface2 = inputSurface;
                                                                                                                                                        z24 = z22;
                                                                                                                                                        j33 = j33;
                                                                                                                                                        j28 = j28;
                                                                                                                                                        j29 = j;
                                                                                                                                                        i50 = i69;
                                                                                                                                                        i60 = i71;
                                                                                                                                                        i47 = i68;
                                                                                                                                                        z52 = z23;
                                                                                                                                                        bufferInfo2 = bufferInfo3;
                                                                                                                                                        inputSurface = inputSurface2;
                                                                                                                                                        mediaCodec5 = createEncoderByType;
                                                                                                                                                        mediaCodec3 = mediaCodec8;
                                                                                                                                                        outputSurface4 = outputSurface2;
                                                                                                                                                        outputSurface2 = outputSurface6;
                                                                                                                                                        j34 = j28;
                                                                                                                                                        z21 = z24;
                                                                                                                                                        j30 = 2500;
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            i80 = i39;
                                                                                                                                            str8 = str4;
                                                                                                                                            i70 = i41;
                                                                                                                                            mediaCodec8 = mediaCodec9;
                                                                                                                                            j28 = j49;
                                                                                                                                            if ((bufferInfo2.flags & 4) != 0) {
                                                                                                                                            }
                                                                                                                                            createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                                                                            z52 = z53;
                                                                                                                                            i69 = i79;
                                                                                                                                            OutputSurface outputSurface82 = z54 ? 1 : 0;
                                                                                                                                            boolean z552 = z54 ? 1 : 0;
                                                                                                                                            outputSurface5 = outputSurface82;
                                                                                                                                            i65 = -1;
                                                                                                                                            inputSurface2 = i80;
                                                                                                                                            mediaCodec8 = mediaCodec8;
                                                                                                                                            j28 = j28;
                                                                                                                                            if (dequeueOutputBuffer == i65) {
                                                                                                                                            }
                                                                                                                                        } catch (Exception e75) {
                                                                                                                                            e6 = e75;
                                                                                                                                            j35 = j34;
                                                                                                                                        } catch (Throwable th73) {
                                                                                                                                            th7 = th73;
                                                                                                                                            j47 = j34;
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        j48 = j34;
                                                                                                                                    }
                                                                                                                                    if ((bufferInfo2.flags & 4) != 0) {
                                                                                                                                    }
                                                                                                                                    createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                                                                    z52 = z53;
                                                                                                                                    i69 = i79;
                                                                                                                                    OutputSurface outputSurface822 = z54 ? 1 : 0;
                                                                                                                                    boolean z5522 = z54 ? 1 : 0;
                                                                                                                                    outputSurface5 = outputSurface822;
                                                                                                                                    i65 = -1;
                                                                                                                                    inputSurface2 = i80;
                                                                                                                                    mediaCodec8 = mediaCodec8;
                                                                                                                                    j28 = j28;
                                                                                                                                    if (dequeueOutputBuffer == i65) {
                                                                                                                                    }
                                                                                                                                } catch (Exception e76) {
                                                                                                                                    e7 = e76;
                                                                                                                                    i64 = i6;
                                                                                                                                    exc = e7;
                                                                                                                                    str7 = i64;
                                                                                                                                    mediaCodec7 = mediaCodec8;
                                                                                                                                    i67 = j28;
                                                                                                                                    i9 = i60;
                                                                                                                                    i66 = str7;
                                                                                                                                    mediaCodec6 = mediaCodec7;
                                                                                                                                    j38 = i67;
                                                                                                                                    mediaCodec3 = mediaCodec6;
                                                                                                                                    i38 = i66;
                                                                                                                                    j18 = j38;
                                                                                                                                    i25 = i19;
                                                                                                                                    mediaCodec2 = createEncoderByType;
                                                                                                                                    i23 = i38;
                                                                                                                                    j6 = j18;
                                                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                                                    }
                                                                                                                                    StringBuilder sb4222222222222222222222222222 = new StringBuilder();
                                                                                                                                    sb4222222222222222222222222222.append("bitrate: ");
                                                                                                                                    sb4222222222222222222222222222.append(i25);
                                                                                                                                    sb4222222222222222222222222222.append(" framerate: ");
                                                                                                                                    int i119222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i120222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i121222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i122222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i123222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i124222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    int i125222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                    sb4222222222222222222222222222.append(i119222222222222222222222222222);
                                                                                                                                    sb4222222222222222222222222222.append(" size: ");
                                                                                                                                    i21 = i5;
                                                                                                                                    sb4222222222222222222222222222.append(i21);
                                                                                                                                    sb4222222222222222222222222222.append("x");
                                                                                                                                    i20 = i4;
                                                                                                                                    sb4222222222222222222222222222.append(i20);
                                                                                                                                    FileLog.e(sb4222222222222222222222222222.toString());
                                                                                                                                    FileLog.e(exc);
                                                                                                                                    i19 = i25;
                                                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                                                    i26 = i9;
                                                                                                                                    z6 = z11;
                                                                                                                                    z13 = true;
                                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                    }
                                                                                                                                    z11 = z6;
                                                                                                                                    z12 = z13;
                                                                                                                                    mediaCodec = mediaCodec2;
                                                                                                                                    outputSurface = outputSurface2;
                                                                                                                                    i9 = i26;
                                                                                                                                    if (outputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (inputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                    }
                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                    }
                                                                                                                                    checkConversionCanceled();
                                                                                                                                    z8 = z12;
                                                                                                                                    z6 = z11;
                                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                    }
                                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                    }
                                                                                                                                    i14 = i21;
                                                                                                                                    i13 = i20;
                                                                                                                                    i15 = i19;
                                                                                                                                    z7 = z8;
                                                                                                                                    if (z6) {
                                                                                                                                    }
                                                                                                                                } catch (Throwable th74) {
                                                                                                                                    th7 = th74;
                                                                                                                                    i64 = i6;
                                                                                                                                    i12 = i4;
                                                                                                                                    i11 = i5;
                                                                                                                                    i63 = i64;
                                                                                                                                    j37 = j28;
                                                                                                                                    th = th7;
                                                                                                                                    i9 = i60;
                                                                                                                                    i62 = i63;
                                                                                                                                    j36 = j37;
                                                                                                                                    i106 = i19;
                                                                                                                                    i22 = i62;
                                                                                                                                    j8 = j36;
                                                                                                                                    z6 = false;
                                                                                                                                    i10 = i22;
                                                                                                                                    j6 = j8;
                                                                                                                                    StringBuilder sb22222222222222222222222222222222222222 = new StringBuilder();
                                                                                                                                    sb22222222222222222222222222222222222222.append("bitrate: ");
                                                                                                                                    sb22222222222222222222222222222222222222.append(i106);
                                                                                                                                    sb22222222222222222222222222222222222222.append(" framerate: ");
                                                                                                                                    int i1072222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                    int i1082222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                    int i1092222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                    int i1102222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                    int i1112222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                    int i1122222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                                                                                    sb22222222222222222222222222222222222222.append(i1072222222222222222222222222222222222222);
                                                                                                                                    sb22222222222222222222222222222222222222.append(" size: ");
                                                                                                                                    sb22222222222222222222222222222222222222.append(i11);
                                                                                                                                    sb22222222222222222222222222222222222222.append("x");
                                                                                                                                    sb22222222222222222222222222222222222222.append(i12);
                                                                                                                                    FileLog.e(sb22222222222222222222222222222222222222.toString());
                                                                                                                                    FileLog.e(th);
                                                                                                                                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                    }
                                                                                                                                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                    }
                                                                                                                                    i13 = i12;
                                                                                                                                    i14 = i11;
                                                                                                                                    i15 = i106;
                                                                                                                                    z7 = true;
                                                                                                                                    if (z6) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                i79 = i50;
                                                                                                                                mediaCodec9 = mediaCodec3;
                                                                                                                                j49 = j48;
                                                                                                                                i80 = i39;
                                                                                                                                str8 = str4;
                                                                                                                                i70 = i41;
                                                                                                                                mediaCodec8 = mediaCodec9;
                                                                                                                                j28 = j49;
                                                                                                                            } catch (Exception e77) {
                                                                                                                                exc = e77;
                                                                                                                                inputSurface = inputSurface2;
                                                                                                                                str7 = str17;
                                                                                                                                mediaCodec7 = outputSurface4;
                                                                                                                                i67 = j28;
                                                                                                                                i9 = i60;
                                                                                                                                i66 = str7;
                                                                                                                                mediaCodec6 = mediaCodec7;
                                                                                                                                j38 = i67;
                                                                                                                                mediaCodec3 = mediaCodec6;
                                                                                                                                i38 = i66;
                                                                                                                                j18 = j38;
                                                                                                                                i25 = i19;
                                                                                                                                mediaCodec2 = createEncoderByType;
                                                                                                                                i23 = i38;
                                                                                                                                j6 = j18;
                                                                                                                                z11 = !(exc instanceof IllegalStateException) && !z3;
                                                                                                                                StringBuilder sb42222222222222222222222222222 = new StringBuilder();
                                                                                                                                sb42222222222222222222222222222.append("bitrate: ");
                                                                                                                                sb42222222222222222222222222222.append(i25);
                                                                                                                                sb42222222222222222222222222222.append(" framerate: ");
                                                                                                                                int i1192222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                int i1202222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                int i1212222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                int i1222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                int i1232222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                int i1242222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                int i1252222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                                sb42222222222222222222222222222.append(i1192222222222222222222222222222);
                                                                                                                                sb42222222222222222222222222222.append(" size: ");
                                                                                                                                i21 = i5;
                                                                                                                                sb42222222222222222222222222222.append(i21);
                                                                                                                                sb42222222222222222222222222222.append("x");
                                                                                                                                i20 = i4;
                                                                                                                                sb42222222222222222222222222222.append(i20);
                                                                                                                                FileLog.e(sb42222222222222222222222222222.toString());
                                                                                                                                FileLog.e(exc);
                                                                                                                                i19 = i25;
                                                                                                                                mediaCodec4 = mediaCodec3;
                                                                                                                                i26 = i9;
                                                                                                                                z6 = z11;
                                                                                                                                z13 = true;
                                                                                                                                mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                }
                                                                                                                                z11 = z6;
                                                                                                                                z12 = z13;
                                                                                                                                mediaCodec = mediaCodec2;
                                                                                                                                outputSurface = outputSurface2;
                                                                                                                                i9 = i26;
                                                                                                                                if (outputSurface != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec != null) {
                                                                                                                                }
                                                                                                                                if (audioRecoder != null) {
                                                                                                                                }
                                                                                                                                checkConversionCanceled();
                                                                                                                                z8 = z12;
                                                                                                                                z6 = z11;
                                                                                                                                mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                }
                                                                                                                                mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                }
                                                                                                                                i14 = i21;
                                                                                                                                i13 = i20;
                                                                                                                                i15 = i19;
                                                                                                                                z7 = z8;
                                                                                                                                if (z6) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                    OutputSurface outputSurface10 = outputSurface4;
                                                                                                                    mediaCodec8 = mediaCodec3;
                                                                                                                    outputSurface5 = outputSurface2;
                                                                                                                    outputSurface2 = outputSurface10;
                                                                                                                    inputSurface2 = i78;
                                                                                                                    j28 = j46;
                                                                                                                    if (dequeueOutputBuffer == i65) {
                                                                                                                    }
                                                                                                                } catch (Exception e78) {
                                                                                                                    e6 = e78;
                                                                                                                    i61 = i6;
                                                                                                                    j35 = j34;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    exc = e6;
                                                                                                                    i9 = i60;
                                                                                                                    i38 = i61;
                                                                                                                    j18 = j35;
                                                                                                                    i25 = i19;
                                                                                                                    mediaCodec2 = createEncoderByType;
                                                                                                                    i23 = i38;
                                                                                                                    j6 = j18;
                                                                                                                    if (!(exc instanceof IllegalStateException)) {
                                                                                                                    }
                                                                                                                    StringBuilder sb422222222222222222222222222222 = new StringBuilder();
                                                                                                                    sb422222222222222222222222222222.append("bitrate: ");
                                                                                                                    sb422222222222222222222222222222.append(i25);
                                                                                                                    sb422222222222222222222222222222.append(" framerate: ");
                                                                                                                    int i11922222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i12022222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i12122222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i12222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i12322222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i12422222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    int i12522222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                                                                                    sb422222222222222222222222222222.append(i11922222222222222222222222222222);
                                                                                                                    sb422222222222222222222222222222.append(" size: ");
                                                                                                                    i21 = i5;
                                                                                                                    sb422222222222222222222222222222.append(i21);
                                                                                                                    sb422222222222222222222222222222.append("x");
                                                                                                                    i20 = i4;
                                                                                                                    sb422222222222222222222222222222.append(i20);
                                                                                                                    FileLog.e(sb422222222222222222222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i19 = i25;
                                                                                                                    mediaCodec4 = mediaCodec3;
                                                                                                                    i26 = i9;
                                                                                                                    z6 = z11;
                                                                                                                    z13 = true;
                                                                                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    z11 = z6;
                                                                                                                    z12 = z13;
                                                                                                                    mediaCodec = mediaCodec2;
                                                                                                                    outputSurface = outputSurface2;
                                                                                                                    i9 = i26;
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    z8 = z12;
                                                                                                                    z6 = z11;
                                                                                                                    mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    i14 = i21;
                                                                                                                    i13 = i20;
                                                                                                                    i15 = i19;
                                                                                                                    z7 = z8;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } catch (Throwable th75) {
                                                                                                                th7 = th75;
                                                                                                                i64 = i6;
                                                                                                                j28 = j34;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                } catch (Exception e79) {
                                                                                                    i38 = i6;
                                                                                                    createEncoderByType = mediaCodec5;
                                                                                                    audioRecoder = audioRecoder2;
                                                                                                    j5 = j21;
                                                                                                    outputSurface2 = outputSurface4;
                                                                                                    i24 = i40;
                                                                                                    j18 = j24;
                                                                                                    exc = e79;
                                                                                                    i25 = i19;
                                                                                                    i9 = i26;
                                                                                                } catch (Throwable th76) {
                                                                                                    th6 = th76;
                                                                                                    i51 = i6;
                                                                                                    j5 = j21;
                                                                                                    i12 = i4;
                                                                                                    i11 = i5;
                                                                                                }
                                                                                            }
                                                                                            i12 = i4;
                                                                                            j24 = j34;
                                                                                            i26 = i60;
                                                                                            inputSurface2 = j29;
                                                                                            i46 = i55;
                                                                                            byteBufferArr2 = byteBufferArr3;
                                                                                            z15 = z19;
                                                                                            i44 = i54;
                                                                                            i40 = i24;
                                                                                            j21 = j5;
                                                                                            j25 = j33;
                                                                                            bufferInfo = bufferInfo2;
                                                                                            audioRecoder2 = audioRecoder;
                                                                                        }
                                                                                        i21 = i5;
                                                                                        i23 = i6;
                                                                                        j6 = j24;
                                                                                        i20 = i12;
                                                                                        mediaCodec4 = mediaCodec3;
                                                                                        audioRecoder = audioRecoder2;
                                                                                        j5 = j21;
                                                                                        outputSurface2 = outputSurface4;
                                                                                        i24 = i40;
                                                                                        z6 = false;
                                                                                        z13 = false;
                                                                                        mediaCodec2 = mediaCodec5;
                                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                                        if (mediaCodec4 != null) {
                                                                                            mediaCodec4.stop();
                                                                                            mediaCodec4.release();
                                                                                        }
                                                                                        z11 = z6;
                                                                                        z12 = z13;
                                                                                        mediaCodec = mediaCodec2;
                                                                                        outputSurface = outputSurface2;
                                                                                        i9 = i26;
                                                                                    } else {
                                                                                        j21 = j2;
                                                                                        byteBufferArr = outputBuffers;
                                                                                        j22 = j;
                                                                                        i46 = -5;
                                                                                        i47 = 0;
                                                                                        audioRecoder2 = null;
                                                                                    }
                                                                                }
                                                                            }
                                                                            z15 = true;
                                                                            if (trackFormat2.getString("mime").equals("audio/unknown")) {
                                                                            }
                                                                            if (i44 < 0) {
                                                                            }
                                                                        } else {
                                                                            j21 = j2;
                                                                            byteBufferArr = outputBuffers;
                                                                            j22 = j;
                                                                            i46 = -5;
                                                                            i47 = 0;
                                                                            audioRecoder2 = null;
                                                                            z15 = true;
                                                                        }
                                                                        byteBuffer = null;
                                                                        j23 = j22;
                                                                        if (i44 < 0) {
                                                                        }
                                                                        checkConversionCanceled();
                                                                        j24 = j17;
                                                                        long j612 = -1;
                                                                        long j622 = -1;
                                                                        ByteBuffer byteBuffer92 = byteBuffer;
                                                                        j25 = -2147483648L;
                                                                        outputSurface2 = null;
                                                                        z17 = false;
                                                                        i26 = -5;
                                                                        z18 = false;
                                                                        boolean z502 = true;
                                                                        long j632 = 0;
                                                                        long j642 = 0;
                                                                        boolean z512 = z49;
                                                                        i50 = 0;
                                                                        byteBufferArr2 = inputBuffers;
                                                                        inputSurface2 = j23;
                                                                        loop4: while (true) {
                                                                            if (outputSurface2 != null) {
                                                                            }
                                                                            checkConversionCanceled();
                                                                            if (!z15) {
                                                                            }
                                                                            i52 = i50;
                                                                            if (!z18) {
                                                                            }
                                                                            i50 = i52;
                                                                            z21 = !z17;
                                                                            i60 = i26;
                                                                            j33 = j25;
                                                                            boolean z522 = true;
                                                                            j34 = j24;
                                                                            while (true) {
                                                                                if (!z21) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                if (z3) {
                                                                                }
                                                                                z22 = z21;
                                                                                createEncoderByType = mediaCodec5;
                                                                                dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j30);
                                                                                i65 = -1;
                                                                                if (dequeueOutputBuffer == -1) {
                                                                                }
                                                                                OutputSurface outputSurface102 = outputSurface4;
                                                                                mediaCodec8 = mediaCodec3;
                                                                                outputSurface5 = outputSurface2;
                                                                                outputSurface2 = outputSurface102;
                                                                                inputSurface2 = i78;
                                                                                j28 = j46;
                                                                                if (dequeueOutputBuffer == i65) {
                                                                                }
                                                                            }
                                                                            i12 = i4;
                                                                            j24 = j34;
                                                                            i26 = i60;
                                                                            inputSurface2 = j29;
                                                                            i46 = i55;
                                                                            byteBufferArr2 = byteBufferArr3;
                                                                            z15 = z19;
                                                                            i44 = i54;
                                                                            i40 = i24;
                                                                            j21 = j5;
                                                                            j25 = j33;
                                                                            bufferInfo = bufferInfo2;
                                                                            audioRecoder2 = audioRecoder;
                                                                        }
                                                                        i21 = i5;
                                                                        i23 = i6;
                                                                        j6 = j24;
                                                                        i20 = i12;
                                                                        mediaCodec4 = mediaCodec3;
                                                                        audioRecoder = audioRecoder2;
                                                                        j5 = j21;
                                                                        outputSurface2 = outputSurface4;
                                                                        i24 = i40;
                                                                        z6 = false;
                                                                        z13 = false;
                                                                        mediaCodec2 = mediaCodec5;
                                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                                        if (mediaCodec4 != null) {
                                                                        }
                                                                        z11 = z6;
                                                                        z12 = z13;
                                                                        mediaCodec = mediaCodec2;
                                                                        outputSurface = outputSurface2;
                                                                        i9 = i26;
                                                                    } catch (Throwable th77) {
                                                                        th10 = th77;
                                                                        i10 = i6;
                                                                        j5 = j2;
                                                                        j6 = j17;
                                                                        i11 = i42;
                                                                        i106 = i19;
                                                                        z6 = false;
                                                                        i9 = -5;
                                                                        th = th10;
                                                                        StringBuilder sb222222222222222222222222222222222222222 = new StringBuilder();
                                                                        sb222222222222222222222222222222222222222.append("bitrate: ");
                                                                        sb222222222222222222222222222222222222222.append(i106);
                                                                        sb222222222222222222222222222222222222222.append(" framerate: ");
                                                                        int i10722222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                        int i10822222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                        int i10922222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                        int i11022222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                        int i11122222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                        int i11222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                                        sb222222222222222222222222222222222222222.append(i10722222222222222222222222222222222222222);
                                                                        sb222222222222222222222222222222222222222.append(" size: ");
                                                                        sb222222222222222222222222222222222222222.append(i11);
                                                                        sb222222222222222222222222222222222222222.append("x");
                                                                        sb222222222222222222222222222222222222222.append(i12);
                                                                        FileLog.e(sb222222222222222222222222222222222222222.toString());
                                                                        FileLog.e(th);
                                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        i13 = i12;
                                                                        i14 = i11;
                                                                        i15 = i106;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    }
                                                                } else {
                                                                    i12 = i4;
                                                                }
                                                            } else {
                                                                i12 = i4;
                                                                i42 = i5;
                                                            }
                                                            z14 = true;
                                                            mediaCodec3 = createDecoderByType;
                                                            mediaCodec3.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                                            mediaCodec3.start();
                                                            if (i36 >= 21) {
                                                            }
                                                            i44 = i128;
                                                            if (i44 < 0) {
                                                            }
                                                            byteBuffer = null;
                                                            j23 = j22;
                                                            if (i44 < 0) {
                                                            }
                                                            checkConversionCanceled();
                                                            j24 = j17;
                                                            long j6122 = -1;
                                                            long j6222 = -1;
                                                            ByteBuffer byteBuffer922 = byteBuffer;
                                                            j25 = -2147483648L;
                                                            outputSurface2 = null;
                                                            z17 = false;
                                                            i26 = -5;
                                                            z18 = false;
                                                            boolean z5022 = true;
                                                            long j6322 = 0;
                                                            long j6422 = 0;
                                                            boolean z5122 = z49;
                                                            i50 = 0;
                                                            byteBufferArr2 = inputBuffers;
                                                            inputSurface2 = j23;
                                                            loop4: while (true) {
                                                                if (outputSurface2 != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                if (!z15) {
                                                                }
                                                                i52 = i50;
                                                                if (!z18) {
                                                                }
                                                                i50 = i52;
                                                                z21 = !z17;
                                                                i60 = i26;
                                                                j33 = j25;
                                                                boolean z5222 = true;
                                                                j34 = j24;
                                                                while (true) {
                                                                    if (!z21) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    if (z3) {
                                                                    }
                                                                    z22 = z21;
                                                                    createEncoderByType = mediaCodec5;
                                                                    dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j30);
                                                                    i65 = -1;
                                                                    if (dequeueOutputBuffer == -1) {
                                                                    }
                                                                    OutputSurface outputSurface1022 = outputSurface4;
                                                                    mediaCodec8 = mediaCodec3;
                                                                    outputSurface5 = outputSurface2;
                                                                    outputSurface2 = outputSurface1022;
                                                                    inputSurface2 = i78;
                                                                    j28 = j46;
                                                                    if (dequeueOutputBuffer == i65) {
                                                                    }
                                                                }
                                                                i12 = i4;
                                                                j24 = j34;
                                                                i26 = i60;
                                                                inputSurface2 = j29;
                                                                i46 = i55;
                                                                byteBufferArr2 = byteBufferArr3;
                                                                z15 = z19;
                                                                i44 = i54;
                                                                i40 = i24;
                                                                j21 = j5;
                                                                j25 = j33;
                                                                bufferInfo = bufferInfo2;
                                                                audioRecoder2 = audioRecoder;
                                                            }
                                                            i21 = i5;
                                                            i23 = i6;
                                                            j6 = j24;
                                                            i20 = i12;
                                                            mediaCodec4 = mediaCodec3;
                                                            audioRecoder = audioRecoder2;
                                                            j5 = j21;
                                                            outputSurface2 = outputSurface4;
                                                            i24 = i40;
                                                            z6 = false;
                                                            z13 = false;
                                                            mediaCodec2 = mediaCodec5;
                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            z11 = z6;
                                                            z12 = z13;
                                                            mediaCodec = mediaCodec2;
                                                            outputSurface = outputSurface2;
                                                            i9 = i26;
                                                        }
                                                    } else {
                                                        i37 = i35;
                                                    }
                                                    i19 = i25;
                                                    createEncoderByType = MediaCodec.createEncoderByType("video/avc");
                                                    createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                                    InputSurface inputSurface62 = new InputSurface(createEncoderByType.createInputSurface());
                                                    inputSurface62.makeCurrent();
                                                    createEncoderByType.start();
                                                    createDecoderByType = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                                                    int i1282 = i32;
                                                    i39 = i37;
                                                    inputSurface = inputSurface62;
                                                    mediaCodec5 = createEncoderByType;
                                                    i40 = i31;
                                                    bufferInfo = bufferInfo5;
                                                    str4 = "video/avc";
                                                    str5 = "prepend-sps-pps-to-idr-frames";
                                                    i41 = i34;
                                                    str6 = str3;
                                                    j17 = j14;
                                                    mediaCodecVideoConvertor2 = this;
                                                    outputSurface4 = outputSurface3;
                                                    outputSurface3 = new OutputSurface(savedFilterState, null, str2, arrayList, cropState, i4, i5, i2, i3, i, i6, false);
                                                    if (!z5) {
                                                    }
                                                    z14 = true;
                                                    mediaCodec3 = createDecoderByType;
                                                    mediaCodec3.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                                    mediaCodec3.start();
                                                    if (i36 >= 21) {
                                                    }
                                                    i44 = i1282;
                                                    if (i44 < 0) {
                                                    }
                                                    byteBuffer = null;
                                                    j23 = j22;
                                                    if (i44 < 0) {
                                                    }
                                                    checkConversionCanceled();
                                                    j24 = j17;
                                                    long j61222 = -1;
                                                    long j62222 = -1;
                                                    ByteBuffer byteBuffer9222 = byteBuffer;
                                                    j25 = -2147483648L;
                                                    outputSurface2 = null;
                                                    z17 = false;
                                                    i26 = -5;
                                                    z18 = false;
                                                    boolean z50222 = true;
                                                    long j63222 = 0;
                                                    long j64222 = 0;
                                                    boolean z51222 = z49;
                                                    i50 = 0;
                                                    byteBufferArr2 = inputBuffers;
                                                    inputSurface2 = j23;
                                                    loop4: while (true) {
                                                        if (outputSurface2 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        if (!z15) {
                                                        }
                                                        i52 = i50;
                                                        if (!z18) {
                                                        }
                                                        i50 = i52;
                                                        z21 = !z17;
                                                        i60 = i26;
                                                        j33 = j25;
                                                        boolean z52222 = true;
                                                        j34 = j24;
                                                        while (true) {
                                                            if (!z21) {
                                                            }
                                                            checkConversionCanceled();
                                                            if (z3) {
                                                            }
                                                            z22 = z21;
                                                            createEncoderByType = mediaCodec5;
                                                            dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j30);
                                                            i65 = -1;
                                                            if (dequeueOutputBuffer == -1) {
                                                            }
                                                            OutputSurface outputSurface10222 = outputSurface4;
                                                            mediaCodec8 = mediaCodec3;
                                                            outputSurface5 = outputSurface2;
                                                            outputSurface2 = outputSurface10222;
                                                            inputSurface2 = i78;
                                                            j28 = j46;
                                                            if (dequeueOutputBuffer == i65) {
                                                            }
                                                        }
                                                        i12 = i4;
                                                        j24 = j34;
                                                        i26 = i60;
                                                        inputSurface2 = j29;
                                                        i46 = i55;
                                                        byteBufferArr2 = byteBufferArr3;
                                                        z15 = z19;
                                                        i44 = i54;
                                                        i40 = i24;
                                                        j21 = j5;
                                                        j25 = j33;
                                                        bufferInfo = bufferInfo2;
                                                        audioRecoder2 = audioRecoder;
                                                    }
                                                    i21 = i5;
                                                    i23 = i6;
                                                    j6 = j24;
                                                    i20 = i12;
                                                    mediaCodec4 = mediaCodec3;
                                                    audioRecoder = audioRecoder2;
                                                    j5 = j21;
                                                    outputSurface2 = outputSurface4;
                                                    i24 = i40;
                                                    z6 = false;
                                                    z13 = false;
                                                    mediaCodec2 = mediaCodec5;
                                                    mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                    if (mediaCodec4 != null) {
                                                    }
                                                    z11 = z6;
                                                    z12 = z13;
                                                    mediaCodec = mediaCodec2;
                                                    outputSurface = outputSurface2;
                                                    i9 = i26;
                                                } else {
                                                    i31 = findTrack2;
                                                    try {
                                                        str3 = "csd-0";
                                                        try {
                                                            this.extractor.seekTo(0L, 0);
                                                        } catch (Exception e80) {
                                                            e = e80;
                                                            i27 = i6;
                                                            j3 = j14;
                                                            i24 = i31;
                                                            mediaCodecVideoConvertor2 = this;
                                                            j5 = j2;
                                                            j9 = j3;
                                                            exc = e;
                                                            mediaCodec3 = null;
                                                            i9 = -5;
                                                            mediaCodec2 = null;
                                                            outputSurface2 = null;
                                                            inputSurface = null;
                                                            audioRecoder = null;
                                                            i23 = i27;
                                                            j6 = j9;
                                                            if (!(exc instanceof IllegalStateException)) {
                                                            }
                                                            StringBuilder sb4222222222222222222222222222222 = new StringBuilder();
                                                            sb4222222222222222222222222222222.append("bitrate: ");
                                                            sb4222222222222222222222222222222.append(i25);
                                                            sb4222222222222222222222222222222.append(" framerate: ");
                                                            int i119222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            int i120222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            int i121222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            int i122222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            int i123222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            int i124222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            int i125222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                            sb4222222222222222222222222222222.append(i119222222222222222222222222222222);
                                                            sb4222222222222222222222222222222.append(" size: ");
                                                            i21 = i5;
                                                            sb4222222222222222222222222222222.append(i21);
                                                            sb4222222222222222222222222222222.append("x");
                                                            i20 = i4;
                                                            sb4222222222222222222222222222222.append(i20);
                                                            FileLog.e(sb4222222222222222222222222222222.toString());
                                                            FileLog.e(exc);
                                                            i19 = i25;
                                                            mediaCodec4 = mediaCodec3;
                                                            i26 = i9;
                                                            z6 = z11;
                                                            z13 = true;
                                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            z11 = z6;
                                                            z12 = z13;
                                                            mediaCodec = mediaCodec2;
                                                            outputSurface = outputSurface2;
                                                            i9 = i26;
                                                            if (outputSurface != null) {
                                                            }
                                                            if (inputSurface != null) {
                                                            }
                                                            if (mediaCodec != null) {
                                                            }
                                                            if (audioRecoder != null) {
                                                            }
                                                            checkConversionCanceled();
                                                            z8 = z12;
                                                            z6 = z11;
                                                            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                            if (mediaExtractor2 != null) {
                                                            }
                                                            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                            if (mP4Builder2 != null) {
                                                            }
                                                            i14 = i21;
                                                            i13 = i20;
                                                            i15 = i19;
                                                            z7 = z8;
                                                            if (z6) {
                                                            }
                                                        } catch (Throwable th78) {
                                                            th11 = th78;
                                                            i86 = i6;
                                                            j52 = j14;
                                                            mediaCodecVideoConvertor2 = this;
                                                            i12 = i4;
                                                            j5 = j2;
                                                            j13 = j52;
                                                            th = th11;
                                                            i106 = i25;
                                                            i29 = i86;
                                                            z6 = false;
                                                            i9 = -5;
                                                            i33 = i29;
                                                            i11 = i5;
                                                            i10 = i33;
                                                            j6 = j13;
                                                            StringBuilder sb2222222222222222222222222222222222222222 = new StringBuilder();
                                                            sb2222222222222222222222222222222222222222.append("bitrate: ");
                                                            sb2222222222222222222222222222222222222222.append(i106);
                                                            sb2222222222222222222222222222222222222222.append(" framerate: ");
                                                            int i107222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                            int i108222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                            int i109222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                            int i110222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                            int i111222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                            int i112222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                            sb2222222222222222222222222222222222222222.append(i107222222222222222222222222222222222222222);
                                                            sb2222222222222222222222222222222222222222.append(" size: ");
                                                            sb2222222222222222222222222222222222222222.append(i11);
                                                            sb2222222222222222222222222222222222222222.append("x");
                                                            sb2222222222222222222222222222222222222222.append(i12);
                                                            FileLog.e(sb2222222222222222222222222222222222222222.toString());
                                                            FileLog.e(th);
                                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            i13 = i12;
                                                            i14 = i11;
                                                            i15 = i106;
                                                            z7 = true;
                                                            if (z6) {
                                                            }
                                                        }
                                                    } catch (Exception e81) {
                                                        e = e81;
                                                        i27 = i6;
                                                        j3 = j14;
                                                        i24 = i31;
                                                        mediaCodecVideoConvertor2 = this;
                                                        j5 = j2;
                                                        j9 = j3;
                                                        exc = e;
                                                        mediaCodec3 = null;
                                                        i9 = -5;
                                                        mediaCodec2 = null;
                                                        outputSurface2 = null;
                                                        inputSurface = null;
                                                        audioRecoder = null;
                                                        i23 = i27;
                                                        j6 = j9;
                                                        if (!(exc instanceof IllegalStateException)) {
                                                        }
                                                        StringBuilder sb42222222222222222222222222222222 = new StringBuilder();
                                                        sb42222222222222222222222222222222.append("bitrate: ");
                                                        sb42222222222222222222222222222222.append(i25);
                                                        sb42222222222222222222222222222222.append(" framerate: ");
                                                        int i1192222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        int i1202222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        int i1212222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        int i1222222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        int i1232222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        int i1242222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        int i1252222222222222222222222222222222 = i23 == 1 ? 1 : 0;
                                                        sb42222222222222222222222222222222.append(i1192222222222222222222222222222222);
                                                        sb42222222222222222222222222222222.append(" size: ");
                                                        i21 = i5;
                                                        sb42222222222222222222222222222222.append(i21);
                                                        sb42222222222222222222222222222222.append("x");
                                                        i20 = i4;
                                                        sb42222222222222222222222222222222.append(i20);
                                                        FileLog.e(sb42222222222222222222222222222222.toString());
                                                        FileLog.e(exc);
                                                        i19 = i25;
                                                        mediaCodec4 = mediaCodec3;
                                                        i26 = i9;
                                                        z6 = z11;
                                                        z13 = true;
                                                        mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                                        if (mediaCodec4 != null) {
                                                        }
                                                        z11 = z6;
                                                        z12 = z13;
                                                        mediaCodec = mediaCodec2;
                                                        outputSurface = outputSurface2;
                                                        i9 = i26;
                                                        if (outputSurface != null) {
                                                        }
                                                        if (inputSurface != null) {
                                                        }
                                                        if (mediaCodec != null) {
                                                        }
                                                        if (audioRecoder != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        z8 = z12;
                                                        z6 = z11;
                                                        mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor2 != null) {
                                                        }
                                                        mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder2 != null) {
                                                        }
                                                        i14 = i21;
                                                        i13 = i20;
                                                        i15 = i19;
                                                        z7 = z8;
                                                        if (z6) {
                                                        }
                                                    } catch (Throwable th79) {
                                                        th11 = th79;
                                                        i86 = i6;
                                                        j52 = j14;
                                                        mediaCodecVideoConvertor2 = this;
                                                        i12 = i4;
                                                        j5 = j2;
                                                        j13 = j52;
                                                        th = th11;
                                                        i106 = i25;
                                                        i29 = i86;
                                                        z6 = false;
                                                        i9 = -5;
                                                        i33 = i29;
                                                        i11 = i5;
                                                        i10 = i33;
                                                        j6 = j13;
                                                        StringBuilder sb22222222222222222222222222222222222222222 = new StringBuilder();
                                                        sb22222222222222222222222222222222222222222.append("bitrate: ");
                                                        sb22222222222222222222222222222222222222222.append(i106);
                                                        sb22222222222222222222222222222222222222222.append(" framerate: ");
                                                        int i1072222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                        int i1082222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                        int i1092222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                        int i1102222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                        int i1112222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                        int i1122222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                                        sb22222222222222222222222222222222222222222.append(i1072222222222222222222222222222222222222222);
                                                        sb22222222222222222222222222222222222222222.append(" size: ");
                                                        sb22222222222222222222222222222222222222222.append(i11);
                                                        sb22222222222222222222222222222222222222222.append("x");
                                                        sb22222222222222222222222222222222222222222.append(i12);
                                                        FileLog.e(sb22222222222222222222222222222222222222222.toString());
                                                        FileLog.e(th);
                                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                                        if (mediaExtractor != null) {
                                                        }
                                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                                        if (mP4Builder != null) {
                                                        }
                                                        i13 = i12;
                                                        i14 = i11;
                                                        i15 = i106;
                                                        z7 = true;
                                                        if (z6) {
                                                        }
                                                    }
                                                }
                                            }
                                            outputSurface3 = new OutputSurface(savedFilterState, null, str2, arrayList, cropState, i4, i5, i2, i3, i, i6, false);
                                            if (!z5) {
                                            }
                                            z14 = true;
                                            mediaCodec3 = createDecoderByType;
                                            mediaCodec3.configure(trackFormat, outputSurface4.getSurface(), (MediaCrypto) null, 0);
                                            mediaCodec3.start();
                                            if (i36 >= 21) {
                                            }
                                            i44 = i1282;
                                            if (i44 < 0) {
                                            }
                                            byteBuffer = null;
                                            j23 = j22;
                                            if (i44 < 0) {
                                            }
                                            checkConversionCanceled();
                                            j24 = j17;
                                            long j612222 = -1;
                                            long j622222 = -1;
                                            ByteBuffer byteBuffer92222 = byteBuffer;
                                            j25 = -2147483648L;
                                            outputSurface2 = null;
                                            z17 = false;
                                            i26 = -5;
                                            z18 = false;
                                            boolean z502222 = true;
                                            long j632222 = 0;
                                            long j642222 = 0;
                                            boolean z512222 = z49;
                                            i50 = 0;
                                            byteBufferArr2 = inputBuffers;
                                            inputSurface2 = j23;
                                            loop4: while (true) {
                                                if (outputSurface2 != null) {
                                                }
                                                checkConversionCanceled();
                                                if (!z15) {
                                                }
                                                i52 = i50;
                                                if (!z18) {
                                                }
                                                i50 = i52;
                                                z21 = !z17;
                                                i60 = i26;
                                                j33 = j25;
                                                boolean z522222 = true;
                                                j34 = j24;
                                                while (true) {
                                                    if (!z21) {
                                                    }
                                                    checkConversionCanceled();
                                                    if (z3) {
                                                    }
                                                    z22 = z21;
                                                    createEncoderByType = mediaCodec5;
                                                    dequeueOutputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo2, j30);
                                                    i65 = -1;
                                                    if (dequeueOutputBuffer == -1) {
                                                    }
                                                    OutputSurface outputSurface102222 = outputSurface4;
                                                    mediaCodec8 = mediaCodec3;
                                                    outputSurface5 = outputSurface2;
                                                    outputSurface2 = outputSurface102222;
                                                    inputSurface2 = i78;
                                                    j28 = j46;
                                                    if (dequeueOutputBuffer == i65) {
                                                    }
                                                }
                                                i12 = i4;
                                                j24 = j34;
                                                i26 = i60;
                                                inputSurface2 = j29;
                                                i46 = i55;
                                                byteBufferArr2 = byteBufferArr3;
                                                z15 = z19;
                                                i44 = i54;
                                                i40 = i24;
                                                j21 = j5;
                                                j25 = j33;
                                                bufferInfo = bufferInfo2;
                                                audioRecoder2 = audioRecoder;
                                            }
                                            i21 = i5;
                                            i23 = i6;
                                            j6 = j24;
                                            i20 = i12;
                                            mediaCodec4 = mediaCodec3;
                                            audioRecoder = audioRecoder2;
                                            j5 = j21;
                                            outputSurface2 = outputSurface4;
                                            i24 = i40;
                                            z6 = false;
                                            z13 = false;
                                            mediaCodec2 = mediaCodec5;
                                            mediaCodecVideoConvertor2.extractor.unselectTrack(i24);
                                            if (mediaCodec4 != null) {
                                            }
                                            z11 = z6;
                                            z12 = z13;
                                            mediaCodec = mediaCodec2;
                                            outputSurface = outputSurface2;
                                            i9 = i26;
                                        } catch (Throwable th80) {
                                            th4 = th80;
                                            i16 = i6;
                                            i12 = i4;
                                            i11 = i5;
                                            j5 = j2;
                                            j7 = j17;
                                            th = th4;
                                            i106 = i19;
                                            z6 = false;
                                            i9 = -5;
                                            i10 = i16;
                                            j6 = j7;
                                            StringBuilder sb222222222222222222222222222222222222222222 = new StringBuilder();
                                            sb222222222222222222222222222222222222222222.append("bitrate: ");
                                            sb222222222222222222222222222222222222222222.append(i106);
                                            sb222222222222222222222222222222222222222222.append(" framerate: ");
                                            int i10722222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i10822222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i10922222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i11022222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i11122222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                            int i11222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                            sb222222222222222222222222222222222222222222.append(i10722222222222222222222222222222222222222222);
                                            sb222222222222222222222222222222222222222222.append(" size: ");
                                            sb222222222222222222222222222222222222222222.append(i11);
                                            sb222222222222222222222222222222222222222222.append("x");
                                            sb222222222222222222222222222222222222222222.append(i12);
                                            FileLog.e(sb222222222222222222222222222222222222222222.toString());
                                            FileLog.e(th);
                                            mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            i13 = i12;
                                            i14 = i11;
                                            i15 = i106;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                        createEncoderByType = MediaCodec.createEncoderByType("video/avc");
                                        createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                        InputSurface inputSurface622 = new InputSurface(createEncoderByType.createInputSurface());
                                        inputSurface622.makeCurrent();
                                        createEncoderByType.start();
                                        createDecoderByType = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                                        int i12822 = i32;
                                        i39 = i37;
                                        inputSurface = inputSurface622;
                                        mediaCodec5 = createEncoderByType;
                                        i40 = i31;
                                        bufferInfo = bufferInfo5;
                                        str4 = "video/avc";
                                        str5 = "prepend-sps-pps-to-idr-frames";
                                        i41 = i34;
                                        str6 = str3;
                                        j17 = j14;
                                        mediaCodecVideoConvertor2 = this;
                                        outputSurface4 = outputSurface3;
                                    } catch (Throwable th81) {
                                        th4 = th81;
                                        i16 = i6;
                                        j17 = j14;
                                        mediaCodecVideoConvertor2 = this;
                                    }
                                    cropState2 = cropState;
                                    if (cropState2 == null) {
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    createVideoFormat = MediaFormat.createVideoFormat("video/avc", i34, i35);
                                    createVideoFormat.setInteger("color-format", 2130708361);
                                    createVideoFormat.setInteger("bitrate", i25);
                                    createVideoFormat.setInteger("frame-rate", i6);
                                    createVideoFormat.setInteger("i-frame-interval", 2);
                                    i36 = Build.VERSION.SDK_INT;
                                    if (i36 >= 23) {
                                    }
                                    i19 = i25;
                                } else {
                                    i20 = i4;
                                    i23 = i6;
                                    i21 = i105;
                                    mediaCodecVideoConvertor2 = this;
                                    i19 = i7;
                                    j5 = j2;
                                    j6 = j3;
                                    z12 = false;
                                    i9 = -5;
                                    outputSurface = null;
                                    mediaCodec = null;
                                    z11 = false;
                                    inputSurface = null;
                                    audioRecoder = null;
                                }
                                if (outputSurface != null) {
                                    try {
                                        outputSurface.release();
                                    } catch (Throwable th82) {
                                        th = th82;
                                        i11 = i21;
                                        i12 = i20;
                                        i106 = i19;
                                        z6 = z11;
                                        i10 = i23;
                                        j6 = j6;
                                        StringBuilder sb2222222222222222222222222222222222222222222 = new StringBuilder();
                                        sb2222222222222222222222222222222222222222222.append("bitrate: ");
                                        sb2222222222222222222222222222222222222222222.append(i106);
                                        sb2222222222222222222222222222222222222222222.append(" framerate: ");
                                        int i107222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i108222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i109222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i110222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i111222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i112222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        sb2222222222222222222222222222222222222222222.append(i107222222222222222222222222222222222222222222);
                                        sb2222222222222222222222222222222222222222222.append(" size: ");
                                        sb2222222222222222222222222222222222222222222.append(i11);
                                        sb2222222222222222222222222222222222222222222.append("x");
                                        sb2222222222222222222222222222222222222222222.append(i12);
                                        FileLog.e(sb2222222222222222222222222222222222222222222.toString());
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                            mediaExtractor.release();
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                            try {
                                                mP4Builder.finishMovie();
                                                mediaCodecVideoConvertor2.endPresentationTime = mediaCodecVideoConvertor2.mediaMuxer.getLastFrameTimestamp(i9);
                                            } catch (Throwable th83) {
                                                FileLog.e(th83);
                                            }
                                        }
                                        i13 = i12;
                                        i14 = i11;
                                        i15 = i106;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                }
                                if (inputSurface != null) {
                                    inputSurface.release();
                                }
                                if (mediaCodec != null) {
                                    mediaCodec.stop();
                                    mediaCodec.release();
                                }
                                if (audioRecoder != null) {
                                    audioRecoder.release();
                                }
                                checkConversionCanceled();
                                z8 = z12;
                                z6 = z11;
                            } else {
                                try {
                                    i87 = i105;
                                    try {
                                        readAndWriteTracks(this.extractor, this.mediaMuxer, bufferInfo5, j, j2, j4, file, i106 != -1);
                                        i20 = i4;
                                        i19 = i7;
                                        j5 = j2;
                                        j6 = j3;
                                        i21 = i87;
                                        z6 = false;
                                        z8 = false;
                                        mediaCodecVideoConvertor2 = this;
                                        i9 = -5;
                                    } catch (Throwable th84) {
                                        th12 = th84;
                                        i12 = i4;
                                        i16 = i6;
                                        i106 = i7;
                                        j5 = j2;
                                        j7 = j3;
                                        th = th12;
                                        i11 = i87;
                                        z6 = false;
                                        mediaCodecVideoConvertor2 = this;
                                        i9 = -5;
                                        i10 = i16;
                                        j6 = j7;
                                        StringBuilder sb22222222222222222222222222222222222222222222 = new StringBuilder();
                                        sb22222222222222222222222222222222222222222222.append("bitrate: ");
                                        sb22222222222222222222222222222222222222222222.append(i106);
                                        sb22222222222222222222222222222222222222222222.append(" framerate: ");
                                        int i1072222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i1082222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i1092222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i1102222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i1112222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        int i1122222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                                        sb22222222222222222222222222222222222222222222.append(i1072222222222222222222222222222222222222222222);
                                        sb22222222222222222222222222222222222222222222.append(" size: ");
                                        sb22222222222222222222222222222222222222222222.append(i11);
                                        sb22222222222222222222222222222222222222222222.append("x");
                                        sb22222222222222222222222222222222222222222222.append(i12);
                                        FileLog.e(sb22222222222222222222222222222222222222222222.toString());
                                        FileLog.e(th);
                                        mediaExtractor = mediaCodecVideoConvertor2.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        i13 = i12;
                                        i14 = i11;
                                        i15 = i106;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                } catch (Throwable th85) {
                                    th12 = th85;
                                    i87 = i105;
                                }
                            }
                        }
                    }
                    z9 = z2;
                    z10 = false;
                    if (!z9) {
                    }
                    if (findTrack2 < 0) {
                    }
                    if (outputSurface != null) {
                    }
                    if (inputSurface != null) {
                    }
                    if (mediaCodec != null) {
                    }
                    if (audioRecoder != null) {
                    }
                    checkConversionCanceled();
                    z8 = z12;
                    z6 = z11;
                } catch (Throwable th86) {
                    th2 = th86;
                    i17 = i4;
                    i18 = i105;
                    i16 = i6;
                    mediaCodecVideoConvertor2 = this;
                    i106 = i7;
                    j5 = j2;
                    j7 = j3;
                    th = th2;
                    i11 = i18;
                    i12 = i17;
                    z6 = false;
                    i9 = -5;
                    i10 = i16;
                    j6 = j7;
                    StringBuilder sb222222222222222222222222222222222222222222222 = new StringBuilder();
                    sb222222222222222222222222222222222222222222222.append("bitrate: ");
                    sb222222222222222222222222222222222222222222222.append(i106);
                    sb222222222222222222222222222222222222222222222.append(" framerate: ");
                    int i10722222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                    int i10822222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                    int i10922222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                    int i11022222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                    int i11122222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                    int i11222222222222222222222222222222222222222222222 = i10 == 1 ? 1 : 0;
                    sb222222222222222222222222222222222222222222222.append(i10722222222222222222222222222222222222222222222);
                    sb222222222222222222222222222222222222222222222.append(" size: ");
                    sb222222222222222222222222222222222222222222222.append(i11);
                    sb222222222222222222222222222222222222222222222.append("x");
                    sb222222222222222222222222222222222222222222222.append(i12);
                    FileLog.e(sb222222222222222222222222222222222222222222222.toString());
                    FileLog.e(th);
                    mediaExtractor = mediaCodecVideoConvertor2.extractor;
                    if (mediaExtractor != null) {
                    }
                    mP4Builder = mediaCodecVideoConvertor2.mediaMuxer;
                    if (mP4Builder != null) {
                    }
                    i13 = i12;
                    i14 = i11;
                    i15 = i106;
                    z7 = true;
                    if (z6) {
                    }
                }
            }
            mediaExtractor2 = mediaCodecVideoConvertor2.extractor;
            if (mediaExtractor2 != null) {
                mediaExtractor2.release();
            }
            mP4Builder2 = mediaCodecVideoConvertor2.mediaMuxer;
            if (mP4Builder2 != null) {
                try {
                    mP4Builder2.finishMovie();
                    mediaCodecVideoConvertor2.endPresentationTime = mediaCodecVideoConvertor2.mediaMuxer.getLastFrameTimestamp(i9);
                } catch (Throwable th87) {
                    FileLog.e(th87);
                }
            }
            i14 = i21;
            i13 = i20;
            i15 = i19;
            z7 = z8;
        } catch (Throwable th88) {
            th2 = th88;
            i17 = i4;
            i18 = i105;
            i16 = i6;
        }
        if (z6) {
            return convertVideoInternal(str, file, i, z, i2, i3, i13, i14, i6, i15, i8, j, j5, j6, j4, z2, true, savedFilterState, str2, arrayList, z4, cropState, z5);
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("compression completed time=" + currentTimeMillis2 + " needCompress=" + z2 + " w=" + i13 + " h=" + i14 + " bitrate=" + i15);
        }
        return z7;
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x0123, code lost:
        if (r9[r6 + 3] != 1) goto L72;
     */
    /* JADX WARN: Removed duplicated region for block: B:113:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private long readAndWriteTracks(MediaExtractor mediaExtractor, MP4Builder mP4Builder, MediaCodec.BufferInfo bufferInfo, long j, long j2, long j3, File file, boolean z) throws Exception {
        long j4;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        boolean z2;
        int i9;
        int i10;
        int i11;
        boolean z3;
        long j5;
        byte[] array;
        int i12;
        int i13;
        int i14;
        int findTrack = MediaController.findTrack(mediaExtractor, false);
        if (z) {
            j4 = j3;
            i = MediaController.findTrack(mediaExtractor, true);
        } else {
            j4 = j3;
            i = -1;
        }
        float f = ((float) j4) / 1000.0f;
        if (findTrack >= 0) {
            mediaExtractor.selectTrack(findTrack);
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(findTrack);
            i2 = mP4Builder.addTrack(trackFormat, false);
            try {
                i14 = trackFormat.getInteger("max-input-size");
            } catch (Exception e) {
                FileLog.e(e);
                i14 = 0;
            }
            if (j > 0) {
                mediaExtractor.seekTo(j, 0);
            } else {
                mediaExtractor.seekTo(0L, 0);
            }
            i3 = i14;
        } else {
            i3 = 0;
            i2 = -1;
        }
        if (i >= 0) {
            mediaExtractor.selectTrack(i);
            MediaFormat trackFormat2 = mediaExtractor.getTrackFormat(i);
            if (trackFormat2.getString("mime").equals("audio/unknown")) {
                i4 = -1;
                i = -1;
            } else {
                i4 = mP4Builder.addTrack(trackFormat2, true);
                try {
                    i3 = Math.max(trackFormat2.getInteger("max-input-size"), i3);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (j > 0) {
                    mediaExtractor.seekTo(j, 0);
                } else {
                    mediaExtractor.seekTo(0L, 0);
                }
            }
        } else {
            i4 = -1;
        }
        if (i3 <= 0) {
            i3 = 65536;
        }
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i3);
        if (i >= 0 || findTrack >= 0) {
            checkConversionCanceled();
            long j6 = 0;
            long j7 = -1;
            boolean z4 = false;
            while (!z4) {
                checkConversionCanceled();
                int i15 = Build.VERSION.SDK_INT;
                if (i15 >= 28) {
                    long sampleSize = mediaExtractor.getSampleSize();
                    i5 = i;
                    if (sampleSize > i3) {
                        int i16 = (int) (sampleSize + 1024);
                        i3 = i16;
                        allocateDirect = ByteBuffer.allocateDirect(i16);
                    }
                } else {
                    i5 = i;
                }
                bufferInfo.size = mediaExtractor.readSampleData(allocateDirect, 0);
                int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
                if (sampleTrackIndex == findTrack) {
                    i7 = i5;
                    i6 = i2;
                } else {
                    i7 = i5;
                    if (sampleTrackIndex == i7) {
                        i6 = i4;
                    } else {
                        i8 = -1;
                        i6 = -1;
                        if (i6 == i8) {
                            if (i15 < 21) {
                                allocateDirect.position(0);
                                allocateDirect.limit(bufferInfo.size);
                            }
                            if (sampleTrackIndex != i7 && (array = allocateDirect.array()) != null) {
                                int arrayOffset = allocateDirect.arrayOffset();
                                int limit = arrayOffset + allocateDirect.limit();
                                i9 = i4;
                                int i17 = arrayOffset;
                                int i18 = -1;
                                while (true) {
                                    int i19 = 4;
                                    z2 = z4;
                                    int i20 = limit - 4;
                                    if (i17 > i20) {
                                        break;
                                    }
                                    if (array[i17] == 0 && array[i17 + 1] == 0 && array[i17 + 2] == 0) {
                                        i12 = i3;
                                        i13 = i7;
                                    } else {
                                        i12 = i3;
                                        i13 = i7;
                                    }
                                    if (i17 != i20) {
                                        i17++;
                                        z4 = z2;
                                        i7 = i13;
                                        i3 = i12;
                                    }
                                    if (i18 != -1) {
                                        int i21 = i17 - i18;
                                        if (i17 == i20) {
                                            i19 = 0;
                                        }
                                        int i22 = i21 - i19;
                                        array[i18] = (byte) (i22 >> 24);
                                        array[i18 + 1] = (byte) (i22 >> 16);
                                        array[i18 + 2] = (byte) (i22 >> 8);
                                        array[i18 + 3] = (byte) i22;
                                    }
                                    i18 = i17;
                                    i17++;
                                    z4 = z2;
                                    i7 = i13;
                                    i3 = i12;
                                }
                            } else {
                                i9 = i4;
                                z2 = z4;
                            }
                            i10 = i3;
                            i11 = i7;
                            if (bufferInfo.size >= 0) {
                                bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                                z3 = false;
                            } else {
                                bufferInfo.size = 0;
                                z3 = true;
                            }
                            if (bufferInfo.size > 0 && !z3) {
                                if (sampleTrackIndex == findTrack) {
                                    j5 = 0;
                                    if (j > 0 && j7 == -1) {
                                        j7 = bufferInfo.presentationTimeUs;
                                    }
                                } else {
                                    j5 = 0;
                                }
                                if (j2 < j5 || bufferInfo.presentationTimeUs < j2) {
                                    bufferInfo.offset = 0;
                                    bufferInfo.flags = mediaExtractor.getSampleFlags();
                                    long writeSampleData = mP4Builder.writeSampleData(i6, allocateDirect, bufferInfo, false);
                                    if (writeSampleData != 0) {
                                        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
                                        if (videoConvertorListener != null) {
                                            long j8 = bufferInfo.presentationTimeUs;
                                            long j9 = j8 - j7 > j6 ? j8 - j7 : j6;
                                            videoConvertorListener.didWriteData(writeSampleData, (((float) j9) / 1000.0f) / f);
                                            j6 = j9;
                                            if (!z3) {
                                                mediaExtractor.advance();
                                            }
                                        }
                                        if (!z3) {
                                        }
                                    }
                                } else {
                                    z3 = true;
                                }
                            }
                            if (!z3) {
                            }
                        } else {
                            i9 = i4;
                            z2 = z4;
                            i10 = i3;
                            i11 = i7;
                            if (sampleTrackIndex == -1) {
                                z3 = true;
                            } else {
                                mediaExtractor.advance();
                                z3 = false;
                            }
                        }
                        z4 = !z3 ? true : z2;
                        i4 = i9;
                        i = i11;
                        i3 = i10;
                    }
                }
                i8 = -1;
                if (i6 == i8) {
                }
                if (!z3) {
                }
                i4 = i9;
                i = i11;
                i3 = i10;
            }
            int i23 = i;
            if (findTrack >= 0) {
                mediaExtractor.unselectTrack(findTrack);
            }
            if (i23 >= 0) {
                mediaExtractor.unselectTrack(i23);
            }
            return j7;
        }
        return -1L;
    }

    private void checkConversionCanceled() {
        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
        if (videoConvertorListener == null || !videoConvertorListener.checkConversionCanceled()) {
            return;
        }
        throw new ConversionCanceledException();
    }

    private static String createFragmentShader(int i, int i2, int i3, int i4, boolean z) {
        int clamp = (int) Utilities.clamp((Math.max(i, i2) / Math.max(i4, i3)) * 0.8f, 2.0f, 1.0f);
        FileLog.d("source size " + i + "x" + i2 + "    dest size " + i3 + i4 + "   kernelRadius " + clamp);
        if (z) {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + clamp + ".0;\nconst float pixelSizeX = 1.0 / " + i + ".0;\nconst float pixelSizeY = 1.0 / " + i2 + ".0;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        }
        return "precision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + clamp + ".0;\nconst float pixelSizeX = 1.0 / " + i2 + ".0;\nconst float pixelSizeY = 1.0 / " + i + ".0;\nuniform sampler2D sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
    }

    /* loaded from: classes.dex */
    public class ConversionCanceledException extends RuntimeException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ConversionCanceledException() {
            super("canceled conversion");
            MediaCodecVideoConvertor.this = r1;
        }
    }
}
